package com.zhicloud.ms.message;

import org.springframework.context.ApplicationEvent;

import java.util.Map;

/**
 * Created by sean on 11/25/15.
 */
public class MessageEvent extends ApplicationEvent {

    public MessageEvent(Map<String, Object> source) {
        super(source);
    }
}
