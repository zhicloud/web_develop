package com.zhicloud.ms.vo;

import java.text.ParseException;

import com.zhicloud.ms.common.util.DateUtil;

/**
 * @author ZYFTMX
 *
 */
public class ClientMessageVO {
	
	private String id;
	private String userName;
	private String content;
	private Integer status;
	private Integer type;
	private String createTime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	
	public String getCreateTimeFormat(){
		try {
			return DateUtil.formatDateString(this.createTime, "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public String getStatusFormat(){
		if(this.status==1){
			return "已读";
		}else if(this.status==2){
			return "未读";
		}else{
			return "";
		}
	}
	
	public String getTypeFormat(){
		if(this.type==1){
			return "故障反馈";
		}else if(this.type==2){
			return "意见建议";
		}else{
			return "其它";
		}
	}
	
}
