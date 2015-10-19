package com.zhicloud.ms.app.pool.hostMonitorInfoPool;

import java.util.Date;
import java.util.Set;

public class HostMonitorInfo {

	private Integer region = null;
	private Set<String> hostList = null;
	private String sessionId = null;
	private Integer task = null;
	private boolean needToRestart = false;
	long lastUpdateTime = 0;

	public Integer getRegion() {
		return region;
	}

	public void setRegion(Integer region) {
		this.region = region;
	}

	public Set<String> getHostList() {
		return hostList;
	}

	public void setHostList(Set<String> hostList) {
		this.hostList = hostList;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Integer getTask() {
		return task;
	}

	public void setTask(Integer task) {
		this.task = task;
	}

	public boolean isNeedToRestart() {
		return needToRestart;
	}

	public void setNeedToRestart(boolean needToRestart) {
		this.needToRestart = needToRestart;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void updateTime() {
		this.lastUpdateTime = new Date().getTime();
	}

}
