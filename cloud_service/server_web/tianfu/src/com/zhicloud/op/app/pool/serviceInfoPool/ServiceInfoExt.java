package com.zhicloud.op.app.pool.serviceInfoPool;

import java.util.Date;

public class ServiceInfoExt extends ServiceInfo {

	private long lastUpdateTime = 0;// 最近更新时间
	private long exceptionTime = 0;// 异常状态下，异常开始时间
	private long exceptionDurationTime = 0;// 异常状态下后，恢复正常状态后，异常持续时间
	private String checkName = getName();

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

	public void setExceptionTime(long exceptionTime) {
		this.exceptionTime = exceptionTime;
	}

	public long getExceptionDurationTime() {
		return exceptionDurationTime;
	}

	public void setExceptionDurationTime(long exceptionDurationTime) {
		this.exceptionDurationTime = exceptionDurationTime;
	}
	
	public String getCheckName() {
		return checkName;
	}

	public void setCheckName(String checkName) {
		this.checkName = checkName;
	}

	public ServiceInfoExt clone() {
		ServiceInfoExt duplication = new ServiceInfoExt();
		duplication.setRegion(this.getRegion());
		duplication.setType(this.getType());
		duplication.setGroup(this.getGroup());
		duplication.setName(this.getName());
		duplication.setIp(this.getIp());
		duplication.setPort(this.getPort());
		duplication.setStatus(this.getStatus());
		
		duplication.setLastUpdateTime(this.getLastUpdateTime());
		duplication.setExceptionTime(this.getExceptionTime());
		duplication.setExceptionDurationTime(this.getExceptionDurationTime());
		
		return duplication;
	}

}
