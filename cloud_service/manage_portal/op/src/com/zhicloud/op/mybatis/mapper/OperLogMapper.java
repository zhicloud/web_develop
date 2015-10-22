package com.zhicloud.op.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.OperLogVO;
 

public interface OperLogMapper {
	public int addOperLog(Map<String, Object> data);
	
	public int queryPageCount(Map<String, Object> condition);
	
	public List<OperLogVO> queryPage(Map<String, Object> condition);

}
