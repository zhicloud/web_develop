package com.zhicloud.ms.service;


import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.vo.SmsTemplateVO;

import java.util.List;
import java.util.Map;

/**
 * Created by sean on 7/6/15.
 */
public interface ISmsTemplateService {


    public List<SmsTemplateVO> getAllTemplate();

    public SmsTemplateVO getTemplateById(String id);

    public SmsTemplateVO getTemplateByCode(String code);

    public MethodResult checkTemplateName(String name);

    public MethodResult checkTemplateCode(String code);

    public MethodResult addTemplate(Map<String, Object> parameter);

    public MethodResult modifyTemplateById(Map<String, Object> parameter);

    public MethodResult removeTemplateByIds(List<?> ids);

}
