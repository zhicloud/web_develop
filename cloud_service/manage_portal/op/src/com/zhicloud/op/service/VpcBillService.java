/**
 * Project Name:op
 * File Name:VpcBillService.java
 * Package Name:com.zhicloud.op.service
 * Date:2015年4月8日下午1:54:16
 * 
 *
*/ 

package com.zhicloud.op.service; 

import java.util.Date;

import com.zhicloud.op.remote.MethodResult;

/**
 * ClassName: VpcBillService 
 * Function:  VPC计费
 * date: 2015年4月8日 下午1:54:16 
 *
 * @author sasa
 * @version 
 * @since JDK 1.7
 */
public interface VpcBillService {
	/**
	 * 
	 * startOneCloudHostBilling:完成一个vpc的计费
	 *
	 * @author sasa
	 * @param endTime
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	public MethodResult startOneVpcBilling(Date endTime);

}

