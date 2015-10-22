package com.zhicloud.op.app.listener.task;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.zhicloud.op.common.util.DateUtil;
import com.zhicloud.op.common.util.ThreadUtil;
import com.zhicloud.op.core.CoreSpringContextManager;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.service.CloudHostWarehouseService;
import com.zhicloud.op.service.constant.AppConstant;


public class WarehouseCloudHostCreationRunnable implements Runnable
{
	
	
	public static final Logger logger = Logger.getLogger(WarehouseCloudHostCreationRunnable.class);
	
	
	private boolean bStop = false;
	
	private int intervalTime       = 60;			// 单位秒
	private String actionStartTime = "02:00:00";	// 单位秒
	private String actionEndTime   = "07:00:00";	// 单位秒
	
	private List<String> warehouseIdsNeedToBeImmediatelyCreated = Collections.synchronizedList(new ArrayList<String>());
	
	
	public WarehouseCloudHostCreationRunnable(int intervalTime, String actionStartTime, String actionEndTime)
	{
		this.intervalTime    = intervalTime;
		this.actionStartTime = actionStartTime;
		this.actionEndTime   = actionEndTime;
	}
	
	
	public void stop()
	{
		bStop = true;
	}
	
	
	public void addWarehouseIdNeedToBeImmediatelyCreated(String warehouseId)
	{
		if( warehouseId==null )
		{
			return ;
		}
		synchronized (warehouseIdsNeedToBeImmediatelyCreated)
		{
			warehouseIdsNeedToBeImmediatelyCreated.add(warehouseId);
		}
	}
	
	public void removeWarehouseIdNeedToBeImmediatelyCreated(String warehouseId)
	{
		if( warehouseId==null )
		{
			return ;
		}
		synchronized (warehouseIdsNeedToBeImmediatelyCreated)
		{
			for(int i=0;i<warehouseIdsNeedToBeImmediatelyCreated.size();i++){
				if(warehouseIdsNeedToBeImmediatelyCreated.get(i).equals(warehouseId)){
					warehouseIdsNeedToBeImmediatelyCreated.remove(i);
					break;
				}
			}
//			warehouseIdsNeedToBeImmediatelyCreated.remove(warehouseId); 
		}
	}
	
	private String getFirstWarehouseIdNeedToBeImmediatelyCreated()
	{
		synchronized (warehouseIdsNeedToBeImmediatelyCreated)
		{
			if( warehouseIdsNeedToBeImmediatelyCreated.size()>0 )
			{
				return warehouseIdsNeedToBeImmediatelyCreated.get(0);
			}
			else
			{
				return null;
			}
		}
	}
	
	public void run()
	{
		while(true)
		{
			try
			{
				if( bStop )
				{
					break;
				}
				
				Date now       = new Date();
				Date today     = DateUtil.cutBelow(new Date(), Calendar.DATE);
				Date startTime = DateUtil.getDatesGreaterThan(today,     actionStartTime+".000", 1)[0];
				Date endTime   = DateUtil.getDatesGreaterThan(startTime, actionEndTime+".000",   1)[0];
				
				
				CloudHostWarehouseService cloudHostWarehouseService = CoreSpringContextManager.getCloudHostWarehouseService();
				
				// 获取立即优先创建的库存信息
				String warehouseId = getFirstWarehouseIdNeedToBeImmediatelyCreated();
				if( warehouseId!=null )
				{
					MethodResult result = cloudHostWarehouseService.createOneWarehouseCloudHostByWarehouseId(warehouseId);
					sleep(result);
					if( result.isSuccess() )
					{
						if( AppConstant.SUCCESS.equals(result.message) )
						{
							// 重新while loop，
							continue;
						}
						else if( "no_more_uncreated_warehouse_cloud_host".equals(result.message) )
						{
							// 此warehouseId已经没有未创建的库存明细了
							
							removeWarehouseIdNeedToBeImmediatelyCreated(warehouseId);
 						}
					}
				}else{
					
					// 根据时间选取已经失败的云主机
					MethodResult result = cloudHostWarehouseService.createOneWarehouseFailedCloudHost();
					 sleep(result);
				}
				
				// 如果代码进入这一步，表示已经没有需要优先创建的库存信息
				// 如果当前时间不在actionStartTime和actionEndTime指定的时间内的话，不执行以下的逻辑
				if( now.getTime() < startTime.getTime() || now.getTime() > endTime.getTime() )
				{
					ThreadUtil.sleep(1000);
					continue;
				}

				MethodResult result = cloudHostWarehouseService.createOneWarehouseCloudHost();
				sleep(result);
			}
			catch (Exception e)
			{
				logger.error("["+Thread.currentThread().getId()+"] ", e);
				ThreadUtil.sleep(intervalTime*1000);	// sleep for 60 seconds
			}
		}
	}
	
	/**
	 * 创建完一个云主机之后sleep一下，根据结果选择sleep的时间长度
	 */
	private void sleep(MethodResult result)
	{
		long sleepMills = 10 * 1000;	// sleep for 10 seconds
		if( result.isSuccess() )
		{
			if( "success".equals(result.message) )
			{
				// 发送了一个创建云主机的消息，过60s再试
				sleepMills = intervalTime * 1000;	// sleep for 60 seconds
			}
			if( "no_more_uncreated_warehouse_cloud_host".equals(result.message) )
			{
				// 已经没有未创建的仓库云主机，过10s再试
				sleepMills = 10 * 1000;	// sleep for 10 seconds
			}
		}
		ThreadUtil.sleep(sleepMills);
	}
	
}