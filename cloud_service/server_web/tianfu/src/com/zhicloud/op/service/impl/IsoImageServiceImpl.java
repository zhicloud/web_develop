package com.zhicloud.op.service.impl;

import java.io.IOException;
import java.io.InputStream;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.zhicloud.op.app.pool.isoImagePool.IsoImageProgressData;
import com.zhicloud.op.app.pool.isoImagePool.IsoImageProgressPool;
import com.zhicloud.op.app.pool.isoImagePool.IsoImageProgressPoolManager;
import com.zhicloud.op.httpGateway.HttpGatewayAsyncChannel;
import com.zhicloud.op.httpGateway.HttpGatewayManager;
import com.zhicloud.op.httpGateway.HttpGatewayResponseHelper;
import com.zhicloud.op.service.IsoImageService;

public class IsoImageServiceImpl implements IsoImageService {
	
	private final static Logger logger = Logger.getLogger(IsoImageServiceImpl.class);

	@Override
	public String upload(Integer region, String fileName, InputStream fileStream, String name, String description, String group, String user) {
		//file upload
		HttpGatewayAsyncChannel channel = null;
		
		try {
			channel = HttpGatewayManager.getAsyncChannel(region);
			JSONObject fileUploadResult = channel.fileUpload("file_path", fileName, fileStream);
			logger.info("upload file to http gateway. region[" + region + "], iso_image_name[" + name + "],  file_name[" + fileName + "], success[" + HttpGatewayResponseHelper.isSuccess(fileUploadResult) + "]");
			
			if (HttpGatewayResponseHelper.isSuccess(fileUploadResult) == true) {
				//iso image upload
				String target = fileUploadResult.getString("uuid");
				JSONObject isoImageUploadResult = channel.isoImageUpload(name, target, description, group, user);
				logger.info("upload iso image to http gateway. region[" + region + "], iso_image_name[" + name + "], success[" + HttpGatewayResponseHelper.isSuccess(isoImageUploadResult) + "]");
				
				if (HttpGatewayResponseHelper.isSuccess(isoImageUploadResult) == true) {
					IsoImageProgressData isoImage = new IsoImageProgressData();
					isoImage.setName(name);
					isoImage.setUser(user);
					isoImage.setGroup(group);
					isoImage.setSessionId(channel.getSessionId());
					IsoImageProgressPool pool = IsoImageProgressPoolManager.singleton().getPool();
					pool.put(isoImage);
					
					return isoImage.getSessionId();
				} else {
					channel.release();
				}
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
