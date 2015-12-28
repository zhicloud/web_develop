package com.zhicloud.ms.message.listener;

import com.zhicloud.ms.common.util.StringUtil;
import com.zhicloud.ms.message.MessageServiceManager;
import com.zhicloud.ms.message.util.MessageConstant;
import com.zhicloud.ms.message.util.MessageEvent;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by sean on 11/25/15.
 */

@Component
public class MessageSendListener implements ApplicationListener<MessageEvent> {

    public static final Logger logger = Logger.getLogger(MessageSendListener.class);

    @Async
    @Override
    public void onApplicationEvent(final MessageEvent event) {

        Map<String, Object> param = (Map<String, Object>) event.getSource();
        try {

            String sendType = StringUtil.trim(param.get(MessageConstant.EMAIL_SEND_TYPE_KEY));
            String templateCode = StringUtil.trim(param.get(MessageConstant.EMAIL_TEMPLATE_KEY));

            switch (sendType) {
                // 直接发送给模板邮箱
                case MessageConstant.EMAIL_FROM_TEMPLATE_ONLY:
                    MessageServiceManager.singleton().getMailService().sendMail(templateCode, param);
                    break;

                // 发送给指定邮箱
                case MessageConstant.EMAIL_TO_RECIPIENT:
                    MessageServiceManager.singleton().getMailService().sendMail(templateCode, StringUtil.trim(param.get("email")), param);
                    break;

                // 发送给指定邮箱并密送给模板邮箱
                case MessageConstant.EMAIL_TO_RECIPIENT_WITH_BCC:
                    MessageServiceManager.singleton().getMailService().sendMailWithBcc(templateCode, StringUtil.trim(param.get("email")), param);
                    break;

                default:
                    MessageServiceManager.singleton().getMailService().sendMail(templateCode, param);
                    break;
            }

            System.err.println("MessageSendListener" + System.currentTimeMillis());
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }
    }
}
