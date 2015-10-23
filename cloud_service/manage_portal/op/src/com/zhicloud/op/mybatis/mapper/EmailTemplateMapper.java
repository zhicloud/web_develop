package com.zhicloud.op.mybatis.mapper;


import com.zhicloud.op.vo.EmailTemplateVO;

import java.util.List;
import java.util.Map;

/**
 * Created by sean on 7/6/15.
 */
public interface EmailTemplateMapper {

    public List<EmailTemplateVO> queryAllPage(Map<String, Object> condition);

    public int queryAllPageCount(Map<String, Object> condition);

    public EmailTemplateVO queryTemplateById(String id);

    public EmailTemplateVO queryTemplateByName(String name);

    public EmailTemplateVO queryTemplateByCode(String code);

    public int insertTemplate(Map<String, Object> data);

    public int updateTemplate(Map<String, Object> data);

    public int deleteTemplateByIds(String[] ids);

}
