package com.zhicloud.op.vo;

import java.math.BigDecimal;

import com.zhicloud.op.common.util.json.JSONBean;

public class BillVO implements JSONBean
{
	private String id;
	/**
	 * 账单所属的用户ID
	 */
	private String userId;
	/**
	 * 账单所属用户的名称
	 */
	private String userAccount;
	/**
	 * 账单的费用
	 */
	private BigDecimal fee;
	/**
	 * 是否付费 1：未付费 2：已付费
	 */
	private int isPaid;
	/**
	 * 应付费时间
	 */
	private String payableTime;
	/**
	 * 实际付费时间
	 */
	private String paymentTime;
	/**
	 * 创建账单的时间
	 */
	private String createTime;
	/**
	 * 地域
	 */
	private Integer region;
	/**
	 * 资源名
	 */
	private String resourceName;
	/**
	 * 资源类型
	 */
	private Integer type;
	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getUserAccount()
	{
		return userAccount;
	}

	public void setUserAccount(String userAccount)
	{
		this.userAccount = userAccount;
	}

	public BigDecimal getFee()
	{
		return fee;
	}

	public void setFee(BigDecimal fee)
	{
		this.fee = fee;
	}

	public int getIsPaid()
	{
		return isPaid;
	}

	public void setIsPaid(int isPaid)
	{
		this.isPaid = isPaid;
	}

	public String getPayableTime()
	{
		return payableTime;
	}

	public void setPayableTime(String payableTime)
	{
		this.payableTime = payableTime;
	}

	public String getPaymentTime()
	{
		return paymentTime;
	}

	public void setPaymentTime(String paymentTime)
	{
		this.paymentTime = paymentTime;
	}

	public String getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(String createTime)
	{
		this.createTime = createTime;
	}

	public Integer getRegion() {
		return region;
	}

	public void setRegion(Integer region) {
		this.region = region;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}
	
}
