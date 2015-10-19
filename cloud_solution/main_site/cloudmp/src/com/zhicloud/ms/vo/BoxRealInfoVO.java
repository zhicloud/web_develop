package com.zhicloud.ms.vo;

import java.text.ParseException;
import java.util.Date;

import com.zhicloud.ms.common.util.DateUtil;
import com.zhicloud.ms.common.util.StringUtil;

/**
 * 
* @ClassName: BoxRealInfoVO 
* @Description: 记录盒子真实运行信息
* @author sasa
* @date 2015年8月6日 下午2:47:53 
*
 */
public class BoxRealInfoVO {
    
    private  String mac ;
    private String ip ;
    private String subnetMask ;
    private String softwareVersion ;
    private String hardwareVersion ;
    private String gateway ;
    private String userId;
    private String userName;
    private Integer status;
    private String lastLoginTime;
    private String lastAliveTime;
    private String lastLogoutTime;
    private Date lastLoginDate;
    private Date lastAliveDate;
    private Date lastLogoutDate;
    private String serialNumber;
    
    private long cumulativeOnlineTime;

    public long getCumulativeOnlineTime() {
		return cumulativeOnlineTime;
	}

	public void setCumulativeOnlineTime(long cumulativeOnlineTime) {
		this.cumulativeOnlineTime = cumulativeOnlineTime;
	}
	
	public String getCumulativeOnlineTimeText(){
		long days = this.cumulativeOnlineTime / (1000 * 60 * 60 * 24);  
	    long hours = (this.cumulativeOnlineTime % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);  
		long minutes = (this.cumulativeOnlineTime % (1000 * 60 * 60)) / (1000 * 60);  
		return days + "天 " + hours + "小时 " + minutes + "分钟 "; 
	}
	
    
    public String getMac() {
        return mac;
    }
    public void setMac(String mac) {
        this.mac = mac;
    }
    public String getIp() {
        return ip;
    }
    public void setIp(String ip) {
        this.ip = ip;
    }
    public String getSubnetMask() {
        return subnetMask;
    }
    public void setSubnetMask(String subnetMask) {
        this.subnetMask = subnetMask;
    }
    public String getSoftwareVersion() {
        return softwareVersion;
    }
    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }
    public String getHardwareVersion() {
        return hardwareVersion;
    }
    public void setHardwareVersion(String hardwareVersion) {
        this.hardwareVersion = hardwareVersion;
    }
    public String getGateway() {
        return gateway;
    }
    public void setGateway(String gateway) {
        this.gateway = gateway;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public Integer getStatus() {
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getLastLoginTime() {
        return lastLoginTime;
    }
    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
        try {
            if(!StringUtil.isBlank(lastLoginTime)){
                this.setLastLoginDate(DateUtil.stringToDate(lastLoginTime, "yyyyMMddHHmmssSSS"));
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public String getLastAliveTime() {
        return lastAliveTime;
    }
    public void setLastAliveTime(String lastAliveTime) {
        this.lastAliveTime = lastAliveTime;
        try {
            if(!StringUtil.isBlank(lastAliveTime)){
                this.setLastAliveDate(DateUtil.stringToDate(lastAliveTime, "yyyyMMddHHmmssSSS"));
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public String getLastLogoutTime() {
        return lastLogoutTime;
    }
    public void setLastLogoutTime(String lastLogoutTime) {
        this.lastLogoutTime = lastLogoutTime;
        try {
            if(!StringUtil.isBlank(lastLogoutTime)){
                this.setLastLogoutDate(DateUtil.stringToDate(lastLogoutTime, "yyyyMMddHHmmssSSS"));
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    public Date getLastLoginDate() {
        return lastLoginDate;
    }
    public void setLastLoginDate(Date lastLoginDate) {
        this.lastLoginDate = lastLoginDate;
    }
    public Date getLastAliveDate() {
        return lastAliveDate;
    }
    public void setLastAliveDate(Date lastAliveDate) {
        this.lastAliveDate = lastAliveDate;
    }
    public Date getLastLogoutDate() {
        return lastLogoutDate;
    }
    public void setLastLogoutDate(Date lastLogoutDate) {
        this.lastLogoutDate = lastLogoutDate;
    }
    public String caculateRealStatus(){
        Date now = new Date();
        if(this.lastLogoutDate != null && this.lastAliveDate != null){
             if(this.lastLogoutDate.after(getLastAliveDate())){
                 return "离线";
             }else {
                 long diff = now.getTime() - this.lastAliveDate.getTime();
                 long minute = diff / (1000 * 60); //计算上次心跳时间和当前时间差
                 if(minute <= 15){
                     return "在线";
                 }else{
                     return "离线";
                 }
             }
        }else if(this.lastLogoutDate != null && this.lastAliveDate == null){
            if(this.lastLogoutDate.after(this.lastLoginDate)){
                return "离线";
            }else { 
                long diff = now.getTime() - this.lastLoginDate.getTime();
                long minute = diff / (1000 * 60); //计算上次心跳时间和当前时间差
                if(minute <= 15){
                    return "在线";
                }else{
                    return "离线";
                }
            }
        }else{
            long diff = now.getTime() - this.lastAliveDate.getTime();
            long minute = diff / (1000 * 60); //计算上次心跳时间和当前时间差
            if(minute <= 15){
                return "在线";
            }else{
                return "离线";
            }
         }
    }
    public String getSerialNumber() {
        return serialNumber;
    }
    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
    
    /**
     * @Description:导出excel需要根据get反射方法,所以重新写了遍
     * @return
     */
    public String getCaculateRealStatus(){
        Date now = new Date();
        if(this.lastLogoutDate != null && this.lastAliveDate != null){
             if(this.lastLogoutDate.after(getLastAliveDate())){
                 return "离线";
             }else {
                 long diff = now.getTime() - this.lastAliveDate.getTime();
                 long minute = diff / (1000 * 60); //计算上次心跳时间和当前时间差
                 if(minute <= 15){
                     return "在线";
                 }else{
                     return "离线";
                 }
             }
        }else if(this.lastLogoutDate != null && this.lastAliveDate == null){
            if(this.lastLogoutDate.after(this.lastLoginDate)){
                return "离线";
            }else { 
                long diff = now.getTime() - this.lastLoginDate.getTime();
                long minute = diff / (1000 * 60); //计算上次心跳时间和当前时间差
                if(minute <= 15){
                    return "在线";
                }else{
                    return "离线";
                }
            }
        }else{
            long diff = now.getTime() - this.lastAliveDate.getTime();
            long minute = diff / (1000 * 60); //计算上次心跳时间和当前时间差
            if(minute <= 15){
                return "在线";
            }else{
                return "离线";
            }
         }
    }
    
    public String getLastlogin_date() {
        return DateUtil.dateToString(lastLoginDate, "yyyy-MM-dd HH:mm:ss");
    }

    public String getLastalive_date() {
        return DateUtil.dateToString(lastAliveDate, "yyyy-MM-dd HH:mm:ss");
    }

}
