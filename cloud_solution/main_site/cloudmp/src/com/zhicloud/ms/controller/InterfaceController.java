/**
 * Project Name:CloudDeskTopMS
 * File Name:InterfaceController.java
 * Package Name:com.zhicloud.ms.controller
 * Date:2015年3月16日上午10:35:12
 * 
 *
*/ 

package com.zhicloud.ms.controller; 

import com.zhicloud.ms.app.cache.box.BoxRealInfoCacheManager;
import com.zhicloud.ms.app.helper.AppHelper;
import com.zhicloud.ms.common.util.HttpUtil;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.*;
import com.zhicloud.ms.util.ServiceUtil;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.*;

/**
 * ClassName: InterfaceController 
 * Function: 提供验证和数据. 
 * Reason: 响应终端请求
 * date: 2015年3月16日 上午10:35:12 
 *
 * @author sasa
 * @version 
 * @since JDK 1.7
 */
@Controller
@RequestMapping("/interface")
public class InterfaceController {
	
	private static final Logger logger = Logger.getLogger(InterfaceController.class);
	
	 
	@Resource
	private IUserService userService;
	@Resource
	private ICloudHostService cloudHostService; 
	@Resource
	private ITerminalUserService terminalUserService; 
	@Resource
	private IVersionRecordService versionRecordService; 
	@Resource
	private IBoxRealInfoService boxRealInfoService;
	@Resource
	IClientMessageService clientMessageService;
	@Resource
	private ITerminalInformationPushService terminalInformationPushService;
	
	/**
	 * 
	 * connect:通知客户端连接信息.  
	 * 只要调用这个方法就表明客户端与服务器连接成功.  
	 *
	 * @author sasa
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws Exception void
	 * @since JDK 1.7
	 */
	@RequestMapping(value="/connect",method=RequestMethod.GET)
	public void connect(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		logger.info("InterfaceController.connect() > success ");
      response.setCharacterEncoding("utf-8");
		response.setContentType("text/json; charset=utf-8");
		ServiceUtil.writeSuccessMessage(response.getOutputStream(), "success");
	}
	/**
	 * 
	 * login:终端用户登录验证.  
	 * 若登录成功，直接返回主机列表.  
	 *
	 * @author sasa
	 * @param request
	 * @param response void
	 * @throws IOException 
	 * @since JDK 1.7
	 */
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public void login(HttpServletRequest request, HttpServletResponse response) throws IOException
	{ 
	    try{
	        response.setCharacterEncoding("utf-8");
	        response.setContentType("text/json; charset=utf-8"); 
	        logger.info(request.getLocalAddr());
	        logger.info(request.getHeader("X-Forwarded-For"));
	        boolean ipInnerFlag = HttpUtil.isIpAddr(request);
	        //获取用户名
	        String username = StringUtil.trim(request.getParameter("user_name"));
	        //获取密码
	        String password = StringUtil.trim(request.getParameter("password"));
	        //获取mac地址
	        String mac = StringUtil.trim(request.getParameter("mac"));
	        String ip = StringUtil.trim(request.getParameter("ip"));
	        String subnetMask = StringUtil.trim(request.getParameter("subnet_mask"));
	        String softwareVersion = StringUtil.trim(request.getParameter("software_version"));
	        String hardwareVersion = StringUtil.trim(request.getParameter("hardware_version"));
	        String gateway = StringUtil.trim(request.getParameter("gateway"));
	        String time = StringUtil.trim(request.getParameter("time"));
	        if(StringUtil.isBlank(time)){
	            time = StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS");
	        }
	        if(StringUtil.isBlank(username)){ 
	            ServiceUtil.writeFailMessage(response.getOutputStream(), "user_name is required"); 
	        }  
	        if(StringUtil.isBlank(password)){ 
	            ServiceUtil.writeFailMessage(response.getOutputStream(), "password is required"); 
	        }  
	        if(userService.checkAvailable(username) && userService.checkAlias(username)){           
	            ServiceUtil.writeFailMessage(response.getOutputStream(), "user_name is not exsit"); 
	        }
	        //查询用户信息
	        SysUser user = userService.checkTerminalLogin(request, response);
	        
	        if(user != null){
	            TerminalUserVO terminalUser = terminalUserService.queryById(user.getId());
	            if(terminalUser == null){
	                ServiceUtil.writeFailMessage(response.getOutputStream(), "user_name is not exsit"); 
	                return ;
	            }
	            //查询主机信息        
	            List<Map<Object, Object>> hostList= cloudHostService.queryCloudHostForTerminal(user,ipInnerFlag);
	            
	            // 行业和地区
	            String region = StringUtil.trim(request.getParameter("region"));
	            String industry = StringUtil.trim(request.getParameter("industry"));
	            List<TerminalInformationPushVO> infoList = terminalInformationPushService.queryInfomationByCondition(
	                    user.getGroupId(), time, region, industry);
	            Map<Object, Object> result = ServiceUtil.toSuccessObject("");
	            List<Map<Object, Object>> infoDatas = new ArrayList<Map<Object, Object>>();
	            for(TerminalInformationPushVO info : infoList){
	                Map<Object, Object> infoData = new LinkedHashMap<Object, Object>();
	                infoData.put("title", info.getTitle());
	                infoData.put("content", info.getContent());
	                infoData.put("time", info.getCreateTime());
	                infoDatas.add(infoData);
	            }
	            result.put("hosts", hostList);  
	            result.put("messages", infoDatas);
	            result.put("name", terminalUser.getUsername());
	            //默认不开通usb 
	            result.put("user_status", terminalUser.getStatus());    
	            result.put("usb", terminalUser.getUsbStatus());      
	            BoxRealInfoVO info = boxRealInfoService.getInfoByUserId(mac);
	            if(info == null){
	                info = new BoxRealInfoVO();
	                info.setMac(mac);
	            }
	            info.setUserId(user.getId());
	            info.setUserName(username);
	            info.setGateway(gateway);
	            info.setHardwareVersion(hardwareVersion);
	            info.setSoftwareVersion(softwareVersion);
	            info.setSubnetMask(subnetMask);
	            info.setLastAliveTime(StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
	            info.setLastLoginTime(StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
	            info.setIp(ip);
//	            boxRealInfoService.addOrUpdateBoxInfo(info);
	            BoxRealInfoCacheManager.singleton().getCache().put(info);
 

	        // 写反回流
	            ServiceUtil.writeJsonTo(response.getOutputStream(), result);
	            
	        }else{  
	            //写返回流
	            ServiceUtil.writeFailMessage(response.getOutputStream(), "not found user information"); 
	        } 
	        
	    }catch(Exception e){
	        e.printStackTrace();
	    }
      
		

	}
	/**
	 * 
	 * hostStatus:查询主机信息.  
	 * 需传入主机id 
	 *
	 * @author sasa
	 * @param request
	 * @param response
	 * @throws IOException void
	 * @since JDK 1.7
	 */
	@RequestMapping(value="/hoststatus",method=RequestMethod.GET)
	public void hostStatus(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
      response.setCharacterEncoding("utf-8");
		response.setContentType("text/json; charset=utf-8"); 		
		//主机id
		String cloudHostId = StringUtil.trim(request.getParameter("cloud_host_id"));
		 
		if(StringUtil.isBlank(cloudHostId)){
			//参数有误
			ServiceUtil.writeFailMessage(response.getOutputStream(), "cloud_host_id is required"); 
		}
		 
		else{ 
			//查询信息		
			CloudHostVO host = cloudHostService.queryCloudHostById(cloudHostId);
			if(host == null){
				//未查询到主机信息
				ServiceUtil.writeFailMessage(response.getOutputStream(), "not find host information"); 
			}else{				
				Map<Object, Object> result = ServiceUtil.toSuccessObject("");
				result.put("host_status", host.getRunningStatus());	
				ServiceUtil.writeJsonTo(response.getOutputStream(), result);
			}
		}   
		

	}
	/**
	 * 
	 * changePassword:用户通过终端修改密码.  
	 *  需要传用户名、当前密码和新密码
	 *
	 * @author sasa
	 * @param request
	 * @param response
	 * @throws IOException void
	 * @since JDK 1.7
	 */
	@RequestMapping(value="/changepassword" ,method=RequestMethod.GET)
	public void changePassword(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
      response.setCharacterEncoding("utf-8");
		response.setContentType("text/json; charset=utf-8"); 		
		//获取用户名
		String userName = StringUtil.trim(request.getParameter("user_name"));
		//获取旧密码
		String oldPassword = StringUtil.trim(request.getParameter("password"));
		//获取新密码
		String newPassword = StringUtil.trim(request.getParameter("new_password"));
		 
		if(StringUtil.isBlank(userName)){
			//用户名为空
			ServiceUtil.writeFailMessage(response.getOutputStream(), "user_name is required"); 
		}
		if(StringUtil.isBlank(oldPassword)){
			//旧密码为空
			ServiceUtil.writeFailMessage(response.getOutputStream(), "password is required"); 
		}
		if(StringUtil.isBlank(newPassword)){
			//新密码为空
			ServiceUtil.writeFailMessage(response.getOutputStream(), "new_password is required"); 
		}
		//更新密码
		else{ 
			MethodResult resultInfo = userService.changePassword(request, response);
			if(resultInfo != null){
				if(resultInfo.isSuccess()){
					ServiceUtil.writeSuccessMessage(response.getOutputStream(), "success");
				}else{
					Map<Object, Object> result = ServiceUtil.toFailObject(resultInfo.message);
	 				ServiceUtil.writeJsonTo(response.getOutputStream(), result);
				}
			}
			 
		}
		

	}
	/**
	 * 
	 * hostOperator:终端用户操作云主机开机、关机、重启和强制关机.  
	 *  
	 *
	 * @author sasa
	 * @param request
	 * @param response
	 * @throws IOException void
	 * @since JDK 1.7
	 */
	@RequestMapping(value="/hostoperator",method=RequestMethod.GET)
	public void hostOperator(HttpServletRequest request, HttpServletResponse response) throws IOException
	{

      response.setCharacterEncoding("utf-8");
		response.setContentType("text/json; charset=utf-8"); 		
		//主机Id
		String cloudHostId = StringUtil.trim(request.getParameter("cloud_host_id"));
		//操作类型
		String operatorType = StringUtil.trim(request.getParameter("operator_type"));
		if(StringUtil.isBlank(cloudHostId)){
			//参数有误
			ServiceUtil.writeFailMessage(response.getOutputStream(), "cloud_host_id is required"); 
		}
		if(StringUtil.isBlank(operatorType)){
			//参数有误
			ServiceUtil.writeFailMessage(response.getOutputStream(), "operator_type is required"); 
		}
		
		MethodResult resultInfo = cloudHostService.operatorCloudHost(cloudHostId, operatorType,false,0);
		if(resultInfo != null){
			if(resultInfo.isSuccess()){
				ServiceUtil.writeSuccessMessage(response.getOutputStream(), "success");
			}else{
				Map<Object, Object> result = ServiceUtil.toFailObject(resultInfo.message);
 				ServiceUtil.writeJsonTo(response.getOutputStream(), result);
			}
		}
	    
		 
		 
	}
	/**
	 * 
	 * version:获取终端最新版本号
	 *
	 * @author sasa
	 * @param request
	 * @param response
	 * @throws IOException void
	 * @since JDK 1.7
	 */
	@RequestMapping(value="/version",method=RequestMethod.GET)
	public void version(HttpServletRequest request, HttpServletResponse response) throws IOException
	{ 
		logger.info("InterfaceController.connect() > success ");

      response.setCharacterEncoding("utf-8");
		response.setContentType("text/json; charset=utf-8");
		Map<Object, Object> result = ServiceUtil.toSuccessObject("");
		//获取版本号
		String versionNumber = StringUtil.trim(request.getParameter("version_number"));	
		//平台类型
		String platformType = StringUtil.trim(request.getParameter("platform_type")); 
		//操作系统类型
		String osType = StringUtil.trim(request.getParameter("os_type")); 
		//硬件产商类型
		String hardwareCompany = StringUtil.trim(request.getParameter("hardware_company"));  
		if(StringUtil.isBlank(versionNumber)){
			//参数有误
			ServiceUtil.writeFailMessage(response.getOutputStream(), "version_number is required"); 
		}
		//设置默认值
		if(StringUtil.isBlank(platformType)){
		    platformType = "ARM";
		}
		if(StringUtil.isBlank(osType)){
		    osType = "ubuntu";
        }
		if(StringUtil.isBlank(hardwareCompany)){
		    hardwareCompany = "XH";
        }
		VersionRecordVO version = versionRecordService.getLatestVersion(platformType,hardwareCompany);
		if(version == null || version.getVersionNumber().equals(versionNumber)){
 			result.put("need_update", false);	
			ServiceUtil.writeJsonTo(response.getOutputStream(), result);
		}else if(version != null && this.digitFromString(version.getVersionNumber()).compareTo(this.digitFromString(versionNumber))<=0){
		    result.put("need_update", false); 
            ServiceUtil.writeJsonTo(response.getOutputStream(), result);
		}
		else{
			result.put("need_update", true);	
			result.put("download_id", version.getId());	
			result.put("update_info", version.getUpdateInfo());	
			result.put("latest_version_number", version.getVersionNumber());	
			ServiceUtil.writeJsonTo(response.getOutputStream(), result);			
		} 	 
	}
	/**
	 * 
	* @Title: download 
	* @Description: 下载客户端版本 
	* @param @param request
	* @param @param response
	* @param @throws IOException      
	* @return void     
	* @throws
	 */
	@RequestMapping(value="/download",method=RequestMethod.GET)
	public void download(HttpServletRequest request, HttpServletResponse response) throws IOException
	{ 
		logger.info("InterfaceController.connect() > success ");


      response.setCharacterEncoding("utf-8");
		response.setContentType("text/json; charset=utf-8"); 
		//获取版本ID
		String downloadId = StringUtil.trim(request.getParameter("download_id"));	
		if(StringUtil.isBlank(downloadId)){
			//参数有误
			ServiceUtil.writeFailMessage(response.getOutputStream(), "download_id is required"); 
		}
		VersionRecordVO version = versionRecordService.getVersionById(downloadId);
		if(version == null ){
			ServiceUtil.writeFailMessage(response.getOutputStream(), "version is not　find"); 
		}
		//定义下载路径
		String filePath = AppHelper.getServerHome()+"/projects/"+AppHelper.APP_NAME+"/user_upload/"+version.getId()+"/"+version.getName();
		
		File file = new File(filePath);
         if (!file.exists()) {
        	 ServiceUtil.writeFailMessage(response.getOutputStream(), "version is not exsit");  
         } 
		
		InputStream in = new FileInputStream(file);
		OutputStream os = response.getOutputStream();
		response.addHeader("Content-Disposition", "attachment;filename="
				+ new String(file.getName().getBytes("utf-8"), "iso8859-1"));
		response.addHeader("Content-Length", file.length() + "");
		response.setCharacterEncoding("utf-8");
		response.setContentType("application/octet-stream");
		int data = 0;
		while ((data = in.read()) != -1) {
			os.write(data);
		}
		os.close();
		in.close(); 	 
	}

	@RequestMapping(value="/updatehoststatus",method=RequestMethod.GET)
    public void updateHostStatus(HttpServletRequest request, HttpServletResponse response) throws IOException
    { 
        logger.info("InterfaceController.connect() > success ");

        response.setCharacterEncoding("utf-8");
        response.setContentType("text/json; charset=utf-8");
         //获取主机Id
        String hostId = StringUtil.trim(request.getParameter("host_id")); 
        //获取主机运行状态
        String runningStatus = StringUtil.trim(request.getParameter("running_status")); 

        if(StringUtil.isBlank(hostId)){
            //参数有误
            ServiceUtil.writeFailMessage(response.getOutputStream(), "host_id is required"); 
        }
        if(StringUtil.isBlank(runningStatus)){
            //参数有误
            ServiceUtil.writeFailMessage(response.getOutputStream(), "running_status is required"); 
        }
        MethodResult result =  cloudHostService.updateRunningStatusFromTerminal(hostId, runningStatus);
        
        ServiceUtil.writeJsonTo(response.getOutputStream(), result);
    }
	/**
	 * 
	* @Title: keepAlive 
	* @Description: 心跳检测
	* @param @param request
	* @param @param response
	* @param @throws IOException      
	* @return void     
	* @throws
	 */
	@RequestMapping(value="/keepalive",method=RequestMethod.GET)
    public void keepAlive(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/json; charset=utf-8"); 
        logger.info(request.getLocalAddr());
        logger.info(request.getHeader("X-Forwarded-For"));
        boolean ipInnerFlag = HttpUtil.isIpAddr(request);
        //获取用户名
        String username = StringUtil.trim(request.getParameter("user_name"));
        String time = StringUtil.trim(request.getParameter("time"));
        SysUser sysUser = userService.getUserInfoByName(username);

        if(sysUser == null){
            ServiceUtil.writeFailMessage(response.getOutputStream(), "success");
            return;
        } 
        
        BoxRealInfoVO info = boxRealInfoService.getInfoByUserId(sysUser.getId());
        if(info == null){
            ServiceUtil.writeFailMessage(response.getOutputStream(), "fail");
            return;
        } 
        info.setLastAliveTime(StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
//        boxRealInfoService.addOrUpdateBoxInfo(info);
        BoxRealInfoCacheManager.singleton().getCache().put(info);

        //行业和地区
        String region = StringUtil.trim(request.getParameter("region"));
        String industry = StringUtil.trim(request.getParameter("industry"));
        List<TerminalInformationPushVO> infoList = terminalInformationPushService.queryInfomationByCondition(
                sysUser.getGroupId(), time, region, industry);
        Map<Object, Object> result = ServiceUtil.toSuccessObject("success");

        List<Map<Object, Object>> infoDatas = new ArrayList<Map<Object, Object>>();
        for(TerminalInformationPushVO minfo : infoList){
            Map<Object, Object> infoData = new LinkedHashMap<Object, Object>();
            infoData.put("title", minfo.getTitle());
            infoData.put("content", minfo.getContent());
            infoData.put("time", minfo.getCreateTime());
            infoDatas.add(infoData);
        }
         result.put("messages", infoDatas);
        
        // 写反回流
        ServiceUtil.writeJsonTo(response.getOutputStream(), result);
        

    }
	@RequestMapping(value="/logout",method=RequestMethod.GET)
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException
    {

        response.setCharacterEncoding("utf-8"); 
        response.setContentType("text/json; charset=utf-8"); 
        logger.info(request.getLocalAddr());
        logger.info(request.getHeader("X-Forwarded-For"));
        boolean ipInnerFlag = HttpUtil.isIpAddr(request);
        //获取用户名
        String username = StringUtil.trim(request.getParameter("user_name"));
        SysUser sysUser = userService.getUserInfoByName(username);

        if(sysUser == null){
            ServiceUtil.writeFailMessage(response.getOutputStream(), "fail");
            return;
        } 
        BoxRealInfoVO info = boxRealInfoService.getInfoByUserId(sysUser.getId());
        if(info == null){
            ServiceUtil.writeFailMessage(response.getOutputStream(), "fail");
            return;
        } 
        info.setLastLogoutTime(StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
//        boxRealInfoService.addOrUpdateBoxInfo(info);
        BoxRealInfoCacheManager.singleton().getCache().put(info);


        // 写反回流
        ServiceUtil.writeFailMessage(response.getOutputStream(), "success"); 
        

    }
	
	@RequestMapping(value="/modifyalias" ,method=RequestMethod.GET)
	public void modifyAlias(HttpServletRequest request, HttpServletResponse response) throws IOException
	{


      response.setCharacterEncoding("utf-8");
		response.setContentType("text/json; charset=utf-8"); 		
		//获取用户名
		String userName = StringUtil.trim(request.getParameter("user_name"));
		//获取旧密码
		String oldPassword = StringUtil.trim(request.getParameter("password"));
		//获取新别名
		String alias = StringUtil.trim(request.getParameter("alias"));
		 
		if(StringUtil.isBlank(userName)){
			//用户名为空
			ServiceUtil.writeFailMessage(response.getOutputStream(), "user_name is required"); 
		}
		if(StringUtil.isBlank(oldPassword)){
			//旧密码为空
			ServiceUtil.writeFailMessage(response.getOutputStream(), "password is required"); 
		}
		if(StringUtil.isBlank(alias)){
			//新别名为空
			ServiceUtil.writeFailMessage(response.getOutputStream(), "alias is required"); 
		}
		//更新别名
		else{ 
			MethodResult resultInfo = userService.modifyAlias(request, response);
			if(resultInfo != null){
				if(resultInfo.isSuccess()){
					ServiceUtil.writeSuccessMessage(response.getOutputStream(), "success");
				}else{
					Map<Object, Object> result = ServiceUtil.toFailObject(resultInfo.message);
	 				ServiceUtil.writeJsonTo(response.getOutputStream(), result);
				}
			}
			 
		}
		

	} 

	@RequestMapping(value="/msgfeedback" ,method=RequestMethod.GET)
	public void msgFeedback(HttpServletRequest request, HttpServletResponse response) throws IOException
	{

      response.setCharacterEncoding("utf-8");
		response.setContentType("text/json; charset=utf-8"); 		
		//获取用户名
		String userName = StringUtil.trim(request.getParameter("user_name"));
		//获取旧密码
		String password = StringUtil.trim(request.getParameter("password"));
		//获取反馈内容
		String content = StringUtil.trim(request.getParameter("content"));
		//获取反馈类型
		String type = StringUtil.trim(request.getParameter("type"));
		 
		if(StringUtil.isBlank(userName)){
			//用户名为空
			ServiceUtil.writeFailMessage(response.getOutputStream(), "user_name is required"); 
		}
		if(StringUtil.isBlank(password)){
			//旧密码为空
			ServiceUtil.writeFailMessage(response.getOutputStream(), "password is required"); 
		}
		if(StringUtil.isBlank(content)){
			//内容为空
			ServiceUtil.writeFailMessage(response.getOutputStream(), "content is required"); 
		}
		if(StringUtil.isBlank(type)){
			//类型
			ServiceUtil.writeFailMessage(response.getOutputStream(), "type is required"); 
		}
		//插入反馈信息
		else{ 
			MethodResult resultInfo = clientMessageService.add(request, response);
			if(resultInfo != null){
				if(resultInfo.isSuccess()){
					ServiceUtil.writeSuccessMessage(response.getOutputStream(), "success");
				}else{
					Map<Object, Object> result = ServiceUtil.toFailObject(resultInfo.message);
	 				ServiceUtil.writeJsonTo(response.getOutputStream(), result);
				}
			}
			 
		}
		

	} 
	
	public static String digitFromString(String str) {
 	    str=str.trim();
	    String str2="";
	    if(str != null && !"".equals(str)){
    	    for(int i=0;i<str.length();i++){
    	    if(str.charAt(i)>=48 && str.charAt(i)<=57){
    	    str2+=str.charAt(i);
    	    }
    	  }
	   }
	    return str2;
	}
}

