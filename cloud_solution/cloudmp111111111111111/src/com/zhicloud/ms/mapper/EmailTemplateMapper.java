package com.zhicloud.ms.mapper;



import com.zhicloud.ms.vo.EmailTemplateVO;

import java.util.List;
import java.util.Map;

/**
 * Created by sean on 7/6/15.
 */
public interface EmailTemplateMapper {

    public List<EmailTemplateVO> queryAllTemplate();

    public EmailTemplateVO queryTemplateById(String id);

    public EmailTemplateVO queryTemplateByName(String name);

    public EmailTemplateVO queryTemplateByCode(String code);

    public int insertTemplate(Map<String, Object> data);

    public int updateTemplate(Map<String, Object> data);

    public int deleteTemplateByIds(String[] ids);

}
