package com.zhicloud.op.app.pool.device;

import java.util.Date;

public class DeviceExt extends Device {

	private long lastUpdateTime = 0;// 最新更新时间

	public void updateTime() {
		this.lastUpdateTime = new Date().getTime();
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public DeviceExt clone() {
		DeviceExt duplication = new DeviceExt();
		duplication.setRegion(this.getRegion());
		duplication.setName(this.getName());
		duplication.setUuid(this.getUuid());
		duplication.setStatus(this.getStatus());
		duplication.setDiskTotal(this.getDiskTotal());
		duplication.setDiskUnused(this.getDiskUnused());
		duplication.setLevel(this.getLevel());
		duplication.setIdentity(this.getIdentity());
		duplication.setSecurity(this.isSecurity());
		duplication.setCrypt(this.isCrypt());
		duplication.setPage(this.getPage());

		duplication.setLastUpdateTime(lastUpdateTime);

		return duplication;
	}

}
