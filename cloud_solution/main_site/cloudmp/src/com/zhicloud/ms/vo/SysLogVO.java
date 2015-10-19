package com.zhicloud.ms.vo;

import java.text.ParseException;

import com.zhicloud.ms.common.util.DateUtil;

/**
 * 操作日志
 * @author ZYFTMX
 *
 */
public class SysLogVO {
	private String id;
	private String operTime;
	private String module;
	private String content;
	private Integer status;
	private Integer level;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getOperTime() {
		return operTime;
	}
	public void setOperTime(String operTime) {
		this.operTime = operTime;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
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
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	
	public String getOperTimeFormat(){
		try {
			return DateUtil.formatDateString(this.operTime, "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss");
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public String getStatusFormat(){
		if(this.status==1){
			return "成功";
		}else if(this.status==2){
			return "失败";
		}else{
			return "异步";
		}
	}
	
	public String getLevelFormat(){
		if(this.level==1){
			return "一般";
		}else if(this.level==2){
			return "敏感";
		}else{
			return "高危";
		}
	}
	
}
