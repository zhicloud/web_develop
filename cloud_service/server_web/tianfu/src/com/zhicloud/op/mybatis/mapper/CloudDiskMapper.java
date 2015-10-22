package com.zhicloud.op.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.CloudDiskVO;

public interface CloudDiskMapper {

	public int queryPageCount(Map<String, Object> condition);
	
	public List<CloudDiskVO> queryPage(Map<String, Object> condition);
	
	public List<CloudDiskVO> getCloudDiskByUserId(String userId);
	
	public CloudDiskVO getCloudDiskById(String id);
	
	public int addCloudDisk(Map<String, Object> data);
	
	public int updateCloudDisk(Map<String, Object> data);
	
	public int deleteCloudDiskById(String id);
	
	public int updateStatusById(Map<String, Object> cloudDiskData);
	
	public List<CloudDiskVO> getCloudDiskForRegion(Map<String, Object> cloudDiskData);
}
