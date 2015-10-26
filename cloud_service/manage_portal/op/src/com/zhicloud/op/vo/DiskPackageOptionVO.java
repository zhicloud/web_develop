package com.zhicloud.op.vo;

import java.math.BigInteger;

import com.zhicloud.op.common.util.json.JSONBean;

public class DiskPackageOptionVO implements JSONBean
{

	private String id;
	private BigInteger min = new BigInteger("0");
	private BigInteger max = new BigInteger("0");
	
	public DiskPackageOptionVO()
	{
		
	}
	
	public DiskPackageOptionVO(BigInteger min, BigInteger max)
	{
		this.min = min;
		this.max = max;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public BigInteger getMin()
	{
		return min;
	}

	public void setMin(BigInteger min)
	{
		this.min = min;
	}

	public BigInteger getMax()
	{
		return max;
	}

	public void setMax(BigInteger max)
	{
		this.max = max;
	}

}
