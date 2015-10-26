package com.zhicloud.op.app.pool.rule;

import java.math.BigInteger;
import java.util.Date;

 
public class RuleInfo {

	private String target;// ip资源池uuid：只在创建时，有效
	private String sessionId;// channel的会话id
 	private String message;// 返回消息
	private int asyncStatus = -1;// 异步通讯状态，-1：正在等待回调，0：操作失败，1：操作成功

	private BigInteger[] portList = new BigInteger[0];// 关联云主机列表，用于查询关联云主机请求
	private String[] ipList = new String[0];// 关联云主机uuid，用于关联云主机请求
	private String[] mode = new String[0];// 关联云主机vpc ip，用于关联云主机请求 
	private long lastUpdateTime = 0;// 最新的更新时间

	 
	public void updateTime() {
		this.lastUpdateTime = new Date().getTime();
	}

	public void initAsyncStatus() {
		this.asyncStatus = -1;
	}

	public void success() {
		this.asyncStatus = 1;
	}

	public void fail() {
		this.asyncStatus = 0;
	}

	public boolean isSuccess() {
		if (this.asyncStatus == 1) {
			return true;
		} else {
			return false;
		}
	}

	 
	public int getAsyncStatus() {
		return asyncStatus;
	}

	public void setAsyncStatus(int asyncStatus) {
		this.asyncStatus = asyncStatus;
	}

	 

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	 

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public BigInteger[] getPortList() {
		return portList;
	}

	public void setPortList(BigInteger[] portList) {
		this.portList = portList;
	}

	public String[] getIpList() {
		return ipList;
	}

	public void setIpList(String[] ipList) {
		this.ipList = ipList;
	} 
	
	
	public String[] getMode() {
		return mode;
	}

	public void setMode(String[] mode) {
		this.mode = mode;
	}
	

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public RuleInfo clone() {
		RuleInfo ruleInfo = new RuleInfo();

   
   		ruleInfo.setMessage(this.getMessage());
		ruleInfo.setSessionId(this.getSessionId());
		ruleInfo.setIpList(this.getIpList());
		ruleInfo.setPortList(this.getPortList());
		ruleInfo.setMode(this.getMode());
		ruleInfo.setLastUpdateTime(this.getLastUpdateTime());
 
		return ruleInfo;
	}
	

	 

	 

}
