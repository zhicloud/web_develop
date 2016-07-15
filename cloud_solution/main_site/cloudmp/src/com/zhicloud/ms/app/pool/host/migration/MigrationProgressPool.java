package com.zhicloud.ms.app.pool.host.migration;

import java.util.Hashtable;
import java.util.Map;

public class MigrationProgressPool {

	private Map<String, MigrationProgressData> pool;

	public MigrationProgressPool() {
		pool = new Hashtable<String, MigrationProgressData>();
	}

	public MigrationProgressData[] getAll() {
		return this.pool.values().toArray(new MigrationProgressData[0]);
	}

	public void put(MigrationProgressData Migration) {
		String uuid = Migration.getRealHostId();
		
		if (uuid != null && uuid.trim().length() != 0 ) {
			String key = uuid;
			pool.put(key, Migration);

		}
	}

	public MigrationProgressData get(String uuid) {
		if (uuid != null && uuid.trim().length() != 0) {
			String key = uuid;
			return pool.get(key);
		}

		return null;
	}
	
	public void remove(MigrationProgressData Migration) {
		String uuid = Migration.getRealHostId();

        if (uuid != null && uuid.trim().length() != 0 ) {
			String key = uuid;
			pool.remove(key);
		}
	}
	
	public MigrationProgressData getDuplication(String uuid) {
		
		MigrationProgressData self = this.get(uuid);
		if (self != null) {
			return self.clone();
		}
		return null;
	}

}
