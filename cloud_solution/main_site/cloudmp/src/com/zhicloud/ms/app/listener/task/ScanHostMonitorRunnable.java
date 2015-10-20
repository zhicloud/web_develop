package com.zhicloud.ms.app.listener.task;

import java.util.Set;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.zhicloud.ms.app.pool.hostMonitorInfoPool.HostMonitorInfo;
import com.zhicloud.ms.app.pool.hostMonitorInfoPool.HostMonitorInfoManager;
import com.zhicloud.ms.common.util.json.JSONLibUtil;
import com.zhicloud.ms.httpGateway.HttpGatewayAsyncChannel;
import com.zhicloud.ms.httpGateway.HttpGatewayManager;
import com.zhicloud.ms.httpGateway.HttpGatewayResponseHelper;

/**
 * 云主机资源监控信息扫描线程 扫描所有region的云主机资源监控信息。维护每个region最新云主机列表的资源监控。
 * 
 * @author Administrator
 *
 */
public class ScanHostMonitorRunnable implements Runnable {

	private final static Logger logger = Logger.getLogger(ScanHostMonitorRunnable.class);
	private long waitTime = 10;// 默认10秒, 单位：秒
	private boolean runnable = true;// 是否可运行

	/**
	 * 构造函数
	 * 
	 * @param waitTime
	 *            - 每个循环执行单元执行的间隔时间
	 */
	public ScanHostMonitorRunnable(long waitTime) {
		this.waitTime = waitTime;
	}

	/**
	 * 停止线程
	 */
	public synchronized void stop() {
		this.runnable = false;
	}

	public synchronized boolean isRunnable() {
		return runnable;
	}

	/**
	 * 等待
	 */
	private void waitObject() {
		synchronized (HostMonitorInfoManager.singleton()) {
			try {
				HostMonitorInfoManager.singleton().wait(this.waitTime * 1000);
			} catch (InterruptedException e) {
				logger.warn("the host monitor thread fail to wait.", e);
			}
		}
	}

	/**
	 * 每个一段时间扫描每个region的云主机监控信息。 如果扫描到isNeedToRestart()返回true,
	 * 则关闭上一监控并使用新的云主机列表开启新的监控。
	 */
	@Override
	public void run() {
		while (this.isRunnable()) {
			HostMonitorInfo[] list = HostMonitorInfoManager.singleton().getAllHostMonitorInfo();
			// 遍历每个已连接上的region
			for (HostMonitorInfo hostMonitorInfo : list) {
				Integer region = null;
				Set<String> hostList = null;
				String sessionId = null;
				Integer task = null;
				boolean needToRestart = false;
				long lastUpdateTime = 0;

				synchronized (hostMonitorInfo) {// 同步处理, 保证这是一个操作单元
					region = hostMonitorInfo.getRegion();
					hostList = hostMonitorInfo.getHostList();
					sessionId = hostMonitorInfo.getSessionId();
					task = hostMonitorInfo.getTask();
					needToRestart = hostMonitorInfo.isNeedToRestart();
					lastUpdateTime = hostMonitorInfo.getLastUpdateTime();
				}

				if (hostList == null || hostList.size() == 0) {// 没有云主机需要监控
					continue;
				}

				boolean timeout = (System.currentTimeMillis() - lastUpdateTime) > 60 * 1000;// 超时:60s
				if (needToRestart || (lastUpdateTime != 0 && timeout == true)) {// 如果需要重启监控,或者超时（长时间没收到消息推送）,则执行1.关闭原有监控，2.开启新的监控
					logger.info(String.format("restart host monitor. region[%s], flag[%s], timeout[%s]", region, needToRestart, timeout));

					HttpGatewayAsyncChannel channel = HttpGatewayManager.getActiveAsyncChannel(sessionId);
					// stop original monitor
					if (channel != null) {
						try {
							JSONObject result = channel.hostStopMonitor(task);
							logger.debug(String.format("stop host monitor. region[%s], session[%s], success[%s].", region, sessionId, HttpGatewayResponseHelper.isSuccess(result)));
						} catch (Exception e) {
							logger.warn(String.format("fail to stop the host monitor. region[%s], session[%s], task[%s], exception[%s]", region, sessionId, task, e.getMessage()));
						} finally {
							channel.release();
						}
					}
					// start new monitor
					try {
						channel = HttpGatewayManager.getAsyncChannel(region);
						JSONObject result = channel.hostStartMonitor(hostList.toArray(new String[0]));
						logger.debug(String.format("start host monitor. region[%s], session[%s], success[%s].", region, channel.getSessionId(), HttpGatewayResponseHelper.isSuccess(result)));

						if (HttpGatewayResponseHelper.isSuccess(result) == true) {// 启动监控成功
							Integer newTask = JSONLibUtil.getInteger(result, "task");
							synchronized (hostMonitorInfo) {// 同步处理, 保证这是一个操作单元
								hostMonitorInfo.setSessionId(channel.getSessionId());
								hostMonitorInfo.setTask(newTask);
								hostMonitorInfo.setNeedToRestart(false);
								hostMonitorInfo.setHostList(hostList);
								hostMonitorInfo.updateTime();
							}
						} else {// 启动监控失败
							channel.release();
							logger.warn(String.format("fail to start host monitor. region[%s], session[%s], message[%s].", region, channel.getSessionId(), HttpGatewayResponseHelper.getMessage(result)));
						}
					} catch (Exception e) {
						logger.warn(String.format("fail to start the host monitor. region[%s], session[%s], exception[%s]", region, sessionId, e.getMessage()));
					}

				}
			}
			// 等待一段时间
			this.waitObject();
		}
	}

}
