package com.zhicloud.ms.transform.constant;

/**
 * @ClassName: TransFormPrivilegeConstant
 * @Description: 功能权限常量
 * @author 张本缘 于 2015年5月19日 上午9:47:39
 */
public class TransFormPrivilegeConstant {

    public static final String transform_user_add = "transform_user_add";
    public static final String transform_user_modify = "transform_user_modify";
    public static final String transform_user_delete = "transform_user_delete";
    public static final String transform_user_resetpassword = "transform_user_resetpassword";
    public static final String transform_user_query = "transform_user_query";
    public static final String transform_user_changepassword = "transform_user_changepassword";
    public static final String transform_user_setstatus = "transform_user_setstatus";
    public static final String transform_user_setusbstatus = "transform_user_setusbstatus";
    public static final String transform_system_address = "transform_system_address";
    public static final String transform_system_update = "transform_system_update";
    public static final String transform_role_add = "transform_role_add";
    public static final String transform_role_modify = "transform_role_modify";
    public static final String transform_role_delete = "transform_role_delete";
    public static final String transform_role_setright = "transform_role_setright";
    public static final String transform_role_query = "transform_role_query";
    public static final String transform_group_add = "transform_group_add";
    public static final String transform_group_modify = "transform_group_modify";
    public static final String transform_group_delete = "transform_group_delete";
    public static final String transform_group_query = "transform_group_query";
    public static final String transform_group_setright = "transform_group_setright";
    public static final String transform_menu_query = "transform_menu_query";
    public static final String transform_menu_add = "transform_menu_add";
    public static final String transform_menu_modify = "transform_menu_modify";
    public static final String transform_menu_delete = "transform_menu_delete";
    public static final String transform_menu_right = "transform_menu_right";
    public static final String transform_right_query = "transform_right_query";
    public static final String transform_right_add = "transform_right_add";
    public static final String transform_right_modify = "transform_right_modify";
    public static final String transform_right_delete = "transform_right_delete";
    public static final String transform_log_query = "transform_log_query";
    public static final String intelligent_router_query = "intelligent_router_query"; //智能路由规则查询
    public static final String intelligent_router_add = "intelligent_router_add"; //智能路由规则添加
    public static final String intelligent_router_remove = "intelligent_router_remove"; //智能路由规则删除
    public static final String transform_admin_moduser = "transform_admin_moduser";
    public static final String transform_admin_changepass = "transform_admin_changepass";
    //云桌面功能权限

    /****************************************分割线*********************************************/

    //主机类型功能权限
    public static final String desktop_type_query = "desktop_type_query"; //主机配置查询
    public static final String desktop_type_add = "desktop_type_add";     //主机配置新增
    public static final String desktop_type_modify = "desktop_type_modify";     //主机配置修改
    public static final String desktop_type_delete = "desktop_type_delete";     //主机配置修改

    //桌面云镜像
    public static final String desktop_image_query = "desktop_image_query"; //镜像查询
    public static final String desktop_image_add = "desktop_image_add";     //镜像新增
    public static final String desktop_image_modify = "desktop_image_modify";     //镜像修改
    public static final String desktop_image_delete = "desktop_image_delete";     //镜像删除
    public static final String desktop_image_type_modify = "desktop_image_type_modify";     //镜像用途修改

    //主机管理
    public static final String desktop_warehouse_query = "desktop_warehouse_query"; //仓库查询
    public static final String desktop_warehouse_add = "desktop_warehouse_add";     //仓库新增
    public static final String desktop_warehouse_modify = "desktop_warehouse_modify";     //仓库修改
    public static final String desktop_warehouse_delete = "desktop_warehouse_delete";     //仓库删除
    public static final String desktop_warehouse_detail = "desktop_warehouse_detail";     //仓库详情
    public static final String desktop_warehouse_add_host_amount = "desktop_warehouse_add_host_amount";     //仓库增加主机数
    public static final String desktop_warehouse_allocate = "desktop_warehouse_allocate";     //仓库分配
    public static final String desktop_warehouse_host_allocate = "desktop_warehouse_host_allocate";     //主机分配
    public static final String desktop_warehouse_host_start = "desktop_warehouse_host_start";     //主机开机
    public static final String desktop_warehouse_host_stop = "desktop_warehouse_host_stop";     //主机关机
    public static final String desktop_warehouse_host_shut_down = "desktop_warehouse_host_shut_down";     //主机强制关机
    public static final String desktop_warehouse_host_restart = "desktop_warehouse_host_restart";     //主机重启
    public static final String desktop_warehouse_host_reset = "desktop_warehouse_host_reset";     //主机重启
    public static final String desktop_warehouse_host_delete = "desktop_warehouse_host_delete";     //主机删除    
    public static final String desktop_warehouse_host_detail = "desktop_warehouse_host_detail";     //主机详情  
    public static final String desktop_warehouse_host_diagram = "desktop_warehouse_host_diagram";     //资源监控
    public static final String desktop_warehouse_check_time = "desktop_warehouse_check_time";     //设置仓库最小库存检查时间 
    public static final String desktop_host_start_from_iso = "desktop_host_start_from_iso";     //桌面云主机从光盘启动
    public static final String desktop_back_up_manage_query = "desktop_back_up_manage_query";   //备份与恢复页面
    public static final String desktop_back_up_manage_add = "desktop_back_up_manage_add";   //新增备份
    public static final String desktop_back_up_manage_resume = "desktop_back_up_manage_resume";   //恢复
    //群组管理
    public static final String desktop_group_query = "desktop_group_query";     //群组查询 
    public static final String desktop_group_add = "desktop_group_add";     //群组新增
    public static final String desktop_group_modify = "desktop_group_modify";     //群组修改
    public static final String desktop_group_delete = "desktop_group_delete";     //删除群组
    public static final String desktop_group_member_manage = "desktop_group_member_manage";     //成员管理
    public static final String desktop_group_member_modify = "desktop_group_member_modify";     //成员增删
    //用户管理
    public static final String desktop_user_query = "desktop_user_query";     //查询用户
    public static final String desktop_user_upload_excel_add_users = "desktop_user_upload_excel_add_users";     //导入excel批量新增
    public static final String desktop_user_add = "desktop_user_add";     //新增用户
    public static final String desktop_user_modify = "desktop_user_modify";     //修改用户
    public static final String desktop_user_delete = "desktop_user_delete";     //删除用户
    public static final String desktop_user_allocate_host = "desktop_user_allocate_host";     //分配主机
    public static final String desktop_user_host_diagram = "desktop_user_host_diagram";     //用户主机资源监控
    public static final String desktop_user_reset_password = "desktop_user_reset_password";     //重置密码
    public static final String desktop_user_modify_usb = "desktop_user_modify_usb";     //修改USB权限
    public static final String desktop_user_modify_status = "desktop_user_modify_status";     //修改用户状态
    public static final String desktop_user_detail = "desktop_user_detail";     //查看用户详情、

    //版本控制
    public static final String desktop_terminal_version_query = "desktop_terminal_version_query";     //版本查询
    public static final String desktop_terminal_version_add = "desktop_terminal_version_add";     //版本新增
    public static final String desktop_terminal_version_delete = "desktop_terminal_version_delete";     //版本删除

    public static final String desktop_resource_pool_query = "desktop_resource_pool_query";     //资源池查询
    public static final String desktop_resource_node_query = "desktop_resource_node_query";     //资源池节点查询
    public static final String desktop_resource_host_query = "desktop_resource_host_query";     //资源池主机查询
    public static final String desktop_resource_pool_add = "desktop_resource_pool_add";     //增加主机资源池
    public static final String desktop_resource_pool_mod = "desktop_resource_pool_mod";           //修改资源池
    public static final String desktop_resource_pool_remove = "desktop_resource_pool_remove";     //删除资源池

    public static final String desktop_resource_pool_host_start = "desktop_resource_pool_host_start";     //资源池主机开机
    public static final String desktop_resource_pool_host_stop = "desktop_resource_pool_host_stop";     //资源池主机关机
    public static final String desktop_resource_pool_host_halt = "desktop_resource_pool_host_halt";     //资源池主机强制关机
    public static final String desktop_resource_pool_host_restart = "desktop_resource_pool_host_restart";     //资源池主机重启
    public static final String desktop_resource_pool_host_changetodesktop = "desktop_resource_pool_host_changetodesktop";     //资源池无归属主机添加进桌面云



    public static final String desktop_disk_manage_query = "desktop_disk_manage_query"; //云桌面主机硬盘查询
    public static final String desktop_disk_manage_add = "desktop_disk_manage_add"; //云桌面主机硬盘添加
    public static final String desktop_disk_manage_delete = "desktop_disk_manage_delete"; //云桌面主机硬盘删除
    public static final String desktop_flush_disk_image = "desktop_flush_disk_image";  //服务器重装系统


    public static final String terminal_box_query = "terminal_box_query"; //盒子查询
    public static final String terminal_box_add = "terminal_box_add";     //新增盒子
    public static final String terminal_box_modify = "terminal_box_modify";     //盒子修改
    public static final String terminal_box_delete = "terminal_box_delete";     //盒子删除
    public static final String terminal_box_allocate = "terminal_box_allocate";     //盒子分配
    public static final String terminal_box_release = "terminal_box_release";     //盒子回收
    public static final String terminal_box_realinfo_query = "terminal_box_realinfo_query" ; // 盒子实时情况查询 
    public static final String terminal_box_info_push_query = "terminal_box_info_push_query" ; // 盒子推送消息查询
    public static final String terminal_box_info_push_add = "terminal_box_info_push_add" ; // 盒子推送消息新增
    public static final String terminal_box_info_push_delete = "terminal_box_info_push_delete" ; // 盒子推送消删除

    public static final String desktop_set_time_back_up_manage = "desktop_set_time_back_up_manage";     //桌面云定时备份管理查看
    public static final String desktop_set_time_back_up_update = "desktop_set_time_back_up_update";     //桌面云定时备份管理修改
    public static final String desktop_set_time_back_up_host_manage = "desktop_set_time_back_up_host_manage";     //桌面云定时备份主机管理查看
    public static final String desktop_set_time_back_up_host_update = "desktop_set_time_back_up_host_update";     //桌面云定时备份主机管理修改
    public static final String desktop_set_time_back_up_detail_manage = "desktop_set_time_back_up_detail_manage";     //桌面云定时备份记录查看

    public static final String desktop_set_time_startup_query = "desktop_set_time_startup_query"; //桌面云定时开机查看
    public static final String desktop_set_time_shutdown_query = "desktop_set_time_shutdown_query"; //桌面云定时关机查看
    public static final String desktop_set_time_startup_host_query = "desktop_set_time_startup_host_query"; //桌面云定时开机云主机查看
    public static final String desktop_set_time_shutdown_host_query = "desktop_set_time_shutdown_host_query"; //桌面云定时关机云主机查看
    public static final String desktop_set_time_operation_detail_query = "desktop_set_time_operation_detail_query"; //桌面云定时操作记录查看
    public static final String desktop_set_time_operation_update = "desktop_set_time_operation_update"; //桌面云定时操作修改
    public static final String desktop_set_time_operation_host_update = "desktop_set_time_operation_host_update"; //桌面云定时操作管理
    public static final String desktop_back_and_resume_resume= "desktop_back_and_resume_resume"; //桌面云备份和恢复 恢复
    public static final String desktop_back_and_resume_manualbackup = "desktop_back_and_resume_manualbackup";  //手动备份

    /****************************************分割线*********************************************/

    //服务器镜像
    public static final String server_image_query = "server_image_query"; //镜像查询
    public static final String server_image_add = "server_image_add";     //镜像新增
    public static final String server_image_modify = "server_image_modify";     //镜像修改
    public static final String server_image_delete = "server_image_delete";     //镜像删除
    public static final String server_image_type_modify = "server_image_type_modify";     //镜像用途修改

    /****************************************分割线*********************************************/

    //云服务器
    public static final String server_manage_query = "server_manage_query"; //云服务器查询
    public static final String server_manage_add = "server_manage_add";     //云服务器新增
    public static final String server_manage_modify = "server_manage_modify";     //云服务器修改
    public static final String server_manage_delete = "server_manage_delete";     //云服务器删除
    public static final String server_manage_diagram = "server_manage_diagram";     //云服务器资源管理
    public static final String server_manage_detail = "server_manage_detail";     //云服务器详情
    public static final String server_host_reset = "server_host_reset";     //云服务器强制重启
    public static final String server_host_start = "server_host_start";     //云服务器启动
    public static final String server_host_shutdown = "server_host_shutdown";     //云服务器关机
    public static final String server_host_restart = "server_host_restart";     //云服务器重启
    public static final String server_host_halt = "server_host_halt";     //云服务器强制关机
    public static final String server_host_allocate = "server_host_allocate";     //云服务器关联用户
    public static final String server_host_bind_tenant = "server_host_bind_tenant";     //云服务器绑定租户
    public static final String server_host_unbound_tenant = "server_host_unbound_tenant";     //云服务器解绑租户
    public static final String server_resource_pool_query = "server_resource_pool_query";     //资源池查询
    public static final String server_resource_pool_remove = "server_resource_pool_remove";   //服务器资源池移除
    public static final String server_resource_node_add = "server_resource_node_add";   //服务器资源池节点新增
    public static final String server_resource_node_remove = "server_resource_node_remove";   //服务器资源池节点移除
    public static final String server_resource_node_query = "server_resource_node_query";     //资源池节点查询
    public static final String server_resource_host_query = "server_resource_host_query";     //资源池主机查询
    public static final String server_resource_pool_add = "server_resource_pool_add";     //添加资源池
    public static final String server_resource_pool_host_start = "server_resource_pool_host_start";     //资源池主机开机
    public static final String server_resource_pool_host_stop = "server_resource_pool_host_stop";     //资源池主机关机
    public static final String server_resource_pool_host_halt = "server_resource_pool_host_halt";     //资源池主机强制关机
    public static final String server_resource_pool_host_restart = "server_resource_pool_host_restart";     //资源池主机重启
    public static final String server_resource_pool_host_changetoserver = "server_resource_pool_host_changetoserver";     //资源池无归属主机添加进桌面云
    public static final String server_resource_node_device = "server_resource_node_device";//查询磁盘挂载
    public static final String server_resource_node_unmount = "server_resource_node_unmount";//磁盘取消挂载    
    public static final String server_resource_node_mount = "server_resource_node_mount";//磁盘挂载    
    public static final String server_resource_node_enabledisk = "server_resource_node_enabledisk";//本地磁盘启用  
    public static final String server_resource_node_disabledisk = "server_resource_node_disabledisk";//本地磁盘禁用
    
    public static final String server_disk_manage_query = "server_disk_manage_query"; //云服务器硬盘查询
    public static final String server_disk_manage_add = "server_disk_manage_add"; //云服务器硬盘添加
    public static final String server_disk_manage_delete = "server_disk_manage_delete"; //云服务器硬盘删除
    
    public static final String server_set_time_back_up_manage = "server_set_time_back_up_manage";     //云服务器定时备份管理查看
    public static final String server_set_time_back_up_update = "server_set_time_back_up_update";     //云服务器定时备份管理修改
    public static final String server_set_time_back_up_host_manage = "server_set_time_back_up_host_manage";     //云服务器定时备份主机管理查看
    public static final String server_set_time_back_up_host_update = "server_set_time_back_up_host_update";     //云服务器定时备份主机管理修改
    public static final String server_set_time_back_up_detail_manage = "server_set_time_back_up_detail_manage";     //云服务器定时备份记录查看

    public static final String server_back_and_resume_resume= "server_back_and_resume_resume"; //云服务器备份和恢复 恢复
    public static final String server_back_and_resume_manualbackup = "server_back_and_resume_manualbackup";  //手动备份
    public static final String server_back_up_query = "server_back_up_query";  //服务器备份查看
    public static final String server_back_up_add = "server_back_up_add";  //服务器备份新增
    public static final String server_flush_disk_image = "server_flush_disk_image";  //服务器重装系统
    public static final String server_resume = "server_resume";  //服务器备份恢复
    public static final String server_type_query = "server_type_query"; //服务器配置查询
    public static final String server_type_add = "server_type_add";     //服务器配置新增
    public static final String server_type_modify = "server_type_modify";     //服务器配置修改
    public static final String server_type_delete = "server_type_delete";     //服务器配置删除


    //平台架构
    public static final String server_room_manage_query = "server_room_manage_query"; //机房查询
    public static final String server_rack_manage_query = "server_rack_manage_query"; //机架查询
    public static final String servers_manage_query = "servers_manage_query"; //服务器查询

    //平台资源监控
    public static final String plat_resource_manage = "plat_resource_manage"; //平台资源监控
    public static final String plat_service_manage = "plat_service_manage"; //服务管理
    public static final String plat_service_enable = "plat_service_enable"; //服务启用
    public static final String plat_service_disable = "plat_service_disable"; //服务禁用
    public static final String plat_service_manage_mod = "plat_service_manage_mod"; //服务修改
    //资源池管理
    public static final String storage_resource_pool_query = "storage_resource_pool_query"; //存储资源池查询
    public static final String storage_resource_pool_node_query = "storage_resource_pool_node_query"; //存储资源池节点查询
    public static final String storage_resource_pool_node_delete = "storage_resource_pool_node_delete"; //存储资源池节点删除
    public static final String compute_resource_pool_query = "compute_resource_pool_query"; //计算资源池查询
    public static final String compute_resource_pool_node_query = "compute_resource_pool_node_query"; //计算资源池节点查询
    public static final String compute_resource_pool_host_query = "compute_resource_pool_host_query"; //计算资源池管理主机查询
    public static final String compute_resource_pool_host_stop = "compute_resource_pool_host_stop"; //计算资源池主机关机
    public static final String compute_resource_pool_host_halt = "compute_resource_pool_host_halt"; //计算资源池主机强制关机
    public static final String compute_resource_pool_host_start = "compute_resource_pool_host_start"; //计算资源池主机强制关机
    public static final String compute_resource_pool_host_restart = "compute_resource_pool_host_restart"; //计算资源池主机重启
    public static final String compute_resource_pool_host_changetoserver = "compute_resource_pool_host_changetoserver"; //计算资源池无归属主机添加进云服务器
    public static final String compute_resource_pool_host_changetodesktop = "compute_resource_pool_host_changetodesktop"; //计算资源池无归属主机添加进云服务器
    public static final String compute_resource_pool_node_device = "compute_resource_pool_node_device"; //计算资源节点硬盘列表
    public static final String compute_resource_pool_node_unmount = "compute_resource_pool_node_unmount"; //计算资源节点硬盘取消挂载
    public static final String compute_resource_pool_node_mount = "compute_resource_pool_node_mount"; //计算资源节点硬盘挂载
    public static final String compute_resource_node_add = "compute_resource_node_add"; //添加计算资源节点
    public static final String compute_resource_node_remove = "compute_resource_node_remove"; //移除计算资源节点  
    public static final String compute_resource_pool_node_enabledisk = "compute_resource_pool_node_enabledisk"; //启用本地磁盘 
    public static final String compute_resource_pool_node_disabledisk = "compute_resource_pool_node_disabledisk"; //禁用本地磁盘     
    //IP资源池管理
    public static final String ip_resource_pool_query = "ip_resource_pool_query"; //地址资源池 _查询
    public static final String ip_resource_pool_add = "ip_resource_pool_add"; //地址资源池 _添加
    public static final String ip_resource_pool_remove = "ip_resource_pool_remove"; //地址资源池_删除
    public static final String ip_resource_pool_detail_query = "ip_resource_pool_detail_query"; //地址资源查询
    public static final String ip_resource_pool_detail_add = "ip_resource_pool_detail_add"; //地址资源添加
    public static final String ip_resource_pool_detail_remove = "ip_resource_pool_detail_remove"; //地址资源移除
    public static final String ip_resource_pool_detail_move = "ip_resource_pool_detail_move"; //地址资源迁移
    //端口资源池管理
    public static final String port_resource_pool_query         = "port_resource_pool_query";   //端口资源池 _查询
    //public static final String port_resource_pool_create        = "port_resource_pool_create";  //端口资源池 _创建界面
    public static final String port_resource_pool_add           = "port_resource_pool_add";  //端口资源池 _创建
    public static final String port_resource_pool_remove        = "port_resource_pool_remove";    //端口资源池_删除
    public static final String port_resource_pool_detail_query  = "port_resource_pool_detail_query";  //端口资源查询
    public static final String port_resource_pool_detail_add    = "port_resource_pool_detail_add";    //端口资源添加
    public static final String port_resource_pool_detail_remove = "port_resource_pool_detail_remove"; //端口资源移除


    //云硬盘管理
    public static final String cloud_disk_manager = "cloud_disk_manager"; //云硬盘管理
    public static final String cloud_disk_manager_add = "cloud_disk_manager_add"; //添加云硬盘
    public static final String cloud_disk_manager_onoff = "cloud_disk_manager_onoff"; //启用或停用云硬盘
    public static final String cloud_disk_manager_delete = "cloud_disk_manager_delete"; //删除云硬盘
    public static final String cloud_disk_manager_update = "cloud_disk_manager_update"; //修改云硬盘配置

    //消息服务

    //邮件配置
    public static final String message_email_config_query = "message_email_config_query"; //邮件配置管理
    public static final String message_email_config_add = "message_email_config_add"; //新增邮件配置
    public static final String message_email_config_mod = "message_email_config_mod"; //修改邮件配置
    public static final String message_email_config_remove = "message_email_config_remove"; //删除邮件配置

    //邮件模板
    public static final String message_email_template_query = "message_email_template_query"; //邮件模板管理
    public static final String message_email_template_add = "message_email_template_add"; //新增邮件模板
    public static final String message_email_template_mod = "message_email_template_mod"; //修改邮件模板
    public static final String message_email_template_remove = "message_email_template_remove"; //删除邮件模板

    //短信配置
    public static final String message_sms_config_query = "message_sms_config_query"; //短信配置管理
    public static final String message_sms_config_add = "message_sms_config_add"; //新增短信配置
    public static final String message_sms_config_mod = "message_sms_config_mod"; //修改短信配置
    public static final String message_sms_config_remove = "message_sms_config_remove"; //删除短信配置

    //邮件模板
    public static final String message_sms_template_query = "message_sms_template_query"; //模板管理
    public static final String message_sms_template_add = "message_sms_template_add"; //新增邮件模板
    public static final String message_sms_template_mod = "message_sms_template_mod"; //修改邮件模板
    public static final String message_sms_template_remove = "message_sms_template_remove"; //删除邮件模板

    //消息记录
    public static final String message_record_query = "message_record_query"; //消息管理
    public static final String message_record_remove = "message_record_remove"; //删除发送记录

    //专属云
    public static final String vpc_manage_page = "vpc_manage_page"; //专属云管理页
    public static final String vpc_manage_add_page = "vpc_manage_add_page"; //创建专属云页
    public static final String vpc_manage_delete = "vpc_manage_delete"; //删除专属云
    public static final String vpc_manage_stop = "vpc_manage_stop"; //停用专属云
    public static final String vpc_manage_start = "vpc_manage_start"; //恢复专属云
    public static final String vpc_manage_ip_page = "vpc_manage_ip_page"; //专属云IP管理页
    public static final String vpc_manage_ip_add = "vpc_manage_ip_add"; //专属云添加IP
    public static final String vpc_manage_ip_delete = "vpc_manage_ip_delete"; //专属云删除IP
    public static final String vpc_manage_network_page = "vpc_manage_network_page"; //专属云端口配置页
    public static final String vpc_manage_network_add = "vpc_manage_network_add"; //专属云添加端口配置
    public static final String vpc_manage_network_delete = "vpc_manage_network_delete"; //专属云删除端口配置
    public static final String vpc_manage_host_page = "vpc_manage_host_page"; //专属云主机列表页
    public static final String vpc_manage_host_add = "vpc_manage_host_add"; //专属云创建主机
    public static final String vpc_manage_host_unbound = "vpc_manage_host_unbound"; //解绑专属云主机

    // 监控管理
    public static final String monitor_warn_query = "monitor_warn_query";// 告警规则查询
    public static final String monitor_warn_add = "monitor_warn_add";// 告警规则新增
    public static final String monitor_warn_modify = "monitor_warn_modify";// 告警规则修改
    public static final String monitor_warn_delete = "monitor_warn_delete";// 告警规则删除
    public static final String monitor_warn_value = "monitor_warn_value";// 编辑规则内容
    public static final String monitor_area_query = "monitor_area_query";// 区域监控查询
    public static final String monitor_room_query = "monitor_room_query";// 机房监控查询
    public static final String monitor_rack_query = "monitor_rack_query";// 机架监控查询
    public static final String monitor_server_query = "monitor_server_query";// 服务器监控查询
    public static final String monitor_host_query = "monitor_server_query";// 云主机监控查询
    public static final String monitor_resource_query = "monitor_resource_query";// 资源管理监控查询
    public static final String monitor_system_query = "monitor_system_query";// 系统监控查询
    
    //消息反馈
    public static final String client_message_manage = "client_message_manage"; //反馈消息管理
    public static final String client_message_delete = "client_message_delete"; //反馈消息删除
    public static final String client_message_mark = "client_message_mark"; //反馈消息标记已读
    public static final String monitor_system_shield_server = "monitor_system_shield_server";// 系统监控屏蔽
    public static final String monitor_system_shield_host = "monitor_system_shield_host";// 系统监控屏蔽
   
    //租户管理
    public static final String tenant_manager = "tenant_manager"; //租户管理
    public static final String tenant_add = "tenant_add";         //新增租户
    public static final String tenant_modify = "tenant_modify";   //修改租户    
    public static final String tenant_del = "tenant_del";         //删除租户    
    public static final String tenant_setuser = "tenant_setuser"; //配置用户      

    public static final String tenant_setuser_query = "tenant_setuser_query"; //配置用户   查询  
    public static final String tenant_host_query = "tenant_host_query"; //主机查询
    public static final String tenant_host_start = "tenant_host_start"; //主机开机
    public static final String tenant_host_shut_down = "tenant_host_shut_down"; //主机关机
    public static final String tenant_host_restart = "tenant_host_restart"; //主机重启
    public static final String tenant_host_reset = "tenant_host_reset"; //主机强制重启
    public static final String tenant_host_halt = "tenant_host_halt"; //主机强制关机
    public static final String tenant_host_delete = "tenant_host_delete"; //主机强制关机
    public static final String tenant_host_edit = "tenant_host_edit"; //主机修改配置
    public static final String tenant_host_add = "tenant_host_add"; //主机新增
    public static final String tenant_host_disk_manage_query = "tenant_host_disk_manage_query"; //云服务器硬盘查询
    public static final String tenant_host_disk_manage_add = "tenant_host_disk_manage_add"; //云服务器硬盘添加
    public static final String tenant_host_disk_manage_delete = "tenant_host_disk_manage_delete"; //云服务器硬盘删除
    public static final String tenant_host_break = "tenant_host_break"; //主机解绑
    public static final String trnant_host_manage_diagram = "trnant_host_manage_diagram";//主机资源监控
    
    //Qos规则管理
    public static final String desktop_qos_query = "desktop_qos_query"; //桌面云QoS规则查询
    public static final String desktop_qos_add = "desktop_qos_add";     //桌面云Qos规则添加
    public static final String desktop_qos_remove = "desktop_qos_remove"; //桌面云Qos规则移除

    public static final String server_qos_query = "server_qos_query"; //云服务器QoS规则查询
    public static final String server_qos_add = "server_qos_add";     //云服务器Qos规则添加
    public static final String server_qos_remove = "server_qos_remove"; //云服务器Qos规则移除

    public static final String vpc_qos_query = "vpc_qos_query"; //VPCQoS规则查询
    public static final String vpc_qos_add = "vpc_qos_add";     //VPCQos规则添加
    public static final String vpc_qos_remove = "vpc_qos_remove"; //VPCQos规则移除

    public static final String shared_memory_query = "shared_memory_query";// 共享存储查询
    public static final String shared_memory_add = "shared_memory_add";// 共享存储新增
    public static final String shared_memory_update = "shared_memory_update";// 共享存储修改
    public static final String shared_memory_delete = "shared_memory_delete";// 共享存储删除
    public static final String shared_memory_available = "shared_memory_available";// 设置可用
    //云服务器快照
    public static final String server_snapshot_query = "server_snapshot_query"; //快照查询
    public static final String server_snapshot_delete = "server_snapshot_delete"; //快照删除
    public static final String server_snapshot_create = "server_snapshot_create"; //快照创建
    public static final String server_snapshot_resume = "server_snapshot_resume"; //快照恢复

    public static final String desktop_resource_node_device = "desktop_resource_node_device";// 查询磁盘挂载
    public static final String desktop_resource_node_unmount = "desktop_resource_node_unmount";// 磁盘取消挂载
    public static final String desktop_resource_node_mount = "desktop_resource_node_mount";// 磁盘挂载
    public static final String desktop_resource_node_enabledisk = "desktop_resource_node_enabledisk";// 本地磁盘启用
    public static final String desktop_resource_node_disabledisk = "desktop_resource_node_disabledisk";// 本地磁盘禁用
    
    public static final String iso_image_query = "iso_image_query"; // 镜像查询
    public static final String iso_image_delete = "iso_image_delete"; // 镜像删除

}
 
 
