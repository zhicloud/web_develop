package com.zhicloud.ms.service.impl;

import com.zhicloud.ms.app.helper.LoginHelper;
import com.zhicloud.ms.constant.AppConstant;
import com.zhicloud.ms.login.LoginInfo;
import com.zhicloud.ms.mapper.SysUserMapper;
import com.zhicloud.ms.mapper.TerminalUserMapper;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.IUserService;
import com.zhicloud.ms.util.MD5;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.SysUser;
import com.zhicloud.ms.vo.TerminalUserVO;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service("userService")
@Transactional(readOnly=true)
public class UserServiceImpl implements IUserService{
	private static final Logger logger = Logger.getLogger(UserServiceImpl.class);
	@Resource
	private SqlSession sqlSession;
	
	@Override
	public boolean isLogin(String username, String password,HttpServletRequest request) {
		logger.debug("UserServiceImpl.isLogin()");
		boolean flag = false;
		if(StringUtil.isBlank(username) || StringUtil.isBlank(password)){
			return false;
		}else{
			password = MD5.md5(username, password);
		}
		SysUserMapper userMapper = this.sqlSession.getMapper(SysUserMapper.class);
		Map<String,Object> userCondition = new LinkedHashMap<String,Object>();
		userCondition.put("username", username);
		userCondition.put("password", password);
		userCondition.put("type", 1);
		SysUser sysUser = userMapper.checkLogin(userCondition);
		if(sysUser!=null){
			flag = true;
			LoginInfo loginInfo = new LoginInfo();
			loginInfo.setLogin(true);
			loginInfo.setUserId(sysUser.getId());
			loginInfo.setGroupId(sysUser.getGroupId());
			loginInfo.setUserType(sysUser.getType());
			loginInfo.setUsername(sysUser.getUsername());
			LoginHelper.setLoginInfo(request, loginInfo);
		}else{
			//如果用户名登录查不到结果，我们认为是使用别名登录
			//通过别名查询用户
			sysUser = userMapper.queryUserByAlias(username);
			//如果用户存在，取出用户名再和密码进行查询
			if(sysUser!=null){
				String name = sysUser.getUsername();
				Map<String,Object> condition = new LinkedHashMap<String,Object>();
				condition.put("username", name);
				condition.put("password", password); 
				condition.put("type", 1);
				//返回结果
				sysUser = userMapper.checkLogin(condition);
				if(sysUser==null){
				}else{
					flag = true;
					LoginInfo loginInfo = new LoginInfo();
					loginInfo.setLogin(true);
					loginInfo.setUserId(sysUser.getId());
					loginInfo.setGroupId(sysUser.getGroupId());
					loginInfo.setUserType(sysUser.getType());
					loginInfo.setUsername(sysUser.getUsername());
					LoginHelper.setLoginInfo(request, loginInfo);
				}
			}else{
			}
		}
		return flag;
	}

	/**
	 * 
	 * @see com.zhicloud.ms.service.IUserService#checkTerminalLogin(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	public SysUser checkTerminalLogin(HttpServletRequest request,
			HttpServletResponse response) {
		//获取用户名
		String username = StringUtil.trim(request.getParameter("user_name"));
		//获取密码
		String password = StringUtil.trim(request.getParameter("password"));
		//设置用户类型
		Integer type = AppConstant.SYS_USER_TYPE_TERMINAL_USER;
 		SysUserMapper userMapper = this.sqlSession.getMapper(SysUserMapper.class);
		Map<String,Object> userCondition = new LinkedHashMap<String,Object>();
		userCondition.put("username", username);
		userCondition.put("password", password); 
		userCondition.put("type", type);
		//通过用户名查询结果
		SysUser user = userMapper.checkLogin(userCondition);
		if(user!=null){
			return user;
		}else{
			//如果用户名登录查不到结果，我们认为是使用别名登录
			//通过别名查询用户
			user = userMapper.queryUserByAlias(username);
			//如果用户存在，取出用户名再和密码进行查询
			if(user!=null){
				String name = user.getUsername();
				Map<String,Object> condition = new LinkedHashMap<String,Object>();
				condition.put("username", name);
				condition.put("password", password); 
				condition.put("type", type);
				//返回结果
				return userMapper.checkLogin(condition);
			}else{
				return null;
			}
		}
	}

	/**
	 * 修改用户密码.
	 * @see com.zhicloud.ms.service.IUserService#changePassword(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
	@Transactional(readOnly=false)
	public MethodResult changePassword(HttpServletRequest request,
			HttpServletResponse response) {
		//获取用户名
		String username = StringUtil.trim(request.getParameter("user_name"));
		//获取密码
		String password = StringUtil.trim(request.getParameter("password"));
		//获取新密码
		String newPassword = StringUtil.trim(request.getParameter("new_password"));
		//设置用户类型
		Integer type = AppConstant.SYS_USER_TYPE_TERMINAL_USER;
 		SysUserMapper userMapper = this.sqlSession.getMapper(SysUserMapper.class);
		Map<String,Object> userCondition = new LinkedHashMap<String,Object>();
		userCondition.put("username", username);
		userCondition.put("password", password); 
		userCondition.put("type", type);
		//返回查询结果
		SysUser user =  userMapper.checkLogin(userCondition);
		
		if(user == null){
			//如果用户名登录查不到结果，我们认为是使用别名登录
			//通过别名查询用户
			user = userMapper.queryUserByAlias(username);
			//如果用户存在，取出用户名再和密码进行查询
			if(user!=null){
				String name = user.getUsername();
				Map<String,Object> condition = new LinkedHashMap<String,Object>();
				condition.put("username", name);
				condition.put("password", password); 
				condition.put("type", type);
				//返回结果
				user = userMapper.checkLogin(condition);
				if(user==null){
					return new MethodResult(MethodResult.FAIL, "not found user info");
				}else{
					Map<String,Object> data = new LinkedHashMap<String,Object>();
					data.put("modified_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
					data.put("password", newPassword);
					data.put("id", user.getId());  
					int i = userMapper.updatePasswordById(data);
					if(i>0){
						return new MethodResult(MethodResult.SUCCESS, "success");				
					}else{
						return new MethodResult(MethodResult.FAIL, "DB operator fail");
					}
				}
			}else{
				return new MethodResult(MethodResult.FAIL, "not found user info");
			}
		}else{
			Map<String,Object> data = new LinkedHashMap<String,Object>();
			data.put("modified_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
			data.put("password", newPassword);
			data.put("id", user.getId());  
			int i = userMapper.updatePasswordById(data);
			if(i>0){
				return new MethodResult(MethodResult.SUCCESS, "success");				
			}else{
				return new MethodResult(MethodResult.FAIL, "DB operator fail");
			}
			
		}
	}

	/**
	 * 
	 * @see com.zhicloud.ms.service.IUserService#changePassword(java.util.Map)
	 */
	@Override
	@Transactional(readOnly=false)
	public MethodResult changePassword(Map<String, Object> parameter) {
		//获取用户id
		String id = StringUtil.trim(parameter.get("id"));
		//获取用户名
		String username = StringUtil.trim(parameter.get("username"));
		//获取密码
		String password = StringUtil.trim(parameter.get("password"));
		
		password = MD5.md5(username, password);
		
 		SysUserMapper userMapper = this.sqlSession.getMapper(SysUserMapper.class);
		Map<String,Object> data = new LinkedHashMap<String,Object>();
		data.put("modified_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
		data.put("password", password);
		data.put("id", id);  
		
		Integer result = userMapper.updatePasswordById(data);
		if(result > 0){
			return new MethodResult(MethodResult.SUCCESS, "更新成功");				
		}else{
			return new MethodResult(MethodResult.FAIL, "更新失败");
		}
	}

	/**
	 * 
	 * @see com.zhicloud.ms.service.IUserService#getAllTerminalInGroup(java.util.Map)
	 */
	@Override
	public List<SysUser> getAllTerminalInGroup(Map<String, Object> parameter) {
 		SysUserMapper userMapper = this.sqlSession.getMapper(SysUserMapper.class);
 		String groupId = StringUtil.trim(parameter.get("group_id"));
		Map<String,Object> condition = new LinkedHashMap<String,Object>();
		condition.put("group_id", groupId);
		condition.put("type", AppConstant.SYS_USER_TYPE_TERMINAL_USER);
		return userMapper.queryAllTerminalInGroup(condition);
	}

	/**
	 * 
	 * @see com.zhicloud.ms.service.IUserService#getAllTerminalOutGroup()
	 */
	@Override
	public List<SysUser> getAllTerminalOutGroup() {
		SysUserMapper userMapper = this.sqlSession.getMapper(SysUserMapper.class);
		Map<String,Object> condition = new LinkedHashMap<String,Object>();
		condition.put("type", AppConstant.SYS_USER_TYPE_TERMINAL_USER);
		return userMapper.queryAllTerminalOutGroup(condition);
	}

	 

	@Override
	public void logout(HttpServletRequest request) {
		LoginHelper.loginOut(request);
	} 

	@Override
	public boolean checkOldPassword(String oldPassword,HttpServletRequest request) {
		SysUserMapper userMapper = this.sqlSession.getMapper(SysUserMapper.class);
		boolean flag = false;
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		String id = loginInfo.getUserId();
		SysUser user = userMapper.getUserById(id);
		String newPassword = MD5.md5(loginInfo.getUsername(), oldPassword);
		if(user.getPassword().equals(newPassword)){
			flag = true;
		}
		return flag;
	}

	@Override
	@Transactional(readOnly=false)
	public boolean changeAdminPwd(String newPassword, HttpServletRequest request) {
		//获取用户名
 		SysUserMapper userMapper = this.sqlSession.getMapper(SysUserMapper.class);
 		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
 		boolean flag = false;
		String userName = loginInfo.getUsername();
		String password = MD5.md5(userName, newPassword);
			Map<String,Object> data = new LinkedHashMap<String,Object>();
			data.put("password", password);
			data.put("id", loginInfo.getUserId());  
			int i = userMapper.updatePasswordById(data);
			if(i>0){
				flag = true;	
				//注销
				LoginHelper.loginOut(request);
			}
					
		return flag;
	}

	/**
	 * 
	 * @see com.zhicloud.ms.service.IUserService#checkAvailable(java.lang.String)
	 */
	@Override
	public boolean checkAvailable(String username) {
		
 		SysUserMapper userMapper = this.sqlSession.getMapper(SysUserMapper.class);
 		TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
 		SysUser sysUser = userMapper.queryUserByUsername(username);

 		if(sysUser == null) {
 			return true;
 		}
      TerminalUserVO terminalUserVO = terminalUserMapper.getById(sysUser.getId());

      if (terminalUserVO == null){
          return true;
      }

		return false;
	}
   /**
    * 通过用户名获取用户
   * <p>Title: getUserInfoByName</p> 
   * <p>Description: </p> 
   * @param username
   * @return 
   * @see com.zhicloud.ms.service.IUserService#getUserInfoByName(java.lang.String)
    */
    @Override
    public SysUser getUserInfoByName(String username) {
        SysUserMapper userMapper = this.sqlSession.getMapper(SysUserMapper.class);
        return userMapper.queryUserByUsername(username);
    }

    /**
	 * 
	 * @see com.zhicloud.ms.service.IUserService#checkAvailable(java.lang.String)
	 */
	@Override
	public boolean checkAlias(String alias) {
 		SysUserMapper userMapper = this.sqlSession.getMapper(SysUserMapper.class);
 		SysUser sysUser = userMapper.queryUserByAlias(alias);
 		boolean flag = false;
 		if(sysUser == null) {
 			flag = true;
 		}
		return flag;
	}
	
	@Transactional(readOnly=false)
	@Override
	public MethodResult modifyAlias(HttpServletRequest request,HttpServletResponse response) {
		//获取用户名
		String username = StringUtil.trim(request.getParameter("user_name"));
		//获取密码
		String password = StringUtil.trim(request.getParameter("password"));
		//获取新别名
		String alias = StringUtil.trim(request.getParameter("alias"));
		//设置用户类型
		Integer type = AppConstant.SYS_USER_TYPE_TERMINAL_USER;
 		SysUserMapper userMapper = this.sqlSession.getMapper(SysUserMapper.class);
		Map<String,Object> userCondition = new LinkedHashMap<String,Object>();
		userCondition.put("username", username);
		userCondition.put("password", password); 
		userCondition.put("type", type);
		//返回查询结果
		SysUser user =  userMapper.checkLogin(userCondition);
		if(user==null){
			//如果用户名登录查不到结果，我们认为是使用别名登录
			//通过别名查询用户
			user = userMapper.queryUserByAlias(username);
			//如果用户存在，取出用户名再和密码进行查询
			if(user!=null){
				String name = user.getUsername();
				Map<String,Object> condition = new LinkedHashMap<String,Object>();
				condition.put("username", name);
				condition.put("password", password); 
				condition.put("type", type);
				//返回结果
				user = userMapper.checkLogin(condition);
				if(user==null){
					return new MethodResult(MethodResult.FAIL, "not found user info");
				}else{
					Map<String,Object> data = new LinkedHashMap<String,Object>();
					data.put("modified_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
					data.put("alias", alias);
					data.put("id", user.getId());  
					int i = userMapper.updateAliasById(data);
					if(i>0){
						return new MethodResult(MethodResult.SUCCESS, "success");				
					}else{
						return new MethodResult(MethodResult.FAIL, "DB operator fail");
					}
				}
			}else{
				return new MethodResult(MethodResult.FAIL, "not found user info");
			}	
		}else{
			Map<String,Object> data = new LinkedHashMap<String,Object>();
			data.put("modified_time", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
			data.put("alias", alias);
			data.put("id", user.getId());  
			int i = userMapper.updateAliasById(data);
			if(i>0){
				return new MethodResult(MethodResult.SUCCESS, "success");				
			}else{
				return new MethodResult(MethodResult.FAIL, "DB operator fail");
			}
			
		}
	}

	@Override
	public List<String> getSysRoleById(String userId) {
		// TODO Auto-generated method stub
 		SysUserMapper userMapper = this.sqlSession.getMapper(SysUserMapper.class);
 	    return userMapper.getSysRoleById(userId);	
	}
}
