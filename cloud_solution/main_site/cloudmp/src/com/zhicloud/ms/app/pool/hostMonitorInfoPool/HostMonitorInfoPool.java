package com.zhicloud.ms.app.pool.hostMonitorInfoPool;

import java.util.Hashtable;
import java.util.Map;

public class HostMonitorInfoPool {

	private final Map<Integer, HostMonitorInfo> pool;

	public HostMonitorInfoPool() {
		pool = new Hashtable<Integer, HostMonitorInfo>();
	}

	public HostMonitorInfo get(Integer region) {
		if (region == null) {
			return null;
		}

		return pool.get(region);
	}
	
	public HostMonitorInfo[] getAll() {
		HostMonitorInfo[] list = pool.values().toArray(new HostMonitorInfo[0]);
		
		return list;
	}

	public boolean put(HostMonitorInfo hostMonitorInfo) {
		Integer key = hostMonitorInfo.getRegion();
		if (key == null) {
			return false;
		}

		pool.put(key, hostMonitorInfo);

		return true;
	}

	public HostMonitorInfo remove(Integer region) {
		if (region == null) {
			return null;
		}

		return pool.remove(region);
	}

}

