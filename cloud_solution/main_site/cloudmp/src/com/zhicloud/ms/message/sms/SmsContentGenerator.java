package com.zhicloud.ms.message.sms;

import java.util.LinkedHashMap;

/**
 * Created by sean on 7/8/15.
 */
public class SmsContentGenerator {

    private TextTemplate textTemplate;

    public SmsContentGenerator(String resourcePath) {
        textTemplate = new TextTemplate(resourcePath);
    }


    public String generateContent(LinkedHashMap<String, Object> properties) {
        return textTemplate.fillTemplate(properties);
    }
}
