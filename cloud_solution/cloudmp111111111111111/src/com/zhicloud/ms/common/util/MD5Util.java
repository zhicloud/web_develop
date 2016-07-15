package com.zhicloud.ms.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.zhicloud.ms.exception.AppException;

public class MD5Util
{

	public static byte[] digest(byte[] bytes)
	{
		try
		{
			byte[] md5bytes = MessageDigest.getInstance("MD5").digest(bytes);
			return md5bytes;
		}
		catch( NoSuchAlgorithmException e )
		{ 
			throw new AppException("失败");
		}
	}
	
	public static String digestToHex(byte[] bytes)
	{
		byte[] md5bytes = digest(bytes);
		return StringUtil.bytesToHex(md5bytes);
	}
	
	public static String digestToBase64(byte[] bytes)
	{
		byte[] md5bytes = digest(bytes);
		return StringUtil.bytesToBase64(md5bytes);
	}
	
	public static void main(String[] args)
	{
		System.out.println(digest("abc".getBytes()));
	}
}
