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
    public static final String listen_port_update_all   = "update_all";    // 更新监听端口配置
    // 证书文件
    public static final String certificte_upload   = "upload";    // 上传证书
    public static final String certificte_query    = "query";     // 证书列表
    public static final String certificte_remove   = "remove";    // 上传证书
    
    // 错误信息对应表
    public static final String err_execute_exception   = "执行异常，具体异常查看log";      // -1
    public static final String err_request_error       = "未知请求，xml中的ns:XXXXXXXXX请求错误";   // 100
    public static final String err_json_format_err     = "请求格式无效，json数据格式有错误";          // 200
    public static final String err_json_lost_element   = "请求json数据中，缺少必要元素";             // 201 
    public static final String err_json_element_format = "请求json数据中，元素的格式无效";            // 202
    public static final String err_flush_request_fail  = "刷新请求失败";                      // 300
    public static final String err_health_check_fail   = "获取转发端口状态（健康检查的结果）失败";     // 301
}
