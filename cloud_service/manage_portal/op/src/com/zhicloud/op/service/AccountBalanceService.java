package com.zhicloud.op.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhicloud.op.remote.MethodResult;
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
	
	/**
	 * 通过userId查询充值记录,跳转到记录页面。
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public String getRechargeRecordByUserId(HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 通过userId查询充值记录，返回到页面。
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public void getRechargeRecord(HttpServletRequest request, HttpServletResponse response);

	/**
	 * @Description:取得消费信息的分组汇总记录
	 * @param request
	 * @param response
	 * @return
	 */
	public AccountBalanceDetailVO getConsumDetail(HttpServletRequest request,HttpServletResponse response,Map<String, Object> condition);
}
