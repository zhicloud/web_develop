package com.zhicloud.ms.vo; 


import com.zhicloud.ms.util.json.JSONBean;

/**
 * @author ZYFTMX
 *
 */
public class BalancerForwardVO implements JSONBean{
	
	private String id;
	private String hostId; //负载均衡器主机的ID,非真实ID
	private Integer balancerProtocol; //衡均器议协(0:tcp,1:http,2:https)
	private Integer balancerProtocolPort; //均衡器协议端口1-65535
	private Integer hostProtocol; //主机协议(0:tcp,1:http,2:https)
	private Integer hostProtocolPort; //主机协议端口1-65535
	private Integer forwardRule; //负载均衡转发规则(0加权轮询,1最小链接)
	private Integer maxConnect; //最大连接数1-4096
	private Integer keepalive; //是否保持会话(0:不保持,1:保持)
	private Integer connectTimeout; //响应超时时间（秒）1-1000
	private Integer realIp; //获取客户真实ip，0:关闭，1:开启，【http/https】
	private Integer checkInterval; //健康检查间隔（秒）1-100
	private Integer fall; //不健康阀值（次）1-30
	private Integer rise; //健康阀值（次）1-30
	private String checkFile; //心跳检测文件（带路径）如/test/check.html
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
	public Integer getBalancerProtocol() {
		return balancerProtocol;
	}
	public void setBalancerProtocol(Integer balancerProtocol) {
		this.balancerProtocol = balancerProtocol;
	}
	public Integer getBalancerProtocolPort() {
		return balancerProtocolPort;
	}
	public void setBalancerProtocolPort(Integer balancerProtocolPort) {
		this.balancerProtocolPort = balancerProtocolPort;
	}
	public Integer getHostProtocol() {
		return hostProtocol;
	}
	public void setHostProtocol(Integer hostProtocol) {
		this.hostProtocol = hostProtocol;
	}
	public Integer getHostProtocolPort() {
		return hostProtocolPort;
	}
	public void setHostProtocolPort(Integer hostProtocolPort) {
		this.hostProtocolPort = hostProtocolPort;
	}
	public Integer getForwardRule() {
		return forwardRule;
	}
	public void setForwardRule(Integer forwardRule) {
		this.forwardRule = forwardRule;
	}
	public Integer getMaxConnect() {
		return maxConnect;
	}
	public void setMaxConnect(Integer maxConnect) {
		this.maxConnect = maxConnect;
	}
	public Integer getKeepalive() {
		return keepalive;
	}
	public void setKeepalive(Integer keepalive) {
		this.keepalive = keepalive;
	}
	public Integer getConnectTimeout() {
		return connectTimeout;
	}
	public void setConnectTimeout(Integer connectTimeout) {
		this.connectTimeout = connectTimeout;
	}
	public Integer getRealIp() {
		return realIp;
	}
	public void setRealIp(Integer realIp) {
		this.realIp = realIp;
	}
	public Integer getCheckInterval() {
		return checkInterval;
	}
	public void setCheckInterval(Integer checkInterval) {
		this.checkInterval = checkInterval;
	}
	public Integer getFall() {
		return fall;
	}
	public void setFall(Integer fall) {
		this.fall = fall;
	}
	public Integer getRise() {
		return rise;
	}
	public void setRise(Integer rise) {
		this.rise = rise;
	}
	public String getCheckFile() {
		return checkFile;
	}
	public void setCheckFile(String checkFile) {
		this.checkFile = checkFile;
	}
	
	
}

