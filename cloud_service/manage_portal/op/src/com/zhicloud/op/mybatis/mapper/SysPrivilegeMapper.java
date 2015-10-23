package com.zhicloud.op.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.SysPrivilegeVO;

public interface SysPrivilegeMapper
{

	public List<SysPrivilegeVO> getAll();
	
	public SysPrivilegeVO getByPrivilegeCode(String privilegeCode);
	
	public List<String> getPrivilegeCodesByUserId(String userId);
	
	public int addSysPrivilege(Map<String, Object> data);
	
	public int updateSysPrivilegeById(Map<String, Object> data);
	
	public int deleteByIds(String[] ids);
	
	public int deleteByCodesNotIn(String[] codes);
	
	
}
