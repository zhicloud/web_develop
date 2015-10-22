package com.zhicloud.op.service.impl;

import java.math.BigDecimal;
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
import com.zhicloud.op.app.helper.LoginHelper;
import com.zhicloud.op.app.helper.ServiceHelper;
import com.zhicloud.op.app.propeties.AppProperties;
import com.zhicloud.op.common.util.SendMail;
import com.zhicloud.op.common.util.SendSms;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.common.util.json.JSONLibUtil;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.login.LoginInfo;
import com.zhicloud.op.mybatis.mapper.AccountBalanceDetailMapper;
import com.zhicloud.op.mybatis.mapper.CashCouponMapper;
import com.zhicloud.op.mybatis.mapper.MarkMapper;
import com.zhicloud.op.mybatis.mapper.OperLogMapper;
import com.zhicloud.op.mybatis.mapper.SysUserMapper;
import com.zhicloud.op.mybatis.mapper.TerminalUserMapper;
import com.zhicloud.op.mybatis.mapper.UserDictionaryMapper;
import com.zhicloud.op.mybatis.mapper.UserMessageMapper;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.request.RequestContext;
import com.zhicloud.op.service.CashCouponService;
import com.zhicloud.op.service.constant.AppConstant;
import com.zhicloud.op.vo.CashCouponVO;
import com.zhicloud.op.vo.MarkVO;
import com.zhicloud.op.vo.SysUserVO;
import com.zhicloud.op.vo.TerminalUserVO;

@Transactional(readOnly = true)
public class CashCouponServiceImpl extends BeanDirectCallableDefaultImpl implements CashCouponService 
{
	public static final Logger logger = Logger.getLogger(CashCouponServiceImpl.class);

	private SqlSession sqlSession;

	public void setSqlSession(SqlSession sqlSession)
	{
		this.sqlSession = sqlSession;
	} 
	
	@Callable
	public void queryCashCouponForOperator(HttpServletRequest request,HttpServletResponse response)
	{
		logger.debug("CashCouponServiceImpl.queryCashCouponForOperator()");
		try
		{
			// 权限判断
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			if(loginInfo.getUserType()==AppConstant.SYS_USER_TYPE_OPERATOR && loginInfo.hasPrivilege(PrivilegeConstant.operator_cash_coupon_manage_page) == false)
			{
				throw new AppException("您没有权限进行此操作");
			}
			
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");

			// 获取参数
//			String terminalUserAccount = StringUtil.trim(request.getParameter("terminal_user_account"));
			String status = StringUtil.trim(request.getParameter("cash_status"));
			String operator = StringUtil.trim(request.getParameter("cash_operator"));
			String username = StringUtil.trim(request.getParameter("username"));
			String markid = StringUtil.trim(request.getParameter("markid"));
			String create_time_from = StringUtil.trim(request.getParameter("create_time_from"));
			String create_time_to = StringUtil.trim(request.getParameter("create_time_to"));
			Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
			Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 2);
			
//			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
//			InviteCodeMapper inviteCodeMapper  = this.sqlSession.getMapper(InviteCodeMapper.class);
			CashCouponMapper cashCouponMapper  = this.sqlSession.getMapper(CashCouponMapper.class);
			if("0".equals(status)){
				status = null;
			}
			if("all".equals(operator)){
				operator = null;
			}
			// 查询数据库
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("createrId",      operator);
			condition.put("status", status);
            condition.put("username", "%" + username + "%");
            condition.put("create_time_from", create_time_from);
            condition.put("create_time_to", create_time_to);
            condition.put("markid", markid);
			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			
//			System.out.println("===="+terminalUserAccount+"=====");
			
//			int total                             = terminalUserMapper.queryPageCount(condition);	// 总行数
//			List<TerminalUserVO> terminalUserList = terminalUserMapper.queryPage(condition);		// 分页结果
			
			BigDecimal totalMoney             = cashCouponMapper.getTotalMoney(condition);	// 总金额
			int total                         = cashCouponMapper.queryPageCount(condition);	// 总行数
			List<CashCouponVO> cashCouponList = cashCouponMapper.queryPage(condition);		// 分页结果
			Map<String, Object> datagridResult = new LinkedHashMap<String, Object>();
			datagridResult.put("total", total);
			datagridResult.put("rows", cashCouponList);
			datagridResult.put("cashAmount",totalMoney==null?"0":totalMoney);
			// 输出json数据
			response.getWriter().write(JSONLibUtil.toJSONString(datagridResult));
//			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, cashCouponList);
		}
		catch( Exception e )
		{
			logger.error("CashCouponServiceImpl.queryCashCouponForOperator()",e);
			throw new AppException("查询失败");
		}
		
	}
	
	@Callable
	public String cashCouponPage(HttpServletRequest request,HttpServletResponse response)
	{
		logger.debug("CashCouponServiceImpl.addCashCouponPageByOperator()");
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request, 4);
		if( AppConstant.SYS_USER_TYPE_TERMINAL_USER.equals(loginInfo.getUserType())==false ){
			return "/public/have_not_access.jsp";
		}
		return "/security/user/coupon.jsp";
		
	}
	
	@Callable
	public String addCashCouponPageByOperator(HttpServletRequest request,HttpServletResponse response)
	{
		logger.debug("CashCouponServiceImpl.addCashCouponPageByOperator()");
		
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		
		// 权限判断，判断是否为运营商、是否有权限
		if( AppConstant.SYS_USER_TYPE_OPERATOR.equals(loginInfo.getUserType())==false || 
			loginInfo.hasPrivilege(PrivilegeConstant.operator_cash_coupon_add) == false )
		{
			return "/public/have_not_access_dialog.jsp";
		}
		
		return "/security/operator/operator_cash_coupon_add.jsp";
	}

	@Callable
	public String operatorCashCouponPage(HttpServletRequest request,HttpServletResponse response) 
	{
		logger.debug("CashCouponServiceImpl.operatorCashCouponPage()");
		
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		
		// 权限判断，判断是否为运营商、是否有权限
		if( AppConstant.SYS_USER_TYPE_OPERATOR.equals(loginInfo.getUserType())==false || 
			loginInfo.hasPrivilege(PrivilegeConstant.operator_cash_coupon_manage_page) == false )
		{
			return "/public/have_not_access.jsp";
		}
		CashCouponMapper cashCouponMapper = this.sqlSession.getMapper(CashCouponMapper.class);
		// 查询数据库
		Map<String, Object> condition = new LinkedHashMap<String, Object>();
		condition.put("createrId",null);
		condition.put("status", null);
		BigDecimal totalMoney =  cashCouponMapper.getTotalMoney(condition);
		SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
		List<SysUserVO> operList = sysUserMapper.getAllOperator();
		request.setAttribute("operList", operList);
		request.getSession().setAttribute("totalMoney", totalMoney);
        MarkMapper markMapper = this.sqlSession.getMapper(MarkMapper.class);
        List<MarkVO> markList = markMapper.getAll();
        request.setAttribute("markList", markList);	
		return "/security/operator/operator_cash_coupon.jsp";
	}

	@Callable
	public String sendCashCouponByPhonePage(HttpServletRequest request,HttpServletResponse response) 
	{
		logger.debug("CashCouponServiceImpl.sendCashCouponByPhonePage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_OPERATOR && loginInfo.hasPrivilege(PrivilegeConstant.operator_cash_coupon_send_phone) == false )
		{
			return "/public/have_not_access_dialog.jsp";
		}
		
		// 参数处理
		String cashCouponId = StringUtil.trim(request.getParameter("cashCouponId"));
		if( StringUtil.isBlank(cashCouponId) )
		{
			throw new AppException("cashCouponId不能为空");
		}
		//
		CashCouponMapper cashCouponMapper =this.sqlSession.getMapper(CashCouponMapper.class);
		CashCouponVO cashCouponVO = cashCouponMapper.getCashCouponById(cashCouponId);
		
			if( cashCouponVO != null)
			{
				if (AppConstant.CASH_COUPON_SEND_NO== cashCouponVO.getStatus()) 
				{
					request.setAttribute("cashCouponVO", cashCouponVO);
					return "/security/operator/operator_cash_coupon_send_phone.jsp";
				}
				else
				{
					request.setAttribute("message", "该代金券不可用");
					return "/public/warning_dialog.jsp";
				}
				
			}
			else
			{
				request.setAttribute("message", "找不到代金券的记录");
				return "/public/warning_dialog.jsp";
			}
		
	}

	@Callable
	@Transactional(readOnly = false)
	public MethodResult sendCashCouponByPhone(Map<String, String> parameter) 
	{
		logger.debug("CashCouponServiceImpl.sendCashCouponByPhone()");
		try {
//			HttpServletRequest request = RequestContext.getHttpRequest();
			
			String id                 = StringUtil.trim(parameter.get("cashCouponId"));
			String  cashCouponMoney   = StringUtil.trim(parameter.get("cashCouponMoney"));
			String cashCouponCode     = StringUtil.trim(parameter.get("cashCouponCode"));
			String phone              = StringUtil.trim(parameter.get("terminal_phone"));
			
//			System.out.println("---------"+inviteCodeId+"------"+phone+"---------"+inviteCode+"-----");
			
			if (id.isEmpty()) 
			{
				return new MethodResult(MethodResult.FAIL, "代金券id不能为空");
			}
			if (cashCouponMoney.isEmpty()) 
			{
				return new MethodResult(MethodResult.FAIL,"现金券价值不能为空");
			}
			if (cashCouponCode.isEmpty())
			{
				return new MethodResult(MethodResult.FAIL,"现金券不能为空");
			}
			if (phone.isEmpty()) 
			{
				return new MethodResult(MethodResult.FAIL, "手机号码不能为空");
			}
			
			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
			//判断手机是否绑定
			TerminalUserVO terminalUserVO = terminalUserMapper.getTerminalUserByPhone(phone);
			if (terminalUserVO == null) 
			{
				return new MethodResult(MethodResult.FAIL,"该手机未绑定云端在线账号");
			}
			
			//发送短信
		    String message = new String("【致云科技】亲爱的用户，您好。致云Zhicloud云服务现金券兑换码："+cashCouponCode+"，价值"+cashCouponMoney+"元，请及时兑换使用，谢谢。"); 
			String state  = new SendSms().zhicloudSendSms(phone,message);
			
			if (("1").equals(state)) 
			{
				// 更改数据库
				CashCouponMapper cashCouponMapper = this.sqlSession.getMapper(CashCouponMapper.class); 
				Map<String, Object> cashCouponMap = new LinkedHashMap<String, Object>();
				cashCouponMap.put("id",       id);
				cashCouponMap.put("sendTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS") );
				cashCouponMap.put("status",   AppConstant.CASH_COUPON_SEND_YES);
				cashCouponMap.put("sendAddress",  phone);
				cashCouponMapper.updateSendTimeAndStatus(cashCouponMap);
				return new MethodResult(MethodResult.SUCCESS, "短信已成功发送");
			}
			else
			{
				System.out.println("短信发送失败，错误代码是："+state);
				return new MethodResult(MethodResult.FAIL, "发送失败");
			}
		} catch (Exception e) 
		{
			logger.error("CashCouponServiceImpl.sendCashCouponByPhone()",e);
			throw new AppException("发送失败");
		}
	}

	@Callable
	public String sendCashCouponByEmailPage(HttpServletRequest request,HttpServletResponse response) 
	{
		logger.debug("CashCouponServiceImpl.sendCashCouponByEmailPage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_OPERATOR && loginInfo.hasPrivilege(PrivilegeConstant.operator_cash_coupon_send_email) == false )
		{
			return "/public/have_not_access_dialog.jsp";
		}
		// 参数处理
		String cashCouponId = StringUtil.trim(request.getParameter("cashCouponId"));
		if( StringUtil.isBlank(cashCouponId) )
		{
			throw new AppException("inviteCodeId不能为空");
		}
		
		//获取相关
		CashCouponMapper cashCouponMapper = this.sqlSession.getMapper(CashCouponMapper.class);
		CashCouponVO cashCouponVO =cashCouponMapper.getCashCouponById(cashCouponId);
		System.out.println(cashCouponVO);
			if( cashCouponVO != null)
			{	if ((AppConstant.CASH_COUPON_SEND_NO==cashCouponVO.getStatus())) 
				{
					request.setAttribute("cashCouponVO", cashCouponVO);
					return "/security/operator/operator_cash_coupon_send_email.jsp";
				}
				else 
				{
					request.setAttribute("message", "该不可用");
					return "/public/warning_dialog.jsp";
	
				}
			}
			else
			{
				request.setAttribute("message", "找不到代金券记录");
				return "/public/warning_dialog.jsp";
			}
		
		}
	

	@Callable
	@Transactional(readOnly = false)
	public MethodResult sendCashCouponByEmail(Map<String, String> parameter) 
	{
		logger.debug("CashCouponServiceImpl.sendCashCouponByEmail()");
		try {
//			HttpServletRequest request = RequestContext.getHttpRequest();
			
			String id           = StringUtil.trim(parameter.get("cashCouponId"));
			String money        = StringUtil.trim(parameter.get("cashCouponMoney"));
			String cashCouponCode   = StringUtil.trim(parameter.get("cashCouponCode"));
			String email        = StringUtil.trim(parameter.get("terminal_email"));
			
//			System.out.println("---------"+inviteCodeId+"------"+email+"---------"+inviteCode+"-----");
			
			if (id.isEmpty()) 
			{
				return new MethodResult(MethodResult.FAIL, "现金券id不能为空");
			}
			if (money.isEmpty())
			{
				return new MethodResult(MethodResult.FAIL,"现金券价值不能为空");
			}
			if (cashCouponCode.isEmpty()) 
			{
				return new MethodResult(MethodResult.FAIL, "现金券码不能为空");
			}
			if (email.isEmpty()) 
			{
				return new MethodResult(MethodResult.FAIL,"电子邮件不能为空");
			}
			//判断手机是否绑定
			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
			TerminalUserVO terminalUserVO = terminalUserMapper.getTerminalUserByEmail(email);
			if (terminalUserVO ==null) 
			{
				return new MethodResult(MethodResult.FAIL,"该邮箱未绑定云端在线账号");
			}
			
			String content = "<body>"
					         + "尊敬的用户"
					         + ":"
					         + "<br>"
					         + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您好！<br>"
					         + "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;致云Zhicloud云服务现金券兑换码："+cashCouponCode+"，价值"+money+"元，请及时登录zhicloud.com兑换使用，谢谢。"
					         + "<br>"
					         + "<br>致云科技感谢您的支持！"
					         + "</body>";
			//发送邮件
			Map<String, Object> terminalUserData = new LinkedHashMap<String, Object>();
			terminalUserData.put("email", email);
			terminalUserData.put("content", content);	
			new SendMail().sendCouponToTerminalUser(terminalUserData);
			// 更改数据库
			CashCouponMapper cashCouponMapper = this.sqlSession.getMapper(CashCouponMapper.class); 
			Map<String, Object> cashCouponMap = new LinkedHashMap<String, Object>();
			cashCouponMap.put("id",       id);
			cashCouponMap.put("sendTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS") );
			cashCouponMap.put("status",   AppConstant.CASH_COUPON_SEND_YES);
			cashCouponMap.put("sendAddress",  email);
			cashCouponMapper.updateSendTimeAndStatus(cashCouponMap);
			return new MethodResult(MethodResult.SUCCESS, "邮件已成功发送");
			
		} catch (Exception e) 
		{
			logger.error("CashCouponServiceImpl.sendCashCouponByEmail()",e);
			throw new AppException("发送失败");
		}
	}
	
	@Callable
	@Transactional(readOnly = false)
	public MethodResult addCashCoupon(Map<String, String> parameter)
	{
		logger.debug("CashCouponServiceImpl.addCashCoupon()");
		try
		{
			HttpServletRequest request = RequestContext.getHttpRequest();
			
			// 权限判断
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			
			if (loginInfo.getUserType()==AppConstant.SYS_USER_TYPE_OPERATOR&&loginInfo.hasPrivilege(PrivilegeConstant.operator_cash_coupon_add) == false) 
			{
				return new MethodResult(MethodResult.FAIL, "您没有权限进行此操作");
			}
			//获取参数
			
			String  cashConponNumber = StringUtil.trim(parameter.get("code_number"));
			String  cashValue = StringUtil.trim(parameter.get("cash_value"));
			if (cashConponNumber.isEmpty()) 
			{
				return new MethodResult(MethodResult.FAIL,"添加代金券张数不能为空");
			}
			if (cashValue.isEmpty())
			{
				return new MethodResult(MethodResult.FAIL,"每张代金券价值不能为空");
			}
			BigDecimal cashV = new BigDecimal(cashValue);
			BigDecimal cashUpper = new BigDecimal(AppProperties.getValue("cash_upper_limit", ""));
			if (cashV.compareTo(cashUpper)==1)
			{
				return new MethodResult(MethodResult.FAIL,"每张代金券价值不能大于"+cashUpper);
			}
			//批量创建
			int n = Integer.parseInt(cashConponNumber);
			for (int i = 0; i < n; i++) {
				String id       = StringUtil.generateUUID();
				String creatId  = loginInfo.getUserId();
				String cashCode = StringUtil.generateUUID().toUpperCase();
				CashCouponMapper cashCouponMapper = this.sqlSession.getMapper(CashCouponMapper.class); 
				// 添加进数据库
				Map<String, Object> cashCoupon = new LinkedHashMap<String, Object>();
				cashCoupon.put("id",         id);
				cashCoupon.put("createrId",  creatId);
				cashCoupon.put("cashCode",   cashCode);
				cashCoupon.put("money",      cashValue);
				cashCoupon.put("createTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS") );
				cashCoupon.put("status",     AppConstant.CASH_COUPON_SEND_NO);
				cashCouponMapper.addCashCoupon(cashCoupon);
				
			}
			
			return new MethodResult(MethodResult.SUCCESS,"添加成功");
			
		}
		catch( Exception e )
		{
			logger.error("CashCouponServiceImpl.addCashCoupon()",e);
			throw new AppException("添加失败");
		}
	}


	@Callable
	@Transactional(readOnly = false)
	public MethodResult deleteCashCouponByIds(List<String> cashCouponIds)
	{
		logger.debug("CashCouponServiceImpl.deleteCashCouponByIds()");
		try
		{
			HttpServletRequest request = RequestContext.getHttpRequest();
			
			// 权限判断
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			if( loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_OPERATOR && loginInfo.hasPrivilege(PrivilegeConstant.operator_cash_coupon_delete) == false )
			{
				throw new AppException("您没有权限进行此操作");
			}
			
			
			// 参数处理
			if( cashCouponIds == null || cashCouponIds.size() == 0 )
			{
				return new MethodResult(MethodResult.FAIL,"选择删除项");
			}

			CashCouponMapper cashCouponMapper = this.sqlSession.getMapper(CashCouponMapper.class); 
			int n = cashCouponMapper.deleteCashCouponByIds(cashCouponIds);
			
			if( n >0  )
			{
				return new MethodResult(MethodResult.SUCCESS, "删除成功");
			}
			else
			{
				return new MethodResult(MethodResult.FAIL, "删除失败");
			}
		}
		catch( Exception e )
		{
			logger.error("CashCouponServiceImpl.deleteCashCouponByIds()",e);
			throw new AppException("删除失败");
		}
	}
	
	@Callable
	@Transactional(readOnly = false)
	public MethodResult convertCashCoupon(Map<String, String> parameter)
	{
		logger.debug("CashCouponServiceImpl.addCashCoupon()");
		Long begin = System.currentTimeMillis();
		Integer logStatus = AppConstant.OPER_LOG_FAIL;
		HttpServletRequest request = RequestContext.getHttpRequest();
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		String now = StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS");
 		String content = "";
		try
		{
  
			//获取参数
			String  cashCode = StringUtil.trim(parameter.get("cashCouponCode"));
			if (cashCode.isEmpty()) 
			{
				return new MethodResult(MethodResult.FAIL,"请输入现金券码");
			}
			//获取代金券
			CashCouponMapper cashCouponMapper = this.sqlSession.getMapper(CashCouponMapper.class); 
			CashCouponVO cashCouponVO = cashCouponMapper.getCashCouponByCode(cashCode);
			content = "通过现金券兑换"+cashCouponVO.getMoney()+"元";
			if (cashCouponVO==null) 
			{
				return new MethodResult(MethodResult.FAIL,"无效的现金卷");
			}
			//更新相关数据库
			AccountBalanceDetailMapper accountBalanceDetailMapper = this.sqlSession.getMapper(AccountBalanceDetailMapper.class);  
			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class); 
 	  		int n = 0;
			BigDecimal balance_before_change =  new BigDecimal("0");
			BigDecimal balance_after_change = new BigDecimal("0"); 
				
			TerminalUserVO terminalUserVO = terminalUserMapper.getBalanceById(loginInfo.getUserId());
			if(terminalUserVO!=null)
			{
				balance_before_change =  terminalUserVO.getAccountBalance();
			}
			
			balance_after_change = balance_before_change.add(cashCouponVO.getMoney());
			Map<String, Object> balanceDetailData = new LinkedHashMap<String, Object>(); 
			 
			if(cashCouponVO.getMoney().compareTo(BigDecimal.ZERO)==1){ 
	 			balanceDetailData = new LinkedHashMap<String, Object>(); 
				balanceDetailData.put("id",     StringUtil.generateUUID()); 
				balanceDetailData.put("balanceAfterChange",     balance_after_change);
				balanceDetailData.put("balanceBeforeChange",     balance_before_change);
				balanceDetailData.put("amount",     cashCouponVO.getMoney());  
				balanceDetailData.put("userId",     loginInfo.getUserId());  
				balanceDetailData.put("rechargeStatus",     "2");  
				balanceDetailData.put("type",     "1");  
				balanceDetailData.put("payType",     "5");  
				balanceDetailData.put("changeTime",     now); 
				balanceDetailData.put("description",     "兑换现金券");  
				int k = accountBalanceDetailMapper.addAccountBalanceDetail(balanceDetailData);
				if(k<=0){
					
					throw new AppException("兑换失败");
				}
			}
			Map<String, Object> balanceData = new LinkedHashMap<String, Object>(); 
			balanceData.put("id",     loginInfo.getUserId()); 
			balanceData.put("accountBalance",     balance_after_change);
			balanceData.put("balanceUpdateTime",     now); 
			n = terminalUserMapper.updateBalanceById(balanceData); 
			if(n<=0){
				
				throw new AppException("兑换失败");
			}
			
			//余额为正，删除扣费通知
			if(balance_after_change.compareTo(BigDecimal.ZERO)>0){
				UserMessageMapper userMessageMapper = this.sqlSession.getMapper(UserMessageMapper.class);
				Map<String, Object> userData = new LinkedHashMap<String, Object>(); 
				userData.put("userId",     loginInfo.getUserId()); 
				userData.put("status",     2); 
				userMessageMapper.deleteUserMessageByStatus(userData);
				
			}
			
			
			
			//更新使用情况
			Map<String, Object> cashCoupon = new LinkedHashMap<String, Object>();
			cashCoupon.put("id",         cashCouponVO.getId());
			cashCoupon.put("userId",     loginInfo.getUserId());
			cashCoupon.put("phone",      loginInfo.getPhone());
			cashCoupon.put("email",      loginInfo.getAccount());
			cashCoupon.put("status",     AppConstant.CASH_COUPON_SEND_USED);
			
			int m = cashCouponMapper.updateStatusOther(cashCoupon);
			if (m<=0) 
			{
				throw new AppException("兑换失败");
			}
			//删除零元通知记录
			UserDictionaryMapper userDictionaryMapper = this.sqlSession.getMapper(UserDictionaryMapper.class);
			Map<String, Object> userDictionaryData = new LinkedHashMap<String, Object>();
			userDictionaryData.put("userId", loginInfo.getUserId());
			userDictionaryData.put("key", "sendTimes");
			userDictionaryMapper.deleteDictionaryByUserId(userDictionaryData);
			logStatus = AppConstant.OPER_LOG_SUCCESS;
			return new MethodResult(MethodResult.SUCCESS,"您已成功兑换"+cashCouponVO.getMoney()+"元，致云科技感谢您的支持。");
			
		}
		catch( Exception e )
		{
			logger.error("CashCouponServiceImpl.convertCashCoupon()",e);
			throw new AppException("兑换失败");
		}finally {
			try {
				OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
				Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
				operLogData.put("id", StringUtil.generateUUID());
				operLogData.put("userId", loginInfo.getUserId());
				operLogData.put("content", content);
				operLogData.put("operTime", now);
				operLogData.put("status", logStatus);
				operLogData.put("resourceName", "兑换现金券");
				operLogData.put("operDuration", System.currentTimeMillis()-begin);
				operLogMapper.addOperLog(operLogData);
 			} catch (Exception e) {
				logger.error(e);
			}

		}
		
	}

    @Callable
    public List<CashCouponVO> queryCashCouponForExport(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("CashCouponServiceImpl.queryCashCouponForExport()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");

            // 获取参数
            String status = StringUtil.trim(request.getParameter("cash_status"));
            String operator = StringUtil.trim(request.getParameter("cash_operator"));
            String username = StringUtil.trim(request.getParameter("username"));
            String markid = StringUtil.trim(request.getParameter("markid"));
            String create_time_from = StringUtil.trim(request.getParameter("create_time_from"));
            String create_time_to = StringUtil.trim(request.getParameter("create_time_to"));
            CashCouponMapper cashCouponMapper = this.sqlSession.getMapper(CashCouponMapper.class);
            if ("0".equals(status)) {
                status = null;
            }
            if ("all".equals(operator)) {
                operator = null;
            }
            // 查询数据库
            Map<String, Object> condition = new LinkedHashMap<String, Object>();
            condition.put("createrId", operator);
            condition.put("status", status);
            condition.put("username", "%" + username + "%");
            condition.put("create_time_from", create_time_from);
            condition.put("create_time_to", create_time_to);
            condition.put("markid", markid);
            int total = cashCouponMapper.queryPageCount(condition); // 总行数
            condition.put("start_row", 0);
            condition.put("row_count", total);
            List<CashCouponVO> cashCouponList = cashCouponMapper.queryPage(condition); // 分页结果
            return cashCouponList;
        } catch (Exception e) {
            logger.error("CashCouponServiceImpl.queryCashCouponForExport()", e);
            throw new AppException("查询失败");
        }

    }
}
