package com.zhicloud.op.common.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.zhicloud.op.exception.AppException;

public class SHA1Util
{

	public static byte[] digest(byte[] bytes)
	{
		try
		{
			return MessageDigest.getInstance("sha1").digest(bytes);
		}
		catch( NoSuchAlgorithmException e )
		{ 
			throw new AppException("失败");
		}
	}
	
	public static String digestToHex(byte[] bytes)
	{
		byte[] sha1bytes = digest(bytes);
		return StringUtil.bytesToHex(sha1bytes);
	}
	
	public static String digestToBase64(byte[] bytes)
	{
		byte[] sha1bytes = digest(bytes);
		return StringUtil.bytesToBase64(sha1bytes);
	}
	
	public static void main(String[] args)
	{
		System.out.println(digestToBase64("abc".getBytes()));;
	}
}
