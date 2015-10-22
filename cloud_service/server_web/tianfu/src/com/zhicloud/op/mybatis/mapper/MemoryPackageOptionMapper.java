package com.zhicloud.op.mybatis.mapper;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.MemoryPackageOptionVO;


public interface MemoryPackageOptionMapper
{
	
	public List<MemoryPackageOptionVO> getAll();
	public MemoryPackageOptionVO getByMemory(BigInteger memory);
	
	public int addMemoryPackageOption(Map<String, Object> condition);
	public int  deleteMemoryPackageOption(String memoryId);
	
}
