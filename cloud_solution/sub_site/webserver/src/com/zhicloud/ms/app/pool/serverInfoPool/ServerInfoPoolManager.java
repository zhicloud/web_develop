package com.zhicloud.ms.app.pool.serverInfoPool;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

public class ServerInfoPoolManager {

	private final static Logger logger = Logger.getLogger(ServerInfoPoolManager.class);
	private static ServerInfoPoolManager instance = null;
	private final ServerInfoPool pool;

	public synchronized static ServerInfoPoolManager singleton() {
		if (ServerInfoPoolManager.instance == null) {
			ServerInfoPoolManager.instance = new ServerInfoPoolManager();
		}

		return ServerInfoPoolManager.instance;
	}

	private ServerInfoPoolManager() {
		pool = new ServerInfoPool();
		// 启动扫描线程
		ScanServerRunnable scanServerRunnable = new ScanServerRunnable();
		Thread thread = new Thread(scanServerRunnable);
		thread.setDaemon(true);
		thread.start();
	}

	public ServerInfoPool getPool() {
		return this.pool;
	}

	private class ScanServerRunnable implements Runnable {

		private final Logger logger = Logger.getLogger(ScanServerRunnable.class);

		private synchronized void waitSelf() {
			try {
				this.wait(1000);
			} catch (InterruptedException e) {
				logger.warn(String.format("the scan server thread is interrupted when waiting. exception[%s]", e.getMessage()));
			}
		}

		@Override
		public void run() {
			while (true) {
				ServerInfoExt[] serverInfoArray = ServerInfoPoolManager.singleton().getPool().getAllServer();
				final List<ServerInfoExt> exceptionServerList = new ArrayList<ServerInfoExt>();
				final List<ServerInfoExt> recoveredServerList = new ArrayList<ServerInfoExt>();

				for (ServerInfoExt serverInfo : serverInfoArray) {
					// 1.维护server info pool:60s超时后，移除该server information
					if (System.currentTimeMillis() - serverInfo.getLastUpdateTime() > 60 * 1000) {
						ServerInfoPoolManager.singleton().getPool().removeServer(serverInfo.getUuid());
						continue;
					}

					// 2.状态监测:检测服务器状态是否异常，并记录异常开始时间；如果服务器恢复正常，记录异常状态持续时间。
					synchronized (serverInfo) {// 同步处理,保证数据一致，防止数据覆盖
						int status = serverInfo.getStatus();
						long exceptionTime = serverInfo.getExceptionTime();
						long lastUpdateTime = serverInfo.getLastUpdateTime();

						if (status == 0) {// 正常状态，重置异常时间
							if (exceptionTime != 0) {// 服务器恢复正常状态，记录异常状态持续时间
								serverInfo.setExceptionDurationTime(lastUpdateTime - exceptionTime);
								recoveredServerList.add(serverInfo.clone());
							}
							serverInfo.setExceptionTime(0);
						} else if (System.currentTimeMillis() - lastUpdateTime < 15 * 1000) {// 状态为异常状态，并且最近更新时间在15s内(保证至少有一次更新机会)
							if (exceptionTime == 0) {// 首次检测到异常状态，设置异常开始时间
								serverInfo.setExceptionTime(lastUpdateTime);
							}
							serverInfo.setExceptionDurationTime(lastUpdateTime - exceptionTime);// 记录异常状态持续时间
							exceptionServerList.add(serverInfo.clone());
						}
					}
				}

				// 调用异常处理
				// 获取所有服务器异常处理器
//				Map<String, AbstractAlertServerProcessor> processorMap = CoreSpringContextManager.getApplicationContext().getBeansOfType(AbstractAlertServerProcessor.class);
//				for (final AbstractAlertServerProcessor processor : processorMap.values()) {
//					Thread alertThread = new Thread() {// 为每个异常处理器新起线程-匿名内部类
//						@Override
//						public void run() {
//							// 异常状态处理
//							try {
//								if (exceptionServerList.size() != 0) {
//									processor.serverExceptionStatusEvent(exceptionServerList);
//								}
//							} catch (Exception e) {
//								logger.warn(String.format("error occurs when invoking processor[%s] to process exception servers. exception[%s]", processor.getClass(), e.getMessage()));
//							}
//							
//							// 恢复正常状态处理
//							try {
//								if (recoveredServerList.size() != 0) {
//									processor.serverRecoverEvent(recoveredServerList);
//								}
//							} catch (Exception e) {
//								logger.warn(String.format("error occurs when invoking processor[%s] to process recovered servers. exception[%s]", processor.getClass(), e.getMessage()));
//							}
//						}
//
//					};
//					alertThread.start();
//				}

				this.waitSelf();
			}
		}

	}
}
