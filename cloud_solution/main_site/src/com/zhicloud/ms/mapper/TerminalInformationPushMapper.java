package com.zhicloud.ms.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.ms.vo.TerminalInformationPushVO;

public interface TerminalInformationPushMapper {
    /**
     * 
    * @Title: queryInfomationByuserIdAndTime 
    * @Description: 根据群主id和时间查询应该推送的消息 
    * @param @param condition
    * @param @return      
    * @return List<TerminalInfomationPushVO>     
    * @throws
     */
    public List<TerminalInformationPushVO> queryInfomationBygroupIdAndTime(Map<String, Object> condition);
    /**
     * 
    * @Title: getAll 
    * @Description: 查询所有消息
    * @param @return      
    * @return List<TerminalInfomationPushVO>     
    * @throws
     */
    public List<TerminalInformationPushVO> getAll();
    /**
     * 
    * @Title: getAllByTime 
    * @Description: 根据时间获取数据
    * @param @param condition
    * @param @return      
    * @return List<TerminalInformationPushVO>     
    * @throws
     */
    public List<TerminalInformationPushVO> getAllByTime(Map<String, Object> condition);
    /**
     * 
    * @Title: deleteInfomationByIds 
    * @Description:根据ids删除消息
    * @param @param ids
    * @param @return      
    * @return Integer     
    * @throws
     */
    public Integer deleteInfomationByIds(String[] ids);
    /**
     * 
    * @Title: insertInfomation 
    * @Description: 新增推送消息 
    * @param @param condition
    * @param @return      
    * @return Integer     
    * @throws
     */
    public Integer insertInfomation(Map<String, Object> condition);

}
