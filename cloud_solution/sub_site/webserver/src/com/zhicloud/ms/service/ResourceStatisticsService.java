package com.zhicloud.ms.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.ms.vo.ResourceStatisticsVO;


public interface ResourceStatisticsService {    
    /**
     * @Description:添加状态信息
     * @param map 参数
     * @return List<LogVO>
     */
    public int addResourceStatistics(Map<String, Object> map);    
    
    /**
     * @Description:根据条件取得所有状态信息
     * @param map 参数
     * @return List<LogVO>
     */
    public List<ResourceStatisticsVO> queryPage(Map<String, Object> map);
}
