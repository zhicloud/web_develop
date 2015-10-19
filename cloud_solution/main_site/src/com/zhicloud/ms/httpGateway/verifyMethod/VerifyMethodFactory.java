package com.zhicloud.ms.httpGateway.verifyMethod;

import com.zhicloud.ms.exception.AppException;

public class VerifyMethodFactory
{

	public static VerifyMethod create(String methodName)
	{
		if( "M1".equals(methodName) )
		{
			return new M1();
		}
		else if( "M2".equals(methodName) )
		{
			return new M2();
		}
		else
		{
			throw new AppException("unsupported verify method name: ["+methodName+"]");
		}
	}
}
