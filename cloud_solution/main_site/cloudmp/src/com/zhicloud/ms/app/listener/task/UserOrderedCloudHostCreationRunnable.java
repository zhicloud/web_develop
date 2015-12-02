package com.zhicloud.ms.app.listener.task;

import org.apache.log4j.Logger; 
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zhicloud.ms.common.util.ThreadUtil;
import com.zhicloud.ms.constant.AppConstant;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.ICloudHostService;
import com.zhicloud.ms.util.RegionHelper;
import com.zhicloud.ms.util.RegionHelper.RegionData;


public class UserOrderedCloudHostCreationRunnable implements Runnable
{
	
	public static final Logger logger = Logger.getLogger(UserOrderedCloudHostCreationRunnable.class);
	
	private boolean bStop = false;
	
	private long intervalTime = 60;	// 单位秒
	private long waitTimeAfterSendingCreation = 60;	// 单位秒
	private ICloudHostService cloudHostService;
	
	public UserOrderedCloudHostCreationRunnable(int intervalTime, int waitTimeAfterSendingCreation)
	{
		this.intervalTime = intervalTime;
		this.waitTimeAfterSendingCreation = waitTimeAfterSendingCreation;
        BeanFactory factory = new ClassPathXmlApplicationContext("classpath:/applicationContext*.xml");  
        
		cloudHostService = (ICloudHostService)factory.getBean("cloudHostService"); 
	}
	
	public void stop()
	{
		bStop = true;
	}

	public void run()
	{
		RegionData[] regionDatas = RegionHelper.singleton.getAllResions();
		int regionIndex = 0;
		while(true)
		{
			try
			{
				if( bStop )
				{
					break;
				}
				
				if( regionDatas.length==0 )
				{
					ThreadUtil.sleep(1000);
					continue;
				}
				 
				
 				MethodResult result = cloudHostService.createOneCloudHost();
				long sleepMills = 0;
				if( result.isSuccess() )
				{
					if( "success".equals(result.message) )
					{
						// 成功的发送了一个创建云主机的消息，
						sleepMills = waitTimeAfterSendingCreation * 1000;
					}
					if( "no_more_uncreated_host_exsit".equals(result.message) )
					{
						// 已经没有未处理的订单，过60s再试
						sleepMills = intervalTime * 1000;	// sleep for 60 seconds
					} 
				}
				else
				{
					if( "failed_to_connect_http_gateway".equals(result.message) )
					{
						sleepMills = 10 * 1000;	// 10s
					}
				}
				ThreadUtil.sleep(sleepMills);	// sleep for 10 seconds
			}
			catch (Throwable e)
			{
				logger.error("UserOrderedCloudHostCreationRunnable.run() > ["+Thread.currentThread().getId()+"] ", e);
				ThreadUtil.sleep(intervalTime*1000);	// sleep for 60 seconds
			}
		}
	}
	
}