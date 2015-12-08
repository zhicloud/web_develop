package com.zhicloud.ms.app.pool.host.back;

import java.util.Hashtable;
import java.util.Map;

import com.sun.media.jfxmedia.logging.Logger;

public class HostBackupProgressPool {

	private Map<String, HostBackupProgressData> pool;

	public HostBackupProgressPool() {
		pool = new Hashtable<String, HostBackupProgressData>();
	}

	public HostBackupProgressData[] getAll() {
		return this.pool.values().toArray(new HostBackupProgressData[0]);
	}

	public void put(HostBackupProgressData hostBackup) {
		String uuid = hostBackup.getUuid();

		if (uuid != null && uuid.trim().length() != 0 ) {
			String key = uuid;
			pool.put(key, hostBackup);
		}
	    System.out.println("hhahhahahhahahhahahahah put"+ uuid);

	}

	public HostBackupProgressData get(String uuid) {
		if (uuid != null && uuid.trim().length() != 0) {
			String key = uuid;
			return pool.get(key);
		}

		return null;
	}
	
	public void remove(HostBackupProgressData hostBackup) {
		String sessionId = hostBackup.getSessionId();

		if (sessionId != null && sessionId.trim().length() != 0 ) {
			String key = sessionId;
			pool.remove(key);
		}
		System.out.println("hhahhahahhahahhahahahah remove"+ sessionId);
	}
	
//	public HostBackupProgressData getDuplication(String sessionId) {
//		HostBackupProgressData self = this.get(sessionId);
//		if (self != null) {
//			return self.clone();
//		}
//		
//		return null;
//	}

}
