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

import com.zhicloud.ms.app.listener.task.ServiceFetcherRunnable;
import com.zhicloud.ms.common.util.constant.NodeTypeDefine;
import com.zhicloud.ms.exception.AppException;

public class ServiceFetcherListener implements Servlet {

	private final static Logger logger = Logger.getLogger(ServiceFetcherListener.class);
	private List<ServiceFetcherRunnable> fetcherList = new ArrayList<ServiceFetcherRunnable>();

	@Override
	public void init(ServletConfig config) throws ServletException {
		logger.info("init all service fetcher listers");
		// 参数
		String fetchServiceIntervalTime = config.getInitParameter("fetch_service_interval_time");
		int intervalTime = Integer.parseInt(fetchServiceIntervalTime);

		int[] nodeList = new int[] { NodeTypeDefine.DATA_SERVER, NodeTypeDefine.CONTROL_SERVER, NodeTypeDefine.NODE_CLIENT, NodeTypeDefine.STORAGE_SERVER, NodeTypeDefine.INTELLIGENT_REOUTER };
		for (int node : nodeList) {
			ServiceFetcherRunnable fetcher = new ServiceFetcherRunnable(node, "default", intervalTime);
			Thread thread = new Thread(fetcher);
			thread.setDaemon(true);
			fetcherList.add(fetcher);
			// 启动线程
			thread.start();
			this.waitSelf(1);
		}
	}

	/**
	 * 
	 * @param waitTime
	 *            单位：s
	 */
	private synchronized void waitSelf(double waitTime) {
		try {
			this.wait((int) (waitTime * 1000));
		} catch (InterruptedException e) {
			logger.warn(String.format("interrupted when waiting for %s seconds. exception[%s]", waitTime, e.getMessage()));
		}
	}

	@Override
	public void destroy() {
		logger.info("destory all the service fetcher listers");

		for (ServiceFetcherRunnable fetcher : fetcherList) {
			fetcher.stop();
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
