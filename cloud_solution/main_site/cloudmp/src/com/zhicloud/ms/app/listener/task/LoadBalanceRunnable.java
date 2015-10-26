package com.zhicloud.ms.app.listener.task;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.zhicloud.ms.httpGateway.HttpGatewayAsyncChannel;
import com.zhicloud.ms.httpGateway.HttpGatewayManager;
import com.zhicloud.ms.httpGateway.HttpGatewayResponseHelper;

public class LoadBalanceRunnable implements Runnable {

	private final static Logger logger = Logger.getLogger(LoadBalanceRunnable.class);

	@Override
	public void run() {
		logger.info("");
		while (true) {
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
		}
	}
}
