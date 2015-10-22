package com.zhicloud.op.vo;

import com.zhicloud.op.common.util.json.JSONBean;

public class SysRoleGroupRelationVO implements JSONBean
{

	private String id;
	private String roleId;
	private String groupId;
	
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
	public String getGroupId()
	{
		return groupId;
	}
	public void setGroupId(String groupId)
	{
		this.groupId = groupId;
	}
	
	
	
}
