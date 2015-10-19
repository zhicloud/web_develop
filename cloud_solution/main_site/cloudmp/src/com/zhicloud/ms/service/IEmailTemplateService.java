package com.zhicloud.ms.service;


import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.vo.EmailTemplateVO;

import java.util.List;
import java.util.Map;

/**
 * @author 张翔
 * @function 邮件模板 Service
 */
public interface IEmailTemplateService {


    /**
     * @function 获取所有模板
     * @return
     */
    public List<EmailTemplateVO> getAllTemplate();

    /**
     * @function 按照 id 获取模板
     * @param id
     * @return
     */
    public EmailTemplateVO getTemplateById(String id);

    /**
     * @function 按照 code 获取模板
     * @param code
     * @return
     */
    public EmailTemplateVO getTemplateByCode(String code);

    /**
     * @function 检测模板名是否重名
     * @param name
     * @return
     */
    public MethodResult checkTemplateName(String name);

    /**
     * @function 检测 code 是否重名
     * @param code
     * @return
     */
    public MethodResult checkTemplateCode(String code);

    /**
     * @function 添加模板
     * @param parameter
     * @return
     */
    public MethodResult addTemplate(Map<String, Object> parameter);

    /**
     * 修改模板
     * @param parameter
     * @return
     */
    public MethodResult modifyTemplateById(Map<String, Object> parameter);

    /**
     * 删除模板
     * @param ids
     * @return
     */
    public MethodResult removeTemplateByIds(List<?> ids);

}
