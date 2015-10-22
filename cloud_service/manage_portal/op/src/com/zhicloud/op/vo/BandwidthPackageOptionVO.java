package com.zhicloud.op.vo;

import java.math.BigInteger;

import com.zhicloud.op.common.util.json.JSONBean;

public class BandwidthPackageOptionVO implements JSONBean
{

	private String id;
	private BigInteger min;
	private BigInteger max;
	
	
	public BandwidthPackageOptionVO()
	{
		
	}
	
	public BandwidthPackageOptionVO(BigInteger min, BigInteger max)
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
