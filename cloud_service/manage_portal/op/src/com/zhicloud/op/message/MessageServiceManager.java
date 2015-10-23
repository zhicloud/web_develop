package com.zhicloud.op.message;

import com.zhicloud.op.message.email.EmailSendService;
import com.zhicloud.op.message.sms.SmsSendService;

/**
 * Created by sean on 7/6/15.
 */
public class MessageServiceManager {

    private static MessageServiceManager instance = null;


    public synchronized static MessageServiceManager singleton() {
        if (MessageServiceManager.instance == null) {
            MessageServiceManager.instance = new MessageServiceManager();
        }

        return instance;

    }

    public EmailSendService getMailService() {
        return new EmailSendService();
    }

    public SmsSendService getSmsService() {
        return new SmsSendService();
    }

}
