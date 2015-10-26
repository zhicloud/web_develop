package com.zhicloud.op.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.CloudHostSysDefaultPortsVO;

public interface CloudHostSysDefaultPortsMapper {
	
	public List<CloudHostSysDefaultPortsVO> getAllPorts();
	
	public CloudHostSysDefaultPortsVO getByProtocolAndPort(Map<String, Object> condition);

}
