package com.zhicloud.op.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.op.app.authorization.PrivilegeConstant;
import com.zhicloud.op.app.helper.CloudHostBillingHelper;
import com.zhicloud.op.app.helper.LoginHelper;
import com.zhicloud.op.app.helper.ServiceHelper;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.login.LoginInfo;
import com.zhicloud.op.mybatis.mapper.CloudHostBillDetailMapper;
import com.zhicloud.op.mybatis.mapper.InvoiceMapper;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.service.CloudHostBillDetailService;
import com.zhicloud.op.service.constant.AppConstant;
import com.zhicloud.op.vo.BusinessStatisticsVO;
import com.zhicloud.op.vo.CloudHostBillDetailVO;
import com.zhicloud.op.vo.InvoiceVO;

@Transactional(readOnly = true)
public class CloudHostBillDetailServiceImpl extends BeanDirectCallableDefaultImpl implements CloudHostBillDetailService
{

	public static final Logger logger = Logger.getLogger(CloudHostBillDetailServiceImpl.class);

	private SqlSession sqlSession;
	
	public void setSqlSession(SqlSession sqlSession)
	{
		this.sqlSession = sqlSession;
	}

	// ---------------
	
	/**
	 * 从云主机账单明细表取出一条未结算完成的记录，进行结算
	 */
	@Transactional(readOnly=false)
	public MethodResult startOneCloudHostBilling(Date billingEndTime)
	{
		try
		{
			return new CloudHostBillingHelper(sqlSession).startOneCloudHostBilling(billingEndTime, true);
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

	@Override
	public MethodResult dealNoBillCloudHost() {
		// TODO Auto-generated method stub
		return new CloudHostBillingHelper(sqlSession).startMoreNewCloudHostBillDetail();
		
	}
	
	
}

















