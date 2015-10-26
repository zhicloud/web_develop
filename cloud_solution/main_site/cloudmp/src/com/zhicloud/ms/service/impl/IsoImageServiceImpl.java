package com.zhicloud.ms.service.impl;

import java.io.IOException;
import java.io.InputStream;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.zhicloud.ms.app.pool.isoImagePool.IsoImageProgressData;
import com.zhicloud.ms.app.pool.isoImagePool.IsoImageProgressPool;
import com.zhicloud.ms.app.pool.isoImagePool.IsoImageProgressPoolManager;
import com.zhicloud.ms.httpGateway.HttpGatewayAsyncChannel;
import com.zhicloud.ms.httpGateway.HttpGatewayManager;
import com.zhicloud.ms.httpGateway.HttpGatewayResponseHelper;
import com.zhicloud.ms.service.IsoImageService;
 
@Service("isoImageService")
public class IsoImageServiceImpl implements IsoImageService {
	
	private final static Logger logger = Logger.getLogger(IsoImageServiceImpl.class);

	@Override
	public String upload(String name,String realImageId,String url,String type,String description) {
		//file upload
		HttpGatewayAsyncChannel channel = null;
		
		try {
			channel = HttpGatewayManager.getAsyncChannel(1);
 			 
			JSONObject isoImageUploadResult = channel.isoImageUpload(name, url, description, "system", "system",2,"");
			logger.info("upload iso image to http gateway.  , iso_image_name[" + name + "], success[" + HttpGatewayResponseHelper.isSuccess(isoImageUploadResult) + "]");
			
			if (HttpGatewayResponseHelper.isSuccess(isoImageUploadResult) == true) {
				IsoImageProgressData isoImage = new IsoImageProgressData();
				isoImage.setName(name);
				isoImage.setUser("system");
				isoImage.setGroup("system");
				isoImage.setRealImageId(realImageId);
				isoImage.setSessionId(channel.getSessionId());
				IsoImageProgressPool pool = IsoImageProgressPoolManager.singleton().getPool();
				pool.put(isoImage);
				
				return isoImage.getSessionId();
			} else {
				channel.release();
			} 
		} catch (IOException e) {
			logger.error("error occurs when upload iso image.", e);
			channel.release();
		}
		
		return null;
	}
	
	public IsoImageProgressData getProgressData(String sessionId, String name) {
		IsoImageProgressData isoImageProgressData = IsoImageProgressPoolManager.singleton().getPool().getDuplication(sessionId, name);
		
		return isoImageProgressData;
	}

}
