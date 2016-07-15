package com.zhicloud.ms.vo; 

import com.zhicloud.ms.util.DateUtil;
import com.zhicloud.ms.util.json.JSONBean;

import java.text.ParseException;

/**
 * @author ZYFTMX
 *
 */
public class VpcBaseInfoVO implements JSONBean{
	
	private String id;
	private String userId;
	private String createTime;
	private String modifiedTime;
	private String realVpcId;
	private String name;
    private String groupId;
    private String groupName;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    private String displayName;
	private String description;
	private Integer status;
	private Integer hostAmount;
	private Integer ipAmount;
	private String realHostId;
 	private String userName; 
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
	public String getRealVpcId() {
		return realVpcId;
	}
	public void setRealVpcId(String realVpcId) {
		this.realVpcId = realVpcId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getHostAmount() {
		return hostAmount;
	}
	public void setHostAmount(Integer hostAmount) {
		this.hostAmount = hostAmount;
	}
	public Integer getIpAmount() {
		return ipAmount;
	}
	public void setIpAmount(Integer ipAmount) {
		this.ipAmount = ipAmount;
	} 
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getRealHostId() {
		return realHostId;
	}
	public void setRealHostId(String realHostId) {
		this.realHostId = realHostId;
	}
	
	public String getStatusFormat(){
		if(this.status==1){
			return "启用";
		}else if(this.status==2){
			return "暂停";
		}else {
			return "关闭";
		}
	}
	
	public String getCraeteTimeFormat(){
		try {
			return DateUtil.formatDateString(this.createTime, "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}
}

