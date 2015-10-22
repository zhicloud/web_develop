package com.zhicloud.op.vo;

import com.zhicloud.op.common.util.json.JSONBean;

public class ImageHostApplicationVO implements JSONBean {
	
	private String id;
	//名
	private String name;
	//联系人
	private String contacts;
	//联系人职位
	private String contactsPosition; 
	//联系人电话
	private String contactsPhone; 
	//QQ或者微信
	private String qqOrWeixin;
	//简介
	private String summary;
	//状态 1：待处理，2：已处理
	private Integer status;
	//处理人Id
	private String handleUserId;
	//创建时间
	private String crteateTime;
	//处理时间
	private String handleTime;
	//处理用户名
	private String handleUserName;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	} 
	public String getContacts() {
		return contacts;
	}
	public void setContacts(String contacts) {
		this.contacts = contacts;
	}
	public String getContactsPosition() {
		return contactsPosition;
	}
	public void setContactsPosition(String contactsPosition) {
		this.contactsPosition = contactsPosition;
	}
	public String getContactsPhone() {
		return contactsPhone;
	}
	public void setContactsPhone(String contactsPhone) {
		this.contactsPhone = contactsPhone;
	}
	public String getQqOrWeixin() {
		return qqOrWeixin;
	}
	public void setQqOrWeixin(String qqOrWeixin) {
		this.qqOrWeixin = qqOrWeixin;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getHandleUserId() {
		return handleUserId;
	}
	public void setHandleUserId(String handleUserId) {
		this.handleUserId = handleUserId;
	}
	public String getCrteateTime() {
		return crteateTime;
	}
	public void setCrteateTime(String crteateTime) {
		this.crteateTime = crteateTime;
	}
	public String getHandleTime() {
		return handleTime;
	}
	public void setHandleTime(String handleTime) {
		this.handleTime = handleTime;
	}
	public String getHandleUserName() {
		return handleUserName;
	}
	public void setHandleUserName(String handleUserName) {
		this.handleUserName = handleUserName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
	

} 
