package com.zhicloud.op.app.pool.serviceInfoPool;

import com.zhicloud.op.common.util.constant.NodeTypeDefine;

public class ServiceInfo {

	private int region;//区域
	private int type = NodeTypeDefine.INVALID;//服务类型，请参考NodeTypeDefine
	private String group = "default";//服务组名称
	private String name;//服务名称
	private String ip;//ip
	private int port;//端口
	private int status;//状态：0=正常,1=告警,2=故障,3=停止
	private String version;//当前版本号

	public int getRegion() {
		return region;
	}

	public void setRegion(int region) {
		this.region = region;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

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

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

}
