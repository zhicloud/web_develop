package com.zhicloud.ms.app.pool.computePool;

/**
 * @description 计算资源池缓存池管理
 * @author 张翔
 */
public class ComputeInfoPoolManager {

    private static ComputeInfoPoolManager instance = null;
    private final ComputeInfoPool pool;

    private ComputeInfoPoolManager() {
        pool = new ComputeInfoPool();
    }

    public synchronized static ComputeInfoPoolManager singleton() {
        if (ComputeInfoPoolManager.instance == null) {
            ComputeInfoPoolManager.instance = new ComputeInfoPoolManager();
        }

        return ComputeInfoPoolManager.instance;
    }

    public ComputeInfoPool getPool() {
        return pool;
    }
}
