package com.zhicloud.op.app.pool.hostMonitorInfoPool;

public class HostMonitorInfoManager {

	private static HostMonitorInfoManager instance = null;
	private final HostMonitorInfoPool hostMonitorInfoPool;

	public static synchronized HostMonitorInfoManager singleton() {
		if (HostMonitorInfoManager.instance == null) {
			HostMonitorInfoManager.instance = new HostMonitorInfoManager();
		}

		return HostMonitorInfoManager.instance;
	}

	private HostMonitorInfoManager() {
		hostMonitorInfoPool = new HostMonitorInfoPool();
	}

	public HostMonitorInfo getHostMonitorInfo(Integer region) {
		return hostMonitorInfoPool.get(region);
	}
	
	public HostMonitorInfo[] getAllHostMonitorInfo() {
		return hostMonitorInfoPool.getAll();
	}

	public boolean saveHostMonitorInfo(HostMonitorInfo hostMonitorInfo) {
		return hostMonitorInfoPool.put(hostMonitorInfo);
	}

	public HostMonitorInfo removeHostMonitorInfo(Integer region) {
		return hostMonitorInfoPool.remove(region);
	}

}
