/**
 * Project Name:op
 * File Name:PlatformResourceMonitorVO.java
 * Package Name:com.zhicloud.op.vo
 * Date:2015年6月9日上午9:55:29
 * 
 *
*/ 

package com.zhicloud.ms.vo; 

import com.zhicloud.ms.common.util.json.JSONBean;

import net.sf.json.JSONArray;

/**
 * ClassName: PlatformResourceMonitorVO 
 * Function:  平台资源监控
 * date: 2015年6月9日 上午9:55:29 
 *
 * @author sasa
 * @version 
 * @since JDK 1.7
 */
public class PlatformResourceMonitorVO implements JSONBean{ 
	
	private int task;//监控任务id
	private JSONArray server;//宿主机数量[停止,告警,故障,正常]
	private JSONArray host ;//云主机数量[停止,告警,故障,正常]
	private int cpuCount;//cpu总核心数
	private String cpuUsage ;//cpu利用率
	private JSONArray memory;//内存空间，单位：字节，[可用,总量]
	private String memoryUsage ;//内存利用率
	private JSONArray diskVolume ;//磁盘空间，单位：字节，[可用,总量]
	private String diskUsage ;//磁盘利用率
	private JSONArray diskIO ;//磁盘io，[读请求,读字节,写请求,写字节,IO错误次数]
	private JSONArray networkIO ;//网络io，[接收字节,接收包,接收错误,接收丢包,发送字节,发送包,发送错误,发送丢包]
	private JSONArray speed ;//速度，单位字节/s，[读速度,写速度,接收速度,发送速度]
	private String timestamp;//时间戳，格式"YYYY-MM-DD HH:MI:SS"
	private Integer region;//地域
	public int getTask() {
		return task;
	}
	public void setTask(int task) {
		this.task = task;
	}
	public JSONArray getServer() {
		return server;
	}
	public void setServer(JSONArray server) {
		this.server = server;
	}
	public JSONArray getHost() {
		return host;
	}
	public void setHost(JSONArray host) {
		this.host = host;
	}
	public int getCpuCount() {
		return cpuCount;
	}
	public void setCpuCount(int cpuCount) {
		this.cpuCount = cpuCount;
	}
	public String getCpuUsage() {
		return cpuUsage;
	}
	public void setCpuUsage(String cpuUsage) {
		this.cpuUsage = cpuUsage;
	}
	public JSONArray getMemory() {
		return memory;
	}
	public void setMemory(JSONArray memory) {
		this.memory = memory;
	}
	public String getMemoryUsage() {
		return memoryUsage;
	}
	public void setMemoryUsage(String memoryUsage) {
		this.memoryUsage = memoryUsage;
	}
	public JSONArray getDiskVolume() {
		return diskVolume;
	}
	public void setDiskVolume(JSONArray diskVolume) {
		this.diskVolume = diskVolume;
	}
	public String getDiskUsage() {
		return diskUsage;
	}
	public void setDiskUsage(String diskUsage) {
		this.diskUsage = diskUsage;
	}
	public JSONArray getDiskIO() {
		return diskIO;
	}
	public void setDiskIO(JSONArray diskIO) {
		this.diskIO = diskIO;
	}
	public JSONArray getNetworkIO() {
		return networkIO;
	}
	public void setNetworkIO(JSONArray networkIO) {
		this.networkIO = networkIO;
	}
	public JSONArray getSpeed() {
		return speed;
	}
	public void setSpeed(JSONArray speed) {
		this.speed = speed;
	}
	public String getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}
	public Integer getRegion() {
		return region;
	}
	public void setRegion(Integer region) {
		this.region = region;
	}
    
	
	

}

