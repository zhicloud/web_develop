package com.zhicloud.op.vo;

import java.math.BigInteger;

public class CloudHostShoppingConfigVO
{
	
	private String id;   
	private String userId;   
	private String hostId;   
	private Integer type;  
	private Integer cpuCore;  
	private BigInteger memory;  
	private String sysImageId;   
	private BigInteger sysDisk;  
	private BigInteger dataDisk;  
	private BigInteger bandwidth;  
	private Integer duration;  
	private String startTime;   
	private String endTime;   
	private BigInteger price;  
	private String createTime;   
	private Integer isHostCreated;  
	private String hostName;
	private Integer region;
   
   
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getUserId()
	{
		return userId;
	}
	public void setUserId(String userId)
	{
		this.userId = userId;
	}
	public String getHostId()
	{
		return hostId;
	}
	public void setHostId(String hostId)
	{
		this.hostId = hostId;
	}
	public Integer getType()
	{
		return type;
	}
	public void setType(Integer type)
	{
		this.type = type;
	}
	public Integer getCpuCore()
	{
		return cpuCore;
	}
	public void setCpuCore(Integer cpuCore)
	{
		this.cpuCore = cpuCore;
	}
	public BigInteger getMemory()
	{
		return memory;
	}
	public void setMemory(BigInteger memory)
	{
		this.memory = memory;
	}
	public String getSysImageId()
	{
		return sysImageId;
	}
	public void setSysImageId(String sysImageId)
	{
		this.sysImageId = sysImageId;
	}
	public BigInteger getSysDisk()
	{
		return sysDisk;
	}
	public void setSysDisk(BigInteger sysDisk)
	{
		this.sysDisk = sysDisk;
	}
	public BigInteger getDataDisk()
	{
		return dataDisk;
	}
	public void setDataDisk(BigInteger dataDisk)
	{
		this.dataDisk = dataDisk;
	}
	public BigInteger getBandwidth()
	{
		return bandwidth;
	}
	public void setBandwidth(BigInteger bandwidth)
	{
		this.bandwidth = bandwidth;
	}
	public Integer getDuration()
	{
		return duration;
	}
	public void setDuration(Integer duration)
	{
		this.duration = duration;
	}
	public String getStartTime()
	{
		return startTime;
	}
	public void setStartTime(String startTime)
	{
		this.startTime = startTime;
	}
	public String getEndTime()
	{
		return endTime;
	}
	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}
	public BigInteger getPrice()
	{
		return price;
	}
	public void setPrice(BigInteger price)
	{
		this.price = price;
	}
	public String getCreateTime()
	{
		return createTime;
	}
	public void setCreateTime(String createTime)
	{
		this.createTime = createTime;
	}
	public Integer getIsHostCreated()
	{
		return isHostCreated;
	}
	public void setIsHostCreated(Integer isHostCreated)
	{
		this.isHostCreated = isHostCreated;
	}
	public String getHostName()
	{
		return hostName;
	}
	public void setHostName(String hostName)
	{
		this.hostName = hostName;
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
