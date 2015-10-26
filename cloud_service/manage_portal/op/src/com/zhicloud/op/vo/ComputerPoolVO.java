package com.zhicloud.op.vo;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.zhicloud.op.common.util.json.JSONBean;

public class ComputerPoolVO implements JSONBean{
	private String uuid;//资源池ID
	private String name;//资源池名
	private Integer[] node;//资源节点数[停止,告警,故障,正常]
	private Integer[] host;//主机数[停止,告警,故障,正常]
	private Integer cpuCount;//CPU核心数
	private BigDecimal cpuUsage;//CPU利用率
	private BigInteger[] memory;//内存
	private BigDecimal memoryUsage;//内存使用率
	private BigInteger[] diskVolume;//磁盘容量[可用，总量]
	private BigDecimal diskUsage;//磁盘使用率
	private int status;//资源池状态
	private int region;
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer[] getNode() {
		return node;
	}
	public void setNode(Integer[] node) {
		this.node = node;
	}
	public Integer[] getHost() {
		return host;
	}
	public void setHost(Integer[] host) {
		this.host = host;
	}
	public Integer getCpuCount() {
		return cpuCount;
	}
	public void setCpuCount(Integer cpuCount) {
		this.cpuCount = cpuCount;
	}
	public BigDecimal getCpuUsage() {
		return cpuUsage;
	}
	public void setCpuUsage(BigDecimal cpuUsage) {
		this.cpuUsage = cpuUsage;
	}
	
	public BigInteger[] getMemory() {
		return memory;
	}
	public void setMemory(BigInteger[] memory) {
		this.memory = memory;
	}
	public BigDecimal getMemoryUsage() {
		return memoryUsage;
	}
	public void setMemoryUsage(BigDecimal memoryUsage) {
		this.memoryUsage = memoryUsage;
	}
	public BigInteger[] getDiskVolume() {
		return diskVolume;
	}
	public void setDiskVolume(BigInteger[] diskVolume) {
		this.diskVolume = diskVolume;
	}
	public BigDecimal getDiskUsage() {
		return diskUsage;
	}
	public void setDiskUsage(BigDecimal diskUsage) {
		this.diskUsage = diskUsage;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int getRegion() {
		return region;
	}
	public void setRegion(int region) {
		this.region = region;
	}
	
	
}
