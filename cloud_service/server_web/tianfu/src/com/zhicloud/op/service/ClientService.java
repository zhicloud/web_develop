package com.zhicloud.op.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface ClientService
{

	public void authenticate(HttpServletRequest request, HttpServletResponse response);

	public void getCloudHost(HttpServletRequest request, HttpServletResponse response);
	
	public void cloudHostOperation(HttpServletRequest request, HttpServletResponse response);
	
	public void cloudHostChange(HttpServletRequest request, HttpServletResponse response);
	
	public void getCloudHostPrice(HttpServletRequest request,HttpServletResponse response);
	
	public void getClientVersion(HttpServletRequest request, HttpServletResponse response);
	
	public void changeDisplayName(HttpServletRequest request,HttpServletResponse response);
	
}
