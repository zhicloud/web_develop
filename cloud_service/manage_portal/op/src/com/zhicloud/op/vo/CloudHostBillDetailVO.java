package com.zhicloud.op.vo;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.zhicloud.op.common.util.CapacityUtil;
import com.zhicloud.op.common.util.FlowUtil;
import com.zhicloud.op.common.util.json.JSONBean;
import com.zhicloud.op.service.constant.AppConstant;

public class CloudHostBillDetailVO implements JSONBean
{
	
	/**
	 * 此ID对应bill_detail表的item_id
	 */
	private String id;
	
	/**
	 * 云主机ID
	 */
	private String hostId;
	
	/**
	 * 付费类型
	 * 1：包月付费
	 * 2：按量付费
	 */
	private Integer type;
	
	/**
	 * cpu核数
	 */
	private Integer cpuCore;
	
	/**
	 * cpu运算时长，以小时为单位，包括所有的核
	 */
	private BigInteger cpuUsed = BigInteger.ZERO;
	
	/**
	 * 内存，以MB为单位
	 */
	private BigInteger memory = BigInteger.ZERO;
	
	/**
	 * 内存使用均值，以MB为单位
	 */
	private BigInteger memoryUsed = BigInteger.ZERO;
	
	/**
	 * 操作系统
	 */
	private String sysImageId;
	
	/**
	 * 系统磁盘容量，以GB为单位
	 */
	private BigInteger sysDisk = BigInteger.ZERO;
	
	/**
	 * 系统磁盘使用均值，以GB为单位
	 */
	private BigInteger sysDiskUsed = BigInteger.ZERO;
	
	/**
	 * 数据磁盘容量，以GB为单位
	 */
	private BigInteger dataDisk = BigInteger.ZERO;
	
	/**
	 * 数据磁盘使用均值，以GB为单位
	 */
	private BigInteger dataDiskUsed = BigInteger.ZERO;
	
	/**
	 * 从磁盘读出的数据总量，以GB为单位
	 */
	private BigInteger diskRead = BigInteger.ZERO;
	
	/**
	 * 向磁盘写入的数据总量，以GB为单位
	 */
	private BigInteger diskWrite = BigInteger.ZERO;
	
	/**
	 * 网络流量，以Mbps为单位
	 */
	private BigInteger bandWidth = BigInteger.ZERO;
	
	/**
	 * 网络流量，以GB为单位
	 */
	private BigInteger networkTraffic = BigInteger.ZERO;
	
	/**
	 * 账单开始时间
	 */
	private String startTime;
	
	/**
	 * 账单结束时间
	 */
	private String endTime;
	
	/**
	 * 账单费用
	 */
	private BigDecimal fee = BigDecimal.ZERO;
	
	/**
	 * 是否付费
	 * 1：未付费
	 * 2：已付费
	 */
	private Integer isPaid;
	
	private Integer region;
	
	private String createTime;
	
	private BigDecimal monthlyPrice;
	
	private Integer hostType;
	
	
	private String hostName;
	
	private String userName;
	
	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getHostId()
	{
		return hostId;
	}

	public void setHostId(String hostId)
	{
		this.hostId = hostId;
	}

	public Integer getType()
	{
		return type;
	}

	public void setType(Integer type)
	{
		this.type = type;
	}
	
	public String getTypeText()
	{
		if( AppConstant.CLOUD_HOST_BILL_DETAIL_TYPE_1_MONTHLY_PAYMENT==this.type )
		{
			return "包月付费";
		}
		else if( AppConstant.CLOUD_HOST_BILL_DETAIL_TYPE_2_PAY_FOR_USED==this.type )
		{
			return "按量付费";
		}
		else
		{
			return "wrong type:["+this.type+"]";
		}
	}

	public Integer getCpuCore()
	{
		return cpuCore;
	}

	public void setCpuCore(Integer cpuCore)
	{
		this.cpuCore = cpuCore;
	}

	public BigInteger getCpuUsed()
	{
		return cpuUsed;
	}

	public void setCpuUsed(BigInteger cpuUsed)
	{
		this.cpuUsed = cpuUsed;
	}

	public BigInteger getMemory()
	{
		return memory;
	}

	public String getMemoryText(int scale)
	{
		return CapacityUtil.toCapacityLabel(this.memory, scale);
	}

	public void setMemory(BigInteger memory)
	{
		this.memory = memory;
	}

	public BigInteger getMemoryUsed()
	{
		return memoryUsed;
	}

	public String getMemoryUsedText(int scale)
	{
		return CapacityUtil.toCapacityLabel(this.memoryUsed, scale);
	}

	public void setMemoryUsed(BigInteger memoryUsed)
	{
		this.memoryUsed = memoryUsed;
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

	public String getSysDiskText(int scale)
	{
		return CapacityUtil.toCapacityLabel(this.sysDisk, scale);
	}

	public void setSysDisk(BigInteger sysDisk)
	{
		this.sysDisk = sysDisk;
	}

	public BigInteger getSysDiskUsed()
	{
		return sysDiskUsed;
	}

	public String getSysDiskUsedText(int scale)
	{
		return CapacityUtil.toCapacityLabel(this.sysDiskUsed, scale);
	}

	public void setSysDiskUsed(BigInteger sysDiskUsed)
	{
		this.sysDiskUsed = sysDiskUsed;
	}

	public BigInteger getDataDisk()
	{
		return dataDisk;
	}

	public String getDataDiskText(int scale)
	{
		return CapacityUtil.toCapacityLabel(this.dataDisk, scale);
	}

	public void setDataDisk(BigInteger dataDisk)
	{
		this.dataDisk = dataDisk;
	}

	public BigInteger getDataDiskUsed()
	{
		return dataDiskUsed;
	}

	public String getDataDiskUsedText(int scale)
	{
		return CapacityUtil.toCapacityLabel(this.dataDiskUsed, scale);
	}

	public void setDataDiskUsed(BigInteger dataDiskUsed)
	{
		this.dataDiskUsed = dataDiskUsed;
	}

	public BigInteger getDiskRead()
	{
		return diskRead;
	}

	public String getDiskReadText(int scale)
	{
		return CapacityUtil.toCapacityLabel(this.diskRead, scale);
	}

	public void setDiskRead(BigInteger diskRead)
	{
		this.diskRead = diskRead;
	}

	public BigInteger getDiskWrite()
	{
		return diskWrite;
	}

	public String getDiskWriteText(int scale)
	{
		return CapacityUtil.toCapacityLabel(this.diskWrite, scale);
	}

	public void setDiskWrite(BigInteger diskWrite)
	{
		this.diskWrite = diskWrite;
	}

	public BigInteger getBandWidth()
	{
		return bandWidth;
	}

	public void setBandWidth(BigInteger bandWidth)
	{
		this.bandWidth = bandWidth;
	}

	public BigInteger getNetworkTraffic()
	{
		return networkTraffic;
	}

	public String getNetworkTrafficText(int scale)
	{
		return FlowUtil.toFlowLabel(this.networkTraffic, scale);
	}

	public void setNetworkTraffic(BigInteger networkTraffic)
	{
		this.networkTraffic = networkTraffic;
	}

	public String getStartTime()
	{
		return startTime;
	}

	public void setStartTime(String startTime)
	{
		this.startTime = startTime;
	}

	public String getEndTime()
	{
		return endTime;
	}

	public void setEndTime(String endTime)
	{
		this.endTime = endTime;
	}

	public BigDecimal getFee()
	{
		return fee;
	}

	public void setFee(BigDecimal fee)
	{
		this.fee = fee;
	}

	public Integer getIsPaid()
	{
		return isPaid;
	}

	public void setIsPaid(Integer isPaid)
	{
		this.isPaid = isPaid;
	}
	
	public String getIsPaidText()
	{
		if( AppConstant.CLOUD_HOST_BILL_DETAIL_IS_PAID_NOT==this.isPaid )
		{
			return "未付费";
		}
		else if( AppConstant.CLOUD_HOST_BILL_DETAIL_IS_PAID_YES==this.isPaid )
		{
			return "已付费";
		}
		else
		{
			return "wrong data:["+this.isPaid+"]";
		}
	}

	public String getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(String createTime)
	{
		this.createTime = createTime;
	}

	public BigDecimal getMonthlyPrice() {
		return monthlyPrice;
	}

	public void setMonthlyPrice(BigDecimal monthlyPrice) {
		this.monthlyPrice = monthlyPrice;
	}

	public Integer getHostType() {
		return hostType;
	}

	public void setHostType(Integer hostType) {
		this.hostType = hostType;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public Integer getRegion() {
		return region;
	}

	public void setRegion(Integer region) {
		this.region = region;
	}
	
	
	
	
	
	
}
