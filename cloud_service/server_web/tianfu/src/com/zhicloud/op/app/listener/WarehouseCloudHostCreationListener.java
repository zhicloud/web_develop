package com.zhicloud.op.app.listener;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

import com.zhicloud.op.app.listener.task.WarehouseCloudHostCreationRunnable;
import com.zhicloud.op.exception.AppException;

public class WarehouseCloudHostCreationListener implements Servlet
{
	
	
	public static final Logger logger = Logger.getLogger(WarehouseCloudHostCreationListener.class);
	
	
	public static WarehouseCloudHostCreationListener instance = null;
	
	
	//---------------------
	
	
	private WarehouseCloudHostCreationRunnable warehouseCloudHostCreationRunnable = null;
	
	
	private int intervalTime       = 60;			// 单位秒
	private String actionStartTime = "02:00:00";	// 单位秒
	private String actionEndTime   = "07:00:00";	// 单位秒
	
	
	public int getIntervalTime()
	{
		return intervalTime;
	}
	

	@Override
	public void init(ServletConfig config) throws ServletException
	{
		logger.debug("WarehouseCloudHostCreationListener.init()");
		
		if( instance!=null )
		{
			throw new AppException("initialized already.");
		}
		
		instance = this;
		
		this.intervalTime    = Integer.parseInt(config.getInitParameter("interval_time"));
		this.actionStartTime = config.getInitParameter("action_start_time");
		this.actionEndTime   = config.getInitParameter("action_end_time");
		
		warehouseCloudHostCreationRunnable = new WarehouseCloudHostCreationRunnable(this.intervalTime, this.actionStartTime, this.actionEndTime);
		new Thread(warehouseCloudHostCreationRunnable).start();
	}
	
	
	@Override
	public void destroy()
	{
		logger.debug("WarehouseCloudHostCreationListener.destroy()");
		warehouseCloudHostCreationRunnable.stop();
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
	
	
	/**
	 * 
	 */
	public void addWarehouseIdNeedToBeImmediatelyCreated(String warehouseId)
	{
		warehouseCloudHostCreationRunnable.addWarehouseIdNeedToBeImmediatelyCreated(warehouseId);
	}
	
	
}
