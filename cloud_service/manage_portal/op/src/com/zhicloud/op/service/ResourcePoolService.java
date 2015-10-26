/**
 * Project Name:op
 * File Name:VpnService.java
 * Package Name:com.zhicloud.op.service
 * Date:2015年4月1日下午2:12:43
 * 
 *
*/ 

package com.zhicloud.op.service;  	  


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public interface ResourcePoolService {
	/**
	 * 查询主机资源池
	 * 
	 * @param request
	 * @param response
	 */
	public void queryComputerPool(HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 查询存储资源池
	 * 
	 * @param request
	 * @param response
	 */
	public void queryStoragePool(HttpServletRequest request,HttpServletResponse response);
	
	
	/**
	 * 查询地址资源池
	 * 
	 * @param request
	 * @param response
	 */
	public void queryAddressPool(HttpServletRequest request,HttpServletResponse response);
	
	
	/**
	 * 查询端口资源池
	 * 
	 * @param request
	 * @param response
	 */
	public void queryPortPool(HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 跳转到主机资源池管理页
	 * @param request
	 * @param response
	 * @return
	 */
	public String computerPoolPage(HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 跳转到存储资源池管理页
	 * @param request
	 * @param response
	 * @return
	 */
	public String storagePoolPage(HttpServletRequest request,HttpServletResponse response); 
	
	/**
	 * 跳转到地址资源池管理页
	 * @param request
	 * @param response
	 * @return
	 */
	public String addressPoolPage(HttpServletRequest request,HttpServletResponse response); 
	
	/**
	 * 跳转到端口资源池管理页
	 * @param request
	 * @param response
	 * @return
	 */
	public String portPoolPage(HttpServletRequest request,HttpServletResponse response); 
}

