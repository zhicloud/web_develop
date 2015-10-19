package com.zhicloud.ms.httpGateway;

import java.util.Hashtable;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.zhicloud.ms.app.propeties.AppProperties;

public class HttpGatewayActiveAsyncChannelPool {

	private final static Logger logger = Logger.getLogger(HttpGatewayActiveAsyncChannelPool.class);
	private static HttpGatewayActiveAsyncChannelPool instance = null;
	private final Map<String, HttpGatewayAsyncChannel> channelPool;
	private final KeepAliveRunnable keepAliveRunnable = new KeepAliveRunnable();

	public synchronized static HttpGatewayActiveAsyncChannelPool singleton() {
		if (HttpGatewayActiveAsyncChannelPool.instance == null) {
			HttpGatewayActiveAsyncChannelPool.instance = new HttpGatewayActiveAsyncChannelPool();
		}

		return HttpGatewayActiveAsyncChannelPool.instance;
	}

	private HttpGatewayActiveAsyncChannelPool() {
		channelPool = new Hashtable<String, HttpGatewayAsyncChannel>();// key:sessionid,value:channel
		// 开启线程:发送keepalive消息到相应的http_gateway上
		Thread thread = new Thread(this.keepAliveRunnable);
		thread.setDaemon(true);
		thread.start();
	}

	// 停止线程
	private void stopThread() {
		this.keepAliveRunnable.setRun(false);
	}

	// 对象被回收时，停止线程
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		this.stopThread();
	}

	public void saveChannel(HttpGatewayAsyncChannel channel) {
		this.channelPool.put(channel.getSessionId(), channel);
	}

	public void removeChannel(HttpGatewayAsyncChannel channel) {
		String sessionId = channel.getSessionId();
		HttpGatewayAsyncChannel target = null;

		if (sessionId != null) {// sessionid不为空
			target = this.channelPool.remove(sessionId);
			if (target != null && target == channel) {
				return;
			}
		}
		// sessiond为空的处理
		String[] keyList = this.channelPool.keySet().toArray(new String[0]);
		for (String key : keyList) {
			if (this.channelPool.get(key) == channel) {
				this.channelPool.remove(key);
			}
		}
	}

	public HttpGatewayAsyncChannel getChannel(String sessionId) {
		if (sessionId == null) {
			return null;
		}
		return this.channelPool.get(sessionId);
	}

	/**
	 * 每隔10秒,循环遍历channelPool,并为每个channel发送keepalive消息
	 */
	private class KeepAliveRunnable implements Runnable {

		private boolean run = true;

		public synchronized boolean isRun() {
			return this.run;
		}

		public synchronized void setRun(boolean run) {
			this.run = run;
		}

		@Override
		public void run() {
			logger.info("Async channels start to keep connection with http gateway.");

			while (this.isRun()) {
				Object[] list = HttpGatewayActiveAsyncChannelPool.this.channelPool.values().toArray();// 转化成数组
				for (int i = 0; i < list.length; i++) {// 不能使用迭代，否则在释放channel时报ConcurrentModificationException错误
					HttpGatewayAsyncChannel channel = (HttpGatewayAsyncChannel) list[i];
					try {
						// 长时间没有接收到异步消息时，释放channel
						long refreshTime = Long.parseLong(AppProperties.getValue("http_gateway_session_refresh_time", "10")); // 单位为分钟
						refreshTime = refreshTime * 60 * 1000;
						// 超过了一定的时间，释放channel
						if (System.currentTimeMillis() - channel.getLastReceiveTime() >= refreshTime) {
							channel.release();
							logger.info("Found long time to get no async message in async channel and release itself. Region[" + channel.getRegion() + "], Session[" + channel.getSessionId() + "].");
							continue;
						}
						// 发送keep connetion alive消息
						JSONObject result = null;
						HttpGatewayHelper helper = channel.getHelper();
						if (helper != null) {
							result = helper.keepConnectionAlive(0);
						}

						if (helper == null || HttpGatewayResponseHelper.isSuccess(result) == false) {
							channel.release();
							logger.warn("Active async channel fail to keep connection with http gateway and release itself. Region[" + channel.getRegion() + "], Session[" + channel.getSessionId() + "].");
						} else {
							logger.debug("Active async channel success to keep connection with http gateway. Region[" + channel.getRegion() + "], Session[" + channel.getSessionId() + "].");
						}
					} catch (Exception exception) {
						channel.release();
						logger.error("Errors occurs when active async channel keeps connection with http gateway, then release itself. Region[" + channel.getRegion() + "], Session[" + channel.getSessionId() + "].", exception);
					}
				}

				try {
					Thread.sleep(10000);
				} catch (InterruptedException exception) {
					logger.error("[" + Thread.currentThread().getId() + "] fail to sleep.", exception);
				}
			}

			logger.info("Async channels stop to keep connection with http gateway.");
		}

	}

}
