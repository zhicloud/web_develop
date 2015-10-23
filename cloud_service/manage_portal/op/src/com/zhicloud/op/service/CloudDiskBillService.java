/**
 * Project Name:op
 * File Name:CloudDiskBillService.java
 * Package Name:com.zhicloud.op.service
 * Date:2015年4月23日上午11:03:11
 * 
 *
*/ 

package com.zhicloud.op.service; 

import java.util.Date;

import com.zhicloud.op.remote.MethodResult;

/**
 * ClassName: CloudDiskBillService 
 * Function: TODO ADD FUNCTION. 
 * Reason: TODO ADD REASON(可选).
 * date: 2015年4月23日 上午11:03:11 
 *
 * @author sasa
 * @version 
 * @since JDK 1.7
 */
public interface CloudDiskBillService {
	public MethodResult startOneVpcBilling(Date endTime);

}

