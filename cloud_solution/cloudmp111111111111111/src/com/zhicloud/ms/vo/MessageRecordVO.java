package com.zhicloud.ms.vo;

import com.zhicloud.ms.common.util.DateUtil;
import com.zhicloud.ms.util.StringUtil;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by sean on 7/6/15.
 */
public class MessageRecordVO {

    private String id;
    private String senderAddress;
    private String recipientAddress;
    private String content;
    private String createTime;
    private Date createTimeDate;
    private String sms_state;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setSenderAddress(String senderAddress) {
        this.senderAddress = senderAddress;
    }

    public String getRecipientAddress() {
        return recipientAddress;
    }

    public void setRecipientAddress(String recipientAddress) {
        this.recipientAddress = recipientAddress;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public String getSms_state() {
        return sms_state;
    }

    public void setSms_state(String sms_state) {
        this.sms_state = sms_state;
    }
    
}
