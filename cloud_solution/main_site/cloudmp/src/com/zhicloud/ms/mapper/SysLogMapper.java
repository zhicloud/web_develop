package com.zhicloud.ms.mapper; 

import java.util.List;
import java.util.Map;

import com.zhicloud.ms.vo.SysLogVO;


public interface SysLogMapper {
	/**
	 * 查询所有操作日志
	 * @return
	 */
	public List<SysLogVO> getAll(Map<String,Object> condition);
	
	/**
	 * 通过userId查询操作日志
	 * @param userId
	 * @return
	 */
	public List<SysLogVO> getByUserId(String userId);
	
	/**
	 * 添加一条操作日志
	 * @param condition
	 * @return
	 */
	public int add(Map<String,Object> condition);
	
	/**
	 * 通过ID删除操作日志
	 * @param id
	 * @return
	 */
	public int deleteById(String id);
	
	/**
	 * 批量删除操作日志
	 * @param ids
	 * @return
	 */
	public int deleteIds(List<String> ids);
}

