/**
 * Project Name:op
 * File Name:VpcBillDetailVO.java
 * Package Name:com.zhicloud.op.vo
 * Date:2015年4月1日下午6:53:46
 * 
 *
*/ 

package com.zhicloud.op.vo; 

import java.math.BigDecimal;

import com.zhicloud.op.common.util.json.JSONBean;

/**
 * ClassName: VpcBillDetailVO 
 * Function: vpc账单信息
 * date: 2015年4月1日 下午6:53:46 
 *
 * @author sasa
 * @version 
 * @since JDK 1.7
 */
public class VpcBillDetailVO implements JSONBean{
	
	private String id;
	private String vpcId;
	private Integer hostAmount;
	private Integer idAmount;
	private String startTime;
	private String endTime;
	private String createTime;
	private BigDecimal fee;
	private Integer isPaid;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVpcId() {
		return vpcId;
	}
	public void setVpcId(String vpcId) {
		this.vpcId = vpcId;
	}
	public Integer getHostAmount() {
		return hostAmount;
	}
	public void setHostAmount(Integer hostAmount) {
		this.hostAmount = hostAmount;
	}
	public Integer getIdAmount() {
		return idAmount;
	}
	public void setIdAmount(Integer idAmount) {
		this.idAmount = idAmount;
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
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public BigDecimal getFee() {
		return fee;
	}
	public void setFee(BigDecimal fee) {
		this.fee = fee;
	}
	public Integer getIsPaid() {
		return isPaid;
	}
	public void setIsPaid(Integer isPaid) {
		this.isPaid = isPaid;
	}
	
	
	
	
	
	

}

