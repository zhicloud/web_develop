package com.zhicloud.op.mybatis.mapper;

import java.util.Map;

import com.zhicloud.op.vo.CloudHostShoppingConfigVO;

public interface CloudHostShoppingConfigMapper
{
	
	public int addCloudHostShoppingConfig(Map<String, Object> condition);
	
	public CloudHostShoppingConfigVO getOneUncreatedCloudHostConfigByOrderIdAndRegion(Map<String, Object> condition);
	
	public int updateProcessStatusById(Map<String, Object> data);
	
	public int updateProcessStatusByHostId(Map<String, Object> data);

	public int updateProcessStatusByHostName(Map<String, Object> data);
	
	public int deleteByHostId(String hostId);
	
}
