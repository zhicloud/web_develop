package com.zhicloud.ms.mapper; 

import java.util.List;
import java.util.Map;

import com.zhicloud.ms.vo.ClientMessageVO;


public interface ClientMessageMapper {
	
	/**
	 * 查询所有反馈信息
	 * @return
	 */
	public List<ClientMessageVO> getAll(Map<String,Object> condition);
	
	/**
	 * 添加反馈信息
	 * @param condition
	 * @return
	 */
	public int add(Map<String,Object> condition);
	
	/**
	 * 批量删除反馈信息
	 * @param ids
	 * @return
	 */
	public int deleteIds(List<String> ids);
	
	/**
	 * 批量标记为已读
	 * @param ids
	 * @return
	 */
	public int markRead(List<String> ids);
}

