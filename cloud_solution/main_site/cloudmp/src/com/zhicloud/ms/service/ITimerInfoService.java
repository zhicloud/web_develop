package com.zhicloud.ms.service;

import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.vo.TimerInfoVO;
/**
 * 
* @ClassName: ITimerInfoService 
* @Description: 定时器信息操作 
* @author sasa
* @date 2015年7月28日 下午5:51:52 
*
 */ 
public interface ITimerInfoService {
    /**
     * 
    * @Title: queryTimerInfoByKey 
    * @Description: 根据KEY查询定时器信息（针对只有单个的定时器） 
    * @param @param key
    * @param @return      
    * @return TimerInfoVO     
    * @throws
     */
    public TimerInfoVO queryTimerInfoByKey(String key);
    /**
     * 
    * @Title: insertTimerInfo 
    * @Description: 新增定时器
    * @param @param timer
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    public MethodResult insertOrUpdateBackUpTimerInfo(TimerInfoVO timer);

    /**
     * @function 新增定时器
     * @author 张翔
     * @param timer
     * @return
     */
    public MethodResult insertOrUpdateTimerInfo(TimerInfoVO timer);

}
