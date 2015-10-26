package com.zhicloud.op.vo;

import com.zhicloud.op.common.util.json.JSONBean;

public class AllResourceVO implements JSONBean{
	private String status;
	private String message;
	private Integer[] ipCount;
	private Integer[] portCount;
	private Integer[] computeCount;
	private Integer[] stroageCount;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Integer[] getIpCount() {
		return ipCount;
	}
	public void setIpCount(Integer[] ipCount) {
		this.ipCount = ipCount;
	}
	public Integer[] getPortCount() {
		return portCount;
	}
	public void setPortCount(Integer[] portCount) {
		this.portCount = portCount;
	}
	public Integer[] getComputeCount() {
		return computeCount;
	}
	public void setComputeCount(Integer[] computeCount) {
		this.computeCount = computeCount;
	}
	public Integer[] getStroageCount() {
		return stroageCount;
	}
	public void setStroageCount(Integer[] stroageCount) {
		this.stroageCount = stroageCount;
	}
	
	
}
