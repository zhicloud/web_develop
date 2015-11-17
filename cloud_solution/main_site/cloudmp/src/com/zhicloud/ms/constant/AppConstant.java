package com.zhicloud.ms.constant;

import java.math.BigDecimal;

public class AppConstant
{

	public static final String SUCCESS = "success";
	public static final String FAIL    = "fail";
	
	//默认密码
	public static final String DEFAULT_PASSWORD = "12345678";
	
	public static final String  PASSWORD_MD5_STR="zxcvbnm";
	
	//默认分组ID
	public static final String DEFAULT_GROUP_ID = "0";

	//导入用户默认格式
	public static final String[] DEFAULT_TABLE_TITLE = {"用户名", "显示名", "邮箱", "电话"};
	
	
//	// 系统通用常量
//	public static final Integer GLOBAL_REGION_1_GUANGZHOU = 1;
//	public static final Integer GLOBAL_REGION_2_CHENGDU   = 2;
//	public static final Integer GLOBAL_REGION_3_BEIJING   = 3;
//	public static final Integer GLOBAL_REGION_4_HONGKONG  = 4;
	
	// 用户类型
	public static final Integer SYS_USER_TYPE_ADMIN         = 1;
	public static final Integer SYS_USER_TYPE_TERMINAL_USER = 2;
	
	//用户USB开启状态
	public static final Integer USB_STATUS_ENABLE = 1; //可用
	public static final Integer USB_STATUS_DISABLE = 0; //禁用
	
	//用户状态
	public static final Integer USER_STATUS_ENABLE = 0; //正常
	public static final Integer USER_STATUS_DISNABLE = 1; //禁用	
	public static final Integer USER_STATUS_DELETE = 9; //禁用

	
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

	// 云主机是否自动启动
	public static final Integer CLOUD_HOST_IS_AUTO_STARTUP_YES = 1;
	public static final Integer CLOUD_HOST_IS_AUTO_STARTUP_NO  = 2;
	
	// 云主机运行状态
	public static final Integer CLOUD_HOST_RUNNING_STATUS_SHUTDOWN = 1;
	public static final Integer CLOUD_HOST_RUNNING_STATUS_RUNNING  = 2;
	public static final Integer CLOUD_HOST_RUNNING_STATUS_ALARM    = 3;
	public static final Integer CLOUD_HOST_RUNNING_STATUS_FAULT    = 4;
//	public static final Integer CLOUD_HOST_RUNNING_STATUS_STARTING = 5;
//	public static final Integer CLOUD_HOST_RUNNING_STATUS_SHUTINGDOWM = 6;
//	public static final Integer CLOUD_HOST_RUNNING_STATUS_RESTARTING = 7;


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
	public static final String PAGE_TITLE = "致云 ZhiCloud";
	
	
	// 镜像类型
	public static final Integer DISK_IMAGE_TYPE_COMMON = 1;
	public static final Integer DISK_IMAGE_TYPE_DESKTOP = 2;
	public static final Integer DISK_IMAGE_TYPE_SERVER = 3;
	public static final Integer DISK_IMAGE_TYPE_VPC = 4;  
	
	 
	//终端盒子分配状态
	public static final Integer TERMINAL_BOX_RELEASE = 0; //未分配
	public static final Integer TERMINAL_BOX_ALLOCATE = 1; //已分配

    //定时器名desktop_back_up
    public static final String DESTTOP_BACK_UP = "desktop_back_up"; //开机定时器
    public static final String SERVER_BACK_UP = "server_back_up"; //开机定时器
    public static final String STARTUP_TIMER = "startup_timer"; //开机定时器
    public static final String SHUTDOWN_TIMER = "shutdown_timer"; //关机定时器

    //操作类型
    public static final Integer MESSAGE_TYPE_STARTUP    = 1; //开机
    public static final Integer MESSAGE_TYPE_SHUTDOWN   = 2; //关机

    //定时任务记录类型
    public static final Integer MESSAGE_TYPE_DESKTOP_BACKUP = 1; //桌面主机备份
    public static final Integer MESSAGE_TYPE_SERVER_BACKUP = 2; //服务器主机备份
     
    public static final Integer BACK_UP_DETAIL_STATUS_SUCCESS = 1; //成功
    public static final Integer BACK_UP_DETAIL_STATUS_FAIL = 2; //失败
    public static final Integer BACK_UP_DETAIL_STATUS_EXPIRED = 3; //过期
    public static final Integer BACK_UP_DETAIL_STATUS_BACKINGUP = 4; //正在备份
    public static final Integer BACK_UP_DETAIL_STATUS_RESUMING = 5; //正在恢复
    public static final Integer BACK_UP_DETAIL_STATUS_ANOTHOR = 6; //正在备份其他版本

    //定时任务操作状态
    public static final Integer RESULT_SUCCESS = 1; //成功
    public static final Integer RESULT_FAIL = 2; //失败

    //定时任务开启状态
    public static final Integer TIMER_ENABLE = 1; // 启用
    public static final Integer TIMER_DISABLE = 2; // 启用


    //消息配置参数
    public static final String SMS_TIME_STAMP = "0";    //短信时间戳

    public static final Integer MESSAGE_TYPE_EMAIL = 0;   //邮件
    public static final Integer MESSAGE_TYPE_SMS   = 1;   //短信
    
    public static final String  WAREHOUSE_CHECK_COUNT_QUARTZ_ID="timer_for_warehouse_check_count";
    
    public static final String  BOX_CUMULATIVE_TIME_QUARTZ_ID= "timer_for_boxUser_cumulative_onlineTime";
    
    public static final String  CLOUD_HOST_RUNNING_STATUS_CHECK="cloud_host_running_status_check";
    
    //消息配置参数
    public static final Integer TENANT_STATUS_ENABLE  = 0;   //正常
    public static final Integer TENANT_STATUS_DELETE  = 9;   //删除

    //主机类型
    public static final Integer HOST_TYPE_DESKTOP  = 1;     //云桌面
    public static final Integer HOST_TYPE_SERVER  = 2;      //云服务器
    public static final Integer HOST_TYPE_VPC  = 3;         //专属云

    // 中税默认密码
    public static final String DEFAULT_USER_PASSWORD = "scgs88888";


}




