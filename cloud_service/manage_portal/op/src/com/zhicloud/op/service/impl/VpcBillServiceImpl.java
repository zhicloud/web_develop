/**
 * Project Name:op
 * File Name:VpcBillServiceImpl.java
 * Package Name:com.zhicloud.op.service.impl
 * Date:2015年4月8日下午1:55:33
 * 
 *
*/ 

package com.zhicloud.op.service.impl;

import java.util.Date;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.op.app.helper.CloudHostBillingHelper;
import com.zhicloud.op.app.helper.VpcBillHelper;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.service.VpcBillService;
 
/**
 * ClassName: VpcBillServiceImpl 
 * Function: Vpc账单操作
 * date: 2015年4月8日 下午1:55:33 
 *
 * @author sasa
 * @version 
 * @since JDK 1.7
 */
@Transactional(readOnly = true)
public class VpcBillServiceImpl extends BeanDirectCallableDefaultImpl implements VpcBillService {
	
	
	public static final Logger logger = Logger.getLogger(CloudHostBillDetailServiceImpl.class);

	private SqlSession sqlSession;
	
	public void setSqlSession(SqlSession sqlSession)
	{
		this.sqlSession = sqlSession;
	}

	/**
	 * 完成一个账单计费
	 * @see com.zhicloud.op.service.VpcBillService#startOneCloudHostBilling(java.util.Date)
	 */
	@Transactional(readOnly=false)
	public MethodResult startOneVpcBilling(Date billingEndTime)
	{
		try
		{
			return new VpcBillHelper(sqlSession).startOneVpcBilling(billingEndTime, true);
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

