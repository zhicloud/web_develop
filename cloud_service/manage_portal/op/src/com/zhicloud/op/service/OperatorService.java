package com.zhicloud.op.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhicloud.op.remote.MethodResult;

public interface OperatorService
{

	// --------------------

	public String managePage(HttpServletRequest request, HttpServletResponse response);

	public String addPage(HttpServletRequest request, HttpServletResponse response);

	public String modPage(HttpServletRequest request, HttpServletResponse response);
	
	public String basicInformationPage(HttpServletRequest request,HttpServletResponse response);
	
	public String updatePasswordPage(HttpServletRequest request,HttpServletResponse response);
	
	public String resetPasswordPage(HttpServletRequest request,HttpServletResponse response);

	// --------------

	public void queryOperator(HttpServletRequest request, HttpServletResponse response);

	// --------------------

	public MethodResult addOperator(Map<String, String> parameter);

	public MethodResult updateOperatorById(Map<String, Object> parameter);

	public MethodResult deleteOperatorById(String operatorId);

	public MethodResult deleteOperatorByIds(List<?> operatorIds);
	
	public MethodResult updateBasicInformation(Map<String, Object> parameter);
	
	public MethodResult updatePasswordById(Map<String, Object> parameter);
	
	public MethodResult resetPasswordById(Map<String, Object> parameter);
	
	public String operatorIndexPage(HttpServletRequest request, HttpServletResponse response);
	
	

}
