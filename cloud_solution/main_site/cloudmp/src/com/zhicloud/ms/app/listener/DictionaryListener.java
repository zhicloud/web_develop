package com.zhicloud.ms.app.listener;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

import com.zhicloud.ms.app.listener.task.DictionaryInitializer;
import com.zhicloud.ms.exception.AppException;

public class DictionaryListener implements Servlet{
	
	public static final Logger logger = Logger.getLogger(DictionaryListener.class);
	
	
	private DictionaryInitializer dictionaryInitializer = null;


	@Override
	public void init(ServletConfig config) throws ServletException
	{
		logger.debug("SysDataInitializerListener.init()");
		String intervalTime = config.getInitParameter("interval_time");
		dictionaryInitializer = new DictionaryInitializer(Integer.parseInt(intervalTime));
		new Thread(dictionaryInitializer).start();
		
	}
	
	@Override
	public void destroy()
	{
		logger.info("SysDataInitializerListener.destroy()");
		dictionaryInitializer.stop();
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
