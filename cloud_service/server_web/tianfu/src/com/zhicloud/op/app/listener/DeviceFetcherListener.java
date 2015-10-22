package com.zhicloud.op.app.listener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

import com.zhicloud.op.app.helper.RegionHelper;
import com.zhicloud.op.app.helper.RegionHelper.RegionData;
import com.zhicloud.op.app.listener.task.DeviceFetcherRunnable;
import com.zhicloud.op.exception.AppException;

public class DeviceFetcherListener implements Servlet {

	private final static Logger logger = Logger.getLogger(DeviceFetcherListener.class);
	private List<DeviceFetcherRunnable> deviceFetcherRunnableList = new ArrayList<DeviceFetcherRunnable>();

	@Override
	public void init(ServletConfig config) throws ServletException {
		logger.info("init device fetcher lister");
		// 参数
		String fetchIntervalTime = config.getInitParameter("fetch_interval_time");
		int intervalTime = Integer.parseInt(fetchIntervalTime);

		RegionData[] regions = RegionHelper.singleton.getAllResions();
		for (RegionData region : regions) {
			DeviceFetcherRunnable deviceFetcherRunnable = new DeviceFetcherRunnable(region.getId(), intervalTime);
			Thread thread = new Thread(deviceFetcherRunnable);
			thread.setDaemon(true);
			thread.start();

			deviceFetcherRunnableList.add(deviceFetcherRunnable);
		}
	}

	@Override
	public void destroy() {
		logger.info("destory the device fetcher listener");
		for (DeviceFetcherRunnable deviceFetcherRunnable : deviceFetcherRunnableList) {
			deviceFetcherRunnable.stop();
		}
	}

	@Override
	public ServletConfig getServletConfig() {
		throw new AppException("unsupported method");
	}

	@Override
	public String getServletInfo() {
		throw new AppException("unsupported method");
	}

	@Override
	public void service(ServletRequest arg0, ServletResponse arg1) throws ServletException, IOException {
		throw new AppException("unsupported method");
	}
}
