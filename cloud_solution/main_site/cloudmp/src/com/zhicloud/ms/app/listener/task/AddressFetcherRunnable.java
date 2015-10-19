package com.zhicloud.ms.app.listener.task;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.zhicloud.ms.app.pool.addressPool.AddressExt;
import com.zhicloud.ms.app.pool.addressPool.AddressPool;
import com.zhicloud.ms.app.pool.addressPool.AddressPoolManager;
import com.zhicloud.ms.httpGateway.HttpGatewayChannelExt;
import com.zhicloud.ms.httpGateway.HttpGatewayManager;
import com.zhicloud.ms.httpGateway.HttpGatewayResponseHelper;

public class AddressFetcherRunnable implements Runnable {

	private final static Logger logger = Logger.getLogger(AddressFetcherRunnable.class);

	private Integer regionId;
	private int intervalTime = 10;// 单位:s
	private boolean running = true;

	public AddressFetcherRunnable(Integer regionId, int intervalTime) {
		this.regionId = regionId;
		this.intervalTime = intervalTime;
	}

	public synchronized void stop() {
		this.running = false;
		this.notifyAll();
	}

	public synchronized boolean isRunning() {
		return running;
	}

	/*private synchronized void waitSelf() {
		try {
			this.wait(this.intervalTime * 1000);
		} catch (InterruptedException e) {
			logger.warn("the address fetcher thread is interrupted when waiting.");
		}
	}*/

	@Override
	public void run() {
		logger.info(String.format("start to fetch address information. region[%s], interval[%ss]", this.regionId, this.intervalTime));
		HttpGatewayChannelExt channel = null;
		try {
			channel = HttpGatewayManager.getChannel(regionId);
			// pool id
			JSONObject result = channel.addressPoolQuery();
			if (HttpGatewayResponseHelper.isSuccess(result) == true) {
				AddressPool addressPool = new AddressPool();
				AddressPoolManager.singleton().put(regionId.toString(), addressPool);
				JSONArray addressList = result.getJSONArray("addressPools");
				for (int i = 0; i < addressList.size(); i ++) {
					JSONObject addressObject = addressList.getJSONObject(i);
					String uuid = addressObject.getString("uuid");
					String name = addressObject.getString("name");
					int status = addressObject.getInt("status");
					JSONArray countList = addressObject.getJSONArray("count");
					int[] count = new int[countList.size()];
					for(int j=0;j<countList.size();j++){
						count[j] = countList.getInt(j);
					}

					AddressExt address = new AddressExt();
					address.setName(name);
					address.setUuid(uuid);
					address.setStatus(status);
					address.setCount(count);
					address.setRegion(regionId);
					address.updateTime();
					addressPool.add(address);
				}
			}
		} catch (Exception e) {
			logger.warn(String.format("fail to fetch address information. region[%s], exception[%s]", this.regionId, e));
		}
		logger.info(String.format("stop to fetch address information.region[%s]", this.regionId));
	}

}
