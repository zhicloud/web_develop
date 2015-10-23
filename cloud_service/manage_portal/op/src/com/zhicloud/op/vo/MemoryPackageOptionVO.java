package com.zhicloud.op.vo;

import java.math.BigInteger;

import com.zhicloud.op.common.util.json.JSONBean;

public class MemoryPackageOptionVO implements JSONBean
{

	private String id;
	private BigInteger memory;
	private String label;
	private BigInteger sort;

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public BigInteger getMemory()
	{
		return memory;
	}

	public void setMemory(BigInteger memory)
	{
		this.memory = memory;
	}

	public String getLabel()
	{
		return label;
	}

	public void setLabel(String label)
	{
		this.label = label;
	}

	public BigInteger getSort()
	{
		return sort;
	}

	public void setSort(BigInteger sort)
	{
		this.sort = sort;
	}


}
