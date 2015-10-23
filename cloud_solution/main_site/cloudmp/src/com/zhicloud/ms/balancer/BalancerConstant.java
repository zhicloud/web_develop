package com.zhicloud.ms.balancer;

/**
 * @author 梁绍辉
 * @function 负载均衡器方法常量定义
 *
 */
public class BalancerConstant {
    // 统计
    public static final String statistics_query = "query";  // 统计
    // 服务
    public static final String summary_query    = "query";   // 查询概要
    public static final String service_start    = "start";   // 启动服务
    public static final String service_stop     = "stop";    // 停止服务
    public static final String service_restart  = "restart"; // 重启服务
    // 监听端口
    public static final String listen_port_query_all = "query_all"; // 监听端口列表
    public static final String listen_port_query   = "query";    // 查看监听端口
    public static final String listen_port_create  = "create";   // 创建监听端口
    public static final String listen_port_modify  = "modify";   // 修改监听端口    
    public static final String listen_port_start   = "start";    // 启动监听端口
    public static final String listen_port_stop    = "stop";     // 停止监听端口
    public static final String listen_port_remove  = "remove";   // 删除监听端口
    // 转发端口
    public static final String forward_port_query_all = "query_all"; // 转发端口列表
    public static final String forward_port_query_all_id = "query_all"; // 转发端口列表
    public static final String forward_port_query   = "query";   // 查看转发端口
    public static final String forward_port_add     = "add";     // 添加转发端口
    public static final String forward_port_modify  = "modify";  // 修改转发端口    
    public static final String forward_port_start   = "start";   // 启动转发端口
    public static final String forward_port_stop    = "stop";    // 停止转发端口
    public static final String forward_port_remove  = "remove";  // 释放转发端口
    // 证书文件
    public static final String certificte_upload   = "upload";    // 上传证书
    public static final String certificte_query    = "query";     // 证书列表
    public static final String certificte_remove   = "remove";    // 上传证书
}
