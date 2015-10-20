package com.zhicloud.ms.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhicloud.ms.remote.MethodResult;

import net.sf.json.JSONArray;

 

public interface IIntelligentRouterService {
	
	/**
	 * 
	* @Title: queryRule 
	* @Description: 查询规则
	* @param @param request
	* @param @param response
	* @param @return      
	* @return List<IntelligentRouterVO>     
	* @throws
	 */
	public String queryRulePage(HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 
	* @Title: deleteRule 
	* @Description: 删除规则
	* @param @param parameter
	* @param @return      
	* @return MethodResult     
	* @throws
	 */
	public MethodResult deleteRule(String  target,String  mode,String ip0,String  ip1,String  port);
	/**
	 * 
	* @Title: addRule 
	* @Description: 新增规则
	* @param @param parameter
	* @param @return      
	* @return MethodResult     
	* @throws
	 */
	public MethodResult addRule(Map<String, Object> parameter);
	/**
	 * 
	* @Title: queryRule 
	* @Description: 查询rule
	* @param @param request      
	* @return void     
	* @throws
	 */
	public void queryRule(HttpServletRequest request);
	/**
	 * 
	* @Title: addRulePage 
	* @Description: 新增规则前数据
	* @param @param request 
	* @param @return      
	* @return  void    
	* @throws
	 */
	public void addRulePage(HttpServletRequest request);

}
