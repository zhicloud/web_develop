package com.zhicloud.op.app.pool.isoImagePool;

import java.util.Date;

import com.zhicloud.op.app.pool.IsoImagePool.IsoImageData;

public class IsoImageProgressData extends IsoImageData {

	private int progress = 0;// 进度
	private boolean finished = false;// 是否已完成
	private boolean success = false;// 成功与否
	private String message;// 成功或失败消息
	private String sessionId;// channel的会话id
	private long lastUpdateTime = 0;// 最新的更新时间
	private String ip;
	private String port;
	private String user;
	private String group;

	public void updateTime() {
		this.lastUpdateTime = new Date().getTime();
	}

	public IsoImageProgressData clone() {
		IsoImageProgressData duplication = new IsoImageProgressData();

		duplication.setRealImageId(this.getRealImageId());
		duplication.setName(this.getName());
		duplication.setDescription(this.getDescription());
		duplication.setSize(this.getSize());
		duplication.setStatus(this.getStatus());
		duplication.setRegion(this.getRegion());

		duplication.setProgress(this.getProgress());
		duplication.setFinished(this.isFinished());
		duplication.setSuccess(this.isSuccess());
		duplication.setMessage(this.getMessage());
		duplication.setSessionId(this.getSessionId());
		duplication.setLastUpdateTime(this.getLastUpdateTime());

		return duplication;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}
}
