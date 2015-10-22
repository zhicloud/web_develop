package com.zhicloud.op.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.CloudHostWarehouseDefinitionVO;

public interface CloudHostWarehouseDefinitionMapper
{
	
	public CloudHostWarehouseDefinitionVO getById(String id);
	
	public int minusOneRemainAmountById(String id);
	
	public int queryPageCount(Map<String, Object> condition);

	public List<CloudHostWarehouseDefinitionVO> queryPage(Map<String, Object> condition);

	public CloudHostWarehouseDefinitionVO getWarehouseById(String id);
	
	public int addWarehouse(Map<String,Object> condition);
	
	public int updateWarehouseById(Map<String,Object> data);
	
	public int updateRemainAmountById(Map<String,Object> date);
	
	public int deleteByIds(String[] warehouseIds);
}
