package com.zhicloud.ms.app.listener;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

import com.zhicloud.ms.app.listener.task.NetworkFetcherRunnable;
import com.zhicloud.ms.exception.AppException;

public class NetworkFetcherListener implements Servlet {
	
	private final static Logger logger = Logger.getLogger(NetworkFetcherListener.class);
	private NetworkFetcherRunnable networkFetcherRunnable = null;

	@Override
	public void init(ServletConfig config) throws ServletException {
		logger.info("init network fetcher lister");
		//参数
		String fetchIntervalTime = config.getInitParameter("fetch_interval_time");
		int intervalTime = Integer.parseInt(fetchIntervalTime);
		networkFetcherRunnable = new NetworkFetcherRunnable(intervalTime);
		Thread thread = new Thread(networkFetcherRunnable);
		thread.setDaemon(true);
		thread.start();
	}
	
	@Override
	public void destroy() {
		logger.info("destory the network fetcher listers");
		this.networkFetcherRunnable.stop();
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
