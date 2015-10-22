/**
 * Project Name:op
 * File Name:VpcOuterIpVO.java
 * Package Name:com.zhicloud.op.vo
 * Date:2015年4月1日下午3:33:12
 * 
 *
*/ 

package com.zhicloud.op.vo; 

import java.math.BigDecimal;

import com.zhicloud.op.common.util.json.JSONBean;

public class VpcPriceVO implements JSONBean{
	
	private String id;
	private Integer vpcAmount;
	private Integer region;
	private BigDecimal price;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Integer getVpcAmount() {
		return vpcAmount;
	}
	public void setVpcAmount(Integer vpcAmount) {
		this.vpcAmount = vpcAmount;
	}
	public Integer getRegion() {
		return region;
	}
	public void setRegion(Integer region) {
		this.region = region;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	
	
}

