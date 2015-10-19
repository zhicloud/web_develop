package com.zhicloud.ms.service;


import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.vo.SmsConfigVO;

import java.util.List;
import java.util.Map;

/**
 * Created by sean on 7/6/15.
 */
public interface ISmsConfigService {

    public List<SmsConfigVO> getAllConfig();

    public SmsConfigVO getConfigById(String id);

    public MethodResult checkConfigName(String name);

    public MethodResult addConfig(Map<String, Object> parameter);

    public MethodResult modifyConfigById(Map<String, Object> parameter);

    public MethodResult removeConfigByIds(List<?> ids);
}
