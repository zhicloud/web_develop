package com.zhicloud.op.vo;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.zhicloud.op.common.util.CapacityUtil;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.common.util.json.JSONBean;
import com.zhicloud.op.service.constant.AppConstant;

public class CloudDiskVO implements JSONBean {

	private String id;
	private  String realDiskId;
	private String userId;
	private String name;
	private String userName;
	private String account;
	private String password;
	private BigInteger disk;
	private Integer runningStatus;
	private Integer status;
	private String innerIp;
	private Integer innerPort;
	private String outerIp;
	private Integer outerPort;
	private String createTime;
	private String inactiveTime;
	private String reactiveTime;
	private BigDecimal monthlyPrice;
	private Integer region;
	private Integer processStatus;
	private String iqn;
	private String ip;
	private Integer type;
	private String accountname;
	private String markname;
	private String belongaccount;
	//翻译显示字段
	private String diskname;
	private String regionname;
	private String statusname;

	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRealDiskId() {
		return realDiskId;
	}
	public void setRealDiskId(String realDiskId) {
		this.realDiskId = realDiskId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public BigInteger getDisk() {
		return disk;
	}
	public void setDisk(BigInteger disk) {
		this.disk = disk;
	}
	public String getDataDiskText(int scale)
	{
		return CapacityUtil.toCapacityLabel(this.disk, scale);
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
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getInactiveTime() {
		return inactiveTime;
	}
	public void setInactiveTime(String inactiveTime) {
		this.inactiveTime = inactiveTime;
	}
	public String getReactiveTime() {
		return reactiveTime;
	}
	public void setReactiveTime(String reactiveTime) {
		this.reactiveTime = reactiveTime; 
	} 
	public Integer getRegion() {
		return region;
	}
	public void setRegion(Integer region) {
		this.region = region;
	}
	public Integer getProcessStatus() {
		return processStatus;
	}
	public void setProcessStatus(Integer processStatus) {
		this.processStatus = processStatus;
	}
	
	public String getInnerMonitorAddr()
	{
		if( StringUtil.isBlank(this.innerIp) )
		{
			return "无";
		}
		StringBuilder sb = new StringBuilder(this.innerIp.trim());
		if( this.innerPort!=null )
		{
			sb.append(":").append(this.innerPort);
		}
		return sb.toString();
	}
	
	public String getOuterMonitorAddr()
	{
		if( StringUtil.isBlank(this.outerIp) )
		{
			return "无";
		}
		StringBuilder sb = new StringBuilder(this.outerIp.trim());
		if( this.outerPort!=null )
		{
			sb.append(":").append(this.outerPort);
		}
		return sb.toString();
	}
	
	public String getSummarizedStatusText()
	{
		if (this.processStatus != null && (this.processStatus == AppConstant.CLOUD_DISK_SHOPPING_CONFIG_PROCESS_STATUS_NOT_PROCESSED || this.processStatus == AppConstant.CLOUD_DISK_SHOPPING_CONFIG_PROCESS_STATUS_CREATING))
		{
			return "创建中";
		}
		if (this.processStatus != null && this.processStatus == AppConstant.CLOUD_DISK_SHOPPING_CONFIG_PROCESS_STATUS_FAIL)
		{
			return "创建失败";
		}
		if (this.realDiskId == null || this.realDiskId.length() == 0)
		{
			return "创建中";
		}
		if (this.status == AppConstant.CLOUD_DISK_STATUS_NORMAL)
		{
			if (this.runningStatus == AppConstant.CLOUD_DISK_RUNNING_STATUS_RUNNING)
			{
				return "运行";
			}
			else if(this.runningStatus == AppConstant.CLOUD_DISK_RUNNING_STATUS_ALARM)
			{
				return "告警";
			}
			else if(this.runningStatus == AppConstant.CLOUD_DISK_RUNNING_STATUS_FAULT)
			{
				return "故障";
			}
			else
			{
				return "关机";
			}
		}
		else if (this.status == AppConstant.CLOUD_DISK_STATUS_HALT)
		{
			return "停机";
		}
		else if (this.status == AppConstant.CLOUD_DISK_STATUS_ARREARAGE)
		{
			return "欠费";
		}
		else
		{
			return "其它";
		}
	}
	public String getIqn() {
		return iqn;
	}
	public void setIqn(String iqn) {
		this.iqn = iqn;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Integer getType() {
		return type;
	}
	public void setType(Integer type) {
		this.type = type;
	}
	public BigDecimal getMonthlyPrice() {
		return monthlyPrice;
	}
	public void setMonthlyPrice(BigDecimal monthlyPrice) {
		this.monthlyPrice = monthlyPrice;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}

    public String getAccountname() {
        return accountname;
    }
    public void setAccountname(String accountname) {
        this.accountname = accountname;
    }
    public String getMarkname() {
        return markname;
    }
    public void setMarkname(String markname) {
        this.markname = markname;
    }
    public String getBelongaccount() {
        return belongaccount;
    }
    public void setBelongaccount(String belongaccount) {
        this.belongaccount = belongaccount;
    }
    public String getDiskname() {
        return diskname;
    }
    public void setDiskname(String diskname) {
        this.diskname = diskname;
    }
    public String getRegionname() {
        return regionname;
    }
    public void setRegionname(String regionname) {
        this.regionname = regionname;
    }
    public String getStatusname() {
        return statusname;
    }
    public void setStatusname(String statusname) {
        this.statusname = statusname;
    }
	
	
	
	
	
}
