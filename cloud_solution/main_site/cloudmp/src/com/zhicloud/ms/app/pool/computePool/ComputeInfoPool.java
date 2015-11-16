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
    
    private Map<String, ComputeInfoExt> computePool; // key值是资源池的uuid

    public ComputeInfoPool() {
        pool = new Hashtable<String, ComputeInfoExt>();
        computePool = new Hashtable<String, ComputeInfoExt>();
    }


    public void put(String key, ComputeInfoExt computeInfoExt) {
        if (key != null && key.trim().length() != 0 ) {
            pool.put(key, computeInfoExt);
        }
    }
    
    public void putToComputePool(String key, ComputeInfoExt computeInfoExt) {
        if (key != null && key.trim().length() != 0 ) {
            computePool.put(key, computeInfoExt);
        }
    }

    public ComputeInfoExt get(String key) {
        if (key != null && key.trim().length() != 0) {
            return pool.get(key);
        }

        return null;
    }
    public ComputeInfoExt getFromComputePool(String key) {
        if (key != null && key.trim().length() != 0) {
            return computePool.get(key);
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
    public void removeFromComputePool(ComputeInfo computeInfo) {
        String uuid = computeInfo.getUuid();

        if (uuid != null && uuid.trim().length() != 0 ) {
            String key = uuid;
            computePool.remove(key);
        }
    }
    public ComputeInfoExt getDuplication(String uuid) {
        ComputeInfoExt self = this.get(uuid);

        return self.clone();
    }
    
     public ComputeInfoExt getDuplicationFromComputePool(String uuid) {
        ComputeInfoExt self = this.getFromComputePool(uuid);
        return self.clone();
    }
    public void clearComputePool(){
        this.computePool.clear();
    }

    public Map<String, ComputeInfoExt> getAllComputePool(){
        return this.computePool;
    }
    public void setComputePool(Map<String, ComputeInfoExt> computePool){
        this.computePool = computePool;
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
