package com.zhicloud.op.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.MarkVO;
 

public interface MarkMapper {
	/**
	 * 添加标签
	 * @param data
	 * @return
	 */
	public int addMark(Map<String, Object> data);
	
	/**
	 * 查询标签总数
	 * @param condition
	 * @return
	 */
	public int queryPageCount(Map<String, Object> condition);
	
	/**
	 * 查询所有标签
	 * @param condition
	 * @return
	 */
	public List<MarkVO> queryPage(Map<String, Object> condition);
	
	/**
	 * 按标签名查询标签
	 * @param name
	 * @return
	 */
	public MarkVO getMarkByName(String name);
	
	/**
	 * 按标签id查询标签
	 * @param id
	 * @return
	 */
	public MarkVO getMarkById(String id);
	
	/**
	 * 按Id更新标签
	 * @param condition
	 * @return
	 */
	public int updateMarkById(Map<String,Object> condition);
	
	/**
	 * 批量删除标签
	 * 
	 * @param ids
	 * @return
	 */
	public int deleteByMarkIds(List<String> ids);
	
	/**
	 * 
	 * 查询所有标签
	 * @return
	 */
	public List<MarkVO> getAll();

}
