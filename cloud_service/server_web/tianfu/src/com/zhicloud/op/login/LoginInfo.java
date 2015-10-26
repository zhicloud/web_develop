package com.zhicloud.op.login;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import com.zhicloud.op.service.constant.AppConstant;

public class LoginInfo
{

	private boolean isLogin = false;
	private String userId;
	private String groupId;
	private Integer userType;
	private String account;
	private String phone;

	private Set<String> privilegeSet = new LinkedHashSet<String>();

	public LoginInfo(boolean login)
	{
		this.isLogin = login;
	}

	public boolean isLogin()
	{
		return isLogin;
	}

	public void setLogin(boolean isLogin)
	{
		this.isLogin = isLogin;
	}

	public String getGroupId()
	{
		return groupId;
	}

	public void setGroupId(String groupId)
	{
		this.groupId = groupId;
	}
	
	public String getUserId()
	{
		return userId;
	}

	public void setUserId(String userId)
	{
		this.userId = userId;
	}

	public Integer getUserType()
	{
		return userType;
	}

	public void setUserType(Integer userType)
	{
		this.userType = userType;
	}

	public String getAccount()
	{
		return account;
	}

	public void setAccount(String account)
	{
		this.account = account;
	}

	public void setPrivilege(Collection<String> privileges)
	{
		this.privilegeSet.clear();
		this.privilegeSet.addAll(privileges);
	}
	
	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * 判断是否有权限privilege
	 */
	public boolean hasPrivilege(String privilege)
	{
		if( AppConstant.SYS_USER_TYPE_ADMIN.equals(this.userType) )
		{
			// 超级管理员什么权限都有
			return true;
		}
//		// TODO 必须删除
//		if( AppConstant.SYS_USER_TYPE_OPERATOR.equals(this.userType) )
//		{
//			return true;
//		}
		return this.privilegeSet.contains(privilege);
	}

	/**
	 * 包含privileges中的全部权限才返回true
	 */
	public boolean hasAllPrivileges(String[] privileges)
	{
		if( AppConstant.SYS_USER_TYPE_ADMIN.equals(this.userType) )
		{
			// 超级管理员什么权限都有
			return true;
		}
		Set<String> set = new HashSet<String>();
		for( String privilege : privileges )
		{
			set.add(privilege);
		}
		return this.privilegeSet.containsAll(set);
	}

	/**
	 * 只要有privileges中的一个就返回true
	 */
	public boolean hasAnyPrivilege(String[] privileges)
	{
		if( AppConstant.SYS_USER_TYPE_ADMIN.equals(this.userType) )
		{
			// 超级管理员什么权限都有
			return true;
		}
		for( String privilege : privileges )
		{
			if( this.privilegeSet.contains(privilege) )
			{
				return true;
			}
		}
		return false;
	}
}
