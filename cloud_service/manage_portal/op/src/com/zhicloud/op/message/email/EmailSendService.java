package com.zhicloud.op.message.email;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.core.CoreSpringContextManager;
import com.zhicloud.op.service.EmailConfigService;
import com.zhicloud.op.service.EmailTemplateService;
import com.zhicloud.op.service.MessageRecordService;
import com.zhicloud.op.service.constant.AppConstant;
import com.zhicloud.op.vo.EmailConfigVO;
import com.zhicloud.op.vo.EmailTemplateVO;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.*;

/**
 * Created by sean on 7/8/15.
 */
@Transactional(readOnly = false)
public class EmailSendService {

    private static final Logger logger = Logger.getLogger(EmailSendService.class);

    private EmailHtmlContentGenerator emailHtmlContentGenerator;

    private EmailConfigService emailConfigService = CoreSpringContextManager.getEmailConfigService();

    private EmailTemplateService emailTemplateService =
        CoreSpringContextManager.getEmailTemplateService();

    private MessageRecordService messageRecordService = CoreSpringContextManager.getMessageRecordService();

    /**
     * @function 生成Email内容
     * @param path 文本路径
     * @param parameter 模板内容关键字参数
     * @return Email正文内容
     */
    public String generateEmailContent(String path, Map<String, Object> parameter) {

        emailHtmlContentGenerator = new EmailHtmlContentGenerator(path);

        LinkedHashMap<String, Object> properties = new LinkedHashMap<>();

        Iterator<Map.Entry<String, Object>> entries = parameter.entrySet().iterator();

        while (entries.hasNext()) {

            Map.Entry<String, Object> entry = entries.next();

            properties.put(entry.getKey(), entry.getValue());

        }

        return emailHtmlContentGenerator.generateContent(properties);

    }


    /**
     * @function 直接调用模板，发送给模板用户
     * @param code 邮件模板标识码
     * @param parameter 模板内容关键字参数
     */
    public void sendMail(String code, Map<String, Object> parameter) {

        if (parameter == null) {
            parameter = new LinkedHashMap<>();
        }

        EmailTemplateVO mailTemplateVO = emailTemplateService.getTemplateByCode(code);
        EmailConfigVO mailConfigVO = emailConfigService.getConfigById(mailTemplateVO.getConfigId());

        String serverName = mailConfigVO.getSender();
        String account = mailConfigVO.getSenderAddress();
        String senderAddress = "<" + account + ">";
        String password = mailConfigVO.getPassword();
        String recipientStr = mailTemplateVO.getRecipient();
        String subject = mailTemplateVO.getSubject();
        String content = generateEmailContent(mailTemplateVO.getContent(), parameter);

        //分离多个收件人
        String[] recipient = recipientStr.split(";");


        try {

            Properties props = new Properties();
            // 也可用Properties props = System.getProperties();
            props.put(EmailConf.MAIL_SMTP_HOST, EmailConf.SMTP_EXMIAL_QQ_COM);
            // 存储发送邮件服务器的信息
            props.put(EmailConf.MAIL_SMTP_AUTH, EmailConf.TRUE);
            // 同时通过验证
            Session s = Session.getInstance(props);
            // 根据属性新建一个邮件会话
//             s.setDebug(true);
            // 输出跟踪日志

            MimeMessage message = new MimeMessage(s);
            // 由邮件会话新建一个消息对象

            // 设置邮件
            InternetAddress from = new InternetAddress(serverName + senderAddress);
            message.setFrom(from);// 设置发件人
            Address[] tos = null;
            if (recipient != null){
                // 为每个邮件接收者创建一个地址
                tos = new InternetAddress[recipient.length];
                for (int i=0; i<recipient.length; i++){
                    tos[i] = new InternetAddress(recipient[i]);
                }
            }

            message.setRecipients(Message.RecipientType.TO, tos);

//            InternetAddress to = new InternetAddress(recipient);
//            message.setRecipient(Message.RecipientType.TO, to);// 设置收件人,并设置其接收类型为TO

            //乱码
            //message.setSubject(MimeUtility.encodeText(ttitle));
            message.setSubject(MimeUtility.encodeText(subject, "UTF-8", "B"));
            message.setText(content);// 设置信件内容

            message.setSentDate(new java.util.Date());// 设置发信时间

            BodyPart mdp = new MimeBodyPart();
            mdp.setContent(content, "text/html;charset=UTF-8");
            Multipart mm = new MimeMultipart();
            mm.addBodyPart(mdp);
            message.setContent(mm);

            // 发送邮件
            message.saveChanges();// 存储邮件信息
            Transport transport = s.getTransport(EmailConf.MAIL_SERVICE);
            transport.connect(EmailConf.SMTP_EXMIAL_QQ_COM, account, password);// 以smtp方式登录邮箱
            transport.sendMessage(message, message.getAllRecipients());// 发送邮件,其中第二个参数是所有
            // 已设好的收件人地址
            transport.close();

            //写入发送纪录
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("id", StringUtil.generateUUID());
            data.put("sender_address", account);
            data.put("recipient_address", recipientStr);
            data.put("content", content);
            data.put("type", AppConstant.MESSAGE_TYPE_EMAIL);
            data.put("create_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
            messageRecordService.addRecord(data);

        } catch (Exception e) {
            logger.error(e);
        }

    }

    /**
     * @function 发送给指定用户
     * @param code 邮件模板标识码
     * @param receivers 收件人
     * @param parameter 模板内容关键字参数
     */
    public void sendMail(String code, String receivers, Map<String, Object> parameter) {
        if (parameter == null) {
            parameter = new LinkedHashMap<>();
        }

        EmailTemplateVO mailTemplateVO = emailTemplateService.getTemplateByCode(code);
        EmailConfigVO mailConfigVO = emailConfigService.getConfigById(mailTemplateVO.getConfigId());

        String serverName = mailConfigVO.getSender();
        String account = mailConfigVO.getSenderAddress();
        String senderAddress = "<" + account + ">";
        String password = mailConfigVO.getPassword();
        String subject = mailTemplateVO.getSubject();
        String content = generateEmailContent(mailTemplateVO.getContent(), parameter);
        try {

            Properties props = new Properties();
            // 也可用Properties props = System.getProperties();
            props.put(EmailConf.MAIL_SMTP_HOST, EmailConf.SMTP_EXMIAL_QQ_COM);
            // 存储发送邮件服务器的信息
            props.put(EmailConf.MAIL_SMTP_AUTH, EmailConf.TRUE);
            // 同时通过验证
            Session s = Session.getInstance(props);
            // 根据属性新建一个邮件会话
//            s.setDebug(true);
            // 输出跟踪日志

            MimeMessage message = new MimeMessage(s);
            // 由邮件会话新建一个消息对象

            // 设置邮件
            InternetAddress from = new InternetAddress(serverName + senderAddress);
            message.setFrom(from);// 设置发件人

            InternetAddress to = new InternetAddress(receivers);
            message.setRecipient(Message.RecipientType.TO, to);// 设置收件人,并设置其接收类型为TO

            //乱码
            //message.setSubject(MimeUtility.encodeText(ttitle));
            message.setSubject(MimeUtility.encodeText(subject, "UTF-8", "B"));
            message.setText(content);// 设置信件内容

            message.setSentDate(new java.util.Date());// 设置发信时间

            BodyPart mdp = new MimeBodyPart();
            mdp.setContent(content, "text/html;charset=UTF-8");
            Multipart mm = new MimeMultipart();
            mm.addBodyPart(mdp);
            message.setContent(mm);

            // 发送邮件
            message.saveChanges();// 存储邮件信息
            Transport transport = s.getTransport(EmailConf.MAIL_SERVICE);
            transport.connect(EmailConf.SMTP_EXMIAL_QQ_COM, account, password);// 以smtp方式登录邮箱
            transport.sendMessage(message, message.getAllRecipients());// 发送邮件,其中第二个参数是所有
            // 已设好的收件人地址
            transport.close();

            //写入发送纪录
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("id", StringUtil.generateUUID());
            data.put("sender_address", account);
            data.put("recipient_address", receivers);
            data.put("content", content);
            data.put("type", AppConstant.MESSAGE_TYPE_EMAIL);
            data.put("create_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
            messageRecordService.addRecord(data);

        } catch (Exception e) {
            logger.error(e);
        }
    }

    /**
     * @function 发送给指定用户，并密送给模板用户
     * @param code 邮件模板标识码
     * @param receivers 收件人
     * @param parameter 模板内容关键字参数
     */
    public void sendMailWithBcc(String code, String receivers, Map<String, Object> parameter) {

        if (parameter == null) {
            parameter = new LinkedHashMap<>();
        }

        EmailTemplateVO mailTemplateVO = emailTemplateService.getTemplateByCode(code);
        EmailConfigVO mailConfigVO = emailConfigService.getConfigById(mailTemplateVO.getConfigId());

        String serverName = mailConfigVO.getSender();
        String account = mailConfigVO.getSenderAddress();
        String senderAddress = "<" + account + ">";
        String password = mailConfigVO.getPassword();
        String[] admin = mailTemplateVO.getRecipient().split(";");
        String subject = mailTemplateVO.getSubject();
        String content = generateEmailContent(mailTemplateVO.getContent(), parameter);
        try {

            Properties props = new Properties();
            // 也可用Properties props = System.getProperties();
            props.put(EmailConf.MAIL_SMTP_HOST, EmailConf.SMTP_EXMIAL_QQ_COM);
            // 存储发送邮件服务器的信息
            props.put(EmailConf.MAIL_SMTP_AUTH, EmailConf.TRUE);
            // 同时通过验证
            Session s = Session.getInstance(props);
            // 根据属性新建一个邮件会话
            //            s.setDebug(true);
            // 输出跟踪日志

            MimeMessage message = new MimeMessage(s);
            // 由邮件会话新建一个消息对象

            // 设置邮件
            InternetAddress from = new InternetAddress(serverName + senderAddress);
            message.setFrom(from);// 设置发件人

            Address[] tos = null;
            if (admin != null) {
                // 为每个邮件接收者创建一个地址
                tos = new InternetAddress[admin.length];
                for (int i = 0; i < admin.length; i++) {
                    tos[i] = new InternetAddress(admin[i]);
                }
            }
            InternetAddress to = new InternetAddress(receivers);
            message.setRecipient(Message.RecipientType.TO, to);// 设置收件人,并设置其接收类型为TO
            message.setRecipients(Message.RecipientType.BCC, tos); //设置密送收件人,并设置其接收类型为BBC

            //乱码
            //message.setSubject(MimeUtility.encodeText(ttitle));
            message.setSubject(MimeUtility.encodeText(subject, "UTF-8", "B"));
            message.setText(content);// 设置信件内容

            message.setSentDate(new java.util.Date());// 设置发信时间

            BodyPart mdp = new MimeBodyPart();
            mdp.setContent(content, "text/html;charset=UTF-8");
            Multipart mm = new MimeMultipart();
            mm.addBodyPart(mdp);
            message.setContent(mm);

            // 发送邮件
            message.saveChanges();// 存储邮件信息
            Transport transport = s.getTransport(EmailConf.MAIL_SERVICE);
            transport.connect(EmailConf.SMTP_EXMIAL_QQ_COM, account, password);// 以smtp方式登录邮箱
            transport.sendMessage(message, message.getAllRecipients());// 发送邮件,其中第二个参数是所有
            // 已设好的收件人地址
            transport.close();

            //写入发送纪录
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("id", StringUtil.generateUUID());
            data.put("sender_address", account);
            data.put("recipient_address", receivers+";"+admin);
            data.put("content", content);
            data.put("type", AppConstant.MESSAGE_TYPE_EMAIL);
            data.put("create_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
            messageRecordService.addRecord(data);
        }catch (Exception e) {
            logger.error(e);
        }
    }


}
