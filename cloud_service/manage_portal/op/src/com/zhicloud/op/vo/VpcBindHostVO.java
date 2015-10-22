/**
 * Project Name:op
 * File Name:VpcBindPortVO.java
 * Package Name:com.zhicloud.op.vo
 * Date:2015年4月1日下午3:35:12
 * 
 *
*/ 

package com.zhicloud.op.vo; 

import com.zhicloud.op.common.util.json.JSONBean;


public class VpcBindHostVO implements JSONBean{
	
	private String id;
	private String VpcId;
	private String hostId;
	private Integer flag;
	private String createTime;
	private String removeTime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVpcId() {
		return VpcId;
	}
	public void setVpcId(String vpcId) {
		VpcId = vpcId;
	}
	public String getHostId() {
		return hostId;
	}
	public void setHostId(String hostId) {
		this.hostId = hostId;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getRemoveTime() {
		return removeTime;
	}
	public void setRemoveTime(String removeTime) {
		this.removeTime = removeTime;
	}
	
}

