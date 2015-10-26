package com.zhicloud.op.app.pool.network;

import java.util.Hashtable;
import java.util.Map;

public class NetworkInfoPool {

	private Map<String, NetworkInfoExt> pool;

	public NetworkInfoPool() {
		pool = new Hashtable<String, NetworkInfoExt>();
	}

	public NetworkInfoExt[] getAll() {
		return pool.values().toArray(new NetworkInfoExt[0]);
	}

	public NetworkInfoExt[] getALlDuplication() {
		NetworkInfoExt[] self = this.getAll();

		NetworkInfoExt[] duplication = new NetworkInfoExt[self.length];
		for (int i = 0; i < self.length; i++) {
			duplication[i] = self[i];
		}

		return duplication;
	}

	public NetworkInfoExt get(String uuid) {
		if (uuid != null && uuid.trim().length() != 0) {
			return pool.get(uuid);
		}
		
		return null;
	}
	
	public NetworkInfoExt getDuplication(String uuid) {
		NetworkInfoExt self = this.get(uuid);
		
		if (self != null) {
			return self.clone();
		}
		
		return null;
	}
	
	public void put(NetworkInfoExt network) {
		String uuid = network.getUuid();
		
		if (uuid != null && uuid.trim().length() != 0) {
			pool.put(uuid, network);
		}
	}
	
	public void remove(NetworkInfoExt network) {
		String uuid = network.getUuid();
		
		if (uuid != null && uuid.trim().length() != 0) {
			pool.remove(uuid);
		}
	}

}
