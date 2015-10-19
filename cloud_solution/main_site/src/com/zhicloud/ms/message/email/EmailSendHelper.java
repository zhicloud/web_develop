package com.zhicloud.ms.message.email;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;
import com.zhicloud.ms.common.util.StringUtil;
import org.apache.log4j.Logger;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.Date;
import java.util.Properties;

/**
 * @function 邮件发送helper
 * @author 张翔
 */
public class EmailSendHelper {

    private static final Logger logger = Logger.getLogger(EmailSendHelper.class);


    /**
     * @function 发送邮件
     * @param serverType 邮件服务器类型
     * @param emailConfHelper 邮件服务配置参数
     * @param emailHelper 邮件内容参数
     * @param account 账号
     * @param password 密码
     */
    public static void sendMail(String serverType, EmailConfHelper emailConfHelper, EmailHelper emailHelper, String account, String password) {
        try {

            //获取参数
            String mail_host = "mail."+serverType+".host";
            String mail_port = "mail."+serverType+".port";
            String mail_auth = "mail."+serverType+".auth";

            String host = emailConfHelper.getHost();
            String port = emailConfHelper.getPort();
            String isAuth = "0".equals(emailConfHelper.getIsAuth())?"false":"true";
            String senderName = emailHelper.getSenderName();
            String sender = emailHelper.getSender();
            String receiver = emailHelper.getReceiver();
            String subject = emailHelper.getSubject();
            String content = emailHelper.getContent();
            String bccReceiver = emailHelper.getBccReceiver();

            // 也可用Properties props = System.getProperties();
            Properties props = new Properties();

            // 存储发送邮件服务器的信息

            props.put(mail_host, host);
            props.put(mail_port, port);

            // 同时通过验证
            props.put(mail_auth, isAuth);

            // 根据属性新建一个邮件会话
            Session s = Session.getInstance(props);

            // 输出跟踪日志
//            s.setDebug(true);

            // 由邮件会话新建一个消息对象
            MimeMessage message = new MimeMessage(s);

            // 设置邮件
            InternetAddress from = new InternetAddress(senderName + sender);
            message.setFrom(from);// 设置发件人

            InternetAddress to = new InternetAddress(receiver);
            message.setRecipient(Message.RecipientType.TO, to);// 设置收件人,并设置其接收类型为TO

            //设置密送
            if (StringUtil.trim(bccReceiver) != null && !"".equals(StringUtil.trim(bccReceiver))) {
                InternetAddress bcc = new InternetAddress(bccReceiver);
                message.setRecipient(Message.RecipientType.BCC, bcc);
            }

            //乱码
            message.setSubject(MimeUtility.encodeText(subject, "UTF-8", "B"));
            message.setText(content);// 设置信件内容

            message.setSentDate(new Date());// 设置发信时间

            BodyPart mdp = new MimeBodyPart();
            mdp.setContent(content, "text/html;charset=UTF-8");
            Multipart mm = new MimeMultipart();
            mm.addBodyPart(mdp);
            message.setContent(mm);

            // 发送邮件
            message.saveChanges();// 存储邮件信息
            Transport transport = s.getTransport(serverType);
            transport.connect(host, account, password);//
            transport.sendMessage(message, message.getAllRecipients());// 发送邮件,其中第二个参数是所有
            // 已设好的收件人地址
            transport.close();


        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
        }
    }
}
