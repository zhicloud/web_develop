package com.zhicloud.op.service;

import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.vo.SmsConfigVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by sean on 7/6/15.
 */
public interface SmsConfigService {

    public String managePage(HttpServletRequest request, HttpServletResponse response);

    public String addPage(HttpServletRequest request, HttpServletResponse response);

    public String modPage(HttpServletRequest request, HttpServletResponse response);

    public void getAllConfig(HttpServletRequest request, HttpServletResponse response);

    public SmsConfigVO getConfigById(String id);

    public MethodResult checkConfigName(String name);

    public MethodResult addConfig(Map<String, String> parameter);

    public MethodResult updateConfigById(Map<String, Object> parameter);

    public MethodResult deleteConfigByIds(List<?> ids);
}
