package com.zhicloud.op.vo;

import com.zhicloud.op.common.util.json.JSONBean;

public class TrialPeriodParamVO implements JSONBean {

	private String id;
	private Integer type;
	private Integer day;

	public TrialPeriodParamVO() {

	}

	public TrialPeriodParamVO(Integer type, Integer day) {
		this.type = type;
		this.day = day;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getDay() {
		return day;
	}

	public void setDay(Integer day) {
		this.day = day;
	}

}
