package com.zhicloud.op.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.CloudHostShoppingPortConfigVO;


public interface CloudHostShoppingPortConfigMapper
{

	public int inserIntoConfigPort(Map<String, Object> condition);
	
	public int deletePortsByConfigId(String configId);
	
	public List<CloudHostShoppingPortConfigVO> getPortsByConfigId(String configId);
	
}
