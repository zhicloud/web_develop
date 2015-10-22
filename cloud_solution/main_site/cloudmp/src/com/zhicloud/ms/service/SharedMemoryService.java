
package com.zhicloud.ms.service;

import java.util.List;
import java.util.Map;

import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.vo.SharedMemoryVO;

/**
 * @ClassName: SharedMemoryService
 * @Description: 共享存储接口定义
 * @author 张本缘 于 2015年10月10日 上午9:58:51
 */
public interface SharedMemoryService {

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
     * @Description:取得单个VO对象
     * @param id 主键
     * @return SharedMemoryVO
     */
    public SharedMemoryVO getVO(String id);
    
    /**
     * @Description:验证名称是否重复
     * @param condition 参数
     * @return boolean (true 重复,false 不重复)
     */
    public boolean validateName(Map<String, Object> condition);
    
    /**
     * 设置为可用
     * @param id
     * @return
     */
    public MethodResult setAvailable(String id);
    
    /**
     * 查询可用的路径
     * @return
     */
    public SharedMemoryVO queryAvailable();
}
