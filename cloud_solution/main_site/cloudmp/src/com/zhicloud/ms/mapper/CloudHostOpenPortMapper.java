package com.zhicloud.ms.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.ms.vo.CloudHostOpenPortVO;

 

public interface CloudHostOpenPortMapper
{
	
	public List<CloudHostOpenPortVO> getByHostId(String hostId);

	public CloudHostOpenPortVO getOneRecordByCondition(Map<String, Object> condition);
	
	//----------------
	
	public int addCloudHostOpenPort(Map<String, Object> data);
	
	//------------
	
	public int updateMappingPortsById(Map<String, Object> data);
	
	//-----------------------

	public int deleteById(String id);
	
	public int deleteByHostId(String hostId);

}
