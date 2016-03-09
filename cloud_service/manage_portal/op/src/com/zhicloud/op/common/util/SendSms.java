package com.zhicloud.op.common.util;

import com.zhicloud.op.app.propeties.AppProperties;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.message.MessageConstant;
import com.zhicloud.op.message.MessageServiceManager;
import com.zhicloud.op.message.sms.SmsSendService;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

//短信验证使用，请勿随便修改
@Transactional(readOnly = false)
public class SendSms {
	
	public String zhicloudSendSms(String phone, String message) {

      SmsSendService smsSendService = MessageServiceManager.singleton().getSmsService();
      Map<String, Object> params = new HashMap<String, Object>();
      params.put("content", message);
      return  smsSendService.sendSms(MessageConstant.SMS_INFO_NOTIFICATION, params, phone);

	}
	public String sendHttpGateWayStatus(String region,String flag,String phone) {
      String type = AppProperties.getValue("version_type", "");
      String message = "";
      try{
          if("1".equals(flag)){
              message = "管理员您好，云端在线发现告警。" +region+" 地域的http-gateway连接失败，请及时排查解决。"+type+"";
              SmsSendService smsSendService = MessageServiceManager.singleton().getSmsService();
              Map<String, Object> params = new HashMap<String, Object>();
              params.put("content",  message);
              return  smsSendService.sendSms(MessageConstant.SMS_INFO_NOTIFICATION, params, phone);
          }else{
              message = "管理员您好，云端在线故障恢复。" +region+" 地域的http-gateway已恢复连接。"+type+"";
              SmsSendService smsSendService = MessageServiceManager.singleton().getSmsService();
              Map<String, Object> params = new HashMap<String, Object>();
              params.put("content", message);
              return  smsSendService.sendSms(MessageConstant.SMS_INFO_NOTIFICATION, params, phone);

          }
      } catch (Exception e){
          throw new AppException("失败");
      }
	}
	public String sendHttpGatewayDetail(String info,String flag,String phone) {
		String type = AppProperties.getValue("version_type", "");

		String message ="";
      try{

        if("1".equals(flag)){
            message = "管理员您好，云端在线发现故障。" +info+"请及时排查解决。"+type+""  ;
            SmsSendService smsSendService = MessageServiceManager.singleton().getSmsService();
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("content", message);
            return  smsSendService.sendSms(MessageConstant.SMS_INFO_NOTIFICATION, params, phone);
        }else {
            message = "管理员您好，云端在线故障恢复。" + info + "已恢复正常。" + type + "";
            SmsSendService smsSendService = MessageServiceManager.singleton().getSmsService();
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("content", message);
            return smsSendService.sendSms(MessageConstant.SMS_INFO_NOTIFICATION, params, phone);

        }
			
		} catch(Exception e){ 
			throw new AppException("失败");
		}
	}
}
