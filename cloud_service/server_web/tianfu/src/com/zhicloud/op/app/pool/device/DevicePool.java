package com.zhicloud.op.app.pool.device;

import java.util.Hashtable;
import java.util.Map;

public class DevicePool {

	private Map<String, DeviceExt> pool;

	public DevicePool() {
		pool = new Hashtable<String, DeviceExt>();
	}

	public DeviceExt[] getAll() {
		return pool.values().toArray(new DeviceExt[0]);
	}

	public DeviceExt[] getAllDuplication() {
		DeviceExt[] self = this.getAll();

		DeviceExt[] duplication = new DeviceExt[self.length];
		for (int i = 0; i < self.length; i++) {
			duplication[i] = self[i].clone();
		}

		return duplication;
	}

	public DeviceExt get(String uuid) {
		if (uuid != null && uuid.trim().length() != 0) {
			return pool.get(uuid);
		}

		return null;
	}

	public DeviceExt getDuplication(String uuid) {
		DeviceExt self = this.get(uuid);

		if (self != null) {
			return self.clone();
		}

		return null;
	}

	public void put(DeviceExt device) {
		String uuid = device.getUuid();

		if (uuid != null && uuid.trim().length() != 0) {
			pool.put(uuid, device);
		}
	}

	public void remove(DeviceExt device) {
		String uuid = device.getUuid();

		if (uuid != null && uuid.trim().length() != 0) {
			pool.remove(uuid);
		}
	}

}
