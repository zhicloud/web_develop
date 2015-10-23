package com.zhicloud.op.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.op.app.authorization.PrivilegeConstant;
import com.zhicloud.op.app.helper.CloudHostBillingHelper;
import com.zhicloud.op.app.helper.LoginHelper;
import com.zhicloud.op.app.helper.ServiceHelper;
import com.zhicloud.op.app.helper.VerificationCodeHelper;
import com.zhicloud.op.app.pool.CloudHostPoolManager;
import com.zhicloud.op.common.util.DateUtil;
import com.zhicloud.op.common.util.DesActivate;
import com.zhicloud.op.common.util.NumberUtil;
import com.zhicloud.op.common.util.RandomPassword;
import com.zhicloud.op.common.util.SendMail;
import com.zhicloud.op.common.util.SendSms;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.common.util.constant.MailConstant;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.httpGateway.HttpGatewayChannelExt;
import com.zhicloud.op.httpGateway.HttpGatewayManager;
import com.zhicloud.op.httpGateway.HttpGatewayResponseHelper;
import com.zhicloud.op.login.LoginInfo;
import com.zhicloud.op.mybatis.mapper.AccountBalanceDetailMapper;
import com.zhicloud.op.mybatis.mapper.CloudHostBillDetailMapper;
import com.zhicloud.op.mybatis.mapper.CloudHostMapper;
import com.zhicloud.op.mybatis.mapper.InviteCodeMapper;
import com.zhicloud.op.mybatis.mapper.OperLogMapper;
import com.zhicloud.op.mybatis.mapper.SysGroupMapper;
import com.zhicloud.op.mybatis.mapper.SysUserMapper;
import com.zhicloud.op.mybatis.mapper.TerminalUserMapper;
import com.zhicloud.op.mybatis.mapper.UserDictionaryMapper;
import com.zhicloud.op.mybatis.mapper.UserMessageMapper;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.CallWithoutLogin;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.request.RequestContext;
import com.zhicloud.op.service.TerminalUserService;
import com.zhicloud.op.service.constant.AppConstant;
import com.zhicloud.op.vo.CloudHostBillDetailVO;
import com.zhicloud.op.vo.CloudHostVO;
import com.zhicloud.op.vo.SysGroupVO;
import com.zhicloud.op.vo.SysUserVO;
import com.zhicloud.op.vo.TerminalUserVO;
import com.zhicloud.op.vo.UserMessageVO;

@Transactional(readOnly = true)
public class TerminalUserServiceImpl extends BeanDirectCallableDefaultImpl implements TerminalUserService
{
	public static final Logger logger = Logger.getLogger(TerminalUserServiceImpl.class);

	private SqlSession sqlSession;
	
//	Map<String, Object> ipMap = new LinkedHashMap<String, Object>();
	
	public void setSqlSession(SqlSession sqlSession)
	{
		this.sqlSession = sqlSession;
	}
	
	//--------------------------

	@Callable
	public String managePageForOperator(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("TerminalUserServiceImpl.managePage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.hasPrivilege(PrivilegeConstant.terminal_user_manage_page_for_operator) == false )
		{
			return "/public/have_not_access.jsp";
		}
		return "/security/operator/terminal_user_manage.jsp";
	}
	
	@Callable
	public String managePageForAgent(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("TerminalUserServiceImpl.managePageForAgent()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.hasPrivilege(PrivilegeConstant.terminal_user_manage_page_for_agent) == false)
		{
			return "/public/have_not_access.jsp";
		}
		return "/security/agent/terminal_user_manage.jsp";
	}
	
	@Callable
	public String addPage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("TerminalUserServiceImpl.addPage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		int userType = loginInfo.getUserType();
		if( loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_OPERATOR && loginInfo.hasPrivilege(PrivilegeConstant.terminal_user_manage_add) == false )
		{
			return "/public/have_not_access_dialog.jsp";
		}else if(loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT && loginInfo.hasPrivilege(PrivilegeConstant.terminal_user_manage_add_agent) == false){
			return "/public/have_not_access_dialog.jsp";
		}
		
		SysGroupMapper sysGroupMapper = this.sqlSession.getMapper(SysGroupMapper.class);
		List<SysGroupVO> sysGroupList = sysGroupMapper.getAll();
		request.setAttribute("sysGroupList", sysGroupList);
		
		//判断用户类型，然后跳转到相应页面
		if(userType == AppConstant.SYS_USER_TYPE_AGENT){
			return "/security/agent/terminal_user_add.jsp";
		}else{
			return "/security/operator/terminal_user_add.jsp";
		}
		
	}

	@Callable
	public String modPage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("TerminalUserServiceImpl.modPage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_OPERATOR && loginInfo.hasPrivilege(PrivilegeConstant.terminal_user_manage_modify) == false )
		{
			return "/public/have_not_access_dialog.jsp";
		}else if(loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT && loginInfo.hasPrivilege(PrivilegeConstant.terminal_user_manage_modify_agent) == false){
			return "/public/have_not_access_dialog.jsp";
		}
		
		// 参数处理
		String terminalUserId = StringUtil.trim(request.getParameter("terminalUserId"));
		if( StringUtil.isBlank(terminalUserId) )
		{
			throw new AppException("terminalUserId不能为空");
		}
		
		//
		SysGroupMapper sysGroupMapper = this.sqlSession.getMapper(SysGroupMapper.class);
		TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
		
		List<SysGroupVO> sysGroupList = sysGroupMapper.getAll();
		request.setAttribute("sysGroupList", sysGroupList);
		
		TerminalUserVO terminalUser = terminalUserMapper.getTerminalUserById(terminalUserId);
		if(loginInfo.getUserType()==AppConstant.SYS_USER_TYPE_OPERATOR){
			if( terminalUser != null)
			{
				request.setAttribute("terminalUser", terminalUser);
 				return "/security/operator/terminal_user_mod.jsp";
			}
			else
			{
				request.setAttribute("message", "找不到终端用户的记录");
				return "/public/warning_dialog.jsp";
			}
		}else{
			if( terminalUser != null)
			{
				request.setAttribute("terminalUser", terminalUser);
				return "/security/agent/terminal_user_mod.jsp";
			}
			else
			{
				request.setAttribute("message", "找不到终端用户的记录");
				return "/public/warning_dialog.jsp";
			}
		}
	}
	
	
	@Callable
	public String resetPasswordPage(HttpServletRequest request,HttpServletResponse response)
	{
		logger.debug("TerminalUserServiceImpl.resetPasswordPage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.hasPrivilege(PrivilegeConstant.operator_terminal_user_reset_password) == false )
		{
			return "/public/have_not_access_dialog.jsp";
		}
		//参数处理
		String terminalUserId = StringUtil.trim(request.getParameter("terminalUserId"));
		TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
		TerminalUserVO terminalUserVO         = terminalUserMapper.getBaseInfoById(terminalUserId);
		if(terminalUserVO.getEmail()==null){
			return "/public/have_not_email.jsp";
		}
		String resetPassword                  = RandomPassword.getRandomNum(MailConstant.PASSWORD_LENGTH);
		request.setAttribute("resetPassword", resetPassword);
		request.setAttribute("terminalUserVO", terminalUserVO);
		
		return "/security/operator/terminal_user_reset_password.jsp";
		
	}
	
	@Callable
	public String terminalUserChangeEmail(HttpServletRequest request,HttpServletResponse response)
	{
		logger.debug("TerminalUserServiceImpl.terminalUserChangeEmail()");
		
		
		//参数处理
		String terminalUserId = StringUtil.trim(request.getParameter("terminalUserId"));
		TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
		TerminalUserVO terminalUserVO         = terminalUserMapper.getBaseInfoById(terminalUserId);
		String resetPassword                  = RandomPassword.getRandomNum(MailConstant.PASSWORD_LENGTH);
		request.setAttribute("resetPassword", resetPassword);
		request.setAttribute("terminalUserVO", terminalUserVO);
		
		return "/security/operator/terminal_user_reset_password.jsp";
		
	}
	
	
	@Callable
	public String setStrToResetPasswordPage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("TerminalUserServiceImpl.setStrToRestPasswordPage()");
		 String str = request.getParameter("str");
		 try {
			 DesActivate des = new DesActivate(MailConstant.ENCRYPTION_KEY);
			 String strId = des.decrypt(str);
			 request.setAttribute("str", strId);
			 
		} catch (Exception e) {
		}
		 
		
		return "/public/user/reset_password.jsp";
	}
	
	
	@Callable
	public String changePasswordPage(HttpServletRequest request,HttpServletResponse response) 
	{
		logger.debug("TerminalUserServiceImpl.changePasswordPage");
		
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request, 4);
		String id = loginInfo.getUserId();
		
//		TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
//		TerminalUserVO terminalUser = terminalUserMapper.getBaseInfoById(id);
//		if(terminalUser==null){
//			request.setAttribute("notexsit", "yes");
//			request.setAttribute("message", "账户不存在，请和管理员联系！");
//			return "/public/warning_dialog.jsp";
//		}
		//权限判断
		if( AppConstant.SYS_USER_TYPE_TERMINAL_USER.equals(loginInfo.getUserType())==false )
		{
			return "/public/have_not_access.jsp";
		}
		else
		{
//			request.setAttribute("terminalUser", terminalUser);
			return "/security/user/changepassword.jsp";
//			return "/security/user/terminal_user_change_password.jsp
		}
	}

	@Callable
	public String baseInfoPageAccountEdit(HttpServletRequest request,HttpServletResponse response) 
	{
		logger.debug("TerminalUserServiceImpl.baseInfoPageAccountEdit()");
		
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request, 4);
		String id = loginInfo.getUserId();
		
		TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
		TerminalUserVO terminalUser = terminalUserMapper.getBaseInfoById(id);
		
		if(terminalUser==null){
			request.setAttribute("notexsit", "yes");
			request.setAttribute("message", "账户不存在，请和管理员联系！");
			return "/public/warning_dialog.jsp";
		}
		
		//权限判断
		if( AppConstant.SYS_USER_TYPE_TERMINAL_USER.equals(loginInfo.getUserType())==false )
		{
			return "/public/have_not_access.jsp";
		}
		else
		{
			request.setAttribute("terminalUser", terminalUser);
			return "/security/user/terminaluser_baseinfo_accountedit.jsp";
		}
	}
	

	@Callable
	public String baseInfoPageEmailEdit(HttpServletRequest request,HttpServletResponse response) 
	{
		logger.debug("TerminalUserServiceImpl.baseInfoPageEmailEdit()");
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request, AppConstant.SYS_USER_TYPE_TERMINAL_USER);
		String id = loginInfo.getUserId();
		TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
		TerminalUserVO terminalUser = terminalUserMapper.getBaseInfoById(id);
		if(terminalUser==null){
			request.setAttribute("notexsit", "yes");
			request.setAttribute("message", "账户不存在，请和管理员联系！");
			return "/public/warning_dialog.jsp";
		}
		
		//权限判断
		if( AppConstant.SYS_USER_TYPE_TERMINAL_USER.equals(loginInfo.getUserType())==false )
		{
			return "/public/have_not_access.jsp";
		}
		else
		{
			request.setAttribute("terminalUser", terminalUser);
//			request.setAttribute("terminalUserVO", terminalUserVO);
			return "/security/user/changeemail.jsp";
//			return "/security/user/terminaluser_baseinfo_emailedit.jsp";
		}
	}
	
	@Callable
	public String baseInfoPagePhoneEdit(HttpServletRequest request,HttpServletResponse response) 
	{
		logger.debug("TerminalUserServiceImpl.baseInfoPagePhoneEdit()");
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request, AppConstant.SYS_USER_TYPE_TERMINAL_USER);
		String id = loginInfo.getUserId();
		TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
		TerminalUserVO terminalUser = terminalUserMapper.getBaseInfoById(id);
		if(terminalUser==null){
			request.setAttribute("notexsit", "yes");
			request.setAttribute("message", "账户不存在，请和管理员联系！");
			return "/public/warning_dialog.jsp";
		}
		
		//权限判断
		if( AppConstant.SYS_USER_TYPE_TERMINAL_USER.equals(loginInfo.getUserType())==false )
		{
			return "/public/have_not_access.jsp";
		}
		else
		{
			request.setAttribute("terminalUser", terminalUser);
			return "/security/user/changemobile.jsp";
//			return "/security/user/terminaluser_baseinfo_phonedit.jsp";
		}
	}
	
	
	@Callable
	@Transactional(readOnly = false)
	public String activated(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("TerminalUserServiceImpl.activated()");
		
		String str = request.getParameter("str");
		DesActivate des = null;
		String id=null;
		try {
			des = new DesActivate(MailConstant.ENCRYPTION_KEY);
			id = des.decrypt(str);
			//判断第二次激活
			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
			TerminalUserVO terminalUserVO = terminalUserMapper.getTerminalUserById(id);	
			if (terminalUserVO.getEmailVerified()==AppConstant.TERMINAL_USER_EMAIL_VERIFIED_YES && terminalUserVO.getStatus()==AppConstant.TERMINAL_USER_STATUS_NORMAL)
			{
				return "/public/user/activated.jsp";
			}
			
			Map<String, Object> terminalUserData = new LinkedHashMap<String, Object>();
			terminalUserData.put("id",            id);
			terminalUserData.put("emailVerified", AppConstant.TERMINAL_USER_EMAIL_VERIFIED_YES);
			terminalUserData.put("status",        AppConstant.TERMINAL_USER_STATUS_NORMAL);
			
			int m = terminalUserMapper.activaTerminalUserByStr(terminalUserData);
			if (m>0) {
				return "/public/user/activated.jsp";
			}
			else{
				return "/public/have_not_access.jsp";
			}
		} 
		catch (Exception e) {
			logger.error(e);
			throw new AppException("失败");
		}
		
	}

	@Callable
	public void queryTerminalUserForOperator(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("TerminalUserServiceImpl.queryTerminalUserForOperator()");
		try
		{
			// 权限判断
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			if( loginInfo.hasPrivilege(PrivilegeConstant.terminal_user_manage_page_for_operator) == false )
			{
				throw new AppException("您没有权限进行此操作");
			}
			
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");

			// 获取参数
			String terminalUserAccount = StringUtil.trim(request.getParameter("terminal_user_account"));
			Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
			Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 2);
			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);

			// 查询数据库
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("terminalUserAccount", "%" + terminalUserAccount + "%");
			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			
//			System.out.println("---"+terminalUserAccount+"--");
			
			int total                             = terminalUserMapper.queryPageCount(condition); // 总行数
			List<TerminalUserVO> terminalUserList = terminalUserMapper.queryPage(condition);// 分页结果

			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, terminalUserList);
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("查询失败");
		}
	}
	
	@Callable
	public void queryTerminalUserForAgent(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("TerminalUserServiceImpl.queryTerminalUserForAgent()");
		try
		{
			// 权限判断
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			if( loginInfo.hasPrivilege(PrivilegeConstant.terminal_user_manage_page_for_agent) == false)
			{
				throw new AppException("您没有权限进行此操作");
			}
			
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");

			// 获取参数
			String terminalUserAccount = StringUtil.trim(request.getParameter("terminal_user_account"));
			Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
			Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 2);
			
			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
			CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
			CloudHostBillDetailMapper cloudHostBillDetailMapper = this.sqlSession.getMapper(CloudHostBillDetailMapper.class);
			
			// 查询数据库
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("belongingId",      loginInfo.getUserId());
			condition.put("terminalUserAccount", "%" + terminalUserAccount + "%");
			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			
//			System.out.println("===="+terminalUserAccount+"=====");
			
			int total                             = terminalUserMapper.queryPageCount(condition);	// 总行数
			List<TerminalUserVO> terminalUserList = terminalUserMapper.queryPage(condition);		// 分页结果
			List<TerminalUserVO> userList = new ArrayList<TerminalUserVO>();
			if(terminalUserList!=null && terminalUserList.size()>0){
				BigDecimal turnover = new BigDecimal(0);
				for(TerminalUserVO terminalUser : terminalUserList){
					Map<String, Object> hostData = new LinkedHashMap<String, Object>();
					hostData.put("userId", terminalUser.getId());
					hostData.put("start_row", null);
					List<CloudHostVO> hostList = cloudHostMapper.getByTerminalUserId(hostData);
					if(hostList!=null && hostList.size()>0){
						for(CloudHostVO cloudHost : hostList){
							Map<String, Object> billDetailData = new LinkedHashMap<String, Object>();
							billDetailData.put("hostId", cloudHost.getId());
							List<CloudHostBillDetailVO> billDetailList = cloudHostBillDetailMapper.getBillDetailByHostId(billDetailData);
							if(billDetailList!=null && billDetailList.size()>0){
								for(CloudHostBillDetailVO billDetail : billDetailList){
									turnover = turnover.add(billDetail.getFee()==null?new BigDecimal(0):billDetail.getFee());
								}
							}
						}
					}
					terminalUser.setTurnover(turnover);
					userList.add(terminalUser);
				}
			}
			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, userList);
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("查询失败");
		}
	}
	
	@Callable
	@CallWithoutLogin
	public String uerForgetPassword(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("TerminalUserServiceImpl.uerForgetPassword()");
		return "/public/user/forgetpassword2.jsp";
		
	}
	
	@Callable
	public boolean queryTerminalUserByName(String TerminalUserName)
	{
		logger.debug("TerminalUserServiceImpl.queryTerminalUserByName()");
		try
		{
			// 查询数据库
			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
			return terminalUserMapper.getTerminalUserByName(TerminalUserName) != null;
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("查询失败");
		}
	}

	@Callable
	@CallWithoutLogin
	public MethodResult optionChangePassword(Map<String, String> parameter)
	{
		logger.debug("TerminalUserServiceImpl.optionChangePassword()");
		try
		{
			HttpServletRequest request = RequestContext.getHttpRequest();
			// 参数处理
			String account = StringUtil.trim(parameter.get("inputemail"));
			String verificationCode = StringUtil.trim(parameter.get("inputcode"));
			if( account.isEmpty() )
			{
				return new MethodResult(MethodResult.FAIL,"邮箱不能为空");
			}
			if( verificationCode.isEmpty() )
			{
				return new MethodResult(MethodResult.FAIL,"验证码不能为空");
			}
			if( VerificationCodeHelper.isMatch(request, verificationCode)==false )
			{
				return new MethodResult(MethodResult.FAIL, "验证码不正确");
			}
			// 判断账号是否已经存在
			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
			TerminalUserVO terminalUserVO = terminalUserMapper.getTerminalUserByAccountForgetPassword(account);
			
			if( terminalUserVO == null )
			{
				return new MethodResult(MethodResult.FAIL,account + "是未绑定邮箱");
			}
			//电话和邮箱显示部分
			if (!(terminalUserVO.getPhone()==null||terminalUserVO.getPhone().isEmpty())) {
				String phone = terminalUserVO.getPhone();
				String begin = phone.substring(0,3);
				String  end  = phone.substring(7,11);
				String newPhone = begin+"****"+end;
				terminalUserVO.setPhone(newPhone);
			}
			
			if (!(terminalUserVO.getEmail()==null||terminalUserVO.getEmail().isEmpty())) {
				String email     = terminalUserVO.getEmail();
				String myemail[] = email.split("@");
				String beging    = myemail[0].substring(0, 3); 
				String newMail   = beging+"****"+"@"+myemail[1];
				terminalUserVO.setEmail(newMail);
			}
				request.getSession().setAttribute("myterminalUserVO", terminalUserVO);
				return new MethodResult(MethodResult.SUCCESS, "存在的账户");
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("不存在的邮箱");
		}
	}

	@Callable
	@Transactional(readOnly = false)
	public MethodResult addTerminalUser(Map<String, String> parameter)
	{
		logger.debug("TerminalUserServiceImpl.addTerminalUser()");
		Long begin = System.currentTimeMillis();
		Integer logStatus = AppConstant.OPER_LOG_FAIL;
		HttpServletRequest request = RequestContext.getHttpRequest();
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		String _userName = "";
		try
		{
			// 权限判断
			int userType = loginInfo.getUserType();
			if( loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_OPERATOR && loginInfo.hasPrivilege(PrivilegeConstant.terminal_user_manage_add) == false )
			{
				throw new AppException("您没有权限进行此操作");
			}else if(loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT && loginInfo.hasPrivilege(PrivilegeConstant.terminal_user_manage_add_agent) == false){
				throw new AppException("您没有权限进行此操作");
			}
			
			String groupId    = StringUtil.trim(parameter.get("group_id"));
			String account    = StringUtil.trim(parameter.get("account"));
			String password   = StringUtil.trim(parameter.get("password"));
			String phone      = StringUtil.trim(parameter.get("phone"));
			String percentOff = StringUtil.trim(parameter.get("percentOff"));
			String id         = StringUtil.generateUUID();

			if( StringUtil.isBlank(account) )
			{
				throw new AppException("用户名不能为空");
			}
			if( phone.isEmpty() )
			{
				throw new AppException("手机号码不能为空");
			}
			//代理商添加 无折扣 默认0
			if (StringUtil.isBlank(percentOff))
			{
				percentOff="0";
			}

			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
			SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
			UserMessageMapper userMessageMapper =this.sqlSession.getMapper(UserMessageMapper.class);
			
			// 判断账号是否已经存在
			TerminalUserVO terminalUser = terminalUserMapper.getTerminalUserByAccount(account);
			if( terminalUser != null )
			{
				return new MethodResult(MethodResult.FAIL, "账号[" + account + "]已经存在");
			}
			TerminalUserVO terminalUserVO = terminalUserMapper.getTerminalUserByPhone(phone);
			if (terminalUserVO != null) {
				return new MethodResult(MethodResult.FAIL,"该手机已绑定致云Zhicloud账号");
			}
			
			Map<String, Object> userData = new LinkedHashMap<String, Object>();
			userData.put("id",       id);
			userData.put("groupId",  groupId);
			userData.put("account",  account);
			password = RandomPassword.getRandomNum(MailConstant.PASSWORD_LENGTH);
			userData.put("password", LoginHelper.toEncryptedPassword(password));
			userData.put("type",     AppConstant.SYS_USER_TYPE_TERMINAL_USER);
			int n = sysUserMapper.addSysUser(userData);
			Map<String, Object> terminalUserData = new LinkedHashMap<String, Object>();
			terminalUserData.put("id",            id);
			terminalUserData.put("email",         account);
			terminalUserData.put("phone",         phone);
			terminalUserData.put("createTime",    StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
			terminalUserData.put("emailVerified", AppConstant.TERMINAL_USER_EMAIL_VERIFIED_NOT);
			terminalUserData.put("phoneVerified", AppConstant.TERMINAL_USER_PHONE_VERIFIED_NOT);
			terminalUserData.put("status",        AppConstant.TERMINAL_USER_STATUS_NORMAL);
			terminalUserData.put("accountBalance",BigDecimal.ZERO);
			terminalUserData.put("belongingId",   loginInfo.getUserId());
			terminalUserData.put("percentOff",    percentOff);
			//根据用户类型，插入到相应的归属地
			if(userType == AppConstant.SYS_USER_TYPE_AGENT){
				terminalUserData.put("belongingType", AppConstant.TERMINAL_USER_BELONGING_TYPE_AGENT);
			}else{
				terminalUserData.put("belongingType", AppConstant.TERMINAL_USER_BELONGING_TYPE_OPERATOR);
			}
			int m = terminalUserMapper.addTerminalUser(terminalUserData);
			
			Map<String, Object> userMessage = new LinkedHashMap<String, Object>();
			userMessage.put("id", StringUtil.generateUUID());
			userMessage.put("userId", id);
			userMessage.put("content", "当前密码是系统初始密码，请及时修改");
			userMessage.put("createTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
			userMessage.put("status", "1");
			int p=userMessageMapper.insertReg(userMessage);
			
			//发送邮件
			Map<String, Object> user = new LinkedHashMap<String, Object>();
			user.put("password", password);
			user.put("email",    account); 
			new SendMail().sendPasswordEmail(user);
			_userName = account;
			if( n > 0 && m > 0 && p>0)
			{   
				logStatus = AppConstant.OPER_LOG_SUCCESS;
				return new MethodResult(MethodResult.SUCCESS, "添加成功");
			}
			else
			{
				return new MethodResult(MethodResult.FAIL, "添加失败");
			}

		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("添加失败");
		}finally {
			if(!StringUtil.isBlank(_userName)){
				try {
					OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
					Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
					operLogData.put("id", StringUtil.generateUUID());
					operLogData.put("userId", loginInfo.getUserId());
					operLogData.put("content", "添加终端用户:" + _userName);
					operLogData.put("operTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
					operLogData.put("status", logStatus);
					operLogData.put("resourceName", "终端用户名:" + _userName);
					operLogData.put("operDuration", System.currentTimeMillis() - begin);
					operLogMapper.addOperLog(operLogData);
				} catch (Exception e) {
					logger.error(e);
				}
			}

		} 
	}
	@Callable
	@Transactional(readOnly = false)
	@CallWithoutLogin
	public MethodResult addTerminalUserWithoutLogin(Map<String, String> parameter)
	{
		logger.debug("TerminalUserServiceImpl.addTerminalUserWithoutLogin()");
		try
		{
			HttpServletRequest request = RequestContext.getHttpRequest();
			
			String account          = StringUtil.trim(parameter.get("account"));
			String password         = StringUtil.trim(parameter.get("password"));
			String confpassword     = StringUtil.trim(parameter.get("confpassword"));
			String phone            = StringUtil.trim(parameter.get("phone"));
			String id               = StringUtil.generateUUID();
			String verificationCode = StringUtil.trim(parameter.get("verificationCode"));
			String messageCode      = StringUtil.trim(parameter.get("messageCode"));
//			String inviteCode       = StringUtil.trim(parameter.get("inviteCode"));
			
			if (request.getSession().getAttribute("mySetPhone")==null) 
			{
				return new MethodResult(MethodResult.FAIL,"该号码没有获取验证码，请重新注册");
			}
			if (request.getSession().getAttribute("loginMobileCode")==null) 
			{
				return new MethodResult(MethodResult.FAIL,"手机验证码不正确，请重新注册");
			}
			String mySetPhone       = request.getSession().getAttribute("mySetPhone").toString();
			String mobileCode       = request.getSession().getAttribute("loginMobileCode").toString();
			
			if( account.isEmpty()) 
			{
				return new MethodResult(MethodResult.FAIL,"邮箱不能为空");
			}
			
			if( password.isEmpty() )
			{
				return new MethodResult(MethodResult.FAIL,"密码不能为空");
			}
			if (password.length()<6||password.length()>20) 
			{
				return new MethodResult(MethodResult.FAIL,"密码长度6-20位");
			}
			if (confpassword.isEmpty()) 
			{
				return new MethodResult(MethodResult.FAIL,"确认密码不能为空");
			}
			if (!confpassword.equals(password)) 
			{
				return new MethodResult(MethodResult.FAIL,"两次输入的密码不一致");
			}
			if( phone.isEmpty() )
			{
				return new MethodResult(MethodResult.FAIL,"手机号码不能为空");
			}
			if( verificationCode.isEmpty())
			{
				return new MethodResult(MethodResult.FAIL,"验证码不能为空");
			}
			// 验证友判断
			if( VerificationCodeHelper.isMatch(request, verificationCode)==false )
			{
				return new MethodResult(MethodResult.FAIL, "验证码错误");
			}
			if (messageCode.isEmpty()) 
			{
				return new MethodResult(MethodResult.FAIL, "短信验证码不能为空");
			}
			if (messageCode.length()!=6) 
			{
				return new MethodResult(MethodResult.FAIL, "短信验证码不正确");
			}
			if (!messageCode.equals(mobileCode)) 
			{
				return new MethodResult(MethodResult.FAIL, "短信验证码不正确");
			}
//			if (inviteCode.isEmpty()) 
//			{
//				return new MethodResult(MethodResult.FAIL,"邀请码不能为空");
//			}
			
			if (!phone.equals(mySetPhone))
			{
				return new MethodResult(MethodResult.FAIL,"手机验证码和手机号码不匹配");
			}
			//有邀请码
//			if (!inviteCode.isEmpty()) 
//			{
//				InviteCodeMapper inviteCodeMapper = this.sqlSession.getMapper(InviteCodeMapper.class);
//				InviteCodeVO inviteCodeVO = inviteCodeMapper.getInviteCodeByCode(inviteCode);
//				
//				if (inviteCodeVO==null) 
//				{
//					return new MethodResult(MethodResult.FAIL,"不存在的邀请码");
//				}
//				//判断间隔
//				long sendTime = Long.valueOf(inviteCodeVO.getSendTime()).longValue();
//				long nowTime  = Long.valueOf(StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS")).longValue();
//				if ((nowTime-sendTime)>700000000*100)
//				{
//					return new MethodResult(MethodResult.FAIL,"邀请码已经过期");
//				}
//				
//			}
			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
			SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
			InviteCodeMapper inviteCodeMapper = this.sqlSession.getMapper(InviteCodeMapper.class);
			UserMessageMapper userMessageMapper =this.sqlSession.getMapper(UserMessageMapper.class);
			
//			// 判断账号是否已经存在
//			TerminalUserVO terminalUser = terminalUserMapper.getTerminalUserByAccount(account);
//			if( terminalUser != null )
//			{
//				return new MethodResult(MethodResult.FAIL, "邮箱[" + account + "]已经存在");
//			}
			
//			TerminalUserVO terminalUserVO = terminalUserMapper.getTerminalUserByIdCard(idCard);
//			if (terminalUserVO != null) {
//				
//				return new MethodResult(MethodResult.FAIL, "身份证"+idCard+"已经存在");
//			}
//			
			Map<String, Object> userData = new LinkedHashMap<String, Object>();
			userData.put("id",       id);
		//	userData.put("groupId",  groupId);
			userData.put("account",  account);
			userData.put("password", LoginHelper.toEncryptedPassword(password));
			userData.put("type",     AppConstant.SYS_USER_TYPE_TERMINAL_USER);
			int n = sysUserMapper.addSysUser(userData);
			
			Map<String, Object> terminalUserData = new LinkedHashMap<String, Object>();
			terminalUserData.put("id",             id);
			terminalUserData.put("phone",          phone);
			terminalUserData.put("email",          account);
			terminalUserData.put("createTime",     StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
			terminalUserData.put("phoneVerified",  AppConstant.TERMINAL_USER_PHONE_VERIFIED_YES);
			terminalUserData.put("status",         AppConstant.TERMINAL_USER_STATUS_NORMAL);
			terminalUserData.put("emailVerified",  AppConstant.TERMINAL_USER_EMAIL_VERIFIED_NOT);
			terminalUserData.put("accountBalance", "20");
			terminalUserData.put("percentOff",     "0");
//			terminalUserData.put("belongingId",   loginInfo.getUserId());
			//根据用户类型，插入到相应的归属地
//			if(userType == AppConstant.SYS_USER_TYPE_AGENT){
//				terminalUserData.put("belongingType", AppConstant.TERMINAL_USER_BELONGING_TYPE_AGENT);
//			}else{
//				terminalUserData.put("belongingType", AppConstant.TERMINAL_USER_BELONGING_TYPE_OPERATOR);
//			}
			int m = terminalUserMapper.addTerminalUser(terminalUserData);
//			//插入数据表
//			Map<String, Object> terminalUserInviteCode = new LinkedHashMap<String, Object>();
//			terminalUserInviteCode.put("code", inviteCode);
//			terminalUserInviteCode.put("userId", id);
//			terminalUserInviteCode.put("status", AppConstant.INVITE_CODE_SEND_USED);
//			int k = inviteCodeMapper.updateByRegisterTerminalUser(terminalUserInviteCode);
			
			//注册生成验证码
//			for (int i = 0; i < AppConstant.CREATE_TERMINAL_INVITE_CODE_NUMBER ; i++) {
//				String codeId   = StringUtil.generateUUID();
//				String code     = codeId.substring(4, 12);
//				// 添加进数据库
//				Map<String, Object> terminalInviteCode = new LinkedHashMap<String, Object>();
//				terminalInviteCode.put("id",         codeId);
//				terminalInviteCode.put("createrId",  id);
//				terminalInviteCode.put("code",       code);
//				terminalInviteCode.put("createTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS") );
//				terminalInviteCode.put("status",     AppConstant.INVITE_CODE_SEND_NO);
//				inviteCodeMapper.addInviteCode(terminalInviteCode);
//			}
			AccountBalanceDetailMapper accountBalanceDetailMapper = this.sqlSession.getMapper(AccountBalanceDetailMapper.class);  
			Map<String, Object> balanceDetailData = new LinkedHashMap<String, Object>(); 
			balanceDetailData.put("id",     StringUtil.generateUUID()); 
			balanceDetailData.put("amount",     "20"); 
			balanceDetailData.put("userId",     id);  
			balanceDetailData.put("rechargeStatus",     "2");  //已充值
			balanceDetailData.put("type",     "1");  //充值
			balanceDetailData.put("payType",     "3"); //系统赠费
			balanceDetailData.put("balance_before_change",     "0");  
			balanceDetailData.put("balance_before_change",     "20");  
			balanceDetailData.put("description",     "充值");  
			balanceDetailData.put("changeTime",     StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));  
			int j = accountBalanceDetailMapper.addAccountBalanceDetail(balanceDetailData);
			
			Map<String, Object> userMessage = new LinkedHashMap<String, Object>();
			userMessage.put("id", StringUtil.generateUUID());
			userMessage.put("userId", id);
			userMessage.put("content", "感谢注册，系统特为新用户赠送20元");
			userMessage.put("createTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
			int p=userMessageMapper.insertReg(userMessage);
			
			// 将用户信息设置到LoginInfo，存放于session
			LoginInfo loginInfo = new LoginInfo(true);
			loginInfo.setUserId(id); 
			loginInfo.setUserType(AppConstant.SYS_USER_TYPE_TERMINAL_USER);
			loginInfo.setAccount(account); 			
			loginInfo.setPhone(phone);  
//			loginInfo.setPrivilege(sysUserService.fetchPrivilegeByUserId(id));	// 获取权限
			
			LoginHelper.setLoginInfo(request, loginInfo);
			
			if( n > 0 && m > 0 && j>0 && p>0)
			{   //发送邮件
//				new SendMail().sendEmail(terminalUserData); 
				return new MethodResult(MethodResult.SUCCESS, "添加成功");
			}
			else
			{
				return new MethodResult(MethodResult.FAIL, "添加失败");
			}

		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("添加失败");
		}
	}

	@Callable
	@Transactional(readOnly = false)
	public MethodResult updateTerminalUserById(Map<String, Object> parameter)
	{
		logger.debug("TerminalUserServiceImpl.updateTerminalUserById()");
		Long begin = System.currentTimeMillis();
		Integer logStatus = AppConstant.OPER_LOG_FAIL;
		HttpServletRequest request = RequestContext.getHttpRequest();
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		String _userName = "";
		try
		{
			// 权限判断
			if( loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_OPERATOR && loginInfo.hasPrivilege(PrivilegeConstant.terminal_user_manage_modify) == false )
			{
				throw new AppException("您没有权限进行此操作");
			}else if(loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT && loginInfo.hasPrivilege(PrivilegeConstant.terminal_user_manage_modify_agent) == false){
				throw new AppException("您没有权限进行此操作");
			}
			
			String terminalUserId = StringUtil.trim(parameter.get("terminalUserId"));
			String account        = StringUtil.trim(parameter.get("account"));
			String phone          = StringUtil.trim(parameter.get("phone"));
			String percentOff     = StringUtil.trim(parameter.get("percentOff"));
			int status            = Integer.parseInt((String) parameter.get("status"));
			
			if( StringUtil.isBlank(terminalUserId) )
			{
				return new MethodResult(MethodResult.FAIL, "terminalUserId不能为空");
			}  
			if( StringUtil.isBlank(account) )
			{
				return new MethodResult(MethodResult.FAIL, "用户名不能为空");
			}
			if( phone.isEmpty() )
			{
				return new MethodResult(MethodResult.FAIL, "手机号码不能为空");
			}
			if(status<1||status>5){
				return new MethodResult(MethodResult.FAIL, "状态不合法");
				
			}

			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
			SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
			CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
			
			// 判断账号是否已经存在
			TerminalUserVO terminalUser = terminalUserMapper.getTerminalUserByAccount(account);
			if( terminalUser != null && terminalUser.getId().equals(terminalUserId)==false )
			{
				return new MethodResult(MethodResult.FAIL, "用户名[" + account + "]已经存在");
			}
			TerminalUserVO User = terminalUserMapper.getBaseInfoById(terminalUserId);
			_userName = User.getAccount();
			TerminalUserVO terminalUserVO = terminalUserMapper.getTerminalUserByPhone(phone);
			if(!phone.endsWith(User.getPhone()))
			{
				if (terminalUserVO != null) {
					return new MethodResult(MethodResult.FAIL,"该手机已绑定云端在线账号");
				}
			}
			//代理商更新无折扣
			if (StringUtil.isBlank(percentOff)) 
			{
				percentOff=User.getPercentOff().toString();
			}
			//如果有更新，马上结算新的数据
			if (User.getPercentOff()!=null) 
			{
				if (!User.getPercentOff().toString().equals(percentOff) && StringUtil.isBlank(percentOff)==false) 
				{
					List<CloudHostVO> cloudHostList = cloudHostMapper.getByUserId(terminalUserId);
					if (cloudHostList != null && cloudHostList.size() > 0) 
					{
						for (CloudHostVO vo : cloudHostList) 
						{
							if (vo.getType() == 3) 
							{
								if (!(vo.getSummarizedStatusText().equals("创建失败") || vo.getSummarizedStatusText().equals("停机") )) 
								{
									// 结算，结算之后产生新的账单
									Date now = new Date();
									new CloudHostBillingHelper(sqlSession).settleAllUndoneCloudHostBills(vo.getId(), now, true);
								}
							}
						}
					}
				}
			}
			
			// 更新数据
			Map<String, Object> userData = new LinkedHashMap<String, Object>();
			userData.put("account",  account);
			userData.put("id",       terminalUserId);
			int n = sysUserMapper.updateSysUser(userData);

			Map<String, Object> terminalUserData = new LinkedHashMap<String, Object>();
			terminalUserData.put("id",            terminalUserId);
			terminalUserData.put("email",         account);
			terminalUserData.put("phone",         phone);
			terminalUserData.put("emailVerified", AppConstant.TERMINAL_USER_EMAIL_VERIFIED_NOT);
			terminalUserData.put("phoneVerified", AppConstant.TERMINAL_USER_PHONE_VERIFIED_NOT);
			terminalUserData.put("status",        status);
			terminalUserData.put("percentOff",    percentOff);
			//根据用户类型的不同存入不同的类型
			if(loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT){
				terminalUserData.put("belongingType", AppConstant.TERMINAL_USER_BELONGING_TYPE_AGENT);
			}else {
				terminalUserData.put("belongingType", AppConstant.TERMINAL_USER_BELONGING_TYPE_OPERATOR);
			}
			//--------
			terminalUserData.put("createTime",    "");
//			terminalUserData.put("accountBalance", BigDecimal.ZERO);
			int m = terminalUserMapper.updateTerminalUser(terminalUserData);

			if( n > 0 && m > 0)
			{
				logStatus = AppConstant.OPER_LOG_SUCCESS;
				return new MethodResult(MethodResult.SUCCESS, "修改成功");
			}
			else
			{
				return new MethodResult(MethodResult.FAIL, "修改失败");
			}
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("失败");
		}finally {
			try {
				OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
				Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
				operLogData.put("id", StringUtil.generateUUID());
				operLogData.put("userId", loginInfo.getUserId());
				operLogData.put("content", "修改终端用户:" + _userName + "的信息");
				operLogData.put("operTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
				operLogData.put("status", logStatus);
				operLogData.put("resourceName", "终端用户:" + _userName);
				operLogData.put("operDuration", System.currentTimeMillis() - begin);
				operLogMapper.addOperLog(operLogData);
			} catch (Exception e) {
				logger.error(e);
			}

		} 
	}
	
	
	
	@Callable
	@Transactional(readOnly = false)
	public MethodResult deleteTerminalUserByIds(List<String> terminalUserIds)
	{
		logger.debug("TerminalUserServiceImpl.deleteTerminalUserByIds()");
		Long begin = System.currentTimeMillis();
		Integer logStatus = AppConstant.OPER_LOG_FAIL;
		HttpServletRequest request = RequestContext.getHttpRequest();
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		String _size = "";
		try
		{
			// 权限判断
			if( loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_OPERATOR && loginInfo.hasPrivilege(PrivilegeConstant.terminal_user_manage_delete) == false )
			{
				throw new AppException("您没有权限进行此操作");
			}
			else if(loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT && loginInfo.hasPrivilege(PrivilegeConstant.terminal_user_manage_delete_agent) == false)
			{
				throw new AppException("您没有权限进行此操作");
			}
			
			// 参数处理
			if( terminalUserIds == null || terminalUserIds.size() == 0 )
			{
				throw new AppException("terminalUserIds不能为空");
			}

			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
			SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
			InviteCodeMapper inviteCodeMapper = this.sqlSession.getMapper(InviteCodeMapper.class);
			
			int n = terminalUserMapper.deleteTerminalUserByIds(terminalUserIds);
			int m = sysUserMapper.deleteSysUsers(terminalUserIds.toArray(new String[0]));
			int k = inviteCodeMapper.deleteInviteByCreaterId(terminalUserIds);
			
			if( n > 0 || m > 0 || k >0)
			{
				CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
				List<CloudHostVO> cloudHostList = cloudHostMapper.getByUserIds(terminalUserIds); 
				String [] ids = new String[cloudHostList.size()];
				for( int i=0; i<cloudHostList.size(); i++ )
				{ 
					CloudHostVO cloudHost = cloudHostList.get(i);
					
					// 从http gateway删除
					HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(cloudHost.getRegion());
					JSONObject hostDeleteResult = channel.hostDelete(cloudHost.getRealHostId());
					if( HttpGatewayResponseHelper.isSuccess(hostDeleteResult) )
					{
						logger.info("CloudHostServiceImpl.deleteTerminalUserByIds() > ["+Thread.currentThread().getId()+"] delete host succeeded, uuid:["+cloudHost.getRealHostId()+"], message:["+HttpGatewayResponseHelper.getMessage(hostDeleteResult)+"]");
					}
					else
					{
						logger.warn("CloudHostServiceImpl.deleteTerminalUserByIds() > ["+Thread.currentThread().getId()+"] delete host failed, uuid:["+cloudHost.getRealHostId()+"], message:["+HttpGatewayResponseHelper.getMessage(hostDeleteResult)+"]");
					}
					
					// 从缓冲池删除
					CloudHostPoolManager.getCloudHostPool().removeByRealHostId(cloudHost.getRealHostId());
					CloudHostPoolManager.getCloudHostPool().removeByHostName(cloudHost.getRegion(), cloudHost.getHostName());  
					ids[i]=cloudHost.getId();
				}
				_size = String.valueOf(terminalUserIds.size());
				if(ids.length>=1){
					//逻辑删除云主机
					int a = cloudHostMapper.updateForDeleteByIds(ids);
					if(a>0){
						logStatus = AppConstant.OPER_LOG_SUCCESS;
						return new MethodResult(MethodResult.SUCCESS, "删除成功");					
					}else{ 				
						return new MethodResult(MethodResult.FAIL, "删除失败");
						
					}
				}
				logStatus = AppConstant.OPER_LOG_SUCCESS;
				return new MethodResult(MethodResult.SUCCESS, "删除成功");					
			}
			else
			{
				return new MethodResult(MethodResult.FAIL, "删除失败");
			}
		}
		catch( Exception e )
		{
			throw new AppException("删除失败");
		}finally {
			try {
				OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
				Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
				operLogData.put("id", StringUtil.generateUUID());
				operLogData.put("userId", loginInfo.getUserId());
				operLogData.put("content", "删除"+_size+"位终端用户");
				operLogData.put("operTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
				operLogData.put("status", logStatus);
				operLogData.put("resourceName", "");
				operLogData.put("operDuration", System.currentTimeMillis() - begin);
				operLogMapper.addOperLog(operLogData);
			} catch (Exception e) {
				logger.error(e);
			}

		} 
	}
	
	
	@Callable
	@Transactional(readOnly = false)
	public MethodResult updateBaseInfo(Map<String, Object> parameter) {
		Long begin = System.currentTimeMillis();
		Integer logStatus = AppConstant.OPER_LOG_FAIL;
		logger.debug("TerminalUserServiceImpl.updateBaseInfo()");
		HttpServletRequest request = RequestContext.getHttpRequest();
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		try
		{
			// 参数处理
			String terminalUserId = StringUtil.trim(parameter.get("terminalUser_id"));
//			String groupId        = StringUtil.trim(parameter.get("group_id"));
			String account        = StringUtil.trim(parameter.get("account"));
//			String name           = StringUtil.trim(parameter.get("name"));
//			String idCard         = StringUtil.trim(parameter.get("id_card"));
			String email          = StringUtil.trim(parameter.get("e_mail"));
			String phone          = StringUtil.trim(parameter.get("phone"));
			Integer status        = StringUtil.parseInteger((String)parameter.get("status"), AppConstant.TERMINAL_USER_STATUS_NORMAL);
//			System.out.println("---------------"+idCard);
			if (terminalUserId.isEmpty()) {
				return new MethodResult(MethodResult.FAIL,"账户id不能为空");
			}
			if( account.isEmpty() )
			{
				return new MethodResult(MethodResult.FAIL, "账户不能为空");
			}
//			if( name.isEmpty() )
//			{
//				return new MethodResult(MethodResult.FAIL, "用户名不能为空");
//			}
//			if (idCard.isEmpty()) 
//			{
//				return new MethodResult(MethodResult.FAIL,"身份证不能为空");
//			}
			if( email.isEmpty() )
			{
				return new MethodResult(MethodResult.FAIL, "邮箱不能为空");
			}
			if( phone.isEmpty() )
			{
				return new MethodResult(MethodResult.FAIL, "手机不能为空");
			}
			
			SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
			
			// 判断运营商账户是否已经存在
			LinkedHashMap<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("type", AppConstant.SYS_USER_TYPE_TERMINAL_USER);
			condition.put("account", account);
			SysUserVO sysUser = sysUserMapper.getByTypeAndAccount(condition);
			if( sysUser!=null && sysUser.getId().equals(terminalUserId)==false )
			{
				return new MethodResult(MethodResult.FAIL, "账号[" + account + "]已经存在");
			}
			
			
			// 更新系统用户表
			Map<String, Object> userData = new LinkedHashMap<String, Object>();
			userData.put("id",       terminalUserId);
			userData.put("account",  account);
			userData.put("password",      "");
//			userData.put("groupId",  groupId);
			int n = sysUserMapper.updateSysUser(userData);
			if( n==0 )
			{
				return new MethodResult(MethodResult.FAIL, "修改失败");
			}

			// 更新终端用户表
			Map<String, Object> terminalUserData = new LinkedHashMap<String, Object>();
			terminalUserData.put("id", terminalUserId);
//			terminalUserData.put("name",    name);
//			terminalUserData.put("idCard", idCard);
			terminalUserData.put("email",   email);
			terminalUserData.put("phone",   phone);
			terminalUserData.put("status",  status);

			terminalUserMapper.updateTerminalUserBaseInfo(terminalUserData);
			logStatus = AppConstant.OPER_LOG_SUCCESS;
			return new MethodResult(MethodResult.SUCCESS, "修改成功，请重新登录");
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("修改失败");
		}finally{
			try{
				OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
				Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
				operLogData.put("id", StringUtil.generateUUID());
				operLogData.put("userId", loginInfo.getUserId());
				operLogData.put("content", "修改基本信息");
				operLogData.put("operTime", StringUtil.dateToString(new Date(),"yyyyMMddhhmmssSSS"));
				operLogData.put("status", logStatus);
				operLogData.put("resourceName", loginInfo.getAccount());
				operLogData.put("operDuration", System.currentTimeMillis()-begin);
				operLogMapper.addOperLog(operLogData);
			}catch(Exception e){
				logger.error(e);
			}
			
		}
		
	}

	@Callable
	@Transactional(readOnly = false)
	public MethodResult updateBaseInfoAccount(Map<String, Object> parameter) {
		logger.debug("TerminalUserServiceImpl.updateBaseInfoAccount()");
		try
		{
			// 参数处理
			String terminalUserId = StringUtil.trim(parameter.get("terminalUser_id"));
			String account        = StringUtil.trim(parameter.get("terminal_account"));
//			System.out.println("------------------"+terminalUserId+"--------------------"+account+"-----------");
			
			if (terminalUserId.isEmpty()) {
				return new MethodResult(MethodResult.FAIL,"账户id不能为空");
			}
			if( account.isEmpty() )
			{
				return new MethodResult(MethodResult.FAIL, "用户名不能为空");
			}
			
			SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
			
			// 判断账户是否已经存在
			LinkedHashMap<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("type", AppConstant.SYS_USER_TYPE_TERMINAL_USER);
			condition.put("account", account);
			SysUserVO sysUser = sysUserMapper.getByTypeAndAccount(condition);
			if( sysUser!=null && sysUser.getId().equals(terminalUserId)==false )
			{
				return new MethodResult(MethodResult.FAIL, "用户名" + account + "已经存在");
			}
			
			
			// 更新系统用户表
			Map<String, Object> userData = new LinkedHashMap<String, Object>();
			userData.put("id",       terminalUserId);
			userData.put("account",  account);
			int n = sysUserMapper.updateSysUser(userData);
			if( n==0 )
			{
				return new MethodResult(MethodResult.FAIL, "修改失败");
			}
			
			return new MethodResult(MethodResult.SUCCESS, "修改成功，请重新登录");
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("修改失败");
		}
		
	}

	
	@Callable
	@Transactional(readOnly = false)
	public MethodResult updateBaseInfoEmail(Map<String, Object> parameter) {
		Long begin = System.currentTimeMillis();
		Integer logStatus = AppConstant.OPER_LOG_FAIL;
		HttpServletRequest request = RequestContext.getHttpRequest();
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		String content = "";
		logger.debug("TerminalUserServiceImpl.updateBaseInfoEmail()");
		try
		{
			// 参数处理
			String terminalUserId = loginInfo.getUserId();
			String email          = StringUtil.trim(parameter.get("myNewEmail"));
			String emailMessage   = StringUtil.trim(parameter.get("changeEmailNewCode")); 
			content = "修改邮箱为"+email;
			if (request.getSession().getAttribute("changeEmailByNewEmailCode")==null) {
				return new MethodResult(MethodResult.FAIL,"请获取验证码");
			}
			String emailCode      = request.getSession().getAttribute("changeEmailByNewEmailCode").toString();
			if (terminalUserId.isEmpty()) {
				return new MethodResult(MethodResult.FAIL,"账户id不能为空");
			}
			if( email.isEmpty() )
			{
				return new MethodResult(MethodResult.FAIL,"邮箱不能为空");
			}
			if (emailMessage.isEmpty()) 
			{
				return new MethodResult(MethodResult.FAIL,"验证码不能为空");
			}
			if (!emailMessage.equals(emailCode)) 
			{
				return new MethodResult(MethodResult.FAIL,"验证码不正确");
			}
			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
			SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
			TerminalUserVO terminalUser = terminalUserMapper.getTerminalUserByAccount(email);
			if( terminalUser != null )
			{
				return new MethodResult(MethodResult.FAIL, "邮箱[" + email + "]已经存在");
			}
			// 更新表
			Map<String, Object> userData = new LinkedHashMap<String, Object>();
			userData.put("id",     terminalUserId);
			userData.put("email",  email);
			userData.put("emailVerified", AppConstant.TERMINAL_USER_EMAIL_VERIFIED_YES);
			int n =terminalUserMapper.updateTerminalUserBaseInfoEmail(userData);
			if( n<=0 )
			{
				throw new AppException("修改失败");
			}
			Map<String,Object> userData2 = new LinkedHashMap<String, Object>();
			userData2.put("id", terminalUserId);
			userData2.put("account", email);
			int m=sysUserMapper.updateSysUserAccount(userData2);
			if (m<=0) {
				throw new AppException("修改失败");
			}
			logStatus = AppConstant.OPER_LOG_SUCCESS;
			return new MethodResult(MethodResult.SUCCESS, email);
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("修改失败");
		}finally{
			try{
				OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
				Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
				operLogData.put("id", StringUtil.generateUUID());
				operLogData.put("userId", loginInfo.getUserId());
				operLogData.put("content", content);
				operLogData.put("operTime", StringUtil.dateToString(new Date(),"yyyyMMddHHmmssSSS"));
				operLogData.put("status", logStatus);
				operLogData.put("resourceName", "用户名:"+loginInfo.getAccount());
				operLogData.put("operDuration", System.currentTimeMillis()-begin);
				operLogMapper.addOperLog(operLogData);
			}catch(Exception e){
				logger.error(e);
			}
			
		}
		
	}

	
	@Callable
	@Transactional(readOnly = false)
	public MethodResult updateBaseInfoEmailSendCode(Map<String, Object> parameter) {
		logger.debug("TerminalUserServiceImpl.updateBaseInfoEmailGetCode()");
		try
		{
			HttpServletRequest request = RequestContext.getHttpRequest();
			// 参数处理
			String email = StringUtil.trim(parameter.get("myEmailNumber"));
			if( email.isEmpty() )
			{
				return new MethodResult(MethodResult.FAIL, "邮箱不能为空");
			}
//			if (request.getSession().getAttribute("changeEmailOrMobileCode")!=null)
//			{
//				return new MethodResult(MethodResult.FAIL, "发送成功，请注意查收");
//			}
			Integer code = (Integer)((int)((Math.random()*9+1)*100000));
			//发送邮件
			Map<String, Object> user = new LinkedHashMap<String, Object>();
			user.put("email", email);
			user.put("code",  code);
			request.getSession().setAttribute("changeEmailOrMobileCode", code);
			new SendMail().terminalCheckEmail(user);
			return new MethodResult(MethodResult.SUCCESS, "发送成功，请注意查收");
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("修改失败");
		}
		
	}

	@Callable
	@Transactional(readOnly = false)
	public MethodResult updateBaseInfoEmailSendCodeAgain(Map<String, Object> parameter) {
		logger.debug("TerminalUserServiceImpl.updateBaseInfoEmailSendCodeAgain()");
		try
		{
			HttpServletRequest request = RequestContext.getHttpRequest();
			// 参数处理
			String email = StringUtil.trim(parameter.get("myNewEmail"));
			if( email.isEmpty() )
			{
				return new MethodResult(MethodResult.FAIL, "邮箱不能为空");
			}
			//判断账号是否以及存在
			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
			TerminalUserVO terminalUser = terminalUserMapper.getTerminalUserByAccount(email);
			if( terminalUser != null )
			{
				return new MethodResult(MethodResult.FAIL, "邮箱[" + email + "]已经存在");
			}
			Integer code = (Integer)((int)((Math.random()*9+1)*100000));
			//发送邮件
			Map<String, Object> user = new LinkedHashMap<String, Object>();
			user.put("email", email);
			user.put("code",  code);
			request.getSession().setAttribute("changeEmailByNewEmailCode", code);
			new SendMail().terminalCheckEmail(user);
			return new MethodResult(MethodResult.SUCCESS, "发送成功,请注意查收");
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("修改失败");
		}
		
	}

	
	@Callable
	@Transactional(readOnly = false)
	public MethodResult updateBaseInfoPhone(Map<String, Object> parameter) {
		Long begin = System.currentTimeMillis();
		Integer logStatus = AppConstant.OPER_LOG_FAIL;
		HttpServletRequest request = RequestContext.getHttpRequest();
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		String content = "";
		logger.debug("TerminalUserServiceImpl.updateBaseInfoPhone()");
		try
		{
			// 参数处理
			String id = loginInfo.getUserId();
			String phone       = StringUtil.trim(parameter.get("myNewMobile")); 
			String messageCode = StringUtil.trim(parameter.get("changeMobileNewCode"));
			content = "修改手机为"+phone;
			if (request.getSession().getAttribute("changeMobileByNewMobileCode")==null) {
				return new MethodResult(MethodResult.FAIL,"请获取验证码");
			}
			String mobileCode      = request.getSession().getAttribute("changeMobileByNewMobileCode").toString();
			if (id.isEmpty()) 
			{
				return new MethodResult(MethodResult.FAIL,"账户id不能为空");
			}
			if( phone.isEmpty() )
			{
				return new MethodResult(MethodResult.FAIL,"手机号码不能为空");
			}
			if (messageCode.isEmpty()) 
			{
				return new MethodResult(MethodResult.FAIL,"验证码不能为空");
			}
			if (!messageCode.equals(mobileCode)) {
				return new MethodResult(MethodResult.FAIL,"短信验证码不正确");
			}
			//判断手机是否绑定
			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
			TerminalUserVO terminalUser = terminalUserMapper.getTerminalUserByPhone(phone);
			
			if (terminalUser != null) {
				return new MethodResult(MethodResult.FAIL,"该手机已绑定云端在线账号");
			}
			
			// 更新表
			Map<String, Object> userData = new LinkedHashMap<String, Object>();
			userData.put("id",    id);
			userData.put("phone", phone);
			userData.put("phoneVerified", AppConstant.TERMINAL_USER_PHONE_VERIFIED_YES);
			
			int n =terminalUserMapper.updateTerminalUserBaseInfoPhone(userData);
			if( n<=0 )
			{
				throw new AppException("修改失败");
			}
			logStatus = AppConstant.OPER_LOG_SUCCESS;
			return new MethodResult(MethodResult.SUCCESS,phone);
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("修改失败");
		}finally{
			try{
				OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
				Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
				operLogData.put("id", StringUtil.generateUUID());
				operLogData.put("userId", loginInfo.getUserId());
				operLogData.put("content", content);
				operLogData.put("operTime", StringUtil.dateToString(new Date(),"yyyyMMddHHmmssSSS"));
				operLogData.put("status", logStatus);
				operLogData.put("resourceName", "用户名:"+loginInfo.getAccount());
				operLogData.put("operDuration", System.currentTimeMillis()-begin);
				operLogMapper.addOperLog(operLogData);
			}catch(Exception e){
				logger.error(e);
			}
			
		}
		
	}

	
	
	@Callable
	@Transactional(readOnly = false)
	public MethodResult updateBaseInfoPhoneEmailCode(Map<String, Object> parameter) {
		logger.debug("TerminalUserServiceImpl.updateBaseInfoPhoneEmailCode()");
		try
		{
			HttpServletRequest request = RequestContext.getHttpRequest();
			// 参数处理
			String email = StringUtil.trim(parameter.get("myEmailNumber"));
			if( email.isEmpty() )
			{
				return new MethodResult(MethodResult.FAIL, "邮箱不能为空");
			}
//			if (request.getSession().getAttribute("changeEmailOrMobileCode")!=null)
//			{
//				return new MethodResult(MethodResult.FAIL, "发送成功，请注意查收");
//			}
			Integer code = (Integer)((int)((Math.random()*9+1)*100000));
			//发送邮件
			Map<String, Object> user = new LinkedHashMap<String, Object>();
			user.put("email", email);
			user.put("code",  code);
			request.getSession().setAttribute("changeEmailOrMobileCode", code);
			new SendMail().terminalCheckEmailForChangeMobile(user);
			return new MethodResult(MethodResult.SUCCESS, "发送成功，请注意查收");
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("修改失败");
		}
		
		
	}

    @Callable
	public MethodResult updateBaseInfoPhoneSendMessage(Map<String, String> parameter)
	{ 
	logger.debug("TerminalUserServiceImpl.updateBaseInfoPhoneSendMessage()");
	try {
		HttpServletRequest request = RequestContext.getHttpRequest();
		String phone       = StringUtil.trim(parameter.get("myNewMobile"));
		if (phone.isEmpty()) 
		{
			return new MethodResult(MethodResult.FAIL, "手机号码不能为空");
		}
		//判断手机是否绑定
		TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
		TerminalUserVO terminalUser = terminalUserMapper.getTerminalUserByPhone(phone);
		if (terminalUser != null) {
			return new MethodResult(MethodResult.FAIL,"该手机已绑定云端在线账号");
		}
		//发送短信
		Integer code = (Integer)((int)((Math.random()*9+1)*100000));
	    String message = new String("【致云科技】致云Zhicloud欢迎您，您的手机验证码是：" + code + "，请及时完成验证。"); 
		String state  = new SendSms().zhicloudSendSms(phone,message);
		if (("1").equals(state)) 
		{
			System.out.println("短信已成功发送");
			request.getSession().setAttribute("changeMobileByNewMobileCode", code);
			return new MethodResult(MethodResult.SUCCESS, "短信已成功发送");
		}
		else
		{
			System.out.println("短信发送失败，错误代码是："+state);
			return new MethodResult(MethodResult.FAIL, "发送失败");
		}
	} catch (Exception e) 
	{
		logger.error(e);
		throw new AppException("信息发送失败");
	}
}

	@Callable
	@Transactional(readOnly = false)
	public MethodResult updateBaseInfoPhoneCheckOldMail(Map<String, Object> parameter) {
		logger.debug("TerminalUserServiceImpl.updateBaseInfoPhoneCheckOldMail()");
		try
		{
			
			// 参数处理
			String terminalUserId = StringUtil.trim(parameter.get("terminalUser_id"));
			String oldMail      = StringUtil.trim(parameter.get("terminal_email_old"));
			if (terminalUserId.isEmpty()) {
				return new MethodResult(MethodResult.FAIL,"账户id不能为空");
			}
			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
			TerminalUserVO terminalUser           = terminalUserMapper.getBaseInfoById(terminalUserId);
			if (!oldMail.equals(terminalUser.getEmail())) {
				return new MethodResult(MethodResult.FAIL,"邮箱不正确");
			}
			else {
				return new MethodResult(MethodResult.SUCCESS,"正确");
			}
			
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("邮箱不正确");
		}
		
	}
	
	@Callable
	@Transactional(readOnly = false)
	public MethodResult changePasswordById(Map<String, Object> parameter) {
		Long begin = System.currentTimeMillis();
		Integer logStatus = AppConstant.OPER_LOG_FAIL;
		HttpServletRequest request = RequestContext.getHttpRequest();
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request,AppConstant.SYS_USER_TYPE_TERMINAL_USER);
		logger.debug("TerminalUserServiceImpl.changePasswordById()");
		try
		{
			// 参数处理
			String terminalUserId = loginInfo.getUserId();
//			String terminalUserId = StringUtil.trim(parameter.get("terminalUser_id"));
			String oldPassword    = StringUtil.trim(parameter.get("old_password")); 
			String password       = StringUtil.trim(parameter.get("password"));
			String confirm        = StringUtil.trim(parameter.get("confirm"));
			
			if( terminalUserId.isEmpty() )
			{
				return new MethodResult(MethodResult.FAIL, "账户id不能为空");
			}
			
			SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
			UserMessageMapper userMessageMapper = this.sqlSession.getMapper(UserMessageMapper.class);
			SysUserVO user = sysUserMapper.getUserPassword(terminalUserId);
			UserMessageVO userMessageVO = userMessageMapper.getUserInitPasswordMessage(terminalUserId);
			
			if (oldPassword.isEmpty()) 
			{
				return new MethodResult(MethodResult.FAIL,"原密码不能为空");
			}
			if (!LoginHelper.toEncryptedPassword(oldPassword).equals(user.getPassword())) 
			{
				return new MethodResult(MethodResult.FAIL,"原密码不正确");
			}
			
			if(password.isEmpty())
			{
				return new MethodResult(MethodResult.FAIL,"新密码不能为空");
			}
			if(password.length()<6||password.length()>20)
			{
				return new MethodResult(MethodResult.FAIL,"密码长度6-20个字符");
			}
			if(user.getPassword().equals(LoginHelper.toEncryptedPassword(password)))
			{
				return new MethodResult(MethodResult.FAIL, "新密码和原密码一致！");				
			}
			
			if(!password.equals(confirm))
			{
				return new MethodResult(MethodResult.FAIL, "密码不匹配");
			}
//			
//			SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
//			SysUserVO user = sysUserMapper.getUserPassword(terminalUserId);
			
			if(user.getPassword().equals(LoginHelper.toEncryptedPassword(password))){
				return new MethodResult(MethodResult.FAIL, "新密码和原密码一致！");				
			}
			//删除初始密码提示
			int m=2;
			if (userMessageVO!=null) 
			{
				 m = userMessageMapper.deleteUserMessage(userMessageVO.getId());
			}
			// 更新系统用户表
			Map<String, Object> userData = new LinkedHashMap<String, Object>();
			userData.put("id",       terminalUserId);
			userData.put("password", LoginHelper.toEncryptedPassword(password));

			int n = sysUserMapper.updateSysUserPassword(userData);
			if( n<=0 && m<=0 )
			{
				return new MethodResult(MethodResult.FAIL, "修改失败");
			}
			logStatus = AppConstant.OPER_LOG_SUCCESS;
			return new MethodResult(MethodResult.SUCCESS, "修改成功，请重新登录");
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("修改失败");
		}finally{
			try{
				OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
				Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
				operLogData.put("id", StringUtil.generateUUID());
				operLogData.put("userId", loginInfo.getUserId());
				operLogData.put("content", "修改密码");
				operLogData.put("operTime", StringUtil.dateToString(new Date(),"yyyyMMddHHmmssSSS"));
				operLogData.put("status", logStatus);
				operLogData.put("resourceName", "用户名:"+loginInfo.getAccount());
				operLogData.put("operDuration", System.currentTimeMillis()-begin);
				operLogMapper.addOperLog(operLogData);
			}catch(Exception e){
				logger.error(e);
			}
			
		}
	}
	
	
	@Callable
	@Transactional(readOnly = false)
	@CallWithoutLogin
	public MethodResult resetPasswordBySendMail(Map<String, Object> parameter)
	{
		logger.debug("TerminalUserServiceImpl.resetPasswordBySendMail()");
		try {
			//参数获取
//			HttpServletRequest request = RequestContext.getHttpRequest();
			
			String id               = StringUtil.trim(parameter.get("str"));
//			String account          = StringUtil.trim(parameter.get("account"));
			String password         = StringUtil.trim(parameter.get("password"));
			String confirmPassword  = StringUtil.trim(parameter.get("confirmPassword"));
//			String verificationCode = StringUtil.trim(parameter.get("verification_code"));
			
//			System.out.println("--------------"+id+"-----------------");
//			System.out.println("-----------------"+password+"--------------------------");
//			System.out.println("------------------------"+confirmPassword+"----------------");
			if (id.isEmpty()) {
				return new MethodResult(MethodResult.FAIL,"链接出错");
			}
//			if (account.isEmpty()) {
//				return new MethodResult(MethodResult.FAIL,"用户名不能为空");
//			}
			
	
//			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
//			TerminalUserVO terminalUserVO = terminalUserMapper.getTerminalUserByAccount(account);
//			//用户不存在
//			if (terminalUserVO==null) {
//				return new MethodResult(MethodResult.FAIL,"用户名错误");
//			}
			
	
			if (password.isEmpty()) {
				return new MethodResult(MethodResult.FAIL,"密码不能为空");
			}
			if (!confirmPassword.equals(password)) {
				return new MethodResult(MethodResult.FAIL,"确认密码和密码不相同");
			}
//			if( verificationCode.isEmpty() )
//			{
//				return new MethodResult(MethodResult.FAIL,"验证码不能为空");
//			}
//			if( VerificationCodeHelper.isMatch(request, verificationCode)==false )
//			{
//				return new MethodResult(MethodResult.FAIL, "验证码错误");
//			}
//			
			
			//参数处理
			Map<String, Object> terminalUserData = new LinkedHashMap<String, Object>();
			terminalUserData.put("id", id);
			terminalUserData.put("password", LoginHelper.toEncryptedPassword(password));
			SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
			int n = sysUserMapper.updateSysUserPassword(terminalUserData);
			
			if (n>0) {
				return new MethodResult(MethodResult.SUCCESS,"修改成功，请重新登陆");
			}
			else {
				return new MethodResult(MethodResult.FAIL,"修改失败");
			}
		} 
		catch (Exception e) {
			logger.error(e);
			throw new AppException("修改失败");
		}
	}

	@Callable
	@Transactional(readOnly = false)
	public MethodResult resetPasswordById(Map<String, Object> parameter) {
		
		logger.debug("TerminalUserServiceImpl.resetPasswordById()");
		try
		{
			// 参数处理
			String terminalUserId = StringUtil.trim(parameter.get("terminalUser_id"));
			String password       = StringUtil.trim(parameter.get("password"));
			String email          = StringUtil.trim(parameter.get("email"));
			
			if( terminalUserId.isEmpty() )
			{
				return new MethodResult(MethodResult.FAIL, "账户id不能为空");
			}
			if (password.isEmpty()) 
			{
				return new MethodResult(MethodResult.FAIL, "密码不能为空");
			}
			if (email.isEmpty()) 
			{
				return new MethodResult(MethodResult.FAIL, "邮箱不能为空");
			}

			
			SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
			 
			// 更新系统用户表
			Map<String, Object> userData = new LinkedHashMap<String, Object>();
			userData.put("id",       terminalUserId);
			userData.put("password", LoginHelper.toEncryptedPassword(password));
			
			//发送邮件
			Map<String, Object> user = new LinkedHashMap<String, Object>();
			user.put("password", password);
			user.put("email",    email);
			int n = sysUserMapper.updateSysUserPassword(userData);
			new SendMail().resetPasswordEmail(user);
			
			if( n==0 )
			{
				return new MethodResult(MethodResult.FAIL, "重置失败");
			}
			return new MethodResult(MethodResult.SUCCESS, "重置成功");
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("重置失败");
		}
	}

	@Callable
	@Transactional(readOnly = false)
	@CallWithoutLogin
	public MethodResult resetPasswordByPhone(Map<String, Object> parameter) {
		
		logger.debug("TerminalUserServiceImpl.resetPasswordByPhone()");
		try
		{
			HttpServletRequest request = RequestContext.getHttpRequest();
			// 参数处理
			String  id            = StringUtil.trim(parameter.get("terminalUser_id"));
			String password       = StringUtil.trim(parameter.get("password"));
			String phone          = StringUtil.trim(parameter.get("phone"));
			String messageCode    = StringUtil.trim(parameter.get("messageCode"));
			//发送的验证码
			String mobileCode     = request.getSession().getAttribute("resetPasswordMobileCode").toString();
			if( id.isEmpty() )
			{
				return new MethodResult(MethodResult.FAIL, "id不能为空");
			}
			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
			TerminalUserVO terminalUserVO = terminalUserMapper.getBaseInfoById(id);
			if (password.isEmpty()) {
				return new MethodResult(MethodResult.FAIL, "密码不能为空");
			}
			if (phone.isEmpty()) {
				return new MethodResult(MethodResult.FAIL,"电话号码不能为空");
			}
			if (!phone.equals(terminalUserVO.getPhone())) {
				return new MethodResult(MethodResult.FAIL,"电话号码不正确");
			}
			if (messageCode.isEmpty()) {
				return new MethodResult(MethodResult.FAIL, "手机验证码不能为空");
			}
			if (!mobileCode.equals(messageCode)) {
				return new MethodResult(MethodResult.FAIL,"手机验证码不正确");
			}
			// 更新系统用户表
			SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
			Map<String, Object> userData = new LinkedHashMap<String, Object>();
			userData.put("id",       id);
			userData.put("password", LoginHelper.toEncryptedPassword(password));
			int n = sysUserMapper.updateSysUserPassword(userData);
			if( n==0 )
			{
				return new MethodResult(MethodResult.FAIL, "重置失败");
			}
			return new MethodResult(MethodResult.SUCCESS, "重置成功");
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("重置失败");
		}
	}

	@Callable
	@CallWithoutLogin
	public MethodResult resetPasswordSendEmailCode(Map<String, String> parameter)
	{
		logger.debug("TerminalUserServiceImpl.sendMailForChangePassword()");
		try
		{
			HttpServletRequest request = RequestContext.getHttpRequest();
			// 参数处理
			String  id= StringUtil.trim(parameter.get("terminalUser_id"));
			if (id.isEmpty()) 
			{
				return new MethodResult(MethodResult.FAIL,"id不能为空");
			}
			// 判断账号是否已经存在
			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
			TerminalUserVO terminalUserVO = terminalUserMapper.getTerminalUserById(id);
			if( terminalUserVO == null )
			{
				return new MethodResult(MethodResult.FAIL, "未绑定云端在线");
			}
			
			//验证码
			Integer code = (Integer)((int)((Math.random()*9+1)*100000));
			Map<String, Object> user = new LinkedHashMap<String, Object>();
			user.put("email", terminalUserVO.getEmail());
			user.put("code", code);
			new SendMail().resetPasswordEmailCode(user); 
			request.getSession().setAttribute("resetPasswordEmailOrPhoneCode", code);
			return new MethodResult(MethodResult.SUCCESS,"发送成功");
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("发送失败");
		}
	}

	@Callable
	@CallWithoutLogin
	public MethodResult resetPasswordSendPhoneCode(Map<String, String> parameter)
	{ 
		logger.debug("TerminalUserServiceImpl.resetPasswordSendPhoneCode()");
		try {
			HttpServletRequest request = RequestContext.getHttpRequest();
			// 参数处理
			String  id= StringUtil.trim(parameter.get("terminalUser_id"));
			if (id.isEmpty()) 
			{
				return new MethodResult(MethodResult.FAIL,"id不能为空");
			}
			// 判断账号是否已经存在
			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
			TerminalUserVO terminalUserVO = terminalUserMapper.getTerminalUserById(id);
			if( terminalUserVO == null )
			{
				return new MethodResult(MethodResult.FAIL, "未绑定云端在线");
			}
			//发送短信
			Integer code = (Integer)((int)((Math.random()*9+1)*100000));
		    String message = new String("【致云科技】致云Zhicloud欢迎您，您的手机验证码是：" + code + "，请及时完成验证。"); 
			String state  =new SendSms().zhicloudSendSms(terminalUserVO.getPhone(),message);
			if (("1").equals(state)) 
			{
				System.out.println("短信已经成功发送");
				request.getSession().setAttribute("resetPasswordEmailOrPhoneCode", code);
				return new MethodResult(MethodResult.SUCCESS, "短信已成功发送");
			}
			else
			{
				System.out.println("短信发送失败，错误代码是："+state);
				return new MethodResult(MethodResult.FAIL, "发送失败");
			}
		} catch (Exception e) 
		{
			logger.error(e);
			throw new AppException("发送失败");
		}
	}

	@Callable
	@Transactional(readOnly = false)
	@CallWithoutLogin
	public MethodResult resetPasswordCheckEmailOrPhoneCode(Map<String, String> parameter)
	{
		logger.debug("TerminalUserServiceImpl.resetPasswordCheckEmailOrPhoneCode()");
		try 
		{
			HttpServletRequest request = RequestContext.getHttpRequest();
			String  id= StringUtil.trim(parameter.get("terminalUser_id"));
			String myGetPhoneMessage  = StringUtil.trim(parameter.get("myGetPhoneMessage"));
			String myGetEmailMessage  = StringUtil.trim(parameter.get("myGetEmailMessage"));
			String code = request.getSession().getAttribute("resetPasswordEmailOrPhoneCode").toString();
			
			SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
			TerminalUserVO terminalUserVO = terminalUserMapper.getTerminalUserById(id);
			//邮箱码验证
			if (!myGetEmailMessage.isEmpty()) {
				if (myGetEmailMessage.equals(code)) 
				{
					//邮件发送重置后的密码
					String password = RandomPassword.getRandomNum(MailConstant.PASSWORD_LENGTH);
					Map<String, Object> userData = new LinkedHashMap<String, Object>();
					userData.put("id",       id);
					userData.put("password", LoginHelper.toEncryptedPassword(password));
					int n = sysUserMapper.updateSysUserPassword(userData);
					if (n<=0) {
						throw new AppException("重置失败");
					}
					Map<String, Object> user = new LinkedHashMap<String, Object>();
					user.put("password", password);
					user.put("email",    terminalUserVO.getEmail());
					new SendMail().resetPasswordEmail(user);
					return new MethodResult(MethodResult.SUCCESS,terminalUserVO.getEmail());
				}else {
					return new MethodResult(MethodResult.FAIL,"验证码不正确");
				}
			}
			//短信验证码
			if (!myGetPhoneMessage.isEmpty())
			{
				if (myGetPhoneMessage.equals(code)) 
				{
					//电话发送重置密码
					String password = RandomPassword.getRandomNum(MailConstant.PASSWORD_LENGTH);
					Map<String, Object> userData = new LinkedHashMap<String, Object>();
					userData.put("id",       id);
					userData.put("password", LoginHelper.toEncryptedPassword(password));
					int n = sysUserMapper.updateSysUserPassword(userData);
					if (n<=0) {
						throw new AppException("重置失败");
					}
					String message = new String("【致云科技】由于您忘记了密码，申请重置，以下是系统为您提供的初始化密码：" + password + "，为了您账号的安全，请登录后及时修改。"); 
					String state  =new SendSms().zhicloudSendSms(terminalUserVO.getPhone(),message);
					if (("1").equals(state)) 
					{
						System.out.println("短信已经成功发送");
						return new MethodResult(MethodResult.SUCCESS,terminalUserVO.getPhone());
					}
					else
					{
						System.out.println("短信发送失败，错误代码是："+state);
						return new MethodResult(MethodResult.FAIL, "发送失败");
					}
				}else {
					return new MethodResult(MethodResult.FAIL,"验证码不正确");
				}
				
			}
			else {
					return new MethodResult(MethodResult.FAIL,"验证失败");
			}
		} catch (Exception e) 
		{
			logger.error(e);
			throw new AppException("验证码不正确");
		}
	} 
	
	

	@Callable
	@CallWithoutLogin
	public MethodResult checkSmsRegister(Map<String, String> parameter)
	{ 
		
		logger.debug("TerminalUserServiceImpl.checkSmsRegister()");
		try {
			HttpServletRequest request = RequestContext.getHttpRequest();
			
			String account          = StringUtil.trim(parameter.get("account"));
			String password         = StringUtil.trim(parameter.get("password"));
			String phone            = StringUtil.trim(parameter.get("phone"));
			String verificationCode = StringUtil.trim(parameter.get("verificationCode"));
			if (account.isEmpty()) 
			{
				return new MethodResult(MethodResult.FAIL, "用户名不能为空");
			}
			if (password.isEmpty())
			{
				return new MethodResult(MethodResult.FAIL, "密码不能为空");
			}
			if (phone.isEmpty()) 
			{
				return new MethodResult(MethodResult.FAIL, "手机号码不能为空");
			}
			//判断手机是否绑定
			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
			TerminalUserVO terminalUser = terminalUserMapper.getTerminalUserByPhone(phone);
			if (terminalUser != null) {
				return new MethodResult(MethodResult.FAIL,"该手机已绑定云端在线账号");
			}
			if( VerificationCodeHelper.isMatch(request, verificationCode)==false )
			{
				return new MethodResult(MethodResult.FAIL, "验证码错误");
			}
			
			//获取ip
//			String ip = null;
//		    Enumeration<?> enu = request.getHeaderNames();
//		    while (enu.hasMoreElements()) {
//		      String name = (String)enu.nextElement();
//		      if (name.equalsIgnoreCase("X-Forwarded-For")) {
//		        ip = request.getHeader(name);
//		      }
//		      else if (name.equalsIgnoreCase("Proxy-Client-IP")) {
//		        ip = request.getHeader(name);
//		      }
//		      else if (name.equalsIgnoreCase("WL-Proxy-Client-IP")) {
//		        ip = request.getHeader(name);
//		      }
//		      if ((ip != null) && (ip.length() != 0))
//		        break;
//		    }
//		    if ((ip == null) || (ip.length() == 0)){
//		      ip = request.getRemoteAddr();
//		    }
//		    System.out.println(ip);
			
			//发送短信
			Integer loginMobileCode = (Integer)((int)((Math.random()*9+1)*100000));
			
		    String message = new String("【致云科技】致云Zhicloud欢迎您的注册，您的手机验证码是：" + loginMobileCode + "，请在30分钟内完成验证。"); 
			
			String state  = new SendSms().zhicloudSendSms(phone,message);

//			System.out.println("----------------"+phone+"-----------------------");
//			System.out.println("----------------"+loginMobileCode+"-------------------");
			
			if (("1").equals(state)) 
			{
				System.out.println("短信已成功发送");
				request.getSession().setAttribute("mySetPhone", phone);
				request.getSession().setAttribute("loginMobileCode", loginMobileCode);
				return new MethodResult(MethodResult.SUCCESS, "短信已成功发送");
			}
			else
			{
				System.out.println("短信发送失败，错误代码是："+state);
				return new MethodResult(MethodResult.FAIL, "发送失败");
			}
		} catch (Exception e) 
		{
			logger.error(e);
			throw new AppException("发送失败");
		}
	}
	
	@Callable
	@Transactional(readOnly = false)
	@CallWithoutLogin
	public MethodResult checkAccount(Map<String, String> parameter)
	{ 
		logger.debug("TerminalUserServiceImpl.checkAccount()");
		try {
			
			String account       = StringUtil.trim(parameter.get("account"));
			
			// 判断账号是否已经存在
			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
			TerminalUserVO terminalUser = terminalUserMapper.getTerminalUserByAccount(account);
			if( terminalUser != null )
			{
				return new MethodResult(MethodResult.FAIL, "账号[" + account + "]已经存在");
			}
			else 
			{
				return new MethodResult(MethodResult.SUCCESS,"可用账号");
			}
		} catch (Exception e) 
		{
			logger.error(e);
			throw new AppException("获取信息失败");
		}
	}
	
	@Callable
	@Transactional(readOnly = false)
	public MethodResult getPhoneAndEmailForSendMessage(String userId, int day)
	{
		logger.debug("TerminalUserServiceImpl.getPhoneAndEmailForSendMassage()");
		
		try
		{
			if (userId.isEmpty())
			{
				return new MethodResult(MethodResult.FAIL, "id不能为空");
			}
			if (String.valueOf(day).isEmpty())
			{
				return new MethodResult(MethodResult.FAIL, "day不能为空");
			}
			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
			TerminalUserVO terminalUserVO = terminalUserMapper.getTerminalUserById(userId);
			if (terminalUserVO == null)
			{
				return new MethodResult(MethodResult.FAIL, "该用户不存在");
			}
			String account = terminalUserVO.getAccount();
			if (!terminalUserVO.getPhone().isEmpty())
			{
				
				String message = new String("【致云科技】尊敬的" + account + "您好，您的余额还可以支付" + day + "天的费用，为不影响您云主机的正常使用，请及时充值。谢谢");
				String state = new SendSms().zhicloudSendSms(terminalUserVO.getPhone(), message);
				if (("1").equals(state))
				{
					System.out.println("短信已经成功发送");
				}
				else
				{
					System.out.println("短信发送失败，错误代码是：" + state);
				}
			}
			if (!terminalUserVO.getEmail().isEmpty())
			{
				// 获取注册用户的邮箱
				String email = terminalUserVO.getEmail();
				Map<String, Object> terminalUserData = new LinkedHashMap<String, Object>();
				terminalUserData.put("email", email);
				terminalUserData.put("account", account);
				terminalUserData.put("day", day);
				new SendMail().sendRechargeMessaToTerminalUser(terminalUserData);
			}
			return new MethodResult(MethodResult.SUCCESS, "发送成功");
			
		}
		catch (Exception e)
		{
			logger.error(e);
			throw new AppException("发送失败");
		}
	}
	
	@Callable
	public MethodResult informUserFeeInfo(String userId, String hostName, Date startTime, Date endTime, BigDecimal remainBalance, BigDecimal fee, BigDecimal monthlyPrice)
	{
		logger.debug("TerminalUserServiceImpl.informUserFeeInfo()");
		try
		{
			if (userId.isEmpty())
			{
				return new MethodResult(MethodResult.FAIL, "id不能为空");
			}
			
			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
			
			TerminalUserVO terminalUserVO = terminalUserMapper.getTerminalUserById(userId);
			if (terminalUserVO == null)
			{
				throw new AppException("该用户不存在");
			}
			
			String account = terminalUserVO.getAccount();
			
			String strBillingStartTime = DateUtil.dateToString(startTime, "yyyy-MM-dd HH:mm");
			String strBillingEndTime   = DateUtil.dateToString(endTime,   "yyyy-MM-dd HH:mm");
			
			int days = NumberUtil.divide(remainBalance, monthlyPrice).multiply(new BigDecimal(30)).intValue();
			String remainTimeText = days+"天";
			if( days > 365 )
			{
				remainTimeText = NumberUtil.scale(days/365.0, 2) +"年";
			}
			else if( days > 31 )
			{
				remainTimeText = NumberUtil.scale(days/30.0, 2)+"月";
			}
			
			if( StringUtil.isBlank(terminalUserVO.getPhone())==false )
			{
				String message = "";
				if( remainBalance.compareTo(BigDecimal.ZERO) > 0 )
				{
					message = "【致云科技】您好，从您的账户扣除云主机["+hostName+"] "+strBillingStartTime+" 至 "+strBillingEndTime+" 的使用费"+fee+"元，扣费之后账户剩余"+remainBalance+"元，您的云主机预计还可以使用"+remainTimeText+"。";
				}
				else
				{
					message = "【致云科技】您好，从您的账户扣除云主机["+hostName+"] "+strBillingStartTime+" 至 "+strBillingEndTime+" 的使用费"+fee+"元，扣费之后您的账户余额不足0元，您的所有云主机将被暂停使用，如果您希望继续使用这些云主机，请于7天内充值激活云主机，超过7天未激活的云主机将被销毁。";
				}
				String state = new SendSms().zhicloudSendSms(terminalUserVO.getPhone(), message);
				if( "1".equals(state) )
				{
					System.out.println("短信已经成功发送");
				}
				else
				{
					System.out.println("短信发送失败，错误代码是：" + state);
				}
			}
			
			if( StringUtil.isBlank(terminalUserVO.getEmail())==false )
			{
				String content = "";
				if( remainBalance.compareTo(BigDecimal.ZERO) > 0 )
				{
					content = 	"<body>"
								+ "尊敬的"
								+ account
								+ ":"
								+ "<br>"
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您好！<br>"
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;从您的账户扣除云主机["+hostName+"] "+strBillingStartTime+" 至 "+strBillingEndTime+" 的使用费"+fee+"元，扣费之后账户剩余"+remainBalance+"元，您的云主机预计还可以使用"+remainTimeText+"。"
								+ "<br>"
								+ "<br>致云科技感谢您的支持！"
								+ "</body>";
				}
				else
				{
					content = 	"<body>"
								+ "尊敬的"
								+ account
								+ ":"
								+ "<br>"
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;您好！<br>"
								+ "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;从您的账户扣除云主机["+hostName+"] "+strBillingStartTime+" 至 "+strBillingEndTime+" 的使用费"+fee+"元，扣费之后您的账户余额不足0元，您的所有云主机将被暂停使用，如果您希望继续使用这些云主机，请于7天内充值激活云主机，超过7天未激活的云主机将被销毁。"
								+ "<br>"
								+ "<br>致云科技感谢您的支持！"
								+ "</body>";
				}
				
				
				// 获取注册用户的邮箱
				Map<String, Object> terminalUserData = new LinkedHashMap<String, Object>();
				terminalUserData.put("recipientEmail", terminalUserVO.getEmail());
				terminalUserData.put("content",        content);
				new SendMail().sendMessaToTerminalUser(terminalUserData);
			}
			
			return new MethodResult(MethodResult.SUCCESS, "发送成功");
		}
		catch (Exception e)
		{
			logger.error(e);
			throw new AppException("发送失败");
		}
	}
	
 
	
	
	@Callable
	public MethodResult changeEmailOrPhone(Map<String, String> parameter)
	{ 
		logger.debug("TerminalUserServiceImpl.changeEmailOrPhone()");
		try {
			HttpServletRequest request = RequestContext.getHttpRequest();
			String phone       = StringUtil.trim(parameter.get("mobilePhone"));
			if (phone.isEmpty()) 
			{
				return new MethodResult(MethodResult.FAIL, "手机号码不能为空");
			}
			//发送短信
			Integer code = (Integer)((int)((Math.random()*9+1)*100000));
		    String message = new String("【致云科技】致云Zhicloud欢迎您，您的手机验证码是：" + code + "，请及时完成验证。"); 
			String state  = new SendSms().zhicloudSendSms(phone,message);
			if (("1").equals(state)) 
			{
				System.out.println("短信已成功发送");
				request.getSession().setAttribute("changeEmailOrMobileCode", code);
				return new MethodResult(MethodResult.SUCCESS, "短信已成功发送");
			}
			else
			{
				System.out.println("短信发送失败，错误代码是："+state);
				return new MethodResult(MethodResult.FAIL, "发送失败");
			}
		} catch (Exception e) 
		{
			logger.error(e);
			throw new AppException("信息发送失败");
		}
	}
	@Callable
	public MethodResult changeEmailCheckEmailOrPhone(Map<String, String> parameter)
	{
		logger.debug("TerminalUserServiceImpl.changeEmailCheckEmailorPhone()");
		try 
		{
			HttpServletRequest request = RequestContext.getHttpRequest();
			String myGetPhoneMessage  = StringUtil.trim(parameter.get("myGetPhoneMessage"));
			String myGetEmailMessage  = StringUtil.trim(parameter.get("myGetEmailMessage"));
			String code = request.getSession().getAttribute("changeEmailOrMobileCode").toString();
			//邮箱码验证
			if (!myGetEmailMessage.isEmpty()) {
				if (myGetEmailMessage.equals(code)) 
				{
					return new MethodResult(MethodResult.SUCCESS,"验证正确");
				}else {
					return new MethodResult(MethodResult.FAIL,"验证码不正确");
				}
			}
			//短信验证码
			if (!myGetPhoneMessage.isEmpty())
			{
				if (myGetPhoneMessage.equals(code)) 
				{
					return new MethodResult(MethodResult.SUCCESS,"验证正确");
				}else {
					return new MethodResult(MethodResult.FAIL,"验证码不正确");
				}
				
			}
			else {
					return new MethodResult(MethodResult.FAIL,"验证失败");
			}
		} catch (Exception e) 
		{
			logger.error(e);
			throw new AppException("验证码不正确");
		}
	} 
	
	
	public static void main(String[] args)
	{
		
		int days = 444;
		String remainTimeText = days+"天";
		if( days > 365 )
		{
			remainTimeText = NumberUtil.scale(days/365.0, 2) +"年";
		}
		else if( days > 31 )
		{
			remainTimeText = NumberUtil.scale(days/30.0, 2)+"月";
		}
		System.out.println(remainTimeText);
		
	}

	@Override
	@Transactional(readOnly = false)
	public void sendHint() {
		logger.debug("TerminalUserServiceImpl.sendHint()");
		try{
			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
			CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
			UserMessageMapper userMessageMapper = this.sqlSession.getMapper(UserMessageMapper.class);
			UserDictionaryMapper userDictionaryMapper = this.sqlSession.getMapper(UserDictionaryMapper.class);
			//获取所有终端用户(包括代理商和运营商创建的)
			List<TerminalUserVO> terminalUserList = terminalUserMapper.getAll();
			if(terminalUserList!=null && terminalUserList.size() > 0){
				for(TerminalUserVO terminalUser : terminalUserList){
					//取出每个用户的所有云主机
					List<CloudHostVO> cloudHostList = cloudHostMapper.getByUserId(terminalUser.getId());
					BigDecimal totalPrice = new BigDecimal("0");
					if(cloudHostList!=null&&cloudHostList.size()>0){
						for(CloudHostVO vo:cloudHostList){
							//取出每一台云主机判断是否为终端用户所创建
							if(vo.getType() == 3){
								if(!(vo.getSummarizedStatusText().equals("创建失败")||vo.getSummarizedStatusText().equals("停机"))&&vo.getMonthlyPrice()!=null){						
									totalPrice = vo.getMonthlyPrice().add(totalPrice);
								}
							}
						} 
						
					}else{
						continue;
					}
					if(terminalUser.getAccountBalance() == null){
						continue;
					}
					if (terminalUser.getPercentOff()!=null) {
						totalPrice=totalPrice.multiply(new BigDecimal(100).subtract(terminalUser.getPercentOff())).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
					}
					//计算出每天的费用
					totalPrice = totalPrice.divide(new BigDecimal("31"),0,BigDecimal.ROUND_HALF_UP);
					BigDecimal balance = terminalUser.getAccountBalance().setScale(0, BigDecimal.ROUND_HALF_UP);
					BigDecimal newBalance = balance;
					balance = balance.subtract(totalPrice.divide(new BigDecimal(24),2,BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal(7)));
					//余额只够使用7天是提示
					if(balance.compareTo(totalPrice.multiply(new BigDecimal(8)))<0 && balance.compareTo(totalPrice.multiply(new BigDecimal(7))) >= 0){
						SendMail sm = new SendMail();
						sm.sendHintEmail(terminalUser, "7","【余额不足】通知-致云Zhicloud");
						String message = new String("【致云科技】尊敬的用户，您好！您的账户余额还可供您当前所有产品使用7天，为了不影响您的业务情况，请您及时充值，谢谢。"); 
						String state  = new SendSms().zhicloudSendSms(terminalUser.getPhone(),message);
						if("1".equals(state)){
//							System.out.println("-----success-----");
						}else{
//							System.out.println("-----fail-----:"+state);
						}
						Map<String, Object> userDictionaryData = new LinkedHashMap<String, Object>();
						userDictionaryData.put("userId", terminalUser.getId());
						userDictionaryData.put("key", "sendTimes");
						userDictionaryMapper.deleteDictionaryByUserId(userDictionaryData);
						Map<String, Object> userMessageData = new LinkedHashMap<String, Object>();
						userMessageData.put("id",StringUtil.generateUUID());
						userMessageData.put("userId",terminalUser.getId());
						userMessageData.put("content","您的余额仅够使用7天，到时将暂停服务，请您及时充值。");
						userMessageData.put("createTime",StringUtil.dateToString(new Date(),"yyyyMMddHHmmssSSS"));
						userMessageData.put("status",4);
						userMessageMapper.insertReg(userMessageData);
						continue;
					} 
					//余额只够使用3天时提示
					if(balance.compareTo(totalPrice.multiply(new BigDecimal(4)))<0 && balance.compareTo(totalPrice.multiply(new BigDecimal(3))) >= 0){
						SendMail sm = new SendMail();
						sm.sendHintEmail(terminalUser, "3","【余额不足】通知-致云Zhicloud");
						String message = new String("【致云科技】尊敬的用户，您好！您的账户余额还可供您当前所有产品使用3天，为了不影响您的业务情况，请您及时充值，谢谢。"); 
						String state  = new SendSms().zhicloudSendSms(terminalUser.getPhone(),message);
						if("1".equals(state)){
//							System.out.println("-----success-----");
						}else{
//							System.out.println("-----fail-----:"+state);
						}
						Map<String, Object> userDictionaryData = new LinkedHashMap<String, Object>();
						userDictionaryData.put("userId", terminalUser.getId());
						userDictionaryData.put("key", "sendTimes");
						userDictionaryMapper.deleteDictionaryByUserId(userDictionaryData);
						Map<String, Object> userMessageData = new LinkedHashMap<String, Object>();
						userMessageData.put("id",StringUtil.generateUUID());
						userMessageData.put("userId",terminalUser.getId());
						userMessageData.put("content","您的余额仅够使用3天，到时将暂停服务，请您及时充值。");
						userMessageData.put("createTime",StringUtil.dateToString(new Date(),"yyyyMMddHHmmssSSS"));
						userMessageData.put("status",4);
						userMessageMapper.insertReg(userMessageData); 
						continue;
					} 
					//余额只够使用2天时提示
					if(balance.compareTo(totalPrice.multiply(new BigDecimal(3)))<0 && balance.compareTo(totalPrice.multiply(new BigDecimal(2))) >= 0){
						SendMail sm = new SendMail();
						sm.sendHintEmail(terminalUser, "2","【余额不足】通知-致云Zhicloud");
						String message = new String("【致云科技】尊敬的用户，您好！您的账户余额还可供您当前所有产品使用2天，为了不影响您的业务情况，请您及时充值，谢谢。"); 
						String state  = new SendSms().zhicloudSendSms(terminalUser.getPhone(),message);
						if("1".equals(state)){
//							System.out.println("-----success-----");
						}else{
//							System.out.println("-----fail-----:"+state);
						}
						Map<String, Object> userDictionaryData = new LinkedHashMap<String, Object>();
						userDictionaryData.put("userId", terminalUser.getId());
						userDictionaryData.put("key", "sendTimes");
						userDictionaryMapper.deleteDictionaryByUserId(userDictionaryData);
						Map<String, Object> userMessageData = new LinkedHashMap<String, Object>();
						userMessageData.put("id",StringUtil.generateUUID());
						userMessageData.put("userId",terminalUser.getId());
						userMessageData.put("content","您的余额仅够使用2天，到时将暂停服务，请您及时充值。");
						userMessageData.put("createTime",StringUtil.dateToString(new Date(),"yyyyMMddHHmmssSSS"));
						userMessageData.put("status",4);
						userMessageMapper.insertReg(userMessageData);
						continue;
					} 
					//余额只够使用1天时提示
					if(balance.compareTo(totalPrice.multiply(new BigDecimal(2)))<0 && balance.compareTo(new BigDecimal(1))>=0){
						SendMail sm = new SendMail();
						sm.sendHintEmail(terminalUser, "1","【余额不足】通知-致云Zhicloud");
						String message = new String("【致云科技】尊敬的用户，您好！您的账户余额还可供您当前所有产品使用1天，为了不影响您的业务情况，请您及时充值，谢谢。"); 
						String state  = new SendSms().zhicloudSendSms(terminalUser.getPhone(),message);
						if("1".equals(state)){
//							System.out.println("-----success-----");
						}else{
//							System.out.println("-----fail-----:"+state);
						}
						Map<String, Object> userDictionaryData = new LinkedHashMap<String, Object>();
						userDictionaryData.put("userId", terminalUser.getId());
						userDictionaryData.put("key", "sendTimes");
						userDictionaryMapper.deleteDictionaryByUserId(userDictionaryData);
						Map<String, Object> userMessageData = new LinkedHashMap<String, Object>();
						userMessageData.put("id",StringUtil.generateUUID());
						userMessageData.put("userId",terminalUser.getId());
						userMessageData.put("content","您的余额仅够使用1天，到时将暂停服务，请您及时充值。");
						userMessageData.put("createTime",StringUtil.dateToString(new Date(),"yyyyMMddHHmmssSSS"));
						userMessageData.put("status",4);
						userMessageMapper.insertReg(userMessageData);
					}
					Map<String, Object> _userDictionaryData = new LinkedHashMap<String, Object>();
					_userDictionaryData.put("userId", terminalUser.getId());
					_userDictionaryData.put("key", "sendTimes");
					int n = userDictionaryMapper.getByUserId(_userDictionaryData);
					if(n>0){
						continue;
					}
					//余额为0时提示
					if(newBalance.compareTo(BigDecimal.ZERO) <= 0){
						SendMail sm = new SendMail();
						sm.sendHintEmail(terminalUser, "0","【余额为零】通知-致云Zhicloud");
						String message = new String("【致云科技】尊敬的用户，您好！您的账户余额已为零，目前已暂停您所有产品的服务，为了不影响您的业务情况，请您尽快充值开通服务，谢谢。"); 
						String state  = new SendSms().zhicloudSendSms(terminalUser.getPhone(),message);
						if("1".equals(state)){
//							System.out.println("-----success-----");
						}else{
//							System.out.println("-----fail-----:"+state);
						}
						Map<String, Object> userDictionaryData = new LinkedHashMap<String, Object>();
						userDictionaryData.put("id", StringUtil.generateUUID());
						userDictionaryData.put("userId", terminalUser.getId());
						userDictionaryData.put("key", "sendTimes");
						userDictionaryMapper.addUserDictionary(userDictionaryData);
						Map<String, Object> userMessageData = new LinkedHashMap<String, Object>();
						userMessageData.put("id",StringUtil.generateUUID());
						userMessageData.put("userId",terminalUser.getId());
						userMessageData.put("content","您的余额已为零，目前已暂停服务，请您及时充值。");
						userMessageData.put("createTime",StringUtil.dateToString(new Date(),"yyyyMMddHHmmssSSS"));
						userMessageData.put("status",4);
						userMessageMapper.insertReg(userMessageData);
					}
				}
			}
		}catch(Exception e){
			logger.error(e);
			throw new AppException(e);
		}
	}

	@Callable
	public String viewItemPage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("TerminalUserServiceImpl.viewItemPage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		int userType = loginInfo.getUserType();
		if( loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_OPERATOR && loginInfo.hasPrivilege(PrivilegeConstant.terminal_user_manage_add) == false )
		{
			return "/public/have_not_access_dialog.jsp";
		}else if(loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT && loginInfo.hasPrivilege(PrivilegeConstant.terminal_user_manage_add_agent) == false){
			return "/public/have_not_access_dialog.jsp";
		}
		// 参数处理
		String terminalUserId = StringUtil.trim(request.getParameter("terminalUserId"));
		if( StringUtil.isBlank(terminalUserId) )
		{
			throw new AppException("terminalUserId不能为空");
		}
		TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
		TerminalUserVO terminalUser = terminalUserMapper.getTerminalUserById(terminalUserId);
		if(terminalUser==null){
			return "/security/agent/page_not_exists.jsp";
		}
		request.setAttribute("terminalUser", terminalUser);
		
		//判断用户类型，然后跳转到相应页面
		return "/security/agent/terminal_user_view_item.jsp";
		
	}
	
	@Callable
	public void viewItem(HttpServletRequest request,HttpServletResponse response) {
		logger.debug("TerminalUserServiceImpl.viewItem()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_OPERATOR && loginInfo.hasPrivilege(PrivilegeConstant.terminal_user_manage_modify) == false )
		{
			throw new AppException("您没有权限进行些操作");
		}else if(loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT && loginInfo.hasPrivilege(PrivilegeConstant.terminal_user_manage_modify_agent) == false){
			throw new AppException("您没有权限进行些操作");
		}
		
		// 参数处理
		String terminalUserId = StringUtil.trim(request.getParameter("terminalUserId"));
		Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
		Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 10);
		if( StringUtil.isBlank(terminalUserId) )
		{
			throw new AppException("terminalUserId不能为空");
		}
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/json; charset=utf-8");
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DAY_OF_MONTH, -7);
		String dayTime = DateUtil.dateToString(calendar.getTime(), "yyyyMMddHHmmssSSS");
		// 查询数据库
		Map<String, Object> condition = new LinkedHashMap<String, Object>();
		condition.put("userId", terminalUserId);
		condition.put("start_row", page * rows);
		condition.put("row_count", rows);
		condition.put("time", dayTime);
		
		//
		CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
		int total = cloudHostMapper.queryHostCount(condition);	// 总行数
		List<CloudHostVO> cloudHostList = cloudHostMapper.getByTerminalUserId(condition);
//		List<CloudDiskVO> cloudDiskList = cloudDiskMapper.getCloudDiskByUserId(terminalUserId);
//		request.setAttribute("cloudHostList", cloudHostList);
//		request.setAttribute("cloudDiskList", cloudDiskList);
		try {
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, cloudHostList);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
