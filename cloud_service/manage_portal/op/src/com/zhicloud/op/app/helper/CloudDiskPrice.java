package com.zhicloud.op.app.helper;

import java.math.BigDecimal;

import com.zhicloud.op.core.CoreSpringContextManager;
import com.zhicloud.op.service.CloudDiskService;

/**
 * @author 
 *
 */
public class CloudDiskPrice {
	private static CloudDiskService cloudDiskService = CoreSpringContextManager.getCloudDiskService();
	public static BigDecimal getCloudDiskPrice(String region,String sizeForGB){
		BigDecimal price = cloudDiskService.getCloudDiskPrice(region, sizeForGB);
		return price;
	}
	//-----------------------

	/**
	 * 
	 */
	public static void main(String[] args)
	{
		System.out.println(CloudDiskPrice.getCloudDiskPrice("1", "20"));
	}
	
}
