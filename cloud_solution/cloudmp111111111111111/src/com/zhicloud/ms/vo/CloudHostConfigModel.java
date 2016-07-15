package com.zhicloud.ms.vo;

import java.math.BigInteger;
import java.text.ParseException;
import java.util.Date;

import com.zhicloud.ms.common.util.DateUtil;
import com.zhicloud.ms.util.CapacityUtil;
import com.zhicloud.ms.util.FlowUtil;

public class CloudHostConfigModel {
	private String id;
	private String name;
	private Integer cpuCore;
	private String sysImageId;
	private BigInteger memory;
	private BigInteger sysDisk;
	private BigInteger dataDisk;
	private BigInteger bandwidth;
	private BigInteger memoryText;
	private BigInteger sysDiskText;
	private BigInteger dataDiskText;
	private BigInteger bandwidthText;
	private String sysImageName;
	private String createTime;
	private String modifiedTime;
	private Date curCreateDate;
	private Date curModifiedDate;
    /* 属性,方便导出拼装数据 */
    private String operate_property;
	/**
	 * 自定义硬盘
	 */
	private Integer diskdiy;
	/**
	 * 自定义带宽
	 */
	private Integer bandwidthdiy;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 推荐配置类型
	 */
	private String allocationType;
	/**
	 * 是否是自定义配置
	 */
	private String isdiy;
	private String type;
	private String sysDiskType;
	private String emptyDisk;
	//是否支持H264
	private Integer supportH264;
	//镜像类型
	private Integer fileType;
	
	/**
	 * 码率，默认为0
	 */
	private Integer codeRate;
	
	/**
	 * 帧率 ,默认为0
	 */
	private Integer frameRate;
	
	/**
	 * 操作系统类型      如：{版本_版本号_os位数}， 如CentOS_6_32
	 */
	private String operationSystem;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getCpuCore() {
		return cpuCore;
	}
	public void setCpuCore(Integer cpuCore) {
		this.cpuCore = cpuCore;
	}
	public BigInteger getMemory() {
		return memory;
	}
	public void setMemory(BigInteger memory) {
		this.memory = memory;
		if(memory!=null){
			this.setMemoryText(new BigInteger((CapacityUtil.toGBValue(memory,0)).toString()));
		}
	}
	public String getSysImageId() {
		return sysImageId;
	}
	public void setSysImageId(String sysImageId) {
		this.sysImageId = sysImageId;
	}
	public BigInteger getSysDisk() {
		return sysDisk;
	}
	public void setSysDisk(BigInteger sysDisk) {
		this.sysDisk = sysDisk;
		if(sysDisk!=null){
			this.setSysDiskText(new BigInteger((CapacityUtil.toGBValue(sysDisk,0)).toString()));
		}
	}
	public BigInteger getDataDisk() {
		return dataDisk;
	}
	public void setDataDisk(BigInteger dataDisk) {
		this.dataDisk = dataDisk;
		if(dataDisk!=null){
			this.setDataDiskText(new BigInteger((CapacityUtil.toGBValue(dataDisk,0)).toString()));
		}
	}
	public BigInteger getBandwidth() {
		return bandwidth;
	}
	public void setBandwidth(BigInteger bandwidth) {
		this.bandwidth = bandwidth;
		if(bandwidth!=null){
			this.setBandwidthText(new BigInteger((FlowUtil.toMbpsValue(bandwidth, 0).toString())));
		}
	}
	public String getSysImageName() {
		return sysImageName;
	}
	public void setSysImageName(String sysImageName) {
		this.sysImageName = sysImageName;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
		if(createTime!=null){
			try {
				this.setCurCreateDate(DateUtil.stringToDate(createTime, "yyyyMMddHHmmssSSS"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
	public String getModifiedTime() {
		return modifiedTime;
	}
	public void setModifiedTime(String modifiedTime) {
		this.modifiedTime = modifiedTime;
		if(modifiedTime!=null){
			try {
				this.setCurModifiedDate(DateUtil.stringToDate(modifiedTime, "yyyyMMddHHmmssSSS"));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
	}
	public String getAllocationType() {
		return allocationType;
	}
	public void setAllocationType(String allocationType) {
		this.allocationType = allocationType;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getIsdiy() {
		return isdiy;
	}
	public void setIsdiy(String isdiy) {
		this.isdiy = isdiy;
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
	public Date getCurCreateDate() {
		return curCreateDate;
	}
	public void setCurCreateDate(Date curCreateDate) {
		this.curCreateDate = curCreateDate;
	}
	public Date getCurModifiedDate() {
		return curModifiedDate;
	}
	public void setCurModifiedDate(Date curModifiedDate) {
		this.curModifiedDate = curModifiedDate;
	}
	public BigInteger getMemoryText() {
		return memoryText;
	}
	public void setMemoryText(BigInteger memoryText) {
		this.memoryText = memoryText;
	}
	public BigInteger getSysDiskText() {
		return sysDiskText;
	}
	public void setSysDiskText(BigInteger sysDiskText) {
		this.sysDiskText = sysDiskText;
	}
	public BigInteger getDataDiskText() {
		return dataDiskText;
	}
	public void setDataDiskText(BigInteger dataDiskText) {
		this.dataDiskText = dataDiskText;
	}
	public BigInteger getBandwidthText() {
		return bandwidthText;
	}
	public void setBandwidthText(BigInteger bandwidthText) {
		this.bandwidthText = bandwidthText;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSysDiskType() {
		return sysDiskType;
	}
	public void setSysDiskType(String sysDiskType) {
		this.sysDiskType = sysDiskType;
	}
	public String getEmptyDisk() {
		return emptyDisk;
	}
	public void setEmptyDisk(String emptyDisk) {
		this.emptyDisk = emptyDisk;
	}
	
	public String getSysImageNameFormat() {
		if(this.sysImageName==null){
			return "空白系统";
		}else{
			return this.sysImageName;
		}
	}

    public String getOperate_property() {
        operate_property = cpuCore + "核/" + memoryText + "G/" + dataDiskText + "G/" + bandwidthText + "M";
        return operate_property;
    }
    
    public Date getModyfy_date() {
        if (modifiedTime == null || modifiedTime.isEmpty()) {
            return curCreateDate;
        } else {
            return curModifiedDate;
        }
    }
	public Integer getSupportH264() {
		return supportH264;
	}
	public void setSupportH264(Integer supportH264) {
		this.supportH264 = supportH264;
	}
    public Integer getFileType() {
        return fileType;
    }
    public void setFileType(Integer fileType) {
        this.fileType = fileType;
    }
	public Integer getCodeRate() {
		return codeRate;
	}
	public void setCodeRate(Integer codeRate) {
		this.codeRate = codeRate;
	}
	public Integer getFrameRate() {
		return frameRate;
	}
	public void setFrameRate(Integer frameRate) {
		this.frameRate = frameRate;
	}
	public String getOperationSystem() {
		return operationSystem;
	}
	public void setOperationSystem(String operationSystem) {
		this.operationSystem = operationSystem;
	}
	
	

}
