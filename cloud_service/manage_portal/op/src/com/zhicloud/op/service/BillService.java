package com.zhicloud.op.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.vo.BillVO;

public interface BillService
{
	
	// --------------------
	
	
	public void billQueryResultPartPage(HttpServletRequest request, HttpServletResponse response);
	
	public String managePage(HttpServletRequest request, HttpServletResponse response);
	public String queryBillDetailPage(HttpServletRequest request, HttpServletResponse response);
	public String queryCloudHostBillDetailPage(HttpServletRequest request, HttpServletResponse response);
	
	// ---------------
	public MethodResult deleteBillByIds(List<?> hostIds);
	
	public List<BillVO> queryAgentMonthlyDetailForExport(HttpServletRequest request, HttpServletResponse response);
	
}
