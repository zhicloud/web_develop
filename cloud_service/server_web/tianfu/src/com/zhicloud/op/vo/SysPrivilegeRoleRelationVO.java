package com.zhicloud.op.vo;

import com.zhicloud.op.common.util.json.JSONBean;

public class SysPrivilegeRoleRelationVO implements JSONBean
{

	private String id;
	private String roleId;
	private String privilegeId;
	
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getRoleId()
	{
		return roleId;
	}
	public void setRoleId(String roleId)
	{
		this.roleId = roleId;
	}
	public String getPrivilegeId()
	{
		return privilegeId;
	}
	public void setPrivilegeId(String privilegeId)
	{
		this.privilegeId = privilegeId;
	}
	
}
