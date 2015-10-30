package com.zhicloud.ms.app.pool.computePool;

import com.zhicloud.ms.util.json.JSONBean;

import java.util.Date;

public class ComputeInfoExt extends ComputeInfo implements JSONBean{

	  private String ip;
	  private String[] hostIps;
    private Integer networkType;    // 计算资源网络类型，0=私有云，1=独享公网地址（ip），2=共享公网地址（端口）, 3 = 直连
    private String network;         // networkType=1、2时不为空，地址资源池uuid或端口池uuid
    private Integer diskType;       // 挂载类型：0=本地磁盘，1=云存储，2=nas磁盘，3=ip san
    private String diskSource;      // diskType=1时不为空，云存储时，对应的存储资源池
    private Integer[] mode;         // (默认值为全0)list of 0/1,[开启高可用, 开启自动QoS调整,开启thin provioning,开启backing image]
    private Integer mode0;
    private Integer mode1;
    private Integer mode2;
    private Integer mode3;
    private long lastUpdateTime = 0;// 最新的更新时间
    private String message;         // 返回消息
    private int asyncStatus = -1;   // 异步通讯状态，-1：正在等待回调，0：操作失败，1：操作成功
    private String path; 			//共享存储/NAS专用，需要连接的共享存储路径
    private String crypt;			//共享存储/NAS专用，共享存储连接信息


	  public String getIp() {
		return ip;
	}

	   public void setIp(String ip) {
		this.ip = ip;
	}

	   public String[] getHostIps() {
		return hostIps;
	}

    public String getOuterIp(){
		return this.hostIps[1];
	}

    public String getInnerIp(){
		return this.hostIps[0];
	}

    public void setHostIps(String[] hostIps) {
		this.hostIps = hostIps;
	}

    public Integer getNetworkType() {
        return networkType;
    }

    public void setNetworkType(Integer networkType) {
        this.networkType = networkType;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public Integer getDiskType() {
        return diskType;
    }

    public void setDiskType(Integer diskType) {
        this.diskType = diskType;
    }

    public String getDiskSource() {
        return diskSource;
    }

    public void setDiskSource(String diskSource) {
        this.diskSource = diskSource;
    }

    public Integer[] getMode() {
        return mode;
    }

    public void setMode(Integer[] mode) {
        this.mode = mode;
    }

    public Integer getMode0() {
        return mode0;
    }

    public void setMode0(Integer mode0) {
        this.mode0 = mode0;
    }

    public Integer getMode1() {
        return mode1;
    }

    public void setMode1(Integer mode1) {
        this.mode1 = mode1;
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

    public Integer getMode2() {
        return mode2;
    }

    public void setMode2(Integer mode2) {
        this.mode2 = mode2;
    }

    public Integer getMode3() {
        return mode3;
    }

    public void setMode3(Integer mode3) {
        this.mode3 = mode3;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getCrypt() {
        return crypt;
    }

    public void setCrypt(String crypt) {
        this.crypt = crypt;
    }


    public ComputeInfoExt clone() {
        ComputeInfoExt duplication = new ComputeInfoExt();
        duplication.setUuid(this.getUuid());
        duplication.setName(this.getName());
        duplication.setIp(this.ip);
        duplication.setHostIps(this.hostIps);
        duplication.setNetworkType(this.getNetworkType());
        duplication.setNetwork(this.getNetwork());
        duplication.setDiskType(this.getDiskType());
        duplication.setDiskSource(this.getDiskSource());
        duplication.setMode(this.getMode());
        duplication.setMode0(this.getMode0());
        duplication.setMode1(this.getMode1());
        duplication.setMode2(this.getMode2());
        duplication.setMode3(this.getMode3());
        duplication.setPath(this.path);
        duplication.setCrypt(this.crypt);

        return duplication;
    }
	
}
