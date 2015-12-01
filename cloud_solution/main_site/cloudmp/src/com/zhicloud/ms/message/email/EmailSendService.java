package com.zhicloud.ms.message.email;

import com.zhicloud.ms.constant.AppConstant;
import com.zhicloud.ms.service.IEmailConfigService;
import com.zhicloud.ms.service.IEmailTemplateService;
import com.zhicloud.ms.service.IMessageRecordService;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.EmailConfigVO;
import com.zhicloud.ms.vo.EmailTemplateVO;

import org.apache.log4j.Logger; 
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by sean on 7/8/15.
 */
@Transactional(readOnly = false)
public class EmailSendService {

 
    private EmailHtmlContentGenerator emailHtmlContentGenerator;

    private IEmailConfigService emailConfigService;

    private IEmailTemplateService emailTemplateService;

    private IMessageRecordService messageRecordService;


    BeanFactory factory = new ClassPathXmlApplicationContext("classpath:/applicationContext*.xml");

    {
        emailConfigService = (IEmailConfigService)factory.getBean("emailConfigService");
        emailTemplateService = (IEmailTemplateService) factory.getBean("emailTemplateService");
        messageRecordService = (IMessageRecordService)factory.getBean("messageRecordService");

    }


    /**
     * @function 生成Email内容
     * @param path 文本路径
     * @param parameter 模板内容关键字参数
     * @return Email正文内容
     */
    private String generateEmailContent(String path, Map<String, Object> parameter) throws Exception{

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
    public void sendMail(String code, Map<String, Object> parameter) throws Exception{

        if (parameter == null) {
            parameter = new LinkedHashMap<>();
        }

        EmailTemplateVO mailTemplateVO = emailTemplateService.getTemplateByCode(code);
        EmailConfigVO mailConfigVO = emailConfigService.getConfigById(mailTemplateVO.getConfigId());

        //获取参数
        String serverType = mailConfigVO.getProtocol();
        String host = mailConfigVO.getHost();
        String port = String.valueOf(mailConfigVO.getPort());
        String isAUth = String.valueOf(mailConfigVO.getIsAuth());
        EmailConfHelper emailConfHelper = new EmailConfHelper();
        emailConfHelper.setHost(host);
        emailConfHelper.setPort(port);
        emailConfHelper.setIsAuth(isAUth);

        String serverName = mailConfigVO.getSender();
        String account = mailConfigVO.getSenderAddress();
        String password = mailConfigVO.getPassword();

        String senderAddress = "<" + account + ">";
        String recipient = mailTemplateVO.getRecipient();
        String subject = mailTemplateVO.getSubject();
        String content = generateEmailContent(mailTemplateVO.getContent(), parameter);
        EmailHelper emailHelper = new EmailHelper();
        emailHelper.setSenderName(serverName);
        emailHelper.setSender(senderAddress);
        emailHelper.setReceiver(recipient);
        emailHelper.setSubject(subject);
        emailHelper.setContent(content);

        EmailSendHelper.sendMail(serverType, emailConfHelper, emailHelper, account, password);

        //写入发送纪录
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", StringUtil.generateUUID());
        data.put("sender_address", account);
        data.put("recipient_address", recipient);
        data.put("content", content);
        data.put("type", AppConstant.MESSAGE_TYPE_EMAIL);
        data.put("create_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
        messageRecordService.addRecord(data);

    }

    /**
     * @function 发送给指定用户
     * @param code 邮件模板标识码
     * @param receiver 收件人
     * @param parameter 模板内容关键字参数
     */
    public void sendMail(String code, String receiver, Map<String, Object> parameter) throws Exception{
        if (parameter == null) {
            parameter = new LinkedHashMap<>();
        }

        EmailTemplateVO mailTemplateVO = emailTemplateService.getTemplateByCode(code);
        EmailConfigVO mailConfigVO = emailConfigService.getConfigById(mailTemplateVO.getConfigId());

        //获取参数
        String serverType = mailConfigVO.getProtocol();
        String host = mailConfigVO.getHost();
        String port = String.valueOf(mailConfigVO.getPort());
        String isAUth = String.valueOf(mailConfigVO.getIsAuth());
        EmailConfHelper emailConfHelper = new EmailConfHelper();
        emailConfHelper.setHost(host);
        emailConfHelper.setPort(port);
        emailConfHelper.setIsAuth(isAUth);

        String serverName = mailConfigVO.getSender();
        String account = mailConfigVO.getSenderAddress();
        String password = mailConfigVO.getPassword();

        String senderAddress = "<" + account + ">";
        String subject = mailTemplateVO.getSubject();
        String content = generateEmailContent(mailTemplateVO.getContent(), parameter);
        EmailHelper emailHelper = new EmailHelper();
        emailHelper.setSenderName(serverName);
        emailHelper.setSender(senderAddress);
        emailHelper.setReceiver(receiver);
        emailHelper.setSubject(subject);
        emailHelper.setContent(content);

        EmailSendHelper.sendMail(serverType, emailConfHelper, emailHelper, account, password);

        //写入发送纪录
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", StringUtil.generateUUID());
        data.put("sender_address", account);
        data.put("recipient_address", receiver);
        data.put("content", content);
        data.put("type", AppConstant.MESSAGE_TYPE_EMAIL);
        data.put("create_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
        messageRecordService.addRecord(data);
    }

    /**
     * @function 发送给指定用户，并密送给模板用户
     * @param code 邮件模板标识码
     * @param receiver 收件人
     * @param parameter 模板内容关键字参数
     */
    public void sendMailWithBcc(String code, String receiver, Map<String, Object> parameter) throws Exception{

        if (parameter == null) {
            parameter = new LinkedHashMap<>();
        }

        EmailTemplateVO mailTemplateVO = emailTemplateService.getTemplateByCode(code);
        EmailConfigVO mailConfigVO = emailConfigService.getConfigById(mailTemplateVO.getConfigId());

        //获取参数
        String serverType = mailConfigVO.getProtocol();
        String host = mailConfigVO.getHost();
        String port = String.valueOf(mailConfigVO.getPort());
        String isAUth = String.valueOf(mailConfigVO.getIsAuth());
        EmailConfHelper emailConfHelper = new EmailConfHelper();
        emailConfHelper.setHost(host);
        emailConfHelper.setPort(port);
        emailConfHelper.setIsAuth(isAUth);

        String serverName = mailConfigVO.getSender();
        String account = mailConfigVO.getSenderAddress();
        String password = mailConfigVO.getPassword();

        String senderAddress = "<" + account + ">";
        String subject = mailTemplateVO.getSubject();
        String content = generateEmailContent(mailTemplateVO.getContent(), parameter);
        String bccReceiver = mailTemplateVO.getRecipient();
        EmailHelper emailHelper = new EmailHelper();
        emailHelper.setSenderName(serverName);
        emailHelper.setSender(senderAddress);
        emailHelper.setReceiver(receiver);
        emailHelper.setBccReceiver(bccReceiver);
        emailHelper.setSubject(subject);
        emailHelper.setContent(content);

        EmailSendHelper.sendMail(serverType, emailConfHelper, emailHelper, account, password);



        //写入发送纪录
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("id", StringUtil.generateUUID());
        data.put("sender_address", account);
        data.put("recipient_address", receiver+";"+bccReceiver);
        data.put("content", content);
        data.put("type", AppConstant.MESSAGE_TYPE_EMAIL);
        data.put("create_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
        messageRecordService.addRecord(data);

    }


}
