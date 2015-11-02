package com.zhicloud.ms.vo;

import com.zhicloud.ms.app.pool.host.back.HostBackupProgressData;
import com.zhicloud.ms.app.pool.host.back.HostBackupProgressPool;
import com.zhicloud.ms.app.pool.host.back.HostBackupProgressPoolManager;
import com.zhicloud.ms.common.util.DateUtil;

import java.text.ParseException;
import java.util.Date;

public class BackUpDetailVO {
    private String  id;
    //主机id
    private String hostId;
    //主机显示名称
    private String displayName;
    //备份状态 1：成功 2失败 3备份过期 4正在备份 5正在恢复 6正在备份其他版本
    private Integer status;
    //备份结束时间
    private String backUpTime;
    //创建时间
    private String createTime;
    //操作类型 0：备份 1： 开机  2： 关机
    private Integer type;
    
    private Integer mode;
    
    private Integer disk;
    
    private String userName;
    
    private Date backUpTimeDate;
    
    private String realHostId;
    
    /*状态翻译*/
    private String status_name;
    
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getHostId() {
        return hostId;
    }
    public void setHostId(String hostId) {
        this.hostId = hostId;
    }
    public String getDisplayName() {
        return displayName;
    }
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
    public Integer getStatus() {
        if(status == 4 || status == 5 || status == 6){
            HostBackupProgressPool pool = HostBackupProgressPoolManager.singleton().getPool();
            HostBackupProgressData data = pool.get(this.realHostId);
            if(data != null && data.getBackupStatus()!=null &&( data.getBackupStatus() ==9 || data.getBackupStatus() ==10)){
                return status;
            }else{
                if(status == 4){
                    return 2; // 失败 
                }else{
                    return 1;//成功
                }
               
            }
        }
        return status;
    }
    public void setStatus(Integer status) {
        this.status = status;
    }
    public String getBackUpTime() {
        return backUpTime;
    }
    public void setBackUpTime(String backUpTime) {
        this.backUpTime = backUpTime;
        try {
            this.backUpTimeDate = DateUtil.stringToDate(backUpTime, "yyyyMMddHHmmssSSS");
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }
    public String getCreateTime() {
        return createTime;
    }
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
    public Date getBackUpTimeDate() {
        return backUpTimeDate;
    }
    public void setBackUpTimeDate(Date backUpTimeDate) {
        this.backUpTimeDate = backUpTimeDate;
    }



    public int compareTo(BackUpDetailVO o) {
        return this.getCreateTime().compareTo(o.getCreateTime());
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
    public Integer getMode() {
        return mode;
    }
    public void setMode(Integer mode) {
        this.mode = mode;
    }
    public Integer getDisk() {
        return disk;
    }
    public void setDisk(Integer disk) {
        this.disk = disk;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getRealHostId() {
        return realHostId;
    }
    public void setRealHostId(String realHostId) {
        this.realHostId = realHostId;
    }
    
    public String getBackUp_date() {
        return DateUtil.dateToString(backUpTimeDate, "yyyy-MM-dd HH:mm:ss");
    }
    
    public String getStatus_name() {
        if (getStatus() == 1) {
            status_name = "成功";
        } else if (getStatus() == 2) {
            status_name = "失败";
        } else if (getStatus() == 3) {
            status_name = "已过期";
        } else if (getStatus() == 4) {
            status_name = "正在备份";
        } else if (getStatus() == 5) {
            status_name = "正在恢复";
        } else if (getStatus() == 6) {
            status_name = "正在备份其他版本";
        }
        return status_name;
    }
    
    public String modeFormat(){
    	if(this.mode==0){
    		return "全备份";
    	}else{
    		return "部分备份";
    	}
    }
    
    public String diskFormat(){
    	if(this.mode==0){
    		return "所有磁盘";
    	}else{
    		if(this.disk==0){
    			return "系统磁盘";
    		}else{
    			return "数据磁盘"+this.disk;
    		}
    	}
    }
}
