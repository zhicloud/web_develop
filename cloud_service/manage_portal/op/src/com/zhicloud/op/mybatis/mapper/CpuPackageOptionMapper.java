package com.zhicloud.op.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.CpuPackageOptionVO;


public interface CpuPackageOptionMapper
{
	
	public List<CpuPackageOptionVO> getAll();
	
	public CpuPackageOptionVO getByCore(String core);
	
	public int addCpuPackageOption(Map<String, Object> condition);
	public int  deleteCpuPackageOption(String coreId);
	
}
