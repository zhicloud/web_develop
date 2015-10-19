package com.zhicloud.ms.httpGateway.verifyMethod;

import java.io.UnsupportedEncodingException;

import com.zhicloud.ms.common.util.SHA1Util;
import com.zhicloud.ms.exception.AppException;

/**
 * M1使用的是sha1加密
 */
public class M2 implements VerifyMethod
{

	@Override
	public String digest(String challengeKey)
	{
		try
		{
			return SHA1Util.digestToHex(challengeKey.getBytes("utf-8"));
		}
		catch( UnsupportedEncodingException e )
		{
//			throw new AppException(e);
			throw new AppException("失败");
		}
	}
	
}
