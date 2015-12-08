package com.zhicloud.ms.message.listener;

import com.zhicloud.ms.common.util.StringUtil;
import com.zhicloud.ms.message.MessageConstant;
import com.zhicloud.ms.message.MessageEvent;
import com.zhicloud.ms.message.MessageServiceManager;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by sean on 11/25/15.
 */

@Component public class MessageSendListener implements ApplicationListener<MessageEvent> {

    @Async @Override public void onApplicationEvent(MessageEvent messageEvent) {

        Map<String, Object> param = (Map<String, Object>) messageEvent.getSource();
        try {
            MessageServiceManager.singleton().getMailService().sendMailWithBcc(MessageConstant.EMAIL_INFO_ADMIN_REGISTER,
                StringUtil.trim(param.get("email")), param);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
