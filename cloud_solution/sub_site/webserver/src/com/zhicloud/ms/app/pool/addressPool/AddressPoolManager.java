package com.zhicloud.ms.app.pool.addressPool;

import java.util.Hashtable;
import java.util.Map;

public class AddressPoolManager {

//	private final static Logger logger = Logger.getLogger(AddressPoolManager.class);
	private static AddressPoolManager instance = null;
//	private final static Object handler = new Object();
//	private final AddressPool pool;
	private Map<String, AddressPool> addressPools = new Hashtable<String, AddressPool>();
	
	public synchronized static AddressPoolManager singleton() {
		if (AddressPoolManager.instance == null) {
			AddressPoolManager.instance = new AddressPoolManager();
		}

		return AddressPoolManager.instance;
	}
	
	public void put(String region,AddressPool addressPool) {

		if (region != null && region.trim().length() != 0) {
			addressPools.put(region, addressPool);
		}
	}

	public void remove(String region) {
		if (region != null && region.trim().length() != 0) {
			addressPools.remove(region);
		}
	}
	/*private AddressPoolManager() {
		pool = new AddressPool();
		// 扫描线程
		Thread timeoutThread = new Thread(new TimeoutRunnable());
		timeoutThread.setDaemon(true);
		timeoutThread.start();
	}*/

	public AddressPool getPool(String region) {
		if (region != null && region.trim().length() != 0) {
			return addressPools.get(region);
		}else{
			return null;
		}
	}

	/*private class TimeoutRunnable implements Runnable {

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
				DeviceExt[] deviceList = AddressPoolManager.this.pool.getAllDuplication();
				for (int i = 0; i < deviceList.length; i++) {
					DeviceExt device = deviceList[i];
					if ((currentTime - device.getLastUpdateTime()) > 15 * 1000) {// 超过15秒没有更新，则认为该数据不再使用.
						AddressPoolManager.this.pool.remove(device);
					}
				}
			}
		}
	}*/

}
