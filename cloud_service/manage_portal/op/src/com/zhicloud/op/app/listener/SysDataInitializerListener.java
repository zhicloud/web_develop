package com.zhicloud.op.app.listener;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

import com.zhicloud.op.app.listener.task.DiskImageInitializer;
import com.zhicloud.op.exception.AppException;

public class SysDataInitializerListener implements Servlet
{

	public static final Logger logger = Logger.getLogger(SysDataInitializerListener.class);
	
	
	private DiskImageInitializer diskImageInitializer = null;


	@Override
	public void init(ServletConfig config) throws ServletException
	{
		logger.debug("SysDataInitializerListener.init()");
		String intervalTime = config.getInitParameter("interval_time");
		diskImageInitializer = new DiskImageInitializer(Integer.parseInt(intervalTime));
		new Thread(diskImageInitializer).start();
		
	}
	
	@Override
	public void destroy()
	{
		logger.info("SysDataInitializerListener.destroy()");
		diskImageInitializer.stop();
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
