package com.zhicloud.ms.mapper;

import com.zhicloud.ms.vo.SetTimeOperationDetailVO;

import java.util.List;
import java.util.Map;

/**
 * 
* @ClassName: SetTimeBackDetailMapper 
* @Description: 定时备份记录表
* @author sasa
* @date 2015年7月30日 下午2:27:28 
*
 */
public interface SetTimeOperationDetailMapper {
    /**
     * 
    * @Title: insertDetail 
    * @Description: 新增定时备份记录 
    * @param @param condition
    * @param @return      
    * @return int     
    * @throws
     */
    public int insertDetail(Map<String, Object> condition);
    /**
     * 
    * @Title: getAllDetail 
    * @Description: 获取所有的备份历史信息
    * @param @return
    * @return List<SetTimeBackUpDeatilVO>     
    * @throws
     */
    public List<SetTimeOperationDetailVO> getAllDetail();

}
