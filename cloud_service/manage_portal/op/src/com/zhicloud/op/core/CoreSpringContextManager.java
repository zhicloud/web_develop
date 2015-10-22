package com.zhicloud.op.core;

import com.zhicloud.op.service.*;
import com.zhicloud.op.service.constant.ServiceConstant;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CoreSpringContextManager {

    // --------------

    private static CoreSpringContextManager manager = null;

	public static CoreSpringContextManager getInstance() {
		if (manager != null) {
			return manager;
		}
		synchronized (CoreSpringContextManager.class) {
			if (manager == null) {
				manager = new CoreSpringContextManager();
			}
		}
		return manager;
	}

	private CoreSpringContextManager() {
		this.applicationContext = new ClassPathXmlApplicationContext("coreApplicationContext.xml");
	}

	// -----------------

	private ApplicationContext applicationContext;

	public static ApplicationContext getApplicationContext() {
		return getInstance().applicationContext;
	}

	public static Object getBean(String beanName) {
		return getApplicationContext().getBean(beanName);
	}

	// -------------------------

	public static TestService getTestService() {
		return getApplicationContext().getBean(ServiceConstant.TEST_SERVICE, TestService.class);
	}

	public static AgentService getAgentService() {
		return getApplicationContext().getBean(ServiceConstant.AGENT_SERVICE, AgentService.class);
	}

	public static PrivilegeService getPrivilegeService() {
		return getApplicationContext().getBean(ServiceConstant.PRIVILEGE_SERVICE, PrivilegeService.class);
	}

	public static ClientService getClientService() {
		return getApplicationContext().getBean(ServiceConstant.CLIENT_SERVICE, ClientService.class);
	}

	public static SysRoleService getSysRoleService() {
		return getApplicationContext().getBean(ServiceConstant.SYS_ROLE_SERVICE, SysRoleService.class);
	}

	public static SysUserService getSysUserService() {
		return getApplicationContext().getBean(ServiceConstant.SYS_USER_SERVICE, SysUserService.class);
	}

	public static SysGroupService getSysGroupService() {
		return getApplicationContext().getBean(ServiceConstant.SYS_GROUP_SERVICE, SysGroupService.class);
	}

	public static OperatorService getOperatorService() {
		return getApplicationContext().getBean(ServiceConstant.OPERATOR_SERVICE, OperatorService.class);
	}

	public static CloudHostService getCloudHostService() {
		return getApplicationContext().getBean(ServiceConstant.CLOUD_HOST_SERVICE, CloudHostService.class);
	}

	public static CloudHostBillDetailService getCloudHostBillDetailService() {
		return getApplicationContext().getBean(ServiceConstant.CLOUD_HOST_BILL_DETAIL_SERVICE, CloudHostBillDetailService.class);
	}

	public static CloudUserService getCloudUserService() {
		return getApplicationContext().getBean(ServiceConstant.CLOUD_USER_SERVICE, CloudUserService.class);
	}

	public static TerminalUserService getTerminalUserService() {
		return getApplicationContext().getBean(ServiceConstant.TERMINAL_USER_SERVICE, TerminalUserService.class);
	}

	public static SysDiskImageService getSysDiskImageService() {
		return getApplicationContext().getBean(ServiceConstant.SYS_DISK_IMAGE_SERVICE, SysDiskImageService.class);
	}

	public static BillService getBillService() {
		return getApplicationContext().getBean(ServiceConstant.BILL_SERVICE, BillService.class);
	}

	public static PackageOptionService getPackageOptionService() {
		return getApplicationContext().getBean(ServiceConstant.PACKAGE_OPTION_SERVICE, PackageOptionService.class);
	}

	public static StatementService getStatementService() {
		return getApplicationContext().getBean(ServiceConstant.STATEMENT_SERVICE, StatementService.class);
	}

	public static PaymentService getPaymentService() {
		return getApplicationContext().getBean(ServiceConstant.PAYMENT_SERVICE, PaymentService.class);
	}

	public static OrderInfoService getOrderInfoService() {
		return getApplicationContext().getBean(ServiceConstant.ORDER_INFO_SERVICE, OrderInfoService.class);
	}

	public static CloudHostWarehouseService getCloudHostWarehouseService() {
		return getApplicationContext().getBean(ServiceConstant.CLOUD_HOST_WAREHOUSE_SERVICE, CloudHostWarehouseService.class);
	}

	public static InviteCodeService getInviteCodeService() {
		return getApplicationContext().getBean(ServiceConstant.INVITE_CODE_SERVICE, InviteCodeService.class);
	}

	public static AccountBalanceService getAccountBalanceService() {
		return getApplicationContext().getBean(ServiceConstant.ACCOUNT_BALANCE_SERVICE, AccountBalanceService.class);
	}

	public static InvoiceService getInvoiceService() {
		return getApplicationContext().getBean(ServiceConstant.INVOICE_SERVICE, InvoiceService.class);
	}

	public static InvoiceAddressService getInvoiceAddressService() {
		return getApplicationContext().getBean(ServiceConstant.INVOICE_ADDRESS_SERVICE, InvoiceAddressService.class);
	}

	public static OperLogService getOperLogService() {
		return getApplicationContext().getBean(ServiceConstant.OPER_LOG_SERVICE, OperLogService.class);
	}

	public static CloudDiskService getCloudDiskService() {
		return getApplicationContext().getBean(ServiceConstant.CLOUD_DISK_SERVICE, CloudDiskService.class);
	}

	public static UserMessageService getUserMessageService() {
		return getApplicationContext().getBean(ServiceConstant.USER_MESSAGE_SERVICE, UserMessageService.class);
	}

	public static CashCouponService getCashCouponService() {
		return getApplicationContext().getBean(ServiceConstant.CASH_CONPON_SERVICE, CashCouponService.class);
	}

	public static AgentAPIService getAgentAPIService() {
		return getApplicationContext().getBean(ServiceConstant.Agent_API_SERVICE, AgentAPIService.class);
	}

	public static BusinessStatisticsService getBusinessStatisticsService() {
		return getApplicationContext().getBean(ServiceConstant.BUSINESS_STATISTICS_SERVICE, BusinessStatisticsService.class);
	}

	public static IsoImageService getIsoImageService() {
		return getApplicationContext().getBean(ServiceConstant.ISO_IMAGE_SERVICE, IsoImageService.class);
	}

	public static NetworkService getNetworkService() {
		return getApplicationContext().getBean(ServiceConstant.NETWORK_SERVICE, NetworkService.class);
	}

	public static DeviceService getDeviceService() {
		return getApplicationContext().getBean(ServiceConstant.DEVICE_SERVICE, DeviceService.class);
	}
	public static VpcBillService getVpcBillDetailService() {
		return getApplicationContext().getBean(ServiceConstant.VPC_BILL_SERVICE, VpcBillService.class);
	}
	public static CloudDiskBillService getCloudDiskBillDetailService() {
		return getApplicationContext().getBean(ServiceConstant.CLOUD_DISK_BILL_SERVICE, CloudDiskBillService.class);
	}
	public static VpcService getVpcService() {
		return getApplicationContext().getBean(ServiceConstant.VPC_SERVICE, VpcService.class);
	}
	public static MarkService getMarkService() {
		return getApplicationContext().getBean(ServiceConstant.MARK_SERVICE, MarkService.class);
	}
	public static ResourcePoolService getResourcePoolService() {
		return getApplicationContext().getBean(ServiceConstant.RESOURCE_POOL_SERVICE, ResourcePoolService.class);
	}
	public static PlatformResourceMonitorService getPlatformResourceMonitorService() {
		return getApplicationContext().getBean(ServiceConstant.PLATFORM_RESOURCE_MONITOR_SERVICE, PlatformResourceMonitorService.class);
	}  
	public static ServiceService getServiceService() {
		return getApplicationContext().getBean(ServiceConstant.SERVICE_SERVICE, ServiceService.class);
	}
	public static EggPlanService getEggPlanService() {
		return getApplicationContext().getBean(ServiceConstant.EGG_PLAN_SERVICE, EggPlanService.class);
	}
	public static ImageHostApplicationService getImageHostApplicationService() {
		return getApplicationContext().getBean(ServiceConstant.IMAGE_HOST_APPLICATION_SERVICE, ImageHostApplicationService.class);
	}

    public static EmailConfigService getEmailConfigService() {
        return getApplicationContext().getBean(ServiceConstant.EMAIL_CONFIG_SERVICE, EmailConfigService.class);
    }
	
	public static EmailTemplateService getEmailTemplateService() {
        return getApplicationContext().getBean(ServiceConstant.EMAIL_TEMPLATE_SERVICE, EmailTemplateService.class);
    }
    public static SmsConfigService getSmsConfigService() {
        return getApplicationContext().getBean(ServiceConstant.SMS_CONFIG_SERVICE, SmsConfigService.class);
    }

    public static SmsTemplateService getSmsTemplateService() {
        return getApplicationContext().getBean(ServiceConstant.SMS_TEMPLATE_SERVICE, SmsTemplateService.class);
    }
    public static MessageRecordService getMessageRecordService() {
        return getApplicationContext().getBean(ServiceConstant.MESSAGE_RECORD_SERVICE, MessageRecordService.class);
    }
    public static MonitorService getMonitorService() {
        return getApplicationContext().getBean(ServiceConstant.MONITOR_SERVICE, MonitorService.class);
    } 
    public static SysWarnService getSysWarnService() {
        return getApplicationContext().getBean(ServiceConstant.SYS_WARN_SERVICE, SysWarnService.class);
    }    
 }
