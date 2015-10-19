package com.zhicloud.ms.vo;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.zhicloud.ms.util.CapacityUtil;
import com.zhicloud.ms.util.json.JSONBean;
public class ComputerPoolDetailVO implements JSONBean{
	private String name;//资源池名
	private String uuid;
	private Integer cpuCount;//CPU核心数
	private BigDecimal cpuUsage;//CPU利用率
	private BigInteger[] memory;//内存
	private BigDecimal memoryUsage;//内存使用率
	private BigInteger[] diskVolume;//磁盘容量[可用，总量]
	private BigDecimal diskUsage;//磁盘使用率
	private int status;//资源池状态
	private String ip;
	private String[] hostIps;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public BigDecimal getCpuUsageFormat(){
		return this.cpuUsage.multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP);
	}
	public void setCpuUsage(BigDecimal cpuUsage) {
		this.cpuUsage = cpuUsage;
	}
	
	public BigInteger[] getMemory() {
		return memory;
	}
	public String getUsedMemoryText(){
		return CapacityUtil.toGBValue(this.memory[1].subtract(this.memory[0]),2).toString();
	}
	public String getAllMemoryText(){
		return CapacityUtil.toGBValue(this.memory[1],0).toString();
	}
	public BigDecimal getMemoryUsageFormat(){
		return this.memoryUsage.multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP);
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
	public String getUsedDiskText(){
		return CapacityUtil.toGBValue(this.diskVolume[1].subtract(this.diskVolume[0]),2).toString();
	}
	public String getAllDiskText(){
		return CapacityUtil.toGBValue(this.diskVolume[1],0).toString();
	}
	public BigDecimal getDiskUsageFormat(){
		return this.diskUsage.multiply(new BigDecimal(100)).setScale(2,BigDecimal.ROUND_HALF_UP);
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
	public String getStatusText(){
		if(this.status == 0){
			return "运行";
		}else if(this.status == 1){
			return "告警";
		}else if(this.status == 2){
			return "故障";
		}else {
			return "关机";
		}
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String[] getHostIps() {
		return hostIps;
	}
	public String getOuterIp(){
		return this.hostIps[1];
	}
	public String getInnerIp(){
		return this.hostIps[0];
	}
	public void setHostIps(String[] hostIps) {
		this.hostIps = hostIps;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	
}
