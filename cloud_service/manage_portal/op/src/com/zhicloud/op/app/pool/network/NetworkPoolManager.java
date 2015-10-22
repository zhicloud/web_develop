package com.zhicloud.op.app.pool.network;

import org.apache.log4j.Logger;

public class NetworkPoolManager {

	private final static Logger logger = Logger.getLogger(NetworkPoolManager.class);
	private static NetworkPoolManager instance = null;
	private final NetworkCreateInfoPool createInfoPool;
	private final NetworkInfoPool infoPool;
	private final NetworkInfoPool modifyPool;
	private final NetworkInfoPool startPool;
	private final NetworkInfoPool stopPool;
	private final NetworkInfoPool delPool;
	private final NetworkInfoPool queryHostPool;
	private final NetworkInfoSessionPool attachHostPool;
	private final NetworkInfoSessionPool detachHostPool;
	private final NetworkInfoSessionPool attachAddressPool;
	private final NetworkInfoSessionPool detachAddressPool;
	private final NetworkInfoSessionPool bindPortPool;
	private final NetworkInfoSessionPool unbindPortPool;
	private final static Object handler = new Object();

	// 单例
	public synchronized static NetworkPoolManager singleton() {
		if (NetworkPoolManager.instance == null) {
			NetworkPoolManager.instance = new NetworkPoolManager();
		}

		return instance;
	}

	private NetworkPoolManager() {
		createInfoPool = new NetworkCreateInfoPool();
		infoPool = new NetworkInfoPool();
		modifyPool = new NetworkInfoPool();
		startPool = new NetworkInfoPool();
		stopPool = new NetworkInfoPool();
		delPool = new NetworkInfoPool();
		queryHostPool = new NetworkInfoPool();
		attachHostPool = new NetworkInfoSessionPool();
		detachHostPool = new NetworkInfoSessionPool();
		attachAddressPool = new NetworkInfoSessionPool();
		detachAddressPool = new NetworkInfoSessionPool();
		bindPortPool = new NetworkInfoSessionPool();
		unbindPortPool = new NetworkInfoSessionPool();
		// 扫描线程
		Thread timeoutThread = new Thread(new TimeoutRunnable());
		timeoutThread.setDaemon(true);
		timeoutThread.start();
	}

	public NetworkCreateInfoPool getCreateInfoPool() {
		return createInfoPool;
	}

	public NetworkInfoPool getInfoPool() {
		return infoPool;
	}

	public NetworkInfoPool getModifyPool() {
		return modifyPool;
	}

	public NetworkInfoPool getStartPool() {
		return startPool;
	}

	public NetworkInfoPool getStopPool() {
		return stopPool;
	}

	public NetworkInfoPool getDelPool() {
		return delPool;
	}

	public NetworkInfoPool getQueryHostPool() {
		return queryHostPool;
	}

	public NetworkInfoSessionPool getAttachHostPool() {
		return attachHostPool;
	}

	public NetworkInfoSessionPool getDetachHostPool() {
		return detachHostPool;
	}

	public NetworkInfoSessionPool getAttachAddressPool() {
		return attachAddressPool;
	}

	public NetworkInfoSessionPool getDetachAddressPool() {
		return detachAddressPool;
	}

	public NetworkInfoSessionPool getBindPortPool() {
		return bindPortPool;
	}

	public NetworkInfoSessionPool getUnbindPortPool() {
		return unbindPortPool;
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
				NetworkInfoExt[] createInfoList = NetworkPoolManager.this.createInfoPool.getAll();
				for (int i = 0; i < createInfoList.length; i++) {
					NetworkInfoExt network = createInfoList[i];
					if ((currentTime - network.getLastUpdateTime()) > 30 * 60 * 1000) {// 超过30分钟没有更新，则认为该数据不再使用.
						NetworkPoolManager.this.createInfoPool.remove(network);
					}
				}
				// 缓冲池：info pool
				NetworkInfoExt[] infoList = NetworkPoolManager.this.infoPool.getAll();
				for (int i = 0; i < infoList.length; i++) {
					NetworkInfoExt network = infoList[i];
					if ((currentTime - network.getLastUpdateTime()) > 15 * 1000) {// 超过15秒没有更新，则认为该数据不再使用.
						NetworkPoolManager.this.infoPool.remove(network);
					}
				}
				// 缓冲池：modify pool
				NetworkInfoExt[] modifyList = NetworkPoolManager.this.modifyPool.getAll();
				for (int i = 0; i < modifyList.length; i++) {
					NetworkInfoExt network = modifyList[i];
					if ((currentTime - network.getLastUpdateTime()) > 30 * 60 * 1000) {// 超过30分钟没有更新，则认为该数据不再使用.
						NetworkPoolManager.this.modifyPool.remove(network);
					}
				}
				// 缓冲池：start pool
				NetworkInfoExt[] startList = NetworkPoolManager.this.startPool.getAll();
				for (int i = 0; i < startList.length; i++) {
					NetworkInfoExt network = startList[i];
					if ((currentTime - network.getLastUpdateTime()) > 30 * 60 * 1000) {// 超过30分钟没有更新，则认为该数据不再使用.
						NetworkPoolManager.this.startPool.remove(network);
					}
				}
				// 缓冲池：stop pool
				NetworkInfoExt[] stopList = NetworkPoolManager.this.stopPool.getAll();
				for (int i = 0; i < stopList.length; i++) {
					NetworkInfoExt network = stopList[i];
					if ((currentTime - network.getLastUpdateTime()) > 30 * 60 * 1000) {// 超过30分钟没有更新，则认为该数据不再使用.
						NetworkPoolManager.this.stopPool.remove(network);
					}
				}
				// 缓冲池：del pool
				NetworkInfoExt[] delList = NetworkPoolManager.this.delPool.getAll();
				for (int i = 0; i < delList.length; i++) {
					NetworkInfoExt network = delList[i];
					if ((currentTime - network.getLastUpdateTime()) > 30 * 60 * 1000) {// 超过30分钟没有更新，则认为该数据不再使用.
						NetworkPoolManager.this.delPool.remove(network);
					}
				}
				// 缓冲池：query host pool
				NetworkInfoExt[] queryHostList = NetworkPoolManager.this.queryHostPool.getAll();
				for (int i = 0; i < queryHostList.length; i++) {
					NetworkInfoExt network = queryHostList[i];
					if ((currentTime - network.getLastUpdateTime()) > 30 * 60 * 1000) {// 超过30分钟没有更新，则认为该数据不再使用.
						NetworkPoolManager.this.queryHostPool.remove(network);
					}
				}
				// 缓冲池：attach host pool
				NetworkInfoExt[] attachHostList = NetworkPoolManager.this.attachHostPool.getAll();
				for (int i = 0; i < attachHostList.length; i++) {
					NetworkInfoExt network = attachHostList[i];
					if ((currentTime - network.getLastUpdateTime()) > 30 * 60 * 1000) {// 超过30分钟没有更新，则认为该数据不再使用.
						NetworkPoolManager.this.attachHostPool.remove(network);
					}
				}
				// 缓冲池：detach host pool
				NetworkInfoExt[] detachHostList = NetworkPoolManager.this.detachHostPool.getAll();
				for (int i = 0; i < detachHostList.length; i++) {
					NetworkInfoExt network = detachHostList[i];
					if ((currentTime - network.getLastUpdateTime()) > 30 * 60 * 1000) {// 超过30分钟没有更新，则认为该数据不再使用.
						NetworkPoolManager.this.detachHostPool.remove(network);
					}
				}
				// 缓冲池：attach address pool
				NetworkInfoExt[] attachAddressList = NetworkPoolManager.this.attachAddressPool.getAll();
				for (int i = 0; i < attachAddressList.length; i++) {
					NetworkInfoExt network = attachAddressList[i];
					if ((currentTime - network.getLastUpdateTime()) > 30 * 60 * 1000) {// 超过30分钟没有更新，则认为该数据不再使用.
						NetworkPoolManager.this.attachAddressPool.remove(network);
					}
				}
				// 缓冲池：detach address pool
				NetworkInfoExt[] detachAddressList = NetworkPoolManager.this.detachAddressPool.getAll();
				for (int i = 0; i < detachAddressList.length; i++) {
					NetworkInfoExt network = detachAddressList[i];
					if ((currentTime - network.getLastUpdateTime()) > 30 * 60 * 1000) {// 超过30分钟没有更新，则认为该数据不再使用.
						NetworkPoolManager.this.detachAddressPool.remove(network);
					}
				}
				// 缓冲池：bind port pool
				NetworkInfoExt[] bindPortList = NetworkPoolManager.this.bindPortPool.getAll();
				for (int i = 0; i < bindPortList.length; i++) {
					NetworkInfoExt network = bindPortList[i];
					if ((currentTime - network.getLastUpdateTime()) > 30 * 60 * 1000) {// 超过30分钟没有更新，则认为该数据不再使用.
						NetworkPoolManager.this.bindPortPool.remove(network);
					}
				}
				// 缓冲池：unbind port pool
				NetworkInfoExt[] unbindPortList = NetworkPoolManager.this.unbindPortPool.getAll();
				for (int i = 0; i < unbindPortList.length; i++) {
					NetworkInfoExt network = unbindPortList[i];
					if ((currentTime - network.getLastUpdateTime()) > 30 * 60 * 1000) {// 超过30分钟没有更新，则认为该数据不再使用.
						NetworkPoolManager.this.unbindPortPool.remove(network);
					}
				}
			}
		}

	}

}
