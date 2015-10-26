package com.zhicloud.op.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.PackagePriceVO;
import com.zhicloud.op.vo.PriceVO;


public interface PackagePriceMapper {
		
	public List<PackagePriceVO> getPackagePrice(Map<String, Object> condition);
	
	public List<PackagePriceVO> getAllPackagePrice(Map<String, Object> condition);
	
	public List<PackagePriceVO> queryPackagePrice(String type);
	
	public PackagePriceVO getById(String id);

	public PackagePriceVO getByRegionAndTypeAndOption(Map<String, Object> condition);
	
	public int queryCount(Map<String, Object> condition); 
	
	public int addPackagePrice(Map<String, Object> data);

	public int updatePackagePrice(Map<String, Object> data);
	
	public int deleteById(String id);
	
	public int updateAllDiskPrice(Map<String, Object> data);
	
	public int addPrice(Map<String,Object> condition);
	
	public int deletepriceByInfoId(String infoId);
	
	public List<PriceVO> getPriceByInfoId(String id);
}
