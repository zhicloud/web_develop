/**
 * Project Name:CloudDeskTopMS
 * File Name:VersionRecordVO.java
 * Package Name:com.zhicloud.ms.vo
 * Date:2015年4月21日下午6:04:10
 * 
 *
*/ 

package com.zhicloud.ms.vo; 

import java.text.ParseException;
import java.util.Date;

import com.zhicloud.ms.common.util.DateUtil;
import com.zhicloud.ms.util.StringUtil;

/**
 * ClassName: VersionRecordVO 
 * Function:  版本管理VO
 * date: 2015年4月21日 下午6:04:10 
 *
 * @author sasa
 * @version 
 * @since JDK 1.7
 */
public class VersionRecordVO {
	
	private String id;
	private String versionNumber;
	private String name;
	private String updateInfo;
	private String createTime;
	private long fsize;
	private String platformType;
	public long getFsize() {
		return fsize;
	}
	public void setFsize(long fsize) {
		this.fsize = fsize;
	}
	private Integer status;
	private Date createTimeDate;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVersionNumber() {
		return versionNumber;
	}
	public void setVersionNumber(String versionNumber) {
		this.versionNumber = versionNumber;
	} 
	public String getUpdateInfo() {
		return updateInfo;
	}
	public void setUpdateInfo(String updateInfo) {
		this.updateInfo = updateInfo;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
		if(!StringUtil.isBlank(createTime)){			
			try {
				this.createTimeDate = DateUtil.stringToDate(createTime, "yyyyMMddHHmmssSSS");
			} catch (ParseException e) {
				
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
		}
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getCreateTimeDate() {
		return createTimeDate;
	}
	public void setCreateTimeDate(Date createTimeDate) {
		this.createTimeDate = createTimeDate;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
    public String getFilesize() {
        Float temp = Float.valueOf(fsize) / 1024 / 1024;
        long l1 = Math.round(temp * 100); // 四舍五入
        double ret = l1 / 100.0;
        return ret + "MB";
    }

    public String getInsert_date() {
        return DateUtil.dateToString(createTimeDate, "yyyy-MM-dd HH:mm:ss");
    }
    public String getPlatformType() {
        return platformType;
    }
    public void setPlatformType(String platformType) {
        this.platformType = platformType;
    }
    
}

