package com.zhicloud.ms.message.util;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;

import java.util.Map;

/**
 * @author 张翔
 * @description 异步消息发送工具类
 * @date 12/28/15
 * @version 1.1
 * @since 1.1
 */
public class MessageAsyncHelper {

    private static MessageAsyncHelper instance;

    private ApplicationContext applicationContext = ContextLoader.getCurrentWebApplicationContext();

    private MessageAsyncHelper(){

    }
    
    public static MessageAsyncHelper getInstance() {
        if (instance == null) {
            instance = new MessageAsyncHelper();
        }

        return instance;
    }

    /**
     * @function 发布消息时间
     * @param parameter 消息内容相关参数
     */
    public void publishMessageEvent(Map<String, Object> parameter) {
        applicationContext.publishEvent(new MessageEvent(parameter));
    }
}

