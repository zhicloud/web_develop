package com.zhicloud.ms.service;

import com.zhicloud.ms.app.pool.computePool.ComputeInfoExt;
import com.zhicloud.ms.remote.MethodResult;

import java.io.IOException;
import java.util.Map;

/**
 * @计算资源池service
 * @author 张翔
 */
public interface IComputePoolService {


    /**
     * @function 获取计算资源池详情（异步）
     * @param uuid
     * @return
     */
    public Boolean getComputePoolDetailAsync(String uuid) throws IOException;

    /**
     * @function 获取计算资源池详情（同步）
     * @param uuid
     * @return
     */
    public ComputeInfoExt getComputePoolDetailSync(String uuid) throws IOException;


    /**
     * @function 创建资源池(异步)
     * @param name
     * @param networkType
     * @param network
     * @param diskType
     * @param diskSource
     * @return
     * @throws IOException
     */
    public String createComputePoolAsync(String name, int networkType, String network, int diskType, String diskSource, Integer[] mode, String path, String crypt) throws IOException;

    /**
     * @function 创建资源池(同步)
     * @param parameter
     * @return
     */
    public MethodResult createComputePoolSync(Map<String, Object> parameter) throws IOException;

    /**
     * @function 修改资源池(异步)
     * @param uuid
     * @param name
     * @param networkType
     * @param network
     * @param diskType
     * @param diskSource
     * @param option
     * @return
     * @throws IOException
     */
    public String modifyComputePoolAsync(String uuid, String name, int networkType, String network, int diskType, String diskSource, Integer[] mode, int option,String path,String crypt) throws IOException;

    /**
     * @function 修改资源池(同步)
     * @param parameter
     * @return
     */
    public MethodResult modifyComputePoolSync(Map<String, Object> parameter) throws IOException;

    /**
     * 删除资源池
     * @param uuid
     * @return
     */
    public MethodResult removeComputePool(String uuid);

}
