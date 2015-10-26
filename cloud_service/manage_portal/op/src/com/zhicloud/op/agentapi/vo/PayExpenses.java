package com.zhicloud.op.agentapi.vo;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class PayExpenses {
	@XmlElement(name="host_name")
	private String hostName;
	@XmlElement(name="start_time")
	private String startTime;
	@XmlElement(name="end_time")
	private String endTime;
	@XmlElement(name="pay_expenses")
	private BigDecimal payExpenses;
	public String getHostName() {
		return hostName;
	}
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public BigDecimal getPayExpenses() {
		return payExpenses;
	}
	public void setPayExpenses(BigDecimal payExpenses) {
		this.payExpenses = payExpenses;
	} 
}
