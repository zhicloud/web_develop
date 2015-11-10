package com.zhicloud.ms.app.pool.computePool;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @description 计算资源池缓存池
 * @author 张翔
 */
public class ComputeInfoPool {

    private Map<String, ComputeInfoExt> pool;

    public ComputeInfoPool() {
        pool = new Hashtable<String, ComputeInfoExt>();
    }


    public void put(String key, ComputeInfoExt computeInfoExt) {
        if (key != null && key.trim().length() != 0 ) {
            pool.put(key, computeInfoExt);
        }
    }

    public ComputeInfoExt get(String key) {
        if (key != null && key.trim().length() != 0) {
            return pool.get(key);
        }

        return null;
    }

    public void remove(ComputeInfo computeInfo) {
        String uuid = computeInfo.getUuid();

        if (uuid != null && uuid.trim().length() != 0 ) {
            String key = uuid;
            pool.remove(key);
        }
    }
    public ComputeInfoExt getDuplication(String uuid) {
        ComputeInfoExt self = this.get(uuid);

        return self.clone();
    }
    
    public List<ComputeInfoExt> getAll(){
    	List<ComputeInfoExt> poolList = new ArrayList<>();
    	Iterator<ComputeInfoExt> i = pool.values().iterator();
    	while(i.hasNext()){
    		poolList.add(i.next());
    	}
    	return poolList;
    }
}
