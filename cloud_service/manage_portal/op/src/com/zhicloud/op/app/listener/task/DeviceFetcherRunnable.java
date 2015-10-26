package com.zhicloud.op.app.listener.task;

import java.math.BigInteger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.zhicloud.op.app.pool.device.DeviceExt;
import com.zhicloud.op.app.pool.device.DevicePool;
import com.zhicloud.op.app.pool.device.DevicePoolManager;
import com.zhicloud.op.common.util.json.JSONLibUtil;
import com.zhicloud.op.httpGateway.HttpGatewayChannelExt;
import com.zhicloud.op.httpGateway.HttpGatewayManager;
import com.zhicloud.op.httpGateway.HttpGatewayResponseHelper;

public class DeviceFetcherRunnable implements Runnable {

	private final static Logger logger = Logger.getLogger(DeviceFetcherRunnable.class);

	private Integer regionId;
	private int intervalTime = 10;// 单位:s
	private boolean running = true;

	public DeviceFetcherRunnable(Integer regionId, int intervalTime) {
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

	private synchronized void waitSelf() {
		try {
			this.wait(this.intervalTime * 1000);
		} catch (InterruptedException e) {
			logger.warn("the device fetcher thread is interrupted when waiting.");
		}
	}

	@Override
	public void run() {
		logger.info(String.format("start to fetch device information. region[%s], interval[%ss]", this.regionId, this.intervalTime));
		while (this.isRunning()) {
			HttpGatewayChannelExt channel = null;
			try {
				channel = HttpGatewayManager.getChannel(regionId);
				// pool id
				JSONObject defaultStoragePool = channel.getDefaultStoragePool();
				if (defaultStoragePool != null) {
					String pool = defaultStoragePool.getString("uuid");
					JSONObject result = channel.deviceQuery(pool);
					if (HttpGatewayResponseHelper.isSuccess(result) == true) {
						JSONArray deviceList = result.getJSONArray("devices");
						for (int i = 0; i < deviceList.size(); i ++) {
							JSONObject deviceObject = deviceList.getJSONObject(i);
							String uuid = deviceObject.getString("uuid");
							String name = deviceObject.getString("name");
							int status = deviceObject.getInt("status");
							BigInteger[] diskVolume = JSONLibUtil.getBigIntegerArray(deviceObject, "disk_volume");
							double level = JSONLibUtil.getDouble(deviceObject, "level");
							String identity = deviceObject.getString("identity");
							int security = JSONLibUtil.getInteger(deviceObject, "security");
							int crypt = JSONLibUtil.getInteger(deviceObject, "crypt");
							BigInteger page = JSONLibUtil.getBigInteger(deviceObject, "page");

							DeviceExt device = new DeviceExt();
							device.setRegion(regionId);
							device.setName(name);
							device.setUuid(uuid);
							device.setStatus(status);
							device.setDiskTotal(diskVolume[1]);
							device.setDiskUnused(diskVolume[0]);
							device.setLevel(level);
							device.setIdentity(identity);
							device.setSecurity(security > 0 ? true : false);
							device.setCrypt(crypt > 0 ? true : false);
							device.setPage(page);
							device.updateTime();

							DevicePool devicePool = DevicePoolManager.singleton().getPool();
							devicePool.put(device);
						}
					}
				}
			} catch (Exception e) {
				logger.warn(String.format("fail to fetch device information. region[%s], exception[%s]", this.regionId, e));
			}
			this.waitSelf();
		}
		logger.info(String.format("stop to fetch device information.region[%s]", this.regionId));
	}

}
