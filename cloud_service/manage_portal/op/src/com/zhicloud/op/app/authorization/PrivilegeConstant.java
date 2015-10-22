package com.zhicloud.op.app.authorization;

/**
 * 权限必须对应privilege-config.xml文件的叶子节点
 */
public class PrivilegeConstant
{
	/**********************************************
	 * 权限必须对应privilege-config.xml文件的叶子节点
	 **********************************************
	 */
	
	// 系统角色
	public static final String sys_role_manage_page          = "sys_role_manage_page";
	public static final String sys_role_manage_add           = "sys_role_manage_add";
	public static final String sys_role_manage_modify        = "sys_role_manage_modify";
	public static final String sys_role_manage_delete        = "sys_role_manage_delete";
	public static final String sys_role_manage_set_privilege = "sys_role_manage_set_privilege";
	
	// 系统组
	public static final String sys_group_manage_page       = "sys_group_manage_page";
	public static final String sys_group_manage_add        = "sys_group_manage_add";
	public static final String sys_group_manage_modify     = "sys_group_manage_modify";
	public static final String sys_group_manage_delete     = "sys_group_manage_delete";
	public static final String sys_group_manage_set_member = "sys_group_manage_set_member";
	public static final String sys_group_manage_set_role   = "sys_group_manage_set_role";
	
	// 运营商
	public static final String operator_manage_page                    = "operator_manage_page";
	public static final String mark_manage_page                        = "mark_manage_page";
	public static final String operator_manage_add                     = "operator_manage_add";
	public static final String operator_manage_modify                  = "operator_manage_modify";
	public static final String operator_manage_delete                  = "operator_manage_delete";
	public static final String operator_reset_password                 = "operator_reset_password";
	public static final String operator_basic_information_page         = "operator_basic_information_page";
	public static final String operator_change_password_page           = "operator_change_password_page";
	public static final String statement_manage_page_operator          = "statement_manage_page_operator";
	public static final String invoice_manage_page_operator            = "invoice_manage_page_operator";
	public static final String operator_terminal_user_reset_password   = "operator_terminal_user_reset_password";
	public static final String suggestion_manage_page_operator         = "suggestion_manage_page_operator";
	public static final String operator_invite_code_manage_page        = "operator_invite_code_manage_page";
	public static final String operator_invite_code_add                = "operator_invite_code_add";
	public static final String operator_invite_code_delete             = "operator_invite_code_delete";
	public static final String operator_invite_code_send_email         = "operator_invite_code_send_email";
	public static final String operator_invite_code_send_phone         = "operator_invite_code_send_phone";
	public static final String operator_cash_coupon_manage_page        = "operator_cash_coupon_manage_page";
	public static final String operator_cash_coupon_add                = "operator_cash_coupon_add";
	public static final String operator_agent_recharge                = "operator_agent_recharge";
	public static final String operator_cash_coupon_delete             = "operator_cash_coupon_delete";
	public static final String operator_cash_coupon_send_email         = "operator_cash_coupon_send_email";
	public static final String operator_cash_coupon_send_phone         = "operator_cash_coupon_send_phone";

	//运营商消息系统

	//邮件
	public static final String message_mail_config_manage_page     		= "message_mail_config_manage_page";
	public static final String message_mail_config_manage_add 			= "message_mail_config_manage_add";
	public static final String message_mail_config_manage_modify 		= "message_mail_config_manage_modify";
	public static final String message_mail_config_manage_delete 		= "message_mail_config_manage_delete";

	public static final String message_mail_template_manage_page     	= "message_mail_template_manage_page";
	public static final String message_mail_template_manage_add 		= "message_mail_template_manage_add";
	public static final String message_mail_template_manage_modify 		= "message_mail_template_manage_modify";
	public static final String message_mail_template_manage_delete 		= "message_mail_template_manage_delete";

	public static final String message_mail_record_manage_page     		= "message_mail_record_manage_page";
	public static final String message_mail_record_manage_add 			= "message_mail_record_manage_add";
	public static final String message_mail_record_manage_delete 		= "message_mail_record_manage_delete";

	// 云主机
	public static final String cloud_host_create              = "cloud_host_create";
	public static final String cloud_host_view_detail         = "cloud_host_view_detail";
	public static final String cloud_host_manage_page         = "cloud_host_manage_page";
	public static final String cloud_host_create_agent        = "cloud_host_create_agent";
	public static final String cloud_host_view_detail_agent   = "cloud_host_view_detail_agent";
	public static final String cloud_host_manage_page_agent   = "cloud_host_manage_page_agent";
	
	// 运营商自用云主机
	public static final String self_use_cloud_host_page                           = "self_use_cloud_host_page";
	public static final String self_use_cloud_host_page_agent                     = "self_use_cloud_host_page_agent";
	public static final String operator_self_use_cloud_host_start_page            = "operator_self_use_cloud_host_start_page";
	public static final String operator_self_use_cloud_host_insert_iso_image_page = "operator_self_use_cloud_host_insert_iso_image_page";
	public static final String operator_self_use_cloud_host_add_port_page   ="operator_self_use_cloud_host_add_port_page";
	public static final String operator_self_use_cloud_host_terminal_binding_page   ="operator_self_use_cloud_host_terminal_binding_page";
	public static final String operator_self_use_cloud_host_update_password   ="operator_self_use_cloud_host_update_password";
	
	// 代理商
	public static final String agent_manage_page              = "agent_manage_page";
	public static final String agent_manage_add               = "agent_manage_add";
	public static final String agent_manage_modify            = "agent_manage_modify";
	public static final String agent_manage_delete            = "agent_manage_delete";
	public static final String agent_reset_password           = "agent_reset_password";
	public static final String agent_basic_information_page   = "agent_basic_information_page";
	public static final String agent_change_password_page     = "agent_change_password_page";
	public static final String statement_manage_page_agent    = "statement_manage_page_agent";
	public static final String agent_invite_code_manage_page  = "agent_invite_code_manage_page";
	public static final String agent_invite_code_add          = "agent_invite_code_add";
	public static final String agent_invite_code_delete       = "agent_invite_code_delete";
	public static final String agent_invite_code_send_email   = "agent_invite_code_send_email";
	public static final String agent_invite_code_send_phone   = "agent_invite_code_send_phone";
	public static final String agent_recharge                 = "agent_recharge";
	public static final String invoice_manage_agent           = "invoice_manage_agent";
	public static final String service_detail_agent           = "service_detail_agent";
	public static final String recharge_detail_agent           = "recharge_detail_agent";
	public static final String consumption_detail_agent           = "consumption_detail_agent";
	public static final String business_statistics_agent      = "business_statistics_agent";
	public static final String agent_business_graphics_page   = "agent_business_graphics_page";
 	
	// 终端用户
	public static final String terminal_user_manage_page_for_operator = "terminal_user_manage_page_for_operator";
	public static final String terminal_user_manage_add               = "terminal_user_manage_add";
	public static final String terminal_user_manage_modify            = "terminal_user_manage_modify";
	public static final String terminal_user_manage_delete            = "terminal_user_manage_delete";
	public static final String terminal_user_manage_page_for_agent    = "terminal_user_manage_page_for_agent";
	public static final String terminal_user_manage_add_agent         = "terminal_user_manage_add_agent";
	public static final String terminal_user_manage_modify_agent      = "terminal_user_manage_modify_agent";
	public static final String terminal_user_manage_delete_agent      = "terminal_user_manage_delete_agent";
	 

	// 套餐项管理
	public static final String package_item_manage_page        = "package_item_manage_page";
	public static final String package_price_manage_page       = "package_price_manage_page";
	public static final String app_porpertise_manage_page      = "app_porpertise_manage_page";
	public static final String cpu_package_option_add          = "cpu_package_option_add";
	public static final String cpu_package_option_delete       = "cpu_package_option_delete";
	public static final String memory_package_option_add       = "memory_package_option_add";
	public static final String memory_package_option_delete    = "memory_package_option_delete";
	public static final String disk_package_option_modify      = "disk_package_option_modify";
	public static final String bandwidth_package_option_modify = "bandwidth_package_option_modify";
	public static final String trial_period_manage             = "trial_period_manage";
	
	// 镜像管理
	public static final String sys_disk_image_manage_page   = "sys_disk_image_manage_page";
	public static final String sys_disk_image_manage_add    = "sys_disk_image_manage_add";
	public static final String sys_disk_image_manage_modify = "sys_disk_image_manage_modify";
	public static final String sys_disk_image_manage_delete = "sys_disk_image_manage_delete";
	
	// 订购云主机管理
	public static final String order_cloud_host_manage_page   = "order_cloud_host_manage_page";
	public static final String order_cloud_host_manage_modify   = "order_cloud_host_manage_modify";
	
	//账单
	public static final String bill_manage_page                  = "bill_manage_page";
	public static final String bill_manage_modify                = "bill_manage_modify";
	public static final String bill_manage_delete                = "bill_manage_delete";
	public static final String bill_part_page                    = "bill_part_page";
	public static final String bill_detail_page                  = "bill_detail_page";
	public static final String cloud_host_bill_detail_page       = "cloud_host_bill_detail_page";
	public static final String bill_manage_page_agent            = "bill_manage_page_agent";
	public static final String bill_manage_modify_agent          = "bill_manage_modify_agent";
	public static final String bill_manage_delete_agent          = "bill_manage_delete_agent";
	public static final String bill_part_page_agent              = "bill_part_page_agent";
	public static final String bill_detail_page_agent            = "bill_detail_page_agent";
	public static final String cloud_host_bill_detail_page_agent = "cloud_host_bill_detail_page_agent";
	
	//报表
	public static final String statement_manage_page = "statement_manage_page";
	
	//库存类型
	public static final String warehouse_manage_page = "warehouse_manage_page";
	public static final String warehouse_manage_add = "warehouse_manage_add";
	public static final String warehouse_manage_detail = "warehouse_manage_detail";
	public static final String warehouse_manage_modify = "warehouse_manage_modify";
	
	//VPC
	public static final String vpc_manage_page        = "vpc_manage_page";
	public static final String vpc_manage_page_agent  = "vpc_manage_page_agent";
	public static final String view_all_user_oper_log  = "view_all_user_oper_log";
	
	//监控信息(区域，机房，机架，服务器，云主机,规则)
    public static final String monitor_area = "monitor_area";
    public static final String monitor_room = "monitor_room";
    public static final String monitor_rack = "monitor_rack";
    public static final String monitor_server = "monitor_server";
    public static final String monitor_cloudhost = "monitor_cloudhost";
    public static final String monitor_rule_query = "monitor_rule_query";
    public static final String monitor_rule_add = "monitor_rule_add";
    public static final String monitor_rule_modify = "monitor_rule_modify";
    public static final String monitor_rule_delete = "monitor_rule_delete";
    public static final String monitor_resource = "monitor_resource";
	
	/**********************************************
	 * 权限必须对应privilege-config.xml文件的叶子节点
	 **********************************************
	 */
	
}
