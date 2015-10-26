package com.zhicloud.op.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhicloud.op.remote.MethodResult;


public interface AppPropertyService
{

	public String appPropertiesManagePage(HttpServletRequest request, HttpServletResponse response);

	public MethodResult updateAppPropertyService(Map<String, String> parameter);
	
}
