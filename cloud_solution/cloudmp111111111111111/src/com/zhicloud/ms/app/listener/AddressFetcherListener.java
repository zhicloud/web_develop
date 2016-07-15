package com.zhicloud.ms.app.listener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

import com.zhicloud.ms.app.helper.RegionHelper;
import com.zhicloud.ms.app.helper.RegionHelper.RegionData;
import com.zhicloud.ms.app.listener.task.AddressFetcherRunnable;
import com.zhicloud.ms.exception.AppException;

public class AddressFetcherListener implements Servlet {

	private final static Logger logger = Logger.getLogger(AddressFetcherListener.class);
	private List<AddressFetcherRunnable> addressFetcherRunnableList = new ArrayList<AddressFetcherRunnable>();

	@Override
	public void init(ServletConfig config) throws ServletException {
		logger.info("init address fetcher listener");
		// 参数
		String fetchIntervalTime = config.getInitParameter("fetch_interval_time");
		int intervalTime = Integer.parseInt(fetchIntervalTime);

//		RegionData[] regions = RegionHelper.singleton.getAllResions();
//		for (RegionData region : regions) {
			AddressFetcherRunnable addressFetcherRunnable = new AddressFetcherRunnable(new Integer(1), intervalTime);
			Thread thread = new Thread(addressFetcherRunnable);
			thread.setDaemon(true);
			thread.start();
			addressFetcherRunnableList.add(addressFetcherRunnable);
//		}
	}

	@Override
	public void destroy() {
		logger.info("destory the device fetcher listener");
		for (AddressFetcherRunnable addressFetcherRunnable : addressFetcherRunnableList) {
			addressFetcherRunnable.stop();
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
