package com.zhicloud.ms.vo;

import java.math.BigDecimal;

import com.zhicloud.ms.util.json.JSONBean;
public class StorageResourceViewVO implements JSONBean{
	private BigDecimal cpuUsage;//CPU利用率
	private BigDecimal memoryUsage;//内存使用率
	public BigDecimal getCpuUsage() {
		return cpuUsage;
	}
	public void setCpuUsage(BigDecimal cpuUsage) {
		this.cpuUsage = cpuUsage;
	}
	public BigDecimal getMemoryUsage() {
		return memoryUsage;
	}
	public void setMemoryUsage(BigDecimal memoryUsage) {
		this.memoryUsage = memoryUsage;
	}
	
	
}
