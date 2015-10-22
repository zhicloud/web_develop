
package com.zhicloud.op.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.vo.MonitorServerVO;

/**
 * @ClassName: MonitorService
 * @Description: 监控接口
 * @author 张本缘 于 2015年6月24日 下午4:08:30
 */
public interface MonitorService {

    /**
     * @Description:返回区域页面
     * @param request
     * @param response
     * @return String
     */
    public String areaPage(HttpServletRequest request, HttpServletResponse response);

    /**
     * @Description:返回机房页面
     * @param request
     * @param response
     * @return String
     */
    public String roomPage(HttpServletRequest request, HttpServletResponse response);

    /**
     * @Description:返回机架页面
     * @param request
     * @param response
     * @return String
     */
    public String rackPage(HttpServletRequest request, HttpServletResponse response);

    /**
     * @Description:返回服务器页面
     * @param request
     * @param response
     * @return String
     */
    public String serverPage(HttpServletRequest request, HttpServletResponse response);

    /**
     * @Description:返回云主机页面
     * @param request
     * @param response
     * @return String
     */
    public String cloudHostPage(HttpServletRequest request, HttpServletResponse response);
    
    /**
     * @Description:返回资源管理页面
     * @param request
     * @param response
     * @return String
     */
    public String hostResourcePage(HttpServletRequest request, HttpServletResponse response);

    /**
     * @Description:查询区域监控信息
     * @param request
     * @param response
     */
    public void areaQuery(HttpServletRequest request, HttpServletResponse response);
    
    /**
     * @Description:查询机房监控信息
     * @param request
     * @param response
     */
    public void roomQuery(HttpServletRequest request, HttpServletResponse response);
    
    /**
     * @Description:查询机架监控信息
     * @param request
     * @param response
     */
    public void rackQuery(HttpServletRequest request, HttpServletResponse response); 

    /**
     * @Description:查询服务器监控信息
     * @param request
     * @param response
     */
    public void serverQuery(HttpServletRequest request, HttpServletResponse response);
    
    /**
     * @Description:查询云主机监控信息
     * @param request
     * @param response
     */
    public void hostQuery(HttpServletRequest request, HttpServletResponse response);

    /**
     * @Description:资源管理系统监控页面转
     * @param request
     * @param response
     * @return String
     */
    @Callable
    public String systemMonitorPage(HttpServletRequest request, HttpServletResponse response);

    /**
     * @Description:插入服务器临时数据到临时表
     * @param map
     */
    public void saveMonitorTempData(Map<String, Object> map);

    /**
     * @Description:保存服务器监控数据，清空临时表
     * @throws
     */
    public void saveMonitorData(String monitor_type,String server_type);
    
    /**
     * @Description:根据日期返回监控数据
     * @param insert_date
     * @return List<MonitorServerVO>
     */
    public List<MonitorServerVO> getDataByDay(Map<String, Object> map);
    
    /**
     * @Description:资源池查询
     * @param areaid
     */
    public JSONArray resourcePoolQuery(String areaid);
    
    /**
     * @Description:资源管理图表层跳转列表层
     * @param request
     * @param response
     * @return String
     */
    public String resourceToHostPage(HttpServletRequest request, HttpServletResponse response);
    
    /**
     * @Description:屏蔽和取消屏蔽
     * @param request
     * @param response
     */
    public MethodResult updateShield(String type,String shield,String uuid);
    
    /**
     * Description:查看详情页面跳转
     * @param request
     * @param response
     * @return String
     */
    public String viewDetailPage(HttpServletRequest request, HttpServletResponse response);
    
    /**
     * @Description:保存
     * @param map
     */
    public void saveShieldData(Map<String, Object> map);
    
    /**
     * @Description:初始化所有屏蔽数据到内存常量
     */
    public void initMonitorShield();
}
