package com.zhicloud.ms.vo;

import java.lang.reflect.Method;



import com.zhicloud.ms.httpGateway.HttpGatewayAsyncChannel;

import net.sf.json.JSONObject;

public class AsyncMessageVO {
	public JSONObject getPostData() {
		return postData;
	}
	public void setPostData(JSONObject postData) {
		this.postData = postData;
	}
	public HttpGatewayAsyncChannel getChannel() {
		return channel;
	}
	public void setChannel(HttpGatewayAsyncChannel channel) {
		this.channel = channel;
	}
	public Method getMethod() {
		return method;
	}
	public void setMethod(Method method) {
		this.method = method;
	}
	
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	JSONObject postData;
	HttpGatewayAsyncChannel channel;
	Method method;
	String messageType;


}
