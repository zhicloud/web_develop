package com.zhicloud.ms.vo;

import com.zhicloud.ms.common.util.json.JSONBean;

 
public class DictionaryVO implements JSONBean{
	
	private String id;
	//编码
	private String code;
	//值
	private String value;
	//创建时间
	private String createTime;
	//更新时间
	private String modifyTime;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	
	
	
	

}
