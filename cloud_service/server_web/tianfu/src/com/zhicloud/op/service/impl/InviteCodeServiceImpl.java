package com.zhicloud.op.service.impl;

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
import com.zhicloud.op.common.util.SendMail;
import com.zhicloud.op.common.util.SendSms;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.login.LoginInfo;
import com.zhicloud.op.mybatis.mapper.InviteCodeMapper;
import com.zhicloud.op.mybatis.mapper.TerminalUserMapper;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.request.RequestContext;
import com.zhicloud.op.service.InviteCodeService;
import com.zhicloud.op.service.constant.AppConstant;
import com.zhicloud.op.vo.InviteCodeVO;
import com.zhicloud.op.vo.TerminalUserVO;


@Transactional(readOnly = true)
public class InviteCodeServiceImpl extends BeanDirectCallableDefaultImpl implements InviteCodeService
{
	public static final Logger logger = Logger.getLogger(InviteCodeServiceImpl.class);

	private SqlSession sqlSession;

	public void setSqlSession(SqlSession sqlSession)
	{
		this.sqlSession = sqlSession;
	}
	
	@Callable
	public void queryInviteCodeForTerminalUser(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("TerminalUserServiceImpl.queryInviteCodeForAgent()");
		try
		{
			// 权限判断
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			if(loginInfo.getUserType()==AppConstant.SYS_USER_TYPE_AGENT && loginInfo.hasPrivilege(PrivilegeConstant.agent_invite_code_manage_page) == false)
			{
				throw new AppException("您没有权限进行此操作");
			}
			
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");

			// 获取参数
//			String terminalUserAccount = StringUtil.trim(request.getParameter("terminal_user_account"));
			Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
			Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 2);
			
//			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
			InviteCodeMapper inviteCodeMapper  = this.sqlSession.getMapper(InviteCodeMapper.class);
			
			// 查询数据库
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("createrId",      loginInfo.getUserId());
//			condition.put("terminalUserAccount", "%" + terminalUserAccount + "%");
			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			
//			System.out.println("===="+terminalUserAccount+"=====");
			
//			int total                             = terminalUserMapper.queryPageCount(condition);	// 总行数
//			List<TerminalUserVO> terminalUserList = terminalUserMapper.queryPage(condition);		// 分页结果
			
			int total                         = inviteCodeMapper.queryPageCount(condition);	// 总行数
			List<InviteCodeVO> inviteCodeList = inviteCodeMapper.queryPage(condition);		// 分页结果

			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, inviteCodeList);
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("查询失败");
		}
	}
	
	@Callable
	public void queryInviteCodeForAgent(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("TerminalUserServiceImpl.queryInviteCodeForAgent()");
		try
		{
			// 权限判断
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			if(loginInfo.getUserType()==AppConstant.SYS_USER_TYPE_AGENT && loginInfo.hasPrivilege(PrivilegeConstant.agent_invite_code_manage_page) == false)
			{
				throw new AppException("您没有权限进行此操作");
			}
			
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");

			// 获取参数
//			String terminalUserAccount = StringUtil.trim(request.getParameter("terminal_user_account"));
			Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
			Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 2);
			
//			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
			InviteCodeMapper inviteCodeMapper  = this.sqlSession.getMapper(InviteCodeMapper.class);
			
			// 查询数据库
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("createrId",      loginInfo.getUserId());
//			condition.put("terminalUserAccount", "%" + terminalUserAccount + "%");
			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			
//			System.out.println("===="+terminalUserAccount+"=====");
			
//			int total                             = terminalUserMapper.queryPageCount(condition);	// 总行数
//			List<TerminalUserVO> terminalUserList = terminalUserMapper.queryPage(condition);		// 分页结果
			
			int total                         = inviteCodeMapper.queryPageCount(condition);	// 总行数
			List<InviteCodeVO> inviteCodeList = inviteCodeMapper.queryPage(condition);		// 分页结果

			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, inviteCodeList);
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("查询失败");
		}
	}
	
	@Callable
	public void queryInviteCodeForOperator(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("TerminalUserServiceImpl.queryInviteCodeForOperator()");
		try
		{
			// 权限判断
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			if(loginInfo.getUserType()==AppConstant.SYS_USER_TYPE_OPERATOR && loginInfo.hasPrivilege(PrivilegeConstant.operator_invite_code_manage_page) == false)
			{
				throw new AppException("您没有权限进行此操作");
			}
			
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");

			// 获取参数
//			String terminalUserAccount = StringUtil.trim(request.getParameter("terminal_user_account"));
			Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
			Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 2);
			
//			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
			InviteCodeMapper inviteCodeMapper  = this.sqlSession.getMapper(InviteCodeMapper.class);
			
			// 查询数据库
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("createrId",      loginInfo.getUserId());
//			condition.put("terminalUserAccount", "%" + terminalUserAccount + "%");
			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			
//			System.out.println("===="+terminalUserAccount+"=====");
			
//			int total                             = terminalUserMapper.queryPageCount(condition);	// 总行数
//			List<TerminalUserVO> terminalUserList = terminalUserMapper.queryPage(condition);		// 分页结果
			
			int total                         = inviteCodeMapper.queryPageCount(condition);	// 总行数
			List<InviteCodeVO> inviteCodeList = inviteCodeMapper.queryPage(condition);		// 分页结果

			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, inviteCodeList);
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("查询失败");
		}
	}
	
	@Callable
	public String terminalInviteCodePage(HttpServletRequest request,HttpServletResponse response) 
	{
		logger.debug("AgentServiceImpl.terminalInviteCodePage()");
		
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		
		// 权限判断，判断是否为运营商、是否有权限
		if( AppConstant.SYS_USER_TYPE_TERMINAL_USER.equals(loginInfo.getUserType())==false)
		{
			return "/public/have_not_access.jsp";
		}
		
		return "/security/user/terminal_user_invite_code.jsp";
	}
	
	@Callable
	public String agentInviteCodePage(HttpServletRequest request,HttpServletResponse response) 
	{
		logger.debug("AgentServiceImpl.agentInviteCodePage()");
		
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		
		// 权限判断，判断是否为运营商、是否有权限
		if( AppConstant.SYS_USER_TYPE_AGENT.equals(loginInfo.getUserType())==false || 
			loginInfo.hasPrivilege(PrivilegeConstant.agent_invite_code_manage_page) == false )
		{
			return "/public/have_not_access.jsp";
		}
		
		return "/security/agent/agent_invite_code.jsp";
	}
	
	@Callable
	public String operatorInviteCodePage(HttpServletRequest request,HttpServletResponse response) 
	{
		logger.debug("AgentServiceImpl.operatorInviteCodePage()");
		
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		
		// 权限判断，判断是否为运营商、是否有权限
		if( AppConstant.SYS_USER_TYPE_OPERATOR.equals(loginInfo.getUserType())==false || 
			loginInfo.hasPrivilege(PrivilegeConstant.operator_invite_code_manage_page) == false )
		{
			return "/public/have_not_access.jsp";
		}
		
		return "/security/operator/operator_invite_code.jsp";
	}
	
	
	@Callable
	public String addInviteCodePageByAgent(HttpServletRequest request,HttpServletResponse response) 
	{
		logger.debug("AgentServiceImpl.addInviteCodePageByAgent()");
		
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		
		// 权限判断，判断是否为运营商、是否有权限
		if( AppConstant.SYS_USER_TYPE_AGENT.equals(loginInfo.getUserType())==false || 
			loginInfo.hasPrivilege(PrivilegeConstant.agent_invite_code_add) == false )
		{
			return "/public/have_not_access_dialog.jsp";
		}
		
		return "/security/agent/agent_invite_code_add.jsp";
	}
	
	@Callable
	public String addInviteCodePageByOperator(HttpServletRequest request,HttpServletResponse response) 
	{
		logger.debug("AgentServiceImpl.addInviteCodePageByOperator()");
		
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		
		// 权限判断，判断是否为运营商、是否有权限
		if( AppConstant.SYS_USER_TYPE_OPERATOR.equals(loginInfo.getUserType())==false || 
			loginInfo.hasPrivilege(PrivilegeConstant.operator_invite_code_add) == false )
		{
			return "/public/have_not_access_dialog.jsp";
		}
		
		return "/security/operator/operator_invite_code_add.jsp";
	}
	
	@Callable
	public String  sendInviteCodeByEmailPage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("TerminalUserServiceImpl.sendInviteCodeByEmailPage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_OPERATOR && loginInfo.hasPrivilege(PrivilegeConstant.operator_invite_code_send_email) == false )
		{
			return "/public/have_not_access_dialog.jsp";
		}else if(loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT && loginInfo.hasPrivilege(PrivilegeConstant.agent_invite_code_send_email) == false){
			return "/public/have_not_access_dialog.jsp";
		}
		
		// 参数处理
		String inviteCodeId = StringUtil.trim(request.getParameter("inviteCodeId"));
		if( StringUtil.isBlank(inviteCodeId) )
		{
			throw new AppException("inviteCodeId不能为空");
		}
		
		//
		InviteCodeMapper inviteCodeMapper =this.sqlSession.getMapper(InviteCodeMapper.class);	
		
		InviteCodeVO inviteCode =inviteCodeMapper.getInviteCodeById(inviteCodeId);
		
		if(loginInfo.getUserType()==AppConstant.SYS_USER_TYPE_OPERATOR){
			if( inviteCode != null)
			{	if ((AppConstant.INVITE_CODE_SEND_NO.toString()).equals(inviteCode.getStatus())) 
				{
					request.setAttribute("inviteCode", inviteCode);
					return "/security/operator/operator_invite_code_send_email.jsp";
				}
				else 
				{
					request.setAttribute("message", "该邀请码不可用");
					return "/public/warning_dialog.jsp";

				}
			}
			else
			{
				request.setAttribute("message", "找不到邀请码的记录");
				return "/public/warning_dialog.jsp";
			}
		}else{
			if( inviteCode != null)
			{
				if ((AppConstant.INVITE_CODE_SEND_NO.toString()).equals(inviteCode.getStatus())) 
				{
					request.setAttribute("inviteCode", inviteCode);
					return "/security/agent/agent_invite_code_send_email.jsp";
				}else 
				{
					request.setAttribute("message", "该邀请码不可用");
					return "/public/warning_dialog.jsp";
				}
			}
			else
			{
				request.setAttribute("message", "找不到邀请码的记录");
				return "/public/warning_dialog.jsp";
			}
		}
	}
	
	@Callable
	public String  sendInviteCodeByEmailTerminalUserPage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("TerminalUserServiceImpl.sendInviteCodeByEmailTerminalUserPage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if(AppConstant.SYS_USER_TYPE_TERMINAL_USER.equals(loginInfo.getUserType())==false )
		{
			return "/public/have_not_access_dialog.jsp";
		}
		// 参数处理
		String inviteCodeId = StringUtil.trim(request.getParameter("inviteCodeId"));
		if( StringUtil.isBlank(inviteCodeId) )
		{
			throw new AppException("inviteCodeId不能为空");
		}
		
		//
		InviteCodeMapper inviteCodeMapper =this.sqlSession.getMapper(InviteCodeMapper.class);	
		InviteCodeVO inviteCode =inviteCodeMapper.getInviteCodeById(inviteCodeId);
		
		
			if( inviteCode != null)
			{	
				if ((AppConstant.INVITE_CODE_SEND_NO.toString()).equals(inviteCode.getStatus())) 
				{
					request.setAttribute("inviteCode", inviteCode);
					return "/security/user/terminal_user_invite_code_send_email.jsp";
				}
				else
				{
					request.setAttribute("message", "该邀请码不可用");
					return "/public/warning_dialog.jsp";
				}
			}
			else
			{
				request.setAttribute("message", "找不到邀请码的记录");
				return "/public/warning_dialog.jsp";
			}
	}
	
	
	@Callable
	public String  sendInviteCodeByPhonePage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("TerminalUserServiceImpl.sendInviteCodeByPhonePage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_OPERATOR && loginInfo.hasPrivilege(PrivilegeConstant.operator_invite_code_send_phone) == false )
		{
			return "/public/have_not_access_dialog.jsp";
		}else if(loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT && loginInfo.hasPrivilege(PrivilegeConstant.agent_invite_code_send_phone) == false){
			return "/public/have_not_access_dialog.jsp";
		}
		
		// 参数处理
		String inviteCodeId = StringUtil.trim(request.getParameter("inviteCodeId"));
		if( StringUtil.isBlank(inviteCodeId) )
		{
			throw new AppException("inviteCodeId不能为空");
		}
		
		//
		InviteCodeMapper inviteCodeMapper =this.sqlSession.getMapper(InviteCodeMapper.class);	
		
		InviteCodeVO inviteCode =inviteCodeMapper.getInviteCodeById(inviteCodeId);
		
		if(loginInfo.getUserType()==AppConstant.SYS_USER_TYPE_OPERATOR){
			if( inviteCode != null)
			{
				if ((AppConstant.INVITE_CODE_SEND_NO.toString()).equals(inviteCode.getStatus())) 
				{
					request.setAttribute("inviteCode", inviteCode);
					return "/security/operator/operator_invite_code_send_phone.jsp";
				}
				else
				{
					request.setAttribute("message", "该邀请码不可用");
					return "/public/warning_dialog.jsp";
				}
				
			}
			else
			{
				request.setAttribute("message", "找不到邀请码的记录");
				return "/public/warning_dialog.jsp";
			}
		}else{
			if( inviteCode != null)
			{
				if ((AppConstant.INVITE_CODE_SEND_NO.toString()).equals(inviteCode.getStatus())) 
				{
					request.setAttribute("inviteCode", inviteCode);
					return "/security/agent/agent_invite_code_send_phone.jsp";
				}
				else 
				{
					request.setAttribute("message", "该邀请码不可用");
					return "/public/warning_dialog.jsp";
				}
			}
			else
			{
				request.setAttribute("message", "找不到邀请码的记录");
				return "/public/warning_dialog.jsp";
			}
		}
		
	}
	
	@Callable
	public String  sendInviteCodeByPhoneTerminalUserPage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("TerminalUserServiceImpl.sendInviteCodeByPhoneTerminalUserPage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( AppConstant.SYS_USER_TYPE_TERMINAL_USER.equals(loginInfo.getUserType())==false  )
		{
			return "/public/have_not_access_dialog.jsp";
		}
		
		// 参数处理
		String inviteCodeId = StringUtil.trim(request.getParameter("inviteCodeId"));
		if( StringUtil.isBlank(inviteCodeId) )
		{
			throw new AppException("inviteCodeId不能为空");
		}
		
		//
		InviteCodeMapper inviteCodeMapper =this.sqlSession.getMapper(InviteCodeMapper.class);	
		
		InviteCodeVO inviteCode =inviteCodeMapper.getInviteCodeById(inviteCodeId);
		
		
			if( inviteCode != null)
			{
				if ((AppConstant.INVITE_CODE_SEND_NO.toString()).equals(inviteCode.getStatus())) 
				{
					request.setAttribute("inviteCode", inviteCode);
					return "/security/user/terminal_user_invite_code_send_phone.jsp";
				}
				else 
				{
					request.setAttribute("message", "该邀请码不可用");
					return "/public/warning_dialog.jsp";
				}
				
			}
			else
			{
				request.setAttribute("message", "找不到邀请码的记录");
				return "/public/warning_dialog.jsp";
			}
	}
	
	
	@Callable
	@Transactional(readOnly = false)
	public MethodResult sendInviteCodeByPhone(Map<String, String> parameter)
	{ 
		
		logger.debug("TerminalUserServiceImpl.sendInviteCodeByPhone()");
		try {
//			HttpServletRequest request = RequestContext.getHttpRequest();
			
			String inviteCodeId = StringUtil.trim(parameter.get("inviteCode_id"));
			String inviteCode   = StringUtil.trim(parameter.get("inviteCode"));
			String phone        = StringUtil.trim(parameter.get("terminal_phone"));
			
//			System.out.println("---------"+inviteCodeId+"------"+phone+"---------"+inviteCode+"-----");
			
			if (inviteCodeId.isEmpty()) 
			{
				return new MethodResult(MethodResult.FAIL, "邀请码id不能为空");
			}
			if (inviteCode.isEmpty())
			{
				return new MethodResult(MethodResult.FAIL,"邀请码不能为空");
			}
			if (phone.isEmpty()) 
			{
				return new MethodResult(MethodResult.FAIL, "手机号码不能为空");
			}
			
			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
			//判断手机是否绑定
			TerminalUserVO terminalUser = terminalUserMapper.getTerminalUserByPhone(phone);
			if (terminalUser != null) {
				return new MethodResult(MethodResult.FAIL,"该手机已绑定云端在线账号");
			}
			
			//发送短信
		    String message = new String("【致云科技】亲爱的用户，您好。云端在线公有云邀请码："+inviteCode+"，本邀请码7天有效，请尽快注册使用，谢谢。"); 
			String state  = new SendSms().zhicloudSendSms(phone,message);
			
			if (state.equals("1")) 
			{
				// 更改数据库
				InviteCodeMapper inviteCodeMapper = this.sqlSession.getMapper(InviteCodeMapper.class);
				Map<String, Object> inviteCodeMap = new LinkedHashMap<String, Object>();
				inviteCodeMap.put("id",       inviteCodeId);
				inviteCodeMap.put("phone",    phone);
				inviteCodeMap.put("sendTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS") );
				inviteCodeMap.put("status",   AppConstant.INVITE_CODE_SEND_YES);
				inviteCodeMapper.updateSendTimeAndStatusAndPhone(inviteCodeMap);
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
	public MethodResult sendInviteCodeByEmail(Map<String, String> parameter)
	{ 
		
		logger.debug("TerminalUserServiceImpl.sendInviteCodeByEmail()");
		try {
//			HttpServletRequest request = RequestContext.getHttpRequest();
			
			String inviteCodeId = StringUtil.trim(parameter.get("inviteCode_id"));
			String inviteCode   = StringUtil.trim(parameter.get("inviteCode"));
			String email        = StringUtil.trim(parameter.get("terminal_email"));
			
//			System.out.println("---------"+inviteCodeId+"------"+email+"---------"+inviteCode+"-----");
			
			if (inviteCodeId.isEmpty()) 
			{
				return new MethodResult(MethodResult.FAIL, "邀请码id不能为空");
			}
			if (inviteCode.isEmpty())
			{
				return new MethodResult(MethodResult.FAIL,"邀请码不能为空");
			}
			if (email.isEmpty()) 
			{
				return new MethodResult(MethodResult.FAIL, "电子邮箱不能为空");
			}
			//判断手机是否绑定
			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
			TerminalUserVO terminalUserVO = terminalUserMapper.getTerminalUserByEmail(email);
			if (terminalUserVO !=null) 
			{
				return new MethodResult(MethodResult.FAIL,"该邮箱已绑定云端在线账号");
			}
			
				//发送邮件
				Map<String, Object> terminalUserData = new LinkedHashMap<String, Object>();
				terminalUserData.put("email", email);
				terminalUserData.put("inviteCode", inviteCode);
				new SendMail().sendInviteCodeEmail(terminalUserData); 
			
				// 更改数据库
				InviteCodeMapper inviteCodeMapper = this.sqlSession.getMapper(InviteCodeMapper.class);
				Map<String, Object> inviteCodeMap = new LinkedHashMap<String, Object>();
				inviteCodeMap.put("id",       inviteCodeId);
				inviteCodeMap.put("email",    email);
				inviteCodeMap.put("sendTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS") );
				inviteCodeMap.put("status",   AppConstant.INVITE_CODE_SEND_YES);
				inviteCodeMapper.updateSendTimeAndStatusAndEmail(inviteCodeMap);
				return new MethodResult(MethodResult.SUCCESS, "邮件已成功发送");
			
		} catch (Exception e) 
		{
			logger.error(e);
			throw new AppException("发送失败");
		}
	}
	
		
	
	@Callable
	@Transactional(readOnly = false)
	public MethodResult addInviteCode(Map<String, String> parameter)
	{
		logger.debug("AgentServiceImpl.addInviteCode()");
		try
		{
			HttpServletRequest request = RequestContext.getHttpRequest();
			
			// 权限判断
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			if( AppConstant.SYS_USER_TYPE_AGENT==loginInfo.getUserType() && loginInfo.hasPrivilege(PrivilegeConstant.agent_invite_code_add) == false )
			{
				return new MethodResult(MethodResult.FAIL, "您没有权限进行此操作");
			}
			if (AppConstant.SYS_USER_TYPE_OPERATOR==loginInfo.getUserType() && loginInfo.hasPrivilege(PrivilegeConstant.operator_invite_code_add) == false) 
			{
				return new MethodResult(MethodResult.FAIL, "您没有权限进行此操作");
			}
			//获取参数
			
			String  inviteCodeNumber = StringUtil.trim(parameter.get("code_number"));
			if (inviteCodeNumber.isEmpty()) {
				return new MethodResult(MethodResult.FAIL,"添加邀请码的个数不能为空");
			}
			int n = Integer.parseInt(inviteCodeNumber);
			
			for (int i = 0; i < n; i++) {
				String id       = StringUtil.generateUUID();
				String creatId  = loginInfo.getUserId();
				String code     = id.substring(4, 12);
				InviteCodeMapper inviteCodeMapper = this.sqlSession.getMapper(InviteCodeMapper.class);
				// 添加进数据库
				Map<String, Object> inviteCode = new LinkedHashMap<String, Object>();
				inviteCode.put("id",         id);
				inviteCode.put("createrId",  creatId);
				inviteCode.put("code",       code);
				inviteCode.put("createTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS") );
				inviteCode.put("status",     AppConstant.INVITE_CODE_SEND_NO);
				inviteCodeMapper.addInviteCode(inviteCode);
				
			}
			
			return new MethodResult(MethodResult.SUCCESS,"添加成功");
			
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("添加失败");
		}
	}

	@Callable
	@Transactional(readOnly = false)
	public MethodResult deleteInviteCodeByIds(List<String> inviteCodeIds)
	{
		logger.debug("TerminalUserServiceImpl.deleteInviteCodeByIds()");
		try
		{
			HttpServletRequest request = RequestContext.getHttpRequest();
			
			// 权限判断
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			if(AppConstant.SYS_USER_TYPE_OPERATOR==loginInfo.getUserType() && loginInfo.hasPrivilege(PrivilegeConstant.operator_invite_code_delete) == false )
			{
				return new MethodResult(MethodResult.FAIL, "您没有权限进行此操作");
			}
			else if(AppConstant.SYS_USER_TYPE_AGENT==loginInfo.getUserType() && loginInfo.hasPrivilege(PrivilegeConstant.agent_invite_code_delete) == false)
			{
				return new MethodResult(MethodResult.FAIL, "您没有权限进行此操作");
			}
			
			// 参数处理
			if( inviteCodeIds == null || inviteCodeIds.size() == 0 )
			{
				return new MethodResult(MethodResult.FAIL,"选择删除项");
			}

			InviteCodeMapper inviteCodeMapper = this.sqlSession.getMapper(InviteCodeMapper.class);
			
			int n = inviteCodeMapper.deleteInviteCodeByIds(inviteCodeIds);
			
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
			logger.error(e);
			throw new AppException("删除失败");
		}
	}
	
	
}
