package com.zhicloud.ms.mapper; 

import com.zhicloud.ms.vo.OperLogVO;

import java.util.List;
import java.util.Map;


public interface OperLogMapper {
	/**
	 * 查询所有操作日志
	 * @return
	 */
	public List<OperLogVO> getAll(Map<String,Object> condition);

    /**
     * 分页查询所有操作日志
     * @return
     */
    public List<OperLogVO> queryByPage(Map<String,Object> condition);

    /**
     * 查询所有操作日志条数
     * @return
     */
    public int queryPageCount(Map<String,Object> condition);
	
	/**
	 * 通过userId查询操作日志
	 * @param userId
	 * @return
	 */
	public List<OperLogVO> getByUserId(String userId);
	
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

