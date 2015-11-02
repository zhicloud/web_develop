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
    private String cpu_count_text;// cpu已使用和未使用显示
    private String memory_text;// 内存显示使用和未使用
    private String disk_text;// 磁盘显示使用和未使用

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
    public String getCpu_count_text() {
        return cpu_count_text;
    }
    public void setCpu_count_text(String cpu_count_text) {
        this.cpu_count_text = cpu_count_text;
    }
    public String getMemory_text() {
        return memory_text;
    }
    public void setMemory_text(String memory_text) {
        this.memory_text = memory_text;
    }
    public String getDisk_text() {
        return disk_text;
    }
    public void setDisk_text(String disk_text) {
        this.disk_text = disk_text;
    }
    
}
