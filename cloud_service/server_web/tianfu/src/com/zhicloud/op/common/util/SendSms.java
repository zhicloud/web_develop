package com.zhicloud.op.common.util;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.zhicloud.op.app.propeties.AppProperties;
import com.zhicloud.op.common.util.constant.SmsConstant;
import com.zhicloud.op.exception.AppException;

//短信验证使用，请勿随便修改
public class SendSms {
	
	private static String Url = "http://sms.gknet.com.cn:8180/Service.asmx/SendMessage";
	
	public String zhicloudSendSms(String phone, String message) {
		
		HttpClient client = new HttpClient(); 
		PostMethod method = new PostMethod(Url); 
		client.getParams().setContentCharset("UTF-8");
		
		method.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=UTF-8");
//		int mobile_code = (int)((Math.random()*9+1)*100000);
//		
//	    String message = new String("【云端在线】欢迎您，您的手机验证码是：" + mobile_code + "，请不要把验证码泄露给其他人。"); 
	    
		NameValuePair[] data = {//提交短信
			    new NameValuePair("Id",       SmsConstant.ID), 
			    new NameValuePair("Name",     SmsConstant.NAME), 
			    new NameValuePair("Psw",      SmsConstant.PASSWORD), 
			    new NameValuePair("Message",  message),
			    new NameValuePair("Phone",    phone),
			    new NameValuePair("Timestamp",SmsConstant.TIMESTAMP),
		};
		
		method.setRequestBody(data);	
		
		try {
			//处理返回值
			client.executeMethod(method);	
			String SendState =method.getResponseBodyAsString();
			//打印查看返回的数据
//			System.out.println(SendState);
			Document doc = DocumentHelper.parseText(SendState); 
			Element root = doc.getRootElement();
			String state = root.elementText("State");
//			String id    = root.elementText("Id");
//			System.out.println("保存发送信息的批号是："+id);
			
//			if(state.equals("1"))
//			{
//				System.out.println("短信发送成功");
//				return state;
//			}
//			if(state.equals("-1")) 
//			{
//				System.out.println("发送失败");
//				System.out.println("发送失败的号码是："+root.elementText("FailPhone"));
//				return state;
//			}
//			if (state.equals("-10")) 
//			{
//				System.out.println("号码错误");
//				return state;
//			}else {
//				return state;
//			}
//			
			return state;
			
		} catch(Exception e){ 
			throw new AppException("失败");
		}
//		return mobile_code;	
		
	}
	public String sendHttpGateWayStatus(String region,String flag,String phone) {
		String type = AppProperties.getValue("version_type", "");
		HttpClient client = new HttpClient(); 
		PostMethod method = new PostMethod(Url); 
		client.getParams().setContentCharset("UTF-8");
		
		method.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=UTF-8");
//		int mobile_code = (int)((Math.random()*9+1)*100000);
		String message  = "";
		if("1".equals(flag)){ 
			message =
					"【致云科技】管理员您好，云端在线发现告警。" +region+" 地域的http-gateway连接失败，请及时排查解决。【"+type+"】"  ;
		}else{ 
			message =
					"【致云科技】管理员您好，云端在线故障恢复。" +region+" 地域的http-gateway已恢复连接。【"+type+"】"  ;
			
			
		}  
		
		NameValuePair[] data = {//提交短信
				new NameValuePair("Id",       SmsConstant.ID), 
				new NameValuePair("Name",     SmsConstant.NAME), 
				new NameValuePair("Psw",      SmsConstant.PASSWORD), 
				new NameValuePair("Message",  message),
				new NameValuePair("Phone",    phone),
				new NameValuePair("Timestamp",SmsConstant.TIMESTAMP),
		};
		
		method.setRequestBody(data);	
		
		try {
			//处理返回值
			client.executeMethod(method);	
			String SendState =method.getResponseBodyAsString();
			//打印查看返回的数据
			System.out.println(SendState);
			Document doc = DocumentHelper.parseText(SendState); 
			Element root = doc.getRootElement();
			String state = root.elementText("State");
			String id    = root.elementText("Id");
			System.out.println("保存发送信息的批号是："+id);
			
			if(state.equals("1"))
			{
				return state;
			}
			if(state.equals("-1")) 
			{
				return state;
			}
			if (state.equals("-10")) 
			{
				return state;
			}else {
				return state;
			}
			
//			return state;
			
		} catch(Exception e){ 
			throw new AppException("失败");
		}
//		return mobile_code;	
		
	}
	public String sendHttpGatewayDetail(String info,String flag,String phone) {
		String type = AppProperties.getValue("version_type", "");
		HttpClient client = new HttpClient(); 
		PostMethod method = new PostMethod(Url); 
		client.getParams().setContentCharset("UTF-8");
		
		method.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=UTF-8");
//		int mobile_code = (int)((Math.random()*9+1)*100000);
		String message  = "";
		if("1".equals(flag)){ 
			message =
					"【致云科技】管理员您好，云端在线发现故障。" +info+"请及时排查解决。【"+type+"】"  ;
		}else{ 
			message =
					"【致云科技】管理员您好，云端在线故障恢复。" +info+"已恢复正常。【"+type+"】"  ;
			
			
		}  
		
		NameValuePair[] data = {//提交短信
				new NameValuePair("Id",       SmsConstant.ID), 
				new NameValuePair("Name",     SmsConstant.NAME), 
				new NameValuePair("Psw",      SmsConstant.PASSWORD), 
				new NameValuePair("Message",  message),
				new NameValuePair("Phone",    phone),
				new NameValuePair("Timestamp",SmsConstant.TIMESTAMP),
		};
		
		method.setRequestBody(data);	
		
		try {
			//处理返回值
			client.executeMethod(method);	
			String SendState =method.getResponseBodyAsString();
			//打印查看返回的数据
			System.out.println(SendState);
			Document doc = DocumentHelper.parseText(SendState); 
			Element root = doc.getRootElement();
			String state = root.elementText("State");
			String id    = root.elementText("Id");
			System.out.println("保存发送信息的批号是："+id);
			
			if(state.equals("1"))
			{
				return state;
			}
			if(state.equals("-1")) 
			{
				return state;
			}
			if (state.equals("-10")) 
			{
				return state;
			}else {
				return state;
			}
			
//			return state;
			
		} catch(Exception e){ 
			throw new AppException("失败");
		}
//		return mobile_code;	
		
	}
}