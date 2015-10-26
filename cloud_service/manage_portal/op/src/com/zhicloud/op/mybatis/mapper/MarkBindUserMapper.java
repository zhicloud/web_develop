package com.zhicloud.op.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.MarkBindUserVO;
 

public interface MarkBindUserMapper {
	/**
	 * 添加一条绑定记录
	 * 
	 * @param condition
	 * @return
	 */
	public int addBindItem(Map<String,Object> condition);
	
	/**
	 * 根据userId查询绑定记录
	 * 
	 * @param userId
	 * @return
	 */
	public MarkBindUserVO queryByUserId(String userId);
	
	/**
	 * 根据userId修改绑定信息
	 * @param condition
	 * @return
	 */
	public int updateByUserId(Map<String,Object> condition);
	
	/**
	 * 
	 * 根据userId删除绑定信息
	 * @param userId
	 * @return
	 */
	public int deleteByUserId(String userId);
	
	/**
	 * 根据标签Id批量删除关联记录
	 * @param markIds
	 * @return
	 */
	public int deleteByMarkIds(List<String> markIds);
	
	/**
	 * 根据userId批量删除关联记录
	 * @param markIds
	 * @return
	 */
	public int deleteByUserIds(List<String> userIds);
}
