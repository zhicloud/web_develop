package com.zhicloud.op.vo;

import com.zhicloud.op.common.util.json.JSONBean;

public class SysGroupVO implements JSONBean {

	private String id;
	private String groupName;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

}
