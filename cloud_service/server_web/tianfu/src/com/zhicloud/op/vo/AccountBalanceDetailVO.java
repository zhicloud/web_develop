package com.zhicloud.op.vo;

import java.math.BigDecimal; 

import com.zhicloud.op.common.util.json.JSONBean;
public class AccountBalanceDetailVO implements JSONBean
{
	
	private String id;
	private String userId;
	private String userAccount;
	private Integer type;
	private BigDecimal amount;
	private BigDecimal giftAmount;
	private BigDecimal balanceBeforeChange;
	private BigDecimal balanceAfterChange;
	private Integer payType;
	private String description;
	private String changeTime;
	private String invoiceId;
	private String billId;
	private Integer rechargeStatus;
	private String resourceName;
	
	
	
	public String getResourceName() 
	{
		return resourceName;
	}
	public void setResourceName(String resourceName) 
	{
		this.resourceName = resourceName;
	}
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
	public Integer getType()
	{
		return type;
	}
	public void setType(Integer type)
	{
		this.type = type;
	}
	public BigDecimal getAmount()
	{
		return amount;
	}
	public void setAmount(BigDecimal amount)
	{
		this.amount = amount;
	}
	public BigDecimal getBalanceBeforeChange()
	{
		return balanceBeforeChange;
	}
	public void setBalanceBeforeChange(BigDecimal balanceBeforeChange)
	{
		this.balanceBeforeChange = balanceBeforeChange;
	}
	public BigDecimal getBalanceAfterChange()
	{
		return balanceAfterChange;
	}
	public void setBalanceAfterChange(BigDecimal balanceAfterChange)
	{
		this.balanceAfterChange = balanceAfterChange;
	}
	public Integer getPayType()
	{
		return payType;
	}
	public void setPayType(Integer payType)
	{
		this.payType = payType;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public String getChangeTime()
	{
		return changeTime;
	}
	public void setChangeTime(String changeTime)
	{
		this.changeTime = changeTime;
	}
	public String getInvoiceId()
	{
		return invoiceId;
	}
	public void setInvoiceId(String invoiceId)
	{
		this.invoiceId = invoiceId;
	}
	public String getBillId()
	{
		return billId;
	}
	public void setBillId(String billId)
	{
		this.billId = billId;
	}
	public Integer getRechargeStatus()
	{
		return rechargeStatus;
	}
	public void setRechargeStatus(Integer rechargeStatus)
	{
		this.rechargeStatus = rechargeStatus;
	}
	public String getUserAccount() {
		return userAccount;
	}
	public void setUserAccount(String userAccount) {
		this.userAccount = userAccount;
	}
	public BigDecimal getGiftAmount() {
		return giftAmount;
	}
	public void setGiftAmount(BigDecimal giftAmount) {
		this.giftAmount = giftAmount;
	}
	
	
	
	
	
}
