package com.zhicloud.op.app.authorization;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.zhicloud.op.core.CoreSpringContextManager;
import com.zhicloud.op.service.PrivilegeService;

public class PrivilegeInitializeListener implements ServletContextListener
{
	
	public static final Logger logger = Logger.getLogger(PrivilegeInitializeListener.class);

	// -------------------

	public void contextInitialized(ServletContextEvent event)
	{
		try
		{
			logger.info("PrivilegeInitializeListener.contextInitialized() > ["+Thread.currentThread().getId()+"] ");
			PrivilegeService privilegeService = CoreSpringContextManager.getPrivilegeService();
			privilegeService.load(event.getServletContext().getResource("/META-INF/privilege-config.xml"));
		}
		catch( Exception e )
		{
			logger.error("["+Thread.currentThread().getId()+"] ", e);
		}
	}

	public void contextDestroyed(ServletContextEvent event)
	{
		logger.info("PrivilegeInitializeListener.contextDestroyed() > ["+Thread.currentThread().getId()+"] ");
	}

	// -------------------

	

}
