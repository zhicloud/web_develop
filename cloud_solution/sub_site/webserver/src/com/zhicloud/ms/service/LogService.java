package com.zhicloud.ms.service;

import java.util.List;
import java.util.Map;

import com.zhicloud.ms.vo.LogVO;

public interface LogService {
    
    /**
     * @Description:根据条件取得所有日志信息
     * @param map 参数
     * @return List<LogVO>
     */
    public List<LogVO> queryPage(Map<String, Object> map);
    
    /**
     * @Description:根据条件获取日志记录的数量
     * @param map 参数
     * @return int
     */
    public int queryPageCount(Map<String, Object> map);
    
    /**
     * @Description:增加日志信息
     * @param map 参数
     */
    public int saveLog(Map<String, Object> map);
    
    /**
     * @Description:增加日志信息
     * @param actiondesc 操作描述
     * @param userid 用户id
     * @return int
     */
    public int addLog(String actiondesc, String userid);
}
