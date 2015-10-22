package com.zhicloud.op.mybatis.mapper;

import java.util.Map;



public interface SysRoleGroupRelationMapper
{
	

	public int addSysRoleGroupRelation(Map<String, Object> data);

	public int deleteSysRoleGroupRelationByGroupId(String groupId);
	
	
	
}
