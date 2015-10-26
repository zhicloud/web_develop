package com.zhicloud.op.app.listener.task;

import org.apache.log4j.Logger;

import com.zhicloud.op.app.pool.IsoImagePoolManager;
import com.zhicloud.op.common.util.ThreadUtil;


public class IsoImageInitializer implements Runnable
{
	
	public static final Logger logger = Logger.getLogger(IsoImageInitializer.class);
	
	private boolean bStop = false;
	
	private int intervalTime = 60;	// 单位为秒
	
	public IsoImageInitializer(int intervalTime)
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
				IsoImagePoolManager.getSingleton().refreshIsoImagePool();
			}
			finally
			{
				ThreadUtil.sleep(intervalTime*1000);
			}
		}
	}
	
}