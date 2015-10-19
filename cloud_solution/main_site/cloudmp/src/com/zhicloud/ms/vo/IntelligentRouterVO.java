package com.zhicloud.ms.vo;

import java.math.BigInteger;

import com.zhicloud.ms.common.util.json.JSONBean;

  
public class IntelligentRouterVO implements JSONBean{
	
	private String target;
	
	private Integer mode;//规则
	
	private BigInteger port[]; //开放端口
	
	private Integer region; // 地域
	
	private String ip[];//Ip; 
	
 
	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public Integer getMode() {
		return mode;
	}

	public void setMode(Integer mode) {
		this.mode = mode;
	} 

	public Integer getRegion() {
		return region;
	}

	public void setRegion(Integer region) {
		this.region = region;
	}

	public BigInteger[] getPort() {
		return port;
	}

	public void setPort(BigInteger[] port) {
		this.port = port;
	}

	public String[] getIp() {
		return ip;
	}

	public void setIp(String[] ip) {
		this.ip = ip;
	} 
	
	
	
	
	

}
