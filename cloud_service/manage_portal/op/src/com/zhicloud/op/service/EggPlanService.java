package com.zhicloud.op.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhicloud.op.remote.MethodResult;

public interface EggPlanService {
	/**
	 * 	
	* @Title: eggPlanManagePage 
	* @Description: 蛋壳计划管理页面 
	* @param @param request
	* @param @param response
	* @param @return      
	* @return String     
	* @throws
	 */
	public String eggPlanManagePage(HttpServletRequest request, HttpServletResponse response);
	//------------------------------------------
	/**
	 * 
	* @Title: queryEggPlan 
	* @Description: 查询信息
	* @param @param request
	* @param @param response      
	* @return void     
	* @throws
	 */
	public void queryEggPlan(HttpServletRequest request, HttpServletResponse response);
	
	//------------------------------------------
	/**
	 * 
	* @Title: addEggPlan 
	* @Description: 新增蛋壳计划 
	* @param @param parameter
	* @param @return      
	* @return MethodResult     
	* @throws
	 */
	public MethodResult addEggPlan(Map<String, String> parameter);
	/**
	 * 
	* @Title: updateEggPlanByIds 
	* @Description: 更新蛋壳计划
	* @param @param cashCouponIds
	* @param @return      
	* @return MethodResult     
	* @throws
	 */
	public MethodResult updateEggPlanByIds(List<String> cashCouponIds);
	/**
	 * 
	* @Title: eggPlanManagePageForDetail 
	* @Description: 查看详情页
	* @param @param request
	* @param @param response
	* @param @return      
	* @return String     
	* @throws
	 */
	public String eggPlanManagePageForDetail(HttpServletRequest request, HttpServletResponse response);
	

}
