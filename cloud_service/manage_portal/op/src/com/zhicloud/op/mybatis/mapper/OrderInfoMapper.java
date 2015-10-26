package com.zhicloud.op.mybatis.mapper;

import java.util.Map;

import com.zhicloud.op.vo.OrderInfoVO;


public interface OrderInfoMapper
{
	
	public int addOrderInfo(Map<String, Object> condition);
	
	public OrderInfoVO getOneUnprocessedOrderByRegion(int region);
	
	public int updateProcessStatusById(Map<String, Object> data);
	
	public int updateProcessStatusToPendingById(Map<String, Object> data);
	
}
