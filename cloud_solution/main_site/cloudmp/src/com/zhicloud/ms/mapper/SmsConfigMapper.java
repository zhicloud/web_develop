package com.zhicloud.ms.mapper;



import com.zhicloud.ms.vo.SmsConfigVO;

import java.util.List;
import java.util.Map;

/**
 * Created by sean on 7/6/15.
 */
public interface SmsConfigMapper {

    public List<SmsConfigVO> queryAllConfig();

    public SmsConfigVO queryConfigByName(String name);

    public SmsConfigVO queryConfigById(String id);

    public int insertConfig(Map<String, Object> data);

    public int updateConfig(Map<String, Object> data);

    public int deleteConfigByIds(String[] ids);

}
