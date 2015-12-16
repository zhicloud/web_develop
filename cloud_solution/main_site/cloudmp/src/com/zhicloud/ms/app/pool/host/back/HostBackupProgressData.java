package com.zhicloud.ms.app.pool.host.back;

import java.util.Date;

import org.apache.log4j.Logger;

import com.zhicloud.ms.controller.CloudServerController;
import com.zhicloud.ms.vo.CloudHostVO;

 
public class HostBackupProgressData extends CloudHostVO {

	private int progress = 0;// 进度
	private boolean finished = false;// 是否已完成
	private boolean success = false;// 成功与否
	private boolean ready = false;//是否可以开始备份和恢复
	private boolean avaliable = false;//是否有缓存
	private Integer[] diskVolume;//磁盘信息
	private String message;// 成功或失败消息
	private String sessionId;// channel的会话id
	private long lastUpdateTime = 0;// 最新的更新时间
	private String timestamp;//备份创建时间戳
	private Integer type;// 1表示定时备份的云主机
	private String uuid;
	//1 表示正在备份 2 表示正在恢复
	private Integer backupStatus;

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
	    Logger logger = Logger.getLogger(CloudServerController.class);
	    logger.info("update backup&resume finish "+finished); 
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

	public HostBackupProgressData clone() {
		HostBackupProgressData hostBackup = new HostBackupProgressData();

		hostBackup.setRealHostId(this.getRealHostId());
		hostBackup.setHostName(this.getHostName());
		hostBackup.setRegion(this.getRegion());

		hostBackup.setProgress(this.getProgress());
		hostBackup.setFinished(this.isFinished());
		hostBackup.setSuccess(this.isSuccess());
		hostBackup.setMessage(this.getMessage());
		hostBackup.setSessionId(this.getSessionId());
		hostBackup.setLastUpdateTime(this.getLastUpdateTime());
		hostBackup.setDiskVolume(this.getDiskVolume());
		hostBackup.setAvaliable((this.isAvaliable()));
		hostBackup.setTimestamp((this.getTimestamp()));
		hostBackup.setReady(this.isReady());
		hostBackup.setType(this.type);
		hostBackup.setBackupStatus(this.backupStatus);

		return hostBackup;
	}

	public Integer[] getDiskVolume() {
		return diskVolume;
	}

	public void setDiskVolume(Integer[] diskVolume) {
		this.diskVolume = diskVolume;
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

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public Integer getBackupStatus() {
		return backupStatus;
	}

	public void setBackupStatus(Integer backupStatus) {
	    Logger logger = Logger.getLogger(CloudServerController.class);
	    logger.info("update status "+backupStatus); 
		this.backupStatus = backupStatus;
	}

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    } 
	
	
	
	
	

}
