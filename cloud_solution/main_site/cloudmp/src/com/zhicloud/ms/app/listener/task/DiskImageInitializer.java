package com.zhicloud.ms.app.listener.task;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zhicloud.ms.common.util.ThreadUtil;
import com.zhicloud.ms.exception.AppException; 
import com.zhicloud.ms.service.ISysDiskImageService;


public class DiskImageInitializer implements Runnable
{
	
	public static final Logger logger = Logger.getLogger(DiskImageInitializer.class);
	
	private boolean bStop = false;
	
	private int intervalTime = 60;		// 单位秒
	
	private ISysDiskImageService sysDiskImageService;

	public DiskImageInitializer(int intervalTime)
	{
		this.intervalTime = intervalTime;
        BeanFactory factory = new ClassPathXmlApplicationContext("classpath:/applicationContext*.xml");  
        
        sysDiskImageService = (ISysDiskImageService)factory.getBean("sysDiskImageService");
	}
	
	public void stop()
	{
		bStop = true;
	}

	public void run()
	{
		while(true)
		{
			try
			{
				if( bStop==true )
				{
					break;
				}
				sysDiskImageService.initSysDiskImageFromHttpGateway();
			}
			catch( AppException e )
			{
				logger.error("DiskImageInitializer.run() > ["+Thread.currentThread().getId()+"] init disk image fail, exception:["+e.getMessage()+"]", e);
			}
			finally
			{
				ThreadUtil.sleep(intervalTime*1000);
			}
		}
		logger.info("DiskImageInitializer thread is dead");
	}
	
}