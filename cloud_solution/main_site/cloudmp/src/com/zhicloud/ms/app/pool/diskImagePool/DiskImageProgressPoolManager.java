package com.zhicloud.ms.app.pool.diskImagePool;

import org.apache.log4j.Logger;

public class DiskImageProgressPoolManager {

	private final static Logger logger = Logger.getLogger(DiskImageProgressPoolManager.class);
	private static DiskImageProgressPoolManager instance = null;
	private final DiskImageProgressPool pool;
	private final static Object handler = new Object();

	// 单例
	public synchronized static DiskImageProgressPoolManager singleton() {
		if (DiskImageProgressPoolManager.instance == null) {
			DiskImageProgressPoolManager.instance = new DiskImageProgressPoolManager();
		}

		return instance;

	}

	private DiskImageProgressPoolManager() {
		pool = new DiskImageProgressPool();
		// 扫描线程
		Thread timeoutThread = new Thread(new TimeoutRunnable());
		timeoutThread.setDaemon(true);
		timeoutThread.start();
	}

	public DiskImageProgressPool getPool() {
		return pool;
	}

	private class TimeoutRunnable implements Runnable {

		@Override
		public void run() {
			while (true) {
				synchronized (handler) {
					try {
						handler.wait(10 * 60 * 1000);
					} catch (InterruptedException exception) {
						logger.error("the timeout thread in disk image progress pool manager fail to wait.", exception);
					}
				}// 10分钟扫描一次
				DiskImageProgressData[] list = DiskImageProgressPoolManager.this.pool.getAll();
				for (int i = 0; i < list.length; i++) {
					DiskImageProgressData diskImage = list[i];
					if ((System.currentTimeMillis() - diskImage.getLastUpdateTime()) > 30 * 60 * 1000) {// 超过30分钟没有更新，则认为该数据不再使用.
						DiskImageProgressPoolManager.this.pool.remove(diskImage);
					}
				}
			}
		}

	}

}
