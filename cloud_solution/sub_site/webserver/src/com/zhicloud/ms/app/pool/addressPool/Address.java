package com.zhicloud.ms.app.pool.addressPool;

public class Address {

	String name;
	int status;
	int count[];
	String uuid;
	int region;
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
	public int getRegion() {
		return region;
	}
	public void setRegion(int region) {
		this.region = region;
	}
	
	
}
