package com.zhicloud.ms.app.pool.addressPool;

import java.util.Hashtable;
import java.util.Map;

public class AddressPoolManager {

	private static AddressPoolManager instance = null;
	private Map<String, AddressPool> addressPools = new Hashtable<String, AddressPool>();
	
	public synchronized static AddressPoolManager singleton() {
		if (AddressPoolManager.instance == null) {
			AddressPoolManager.instance = new AddressPoolManager();
		}

		return AddressPoolManager.instance;
	}
	
	public void put(String region,AddressPool addressPool) {

		if (region != null && region.trim().length() != 0) {
			addressPools.put(region, addressPool);
		}
	}

	public void remove(String region) {
		if (region != null && region.trim().length() != 0) {
			addressPools.remove(region);
		}
	}
	public AddressPool getPool(String region) {
		if (region != null && region.trim().length() != 0) {
			return addressPools.get(region);
		}else{
			return null;
		}
	}

}
