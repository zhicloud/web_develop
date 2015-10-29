package com.zhicloud.op.vo;

import java.math.BigDecimal;
import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import com.zhicloud.op.common.util.CapacityUtil;
import com.zhicloud.op.common.util.FlowUtil;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.common.util.json.JSONBean;
import com.zhicloud.op.common.util.json.JSONLibUtil;
import com.zhicloud.op.service.constant.AppConstant;
@XmlAccessorType(XmlAccessType.FIELD)
public class CloudHostVO implements JSONBean
{

	
	private String id;
	@XmlTransient
	private String realHostId;
	@XmlTransient
	private Integer type;
	@XmlElement(name="user_id")
	private String userId;
	@XmlTransient
	private String userAccount;
	@XmlTransient
	private String hostName;
	@XmlTransient
	private String account;
	@XmlTransient
	private String password;
	@XmlElement(name="cpu_core")
	private Integer cpuCore;
	private BigInteger memory = BigInteger.ZERO;
	@XmlTransient 
	private String sysImageId;
	@XmlElement(name="sys_disk")
	private BigInteger sysDisk = BigInteger.ZERO;
	@XmlElement(name="data_disk")
	private BigInteger dataDisk = BigInteger.ZERO;
	private BigInteger bandwidth = BigInteger.ZERO;
	@XmlTransient 
	private Integer isAutoStartup;
	@XmlElement(name="running_status")
	private Integer runningStatus;
	private Integer status;
	@XmlElement(name="inner_ip")
	private String innerIp;
	@XmlElement(name="inner_port")
	private Integer innerPort;
	@XmlElement(name="outer_ip")
	private String outerIp;
	@XmlElement(name="outer_port")
	private Integer outerPort;
	@XmlElement(name="create_time")
	private String createTime;
	@XmlElement(name="monthly_price")
	private BigDecimal monthlyPrice;
	private Integer region;
	@XmlElement(name="inactivate_time")
	private String inactivateTime;
	@XmlElement(name="process_status") 
	private Integer processStatus;
	@XmlElement(name="sys_image_name")
	private String sysImageName;
	private String sysImageNameOld;
	@XmlElement(name="host_name")
	private String displayName;
	private String vpcIp;
	
	private String packageId;
	private String description;
	private String username;
	private String markname;
	private String belong_account;
	//导出翻译字段
	private String cpuname;
	private String memoryname;
	private String sysdiskname;
	private String datadiskname;
	private String bandwidthname;
	private String regionname;
	private String phone;
	private String email;
	private BigDecimal account_balance;
	private String hostanddisplay;
	private String systemanddatadisk;
	private String outerandinnerip;
	private String belong_accountandusername;
	private String everymonth;
	private String inactivateTimeText;
	
	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getRealHostId()
	{
		return realHostId;
	}

	public void setRealHostId(String realHostId)
	{
		this.realHostId = realHostId;
	}

	public Integer getType()
	{
		return type;
	}

	public void setType(Integer type)
	{
		this.type = type;
	}

	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public String getUserAccount()
	{
		return userAccount;
	}

	public void setUserAccount(String userAccount)
	{
		this.userAccount = userAccount;
	}

	public String getHostName()
	{
		return hostName;
	}

	public void setHostName(String hostName)
	{
		this.hostName = hostName;
	}

	public String getAccount()
	{
		return account;
	}

	public void setAccount(String account)
	{
		this.account = account;
	}

	public String getPassword()
	{
		return password;
	}

	public void setPassword(String password)
	{
		this.password = password;
	}

	public Integer getCpuCore()
	{
		return cpuCore;
	}

	public void setCpuCore(Integer cpuCore)
	{
		this.cpuCore = cpuCore;
	}
	
	public BigInteger getMemory()
	{
		return memory;
	}
	
	public void setMemory(BigInteger memory)
	{
		this.memory = memory;
	}

	public String getMemoryText(int scale)
	{
		return CapacityUtil.toCapacityLabel(this.memory, scale);
	}
	
	public String getSysImageId()
	{
		return sysImageId;
	}

	public void setSysImageId(String sysImageId)
	{
		this.sysImageId = sysImageId;
	}

	public BigInteger getSysDisk()
	{
		return sysDisk;
	}

	public void setSysDisk(BigInteger sysDisk)
	{
		this.sysDisk = sysDisk;
	}
	
	public String getSysDiskText(int scale)
	{
		return CapacityUtil.toCapacityLabel(this.sysDisk, scale);
	}

	public BigInteger getDataDisk()
	{
		return dataDisk;
	}
	
	public void setDataDisk(BigInteger dataDisk)
	{
		this.dataDisk = dataDisk;
	}
	
	public String getDataDiskText(int scale)
	{
		return CapacityUtil.toCapacityLabel(this.dataDisk, scale);
	}
	
	public BigInteger getBandwidth()
	{
		return bandwidth;
	}
	
	public void setBandwidth(BigInteger bandwidth)
	{
		this.bandwidth = bandwidth;
	}
	
	public String getBandwidthText(int scale)
	{
		return FlowUtil.toFlowLabel(this.bandwidth, scale);
	}
	
	public Integer getIsAutoStartup()
	{
		return isAutoStartup;
	}

	public void setIsAutoStartup(Integer isAutoStartup)
	{
		this.isAutoStartup = isAutoStartup;
	}

	public Integer getRunningStatus()
	{
		return runningStatus;
	}

	public void setRunningStatus(Integer runningStatus)
	{
		this.runningStatus = runningStatus;
	}

	public Integer getStatus()
	{
		return status;
	}

	public void setStatus(Integer status)
	{
		this.status = status;
	}
	
	public Integer getStatusText()
	{
		return status;
	}

	public String getInnerIp()
	{
		return innerIp;
	}

	public void setInnerIp(String innerIp)
	{
		this.innerIp = innerIp;
	}

	public Integer getInnerPort()
	{
		return innerPort;
	}

	public void setInnerPort(Integer innerPort)
	{
		this.innerPort = innerPort;
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

	public String getOuterIp()
	{
		return outerIp;
	}
	
	public void setOuterIp(String outerIp)
	{
		this.outerIp = outerIp;
	}

	public Integer getOuterPort()
	{
		return outerPort;
	}

	public void setOuterPort(Integer outerPort)
	{
		this.outerPort = outerPort;
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
	
	public String getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(String createTime)
	{
		this.createTime = createTime;
	}

	public String getSummarizedStatusText()
	{
		if (this.processStatus != null && (this.processStatus == 0 || this.processStatus == 3))
		{
			return "创建中";
		}
		if (this.processStatus != null && this.processStatus == 2)
		{
			return "创建失败";
		}
		if (this.realHostId == null || this.realHostId.length() == 0)
		{
			return "创建中";
		}
		if (this.status == AppConstant.CLOUD_HOST_STATUS_1_NORNAL)
		{
			if (this.runningStatus == AppConstant.CLOUD_HOST_RUNNING_STATUS_RUNNING)
			{
				return "运行";
			}
			else if(this.runningStatus == AppConstant.CLOUD_HOST_RUNNING_STATUS_ALARM)
			{
				return "告警";
			}
			else if(this.runningStatus == AppConstant.CLOUD_HOST_RUNNING_STATUS_FAULT)
			{
				return "故障";
			}
			else if(this.runningStatus == AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN)
			{
				return "关机";
			}
			else if(this.runningStatus == AppConstant.CLOUD_HOST_RUNNING_STATUS_STARTING)
			{
				return "开机中";
			}
			else if(this.runningStatus == AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTTING_DOWN)
			{
				return "关机中";
			}
			else if(this.runningStatus == AppConstant.CLOUD_HOST_RUNNING_STATUS_RESTARTING)
            {
                return "重启中";
            }
			else{
				return "其他";
			}
		}
		else if (this.status == AppConstant.CLOUD_HOST_STATUS_2_HALT)
		{
			return "停机";
		}
		else if (this.status == AppConstant.CLOUD_HOST_STATUS_3_ARREARAGE)
		{
			return "欠费";
		}
		else
		{
			return "其它";
		}
	}
	
	public static void main(String[] args)
	{
		CloudHostVO vo = new CloudHostVO();
		System.out.println(JSONLibUtil.toJSONString(vo));
	}

	public Integer getProcessStatus() {
		return processStatus;
	}

	public void setProcessStatus(Integer processStatus) {
		this.processStatus = processStatus;
	}

	public BigDecimal getMonthlyPrice()
	{
		return monthlyPrice;
	}

	public void setMonthlyPrice(BigDecimal monthlyPrice)
	{
		this.monthlyPrice = monthlyPrice;
	}

	public Integer getRegion()
	{
		return region;
	}

	public void setRegion(Integer region)
	{
		this.region = region;
	}

	public String getSysImageName() {
		return sysImageName;
	}

	public void setSysImageName(String sysImageName) {
		this.sysImageName = sysImageName;
	}

	public String getInactivateTime() {
		return inactivateTime;
	}

	public void setInactivateTime(String inactivateTime) {
		this.inactivateTime = inactivateTime;
	}

	public String getSysImageNameOld() {
		return sysImageNameOld;
	}

	public void setSysImageNameOld(String sysImageNameOld) {
		this.sysImageNameOld = sysImageNameOld;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getPackageId() {
		return packageId;
	}

	public void setPackageId(String packageId) {
		this.packageId = packageId;
	}

	public String getVpcIp() {
		return vpcIp;
	}

	public void setVpcIp(String vpcIp) {
		this.vpcIp = vpcIp;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMarkname() {
        return markname;
    }

    public void setMarkname(String markname) {
        this.markname = markname;
    }

    public String getBelong_account() {
        return belong_account;
    }

    public void setBelong_account(String belong_account) {
        this.belong_account = belong_account;
    }

    public String getCpuname() {
        return cpuname;
    }

    public void setCpuname(String cpuname) {
        this.cpuname = cpuname;
    }

    public String getMemoryname() {
        return memoryname;
    }

    public void setMemoryname(String memoryname) {
        this.memoryname = memoryname;
    }

    public String getSysdiskname() {
        return sysdiskname;
    }

    public void setSysdiskname(String sysdiskname) {
        this.sysdiskname = sysdiskname;
    }

    public String getDatadiskname() {
        return datadiskname;
    }

    public void setDatadiskname(String datadiskname) {
        this.datadiskname = datadiskname;
    }

    public String getBandwidthname() {
        return bandwidthname;
    }

    public void setBandwidthname(String bandwidthname) {
        this.bandwidthname = bandwidthname;
    }

    public String getRegionname() {
        return regionname;
    }

    public void setRegionname(String regionname) {
        this.regionname = regionname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public BigDecimal getAccount_balance() {
        return account_balance;
    }

    public void setAccount_balance(BigDecimal account_balance) {
        this.account_balance = account_balance;
    }

    public String getHostanddisplay() {
        return hostanddisplay;
    }

    public void setHostanddisplay(String hostanddisplay) {
        this.hostanddisplay = hostanddisplay;
    }

    public String getSystemanddatadisk() {
        return systemanddatadisk;
    }

    public void setSystemanddatadisk(String systemanddatadisk) {
        this.systemanddatadisk = systemanddatadisk;
    }

    public String getOuterandinnerip() {
        return outerandinnerip;
    }

    public void setOuterandinnerip(String outerandinnerip) {
        this.outerandinnerip = outerandinnerip;
    }

    public String getBelong_accountandusername() {
        return belong_accountandusername;
    }

    public void setBelong_accountandusername(String belong_accountandusername) {
        this.belong_accountandusername = belong_accountandusername;
    }

    public String getEverymonth() {
        return everymonth;
    }

    public void setEverymonth(String everymonth) {
        this.everymonth = everymonth;
    }

    public String getInactivateTimeText() {
        return inactivateTimeText;
    }

    public void setInactivateTimeText(String inactivateTimeText) {
        this.inactivateTimeText = inactivateTimeText;
    }
	
	
}
