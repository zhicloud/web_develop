package com.zhicloud.ms.app.cache.address;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by sean on 2/25/16.
 */
public class AddressCache {

    private Map<String, AddressVO> cache;

    public AddressCache() {
        cache = new Hashtable<>();
    }

    public AddressVO[] getAll() {

        return this.cache.values().toArray(new AddressVO[0]);
    }

    public AddressVO get(String id) {

        if (id != null && id.trim().length() != 0) {
            String key = id;
            return cache.get(key);
        }

        return null;
    }

    public void put(AddressVO addressVO) {

        String fileSystem = addressVO.getFileSystem();

        if (fileSystem != null && fileSystem.trim().length() != 0 ) {
            String key = fileSystem;
            cache.put(key, addressVO);
        }
    }

    public void remove(String key) {

        cache.remove(key);
    }

    public void removeAll(){

        cache.clear();
    }

}
