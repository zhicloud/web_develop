package com.zhicloud.op.mybatis.mapper;

import java.util.Map;

import com.zhicloud.op.vo.UserDictionaryVO;

public interface UserDictionaryMapper {
	
	public UserDictionaryVO getUserDictionaryByKey(Map<String, Object> condition);
	public int addUserDictionary(Map<String, Object> condition);
	public int deleteDictionaryByUserId(Map<String, Object> condition);
	public int getByUserId(Map<String, Object> condition);
}
