package com.zhicloud.op.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.zhicloud.op.app.authorization.PrivilegeConstant;
import com.zhicloud.op.app.helper.LoginHelper;
import com.zhicloud.op.app.helper.ServiceHelper;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.login.LoginInfo;
import com.zhicloud.op.mybatis.mapper.BillMapper;
import com.zhicloud.op.mybatis.mapper.CloudHostBillDetailMapper;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.service.BusinessStatisticsService;
import com.zhicloud.op.vo.BillVO;
import com.zhicloud.op.vo.BusinessStatisticsVO;
import com.zhicloud.op.vo.CloudHostBillDetailVO;

public class BusinessStatisticsServiceImpl extends BeanDirectCallableDefaultImpl implements BusinessStatisticsService {
	
	
	public static final Logger logger = Logger.getLogger(CloudHostBillDetailServiceImpl.class);

	private SqlSession sqlSession;
	
	public void setSqlSession(SqlSession sqlSession)
	{
		this.sqlSession = sqlSession;
	}



	@Callable
	public String toBusinessDetailManagePage(HttpServletRequest request,
			HttpServletResponse response) { 
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.hasPrivilege(PrivilegeConstant.service_detail_agent) == false )
		{
			return "/public/have_not_access.jsp";
		}
		
		// 查询数据库
		CloudHostBillDetailMapper cloudHostBillDetailMapper = this.sqlSession.getMapper(CloudHostBillDetailMapper.class);
		BigDecimal incomeCount = cloudHostBillDetailMapper.countAllBill(loginInfo.getUserId());
		request.setAttribute("incomeCount", incomeCount);
		return "/security/agent/business_detail_manage.jsp";
	}
	
	
	@Callable
	public void queryBusinessByMonthly(HttpServletRequest request, HttpServletResponse response) { 
		
		try
		{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");
			
			// 获取参数
			String userId = request.getParameter("user_id");
			Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
			Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 10);

			// 查询数据库
			BillMapper billMapper = this.sqlSession.getMapper(BillMapper.class);
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("id", userId);
			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			int total = billMapper.getDetailByMonthlyCount(condition); // 总行数
			List<BillVO> invoiceList = (List<BillVO>) billMapper.getDetailByMonthly(condition);// 分页结果
			
			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, invoiceList);
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("查询失败");
		}
	}

	@Callable
	public String agentMonthlyDetailPage(HttpServletRequest request,
			HttpServletResponse response) {
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request); 
		if( loginInfo.hasPrivilege(PrivilegeConstant.service_detail_agent) == false )
		{
			return "/public/have_not_access.jsp";
		}
		// 查询数据库
		BillMapper billMapper = this.sqlSession.getMapper(BillMapper.class);
		Map<String, Object> condition = new LinkedHashMap<String, Object>();
		condition.put("id", loginInfo.getUserId());
		condition.put("createTime", "%"+request.getParameter("createTime")+"%"); 
		BigDecimal incomeCount = billMapper.countMonthlyBill(condition);
		request.setAttribute("incomeCount", incomeCount);
		return "/security/agent/agent_monthly_detail_manage.jsp";
	}

	@Callable
	public void queryAgentMonthlyDetail(HttpServletRequest request,
			HttpServletResponse response) {
		try
		{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");
			
			// 获取参数
			 LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
			Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 10);

			// 查询数据库
			BillMapper billMapper = this.sqlSession.getMapper(BillMapper.class);
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("id", loginInfo.getUserId());
			condition.put("createTime", "%"+request.getParameter("createTime")+"%");
			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			int total = billMapper.getDetailByHostMonthlyCount(condition); // 总行数
			List<BillVO> invoiceList = (List<BillVO>) billMapper.getDetailByHostMonthly(condition);// 分页结果
			
			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, invoiceList);
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("查询失败");
		}
		
	}

	@Override
	public List<CloudHostBillDetailVO> queryAgentMonthlyDetailForExport(
			HttpServletRequest request, HttpServletResponse response) {
		try
		{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");
			
			// 获取参数
			 LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
//			Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
//			Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 10);

			// 查询数据库
			CloudHostBillDetailMapper cloudHostBillDetailMapper = this.sqlSession.getMapper(CloudHostBillDetailMapper.class);
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("id", loginInfo.getUserId());
			condition.put("createTime", "%"+request.getParameter("createTime")+"%");
//			condition.put("start_row", page * rows);
//			condition.put("row_count", rows);
 			List<CloudHostBillDetailVO> billList = (List<CloudHostBillDetailVO>) cloudHostBillDetailMapper.getAllDetailByHostMonthly(condition);// 分页结果
			return billList;
 		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("查询失败");
		}
	}

	@Callable
	public void queryBillDetailByTimeAndRegion(HttpServletRequest request,
			HttpServletResponse response) { 
		try
		{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");
			
			// 获取参数
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			String time = request.getParameter("time"); 
			if(StringUtil.isBlank(time)){
				time = "0";
			}
			Calendar now = Calendar.getInstance(); 
			if("1".equals(time)){
				//最近一年
				now.set(Calendar.YEAR, now.get(Calendar.YEAR)-1);
			}else if("2".equals(time)){
				//最近一个月
				now.set(Calendar.MONTH, now.get(Calendar.MONTH)-1);				
			}else if("3".equals(time)){
				//最近一周
				now.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH)-7);								
			} 
			String passedTime = StringUtil.dateToString(now.getTime(), "yyyyMMddHHmmssSSS");
			List<BusinessStatisticsVO> totalList = new ArrayList<BusinessStatisticsVO>();
			// 查询云主机的消费情况
			BillMapper billMapper = this.sqlSession.getMapper(BillMapper.class);
			BusinessStatisticsVO hostVo = new BusinessStatisticsVO();
			BusinessStatisticsVO vpcVo = new BusinessStatisticsVO();
			hostVo.setType(1);
			vpcVo.setType(3);
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("id", loginInfo.getUserId());
			condition.put("type", 1);
			if(!("0".equals(time))){				
				condition.put("startTime", passedTime); 
			}
			condition.put("region", "1"); 
			BigDecimal  gzHostStatistics = billMapper.getIncomeByTimeAndRegion(condition);
			hostVo.setGzStatistics(gzHostStatistics);
			condition.put("region", "2"); 
			BigDecimal  cdHostStatistics = billMapper.getIncomeByTimeAndRegion(condition);
			hostVo.setCdStatistics(cdHostStatistics);
			condition.put("region", "3"); 
			BigDecimal  bjHostStatistics = billMapper.getIncomeByTimeAndRegion(condition);
			hostVo.setBjStatistics(bjHostStatistics);
			condition.put("region", "4"); 
			BigDecimal  xgHostStatistics = billMapper.getIncomeByTimeAndRegion(condition);
			hostVo.setXgStatistics(xgHostStatistics);			
			hostVo.setTotalStatistics();
			totalList.add(hostVo); 
			
			condition.put("type", 3);
			condition.put("region", "1"); 
			BigDecimal  gzVpcStatistics = billMapper.getIncomeByTimeAndRegion(condition);
			vpcVo.setGzStatistics(gzVpcStatistics);
			condition.put("region", "2"); 
			BigDecimal  cdVpcStatistics = billMapper.getIncomeByTimeAndRegion(condition);
			vpcVo.setCdStatistics(cdVpcStatistics);
			condition.put("region", "3"); 
			BigDecimal  bjVpcStatistics = billMapper.getIncomeByTimeAndRegion(condition);
			vpcVo.setBjStatistics(bjVpcStatistics);
			condition.put("region", "4"); 
			BigDecimal  xgVpcStatistics = billMapper.getIncomeByTimeAndRegion(condition);
			vpcVo.setXgStatistics(xgVpcStatistics);			
			vpcVo.setTotalStatistics();
			totalList.add(vpcVo);  
			
			//云硬盘的消费情况
			BusinessStatisticsVO storageVo = new BusinessStatisticsVO();
			storageVo.setType(2);
			storageVo.setBjStatistics(BigDecimal.ZERO);
			storageVo.setGzStatistics(BigDecimal.ZERO);
			storageVo.setCdStatistics(BigDecimal.ZERO);
			storageVo.setXgStatistics(BigDecimal.ZERO);
			storageVo.setTotalStatistics();
//			totalList.add(storageVo);
			
			//计算总的统计
			BusinessStatisticsVO totalVo = new BusinessStatisticsVO();
			totalVo.setType(0);
			totalVo.setBjStatistics(hostVo.getBjStatistics().add(vpcVo.getBjStatistics()));
			totalVo.setGzStatistics(hostVo.getGzStatistics().add(vpcVo.getGzStatistics()));
			totalVo.setCdStatistics(hostVo.getCdStatistics().add(vpcVo.getCdStatistics()));
			totalVo.setXgStatistics(hostVo.getXgStatistics().add(vpcVo.getXgStatistics()));
			totalVo.setTotalStatistics();
			totalList.add(totalVo);
			
			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), totalList.size(), totalList);
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("查询失败");
		}
	}

	@Callable
	public String agentBusinessStatistics(HttpServletRequest request,
			HttpServletResponse response) { 
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.hasPrivilege(PrivilegeConstant.business_statistics_agent) == false )
		{
			return "/public/have_not_access.jsp";
		}
		return "/security/agent/agent_business_statistics_manage.jsp";
	}



	@Override
	public List<BusinessStatisticsVO> queryBusinessStatistics( HttpServletRequest request, HttpServletResponse response) {
		// 获取参数
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		String time = request.getParameter("time"); 
		if(StringUtil.isBlank(time)){
			time = "0";
		}
		Calendar now = Calendar.getInstance(); 
		if("1".equals(time)){
			//最近一年
			now.set(Calendar.YEAR, now.get(Calendar.YEAR)-1);
		}else if("2".equals(time)){
			//最近一个月
			now.set(Calendar.MONTH, now.get(Calendar.MONTH)-1);				
		}else if("3".equals(time)){
			//最近一周
			now.set(Calendar.DAY_OF_MONTH, now.get(Calendar.DAY_OF_MONTH)-7);								
		} 
		String passedTime = StringUtil.dateToString(now.getTime(), "yyyyMMddHHmmssSSS");
		List<BusinessStatisticsVO> totalList = new ArrayList<BusinessStatisticsVO>();
		// 查询云主机的消费情况
		BillMapper billMapper = this.sqlSession.getMapper(BillMapper.class);
		BusinessStatisticsVO hostVo = new BusinessStatisticsVO();
		BusinessStatisticsVO vpcVo = new BusinessStatisticsVO();
		hostVo.setType(1);
		vpcVo.setType(3);
		Map<String, Object> condition = new LinkedHashMap<String, Object>();
		condition.put("id", loginInfo.getUserId());
		condition.put("type", 1);
		if(!("0".equals(time))){				
			condition.put("startTime", passedTime); 
		}
		condition.put("region", "1"); 
		BigDecimal  gzHostStatistics = billMapper.getIncomeByTimeAndRegion(condition);
		hostVo.setGzStatistics(gzHostStatistics);
		condition.put("region", "2"); 
		BigDecimal  cdHostStatistics = billMapper.getIncomeByTimeAndRegion(condition);
		hostVo.setCdStatistics(cdHostStatistics);
		condition.put("region", "3"); 
		BigDecimal  bjHostStatistics = billMapper.getIncomeByTimeAndRegion(condition);
		hostVo.setBjStatistics(bjHostStatistics);
		condition.put("region", "4"); 
		BigDecimal  xgHostStatistics = billMapper.getIncomeByTimeAndRegion(condition);
		hostVo.setXgStatistics(xgHostStatistics);			
		hostVo.setTotalStatistics();
		totalList.add(hostVo);  
		
		
		condition.put("type", 3);
		condition.put("region", "1"); 
		BigDecimal  gzVpcStatistics = billMapper.getIncomeByTimeAndRegion(condition);
		vpcVo.setGzStatistics(gzVpcStatistics);
		condition.put("region", "2"); 
		BigDecimal  cdVpcStatistics = billMapper.getIncomeByTimeAndRegion(condition);
		vpcVo.setCdStatistics(cdVpcStatistics);
		condition.put("region", "3"); 
		BigDecimal  bjVpcStatistics = billMapper.getIncomeByTimeAndRegion(condition);
		vpcVo.setBjStatistics(bjVpcStatistics);
		condition.put("region", "4"); 
		BigDecimal  xgVpcStatistics = billMapper.getIncomeByTimeAndRegion(condition);
		vpcVo.setXgStatistics(xgVpcStatistics);			
		vpcVo.setTotalStatistics();
		totalList.add(vpcVo);
		//云硬盘的消费情况
		BusinessStatisticsVO storageVo = new BusinessStatisticsVO();
		storageVo.setType(2);
		storageVo.setBjStatistics(BigDecimal.ZERO);
		storageVo.setGzStatistics(BigDecimal.ZERO);
		storageVo.setCdStatistics(BigDecimal.ZERO);
		storageVo.setXgStatistics(BigDecimal.ZERO);
		storageVo.setTotalStatistics();
//		totalList.add(storageVo);
		//计算总的统计
		BusinessStatisticsVO totalVo = new BusinessStatisticsVO();
		totalVo.setType(0);
		totalVo.setBjStatistics(hostVo.getBjStatistics().add(vpcVo.getBjStatistics()));
		totalVo.setGzStatistics(hostVo.getGzStatistics().add(vpcVo.getGzStatistics()));
		totalVo.setCdStatistics(hostVo.getCdStatistics().add(vpcVo.getCdStatistics()));
		totalVo.setXgStatistics(hostVo.getXgStatistics().add(vpcVo.getXgStatistics()));
		totalVo.setTotalStatistics();
		totalList.add(totalVo);
		
		
		return totalList;
	}

}
