package com.zhicloud.op.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhicloud.op.remote.MethodResult;


public interface SysRoleService
{
	
	//--------------------
	
	public String managePage(HttpServletRequest request, HttpServletResponse response);
	
	public String addPage(HttpServletRequest request, HttpServletResponse response);
	
	public String modPage(HttpServletRequest request, HttpServletResponse response);
	
	public String setPrivilegePage(HttpServletRequest request, HttpServletResponse response);
	
	//------------------
	
	public void querySysRole(HttpServletRequest request, HttpServletResponse response);
	
	//-------------------
	
	public MethodResult addSysRole(Map<String, String> parameter);
	
	public MethodResult updateSysRoleById(Map<String, String> parameter);
	
	public MethodResult deleteSysRoleById(String roleId);
	
	public MethodResult deleteSysRoleByIds(List<?> roleId);
	
	//--------------
	
	
}
