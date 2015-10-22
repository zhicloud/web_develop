package com.zhicloud.op.pool.addressPool;

public class Address {

	String name;// 云磁盘名称
	int status;// 云磁盘状态，0-正常，1-告警，2-故障，3-停止
	int count[];
	String uuid;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public int[] getCount() {
		return count;
	}
	public void setCount(int[] count) {
		this.count = count;
	}
	public String getUuid() {
		return uuid;
	}
	public void setUuid(String uuid) {
		this.uuid = uuid;
	}
	
	
}
