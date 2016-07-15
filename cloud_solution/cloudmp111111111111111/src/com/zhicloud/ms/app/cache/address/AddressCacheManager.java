package com.zhicloud.ms.app.cache.address;

/**
 * Created by sean on 2/25/16.
 */
public class AddressCacheManager {

    private static AddressCacheManager instance = null;
    private final AddressCache cache;


    // 单例
    public synchronized static AddressCacheManager singleton() {

        if (AddressCacheManager.instance == null) {
            AddressCacheManager.instance = new AddressCacheManager();
        }

        return instance;

    }

    private AddressCacheManager() {
        cache = new AddressCache();
    }

    public AddressCache getCache() {
        return cache;
    }
}
