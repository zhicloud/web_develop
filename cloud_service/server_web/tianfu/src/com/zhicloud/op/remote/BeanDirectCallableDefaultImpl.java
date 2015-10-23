package com.zhicloud.op.remote;

import java.lang.reflect.Method;

public class BeanDirectCallableDefaultImpl implements BeanDirectCallable
{

	@Override
	public int isCallable(Method method, boolean isLogin) throws SecurityException, NoSuchMethodException
	{
		Method foundMethod = this.getClass().getMethod(method.getName(), method.getParameterTypes());
		if( foundMethod.getAnnotation(Callable.class) == null )
		{
			return BeanDirectCallable.NOT_CALLABLE;
		}
		if( isLogin == false && foundMethod.getAnnotation(CallWithoutLogin.class) == null )
		{
			return BeanDirectCallable.NOT_CALLABLE_BEFORE_LOGIN;
		}
		return BeanDirectCallable.CALLABLE;
	}

}
