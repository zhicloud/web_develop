package com.zhicloud.op.app.pool.host.back;

import java.util.Hashtable;
import java.util.Map;

public class HostBackupProgressPool {

	private Map<String, HostBackupProgressData> pool;

	public HostBackupProgressPool() {
		pool = new Hashtable<String, HostBackupProgressData>();
	}

	public HostBackupProgressData[] getAll() {
		return this.pool.values().toArray(new HostBackupProgressData[0]);
	}

	public void put(HostBackupProgressData hostBackup) {
		String uuid = hostBackup.getRealHostId();

		if (uuid != null && uuid.trim().length() != 0 ) {
			String key = uuid;
			pool.put(key, hostBackup);
		}
	}

	public HostBackupProgressData get(String uuid) {
		if (uuid != null && uuid.trim().length() != 0) {
			String key = uuid;
			return pool.get(key);
		}

		return null;
	}
	
	public void remove(HostBackupProgressData hostBackup) {
		String uuid = hostBackup.getRealHostId();

		if (uuid != null && uuid.trim().length() != 0 ) {
			String key = uuid;
			pool.remove(key);
		}
	}
	
	public HostBackupProgressData getDuplication(String uuid) {
		HostBackupProgressData self = this.get(uuid);
		if (self != null) {
			return self.clone();
		}
		
		return null;
	}

}
