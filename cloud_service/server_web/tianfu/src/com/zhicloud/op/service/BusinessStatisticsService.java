package com.zhicloud.op.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhicloud.op.vo.BusinessStatisticsVO;
import com.zhicloud.op.vo.CloudHostBillDetailVO;

public interface BusinessStatisticsService {
	
public String toBusinessDetailManagePage(HttpServletRequest request, HttpServletResponse response);
	
	public String agentMonthlyDetailPage(HttpServletRequest request, HttpServletResponse response);
	/**
	 * 代理商业务统计
	 * @param request
	 * @param response
	 * @return
	 */
	public String agentBusinessStatistics(HttpServletRequest request, HttpServletResponse response);
	/**
	 * 查询代理商业务明细，按月统计
	 * @param request
	 * @param response
	 */
	public void queryBusinessByMonthly(HttpServletRequest request, HttpServletResponse response);
	/**
	 * 查询代理商月业务明细，用于展示
	 * @param request
	 * @param response
	 */
	public void queryAgentMonthlyDetail(HttpServletRequest request, HttpServletResponse response);
	/**
	 * 通过时间查询代理商的业务统计
	 * @param request
	 * @param response
	 */
	public void queryBillDetailByTimeAndRegion(HttpServletRequest request, HttpServletResponse response);
	/**
	 * 查询代理商月业务明细
	 * @param request
	 * @param response
	 * @return
	 */
	public List<CloudHostBillDetailVO> queryAgentMonthlyDetailForExport(HttpServletRequest request, HttpServletResponse response);
	/**
	 * 查询代理商业务统计
	 * @param request
	 * @param response
	 * @return
	 */
	public List<BusinessStatisticsVO> queryBusinessStatistics(HttpServletRequest request, HttpServletResponse response);

}
