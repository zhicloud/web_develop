
package com.zhicloud.op.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.MonitorServerVO;

/**
 * @ClassName: MonitorServerMapper
 * @Description: 服务器监控映射
 * @author 张本缘 于 2015年7月2日 上午11:24:12
 */
public interface MonitorServerMapper {
    /**
     * 查询数据条数
     * @param condition
     * @return
     */
    public int queryPageCount(Map<String, Object> condition);

    /**
     * 根据日期查询数据
     * @param condition
     * @return
     */
    public List<MonitorServerVO> getDataByDay(Map<String, Object> map);

    /**
     * 查询所有数据
     * @param condition
     * @return
     */
    public List<MonitorServerVO> queryPage(Map<String, Object> condition);
    
    /**
     * 查询临时表的平均值
     * @param condition
     * @return
     */
    public List<MonitorServerVO> queryAverage(Map<String, Object> condition);

    /**
     * 添加服务器监控信息
     * @param data
     * @return
     */
    public int addMonitor(Map<String, Object> data);

    /**
     * 添加服务器临时监控信息
     * @param data
     * @return
     */
    public int addMonitorTemp(Map<String, Object> data);

    /**
     * 删除服务器监控信息
     * @param data
     * @return
     */
    public int deleteMonitors(Map<String, Object> data);

    /**
     * 删除服务器临时监控信息
     * @param data
     * @return
     */
    public int deleteMonitorTemp(Map<String, Object> data);
    
    /**
     * @Description:增加对象id和屏蔽的关系
     * @param data
     * @return int
     */
    public int addShield(Map<String, Object> data);

    /**
     * @Description:更新对象id和屏蔽的关系
     * @param data
     * @return int
     */
    public int updateShield(Map<String, Object> data);
    
    /**
     * @Description:根据ID查询监控对象
     * @param id
     * @return MonitorServerVO
     */
    public MonitorServerVO queryShieldByID(String id);
    
    /**
     * @Description:查询所有对象id和屏蔽的对应关系
     * @return List<MonitorServerVO>
     */
    public List<MonitorServerVO> queryShield();

}
