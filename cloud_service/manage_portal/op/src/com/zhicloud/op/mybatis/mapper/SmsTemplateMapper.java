package com.zhicloud.op.mybatis.mapper;


import com.zhicloud.op.vo.SmsTemplateVO;

import java.util.List;
import java.util.Map;

/**
 * Created by sean on 7/6/15.
 */
public interface SmsTemplateMapper {

    public List<SmsTemplateVO> queryAllPage(Map<String, Object> condition);

    public int queryAllPageCount(Map<String, Object> condition);

    public SmsTemplateVO queryTemplateById(String id);

    public SmsTemplateVO queryTemplateByName(String name);

    public SmsTemplateVO queryTemplateByCode(String code);

    public int insertTemplate(Map<String, Object> data);

    public int updateTemplate(Map<String, Object> data);

    public int deleteTemplateByIds(String[] ids);

}
