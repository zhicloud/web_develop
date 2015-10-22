package com.zhicloud.ms.app.pool.rule;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class RuleInfoPool {

	private Map<String, List<RuleInfo>> pool;

	public RuleInfoPool() {
		pool = new Hashtable<String, List<RuleInfo>>();
	}

	public RuleInfo[] getAll() {
		return pool.values().toArray(new RuleInfo[0]);
	}

	public RuleInfo[] getALlDuplication() {
		RuleInfo[] self = this.getAll();

		RuleInfo[] duplication = new RuleInfo[self.length];
		for (int i = 0; i < self.length; i++) {
			duplication[i] = self[i];
		}

		return duplication;
	}

	public List<RuleInfo> get(String uuid) {
		if (uuid != null && uuid.trim().length() != 0) {
			return pool.get(uuid);
		}
		
		return null;
	}
	
	public List<RuleInfo> getDuplication(String uuid) {
		List<RuleInfo> self = this.get(uuid);
		
		if (self != null) {
			List<RuleInfo> cloneList = new ArrayList<RuleInfo>();
			for(int i=0;i<self.size();i++){
				cloneList.add(self.get(i).clone());
			}
			return cloneList;
		}
		
		return null;
	}
	
	public void put(List<RuleInfo> rule) {  
		pool.put(rule.get(0).getSessionId(), rule);
 	}
	
	public void remove(RuleInfo rule) {
		String uuid = rule.getTarget();
		
		if (uuid != null && uuid.trim().length() != 0) {
			pool.remove(uuid);
		}
	}

}
