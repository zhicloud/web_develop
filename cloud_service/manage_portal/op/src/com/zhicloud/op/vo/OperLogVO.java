package com.zhicloud.op.vo;
  
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlTransient;

import com.zhicloud.op.common.util.json.JSONBean;
@XmlAccessorType(XmlAccessType.FIELD)
public class OperLogVO implements JSONBean {
	@XmlTransient
	private String id;
	@XmlTransient
	private String userId;
	private String content; 
	private String operTime;
	@XmlTransient
	private String operDuration;
	@XmlTransient
	private Integer status;
	@XmlTransient
	private String resourceName; 
	private String account;
	private Integer userType;
	private String name;
	private String displayStatus;
	private String typeName;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getOperTime() {
		return operTime;
	}
	public void setOperTime(String operTime) {
		this.operTime = operTime;
	}
	public String getOperDuration() {
		return operDuration;
	}
	public void setOperDuration(String operDuration) {
		this.operDuration = operDuration;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public String getResourceName() {
		return resourceName;
	}
	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public Integer getUserType() {
		return userType;
	}
	public void setUserType(Integer userType) {
		this.userType = userType;
	}
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDisplayStatus() {
        return displayStatus;
    }
    public void setDisplayStatus(String displayStatus) {
        this.displayStatus = displayStatus;
    }
    public String getTypeName() {
        return typeName;
    }
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    } 
	
	
	

}
