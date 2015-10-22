package com.zhicloud.op.vo;

import java.math.BigInteger;

import com.zhicloud.op.common.util.json.JSONBean;

public class CloudHostWarehouseDefinitionVO implements JSONBean
{

	private String id;
	private String sysImageId;
	private String sysImageName;
	private BigInteger dataDisk;
	private String description;
	private Integer totalAmount;
	private Integer remainAmount;
	private Integer region;
	
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getSysImageId()
	{
		return sysImageId;
	}
	public void setSysImageId(String sysImageId)
	{
		this.sysImageId = sysImageId;
	}
	
	public String getSysImageName() {
		return sysImageName;
	}
	public void setSysImageName(String sysImageName) {
		this.sysImageName = sysImageName;
	}
	public BigInteger getDataDisk()
	{
		return dataDisk;
	}
	public void setDataDisk(BigInteger dataDisk)
	{
		this.dataDisk = dataDisk;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public Integer getTotalAmount()
	{
		return totalAmount;
	}
	public void setTotalAmount(Integer totalAmount)
	{
		this.totalAmount = totalAmount;
	}
	public Integer getRemainAmount()
	{
		return remainAmount;
	}
	public void setRemainAmount(Integer remainAmount)
	{
		this.remainAmount = remainAmount;
	}
	public Integer getRegion()
	{
		return region;
	}
	public void setRegion(Integer region)
	{
		this.region = region;
	}
	
	
}
