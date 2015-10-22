package com.zhicloud.op.app.pool.rule;

import org.apache.log4j.Logger;

public class RulePoolManager {

	private final static Logger logger = Logger.getLogger(RulePoolManager.class);
	private static RulePoolManager instance = null;
	private final RuleInfoPool ruleInfoPool; 
	private final static Object handler = new Object();

	// 单例
	public synchronized static RulePoolManager singleton() {
		if (RulePoolManager.instance == null) {
			RulePoolManager.instance = new RulePoolManager();
		}

		return instance;
	}

	private RulePoolManager() {
		ruleInfoPool = new RuleInfoPool(); 
		// 扫描线程
		Thread timeoutThread = new Thread(new TimeoutRunnable());
		timeoutThread.setDaemon(true);
		timeoutThread.start();
	} 
	public RuleInfoPool getInfoPool() {
		return ruleInfoPool;
	}
 
	 

	 

	 

	 

	private class TimeoutRunnable implements Runnable {

		@Override
		public void run() {
			while (true) { 
				synchronized (handler) {
					try {
						handler.wait(1000);
					} catch (InterruptedException exception) {
						logger.error("the timeout thread in network pool manager fail to wait.", exception);
					}
				}// 每秒扫描一次

				long currentTime = System.currentTimeMillis();
				// 创建信息：create info pool
				RuleInfo[] createInfoList = RulePoolManager.this.ruleInfoPool.getAll();
				for (int i = 0; i < createInfoList.length; i++) {
					RuleInfo rule = createInfoList[i];
					if ((currentTime - rule.getLastUpdateTime()) > 30 * 60 * 1000) {// 超过30分钟没有更新，则认为该数据不再使用.
						RulePoolManager.this.ruleInfoPool.remove(rule);
					}
				}
				 
			}
		}

	}

}
