package com.zhicloud.op.remote;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zhicloud.op.app.helper.LoginHelper;
import com.zhicloud.op.common.util.StreamUtil;
import com.zhicloud.op.common.util.json.JSONLibUtil;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.exception.IllegalDataException;

@Controller
public class RemoteCallController
{
	
	private static final Logger logger = Logger.getLogger(RemoteCallController.class);
	

	@RequestMapping("/remote/call.do")
	public void call(HttpServletRequest request, HttpServletResponse response)
	{
		long time1 = System.nanoTime();
		logger.debug(request.getRequestURL());

		response.setContentType("text/plain; charset=utf-8");
		PrintWriter out = null;
		
		String rtString = "";
		boolean isLogin = LoginHelper.isLogin(request);
		try
		{
			out = response.getWriter();
			request.setCharacterEncoding("utf-8");
			
			// 获取request流
			byte[] bytes = StreamUtil.inputStreamToByteArray(request.getInputStream());
			String postString = new String(bytes, "utf-8");
			logger.debug("<RemoteCallServlet>.doPost() -> " + postString);

			// 将获取到的json数据转换为json对象
			JSONObject obj = (JSONObject) JSONObject.fromObject(postString);
			if (obj == null)
			{
				throw new IllegalDataException("无法处理json字符串, json:[" + postString + "]");
			}
			Boolean async = (Boolean) obj.get("async");
			String className = (String) obj.get("className");
			String methodName = (String) obj.get("methodName");
			JSONArray methodParameters = (JSONArray) obj.get("methodParameters");

			logger.debug("[RemoteCall(async:" + async + ")] " + className + ":" + methodName + "(...)");

			Class<?> klass = null;
			Class<?>[] methodParameterTypes = null;
			Method method = null;
			try
			{	
				// 获取要调用的函数
				klass = Class.forName(className);
				methodParameterTypes = new Class[methodParameters.size()];
				for (int i = 0; i < methodParameterTypes.length; i++)
				{
					methodParameterTypes[i] = methodParameters.get(i).getClass();
				}
				method = klass.getMethod(methodName, methodParameterTypes);
			}
			catch (NoSuchMethodException e)
			{
				// 找不到要调用的方法, 则查找函数签名兼容的方法
				method = getCompatibleMethod(klass, methodName, methodParameters);
				if (method != null)
				{
					logger.debug("查找到的兼容方法的函数签名为: " + toMethodDescription(method));
				}
				else
				{
					logger.error("找不到函数签名兼容的方法:" + e.getMessage(), e);
					rtString = JSONLibUtil.toJSONString(toExceptionReply(e));
					return;
				}
			}
			catch (Exception e)
			{
				logger.error("", e);
				rtString = JSONLibUtil.toJSONString(toExceptionReply(e));
				return;
			}
			try
			{ 
				// 调用查找到的函数
				// 检测这个方法能不能被调用
				checkMethodCallable(method, isLogin);
				long time2 = System.nanoTime();
				logger.debug("查找方法使用了[" + ((time2 - time1) / 1000000.0) + "]毫秒");
				Object rtObj = method.invoke(klass.newInstance(), methodParameters.toArray());
				rtString = JSONLibUtil.toJSONString(toSuccessReply(rtObj));
				return;
			}
			catch (InvocationTargetException e)
			{
				// 被调用的数据里面发生了异常
				Throwable targetException = e.getTargetException();
				targetException.printStackTrace();
				rtString = JSONLibUtil.toJSONString(toExceptionReply(e.getTargetException()));
				return;
			}
			catch (Exception e)
			{
				// 其它异常
				logger.error("", e);
				rtString = JSONLibUtil.toJSONString(toExceptionReply(e));
				return;
			}
		}
		catch (Exception e)
		{
			logger.error("", e);
			rtString = JSONLibUtil.toJSONString(toExceptionReply(e));
		}
		finally
		{
			out.print(rtString);
		}
	}
	

	/*
	 * 查找兼容函数签名的方法
	 */
	public static Method getCompatibleMethod(Class<?> klass, String methodName, JSONArray parameters)
	{
		List<Method> sameNameMethod = getSameNameMethod(klass, methodName, parameters);
		loop1: for (int i = 0; i < sameNameMethod.size(); i++)
		{
			Method method = sameNameMethod.get(i);
			Class<?>[] types = method.getParameterTypes();
			for (int j = 0; j < types.length; j++)
			{
				if (types[j].isInstance(parameters.get(j)))
				{
					continue; // 可接受
				}
				else
				{
					continue loop1; // 查找下一个method
				}
			}
			return method;
		}
		return null;
	}

	/*
	 * 获取名称相同,参数个数相同的方法
	 */
	public static List<Method> getSameNameMethod(Class<?> klass, String methodName, JSONArray parameters)
	{
		Method[] methods = klass.getMethods();
		List<Method> sameNameMethod = new ArrayList<Method>();
		for (int i = 0; i < methods.length; i++)
		{
			Method method = methods[i];
			if (method.getName().equals(methodName) && method.getParameterTypes().length == parameters.size())
			{
				sameNameMethod.add(method);
			}
		}
		return sameNameMethod;
	}
	

	public static String toMethodDescription(Method method)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(method.getReturnType().getName() + " " + method.getName() + "(");
		Class<?>[] parameterTypes = method.getParameterTypes();
		for (int i = 0; i < parameterTypes.length - 1; i++)
		{
			sb.append(" " + parameterTypes[i].getName() + ",");
		}
		if (parameterTypes.length > 0)
		{
			sb.append(" " + parameterTypes[parameterTypes.length - 1].getName() + " ");
		}
		sb.append(")");
		return sb.toString();
	}


	/*
	 * 检测这个方法能否被调用
	 */
	public void checkMethodCallable(Method method, boolean isLogin)
	{
		if( method.getAnnotation(Callable.class) == null )
		{
			throw new AppException("此方法不可远程调用, method:[" + toMethodDescription(method) + "]");
		}
		if( isLogin==false && method.getAnnotation(CallWithoutLogin.class) == null )
		{
			throw new AppException("此方法必须登录后才可远程调用, method:[" + toMethodDescription(method) + "]");
		}
	}
	
	public static JSONObject toExceptionReply(Throwable e)
	{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("status", "exception");
		jsonObject.put("exceptionClass", e.getClass().getName());
		jsonObject.put("exceptionMessage", e.getMessage());
		return jsonObject;
	}

	public static JSONObject toSuccessReply(Object obj)
	{
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("status", "success");
		jsonObject.put("result", obj);
		return jsonObject;
	}
	
}
