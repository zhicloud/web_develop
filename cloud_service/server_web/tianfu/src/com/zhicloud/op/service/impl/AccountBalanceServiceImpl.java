package com.zhicloud.op.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.op.app.authorization.PrivilegeConstant;
import com.zhicloud.op.app.helper.CountUserProductsPriceHelper;
import com.zhicloud.op.app.helper.LoginHelper;
import com.zhicloud.op.app.helper.ServiceHelper;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.login.LoginInfo;
import com.zhicloud.op.mybatis.mapper.AccountBalanceDetailMapper;
import com.zhicloud.op.mybatis.mapper.AgentMapper;
import com.zhicloud.op.mybatis.mapper.CloudHostMapper;
import com.zhicloud.op.mybatis.mapper.OperLogMapper;
import com.zhicloud.op.mybatis.mapper.SysUserMapper;
import com.zhicloud.op.mybatis.mapper.TerminalUserMapper;
import com.zhicloud.op.mybatis.mapper.UserMessageMapper;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.service.AccountBalanceService;
import com.zhicloud.op.service.constant.AppConstant;
import com.zhicloud.op.vo.AccountBalanceDetailVO;
import com.zhicloud.op.vo.AgentVO;
import com.zhicloud.op.vo.CloudHostVO;
import com.zhicloud.op.vo.SysUserVO;
import com.zhicloud.op.vo.TerminalUserVO;
@Transactional(readOnly = false)
public class AccountBalanceServiceImpl extends BeanDirectCallableDefaultImpl implements AccountBalanceService {
	public static final Logger logger = Logger.getLogger(AgentServiceImpl.class);

	private SqlSession sqlSession;

	public void setSqlSession(SqlSession sqlSession)
	{
		this.sqlSession = sqlSession;
	} 
	@Callable
	public  String  updateAfterPay(String orderId) {  
		Long begin = System.currentTimeMillis();
		String now = StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS");
		AccountBalanceDetailMapper accountBalanceDetailMapper = this.sqlSession.getMapper(AccountBalanceDetailMapper.class);  
		SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
		TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class); 
 		AccountBalanceDetailVO detail = accountBalanceDetailMapper.getBalanceDetailById(orderId);
 		int n = 0;
 		BigDecimal balance_before_change =  new BigDecimal("0");
 		BigDecimal balance_after_change = new BigDecimal("0"); 
  		if(detail!=null){
  			SysUserVO vo = sysUserMapper.getById(detail.getUserId());
  			// 代理商用户充值处理
  			if(vo.getType()==3){
   				AgentMapper agentMapper = this.sqlSession.getMapper(AgentMapper.class); 
  				AgentVO agentVO = agentMapper.getAgentById(detail.getUserId());
  				if(agentVO!=null){
  					balance_before_change =  agentVO.getAccountBalance();
  					
  				}
  				
  				balance_after_change = balance_before_change.add(detail.getAmount());
  				Map<String, Object> balanceDetailData = new LinkedHashMap<String, Object>(); 
  				balanceDetailData.put("id",     orderId); 
  				balanceDetailData.put("balanceAfterChange",     balance_after_change);
  				balanceDetailData.put("balanceBeforeChange",     balance_before_change);
  				balanceDetailData.put("rechargeStatus",     2); 
  				balanceDetailData.put("changeTime",     now); 
  				int m = accountBalanceDetailMapper.updateAfterRecharge(balanceDetailData);
  				if(m>0){ 
  					Map<String, Object> balanceData = new LinkedHashMap<String, Object>(); 
  					balanceData.put("id",     detail.getUserId()); 
  					balanceData.put("accountBalance",     balance_after_change);
   					n = agentMapper.updateBalanceById(balanceData); 
  					if(n<=0){
  						
  						throw new AppException("失败");
  					} 
  				}else{
  					throw new AppException("失败");
  				} 
  				
  			}else{
  				//终端用户充值
  				TerminalUserVO terminalUserVO = terminalUserMapper.getBalanceById(detail.getUserId());
  				if(terminalUserVO!=null){
  					balance_before_change =  terminalUserVO.getAccountBalance();
  					
  				}
  				
  				balance_after_change = balance_before_change.add(detail.getAmount());
  				Map<String, Object> balanceDetailData = new LinkedHashMap<String, Object>(); 
  				balanceDetailData.put("id",     orderId); 
  				balanceDetailData.put("balanceAfterChange",     balance_after_change);
  				balanceDetailData.put("balanceBeforeChange",     balance_before_change);
  				balanceDetailData.put("rechargeStatus",     2); 
  				balanceDetailData.put("changeTime",     now); 
  				int m = accountBalanceDetailMapper.updateAfterRecharge(balanceDetailData);
  				if(m>0){
  					if(detail.getGiftAmount()!=null&&detail.getGiftAmount().compareTo(BigDecimal.ZERO)==1){
  						balance_before_change = balance_after_change;
  						balance_after_change = balance_after_change.add(detail.getGiftAmount());
  						balanceDetailData = new LinkedHashMap<String, Object>(); 
  						balanceDetailData.put("id",     StringUtil.generateUUID()); 
  						balanceDetailData.put("balanceAfterChange",     balance_after_change);
  						balanceDetailData.put("balanceBeforeChange",     balance_before_change);
  						balanceDetailData.put("amount",     detail.getGiftAmount());  
  						balanceDetailData.put("userId",     detail.getUserId());  
  						balanceDetailData.put("rechargeStatus",     "2");  
  						balanceDetailData.put("type",     "1");  
  						balanceDetailData.put("payType",     "3");  
  						balanceDetailData.put("changeTime",     now); 
  						balanceDetailData.put("description",     "送费");  
  						int k = accountBalanceDetailMapper.addAccountBalanceDetail(balanceDetailData);
  						if(k<=0){
  							
  							throw new AppException("失败");
  						}
  					}
  					Map<String, Object> balanceData = new LinkedHashMap<String, Object>(); 
  					balanceData.put("id",     detail.getUserId()); 
  					balanceData.put("accountBalance",     balance_after_change);
  					balanceData.put("balanceUpdateTime",     now); 
  					n = terminalUserMapper.updateBalanceById(balanceData); 
  					if(n<=0){
  						
  						throw new AppException("失败");
  					}
  					//余额为正，删除扣费通知
  					if(balance_after_change.compareTo(BigDecimal.ZERO)>0){
  						UserMessageMapper userMessageMapper = this.sqlSession.getMapper(UserMessageMapper.class);
  						Map<String, Object> userData = new LinkedHashMap<String, Object>(); 
  						userData.put("userId",     detail.getUserId()); 
  						userData.put("status",     2); 
  						userMessageMapper.deleteUserMessageByStatus(userData);
  						
  					}
  				}else{
  					throw new AppException("失败");
  				}
  			}
  			OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
				Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
				operLogData.put("id", StringUtil.generateUUID());
				operLogData.put("userId", detail.getUserId());
				operLogData.put("content", "通过"+(detail.getPayType() == 1 ? "支付宝" : "银联")+"充值了"+detail.getAmount()+"元人民币");
				operLogData.put("operTime", StringUtil.dateToString(new Date(),"yyyyMMddHHmmssSSS"));
				operLogData.put("status", AppConstant.OPER_LOG_SUCCESS);
				operLogData.put("resourceName", "充值");
				operLogData.put("operDuration", System.currentTimeMillis()-begin);
				operLogMapper.addOperLog(operLogData);
  		}
			
		
		return "success";
	}
	@Callable
	public void queryRechargeRecord(HttpServletRequest request,HttpServletResponse response) {
		logger.debug("AccountBalanceServiceImpl.queryRechargeRecord()");
		try
		{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");
			
			// 参数处理
			String userId="";
			String startTime = StringUtil.trim(request.getParameter("startTime"));
			String endTime = StringUtil.trim(request.getParameter("endTime"));
			Integer page    = StringUtil.parseInteger(request.getParameter("page"), 1) - 1;	// 前台过来的是从1开始，需要的是从0开始
			Integer rows    = StringUtil.parseInteger(request.getParameter("rows"), 10);
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			if(loginInfo!=null){
				userId = loginInfo.getUserId();
			}
			// 查询数据库
			AccountBalanceDetailMapper accountBalanceDetailMapper = this.sqlSession.getMapper(AccountBalanceDetailMapper.class);  
			
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("userId",  userId);
			condition.put("type", 1);
			if(endTime!=null&&endTime.length()>0){
				endTime = endTime.replace("-", "")+"240000000";
				condition.put("endTime",  endTime);
				
			}
			if(startTime!=null&&startTime.length()>0){
				startTime = startTime.replace("-", "")+"000000000";
				condition.put("startTime",  startTime);
				
			} 
			 
			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			int total = accountBalanceDetailMapper.getRechargeDetailByUserIdCount(condition);		// 总行数 
			List<AccountBalanceDetailVO> record = accountBalanceDetailMapper.getRechargeDetailByUserId(condition);	// 分页结果
			List<AccountBalanceDetailVO> recordLast = new ArrayList<AccountBalanceDetailVO> ();
			for(AccountBalanceDetailVO vo : record){
				if(vo.getPayType() == 1 || vo.getPayType() ==2){
					vo.setDescription("充值单号："+vo.getId());
				}
				recordLast.add(vo);
			}
			
			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, recordLast);
		}
		catch (Exception e)
		{
			logger.error("AccountBalanceServiceImpl.queryRechargeRecord()",e);
			throw new AppException("查询失败");
		}
		
	}
	@Callable
	public void queryConsumptionRecord(HttpServletRequest request,HttpServletResponse response) {
		logger.debug("AccountBalanceServiceImpl.queryConsumptionRecord()");
		try
		{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");
			
			// 参数处理
			String userId="";
			String startTime = StringUtil.trim(request.getParameter("startTime"));
			String endTime = StringUtil.trim(request.getParameter("endTime"));
			Integer page    = StringUtil.parseInteger(request.getParameter("page"), 1) - 1;	// 前台过来的是从1开始，需要的是从0开始
			Integer rows    = StringUtil.parseInteger(request.getParameter("rows"), 10);
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			if(loginInfo!=null){
				userId = loginInfo.getUserId();
			}
			// 查询数据库
			AccountBalanceDetailMapper accountBalanceDetailMapper = this.sqlSession.getMapper(AccountBalanceDetailMapper.class);  
			
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("userId",  userId);
			condition.put("type", 2);
			if(endTime!=null&&endTime.length()>0){
				endTime = endTime.replace("-", "")+"240000000";
				condition.put("endTime",  endTime);
				
			}
			if(startTime!=null&&startTime.length()>0){
				startTime = startTime.replace("-", "")+"000000000";
				condition.put("startTime",  startTime);
				
			} 
			 
			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			int total = accountBalanceDetailMapper.getRechargeDetailByUserIdCount(condition);		// 总行数 
			List<AccountBalanceDetailVO> record = accountBalanceDetailMapper.getRechargeDetailByUserId(condition);	// 分页结果
			
			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, record);
		}
		catch (Exception e)
		{
			logger.error("AccountBalanceServiceImpl.queryConsumptionRecord()",e);
			throw new AppException("查询失败");
		}
		
	}
	
	@Callable
	public String toRechargeRecordPage(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("AccountBalanceServiceImpl.toRechargeRecordPage()"); 
		this.getBalance(request, response);
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		AccountBalanceDetailMapper accountBalanceDetailMapper = this.sqlSession.getMapper(AccountBalanceDetailMapper.class);  
		Map<String, Object> condition = new LinkedHashMap<String, Object>();
		condition.put("userId",  loginInfo.getUserId());
		condition.put("type", "1"); 
		String all = accountBalanceDetailMapper.getAllAccount(condition);
		request.setAttribute("all", all==null?"0":all);
		if(loginInfo.getUserType()==4){
			
			return "/security/user/rechargelog.jsp" ; 
		}else{
			if( loginInfo.hasPrivilege(PrivilegeConstant.recharge_detail_agent) == false )
			{
				return "/public/have_not_access.jsp";
			}
			AgentMapper agentMapper = this.sqlSession.getMapper(AgentMapper.class);
			AgentVO agentVO = agentMapper.getAgentById(loginInfo.getUserId());
			request.setAttribute("agentVO", agentVO);
			return "/security/agent/rechargelog.jsp" ; 
			
		}
//	    return "/security/user/recharge_record_manage.jsp" ; 
	}
	@Callable
	public String toConsumptionRecordPage(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("AccountBalanceServiceImpl.toConsumptionRecordPage()");   
		this.getBalance(request, response);
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request); 
		AccountBalanceDetailMapper accountBalanceDetailMapper = this.sqlSession.getMapper(AccountBalanceDetailMapper.class); 
		Map<String, Object> condition = new LinkedHashMap<String, Object>();
		condition.put("userId",  loginInfo.getUserId());
		condition.put("type", "2"); 
		String all = accountBalanceDetailMapper.getAllAccount(condition);
		request.setAttribute("all", all==null?"0":all);
        if(loginInfo.getUserType()==4){			
    	   return "/security/user/consumptionlog.jsp" ;  
		}else{
			if( loginInfo.hasPrivilege(PrivilegeConstant.consumption_detail_agent) == false )
			{
				return "/public/have_not_access.jsp";
			}
			AgentMapper agentMapper = this.sqlSession.getMapper(AgentMapper.class);
			AgentVO agentVO = agentMapper.getAgentById(loginInfo.getUserId());
			request.setAttribute("agentVO", agentVO);
			
			return "/security/agent/consumptionlog.jsp" ;  
			 
		}
//	    return "/security/user/recharge_record_manage.jsp" ; 
	}
	@Callable
	public String toRechargePage(HttpServletRequest request,
			HttpServletResponse response) {
		logger.debug("AccountBalanceServiceImpl.toRechargePage()");   
//		LoginInfo loginInfo = LoginHelper.getLoginInfo(request, AppConstant.SYS_USER_TYPE_TERMINAL_USER);
//		TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class); 

//		TerminalUserVO terminalUserVO = terminalUserMapper.getBalanceById(loginInfo.getUserId()); 
//		if(terminalUserVO!=null){
//			 String balance =  terminalUserVO.getAccountBalance()+"";
//			
//		}
		this.getBalance(request, response);
	    request.setAttribute("amount", StringUtil.trim(request.getParameter("amount")));
		return "/security/user/recharge.jsp" ; 
	}
	@Callable
	public String toRechargeGiftPage(HttpServletRequest request,
			HttpServletResponse response) {
		logger.debug("AccountBalanceServiceImpl.toRechargeGiftPage()");   
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request, AppConstant.SYS_USER_TYPE_TERMINAL_USER);
		TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class); 
		
//		TerminalUserVO terminalUserVO = terminalUserMapper.getBalanceById(loginInfo.getUserId()); 
//		if(terminalUserVO!=null){
//			String balance =  terminalUserVO.getAccountBalance()+"";
//			request.setAttribute("balance", balance);
//			
//		}
		this.getBalance(request, response);
		CloudHostMapper  cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class); 

		List<CloudHostVO> cloudHostList = cloudHostMapper.getByUserId(loginInfo.getUserId());
		BigDecimal totalPrice = new BigDecimal("0");
		if(cloudHostList!=null&&cloudHostList.size()>0){
			for(CloudHostVO vo:cloudHostList){
				if(!(vo.getSummarizedStatusText().equals("创建失败")||vo.getSummarizedStatusText().equals("停机"))&&vo.getMonthlyPrice()!=null){						
					totalPrice = vo.getMonthlyPrice().add(totalPrice);
				}
			}
			request.setAttribute("monthlyPrice", totalPrice);
			
		}
		int month = Integer.parseInt(request.getParameter("month")); 
		request.setAttribute("month", month); 
		int month_pay = (month - (month/12)*2);  
		request.setAttribute("amount", totalPrice.multiply(new BigDecimal(""+month_pay)).setScale(2, BigDecimal.ROUND_HALF_UP)); 
		return "/security/user/recharge_for_gift.jsp" ; 
	}
	
	@Callable
	public void queryRechargeRecordForInvoice(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("AccountBalanceServiceImpl.queryRechargeRecordForInvoice()");
		try
		{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");
			
			// 参数处理
			String userId="";
			Integer page    = StringUtil.parseInteger(request.getParameter("page"), 1) - 1;	// 前台过来的是从1开始，需要的是从0开始
			Integer rows    = StringUtil.parseInteger(request.getParameter("rows"), 10);
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			if(loginInfo!=null){
				userId = loginInfo.getUserId();
			}
			// 查询数据库
			AccountBalanceDetailMapper accountBalanceDetailMapper = this.sqlSession.getMapper(AccountBalanceDetailMapper.class);  
			
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("userId",  userId);
//			condition.put("start_row", page * rows);
//			condition.put("row_count", rows);
			int total = accountBalanceDetailMapper.getBalanceDetailByUserIdCountForInvoice(condition);		// 总行数 
			List<AccountBalanceDetailVO> record = accountBalanceDetailMapper.getBalanceDetailByUserIdForInvoice(condition);	// 分页结果
			
			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, record);
		}
		catch (Exception e)
		{
			logger.error("AccountBalanceServiceImpl.queryRechargeRecordForInvoice()",e);
			throw new AppException("查询失败");
		}
	}
	
	@Callable
	public void queryRechargeRecordForInvoiceForAgent(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("AccountBalanceServiceImpl.queryRechargeRecordForInvoiceForAgent()");
		try
		{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");
			
			// 参数处理
			String userId="";
			Integer page    = StringUtil.parseInteger(request.getParameter("page"), 1) - 1;	// 前台过来的是从1开始，需要的是从0开始
			Integer rows    = StringUtil.parseInteger(request.getParameter("rows"), 10);
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request, AppConstant.SYS_USER_TYPE_AGENT);
			if(loginInfo!=null){
				userId = loginInfo.getUserId();
			}
			// 查询数据库
			AccountBalanceDetailMapper accountBalanceDetailMapper = this.sqlSession.getMapper(AccountBalanceDetailMapper.class);  
			
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("userId",  userId);
			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			int total = accountBalanceDetailMapper.getBalanceDetailByUserIdCountForInvoice(condition);		// 总行数 
			List<AccountBalanceDetailVO> record = accountBalanceDetailMapper.getBalanceDetailByUserIdForInvoiceForAgent(condition);	// 分页结果
			
			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, record);
		}
		catch (Exception e)
		{
			logger.error("AccountBalanceServiceImpl.queryRechargeRecordForInvoice()",e);
			throw new AppException("查询失败");
		}
	}
	
	@Callable
	public void getBalance(HttpServletRequest request,HttpServletResponse response) 
	{
		logger.debug("AccountBalanceServiceImpl.getBalance()");   
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request, AppConstant.SYS_USER_TYPE_TERMINAL_USER);
		try{
			
			if(loginInfo!=null){
				    
 					request.setAttribute("balance", new CountUserProductsPriceHelper(sqlSession).getRealBalance(loginInfo.getUserId())+"");
					
				} 
 		}catch(Exception e){
			logger.error("AccountBalanceServiceImpl.getBalance()", e);
			throw new AppException("获取信息失败"); 
		}
	}
	
	@Callable
	@Override
	public String queryRechargeDetailPage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("AccountBalanceServiceImpl.queryRechargeDetailPage()");
		String id = StringUtil.trim(request.getParameter("id"));
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_OPERATOR && loginInfo.hasPrivilege(PrivilegeConstant.bill_detail_page) == false )
		{
			return "/public/have_not_access.jsp";
		}else if(loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT && loginInfo.hasPrivilege(PrivilegeConstant.bill_detail_page_agent) == false){
			return "/public/have_not_access.jsp";
		}
		
		// 查询数据库
		AccountBalanceDetailMapper accountBalanceDetailMapper = this.sqlSession.getMapper(AccountBalanceDetailMapper.class);  
		
		Map<String, Object> condition = new LinkedHashMap<String, Object>();
		condition.put("id",  id); 
		condition.put("userId",  loginInfo.getUserId()); 
		List<AccountBalanceDetailVO> record = accountBalanceDetailMapper.getRechargeDetailById(condition);	// 分页结果
		if( record!=null&&record.size()>0 )
		{
			request.setAttribute("record", record.get(0));
			if(loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT){
				return "/security/agent/bill_detail.jsp";
			}else if(loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_TERMINAL_USER){
				return "/security/user/recharge_detail.jsp";
			}
			return "/security/operator/bill_detail.jsp";
		}
		else
		{
			request.setAttribute("message", "找不到账单项的记录");
			return "/public/warning_dialog.jsp";
		}
	}
	
	
	@Callable
	public List<AccountBalanceDetailVO> queryRechargeRecordForExport(HttpServletRequest request,HttpServletResponse response) {
		logger.debug("AccountBalanceServiceImpl.queryRechargeRecordForExport()");
		try
		{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");
			
			// 参数处理
			String userId="";
			String startTime = StringUtil.trim(request.getParameter("startTime"));
			String endTime = StringUtil.trim(request.getParameter("endTime"));
//			Integer page    = StringUtil.parseInteger(request.getParameter("page"), 1) - 1;	// 前台过来的是从1开始，需要的是从0开始
//			Integer rows    = StringUtil.parseInteger(request.getParameter("rows"), 10);
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			if(loginInfo!=null){
				userId = loginInfo.getUserId();
			}
			// 查询数据库
			AccountBalanceDetailMapper accountBalanceDetailMapper = this.sqlSession.getMapper(AccountBalanceDetailMapper.class);  
			
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("userId",  userId);
			condition.put("type", 1);
			if(endTime!=null&&endTime.length()>0){
				endTime = endTime.replace("-", "")+"240000000";
				condition.put("endTime",  endTime);
				
			}
			if(startTime!=null&&startTime.length()>0){
				startTime = startTime.replace("-", "")+"000000000";
				condition.put("startTime",  startTime);
				
			} 
			 
//			condition.put("start_row", page * rows);
//			condition.put("row_count", rows); 
			List<AccountBalanceDetailVO> record = accountBalanceDetailMapper.getRechargeDetailByUserId(condition);	
			return record;
			 
		}
		catch (Exception e)
		{
			logger.error("AccountBalanceServiceImpl.queryRechargeRecordForExport()",e);
			throw new AppException("查询失败");
		}
		
	}
	@Override
	public List<AccountBalanceDetailVO> queryConsumptionRecordForExport( HttpServletRequest request, HttpServletResponse response) {
		logger.debug("AccountBalanceServiceImpl.queryConsumptionRecordForExport()");
		try
		{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");
			
			// 参数处理
			String userId="";
			String startTime = StringUtil.trim(request.getParameter("startTime"));
			String endTime = StringUtil.trim(request.getParameter("endTime"));
			Integer page    = StringUtil.parseInteger(request.getParameter("page"), 1) - 1;	// 前台过来的是从1开始，需要的是从0开始
			Integer rows    = StringUtil.parseInteger(request.getParameter("rows"), 10);
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			if(loginInfo!=null){
				userId = loginInfo.getUserId();
			}
			// 查询数据库
			AccountBalanceDetailMapper accountBalanceDetailMapper = this.sqlSession.getMapper(AccountBalanceDetailMapper.class);  
			
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("userId",  userId);
			condition.put("type", 2);
			if(endTime!=null&&endTime.length()>0){
				endTime = endTime.replace("-", "")+"240000000";
				condition.put("endTime",  endTime);
				
			}
			if(startTime!=null&&startTime.length()>0){
				startTime = startTime.replace("-", "")+"000000000";
				condition.put("startTime",  startTime);
				
			} 
			 
			condition.put("start_row", page * rows);
			condition.put("row_count", rows); 
			List<AccountBalanceDetailVO> record = accountBalanceDetailMapper.getRechargeDetailByUserId(condition);	// 分页结果
			return record; 
		}
		catch (Exception e)
		{
			logger.error("AccountBalanceServiceImpl.queryConsumptionRecordForExport()",e);
			throw new AppException("查询失败");
		}
	}

}
