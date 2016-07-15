package com.zhicloud.ms.app.listener;

import java.io.IOException;

 
import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;

import com.zhicloud.ms.app.listener.task.RealCloudHostFetcherRunnable;
import com.zhicloud.ms.app.listener.task.ScanHostMonitorRunnable;
import com.zhicloud.ms.app.pool.hostMonitorInfoPool.HostMonitorInfoManager;
import com.zhicloud.ms.exception.AppException; 

public class RealCloudHostFetcherListener implements Servlet {

	public static final Logger logger = Logger.getLogger(RealCloudHostFetcherListener.class);
	private RealCloudHostFetcherRunnable realCloudHostFetcherRunnable = null;
	private ScanHostMonitorRunnable scanHostMonitorRunnable = null;  

	/**
	 * 1.启动云主机资源监控信息扫描线程
	 * 2.启动获取云主机列表信息线程
	 * 
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		logger.info("init real cloud host fetcher listener.");
		// 参数
		String fetchHostIntervalTime = config.getInitParameter("fetch_host_interval_time");
		String scanMonitorInfoIntervalTime = config.getInitParameter("scan_monitor_info_interval_time");
		// 获取云主机列表信息线程
		realCloudHostFetcherRunnable = new RealCloudHostFetcherRunnable(this, Integer.parseInt(fetchHostIntervalTime));
		Thread hostFetcherThread = new Thread(realCloudHostFetcherRunnable);
		hostFetcherThread.setDaemon(true);
		// 云主机资源监控信息扫描线程
		scanHostMonitorRunnable = new ScanHostMonitorRunnable(Integer.parseInt(scanMonitorInfoIntervalTime));
		Thread scanHostMonitorThread = new Thread(scanHostMonitorRunnable);
		scanHostMonitorThread.setDaemon(true);

		// 分别启动线程
		scanHostMonitorThread.start();
		hostFetcherThread.start();
	}

	/**
	 * 云主机列表更新事件
	 * 1.唤醒等待HostMonitorInfoPoolManager单例的所有对象
	 */
	public void handleHostListUpdateEvent() {
		synchronized (HostMonitorInfoManager.singleton()) {
			HostMonitorInfoManager.singleton().notifyAll();
		}
	}

	/**
	 * 关闭所有线程
	 * 1.云主机资源监控信息扫描线程
	 * 2.获取云主机列表信息线程
	 */
	@Override
	public void destroy() {
		logger.info("ReadCloudHostFetcherListener.destroy()");
		realCloudHostFetcherRunnable.stop();
		scanHostMonitorRunnable.stop();
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
