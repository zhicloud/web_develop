package com.zhicloud.op.vo;

import com.zhicloud.op.common.util.json.JSONBean;

public class SysRoleVO implements JSONBean
{

	private String id;
	private String roleName;
	
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getRoleName()
	{
		return roleName;
	}
	public void setRoleName(String roleName)
	{
		this.roleName = roleName;
	}
	
}
