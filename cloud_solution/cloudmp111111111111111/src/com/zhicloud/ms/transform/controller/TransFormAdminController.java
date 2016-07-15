
package com.zhicloud.ms.transform.controller;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhicloud.ms.app.helper.RegionHelper;
import com.zhicloud.ms.app.helper.RegionHelper.RegionData;
import com.zhicloud.ms.app.propeties.AppProperties;
import com.zhicloud.ms.common.util.json.JSONLibUtil;
import com.zhicloud.ms.constant.AppConstant;
import com.zhicloud.ms.constant.AppInconstant;
import com.zhicloud.ms.httpGateway.HttpGatewayAsyncChannel;
import com.zhicloud.ms.httpGateway.HttpGatewayManager;
import com.zhicloud.ms.httpGateway.HttpGatewayReceiveChannel;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.IDictionaryService;
import com.zhicloud.ms.service.IOperLogService;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.constant.TransformConstant;
import com.zhicloud.ms.transform.mapper.ManSystemUserMapper;
import com.zhicloud.ms.transform.service.ManSysUserService;
import com.zhicloud.ms.transform.util.SendMail;
import com.zhicloud.ms.transform.util.TransFormLoginHelper;
import com.zhicloud.ms.transform.util.TransFormLoginInfo;
import com.zhicloud.ms.transform.util.TransFormPrivilegeUtil;
import com.zhicloud.ms.transform.vo.ManSystemUserVO;
import com.zhicloud.ms.util.NumberUtil;
import com.zhicloud.ms.vo.DictionaryVO;

/**
 * @ClassName: TransFormAdminController
 * @Description: 后台管理控制层
 * @author 张本缘 于 2015年4月23日 上午9:53:09
 */
@Controller
public class TransFormAdminController extends TransFormBaseAction{
    public static final Logger logger = Logger.getLogger(TransFormAdminController.class);
    
    @Resource
    ManSysUserService manSysUserService;
    @Resource
    IDictionaryService dictionaryService;
    @Resource
    private IOperLogService operLogService;

    /**
     * @Description:后台管理登录跳转
     * @param request
     * @param response
     * @return String
     * @throws Exception
     */
    @RequestMapping("/transform/admin")
    public String publicAdmin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("TransFormAdminController.publicAdmin()");
        TransFormLoginInfo loginInfo = TransFormLoginHelper.getLoginInfo(request);
        request.setAttribute("userVO", loginInfo);
        return TransformConstant.transform_jsp_main;
    }
    /**
     * @Description:用户登录
     * @param request
     * @param response
     * @throws ServletException 
     * @throws IOExcept7ion
     */
    @RequestMapping("/transform/login")
    public void login(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String usercount = request.getParameter("usercount");
        String password = request.getParameter("password");
        Map<String, String> condition = new LinkedHashMap<String, String>();
        condition.put("usercount", usercount);
        condition.put("password", password);
        Map<String, Object> map = manSysUserService.login(condition);
        //设置cookie保存密码
        if (TransformConstant.success.equals(map.get("status"))) {
            map.put("url", getFirstRequestURL(request));
        }
        printWriter(response, JSONLibUtil.toJSONString(map));
    }
    
    /**
     * @Description:用户注销操作
     * @param request
     * @param response
     * @throws IOException
     */
    @RequestMapping("/transform/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException{
        try {
            String sessionID = (String) request.getSession().getAttribute(TransformConstant.transform_session_admin);
            if (sessionID == null) {
                printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("注销成功", true)));
            }
            request.getSession().removeAttribute(TransformConstant.transform_session_admin);
            request.getSession().removeAttribute("displayname");
            printWriter(response, JSONLibUtil.toJSONString(manSysUserService.logout(sessionID)));
        } catch (Exception e) {
            e.printStackTrace();
            printWriter(response, JSONLibUtil.toJSONString(e.getMessage()));
        }
    }
    
    /**
     * @Description:跳转登录界面
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/transform/userlogin")
    public String userLogin(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("TransFormAdminController.userLogin()");
        return TransformConstant.transform_jsp_login;
    }
    
    /**
     * @Description:重新登录
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/transform/userloginagain")
    public String userLoginAgain(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("TransFormAdminController.userLoginAgain()");
        try {
            String sessionID = request.getParameter(TransformConstant.transform_session_admin);
            manSysUserService.logout(sessionID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return TransformConstant.transform_jsp_login;
    }
    
    /**
     * @Description:更新用户最新登录时间
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/transform/updateuserlogin")
    public  void updateUserLoginTime(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("TransFormAdminController.userLoginAgain()");
        TransFormLoginInfo info = TransFormLoginHelper.getLoginInfo(request);
         if (info != null) {
             info.setLastupdatetime(System.currentTimeMillis());
        }
    }
    /**
     * 
    * @Title: systemOptionPage 
    * @Description: 查看系统配置
    * @param @param model
    * @param @param request
    * @param @return      
    * @return String     
    * @throws
     */
    @RequestMapping(value="/transform/system/index",method=RequestMethod.GET)
    public String systemOptionPage(Model model,HttpServletRequest request){
    	if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.transform_system_address)){
			return "not_have_access";
		}
    	
    	String systemAddr = AppProperties.getValue("address_of_this_system");
    	String blackAndWhite = AppProperties.getValue("black_white_list");
    	model.addAttribute("systemAddr", systemAddr);
    	model.addAttribute("blackAndWhite", blackAndWhite);
    	List<DictionaryVO> voList = dictionaryService.getValuesByCode("product_name");
    	if(voList!=null&&voList.size()>0){
        	model.addAttribute("name", voList.get(0));
    	}
    	return "/transform/admin/system_address";
    }
    /**
     * 
    * @Title: updateOption 
    * @Description:根性系统配置 
    * @param @param sysAddress
    * @param @param productName
    * @param @param request
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    @RequestMapping(value="/transform/system/add",method=RequestMethod.POST)
    @ResponseBody
    public MethodResult updateOption(@RequestParam("sysAddress") String sysAddress,@RequestParam("blackAndWhite") String blackAndWhite ,DictionaryVO productName,HttpServletRequest request){
    	if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.transform_system_update)){
			return new MethodResult(MethodResult.FAIL,"您没有系统配置的权限，请联系管理员");
		}
    	String oldAddressOfThisSystem = AppProperties.getValue("address_of_this_system");
    	String oldBlackAndWhite = AppProperties.getValue("black_white_list");
    	if(!oldBlackAndWhite.equals(blackAndWhite)){
    		AppProperties.setValue("black_white_list", blackAndWhite);
    	}
    	// 如果系统地址变更，需要重新去gw注册回调地址
    	try{
    	    if(!oldAddressOfThisSystem.equals(sysAddress)){ 
                
                //停止所有线程
                Map<String, HttpGatewayReceiveChannel> map = HttpGatewayManager.getAll().getMap();
                 
                for (Map.Entry<String, HttpGatewayReceiveChannel> entry : map.entrySet()) {  
                    
                    HttpGatewayReceiveChannel val = (HttpGatewayReceiveChannel) entry.getValue();
                    val.setFlag(false); 
                    System.out.println("key = " + entry.getKey() + " and value = " + entry.getValue());  
                } 
                //清除所有的channel
                map.clear();
                
                ServletContextEvent event = new ServletContextEvent(request.getSession().getServletContext());
                Document doc = new SAXReader().read(event.getServletContext().getResource("/META-INF/regions.xml"));
                Element root = doc.getRootElement();
                Iterator<Element> regionIter = root.elementIterator("region");
                while (regionIter.hasNext())
                {
                    Element paramElement = regionIter.next();
                    
                    // 构造region数据
                    RegionData regionData = new RegionData();
                    regionData.setId(Integer.valueOf(paramElement.attributeValue("id")));
                    regionData.setName(paramElement.attributeValue("name"));
                    regionData.setHttpGatewayAddr(paramElement.attributeValue("http_gateway_addr"));
                    RegionHelper.singleton.putRegionData(regionData);
                    
                    // 向http gateway注册消息推送地址 
                    if( sysAddress.endsWith("/")==false )
                    {
                        sysAddress += "/";
                    }
                    String registerUrl = sysAddress + "hgMessage/receive.do";
                    HttpGatewayReceiveChannel receiveChannel = new HttpGatewayReceiveChannel(regionData.getId()); 
                    receiveChannel.messagePushRegisterThreadly(registerUrl); 
                     
                    HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(1);
                    channel.updateCallback(registerUrl);
                }
            }
    	}catch(Exception e){
    	    e.printStackTrace();
    	    operLogService.addLog("系统配置", "更新系统配置失败", "1", "2", request);
    	    return new MethodResult(MethodResult.FAIL,"系统地址注册失败");
    	}
    	// 保存系统地址
        AppProperties.setValue("address_of_this_system", sysAddress);

    	
    	//更新数据库的产品名，更新成功之后更新缓存，如果不成功，返回
    	MethodResult result = dictionaryService.updateValueById(productName);
    	if(result.isSuccess()){
    	    //同步修改缓存的数据，以保证页面正常显示
    		synchronized (AppInconstant.productName){
        		AppInconstant.productName = productName.getValue();
        	}
    	}else{
    		return result;
    	}
    	try {
			AppProperties.save();
		} catch (Exception e) {
	          operLogService.addLog("系统配置", "更新系统配置失败", "1", "2", request);
			return new MethodResult(MethodResult.FAIL, "修改失败");
		}
        operLogService.addLog("系统配置", "更新系统配置成功", "1", "1", request);
    	return new MethodResult(MethodResult.SUCCESS, "修改成功");
    }
    
    /**
     * @Description:跳转找回密码界面
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/transform/findPwdPage")
    public String FindPwdPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("TransFormAdminController.findPwdPage()");
        return TransformConstant.transform_jsp_findpassword;
    }
    
    /**
     * @Description:验证用户名和绑定邮箱
     * @param request
     * @param response
     * @throws ServletException 
     * @throws IOExcept7ion
     */
    @RequestMapping("/transform/cheackuNameAndEmail")
    @ResponseBody
    public void cheackuNameAndEmail(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String usercount = request.getParameter("username");
        String email = request.getParameter("email");
        ManSystemUserVO  ManSystemUserVO = null;
        if(null != usercount && !"".equals(usercount) && null != email && !"".equals(email)){
        	ManSystemUserVO = manSysUserService.getUserByIdAndEamel(usercount,email);
        	//查询到用户信息将验证码code以邮件发送出去
        	if(null != ManSystemUserVO){
        		if(usercount.equals(ManSystemUserVO.getUsercount()) && email.equals(ManSystemUserVO.getEmail())){
        			Map<String, Object> user = new LinkedHashMap<String, Object>();
            		SendMail sMail = new SendMail();
            		user.put("email", ManSystemUserVO.getEmail());
            		String code = NumberUtil.getCode();
            		ManSystemUserVO.setPassword(code);
            		user.put("code", code);//随机生成的4个数字
            		//发送邮件
            		sMail.resetPasswordEmailCode(user);
        		}else{
        			printWriter(response, JSONLibUtil.toJSONString(ManSystemUserVO));
        		}
        	}
        	
            printWriter(response, JSONLibUtil.toJSONString(ManSystemUserVO));
        }else{
        	printWriter(response, JSONLibUtil.toJSONString(ManSystemUserVO));
        }
        
        
    }
    
    /**
     * @Description:跳转重置密码页面
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/transform/toSetPasswordPage")
    public String toSetPasswordPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("TransFormAdminController.toSetPasswordPage()");
        String uid = request.getParameter("uid");
        request.setAttribute("uid", uid);
        return TransformConstant.transform_jsp_tosetpwd;
    }
    
    /**
     * @Description:找回密码
     * @param request
     * @param response
     * @throws ServletException 
     * @throws IOExcept7ion
     */
    @RequestMapping("/transform/findUserPassWord")
    @ResponseBody
    public void findUserPassWord(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String billid = request.getParameter("uid");
        String respassword = request.getParameter("password");
        Map<String, String> condition = new LinkedHashMap<String, String>();
        condition.put("billid", billid);
        condition.put("password", respassword);
        String result = manSysUserService.resPasswordById(billid, respassword);
        printWriter(response, JSONLibUtil.toJSONString(result));
        //return TransformConstant.transform_jsp_findpassword;
    }
}
