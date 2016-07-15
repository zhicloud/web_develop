
package com.zhicloud.ms.constant;

import net.sf.json.JSONArray;

/**
 * @ClassName: StaticReportConstant
 * @Description: 统计报表存放数据常量(为了方便导出)
 * @author 张本缘 于 2015年10月16日 上午11:36:27
 */
public class StaticReportConstant {
    // 云主机概览 cpu时长 标示符
    public static String cscpu = "cscpu";
    // 云主机概览 磁盘读写 标示符
    public static String cswrite = "cswrite";
    // 云主机明细 服务状态 标示符
    public static String cdserver = "cdserver";
    // 云主机明细 cpu时长 标示符
    public static String cdonline = "cdonline";
    // 云主机明细 内存和磁盘 标示符
    public static String cdmemory = "cdmemory";
    // 云主机明细 内存和磁盘 标示符
    public static String cdwrite = "cdwrite";
    // 云主机明细 接收 标示符
    public static String cdreceive = "cdreceive";
    // 云主机明细 接收 标示符
    public static String cdsent = "cdsent";
    
    // 服务器概览 cpu时长 标示符
    public static String sscpu = "sscpu";
    // 服务器概览 磁盘读写 标示符
    public static String sswrite = "sswrite";
    // 服务器明细 服务状态 标示符
    public static String sdserver = "sdserver";
    // 服务器明细 cpu时长 标示符
    public static String sdonline = "sdonline";
    // 服务器明细 内存和磁盘 标示符
    public static String sdmemory = "sdmemory";
    // 服务器明细 内存和磁盘 标示符
    public static String sdwrite = "sdwrite";
    // 服务器明细 接收 标示符
    public static String sdreceive = "sdreceive";
    // 服务器明细 接收 标示符
    public static String sdsent = "sdsent";

    /* ========================云主机=============================== */
    // 云主机概览 cpu时长临时存放数据
    public static JSONArray cloudhost_summary_cpu_temp = null;
    // 云主机概览 cpu时长自定义开始日期
    public static String cloudhost_summary_cpu_startdate = null;
    // 云主机概览 cpu时长自定义截止日期
    public static String cloudhost_summary_cpu_enddate = null;

    // 云主机概览 磁盘读写临时存放数据
    public static JSONArray cloudhost_summary_readwrite_temp = null;
    // 云主机概览磁盘读写自定义开始日期
    public static String cloudhost_summary_readwrite_startdate = null;
    // 云主机概览 磁盘读写自定义截止日期
    public static String cloudhost_summary_readwrite_enddate = null;

    // 云主机明细 服务状态数据临时存放数据
    public static JSONArray cloudhost_detail_server_temp = null;
    // 云主机明细 cpu时长临时存放数据
    public static JSONArray cloudhost_detail_onlinetime_temp = null;
    // 云主机明细 内存和磁盘临时存放数据
    public static JSONArray cloudhost_detail_memorydisk_temp = null;
    // 云主机明细 读/写临时存放数据
    public static JSONArray cloudhost_detail_readwrite_temp = null;
    // 云主机明细 接收临时存放数据
    public static JSONArray cloudhost_detail_receive_temp = null;
    // 云主机明细 发送临时存放数据
    public static JSONArray cloudhost_detail_sent_temp = null;
    // 云主机明细 自定义开始日期
    public static String cloudhost_detail_startdate = null;
    // 云主机明细 自定义截止日期
    public static String cloudhost_detail_enddate = null;
    
    /* ========================服务器=============================== */
    // 服务器概览 cpu时长临时存放数据
    public static JSONArray server_summary_cpu_temp = null;
    // 服务器概览 cpu时长自定义开始日期
    public static String server_summary_cpu_startdate = null;
    // 服务器概览 cpu时长自定义截止日期
    public static String server_summary_cpu_enddate = null;

    // 服务器概览 磁盘读写临时存放数据
    public static JSONArray server_summary_readwrite_temp = null;
    // 服务器概览磁盘读写自定义开始日期
    public static String server_summary_readwrite_startdate = null;
    // 服务器概览 磁盘读写自定义截止日期
    public static String server_summary_readwrite_enddate = null;

    // 服务器明细 服务状态数据临时存放数据
    public static JSONArray server_detail_server_temp = null;
    // 服务器明细 cpu时长临时存放数据
    public static JSONArray server_detail_onlinetime_temp = null;
    // 服务器明细 内存和磁盘临时存放数据
    public static JSONArray server_detail_memorydisk_temp = null;
    // 服务器明细 读/写临时存放数据
    public static JSONArray server_detail_readwrite_temp = null;
    // 服务器明细 接收临时存放数据
    public static JSONArray server_detail_receive_temp = null;
    // 服务器明细 发送临时存放数据
    public static JSONArray server_detail_sent_temp = null;
    // 服务器明细 自定义开始日期
    public static String server_detail_startdate = null;
    // 服务器明细 自定义截止日期
    public static String server_detail_enddate = null;  
    

    // 云主机概览 cpu时长导出excel字段数组
    public static String[][] summary_cpu_columns = new String[][] { {"日期", "name" }, {"在线时长", "value" } };
    // 云主机明细 服务状态导出excel字段数组
    public static String[][] summary_detail_server_columns = new String[][] { {"日期", "name" }, {"状态", "status" } };
    // 云主机概览 磁盘读写导出excel字段数组
    public static String[][] summary_readwrite_columns = new String[][] { {"日期", "y" }, {"读", "read" },
            {"写", "write" }, {"发送", "send" }, {"接收", "receive" } };
    // 云主机明细 内存和磁盘导出excel字段数组
    public static String[][] summary_detail_memorydisk_columns = new String[][] { {"日期", "y" },
            {"内存使用率(%)", "memory_use" }, {"磁盘使用率(%)", "disk_use" } };
    // 云主机明细 读/写导出excel字段数组
    public static String[][] summary_detail_readwrite_columns = new String[][] { {"日期", "timestamp" },
            {"读的数据(M)", "read_byte" }, {"写的数据(M)", "write_byte" }, {"读的次数", "read_request" },
            {"写的次数", "write_request" }, {"读的速度", "read_speed" }, {"写的速度", "write_speed" } };
    // 云主机明细 接收导出excel字段数组
    public static String[][] summary_detail_receive_columns = new String[][] { {"日期", "timestamp" },
            {"接收的数据(M)", "received_bytes" }, {"接收数据包", "received_packets" }, {"出错数", "received_errors" },
            {"丢包数", "received_drop" }, {"接收速度", "received_speed" } };
    // 云主机明细 接收导出excel字段数组
    public static String[][] summary_detail_sent_columns = new String[][] { {"日期", "timestamp" },
            {"发送的数据(M)", "sent_bytes" }, {"发送数据包", "sent_packets" }, {"出错数", "sent_errors" }, {"丢包数", "sent_drop" },
            {"接收速度", "sent_speed" } };

}
