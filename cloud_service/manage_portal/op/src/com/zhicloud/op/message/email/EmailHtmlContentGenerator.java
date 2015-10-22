package com.zhicloud.op.message.email;

import java.util.LinkedHashMap;

/**
 * Created by sean on 7/8/15.
 */
public class EmailHtmlContentGenerator {

    private TextTemplate textTemplate;

    public EmailHtmlContentGenerator(String resourcePath) {
        textTemplate = new TextTemplate(resourcePath);
    }


    public String generateContent(LinkedHashMap<String, Object> properties) {
        return textTemplate.fillTemplate(properties);
    }
}
