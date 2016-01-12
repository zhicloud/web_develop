package com.zhicloud.op.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.TerminalUserVO;

public interface TerminalUserMapper
{

	public int terminalUserCount(String agentId);
	public int newTerminalUserCount(Map<String, Object> condition);


	public int                  queryPageCount(Map<String, Object> condition);
	public List<TerminalUserVO> queryPage(Map<String, Object> condition);
	
	public List<TerminalUserVO> getAll();
	
	public List<TerminalUserVO> getTerminalUserFromAgent(String agentId);
	
	public TerminalUserVO getTerminalUserById(String id);
	
	public TerminalUserVO getBaseInfoById(String id);

	public TerminalUserVO getTerminalUserByAccount(String account);
	
	public TerminalUserVO getTerminalUserByAccountForgetPassword(String account);
	
	public TerminalUserVO getTerminalUserByPhone(String phone);
	
	public TerminalUserVO getTerminalUserByEmail(String email);

	public TerminalUserVO getTerminalUserByName(String name);
	
	public TerminalUserVO getTerminalUserByIdCard(String idCard);
	
	public TerminalUserVO getBalanceById(String id);
	
	public TerminalUserVO getTerminalUserByAccountToAgentApi(Map<String, Object> data);
	
	public TerminalUserVO getUserByName(String name);

	//--------------------
	
	public int addTerminalUser(Map<String, Object> data);

	//--------------------
	
	public int updateTerminalUser(Map<String, Object> data);
	
	public int updateBalanceById(Map<String, Object> data);
	
	public int updateTerminalUserBaseInfo(Map<String, Object> data);
	
	public int updateTerminalUserBaseInfoEmail(Map<String, Object> data);
	
	public int updateTerminalUserBaseInfoPhone(Map<String, Object> data); 
	
	public int updateAccountBalanceById(Map<String, Object> data);

	//--------------------
	
	public int deleteTerminalUserById(String userId);

	public int deleteTerminalUserByIds(List<String> terminalUserIds);
	
	public int activaTerminalUserByStr(Map<String, Object> data);
	
	public int changePasswordByMail(Map<String, Object> data);
	
	//-------------------
	public int newTerminalUserCountWeek(Map<String, Object> condition);
	
	public int newTerminalUserCountMonth(Map<String, Object> condition);
	
	public int newTerminalUserCountYear(Map<String, Object> condition);
	
	public int getTerminalUserCountByTime(Map<String, Object> condition);
	
	/**
	 * 查询所有或近七天的欠费用户
	 * @param condition
	 * @return
	 */
	public int getAllOrSevenDaysCount(Map<String,Object> condition);
	
}
