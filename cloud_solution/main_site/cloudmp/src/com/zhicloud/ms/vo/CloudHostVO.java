
/**
 * Project Name:CloudDeskTopMS
 * File Name:CloudHost.java
 * Package Name:com.zhicloud.ms.vo
 * Date:2015年3月16日上午11:39:18
 * 
 *
 */

package com.zhicloud.ms.vo; 


import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;

import com.zhicloud.ms.common.util.DateUtil;
import com.zhicloud.ms.util.CapacityUtil;
import com.zhicloud.ms.util.FlowUtil;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.util.json.JSONBean;
/**
 * ClassName: CloudHost 
 * Function: 主机信息.  
 * date: 2015年3月16日 上午11:39:18 
 *
 * @author sasa
 * @version 
 * @since JDK 1.7
 */ 
public class CloudHostVO implements JSONBean
{

	
	private String id; 
	private String realHostId; 
	private Integer type; 
	private String userId; 
	private String userAccount; 
	private String hostName;  
	private String account; 
	private String password; 
	private Integer cpuCore;
	private BigInteger memory = BigInteger.ZERO; 
	private String sysImageId; 
	private BigInteger sysDisk = BigInteger.ZERO; 
	private BigInteger dataDisk = BigInteger.ZERO;
	private BigInteger bandwidth = BigInteger.ZERO; 
	private Integer diskdiy;
	private Integer bandwidthdiy;
	private Integer isAutoStartup = 0; 
	private Integer runningStatus;
	private Integer status = 0; 
	private String innerIp; 
	private Integer innerPort; 
	private String outerIp; 
	private Integer outerPort; 
	private String createTime; 
	private String assignTime;
	private BigDecimal monthlyPrice;
	private Integer region = 1; 
	private String inactivateTime; 
	private Integer processStatus; 
	private String sysImageName;
	private String sysImageNameOld; 
	private String displayName; 
	private String userName;
	private String warehouseId;
	private Date curCreateDate;
	private double cpuUsage = 0.0;
	private double memoryUsage = 0.0; 
	private double diskUsage = 0.0;
	private String modelName;
 	
	private String ports ;
	
	private String sysDiskType;
	
	private String  emptyDisk;
	
	private String poolId;
	private String vpcIp;
 	private String chcmId;
 	
 	private String tenantName;
 	
 	private String tenantId;
 	
    /* 配置翻译 */
    private String properties;
    /* 用户状态翻译 */
    private String userid_name;
    /*CPU字段处理*/
    private String cpucore_name;
    
    private Integer supportH264;
 	
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
	
	public String getMemoryValue()
	{
		return CapacityUtil.toGBValue(this.memory,0).toString();
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
		String d = CapacityUtil.toCapacityLabel(this.dataDisk, scale);
		if("0B".equals(d)){
			return "未启用";
		}
		return d;
	}
	public String getDataDiskValue()
	{
		return CapacityUtil.toGBValue(this.dataDisk,0).toString();
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
	public String getBandwidthValue()
	{
		return FlowUtil.toMbpsValue(bandwidth, 0).toString();
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
		if(createTime!=null){
			try {
				this.setCurCreateDate(DateUtil.stringToDate(createTime, "yyyyMMddHHmmssSSS"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}

	public String getSummarizedStatusText()
	{
		if (this.status == 0)
		{
			return "未创建";
		}
		if (this.status == 3)
		{
			return "创建失败";
		}
		if (this.realHostId == null || this.realHostId.length() == 0)
		{
			return "创建中";
		}
		if(this.status == 9){
			return "正在备份";
		}
		if(this.status == 10){
			return "正在恢复";
		}
		if(this.status == 11){
            return "正在重装";
        }
		if (this.status == 2)
		{
			if (this.runningStatus == 1)
			{
				return "关机";
			}
			else if(this.runningStatus == 2)
			{
				return "运行";
			}
			else if(this.runningStatus == 3)
			{
				return "告警";
			}
			else if(this.runningStatus == 4)
			{
				return "故障";
			}
			else if(this.runningStatus == 5)
            {
                return "开机中。。。";
            }
			else if(this.runningStatus == 6)
            {
                return "关机中。。。";
            }
			else if(this.runningStatus == 7)
            {
                return "重启中。。。";
            }
		}
		return "其他";
		 
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

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(String warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getAssignTime() {
		return assignTime;
	}

	public void setAssignTime(String assignTime) {
		this.assignTime = assignTime;
	} 
	
	public Date getAssignDate(){
		Date assignDate = null;
		if(this.assignTime!=null){
			try {
				assignDate = DateUtil.stringToDate(this.assignTime, "yyyyMMddHHmmssSSS");
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		return assignDate;
	}

	public Date getCurCreateDate() {
		return curCreateDate;
	}

	public void setCurCreateDate(Date curCreateDate) {
		this.curCreateDate = curCreateDate;
	}

	public double getCpuUsage() {
		return cpuUsage;
	}

	public void setCpuUsage(double cpuUsage) {
		this.cpuUsage = cpuUsage;
	}

	public double getMemoryUsage() {
		return memoryUsage;
	}

	public void setMemoryUsage(double memoryUsage) {
		this.memoryUsage = memoryUsage;
	}

	public double getDiskUsage() {
		return diskUsage;
	}

	public void setDiskUsage(double diskUsage) {
		this.diskUsage = diskUsage;
	}
	
	/**
	 * @param type 
	 * @return
	 * 1:cpuUsage
	 * 2:memoryUsage
	 * 3:diskUsage
	 */
	public String getUsageFormat(int type){
		if(type==1){
			Double usage = this.cpuUsage * 100;
			String u = String.format("%.2f", usage);
			return u+"%"; 
		}else if(type == 2){
			Double usage = this.memoryUsage * 100;
			String u = String.format("%.2f", usage);
			return u+"%"; 
		}else if(type == 3){
			Double usage = this.diskUsage * 100;
			String u = String.format("%.2f", usage);
			return u+"%"; 
		}else {
			return "0.0%";
		}
	}

	public Integer getDiskdiy() {
		return diskdiy;
	}

	public void setDiskdiy(Integer diskdiy) {
		this.diskdiy = diskdiy;
	}

	public Integer getBandwidthdiy() {
		return bandwidthdiy;
	}

	public void setBandwidthdiy(Integer bandwidthdiy) {
		this.bandwidthdiy = bandwidthdiy;
	}
  

	public String getSysDiskType() {
		return sysDiskType;
	}

	public void setSysDiskType(String sysDiskType) {
		this.sysDiskType = sysDiskType;
	}

	public String getPorts() {
		return ports;
	}

	public void setPorts(String ports) {
		this.ports = ports;
	}

	public String getEmptyDisk() {
		return emptyDisk;
	}

	public void setEmptyDisk(String emptyDisk) {
		this.emptyDisk = emptyDisk;
	}
	
	
	
	
	
	 

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getPoolId() {
		return poolId;
	}

	public void setPoolId(String poolId) {
		this.poolId = poolId;
	}
	
	public String getStatusTextFromComputePool(){
        if(this.status == 0){
            return "运行";
        }else if(this.status == 1){
            return "告警";
        }else if(this.status == 2){
            return "故障";
        }else {
            return "关机";
        }
    }

	public String getVpcIp() {
		return vpcIp;
	}

	public void setVpcIp(String vpcIp) {
		this.vpcIp = vpcIp;
	}

	public String getChcmId() {
		return chcmId;
	}

	public void setChcmId(String chcmId) {
		this.chcmId = chcmId;
	}

    public String getTenantName() {
        return tenantName;
    }

    public void setTenantName(String tenantName) {
        this.tenantName = tenantName;
    }

    public String getTenantId() {
        return tenantId;
    }

    public void setTenantId(String tenantId) {
        this.tenantId = tenantId;
    }

    /**
     * @Description:导出需要，拼装数据
     * @return String
     */
    public String getProperties() {
        properties = cpuCore + "核/" + getMemoryText(0) + "/" + getDataDiskText(0) + "/" + getBandwidthText(0);
        return properties;
    }

    /**
     * @Description:导出需要,拼装数据
     * @return String
     */
    public String getUserid_name() {
        if (userId == null || userId.isEmpty()) {
            userid_name = "未分配";
        } else {
            userid_name = "已分配";
        }
        return userid_name;
    }

    public String getCpucore_name() {
        cpucore_name = cpuCore + "核";
        return cpucore_name;
    }
	
    /**
     * @Description:内存
     * @return String
     */
    public String getMemory_name() {
        return getMemoryText(0);
    }

    /**
     * @Description:磁盘
     * @return String
     */
    public String getDisk_name() {
        return getDataDiskText(0);
    }
	
    /**
     * @Description:cpu利用率
     * @return String
     */
    public String getCpuusage() {
        return getUsageFormat(1);
    }
    
    /**
     * @Description:内存利用率
     * @return String
     */
    public String getMemeoryusage() {
        return getUsageFormat(2);
    }
    
    /**
     * @Description:磁盘利用率
     * @return String
     */
    public String getDiskusage() {
        return getUsageFormat(3);
    }
     
    public String getOuterIpAndPort(){
        if(StringUtil.isBlank(outerIp)){
            return "无";
        }
        return outerIp+":"+"outerPort";
    } 

	public Integer getSupportH264() {
		return supportH264;
	}

	public void setSupportH264(Integer supportH264) {
		this.supportH264 = supportH264;
	}
    
    
}


