package com.zhicloud.op.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.vo.JsBankFormVO;

public interface PaymentService
{
	
	public boolean toPayPage(StringBuffer formstr, JsBankFormVO bank);
	
	public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception;
	
	public void getParameter(HttpServletRequest request, HttpServletResponse response);
	
	public MethodResult addCart(Map<String, String> parameter);
	
	public MethodResult getTrail(Map<String, Object> parameter);
	
	public MethodResult deleteDetailAndConfig(Map<String, String> parameter);
	
	public void getCartParameter(HttpServletRequest request, HttpServletResponse response);
	
	public String toAccountPage(HttpServletRequest request, HttpServletResponse response);
	
	public String updateOrder(String id);
	
	public String managePage(HttpServletRequest request, HttpServletResponse response);
	
	public void queryOrderConfig(HttpServletRequest request, HttpServletResponse response);
	
	public String toManageModifyPage(HttpServletRequest request, HttpServletResponse response);
	
	public String toManageDetailPage(HttpServletRequest request, HttpServletResponse response);
	
	public MethodResult addRealHost(Map<String, String> parameter);
	
	public MethodResult checkOrderTrail(Map<String, String> parameter);
	
	public MethodResult getCloudHost(Map<String, Object> parameter);
	 
	public MethodResult getCloudHostFromAgent(Map<String, Object> parameter);
	
	public MethodResult getCloudHostForNormal(Map<String, Object> parameter);
	
	public void toAccountPageForSoftWarePark(HttpServletRequest request,HttpServletResponse response);
}
