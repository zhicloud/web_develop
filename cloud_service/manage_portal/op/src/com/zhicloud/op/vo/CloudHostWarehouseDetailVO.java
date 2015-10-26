package com.zhicloud.op.vo;

import java.math.BigInteger;

import com.zhicloud.op.common.util.json.JSONBean;

public class CloudHostWarehouseDetailVO implements JSONBean
{

	private String id;
	private String warehouseId;
	private String hostId;
	private Integer status;
	private String processMessage;
	

	private String realHostId;
	private String hostName;
	private String createTime;
	private Integer cpuCore;
	private BigInteger memory;
	private String sysImageId;
	private BigInteger sysDisk;
	private BigInteger dataDisk;
	private BigInteger bandwidth;
	private String innerIp;
	private String outerIp;
	private Integer region;
	private String sysImageName;
	
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getWarehouseId()
	{
		return warehouseId;
	}
	public void setWarehouseId(String warehouseId)
	{
		this.warehouseId = warehouseId;
	}
	public String getHostId()
	{
		return hostId;
	}
	public void setHostId(String hostId)
	{
		this.hostId = hostId;
	}
	
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public Integer getStatus()
	{
		return status;
	}
	public void setStatus(Integer status)
	{
		this.status = status;
	}
	public String getProcessMessage()
	{
		return processMessage;
	}
	public void setProcessMessage(String processMessage)
	{
		this.processMessage = processMessage;
	}
	public String getRealHostId()
	{
		return realHostId;
	}
	public void setRealHostId(String realHostId)
	{
		this.realHostId = realHostId;
	}
	public String getHostName()
	{
		return hostName;
	}
	public void setHostName(String hostName)
	{
		this.hostName = hostName;
	}
	public BigInteger getMemory()
	{
		return memory;
	}
	public void setMemory(BigInteger memory)
	{
		this.memory = memory;
	}
	public Integer getCpuCore()
	{
		return cpuCore;
	}
	public void setCpuCore(Integer cpuCore)
	{
		this.cpuCore = cpuCore;
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
	public String getInnerIp()
	{
		return innerIp;
	}
	public void setInnerIp(String innerIp)
	{
		this.innerIp = innerIp;
	}
	public String getOuterIp()
	{
		return outerIp;
	}
	public void setOuterIp(String outerIp)
	{
		this.outerIp = outerIp;
	}
	public Integer getRegion()
	{
		return region;
	}
	public void setRegion(Integer region)
	{
		this.region = region;
	}
	public String getSysImageName() {
		return sysImageName;
	}
	public void setSysImageName(String sysImageName) {
		this.sysImageName = sysImageName;
	}
	
	
}
