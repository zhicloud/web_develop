package com.zhicloud.ms.controller;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.IOperLogService;
import com.zhicloud.ms.service.ISysGroupService;
import com.zhicloud.ms.service.IUserService;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.util.TransFormPrivilegeUtil;
import com.zhicloud.ms.vo.SysGroupVO;
import com.zhicloud.ms.vo.SysUser;


/**
 * ClassName: SysGroupController 
 * Function: 群组的controller. 
 * date: Mar 16, 2015 12:00:31 PM 
 *
 * @author sean
 * @version 
 * @since JDK 1.7
 */

@Controller
@RequestMapping("/group")
public class SysGroupController {

	@Resource
	private ISysGroupService sysGroupService;
	
	@Resource
	private IUserService userService;
	
    @Resource
    private IOperLogService operLogService;
	
	@RequestMapping(value="/all",method=RequestMethod.GET)
	public String list(Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_group_query)){
			return "not_have_access";
		}
		List<SysGroupVO> sysGroupList = sysGroupService.getCloudHostAmountInGroup();	
		List<SysUser> userOutList = userService.getAllTerminalOutGroup();
		model.addAttribute("sys_group_list", sysGroupList);
		model.addAttribute("terminal_user_list_out", userOutList);
		return "group_manage";
	}
	
	@RequestMapping(value="/amount",method=RequestMethod.GET)
	@ResponseBody
	public MethodResult getAmount(){
		MethodResult result = new MethodResult(MethodResult.SUCCESS, "保存成功");
		List<SysGroupVO> list = sysGroupService.getCloudHostAmountInGroup();
		result.put("amount_list", list);
		return result;
	}
	
	@RequestMapping(value="/add",method=RequestMethod.GET)
	public String addGroupPage(Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_group_add)){
			return "not_have_access";
		}
		List<SysGroupVO> list = sysGroupService.queryAll();
		model.addAttribute("groupList", list);
		return "group_manage_add";
	}
	
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult addSysGroup(SysGroupVO sysGroupVO,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_group_add)){
			return new MethodResult(MethodResult.FAIL,"您没有新增群组的权限，请联系管理员");
		}
		Map<String, Object> data = new LinkedHashMap<String, Object>();
		data.put("group_name", sysGroupVO.getGroupName());
		data.put("parent_id", sysGroupVO.getParentId());
		data.put("description", sysGroupVO.getDescription());
		if(MethodResult.SUCCESS.equals(sysGroupService.addSysGroup(data).status)) {
		    operLogService.addLog("群组信息", "新增群组"+sysGroupVO.getGroupName()+"成功", "1", "1", request);
			return new MethodResult(MethodResult.SUCCESS, "保存成功");
		}
        operLogService.addLog("群组信息", "新增群组"+sysGroupVO.getGroupName()+"失败", "1", "2", request);
		return new MethodResult(MethodResult.FAIL, "保存失败");
	}
	
	@RequestMapping(value="/{id}/relation",method=RequestMethod.GET)
	public String relationPage(@PathVariable("id") String id,Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_group_member_modify)){
			return "not_have_access";
		}
		Map<String, Object> condition = new LinkedHashMap<String, Object>();
		condition.put("group_id", id);
		List<SysUser> userList =  userService.getAllTerminalInGroup(condition);
		List<SysUser> userOutList = userService.getAllTerminalOutGroup();
		model.addAttribute("userOutGroup", userOutList);
		model.addAttribute("userInGroup", userList);
		model.addAttribute("groupId", id);
		return "group_manage_relation";
	}
	
	@RequestMapping(value="/{id}/item",method=RequestMethod.GET)
	@ResponseBody
	public List<SysUser> manageGroupItemPage(@PathVariable("id") String id,HttpServletRequest request){
		Map<String, Object> condition = new LinkedHashMap<String, Object>();
		condition.put("group_id", id);
		return userService.getAllTerminalInGroup(condition);
	}
	
	@RequestMapping(value="/{id}/updatepage",method=RequestMethod.GET)
	public String updateGroupPage(@PathVariable("id") String id,Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_group_modify)){
			return "not_have_access";
		}
		SysGroupVO group = sysGroupService.queryById(id);
//		List<SysGroupVO> list = sysGroupService.queryAll();
		List<SysGroupVO> parentList =  sysGroupService.removeAllChildren(id);
		model.addAttribute("groupList", parentList);
		model.addAttribute("group", group);
		return "group_manage_update";
	}
	
	@RequestMapping(value="/{id}/update",method=RequestMethod.GET)
	@ResponseBody
	public MethodResult updateSysGroupPage(@PathVariable("id") String id,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_group_modify)){
			return new MethodResult(MethodResult.FAIL,"您没有修改群组的权限，请联系管理员");
		}
		
		MethodResult result = new MethodResult(MethodResult.SUCCESS, "更新成功");
		
		SysGroupVO sysGroupVO = sysGroupService.queryById(id);
		
		List<SysGroupVO> parentList =  sysGroupService.removeAllChildren(id);
		result.put("sys_group", sysGroupVO);
		result.put("parent_id_list", parentList);
        operLogService.addLog("群组信息", "修改群组"+sysGroupVO.getGroupName()+"成功", "1", "1", request);
		return result;
	}
	
	@RequestMapping(value="/{id}/update",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult updateSysGroup(@PathVariable("id") String id, SysGroupVO sysGroupVO,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_group_modify)){
			return new MethodResult(MethodResult.FAIL,"您没有修改群组的权限，请联系管理员");
		}
		Map<String, Object> data = new LinkedHashMap<String, Object>();
		data.put("id", id);
		data.put("group_name", sysGroupVO.getGroupName());
		data.put("parent_id", sysGroupVO.getParentId());
		data.put("description", sysGroupVO.getDescription());
		if(MethodResult.SUCCESS.equals(sysGroupService.updateSysGroupById(data).status)) {
	        operLogService.addLog("群组信息", "修改群组"+sysGroupVO.getGroupName()+"成功", "1", "1", request);
			return new MethodResult(MethodResult.SUCCESS, "更新成功");
		}
        operLogService.addLog("群组信息", "修改群组"+sysGroupVO.getGroupName()+"失败", "1", "2", request);
		return new MethodResult(MethodResult.FAIL, "更新失败");
	}
	
	@RequestMapping(value="/item/manage",method=RequestMethod.GET)
	@ResponseBody
	public MethodResult manageMember(String groupId, String userIdOut, String userIdIn,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_group_modify)){
			return new MethodResult(MethodResult.FAIL,"您没有修改群组的权限，请联系管理员");
		}
		Map<String, Object> parameter = new LinkedHashMap<String, Object>();
		parameter.put("group_id", groupId);
		parameter.put("user_id_in", userIdIn);
		parameter.put("user_id_out", userIdOut);
		MethodResult mr = sysGroupService.manageItems(parameter);
		if(MethodResult.SUCCESS.equals(mr.status)){
	        operLogService.addLog("群组信息", "修改群组"+mr.getProperty("names")+"成员成功", "1", "1", request);
			return new MethodResult(MethodResult.SUCCESS, "更新成功");
		}else{
	          operLogService.addLog("群组信息", "修改群组"+mr.getProperty("names")+"成员失败", "1", "2", request);
			return new MethodResult(MethodResult.FAIL, "更新失败");
		}
		
	}
	
	@RequestMapping(value="/{id}/delete",method=RequestMethod.GET)
	@ResponseBody
	public MethodResult deleteSysGroup(@PathVariable("id") String id,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_group_delete)){
			return new MethodResult(MethodResult.FAIL,"您没有删除群组的权限，请联系管理员");
		}
		System.out.println("controller "+id);
		MethodResult mr = sysGroupService.deleteSysGroupById(id);
		if(MethodResult.SUCCESS.equals(mr.status)){
            operLogService.addLog("群组信息", "删除群组"+mr.getProperty("name")+"成功", "1", "1", request);
			return new MethodResult(MethodResult.SUCCESS, "删除成功");
		}
        operLogService.addLog("群组信息", "删除群组"+mr.getProperty("name")+"失败", "1", "2", request);
		return new MethodResult(MethodResult.FAIL, "删除失败");
	}
	
	@RequestMapping(value="/deleteIds",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult deleteSysGroups(@RequestParam("ids[]") String[] ids,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_group_delete)){
			return new MethodResult(MethodResult.FAIL,"您没有删除群组的权限，请联系管理员");
		}
		List<String> idList = Arrays.asList(ids);
        operLogService.addLog("群组信息", "批量删除群组成功", "1", "1", request);
		return sysGroupService.deleteSysGroupByIds(idList);
	}
	
	@RequestMapping(value="/checkAvaliable",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult checkAvaliable(String groupName){
		if(sysGroupService.checkAvailable(groupName)){
			return new MethodResult(MethodResult.SUCCESS,"群组名可用");
		}
		return new MethodResult(MethodResult.FAIL,"该群组名已存在");
	}
	
	@RequestMapping(value="/list",method=RequestMethod.GET)
	@ResponseBody
	public MethodResult getAll() {
		MethodResult result = new MethodResult(MethodResult.SUCCESS, "获取成功");
		List<SysGroupVO> list = sysGroupService.queryAll();
		result.put("sys_group_list", list);
		return result;
	}
}

