package com.zhicloud.ms.vo;

import com.zhicloud.ms.common.util.DateUtil;
import com.zhicloud.ms.util.StringUtil;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by sean on 7/6/15.
 */
public class SmsConfigVO {

    private String id;              // ID
    private String smsId;           // 短信配置ID
    private String serviceUrl;      // 服务端地址
    private String configName;      // 配置名
    private String name;            // 账户名
    private String password;        // 密码
    private String createTime;      // 创建时间
    private String modifiedTime;    // 修改时间
    private Date createTimeDate;    // 创建时间格式化
    private Date modifiedTimeDate;  // 修改时间格式化

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSmsId() {
        return smsId;
    }

    public void setSmsId(String smsId) {
        this.smsId = smsId;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getModifiedTime() {
        return modifiedTime;
    }

    public void setModifiedTime(String modifiedTime) {
        if(!StringUtil.isBlank(modifiedTime)){
            try {
                this.modifiedTime = modifiedTime;
                this.modifiedTimeDate = DateUtil.stringToDate(modifiedTime, "yyyyMMddHHmmssSSS");
            } catch (ParseException e) {
                e.printStackTrace();

            }
        }
    }

    public Date getModifiedTimeDate() {
        return modifiedTimeDate;
    }

    public void setModifiedTimeDate(Date modifiedTimeDate) {
        this.modifiedTimeDate = modifiedTimeDate;
    }
}
