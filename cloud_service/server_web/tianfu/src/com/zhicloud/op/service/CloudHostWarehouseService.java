package com.zhicloud.op.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhicloud.op.remote.MethodResult;


public interface CloudHostWarehouseService
{
	
	/**
	 * 创建一部云主机仓库的云主机
	 */
	public MethodResult createOneWarehouseCloudHost();
	
	/**
	 * 已经失败的云主机重新创建
	 * @return
	 */
	public MethodResult createOneWarehouseFailedCloudHost();
	
	public MethodResult createOneWarehouseCloudHostByWarehouseId(String warehouseId);
	
	//====================
	public String managePage(HttpServletRequest request, HttpServletResponse response);
	
	public void queryWarehouse(HttpServletRequest request, HttpServletResponse response);
	
	public String addPage(HttpServletRequest request, HttpServletResponse response);
	
	public String modPage(HttpServletRequest request, HttpServletResponse response);
	
	public MethodResult addWarehouse(Map<String, Object> parameter);
	
	public MethodResult modWarehouse(Map<String, Object> parameter);
	
	public MethodResult deleteWarehouseByIds(List<?> warehouseIds);
	
	public MethodResult deleteCloudHostById(String hostId);
	
	public String warehouseDetailPage(HttpServletRequest request, HttpServletResponse response);
	
	public void queryWarehouseDetail(HttpServletRequest request, HttpServletResponse response);
	
	public MethodResult addACloudHostToWareById(String warehouseId,String userId);
}
