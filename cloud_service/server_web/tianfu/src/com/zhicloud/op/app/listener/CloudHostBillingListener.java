package com.zhicloud.op.app.listener;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

import com.zhicloud.op.common.util.DateUtil;
import com.zhicloud.op.common.util.ThreadUtil;
import com.zhicloud.op.core.CoreSpringContextManager;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.service.CloudHostBillDetailService;
import com.zhicloud.op.service.TerminalUserService;

public class CloudHostBillingListener implements Servlet
{
	
	public static final Logger logger = Logger.getLogger(CloudHostBillingListener.class);
	
	
	private CloudHostBillingListenerRunnable cloudHostBillingListenerRunnable = null;


	@Override
	public void init(ServletConfig config) throws ServletException
	{
		logger.debug("CloudHostBillingListener.init()");
		
		String startBillingTime = config.getInitParameter("start_billing_time");
		
		cloudHostBillingListenerRunnable = new CloudHostBillingListenerRunnable(startBillingTime);
		new Thread(cloudHostBillingListenerRunnable).start();
	}
	
	
	@Override
	public void destroy()
	{
		logger.debug("CloudHostBillingListener.destroy()");
		cloudHostBillingListenerRunnable.stop();
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
	
	/**************************************
	 * 
	 */
	public static class CloudHostBillingListenerRunnable implements Runnable
	{
		
		private String startBillingTime;
		
		private boolean stop;
		
		public CloudHostBillingListenerRunnable(String startBillingTime)
		{
			this.startBillingTime = startBillingTime;
		}
		
		@Override
		public void run()
		{
			while( true )
			{
				try
				{
					if( this.stop )
					{
						break;
					}
					
					// 获取下一次结算的时间
					Date dateStartBillingTime = DateUtil.getDatesGreaterThan(new Date(), startBillingTime+".000", 1)[0];
					while( new Date().getTime() < dateStartBillingTime.getTime() )
					{
						ThreadUtil.sleep(1000);
					}
					
					logger.info("CloudHostBillingListener.CloudHostBillingListenerRunnable.run() > ["+Thread.currentThread().getId()+"] 开始进行计费");
					
					// 下一次结算的时间已到
					CloudHostBillDetailService cloudHostBillDetailService = CoreSpringContextManager.getCloudHostBillDetailService();
					while( true )
					{
						// 获取一条未结算的记录，进行结算
						MethodResult methodResult = cloudHostBillDetailService.startOneCloudHostBilling(dateStartBillingTime);
						if( MethodResult.SUCCESS.equals(methodResult.status) )
						{
							if( "no_undone_bill_detail_record".equals(methodResult.message) )
							{
								break;
							}
						}
					}
					sendInfo();
				}
				catch (Exception e)
				{ 
					logger.error("CloudHostBillingListenerRunnable.run() > ["+Thread.currentThread().getId()+"] ", e);
				}
			}
		}
		
		public void stop()
		{
			this.stop = true;
		}
		public String sendInfo(){
			  final TerminalUserService terminalUserService = CoreSpringContextManager.getTerminalUserService();
			  
			  Calendar calendar = Calendar.getInstance();
			  /*** 定制每日10:00执行方法 ***/
			  calendar.set(Calendar.HOUR_OF_DAY, 10);
			  calendar.set(Calendar.MINUTE, 0);
			  calendar.set(Calendar.SECOND, 0);
			  
			  Date date=calendar.getTime(); //第一次执行定时任务的时间
			   
			  //如果第一次执行定时任务的时间 小于 当前的时间
			  //此时要在 第一次执行定时任务的时间 加一天，以便此任务在下个时间点执行。如果不加一天，任务会立即执行。
//			  if (date.before(new Date())) {
//			      date = date.addDay(date, 1);
//			  }
			  Timer timer = new Timer();
			  TimerTask tt =  new TimerTask() {
				@Override
				public void run() {
					terminalUserService.sendHint();
				}
			  };
			  //安排指定的任务在指定的时间开始进行重复的固定延迟执行。
			  timer.schedule(tt,date);
			return null;
		}
	}
	
	
	
}










