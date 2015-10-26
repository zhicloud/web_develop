package com.zhicloud.op.app.helper;

import java.math.BigDecimal;
import com.zhicloud.op.core.CoreSpringContextManager;
import com.zhicloud.op.service.PackageOptionService;

/**
 * @author ZYFTMX
 *
 */
public class VPCAmountAndPrice
{
	private static PackageOptionService packageOptionService = CoreSpringContextManager.getPackageOptionService();
	public static BigDecimal getVpcPrice(String region,String amount){
		BigDecimal price = packageOptionService.getVpcPrice(region, Integer.parseInt(amount));
		return price;
	}
	//-----------------------

	/**
	 * 
	 */
	public static void main(String[] args)
	{
		System.out.println(VPCAmountAndPrice.getVpcPrice("1", "2"));
	}
	
}
