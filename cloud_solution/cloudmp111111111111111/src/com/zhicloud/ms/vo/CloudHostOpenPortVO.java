package com.zhicloud.ms.vo;

import com.zhicloud.ms.common.util.json.JSONBean;

public class CloudHostOpenPortVO implements JSONBean
{
	
	private String id;
	private String hostId;
	private String name;
	private int protocol;
	private int port;
	private int serverPort;
	private int outerPort;
	private String keyValue;
	private String protocolName;
	
	public String getId()
	{
		return id;
	}
	
	public void setId(String id)
	{
		this.id = id;
	}
	
	public String getHostId()
	{
		return hostId;
	}
	
	public void setHostId(String hostId)
	{
		this.hostId = hostId;
	}
	
	public int getPort()
	{
		return port;
	}
	
	public void setPort(int port)
	{
		this.port = port;
	}
	
	public int getOuterPort()
	{
		return outerPort;
	}
	
	public void setOuterPort(int outerPort)
	{
		this.outerPort = outerPort;
	}
	
	public int getProtocol()
	{
		return protocol;
	}
	
	public void setProtocol(int protocol)
	{
		this.protocol = protocol;
	}
	
	public int getServerPort()
	{
		return serverPort;
	}
	
	public void setServerPort(int serverPort)
	{
		this.serverPort = serverPort;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	public String getProtocolName() {
		if(0 == this.protocol){
			return "所有协议";
		}else if(1 ==  this.protocol){
			return "TCP";
		}else if(2 == this.protocol){
			return "UDP";			
		}else{
			return "";
		}
 	} 
	
	
	
}
