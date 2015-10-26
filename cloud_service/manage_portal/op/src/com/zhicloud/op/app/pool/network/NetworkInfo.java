package com.zhicloud.op.app.pool.network;

public class NetworkInfo {

	private Integer regionId;// 地域id
	private String uuid;// 唯一标识
	private String name;// vpc名称
	private int netmask = 24;// 子网掩码位数
	private String description;// 描述
	private String networkAddress;// 网段
	private int size;// 网络的大小
	private int status;// 状态:0=未启用，1=启用
	private String[] ip = new String[0];// 公网ip列表

	public Integer getRegionId() {
		return regionId;
	}

	public void setRegionId(Integer regionId) {
		this.regionId = regionId;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNetmask() {
		return netmask;
	}

	public void setNetmask(int netmask) {
		this.netmask = netmask;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNetworkAddress() {
		return networkAddress;
	}

	public void setNetworkAddress(String networkAddress) {
		this.networkAddress = networkAddress;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String[] getIp() {
		return ip;
	}

	public void setIp(String[] ip) {
		this.ip = ip;
	}

}
