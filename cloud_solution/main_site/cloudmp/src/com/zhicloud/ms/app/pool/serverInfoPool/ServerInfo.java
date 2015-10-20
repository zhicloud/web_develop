package com.zhicloud.ms.app.pool.serverInfoPool;

import java.math.BigInteger;

public class ServerInfo {

	private int region;//地域
	private String name;//名称
	private String uuid;//唯一标识
	private int status = 0;//状态：0=正常,1=告警,2=故障,3=停止
	private int cpuCount = 0;//cpu核心数
	private double cpuUsage = 0;//cpu使用率
	private BigInteger memory = BigInteger.ZERO;//内存总容量
	private double memoryUsage = 0;//内存使用率
	private BigInteger diskVolume = BigInteger.ZERO;//磁盘总容量
	private double diskUsage = 0;//磁盘利用率
	private String ip;//节点ip

	public int getRegion() {
		return region;
	}

	public void setRegion(int region) {
		this.region = region;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getCpuCount() {
		return cpuCount;
	}

	public void setCpuCount(int cpuCount) {
		this.cpuCount = cpuCount;
	}

	public double getCpuUsage() {
		return cpuUsage;
	}

	public void setCpuUsage(double cpuUsage) {
		this.cpuUsage = cpuUsage;
	}

	public BigInteger getMemory() {
		return memory;
	}

	public void setMemory(BigInteger memory) {
		this.memory = memory;
	}

	public double getMemoryUsage() {
		return memoryUsage;
	}

	public void setMemoryUsage(double memoryUsage) {
		this.memoryUsage = memoryUsage;
	}

	public BigInteger getDiskVolume() {
		return diskVolume;
	}

	public void setDiskVolume(BigInteger diskVolume) {
		this.diskVolume = diskVolume;
	}

	public double getDiskUsage() {
		return diskUsage;
	}

	public void setDiskUsage(double diskUsage) {
		this.diskUsage = diskUsage;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

}
