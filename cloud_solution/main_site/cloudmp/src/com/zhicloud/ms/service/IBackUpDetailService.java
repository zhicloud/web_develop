package com.zhicloud.ms.service;

import com.zhicloud.ms.vo.BackUpDetailVO;

import java.util.List;
/**
 * 
* @ClassName: SetTimeBackDetailService 
* @Description: 定时备份记录操作service 
* @author sasa
* @date 2015年7月30日 下午3:00:04 
*
 */
public interface IBackUpDetailService {
    
    /**
     * 
    * @Title: insertDetail 
    * @Description:新增历史记录 
    * @param @param realHostId
    * @param @param status
    *@param @param  type 记录类型 0：备份 1： 开机  2： 关机
    * @param @return      
    * @return int     
    * @throws
     */
    public int insertDetail(String realHostId,Integer status, Integer type,Integer mode,Integer disk);
    /**
     * 
    * @Title: getAllDetail 
    * @Description: 获取所有的备份历史信息 
    * @param @return      
    * @return List<SetTimeBackUpDeatilVO>     
    * @throws
     */
    public List<BackUpDetailVO> getAllDetail(Integer type);
    /**
     * 
    * @Title: updateDetail 
    * @Description: 修改记录信息 
    * @param @param realHostId
    * @param @param type
    * @param @param oldstatus
    * @param @param newstatus
    * @param @return      
    * @return int     
    * @throws
     */
    public int updateDetail(String realHostId, Integer oldstatus,Integer newstatus);
    /**
     * 
    * @Title: getLastAvailableBackUp 
    * @Description: 根据主机id查询可用的备份信息 
    * @param @param hostId
    * @param @return      
    * @return BackUpDetailVO     
    * @throws
     */
    public BackUpDetailVO getLastAvailableBackUp(String hostId);

}
