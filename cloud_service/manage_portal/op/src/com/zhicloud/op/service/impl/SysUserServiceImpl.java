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

import com.zhicloud.op.app.helper.LoginHelper;
import com.zhicloud.op.app.helper.ServiceHelper;
import com.zhicloud.op.app.helper.VerificationCodeHelper;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.login.LoginInfo;
import com.zhicloud.op.mybatis.mapper.OperLogMapper;
import com.zhicloud.op.mybatis.mapper.OperatorMapper;
import com.zhicloud.op.mybatis.mapper.SysPrivilegeMapper;
import com.zhicloud.op.mybatis.mapper.SysUserMapper;
import com.zhicloud.op.mybatis.mapper.TerminalUserMapper;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.CallWithoutLogin;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.request.RequestContext;
import com.zhicloud.op.service.SysUserService;
import com.zhicloud.op.service.constant.AppConstant;
import com.zhicloud.op.vo.OperatorVO;
import com.zhicloud.op.vo.SysUserVO;
import com.zhicloud.op.vo.TerminalUserVO;

@Transactional(readOnly=true)
public class SysUserServiceImpl extends BeanDirectCallableDefaultImpl implements SysUserService
{
	
	public static final Logger logger = Logger.getLogger(SysUserServiceImpl.class);
	
	
	private SqlSession sqlSession;
	
	
	public void setSqlSession(SqlSession sqlSession)
	{
		this.sqlSession = sqlSession;
	}

	//---------------
	
	@Callable
	@CallWithoutLogin
	@Transactional(readOnly=false)
	public MethodResult login(Map<String, String> parameter)
	{
		logger.debug("SysUserServiceImpl.login()");
		
		HttpServletRequest request = RequestContext.getHttpRequest();
		
		String type             = StringUtil.trim(parameter.get("type"));
		String account          = StringUtil.trim(parameter.get("account"));
		String password         = StringUtil.trim(parameter.get("password"));
		String verificationCode = StringUtil.trim(parameter.get("verification_code"));
 		// 数据完整性判断
		if( type==null )
		{
			throw new AppException("type不能为空");
		}
		if( account.isEmpty() )
		{
			return new MethodResult(MethodResult.FAIL,"用户名不能为空");
		}
		if( password.isEmpty() )
		{
			return new MethodResult(MethodResult.FAIL,"密码不能为空");
		}
		if( verificationCode==null )
		{
			return new MethodResult(MethodResult.FAIL,"验证码不能为空");
		}
		
		// 验证码判断
		if( VerificationCodeHelper.isMatch(request, verificationCode)==false )
		{
			return new MethodResult(MethodResult.FAIL, "验证码错误");
		}
		
		// 从数据库查询用户信息
		SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
		
		Map<String, Object> condition = new LinkedHashMap<String, Object>();
		condition.put("type",     type);
		condition.put("account",  account);
		condition.put("password", LoginHelper.toEncryptedPassword(password));
		SysUserVO sysUser = sysUserMapper.getByTypeAndAccountAndPassword(condition);
		
		if( sysUser==null )
		{
			return new MethodResult(MethodResult.FAIL, "用户名或密码错误");
		}
		String balance = "";
		//查TerminalUser表
		TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
		TerminalUserVO terminalUserVO = terminalUserMapper.getTerminalUserById(sysUser.getId());
		if( Integer.parseInt(type) == (AppConstant.SYS_USER_TYPE_TERMINAL_USER) )
		{

			if( terminalUserVO.getStatus() == AppConstant.TERMINAL_USER_STATUS_DISABLED )
			{
				return new MethodResult(MethodResult.FAIL, "该账户已禁用");
			}else if(terminalUserVO.getStatus() == AppConstant.TERMINAL_USER_STATUS_END){
				return new MethodResult(MethodResult.FAIL, "该账户已结束使用");
				
			}else if(terminalUserVO.getStatus() == AppConstant.TERMINAL_USER_STATUS_NOT_VERIFIED){
				return new MethodResult(MethodResult.FAIL, "该账户未激活");
				
			}else{
				balance = terminalUserVO.getAccountBalance()+"";
			}
		}
		
		
		//判断是否存在该用户
		
		//如果用户是终端用户，状态为禁用时提示已禁用。否则为其他类型用户(代理商或运营商)
		if( Integer.parseInt(type) == AppConstant.SYS_USER_TYPE_TERMINAL_USER )
		{
			if( sysUser.getStatus() == AppConstant.TERMINAL_USER_STATUS_DISABLED )
			{
				return new MethodResult(MethodResult.FAIL, "该账户已禁用");
			}
			if( sysUser.getStatus() == AppConstant.TERMINAL_USER_STATUS_END)
			{
				return new MethodResult(MethodResult.FAIL, "该账户已结束使用");
			}
		}
		else
		{
			if( sysUser.getStatus() == AppConstant.AGENT_STATUS_DISABLED )
			{
				return new MethodResult(MethodResult.FAIL, "该账户已禁用");
			}
			if( sysUser.getStatus() == AppConstant.AGENT_STATUS_END )
			{
				return new MethodResult(MethodResult.FAIL, "该账户已结束使用");
			}
		}
		OperatorMapper operMapper = this.sqlSession.getMapper(OperatorMapper.class);
		// 将用户信息设置到LoginInfo，存放于session
		LoginInfo loginInfo = new LoginInfo(true);
		loginInfo.setUserId(sysUser.getId());
		loginInfo.setGroupId(sysUser.getGroupId());
		loginInfo.setUserType(sysUser.getType());
		if(sysUser.getType()==2){
			OperatorVO oper = operMapper.getOperatorById(sysUser.getId());
			if(oper.getName()!=null && !StringUtil.isBlank(oper.getName())){
				loginInfo.setAccount(oper.getName());
			}else{
				loginInfo.setAccount(sysUser.getAccount());
			}
		}else{
			loginInfo.setAccount(sysUser.getAccount());
		}
		if(terminalUserVO!=null){			
			loginInfo.setPhone(terminalUserVO.getPhone());
		}
		loginInfo.setPrivilege(fetchPrivilegeByUserId(sysUser.getId()));	// 获取权限
		
		LoginHelper.setLoginInfo(request, loginInfo);
		if(sysUser.getType() != AppConstant.SYS_USER_TYPE_ADMIN){			
			OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
			Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
			operLogData.put("id", StringUtil.generateUUID());
			operLogData.put("userId", loginInfo.getUserId());
			operLogData.put("content", "用户登录");
			operLogData.put("operTime", StringUtil.dateToString(new Date(),"yyyyMMddHHmmssSSS"));
			operLogData.put("status", AppConstant.OPER_LOG_SUCCESS);
			operLogData.put("resourceName", "用户名:"+loginInfo.getAccount());
			operLogData.put("operDuration", "1");
			operLogMapper.addOperLog(operLogData);
		}
		if(balance.equals("")){			
			return new MethodResult(MethodResult.SUCCESS, "登陆成功");
		}else{
			MethodResult result = new MethodResult(MethodResult.SUCCESS, "登陆成功");
			result.put("balance", balance);
			result.put("userId", sysUser.getId());
			return  result;
			
		}
	}
	
	private List<String> fetchPrivilegeByUserId(String userId)
	{
		SysPrivilegeMapper sysPrivilegeMapper = this.sqlSession.getMapper(SysPrivilegeMapper.class);
		List<String> privileges = sysPrivilegeMapper.getPrivilegeCodesByUserId(userId);
		logger.debug("privileges: "+privileges);
		return privileges;
	}
	
	
	@Callable
	@CallWithoutLogin
	@Transactional(readOnly=false)
	public MethodResult logout()
	{
		logger.debug("SysUserServiceImpl.logout()");
		HttpServletRequest request = RequestContext.getHttpRequest();
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		LoginHelper.setLoginInfo(request, null);
		OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
		Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
		operLogData.put("id", StringUtil.generateUUID());
		operLogData.put("userId", loginInfo.getUserId());
		operLogData.put("content", "用户注销");
		operLogData.put("operTime", StringUtil.dateToString(new Date(),"yyyyMMddHHmmssSSS"));
		operLogData.put("status", AppConstant.OPER_LOG_SUCCESS);
		operLogData.put("resourceName", "用户名:"+loginInfo.getAccount());
		operLogData.put("operDuration", "1");
		operLogMapper.addOperLog(operLogData);
		return new MethodResult(MethodResult.SUCCESS, "注销成功");
	}
	
	
//	@Callable
//	public MethodResult logout1()
//	{
//		logger.debug("SysUserServiceImpl.logout1()");
//		HttpServletRequest request = RequestContext.getHttpRequest();
//		request.getSession().setAttribute("LOGIN_INFO1",null); 
//		return new MethodResult(MethodResult.SUCCESS, "注销成功");
//	}
//	
//	@Callable
//	public MethodResult logout2()
//	{
//		logger.debug("SysUserServiceImpl.logout2()");
//		HttpServletRequest request = RequestContext.getHttpRequest();
//		request.getSession().setAttribute("LOGIN_INFO2",null); 
//		return new MethodResult(MethodResult.SUCCESS, "注销成功");
//	}
//	
//	@Callable
//	public MethodResult logout3()
//	{
//		logger.debug("SysUserServiceImpl.logout3()");
//		HttpServletRequest request = RequestContext.getHttpRequest();
//		request.getSession().setAttribute("LOGIN_INFO3",null); 
//		return new MethodResult(MethodResult.SUCCESS, "注销成功");
//	}
//	
//	@Callable
//	public MethodResult logout4()
//	{
//		logger.debug("SysUserServiceImpl.logout4()");
//		HttpServletRequest request = RequestContext.getHttpRequest();
//		request.getSession().setAttribute("LOGIN_INFO4",null); 
//		return new MethodResult(MethodResult.SUCCESS, "注销成功");
//	}
	
	
	@Callable
	public void querySysUser(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("SysUserServiceImpl.querySysUser()");
		try
		{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");
			
			// 参数处理
			Integer type   = StringUtil.parseInteger(request.getParameter("type"), null);
			String account = StringUtil.trim(request.getParameter("account"));
			Integer page   = StringUtil.parseInteger(request.getParameter("page"), 1) - 1;	// 前台过来的是从1开始，需要的是从0开始
			Integer rows   = StringUtil.parseInteger(request.getParameter("rows"), 10);
			
			
			SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
			
			// 查询数据库
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("type",      type);
			condition.put("account",   "%"+account+"%");
			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			int total = sysUserMapper.queryPageCount(condition);
			List<SysUserVO> groupList = sysUserMapper.queryPage(condition);
			
			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, groupList);
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("查询失败");
		}

	}
	
	
	@Callable
	public void querySysUserNotInGroupId(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("SysUserServiceImpl.querySysUserNotInGroupId()");
		try
		{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");
			
			// 参数处理
			String groupId = StringUtil.trim(request.getParameter("groupId"));
			Integer type   = StringUtil.parseInteger(request.getParameter("type"), null);
			String account = StringUtil.trim(request.getParameter("account"));
			Integer page   = StringUtil.parseInteger(request.getParameter("page"), 1) - 1;	// 前台过来的是从1开始，需要的是从0开始
			Integer rows   = StringUtil.parseInteger(request.getParameter("rows"), 10);
			
			if( StringUtil.isBlank(groupId) )
			{
				throw new AppException("groupId不能为空");
			}
			
			SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
			
			// 查询数据库
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("groupId",   groupId);
			condition.put("type",      type);
			condition.put("account",   "%"+account+"%");
			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			int total = sysUserMapper.queryPageNotInGroupIdCount(condition);
			List<SysUserVO> groupList = sysUserMapper.queryPageNotInGroupId(condition);
			
			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, groupList);
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("查询失败");
		}

	}
	
	
	@Callable
	public String updatePasswordPage(HttpServletRequest request,HttpServletResponse response) 
	{
		logger.debug("OperatorServiceImpl.passwordUpdatePage");
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request, 1);
		// 权限判断，判断是否为运营商、是否有权限
		if( AppConstant.SYS_USER_TYPE_ADMIN.equals(loginInfo.getUserType())==false )
		{
			return "/public/have_not_access.jsp";
		}
		String id = loginInfo.getUserId();
		request.setAttribute("id", id);
		return "/security/admin/admin_update_password.jsp";
	}
	
	@Callable
	@Transactional(readOnly = false)
	public MethodResult updatePasswordById(Map<String, Object> parameter) {
		
		logger.debug("SysUserServiceImpl.updatePasswordById()");
		try
		{
			// 参数处理
			String id             = StringUtil.trim(parameter.get("admin_id"));
			String oldPassword    = StringUtil.trim(parameter.get("old_password"));
			String password       = StringUtil.trim(parameter.get("password"));
			String confirm        = StringUtil.trim(parameter.get("confirm"));
			
			SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
			SysUserVO sysUserVO = sysUserMapper.getUserPassword(id);
			
			if( id.isEmpty() )
			{
				return new MethodResult(MethodResult.FAIL, "账户id不能为空");
			}
			if (oldPassword.isEmpty()) 
			{
				return new MethodResult(MethodResult.FAIL,"原密码不能为空");
			}
			
			if (!LoginHelper.toEncryptedPassword(oldPassword).equals(sysUserVO.getPassword())) 
			{
				return new MethodResult(MethodResult.FAIL,"原密码不正确");
			}
			
			if(password.isEmpty())
			{
				return new MethodResult(MethodResult.FAIL,"新密码不能为空");
			}
			if(sysUserVO.getPassword().equals(LoginHelper.toEncryptedPassword(password)))
			{
				return new MethodResult(MethodResult.FAIL, "新密码和原密码一致！");				
			}
			
			if(!password.equals(confirm))
			{
				return new MethodResult(MethodResult.FAIL, "密码不匹配");
			}
			
			// 更新系统用户表
			Map<String, Object> userData = new LinkedHashMap<String, Object>();
			userData.put("id",       id);
			userData.put("password", LoginHelper.toEncryptedPassword(password));

			int n = sysUserMapper.updateSysUserPassword(userData);
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
	public void querySysUserNotGetImage(HttpServletRequest request,
			HttpServletResponse response) { 
		
		logger.debug("SysUserServiceImpl.querySysUserNotGetImage()");
		try
		{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");
			
			// 参数处理
			String imageId = StringUtil.trim(request.getParameter("imageId"));  
			String agentIds = StringUtil.trim(request.getParameter("agentIds"));  
			Integer page   = StringUtil.parseInteger(request.getParameter("page"), 1) - 1;	// 前台过来的是从1开始，需要的是从0开始
			Integer rows   = StringUtil.parseInteger(request.getParameter("rows"), 10);
			 
			String alreadyChecked = "";
			if(agentIds != null && agentIds.length()>0){
				
				String [] agents = agentIds.split(",");
				for(int i = 0;i<agents.length;i++)
				{ 
					if(i == agents.length-1){						
						alreadyChecked =alreadyChecked+ "'"+agents[i]+"'";
					}else{						
						alreadyChecked =alreadyChecked+ "'"+agents[i]+"',";
					}
				}
			}
			SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
			
			// 查询数据库
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("imageId",   imageId);  
			if(agentIds != null && agentIds.length()>0){ 
				condition.put("alreadyChecked",   alreadyChecked);  
			}
			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			int total = sysUserMapper.queryPageNotInImageIdCount(condition);
			List<SysUserVO> userList = sysUserMapper.queryPageNotInImageId(condition);
			
			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, userList);
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("查询失败");
		}
		
	}
	
	
}

















