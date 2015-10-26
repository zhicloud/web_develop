package com.zhicloud.op.app.helper;

import java.util.ArrayList;
import java.util.List;

import com.zhicloud.op.mybatis.mapper.CloudHostShoppingPortConfigMapper;
import com.zhicloud.op.vo.CloudHostShoppingPortConfigVO;

public class CloudHostShoppingPortConfigHelper
{
	
	public static Integer[] getPortsByConfigId(CloudHostShoppingPortConfigMapper mapper, String configId)
	{
		List<CloudHostShoppingPortConfigVO> ports = mapper.getPortsByConfigId(configId);
		List<Integer> result = new ArrayList<Integer>();
		for( CloudHostShoppingPortConfigVO portVO : ports )
		{
			result.add(portVO.getProtocol());
			result.add(portVO.getPort());
		}
		return result.toArray(new Integer[0]);
	}
	
}
