package com.zhicloud.ms.util;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger; 

import com.zhicloud.ms.util.json.JSONBean;
import com.zhicloud.ms.util.json.JSONLibUtil;

public class RegionHelper
{
	
	private static final Logger logger = Logger.getLogger(RegionHelper.class);
	
	public static RegionHelper singleton = new RegionHelper();
	
	private RegionHelper()
	{
		logger.info("RegionHelper.RegionHelper() > ["+Thread.currentThread().getId()+"] initialized");
	}
	
	//------------------
	
	public Map<Integer, RegionData> cache = new LinkedHashMap<Integer, RegionData>();
	
	public synchronized void putRegionData(RegionData regionData)
	{
		cache.put(regionData.getId(), regionData);
	}
	
	public synchronized RegionData getRegionData(Integer id)
	{
		return cache.get(id);
	}
	
	public synchronized RegionData[] getAllResions()
	{
		return cache.values().toArray(new RegionData[0]);
	}
	
	public synchronized void print()
	{
		logger.info("RegionHelper.print() > ["+Thread.currentThread().getId()+"] " + JSONLibUtil.toJSONString(cache));
	}
	
	public static class RegionData implements JSONBean
	{
		private Integer id;
		private String name;
		private String httpGatewayAddr;
		
		public synchronized Integer getId()
		{
			return id;
		}
		public synchronized void setId(Integer id)
		{
			this.id = id;
		}
		public synchronized String getName()
		{
			return name;
		}
		public synchronized void setName(String name)
		{
			this.name = name;
		}
		public synchronized String getHttpGatewayAddr()
		{
			return httpGatewayAddr;
		}
		public synchronized void setHttpGatewayAddr(String httpGatewayAddr)
		{
			this.httpGatewayAddr = httpGatewayAddr;
		}
	}
	
}
