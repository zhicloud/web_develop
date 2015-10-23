package com.zhicloud.op.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.vo.IntelligentRouterVO;

public interface IntelligentRouterService {
	
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
	public MethodResult deleteRule(String target,JSONArray ip,JSONArray port,Integer region,Integer mode);
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
	* @param @param response      
	* @return void     
	* @throws
	 */
	public void queryRule(HttpServletRequest request, HttpServletResponse response);
	/**
	 * 
	* @Title: addRulePage 
	* @Description: 新增rulepage
	* @param @param request
	* @param @param response
	* @param @return      
	* @return String     
	* @throws
	 */
	public String addRulePage(HttpServletRequest request,HttpServletResponse response);

}
