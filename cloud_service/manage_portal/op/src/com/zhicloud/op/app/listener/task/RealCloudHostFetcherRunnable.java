package com.zhicloud.op.app.listener.task;

import org.apache.log4j.Logger;

import com.zhicloud.op.app.listener.RealCloudHostFetcherListener;
import com.zhicloud.op.app.pool.CloudHostPoolManager;
import com.zhicloud.op.core.CoreSpringContextManager;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.service.CloudHostService;

/**
 * 定时同步所有region的云主机信息。并决定是否触发发现云主机列表更新事件。
 * 
 * @author Administrator
 *
 */
public class RealCloudHostFetcherRunnable implements Runnable {

	public static final Logger logger = Logger.getLogger(RealCloudHostFetcherRunnable.class);
	private boolean bStop = false;
	private int intervalTime = 60; // 单位秒
	private final RealCloudHostFetcherListener handler;

	public RealCloudHostFetcherRunnable(RealCloudHostFetcherListener handler, int intervalTime) {
		this.handler = handler;
		this.intervalTime = 10;
	}

	/**
	 * 停止线程
	 */
	public synchronized void stop() {
		bStop = true;
	}

	public synchronized boolean isbStop() {
		return bStop;
	}

	/**
	 * 初始化云主机池 每隔一段时间去同步每个region的云主机列表，并判断是否发送云主机列表更新事件。
	 */
	public void run() {
		logger.info("real cloud host fetch thread start.");
		while (!this.isbStop()) {
			try {
				// 初始化云主机池
				if (CloudHostPoolManager.getSingleton().isInitialized() == false) {
					CloudHostPoolManager.getSingleton().initCloudHostPool();
				}
				// 同步每个region的云主机列表
				CloudHostService cloudHostService = CoreSpringContextManager.getCloudHostService();
				MethodResult result = cloudHostService.fetchNewestCloudHostFromHttpGateway();
				// 判断是否发送云主机列表更新事件
				boolean update = false;
				Object hostListUpdate = result.get("host_list_update");
				if (hostListUpdate != null && ((Boolean) hostListUpdate) == Boolean.TRUE) {
					update = true;
				}
				logger.info(String.format("fetch cloud host end.host list update[%s].", update));
				if (update) {// 发送云主机列表更新事件
					handler.handleHostListUpdateEvent();
				}
			} catch (Exception e) {
				logger.error("fail to fetch host list from http gateway", e);
			} finally {
				try {
					synchronized (this) {
						this.wait(intervalTime * 1000);// wait for 60 seconds
					}
				} catch (InterruptedException e) {
					logger.warn("the fetch real cloud host thread fail to wait.", e);
				}
			}
		}
		logger.warn("real cloud host fetch thread stop.");
	}

}