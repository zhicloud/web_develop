package com.zhicloud.op.vo;

import com.zhicloud.op.common.util.json.JSONBean;

public class CloudHostShoppingPortConfigVO implements JSONBean{
	
	public String id;
	public String configId;
	public int port;
	public int protocol;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getConfigId() {
		return configId;
	}
	public void setConfigId(String configId) {
		this.configId = configId;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getProtocol() {
		return protocol;
	}
	public void setProtocol(int protocol) {
		this.protocol = protocol;
	}
	
	

}
