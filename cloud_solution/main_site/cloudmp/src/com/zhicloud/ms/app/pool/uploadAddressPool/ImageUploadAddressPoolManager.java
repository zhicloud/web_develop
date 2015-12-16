package com.zhicloud.ms.app.pool.uploadAddressPool;

public class ImageUploadAddressPoolManager {

    private static ImageUploadAddressPoolManager instance = null;
//    private Map<String, ImageUploadAddressPool> addressPools = new Hashtable<String, ImageUploadAddressPool>();
    private ImageUploadAddressPool addressPool = new ImageUploadAddressPool();

    public synchronized static ImageUploadAddressPoolManager singleton() {
        if (ImageUploadAddressPoolManager.instance == null) {
            ImageUploadAddressPoolManager.instance = new ImageUploadAddressPoolManager();
        }

        return ImageUploadAddressPoolManager.instance;
    }

    public ImageUploadAddressPool getPool() {
        return  addressPool;
    }

//    public void put(String session,ImageUploadAddressPool addressPool) {
//
//        if (session != null && session.trim().length() != 0) {
//            addressPools.put(session, addressPool);
//        }
//    }
//
//    public void remove(String session) {
//        if (session != null && session.trim().length() != 0) {
//            addressPools.remove(session);
//        }
//    }
//
//    public ImageUploadAddressPool getPool(String session) {
//        if (session != null && session.trim().length() != 0) {
//            return addressPools.get(session);
//        }else{
//            return null;
//        }
//    }

}
