package com.zhicloud.ms.service;

import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.vo.CloudHostConfigModel;

import java.util.List;

public interface CloudHostConfigModelService {
	public List<CloudHostConfigModel> getAll();
	public String addType(CloudHostConfigModel chcm);
	public MethodResult deleteType(String id);
	public CloudHostConfigModel getById(String id);
	public int updateType(String id,CloudHostConfigModel chcm);
	/**
	 * 
	 * getCloudHostConfigModelServiceByImageId:根据镜像Id查询主机类型
	 *
	 * @author sasa
	 * @param imageId
	 * @return List<CloudHostConfigModel>
	 * @since JDK 1.7
	 */
	public List<CloudHostConfigModel> getCloudHostConfigModelServiceByImageId(String imageId);
	/**
	 * 
	 * getCloudHostConfigModelByName:根据类型名称获取类型
	 *
	 * @author sasa
	 * @param name
	 * @return CloudHostConfigModel
	 * @since JDK 1.7
	 */
	public CloudHostConfigModel getCloudHostConfigModelByName(String name);
	
	/**
	 * 查询所有云服务器配置
	 * @return
	 */
	public List<CloudHostConfigModel> getAllServer();
	
	/**
	 * 添加服务器配置类型
	 * @param chcm
	 * @return
	 */
	public MethodResult addServerType(CloudHostConfigModel chcm);
	
	/**
	 * 修改服务器配置类型
	 * @param id
	 * @param chcm
	 * @return
	 */
	public MethodResult updateServerType(String id,CloudHostConfigModel chcm);
}
