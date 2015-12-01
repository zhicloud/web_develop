package com.zhicloud.ms.httpGateway;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketException;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import com.zhicloud.ms.common.util.StringUtil;
import com.zhicloud.ms.common.util.ThreadUtil;
import com.zhicloud.ms.exception.AppException;

public class HttpGatewayReceiveChannel extends HttpGatewayChannelExt
{
	
	
	private String registerUrl = null;
	
	private  boolean flag = true ; 
	
	
	public HttpGatewayReceiveChannel(int region) throws MalformedURLException, IOException
	{
		super(region);
	}
	
	
	public String getSessionId()
	{
		return getHelper().getSessionId();
	}
	
	public synchronized void setFlag(boolean isAlive){ 
		flag = isAlive;
	} 
	public synchronized boolean getFlag(){ 
		return flag;
	} 
	
	
	
	
	
	public boolean isFlag() {
		return flag;
	}


	public void messagePushRegisterThreadly(String url)
	{
		if( StringUtil.isBlank(url) )
		{
			throw new AppException("url cannot be empty.");
		}
		this.registerUrl = url; 
		new Thread(new MessagePushRegisterAction()).start();
	}
	
	
	
	public JSONObject decryptReceivedData(HttpServletRequest request) throws IOException
	{
		return getHelper().decryptReceivedData(request);
	}
	
	
	
	/**
	 * 向http gateway注册推送接口，然后不断调用keepConnection接口保持与http gateway的连接。
	 * keepConnection接口正常情况下不会立即返回，
	 */
	public class MessagePushRegisterAction implements Runnable
	{
		public void run()
		{
 			loop1: while( getFlag() )
			{
				int sleepTime = 1000;
				try
				{
					JSONObject result = null;
					try
					{
						// init http gateway helper
						if( getHelper()==null )
						{
							initHelper();
							// 初始化完成之后立即保存到HttpGatewayManager的receiveChannelPool中去
							HttpGatewayManager.saveReceiveChannel(HttpGatewayReceiveChannel.this);
						}
						
						// message push register
						result = getHelper().messagePushRegister(registerUrl);
						if( HttpGatewayResponseHelper.isSuccess(result)==false )
						{
							logger.error("HttpGatewayReceiveChannel.messagePushRegister() > ["+Thread.currentThread().getId()+"] register message push url ["+registerUrl+"] in http gateway failed, region:["+getRegion()+"], result: "+result);
							refreshHelper();
							continue;
						}
						logger.info("HttpGatewayReceiveChannel.messagePushRegister() > ["+Thread.currentThread().getId()+"] register message push url ["+registerUrl+"] in http gateway successed, region:["+getRegion()+"], result: "+result);
						
						// keep connection
						while(getFlag())
						{
							// 一般情况下keepConnection会保持10秒的连接，如果中途断开的话一般是因为http gateway出现了什么情况，代码转到catch代码块
							// 这种情况下需要重新刷新httpGatewayChannel里面的连接
							result = getHelper().keepConnectionAlive(0);
							Thread.sleep(10000);
							if( HttpGatewayResponseHelper.isSuccess(result)==false )
							{
								// result为false的时候，为http gateway主动通知客户端需要重连
								logger.error("HttpGatewayReceiveChannel.messagePushRegister() > ["+Thread.currentThread().getId()+"] keep connection with http gateway failed, result: "+result);
								logger.warn("HttpGatewayReceiveChannel.messagePushRegister() > ["+Thread.currentThread().getId()+"] refresh all http gateway channel, region:["+getRegion()+"]");
								HttpGatewayManager.refreshAllChannels(getRegion());
								// 刷新完所有的channel之后，本次循环从头开始，建立新的连接，然后重新注册
								sleepTime = 3000;
								continue loop1;
							}
							logger.info("HttpGatewayReceiveChannel.messagePushRegister() > ["+Thread.currentThread().getId()+"] keep connection with http gateway succeeded, result: "+result);
						}
					}
					catch (SocketException e)
					{
						logger.error("HttpGatewayReceiveChannel.messagePushRegister() > ["+Thread.currentThread().getId()+"] connect to http gateway failed, exception:["+e.getMessage()+"], region:["+getRegion()+"]");
						logger.warn("HttpGatewayReceiveChannel.messagePushRegister() > ["+Thread.currentThread().getId()+"] refresh all http gateway channel, region:["+getRegion()+"]");
						refreshHelper();
						HttpGatewayManager.refreshAllChannels(getRegion());
						sleepTime = 3000;
					}
					catch (Exception e)
					{
						logger.error("["+Thread.currentThread().getId()+"]", e);
						refreshHelper();
					}
				}
				finally
				{
					if(flag){						
						ThreadUtil.sleep(sleepTime);
					}
				}
				
			}  
		};
	}
}
