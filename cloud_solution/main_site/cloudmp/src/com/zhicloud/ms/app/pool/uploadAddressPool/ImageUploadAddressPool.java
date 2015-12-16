package com.zhicloud.ms.app.pool.uploadAddressPool;

import com.zhicloud.ms.vo.ImageUploadAddressVO;

import java.util.*;

public class ImageUploadAddressPool {

    private List<ImageUploadAddressVO> pool;

    public ImageUploadAddressPool() {
        pool = new ArrayList<>();
    }

    public List<ImageUploadAddressVO> getAll() {
        return pool;
    }
    public synchronized void add(ImageUploadAddressVO address){
        pool.add(address);
    }

}
