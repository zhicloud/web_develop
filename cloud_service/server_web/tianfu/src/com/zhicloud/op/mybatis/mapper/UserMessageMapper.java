package com.zhicloud.op.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.UserMessageVO;

public interface UserMessageMapper {
	
	public List<UserMessageVO> getUserMessage(Map<String, Object> condition);
	
	public int getUserMessageCount(Map<String, Object> condition);
	
	public UserMessageVO getUserInitPasswordMessage(String id);
	
	//=====================================================
	
	public int insertReg(Map<String, Object> condition);
	
	public int deleteUserMessageByStatus(Map<String, Object> condition);
	
	public int deleteUserMessage(String id);
}
