package com.zhicloud.ms.app.pool.diskImagePool;

import java.util.Hashtable;
import java.util.Map;

public class DiskImageProgressPool {

	private Map<String, DiskImageProgressData> pool;

	public DiskImageProgressPool() {
		pool = new Hashtable<String, DiskImageProgressData>();
	}

	public DiskImageProgressData[] getAll() {
		return this.pool.values().toArray(new DiskImageProgressData[0]);
	}

	public void put(DiskImageProgressData diskImage) {
		String sessionId = diskImage.getSessionId();
		String name = diskImage.getName();

		if (sessionId != null && sessionId.trim().length() != 0 && name != null && name.trim().length() != 0) {
			String key = sessionId + "_" + name;
			pool.put(key, diskImage);
		}
	}

	public DiskImageProgressData get(String sessionId, String name) {
		if (sessionId != null && sessionId.trim().length() != 0 && name != null && name.trim().length() != 0) {
			String key = sessionId + "_" + name;
			return pool.get(key);
		}

		return null;
	}
	
	public DiskImageProgressData getDuplication(String sessionId, String name) {
		DiskImageProgressData self = this.get(sessionId, name);
		if (self != null) {
			return self.clone();
		}
		
		return null;
	}

	public void remove(DiskImageProgressData diskImage) {
		String sessionId = diskImage.getSessionId();
		String name = diskImage.getName();

		if (sessionId != null && sessionId.trim().length() != 0 && name != null && name.trim().length() != 0) {
			String key = sessionId + "_" + name;
			pool.remove(key);
		}
	}

}
