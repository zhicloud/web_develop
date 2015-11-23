package com.zhicloud.ms.app.cache;

import com.zhicloud.ms.vo.BoxRealInfoVO;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by sean on 11/16/15.
 */
public class BoxRealInfoCache {

    private Map<String, BoxRealInfoVO> cache;

    public BoxRealInfoCache() {
        cache = new Hashtable<String, BoxRealInfoVO>();
    }

    public BoxRealInfoVO[] getAll() {
        return this.cache.values().toArray(new BoxRealInfoVO[0]);
    }

    public void put(BoxRealInfoVO boxRealInfoVO) {
        String uesrId = boxRealInfoVO.getUserId();

        if (uesrId != null && uesrId.trim().length() != 0 ) {
            String key = uesrId;
            cache.put(key, boxRealInfoVO);
        }
    }

    public BoxRealInfoVO get(String uesrId) {
        if (uesrId != null && uesrId.trim().length() != 0) {
            String key = uesrId;
            return cache.get(key);
        }

        return null;
    }

    public void remove(BoxRealInfoVO boxRealInfoVO) {
        String uesrId = boxRealInfoVO.getUserId();

        if (uesrId != null && uesrId.trim().length() != 0 ) {
            String key = uesrId;
            cache.remove(key);
        }
    }

    public void removeAll() {
        this.cache.clear();
    }
}
