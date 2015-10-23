package com.zhicloud.ms.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.ms.vo.ResourceStatisticsVO;

public interface ResourceStatisticsMapper {
    
    /**
     * @Description:根据条件取得所有资源使用情况信息
     * @param map 参数
     * @return List<ResourceStatisticsVO>
     */
    public List<ResourceStatisticsVO> queryPage(Map<String, Object> map);
    
  
    /**
     * @Description:增加资源使用情况
     * @param map 参数
     * @return int
     */
    public int addResourceStatistics(Map<String,Object> map);
}
