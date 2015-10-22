package com.zhicloud.op.mybatis.mapper;

import java.util.Map;

import com.zhicloud.op.vo.DiskPackageOptionVO;

public interface DiskPackageOptionMapper
{

	public DiskPackageOptionVO getOne();

	// ---------------------------
	
	public int addDiskPackageOption(Map<String, Object> data);

	public int updateDiskPackageOption(Map<String, Object> data);
}
