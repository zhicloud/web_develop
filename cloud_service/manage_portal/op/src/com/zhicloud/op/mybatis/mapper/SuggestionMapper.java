package com.zhicloud.op.mybatis.mapper;

import java.util.List; 
import java.util.Map;

import com.zhicloud.op.vo.SuggestionVO;

public interface SuggestionMapper {
	public List<SuggestionVO> queryPage(Map<String, Object> condition);
	public List<SuggestionVO> queryPageAll(Map<String, Object> condition);
	public int queryPageCount(Map<String, Object> condition);
	public SuggestionVO getSuggestionById(Map<String, Object> condition);
	public int addSuggestion(Map<String, Object> condition);
	public int updateSuggestion(Map<String, Object> condition);

}
