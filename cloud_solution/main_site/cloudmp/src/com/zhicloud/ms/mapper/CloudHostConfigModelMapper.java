package com.zhicloud.ms.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.ms.vo.CloudHostConfigModel;

public interface CloudHostConfigModelMapper {
	public List<CloudHostConfigModel> getAll();
	public int addType(Map<String,Object> condition);
	public void deleteType(String id);
	public CloudHostConfigModel getById(String id);
	public int updateById(Map<String,Object> condition);
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
	 * getCloudHostConfigModelByName:根据类型名称获取一类型
	 * TODO(这里描述这个方法的注意事项 – 可选). 
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
}