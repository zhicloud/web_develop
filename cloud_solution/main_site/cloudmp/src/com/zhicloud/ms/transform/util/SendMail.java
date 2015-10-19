package com.zhicloud.ms.transform.util;

import java.util.Map;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import org.apache.log4j.Logger;
import com.zhicloud.ms.app.propeties.AppProperties;
import com.zhicloud.ms.common.util.DesActivate;
import com.zhicloud.ms.common.util.constant.MailConstant;

public class SendMail
{
	
	private static final Logger logger = Logger.getLogger(SendMail.class);
	

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

			String ttitle = "云端在线忘记密码";
			String tcontent =

			"<body>"
					+ "<div id='content'>"
					+ "尊敬的用户"
					+ ":"
					+ "<br>"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您好！<br>"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;由于您忘记了您的密码，正在申请修改，所以您收到了这封邮件！现在您只需点击下面的链接就可进行密码的修改。"
					+ "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<a href='"+AppProperties.getValue("address_of_this_system", "/")+"user/changePasswordbysendmail.do?str="
					+ str
					+ "'>"+AppProperties.getValue("address_of_this_system", "/")+"user/changePasswordbysendmail.do?str="
					+ str
					+ "</a>"
					+ "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您也可以将地址复制到浏览器进行访问。"
					+ "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<strong>若您没申请过"
					+ "密码修改"
					+ "，请您忽略此邮件，给您的带来的不便敬请谅解！</strong><br>"
					+ "<br>致云科技感谢您的支持！"
					+ "</body>";
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
			InternetAddress from = new InternetAddress(MailConstant.ENCRYPTION_KEY
					+ MailConstant.ADDRESSOR);
			message.setFrom(from);// 设置发件人
			InternetAddress to = new InternetAddress(terminalUserData.get("email").toString());
			message.setRecipient(Message.RecipientType.TO, to);// 设置收件人,并设置其接收类型为TO

			//乱码
			//message.setSubject(MimeUtility.encodeText(ttitle));
			message.setSubject(MimeUtility.encodeText(ttitle, "UTF-8", "B"));
			message.setText(tcontent);// 设置信件内容

			message.setSentDate(new java.util.Date());// 设置发信时间

			BodyPart mdp = new MimeBodyPart();
			mdp.setContent(tcontent, "text/html;charset=UTF-8");
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
		} catch (Exception e) {
			System.out.print(e.toString());
		}
	}
	
	//----------------------
	public void resetPasswordEmail(Map<String, Object> user) {
		try {
			String ttitle = "致云Zhicloud重置密码";
			String password = user.get("password").toString();
			String tcontent =
					"<body>"
							+ "<div id='content'>"
							+ "尊敬的用户"
							+ ":"
							+ "<br>"
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您好！<br>"
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;由于您忘记了您的密码，申请重置密码，所以您收到了这封邮件！以下是系统为您提供的初始化密码，"
							+ "为了您账号的安全，请登录后及时修改。<br>"
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=red size=4>"+password
							+ "</font><br>"
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请勿把这封邮件泄露给其他非相关人士！<br>"
							+ "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;致云科技感谢您的支持！"
							+ "</body>";

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
			InternetAddress from = new InternetAddress(MailConstant.ENCRYPTION_KEY
					+ MailConstant.ADDRESSOR);
			message.setFrom(from);// 设置发件人
			InternetAddress to = new InternetAddress(user.get("email").toString());
			message.setRecipient(Message.RecipientType.TO, to);// 设置收件人,并设置其接收类型为TO
			
			//乱码
			//message.setSubject(MimeUtility.encodeText(ttitle));
			message.setSubject(MimeUtility.encodeText(ttitle, "UTF-8", "B"));
			message.setText(tcontent);// 设置信件内容

			message.setSentDate(new java.util.Date());// 设置发信时间

			BodyPart mdp = new MimeBodyPart();
			mdp.setContent(tcontent, "text/html;charset=UTF-8");
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
		} catch (Exception e) {
			System.out.print(e.toString());
		}
	}
	
	//----------------------
	public void sendPasswordEmail(Map<String, Object> user) {
		
		try {
			
			String ttitle = "致云Zhicloud初始密码";
			String password = user.get("password").toString();
			String tcontent =
					"<body>"
							+ "<div id='content'>"
							+ "尊敬的用户"
							+ ":"
							+ "<br>"
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您好！<br>"
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您的账户已经注册成功，以下是系统为您提供的初始密码。"
							+ "为了您账号的安全，请登录后及时修改。<br>"
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=red size=4>"+password
							+ "</font><br>"
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请勿把这封邮件泄露给其他非相关人士！<br>"
							+ "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;致云科技感谢您的支持！"
							+ "</body>";
			
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
			InternetAddress from = new InternetAddress(MailConstant.ENCRYPTION_KEY
					+ MailConstant.ADDRESSOR);
			message.setFrom(from);// 设置发件人
			InternetAddress to = new InternetAddress(user.get("email").toString());
			message.setRecipient(Message.RecipientType.TO, to);// 设置收件人,并设置其接收类型为TO
			
			//乱码
			//message.setSubject(MimeUtility.encodeText(ttitle));
			message.setSubject(MimeUtility.encodeText(ttitle, "UTF-8", "B"));
			message.setText(tcontent);// 设置信件内容
			
			message.setSentDate(new java.util.Date());// 设置发信时间
			
			BodyPart mdp = new MimeBodyPart();
			mdp.setContent(tcontent, "text/html;charset=UTF-8");
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
		} catch (Exception e) {
			System.out.print(e.toString());
		}
	}
	public void sendPasswordEmailForTerminalUser(Map<String, Object> user) {
		
		try {
			
			String ttitle = "致云Zhicloud初始密码";
			String password = user.get("password").toString();
			String tcontent =
					"<body>"
							+ "<div id='content'>"
							+ "尊敬的用户"
							+ ":"
							+ "<br>"
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您好！<br>"
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 恭喜您的账户已注册成功，点击下面的链接即可体验！ <br> "
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; https://www.zhicloud.com/ <br> "
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 登录账户是:"+user.get("email").toString()+" &nbsp;  初始密码为:"+password+"<br>"
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;(为了您账号的安全，请登录后及时修改)<br>"
 							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请勿把这封邮件泄露给其他非相关人士！<br>"
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;如果您有什么疑问、意见、建议，欢迎来电：<br>"
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;技术支持：4000-212-999          业务咨询：185 0207 7846<br>"
							+ "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;致云科技感谢您的支持！"
							+ "</body>";
			
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
			InternetAddress from = new InternetAddress(MailConstant.ENCRYPTION_KEY
					+ MailConstant.ADDRESSOR);
			message.setFrom(from);// 设置发件人
			InternetAddress to = new InternetAddress(user.get("email").toString());
			message.setRecipient(Message.RecipientType.TO, to);// 设置收件人,并设置其接收类型为TO
			
			//乱码
			//message.setSubject(MimeUtility.encodeText(ttitle));
			message.setSubject(MimeUtility.encodeText(ttitle, "UTF-8", "B"));
			message.setText(tcontent);// 设置信件内容
			
			message.setSentDate(new java.util.Date());// 设置发信时间
			
			BodyPart mdp = new MimeBodyPart();
			mdp.setContent(tcontent, "text/html;charset=UTF-8");
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
		} catch (Exception e) {
			System.out.print(e.toString());
		}
	}
	
    public void forgetAccountEmail(Map<String, Object> terminalUserData) {
		
		try {

			String ttitle = "云端在线忘记用户名";
			String account = terminalUserData.get("account").toString();
			String tcontent =
			"<body>"
					+ "<div id='content'>"
					+ "尊敬的用户"
					+ ":"
					+ "<br>"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您好！<br>"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;由于您忘记了您的用户名，申请找回，所以您收到了这封邮件！以下是系统为您找回的用户名，"
					+ "为了安全您的账号，请登陆修改。<br>"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=red size=4>"+account
					+ "</font><br>"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请别把这封邮件泄露给别人！<br>"
					+ "<br>致云科技感谢您的支持！"
					+ "</body>";

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
			InternetAddress from = new InternetAddress(MailConstant.ENCRYPTION_KEY
					+ MailConstant.ADDRESSOR);
			message.setFrom(from);// 设置发件人
			InternetAddress to = new InternetAddress(terminalUserData.get("email").toString());
			message.setRecipient(Message.RecipientType.TO, to);// 设置收件人,并设置其接收类型为TO
			
			//乱码
			//message.setSubject(MimeUtility.encodeText(ttitle));
			message.setSubject(MimeUtility.encodeText(ttitle, "UTF-8", "B"));
			message.setText(tcontent);// 设置信件内容

			message.setSentDate(new java.util.Date());// 设置发信时间

			BodyPart mdp = new MimeBodyPart();
			mdp.setContent(tcontent, "text/html;charset=UTF-8");
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
		} catch (Exception e) {
			System.out.print(e.toString());
		}
	}
	
    public void terminalCheckEmail(Map<String, Object> user) {
		
		try {

			String ttitle = "致云Zhicloud邮箱验证";
			String code = user.get("code").toString();
			String tcontent =
			"<body>"
					+ "<div id='content'>"
					+ "尊敬的用户"
					+ ":"
					+ "<br>"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您好！<br>"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;由于您将更改了您的邮箱信息，所以您收到了这封邮件！以下是系统为您提供的验证码。<br>"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=red size=4>"+code
					+ "</font><br>"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请勿把这封邮件泄露给他人！<br>"
					+ "<br>致云科技感谢您的支持！"
					+ "</body>";

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
			InternetAddress from = new InternetAddress(MailConstant.ENCRYPTION_KEY
					+ MailConstant.ADDRESSOR);
			message.setFrom(from);// 设置发件人
			InternetAddress to = new InternetAddress(user.get("email").toString());
			message.setRecipient(Message.RecipientType.TO, to);// 设置收件人,并设置其接收类型为TO
			
			//乱码
			//message.setSubject(MimeUtility.encodeText(ttitle));
			message.setSubject(MimeUtility.encodeText(ttitle, "UTF-8", "B"));
			message.setText(tcontent);// 设置信件内容

			message.setSentDate(new java.util.Date());// 设置发信时间

			BodyPart mdp = new MimeBodyPart();
			mdp.setContent(tcontent, "text/html;charset=UTF-8");
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
		} catch (Exception e) {
			System.out.print(e.toString());
		}
	}
    
public void terminalCheckEmailForChangeMobile(Map<String, Object> user) {
		
		try {

			String ttitle = "致云Zhicloud邮箱验证";
			String code = user.get("code").toString();
			String tcontent =
			"<body>"
					+ "<div id='content'>"
					+ "尊敬的用户"
					+ ":"
					+ "<br>"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您好！<br>"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;由于您将更改了您的手机信息，所以您收到了这封邮件！以下是系统为您提供的验证码。<br>"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=red size=4>"+code
					+ "</font><br>"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请勿把这封邮件泄露给他人！<br>"
					+ "<br>致云科技感谢您的支持！"
					+ "</body>";

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
			InternetAddress from = new InternetAddress(MailConstant.ENCRYPTION_KEY
					+ MailConstant.ADDRESSOR);
			message.setFrom(from);// 设置发件人
			InternetAddress to = new InternetAddress(user.get("email").toString());
			message.setRecipient(Message.RecipientType.TO, to);// 设置收件人,并设置其接收类型为TO
			
			//乱码
			//message.setSubject(MimeUtility.encodeText(ttitle));
			message.setSubject(MimeUtility.encodeText(ttitle, "UTF-8", "B"));
			message.setText(tcontent);// 设置信件内容

			message.setSentDate(new java.util.Date());// 设置发信时间

			BodyPart mdp = new MimeBodyPart();
			mdp.setContent(tcontent, "text/html;charset=UTF-8");
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
		} catch (Exception e) {
			System.out.print(e.toString());
		}
	}
public void resetPasswordEmailCode(Map<String, Object> user) {
	
	try {

		String ttitle = "致云Zhicloud邮箱验证";
		String code = user.get("code").toString();
		String tcontent =
		"<body>"
				+ "<div id='content'>"
				+ "尊敬的用户"
				+ ":"
				+ "<br>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您好！<br>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;由于您忘记了您的登陆信息，所以您收到了这封邮件！以下是系统为您提供的验证码。<br>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<font color=red size=4>"+code
				+ "</font><br>"
				+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请勿把这封邮件泄露给他人！<br>"
				+ "<br>致云科技感谢您的支持！"
				+ "</body>";

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
		InternetAddress from = new InternetAddress(MailConstant.ENCRYPTION_KEY
				+ MailConstant.ADDRESSOR);
		message.setFrom(from);// 设置发件人
		InternetAddress to = new InternetAddress(user.get("email").toString());
		message.setRecipient(Message.RecipientType.TO, to);// 设置收件人,并设置其接收类型为TO
		
		//乱码
		//message.setSubject(MimeUtility.encodeText(ttitle));
		message.setSubject(MimeUtility.encodeText(ttitle, "UTF-8", "B"));
		message.setText(tcontent);// 设置信件内容

		message.setSentDate(new java.util.Date());// 设置发信时间

		BodyPart mdp = new MimeBodyPart();
		mdp.setContent(tcontent, "text/html;charset=UTF-8");
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
	} catch (Exception e) {
		System.out.print(e.toString());
	}
}
    public void sendInviteCodeEmail(Map<String, Object> terminalUserData) {
		String inviteCode = terminalUserData.get("inviteCode").toString();
		try {

			String ttitle = "【云端在线】公有云邀请码-致云科技";
			String tcontent =

			"<body>"
					+ "<div id='content'>"
					+ "尊敬的用户"
					+ ":"
					+ "<br>"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您好！<br>"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;让您久等了，这是【云端在线】公有云邀请码："
					+ inviteCode
					+ "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;本邀请码7天有效，请尽快注册使用，谢谢。" 
					+ "<br>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;请在这里注册使用：<a href='"+AppProperties.getValue("address_of_this_system", "/")+"register.do?code="
					+ inviteCode
					+ "'>"+AppProperties.getValue("address_of_this_system", "/")+"register.do?code="
					+ inviteCode
					+ "</a>"
					+ "<br>感谢您的支持！"
					+ "<br>【云端在线&致云科技】"
					+ "</body>";
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
			InternetAddress from = new InternetAddress(MailConstant.ENCRYPTION_KEY
					+ MailConstant.ADDRESSOR);
			message.setFrom(from);// 设置发件人
			InternetAddress to = new InternetAddress(terminalUserData.get("email").toString());
			message.setRecipient(Message.RecipientType.TO, to);// 设置收件人,并设置其接收类型为TO

			//乱码
			//message.setSubject(MimeUtility.encodeText(ttitle));
			message.setSubject(MimeUtility.encodeText(ttitle, "UTF-8", "B"));
			message.setText(tcontent);// 设置信件内容

			message.setSentDate(new java.util.Date());// 设置发信时间

			BodyPart mdp = new MimeBodyPart();
			mdp.setContent(tcontent, "text/html;charset=UTF-8");
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
		} catch (Exception e) {
			System.out.print(e.toString());
		}
	}
    
    public void sendRechargeMessaToTerminalUser(Map<String, Object> terminalUserData) {
		String day = terminalUserData.get("day").toString();
		String account = terminalUserData.get("account").toString();
		try {

			String ttitle = "【致云Zhicloud】通知-致云科技";
			String tcontent =

			"<body>"
					+ "<div id='content'>"
					+ "尊敬的"
					+ account
					+ ":"
					+ "<br>"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您好！<br>"
					+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您的余额还可以支付："
					+ day
					+ "天的费用，为不影响您的云主机正常使用，请及时充值。谢谢" 
					+ "<br>"
					+ "<br>致云科技感谢您的支持！"
					+ "</body>";
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
			InternetAddress from = new InternetAddress(MailConstant.ENCRYPTION_KEY
					+ MailConstant.ADDRESSOR);
			message.setFrom(from);// 设置发件人
			InternetAddress to = new InternetAddress(terminalUserData.get("email").toString());
			message.setRecipient(Message.RecipientType.TO, to);// 设置收件人,并设置其接收类型为TO

			//乱码
			//message.setSubject(MimeUtility.encodeText(ttitle));
			message.setSubject(MimeUtility.encodeText(ttitle, "UTF-8", "B"));
			message.setText(tcontent);// 设置信件内容

			message.setSentDate(new java.util.Date());// 设置发信时间

			BodyPart mdp = new MimeBodyPart();
			mdp.setContent(tcontent, "text/html;charset=UTF-8");
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
//			String tcontent = 	"<body>"
//								+ "<div id='content'>"
//								+ "尊敬的"
//								+ account
//								+ ":"
//								+ "<br>"
//								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您好！<br>"
//								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您的余额还可以支付："
//								+ day
//								+ "天的费用，为不影响您的云主机正常使用，请及时充值。谢谢" 
//								+ "<br>感谢您的支持！"
//								+ "<br>【云端在线&致云科技】"
//								+ "</body>";
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
//			String tcontent = 	"<body>"
//								+ "<div id='content'>"
//								+ "尊敬的"
//								+ account
//								+ ":"
//								+ "<br>"
//								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您好！<br>"
//								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您的余额还可以支付："
//								+ day
//								+ "天的费用，为不影响您的云主机正常使用，请及时充值。谢谢" 
//								+ "<br>感谢您的支持！"
//								+ "<br>【云端在线&致云科技】"
//								+ "</body>";
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
    
       /**
        * flag : 1:告警 2：恢复
        * @param region
        * @param flag
        */
       public void sendHttpGateWayStatus(String region,String flag) {
    	   String type = AppProperties.getValue("version_type", "");
		try {
			
			String ttitle = " "; 
			String tcontent = " "; 
			if("1".equals(flag)){
				ttitle = "【"+type+"】致云ZhiCloud告警通知";
				tcontent =
						"<body>"
								+ "<div id='content'>"
								+ "管理员"
								+ ":"
								+ "<br>"
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您好！<br>"
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;云端在线发现告警。"
								+ "<font color=red size=4>"+region+"</font>地域的http-gateway连接失败，请及时排查解决。" 
								+ "</body>";
			}else{
				ttitle = "【"+type+"】致云ZhiCloud恢复通知";
				tcontent =
						"<body>"
								+ "<div id='content'>"
								+ "管理员"
								+ ":"
								+ "<br>"
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您好！<br>"
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;云端在线故障恢复。"
								+ "<font color=red size=4>"+region+"</font>地域的http-gateway已恢复连接。" 
								+ "</body>";
				
				
			}
			 
			
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
			InternetAddress from = new InternetAddress(MailConstant.ENCRYPTION_KEY
					+ MailConstant.ADDRESSOR);
			message.setFrom(from);// 设置发件人
			InternetAddress to = new InternetAddress(AppProperties.getValue("monitor_mail", ""));
//			InternetAddress to = new InternetAddress("zyafeng@zhicloud.com");
			message.setRecipient(Message.RecipientType.TO, to);// 设置收件人,并设置其接收类型为TO
			
			//乱码
			//message.setSubject(MimeUtility.encodeText(ttitle));
			message.setSubject(MimeUtility.encodeText(ttitle, "UTF-8", "B"));
			message.setText(tcontent);// 设置信件内容
			
			message.setSentDate(new java.util.Date());// 设置发信时间
			
			BodyPart mdp = new MimeBodyPart();
			mdp.setContent(tcontent, "text/html;charset=UTF-8");
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
		} catch (Exception e) {
			System.out.print(e.toString());
		}
	}
    /**
     * @param region
     * @param flag 1:异常      2：正常
     * @param status 具体异常内容：告警、故障或其它
     */
    public void sendHttpGatewayDetail(String info,String flag) {
    	String type = AppProperties.getValue("version_type", "");
   		try {
   			
   			String ttitle = " "; 
   			String tcontent = " "; 
   			if("1".equals(flag)){
   				ttitle = "【"+type+"】致云ZhiCloud故障通知";
   				tcontent =
   						"<body>"
   								+ "<div id='content'>"
   								+ "管理员"
   								+ ":"
   								+ "<br>"
   								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您好！<br>"
   								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;云端在线发现故障。<br>"
   								+ info
   								+ "<br>请及时排查解决。" 
   								+ "</body>";
   			}else{
   				ttitle = "【"+type+"】致云ZhiCloud恢复通知";
   				tcontent =
   						"<body>"
   								+ "<div id='content'>"
   								+ "管理员"
   								+ ":"
   								+ "<br>"
   								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您好！<br>"
   								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;云端在线故障恢复。<br>"
   								+ info 
   								+ "<br>已恢复正常</body>";
   				
   				
   			}
   			 
   			
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
   			InternetAddress from = new InternetAddress(MailConstant.ENCRYPTION_KEY
   					+ MailConstant.ADDRESSOR);
   			message.setFrom(from);// 设置发件人
   			InternetAddress to = new InternetAddress(AppProperties.getValue("monitor_mail", ""));
//   			InternetAddress to = new InternetAddress("zyafeng@zhicloud.com");
   			message.setRecipient(Message.RecipientType.TO, to);// 设置收件人,并设置其接收类型为TO
   			
   			//乱码
   			//message.setSubject(MimeUtility.encodeText(ttitle));
   			message.setSubject(MimeUtility.encodeText(ttitle, "UTF-8", "B"));
   			message.setText(tcontent);// 设置信件内容
   			
   			message.setSentDate(new java.util.Date());// 设置发信时间
   			
   			BodyPart mdp = new MimeBodyPart();
   			mdp.setContent(tcontent, "text/html;charset=UTF-8");
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
        try {
            
            String ttitle = " "; 
            String tcontent = " "; 
            if(flag){
                ttitle = "【"+type+"】致云ZhiCloud恢复通知";
                tcontent =
                        "<body>"
                                + "<div id='content'>"
                                + "管理员"
                                + ":"
                                + "<br>"
                                + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您好！<br>"
                                + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;云端在线故障恢复。<br>"
                                + "<br>数据库连接已恢复正常</body>";
            }else{
                ttitle = "【"+type+"】致云ZhiCloud故障通知";
                tcontent =
                        "<body>"
                                + "<div id='content'>"
                                + "管理员"
                                + ":"
                                + "<br>"
                                + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您好！<br>"
                                + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;云端在线发现告警，数据库无法连接或连接超时。<br>"
                                + "<br>请及时排查解决。" 
                                + "</body>";
            }
             
            
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
            InternetAddress from = new InternetAddress(MailConstant.ENCRYPTION_KEY
                    + MailConstant.ADDRESSOR);
            message.setFrom(from);// 设置发件人
            InternetAddress to = new InternetAddress(AppProperties.getValue("monitor_mail", ""));
            //InternetAddress to = new InternetAddress("zbenyuan@zhicloud.com");
            message.setRecipient(Message.RecipientType.TO, to);// 设置收件人,并设置其接收类型为TO
            
            //乱码
            //message.setSubject(MimeUtility.encodeText(ttitle));
            message.setSubject(MimeUtility.encodeText(ttitle, "UTF-8", "B"));
            message.setText(tcontent);// 设置信件内容
            
            message.setSentDate(new java.util.Date());// 设置发信时间
            
            BodyPart mdp = new MimeBodyPart();
            mdp.setContent(tcontent, "text/html;charset=UTF-8");
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
        } catch (Exception e) {
            System.out.print(e.toString());
        }
    }  
    
    /**
     * flag : 1:告警 2：恢复
     * @param region
     * @param flag
     */
    public void sendSuggestion(String userName,String userType,String content) {
 		try {
 			String type = AppProperties.getValue("version_type", "");
			String ttitle = " "; 
			String tcontent = " ";  
			ttitle = "【"+type+"】云端在线意见反馈";
			tcontent =
					"<body>" 
							+ "<br>" 
							+ "&nbsp;收到来自【"+userType+"】"+userName+"的意见反馈:<br>"
							+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"+content+"<br>"
							+ "&nbsp;请及时处理.<br>"
							+ "</body>";
			 
			
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
			InternetAddress from = new InternetAddress(MailConstant.ENCRYPTION_KEY
					+ MailConstant.ADDRESSOR);
			message.setFrom(from);// 设置发件人
			InternetAddress to = new InternetAddress(AppProperties.getValue("support_email", ""));
			message.setRecipient(Message.RecipientType.TO, to);// 设置收件人,并设置其接收类型为TO
			
			//乱码
			//message.setSubject(MimeUtility.encodeText(ttitle));
			message.setSubject(MimeUtility.encodeText(ttitle, "UTF-8", "B"));
			message.setText(tcontent);// 设置信件内容
			
			message.setSentDate(new java.util.Date());// 设置发信时间
			
			BodyPart mdp = new MimeBodyPart();
			mdp.setContent(tcontent, "text/html;charset=UTF-8");
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
		} catch (Exception e) {
			System.out.print(e.toString());
		}
	}
    
}

