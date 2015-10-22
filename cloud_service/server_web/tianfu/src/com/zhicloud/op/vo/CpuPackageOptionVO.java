package com.zhicloud.op.vo;

import com.zhicloud.op.common.util.json.JSONBean;

public class CpuPackageOptionVO implements JSONBean
{

	private String id;
	private Integer core;
	private Integer sort;
	
	
	
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public Integer getCore()
	{
		return core;
	}
	public void setCore(Integer core)
	{
		this.core = core;
	}
	public Integer getSort()
	{
		return sort;
	}
	public void setSort(Integer sort)
	{
		this.sort = sort;
	}

	
	
	
}
