package com.zhicloud.op.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhicloud.op.remote.MethodResult;

public interface InvoiceAddressService {
	
	public String managePage(HttpServletRequest request, HttpServletResponse response);
	
	public String addAddressPage(HttpServletRequest request, HttpServletResponse response);
	
	public String modPage(HttpServletRequest request, HttpServletResponse response);
	
	public void queryAddress(HttpServletRequest request, HttpServletResponse response);
	
	public MethodResult addAddress(Map<String, String> parameter);
	
	public MethodResult updateAddress(Map<String, String> parameter);

	public MethodResult deleteAddressByIds(List<?> ids);

}
