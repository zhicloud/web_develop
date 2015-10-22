package com.zhicloud.op.pool.addressPool;

import java.util.Date;

public class AddressExt extends Address {

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

	public AddressExt clone() {
		AddressExt duplication = new AddressExt();
		duplication.setName(this.getName());
		duplication.setUuid(this.getUuid());
		duplication.setStatus(this.getStatus());
		duplication.setCount(this.getCount());
		duplication.setLastUpdateTime(lastUpdateTime);

		return duplication;
	}

}
