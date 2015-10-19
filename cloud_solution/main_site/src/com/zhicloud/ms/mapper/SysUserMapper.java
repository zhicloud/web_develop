package com.zhicloud.ms.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.ms.vo.SysUser;

public interface SysUserMapper {
	public SysUser checkLogin(Map<String,Object> condition);
	/**
	 * 
	 * updatePasswordById:通过id修改用户密码
	 *
	 * @author sasa
	 * @param condition
	 * @return int
	 * @since JDK 1.7
	 */
	public int updatePasswordById(Map<String,Object> condition);
	/**
	 * addSysUser:(这里用一句话描述这个方法的作用).  
	 *
	 * @author sean
	 * @param data
	 * @return Integer
	 * @since JDK 1.7
	 */
	public Integer addSysUser(Map<String, Object> data);
	
	
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
	 * updateSysUserById:(这里用一句话描述这个方法的作用).  
	 *
	 * @author sean
	 * @param data
	 * @return Integer
	 * @since JDK 1.7
	 */
	public Integer updateSysUserById(Map<String, Object> data);
	/**
	 * deleteSysUserById:(这里用一句话描述这个方法的作用).  
	 *
	 * @author sean
	 * @param userId
	 * @return Integer
	 * @since JDK 1.7
	 */
	public Integer deleteSysUserById(String userId);
	
	/**
	 * 
	 * queryAllTerminalInGroup:查询所有有群组的用户.  
	 *
	 * @author sean
	 * @return List<SysGroupVO>
	 * @since JDK 1.7
	 */
	public List<SysUser> queryAllTerminalInGroup(Map<String, Object> condition);

	/**
	 * 
	 * queryAllTerminalOutGroup:查询所有没有群组的用户.  
	 * @author sean
	 * @return List<SysGroupVO>
	 * @since JDK 1.7
	 */
	public List<SysUser> queryAllTerminalOutGroup(Map<String, Object> condition);
	
	/**
	 * 
	 * queryUserByUsername:按username查询.  
	 *
	 * @author sean
	 * @param username
	 * @return SysUser
	 * @since JDK 1.7
	 */
	public SysUser queryUserByUsername(String username);
	/**
	 * 
	 * addItems:添加群组成员.  
	 *
	 * @author sean
	 * @param condition
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	public int addItems(Map<String, Object> condition);
	
	/**
	 * 
	 * deleteItems:移除群组成员.  
	 *
	 * @author sean
	 * @param ids
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	public int deleteItems(String id);
	/**
	 * 
	 * getUserById:根据id查询用户 
	 *
	 * @author sasa
	 * @param id
	 * @return SysUser
	 * @since JDK 1.7
	 */
	public SysUser getUserById(String id);
	
	/**
	 * 
	 * queryUserByAlias:按alias查询.  
	 *
	 * @author zyf
	 * @param alias
	 * @return SysUser
	 * @since JDK 1.7
	 */
	public SysUser queryUserByAlias(String alias);
	
	/**
	 * 根据ID修改别名
	 * @param condition
	 * @return
	 */
	public int updateAliasById(Map<String,Object> condition);
	
}