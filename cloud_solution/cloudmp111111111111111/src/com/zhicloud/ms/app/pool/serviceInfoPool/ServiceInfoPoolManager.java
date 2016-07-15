package com.zhicloud.ms.app.pool.serviceInfoPool;

import java.util.ArrayList;
import java.util.List; 

import org.apache.log4j.Logger;

import com.zhicloud.ms.constant.MonitorConstant;


public class ServiceInfoPoolManager {

 	private static ServiceInfoPoolManager instance = null;
	private final ServiceInfoPool serviceInfoPool;

	public synchronized static ServiceInfoPoolManager singleton() {
		if (ServiceInfoPoolManager.instance == null) {
			ServiceInfoPoolManager.instance = new ServiceInfoPoolManager();
		}

		return ServiceInfoPoolManager.instance;
	}

	private ServiceInfoPoolManager() {
		serviceInfoPool = new ServiceInfoPool();
		// 启动扫描线程
		ScanServiceRunnable scanServiceRunnable = new ScanServiceRunnable();
		Thread thread = new Thread(scanServiceRunnable);
		thread.setDaemon(true);
		thread.start();
	}

	public ServiceInfoPool getPool() {
		return this.serviceInfoPool;
	}

	private class ScanServiceRunnable implements Runnable {

		private final Logger logger = Logger.getLogger(ScanServiceRunnable.class);

		private synchronized void waitSelf() {
			try {
				this.wait(1000);
			} catch (InterruptedException e) {
				logger.warn(String.format("the scan service thread is interrupted when waiting. exception[%s]", e.getMessage()));
			}
		}

		@Override
		public void run() {
			while (true) {
				ServiceInfoExt[] serviceInfoArray = ServiceInfoPoolManager.singleton().getPool().getAll();
				final List<ServiceInfoExt> exceptionServiceList = new ArrayList<ServiceInfoExt>();
				final List<ServiceInfoExt> recoveredServiceList = new ArrayList<ServiceInfoExt>();

				for (ServiceInfoExt serviceInfo : serviceInfoArray) {
                    // 计算新状态(主要是检测是否被屏蔽和停止状态设置为故障)
                    String shield = MonitorConstant.shieldmap.get(serviceInfo.getName());
                    if (MonitorConstant.shield_off.equals(shield)) {
                        serviceInfo.setNewStatus(MonitorConstant.status_stop);
                        continue;
                    } else {
                        if (MonitorConstant.gw_status_error.equals(serviceInfo.getStatus() + "")
                                || MonitorConstant.gw_status_stop.equals(serviceInfo.getStatus() + "")) {
                            serviceInfo.setNewStatus(MonitorConstant.status_error);
                        } else if (MonitorConstant.gw_status_warn.equals(serviceInfo.getStatus() + "")) {
                            serviceInfo.setNewStatus(MonitorConstant.status_warn);
                        } else if (MonitorConstant.gw_status_normal.equals(serviceInfo.getStatus() + "")) {
                            serviceInfo.setNewStatus(MonitorConstant.status_normal);
                        }
                    }
					// 1.维护service info pool:60s超时后，移除该service information
					if (System.currentTimeMillis() - serviceInfo.getLastUpdateTime() > 60 * 1000) {
						ServiceInfoPoolManager.singleton().getPool().remove(serviceInfo.getName());
						continue;
					}

					// 2.状态监测:检测服务状态是否异常，并记录异常开始时间；如果服务恢复正常，记录异常状态持续时间。
					synchronized (serviceInfo) {// 同步处理,保证数据一致，防止数据覆盖
						int status = serviceInfo.getStatus();
						long exceptionTime = serviceInfo.getExceptionTime();
						long lastUpdateTime = serviceInfo.getLastUpdateTime();
						long currentTime = System.currentTimeMillis();

						if (currentTime - lastUpdateTime < 15 * 1000) {// 最近更新时间在15s内(保证至少有一次更新机会)
							if (status != 0) {// 异常状态
								if (exceptionTime == 0) {// 首次检测到异常状态，设置异常开始时间
									serviceInfo.setExceptionTime(lastUpdateTime);
									exceptionTime = serviceInfo.getExceptionTime();
								}
								serviceInfo.setExceptionDurationTime(currentTime - exceptionTime);// 记录异常状态持续时间

								if (lastUpdateTime - exceptionTime > 0) {// 当异常状态得到再次确认时，启动预警。
									exceptionServiceList.add(serviceInfo.clone());
								}
							} else {// 正常状态，重置异常时间
								if (exceptionTime != 0) {// 服务恢复正常状态，记录异常状态持续时间
									if (lastUpdateTime - exceptionTime >= 20 * 1000) {// 当发现异常在短时间内恢复时，并不预警。防止过度敏感。
										serviceInfo.setExceptionDurationTime(currentTime - exceptionTime);
										recoveredServiceList.add(serviceInfo.clone());
									}
								}
								serviceInfo.setExceptionTime(0);
							}
						}
					}
				}

				// 调用异常处理
				// 获取所有服务器异常处理器
//				Map<String, AbstractAlertServiceProcessor> processorMap = CoreSpringContextManager.getApplicationContext().getBeansOfType(AbstractAlertServiceProcessor.class);
//				for (final AbstractAlertServiceProcessor processor : processorMap.values()) {
//					Thread alertThread = new Thread() {// 为每个异常处理器新起线程-匿名内部类
//						@Override
//						public void run() {
//							// 异常状态处理
//							try {
//								if (exceptionServiceList.size() != 0) {
//									processor.exceptionStatusEvent(exceptionServiceList);
//								}
//							} catch (Exception e) {
//								logger.warn(String.format("error occurs when invoking processor[%s] to process exception services. exception[%s]", processor.getClass(), e.getMessage()));
//							}
//
//							// 恢复正常状态处理
//							try {
//								if (recoveredServiceList.size() != 0) {
//									processor.recoverEvent(recoveredServiceList);
//								}
//							} catch (Exception e) {
//								logger.warn(String.format("error occurs when invoking processor[%s] to process recovered services. exception[%s]", processor.getClass(), e.getMessage()));
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
