package com.zhicloud.ms.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.vo.SysUser;

public interface IUserService {
	public boolean isLogin(String username,String password,HttpServletRequest request);
	
	/**
	 * 
	 * checkTerminalLogin:根据客户端的请求判断是否提供登录.   
	 *
	 * @author sasa
	 * @param request
	 * @param response
	 * @return User
	 * @since JDK 1.7
	 */
	public SysUser checkTerminalLogin(HttpServletRequest request, HttpServletResponse response); 
	
	/**
	 * 
	 * checkAvailable:检测用户名时候存在.  
	 *
	 * @author sean
	 * @param username
	 * @return boolean
	 * @since JDK 1.7
	 */
	public boolean checkAvailable(String username);

	/**
	 * 
	 * changePassword:修改用户的密码.    
	 *
	 * @author sasa
	 * @param request
	 * @param response
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	public MethodResult changePassword(HttpServletRequest request, HttpServletResponse response);
	
	/**
	 * 
	 * changePassword:修改用户的密码.  
	 * @author sean
	 * @param parameter
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	public MethodResult changePassword(Map<String, Object> parameter);
	
	/**
	 * 
	 * queryAllTerminalInGroup:获取所有有群组的用户.  
	 * @author sean
	 * @return List<SysGroupVO>
	 * @since JDK 1.7
	 */
	public List<SysUser> getAllTerminalInGroup(Map<String, Object> parameter);
	
	/**
	 * 
	 * queryAllTerminalOutGroup:获取所有没有群组的用户.  
	 * @author sean
	 * @return List<SysGroupVO>
	 * @since JDK 1.7
	 */
	public List<SysUser> getAllTerminalOutGroup();
	
	/**
	 * 注销用户
	 * 
	 * @param request
	 * @return
	 */
	public void logout(HttpServletRequest request);
	
	/**
	 * 
	 * 修改密码时验证旧密码是否正确
	 * @param oldPassword
	 * @param request
	 * @return
	 */
	public boolean checkOldPassword(String oldPassword,HttpServletRequest request);
	
	/**
	 * 
	 * 修改管理员密码
	 * @param newPassword
	 * @param request
	 * @return
	 */
	public boolean changeAdminPwd(String newPassword,HttpServletRequest request);
	/**
	 * 
	* @Title: getUserInfoByName 
	* @Description: 通过用户名获取用户 
	* @param @param username
	* @param @return      
	* @return SysUser     
	* @throws
	 */
	public SysUser getUserInfoByName(String username);
	
	/**
	 * getSysRoleById:(根据用户的ID去查询用户的授权角色，包含角色组下的角色).  
	 *
	 * @author chenjinhua
	 * @param userId
	 * @return List<String>
	 * @since JDK 1.7
	 */
	public List<String> getSysRoleById(String userId);
	
	
	/**
	 * 
	 * 检查别名是否存在
	 * @param alias
	 * @return
	 */
	public boolean checkAlias(String alias);
	
	/**
	 * 修改别名
	 * @param request
	 * @param response
	 * @return
	 */
	public MethodResult modifyAlias(HttpServletRequest request,HttpServletResponse response);
} 
