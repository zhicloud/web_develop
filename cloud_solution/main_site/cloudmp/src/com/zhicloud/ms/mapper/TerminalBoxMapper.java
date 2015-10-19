package com.zhicloud.ms.mapper;

import com.zhicloud.ms.vo.TerminalBoxVO;

import java.util.List;
import java.util.Map;

/**
 * Created by sean on 6/23/15.
 */
public interface TerminalBoxMapper {

    public List<TerminalBoxVO> queryAll();

    public List<TerminalBoxVO> queryAllWithConditions(Map<String, Object> condition);

    public TerminalBoxVO queryById(String id);

    public TerminalBoxVO queryByName(String name);

    public TerminalBoxVO queryBySerialNumber(String serialNumber);

    public Integer addTerminalBox(Map<String, Object> data);

    public Integer updateTerminalBoxById(Map<String, Object> condition);

    public Integer allocateTerminalBoxById(Map<String, Object> condition);

    public Integer deleteTerminalBoxByIds(String[] ids);
    /**
     * 
    * @Title: releaseTerminalBoxByUserId 
    * @Description: 根据用户id回收已分配的盒子 
    * @param @param userId
    * @param @return      
    * @return Integer     
    * @throws
     */
    public Integer releaseTerminalBoxByUserId(String userId);
    
    /**
     * 查询所有未绑定的盒子
     * @return
     */
    public List<TerminalBoxVO> getUnboundBox();
}
