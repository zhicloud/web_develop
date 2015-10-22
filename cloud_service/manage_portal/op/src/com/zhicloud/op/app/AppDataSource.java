package com.zhicloud.op.app;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.dbcp.BasicDataSource;

import com.zhicloud.op.common.util.AESUtil;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.exception.AppException;

public class AppDataSource extends BasicDataSource
{
	
	//--------------------
	
	private static String key = "<--!@-zcop-#$-->";		// 这个key很关键，请不要随意改变
	
	private static String encriptPassword(String password)
	{
		try
		{
			return StringUtil.bytesToBase64(AESUtil.encrypt(key.getBytes(), password.getBytes("utf8")));
		}
		catch( UnsupportedEncodingException e )
		{
			throw new AppException("失败");
		}
	}
	
	private static String decriptPassword(String password)
	{
		try
		{
			return new String(AESUtil.decrypt(key.getBytes(), StringUtil.base64ToBytes(password)), "utf8");
		}
		catch( IOException e )
		{
			throw new AppException("失败");
		}
	}
	
	//----------------------------

	@Override
	public void setUsername(String username)
	{
		super.setUsername(username);
	}
	
	@Override
	public void setPassword(String password)
	{
		super.setPassword(decriptPassword(password));
	}
	
	
	
	public static void main(String[] args)
	{
		System.out.println(encriptPassword("920125"));
		System.out.println(decriptPassword("2HvO4ncMJ8vPzY//Vos+tA=="));
	}
}
