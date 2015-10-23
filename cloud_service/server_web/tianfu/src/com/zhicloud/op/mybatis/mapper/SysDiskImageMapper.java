package com.zhicloud.op.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.SysDiskImageVO;

public interface SysDiskImageMapper
{
	
	public List<SysDiskImageVO> getAll();
	
	public List<SysDiskImageVO> getAllCommonImage();
	
	public List<SysDiskImageVO> getDiyImage(String userId);
	
	public List<SysDiskImageVO> getImageByUserId(Map<String, Object> condition);

	public int queryPageCount(Map<String, Object> condition);

	public List<SysDiskImageVO> queryPage(Map<String, Object> condition);
	
	
	public Long getMaxSort();
	
	
	public SysDiskImageVO getById(String sysDiskImageId);
	
	public List<SysDiskImageVO> getByRegion(Integer region);
	
	public SysDiskImageVO getByRealImageId(String realImageId);

	//public SysDiskImageVO getBySysDiskImageName(String sysDiskImageName);
	
	public SysDiskImageVO getByRegionAndName(Map<String, Object> condition);
	
	
	//-----------------

	public int addSysDiskImage(Map<String, Object> data);
	
	
	//-------------

	public int updateSysDiskImageById(Map<String, Object> data);
	
	public int updateSysDiskImageByRealImageId(Map<String, Object> data);

	//public int updateUnrelatedSysDiskImageByRealImageName(Map<String, Object> data);
	
	public int updateUnrelatedSysDiskImageByRegionAndName(Map<String, Object> data);
	
	//----------------

	public int deleteSysDiskImageById(String sysDiskImageId);

	public int deleteSysDiskImageByIds(String[] sysDiskImageIds);
	
	public int publishSysDiskImageByIds(String[] sysDiskImageIds);
}
