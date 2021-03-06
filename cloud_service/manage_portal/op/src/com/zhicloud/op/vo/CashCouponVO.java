package com.zhicloud.op.vo;

import java.math.BigDecimal;

import com.zhicloud.op.common.util.json.JSONBean;

public class CashCouponVO implements JSONBean {
	private String id;
	private String createrId;
	private String cashCode;
	private BigDecimal money;
	private String userId;
	private String dealine;
	private String sendTime;
	private String createTime;
	private String phone;
	private String email;
	private Integer status;
	private String sendAddress;
	private String userName;
	private String name;
	private String markname;
	//翻译字段信息
	private String statusname;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCreaterId() {
		return createrId;
	}
	public void setCreaterId(String createrId) {
		this.createrId = createrId;
	}
	public String getCashCode() {
		return cashCode;
	}
	public void setCashCode(String cashCode) {
		this.cashCode = cashCode;
	}
	public BigDecimal getMoney() {
		return money;
	}
	public void setMoney(BigDecimal money) {
		this.money = money;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getDealine() {
		return dealine;
	}
	public void setDealine(String dealine) {
		this.dealine = dealine;
	}
	public String getSendTime() {
		return sendTime;
	}
	public void setSendTime(String sendTime) {
		this.sendTime = sendTime;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getSendAddress() {
		return sendAddress;
	}
	public void setSendAddress(String sendAddress) {
		this.sendAddress = sendAddress;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getMarkname() {
        return markname;
    }
    public void setMarkname(String markname) {
        this.markname = markname;
    }
    public String getStatusname() {
        return statusname;
    }
    public void setStatusname(String statusname) {
        this.statusname = statusname;
    }
	
	
	
}
