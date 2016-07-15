package com.zhicloud.ms.service;

import java.util.Map;

import com.zhicloud.ms.remote.MethodResult;

public interface IHostTimerRelationship {
    
    public MethodResult updateRunningStatusByRealHostId(Map<String, Object> cloudHostData);

}
