package com.zhicloud.ms.app.pool.isoImagePool;

import org.apache.log4j.Logger;

public class IsoImageProgressPoolManager {

	private final static Logger logger = Logger.getLogger(IsoImageProgressPoolManager.class);
	private static IsoImageProgressPoolManager instance = null;
	private final IsoImageProgressPool pool;
	private final static Object handler = new Object();

	public synchronized static IsoImageProgressPoolManager singleton() {
		if (IsoImageProgressPoolManager.instance == null) {
			IsoImageProgressPoolManager.instance = new IsoImageProgressPoolManager();
		}

		return instance;
	}

	private IsoImageProgressPoolManager() {
		pool = new IsoImageProgressPool();
		// 扫描线程
		Thread timeoutThread = new Thread(new TimeoutRunnable());
		timeoutThread.setDaemon(true);
		timeoutThread.start();
	}

	public IsoImageProgressPool getPool() {
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
						logger.error("the timeout thread in iso image progress pool manager fail to wait.", exception);
					}
				}// 10分钟扫描一次
				IsoImageProgressData[] list = IsoImageProgressPoolManager.this.pool.getAll();
				for (int i = 0; i < list.length; i++) {
					IsoImageProgressData isoImage = list[i];
					if ((System.currentTimeMillis() - isoImage.getLastUpdateTime()) > 30 * 60 * 1000) {// 超过30分钟没有更新，则认为该数据不再使用.
						IsoImageProgressPoolManager.this.pool.remove(isoImage);
					}
				}
			}
		}

	}
}
