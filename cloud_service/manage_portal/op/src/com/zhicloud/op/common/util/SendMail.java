package com.zhicloud.op.common.util;

import com.sun.xml.internal.messaging.saaj.packaging.mime.internet.MimeUtility;
import com.zhicloud.op.app.propeties.AppProperties;
import com.zhicloud.op.common.util.constant.MailConstant;
import com.zhicloud.op.message.MessageConstant;
import com.zhicloud.op.message.MessageServiceManager;
import com.zhicloud.op.message.email.EmailSendService;
import com.zhicloud.op.vo.AgentVO;
import com.zhicloud.op.vo.TerminalUserVO;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
@Transactional(readOnly = false)
public class SendMail
{
    
    private static final Logger logger = Logger.getLogger(SendMail.class);
    
    public void sendEmail(Map<String, Object> terminalUserData) {
        String des_id = terminalUserData.get("id").toString();
        String str = null;
        DesActivate des = null;
        try {
            des = new DesActivate(MailConstant.ENCRYPTION_KEY);
            str = des.encrypt(des_id);
        } 
        catch (Exception e) {
        }
        try {

            EmailSendService emailSendService = new MessageServiceManager().singleton().getMailService();
            String receiver = terminalUserData.get("email").toString();
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("address_of_this_system", AppProperties.getValue("address_of_this_system", "/"));
            params.put("str",str);
            emailSendService.sendMail(MessageConstant.EMAIL_INFO_REGISTER, receiver, params);

        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }

    public void changePasswordEmail(Map<String, Object> terminalUserData) {
        String des_id = terminalUserData.get("id").toString();
        String str = null;
        DesActivate des = null;
        try {
            des = new DesActivate(MailConstant.ENCRYPTION_KEY);
            str = des.encrypt(des_id);
        } 
        catch (Exception e) {
        }
        try {

            EmailSendService emailSendService = new MessageServiceManager().singleton().getMailService();
            String receiver = terminalUserData.get("email").toString();
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("address_of_this_system", AppProperties.getValue("address_of_this_system", "/"));
            params.put("str",str);
            emailSendService.sendMail(MessageConstant.EMAIL_INFO_CHANGE_PASSWORD, receiver, params);


        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }
    //----------------------
    public void resetPasswordEmail(Map<String, Object> user) {
        try {

            EmailSendService emailSendService = new MessageServiceManager().singleton().getMailService();
            String receiver = user.get("email").toString();
            Map<String, Object> params = new HashMap<String, Object>();
            String password = user.get("password").toString();
            params.put("password",password);
            emailSendService.sendMail(MessageConstant.EMAIL_INFO_RESET_PASSWORD, receiver, params);

        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }
    //----------------------
    public void sendPasswordEmail(Map<String, Object> user) {
        
        try {

            EmailSendService emailSendService = new MessageServiceManager().singleton().getMailService();
            String receiver = user.get("email").toString();
            Map<String, Object> params = new HashMap<String, Object>();
            String password = user.get("password").toString();
            params.put("password",password);
            emailSendService.sendMail(MessageConstant.EMAIL_INFO_INIT_PASSWORD, receiver, params);

        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }
    //----------------------
    public void sendPasswordEmailForAgent(Map<String, Object> user) {
        
        try {


            EmailSendService emailSendService = new MessageServiceManager().singleton().getMailService();
            String receiver = user.get("email").toString();
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("username",user.get("email").toString());
            String password = user.get("password").toString();
            params.put("password",password);
            params.put("address_of_this_system",AppProperties.getValue("address_of_this_system", ""));
            emailSendService.sendMail(MessageConstant.EMAIL_INFO_AGENT_INIT_PASSWORD, receiver, params);


        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }
    public void sendPasswordEmailForTerminalUser(Map<String, Object> user) {
        
        try {

            EmailSendService emailSendService = new MessageServiceManager().singleton().getMailService();
            String receiver = user.get("email").toString();
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("username",user.get("email").toString());
            String password = user.get("password").toString();
            params.put("password",password);
            emailSendService.sendMail(MessageConstant.EMAIL_INFO_USER_INIT_PASSWORD, receiver, params);

        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }
    
    public void forgetAccountEmail(Map<String, Object> terminalUserData) {
        
        try {

            EmailSendService emailSendService = new MessageServiceManager().singleton().getMailService();
            String receiver = terminalUserData.get("email").toString();
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("account",terminalUserData.get("account").toString());
            emailSendService.sendMail(MessageConstant.EMAIL_INFO_FORGET_USERNAME, receiver, params);


        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }
    
    public void terminalCheckEmail(Map<String, Object> user) {
        
        try {

            EmailSendService emailSendService = new MessageServiceManager().singleton().getMailService();
            String receiver = user.get("email").toString();
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("code",user.get("code").toString());
            emailSendService.sendMail(MessageConstant.EMAIL_INFO_EMAIL_CHECK, receiver, params);


        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }
public void terminalCheckEmailForChangeMobile(Map<String, Object> user) {
        
        try {

            EmailSendService emailSendService = new MessageServiceManager().singleton().getMailService();
            String receiver = user.get("email").toString();
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("code",user.get("code").toString());
            emailSendService.sendMail(MessageConstant.EMAIL_INFO_PHONE_CHECK, receiver, params);

        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }
public void resetPasswordEmailCode(Map<String, Object> user) {
    
    try {

        EmailSendService emailSendService = new MessageServiceManager().singleton().getMailService();
        String receiver = user.get("email").toString();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("code",user.get("code").toString());
        emailSendService.sendMail(MessageConstant.EMAIL_INFO_LOGIN_CHECK, receiver, params);

    } catch (Exception e) {
        System.out.print(e.toString());
    }
}
    public void sendInviteCodeEmail(Map<String, Object> terminalUserData) {
        String inviteCode = terminalUserData.get("inviteCode").toString();
        try {

            EmailSendService emailSendService = new MessageServiceManager().singleton().getMailService();
            String receiver = terminalUserData.get("email").toString();
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("invite_code", inviteCode);
            params.put("address_of_this_system", AppProperties.getValue("address_of_this_system ", "/"));
            emailSendService.sendMail(MessageConstant.EMAIL_INFO_INVITAION_CODE, receiver, params);


        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }
    
    public void sendRechargeMessaToTerminalUser(Map<String, Object> terminalUserData) {
        String day = terminalUserData.get("day").toString();
        String account = terminalUserData.get("account").toString();
        try {

            EmailSendService emailSendService = new MessageServiceManager().singleton().getMailService();
            String receiver = terminalUserData.get("email").toString();
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("day", day);
            params.put("account", account);
            emailSendService.sendMail(MessageConstant.EMAIL_INFO_BALANCE_NOTIFICATION, receiver, params);

        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }
    
    public void sendMessaToTerminalUser(Map<String, Object> terminalUserData)
    {
        String recipientEmail = (String)terminalUserData.get("recipientEmail");
        String content        = (String)terminalUserData.get("content");
        try
        {
            String ttitle = "【致云Zhicloud】通知-致云科技";
//          String tcontent =   "<body>"
//                              + "<div id='content'>"
//                              + "尊敬的"
//                              + account
//                              + ":"
//                              + "<br>"
//                              + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您好！<br>"
//                              + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您的余额还可以支付："
//                              + day
//                              + "天的费用，为不影响您的云主机正常使用，请及时充值。谢谢" 
//                              + "<br>感谢您的支持！"
//                              + "<br>【云端在线&致云科技】"
//                              + "</body>";
            Properties props = new Properties();
            // 也可用Properties props = System.getProperties();
            props.put(MailConstant.MAIL_SMTP_HOST, MailConstant.SMTP_EXMIAL_QQ_COM);
            // 存储发送邮件服务器的信息
            props.put(MailConstant.MAIL_SMTP_AUTH, MailConstant.TRUE);
            // 同时通过验证
            Session s = Session.getInstance(props);
            // 根据属性新建一个邮件会话
            // s.setDebug(true);
            // 输出跟踪日志

            MimeMessage message = new MimeMessage(s);
            // 由邮件会话新建一个消息对象

            // 设置邮件
            InternetAddress from = new InternetAddress(MailConstant.ENCRYPTION_KEY + MailConstant.ADDRESSOR);
            message.setFrom(from);// 设置发件人
            InternetAddress to = new InternetAddress(recipientEmail);
            message.setRecipient(Message.RecipientType.TO, to);// 设置收件人,并设置其接收类型为TO

            // 乱码
            // message.setSubject(MimeUtility.encodeText(ttitle));
            message.setSubject(MimeUtility.encodeText(ttitle, "UTF-8", "B"));
            message.setText(content);// 设置信件内容

            message.setSentDate(new java.util.Date());// 设置发信时间

            BodyPart mdp = new MimeBodyPart();
            mdp.setContent(content, "text/html;charset=UTF-8");
            Multipart mm = new MimeMultipart();
            mm.addBodyPart(mdp);
            message.setContent(mm);

            // 发送邮件
            message.saveChanges();// 存储邮件信息
            Transport transport = s.getTransport(MailConstant.MAIL_SERVICE);
            transport.connect(MailConstant.SMTP_EXMIAL_QQ_COM, MailConstant.MAIL_ACCOUNT, MailConstant.PASSWORD);// 以smtp方式登录邮箱
            transport.sendMessage(message, message.getAllRecipients());// 发送邮件,其中第二个参数是所有
            
            // 已设好的收件人地址
            transport.close();
        }
        catch (Exception e)
        {
            logger.error("["+Thread.currentThread().getId()+"] ", e);
        }
    }
    
    public void sendCouponToTerminalUser(Map<String, Object> terminalUserData)
    {
        String recipientEmail = (String)terminalUserData.get("email");
        String content        = (String)terminalUserData.get("content");
        try
        {
            String ttitle = "【致云Zhicloud】通知-致云科技";
//          String tcontent =   "<body>"
//                              + "<div id='content'>"
//                              + "尊敬的"
//                              + account
//                              + ":"
//                              + "<br>"
//                              + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您好！<br>"
//                              + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您的余额还可以支付："
//                              + day
//                              + "天的费用，为不影响您的云主机正常使用，请及时充值。谢谢" 
//                              + "<br>感谢您的支持！"
//                              + "<br>【云端在线&致云科技】"
//                              + "</body>";
            Properties props = new Properties();
            // 也可用Properties props = System.getProperties();
            props.put(MailConstant.MAIL_SMTP_HOST, MailConstant.SMTP_EXMIAL_QQ_COM);
            // 存储发送邮件服务器的信息
            props.put(MailConstant.MAIL_SMTP_AUTH, MailConstant.TRUE);
            // 同时通过验证
            Session s = Session.getInstance(props);
            // 根据属性新建一个邮件会话
            // s.setDebug(true);
            // 输出跟踪日志

            MimeMessage message = new MimeMessage(s);
            // 由邮件会话新建一个消息对象

            // 设置邮件
            InternetAddress from = new InternetAddress(MailConstant.ENCRYPTION_KEY + MailConstant.ADDRESSOR);
            message.setFrom(from);// 设置发件人
            InternetAddress to = new InternetAddress(recipientEmail);
            message.setRecipient(Message.RecipientType.TO, to);// 设置收件人,并设置其接收类型为TO

            // 乱码
            // message.setSubject(MimeUtility.encodeText(ttitle));
            message.setSubject(MimeUtility.encodeText(ttitle, "UTF-8", "B"));
            message.setText(content);// 设置信件内容

            message.setSentDate(new java.util.Date());// 设置发信时间

            BodyPart mdp = new MimeBodyPart();
            mdp.setContent(content, "text/html;charset=UTF-8");
            Multipart mm = new MimeMultipart();
            mm.addBodyPart(mdp);
            message.setContent(mm);

            // 发送邮件
            message.saveChanges();// 存储邮件信息
            Transport transport = s.getTransport(MailConstant.MAIL_SERVICE);
            transport.connect(MailConstant.SMTP_EXMIAL_QQ_COM, MailConstant.MAIL_ACCOUNT, MailConstant.PASSWORD);// 以smtp方式登录邮箱
            transport.sendMessage(message, message.getAllRecipients());// 发送邮件,其中第二个参数是所有
            
            // 已设好的收件人地址
            transport.close();
        }
        catch (Exception e)
        {
            logger.error("["+Thread.currentThread().getId()+"] ", e);
        }
    }
    
    public void sendHintEmail(Object obj,String day,String t) {
        String email = "";
        String account = "";
        if(obj instanceof TerminalUserVO){
            email = ((TerminalUserVO)obj).getEmail();
            account = ((TerminalUserVO)obj).getAccount();
        }
        if(obj instanceof AgentVO){
            email = ((AgentVO)obj).getEmail();
            account = ((AgentVO)obj).getAccount();
        }
        try {
            if(t.contains("零")){
                EmailSendService emailSendService = new MessageServiceManager().singleton().getMailService();
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("day", day);
                params.put("account", account);
                emailSendService.sendMail(MessageConstant.EMAIL_INFO_BALANCE_ZERO, email, params);
            }else{
                EmailSendService emailSendService = new MessageServiceManager().singleton().getMailService();
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("day", day);
                params.put("account", account);
                emailSendService.sendMail(MessageConstant.EMAIL_INFO_BALANCE_NOTIFICATION, email, params);
            }

        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }
       /**
        * flag : 1:告警 2：恢复
        * @param region
        * @param flag
        */
       public void sendHttpGateWayStatus(String region,String flag) {
           String type = AppProperties.getValue("version_type", "");
           String receiver = AppProperties.getValue("monitor_mail", "");

           try {

            if("1".equals(flag)){
                EmailSendService emailSendService = new MessageServiceManager().singleton().getMailService();
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("type", type);
                params.put("region", region);
                params.put("content", "地域的http-gateway连接失败，请及时排查解决。");
                emailSendService.sendMail(MessageConstant.EMAIL_INFO_WARN, receiver, params);
            }else{
                EmailSendService emailSendService = new MessageServiceManager().singleton().getMailService();
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("type", type);
                params.put("region", region);
                params.put("content", "地域的http-gateway已恢复连接。");
                emailSendService.sendMail(MessageConstant.EMAIL_INFO_RCOVER, receiver, params);

            }

        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }
    /**
     * @param info
     * @param flag 1:异常      2：正常
//     * @param status 具体异常内容：告警、故障或其它
     */
    public void sendHttpGatewayDetail(String info,String flag) {
        String type = AppProperties.getValue("version_type", "");
        String receiver = AppProperties.getValue("monitor_mail", "");

        try {

            if("1".equals(flag)){
                EmailSendService emailSendService = new MessageServiceManager().singleton().getMailService();
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("type", type);
                params.put("content", info+"请及时排查解决。");
                emailSendService.sendMail(MessageConstant.EMAIL_INFO_WARN, receiver, params);

            }else{
                EmailSendService emailSendService = new MessageServiceManager().singleton().getMailService();
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("type", type);
                params.put("content", info+"已恢复正常。");
                emailSendService.sendMail(MessageConstant.EMAIL_INFO_RCOVER, receiver, params);
                
            }

        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }
    
    /**
     * @Description:检测数据库连接信息发送邮件
     * @param flag 数据库连接状态true为成功，false为失败
     */
    public void sendConnectInfo(boolean flag) {
        String type = AppProperties.getValue("version_type", "");
        String receiver = AppProperties.getValue("monitor_mail", "");

        try {

            if(flag){
                EmailSendService emailSendService = new MessageServiceManager().singleton().getMailService();
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("type", type);
                params.put("content", "数据库连接已恢复正常。");
                emailSendService.sendMail(MessageConstant.EMAIL_INFO_RCOVER, receiver, params);

            }else{
                EmailSendService emailSendService = new MessageServiceManager().singleton().getMailService();
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("type", type);
                params.put("content", "云端在线发现告警，数据库无法连接或连接超时。");
                emailSendService.sendMail(MessageConstant.EMAIL_INFO_WARN, receiver, params);

            }
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }  
    
    /**
     * flag : 1:告警 2：恢复
     * @param userName
     * @param contact
     */
    public void sendSuggestion(String userName,String contact,String userType,String content) { 
 		try {
 			  String type = AppProperties.getValue("version_type", "");
        String receiver = AppProperties.getValue("monitor_mail", "");

        EmailSendService emailSendService = new MessageServiceManager().singleton().getMailService();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("type", type);
        params.put("user_type", userType);
        params.put("user_name", userName);
        params.put("content", content);
        params.put("contact", contact);
        emailSendService.sendMail(MessageConstant.EMAIL_INFO_SUGGESTION, receiver, params);

		} catch (Exception e) {
			System.out.print(e.toString());
		}
	}
     
    /**
     * @Description:检测服务器状态数据
     * @param flag 数据库连接状态true为成功，false为失败
     */
    public void sendServerErrorInfo(Integer flag, String content) {
        String type = AppProperties.getValue("version_type", "");
        String receiver = AppProperties.getValue("monitor_mail", "");

        try {

            if (flag == 0) {
                EmailSendService emailSendService = new MessageServiceManager().singleton().getMailService();
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("type", type);
                params.put("content", "以下服务器已恢复正常:\n" + content);
                emailSendService.sendMail(MessageConstant.EMAIL_INFO_RCOVER, receiver, params);

            } else if (flag == 1) {
                EmailSendService emailSendService = new MessageServiceManager().singleton().getMailService();
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("type", type);
                params.put("content", "以下服务器已恢复正常:\n" + content);
                emailSendService.sendMail(MessageConstant.EMAIL_INFO_WARN, receiver, params);

            } else {

                EmailSendService emailSendService = new MessageServiceManager().singleton().getMailService();
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("type", type);
                params.put("content", "以下服务器当天处于告警状态：:\n" + content);
                emailSendService.sendMail(MessageConstant.EMAIL_INFO_WARN_SUMMARY, receiver, params);

            }

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
    /**
     * @Description:发票索取邮件通知
     */
    public void sendBillNotice(Map<String, Object> terminalUserData) {        
        try {
            EmailSendService emailSendService = new MessageServiceManager().singleton().getMailService();
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("customer", terminalUserData.get("customer").toString());
            emailSendService.sendMail(MessageConstant.EMAIL_BILL_NOTICE, params);
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }
}

