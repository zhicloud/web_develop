package com.zhicloud.ms.app.pool.serverInfoPool;

import java.util.Hashtable;
import java.util.Map;

public class ServerInfoPool {

	private Map<String, ServerInfoExt> serverPool = null;

	public ServerInfoPool() {
		serverPool = new Hashtable<String, ServerInfoExt>();
	}

	public ServerInfoExt[] getAllServer() {
		return this.serverPool.values().toArray(new ServerInfoExt[0]);
	}

	public void putServer(ServerInfoExt serverInfo) {
		String uuid = serverInfo.getUuid();

		if (uuid != null && uuid.trim().length() != 0) {
			this.serverPool.put(uuid, serverInfo);
		}
	}

	public ServerInfoExt getServer(String uuid) {
		if (uuid != null && uuid.trim().length() != 0) {
			return this.serverPool.get(uuid);
		}
		
		return null;
	}
	
	public ServerInfoExt getServerDuplication(String uuid) {
		ServerInfoExt serverInfo = this.getServer(uuid);
		
		if (serverInfo != null) {
			return serverInfo.clone();
		}
		
		return null;
	}
	
	public void removeServer(String uuid) {
		if (uuid != null && uuid.trim().length() != 0) {
			this.serverPool.remove(uuid);
		}
	}
}
