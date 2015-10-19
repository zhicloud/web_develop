package com.zhicloud.ms.httpGateway;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.apache.log4j.Logger;

import com.zhicloud.ms.exception.AppException;
import com.zhicloud.ms.util.RegionHelper;
import com.zhicloud.ms.util.RegionHelper.RegionData;

public class HttpGatewayManager
{
	
	private static Logger logger = Logger.getLogger(HttpGatewayManager.class);
	
	private static Map<Integer, HttpGatewayChannelPool> channelPoolCache = new HashMap<Integer, HttpGatewayChannelPool>();
	private static HttpGatewayReceiveChannelPool receiveChannelPool = new HttpGatewayReceiveChannelPool();
	private final static Map<Integer, HttpGatewayAsyncChannelPool> asyncChannelPoolMap = new Hashtable<Integer, HttpGatewayAsyncChannelPool>();
	private final static HttpGatewayActiveAsyncChannelPool activeAsyncChannelPool;
	
	//----------------------
	
	// 初始化
	static
	{
		RegionData[] regionDatas = RegionHelper.singleton.getAllResions();
		synchronized (channelPoolCache)
		{
			for( RegionData regionData : regionDatas )
			{
				channelPoolCache.put(regionData.getId(), new HttpGatewayChannelPool(30, regionData.getId()));
			}
		}
		//异步channel
		for(RegionData regionData : regionDatas) {
			asyncChannelPoolMap.put(regionData.getId(), new HttpGatewayAsyncChannelPool(regionData.getId()));
		}
		
		//TODO:定时扫描线程，释放所有长时间没有使用的异步channel
		
		activeAsyncChannelPool = HttpGatewayActiveAsyncChannelPool.singleton();
	}
	
	/**
	 * 
	 */
	public static HttpGatewayChannelExt getChannel(Integer region)
	{
		synchronized (channelPoolCache)
		{
			HttpGatewayChannelPool channelPool = channelPoolCache.get(region);
			if( channelPool==null )
			{
				throw new AppException("wrong value of region, ["+region+"]");
			}
			return channelPool.getChannel();
		}
	}
	

	/**
	 * 
	 */
	public synchronized static void refreshAllChannels(Integer region)
	{
		synchronized (channelPoolCache)
		{
//			RegionData regionData = RegionHelper.singleton.getRegionData(region);
//			channelPoolCache.put(regionData.getId(), new HttpGatewayChannelPool(30, regionData.getId()));
		}
	}
	
	//-------------------------
	
	/**
	 * 
	 */
	public static void saveReceiveChannel(HttpGatewayReceiveChannel receiveChannel)
	{
		synchronized (receiveChannelPool)
		{
			receiveChannelPool.save(receiveChannel);
		}
	}
	
	/**
	 * 
	 */
	public static HttpGatewayReceiveChannel getReceiveChannel(String sessionId)
	{
		synchronized (receiveChannelPool)
		{
			return receiveChannelPool.get(sessionId);
		}
	}
	
	/**
	 * 从异步channel pool中获取异步channel
	 * 
	 * @param region
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public static HttpGatewayAsyncChannel getAsyncChannel(Integer region) throws MalformedURLException, IOException {
		HttpGatewayAsyncChannelPool channelPool = HttpGatewayManager.asyncChannelPoolMap.get(region);
		if(channelPool == null) {
			throw new AppException("wrong value of region, ["+region+"]");
		}
		
		return channelPool.getChannel();
	}
	
	/**
	 * 释放异步channel回对应的池中
	 * 
	 * @param channel
	 */
	public static void releseAsyncChannel(HttpGatewayAsyncChannel channel) {
		int region = channel.getRegion();
		HttpGatewayAsyncChannelPool channelPool = HttpGatewayManager.asyncChannelPoolMap.get(region);
		if(channelPool == null) {
			throw new AppException("wrong value of region, ["+region+"]");
		}
		
		channelPool.releaseChannel(channel);
	}
	
	public static HttpGatewayReceiveChannelPool getAll(){
		return receiveChannelPool;
	}
	
	public static HttpGatewayAsyncChannel getActiveAsyncChannel(String sessionId) {
		return activeAsyncChannelPool.getChannel(sessionId);
	} 
	
}





