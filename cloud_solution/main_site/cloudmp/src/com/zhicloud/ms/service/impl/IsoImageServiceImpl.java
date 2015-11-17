package com.zhicloud.ms.service.impl;

import java.io.IOException;
import java.io.InputStream;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.zhicloud.ms.app.pool.IsoImagePool;
import com.zhicloud.ms.app.pool.IsoImagePoolManager;
import com.zhicloud.ms.app.pool.isoImagePool.IsoImageProgressData;
import com.zhicloud.ms.app.pool.isoImagePool.IsoImageProgressPool;
import com.zhicloud.ms.app.pool.isoImagePool.IsoImageProgressPoolManager;
import com.zhicloud.ms.httpGateway.HttpGatewayAsyncChannel;
import com.zhicloud.ms.httpGateway.HttpGatewayManager;
import com.zhicloud.ms.httpGateway.HttpGatewayResponseHelper;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.IsoImageService;
 
@Service("isoImageService")
public class IsoImageServiceImpl implements IsoImageService {
	
	private final static Logger logger = Logger.getLogger(IsoImageServiceImpl.class);

	@Override
	public MethodResult upload(String name,String realImageId,String url,String type,String description) {
		//file upload
//		HttpGatewayAsyncChannel channel = null;
//		
//		try {
//			channel = HttpGatewayManager.getAsyncChannel(1);
// 			 
//			JSONObject isoImageUploadResult = channel.isoImageUpload(name, url, description, "system", "system",2,realImageId);
//			logger.info("upload iso image to http gateway.  , iso_image_name[" + name + "], success[" + isoImageUploadResult + "]");
//			
//			if (HttpGatewayResponseHelper.isSuccess(isoImageUploadResult) == true) {
//				IsoImageProgressData isoImage = new IsoImageProgressData();
//				isoImage.setName(name);
//				isoImage.setUser("system");
//				isoImage.setGroup("system");
//				isoImage.setRealImageId(realImageId);
//				isoImage.setDescription(description);
//				isoImage.setSessionId(channel.getSessionId());
//				isoImage.setStatus(2);
//				IsoImageProgressPool pool = IsoImageProgressPoolManager.singleton().getPool();
//				pool.put(isoImage);
				
//				return new MethodResult(MethodResult.SUCCESS,"上传成功");
//			} else {
//	             channel.release();
//	             return new MethodResult(MethodResult.FAIL,"上传失败");
//			} 
//		} catch (IOException e) {
//			logger.error("error occurs when upload iso image.", e);
//			channel.release();
//		}
		
		return null;
	}
	
	public IsoImageProgressData getProgressData(String sessionId, String name) {
		IsoImageProgressData isoImageProgressData = IsoImageProgressPoolManager.singleton().getPool().getDuplication(sessionId);
		
		return isoImageProgressData;
	}

    @Override
    public MethodResult delete(String imageId) {
        HttpGatewayAsyncChannel channel = null;
        
        try {
            channel = HttpGatewayManager.getAsyncChannel(1);
            JSONObject isoImageDeleteResult = channel.isoImageDelete(imageId);
            if (HttpGatewayResponseHelper.isSuccess(isoImageDeleteResult) == true) { 
                IsoImagePool pool = IsoImagePoolManager.getSingleton().getIsoImagePool();
                pool.remove(imageId);
                return new MethodResult(MethodResult.SUCCESS,"删除成功");
            } else {
                return new MethodResult(MethodResult.FAIL,"删除失败");
             } 
        }catch(Exception e){
            e.printStackTrace();
        }
        channel.release();
        return new MethodResult(MethodResult.FAIL,"删除失败");

    }

}
