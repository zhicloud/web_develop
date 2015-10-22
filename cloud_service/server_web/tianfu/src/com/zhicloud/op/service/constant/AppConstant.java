package com.zhicloud.op.service.constant;

import java.math.BigDecimal;

public class AppConstant
{

	public static final String SUCCESS = "success";
	public static final String FAIL    = "fail";
	
//	// 系统通用常量
//	public static final Integer GLOBAL_REGION_1_GUANGZHOU = 1;
//	public static final Integer GLOBAL_REGION_2_CHENGDU   = 2;
//	public static final Integer GLOBAL_REGION_3_BEIJING   = 3;
//	public static final Integer GLOBAL_REGION_4_HONGKONG  = 4;
	
	// 用户类型
	public static final Integer SYS_USER_TYPE_ADMIN         = 1;
	public static final Integer SYS_USER_TYPE_OPERATOR      = 2;
	public static final Integer SYS_USER_TYPE_AGENT         = 3;
	public static final Integer SYS_USER_TYPE_TERMINAL_USER = 4;
	
	// 运营商状态
	public static final Integer OPERATOR_STATUS_NORMAL   = 1;
	public static final Integer OPERATOR_STATUS_DISABLED = 2;
	public static final Integer OPERATOR_STATUS_END      = 3;

	// 代理商状态
	public static final Integer AGENT_STATUS_NORMAL   = 1;
	public static final Integer AGENT_STATUS_DISABLED = 2;
	public static final Integer AGENT_STATUS_END      = 3;
	
	// 终端用户
	public static final Integer TERMINAL_USER_EMAIL_VERIFIED_NOT = 1;
	public static final Integer TERMINAL_USER_EMAIL_VERIFIED_YES = 2;
	
	public static final Integer TERMINAL_USER_PHONE_VERIFIED_NOT = 1;
	public static final Integer TERMINAL_USER_PHONE_VERIFIED_YES = 2;

	public static final Integer TERMINAL_USER_STATUS_NOT_VERIFIED = 1;
	public static final Integer TERMINAL_USER_STATUS_NORMAL       = 2;
	public static final Integer TERMINAL_USER_STATUS_DISABLED     = 3;
	public static final Integer TERMINAL_USER_STATUS_ARREARAGE    = 4;
	public static final Integer TERMINAL_USER_STATUS_END          = 5;
	
	public static final Integer TERMINAL_USER_BELONGING_TYPE_OPERATOR = 1;
	public static final Integer TERMINAL_USER_BELONGING_TYPE_AGENT    = 2;
	
	// 余额变动明细表
	public static final Integer ACCOUNT_BALANCE_DETAIL_TYPE_RECHARGE    = 1;
	public static final Integer ACCOUNT_BALANCE_DETAIL_TYPE_CONSUMPTION = 2;
	
	public static final Integer ACCOUNT_BALANCE_DETAIL_PAY_TYPE_1_ALIPAY        = 1;	// 支付宝
	public static final Integer ACCOUNT_BALANCE_DETAIL_PAY_TYPE_2_UNIONPAY      = 2;	// 银联
	public static final Integer ACCOUNT_BALANCE_DETAIL_PAY_TYPE_3_GIFT_PRESENT  = 3;	// 系统赠送
	public static final Integer ACCOUNT_BALANCE_DETAIL_PAY_TYPE_4_FEE_DEFUCTION = 4;	// 系统扣费
	
	public static final Integer ACCOUNT_BALANCE_DETAIL_IS_NOT_RECHARGE= 1;	//未支付 
	public static final Integer ACCOUNT_BALANCE_DETAIL_IS_RECHARGE= 2;	//已支付 
	
	
	// 权限类型
	public static final String PRIVILEGE_TYPE_MENU     = "1";
	public static final String PRIVILEGE_TYPE_FUNCTION = "2";
	
	// 云主机类型
	public static final Integer CLOUD_HOST_TYPE_1_OPERATOR      = 1;
	public static final Integer CLOUD_HOST_TYPE_2_AGENT         = 2;
	public static final Integer CLOUD_HOST_TYPE_3_TERMINAL_USER = 3;
	public static final Integer CLOUD_HOST_TYPE_4_WAREHOUSE     = 4;
	public static final Integer CLOUD_HOST_TYPE_5_TERMINAL_USER_VPC    = 5;
	public static final Integer CLOUD_HOST_TYPE_6_Agent_VPC    = 6;

	// 云主机是否自动启动
	public static final Integer CLOUD_HOST_IS_AUTO_STARTUP_YES = 1;
	public static final Integer CLOUD_HOST_IS_AUTO_STARTUP_NO  = 2;
	
	// 云主机运行状态 
	public static final Integer CLOUD_HOST_RUNNING_STATUS_SHUTDOWN = 1;
	public static final Integer CLOUD_HOST_RUNNING_STATUS_RUNNING  = 2;
	public static final Integer CLOUD_HOST_RUNNING_STATUS_ALARM    = 3;
	public static final Integer CLOUD_HOST_RUNNING_STATUS_FAULT    = 4;
	public static final Integer CLOUD_HOST_RUNNING_STATUS_SHUTTING_DOWN    = 5;
	public static final Integer CLOUD_HOST_RUNNING_STATUS_STARTING   = 6;

	// 云主机状态
	public static final Integer CLOUD_HOST_STATUS_1_NORNAL       = 1;
	public static final Integer CLOUD_HOST_STATUS_2_HALT         = 2;
	public static final Integer CLOUD_HOST_STATUS_3_ARREARAGE    = 3;
	public static final Integer CLOUD_HOST_STATUS_4_HALT_FOREVER = 4;
	
	//试用期类型
	public static final Integer CLOUD_HOST = 1;
	public static final Integer CLOUD_DISK = 2;
	public static final Integer DEFAULT_DAY = 1;
	
	// 账单明细
	public static final Integer BILL_DETAIL_ITEM_TYPE_CLOUD_HOST = 1;
	public static final Integer BILL_DETAIL_ITEM_TYPE_CLOUD_DISK = 2;
	public static final Integer BILL_DETAIL_ITEM_TYPE_VPC = 3;
	
	// 云主机账单明细类型
	public static final Integer CLOUD_HOST_BILL_DETAIL_TYPE_1_MONTHLY_PAYMENT = 1;
	public static final Integer CLOUD_HOST_BILL_DETAIL_TYPE_2_PAY_FOR_USED    = 2;
	public static final Integer CLOUD_HOST_BILL_DETAIL_TYPE_3_TRAIL           = 3;
	public static final Integer CLOUD_HOST_BILL_DETAIL_TYPE_4_PAY_FOR_TIME    = 4;

	// 云主机账单明细付费状态
	public static final Integer CLOUD_HOST_BILL_DETAIL_IS_PAID_NOT = 1;
	public static final Integer CLOUD_HOST_BILL_DETAIL_IS_PAID_YES = 2;

	// 云方机订购配置
	public static final Integer CLOUD_HOST_SHOPPING_CONFIG_TYPE_PAY_FOR_TIME  = 1;
	public static final Integer CLOUD_HOST_SHOPPING_CONFIG_TYPE_PAY_FOR_USAGE = 2;
	public static final Integer CLOUD_HOST_SHOPPING_CONFIG_TYPE_TRAIL         = 3;
	
	public static final Integer CLOUD_HOST_SHOPPING_CONFIG_PROCESS_STATUS_NOT_PROCESSED  = 0;
	public static final Integer CLOUD_HOST_SHOPPING_CONFIG_PROCESS_STATUS_SUCCESS        = 1;
	public static final Integer CLOUD_HOST_SHOPPING_CONFIG_PROCESS_STATUS_FAIL           = 2;
	public static final Integer CLOUD_HOST_SHOPPING_CONFIG_PROCESS_STATUS_CREATING       = 3;

	// 订单信息
	public static final Integer ORDER_INFO_PROCESS_STATUS_NOT_PROCESSED  = 0;
	public static final Integer ORDER_INFO_PROCESS_STATUS_SUCCESS        = 1;
	public static final Integer ORDER_INFO_PROCESS_STATUS_FAIL           = 2;
	public static final Integer ORDER_INFO_PROCESS_STATUS_PENDING   = 3;
	
	public static final Integer ORDER_INFO_IS_PAID_NOT  = 1;
	public static final Integer ORDER_INFO_IS_PAID_YES  = 2;

	// 订单详情
	public static final Integer ORDER_DETAIL_ITEM_TYPE_CLOUD_HOST      = 1;
	public static final Integer ORDER_DETAIL_ITEM_TYPE_STORAGE_SERVICE = 2;
	
	// 云主机仓库明细状态
	public static final Integer CLOUD_HOST_WAREHOUSE_DETAIL_STATUS_1_NOT_PROCESSED   = 1;
	public static final Integer CLOUD_HOST_WAREHOUSE_DETAIL_STATUS_2_FAIL            = 2;
	public static final Integer CLOUD_HOST_WAREHOUSE_DETAIL_STATUS_3_NOT_DISTRIBUTED = 3;
	public static final Integer CLOUD_HOST_WAREHOUSE_DETAIL_STATUS_4_DISTRIBUTED     = 4;
	public static final Integer CLOUD_HOST_WAREHOUSE_DETAIL_STATUS_5_CREATING        = 5;
	
	
	//--------------------
	
	
	// 协议
	public static final Integer PROTOCOL_ALL = 0;
	public static final Integer PROTOCOL_TCP = 1;
	public static final Integer PROTOCOL_UDP = 2;
	
	
	// 
	public static final String STATUS_SUCCESS = "success";
	public static final String STATUS_FAIL    = "fail";
	
	//邀请码
	public static final Integer INVITE_CODE_SEND_NO      = 1;
	public static final Integer INVITE_CODE_SEND_YES     = 2;
	public static final Integer INVITE_CODE_SEND_EXPIRE  = 3;
	public static final Integer INVITE_CODE_SEND_USED    = 4;
	
	//代金券
	public static final Integer CASH_COUPON_SEND_NO      = 1;
	public static final Integer CASH_COUPON_SEND_YES     = 2;
	public static final Integer CASH_COUPON_SEND_EXPIRE  = 3;
	public static final Integer CASH_COUPON_SEND_USED    = 4;
	
	public static final Integer CREATE_TERMINAL_INVITE_CODE_NUMBER = 20;
	
	//云主机操作
	public static final String CLOUD_HOST_START    = "1";
	public static final String CLOUD_HOST_STOP     = "2";
	public static final String CLOUD_HOST_RESTART  = "3";
	public static final String CLOUD_HOST_HALT     = "4";
	
	//发票状态
	public static final String INVOICE_IS_NONE = "1";//未开据发票
	public static final String INVOICE_IS_PENDING = "2";//发票已开，尚未寄送
	public static final String INVOICE_IS_SENDING = "3";//发票已寄送
	public static final String INVOICE_IS_EXPORTED = "4";//发票已导出，尚未寄送
	public static final String INVOICE_IS_SENT_SUCCESS = "5";//发票已寄送完成

	
	//云主机推荐配置价格
	public static final BigDecimal CLOUD_HOST_PRICE_LEVEL_GZ_1  = new BigDecimal("231");//入门级
	public static final BigDecimal CLOUD_HOST_PRICE_LEVEL_GZ_2  = new BigDecimal("310");//标准级
	public static final BigDecimal CLOUD_HOST_PRICE_LEVEL_GZ_3  = new BigDecimal("480");//商务级
	public static final BigDecimal CLOUD_HOST_PRICE_LEVEL_GZ_4  = new BigDecimal("1000");//标准级
	public static final BigDecimal CLOUD_HOST_PRICE_LEVEL_GZ_5  = new BigDecimal("1900");//专业级
	
	public static final BigDecimal CLOUD_HOST_PRICE_LEVEL_HK_1  = new BigDecimal("393.6");//入门级
	public static final BigDecimal CLOUD_HOST_PRICE_LEVEL_HK_2  = new BigDecimal("520");//标准级
	public static final BigDecimal CLOUD_HOST_PRICE_LEVEL_HK_3  = new BigDecimal("816");//商务级
	public static final BigDecimal CLOUD_HOST_PRICE_LEVEL_HK_4  = new BigDecimal("1600");//标准级
	public static final BigDecimal CLOUD_HOST_PRICE_LEVEL_HK_5  = new BigDecimal("2800");//专业级
	//日志状态
	public static final Integer OPER_LOG_SUCCESS  = 1;
	public static final Integer OPER_LOG_FAIL     = 2;
	
	//云硬盘运行状态
	public static final Integer CLOUD_DISK_RUNNING_STATUS_SHUTDOWN = 1;//关机
	public static final Integer CLOUD_DISK_RUNNING_STATUS_RUNNING  = 2;//运行
	public static final Integer CLOUD_DISK_RUNNING_STATUS_ALARM    = 3;//警告
	public static final Integer CLOUD_DISK_RUNNING_STATUS_FAULT    = 4;//故障
	
	//云硬盘状态
	public static final Integer CLOUD_DISK_STATUS_NORMAL = 1;//正常
	public static final Integer CLOUD_DISK_STATUS_HALT  = 2;//停机
	public static final Integer CLOUD_DISK_STATUS_ARREARAGE  = 3;//欠费
	public static final Integer CLOUD_DISK_STATUS_HALT_FOREVER   = 4;//永久停机，已删除
	
	//云硬盘订购信息
	public static final Integer CLOUD_DISK_SHOPPING_CONFIG_PROCESS_STATUS_NOT_PROCESSED  = 0;//未处理
	public static final Integer CLOUD_DISK_SHOPPING_CONFIG_PROCESS_STATUS_SUCCESS        = 1;//创建成功
	public static final Integer CLOUD_DISK_SHOPPING_CONFIG_PROCESS_STATUS_FAIL           = 2;//创建失败
	public static final Integer CLOUD_DISK_SHOPPING_CONFIG_PROCESS_STATUS_CREATING       = 3;//正在创建
	//页面需要
	public static final String PAGE_TITLE = "天府软件园创业场";
	
	 //域名操作
	public static final String DOMAIN_BIND = "0";
	public static final String DOMAIN_REMOVE = "1";
	
	//http client相关参数
	
	//请求路径(软件园)
	public final static String MAPPING_URL_TIANFU = "/tianfu/server/binding.do";
	public static final String METHOD_POST = "POST";
	public static final String CHARSET = "UTF-8";
	
}




