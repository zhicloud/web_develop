package com.zhicloud.ms.app.pool.serviceInfoPool;

import java.util.Hashtable;
import java.util.Map;


public class ServiceInfoPool {

	private Map<String, ServiceInfoExt> servicePool = null;

	public ServiceInfoPool() {
		servicePool = new Hashtable<String, ServiceInfoExt>();
	}

	public ServiceInfoExt[] getAll() {
		return this.servicePool.values().toArray(new ServiceInfoExt[0]);
	}

	public  void put(ServiceInfoExt serviceInfo) {
		String name = serviceInfo.getName();
    String sessionId = serviceInfo.getSessionId();

    //用于gw获取数据
    if (sessionId != null && sessionId.trim().length() != 0) {
        this.servicePool.put(sessionId, serviceInfo);
    }

		if (name != null && name.trim().length() != 0) {
			this.servicePool.put(name, serviceInfo);
		}
	}

	public  ServiceInfoExt get(String name) {
		if (name != null && name.trim().length() != 0) {
			return this.servicePool.get(name);
		}

		return null;
	}

	public ServiceInfoExt getDuplication(String name) {
		ServiceInfoExt serviceInfo = this.get(name);

		if (serviceInfo != null) {
			return serviceInfo.clone();
		}

		return null;
	}

	public void remove(String name) {
		if (name != null && name.trim().length() != 0) {
			this.servicePool.remove(name);
		}
	}

}
