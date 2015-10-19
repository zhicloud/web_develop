package com.zhicloud.ms.controller;

import com.zhicloud.ms.app.helper.AppHelper;
import com.zhicloud.ms.constant.AppConstant;
import com.zhicloud.ms.exception.AppException;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.*;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.util.TransFormPrivilegeUtil;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.*;
import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
	
	@Resource
	private IUserService userService;
	
	@Resource
	private ITerminalUserService terminalUserService;
	
	@Resource
	private ISysGroupService sysGroupService;
	
	@Resource
	ICloudHostWarehouseService cloudHostWarehouseService;
	
	@Resource
	ICloudHostService cloudHostService;
	
	@Resource
    private ITerminalBoxService terminalBoxService;
	
	@Resource
    private IOperLogService operLogService;
	
	@RequestMapping(value="/login",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult isLogin(@RequestParam("username") String username,
			@RequestParam("password") String password,
			HttpServletRequest request){ 
		if(userService.isLogin(username, password,request)){
			return new MethodResult(MethodResult.SUCCESS,"登录成功");		
		}else{
			return new MethodResult(MethodResult.FAIL,"用户名或者密码错误");		
		}
 	}
	
	@RequestMapping(value="/login",method=RequestMethod.GET)
	public String loginPage(){
		return "index";
	}
	
	@RequestMapping(value="/logout",method=RequestMethod.GET)
	@ResponseBody
	public MethodResult logout(HttpServletRequest request){
		MethodResult mr = new MethodResult(MethodResult.SUCCESS,"用户已注销");
		userService.logout(request);
		return mr;
	}
	
	@RequestMapping(value="/logout_admin",method=RequestMethod.GET)
	public String logoutAdmin(HttpServletRequest request){
		userService.logout(request);
		return "redirect:/user/login";
	}
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	public String list(Model model,HttpServletRequest request) throws UnsupportedEncodingException {
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_user_query)){
			return "not_have_access";
		}

      String param = request.getParameter("param");

      String statusStr = request.getParameter("status");
      String usbStatusStr = request.getParameter("usb_status");



      Integer status = null;
      Integer usbStatus = null;

      if(StringUtil.isBlank(param)){
          param = null;
      }else{
          param = new String(request.getParameter("param").getBytes("ISO-8859-1"),"UTF-8");
          model.addAttribute("parameter", param);
          param = "%"+param+"%";
      }

      if(StringUtil.isBlank(statusStr)){
          status = null;
      } else{
          status = Integer.valueOf(statusStr);
      }

      if(StringUtil.isBlank(usbStatusStr)){
          usbStatus = null;
      } else{
          usbStatus = Integer.valueOf(usbStatusStr);
      }
      model.addAttribute("status", status);
      model.addAttribute("usb_status", usbStatus);



      Map<String, Object> condition = new LinkedHashMap<String, Object>();
      condition.put("param", param);
      condition.put("status", status);
      condition.put("usb_status", usbStatus);


      List<TerminalUserVO> terminalUserList = terminalUserService.queryAll(condition);
		model.addAttribute("terminal_user_list", terminalUserList);
		List<CloudHostWarehouse> warelist = cloudHostWarehouseService.getAll();
		model.addAttribute("warelist", warelist);
		return "terminal_user_manage";
	}
	@RequestMapping(value="/addpage",method=RequestMethod.GET)
	public String addPage(Model model,HttpServletRequest request){ 
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_user_add)){
			return "not_have_access";
		}
		List<SysGroupVO> sysGroupList = sysGroupService.queryAll();
		model.addAttribute("sys_group_list", sysGroupList);
		return "terminal_user_add";
	}
	
	@RequestMapping(value="/warehouse/list",method=RequestMethod.GET)
	@ResponseBody
	public MethodResult getUserList(){
		List<TerminalUserVO> terminalUserList = terminalUserService.queryAll();
		MethodResult result = new MethodResult(MethodResult.SUCCESS,""); 
		result.put("terminalUserList", terminalUserList);
		return result;
	}
	
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult addTerminalUser(TerminalUserVO terminalUserVO,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_user_add)){
			return new MethodResult(MethodResult.FAIL,"您没有新增用户的权限，请联系管理员");
		}
		Map<String, Object> data = new LinkedHashMap<String, Object>();
		data.put("username", terminalUserVO.getUsername());
//		data.put("alias", terminalUserVO.getAlias());
		data.put("name", terminalUserVO.getName());	
//		data.put("password", terminalUserVO.getPassword());
      data.put("region", terminalUserVO.getRegion());
      data.put("industry", terminalUserVO.getIndustry());
      data.put("group_id", terminalUserVO.getGroupId());
		data.put("email", terminalUserVO.getEmail());
		data.put("phone", terminalUserVO.getPhone());
		data.put("usb_status", terminalUserVO.getUsbStatus());
		data.put("status", terminalUserVO.getStatus());
      MethodResult result = terminalUserService.addTerminalUser(data);
		if(MethodResult.SUCCESS.equals(result.status)) {
            operLogService.addLog("终端用户", "新增终端用户"+terminalUserVO.getUsername()+"成功", "1", "1", request);
			return new MethodResult(result.status, result.message);
		} 
        operLogService.addLog("终端用户", "新增终端用户" + terminalUserVO.getUsername() + "失败", "1", "2",
            request);
      return new MethodResult(result.status, result.message);
	}
	
	@RequestMapping(value="/{id}/update",method=RequestMethod.GET)
 	public String updateTerminalUserPage(@PathVariable("id") String id,Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_user_modify)){
			return "not_have_access";
		}
		TerminalUserVO user = terminalUserService.queryById(id);
		model.addAttribute("user", user);
		List<SysGroupVO> sysGroupList = sysGroupService.queryAll();
		model.addAttribute("sys_group_list", sysGroupList);
		return "terminal_user_mod";
	}
	
	@RequestMapping(value="/{id}/update",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult updateTerminalUser(@PathVariable("id") String id, TerminalUserVO terminalUserVO,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_user_modify)){
			return new MethodResult(MethodResult.FAIL,"您没有修改用户的权限，请联系管理员");
		}
		Map<String, Object> data = new LinkedHashMap<String, Object>();
		data.put("id", id);
		data.put("name", terminalUserVO.getName());
		data.put("group_id", terminalUserVO.getGroupId());
		data.put("email", terminalUserVO.getEmail());
		data.put("phone", terminalUserVO.getPhone());
//		data.put("alias", terminalUserVO.getAlias());
      data.put("region", terminalUserVO.getRegion());
      data.put("industry", terminalUserVO.getIndustry());
		if(MethodResult.SUCCESS.equals(terminalUserService.updateTerminalUserById(data).status)) {
            operLogService.addLog("终端用户", "修改终端用户"+terminalUserVO.getUsername()+"成功", "1", "1", request);
			return new MethodResult(MethodResult.SUCCESS, "更新成功");
		}
        operLogService.addLog("终端用户", "修改终端用户" + terminalUserVO.getUsername() + "成功", "1", "2",
            request);
		return new MethodResult(MethodResult.FAIL, "更新失败");
	}
	
	@RequestMapping(value="/change/usb",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult updateUSBStatus(String ids, String usbStatus,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_user_modify_usb)){
			return new MethodResult(MethodResult.FAIL,"您没有修改USB的权限，请联系管理员");
		}
		String[] idsArr = ids.split(",");
		int usbStatusInt = Integer.parseInt(usbStatus);
		int result = 0;
		
		
		for(int i = 0; i < idsArr.length; i++){
			Map<String, Object> data = new LinkedHashMap<String, Object>();
			data.put("id", idsArr[i]);
			data.put("usb_status", usbStatusInt);
			if(MethodResult.SUCCESS.equals(terminalUserService.updateUSBStatusById(data).status)) {
				result ++;
			}
		}
		
		if(result == idsArr.length) {
            operLogService.addLog("终端用户", "修改终端用户USB权限成功", "1", "1", request);
			return new MethodResult(MethodResult.SUCCESS, "更新成功");
		}
		operLogService.addLog("终端用户", "修改终端用户USB权限失败", "1", "2", request);
		return new MethodResult(MethodResult.FAIL, "更新失败");
	}
	
	@RequestMapping(value="/change/status",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult updateStatus(String ids, String status,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_user_modify_status)){
			return new MethodResult(MethodResult.FAIL,"您没有修改用户状态的权限，请联系管理员");
		}
		String[] idsArr = ids.split(",");
		int statusInt = Integer.parseInt(status);
		int result = 0;
		
		
		for(int i = 0; i < idsArr.length; i++){
			Map<String, Object> data = new LinkedHashMap<String, Object>();
			data.put("id", idsArr[i]);
			data.put("status", statusInt);
			if(MethodResult.SUCCESS.equals(terminalUserService.updateStatusById(data).status)) {
				result ++;
			}
		}
		
		if(result == idsArr.length) {
            operLogService.addLog("终端用户", "修改终端用户状态成功", "1", "1", request);
			return new MethodResult(MethodResult.SUCCESS, "更新成功");
		}
        operLogService.addLog("终端用户", "修改终端用户状态失败", "1", "2", request);
		return new MethodResult(MethodResult.FAIL, "更新失败");
	}
	
	@RequestMapping(value="/password/reset",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult resetPassword(String ids, String usernames, String password,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_user_reset_password)){
			return new MethodResult(MethodResult.FAIL,"您没有重置用户密码的权限，请联系管理员");
		}
		System.out.println("controller "+ids);
		System.out.println(usernames);
		System.out.println(password);
		String[] idsArr = ids.split(",");
		String[] usernamesArr = usernames.split(",");
		int result = 0;
		
		
		for(int i = 0; i < idsArr.length; i++){
			Map<String, Object> data = new LinkedHashMap<String, Object>();
			data.put("id", idsArr[i]);
			data.put("username", usernamesArr[i]);
			data.put("password", password);
			if(MethodResult.SUCCESS.equals(userService.changePassword(data).status)) {
				result ++;
			}
		}
		
		if(result == idsArr.length) {
            operLogService.addLog("终端用户", "重置终端用户"+usernamesArr+"密码成功", "1", "1", request);
			return new MethodResult(MethodResult.SUCCESS, "更新成功");
		}
        operLogService.addLog("终端用户", "重置终端用户"+usernamesArr+"密码失败", "1", "2", request);
		return new MethodResult(MethodResult.FAIL, "更新失败");
	}
	
	@RequestMapping(value="/{id}/password/update",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult updatePassword(@PathVariable("id") String id, TerminalUserVO terminalUserVO,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_user_reset_password)){
			return new MethodResult(MethodResult.FAIL,"您没有重置用户密码的权限，请联系管理员");
		}
		Map<String, Object> data = new LinkedHashMap<String, Object>();
		data.put("id", id);
		data.put("username", terminalUserVO.getUsername());
		data.put("password", terminalUserVO.getPassword());

		if(MethodResult.SUCCESS.equals(userService.changePassword(data).status)) {
            operLogService.addLog("终端用户", "重置终端用户"+terminalUserVO.getUsername()+"密码成功", "1", "1", request);
			return new MethodResult(MethodResult.SUCCESS, "更新成功");
		}
        operLogService.addLog("终端用户", "重置终端用户"+terminalUserVO.getUsername()+"密码失败", "1", "2", request);
		return new MethodResult(MethodResult.FAIL, "更新失败");
	}
	
	@RequestMapping(value="/{id}/delete",method=RequestMethod.GET)
	@ResponseBody
	public MethodResult deleteTerminalUser(@PathVariable("id") String id,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_user_delete)){
			return new MethodResult(MethodResult.FAIL,"您没有删除用户的权限，请联系管理员");
		}
		System.out.println("controller " + id);
		MethodResult mr = terminalUserService.deleteTerminalUserById(id);
		if(MethodResult.SUCCESS.equals(mr.status)){
            operLogService.addLog("终端用户", "删除终端用户"+mr.getProperty("names")+"成功", "1", "1", request);
			return new MethodResult(MethodResult.SUCCESS, "删除成功");
		}
        operLogService.addLog("终端用户", "删除终端用户"+mr.getProperty("names")+"失败", "1", "2", request);
		return new MethodResult(MethodResult.FAIL, "删除失败");
	}
	
	@RequestMapping(value="/allocat",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult allocateHost(String warehouseId,String allocateHostsIds,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_user_allocate_host)){
			return new MethodResult(MethodResult.FAIL,"您没有为用户分配主机的权限，请联系管理员");
		}
		if(StringUtil.isBlank(allocateHostsIds)){
			return new MethodResult(MethodResult.FAIL, "未选择用户");			
		}
		if(StringUtil.isBlank(warehouseId)){
			return new MethodResult(MethodResult.FAIL, "未选择仓库");			
		}
		MethodResult mr = cloudHostWarehouseService.allocateHostsByWarehouseIdAndUserIds(warehouseId,
        allocateHostsIds);
		if(mr.isSuccess()){
            operLogService.addLog("终端用户", "分配主机成功", "1", "1", request);
        }else{
            operLogService.addLog("终端用户", "分配主机失败", "1", "2", request);
        }
		return mr;
	}
 	   
	@RequestMapping(value ="/import", method=RequestMethod.POST)
	@ResponseBody
	public MethodResult importTerminalUser(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_user_upload_excel_add_users)){
			return new MethodResult(MethodResult.FAIL,"您没有导入Excel的权限，请联系管理员");
		}
		
		String status = "fail";
		String message = "导入失败，请稍后再试";
		
		try {
			
			//获取前台参数
			
			System.out.println("uploadFile is running");
			
			MultipartHttpServletRequest  multipartRequest = (MultipartHttpServletRequest) request;
			
			MultipartFile attach = multipartRequest.getFile("attach");
			
			
			//获取文件名
			String fileName = new String(attach.getOriginalFilename());

			
			
			//定义上传路径
			String filePath = AppHelper.getServerHome()+"/projects/"+AppHelper.APP_NAME+"/user_upload";
			
			//若无该文件夹自动创建
			File fp = new File(filePath);
			
			if(!fp.exists()){
				fp.mkdirs();
			}
			
			File file = new File(filePath+"/"+fileName);
			
			// 上传文件
			FileUtils.copyInputStreamToFile(attach.getInputStream(), file);
			
			MethodResult result = terminalUserService.importTerminalUser(file.getAbsolutePath());
			
			message = result.message;
			status = result.status;
			
			if(MethodResult.SUCCESS.equals(status)){
	            operLogService.addLog("终端用户", "导入用户成功", "1", "1", request);
				return new MethodResult(MethodResult.SUCCESS, message);
			}
            operLogService.addLog("终端用户", "导入用户失败", "1", "2", request);
			return new MethodResult(MethodResult.FAIL, message);
		} catch (Exception e) {
			e.printStackTrace();
            operLogService.addLog("终端用户", "导入用户失败", "1", "2", request);
			return new MethodResult(MethodResult.FAIL, message);
		}
		
	}
	
	@RequestMapping(value = "/download/demo", method=RequestMethod.GET)
	public void download(HttpServletResponse response,HttpServletRequest request) throws IOException {
		try {
        String fileName = "用户导入模版.xls";
        String path = AppHelper.getServerHome()+"/projects/"+AppHelper.APP_NAME+"/user_download/";
        String filePath = path + fileName;
        File file = new File(filePath);

			if(!file.isFile()){

				  //创建excel工作簿 
		        Workbook wb = new HSSFWorkbook(); 
		        //创建第一个sheet（页)
		        Sheet sheet = wb.createSheet("用户导入模版"); 
		        //Row 行 Cell 方格 
		        // 创建创建表头 
		        Row row = sheet.createRow(0);
		        for(int i = 0; i < AppConstant.DEFAULT_TABLE_TITLE.length; i++){
		        	row.createCell(i).setCellValue(AppConstant.DEFAULT_TABLE_TITLE[i]);
		        }
		        
		        //填充demo
		        row = sheet.createRow(1);
		        row.createCell(0).setCellValue("guest");
		        row.createCell(1).setCellValue("张三");
		        row.createCell(2).setCellValue("zhangsan@zhicloud.com");
		        row.createCell(3).setCellValue("12345678900");
		    	
		    	//若无该文件夹自动创建
				File fp = new File(path);
				
				if(!fp.exists()){
					fp.mkdirs();
				}
								
		        FileOutputStream fileOut = new FileOutputStream(filePath); 
		        // 把上面创建的工作簿输出到文件中 
		        wb.write(fileOut); 
		        
		        //关闭输出流 
		        fileOut.close(); 
		        
		        wb.close();
			}
			
			
			InputStream in = new FileInputStream(file);
			OutputStream os = response.getOutputStream();
			String finalFileName = this.encodeFileName(request, response, file.getName());
      response.setHeader("Content-Disposition", "attachment;" + finalFileName);//这里设置一下让浏览器弹出下载提示框，而不是直接在浏览器中打开
      response.addHeader("Content-Length", file.length() + "");
			response.setCharacterEncoding("utf-8");
			response.setContentType("application/vnd.ms-excel");
			int data = 0;
			while ((data = in.read()) != -1) {
				os.write(data);
			}
			os.close();
			in.close();
            operLogService.addLog("终端用户", "下载模板成功", "1", "1", request);
		} catch (Exception e) {
            operLogService.addLog("终端用户", "下载模板失败", "1", "2", request);
			throw new AppException("下载失败");
		}
		
	}

    public  String  encodeFileName(HttpServletRequest request, HttpServletResponse response, String fileName) {
        try {

            String finalFileName = null;

            finalFileName = "filename=\"" + new String(fileName.getBytes("UTF-8"), "ISO8859-1") + "\"";

            return finalFileName;
        } catch (UnsupportedEncodingException e) {
        	throw new AppException(e);
        }
    }
	
	@RequestMapping(value="/checkOldPassword",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult checkOldPassword(String oldPassword,HttpServletRequest request){
		if(userService.checkOldPassword(oldPassword, request)){
			return new MethodResult(MethodResult.SUCCESS,"正确");
		}
		return new MethodResult(MethodResult.FAIL,"错误");
	}
	@RequestMapping(value="/updateAdminPwd",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult updateAdminPassword(String newPassword,HttpServletRequest request){
		if(userService.changeAdminPwd(newPassword, request)){
			return new MethodResult(MethodResult.SUCCESS,"密码修改成功");
		}
		return new MethodResult(MethodResult.FAIL,"密码修改失败");
	}
	@RequestMapping(value="/checkAvaliable",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult checkAvaliable(String username){
      try{
          if(userService.checkAvailable(username)){
              return new MethodResult(MethodResult.SUCCESS,"用户名可用");

          }
          return new MethodResult(MethodResult.FAIL,"该用户已存在");
      } catch (Exception e) {
          e.printStackTrace();
      }

      return new MethodResult(MethodResult.FAIL,"该用户已存在");
	}
	@RequestMapping(value="/{id}/detail",method=RequestMethod.GET) 
	public String toDetail(@PathVariable("id") String id,Model model){ 
		TerminalUserVO user = terminalUserService.queryById(id);
		List<CloudHostVO> hosts = cloudHostService.getCloudHostByUserId(id);
		model.addAttribute("user", user);
		model.addAttribute("hosts", hosts);
		return "user_detail";
	}
	
	@RequestMapping(value="/checkAlias",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult checkAlias(String alias){
		if(userService.checkAlias(alias)){
			return new MethodResult(MethodResult.SUCCESS,"别名可用");
		}
		return new MethodResult(MethodResult.FAIL,"别名已存在");
	}
	
	@RequestMapping(value="/{id}/bindbox",method=RequestMethod.GET)
	public String bindBoxPage(@PathVariable("id") String userId, Model model){
		List<TerminalBoxVO> boxList = terminalBoxService.getUnboundBox();
		model.addAttribute("userId", userId);
		model.addAttribute("boxList", boxList);
		return "box/terminal_unbound_box";
	}
	
	@RequestMapping(value="/bindbox",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult bindBox(@RequestParam("userId") String userId,@RequestParam("boxId") String boxId,HttpServletRequest request){
		Map<String,Object> condition = new LinkedHashMap<String, Object>();
		condition.put("id", boxId);
		condition.put("allocate_user_id", userId);
		MethodResult mr = terminalBoxService.allocateTerminalBox(condition);
		if(mr.isSuccess()){
            operLogService.addLog("终端盒子", "分配盒子成功", "1", "1", request);
        }else{
              operLogService.addLog("终端盒子", "分配盒子失败", "1", "2", request);
        }
		return mr;
	}
	
	@RequestMapping(value="/unboundbox",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult unboundBox(@RequestParam("id") String userId,HttpServletRequest request){
		MethodResult mr = terminalBoxService.releaseTerminalBoxByUserId(userId);
		if(mr.isSuccess()){
            operLogService.addLog("终端盒子", "回收盒子成功", "1", "1", request);
        }else{
              operLogService.addLog("终端盒子", "回收盒子失败", "1", "2", request);
        }
		return mr;
	}
}
