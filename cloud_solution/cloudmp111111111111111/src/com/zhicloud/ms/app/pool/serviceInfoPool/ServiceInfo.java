package com.zhicloud.ms.app.pool.serviceInfoPool;

import com.zhicloud.ms.common.util.constant.NodeTypeDefine;
import com.zhicloud.ms.common.util.json.JSONBean;

public class ServiceInfo implements JSONBean{

	private int region;//区域
	private int type = NodeTypeDefine.INVALID;//服务类型，请参考NodeTypeDefine
	private String group = "default";//服务组名称
	private String name;//服务名称
	private String ip;//ip
	private int port;//端口
	private int status;//状态：0=正常,1=告警,2=故障,3=停止
	private String version;//版本号
	private String newStatus;//状态显示值
  private int diskType; //存储模式 0: 本地存储 1:共享存储
  private String diskSource;  //存储源信息，比如共享路径
  private String crypt; //存储校验信息


	public int getRegion() {
		return region;
	}

	public void setRegion(int region) {
		this.region = region;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
	
	public String getNewStatus() {
        return newStatus;
    }

    public void setNewStatus(String newStatus) {
        this.newStatus = newStatus;
    }

    public int getDiskType() {
        return diskType;
    }

    public void setDiskType(int diskType) {
        this.diskType = diskType;
    }

    public String getDiskSource() {
        return diskSource;
    }

    public void setDiskSource(String diskSource) {
        this.diskSource = diskSource;
    }

    public String getCrypt() {
        return crypt;
    }

    public void setCrypt(String crypt) {
        this.crypt = crypt;
    }

    public String getTypeText() {
		if(this.type==0){
			return "INVALID";
		}else if(this.type == 1){
			return "DATA_SERVER";
		}else if(this.type == 2){
			return "CONTROL_SERVER";
		}else if(this.type == 3){
			return "NODE_CLIENT";
		}else if(this.type == 4){
			return "STORAGE_SERVER";
		}else if(this.type == 5){
			return "STATISTIC_SERVER";
		}else if(this.type == 6){
			return "MANAGE_TERMINAL";
		}else if(this.type == 7){
			return "HTTP_GATEWAY";
		}else if(this.type == 8){
			return "DATA_INDEX";
		}else if(this.type == 9){
			return "DATA_NODE";
		}else if(this.type == 10){
			return "STORAGE_MANAGER";
		}else if(this.type == 11){
			return "STORAGE_CLIENT";
		}else if(this.type == 12){
			return "STORAGE_PROTAL";
		}else if(this.type == 13){
			return "STORAGE_OBJECT";
		}else if(this.type == 14){
			return "INTELLIGENT_REOUTER";
		}else{
			return "未知";
		}
	}

	public String getRegionText() {
		if(this.region==1){
			return "广州";
		}else if(this.region==2){
			return "成都";
		}else if(this.region==3){
			return "北京";
		}else{
			return "香港";
		}
	}

	public String getStatusText() {
		if(this.status==0){
			return "正常";
		}else if(this.status==1){
			return "告警";
		}else if(this.status==2){
			return "故障";
		}else{
			return "停止";
		}
	}

	
	
}
