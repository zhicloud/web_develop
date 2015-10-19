package com.zhicloud.ms.app.pool.network;

import java.util.Hashtable;
import java.util.Map;

public class NetworkCreateInfoPool {
	
	private Map<String, NetworkInfoExt> pool;
	
	public NetworkCreateInfoPool() {
		pool = new Hashtable<String, NetworkInfoExt>();
	}
	
	public NetworkInfoExt[] getAll() {
		return this.pool.values().toArray(new NetworkInfoExt[0]);
	}
	
	public void put(NetworkInfoExt network) {
		Integer regionId = network.getRegionId();
		String name = network.getName();
		
		if (regionId != null && name != null && name.trim().length() != 0) {
			String key = regionId + "_" + name;
			pool.put(key, network);
		}
	}
	
	public NetworkInfoExt get(Integer regionId, String name) {
		if (regionId != null && name != null && name.trim().length() != 0) {
			String key = regionId + "_" + name;
			return pool.get(key);
		}
		
		return null;
	}
	
	public NetworkInfoExt getDuplication(Integer regionId, String name) {
		NetworkInfoExt self = this.get(regionId, name);
		if (self != null) {
			return self.clone();
		}
		
		return null;
	}
	
	public void remove(NetworkInfoExt network) {
		Integer regionId = network.getRegionId();
		String name = network.getName();
		
		if (regionId != null && name != null && name.trim().length() != 0) {
			String key = regionId + "_" + name;
			pool.remove(key);
		}
	}

}
