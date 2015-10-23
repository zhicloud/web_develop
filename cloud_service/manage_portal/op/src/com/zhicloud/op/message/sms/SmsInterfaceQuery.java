
package com.zhicloud.op.message.sms;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import com.zhicloud.op.message.MessageServiceManager;
import com.zhicloud.op.message.email.EmailSendService;

/**
 * @ClassName: SmsInterfaceQuery
 * @Description: 短信的相关接口信息查询
 * @author 张本缘 于 2015年8月6日 下午2:04:25
 */
public class SmsInterfaceQuery {
    /* 日志 */
    private static final Logger logger = Logger.getLogger(SmsInterfaceQuery.class);
    /* http get请求路径 */
    private final static String BALANCE_URL = "http://sms.gknet.com.cn:8180/service.asmx/GetBalance";
    /* 机构代码 */
    private final static String BALANCE_ID = "1019";
    /* 账户名 */
    private final static String BALANCE_NAME = "ld2";
    /* 密码 */
    private final static String BALANCE_PSW = "zhicloud028";
    /* 调用邮件模板的代码 */
    private final static String SMS_CODE = "SMS_CODE";
    /* 可用短信临界值 */
    private final static Integer BALANCE_NUM = 100;
    /* 上次发送短信时间(账户正常) */
    private static long A_LASTSENDTIME = 0;
    /* 短信发送间隔时间(账户正常) */
    private static final long A_INTERVAL_TIME = 1000 * 60 * 10;
    /* 上次发送短信时间(账户不正常) */
    private static long B_LASTSENDTIME = 0;
    /* 短信发送间隔时间(账户不正常) */
    private static final long B_INTERVAL_TIME = 1000 * 60 * 10;
    /* 上次发送短信时间(短信发送不正常) */
    private static long C_LASTSENDTIME = 0;
    /* 短信发送间隔时间(短信发送不正常) */
    private static final long C_INTERVAL_TIME = 1000 * 60 * 10;
    /**
     * @Description:获取短信账号余额相关信息
     * @return Map
     */
    public static Map<String, Object> getBalance() {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        HttpClient client = new HttpClient();
        PostMethod method = new PostMethod(BALANCE_URL);
        client.getParams().setContentCharset("UTF-8");

        method.setRequestHeader("ContentType", "application/x-www-form-urlencoded;charset=UTF-8");

        NameValuePair[] data = {// 提交短信
        new NameValuePair("Id", BALANCE_ID), new NameValuePair("Name", BALANCE_NAME),
                new NameValuePair("Psw", BALANCE_PSW)};
        method.setRequestBody(data);
        try {
            // 处理返回值
            client.executeMethod(method);
            String SendState = method.getResponseBodyAsString();
            // 打印查看返回的数据
            Document doc = DocumentHelper.parseText(SendState);
            Element root = doc.getRootElement();
            map.put("balance", root.elementText("Balance"));
            map.put("Sended", root.elementText("Sended"));
            map.put("State", root.elementText("State"));
            map.put("Totaled", root.elementText("Totaled"));
        } catch (Exception e) {
            map.put("balance", 0);
            map.put("Sended", 0);
            map.put("State", 0);
            map.put("Totaled", 0);     
            e.printStackTrace();
            logger.error("调用短信接口getBalance出错:" + e.getMessage());
        }
        return map;
    }

    /**
     * @Description:检测短信账户是否正常和可用短信数量是否达到临界值
     * @throws
     */
    public static void checkBalanceNum() {
        Map<String, Object> map = getBalance();
        Integer state = Integer.parseInt(map.get("State").toString());
        EmailSendService mailSendService = MessageServiceManager.singleton().getMailService();
        Map<String, Object> parameter = new LinkedHashMap<>();
        long currenttime = System.currentTimeMillis();
        // 接口调用成功
        if (state > 0) {
            Integer balance = Integer.parseInt(map.get("balance").toString());
            // 检测短信可用数量是否达到临界值
            if (balance <= BALANCE_NUM && (currenttime - A_LASTSENDTIME) >= A_INTERVAL_TIME) {
                parameter.put("content", "账户可用短信数量为: " + balance + " 条,已不足 " + BALANCE_NUM + " 条");
                mailSendService.sendMail(SMS_CODE, parameter);
                // 更新发送时间
                A_LASTSENDTIME = currenttime;
            }
        } else {// 接口调用失败
            if ((currenttime - B_LASTSENDTIME) >= B_INTERVAL_TIME) {
                parameter.put("content", "账户短信通道出现问题,不能成功发送短信,错误代码:(state=" + map.get("State") + ")");
                mailSendService.sendMail(SMS_CODE, parameter);
                // 更新发送时间
                B_LASTSENDTIME = currenttime;
            }
        }
    }
    
    /**
     * @Description:根据发送短信返回的状态判断短信通道时候可用
     * @param state
     */
    public static void checkReturnState(String state) {
        if (!"1".equals(state)) {
            EmailSendService mailSendService = MessageServiceManager.singleton().getMailService();
            Map<String, Object> parameter = new LinkedHashMap<>();
            long currenttime = System.currentTimeMillis();
            if ((currenttime - C_LASTSENDTIME) >= C_INTERVAL_TIME) {
                parameter.put("content", "短信发送失败,错误代码:(state=" + state + ")");
                mailSendService.sendMail(SMS_CODE, parameter);
                // 更新发送时间
                C_LASTSENDTIME = currenttime;
            }
        }
    }
}
