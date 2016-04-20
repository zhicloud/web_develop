package com.zhicloud.op.message.sms;

 
import com.zhicloud.op.common.util.MD5Util;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.core.CoreSpringContextManager;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.service.MessageRecordService;
import com.zhicloud.op.service.SmsConfigService;
import com.zhicloud.op.service.SmsTemplateService;
import com.zhicloud.op.service.constant.AppConstant;
import com.zhicloud.op.vo.SmsConfigVO;
import com.zhicloud.op.vo.SmsTemplateVO;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.transaction.annotation.Transactional;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by sean on 7/8/15.
 */
@Transactional(readOnly = false)
public class SmsSendService {

    private static final Logger logger = Logger.getLogger(SmsSendService.class);

    private SmsContentGenerator smsContentGenerator;

    private SmsConfigService smsConfigService = CoreSpringContextManager.getSmsConfigService();

    private SmsTemplateService smsTemplateService =
        CoreSpringContextManager.getSmsTemplateService();

    private MessageRecordService
        messageRecordService = CoreSpringContextManager.getMessageRecordService();


    private Map<String, Object> parameter;

    public Map<String, Object> getParameter() {
        return parameter;
    }

    public void setParameter(Map<String, Object> parameter) {
        this.parameter = parameter;
    }

    public String generateSmsContent(String path, Map<String, Object> parameter) {

        smsContentGenerator = new SmsContentGenerator(path);

        LinkedHashMap<String, Object> properties = new LinkedHashMap<>();

        Iterator<Map.Entry<String, Object>> entries = parameter.entrySet().iterator();

        while (entries.hasNext()) {

            Map.Entry<String, Object> entry = entries.next();

            properties.put(entry.getKey(), entry.getValue());

        }

        return smsContentGenerator.generateContent(properties);

    }

    /**
     * @function 调用模板，发送指定手机
     * @param code
     * @param parameter
     * @param phone
     * @return
     */
    public String sendSms_old(String code, Map<String, Object> parameter, String phone) {

        if (parameter == null) {
            parameter = new LinkedHashMap<>();
        }

        SmsTemplateVO smsTemplateVO = smsTemplateService.getTemplateByCode(code);
        SmsConfigVO smsConfigVO = smsConfigService.getConfigById(smsTemplateVO.getConfigId());

        String serviceUrl = smsConfigVO.getServiceUrl();
        String configName = smsConfigVO.getConfigName();
        String smsId = smsConfigVO.getSmsId();
        String name = smsConfigVO.getName();
        String password = smsConfigVO.getPassword();
        String recipient = phone;
        String content = generateSmsContent(smsTemplateVO.getContent(), parameter);
        String timeStamp = AppConstant.SMS_TIME_STAMP;

        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(serviceUrl);
        client.getParams().setContentCharset("UTF-8");
        
        method.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=UTF-8");

        NameValuePair[] data = {//提交短信
            new NameValuePair("Id",       smsId),
            new NameValuePair("Name",     name),
            new NameValuePair("Psw",      password),
            new NameValuePair("Message",  content),
            new NameValuePair("Phone",    recipient),
            new NameValuePair("Timestamp",timeStamp),
        };

        method.setRequestBody(data);
        try {
            //处理返回值
            client.executeMethod(method);
            
            // 检测账户短信余额
            SmsInterfaceQuery.checkBalanceNum();
            
            String SendState =method.getResponseBodyAsString();
            //打印查看返回的数据
            Document doc = DocumentHelper.parseText(SendState);
            Element root = doc.getRootElement();
            String state = root.elementText("State");
            //String id    = root.elementText("Id");
            
            // 检测短信发送状态
            SmsInterfaceQuery.checkReturnState(state);
            
            //写入发送纪录
            Map<String, Object> record = new LinkedHashMap<>();
            record.put("id", StringUtil.generateUUID());
            record.put("sender_address", configName);
            record.put("recipient_address", recipient);
            record.put("content", content);
            record.put("type", AppConstant.MESSAGE_TYPE_SMS);
            System.out.println("状态:"+state);
            record.put("sms_state", state);
            record.put("create_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
            messageRecordService.addRecord(record);

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

        } catch(Exception e){
            throw new AppException("失败");
        }

    }

    /**
     * @function 直接调用模板发送短信
     * @param code
     * @param parameter
     * @return
     */
    public String sendSms_old(String code, Map<String, Object> parameter) {

        if (parameter == null) {
            parameter = new LinkedHashMap<>();
        }

        SmsTemplateVO smsTemplateVO = smsTemplateService.getTemplateByCode(code);
        SmsConfigVO smsConfigVO = smsConfigService.getConfigById(smsTemplateVO.getConfigId());

        String serviceUrl = smsConfigVO.getServiceUrl();
        String configName = smsConfigVO.getConfigName();
        String smsId = smsConfigVO.getSmsId();
        String name = smsConfigVO.getName();
        String password = smsConfigVO.getPassword();
        String recipient = smsTemplateVO.getRecipient();
        String content = generateSmsContent(smsTemplateVO.getContent(), parameter);
        String timeStamp = AppConstant.SMS_TIME_STAMP;

        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(serviceUrl);
        client.getParams().setContentCharset("UTF-8");

        method.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=UTF-8");

        NameValuePair[] data = {//提交短信
            new NameValuePair("Id",       smsId),
            new NameValuePair("Name",     name),
            new NameValuePair("Psw",      password),
            new NameValuePair("Message",  content),
            new NameValuePair("Phone",    recipient),
            new NameValuePair("Timestamp",timeStamp),
        };

        method.setRequestBody(data);

        try {
            //处理返回值
            client.executeMethod(method);
            
            // 检测账户短信余额
            SmsInterfaceQuery.checkBalanceNum();
            
            String SendState =method.getResponseBodyAsString();
            //打印查看返回的数据
            Document doc = DocumentHelper.parseText(SendState);
            Element root = doc.getRootElement();
            String state = root.elementText("State");
            //String id    = root.elementText("Id");

            // 检测短信发送状态
            SmsInterfaceQuery.checkReturnState(state);
            
            //写入发送纪录
            Map<String, Object> record = new LinkedHashMap<>();
            record.put("id", StringUtil.generateUUID());
            record.put("sender_address", configName);
            record.put("recipient_address", recipient);
            record.put("content", content);
            record.put("type", AppConstant.MESSAGE_TYPE_SMS);
            record.put("create_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
            messageRecordService.addRecord(record);

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

        } catch(Exception e){
            throw new AppException("失败");
        }

    }
    
    /**
     * 
    * @Title: sendSms 
    * @Description: 新运营商的短信发送 
    * @param @param code
    * @param @param parameter
    * @param @return      
    * @return String     
    * @throws
     */
    public String sendSms(String code, Map<String, Object> parameter) {

        if (parameter == null) {
            parameter = new LinkedHashMap<>();
        }

        SmsTemplateVO smsTemplateVO = smsTemplateService.getTemplateByCode(code);
        SmsConfigVO smsConfigVO = smsConfigService.getConfigById(smsTemplateVO.getConfigId());

        String serviceUrl = smsConfigVO.getServiceUrl();
        String configName = smsConfigVO.getConfigName();
        String smsId = smsConfigVO.getSmsId();
        String name = smsConfigVO.getName();
        String password = smsConfigVO.getPassword();
        String recipient = smsTemplateVO.getRecipient();
        String content = generateSmsContent(smsTemplateVO.getContent(), parameter);
        String timeStamp = StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS");

        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(serviceUrl);
        client.getParams().setContentCharset("UTF-8");

        method.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=UTF-8");

        NameValuePair[] data = {//提交短信
            new NameValuePair("batchno",       smsId),
            new NameValuePair("account",     name), 
            new NameValuePair("content",  content),
            new NameValuePair("mobiles",    recipient),
            new NameValuePair("timestamp",timeStamp),
            new NameValuePair("digest",MD5Util.md5(name+MD5Util.md5_16(password)+recipient+content+timeStamp)),
        };

        method.setRequestBody(data);

        try {
            //处理返回值
            String state = client.executeMethod(method)+"";           
            //写入发送纪录
            Map<String, Object> record = new LinkedHashMap<>();
            record.put("id", StringUtil.generateUUID());
            record.put("sender_address", configName);
            record.put("recipient_address", recipient);
            record.put("content", content);
            record.put("type", 1);
            record.put("create_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
            messageRecordService.addRecord(record);
 
            if(state.equals("200"))
            {
                return "1";
            }else {
                return state;
            }

        } catch(Exception e){
            e.printStackTrace();
            throw new AppException("失败");
        }

    }
    
    public String sendSms(String code, Map<String, Object> parameter, String phone) {

        if (parameter == null) {
            parameter = new LinkedHashMap<>();
        }

        SmsTemplateVO smsTemplateVO = smsTemplateService.getTemplateByCode(code);
        SmsConfigVO smsConfigVO = smsConfigService.getConfigById(smsTemplateVO.getConfigId());

        String serviceUrl = smsConfigVO.getServiceUrl();
        String configName = smsConfigVO.getConfigName();
        String smsId = smsConfigVO.getSmsId();
        String name = smsConfigVO.getName();
        String password = smsConfigVO.getPassword();
        String recipient = phone;
        String content = generateSmsContent(smsTemplateVO.getContent(), parameter);
        String timeStamp = StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS");

        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(serviceUrl);
        client.getParams().setContentCharset("UTF-8");

        method.setRequestHeader("ContentType","application/x-www-form-urlencoded;charset=UTF-8");
         NameValuePair[] data = {//提交短信
            new NameValuePair("batchno",       smsId),
            new NameValuePair("account",     name), 
            new NameValuePair("content",  content),
            new NameValuePair("mobiles",    recipient),
            new NameValuePair("timestamp",timeStamp),
            new NameValuePair("digest",MD5Util.md5(name+MD5Util.md5_16(password)+recipient+content+timeStamp)),
        };   

        method.setRequestBody(data);
        try {
            //处理返回值
            String state = client.executeMethod(method)+"";
            if("200".equals(state)){
                state = "1";
            }
            
            //写入发送纪录
            Map<String, Object> record = new LinkedHashMap<>();
            record.put("id", StringUtil.generateUUID());
            record.put("sender_address", configName);
            record.put("recipient_address", recipient);
            record.put("content", content);
            record.put("type", AppConstant.MESSAGE_TYPE_SMS);
            record.put("sms_state", state);
            record.put("create_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
            messageRecordService.addRecord(record);

             
            return state; 

        } catch(Exception e){
            throw new AppException("失败");
        }

    }  
    
     



}
