package com.zhicloud.op.vo;

import com.zhicloud.op.common.util.json.JSONBean;

public class UserDictionaryVO implements JSONBean{
	
	private String id;
	private String userId;
	private String dictionaryKey;
	private String dictionaryValue;
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
	public String getDictionaryKey() {
		return dictionaryKey;
	}
	public void setDictionaryKey(String dictionaryKey) {
		this.dictionaryKey = dictionaryKey;
	}
	public String getDictionaryValue() {
		return dictionaryValue;
	}
	public void setDictionaryValue(String dictionaryValue) {
		this.dictionaryValue = dictionaryValue;
	}
	
	

}
