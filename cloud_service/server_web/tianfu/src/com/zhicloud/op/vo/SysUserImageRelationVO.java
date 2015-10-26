package com.zhicloud.op.vo;

import com.zhicloud.op.common.util.json.JSONBean;

public class SysUserImageRelationVO implements JSONBean {
	private String id;
	private String userId;
	private String sysImageId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSysImageId() {
		return sysImageId;
	}
	public void setSysImageId(String sysImageId) {
		this.sysImageId = sysImageId;
	}
	
	

}
