package com.zhicloud.op.remote;

import java.lang.reflect.Method;

public interface BeanDirectCallable
{

	public static final int CALLABLE = 1;
	public static final int NOT_CALLABLE = 2;
	public static final int NOT_CALLABLE_BEFORE_LOGIN = 3;
	
	public int isCallable(Method method, boolean isLogin) throws SecurityException, NoSuchMethodException;
	
}
