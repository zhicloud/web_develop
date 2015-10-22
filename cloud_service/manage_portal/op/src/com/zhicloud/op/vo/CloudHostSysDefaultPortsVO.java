package com.zhicloud.op.vo;

import com.zhicloud.op.common.util.json.JSONBean;

public class CloudHostSysDefaultPortsVO implements JSONBean{
	private String id;
	private String name;
	private String port;
	private Integer protocol; 
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPort() {
		return port;
	}
	public void setPort(String port) {
		this.port = port;
	} 
	
	public Integer getProtocol() {
		return protocol;
	}
	public void setProtocol(Integer protocol) {
		this.protocol = protocol;
	}
	public String getProtocolName() {
		if(protocol == 0){
			return "全部";
		}else if(protocol == 1){
			return "TCP";
		}else{
			return "UDP";
		}
	} 
	
	
	

}
