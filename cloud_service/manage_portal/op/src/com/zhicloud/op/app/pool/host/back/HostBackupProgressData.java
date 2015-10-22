package com.zhicloud.op.app.pool.host.back;

import com.zhicloud.op.vo.CloudHostVO;

import java.util.Date;

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
  private Integer mode;//备份模式
  private Integer disk;//备份盘

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

    public Integer getMode() {
        return mode;
    }

    public void setMode(Integer mode) {
        this.mode = mode;
    }

    public Integer getDisk() {
        return disk;
    }

    public void setDisk(Integer disk) {
        this.disk = disk;
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
        hostBackup.setMode(this.getMode());
        hostBackup.setDisk(this.getDisk());
        return hostBackup;
    }

}
