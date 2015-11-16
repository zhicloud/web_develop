package com.zhicloud.ms.mapper;

import com.zhicloud.ms.vo.QosVO;

import java.util.List;
import java.util.Map;

/**
 * Created by sean on 6/23/15.
 */
public interface QosMapper {

    public List<QosVO> queryAll(int type);

    public QosVO queryByName(Map<String, Object> condition);

    public Integer addQos(Map<String, Object> data);

    public Integer deleteQosByHostUuids(String[] hostIds);


}
