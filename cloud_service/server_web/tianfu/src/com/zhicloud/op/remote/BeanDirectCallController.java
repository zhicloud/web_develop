package com.zhicloud.op.remote;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zhicloud.op.app.helper.LoginHelper;
import com.zhicloud.op.common.util.ObjectUtil;
import com.zhicloud.op.common.util.StreamUtil;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.common.util.json.JSONLibUtil;
import com.zhicloud.op.core.CoreSpringContextManager;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.exception.CodedException;
import com.zhicloud.op.exception.ErrorCode;
import com.zhicloud.op.exception.IllegalDataException;

@Controller
public class BeanDirectCallController
{
	
	
	public static final Logger logger = Logger.getLogger(BeanDirectCallController.class);
	
	/**
	 * 用于将url直接转到service处理，返回一个处理页面的url，然后跳转到该页面
	 */
	@RequestMapping("/bean/page.do")
	public String beanPage(HttpServletRequest request, HttpServletResponse response) throws AppException
	{
		logger.debug("DirectBeanController.beanPage()");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/plain; charset=utf-8");
		
		long time1 = System.nanoTime();
		
		String beanName = request.getParameter("bean");
		String methodName = request.getParameter("method");
//		logger.info("DirectBeanController.beanPage() -> request:" + this.getRemoteHost(request));
		if( StringUtil.isBlank(beanName) )
		{
			logger.error("["+Thread.currentThread().getId()+"] ", new AppException("参数beanName不能为空"));
			return "/public/page_not_exists.jsp";
		}
		
		if( StringUtil.isBlank(methodName) )
		{
			logger.error("["+Thread.currentThread().getId()+"] ", new AppException("参数methodName不能为空"));
			return "/public/page_not_exists.jsp";
		}
		
		// 查找bean
		Object beanObj = CoreSpringContextManager.getBean(beanName);
		if( beanObj==null )
		{
			logger.error("["+Thread.currentThread().getId()+"] ", new AppException("找不到bean["+beanName+"]"));
			return "/public/page_not_exists.jsp";
		}
		
		// 查找函数签名兼容的方法
		Class<?>[] parameterTypes = new Class<?>[2];
		parameterTypes[0] = request.getClass();
		parameterTypes[1] = response.getClass();
		Method method = ObjectUtil.getCompatibleMethod(beanObj.getClass(), methodName, parameterTypes);
		if( method==null )
		{
			logger.error("["+Thread.currentThread().getId()+"] ", new NoSuchMethodException(ObjectUtil.toMethodDescription(methodName, parameterTypes)));
			return "/public/page_not_exists.jsp";
		}
		
		// 调用找到的方法
		try
		{
			long time2 = System.nanoTime();
			logger.debug("查找到的兼容方法的函数签名为: " + ObjectUtil.toMethodDescription(method));
			logger.debug("查找方法使用了[" + ((time2 - time1) / 1000000.0) + "]毫秒");
 				
			checkMethodCallable(beanObj, beanName, method, LoginHelper.isLogin(request));
			 
			// 返回的必须为一个uri路径
			return (String)method.invoke(beanObj, request, response);
		}
		catch( CodedException e )
		{
			if( ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN.equals(e.getErrorCode()) )
			{
				logger.error("["+Thread.currentThread().getId()+"] ", e);
				request.setAttribute("outtime", "yes");
				String userType = request.getParameter("userType");
				if(userType!=null&&userType.equals("4")){
					
					return "/public/user/big_login.jsp";
				}
				if(userType!=null&&userType.equals("3")){
					
					return "/public/agent/login.jsp";
				}
				return "/public/top_reload.jsp";
			}
			else
			{
				throw e;
			}
		}
		catch( InvocationTargetException e )
		{
			Throwable targetException = e.getTargetException();
			logger.error("", targetException);
			throw AppException.wrapException(targetException);
		}
		catch( Throwable e )
		{
			logger.error("["+Thread.currentThread().getId()+"] ", e);
			throw AppException.wrapException(e);
		}
	}
	
	/**
	 * 用于将url直接转到service处理，service的处理完之后将数据写进response返回
	 */
	@RequestMapping("/bean/ajax.do")
	public String beanAjax(HttpServletRequest request, HttpServletResponse response) throws AppException
	{
		logger.debug("DirectBeanController.beanAjax()");
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/plain; charset=utf-8");
		
		long time1 = System.nanoTime();
		
		String beanName = request.getParameter("bean");
		String methodName = request.getParameter("method");
		
		// 查找bean
		Object beanObj = CoreSpringContextManager.getBean(beanName);
		if( beanObj==null )
		{
			throw new AppException("找不到bean["+beanName+"]");
		}
		
		// 查找函数签名兼容的方法
		Class<?>[] parameterTypes = new Class<?>[2];
		parameterTypes[0] = request.getClass();
		parameterTypes[1] = response.getClass();
		Method method = ObjectUtil.getCompatibleMethod(beanObj.getClass(), methodName, parameterTypes);
		if( method==null )
		{
			throw new AppException(new NoSuchMethodException(ObjectUtil.toMethodDescription(methodName, parameterTypes)));
		}
		
		// 调用找到的方法
		try
		{
			long time2 = System.nanoTime();
			logger.debug("查找到的兼容方法的函数签名为: " + ObjectUtil.toMethodDescription(method));
			logger.debug("查找方法使用了[" + ((time2 - time1) / 1000000.0) + "]毫秒");
			checkMethodCallable(beanObj, beanName, method, LoginHelper.isLogin(request));
			// 返回的必须为一个uri路径
			return (String)method.invoke(beanObj, request, response);
		}
		catch( InvocationTargetException e )
		{
			Throwable targetException = e.getTargetException();
			logger.error("", targetException);
			throw AppException.wrapException(targetException);
		}
		catch( Throwable e )
		{
			logger.error("", e);
			throw AppException.wrapException(e);
		}
	}
	
	/**
	 * 用于直接方法调用，返回json数据
	 * @throws IOException 
	 */
	@RequestMapping("/bean/call.do")
	public void beanCall(HttpServletRequest request, HttpServletResponse response) throws AppException
	{
		try
		{
			logger.debug("DirectBeanController.beanCall()");
			long time1 = System.nanoTime();

			response.setCharacterEncoding("utf-8");
			response.setContentType("text/plain; charset=utf-8");
			
			byte[] bytes = StreamUtil.inputStreamToByteArray(request.getInputStream());
			String postString = new String(bytes, "utf-8");
			logger.debug("<RemoteCallServlet>.doPost() -> " + postString);
			
			
			// 将获取到的json数据转换为json对象
			JSONObject obj = (JSONObject)JSONObject.fromObject(postString);
			if (obj == null)
			{
				throw new IllegalDataException("无法处理json字符串, json:[" + postString + "]");
			}
			Boolean async = (Boolean) obj.get("async");
			String beanName = (String) obj.get("beanName");
			String methodName = (String) obj.get("methodName");
			JSONArray methodParameters = (JSONArray) obj.get("methodParameters");
			logger.debug("[RemoteCall(async:" + async + ")] " + beanName + ":" + methodName + "(...)");
			
			// 查找bean
			Object beanObj = CoreSpringContextManager.getBean(beanName);
			if( beanObj==null )
			{
				throw new AppException("找不到bean["+beanName+"]");
			}
			
			// 查找函数签名兼容的方法
			Class<?>[] parameterTypes = ObjectUtil.toClassList(methodParameters).toArray(new Class[0]);
			Method method = ObjectUtil.getCompatibleMethod(beanObj.getClass(), methodName, parameterTypes);
			if( method==null )
			{
				throw new NoSuchMethodException(ObjectUtil.toMethodDescription(methodName, parameterTypes));
			}
			
			// 调用找到的方法
			try
			{
				long time2 = System.nanoTime();
				logger.debug("查找到的兼容方法的函数签名为: " + ObjectUtil.toMethodDescription(method));
				logger.debug("查找方法使用了[" + ((time2 - time1) / 1000000.0) + "]毫秒");
				// 判断是否可以调用 
				checkMethodCallable(beanObj, beanName, method, LoginHelper.isLogin(request)); 
				// 调用
				Object rtObj = method.invoke(beanObj, methodParameters.toArray());
				// 返回json数据
				response.getWriter().print(JSONLibUtil.toJSONString(toSuccessReply(rtObj)));
			}
			catch( InvocationTargetException e )
			{
				Throwable targetException = e.getTargetException();
				throw AppException.wrapException(targetException);
			}
		}
		catch( Throwable e )
		{
			// 返回json数据
			logger.error("", e);
			try
			{
				response.getWriter().print(JSONLibUtil.toJSONString(toExceptionReply(AppException.wrapException(e))));
			}
			catch (IOException e1)
			{
				logger.error("", e1);
			}
		}
	}
	
	
	/*
	 * 检测这个方法能否被调用
	 */
	public void checkMethodCallable(Object bean, String beanName, Method method, boolean isLogin) throws SecurityException, NoSuchMethodException
	{
		if( bean instanceof BeanDirectCallable==false )
		{
			throw new AppException("bean["+beanName+"]没有实现接口BeanDirectCallable, 不可远程调用方法" + ObjectUtil.toMethodDescription(method));
		}
		int rt = ((BeanDirectCallable) bean).isCallable(method, isLogin);
		if( rt==BeanDirectCallable.NOT_CALLABLE )
		{
			throw new AppException("此方法不可远程调用, " + ObjectUtil.toMethodDescription(method));
		}
		if( rt==BeanDirectCallable.NOT_CALLABLE_BEFORE_LOGIN )
		{
			throw new CodedException("此方法必须登录后才可远程调用, " + ObjectUtil.toMethodDescription(method), ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN);
		}
	}
	
	

	public static Map<String, Object> toExceptionReply(Throwable e)
	{
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("status", "exception");
		map.put("exceptionClass", e.getClass().getName());
		map.put("exceptionMessage", e.getMessage());
		
		if( e instanceof CodedException )
		{
			map.put("errorCode", ((CodedException)e).getErrorCode());
		}
		
		return map;
	}

	
	public static Map<String, Object> toSuccessReply(Object obj)
	{
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		map.put("status", "success");
		map.put("result", obj);
		return map;
	}
	
	
	
	public String getRemoteHost(javax.servlet.http.HttpServletRequest request){
	    String ip = request.getHeader("x-forwarded-for");
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getHeader("WL-Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
	        ip = request.getRemoteAddr();
	    }
	    return ip.equals("0:0:0:0:0:0:0:1")?"127.0.0.1":ip;
	}
	
	
	
	
	
	
	
	
	
}











