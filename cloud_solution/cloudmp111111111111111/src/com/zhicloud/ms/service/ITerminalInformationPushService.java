package com.zhicloud.ms.service;

import java.util.List; 

import com.zhicloud.ms.vo.TerminalInformationPushVO;

/**
 * 
* @ClassName: TerminalInfomationPushService 
* @Description:  
* @author sasa
* @date 2015年9月1日 上午10:01:33 
*
 */
public interface ITerminalInformationPushService {
    /**
     * 
    * @Title: queryAll 
    * @Description: 查询所有的消息 
    * @param @return      
    * @return List<TerminalInfomationPushVO>     
    * @throws
     */
    public List<TerminalInformationPushVO> queryAll();
    /**
     * 
    * @Title: queryAllByTime 
    * @Description: 根据不同的时间查询 不同的数据
    * @param @param time
    * @param @return      
    * @return List<TerminalInformationPushVO>     
    * @throws
     */
    public List<TerminalInformationPushVO> queryAllByTime(String time);
    /**
     * 
    * @Title: queryInfomationByGroupIdAndTime 
    * @Description: 
    * @param @return      
    * @return List<TerminalInformationPushVO>     
    * @throws
     */
    public List<TerminalInformationPushVO> queryInfomationByCondition(String groupId, String time, String region,
            String industry);

    /**
     * 
    * @Title: deleteInformation 
    * @Description: 批量删除信息 
    * @param @param ids
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    public Integer deleteInformation(List<String> ids);
    /**
     * 
    * @Title: addInfomation 
    * @Description: 新增信息
    * @param @param vo
    * @param @return      
    * @return Integer     
    * @throws
     */
    public Integer addInfomation(TerminalInformationPushVO vo);
    
}
