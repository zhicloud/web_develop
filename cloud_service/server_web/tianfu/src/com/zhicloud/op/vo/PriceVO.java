package com.zhicloud.op.vo;

import java.math.BigDecimal;
import com.zhicloud.op.common.util.json.JSONBean;
public class PriceVO implements JSONBean
{
	private String id;
	private String packageId;
	private Integer status;
	private BigDecimal monthlyPrice;
	private String createTime;
	private String modifiedTime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getPackageId() {
		return packageId;
	}
	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public BigDecimal getMonthlyPrice() {
		return monthlyPrice;
	}
	public void setMonthlyPrice(BigDecimal monthlyPrice) {
		this.monthlyPrice = monthlyPrice;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(String modifiedTime) {
		this.modifiedTime = modifiedTime;
	}
	
	
}
