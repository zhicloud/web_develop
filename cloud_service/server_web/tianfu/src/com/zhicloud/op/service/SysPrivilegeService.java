package com.zhicloud.op.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhicloud.op.remote.MethodResult;



public interface SysPrivilegeService
{
	
	public void getPrivilegeTree(HttpServletRequest request, HttpServletResponse response);
	
	public MethodResult saveSysRolePrivilege(String roleId, List<String> privilege);
	
}
