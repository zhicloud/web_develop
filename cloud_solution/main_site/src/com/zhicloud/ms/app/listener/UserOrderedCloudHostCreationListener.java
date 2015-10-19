package com.zhicloud.ms.app.listener;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

import com.zhicloud.ms.app.listener.task.UserOrderedCloudHostCreationRunnable;
import com.zhicloud.ms.exception.AppException;

public class UserOrderedCloudHostCreationListener implements Servlet
{
	
	public static final Logger logger = Logger.getLogger(UserOrderedCloudHostCreationListener.class);
	
	
	private UserOrderedCloudHostCreationRunnable userOrderedCloudHostCreationRunnable = null;


	@Override
	public void init(ServletConfig config) throws ServletException
	{
		logger.debug("UserOrderedCloudHostCreationListener.init()");
		String intervalTime = config.getInitParameter("interval_time");
		String waitTimeAfterSendingCreation = config.getInitParameter("wait_time_after_sending_creation");
		userOrderedCloudHostCreationRunnable = new UserOrderedCloudHostCreationRunnable(Integer.parseInt(intervalTime),
																						Integer.parseInt(waitTimeAfterSendingCreation));
		new Thread(userOrderedCloudHostCreationRunnable).start();
	}
	
	
	@Override
	public void destroy()
	{
		logger.debug("UserOrderedCloudHostCreationListener.destroy()");
		userOrderedCloudHostCreationRunnable.stop();
	}


	@Override
	public ServletConfig getServletConfig()
	{
		throw new AppException("unsupported method");
	}


	@Override
	public String getServletInfo()
	{
		throw new AppException("unsupported method");
	}
	
	
	@Override
	public void service(ServletRequest arg0, ServletResponse arg1) throws ServletException, IOException
	{
		throw new AppException("unsupported method");
	}
	
	
}
