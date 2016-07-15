package com.zhicloud.ms.service;



import com.zhicloud.ms.app.pool.isoImagePool.IsoImageProgressData;
import com.zhicloud.ms.remote.MethodResult;

 
public interface IsoImageService {
	
	public MethodResult upload(String name,String realImageId,String url,String type,String description);
	public IsoImageProgressData getProgressData(String sessionId, String name);
	/**
	 * 
	* @Title: delete 
	* @Description: 根据镜像id删除镜像 
	* @param @param imageId
	* @param @return      
	* @return MethodResult     
	* @throws
	 */
	public MethodResult delete(String imageId);

}
