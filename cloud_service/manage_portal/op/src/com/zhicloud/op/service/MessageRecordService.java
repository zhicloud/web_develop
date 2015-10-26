package com.zhicloud.op.service;

import com.zhicloud.op.remote.MethodResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by sean on 7/6/15.
 */
public interface MessageRecordService {

    public String managePage(HttpServletRequest request, HttpServletResponse response);

    public void getAllRecord(HttpServletRequest request, HttpServletResponse response);

    public MethodResult addRecord(Map<String, Object> parameter);

    public MethodResult deleteRecordByIds(List<?> ids);
}
