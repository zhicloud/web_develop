package com.zhicloud.ms.httpGateway;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.zhicloud.ms.exception.AppException;

public class HttpGatewayChannelPool
{
	
	private List<HttpGatewayChannelExt> channelPool = Collections.synchronizedList(new ArrayList<HttpGatewayChannelExt>());
	
	
	private int region = 0;
	
	private int poolSize = 0;
	private int loopIndex = 0;
	
	
	public HttpGatewayChannelPool(int poolSize, int region)
	{
		if( poolSize<=0 )
		{
			throw new AppException("poolSize must be greater than 0");
		}
		this.poolSize = poolSize;
		this.region = region;
	}
	
	
	public synchronized HttpGatewayChannelExt getChannel()
	{
		if( channelPool.size() < poolSize )
		{
			HttpGatewayChannelExt channel = new HttpGatewayChannelExt(this.region);
			channelPool.add(channel);
			return channel;
		}
		else
		{
			HttpGatewayChannelExt channel = channelPool.get(loopIndex);
			loopIndex = (loopIndex+1) % poolSize;
			return channel;
		}
	}
	
}
