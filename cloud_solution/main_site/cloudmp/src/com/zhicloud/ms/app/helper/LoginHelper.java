package com.zhicloud.ms.app.helper;

import javax.servlet.http.HttpServletRequest;

import com.zhicloud.ms.login.LoginInfo;

public class LoginHelper
{
	
	public final static String LOGIN_INFO = "_LOGIN_INFO_";
	
	//-------------

	/**
	 * 判断用户是否已经登录
	 */
	public static boolean isLogin(HttpServletRequest request)
	{
		LoginInfo loginInfo = getLoginInfo(request);
		if( loginInfo!=null )
		{
			return ((LoginInfo)loginInfo).isLogin();
		}
		else
		{
			return false;
		}
	}
	
	//------------------
	
	/**
	 * 将LoginInfo存放到session里
	 */
	public static void setLoginInfo(HttpServletRequest request, LoginInfo loginInfo)
	{
		request.getSession().setAttribute(LOGIN_INFO, loginInfo);
	}
	
	//--------------
	
	/**
	 * 从session里获取LoginInfo
	 */
	public static LoginInfo getLoginInfo(HttpServletRequest request)
	{
		return (LoginInfo)request.getSession().getAttribute(LOGIN_INFO);
	}
	
	public static void loginOut(HttpServletRequest request){
		request.getSession().invalidate();
	}
	
	//-----------------------------
	
	public static void main(String[] args)
	{
		
	}
}