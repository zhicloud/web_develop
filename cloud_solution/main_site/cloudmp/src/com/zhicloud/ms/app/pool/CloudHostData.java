package com.zhicloud.ms.app.pool;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.zhicloud.ms.common.util.json.JSONBean;

/**
 * 云主机信息
 * 
 * @author Administrator
 *
 */
public class CloudHostData implements JSONBean {

	private Integer region;
	private String realHostId;
	private String hostName;
	private Integer cpuCore;
	private Double cpuUsage;
	private BigInteger memory = BigInteger.ZERO;
	private Double memoryUsage = 0.0;
	private BigInteger sysDisk = BigInteger.ZERO;
	private Double sysDiskUsage = 0.0;
	private BigInteger dataDisk = BigInteger.ZERO;
	private Double dataDiskUsage = 0.0;
	private BigInteger bandwidth = BigInteger.ZERO;
	private Integer isAutoStartup;
	private Integer runningStatus;
	private Integer status;
	private String innerIp;
	private Integer innerPort;
	private String outerIp;
	private Integer outerPort;
	// monitor data
	private BigInteger diskIOReadTimes = BigInteger.ZERO;// 读请求
	private BigInteger diskIOReadByte = BigInteger.ZERO;// 读字节
	private BigInteger diskIOWriteTimes = BigInteger.ZERO;// 写请求
	private BigInteger diskIOWriteByte = BigInteger.ZERO;// 写字节
	private BigInteger diskIOErrorTimes = BigInteger.ZERO;// IO错误次数

	private BigInteger networkIOReceiveByte = BigInteger.ZERO;// 接收字节
	private BigInteger networkIOReceivePackage = BigInteger.ZERO;// 接收包
	private BigInteger networkIOReceiveError = BigInteger.ZERO;// 接收错误
	private BigInteger networkIOReceiveLoss = BigInteger.ZERO;// 接收丢包
	private BigInteger networkIOSendByte = BigInteger.ZERO;// 发送字节
	private BigInteger networkIOSendPackage = BigInteger.ZERO;// 发送包
	private BigInteger networkIOSendError = BigInteger.ZERO;// 发送错误
	private BigInteger networkIOSendLoss = BigInteger.ZERO;// 发送丢包

	private BigInteger readSpeed = BigInteger.ZERO;// 读速度-单位:字节/s
	private BigInteger writeSpeed = BigInteger.ZERO;// 写速度-单位:字节/s
	private BigInteger receiveSpeed = BigInteger.ZERO;// 接收速度-单位:字节/s
	private BigInteger sendSpeed = BigInteger.ZERO;// 发送速度-单位:字节/s
	private long timestamp = 0;//时间戳，定义获取该云主机信息的时间
	private List<RealCloudHostPort> ports = new ArrayList<RealCloudHostPort>();
	private Integer lastStatus;//上次操作前状态， 
    private String lastOperTime; //上次操作时间
    private Integer lastOperStatus; //上次操作的状态 2 表示成功
    
    private String poolId;

	public CloudHostData clone() {
		CloudHostData newData = new CloudHostData();
		// host data
		newData.setRegion(this.getRegion());
		newData.setRealHostId(this.getRealHostId());
		newData.setHostName(this.getHostName());
		newData.setCpuCore(this.getCpuCore());
		newData.setCpuUsage(this.getCpuUsage());
		newData.setMemory(this.getMemory());
		newData.setMemoryUsage(this.getMemoryUsage());
		newData.setSysDisk(this.getSysDisk());
		newData.setSysDiskUsage(this.getSysDiskUsage());
		newData.setDataDisk(this.getDataDisk());
		newData.setDataDiskUsage(this.getDataDiskUsage());
		newData.setBandwidth(this.getBandwidth());
		newData.setIsAutoStartup(this.getIsAutoStartup());
		newData.setRunningStatus(this.getRunningStatus());
		newData.setStatus(this.getStatus());
		newData.setInnerIp(this.getInnerIp());
		newData.setInnerPort(this.getInnerPort());
		newData.setOuterIp(this.getOuterIp());
		newData.setOuterPort(this.getOuterPort());

		for (RealCloudHostPort port : this.getPorts()) {
			newData.getPorts().add(port.clone());
		}

		// monitor data
		newData.setDiskIOReadTimes(this.getDiskIOReadTimes());
		newData.setDiskIOReadByte(this.getDiskIOReadByte());
		newData.setDiskIOWriteTimes(this.getDiskIOWriteTimes());
		newData.setDiskIOWriteByte(this.getDiskIOWriteByte());
		newData.setDiskIOErrorTimes(this.getDiskIOErrorTimes());

		newData.setNetworkIOReceiveByte(this.getNetworkIOReceiveByte());
		newData.setNetworkIOReceivePackage(this.getNetworkIOReceivePackage());
		newData.setNetworkIOReceiveError(this.getNetworkIOReceiveError());
		newData.setNetworkIOReceiveLoss(this.getNetworkIOReceiveLoss());
		newData.setNetworkIOSendByte(this.getNetworkIOSendByte());
		newData.setNetworkIOSendPackage(this.getNetworkIOSendPackage());
		newData.setNetworkIOSendError(this.getNetworkIOSendError());
		newData.setNetworkIOSendLoss(this.getNetworkIOSendLoss());

		newData.setReadSpeed(this.getReadSpeed());
		newData.setWriteSpeed(this.getWriteSpeed());
		newData.setReceiveSpeed(this.getReceiveSpeed());
		newData.setSendSpeed(this.getSendSpeed());
		newData.setLastStatus(this.lastStatus);
        newData.setLastOperTime(this.lastOperTime);
        newData.setLastOperStatus(this.lastOperStatus);
        newData.setPoolId(this.poolId);

		return newData;
	}

	/**
	 * 云主机开放端口
	 * 
	 * @author Administrator
	 *
	 */
	public class RealCloudHostPort {
		
		private Integer protocol;
		private Integer port;
		private Integer serverPort;
		private Integer outerPort;

		public RealCloudHostPort clone() {
			RealCloudHostPort newPort = new RealCloudHostPort();

			newPort.setProtocol(this.getProtocol());
			newPort.setPort(this.getPort());
			newPort.setServerPort(this.getServerPort());
			newPort.setOuterPort(this.getOuterPort());

			return newPort;
		}

		public Integer getProtocol() {
			return protocol;
		}

		public void setProtocol(Integer protocol) {
			this.protocol = protocol;
		}

		public Integer getPort() {
			return port;
		}

		public void setPort(Integer port) {
			this.port = port;
		}

		public Integer getServerPort() {
			return serverPort;
		}

		public void setServerPort(Integer serverPort) {
			this.serverPort = serverPort;
		}

		public Integer getOuterPort() {
			return outerPort;
		}

		public void setOuterPort(Integer outerPort) {
			this.outerPort = outerPort;
		}
	}

	public Integer getRegion() {
		return region;
	}

	public void setRegion(Integer region) {
		this.region = region;
	}

	public String getRealHostId() {
		return realHostId;
	}

	public void setRealHostId(String realHostId) {
		this.realHostId = realHostId;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public Integer getCpuCore() {
		return cpuCore;
	}

	public void setCpuCore(Integer cpuCore) {
		this.cpuCore = cpuCore;
	}

	public Double getCpuUsage() {
		return cpuUsage;
	}

	public void setCpuUsage(Double cpuUsage) {
		this.cpuUsage = cpuUsage;
	}

	public BigInteger getMemory() {
		return memory;
	}

	public void setMemory(BigInteger memory) {
		this.memory = memory;
	}

	public Double getMemoryUsage() {
		return memoryUsage;
	}

	public void setMemoryUsage(Double memoryUsage) {
		this.memoryUsage = memoryUsage;
	}

	public BigInteger getSysDisk() {
		return sysDisk;
	}

	public void setSysDisk(BigInteger sysDisk) {
		this.sysDisk = sysDisk;
	}

	public Double getSysDiskUsage() {
		return sysDiskUsage;
	}

	public void setSysDiskUsage(Double sysDiskUsage) {
		this.sysDiskUsage = sysDiskUsage;
	}

	public BigInteger getDataDisk() {
		return dataDisk;
	}

	public void setDataDisk(BigInteger dataDisk) {
		this.dataDisk = dataDisk;
	}

	public Double getDataDiskUsage() {
		return dataDiskUsage;
	}

	public void setDataDiskUsage(Double dataDiskUsage) {
		this.dataDiskUsage = dataDiskUsage;
	}

	public BigInteger getBandwidth() {
		return bandwidth;
	}

	public void setBandwidth(BigInteger bandwidth) {
		this.bandwidth = bandwidth;
	}

	public Integer getIsAutoStartup() {
		return isAutoStartup;
	}

	public void setIsAutoStartup(Integer isAutoStartup) {
		this.isAutoStartup = isAutoStartup;
	}

	public Integer getRunningStatus() {
		return runningStatus;
	}

	public void setRunningStatus(Integer runningStatus) {
		this.runningStatus = runningStatus;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getInnerIp() {
		return innerIp;
	}

	public void setInnerIp(String innerIp) {
		this.innerIp = innerIp;
	}

	public Integer getInnerPort() {
		return innerPort;
	}

	public void setInnerPort(Integer innerPort) {
		this.innerPort = innerPort;
	}

	public String getOuterIp() {
		return outerIp;
	}

	public void setOuterIp(String outerIp) {
		this.outerIp = outerIp;
	}

	public Integer getOuterPort() {
		return outerPort;
	}

	public void setOuterPort(Integer outerPort) {
		this.outerPort = outerPort;
	}

	public List<RealCloudHostPort> getPorts() {
		return ports;
	}

	public void addPort(RealCloudHostPort port) {
		this.ports.add(port);
	}

	public BigInteger getDiskIOReadTimes() {
		return diskIOReadTimes;
	}

	public void setDiskIOReadTimes(BigInteger diskIOReadTimes) {
		this.diskIOReadTimes = diskIOReadTimes;
	}

	public BigInteger getDiskIOReadByte() {
		return diskIOReadByte;
	}

	public void setDiskIOReadByte(BigInteger diskIOReadByte) {
		this.diskIOReadByte = diskIOReadByte;
	}

	public BigInteger getDiskIOWriteTimes() {
		return diskIOWriteTimes;
	}

	public void setDiskIOWriteTimes(BigInteger diskIOWriteTimes) {
		this.diskIOWriteTimes = diskIOWriteTimes;
	}

	public BigInteger getDiskIOWriteByte() {
		return diskIOWriteByte;
	}

	public void setDiskIOWriteByte(BigInteger diskIOWriteByte) {
		this.diskIOWriteByte = diskIOWriteByte;
	}

	public BigInteger getDiskIOErrorTimes() {
		return diskIOErrorTimes;
	}

	public void setDiskIOErrorTimes(BigInteger diskIOErrorTimes) {
		this.diskIOErrorTimes = diskIOErrorTimes;
	}

	public BigInteger getNetworkIOReceiveByte() {
		return networkIOReceiveByte;
	}

	public void setNetworkIOReceiveByte(BigInteger networkIOReceiveByte) {
		this.networkIOReceiveByte = networkIOReceiveByte;
	}

	public BigInteger getNetworkIOReceivePackage() {
		return networkIOReceivePackage;
	}

	public void setNetworkIOReceivePackage(BigInteger networkIOReceivePackage) {
		this.networkIOReceivePackage = networkIOReceivePackage;
	}

	public BigInteger getNetworkIOReceiveError() {
		return networkIOReceiveError;
	}

	public void setNetworkIOReceiveError(BigInteger networkIOReceiveError) {
		this.networkIOReceiveError = networkIOReceiveError;
	}

	public BigInteger getNetworkIOReceiveLoss() {
		return networkIOReceiveLoss;
	}

	public void setNetworkIOReceiveLoss(BigInteger networkIOReceiveLoss) {
		this.networkIOReceiveLoss = networkIOReceiveLoss;
	}

	public BigInteger getNetworkIOSendByte() {
		return networkIOSendByte;
	}

	public void setNetworkIOSendByte(BigInteger networkIOSendByte) {
		this.networkIOSendByte = networkIOSendByte;
	}

	public BigInteger getNetworkIOSendPackage() {
		return networkIOSendPackage;
	}

	public void setNetworkIOSendPackage(BigInteger networkIOSendPackage) {
		this.networkIOSendPackage = networkIOSendPackage;
	}

	public BigInteger getNetworkIOSendError() {
		return networkIOSendError;
	}

	public void setNetworkIOSendError(BigInteger networkIOSendError) {
		this.networkIOSendError = networkIOSendError;
	}

	public BigInteger getNetworkIOSendLoss() {
		return networkIOSendLoss;
	}

	public void setNetworkIOSendLoss(BigInteger networkIOSendLoss) {
		this.networkIOSendLoss = networkIOSendLoss;
	}

	public BigInteger getReadSpeed() {
		return readSpeed;
	}

	public void setReadSpeed(BigInteger readSpeed) {
		this.readSpeed = readSpeed;
	}

	public BigInteger getWriteSpeed() {
		return writeSpeed;
	}

	public void setWriteSpeed(BigInteger writeSpeed) {
		this.writeSpeed = writeSpeed;
	}

	public BigInteger getReceiveSpeed() {
		return receiveSpeed;
	}

	public void setReceiveSpeed(BigInteger receiveSpeed) {
		this.receiveSpeed = receiveSpeed;
	}

	public BigInteger getSendSpeed() {
		return sendSpeed;
	}

	public void setSendSpeed(BigInteger sendSpeed) {
		this.sendSpeed = sendSpeed;
	}

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	} 

    public String getLastOperTime() {
        return lastOperTime;
    }

    public void setLastOperTime(String lastOperTime) {
        this.lastOperTime = lastOperTime;
    }

    public void setPorts(List<RealCloudHostPort> ports) {
        this.ports = ports;
    }

    public Integer getLastStatus() {
        return lastStatus;
    }

    public void setLastStatus(Integer lastStatus) {
        this.lastStatus = lastStatus;
    }

    public Integer getLastOperStatus() {
        return lastOperStatus;
    }

    public void setLastOperStatus(Integer lastOperStatus) {
        this.lastOperStatus = lastOperStatus;
    }

    public String getPoolId() {
        return poolId;
    }

    public void setPoolId(String poolId) {
        this.poolId = poolId;
    }
    
    
    
	
}
