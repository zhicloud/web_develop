package com.zhicloud.op.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.CloudTerminalBindingVO;

public interface CloudTerminalBindingMapper {

	public List<CloudTerminalBindingVO> getCloudTerminalId(Map<String, Object> condition);
	
	public List<CloudTerminalBindingVO> getCloudTerminalBycloudTerminalId(Map<String, Object> condition);
	
	public int addCloudTerminalBinding(Map<String, Object> data);
	
	public int deleteByUserIdAndHostId(Map<String, Object> condition);
	
}
