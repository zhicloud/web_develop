package com.zhicloud.op.vo;

import com.zhicloud.op.common.util.json.JSONBean;

public class BillDetailVO implements JSONBean
{
	
	private String id;
	private String billId;
	private Integer itemType;
	private String itemId;
	private Integer region;
	private String userName;
	private String resourceName;
	
	
	public String getId()
	{
		return id;
	}
	public void setId(String id)
	{
		this.id = id;
	}
	public String getBillId()
	{
		return billId;
	}
	public void setBillId(String billId)
	{
		this.billId = billId;
	}
	public Integer getItemType()
	{
		return itemType;
	}
	public void setItemType(Integer itemType)
	{
		this.itemType = itemType;
	}
	public String getItemId()
	{
		return itemId;
	}
	public void setItemId(String itemId)
	{
		this.itemId = itemId;
	}
	public Integer getRegion() {
		return region;
	}
	public void setRegion(Integer region) {
		this.region = region;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	
}
