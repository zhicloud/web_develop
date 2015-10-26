package com.zhicloud.op.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.InviteCodeVO;

public interface InviteCodeMapper 
{
	public int addInviteCode(Map<String, Object> data);
	
	public int updateSendTimeAndStatusAndPhone(Map<String, Object> data);
	
	public int updateSendTimeAndStatusAndEmail(Map<String, Object> data);
	
	public int updateByRegisterTerminalUser(Map<String, Object> data);
	
	public int deleteInviteCodeByIds(List<String> inviteCodeIds);
	
	public int deleteInviteByCreaterId(List<String> createrIds);
	
	//---------------
	
	public int                 queryPageCount(Map<String, Object> condition);
	public List<InviteCodeVO>  queryPage(Map<String, Object> condition);
	
	//----------------
	
	public InviteCodeVO getInviteCodeById(String id);
	
	public InviteCodeVO getInviteCodeByCode(String code);
}
