package com.zhicloud.op.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhicloud.op.remote.MethodResult;

public interface SysGroupService
{

	// --------------------

	public String managePage(HttpServletRequest request, HttpServletResponse response);

	public String addPage(HttpServletRequest request, HttpServletResponse response);

	public String modPage(HttpServletRequest request, HttpServletResponse response);

	public String setGroupMemberPage(HttpServletRequest request, HttpServletResponse response);

	public String setRolePage(HttpServletRequest request, HttpServletResponse response);

	// --------------
	
	public void querySysGroup(HttpServletRequest request, HttpServletResponse response);

	public void queryUserFromGroupItem(HttpServletRequest request, HttpServletResponse response);

	// -------------------

	public MethodResult addSysGroup(Map<String, String> parameter);
	
	public MethodResult addGroupItem(Map<String, String> parameter);
	
	// ------------------

	public MethodResult updateSysGroupById(Map<String, String> parameter);

	public MethodResult updateRoleFromGroupItem(String groupId, List<String> roleList);
	
	public MethodResult setGroupMembers(String groupId, List<String> userIds);
	
	// ------------------
	
	public MethodResult deleteSysGroupById(String groupId);

	public MethodResult deleteSysGroupByIds(List<String> groupId);
	
	public MethodResult deleteUserFromGroupItem(String account);


}
