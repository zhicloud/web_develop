package com.zhicloud.op.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhicloud.op.remote.MethodResult;


public interface SysUserService
{

	public MethodResult login(Map<String, String> parameter);
	
	public MethodResult logout();
	
	public void querySysUser(HttpServletRequest request, HttpServletResponse response);
	
	public void querySysUserNotInGroupId(HttpServletRequest request, HttpServletResponse response);
	
	public String updatePasswordPage(HttpServletRequest request,HttpServletResponse response);
	
	public MethodResult updatePasswordById(Map<String, Object> parameter);
	
	public void querySysUserNotGetImage(HttpServletRequest request, HttpServletResponse response);
	
}
