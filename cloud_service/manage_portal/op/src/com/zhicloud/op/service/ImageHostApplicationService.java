package com.zhicloud.op.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhicloud.op.remote.MethodResult;

public interface ImageHostApplicationService {
	/**
	 * 	
	* @Title: imageHostApplicationManagePage 
	* @Description: 镜像主机申请管理页面 
	* @param @param request
	* @param @param response
	* @param @return      
	* @return String     
	* @throws
	 */
	public String imageHostApplicationManagePage(HttpServletRequest request, HttpServletResponse response);
	//------------------------------------------
	/**
	 * 
	* @Title: queryImageHostApplication 
	* @Description: 查询信息
	* @param @param request
	* @param @param response      
	* @return void     
	* @throws
	 */
	public void queryImageHostApplication(HttpServletRequest request, HttpServletResponse response);
	
	//------------------------------------------
	/**
	 * 
	* @Title: addImageHostApplication 
	* @Description: 新增镜像主机申请 
	* @param @param parameter
	* @param @return      
	* @return MethodResult     
	* @throws
	 */
	public MethodResult addImageHostApplication(Map<String, String> parameter);
	/**
	 * 
	* @Title: updateImageHostApplicationByIds 
	* @Description: 更新镜像主机申请
	* @param @param cashCouponIds
	* @param @return      
	* @return MethodResult     
	* @throws
	 */
	public MethodResult updateImageHostApplicationByIds(List<String> ids);
	/**
	 * 
	* @Title: imageHostApplicationManagePageForDetail 
	* @Description: 查看详情页
	* @param @param request
	* @param @param response
	* @param @return      
	* @return String     
	* @throws
	 */
	public String imageHostApplicationManagePageForDetail(HttpServletRequest request, HttpServletResponse response);
	

}
