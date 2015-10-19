package com.zhicloud.ms.app.pool.host.back;

import org.apache.log4j.Logger;

public class HostBackupProgressPoolManager {

	private final static Logger logger = Logger.getLogger(HostBackupProgressPoolManager.class);
	private static HostBackupProgressPoolManager instance = null;
	private final HostBackupProgressPool pool;
	private final static Object handler = new Object();

	// 单例
	public synchronized static HostBackupProgressPoolManager singleton() {
		if (HostBackupProgressPoolManager.instance == null) {
			HostBackupProgressPoolManager.instance = new HostBackupProgressPoolManager();
		}

		return instance;

	}

	private HostBackupProgressPoolManager() {
		pool = new HostBackupProgressPool();
		// 扫描线程
		Thread timeoutThread = new Thread(new TimeoutRunnable());
		timeoutThread.setDaemon(true);
		timeoutThread.start();
	}

	public HostBackupProgressPool getPool() {
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
						logger.error("the timeout thread in host backup progress pool manager fail to wait.", exception);
					}
				}// 10分钟扫描一次
				HostBackupProgressData[] list = HostBackupProgressPoolManager.this.pool.getAll();
				for (int i = 0; i < list.length; i++) {
					HostBackupProgressData hostBackup = list[i];
					if ((System.currentTimeMillis() - hostBackup.getLastUpdateTime()) > 30 * 60 * 1000) {// 超过30分钟没有更新，则认为该数据不再使用.
						HostBackupProgressPoolManager.this.pool.remove(hostBackup);
					}
				}
			}
		}

	}

}
