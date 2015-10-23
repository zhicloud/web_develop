package com.zhicloud.ms.vo;

public class NetworkInfoVO {
	//网卡名
	private String name;
	//网卡IP
	private String ip;
	//子网掩码
	private String netmask;
	//默认网关
	private String gateway;
	//网卡mac地址
	private String mac;
	//带宽
	private String speed;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getNetmask() {
		return netmask;
	}
	public void setNetmask(String netmask) {
		this.netmask = netmask;
	}
	public String getGateway() {
		return gateway;
	}
	public void setGateway(String gateway) {
		this.gateway = gateway;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getSpeed() {
		return speed;
	}
	public void setSpeed(String speed) {
		this.speed = speed;
	}
	
	
}
