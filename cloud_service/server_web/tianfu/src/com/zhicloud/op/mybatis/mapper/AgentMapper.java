package com.zhicloud.op.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.AgentVO;


public interface AgentMapper {
		
	public List<AgentVO> getAll();
	
	public int queryPageCount(Map<String, Object> condition);

	public List<AgentVO> queryPage(Map<String, Object> condition);

	public AgentVO getAgentById(String id);
	
	public AgentVO getAgentByUserId(String userId);
	
	public AgentVO getBasicInformationById(String id);

	public AgentVO getAgentByAccount(String account);
	
	public AgentVO getAgentByPhone(String phone);

	public int addAgent(Map<String, Object> data);

	public int updateAgent(Map<String, Object> data);
	
	public int updateEmailById(Map<String, Object> data);
	
	public int deleteAgentById(String userId);
	
	public int deleteAgentByIds(String[] roleId);
	
	public int updateBalanceById(Map<String, Object> data);
	
	public int updatePhoneById(Map<String, Object> data);
	
}
