package com.zhicloud.ms.app.pool.host.reset;

import java.util.Date;

import com.zhicloud.ms.vo.CloudHostVO;

public class HostResetProgressData extends CloudHostVO {

	private int progress = 0;// 进度
	private boolean finished = false;// 是否已完成
	private boolean success = false;// 成功与否
	private boolean ready = false;//是否可以开始备份和恢复
	private boolean avaliable = false;//是否有缓存
	private String message;// 成功或失败消息
	private String sessionId;// channel的会话id
	private long lastUpdateTime = 0;// 最新的更新时间
	private String timestamp;//备份创建时间戳
	private Integer resetStatus; //重装状态 1：表示正在重装 0表示安装完毕或者还未开始安装
	

	public void updateTime() {
		this.lastUpdateTime = new Date().getTime();
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

	public HostResetProgressData clone() {
		HostResetProgressData hostBackup = new HostResetProgressData();

		hostBackup.setRealHostId(this.getRealHostId());
		hostBackup.setHostName(this.getHostName());
		hostBackup.setRegion(this.getRegion());

		hostBackup.setProgress(this.getProgress());
		hostBackup.setFinished(this.isFinished());
		hostBackup.setSuccess(this.isSuccess());
		hostBackup.setMessage(this.getMessage());
		hostBackup.setSessionId(this.getSessionId());
		hostBackup.setLastUpdateTime(this.getLastUpdateTime());
		hostBackup.setAvaliable((this.isAvaliable()));
		hostBackup.setTimestamp((this.getTimestamp()));
		hostBackup.setReady(this.isReady());
		hostBackup.setResetStatus(this.resetStatus);

		return hostBackup;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isAvaliable() {
		return avaliable;
	}

	public void setAvaliable(boolean avaliable) {
		this.avaliable = avaliable;
	}

	public boolean isReady() {
		return ready;
	}

	public void setReady(boolean ready) {
		this.ready = ready;
	}

    public Integer getResetStatus() {
        return resetStatus;
    }

    public void setResetStatus(Integer resetStatus) {
        this.resetStatus = resetStatus;
    }
	

}
