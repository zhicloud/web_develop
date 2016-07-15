package com.zhicloud.ms.mapper;

import com.zhicloud.ms.vo.BackUpDetailVO;

import java.util.List;
import java.util.Map;

/**
 * 
* @ClassName: BackDetailMapper 
* @Description: 定时备份记录表
* @author sasa
* @date 2015年7月30日 下午2:27:28 
*
 */
public interface BackUpDetailMapper {
    /**
     * 
    * @Title: insertDetail 
    * @Description: 新增定时备份记录 
    * @param @param condition
    * @param @return      
    * @return int     
    * @throws
     */
    public int insertDetail(Map<String,Object> condition);
    /**
     * 
    * @Title: getAllDetail 
    * @Description: 获取所有的备份历史信息
    * @param type 记录类型 0：备份 1： 开机  2： 关机
    * @param @return      
    * @return List<SetTimeBackUpDeatilVO>     
    * @throws
     */
    public List<BackUpDetailVO> getAllDetail(Integer type);
    /**
     * 
    * @Title: updateDetailStatus 
    * @Description: 更新记录状态
    * @param @param condition
    * @param @return      
    * @return int     
    * @throws
     */
    public int updateDetailStatus(Map<String,Object> condition);
    /**
     * 
    * @Title: getLastAvailableBackUp 
    * @Description: 根据主机id查询最新可用备份
    * @param @param hostId
    * @param @return      
    * @return BackUpDetailVO     
    * @throws
     */
    public BackUpDetailVO getLastAvailableBackUp(String hostId);

}
