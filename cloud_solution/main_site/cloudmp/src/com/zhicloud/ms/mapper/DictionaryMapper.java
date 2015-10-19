package com.zhicloud.ms.mapper;

import java.util.List;
 
import java.util.Map;

import com.zhicloud.ms.vo.DictionaryVO;

 
 
public interface DictionaryMapper {
	/**
	 * 
	* @Title: queryValueByCode 
	* @Description: 根据编码查询值
	* @param @param code
	* @param @return      
	* @return List<DictionaryVO>     
	* @throws
	 */
	public List<DictionaryVO> queryValueByCode(String code);
	/**
	 * 
	* @Title: addDictionary 
	* @Description: 新增字典数据
	* @param @param condition
	* @param @return      
	* @return int     
	* @throws
	 */
	public int addDictionary(Map<String, Object> condition);
	/**
	 * 
	* @Title: updateValueById 
	* @Description: 通过ID修改value
	* @param @param condition
	* @param @return      
	* @return int     
	* @throws
	 */
    public int updateValueById(Map<String, Object> condition);


	
	

}
