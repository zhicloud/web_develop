package com.zhicloud.op.service;

import com.zhicloud.op.remote.MethodResult;


public interface OrderInfoService
{
	
	/**
	 * 从订单表里面按时间找出一个需要创建的云主机，进行创建
	 */
	public MethodResult createOneUserOrderedCloudHost(int region);
	
//	/**
//	 * 创建一部云主机仓库的云主机
//	 */
//	public MethodResult createOneWarehouseCloudHost();
	
}
