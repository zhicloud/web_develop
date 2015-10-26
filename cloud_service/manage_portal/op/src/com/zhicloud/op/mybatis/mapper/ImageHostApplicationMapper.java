package com.zhicloud.op.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.ImageHostApplicationVO;

public interface ImageHostApplicationMapper {
	/**
	 *
	* @Title: addImageHostApplication 
	* @Description:  添加蛋壳计划信息
	* @param @param data
	* @param @return      
	* @return int     
	* @throws
	 */
	public int addImageHostApplication(Map<String, Object> data);
	/**
	 * 
	* @Title: updateImageHostApplication 
	* @Description:更新蛋壳计划状态
	* @param @param data
	* @param @return      
	* @return int     
	* @throws
	 */
	public int updateImageHostApplication(Map<String, Object> data);
	/**
	 * 
	* @Title: queryImageHostApplication 
	* @Description: 查询蛋壳计划信息 
	* @param @param data
	* @param @return      
	* @return List<ImageHostApplication>     
	* @throws
	 */
	public List<ImageHostApplicationVO> queryImageHostApplication(Map<String, Object> data);
	/**
	 * 
	* @Title: queryImageHostApplicationCount 
	* @Description: 查询数量
	* @param @param data
	* @param @return      
	* @return int     
	* @throws
	 */
	public int queryImageHostApplicationCount(Map<String, Object> data);
	/**
	 * 
	* @Title: queryImageHostApplicationById 
	* @Description: 根据id查询申请详情 
	* @param @param id
	* @param @return      
	* @return ImageHostApplicationVO     
	* @throws
	 */
	public ImageHostApplicationVO queryImageHostApplicationById(String id);
	

}
