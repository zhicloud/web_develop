package com.zhicloud.ms.app.pool.box;

import com.zhicloud.ms.vo.BoxRealInfoVO;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by sean on 11/16/15.
 */
public class BoxRealInfoPool {

    private Map<String, BoxRealInfoVO> pool;

    public BoxRealInfoPool() {
        pool = new Hashtable<String, BoxRealInfoVO>();
    }

    public BoxRealInfoVO[] getAll() {
        return this.pool.values().toArray(new BoxRealInfoVO[0]);
    }

    public void put(BoxRealInfoVO boxRealInfoVO) {
        String mac = boxRealInfoVO.getMac();

        if (mac != null && mac.trim().length() != 0 ) {
            String key = mac;
            pool.put(key, boxRealInfoVO);
        }
    }

    public BoxRealInfoVO get(String mac) {
        if (mac != null && mac.trim().length() != 0) {
            String key = mac;
            return pool.get(key);
        }

        return null;
    }

    public void remove(BoxRealInfoVO boxRealInfoVO) {
        String mac = boxRealInfoVO.getMac();

        if (mac != null && mac.trim().length() != 0 ) {
            String key = mac;
            pool.remove(key);
        }
    }
}
