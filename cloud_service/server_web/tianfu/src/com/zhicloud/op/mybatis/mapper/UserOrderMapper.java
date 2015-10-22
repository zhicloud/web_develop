package com.zhicloud.op.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.UserOrderVO;

// 因为类名与表名不一致，以后不建议使用，可能会被删除
@Deprecated
public interface UserOrderMapper
{
	public List<UserOrderVO> getOrderById(String userId);
	
	public UserOrderVO getOrderByOrderId(String id);
	
	public UserOrderVO getOrderConfigById(String id);
	
	public List<UserOrderVO> getOrderDetailByOrderId(String orderId);
	
	public int queryPageCount(Map<String, Object> condition);
	
	public List<UserOrderVO> queryPage(Map<String, Object> condition);
	
	public int insertIntoOrder(Map<String, Object> condition);
	
	public int insertIntoOrderDetail(Map<String, Object> condition);
	
	public int updateOrder(String id);
	
	public int updateConfig(String id, String startTime, String endTime);
	
	public int updateOrderConfig(Map<String, Object> condition);
	
	public int queryOrderConfigPageCount(Map<String, Object> condition);
	
	public List<UserOrderVO> queryOrderConfigPage(Map<String, Object> condition);
	
	public List<UserOrderVO> getTrailOrderConfigByUserId(String userId);
	
	public List<UserOrderVO> getPortsByConfigId(String configId);
	
}
