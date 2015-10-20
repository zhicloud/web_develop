package com.zhicloud.ms.vo;

import com.zhicloud.ms.common.util.DateUtil;
import com.zhicloud.ms.util.StringUtil;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by sean on 6/23/15.
 */
public class TerminalBoxVO {

    private String id;
    private String serialNumber;
    private String name;
    private String createTime;
    private Date createTimeDate;
    private String allocateUserId;
    private String allocateUser;
    private String allocateTime;
    private Date allocateTimeDate;
    private Integer status;
    private String modifiedTime;
    /*状态字段翻译(0,未分配 1,已分配)*/
    private String status_name;

    public String getAllocateUserId() {
        return allocateUserId;
    }

    public void setAllocateUserId(String allocateUserId) {
        this.allocateUserId = allocateUserId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        if(!StringUtil.isBlank(createTime)){
            try {
                this.createTimeDate = DateUtil.stringToDate(createTime, "yyyyMMddHHmmssSSS");
            } catch (ParseException e) {
                e.printStackTrace();

            }
        }
    }

    public String getAllocateUser() {
        return allocateUser;
    }

    public void setAllocateUser(String allocateUser) {
        this.allocateUser = allocateUser;
    }

    public String getAllocateTime() {
        return allocateTime;
    }

    public void setAllocateTime(String allocateTime) {
        if(!StringUtil.isBlank(allocateTime)){
            try {
                this.allocateTimeDate = DateUtil.stringToDate(allocateTime, "yyyyMMddHHmmssSSS");
            } catch (ParseException e) {
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

    public String getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(String modifiedTime) {
        this.modifiedTime = modifiedTime;
    }

    public Date getCreateTimeDate() {
        return createTimeDate;
    }

    public void setCreateTimeDate(Date createTimeDate) {
        this.createTimeDate = createTimeDate;
    }

    public Date getAllocateTimeDate() {
        return allocateTimeDate;
    }

    public void setAllocateTimeDate(Date allocateTimeDate) {
        this.allocateTimeDate = allocateTimeDate;
    }
    
    public String getInsert_date() {
        return DateUtil.dateToString(createTimeDate, "yyyy-MM-dd HH:mm:ss");
    }
    
    public String getAllocate_date() {
        return DateUtil.dateToString(allocateTimeDate, "yyyy-MM-dd HH:mm:ss");
    }

    public String getStatus_name() {
        if (status == 0) {
            status_name = "未分配";
        } else {
            status_name = "已分配";
        }
        return status_name;
    }

}
