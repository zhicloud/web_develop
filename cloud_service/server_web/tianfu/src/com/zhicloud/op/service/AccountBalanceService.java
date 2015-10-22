package com.zhicloud.op.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhicloud.op.vo.AccountBalanceDetailVO;

public interface AccountBalanceService { 
	 
	
	public String updateAfterPay(String orderId);
	
	public void queryRechargeRecord(HttpServletRequest request, HttpServletResponse response);
	
	public void queryConsumptionRecord(HttpServletRequest request, HttpServletResponse response);
	
	public void queryRechargeRecordForInvoice(HttpServletRequest request, HttpServletResponse response);
	
	public void queryRechargeRecordForInvoiceForAgent(HttpServletRequest request, HttpServletResponse response);
	
	public String toRechargeRecordPage(HttpServletRequest request, HttpServletResponse response);
	
	public String toRechargePage(HttpServletRequest request, HttpServletResponse response);
	
	public String toRechargeGiftPage(HttpServletRequest request, HttpServletResponse response);

	String queryRechargeDetailPage(HttpServletRequest request,
			HttpServletResponse response);
	
	public void getBalance(HttpServletRequest request,
			HttpServletResponse response);
	
	public String toConsumptionRecordPage(HttpServletRequest request, HttpServletResponse response);
	
	public List<AccountBalanceDetailVO> queryRechargeRecordForExport(HttpServletRequest request,HttpServletResponse response);
	
	public List<AccountBalanceDetailVO> queryConsumptionRecordForExport(HttpServletRequest request,HttpServletResponse response);


}
