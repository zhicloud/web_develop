package com.zhicloud.ms.message.email;

/**
 * Created by sean on 8/19/15.
 */
public class EmailHelper {

    private String senderName;
    private String subject;
    private String sender;
    private String receiver;
    private String bccReceiver;
    private String content;

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public String getBccReceiver() {
        return bccReceiver;
    }

    public void setBccReceiver(String bccReceiver) {
        this.bccReceiver = bccReceiver;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
