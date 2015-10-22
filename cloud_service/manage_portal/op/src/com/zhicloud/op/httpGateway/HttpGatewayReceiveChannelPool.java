package com.zhicloud.op.httpGateway;

import java.util.HashMap;
import java.util.Map;

public class HttpGatewayReceiveChannelPool
{
	
	/**
	 * key: session_id
	 * value: HttpGatewayReceiveChannel
	 */
	private Map<String, HttpGatewayReceiveChannel> map = new HashMap<String, HttpGatewayReceiveChannel>();
	
	/**
	 * 
	 */
	public void save(HttpGatewayReceiveChannel receiveChannel)
	{
		map.put(receiveChannel.getSessionId(), receiveChannel);
	}
	
	/**
	 * 
	 */
	public HttpGatewayReceiveChannel get(String sessionId)
	{
		return map.get(sessionId);
	}
	
	public void clearMap(){
		map.clear();
	}
	public Map<String, HttpGatewayReceiveChannel> getMap(){
		return map;
	}
}
