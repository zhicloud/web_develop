package com.zhicloud.ms.app.pool.addressPool;

import java.util.ArrayList;
import java.util.List;

public class AddressPool {

	private List<AddressExt> pool;

	public AddressPool() {
		pool = new ArrayList<AddressExt>();
	}

	public List<AddressExt> getAll() {
		return pool;
	}
	public synchronized void add(AddressExt address){
		pool.add(address);
	}
	public List<AddressExt> getAllDuplication() {
		List<AddressExt> self = this.getAll();

		List<AddressExt> duplication = new ArrayList<AddressExt>();
		for (int i = 0; i < duplication.size(); i++) {
			duplication.add(self.get(i).clone());
		}

		return duplication;
	}

}
