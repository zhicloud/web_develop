/**
 * Project Name:CloudDeskTopMS
 * File Name:VersionRecordController.java
 * Package Name:com.zhicloud.ms.controller
 * Date:2015年4月21日下午5:59:21
 * 
 *
*/ 

package com.zhicloud.ms.controller;

import java.io.File; 
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping; 
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.zhicloud.ms.app.helper.AppHelper;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.IOperLogService;
 import com.zhicloud.ms.service.IVersionRecordService;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.util.TransFormPrivilegeUtil;
import com.zhicloud.ms.util.StringUtil; 
import com.zhicloud.ms.vo.VersionRecordVO;
/**
 * ClassName: VersionRecordController 
 * Function:  版本管理
 * date: 2015年4月21日 下午5:59:21 
 *
 * @author sasa
 * @version 
 * @since JDK 1.7
 */
@Controller
@RequestMapping("/version")
public class VersionRecordController {
	@Resource
	private IVersionRecordService versionRecordService;
	@Resource
    private IOperLogService operLogService;
	/**
	 * 
	* @Title: list 
	* @Description: 查询版本列表
	* @param @param model
	* @param @param request
	* @param @return      
	* @return String     
	* @throws
	 */
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String list(Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_terminal_version_query)){
			return "not_have_access";
		}
		List<VersionRecordVO> versionList = versionRecordService.getAllVersion();
		model.addAttribute("versionList", versionList); 
		return "version_record_manage";
	}
	/**
	 * 
	* @Title: list 
	* @Description: 新增版本页面
	* @param @param request
	* @param @param response
	* @param @return      
	* @return String     
	* @throws
	 */
	@RequestMapping(value="/addpage",method=RequestMethod.GET)
	public String list(HttpServletRequest request, HttpServletResponse response){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_terminal_version_add)){
			return "not_have_access";
		}
  		return "version_record_add";
	}
	/**
	 * 
	* @Title: deleteVersion 
	* @Description: 版本删除
	* @param @param id
	* @param @param request
	* @param @return      
	* @return MethodResult     
	* @throws
	 */
	@RequestMapping(value="/{id}/delete",method=RequestMethod.GET)
	@ResponseBody
	public MethodResult deleteVersion(@PathVariable("id") String id,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_terminal_version_delete)){
			return new MethodResult(MethodResult.FAIL,"您没有删除版本的权限，请联系管理员");
		}
		MethodResult result = versionRecordService.deleteVersion(id); 
		if(result.isSuccess()){
			//定义路径
			String [] versionIds = id.split(",");
			for(String versionId : versionIds){
				
				String filePath = AppHelper.getServerHome()+"/projects/"+AppHelper.APP_NAME+"/user_upload/"+versionId;
				
				//删除所有的文件
				this.delAllFile(filePath);
			}
		}
		operLogService.addLog("固件版本", "删除固件版本成功", "1", "1", request);
		return result;
	}
	/**
	 * 
	* @Title: addImage 
	* @Description: 新增版本方法 
	* @param @param version
	* @param @param request
	* @param @param response
	* @param @return      
	* @return MethodResult     
	* @throws
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult addImage(VersionRecordVO version,HttpServletRequest request, HttpServletResponse response){
 		try {
 			if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_terminal_version_add)){
 				return new MethodResult(MethodResult.FAIL,"您没有新增版本的权限，请联系管理员");
 			}
 			version.setId(StringUtil.generateUUID());
 			MultipartHttpServletRequest  multipartRequest = (MultipartHttpServletRequest) request;
			
			MultipartFile attach = multipartRequest.getFile("filename");
			
			version.setFsize(attach.getSize());
			
			//获取文件名
			String fileName = new String(attach.getOriginalFilename().getBytes("ISO-8859-1"), "UTF-8");
			version.setName(fileName);
			
			//定义上传路径
			String filePath = AppHelper.getServerHome()+"/projects/"+AppHelper.APP_NAME+"/user_upload/"+version.getId();
			
			//若无该文件夹自动创建
			File fp = new File(filePath);
			
			if(!fp.exists()){
				fp.mkdirs();
			}
			
			File file = new File(filePath+"/"+fileName);
			
			// 上传文件
			FileUtils.copyInputStreamToFile(attach.getInputStream(), file);	
			
			
			
			MethodResult result = versionRecordService.addVersion(version);
			if(result.isSuccess()){
	            operLogService.addLog("固件版本", "上传固件版本"+fileName+"成功", "1", "1", request);
	        }else{
	            operLogService.addLog("固件版本", "上传固件版本"+fileName+"失败", "1", "2", request);
	        }
			return result;
		} catch (Exception e) { 
			e.printStackTrace();
            operLogService.addLog("固件版本", "上传固件版本"+version.getName()+"失败", "1", "2", request);
			return new MethodResult(MethodResult.FAIL,"添加失败");			
			
		}  
	} 
	
	/**
	 * 
	* @Title: delAllFile 
	* @Description: 根据路径删除文件
	* @param @param path
	* @param @return      
	* @return boolean     
	* @throws
	 */
	private boolean delAllFile(String path) {
	       boolean flag = false;
	       File file = new File(path);
	       if (!file.exists()) {
	         return flag;
	       }
	       if (!file.isDirectory()) {
	         return flag;
	       }
	       String[] tempList = file.list();
	       File temp = null;
	       for (int i = 0; i < tempList.length; i++) {
	          if (path.endsWith(File.separator)) {
	             temp = new File(path + tempList[i]);
	          } else {
	              temp = new File(path + File.separator + tempList[i]);
	          }
	          if (temp.isFile()) {
	             temp.delete();
	          }
	          if (temp.isDirectory()) {
	             delAllFile(path + "/" + tempList[i]);//先删除文件夹里面的文件
	             delFolder(path + "/" + tempList[i]);//再删除空文件夹
	             flag = true;
	          }
	       }
	       return flag;
     }
	/**
	 * 
	* @Title: delFolder 
	* @Description: 删除文件夹 
	* @param @param folderPath      
	* @return void     
	* @throws
	 */
	private  void delFolder(String folderPath) {
	     try {
	        delAllFile(folderPath); //删除完里面所有内容
	        String filePath = folderPath;
	        filePath = filePath.toString();
	        java.io.File myFilePath = new java.io.File(filePath);
	        myFilePath.delete(); //删除空文件夹
	     } catch (Exception e) {
	       e.printStackTrace(); 
	     }
	}

}

