package com.zhicloud.ms.message;


import com.zhicloud.ms.message.sms.SmsSendService;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by sean on 7/9/15.
 */
public class Test {

    public static void main (String[] args) {
//        EmailSendService emailSendService = MessageServiceManager.singleton().getMailService();
//        Map<String, Object> parameter = new LinkedHashMap<>();
//        parameter.put("name", "广州");
//        emailSendService.setParameter(parameter);
//        emailSendService.sendMail("GW_WARN");

        SmsSendService smsSendService = MessageServiceManager.singleton().getSmsService();
        Map<String, Object> parameter = new LinkedHashMap<>();
        parameter.put("region", "广州");
        smsSendService.sendSms_new("DB_WARN", parameter); 

    }
}
