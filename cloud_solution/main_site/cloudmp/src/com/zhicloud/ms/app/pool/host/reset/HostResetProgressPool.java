package com.zhicloud.ms.app.pool.host.reset;

import java.util.Hashtable;
import java.util.Map;

public class HostResetProgressPool {

	private Map<String, HostResetProgressData> pool;

	public HostResetProgressPool() {
		pool = new Hashtable<String, HostResetProgressData>();
	}

	public HostResetProgressData[] getAll() {
		return this.pool.values().toArray(new HostResetProgressData[0]);
	}

	public void put(HostResetProgressData hostReset) {
		String uuid = hostReset.getRealHostId();
		
		if (uuid != null && uuid.trim().length() != 0 ) {
			String key = uuid;
			pool.put(key, hostReset);

		}
	}

	public HostResetProgressData get(String uuid) {
		if (uuid != null && uuid.trim().length() != 0) {
			String key = uuid;
			return pool.get(key);
		}

		return null;
	}
	
	public void remove(HostResetProgressData hostReset) {
		String uuid = hostReset.getRealHostId();

        if (uuid != null && uuid.trim().length() != 0 ) {
			String key = uuid;
			pool.remove(key);
		}
	}
	
	public HostResetProgressData getDuplication(String uuid) {
		
		HostResetProgressData self = this.get(uuid);
		if (self != null) {
			return self.clone();
		}
		return null;
	}

}
