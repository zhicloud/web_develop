package com.zhicloud.ms.service;

import java.io.InputStream;

import com.zhicloud.ms.app.pool.isoImagePool.IsoImageProgressData;

 
public interface IsoImageService {
	
	public String upload(String name,String realImageId,String url,String type,String description);
	public IsoImageProgressData getProgressData(String sessionId, String name);

}
