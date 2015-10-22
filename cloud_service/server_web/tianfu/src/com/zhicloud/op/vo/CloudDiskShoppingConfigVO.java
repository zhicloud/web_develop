package com.zhicloud.op.vo;

import java.math.BigInteger;

import com.zhicloud.op.common.util.json.JSONBean;

public class CloudDiskShoppingConfigVO implements JSONBean {

	private String id;
	private String userId;
	private String diskId;
	private BigInteger disk;
	private String startTime;
	private String endTime;
	private Integer price;
	private String createTime;
	private Integer processStatus;
	private String processMessage;
	private Integer region;
	
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
	public String getDiskId() {
		return diskId;
	}
	public void setDiskId(String diskId) {
		this.diskId = diskId;
	}
	public BigInteger getDisk() {
		return disk;
	}
	public void setDisk(BigInteger disk) {
		this.disk = disk;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public Integer getProcessStatus() {
		return processStatus;
	}
	public void setProcessStatus(Integer processStatus) {
		this.processStatus = processStatus;
	}
	public String getProcessMessage() {
		return processMessage;
	}
	public void setProcessMessage(String processMessage) {
		this.processMessage = processMessage;
	}
	public Integer getRegion() {
		return region;
	}
	public void setRegion(Integer region) {
		this.region = region;
	}
	
	
}
