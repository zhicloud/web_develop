package com.zhicloud.op.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.CloudHostWarehouseDetailVO;


public interface CloudHostWarehouseDetailMapper
{

	public CloudHostWarehouseDetailVO getByHostId(String hostId);
	
	public CloudHostWarehouseDetailVO getOneUncreatedCloudHost();
	
	public CloudHostWarehouseDetailVO getOneFailedCloudHost();

	public CloudHostWarehouseDetailVO getOneUncreatedCloudHostByWarehouseId(String warehouseId);

	public List<CloudHostWarehouseDetailVO> getUncreatedCloudHostsByWarehouseId();
	
	public CloudHostWarehouseDetailVO getOneUndistributedCloudHostFromWarehouse(Map<String, Object> condition);

	public List<CloudHostWarehouseDetailVO> getAllUndistributedCloudHostFromWarehouse(Map<String, Object> condition);
	
	public int getRemainAmountByWarehouseId(String warehouseId);
	
	//-------------
	
	public int addWarehouseDetail(Map<String, Object> data);
	
	//--------------

	public int updateStatusById(Map<String, Object> data);

	public int updateStatusByHostId(Map<String, Object> data);
	
	//---------------
	
	public int queryPageCount(String warehouseId);

	public List<CloudHostWarehouseDetailVO> queryPage(String warehouseId);
	
	//---------------
	
	public int deleteById(String hostId);
	
	public int deleteByWarehouseIds(String[] warehouseIds);

	public int deleteByWarehouseIdsAndStatus(Map<String, Object> condition);
}
