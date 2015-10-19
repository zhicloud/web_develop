package com.zhicloud.ms.transform.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.ms.transform.vo.ManSystemLogVO;



/**
 * @ClassName: SystemLogMapper
 * @Description: 系统日志ibatis映射
 * @author 张本缘 于 2015年5月5日 上午11:34:46
 */
public interface ManSystemLogMapper {
    

    /**
     * @Description:新增日志信息
     * @param condition 日志信息
     */
    public int addSystemLog(Map<String, Object> data);
    
    /**
     * @Description:取得所有日志信息
     * @return List<ManSystemLogVO>
     */
    public List<ManSystemLogVO> getAll();
    
    /**
     * @Description:取得不同类型日志信息
     * @return List<ManSystemLogVO>
     */
    public List<ManSystemLogVO> getAllByType(Integer type); 
    
    /**
     * @Description:清除日志信息
     * @return
     */
    public int deleteLogsWithoutDays(Integer days);
    
}
