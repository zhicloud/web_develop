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
	
	public int updateStatusByUserId(Map<String, Object> cloudDiskData);
	
	public int updateStatusById(Map<String, Object> cloudDiskData);
	
	public List<CloudDiskVO> getCloudDiskForRegion(Map<String, Object> cloudDiskData);
	
	/**
	 * 查询所有云硬盘数量
	 * 
	 * @param condition
	 * @return
	 */
	public int queryAllCount(Map<String, Object> condition);
	
	/**
	 * 查询所有云主机信息
	 * 
	 * @param condition
	 * @return
	 */
	public List<CloudDiskVO> queryAllCloudDisk(Map<String, Object> condition);
	
	/**
	 * 
	 * 逻辑删除云硬盘
	 * @param ids
	 * @return
	 */
	public int logicDeleteDiskByIds(List<String> ids);
}
