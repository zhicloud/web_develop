package com.zhicloud.ms.service;

import java.util.List;

import com.zhicloud.ms.vo.ServerComponentVO;

public interface SysmanageService {
    /**
     * 更新系统时间
     * @param netDatetime
     * @return
     */
    public boolean updateDatetime(String netDatetime);
    /**
     * 更新主机名
     * @param newHostname
     * @return
     */
    public boolean updateHostName(String newHostname);
    /**
     * 关闭服务器
     * @return
     */
    public boolean shutdownServer();
    /**
     * 重启服务器
     * @return
     */
    public boolean rebootServer();
    /**
     * 重启tomcat
     * @return
     */
    public boolean rebootTomcat();
    /**
     * 重启组件服务
     * @param componentName
     * @return
     */
    public boolean restartComponent(String componentName);
    
    /**
     * 取组件列表
     * @return
     */
    public List<ServerComponentVO> getComponentList();
}
