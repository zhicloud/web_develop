package com.zhicloud.op.app.helper;

import javax.servlet.http.HttpServletRequest;

import com.zhicloud.op.exception.AppException;

public class VerificationCodeHelper
{

	public static final String VERIFICATION_CODE_KEY = "_VERIFICATION_CODE_KEY_";
	
	public static void setCode(HttpServletRequest request, String code)
	{
		String userType = request.getParameter("userType");
		if( userType==null )
		{
			throw new AppException("parameter 'userType' not found");
		}
		request.getSession().setAttribute(VERIFICATION_CODE_KEY + userType, code);
	}
	
	public static String getCode(HttpServletRequest request)
	{
		String userType = request.getParameter("userType");
		if( userType==null )
		{
			throw new AppException("parameter 'userType' not found");
		}
		return (String)request.getSession().getAttribute(VERIFICATION_CODE_KEY + userType);
	}
	
	public static boolean isMatch(HttpServletRequest request, String code)
	{
		String realCode = getCode(request);
		if( realCode==null )
		{
			return false;
		}
		return realCode.equalsIgnoreCase(code);
	}
}
