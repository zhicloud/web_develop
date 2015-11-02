package com.zhicloud.ms.vo; 


import com.zhicloud.ms.util.json.JSONBean;

/**
 * @author ZYFTMX
 *
 */
public class BalancerServiceVO implements JSONBean{
	
	private String id;
	private String hostId;//对应后台真实均衡器的id,非主机真实id
	private String balancerProtocolName;//衡均器议协名称
	private Integer balancerProtocolPort;//均衡器服务端口
	private String hostProtocolName;//实例协议名
	private Integer hostProtocolPort;//实例协议端口
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
	public String getBalancerProtocolName() {
		return balancerProtocolName;
	}
	public void setBalancerProtocolName(String balancerProtocolName) {
		this.balancerProtocolName = balancerProtocolName;
	}
	public Integer getBalancerProtocolPort() {
		return balancerProtocolPort;
	}
	public void setBalancerProtocolPort(Integer balancerProtocolPort) {
		this.balancerProtocolPort = balancerProtocolPort;
	}
	public String getHostProtocolName() {
		return hostProtocolName;
	}
	public void setHostProtocolName(String hostProtocolName) {
		this.hostProtocolName = hostProtocolName;
	}
	public Integer getHostProtocolPort() {
		return hostProtocolPort;
	}
	public void setHostProtocolPort(Integer hostProtocolPort) {
		this.hostProtocolPort = hostProtocolPort;
	}
	
}

