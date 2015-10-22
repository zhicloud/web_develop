package com.zhicloud.op.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.EggPlanVO;

public interface EggPlanMapper {
	/**
	 *
	* @Title: addEggPlan 
	* @Description:  添加蛋壳计划信息
	* @param @param data
	* @param @return      
	* @return int     
	* @throws
	 */
	public int addEggPlan(Map<String, Object> data);
	/**
	 * 
	* @Title: updateEggPlan 
	* @Description:更新蛋壳计划状态
	* @param @param data
	* @param @return      
	* @return int     
	* @throws
	 */
	public int updateEggPlan(Map<String, Object> data);
	/**
	 * 
	* @Title: queryEggPlan 
	* @Description: 查询蛋壳计划信息 
	* @param @param data
	* @param @return      
	* @return List<EggPlan>     
	* @throws
	 */
	public List<EggPlanVO> queryEggPlan(Map<String, Object> data);
	/**
	 * 
	* @Title: queryEggPlanCount 
	* @Description: 查询数量
	* @param @param data
	* @param @return      
	* @return int     
	* @throws
	 */
	public int queryEggPlanCount(Map<String, Object> data);
	/**
	 * 
	* @Title: queryEggPlanById 
	* @Description: 根据id查询申请详情 
	* @param @param id
	* @param @return      
	* @return EggPlanVO     
	* @throws
	 */
	public EggPlanVO queryEggPlanById(String id);
	

}
