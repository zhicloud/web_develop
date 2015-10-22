package com.zhicloud.op.app.listener.task;

import org.apache.log4j.Logger;

import com.zhicloud.op.common.util.ThreadUtil;
import com.zhicloud.op.core.CoreSpringContextManager;
import com.zhicloud.op.exception.AppException;


public class DiskImageInitializer implements Runnable
{
	
	public static final Logger logger = Logger.getLogger(DiskImageInitializer.class);
	
	private boolean bStop = false;
	
	private int intervalTime = 60;		// 单位秒

	public DiskImageInitializer(int intervalTime)
	{
		this.intervalTime = intervalTime;
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
				CoreSpringContextManager.getSysDiskImageService().initSysDiskImageFromHttpGateway();
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