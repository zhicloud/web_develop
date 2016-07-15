package com.zhicloud.ms.mapper;

import com.zhicloud.ms.vo.TimerInfoVO;

import java.util.Map;

/**
 * 
* @ClassName: TimerInfoMapper 
* @Description: 定时器信息mapper 
* @author sasa
* @date 2015年7月29日 下午3:00:53 
*
 */
public interface TimerInfoMapper {
    /**
     * 
    * @Title: queryByKey 
    * @Description: 根据KEY查询定时器信息
    * @param @param key
    * @param @return      
    * @return List<TimerInfoVO>     
    * @throws
     */
    public TimerInfoVO queryByKey(String key);
    /**
     * 
    * @Title: insertOrUpdate 
    * @Description: 新增或者更新
    * @param @param condition
    * @param @return      
    * @return int     
    * @throws
     */
    public int insertOrUpdate(Map<String,Object> condition);

}
