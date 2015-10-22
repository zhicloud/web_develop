package com.zhicloud.op.app.pool.diskImagePool;

import java.util.Date;

import com.zhicloud.op.vo.SysDiskImageVO;

public class DiskImageProgressData extends SysDiskImageVO {

	private int progress = 0;// 进度
	private boolean finished = false;// 是否已完成
	private boolean success = false;// 成功与否
	private String message;// 成功或失败消息
	private long size = 0;// 磁盘镜像大小
	private String sessionId;// channel的会话id
	private long lastUpdateTime = 0;// 最新的更新时间

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

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
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

	public DiskImageProgressData clone() {
		DiskImageProgressData diskImage = new DiskImageProgressData();

		diskImage.setId(this.getId());
		diskImage.setRealImageId(this.getRealImageId());
		diskImage.setName(this.getName());
		diskImage.setDisplayName(this.getDisplayName());
		diskImage.setFromHostId(this.getFromHostId());
		diskImage.setTag(this.getTag());
		diskImage.setDescription(this.getDescription());
		diskImage.setGroupId(this.getGroupId());
		diskImage.setUserId(this.getUserId());
		diskImage.setSort(this.getSort());
		diskImage.setRegion(this.getRegion());
		diskImage.setStatus(this.getStatus());

		diskImage.setProgress(this.getProgress());
		diskImage.setFinished(this.isFinished());
		diskImage.setSuccess(this.isSuccess());
		diskImage.setMessage(this.getMessage());
		diskImage.setSize(this.getSize());
		diskImage.setSessionId(this.getSessionId());
		diskImage.setLastUpdateTime(this.getLastUpdateTime());

		return diskImage;
	}

}
