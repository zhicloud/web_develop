package com.zhicloud.ms.app.pool.host.migration;

import org.apache.log4j.Logger;

public class MigrationProgressPoolManager {

	private final static Logger logger = Logger.getLogger(MigrationProgressPoolManager.class);
	private static MigrationProgressPoolManager instance = null;
	private final MigrationProgressPool pool;
	private final static Object handler = new Object();

	// 单例
	public synchronized static MigrationProgressPoolManager singleton() {
		if (MigrationProgressPoolManager.instance == null) {
			MigrationProgressPoolManager.instance = new MigrationProgressPoolManager();
		}

		return instance;

	}

	private MigrationProgressPoolManager() {
		pool = new MigrationProgressPool();
		// 扫描线程
		Thread timeoutThread = new Thread(new TimeoutRunnable());
		timeoutThread.setDaemon(true);
		timeoutThread.start();
	}

	public MigrationProgressPool getPool() {
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
						logger.error("the timeout thread in host migrate progress pool manager fail to wait.", exception);
					}
				}// 10分钟扫描一次
				MigrationProgressData[] list = MigrationProgressPoolManager.this.pool.getAll();
				for (int i = 0; i < list.length; i++) {
					MigrationProgressData Migration = list[i];
					if ((System.currentTimeMillis() - Migration.getLastUpdateTime()) > 30 * 60 * 1000) {// 超过30分钟没有更新，则认为该数据不再使用.
						MigrationProgressPoolManager.this.pool.remove(Migration);
					}
				}
			}
		}

	}

}
