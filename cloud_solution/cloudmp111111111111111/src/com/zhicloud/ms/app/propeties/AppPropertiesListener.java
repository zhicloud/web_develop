package com.zhicloud.ms.app.propeties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

public class AppPropertiesListener implements ServletContextListener
{
	
	private static final Logger logger = Logger.getLogger(AppPropertiesListener.class);

	public void contextInitialized(ServletContextEvent event)
	{
		try
		{
			logger.info("AppPropertiesListener.contextInitialized() > ["+Thread.currentThread().getId()+"] ");
			WarAppPropertiesLoader.load(event.getServletContext().getResource("/META-INF/app-properties.xml"));
		}
		catch (Throwable e)
		{
			logger.error("["+Thread.currentThread().getId()+"] ", e);
		}
	}

	public void contextDestroyed(ServletContextEvent event)
	{
		logger.info("AppPropertiesListener.contextDestroyed() > ["+Thread.currentThread().getId()+"] ");
	}

}
