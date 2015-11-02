package com.zhicloud.ms.service.impl;

import com.zhicloud.ms.app.pool.serviceInfoPool.ServiceInfoExt;
import com.zhicloud.ms.app.pool.serviceInfoPool.ServiceInfoPool;
import com.zhicloud.ms.app.pool.serviceInfoPool.ServiceInfoPoolManager;
import com.zhicloud.ms.common.util.StringUtil;
import com.zhicloud.ms.httpGateway.HttpGatewayAsyncChannel;
import com.zhicloud.ms.httpGateway.HttpGatewayManager;
import com.zhicloud.ms.httpGateway.HttpGatewayResponseHelper;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.IPlatformResourceService;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.Map;

/**
 * Created by sean on 10/13/15.
 */
public class PlatformResourceServiceImpl implements IPlatformResourceService {

    private static final Logger logger = Logger.getLogger(ComputePoolServiceImpl.class);

    /**
     * @function　修改服务（异步）
     * @param type 服务类型
     * @param target 服务名
     * @param diskType 存储模式 0: 本地存储 2:共享存储
     * @param diskSource 存储源信息，比如共享路径
     * @param crypt 存储校验信息
     * @return
     */
    @Override public String modifyServiceAsync(int type, String target, int diskType, String diskSource,
        String crypt) throws IOException {
        ServiceInfoPool pool = ServiceInfoPoolManager.singleton().getPool();

        HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(1);
        JSONObject result = null;
        String sessionId = null;
        try {
            result = channel.serviceModify(type, target, diskType, diskSource, crypt);
            sessionId = channel.getSessionId();
        } catch (Exception e) {
            logger.error(String.format("fail to modify service. exception[%s]", e));
            return null;
        }

        if (HttpGatewayResponseHelper.isSuccess(result) == true) {
            ServiceInfoExt serviceInfo = new ServiceInfoExt();
            serviceInfo.setSessionId(sessionId);
            serviceInfo.setType(type);
            serviceInfo.setDiskType(diskType);
            serviceInfo.setDiskSource(diskSource);
            serviceInfo.setCrypt(crypt);
            pool.put(serviceInfo);
        }
        return sessionId;
    }

    /**
     * @function 修改服务（同步）
     * @param parameter
     * @return
     */
    @Override public MethodResult modifyServiceSync(Map<String, Object> parameter)
        throws IOException {

        int type = Integer.valueOf(StringUtil.trim(parameter.get("type")));
        String target  = StringUtil.trim(parameter.get("target"));
        int diskType =  Integer.valueOf(StringUtil.trim(parameter.get("disk_type")));
        String diskSource =  StringUtil.trim(parameter.get("disk_source"));
        String crypt = "";

        String sessionId = null;
        sessionId = this.modifyServiceAsync(type, target, diskType, diskSource, crypt);

        ServiceInfoExt serviceInfo = null;
        ServiceInfoPool pool = ServiceInfoPoolManager.singleton().getPool();


        if (!StringUtil.isBlank(sessionId)) {
            try {
                //获取对象
                serviceInfo = pool.get(sessionId);

                synchronized (serviceInfo) {//wait for 5 second or notify by response message.
                    serviceInfo.wait(5 * 1000);
                }
            } catch (InterruptedException e) {
                logger.error("error occur when modify compute pool response call back.", e);
                return new MethodResult(MethodResult.FAIL,"服务修改失败");
            }finally  {
                pool.remove(sessionId);
            }

            if (serviceInfo.isSuccess()) {//成功
                return new MethodResult(MethodResult.SUCCESS,"服务修改成功");
            } else {//失败或无应答
                return new MethodResult(MethodResult.FAIL,"服务修改失败");

            }
        }else{
            return new MethodResult(MethodResult.FAIL,"服务修改失败");
        }


    }
}
