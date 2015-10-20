package com.zhicloud.ms.app.pool.host.reset;

import org.apache.log4j.Logger;

public class HostResetProgressPoolManager {

	private final static Logger logger = Logger.getLogger(HostResetProgressPoolManager.class);
	private static HostResetProgressPoolManager instance = null;
	private final HostResetProgressPool pool;
	private final static Object handler = new Object();

	// 单例
	public synchronized static HostResetProgressPoolManager singleton() {
		if (HostResetProgressPoolManager.instance == null) {
			HostResetProgressPoolManager.instance = new HostResetProgressPoolManager();
		}

		return instance;

	}

	private HostResetProgressPoolManager() {
		pool = new HostResetProgressPool();
		// 扫描线程
		Thread timeoutThread = new Thread(new TimeoutRunnable());
		timeoutThread.setDaemon(true);
		timeoutThread.start();
	}

	public HostResetProgressPool getPool() {
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
						logger.error("the timeout thread in host reset progress pool manager fail to wait.", exception);
					}
				}// 10分钟扫描一次
				HostResetProgressData[] list = HostResetProgressPoolManager.this.pool.getAll();
				for (int i = 0; i < list.length; i++) {
					HostResetProgressData hostReset = list[i];
					if ((System.currentTimeMillis() - hostReset.getLastUpdateTime()) > 30 * 60 * 1000) {// 超过30分钟没有更新，则认为该数据不再使用.
						HostResetProgressPoolManager.this.pool.remove(hostReset);
					}
				}
			}
		}

	}

}
