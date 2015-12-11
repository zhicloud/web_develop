package com.zhicloud.op.httpGateway;

import java.util.LinkedHashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.zhicloud.op.common.util.json.JSONLibUtil;
import com.zhicloud.op.controller.HttpGatewayMessageController;
import com.zhicloud.op.core.CoreSpringContextManager;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.service.constant.AppConstant;
import com.zhicloud.op.service.constant.AppInconstant;

public class HttpGatewayMessageHandlerImpl
{

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
			
			if( success==true )
			{
				// 创建云主机成功
 				JSONObject hostQueryInfoResult = receiveChannel.hostQueryInfo(realHostId);
				CoreSpringContextManager.getCloudHostService().handleNewlyCreatedCloudHost(receiveChannel.getRegion(), realHostId, hostName, hostQueryInfoResult);
				
			}
			else
			{
				//通过主机名称更新云主机状态为失败
				CoreSpringContextManager.getCloudHostService().handleCreatedFailedCloudHost(hostName);
			}
		}
		catch (Exception e)
		{
			throw AppException.wrapException(e);
		}
	}
	
	
}
