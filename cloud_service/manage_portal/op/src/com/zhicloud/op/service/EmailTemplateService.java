package com.zhicloud.op.service;

import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.vo.EmailTemplateVO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Map;

/**
 * Created by sean on 7/6/15.
 */
public interface EmailTemplateService {

    public String managePage(HttpServletRequest request, HttpServletResponse response);

    public String addPage(HttpServletRequest request, HttpServletResponse response);

    public String modPage(HttpServletRequest request, HttpServletResponse response);

    public void getAllTemplate(HttpServletRequest request, HttpServletResponse response);

    public EmailTemplateVO getTemplateById(String id);

    public EmailTemplateVO getTemplateByCode(String code);

    public MethodResult checkTemplateName(String name);

    public MethodResult checkTemplateCode(String code);

    public MethodResult addTemplate(Map<String, String> parameter);

    public MethodResult updateTemplateById(Map<String, Object> parameter);

    public MethodResult deleteTemplateByIds(List<?> ids);

}
