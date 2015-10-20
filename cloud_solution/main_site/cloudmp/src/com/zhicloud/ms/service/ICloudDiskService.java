package com.zhicloud.ms.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.vo.CloudDisk;


public interface ICloudDiskService {
	/**
	 * 
	 * getAllCloudDisk:获取所有云硬盘 .  
	 * @author wildBear
	 * @return List<CloudDisk>
	 * @since JDK 1.7
	 */
	public List<CloudDisk> getAllCloudDisk(CloudDisk parameter);

	
	/**
	 * 
	 * addCloudDisk:添加云硬盘.  
	 * @author wildBear
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	public MethodResult addCloudDisk(CloudDisk disk, HttpServletRequest request);

	
	/**
	 * 
	 * delDisk:删除云硬盘.  
	 * @author wildBear
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	public MethodResult delCloudDisk(String id);

	/**
	 * 
	 * changeDiskStatus:修改云硬盘状态.  
	 * @author wildBear
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	public MethodResult changeDiskStatus(String id, Integer status,
			HttpServletRequest request);

	/**
	 * 
	 * selectCloudDiskById:根据ID返回云硬盘信息.  
	 * @author wildBear
	 * @return CloudDisk
	 * @since JDK 1.7
	 */
	public CloudDisk selectCloudDiskById(String id);
	
	
	/**
	 * 
	 * updateCloudDisk:  保存修改云硬盘信息.  
	 * @author wildBear
	 * @return CloudDisk
	 * @since JDK 1.7
	 */
	public MethodResult updateCloudDisk(CloudDisk disk,
			HttpServletRequest request);
	
} 
