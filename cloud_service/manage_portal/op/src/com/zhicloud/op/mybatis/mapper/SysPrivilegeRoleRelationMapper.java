package com.zhicloud.op.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.SysPrivilegeRoleRelationVO;

public interface SysPrivilegeRoleRelationMapper
{

	public List<SysPrivilegeRoleRelationVO> getByRoleId(String roleId);

	public int add(Map<String, Object> data);
	
	public int deleteByRoleId(String roleId);

	public int deleteUselessRecords();
	
}
