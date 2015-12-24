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
import com.zhicloud.op.common.util.RandomPassword;
import com.zhicloud.op.common.util.SendMail;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.common.util.constant.MailConstant;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.login.LoginInfo;
import com.zhicloud.op.mybatis.mapper.OperatorMapper;
import com.zhicloud.op.mybatis.mapper.SysGroupMapper;
import com.zhicloud.op.mybatis.mapper.SysUserMapper;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.request.RequestContext;
import com.zhicloud.op.service.OperatorService;
import com.zhicloud.op.service.constant.AppConstant;
import com.zhicloud.op.vo.OperatorVO;
import com.zhicloud.op.vo.SysGroupVO;
import com.zhicloud.op.vo.SysUserVO;

@Transactional(readOnly = true)
public class OperatorServiceImpl extends BeanDirectCallableDefaultImpl implements OperatorService
{
	public static final Logger logger = Logger.getLogger(OperatorServiceImpl.class);

	private SqlSession sqlSession;

	public void setSqlSession(SqlSession sqlSession)
	{
		this.sqlSession = sqlSession;
	}

	// ---------------

	/**
	 * 
	 */
	@Callable
	public String managePage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("OperatorServiceImpl.managePage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.hasPrivilege(PrivilegeConstant.operator_manage_page) == false )
		{
			return "/public/have_not_access.jsp";
		}
		
		return "/security/admin/operator_manage.jsp";
	}
	@Callable
	public String operatorIndexPage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("OperatorServiceImpl.operatorIndexPage()");
		
		// 权限判断
//		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
//		if( loginInfo.hasPrivilege(PrivilegeConstant.operator_manage_page) == false )
//		{
//			return "/public/have_not_access.jsp";
//		}
		
		return "/security/operator/operator_index.jsp";
	}
	

	@Callable
	public String basicInformationPage(HttpServletRequest request, HttpServletResponse response) 
	{
		logger.debug("OperatorServiceImpl.basicInformationPage()");
		
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request, 2);
		
		// 权限判断，判断是否为运营商、是否有权限
		if( AppConstant.SYS_USER_TYPE_OPERATOR.equals(loginInfo.getUserType())==false || loginInfo.hasPrivilege(PrivilegeConstant.operator_basic_information_page)==false )
		{
			return "/public/have_not_access.jsp";
		}
		
		String id = loginInfo.getUserId();
		
		OperatorMapper operatorMapper = this.sqlSession.getMapper(OperatorMapper.class);
		OperatorVO operator = operatorMapper.getBasicInformationById(id);
		if(operator==null)
		{
			request.setAttribute("notexsit", "yes");
			request.setAttribute("message", "账户不存在，请和管理员联系！");
			return "/public/warning_dialog.jsp";
		}
		request.setAttribute("operator", operator);
		
		return "/security/operator/operator_basic_information.jsp";
	}

	@Callable
	public String updatePasswordPage(HttpServletRequest request,HttpServletResponse response) 
	{
		logger.debug("OperatorServiceImpl.passwordUpdatePage");
		
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request, 2);
		
		// 权限判断，判断是否为运营商、是否有权限
		if( AppConstant.SYS_USER_TYPE_OPERATOR.equals(loginInfo.getUserType())==false || 
			loginInfo.hasPrivilege(PrivilegeConstant.operator_change_password_page) == false )
		{
			return "/public/have_not_access.jsp";
		}
		
		String id = loginInfo.getUserId();
		
		OperatorMapper operatorMapper = this.sqlSession.getMapper(OperatorMapper.class);
		OperatorVO operator = operatorMapper.getBasicInformationById(id);
		request.setAttribute("operator", operator);
		if(operator==null){
			request.setAttribute("notexsit", "yes");
			request.setAttribute("message", "账户不存在，请和管理员联系！");
			return "/public/warning_dialog.jsp";
		}
		
		return "/security/operator/operator_update_password.jsp";
	}

	@Callable
	public String addPage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("OperatorServiceImpl.addPage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.hasPrivilege(PrivilegeConstant.operator_manage_add) == false )
		{
			return "/public/have_not_access.jsp";
		}
		
		SysGroupMapper sysGroupMapper = this.sqlSession.getMapper(SysGroupMapper.class);
		List<SysGroupVO> sysGroupList = sysGroupMapper.getAll();
		request.setAttribute("sysGroupList", sysGroupList);
		
		return "/security/admin/operator_add.jsp";
	}
	
	@Callable
	public String resetPasswordPage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("OperatorServiceImpl.resetPasswordPage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.hasPrivilege(PrivilegeConstant.operator_reset_password) == false )
		{
			return "/public/have_not_access_dialog.jsp";
		}
		//参数处理
		String operatorId = StringUtil.trim(request.getParameter("operatorId"));
		OperatorMapper operatorMapper = this.sqlSession.getMapper(OperatorMapper.class);
		OperatorVO operatorVO         = operatorMapper.getBasicInformationById(operatorId);
		String resetPassword          = RandomPassword.getRandomNum(MailConstant.PASSWORD_LENGTH);
		request.setAttribute("resetPassword", resetPassword);
		request.setAttribute("operatorVO", operatorVO);
		
		return "/security/admin/operator_reset_password.jsp";
	}


	@Callable
	public String modPage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("OperatorServiceImpl.modPage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.hasPrivilege(PrivilegeConstant.operator_manage_modify) == false )
		{
			return "/public/have_not_access.jsp";
		}
		
		// 参数处理
		String operatorId = StringUtil.trim(request.getParameter("operatorId"));
		if( StringUtil.isBlank(operatorId) )
		{
			throw new AppException("operatorId不能为空");
		}

		OperatorMapper operatorMapper = this.sqlSession.getMapper(OperatorMapper.class);
		SysGroupMapper sysGroupMapper = this.sqlSession.getMapper(SysGroupMapper.class);
		
		
		List<SysGroupVO> sysGroupList = sysGroupMapper.getAll();
		request.setAttribute("sysGroupList", sysGroupList);
		
		OperatorVO operator = operatorMapper.getOperatorById(operatorId);
		if( operator != null )
		{
			request.setAttribute("operator", operator);
			return "/security/admin/operator_mod.jsp";
		}
		else
		{
			request.setAttribute("message", "找不到运营商的记录");
			return "/public/warning_dialog.jsp";
		}
	}

	@Callable
	public void queryOperator(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("OperatorServiceImpl.queryOperator()");
		try
		{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");

			// 获取参数
			String account = StringUtil.trim(request.getParameter("account"));
			Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
			Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 10);

			// 查询数据库
			OperatorMapper operatorMapper = this.sqlSession.getMapper(OperatorMapper.class);
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("account", "%" + account + "%");
			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			int total = operatorMapper.queryPageCount(condition); // 总行数
			List<OperatorVO> operatorList = operatorMapper.queryPage(condition);// 分页结果

			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, operatorList);
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("查询失败");
		}
	}

	@Callable
	@Transactional(readOnly = false)
	public MethodResult addOperator(Map<String, String> parameter)
	{
		logger.debug("OperatorServiceImpl.addOpertator()");
		try
		{
			String groupId  = StringUtil.trim(parameter.get("group_id"));
			String account  = StringUtil.trim(parameter.get("account"));
			String name     = StringUtil.trim(parameter.get("name"));
			String password = StringUtil.trim(parameter.get("password"));
			String email    = StringUtil.trim(parameter.get("email"));
			String phone    = StringUtil.trim(parameter.get("phone"));
			if(groupId==null || groupId==""){
				return new MethodResult(MethodResult.FAIL,"请选择一个组");
			}
//			if(account==null || account==""){
//				return new MethodResult(MethodResult.FAIL,"用户名不能为空");
//			}
			if(name==null || name==""){
				return new MethodResult(MethodResult.FAIL,"昵称不能为空");
			}
//			if(password==null || password==""){
//				return new MethodResult(MethodResult.FAIL,"密码不能为空");
//			}
			if(email==null || email==""){
				return new MethodResult(MethodResult.FAIL,"邮箱不能为空");
			}
			if(phone==null || phone==""){
				return new MethodResult(MethodResult.FAIL,"手机号不能为空");
			}
			SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
			OperatorMapper operatorMapper = this.sqlSession.getMapper(OperatorMapper.class);
			
			// 判断运营商账户是否已经存在
			LinkedHashMap<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("type", AppConstant.SYS_USER_TYPE_OPERATOR);
			condition.put("account", email);
			SysUserVO sysUser = sysUserMapper.getByTypeAndAccount(condition);
			List<OperatorVO> operList = operatorMapper.getByName(name);
			if(operList!=null && operList.size()>0){
				return new MethodResult(MethodResult.FAIL, "昵称[" + name + "]已经存在");
			}
	        if( sysUser!=null )
	        {
	           return new MethodResult(MethodResult.FAIL, "邮箱[" + email + "]已经存在");
	        }

			
			String id = StringUtil.generateUUID();
			
			Map<String, Object> userData = new LinkedHashMap<String, Object>();
			userData.put("id",       id);
			userData.put("account",  email);
			password = RandomPassword.getRandomNum(MailConstant.PASSWORD_LENGTH);

			userData.put("password", LoginHelper.toEncryptedPassword(password));
			userData.put("type",     AppConstant.SYS_USER_TYPE_OPERATOR);
			userData.put("groupId",  groupId);

			int n = sysUserMapper.addSysUser(userData);

			Map<String, Object> operatorData = new LinkedHashMap<String, Object>();
			operatorData.put("id",         id);
			operatorData.put("name",       name);
			operatorData.put("email",      email);
			operatorData.put("phone",      phone);
			operatorData.put("status",     AppConstant.OPERATOR_STATUS_NORMAL);
			operatorData.put("createTime", StringUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm"));

			int m = operatorMapper.addOperator(operatorData);
			//发送邮件
			Map<String, Object> user = new LinkedHashMap<String, Object>();
			user.put("password", password);
			user.put("email",    email); 
			user.put("account",  email); 
			new SendMail().sendPasswordEmail(user);

			if( n > 0 && m > 0 )
			{
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
	public MethodResult updateOperatorById(Map<String, Object> parameter)
	{
		logger.debug("OperatorServiceImpl.updateOperatorById()");
		try
		{
			// 权限判断
			HttpServletRequest request = RequestContext.getHttpRequest();
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			if( loginInfo.hasPrivilege("operator_manage_modify") == false )
			{
				return new MethodResult(MethodResult.FAIL, "您没有权限进行此操作");
			}
			
			// 参数处理
			String operatorId = StringUtil.trim(parameter.get("operator_id"));
			String groupId    = StringUtil.trim(parameter.get("group_id"));
			String account    = StringUtil.trim(parameter.get("account"));
			String name       = StringUtil.trim(parameter.get("name"));
			String email      = StringUtil.trim(parameter.get("email"));
			String phone      = StringUtil.trim(parameter.get("phone"));
			Integer status    = StringUtil.parseInteger((String)parameter.get("status"), AppConstant.OPERATOR_STATUS_NORMAL);

			if(groupId==null || groupId==""){
				return new MethodResult(MethodResult.FAIL,"请选择一个组");
			}
//			if(account==null || account==""){
//				return new MethodResult(MethodResult.FAIL,"账户不能为空");
//			}
			if(name==null || name==""){
				return new MethodResult(MethodResult.FAIL,"昵称不能为空");
			}
			if(email==null || email==""){
				return new MethodResult(MethodResult.FAIL,"邮箱不能为空");
			}
			if(phone==null || phone==""){
				return new MethodResult(MethodResult.FAIL,"手机号不能为空");
			}
			if( operatorId.isEmpty() )
			{
				return new MethodResult(MethodResult.FAIL, "账户id不能为空");
			}
			if(status!=null){
				if(status<1||status>3){
					return new MethodResult(MethodResult.FAIL, "状态不合法");
					
				}
			}
			SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
			OperatorMapper operatorMapper = this.sqlSession.getMapper(OperatorMapper.class);
			
			// 判断运营商账户是否已经存在
			LinkedHashMap<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("type", AppConstant.SYS_USER_TYPE_OPERATOR);
			condition.put("account", email);
			SysUserVO sysUser = sysUserMapper.getByTypeAndAccount(condition);
			List<OperatorVO> operList = operatorMapper.getByName(name);
			if(operList!=null && operList.size()>0 && operList.get(0).getId().equals(operatorId)==false){
				return new MethodResult(MethodResult.FAIL, "昵称[" + name + "]已经存在");
			}
			if( sysUser!=null && sysUser.getId().equals(operatorId)==false )
			{
				return new MethodResult(MethodResult.FAIL, email+ "已经存在");
			}
			
			// 更新系统用户表
			Map<String, Object> userData = new LinkedHashMap<String, Object>();
			userData.put("id",       operatorId);
			userData.put("account",  email);
			userData.put("groupId",  groupId);

			int n = sysUserMapper.updateSysUser(userData);
			if( n==0 )
			{
				return new MethodResult(MethodResult.FAIL, "修改失败");
			}

			// 更新运营商表
			Map<String, Object> operatorData = new LinkedHashMap<String, Object>();
			operatorData.put("id", operatorId);
			operatorData.put("name", name);
			operatorData.put("email", email);
			operatorData.put("phone", phone);
			operatorData.put("status", status);

			operatorMapper.updateOperator(operatorData);

			return new MethodResult(MethodResult.SUCCESS, "修改成功");
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("修改失败");
		}
	}

	@Callable
	@Transactional(readOnly = false)
	public MethodResult deleteOperatorById(String operatorId)
	{
		logger.debug("operatorServiceImpl.deleteOperatorById()");
		try
		{
			if( operatorId == null )
			{
				return new MethodResult(MethodResult.FAIL, "operatorId不能为空");
			}

			OperatorMapper operatorMapper = this.sqlSession.getMapper(OperatorMapper.class);
			int n = operatorMapper.deleteOperatorById(operatorId);

			SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
			int m = sysUserMapper.deleteSysUser(operatorId);

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
			logger.error(e);
			throw new AppException("删除失败");
		}
	}

	@Callable
	@Transactional(readOnly = false)
	public MethodResult deleteOperatorByIds(List<?> operatorIds)
	{
		logger.debug("OperatorServiceImpl.deleteOperatorByIds()");
		try
		{
			if( operatorIds == null || operatorIds.size() == 0 )
			{
				return new MethodResult(MethodResult.FAIL, "operatorIds不能为空");
			}

			OperatorMapper operatorMapper = this.sqlSession.getMapper(OperatorMapper.class);
			int n = operatorMapper.deleteOperatorByIds(operatorIds.toArray(new String[0]));

			SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
			int m = sysUserMapper.deleteSysUsers(operatorIds.toArray(new String[0]));

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
			logger.error(e);
			throw new AppException("删除失败");
		}
	}

	@Callable
	@Transactional(readOnly = false)
	public MethodResult updateBasicInformation(Map<String, Object> parameter) {
		
		logger.debug("OperatorServiceImpl.updateBasicInformation()");
		try
		{
			// 参数处理
			String operatorId = StringUtil.trim(parameter.get("operator_id"));
			String groupId  = StringUtil.trim(parameter.get("group_id"));
			String account    = StringUtil.trim(parameter.get("account"));
			String name       = StringUtil.trim(parameter.get("name"));
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
			if (operatorId.isEmpty()) {
				return new MethodResult(MethodResult.FAIL,"转户id不能为空");
			}

			
			SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
			OperatorMapper operatorMapper = this.sqlSession.getMapper(OperatorMapper.class);
			
			// 判断运营商账户是否已经存在
			LinkedHashMap<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("type", AppConstant.SYS_USER_TYPE_OPERATOR);
			condition.put("account", email);
			SysUserVO sysUser = sysUserMapper.getByTypeAndAccount(condition);
			if( sysUser!=null && sysUser.getId().equals(operatorId)==false )
			{
				return new MethodResult(MethodResult.FAIL, email + "已经存在");
			}
			
			
			// 更新系统用户表
			Map<String, Object> userData = new LinkedHashMap<String, Object>();
			userData.put("id",       operatorId);
			userData.put("account",  email);
			userData.put("password",  "");
			userData.put("groupId",  groupId);

			int n = sysUserMapper.updateSysUser(userData);
			if( n==0 )
			{
				return new MethodResult(MethodResult.FAIL, "修改失败");
			}

			// 更新运营商表
			Map<String, Object> operatorData = new LinkedHashMap<String, Object>();
			operatorData.put("id", operatorId);
			operatorData.put("name", name);
			operatorData.put("email", email);
			operatorData.put("phone", phone);
			operatorData.put("status", status);

			operatorMapper.updateOperator(operatorData);

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
	public MethodResult updatePasswordById(Map<String, Object> parameter) {
		
		logger.debug("OperatorServiceImpl.updatePasswordById()");
		try
		{
			
			// 参数处理
			String operatorId     = StringUtil.trim(parameter.get("operator_id"));
			String oldPassword    = StringUtil.trim(parameter.get("old_password"));
			String password       = StringUtil.trim(parameter.get("password"));
			String confirm        = StringUtil.trim(parameter.get("confirm"));

			SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
			SysUserVO user = sysUserMapper.getUserPassword(operatorId);
			
			if( operatorId.isEmpty() )
			{
				return new MethodResult(MethodResult.FAIL, "账户id不能为空");
			}
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
			userData.put("id",       operatorId);
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
	@Transactional(readOnly = false)
	public MethodResult resetPasswordById(Map<String, Object> parameter) {
		
		logger.debug("OperatorServiceImpl.resetPasswordById()");
		try
		{
			
			// 参数处理
			String operatorId = StringUtil.trim(parameter.get("operator_id"));
			String password   = StringUtil.trim(parameter.get("password"));
			String email      = StringUtil.trim(parameter.get("email"));
			
			if( operatorId.isEmpty() )
			{
				return new MethodResult(MethodResult.FAIL, "账户id不能为空");
			}
			if (password.isEmpty()) {
				return new MethodResult(MethodResult.FAIL, "密码不能为空");
			}
			if (email.isEmpty()) {
				return new MethodResult(MethodResult.FAIL, "邮箱不能为空");
			}
			
			
			SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
			 
			// 更新系统用户表
			Map<String, Object> userData = new LinkedHashMap<String, Object>();
			userData.put("id",       operatorId);
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


}
