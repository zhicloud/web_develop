package com.zhicloud.op.app.pool.device;

import org.apache.log4j.Logger;


public class DevicePoolManager {

	private final static Logger logger = Logger.getLogger(DevicePoolManager.class);
	private static DevicePoolManager instance = null;
	private final static Object handler = new Object();
	private final DevicePool pool;

	public synchronized static DevicePoolManager singleton() {
		if (DevicePoolManager.instance == null) {
			DevicePoolManager.instance = new DevicePoolManager();
		}

		return DevicePoolManager.instance;
	}

	private DevicePoolManager() {
		pool = new DevicePool();
		// 扫描线程
		Thread timeoutThread = new Thread(new TimeoutRunnable());
		timeoutThread.setDaemon(true);
		timeoutThread.start();
	}

	public DevicePool getPool() {
		return pool;
	}

	private class TimeoutRunnable implements Runnable {

		@Override
		public void run() {
			while (true) {
				synchronized (handler) {
					try {
						handler.wait(1000);
					} catch (InterruptedException exception) {
						logger.error("the timeout thread in device pool manager fail to wait.", exception);
					}
				}// 每秒扫描一次

				long currentTime = System.currentTimeMillis();
				// 缓冲池：device pool
				DeviceExt[] deviceList = DevicePoolManager.this.pool.getAllDuplication();
				for (int i = 0; i < deviceList.length; i++) {
					DeviceExt device = deviceList[i];
					if ((currentTime - device.getLastUpdateTime()) > 15 * 1000) {// 超过15秒没有更新，则认为该数据不再使用.
						DevicePoolManager.this.pool.remove(device);
					}
				}
			}
		}
	}

}
