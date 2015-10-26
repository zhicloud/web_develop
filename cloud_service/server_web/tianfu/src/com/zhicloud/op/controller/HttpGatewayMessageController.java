package com.zhicloud.op.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.multiaction.MultiActionController;

import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.common.util.json.JSONLibUtil;
import com.zhicloud.op.httpGateway.HttpGatewayManager;
import com.zhicloud.op.httpGateway.HttpGatewayMessageHandler;
import com.zhicloud.op.httpGateway.HttpGatewayMessageHandlerImpl;
import com.zhicloud.op.httpGateway.HttpGatewayReceiveChannel;

@Controller
public class HttpGatewayMessageController extends MultiActionController
{
	
	private final static Logger logger = Logger.getLogger(HttpGatewayMessageController.class);
	
	private final static HttpGatewayMessageHandlerImpl httpGatewayMessageHandlerImpl = new HttpGatewayMessageHandlerImpl();
	
	private final static Map<String, Method> httpGatewayMessageHandlerMap = new HashMap<String, Method>();
	
	// 初始化http gateway message handler
	static
	{
		Method[] methods = HttpGatewayMessageHandlerImpl.class.getMethods();
		for( Method method : methods )
		{
			HttpGatewayMessageHandler annotation = method.getAnnotation(HttpGatewayMessageHandler.class);
			if( annotation==null )
			{
				continue;
			}
			if( StringUtil.isBlank(annotation.messageType()) )
			{
				continue;
			}
			if( httpGatewayMessageHandlerMap.containsKey(annotation.messageType()) )
			{
				logger.error("HttpGatewayMessageController.static_block() > there is another method annotated with message type ["+annotation.messageType()+"]");
				continue;
			}
			httpGatewayMessageHandlerMap.put(annotation.messageType(), method);
		}
	}

	/**
	 * 通用方法，不可随便改
	 */
	@RequestMapping("/hgMessage/receive.do")
	public void receive(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			String sessionId = request.getParameter("session_id");
			if( StringUtil.isBlank(sessionId) )
			{
				logger.error("HttpGatewayMessageController.receive() > ["+Thread.currentThread().getId()+"] received message, url=["+request.getRequestURI()+"], parameter 'sessionId' is blank.");
				return ;
			}
			HttpGatewayReceiveChannel receiveChannel = HttpGatewayManager.getReceiveChannel(sessionId);
			if( receiveChannel==null )
			{
				logger.error("HttpGatewayMessageController.receive() > ["+Thread.currentThread().getId()+"] received message, url=["+request.getRequestURI()+"], receive channel not found for session_id ["+sessionId+"]");
				return ;
			}
			
			JSONObject postData = receiveChannel.decryptReceivedData(request);
			String messageType = JSONLibUtil.getString(postData, "message_type");
			// 查找处理方法
			Method method = httpGatewayMessageHandlerMap.get(messageType);
			if( method==null )
			{
				logger.error("HttpGatewayMessageController.receive() > ["+Thread.currentThread().getId()+"] received message, url=["+request.getRequestURI()+"], invalid message type ["+messageType+"]");
				return ; 
			}
			// 进行处理
			logger.info("HttpGatewayMessageController.receive() > ["+Thread.currentThread().getId()+"] received message, url=["+request.getRequestURI()+"], message type ["+messageType+"]");
			method.invoke(httpGatewayMessageHandlerImpl, receiveChannel, JSONLibUtil.getJSONObject(postData, "message_data"));
		}
		catch (InvocationTargetException e)
		{
			logger.error("", e.getTargetException());
		}
		catch (Exception e)
		{
			logger.error("", e);
		}
	}
	
}
