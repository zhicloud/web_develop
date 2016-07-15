package com.zhicloud.ms.service;

import com.zhicloud.ms.remote.MethodResult;

import java.io.IOException; 
import java.util.Map;

/**
 * Created by sean on 10/13/15.
 */
public interface IPlatformResourceService {

    /**
     *
     * @param type 服务类型
     * @param target 服务名
     * @param diskType 存储模式 0: 本地存储 1:共享存储
     * @param diskSource 存储源信息，比如共享路径
     * @param crypt 存储校验信息
     * @return
     */
    public String modifyServiceAsync(int type, String target, int diskType, String diskSource, String crypt)
        throws IOException;

    /**
     * @function 修改服务(同步)
     * @param parameter
     * @return
     */
    public MethodResult modifyServiceSync(Map<String, Object> parameter) throws IOException;

}
