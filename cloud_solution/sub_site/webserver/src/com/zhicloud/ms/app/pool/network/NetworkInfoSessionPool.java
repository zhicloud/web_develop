package com.zhicloud.ms.app.pool.network;

import java.util.Hashtable;
import java.util.Map;

public class NetworkInfoSessionPool {

	private Map<String, NetworkInfoExt> pool;

	public NetworkInfoSessionPool() {
		pool = new Hashtable<String, NetworkInfoExt>();
	}

	public NetworkInfoExt[] getAll() {
		return pool.values().toArray(new NetworkInfoExt[0]);
	}

	public NetworkInfoExt get(String sessionId, String uuid) {
		if (sessionId != null && sessionId.trim().length() != 0 && uuid != null && uuid.trim().length() != 0) {
			String key = sessionId + "_" + uuid;
			return pool.get(key);
		}

		return null;
	}

	public NetworkInfoExt getDuplication(String sessionId, String uuid) {
		NetworkInfoExt self = this.get(sessionId, uuid);

		if (self != null) {
			return self.clone();
		}

		return null;
	}

	public void put(NetworkInfoExt network) {
		String sessionId = network.getSessionId();
		String uuid = network.getUuid();

		if (sessionId != null && sessionId.trim().length() != 0 && uuid != null && uuid.trim().length() != 0) {
			String key = sessionId + "_" + uuid;
			pool.put(key, network);
		}
	}

	public void remove(NetworkInfoExt network) {
		String sessionId = network.getSessionId();
		String uuid = network.getUuid();

		if (sessionId != null && sessionId.trim().length() != 0 && uuid != null && uuid.trim().length() != 0) {
			String key = sessionId + "_" + uuid;
			pool.remove(key);
		}
	}

}
