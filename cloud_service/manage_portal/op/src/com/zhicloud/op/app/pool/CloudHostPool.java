package com.zhicloud.op.app.pool;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.exception.AppException;

/**
 * CloudHostPool缓存的数据是从http gateway获取的数据
 */
public class CloudHostPool {

	private static final Logger logger = Logger.getLogger(CloudHostPool.class);

	private Map<String, CloudHostData> realHostIdMap = new Hashtable<String, CloudHostData>();
	private Map<String, CloudHostData> hostNameMap = new Hashtable<String, CloudHostData>(); // region+hostName具有唯一性

	public synchronized void put(CloudHostData cloudHost) {
		if (StringUtil.isBlank(cloudHost.getRealHostId()) == false) {
			realHostIdMap.put(cloudHost.getRealHostId(), cloudHost);
		}
		if (cloudHost.getRegion() != null && StringUtil.isBlank(cloudHost.getHostName()) == false) {
			String key = cloudHost.getRegion() + "|" + cloudHost.getHostName();
			hostNameMap.put(key, cloudHost);
		}
	}

	public synchronized void remove(CloudHostData cloudHost) {
		if (cloudHost == null) {
			return;
		}
		if (StringUtil.isBlank(cloudHost.getRealHostId()) == false) {
			realHostIdMap.remove(cloudHost.getRealHostId());
		}
		if (cloudHost.getRegion() != null && StringUtil.isBlank(cloudHost.getHostName()) == false) {
			String key = cloudHost.getRegion() + "|" + cloudHost.getHostName();
			hostNameMap.remove(key);
		}
	}

	public synchronized List<CloudHostData> getAll() {
		List<CloudHostData> result = new ArrayList<CloudHostData>();
		Iterator<CloudHostData> iter = realHostIdMap.values().iterator();
		while (iter.hasNext()) {
			CloudHostData cloudHostData = iter.next();
			result.add(cloudHostData.clone());
		}
		return result;
	}

	public synchronized CloudHostData getByRealHostId(String realHostId) {
		if (StringUtil.isBlank(realHostId)) {
			return null;
		}
		CloudHostData cloudHostData = realHostIdMap.get(realHostId);
		if (cloudHostData == null) {
			return null;
		} else {
			return cloudHostData.clone();
		}
	}

	public synchronized CloudHostData getByHostName(Integer region, String hostName) {
		if (region == null) {
			throw new AppException("region is null");
		}
		if (StringUtil.isBlank(hostName)) {
			return null;
		}
		String key = region + "|" + hostName;
		CloudHostData cloudHostData = hostNameMap.get(key);
		if (cloudHostData == null) {
			return null;
		} else {
			return cloudHostData.clone();
		}
	}

	public synchronized CloudHostData removeByHostName(Integer region, String hostName) {
		logger.info("CloudHostPool.removeByHostName() > [" + Thread.currentThread().getId() + "] region:[" + region + "], hostName:[" + hostName + "]");
		if (region == null) {
			throw new AppException("region is null");
		}
		if (StringUtil.isBlank(hostName)) {
			return null;
		}
		String key = region + "|" + hostName;
		CloudHostData cloudHostData = hostNameMap.get(key);
		this.remove(cloudHostData);
		return cloudHostData;
	}

	public synchronized CloudHostData removeByRealHostId(String realHostId) {
		logger.info("CloudHostPool.removeByRealHostId() > [" + Thread.currentThread().getId() + "] realHostId:[" + realHostId + "]");
		if (StringUtil.isBlank(realHostId)) {
			return null;
		}
		CloudHostData cloudHostData = realHostIdMap.get(realHostId);
		this.remove(cloudHostData);
		return cloudHostData;
	}
}
