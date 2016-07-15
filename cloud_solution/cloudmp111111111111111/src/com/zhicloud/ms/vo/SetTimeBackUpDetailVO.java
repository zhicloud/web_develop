package com.zhicloud.ms.vo;

import com.zhicloud.ms.common.util.DateUtil;
import com.zhicloud.ms.util.StringUtil;

import java.text.ParseException;
import java.util.Date;

public class SetTimeBackUpDetailVO {
    private String  id;
    //主机id
    private String hostId;
    //主机显示名称
    private String displayName;
    //备份状态 1：成功 2失败
    private Integer status;
    //备份结束时间
    private String backUpTime;
    //创建时间
    private String createTime;

    //格式化
    private Date createTimeDate;
    //操作类型 0：备份 1： 开机  2： 关机
    private Integer type;
    
    private Date backUpTimeDate;
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
        if(!StringUtil.isBlank(createTime)){
            try {
                this.createTime = createTime;
                this.createTimeDate = DateUtil.stringToDate(createTime, "yyyyMMddHHmmssSSS");
            } catch (ParseException e) {
                e.printStackTrace();

            }
        }
    }
    public Date getCreateTimeDate() {
        return createTimeDate;
    }

    public void setCreateTimeDate(Date createTimeDate) {
        this.createTimeDate = createTimeDate;
    }

    public Date getBackUpTimeDate() {
        return backUpTimeDate;
    }
    public void setBackUpTimeDate(Date backUpTimeDate) {
        this.backUpTimeDate = backUpTimeDate;
    }



    public int compareTo(SetTimeBackUpDetailVO o) {
        return this.getCreateTime().compareTo(o.getCreateTime());
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
