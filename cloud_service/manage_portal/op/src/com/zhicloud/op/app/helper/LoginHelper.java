package com.zhicloud.op.app.helper;

import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;

import com.zhicloud.op.common.util.SHA1Util;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.login.LoginInfo;
import com.zhicloud.op.service.constant.AppInconstant;

public class LoginHelper
{
	
	public final static String LOGIN_INFO = "_LOGIN_INFO_";
	
	//-------------

	/**
	 * 判断用户是否已经登录
	 */
	public static boolean isLogin(HttpServletRequest request)
	{
		Integer userType = Integer.valueOf(request.getParameter("userType"));
		return isLogin(request, userType);
	}
	
	/**
	 * 判断用户是否已经登录
	 */
	public static boolean isLogin(HttpServletRequest request, Integer userType)
	{
		LoginInfo loginInfo = getLoginInfo(request, userType);
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
		String userType = request.getParameter("userType");
		setLoginInfo(request, userType, loginInfo);
	}
	/**
	 * 将代理商信息存放到session里
	 * @return 
	 */
	public static  synchronized void setAgentLoginInfo(String sessionId, LoginInfo loginInfo)
	{   		
			AppInconstant.agentLogInfo.put(sessionId, loginInfo); 
	}
	/**
	 * 将存放的代理商信息从map中取出来
	 */
	public static synchronized LoginInfo getAgentLoginInfo(String sessionId)
	{   		
			return (LoginInfo) AppInconstant.agentLogInfo.get(sessionId); 
	}
	
	/**
	 * 将LoginInfo存放到session里
	 */
	public static void setLoginInfo(HttpServletRequest request, String userType, LoginInfo loginInfo)
	{
		if( userType==null )
		{
			throw new AppException("userType cannot be ["+userType+"]");
		}
		request.getSession().setAttribute(LOGIN_INFO + userType, loginInfo);
	}
	
	//--------------
	
	/**
	 * 从session里获取LoginInfo
	 */
	public static LoginInfo getLoginInfo(HttpServletRequest request)
	{
		Integer userType = Integer.valueOf(request.getParameter("userType"));
		return getLoginInfo(request, userType);
	}
	
	/**
	 * 从session里获取LoginInfo
	 */
	public static LoginInfo getLoginInfo(HttpServletRequest request, Integer userType)
	{
		if( userType==null )
		{
			throw new AppException("userType cannot be ["+userType+"]");
		}
		return (LoginInfo)request.getSession().getAttribute(LOGIN_INFO + userType);
	}
	
	//-----------------------------
	
	/**
	 * 将参数加密成密码的密文
	 */
	public static String toEncryptedPassword(String password)
	{
		try
		{
			if( StringUtil.isBlank(password) )
			{
				return "";
			}
			return SHA1Util.digestToHex((password+"xyz").getBytes("utf8"));
		}
		catch( UnsupportedEncodingException e )
		{
			throw new AppException("失败");
		} 
	}
	
	
	public static void main(String[] args)
	{
		System.out.println(toEncryptedPassword("ddd"));
	}
}
