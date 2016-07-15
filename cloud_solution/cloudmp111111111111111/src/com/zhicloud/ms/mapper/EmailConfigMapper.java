package com.zhicloud.ms.mapper;



import com.zhicloud.ms.vo.EmailConfigVO;

import java.util.List;
import java.util.Map;

/**
 * Created by sean on 7/6/15.
 */
public interface EmailConfigMapper {

    public List<EmailConfigVO> queryAllConfig();

    public List<EmailConfigVO> queryAllPage(Map<String, Object> condition);

    public int queryAllPageCount(Map<String, Object> condition);

    public EmailConfigVO queryConfigByName(String name);

    public EmailConfigVO queryConfigById(String id);

    public int insertConfig(Map<String, Object> data);

    public int updateConfig(Map<String, Object> data);

    public int deleteConfigByIds(String[] ids);

}
