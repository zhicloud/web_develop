package com.zhicloud.ms.service;

import com.zhicloud.ms.vo.SetTimeOperationDetailVO;

import java.util.List;

/**
 * 
* @ClassName: SetTimeBackDetailService 
* @Description: 定时备份记录操作service 
* @author sasa
* @date 2015年7月30日 下午3:00:04 
*
 */
public interface ISetTimeOperationDetailService {
    
    /**
     * 
    * @Title: insertDetail 
    * @Description:新增历史记录 
    * @param @param realHostId
    * @param @param status
    *@param @param  type 记录类型  1： 开机  2： 关机
    * @param @return      
    * @return int     
    * @throws
     */
    public int insertDetail(String realHostId, Integer status, Integer type);
    /**
     * 
    * @Title: getAllDetail 
    * @Description: 获取所有的备份历史信息 
    * @param @return      
    * @return List<SetTimeOperationDetailVO>
    * @throws
     */
    public List<SetTimeOperationDetailVO> getAllDetail();


    /**
     * @function 计算定时开关机时间差
     * @param startTime
     * @param endTime
     * @return 定时开关机时间差
     */
    public Long getTimeInterval(String startTime, String endTime);

}
