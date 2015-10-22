package com.zhicloud.ms.app.pool.addressPool;

import java.util.Date;

import com.zhicloud.ms.common.util.json.JSONBean;

public class AddressExt extends Address implements JSONBean{

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
