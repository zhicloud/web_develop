package com.zhicloud.op.vo;

import com.zhicloud.op.common.util.json.JSONBean;

public class SysPrivilegeVO implements JSONBean
{

	private String id;
	private String privilegeCode;
	private Integer type;
	private Integer level;
	private String name;
	private String parentId;
	private Integer sortNum;
	
	
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getPrivilegeCode()
	{
		return privilegeCode;
	}
	public void setPrivilegeCode(String privilegeCode)
	{
		this.privilegeCode = privilegeCode;
	}
	public Integer getType()
	{
		return type;
	}
	public void setType(Integer type)
	{
		this.type = type;
	}
	public Integer getLevel()
	{
		return level;
	}
	public void setLevel(Integer level)
	{
		this.level = level;
	}
	public String getName()
	{
		return name;
	}
	public void setName(String name)
	{
		this.name = name;
	}
	public String getParentId()
	{
		return parentId;
	}
	public void setParentId(String parentId)
	{
		this.parentId = parentId;
	}
	public Integer getSortNum()
	{
		return sortNum;
	}
	public void setSortNum(Integer sortNum)
	{
		this.sortNum = sortNum;
	}
	
	
	
}
