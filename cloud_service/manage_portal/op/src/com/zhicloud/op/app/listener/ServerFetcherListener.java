package com.zhicloud.op.app.listener;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

import com.zhicloud.op.app.listener.task.ServerFetcherRunnable;
import com.zhicloud.op.exception.AppException;

/**
 * 
 * 获取服务器信息
 *
 */
public class ServerFetcherListener implements Servlet {

	private final static Logger logger = Logger.getLogger(ServerFetcherListener.class);
	private ServerFetcherRunnable serverFetcherRunnable = null;

	@Override
	public void init(ServletConfig config) throws ServletException {
		logger.info("init server fetcher listener");
		// 参数
		String fetchServerIntervalTime = config.getInitParameter("fetch_server_interval_time");
		// 服务器信息获取线程
		serverFetcherRunnable = new ServerFetcherRunnable(Integer.parseInt(fetchServerIntervalTime));
		Thread serverFetcherThread = new Thread(serverFetcherRunnable);
		serverFetcherThread.setDaemon(true);
		// 启动线程
		serverFetcherThread.start();
	}

	@Override
	public void destroy() {
		logger.info("destory the server fetcher listener");
		serverFetcherRunnable.stop();
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
