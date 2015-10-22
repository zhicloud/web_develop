package com.zhicloud.op.vo;

import com.zhicloud.op.common.util.json.JSONBean;

public class CloudHostOpenPortVO implements JSONBean
{
	
	private String id;
	private String hostId;
	private String name;
	private int protocol;
	private int port;
	private int serverPort;
	private int outerPort;
	
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
	
}
