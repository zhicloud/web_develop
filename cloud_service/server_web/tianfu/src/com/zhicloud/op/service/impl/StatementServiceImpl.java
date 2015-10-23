package com.zhicloud.op.service.impl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.op.app.authorization.PrivilegeConstant;
import com.zhicloud.op.app.helper.LoginHelper;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.login.LoginInfo;
import com.zhicloud.op.mybatis.mapper.CloudHostBillDetailMapper;
import com.zhicloud.op.mybatis.mapper.CloudHostMapper;
import com.zhicloud.op.mybatis.mapper.TerminalUserMapper;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.request.RequestContext;
import com.zhicloud.op.service.StatementService;
import com.zhicloud.op.service.constant.AppConstant;

@Transactional(readOnly = true)
public class StatementServiceImpl extends BeanDirectCallableDefaultImpl implements StatementService
{
	public static final Logger logger = Logger.getLogger(StatementServiceImpl.class);

	private SqlSession sqlSession;

	public void setSqlSession(SqlSession sqlSession)
	{
		this.sqlSession = sqlSession;
	}

	@Callable
	public String managePage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("StatementServiceImpl.managePage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		
		if( loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT && loginInfo.hasPrivilege(PrivilegeConstant.statement_manage_page_agent) == false )
		{
			return "/public/have_not_access.jsp";
		}else if( loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_OPERATOR && loginInfo.hasPrivilege(PrivilegeConstant.statement_manage_page_operator) == false )
		{
			return "/public/have_not_access.jsp";
		}
		
		String userId = loginInfo.getUserId();
		String currentMonth = StringUtil.dateToString(new Date(),"yyyyMM");
		TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
		CloudHostBillDetailMapper cloudHostBillDetailMapper = this.sqlSession.getMapper(CloudHostBillDetailMapper.class);
		CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
		Map<String,Object> condition  = new LinkedHashMap<String, Object>();
		Map<String,Object> results = new HashMap<String, Object>();
		condition.put("userId", userId);
		//传入当前月
		condition.put("currentMonth", "%"+currentMonth+"%");
		//查询总用户数和当月新增用户数
		int userCount = terminalUserMapper.terminalUserCount(userId);
		int newUserCount = terminalUserMapper.newTerminalUserCount(condition);
		//总收入、包月收入和按量付费收入
		BigDecimal incomeCount = cloudHostBillDetailMapper.incomeCountForAgent(userId);
		BigDecimal incomeCountForMonthly = cloudHostBillDetailMapper.incomeCountPaymentForMonthly(userId);
		BigDecimal incomeCountForDosage = cloudHostBillDetailMapper.incomeCountPaymentForDosage(userId);
		//当前云主机总数
		int cloudHostCount = 0;
		if(loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT){
			int countForAgentOne = cloudHostMapper.cloudHostCountForAgent(userId);
			int countForAgentTwo = cloudHostMapper.cloudHostCountForAgentTwo(userId);
			cloudHostCount = countForAgentOne + countForAgentTwo;
		}else{
			int countForOperatorOne   = cloudHostMapper.cloudHostCountForOperator(userId);
			int countForOperatorTwo   = cloudHostMapper.cloudHostCount(userId);
			//此方查询所属运营商用户的云主机数量，因sql语句与代理商的完全相同所以直接调用代理商的方法。
			int countForoperatorThree = cloudHostMapper.cloudHostCountForAgentTwo(userId);
			//---
			cloudHostCount = countForOperatorOne + countForOperatorTwo + countForoperatorThree;
		}
		if(incomeCount == null){
			incomeCount = BigDecimal.ZERO;
		}
		if(incomeCountForMonthly == null){
			incomeCountForMonthly = BigDecimal.ZERO;
		}
		if(incomeCountForDosage == null){
			incomeCountForDosage = BigDecimal.ZERO;
		}
		
		//将结果添加到Map中，传到前台页面
		results.put("userCount", userCount);
		results.put("newUserCount", newUserCount);
		results.put("incomeCount", incomeCount);
		results.put("incomeCountForMonthly", incomeCountForMonthly);
		results.put("incomeCountForDosage", incomeCountForDosage);
		results.put("cloudHostCount", cloudHostCount);
		request.setAttribute("statement", results);
		if(loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT){
			return "/security/agent/statement_manage.jsp";
		}
		return "/security/operator/statement_manage.jsp";
	}
	
	@Callable
	public String businessGraphicsPage (HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("StatementServiceImpl.businessGraphicsPage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		
		if( loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT && loginInfo.hasPrivilege(PrivilegeConstant.agent_business_graphics_page) == true )
		{
			return "/security/agent/business_graphics.jsp";
		}else 
		{
			return "/public/have_not_access.jsp";
		}
		
		
		
	}

	@Callable
	public MethodResult businessGraphicsByTime(Map<String, Object> parameter) 
	{
		logger.debug("StatementServiceImpl.businessGraphicsByTime()");
		try {
			String select_time = StringUtil.trim(parameter.get("select_time"));
			HttpServletRequest request =RequestContext.getHttpRequest();
			LoginInfo loginInfo=LoginHelper.getLoginInfo(request);
			String userId=loginInfo.getUserId(); 
//			System.out.println(select_time+"----"+region+"===="+goods);
	//		String select_time=StringUtil.trim(parameter.get("select_time"));
			
			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
			CloudHostBillDetailMapper cloudHostBillDetailMapper = this.sqlSession.getMapper(CloudHostBillDetailMapper.class);
			CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
			Map<String,Object> results = new HashMap<String, Object>();
			Map<String,Object> condition  = new LinkedHashMap<String, Object>();
			condition.put("userId", userId);
			
			//七天要获取的数据
			if (select_time.equals("seven")) {
				Calendar b= Calendar.getInstance();
				b.add(Calendar.DAY_OF_YEAR,-7);
				int userCount[]=new int[7];
				int hostCount[]=new int[7];
				String sevenTime[] =new String[7];
				int userCounts=0;
				int hostCounts=0;
				for (int i = 0; i < 7; i++) {
					b.add(Calendar.DAY_OF_YEAR,+1);
					String time  =new SimpleDateFormat("yyyyMMdd").format(b.getTime());
					condition.put("time", "%"+time+"%");
					sevenTime[i]=new SimpleDateFormat("yyyy/MM/dd").format(b.getTime());
					userCount[i]=terminalUserMapper.getTerminalUserCountByTime(condition);
					hostCount[i]=cloudHostMapper.getCloudHostForUserByTime(condition);
					userCounts = userCount[i]+userCounts;
					hostCounts = hostCount[i]+hostCounts;
				}
				MethodResult methodResult =new MethodResult();
				methodResult.properties.put("userCount",userCount);
				methodResult.properties.put("hostCount", hostCount);
				methodResult.properties.put("userCounts", userCounts);
				methodResult.properties.put("hostCounts", hostCounts);
				methodResult.properties.put("time", sevenTime);
				return methodResult;
			}
			//一个月要获取的数据
			if (select_time.equals("month")) {
				Calendar a = Calendar.getInstance();
				a.add(Calendar.DAY_OF_YEAR,-30);
				int userCountMonth[]=new int[30];
				int hostCountMonth[]=new int[30];
				String monthTime[]=new String[30];
				int userCount[]=new int[10];
				int hostCount[]=new int[10];
				String monthtime[]=new String[10];
				int userCountMonths=0;
				int hostCountMonths=0;
				
				for (int j = 0; j < 10; j++) {
					
					for (int i = 0;i < 3; i++) {
						a.add(Calendar.DAY_OF_YEAR,+1);
						String time  =new SimpleDateFormat("yyyyMMdd").format(a.getTime());
						condition.put("time", "%"+time+"%");
						monthTime[i]=new SimpleDateFormat("yyyy/MM/dd").format(a.getTime());
						userCountMonth[i]=terminalUserMapper.getTerminalUserCountByTime(condition);
						hostCountMonth[i]=cloudHostMapper.getCloudHostForUserByTime(condition);
						userCountMonths = userCountMonth[i]+userCountMonths;
						hostCountMonths = hostCountMonth[i]+hostCountMonths;
						//
					}
					userCount[j]=userCountMonth[0]+userCountMonth[1]+userCountMonth[2];
					hostCount[j]=hostCountMonth[0]+hostCountMonth[1]+hostCountMonth[2];
					monthtime[j]=monthTime[2];
						
				}
				
				MethodResult methodResult =new MethodResult();
				methodResult.properties.put("userCount",userCount);
				methodResult.properties.put("hostCount", hostCount);
				methodResult.properties.put("userCounts", userCountMonths);
				methodResult.properties.put("hostCounts", hostCountMonths);
				methodResult.properties.put("time", monthtime);
				return methodResult;
				
			}
			//一年要获取的数据
			if (select_time.equals("year")) {
				Calendar c = Calendar.getInstance();
				c.add(Calendar.MONTH,-12);
				int userCount[]=new int[12];
				int hostCount[]=new int[12];
				String yearTime[] =new String[12];
				int userCounts=0;
				int hostCounts=0;
				
				for (int i = 0; i < 12; i++) {
					c.add(Calendar.MONTH,+1);
					String time  =new SimpleDateFormat("yyyyMM").format(c.getTime());
					condition.put("time", "%"+time+"%");
					yearTime[i]=new SimpleDateFormat("yyyy/MM").format(c.getTime());
					userCount[i]=terminalUserMapper.getTerminalUserCountByTime(condition);
					hostCount[i]=cloudHostMapper.getCloudHostForUserByTime(condition);
					userCounts = userCount[i]+userCounts;
					hostCounts = hostCount[i]+hostCounts;
				}
				MethodResult methodResult =new MethodResult();
				methodResult.properties.put("userCount",userCount);
				methodResult.properties.put("hostCount", hostCount);
				methodResult.properties.put("userCounts", userCounts);
				methodResult.properties.put("hostCounts", hostCounts);
				methodResult.properties.put("time", yearTime);
				return methodResult;
			}
			
		} catch (Exception e) {
			logger.error("StatementServiceImpl.businessGraphicsByTime()",e);
			throw new AppException("失败");
		}
		return new MethodResult(MethodResult.FAIL,"失败");
	}

	@Callable
	public MethodResult businessGraphicsByTimeAndOthers(Map<String, Object> parameter) 
	{
		logger.debug("StatementServiceImpl.businessGraphicsByTimeAndOthers()");
		try {
			
			HttpServletRequest request =RequestContext.getHttpRequest();
			LoginInfo loginInfo=LoginHelper.getLoginInfo(request);
			String userId=loginInfo.getUserId(); 
//			System.out.println(select_time+"----"+region+"===="+goods);
			String select_time=StringUtil.trim(parameter.get("select_time2")); 
			String region=StringUtil.trim(parameter.get("select_region")); 
			String goods=StringUtil.trim(parameter.get("select_goods")); 
			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
			CloudHostBillDetailMapper cloudHostBillDetailMapper = this.sqlSession.getMapper(CloudHostBillDetailMapper.class);
			CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
			Map<String,Object> results = new HashMap<String, Object>();
			Map<String,Object> condition  = new LinkedHashMap<String, Object>();
			condition.put("userId", userId);
			
			//七天要获取的数据
			if (select_time.equals("seven")) {
				Calendar b= Calendar.getInstance();
				b.add(Calendar.DAY_OF_YEAR,-7);
				String sevenTime[] =new String[7];
				BigDecimal incomeCount[]=new BigDecimal[7];
				BigDecimal incomeCounts =new BigDecimal("0");
				if (StringUtil.isBlank(region)==false) {
					condition.put("region", region);
				}
				if (StringUtil.isBlank(goods)==false) {
					condition.put("", goods);
				}
				for (int i = 0; i < 7; i++) {
					b.add(Calendar.DAY_OF_YEAR,+1);
					String time  =new SimpleDateFormat("yyyyMMdd").format(b.getTime());
					condition.put("time", "%"+time+"%");
					sevenTime[i]=new SimpleDateFormat("yyyy/MM/dd").format(b.getTime());
					incomeCount[i]=cloudHostBillDetailMapper.getIncomeCountByTime(condition);
					if (incomeCount[i]==null) {
						incomeCount[i]=BigDecimal.ZERO;
					}
					incomeCounts =incomeCount[i].add(incomeCounts);
				}
				MethodResult methodResult =new MethodResult();
				methodResult.properties.put("incomeCount",incomeCount);
				methodResult.properties.put("incomeCounts", incomeCounts);
				methodResult.properties.put("time", sevenTime);
				return methodResult;
			}
			//一个月要获取的数据
			if (select_time.equals("month")) {
				Calendar a = Calendar.getInstance();
				a.add(Calendar.DAY_OF_YEAR,-30);
				String monthTime[]=new String[30];
				String monthtime[]=new String[10];
				BigDecimal incomeCountMonth[]=new BigDecimal[30];
				BigDecimal incomeCount[]=new BigDecimal[10];
				BigDecimal incomeCountMonths =new BigDecimal("0");
				if (StringUtil.isBlank(region)==false) {
					condition.put("region", region);
				}
				if (StringUtil.isBlank(goods)==false) {
					condition.put("", goods);
				}
				
				for (int j = 0; j < 10; j++) {
					
					for (int i = 0; i < 3; i++) {
						a.add(Calendar.DAY_OF_YEAR,+1);
						String time  =new SimpleDateFormat("yyyyMMdd").format(a.getTime());
						condition.put("time", "%"+time+"%");
						monthTime[i]=new SimpleDateFormat("yyyy/MM/dd").format(a.getTime());
						incomeCountMonth[i] =BigDecimal.ZERO;
						incomeCountMonth[i]=cloudHostBillDetailMapper.getIncomeCountByTime(condition);
						if (incomeCountMonth[i]==null) {
							incomeCountMonth[i]=new BigDecimal("0");
						}
						incomeCountMonths =incomeCountMonths.add(incomeCountMonth[i]==null?BigDecimal.ZERO:incomeCountMonth[i]);
					}
					
					incomeCount[j]=incomeCountMonth[0].add(incomeCountMonth[1]).add(incomeCountMonth[2]);
					monthtime[j]=monthTime[2];
				}
				
				MethodResult methodResult =new MethodResult();
				methodResult.properties.put("incomeCount",incomeCount);
				methodResult.properties.put("incomeCounts",incomeCountMonths);
				methodResult.properties.put("time", monthtime);
				return methodResult;
				
			}
			//一年要获取的数据
			if (select_time.equals("year")) {
				Calendar c = Calendar.getInstance();
				c.add(Calendar.MONTH,-12);
				String yearTime[] =new String[12];
				BigDecimal incomeCount[]=new BigDecimal[12];
				BigDecimal incomeCounts =new BigDecimal("0");
				if (StringUtil.isBlank(region)==false) {
					condition.put("region", region);
				}
				if (StringUtil.isBlank(goods)==false) {
					condition.put("", goods);
				}
				
				for (int i = 0; i < 12; i++) {
					c.add(Calendar.MONTH,+1);
					String time  =new SimpleDateFormat("yyyyMM").format(c.getTime());
					condition.put("time", "%"+time+"%");
					yearTime[i]=new SimpleDateFormat("yyyy/MM").format(c.getTime());
					incomeCount[i]=cloudHostBillDetailMapper.getIncomeCountByTime(condition);
					if (incomeCount[i]==null) {
						incomeCount[i]=BigDecimal.ZERO;
					}
					incomeCounts =incomeCount[i].add(incomeCounts);
				}
				MethodResult methodResult =new MethodResult();
				methodResult.properties.put("incomeCount",incomeCount);
				methodResult.properties.put("incomeCounts",incomeCounts);
				methodResult.properties.put("time", yearTime);
				return methodResult;
			}
			
		} catch (Exception e) {
			logger.error("StatementServiceImpl.businessGraphicsByTimeAndOthers()",e);
			throw new AppException(e);
		}
		return new MethodResult(MethodResult.FAIL,"失败");
	}
	
	

}
