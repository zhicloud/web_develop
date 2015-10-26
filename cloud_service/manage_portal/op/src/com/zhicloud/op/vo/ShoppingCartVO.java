package com.zhicloud.op.vo;

import java.math.BigDecimal;

import com.zhicloud.op.common.util.json.JSONBean;

public class ShoppingCartVO implements JSONBean{
	private String id;
	private String userId;
	private String createTime;
	private BigDecimal totalPrice;
	private String orderId;
	private String itemType;
	private String itemId;
	private Integer type;
	private Integer cpuCore;
	private BigDecimal memory;
	private String sysImageId;
	private BigDecimal sysDisk;
	private BigDecimal dataDisk;
	private BigDecimal bandwidth;
	private String startTime;
	private String endTime;
	private BigDecimal price;  
	private Integer isProcessed;
	private Integer duration;
	
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
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public BigDecimal getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(BigDecimal totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getItemType() {
		return itemType;
	}
	public void setItemType(String itemType) {
		this.itemType = itemType;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public Integer getCpuCore() {
		return cpuCore;
	}
	public void setCpuCore(Integer cpuCore) {
		this.cpuCore = cpuCore;
	}
	public BigDecimal getMemory() {
		return memory;
	}
	public void setMemory(BigDecimal memory) {
		this.memory = memory;
	}
	public String getSysImageId() {
		return sysImageId;
	}
	public void setSysImageId(String sysImageId) {
		this.sysImageId = sysImageId;
	}
	public BigDecimal getSysDisk() {
		return sysDisk;
	}
	public void setSysDisk(BigDecimal sysDisk) {
		this.sysDisk = sysDisk;
	}
	public BigDecimal getDataDisk() {
		return dataDisk;
	}
	public void setDataDisk(BigDecimal dataDisk) {
		this.dataDisk = dataDisk;
	}
	public BigDecimal getBandwidth() {
		return bandwidth;
	}
	public void setBandwidth(BigDecimal bandwidth) {
		this.bandwidth = bandwidth;
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
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public Integer getIsProcessed() {
		return isProcessed;
	}
	public void setIsProcessed(Integer isProcessed) {
		this.isProcessed = isProcessed;
	}
	public Integer getDuration() {
		return duration;
	}
	public void setDuration(Integer duration) {
		this.duration = duration;
	}
	
	
	
	

}

