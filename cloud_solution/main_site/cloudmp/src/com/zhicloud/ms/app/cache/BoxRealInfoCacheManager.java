package com.zhicloud.ms.app.cache;


/**
 * Created by sean on 11/16/15.
 */
public class BoxRealInfoCacheManager {

    private static BoxRealInfoCacheManager instance = null;
    private final BoxRealInfoCache cache;


    // 单例
    public synchronized static BoxRealInfoCacheManager singleton() {

        if (BoxRealInfoCacheManager.instance == null) {
            BoxRealInfoCacheManager.instance = new BoxRealInfoCacheManager();
        }

        return instance;

    }

    private BoxRealInfoCacheManager() {
        cache = new BoxRealInfoCache();
    }

    public BoxRealInfoCache getCache() {
        return cache;
    }

}
