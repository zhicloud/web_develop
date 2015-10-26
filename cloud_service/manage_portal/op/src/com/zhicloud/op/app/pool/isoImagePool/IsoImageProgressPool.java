package com.zhicloud.op.app.pool.isoImagePool;

import java.util.Hashtable;
import java.util.Map;

public class IsoImageProgressPool {
	
	private Map<String, IsoImageProgressData> pool;

	public IsoImageProgressPool() {
		pool = new Hashtable<String, IsoImageProgressData>();
	}
	
	public IsoImageProgressData[] getAll() {
		return this.pool.values().toArray(new IsoImageProgressData[0]);
	}
	
	public void put(IsoImageProgressData isoImage) {
		String sessionId = isoImage.getSessionId();
		String name = isoImage.getName();
		
		if (sessionId != null && sessionId.trim().length() != 0 && name != null && name.trim().length() != 0) {
			String key = sessionId + "_" + name;
			pool.put(key, isoImage);
		}
	}
	
	public IsoImageProgressData get(String sessionId, String name) {
		if (sessionId != null && sessionId.trim().length() != 0 && name != null && name.trim().length() != 0) {
			String key = sessionId + "_" + name;
			return pool.get(key);
		}
		
		return null;
	}
	
	public IsoImageProgressData getDuplication(String sessionId, String name) {
		IsoImageProgressData self = this.get(sessionId, name);
		if (self != null) {
			return self.clone();
		}
		
		return null;
	}
	
	public void remove(IsoImageProgressData isoImage) {
		String sessionId = isoImage.getSessionId();
		String name = isoImage.getName();
		
		if (sessionId != null && sessionId.trim().length() != 0 && name != null && name.trim().length() != 0) {
			String key = sessionId + "_" + name;
			pool.remove(key);
		}
	}

}
