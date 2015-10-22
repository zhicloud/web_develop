package com.zhicloud.op.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhicloud.op.remote.MethodResult;

public interface CashCouponService 
{
	public String operatorCashCouponPage(HttpServletRequest request,HttpServletResponse response);
	
	public String addCashCouponPageByOperator(HttpServletRequest request,HttpServletResponse response);
	
	public String sendCashCouponByEmailPage(HttpServletRequest request, HttpServletResponse response);
	
	public String sendCashCouponByPhonePage(HttpServletRequest request, HttpServletResponse response);
	
	public String cashCouponPage(HttpServletRequest request, HttpServletResponse response);
	//------------------------------------------
	
	public void queryCashCouponForOperator(HttpServletRequest request, HttpServletResponse response);
	
	//------------------------------------------
	public MethodResult addCashCoupon(Map<String, String> parameter);
	
	public MethodResult deleteCashCouponByIds(List<String> cashCouponIds);
	
	public MethodResult sendCashCouponByPhone(Map<String, String> parameter);
	
	public MethodResult sendCashCouponByEmail(Map<String, String> parameter);
	
	public MethodResult convertCashCoupon(Map<String, String> parameter);
}
