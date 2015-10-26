package com.zhicloud.op.app.pool.serverInfoPool;

import java.util.Date;

public class ServerInfoExt extends ServerInfo {

	private long lastUpdateTime = 0;//最近更新时间
	private long exceptionTime = 0;//异常状态下，异常开始时间
	private long exceptionDurationTime = 0;//异常状态下后，恢复正常状态后，异常持续时间
	
	public void updateTime() {
		this.lastUpdateTime = new Date().getTime();
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public long getExceptionTime() {
		return exceptionTime;
	}

	public void setExceptionTime(long stopTime) {
		this.exceptionTime = stopTime;
	}
	
	public long getExceptionDurationTime() {
		return exceptionDurationTime;
	}

	public void setExceptionDurationTime(long stopDurationTime) {
		this.exceptionDurationTime = stopDurationTime;
	}

	public ServerInfoExt clone() {
		ServerInfoExt serverInfoExt = new ServerInfoExt();
		serverInfoExt.setName(this.getName());
		serverInfoExt.setUuid(this.getUuid());
		serverInfoExt.setStatus(this.getStatus());
		serverInfoExt.setCpuCount(this.getCpuCount());
		serverInfoExt.setCpuUsage(this.getCpuUsage());
		serverInfoExt.setMemory(this.getMemory());
		serverInfoExt.setMemoryUsage(this.getMemoryUsage());
		serverInfoExt.setDiskVolume(this.getDiskVolume());
		serverInfoExt.setDiskUsage(this.getDiskUsage());
		serverInfoExt.setIp(this.getIp());
		serverInfoExt.setLastUpdateTime(this.getLastUpdateTime());
		serverInfoExt.setExceptionTime(this.getExceptionTime());
		serverInfoExt.setExceptionDurationTime(this.getExceptionDurationTime());
		
		return serverInfoExt;
	}

}
