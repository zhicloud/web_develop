package com.zhicloud.ms.vo;

import com.zhicloud.ms.common.util.DateUtil;
import com.zhicloud.ms.util.StringUtil;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by sean on 7/6/15.
 */
public class SmsTemplateVO {

    private String id;
    private String configId;
    private String configName;
    private String code;
    private String name;
    private String sender;
    private String recipient;
    private String content;
    private String createTime;
    private String modifiedTime;
    private Date createTimeDate;    // 创建时间格式化
    private Date modifiedTimeDate;  // 修改时间格式化

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConfigId() {
        return configId;
    }

    public void setConfigId(String configId) {
        this.configId = configId;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
