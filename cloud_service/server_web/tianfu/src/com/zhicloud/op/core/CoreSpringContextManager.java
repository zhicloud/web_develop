package com.zhicloud.op.core;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zhicloud.op.service.AccountBalanceService;
import com.zhicloud.op.service.AgentAPIService;
import com.zhicloud.op.service.AgentService;
import com.zhicloud.op.service.BillService;
import com.zhicloud.op.service.BusinessStatisticsService;
import com.zhicloud.op.service.CashCouponService;
import com.zhicloud.op.service.ClientService;
import com.zhicloud.op.service.CloudDiskService;
import com.zhicloud.op.service.CloudHostBillDetailService;
import com.zhicloud.op.service.CloudHostService;
import com.zhicloud.op.service.CloudHostWarehouseService;
import com.zhicloud.op.service.CloudUserService;
import com.zhicloud.op.service.DeviceService;
import com.zhicloud.op.service.InviteCodeService;
import com.zhicloud.op.service.InvoiceAddressService;
import com.zhicloud.op.service.InvoiceService;
import com.zhicloud.op.service.IsoImageService;
import com.zhicloud.op.service.NetworkService;
import com.zhicloud.op.service.OperLogService;
import com.zhicloud.op.service.OperatorService;
import com.zhicloud.op.service.OrderInfoService;
import com.zhicloud.op.service.PackageOptionService;
import com.zhicloud.op.service.PaymentService;
import com.zhicloud.op.service.PrivilegeService;
import com.zhicloud.op.service.StatementService;
import com.zhicloud.op.service.SysDiskImageService;
import com.zhicloud.op.service.SysGroupService;
import com.zhicloud.op.service.SysRoleService;
import com.zhicloud.op.service.SysUserService;
import com.zhicloud.op.service.TerminalUserService;
import com.zhicloud.op.service.TestService;
import com.zhicloud.op.service.UserMessageService;
import com.zhicloud.op.service.VpcService;
import com.zhicloud.op.service.constant.ServiceConstant;

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
	public static VpcService getVpcService() {
		return getApplicationContext().getBean(ServiceConstant.VPC_SERVICE, VpcService.class);
	}
}
