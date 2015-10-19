package com.zhicloud.ms.common.util;

import com.zhicloud.ms.exception.AppException;

public class ThreadUtil
{
	
	public static void sleep(long millis)
	{
		try
		{
			Thread.sleep(millis);
		}
		catch (InterruptedException e)
		{
			throw new AppException("失败");
		}
	}
	
	
}
