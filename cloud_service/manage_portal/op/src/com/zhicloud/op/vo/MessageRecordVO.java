package com.zhicloud.op.vo;

import com.zhicloud.op.common.util.json.JSONBean;

/**
 * Created by sean on 7/6/15.
 */
public class MessageRecordVO implements JSONBean {

    private String id;
    private String senderAddress;
    private String recipientAddress;
    private String content;
    private String createTime;
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
        this.createTime = createTime;
    }

    public String getSms_state() {
        return sms_state;
    }

    public void setSms_state(String sms_state) {
        this.sms_state = sms_state;
    }
    
}
