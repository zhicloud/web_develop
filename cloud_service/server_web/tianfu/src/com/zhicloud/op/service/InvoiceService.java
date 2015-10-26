package com.zhicloud.op.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.vo.InvoiceVO;

public interface InvoiceService {
	
	public String managePage(HttpServletRequest request, HttpServletResponse response);
	
	public String managePageForAgent(HttpServletRequest request, HttpServletResponse response);
	
	public String pendingPage(HttpServletRequest request, HttpServletResponse response);
	
	public String exportedPage(HttpServletRequest request, HttpServletResponse response);
	
	public String sentPage(HttpServletRequest request, HttpServletResponse response);
	
	public String sentSuccessPage(HttpServletRequest request, HttpServletResponse response);
	
	public String settingPage(HttpServletRequest request, HttpServletResponse response);
	
	public String addInvoicePage(HttpServletRequest request, HttpServletResponse response);

	public String addInvoicePageForAgent(HttpServletRequest request, HttpServletResponse response);
	
	public void queryInvoice(HttpServletRequest request, HttpServletResponse response);
	
	public void queryAllInvoice(HttpServletRequest request, HttpServletResponse response);
	
	public MethodResult addInvoice(Map<String, String> parameter);
	
	public MethodResult saveSetting(Map<String, String> parameter);
	
	public MethodResult exportInvoiceByIds(List<String> ids);
	
	public MethodResult sendInvoiceByIds(List<String> ids);

	public MethodResult confirmInvoiceByIds(List<String> ids);
	
	public List<InvoiceVO> queryInvoiceByIds(String[]ids);

	
}
