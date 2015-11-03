package com.zhicloud.ms.vo; 


import com.zhicloud.ms.util.json.JSONBean;

/**
 * @author ZYFTMX
 *
 */
public class BalancerConfigVO implements JSONBean{
	
	private String id;
	private String hostId; //负载均衡器主机的ID,非真实ID
	private Integer maxConnect; //最大连接数
	private Integer isKeepSession; //是否保存会话
	private Integer keepSessionTime; //会话保存时间(单位：分钟)
	private String checkProtocol; //健康检查协议
	private Integer responseTimeout; //响应超时时间(单位：秒)
	private Integer checkInterval; //健康检查间隔(单位：秒)
	private Integer unhealthyValue; //不健康阀值(次)
	private Integer healthyValue; //健康阀值(次)
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	public String getHostId() {
		return hostId;
	}
	public void setHostId(String hostId) {
		this.hostId = hostId;
	}
	public Integer getMaxConnect() {
		return maxConnect;
	}
	public void setMaxConnect(Integer maxConnect) {
		this.maxConnect = maxConnect;
	}
	public Integer getIsKeepSession() {
		return isKeepSession;
	}
	public void setIsKeepSession(Integer isKeepSession) {
		this.isKeepSession = isKeepSession;
	}
	public Integer getKeepSessionTime() {
		return keepSessionTime;
	}
	public void setKeepSessionTime(Integer keepSessionTime) {
		this.keepSessionTime = keepSessionTime;
	}
	public String getCheckProtocol() {
		return checkProtocol;
	}
	public void setCheckProtocol(String checkProtocol) {
		this.checkProtocol = checkProtocol;
	}
	public Integer getResponseTimeout() {
		return responseTimeout;
	}
	public void setResponseTimeout(Integer responseTimeout) {
		this.responseTimeout = responseTimeout;
	}
	public Integer getCheckInterval() {
		return checkInterval;
	}
	public void setCheckInterval(Integer checkInterval) {
		this.checkInterval = checkInterval;
	}
	public Integer getUnhealthyValue() {
		return unhealthyValue;
	}
	public void setUnhealthyValue(Integer unhealthyValue) {
		this.unhealthyValue = unhealthyValue;
	}
	public Integer getHealthyValue() {
		return healthyValue;
	}
	public void setHealthyValue(Integer healthyValue) {
		this.healthyValue = healthyValue;
	}
	
	

}

