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
import com.zhicloud.op.app.helper.VerificationCodeHelper;
import com.zhicloud.op.app.propeties.AppProperties;
import com.zhicloud.op.common.util.RandomPassword;
import com.zhicloud.op.common.util.SendMail;
import com.zhicloud.op.common.util.SendSms;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.common.util.constant.MailConstant;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.login.LoginInfo;
import com.zhicloud.op.mybatis.mapper.AccountBalanceDetailMapper;
import com.zhicloud.op.mybatis.mapper.AgentMapper;
import com.zhicloud.op.mybatis.mapper.CashCouponMapper;
import com.zhicloud.op.mybatis.mapper.CloudHostMapper;
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
import com.zhicloud.op.service.AgentService;
import com.zhicloud.op.service.constant.AppConstant;
import com.zhicloud.op.vo.AgentVO;
import com.zhicloud.op.vo.CloudHostVO;
import com.zhicloud.op.vo.SysGroupVO;
import com.zhicloud.op.vo.SysUserVO;
import com.zhicloud.op.vo.TerminalUserVO;

@Transactional(readOnly = true)
public class AgentServiceImpl extends BeanDirectCallableDefaultImpl implements AgentService
{
	public static final Logger logger = Logger.getLogger(AgentServiceImpl.class);

	private SqlSession sqlSession;

	public void setSqlSession(SqlSession sqlSession)
	{
		this.sqlSession = sqlSession;
	}

	@Callable
	public String managePage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("AgentServiceImpl.managePage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.hasPrivilege(PrivilegeConstant.agent_manage_page) == false )
		{
			return "/public/have_not_access.jsp";
		}
		
		return "/security/operator/agent_manage.jsp";
	}

	@Callable
	public String addPage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("AgentServiceImpl.addPage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.hasPrivilege(PrivilegeConstant.agent_manage_add) == false )
		{
			return "/public/have_not_access_dialog.jsp";
		}
		
		// 获取组列表
		SysGroupMapper sysGroupMapper = this.sqlSession.getMapper(SysGroupMapper.class);
		List<SysGroupVO> sysGroupList = sysGroupMapper.getAll();
		request.setAttribute("sysGroupList", sysGroupList);
		
		return "/security/operator/agent_add.jsp";
	}

	@Callable
	public String modPage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("AgentServiceImpl.modPage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.hasPrivilege(PrivilegeConstant.agent_manage_modify) == false )
		{
			return "/public/have_not_access_dialog.jsp";
		}
		
		// 参数判断
		String agentId = (String) request.getParameter("agentId");
		if( StringUtil.isBlank(agentId) )
		{
			throw new AppException("agentId不能为空");
		}
		
		SysGroupMapper sysGroupMapper = this.sqlSession.getMapper(SysGroupMapper.class);
		AgentMapper agentMapper       = this.sqlSession.getMapper(AgentMapper.class);
		
		// 获取组
		List<SysGroupVO> sysGroupList = sysGroupMapper.getAll();
		request.setAttribute("sysGroupList", sysGroupList);

		// 获取代理商数据
		AgentVO agent = agentMapper.getAgentById(agentId);
		if( agent != null )
		{
			request.setAttribute("agent", agent);
			return "/security/operator/agent_mod.jsp";
		}
		else
		{
			request.setAttribute("message", "找不到代理商的记录");
			return "/public/warning_dialog.jsp";
		}
	}
	
	
	@Callable
	public String resetPasswordPage(HttpServletRequest request,HttpServletResponse response) 
	{

		logger.debug("AgentServiceImpl.resetPasswordPage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.hasPrivilege(PrivilegeConstant.agent_reset_password) == false )
		{
			return "/public/have_not_access_dialog.jsp";
		}
		//参数处理
		String agentId = StringUtil.trim(request.getParameter("agentId"));
		AgentMapper agentMapper = this.sqlSession.getMapper(AgentMapper.class);
		AgentVO agentVO         = agentMapper.getBasicInformationById(agentId);
		if(agentVO==null){
			request.setAttribute("notexsit", "yes");
			request.setAttribute("message", "账户不存在，请和管理员联系！");
			return "/public/warning_dialog.jsp";
		}
		String resetPassword    = RandomPassword.getRandomNum(MailConstant.PASSWORD_LENGTH);
		
		request.setAttribute("resetPassword", resetPassword);
		request.setAttribute("agentVO", agentVO);
		
		return "/security/operator/agent_reset_password.jsp";
	}
	
	@Callable
	public String basicInformationPage(HttpServletRequest request, HttpServletResponse response) 
	{
		logger.debug("AgentServiceImpl.basicInformationPage()");
		
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request, 3);
		
		// 权限判断，判断是否为运营商、是否有权限
		if( AppConstant.SYS_USER_TYPE_AGENT.equals(loginInfo.getUserType())==false || loginInfo.hasPrivilege(PrivilegeConstant.agent_basic_information_page)==false )
		{
			return "/public/have_not_access.jsp";
		}
		
		String id = loginInfo.getUserId();
		
		AgentMapper agentMapper = this.sqlSession.getMapper(AgentMapper.class);
		AgentVO agent = agentMapper.getBasicInformationById(id);
		request.setAttribute("agent", agent);
		if(agent==null){
			request.setAttribute("notexsit", "yes");
			request.setAttribute("message", "账户不存在，请和管理员联系！");
			return "/public/warning_dialog.jsp";
		}
		
		return "/security/agent/agent_basic_information.jsp";
	}

	@Callable
	public String updatePasswordPage(HttpServletRequest request,HttpServletResponse response) 
	{
		logger.debug("AgentServiceImpl.passwordUpdatePage");
		
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request, 3);
		
		// 权限判断，判断是否为运营商、是否有权限
		if( AppConstant.SYS_USER_TYPE_AGENT.equals(loginInfo.getUserType())==false || 
			loginInfo.hasPrivilege(PrivilegeConstant.agent_change_password_page) == false )
		{
			return "/public/have_not_access.jsp";
		}
		
		String id = loginInfo.getUserId();
		
		AgentMapper agentMapper = this.sqlSession.getMapper(AgentMapper.class);
		AgentVO agent = agentMapper.getBasicInformationById(id);
		request.setAttribute("agent", agent);
		
		return "/security/agent/agent_update_password.jsp";
	}
	@Callable
	public void queryAgent(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("AgentServiceImpl.queryAgent()");
		try
		{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");

			// 权限判断
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			if( loginInfo.hasPrivilege(PrivilegeConstant.agent_manage_page) == false )
			{
				throw new AppException("您没有权限进行此操作");
			}

			// 获取参数
			String account = StringUtil.trim(request.getParameter("account"));
			Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
			Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 10);

			// 查询数据库
			AgentMapper agentMapper = this.sqlSession.getMapper(AgentMapper.class);
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("account", "%" + account + "%");
			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			int total = agentMapper.queryPageCount(condition); // 总行数
			List<AgentVO> agentList = agentMapper.queryPage(condition);// 分页结果

			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, agentList);
		}
		catch( Exception e )
		{
			logger.error("AgentServiceImpl.queryAgent()",e);
			throw new AppException("查询失败");
		}
	}

	@Callable
	public boolean queryAgentByAccount(String account)
	{

		logger.debug("AgentServiceImpl.queryAgentByAccount()");
		try
		{
			// 查询数据库
			AgentMapper AgentMapper = this.sqlSession.getMapper(AgentMapper.class);
			return AgentMapper.getAgentByAccount(account) != null;
		}
		catch( Exception e )
		{
			logger.error("AgentServiceImpl.queryAgentByAccount()",e);
			throw new AppException("查询失败");
		}
	}

	@Callable
	@Transactional(readOnly = false)
	public MethodResult addAgent(Map<String, String> parameter)
	{
		logger.debug("AgentServiceImpl.addAgent()");
		try
		{
			HttpServletRequest request = RequestContext.getHttpRequest();
			
			// 权限判断
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			if( loginInfo.hasPrivilege(PrivilegeConstant.agent_manage_add) == false )
			{
				return new MethodResult(MethodResult.FAIL, "您没有权限进行此操作");
			}
			
			// 参数处理
			String groupId    = StringUtil.trim(parameter.get("group_id"));
			String account    = StringUtil.trim(parameter.get("account"));
			String name       = StringUtil.trim(parameter.get("name"));
			String password   = StringUtil.trim(parameter.get("password"));
			String email      = StringUtil.trim(parameter.get("email"));
			String phone      = StringUtil.trim(parameter.get("phone"));
			String percentOff = StringUtil.trim(parameter.get("percentOff"));
					
			if(groupId==null || groupId==""){
				return new MethodResult(MethodResult.FAIL,"请选择一个组");
			}
//			if(account==null || account==""){
//				return new MethodResult(MethodResult.FAIL,"用户名不能为空");
//			}
//			if(name==null || name==""){
//				return new MethodResult(MethodResult.FAIL,"名字不能为空");
//			}
//			if(password==null || password==""){
//				return new MethodResult(MethodResult.FAIL,"密码不能为空");
//			}
			if(email==null || email==""){
				return new MethodResult(MethodResult.FAIL,"邮箱不能为空");
			}
			if(phone==null || phone==""){
				return new MethodResult(MethodResult.FAIL,"手机号不能为空");
			}
			if(percentOff==null || percentOff==""){
				return new MethodResult(MethodResult.FAIL,"折扣不能为空");
			}
			
			SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
			AgentMapper agentMapper = this.sqlSession.getMapper(AgentMapper.class);

			// 判断该账号是否已经存在
			AgentVO agent = agentMapper.getAgentByAccount(email);
			if( agent != null )
			{
				return new MethodResult(MethodResult.FAIL,  email+"已存在");
			}
			// 添加进数据库
			String id = StringUtil.generateUUID();
			
			Map<String, Object> userData = new LinkedHashMap<String, Object>();
			userData.put("id",       id);
			userData.put("account",  email);
			password = RandomPassword.getRandomNum(MailConstant.PASSWORD_LENGTH);
			userData.put("password", LoginHelper.toEncryptedPassword(password));
			userData.put("groupId",  groupId);
			userData.put("type",     AppConstant.SYS_USER_TYPE_AGENT);
			
			int n = sysUserMapper.addSysUser(userData);
			
			Map<String, Object> agentData = new LinkedHashMap<String, Object>();
			agentData.put("id",          id);
			agentData.put("name",        name);
			agentData.put("email",       email);
			agentData.put("phone",       phone);
			agentData.put("create_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
			agentData.put("status",      AppConstant.AGENT_STATUS_NORMAL);
			agentData.put("operatorId",  loginInfo.getUserId());
			agentData.put("percentOff",  percentOff);

			int m = agentMapper.addAgent(agentData);
			
			//发送邮件
			Map<String, Object> user = new LinkedHashMap<String, Object>();
			user.put("password", password);
			user.put("email",    email); 
//			user.put("account",    account); 
			new SendMail().sendPasswordEmailForAgent(user);
			if( n<0||m<0 )
			{
				return new MethodResult(MethodResult.FAIL, "添加失败");
			}
			else
			{
				return new MethodResult(MethodResult.SUCCESS, "添加成功");
			}
		}
		catch( Exception e )
		{
			logger.error("AgentServiceImpl.addAgent()",e);
			throw new AppException("新增失败");
		}
	}

	@Callable
	@Transactional(readOnly = false)
	public MethodResult updateAgentById(Map<String, Object> parameter)
	{
		logger.debug("AgentServiceImpl.updateAgentById()");
		try
		{
			HttpServletRequest request = RequestContext.getHttpRequest();
			
			// 权限判断
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			if( loginInfo.hasPrivilege(PrivilegeConstant.agent_manage_modify) == false )
			{
				return new MethodResult(MethodResult.FAIL, "您没有权限进行此操作");
			}

			// 参数处理
			String groupId    = StringUtil.trim(parameter.get("group_id"));
//			String account    = StringUtil.trim(parameter.get("account"));
			String agentId    = StringUtil.trim(parameter.get("agent_id"));
//			String name       = StringUtil.trim(parameter.get("name"));
			String email      = StringUtil.trim(parameter.get("email"));
			String phone      = StringUtil.trim(parameter.get("phone"));
			String percentoff = StringUtil.trim(parameter.get("percentOff"));
			Integer status    = StringUtil.parseInteger((String)parameter.get("status"), AppConstant.AGENT_STATUS_NORMAL);

			if(groupId==null || groupId==""){
				return new MethodResult(MethodResult.FAIL,"请选择一个组");
			}
//			if(account==null || account==""){
//				return new MethodResult(MethodResult.FAIL,"用户名不能为空");
//			}
//			if(name==null || name==""){
//				return new MethodResult(MethodResult.FAIL,"名字不能为空");
//			}
			if(email==null || email==""){
				return new MethodResult(MethodResult.FAIL,"邮箱不能为空");
			}
			if(phone==null || phone==""){
				return new MethodResult(MethodResult.FAIL,"手机号不能为空");
			}
			if (percentoff==null|percentoff=="") {
				return new MethodResult(MethodResult.FAIL,"折扣不能为空");
			}
			if(status!=null){
				if(status<1||status>3){
					return new MethodResult(MethodResult.FAIL, "状态不合法");
					
				}
			}
			SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
			AgentMapper agentMapper = this.sqlSession.getMapper(AgentMapper.class);
			
			// 判断账号是不是被其它的记录使用
			AgentVO agent = agentMapper.getAgentByAccount(email);
			if( agent != null && agentId.equals(agent.getId())==false )
			{
				return new MethodResult(MethodResult.FAIL,email +"已存在");
			}

			Map<String, Object> userData = new LinkedHashMap<String, Object>();
			userData.put("groupId",  groupId);
			userData.put("account",  email);
			userData.put("id",       agentId);
			int n = sysUserMapper.updateSysUser(userData);

			Map<String, Object> agentData = new LinkedHashMap<String, Object>();
//			agentData.put("name",        email);
			agentData.put("email",       email);
			agentData.put("phone",       phone);
			agentData.put("status",      status);
			agentData.put("id",          agentId);
			agentData.put("percentOff",  percentoff);
			agentData.put("operatorId",  loginInfo.getUserId());
			agentMapper.updateAgent(agentData);
			
			if( n > 0 )
			{
				return new MethodResult(MethodResult.SUCCESS, "修改成功");
			}
			else
			{
				return new MethodResult(MethodResult.FAIL, "修改失败");
			}
		}
		catch( Exception e )
		{
			logger.error("AgentServiceImpl.updateAgentById()",e);
			throw new AppException("修改失败");
		}
	}

	@Callable
	@Transactional(readOnly = false)
	public MethodResult updateAgentByIdToUser(Map<String, Object> parameter)
	{
		logger.debug("AgentServiceImpl.updateAgentByIdToUser()");
		try
		{

		}
		catch( Exception e )
		{
			//
		}
		return null;
	}

	@Callable
	@Transactional(readOnly = false)
	public MethodResult deleteAgentById(String agentId)
	{
		logger.debug("AgentServiceImpl.deleteAgentById()");
		try
		{
			if( agentId == null )
			{
				throw new AppException("agentId不能为空");
			}

			AgentMapper AgentMapper = this.sqlSession.getMapper(AgentMapper.class);
			int n = AgentMapper.deleteAgentById(agentId);

			SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
			int m = sysUserMapper.deleteSysUser(agentId);

			if( n > 0 && m > 0 )
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
			logger.error("AgentServiceImpl.deleteAgentById()",e);
			throw new AppException("删除失败");
		}
	}

	@Callable
	@Transactional(readOnly = false)
	public MethodResult deleteAgentByIds(List<?> agentIds)
	{
		logger.debug("AgentServiceImpl.deleteAgentByIds()");
		try
		{
			if( agentIds == null || agentIds.size() == 0 )
			{
				throw new AppException("agentIds不能为空");
			}

			AgentMapper AgentMapper = this.sqlSession.getMapper(AgentMapper.class);
			int n = AgentMapper.deleteAgentByIds(agentIds.toArray(new String[0]));

			SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
			int m = sysUserMapper.deleteSysUsers(agentIds.toArray(new String[0]));

			if( n > 0 && m > 0 )
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
			logger.error("AgentServiceImpl.deleteAgentByIds()",e);
			throw new AppException("删除失败");
		}
	}
	
	@Callable
	@Transactional(readOnly = false)
	public MethodResult updateBasicInformation(Map<String, Object> parameter) {
		Long begin = System.currentTimeMillis();
		Integer logStatus = AppConstant.OPER_LOG_FAIL;
		HttpServletRequest request = RequestContext.getHttpRequest();
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		logger.debug("AgentServiceImpl.updateBasicInformation()");
		try
		{
			// 参数处理
			String agentId    = StringUtil.trim(parameter.get("agent_id"));
			String groupId    = StringUtil.trim(parameter.get("group_id"));
//			String account    = StringUtil.trim(parameter.get("account"));
//			String name       = StringUtil.trim(parameter.get("name"));
			String email      = StringUtil.trim(parameter.get("e_mail"));
			String phone      = StringUtil.trim(parameter.get("phone"));
			Integer status    = StringUtil.parseInteger((String)parameter.get("status"), AppConstant.OPERATOR_STATUS_NORMAL);
	
			if(groupId==null || groupId==""){
				return new MethodResult(MethodResult.FAIL,"请选择一个组");
			}
//			if(account==null || account==""){
//				return new MethodResult(MethodResult.FAIL,"用户名不能为空");
//			}
//			if(name==null || name==""){
//				return new MethodResult(MethodResult.FAIL,"名字不能为空");
//			}
			if(email==null || email==""){
				return new MethodResult(MethodResult.FAIL,"邮箱不能为空");
			}
			if(phone==null || phone==""){
				return new MethodResult(MethodResult.FAIL,"手机号不能为空");
			}
			SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
			AgentMapper agentMapper = this.sqlSession.getMapper(AgentMapper.class);
			
			// 判断运营商账户是否已经存在
			LinkedHashMap<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("type", AppConstant.SYS_USER_TYPE_AGENT);
			condition.put("account", email);
			SysUserVO sysUser = sysUserMapper.getByTypeAndAccount(condition);
			if( sysUser!=null && sysUser.getId().equals(agentId)==false )
			{
				return new MethodResult(MethodResult.FAIL,  email+"已经存在");
			}
			
			
			// 更新系统用户表
			Map<String, Object> userData = new LinkedHashMap<String, Object>();
			userData.put("id",       agentId);
			userData.put("account",  email);
			userData.put("password",  "");
			userData.put("groupId",  groupId);

			int n = sysUserMapper.updateSysUser(userData);

			// 更新运营商表
			Map<String, Object> agentData = new LinkedHashMap<String, Object>();
			agentData.put("id", agentId);
//			agentData.put("name", name);
			agentData.put("email", email);
			agentData.put("phone", phone);
			agentData.put("status", status);
			agentData.put("percentOff", "");

			int m = agentMapper.updateAgent(agentData);
			if(n > 0 && m > 0){
				logStatus = AppConstant.OPER_LOG_SUCCESS;
				return new MethodResult(MethodResult.SUCCESS, "修改成功,请重新登录");
			}else{
				return new MethodResult(MethodResult.FAIL, "修改失败");
			}
		}
		catch( Exception e )
		{
			logger.error("AgentServiceImpl.updateBasicInformation()",e);
			throw new AppException("修改失败");
		}finally{
			try {
				OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
				Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
				operLogData.put("id", StringUtil.generateUUID());
				operLogData.put("userId", loginInfo.getUserId());
				operLogData.put("content", "修改了基本信息");
				operLogData.put("operTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
				operLogData.put("status", logStatus);
				operLogData.put("resourceName", loginInfo.getAccount());
				operLogData.put("operDuration", System.currentTimeMillis() - begin);
				operLogMapper.addOperLog(operLogData);
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}

	@Callable
	@Transactional(readOnly = false)
	public MethodResult updatePasswordById(Map<String, Object> parameter) {
		Long begin = System.currentTimeMillis();
		Integer logStatus = AppConstant.OPER_LOG_FAIL;
		HttpServletRequest request = RequestContext.getHttpRequest();
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		logger.debug("AgentServiceImpl.updatePasswordById()");
		try
		{
			
			// 参数处理
			String agentId        = loginInfo.getUserId();
			String oldPassword    = StringUtil.trim(parameter.get("old_password")); 
			String password       = StringUtil.trim(parameter.get("password"));
			String confirm        = StringUtil.trim(parameter.get("confirm"));
			
			SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
			SysUserVO user = sysUserMapper.getUserPassword(agentId);

			if( agentId.isEmpty() )
			{
				return new MethodResult(MethodResult.FAIL, "账户id不能为空");
			}
			if (oldPassword.isEmpty()) 
			{
				return new MethodResult(MethodResult.FAIL, "原密码不能为空");
			}
			if (!LoginHelper.toEncryptedPassword(oldPassword).equals(user.getPassword())) 
			{
				return new MethodResult(MethodResult.FAIL,"原密码不正确");
			}
			if(password.isEmpty())
			{
				return new MethodResult(MethodResult.FAIL,"新密码不能为空");
			}
			if(user.getPassword().equals(LoginHelper.toEncryptedPassword(password)))
			{
				return new MethodResult(MethodResult.FAIL, "新密码和原密码一致！");				
			}
			
			if(!password.equals(confirm))
			{
				return new MethodResult(MethodResult.FAIL, "密码不匹配");
			}
			
			
			// 更新系统用户表
			Map<String, Object> userData = new LinkedHashMap<String, Object>();
			userData.put("id",       agentId);
			userData.put("password", LoginHelper.toEncryptedPassword(password));

			int n = sysUserMapper.updateSysUserPassword(userData);
			if( n==0 )
			{
				return new MethodResult(MethodResult.FAIL, "修改失败，请重新登录");
			}
			logStatus = AppConstant.OPER_LOG_SUCCESS;
			return new MethodResult(MethodResult.SUCCESS, "修改成功");
		}
		catch( Exception e )
		{
			logger.error("AgentServiceImpl.updatePasswordById()",e);
			throw new AppException("修改失败");
		}finally{
			try {
				OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
				Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
				operLogData.put("id", StringUtil.generateUUID());
				operLogData.put("userId", loginInfo.getUserId());
				operLogData.put("content", "修改密码");
				operLogData.put("operTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
				operLogData.put("status", logStatus);
				operLogData.put("resourceName", loginInfo.getAccount());
				operLogData.put("operDuration", System.currentTimeMillis() - begin);
				operLogMapper.addOperLog(operLogData);
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}
	@Callable
	@Transactional(readOnly = false)
	public MethodResult resetPasswordById(Map<String, Object> parameter) {
		
		logger.debug("AgentServiceImpl.resetPasswordById()");
		try
		{
			
			// 参数处理
			String agentId    = StringUtil.trim(parameter.get("agent_id"));
			String password   = StringUtil.trim(parameter.get("password"));
			String email      = StringUtil.trim(parameter.get("email"));
			
			if(password==null || password==""){
				return new MethodResult(MethodResult.FAIL,"密码不能为空");
			}
			if(email==null || email==""){
				return new MethodResult(MethodResult.FAIL,"密码不能为空");
			}

			if( agentId.isEmpty() )
			{
				return new MethodResult(MethodResult.FAIL, "账户id不能为空");
			}		
			
			SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class); 
			// 更新系统用户表
			Map<String, Object> userData = new LinkedHashMap<String, Object>();
			userData.put("id",       agentId);
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
			logger.error("AgentServiceImpl.resetPasswordById()",e);
			throw new AppException("重置失败");
		}
	}
	// -------------------

	@Callable 
	public String rechargePage(HttpServletRequest request,
			HttpServletResponse response) {
		logger.debug("AgentServiceImpl.rechargePage()"); 
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request,AppConstant.SYS_USER_TYPE_AGENT);
 		if( loginInfo.hasPrivilege(PrivilegeConstant.agent_recharge) == false )
		{
			return "/public/have_not_access.jsp";
		}
		AgentMapper agentMapper = this.sqlSession.getMapper(AgentMapper.class);
		AgentVO agentVO = agentMapper.getAgentById(loginInfo.getUserId());
		request.setAttribute("agent", agentVO);
		String fee = StringUtil.trim(request.getParameter("total_fee"));
		if(fee == null||fee.equals("")||fee.equals("undefined")){
			fee = StringUtil.trim(request.getParameter("feeDIY"));
		}
		request.setAttribute("total_fee", fee);
 		return "/security/agent/agent_recharge.jsp";
	}

	@Override
	@Transactional(readOnly = false)
	public void CheckBalanceForAgent() {
		logger.debug("AgentServiceImpl.CheckBalanceForAgent()");
		try{
			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
			CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
			UserDictionaryMapper userDictionaryMapper = this.sqlSession.getMapper(UserDictionaryMapper.class);
			AgentMapper agentMapper = this.sqlSession.getMapper(AgentMapper.class);
			//查询所有代理商
			List<AgentVO> agentList = agentMapper.getAll();
			
			if(agentList!=null && agentList.size()>0){
				for(AgentVO agent : agentList){
					BigDecimal totalPrice = new BigDecimal("0");
					List<TerminalUserVO> terminalUserList = terminalUserMapper.getTerminalUserFromAgent(agent.getId());
					if(terminalUserList!=null && terminalUserList.size()>0){
						for(TerminalUserVO terminalUser : terminalUserList){
							List<CloudHostVO> cloudHostList = cloudHostMapper.getByUserId(terminalUser.getId());
							if(cloudHostList!=null && cloudHostList.size()>0){
								for(CloudHostVO cloudHost : cloudHostList){
									if(cloudHost.getType() == 2){
										if(!(cloudHost.getSummarizedStatusText().equals("创建失败")||cloudHost.getSummarizedStatusText().equals("停机"))&&cloudHost.getMonthlyPrice()!=null){						
											totalPrice = cloudHost.getMonthlyPrice().add(totalPrice);
										}
									}
								}
							}
						}
					}else{
						continue;
					}
					if(agent.getAccountBalance() == null){
						continue;
					}
					if (agent.getPercentOff()!=null) {
						totalPrice=totalPrice.multiply(new BigDecimal(100).subtract(agent.getPercentOff())).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
					}
					//计算出每天的费用
					totalPrice = totalPrice.divide(new BigDecimal("31"),0,BigDecimal.ROUND_HALF_UP);
					BigDecimal balance = agent.getAccountBalance().setScale(0, BigDecimal.ROUND_HALF_UP);
					BigDecimal newBalance = balance;
					balance = balance.subtract(totalPrice.divide(new BigDecimal(24),2,BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal(7)));
					//余额只够使用7天是提示
					if(balance.compareTo(totalPrice.multiply(new BigDecimal(8)))<0 && balance.compareTo(totalPrice.multiply(new BigDecimal(7))) >= 0){
						SendMail sm = new SendMail();
						sm.sendHintEmail(agent, "7","【余额不足】通知-致云Zhicloud");
						String message = new String("【致云科技】尊敬的用户，您好！您的账户余额还可供您当前所有产品使用7天，为了不影响您的业务情况，请您及时充值，谢谢。"); 
						String state  = new SendSms().zhicloudSendSms(agent.getPhone(),message);
						if("1".equals(state)){
//							System.out.println("-----success-----");
						}else{
//							System.out.println("-----fail-----:"+state);
						}
						Map<String, Object> userDictionaryData = new LinkedHashMap<String, Object>();
						userDictionaryData.put("userId", agent.getId());
						userDictionaryData.put("key", "sendTimes");
						userDictionaryMapper.deleteDictionaryByUserId(userDictionaryData);
						continue;
					} 
					//余额只够使用3天时提示
					if(balance.compareTo(totalPrice.multiply(new BigDecimal(4)))<0 && balance.compareTo(totalPrice.multiply(new BigDecimal(3))) >= 0){
						SendMail sm = new SendMail();
						sm.sendHintEmail(agent, "3","【余额不足】通知-致云Zhicloud");
						String message = new String("【致云科技】尊敬的用户，您好！您的账户余额还可供您当前所有产品使用3天，为了不影响您的业务情况，请您及时充值，谢谢。"); 
						String state  = new SendSms().zhicloudSendSms(agent.getPhone(),message);
						if("1".equals(state)){
//							System.out.println("-----success-----");
						}else{
//							System.out.println("-----fail-----:"+state);
						}
						Map<String, Object> userDictionaryData = new LinkedHashMap<String, Object>();
						userDictionaryData.put("userId", agent.getId());
						userDictionaryData.put("key", "sendTimes");
						userDictionaryMapper.deleteDictionaryByUserId(userDictionaryData);
						continue;
					} 
					//余额只够使用2天时提示
					if(balance.compareTo(totalPrice.multiply(new BigDecimal(3)))<0 && balance.compareTo(totalPrice.multiply(new BigDecimal(2))) >= 0){
						SendMail sm = new SendMail();
						sm.sendHintEmail(agent, "2","【余额不足】通知-致云Zhicloud");
						String message = new String("【致云科技】尊敬的用户，您好！您的账户余额还可供您当前所有产品使用2天，为了不影响您的业务情况，请您及时充值，谢谢。"); 
						String state  = new SendSms().zhicloudSendSms(agent.getPhone(),message);
						if("1".equals(state)){
//							System.out.println("-----success-----");
						}else{
//							System.out.println("-----fail-----:"+state);
						}
						Map<String, Object> userDictionaryData = new LinkedHashMap<String, Object>();
						userDictionaryData.put("userId", agent.getId());
						userDictionaryData.put("key", "sendTimes");
						userDictionaryMapper.deleteDictionaryByUserId(userDictionaryData);
						continue;
					} 
					//余额只够使用1天时提示
					if(balance.compareTo(totalPrice.multiply(new BigDecimal(2)))<0 && balance.compareTo(new BigDecimal(1)) >= 0){
						SendMail sm = new SendMail();
						sm.sendHintEmail(agent, "1","【余额不足】通知-致云Zhicloud");
						String message = new String("【致云科技】尊敬的用户，您好！您的账户余额还可供您当前所有产品使用1天，为了不影响您的业务情况，请您及时充值，谢谢。"); 
						String state  = new SendSms().zhicloudSendSms(agent.getPhone(),message);
						if("1".equals(state)){
//							System.out.println("-----success-----");
						}else{
//							System.out.println("-----fail-----:"+state);
						}
						Map<String, Object> userDictionaryData = new LinkedHashMap<String, Object>();
						userDictionaryData.put("userId", agent.getId());
						userDictionaryData.put("key", "sendTimes");
						userDictionaryMapper.deleteDictionaryByUserId(userDictionaryData);
					}
					Map<String, Object> _userDictionaryData = new LinkedHashMap<String, Object>();
					_userDictionaryData.put("userId", agent.getId());
					_userDictionaryData.put("key", "sendTimes");
					int n = userDictionaryMapper.getByUserId(_userDictionaryData);
					if(n>0){
						continue;
					}
					//余额为0时提示
					if(newBalance.compareTo(BigDecimal.ZERO) <= 0){
						SendMail sm = new SendMail();
						sm.sendHintEmail(agent, "0","【余额为零】通知-致云Zhicloud");
						String message = new String("【致云科技】尊敬的用户，您好！您的账户余额已为零，目前已暂停您所有产品的服务，为了不影响您的业务情况，请您尽快充值开通服务，谢谢。"); 
						String state  = new SendSms().zhicloudSendSms(agent.getPhone(),message);
						if("1".equals(state)){
							System.out.println("-----success-----");
						}else{
							System.out.println("-----fail-----:"+state);
						}
						Map<String, Object> userDictionaryData = new LinkedHashMap<String, Object>();
						userDictionaryData.put("id", StringUtil.generateUUID());
						userDictionaryData.put("userId", agent.getId());
						userDictionaryData.put("key", "sendTimes");
						userDictionaryMapper.addUserDictionary(userDictionaryData);
					}
			}
		}
		}catch(Exception e){
			logger.error(e);
			throw new AppException(e);
		}
	}
	
	
	@Callable
	@CallWithoutLogin
	public MethodResult optionChangePassword(Map<String, String> parameter)
	{
		logger.debug("AgentServiceImpl.optionChangePassword()");
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
			AgentMapper agentMapper = this.sqlSession.getMapper(AgentMapper.class);
			AgentVO agentVO = agentMapper.getAgentByAccount(account);
 			
			if( agentVO == null )
			{
				return new MethodResult(MethodResult.FAIL,account + "是未绑定邮箱");
			}
			//电话和邮箱显示部分
			if (!(agentVO.getPhone()==null||agentVO.getPhone().isEmpty())) {
				String phone = agentVO.getPhone();
				String begin = phone.substring(0,3);
				String  end  = phone.substring(7,11);
				String newPhone = begin+"****"+end;
				agentVO.setPhone(newPhone);
			}
			
			if (!(agentVO.getEmail()==null||agentVO.getEmail().isEmpty())) {
				String email     = agentVO.getEmail();
				String myemail[] = email.split("@");
				String beging    = myemail[0].substring(0, 3); 
				String newMail   = beging+"****"+"@"+myemail[1];
				agentVO.setEmail(newMail);
			}
 				MethodResult result = new MethodResult(MethodResult.SUCCESS, "存在的账户");
				result.put("userId", agentVO.getId());
				result.put("email", agentVO.getEmail());
				result.put("phone", agentVO.getPhone());
				return result;
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("不存在的邮箱");
		}
	}
	
	
	@Callable
	@CallWithoutLogin
	public MethodResult resetPasswordSendEmailCode(Map<String, String> parameter)
	{
		logger.debug("AgentServiceImpl.sendMailForChangePassword()");
		try
		{
			HttpServletRequest request = RequestContext.getHttpRequest();
			// 参数处理
			String  id= StringUtil.trim(parameter.get("userId"));
			if (id.isEmpty()) 
			{
				return new MethodResult(MethodResult.FAIL,"id不能为空");
			}
			// 判断账号是否已经存在
			AgentMapper agentMapper = this.sqlSession.getMapper(AgentMapper.class);
			AgentVO agentVO = agentMapper.getAgentById(id);
			if( agentVO == null )
			{
				return new MethodResult(MethodResult.FAIL, "未绑定云端在线");
			}
			
			//验证码
			Integer code = (Integer)((int)((Math.random()*9+1)*100000));
			Map<String, Object> user = new LinkedHashMap<String, Object>();
			user.put("email", agentVO.getEmail());
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
		logger.debug("AgentServiceImpl.resetPasswordSendPhoneCode()");
		try {
			HttpServletRequest request = RequestContext.getHttpRequest();
			// 参数处理
			String  id= StringUtil.trim(parameter.get("userId"));
			if (id.isEmpty()) 
			{
				return new MethodResult(MethodResult.FAIL,"id不能为空");
			}
			// 判断账号是否已经存在
			AgentMapper agentMapper = this.sqlSession.getMapper(AgentMapper.class);
			AgentVO agentVO = agentMapper.getAgentById(id);
			if( agentVO == null )
			{
				return new MethodResult(MethodResult.FAIL, "未绑定云端在线");
			}
			//发送短信
			Integer code = (Integer)((int)((Math.random()*9+1)*100000));
		    String message = new String("【致云科技】致云Zhicloud欢迎您，您的手机验证码是：" + code + "，请及时完成验证。"); 
			String state  =new SendSms().zhicloudSendSms(agentVO.getPhone(),message);
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
		logger.debug("AgentServiceImpl.resetPasswordCheckEmailOrPhoneCode()");
		try 
		{
			HttpServletRequest request = RequestContext.getHttpRequest();
			String  id= StringUtil.trim(parameter.get("userId"));
			String myGetPhoneMessage  = StringUtil.trim(parameter.get("myGetPhoneMessage"));
			String myGetEmailMessage  = StringUtil.trim(parameter.get("myGetEmailMessage"));
			String code = request.getSession().getAttribute("resetPasswordEmailOrPhoneCode").toString();
			
			SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
			AgentMapper agentMapper = this.sqlSession.getMapper(AgentMapper.class);
			AgentVO agentVO = agentMapper.getAgentById(id);
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
					user.put("email",    agentVO.getEmail());
					new SendMail().resetPasswordEmail(user);
					return new MethodResult(MethodResult.SUCCESS,agentVO.getEmail());
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
					String state  =new SendSms().zhicloudSendSms(agentVO.getPhone(),message);
					if (("1").equals(state)) 
					{
						System.out.println("短信已经成功发送");
						return new MethodResult(MethodResult.SUCCESS,agentVO.getPhone());
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
	public String baseInfoPageEmailEdit(HttpServletRequest request,HttpServletResponse response) 
	{
		logger.debug("AgentServiceImpl.baseInfoPageEmailEdit()");
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		String id = loginInfo.getUserId();
		AgentMapper agentMapper = this.sqlSession.getMapper(AgentMapper.class);
		AgentVO agentVO = agentMapper.getAgentById(id);
		if(agentVO==null){
			request.setAttribute("notexsit", "yes");
			request.setAttribute("message", "账户不存在，请和管理员联系！");
			return "/public/warning_dialog.jsp";
		}
		
		 
		else
		{
			request.setAttribute("agentVO", agentVO); 
			return "/security/agent/changeemail.jsp"; 
		}
	}
	
	@Callable
	@Transactional(readOnly = false)
	public MethodResult updateBaseInfoEmail(Map<String, Object> parameter) {
		Long begin = System.currentTimeMillis();
		Integer logStatus = AppConstant.OPER_LOG_FAIL;
		HttpServletRequest request = RequestContext.getHttpRequest();
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		logger.debug("AgentServiceImpl.updateBaseInfoEmail()");
		try
		{
			// 参数处理
			String userId = loginInfo.getUserId();
			String email          = StringUtil.trim(parameter.get("myNewEmail"));
			String emailMessage   = StringUtil.trim(parameter.get("changeEmailNewCode")); 
			if (request.getSession().getAttribute("changeEmailByNewEmailCode")==null) {
				return new MethodResult(MethodResult.FAIL,"请获取验证码");
			}
			String emailCode      = request.getSession().getAttribute("changeEmailByNewEmailCode").toString();
			if (userId.isEmpty()) {
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
			AgentMapper agentMapper = this.sqlSession.getMapper(AgentMapper.class); 
			AgentVO agentVO = agentMapper.getAgentByAccount(email);
			SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
			if( agentVO != null )
			{
				return new MethodResult(MethodResult.FAIL, "邮箱[" + email + "]已经存在");
			}
			// 更新表
			Map<String, Object> userData = new LinkedHashMap<String, Object>();
			userData.put("id",     userId);
			userData.put("email",  email);
 			int n =agentMapper.updateEmailById(userData);
			if( n<=0 )
			{
				throw new AppException("修改失败");
			}
			Map<String,Object> userData2 = new LinkedHashMap<String, Object>();
			userData2.put("id", userId);
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
				operLogData.put("content", "修改邮箱");
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
	public MethodResult changeEmailOrPhone(Map<String, String> parameter)
	{ 
		logger.debug("AgentServiceImpl.changeEmailOrPhone()");
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
	@Transactional(readOnly = false)
	public MethodResult updateBaseInfoEmailSendCode(Map<String, Object> parameter) {
		logger.debug("AgentServiceImpl.updateBaseInfoEmailGetCode()");
		try
		{
			HttpServletRequest request = RequestContext.getHttpRequest();
			// 参数处理
			String email = StringUtil.trim(parameter.get("myEmailNumber"));
			if( email.isEmpty() )
			{
				return new MethodResult(MethodResult.FAIL, "邮箱不能为空");
			} 
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
		logger.debug("AgentServiceImpl.updateBaseInfoEmailSendCodeAgain()");
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
			AgentMapper agentMapper = this.sqlSession.getMapper(AgentMapper.class); 
			AgentVO agentVO = agentMapper.getAgentByAccount(email);
			if( agentVO != null )
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
	public MethodResult changeEmailCheckEmailOrPhone(Map<String, String> parameter)
	{
		logger.debug("AgentServiceImpl.changeEmailCheckEmailorPhone()");
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
	
	@Callable
	public String baseInfoPagePhoneEdit(HttpServletRequest request,HttpServletResponse response) 
	{
		logger.debug("AgentServiceImpl.baseInfoPagePhoneEdit()");
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		String id = loginInfo.getUserId();
		AgentMapper agentMapper = this.sqlSession.getMapper(AgentMapper.class);
		AgentVO agentVO = agentMapper.getAgentById(id);
		if(agentVO==null){
			request.setAttribute("notexsit", "yes");
			request.setAttribute("message", "账户不存在，请和管理员联系！");
			return "/public/warning_dialog.jsp";
		}
		
		 
		else
		{
			request.setAttribute("agentVO", agentVO); 
			return "/security/agent/changemobile.jsp"; 
		}
	}
	
	@Callable
	@Transactional(readOnly = false)
	public MethodResult updateBaseInfoPhoneEmailCode(Map<String, Object> parameter) {
		logger.debug("AgentServiceImpl.updateBaseInfoPhoneEmailCode()");
		try
		{
			HttpServletRequest request = RequestContext.getHttpRequest();
			// 参数处理
			String email = StringUtil.trim(parameter.get("myEmailNumber"));
			if( email.isEmpty() )
			{
				return new MethodResult(MethodResult.FAIL, "邮箱不能为空");
			} 
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
		logger.debug("AgentServiceImpl.updateBaseInfoPhoneSendMessage()");
		try {
			HttpServletRequest request = RequestContext.getHttpRequest();
			String phone       = StringUtil.trim(parameter.get("myNewMobile"));
			if (phone.isEmpty()) 
			{
				return new MethodResult(MethodResult.FAIL, "手机号码不能为空");
			}
			//判断手机是否绑定
			AgentMapper agentMapper = this.sqlSession.getMapper(AgentMapper.class);
			AgentVO agentVO = agentMapper.getAgentByPhone(phone);
			if (agentVO != null) {
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
	public MethodResult updateBaseInfoPhone(Map<String, Object> parameter) {
		Long begin = System.currentTimeMillis();
		Integer logStatus = AppConstant.OPER_LOG_FAIL;
		HttpServletRequest request = RequestContext.getHttpRequest();
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		logger.debug("AgentServiceImpl.updateBaseInfoPhone()");
		try
		{
			// 参数处理
			String id = loginInfo.getUserId();
			String phone       = StringUtil.trim(parameter.get("myNewMobile")); 
			String messageCode = StringUtil.trim(parameter.get("changeMobileNewCode"));
			
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
			AgentMapper agentMapper = this.sqlSession.getMapper(AgentMapper.class);
			AgentVO agentVO = agentMapper.getAgentByPhone(phone);
			
			if (agentVO != null) {
				return new MethodResult(MethodResult.FAIL,"该手机已绑定云端在线账号");
			}
			
			// 更新表
			Map<String, Object> userData = new LinkedHashMap<String, Object>();
			userData.put("id",    id);
			userData.put("phone", phone); 			
			int n =agentMapper.updatePhoneById(userData);
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
				operLogData.put("content", "修改手机号码");
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

	/**
	 * 跳转充值页面
	 * @see com.zhicloud.op.service.AgentService#toRecharge(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */

    @Callable
	public String toRecharge(HttpServletRequest request,
			HttpServletResponse response) {

		logger.debug("AgentServiceImpl.toRecharge()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
//		if( loginInfo.hasPrivilege(PrivilegeConstant.operator_agent_recharge) == false )
//		{
//			return "/public/have_not_access_dialog.jsp";
//		}
		
		// 参数判断
		String agentId = (String) request.getParameter("agentId");
		if( StringUtil.isBlank(agentId) )
		{
			throw new AppException("agentId不能为空");
		}
		
		SysGroupMapper sysGroupMapper = this.sqlSession.getMapper(SysGroupMapper.class);
		AgentMapper agentMapper       = this.sqlSession.getMapper(AgentMapper.class);
		
		// 获取组
		List<SysGroupVO> sysGroupList = sysGroupMapper.getAll();
		request.setAttribute("sysGroupList", sysGroupList);

		// 获取代理商数据
		AgentVO agent = agentMapper.getAgentById(agentId);
		if( agent != null )
		{
			request.setAttribute("agent", agent);
			return "/security/operator/agent_recharge.jsp";
		}
		else
		{
			request.setAttribute("message", "找不到代理商的记录");
			return "/public/warning_dialog.jsp";
		}
	}

	/**
	 * 运营商为代理商充值
	 * @see com.zhicloud.op.service.AgentService#operatorRecharge(java.util.Map)
	 */
	@Callable
	@Transactional(readOnly = false)
	public MethodResult operatorRecharge(Map<String, Object> parameter) {
		logger.debug("AgentServiceImpl.operatorRecharge()");
		try
		{
			HttpServletRequest request = RequestContext.getHttpRequest();
			
			// 权限判断
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			
//			if (loginInfo.getUserType()==AppConstant.SYS_USER_TYPE_OPERATOR&&loginInfo.hasPrivilege(PrivilegeConstant.operator_agent_recharge) == false) 
//			{
//				return new MethodResult(MethodResult.FAIL, "您没有权限进行此操作");
//			}
			//获取参数
			
			String  agentId = StringUtil.trim(parameter.get("agent_id"));
			String  cashValue = StringUtil.trim(parameter.get("cash_value")); 
			String  reason = StringUtil.trim(parameter.get("reason")); 
			if(reason.equals("3")){
				reason = StringUtil.trim(parameter.get("diy_reason")); 
			}else if (reason.equals("1")){
				reason = "试用赠送费用";
			} 
			if (cashValue.isEmpty())
			{
				return new MethodResult(MethodResult.FAIL,"充值金额不能为空");
			}
			BigDecimal cashV = new BigDecimal(cashValue); 
			
			
			//更新相关数据库
 			String now = StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS");
			AccountBalanceDetailMapper accountBalanceDetailMapper = this.sqlSession.getMapper(AccountBalanceDetailMapper.class);  
			AgentMapper agentMapper = this.sqlSession.getMapper(AgentMapper.class); 
 	  		int n = 0;
			BigDecimal balance_before_change =  new BigDecimal("0");
			BigDecimal balance_after_change = new BigDecimal("0"); 
				
			AgentVO agentVO = agentMapper.getAgentById(agentId);
			if(agentVO != null)
			{
				balance_before_change =  agentVO.getAccountBalance();
			}
			
			balance_after_change = balance_before_change.add(cashV);
			Map<String, Object> balanceDetailData = new LinkedHashMap<String, Object>(); 
			 
			 
 			balanceDetailData = new LinkedHashMap<String, Object>(); 
			balanceDetailData.put("id",     StringUtil.generateUUID()); 
			balanceDetailData.put("balanceAfterChange",     balance_after_change);
			balanceDetailData.put("balanceBeforeChange",     balance_before_change);
			balanceDetailData.put("amount",     cashV);  
			balanceDetailData.put("userId",     agentVO.getId());  
			balanceDetailData.put("rechargeStatus",     "2");  
			balanceDetailData.put("type",     "1");  
			balanceDetailData.put("payType",     "6");  
			balanceDetailData.put("changeTime",     now); 
			balanceDetailData.put("description",     reason);  
			int k = accountBalanceDetailMapper.addAccountBalanceDetail(balanceDetailData);
			if(k<=0){
				
				throw new AppException("充值失败");
			}
			 
			Map<String, Object> balanceData = new LinkedHashMap<String, Object>(); 
			balanceData.put("id",     agentVO.getId()); 
			balanceData.put("accountBalance",     balance_after_change);
			balanceData.put("balanceUpdateTime",     now); 
			n = agentMapper.updateBalanceById(balanceData); 
			
			
			return new MethodResult(MethodResult.SUCCESS,"充值成功");
			
		}
		catch( Exception e )
		{
			logger.error("AgentServiceImpl.operatorRecharge()",e);
			throw new AppException("充值失败");
		}
	} 
	


}
