package com.zhicloud.op.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public interface TestService
{

	public void queryTermianlUser(HttpServletRequest request, HttpServletResponse response);
	
	
	public int addTermianlUser(Map<Object, Object> parameter);

	public int deleteTermianlUser(String id);
	
}
