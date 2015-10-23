/**
 * Project Name:op
 * File Name:CloudDiskBillServiceImpl.java
 * Package Name:com.zhicloud.op.service.impl
 * Date:2015年4月23日上午11:07:05
 * 
 *
*/ 

package com.zhicloud.op.service.impl;

import java.util.Date;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.op.app.helper.CloudDiskBillHelper;
import com.zhicloud.op.app.helper.CloudHostBillingHelper;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.service.CloudDiskBillService;

 
/**
 * ClassName: CloudDiskBillServiceImpl 
 * Function:  账单
 * date: 2015年4月23日 上午11:07:05 
 *
 * @author sasa
 * @version 
 * @since JDK 1.7
 */
@Transactional(readOnly = true)
public class CloudDiskBillServiceImpl extends BeanDirectCallableDefaultImpl implements CloudDiskBillService {

	public static final Logger logger = Logger.getLogger(CloudHostBillDetailServiceImpl.class);

	private SqlSession sqlSession;
	
	public void setSqlSession(SqlSession sqlSession)
	{
		this.sqlSession = sqlSession;
	}

	// ---------------
	 

	/**
	 * TODO 简单描述该方法的实现功能（可选）.
	 * @see com.zhicloud.op.service.CloudDiskBillService#startOneVpcBilling(java.util.Date)
	 */
	@Transactional(readOnly=false)
	public MethodResult startOneVpcBilling(Date endTime) {
		
		try
		{
			return new CloudDiskBillHelper(sqlSession).startOneCloudDiskBilling(endTime, true);
		}
		catch (AppException e)
		{ 
			throw e;
		}
		catch (Exception e)
		{
 			throw new AppException(e);
		}
	}

}

