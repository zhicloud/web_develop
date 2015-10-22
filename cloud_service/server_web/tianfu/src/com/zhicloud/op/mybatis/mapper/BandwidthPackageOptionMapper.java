package com.zhicloud.op.mybatis.mapper;

import java.util.Map;

import com.zhicloud.op.vo.BandwidthPackageOptionVO;

public interface BandwidthPackageOptionMapper
{

	public BandwidthPackageOptionVO getOne();

	// -----------------
	
	public int addBandwidthPackageOption(Map<String, Object> data);

	public int updateBandwidthPackageOption(Map<String, Object> data);

}
