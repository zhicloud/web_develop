package com.zhicloud.op.vo;

import com.zhicloud.op.common.util.json.JSONBean;

public class CloudTerminalBindingVO implements JSONBean {

	private String id;
	private String cloudTerminalId;
	private String userId;
	private String hostId;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCloudTerminalId() {
		return cloudTerminalId;
	}
	public void setCloudTerminalId(String cloudTerminalId) {
		this.cloudTerminalId = cloudTerminalId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getHostId() {
		return hostId;
	}
	public void setHostId(String hostId) {
		this.hostId = hostId;
	}
	
	
}
