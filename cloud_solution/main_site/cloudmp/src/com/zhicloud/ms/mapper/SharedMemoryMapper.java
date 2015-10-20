
package com.zhicloud.ms.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.ms.vo.SharedMemoryVO;

/**
 * @ClassName: SharedMemoryMapper
 * @Description: 共享存储mapper
 * @author 张本缘 于 2015年10月10日 上午10:05:30
 */
public interface SharedMemoryMapper {

    /**
     * @Description:根据条件查询共享存储列表信息
     * @param condition 条件
     * @return List<SharedMemoryVO>
     */
    public List<SharedMemoryVO> queryInfo(Map<String, Object> condition);

    /**
     * @Description:增加共享存储信息
     * @param condition 条件
     * @return int
     */
    public int addInfo(Map<String, Object> data);

    /**
     * @Description:修改共享存储信息
     * @param condition 条件
     * @return int
     */
    public int updateInfo(Map<String, Object> data);

    /**
     * @Description:删除共享存储信息
     * @param ids 主键数组
     * @return int
     */
    public int deleteInfo(String[] ids);
    
    /**
     * 修改可用状态
     * @param condition
     * @return
     */
    public int updateAvailable(String id);
    
    /**
     * 将当前可用的改为不可用
     * @return
     */
    public int setDisable();

}
