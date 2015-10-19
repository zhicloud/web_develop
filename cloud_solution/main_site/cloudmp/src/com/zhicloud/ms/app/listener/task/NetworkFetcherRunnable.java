package com.zhicloud.ms.app.listener.task;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.zhicloud.ms.app.helper.RegionHelper;
import com.zhicloud.ms.app.helper.RegionHelper.RegionData;
import com.zhicloud.ms.httpGateway.HttpGatewayAsyncChannel;
import com.zhicloud.ms.httpGateway.HttpGatewayManager;
import com.zhicloud.ms.httpGateway.HttpGatewayResponseHelper;

public class NetworkFetcherRunnable implements Runnable {

	private final static Logger logger = Logger.getLogger(NetworkFetcherRunnable.class);

	private int intervalTime = 10;// 单位:s
	private boolean running = true;

	public NetworkFetcherRunnable(int intervalTime) {
		this.intervalTime = intervalTime;
	}

	public synchronized void stop() {
		this.running = false;
		this.notifyAll();
	}

	public synchronized boolean isRunning() {
		return running;
	}

	private synchronized void waitSelf() {
		try {
			this.wait(this.intervalTime * 1000);
		} catch (InterruptedException e) {
			logger.warn("the network fetcher thread is interrupted when waiting.");
		}
	}

	@Override
	public void run() {
		logger.info(String.format("start to fetch network information. interval[%ss]", this.intervalTime));
		while (this.isRunning()) {
//			RegionData[] regions = RegionHelper.singleton.getAllResions();

//			for (RegionData region : regions) {
				HttpGatewayAsyncChannel channel = null;
				try {
					channel = HttpGatewayManager.getAsyncChannel(new Integer(1));
					JSONObject result = channel.networkQuery();
					if (HttpGatewayResponseHelper.isSuccess(result) == false) {
						channel.release();
					}
				} catch (Exception e) {
					logger.warn(String.format("fail to fetch network information. region[%s], exception[%s]", 1, e));
					if (channel != null) {
						channel.release();
					}
				}
//			}
			this.waitSelf();
		}
		logger.info("stop to fetch network information.");
	}
}
