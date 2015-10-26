package com.zhicloud.op.app.pool;

import java.math.BigInteger;
import java.net.SocketException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.zhicloud.op.app.helper.RegionHelper;
import com.zhicloud.op.app.helper.RegionHelper.RegionData;
import com.zhicloud.op.app.pool.IsoImagePool.IsoImageData;
import com.zhicloud.op.common.util.json.JSONLibUtil;
import com.zhicloud.op.httpGateway.HttpGatewayChannelExt;
import com.zhicloud.op.httpGateway.HttpGatewayManager;
import com.zhicloud.op.httpGateway.HttpGatewayResponseHelper;

public class IsoImagePoolManager
{
	
	private static final Logger logger = Logger.getLogger(IsoImagePoolManager.class);
	
	//-------------
	
	private static IsoImagePoolManager singleton = new IsoImagePoolManager();
	
	public static IsoImagePoolManager getSingleton()
	{
		return singleton;
	}
	
	public static IsoImagePool getIsoImagePool()
	{
		return singleton.isoImagePool;
	}
	
	//-------------
	
	
	private IsoImagePool isoImagePool = new IsoImagePool();
	
	
	public void refreshIsoImagePool()
	{
		logger.info("IsoImagePoolManager.refreshIsoImagePool() > ["+Thread.currentThread().getId()+"] 获取光盘镜像信息");
		
		RegionData[] regionDatas = RegionHelper.singleton.getAllResions();
		for( RegionData regionData : regionDatas)
		{
			try
			{
				HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(regionData.getId());
				JSONObject isoImageQueryResult = channel.isoImageQuery();
				
				if( HttpGatewayResponseHelper.isSuccess(isoImageQueryResult)==false )
				{
					logger.warn("IsoImagePoolManager.refreshIsoImagePool() > ["+Thread.currentThread().getId()+"] query iso images failed, message:["+HttpGatewayResponseHelper.getMessage(isoImageQueryResult)+"], region:["+String.format("%s:%s", regionData.getId(), regionData.getName()) +"]");
					return ;
				}
				
				JSONArray isoImages = (JSONArray)isoImageQueryResult.get("iso_images");
				for( int i=0; i<isoImages.size(); i++ )
				{
					JSONObject isoImage = (JSONObject)isoImages.get(i);

					String uuid        = JSONLibUtil.getString(isoImage,     "uuid");                           
					String name        = JSONLibUtil.getString(isoImage,     "name");                            
					String description = JSONLibUtil.getString(isoImage,     "description");
					BigInteger size    = JSONLibUtil.getBigInteger(isoImage, "size");
					Integer status     = JSONLibUtil.getInteger(isoImage,    "status");
					
					IsoImageData isoImageData = new IsoImageData();
					isoImageData.setRealImageId(uuid);
					isoImageData.setName(name);
					isoImageData.setDescription(description);
					isoImageData.setSize(size);
					isoImageData.setStatus(status);
					isoImageData.setRegion(regionData.getId());
					
					isoImagePool.put(isoImageData);
				}
			}
			catch( SocketException e )
			{
				logger.error("IsoImageInitializer.run() > ["+Thread.currentThread().getId()+"] connect to http gateway failed, exception:["+e.getMessage()+"], region:["+String.format("%s:%s", regionData.getId(), regionData.getName()) +"]");
			}
			catch (Exception e)
			{
				logger.error("IsoImageInitializer.run() > ["+Thread.currentThread().getId()+"] get iso images failed, exception:["+e.getMessage()+"], region:["+String.format("%s:%s", regionData.getId(), regionData.getName()) +"]");
			}
		}
		
	}
}










