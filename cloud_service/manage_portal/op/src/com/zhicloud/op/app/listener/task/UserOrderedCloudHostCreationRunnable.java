package com.zhicloud.op.app.listener.task;

import org.apache.log4j.Logger;

import com.zhicloud.op.app.helper.RegionHelper;
import com.zhicloud.op.app.helper.RegionHelper.RegionData;
import com.zhicloud.op.common.util.ThreadUtil;
import com.zhicloud.op.core.CoreSpringContextManager;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.service.OrderInfoService;


public class UserOrderedCloudHostCreationRunnable implements Runnable
{
	
	public static final Logger logger = Logger.getLogger(UserOrderedCloudHostCreationRunnable.class);
	
	private boolean bStop = false;
	
	private long intervalTime = 60;	// 单位秒
	private long waitTimeAfterSendingCreation = 60;	// 单位秒
	
	public UserOrderedCloudHostCreationRunnable(int intervalTime, int waitTimeAfterSendingCreation)
	{
		this.intervalTime = intervalTime;
		this.waitTimeAfterSendingCreation = waitTimeAfterSendingCreation;
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
				
				// 每次都从不同的region取订单
				RegionData regionData = regionDatas[regionIndex];
				regionIndex = (regionIndex+1) % regionDatas.length;
				
				OrderInfoService orderInfoService = CoreSpringContextManager.getOrderInfoService();
				MethodResult result = orderInfoService.createOneUserOrderedCloudHost(regionData.getId());
				long sleepMills = 0;
				if( result.isSuccess() )
				{
					if( "success".equals(result.message) )
					{
						// 成功的发送了一个创建云主机的消息，
						sleepMills = waitTimeAfterSendingCreation * 1000;
					}
					if( "no_order_unprocessed".equals(result.message) )
					{
						// 已经没有未处理的订单，过60s再试
						sleepMills = intervalTime * 1000;	// sleep for 60 seconds
					}
					if( "no_cloud_host_config_unprocessed".equals(result.message) )
					{
						// 该订单已经没有未处理云主机，马上找其它的订单
						sleepMills = 0;			// sleep for 10 seconds
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