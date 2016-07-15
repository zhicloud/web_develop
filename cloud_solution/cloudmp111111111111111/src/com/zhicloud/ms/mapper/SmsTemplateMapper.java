package com.zhicloud.ms.mapper;



import com.zhicloud.ms.vo.SmsTemplateVO;

import java.util.List;
import java.util.Map;

/**
 * Created by sean on 7/6/15.
 */
public interface SmsTemplateMapper {

    public List<SmsTemplateVO> queryAllTemplate();

    public SmsTemplateVO queryTemplateById(String id);

    public SmsTemplateVO queryTemplateByName(String name);

    public SmsTemplateVO queryTemplateByCode(String code);

    public int insertTemplate(Map<String, Object> data);

    public int updateTemplate(Map<String, Object> data);

    public int deleteTemplateByIds(String[] ids);

}
