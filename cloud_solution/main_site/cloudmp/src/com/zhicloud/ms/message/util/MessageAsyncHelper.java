package com.zhicloud.ms.message.util;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import java.util.Map;

/**
 * Created by sean on 12/28/15.
 */
public class MessageAsyncHelper {

    private static MessageAsyncHelper instance;

    ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();

    private MessageAsyncHelper(){

    }

    public static MessageAsyncHelper getInstance() {
        if (instance == null) {
            instance = new MessageAsyncHelper();
        }

        return instance;
    }

    public void publishMessageEvent(Map<String, Object> parameter) {
        applicationContext.publishEvent(new MessageEvent(parameter));
    }
}

