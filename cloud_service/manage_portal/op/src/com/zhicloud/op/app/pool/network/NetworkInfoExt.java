package com.zhicloud.op.app.pool.network;

import java.util.Date;

public class NetworkInfoExt extends NetworkInfo {

	private String ipPool;// ip资源池uuid：只在创建时，有效
	private String sessionId;// channel的会话id
	private long lastUpdateTime = 0;// 最新的更新时间
	private String message;// 返回消息
	private int asyncStatus = -1;// 异步通讯状态，-1：正在等待回调，0：操作失败，1：操作成功

	private Host[] hostList = new Host[0];// 关联云主机列表，用于查询关联云主机请求
	private String hostUuid;// 关联云主机uuid，用于关联云主机请求
	private String hostNetworkAddress;// 关联云主机vpc ip，用于关联云主机请求
	private int count;// 申请公网ip的数量，用于申请公网ip请求
	private String[] successIpList = new String[0];// 成功移除的公网ip，用于移除公网ip请求
	private Port[] requestPortList = new Port[0];// 请求绑定的端口，用于绑定端口请求
	private Port[] successPortList = new Port[0];// 成功绑定的端口，用于绑定端口请求

	public String getHostNetworkAddress() {
		return hostNetworkAddress;
	}

	public void setHostNetworkAddress(String hostNetworkAddress) {
		this.hostNetworkAddress = hostNetworkAddress;
	}

	public String getHostUuid() {
		return hostUuid;
	}

	public void setHostUuid(String hostUuid) {
		this.hostUuid = hostUuid;
	}

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

	public Port[] getRequestPortList() {
		return requestPortList;
	}

	public void setRequestPortList(Port[] requestPortList) {
		this.requestPortList = requestPortList;
	}

	public Port[] getSuccessPortList() {
		return successPortList;
	}

	public void setSuccessPortList(Port[] successPortList) {
		this.successPortList = successPortList;
	}

	public String[] getSuccessIpList() {
		return successIpList;
	}

	public void setSuccessIpList(String[] successIpList) {
		this.successIpList = successIpList;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public Host[] getHostList() {
		return hostList;
	}

	public void setHostList(Host[] hostList) {
		this.hostList = hostList;
	}

	public int getAsyncStatus() {
		return asyncStatus;
	}

	public void setAsyncStatus(int asyncStatus) {
		this.asyncStatus = asyncStatus;
	}

	public String getIpPool() {
		return ipPool;
	}

	public void setIpPool(String ipPool) {
		this.ipPool = ipPool;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public long getLastUpdateTime() {
		return lastUpdateTime;
	}

	public void setLastUpdateTime(long lastUpdateTime) {
		this.lastUpdateTime = lastUpdateTime;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public NetworkInfoExt clone() {
		NetworkInfoExt duplication = new NetworkInfoExt();

		duplication.setUuid(this.getUuid());
		duplication.setName(this.getName());
		duplication.setNetmask(this.getNetmask());
		duplication.setDescription(this.getDescription());
		duplication.setNetworkAddress(this.getNetworkAddress());
		duplication.setNetworkAddress(this.getNetworkAddress());
		duplication.setSize(this.getSize());
		duplication.setStatus(this.getStatus());

		String[] ipList = this.getIp();
		String[] ipListCopy = new String[ipList.length];
		for (int i = 0; i < ipList.length; i++) {
			ipListCopy[i] = ipList[i];
		}

		duplication.setIp(ipListCopy);

		duplication.setIpPool(this.getIpPool());
		duplication.setSessionId(this.getSessionId());
		duplication.setLastUpdateTime(this.getLastUpdateTime());
		duplication.setMessage(this.getMessage());
		duplication.setAsyncStatus(this.asyncStatus);

		Host[] hostListCopy = new Host[this.hostList.length];
		for (int i = 0; i < hostList.length; i++) {
			hostListCopy[i] = hostList[i].clone();
		}
		duplication.setHostList(hostListCopy);

		duplication.setHostUuid(this.hostUuid);
		duplication.setHostNetworkAddress(this.hostNetworkAddress);
		duplication.setCount(this.count);

		String[] successIpListCopy = new String[successIpList.length];
		for (int i = 0; i < successIpList.length; i++) {
			successIpListCopy[i] = successIpList[i];
		}
		duplication.setSuccessIpList(successIpListCopy);

		Port[] requestPortListCopy = new Port[this.requestPortList.length];
		for (int i = 0; i < requestPortList.length; i++) {
			requestPortListCopy[i] = requestPortList[i].clone();
		}
		duplication.setRequestPortList(requestPortListCopy);

		Port[] successPortListCopy = new Port[this.successPortList.length];
		for (int i = 0; i < successPortList.length; i++) {
			successPortListCopy[i] = successPortList[i].clone();
		}
		duplication.setSuccessPortList(successPortListCopy);

		return duplication;
	}

	public class Host {
		private String name;
		private String uuid;
		private String networkAddress;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getUuid() {
			return uuid;
		}

		public void setUuid(String uuid) {
			this.uuid = uuid;
		}

		public String getNetworkAddress() {
			return networkAddress;
		}

		public void setNetworkAddress(String networkAddress) {
			this.networkAddress = networkAddress;
		}

		public Host clone() {
			Host duplication = new Host();
			duplication.setName(this.name);
			duplication.setUuid(this.uuid);
			duplication.setNetworkAddress(this.networkAddress);

			return duplication;
		}
	}

	public class Port {
		private String protocol;
		private String publicIp;
		private String publicPort;
		private String hostUuid;
		private String hostPort;

		public String getProtocol() {
			return protocol;
		}

		public void setProtocol(String protocol) {
			this.protocol = protocol;
		}

		public String getPublicIp() {
			return publicIp;
		}

		public void setPublicIp(String publicIp) {
			this.publicIp = publicIp;
		}

		public String getPublicPort() {
			return publicPort;
		}

		public void setPublicPort(String publicPort) {
			this.publicPort = publicPort;
		}

		public String getHostUuid() {
			return hostUuid;
		}

		public void setHostUuid(String hostUuid) {
			this.hostUuid = hostUuid;
		}

		public String getHostPort() {
			return hostPort;
		}

		public void setHostPort(String hostPort) {
			this.hostPort = hostPort;
		}

		public Port clone() {
			Port duplication = new Port();
			duplication.setProtocol(this.protocol);
			duplication.setPublicIp(this.publicIp);
			duplication.setPublicPort(this.publicPort);
			duplication.setHostUuid(this.hostUuid);
			duplication.setHostPort(this.hostPort);

			return duplication;
		}
	}

}
