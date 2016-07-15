package com.zhicloud.ms.httpGateway;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger; 
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zhicloud.ms.common.util.json.JSONLibUtil;
import com.zhicloud.ms.constant.AppConstant;
import com.zhicloud.ms.constant.AppInconstant;
import com.zhicloud.ms.controller.HttpGatewayMessageController;
import com.zhicloud.ms.exception.AppException;
import com.zhicloud.ms.service.ICloudHostService;

public class HttpGatewayMessageHandlerImpl
{
    
    BeanFactory factory = new ClassPathXmlApplicationContext("classpath:/applicationContext*.xml"); 
    ICloudHostService cloudHostService = (ICloudHostService)factory.getBean("cloudHostService");

	private final static Logger logger = Logger.getLogger(HttpGatewayMessageController.class);
	
	
	@HttpGatewayMessageHandler(messageType="create_host_progress")
	public void createHostProgress(HttpGatewayReceiveChannel receiveChannel, JSONObject messageData)
	{
		String hostName = JSONLibUtil.getString(messageData, "host_name");
		Double progress = JSONLibUtil.getDouble(messageData, "progress");
		AppInconstant.cloudHostProgress.put(hostName, progress);
		logger.info("HttpGatewayMessageHandlerImpl.createHostProgress() > receive host creation progress message, host ["+hostName+"], progress ["+progress+"]");
	}
	
	
	@HttpGatewayMessageHandler(messageType="create_host_result")
	public void createHostResult(HttpGatewayReceiveChannel receiveChannel, JSONObject messageData)
	{
		try
		{
			Boolean success   = JSONLibUtil.getBoolean(messageData, "success");
			String realHostId = JSONLibUtil.getString(messageData, "uuid");
			String hostName   = JSONLibUtil.getString(messageData, "host_name");
			
			logger.info("HttpGatewayMessageHandlerImpl.createHostResult() > receive create host result message, host_name:["+hostName+"], host_uuid:["+realHostId+"], success:["+success+"]");
			AppInconstant.cloudHostProgress.remove(hostName);
			if( success==true )
			{
				// 创建云主机成功，修改状态
				
				//JSONObject hostQueryInfoResult = receiveChannel.hostQueryInfo(realHostId);
//				CoreSpringContextManager.getCloudHostService().handleNewlyCreatedCloudHost(receiveChannel.getRegion(), realHostId, hostName, hostQueryInfoResult);
				cloudHostService.updateStautsByName(hostName);
			}
			else
			{
				//通过主机名称更新云主机状态为失败
			    cloudHostService.updateHostStatusToFailByName(hostName);
			}
		}
		catch (Exception e)
		{
			throw AppException.wrapException(e);
		}
	}
	
	
}
