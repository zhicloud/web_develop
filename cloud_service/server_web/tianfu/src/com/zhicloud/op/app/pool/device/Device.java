package com.zhicloud.op.app.pool.device;

import java.math.BigInteger;

public class Device {

	Integer region;// 区域
	String name;// 云磁盘名称
	String uuid;// 云磁盘唯一标识
	int status;// 云磁盘状态，0-正常，1-告警，2-故障，3-停止
	BigInteger diskTotal;// 云磁盘磁盘空间总量,单位：字节
	BigInteger diskUnused;// 可用磁盘空间,单位：字节
	double level;// 健康度
	String identity;// iqn
	boolean security;// 是否要求校验
	boolean crypt;// 是否加密
	BigInteger page;// 页大小,单位：字节

	public Integer getRegion() {
		return region;
	}

	public void setRegion(Integer region) {
		this.region = region;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public BigInteger getDiskTotal() {
		return diskTotal;
	}

	public void setDiskTotal(BigInteger diskTotal) {
		this.diskTotal = diskTotal;
	}

	public BigInteger getDiskUnused() {
		return diskUnused;
	}

	public void setDiskUnused(BigInteger diskUnused) {
		this.diskUnused = diskUnused;
	}

	public double getLevel() {
		return level;
	}

	public void setLevel(double level) {
		this.level = level;
	}

	public String getIdentity() {
		return identity;
	}

	public void setIdentity(String identity) {
		this.identity = identity;
	}

	public boolean isSecurity() {
		return security;
	}

	public void setSecurity(boolean security) {
		this.security = security;
	}

	public boolean isCrypt() {
		return crypt;
	}

	public void setCrypt(boolean crypt) {
		this.crypt = crypt;
	}

	public BigInteger getPage() {
		return page;
	}

	public void setPage(BigInteger page) {
		this.page = page;
	}

}
