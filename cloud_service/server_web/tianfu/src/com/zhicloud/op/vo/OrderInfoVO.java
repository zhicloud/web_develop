package com.zhicloud.op.vo;

public class OrderInfoVO
{
	
	
	private String id;             
	private String userId;        
	private String createTime;    
	private String totalPrice;    
	private String processStatus;   
	private String processMessage;   
	private String isPaid ;
	
	
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
	public String getCreateTime()
	{
		return createTime;
	}
	public void setCreateTime(String createTime)
	{
		this.createTime = createTime;
	}
	public String getTotalPrice()
	{
		return totalPrice;
	}
	public void setTotalPrice(String totalPrice)
	{
		this.totalPrice = totalPrice;
	}
	public String getProcessStatus()
	{
		return processStatus;
	}
	public void setProcessStatus(String processStatus)
	{
		this.processStatus = processStatus;
	}
	public String getProcessMessage()
	{
		return processMessage;
	}
	public void setProcessMessage(String processMessage)
	{
		this.processMessage = processMessage;
	}
	public String getIsPaid()
	{
		return isPaid;
	}
	public void setIsPaid(String isPaid)
	{
		this.isPaid = isPaid;
	}
	
	
	
}
