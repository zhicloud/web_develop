
package com.zhicloud.ms.transform.service;

import java.util.List;
import java.util.Map;

import com.zhicloud.ms.transform.vo.ManSystemLogVO;

/**
 * @ClassName: ManSysLogService
 * @Description: 日志管理接口 
 * @author 张本缘 于 2015年5月22日 上午9:53:36
 */
public interface ManSysLogService {

    /**
     * @Description:根据所有日志信息
     * @return List<ManSystemLogVO>
     */
    public List<ManSystemLogVO> getAll();

    /**
     * @Description:取得不同类型的日志
     * @param typeid
     * @return List<ManSystemLogVO>
     */
    public List<ManSystemLogVO> getAllByType(String typeid);
    
    /**
     * @Description:增加日志信息
     * @param parameter
     * @return
     */
    public String addSysLog(Map<String, Object> parameter);

}
