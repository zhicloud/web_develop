/**
 * Project Name:op
 * File Name:CloudDiskBillHelper.java
 * Package Name:com.zhicloud.op.app.helper
 * Date:2015年4月8日下午1:59:08
 * 
 *
*/ 

package com.zhicloud.op.app.helper; 

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.zhicloud.op.app.propeties.AppProperties;
import com.zhicloud.op.common.util.CapacityUtil;
import com.zhicloud.op.common.util.DateUtil;
import com.zhicloud.op.common.util.FlowUtil;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.core.CoreSpringContextManager;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.httpGateway.HttpGatewayChannelExt;
import com.zhicloud.op.httpGateway.HttpGatewayManager;
import com.zhicloud.op.mybatis.mapper.AccountBalanceDetailMapper;
import com.zhicloud.op.mybatis.mapper.AgentMapper;
import com.zhicloud.op.mybatis.mapper.BillDetailMapper;
import com.zhicloud.op.mybatis.mapper.BillMapper;
import com.zhicloud.op.mybatis.mapper.CloudDiskBillDetailMapper;
import com.zhicloud.op.mybatis.mapper.CloudDiskMapper;
import com.zhicloud.op.mybatis.mapper.CloudHostBillDetailMapper;
import com.zhicloud.op.mybatis.mapper.CloudHostMapper;
import com.zhicloud.op.mybatis.mapper.PackagePriceMapper;
import com.zhicloud.op.mybatis.mapper.PriceUpdateMapper;
import com.zhicloud.op.mybatis.mapper.TerminalUserMapper;
import com.zhicloud.op.mybatis.mapper.UserMessageMapper;
import com.zhicloud.op.mybatis.mapper.VpcBaseInfoMapper;
import com.zhicloud.op.mybatis.mapper.VpcBillDetailMapper;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.service.NetworkService;
import com.zhicloud.op.service.constant.AppConstant;
import com.zhicloud.op.vo.AgentVO;
import com.zhicloud.op.vo.CloudDiskBillDetailVO;
import com.zhicloud.op.vo.CloudDiskVO;
import com.zhicloud.op.vo.CloudHostBillDetailVO;
import com.zhicloud.op.vo.CloudHostVO;
import com.zhicloud.op.vo.PackagePriceVO;
import com.zhicloud.op.vo.TerminalUserVO;
import com.zhicloud.op.vo.VpcBaseInfoVO;
import com.zhicloud.op.vo.VpcBillDetailVO;

/**
 * ClassName: CloudDiskBillHelper 
 * Function: 云磁盘计费
 * date: 2015年4月8日 下午1:59:08 
 *
 * @author sasa
 * @version 
 * @since JDK 1.7
 */
public class CloudDiskBillHelper {
	
private static final Logger logger = Logger.getLogger(CloudHostBillingHelper.class);
	
	private SqlSession sqlSession;
	
	public CloudDiskBillHelper(SqlSession sqlSession)
	{
		this.sqlSession = sqlSession;
	}
	
	/**
	 * 从所有还没有结算的正在计费的的账单中获取出一条进行结算
	 */
	public MethodResult startOneCloudDiskBilling(Date billingEndTime, boolean startNewBillDetail) throws ParseException
	{
		CloudDiskBillDetailMapper cloudDiskBillDetailMapper = this.sqlSession.getMapper(CloudDiskBillDetailMapper.class);
		
		String strEndTime = DateUtil.dateToString(billingEndTime, "yyyyMMddHHmmssSSS");
		
		// 获取end_time is null的记录，表示计费还没有完成的记录
		CloudDiskBillDetailVO cloudDiskBillDetailVO = cloudDiskBillDetailMapper.getOneUndoneReocrdBeforeTime(strEndTime);
		if( cloudDiskBillDetailVO == null )
		{
			logger.info("CloudDiskBillHelper.startOneVpcBilling() > ["+Thread.currentThread().getId()+"] no_undone_bill_detail_record");
			return new MethodResult(MethodResult.SUCCESS, "no_undone_bill_detail_record");
		}
		
		return doCloudDiskBilling(cloudDiskBillDetailVO, billingEndTime, startNewBillDetail);
	}
	 
	
	/**
	 * @param cloudHostBillDetailVO: 要结算的那条云主机计费明细
	 * @param billingEndTime:        结算结束时间
	 * @param startNewBillDetail:    完成结算之后是否开启一条新的结算
	 */
	private MethodResult doCloudDiskBilling(CloudDiskBillDetailVO cloudDiskBillDetailVO, Date billingEndTime, boolean startNewBillDetail) throws ParseException
	{

		TerminalUserMapper terminalUserMapper                 = this.sqlSession.getMapper(TerminalUserMapper.class);
		AccountBalanceDetailMapper accountBalanceDetailMapper = this.sqlSession.getMapper(AccountBalanceDetailMapper.class);
		CloudDiskMapper cloudDiskMapper                       = this.sqlSession.getMapper(CloudDiskMapper.class);
		CloudHostMapper cloudHostMapper                       = this.sqlSession.getMapper(CloudHostMapper.class);
		BillMapper billMapper                                 = this.sqlSession.getMapper(BillMapper.class);
		BillDetailMapper billDetailMapper                     = this.sqlSession.getMapper(BillDetailMapper.class);
		CloudDiskBillDetailMapper cloudDiskBillDetailMapper   = this.sqlSession.getMapper(CloudDiskBillDetailMapper.class);
		CloudHostBillDetailMapper cloudHostBillDetailMapper   = this.sqlSession.getMapper(CloudHostBillDetailMapper.class);
		VpcBillDetailMapper vpcBillDetailMapper   = this.sqlSession.getMapper(VpcBillDetailMapper.class);
		VpcBaseInfoMapper vpcBaseInfoMapper   = this.sqlSession.getMapper(VpcBaseInfoMapper.class);
 		
		String strNow            = DateUtil.dateToString(new Date(),     "yyyyMMddHHmmssSSS");
		String strBillingEndTime = DateUtil.dateToString(billingEndTime, "yyyyMMddHHmmssSSS");
		// 计算24：00的费用，新的账单也从24点开始去算
//		String strBillingEndTime = DateUtil.dateToString(billingEndTime, "yyyyMMdd")+"000000000";
		
		cloudDiskBillDetailVO.setEndTime(strBillingEndTime);
		Date billingStartTime          = DateUtil.stringToDate(cloudDiskBillDetailVO.getStartTime(), "yyyyMMddHHmmssSSS");
		// 获取硬盘
		CloudDiskVO  cloudDiskVO =  cloudDiskMapper.getCloudDiskById(cloudDiskBillDetailVO.getDiskId());
 		BigDecimal totalUserPrice = BigDecimal.ZERO;
  		// 计算硬盘价格
//		totalUserPrice =      
		
		
		
		BigDecimal nowPrice = BigDecimal.ZERO;  
		//如果硬盘是代理商创建
		if(cloudDiskVO.getType() == AppConstant.CLOUD_DISK_TYPE_2_AGENT){
			
			// 获取代理商用户信息
			AgentMapper agentMapper   = this.sqlSession.getMapper(AgentMapper.class);
			AgentVO agentVO = agentMapper.getAgentByUserId(cloudDiskVO.getUserId());
			// 若代理商用户不存在或者用户余额已为0
			if( agentVO==null)
			{
				logger.info("CloudDiskBillHelper.startOneCloudHostBilling() > ["+Thread.currentThread().getId()+"] user_not_found, id:["+cloudDiskVO.getUserId()+"]");
				// 删除这条没用的记录
				cloudDiskBillDetailMapper.deleteCloudDiskBillDetailById(cloudDiskVO.getId());
				return new MethodResult(MethodResult.FAIL, "user_not_found");
			}
			if(agentVO.getAccountBalance().compareTo(BigDecimal.ZERO)<=0){
				// 如果余额小于或等于0元，将代理商用户的下属终端用户的的全部产品置为停机
				logger.info("CloudDiskBillHelper.startOneCloudHostBilling() > ["+Thread.currentThread().getId()+"] balance_not_enough, id:["+cloudDiskVO.getUserId()+"]");
				List<TerminalUserVO> terminalUserList = terminalUserMapper.getTerminalUserFromAgent(agentVO.getId());
				for(TerminalUserVO terminalUser : terminalUserList){
					
					Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
					cloudHostData.put("status",  AppConstant.CLOUD_HOST_STATUS_2_HALT);
					cloudHostData.put("inactivateTime",  strNow);
					cloudHostData.put("userId",  terminalUser.getId());
					cloudHostData.put("type",  AppConstant.CLOUD_HOST_TYPE_2_AGENT);
					cloudHostMapper.updateUserHostStatusByUserId(cloudHostData);
					
					List<CloudHostVO> cloudHostList = cloudHostMapper.getByUserId(terminalUser.getId());
					if(cloudHostList!=null&&cloudHostList.size()>0){
						for(CloudHostVO vo:cloudHostList){
							//将所有开机状态的云主机关机
							if(vo.getType()==AppConstant.CLOUD_HOST_TYPE_2_AGENT){
								
								if(!(vo.getSummarizedStatusText().equals("创建失败")||vo.getSummarizedStatusText().equals("创建中"))&&vo.getRunningStatus()!=1){						
									HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(vo.getRegion());
									try {
										JSONObject haltResullt = channel.hostStop(vo.getRealHostId());
									} catch (MalformedURLException e) {  
										logger.error(e);
									} catch (IOException e) { 
										logger.error(e);
									}
								}
							}
						} 
						
					}
 					//删除该用户下所有未结算的账单 
					Map<String, Object> data = new LinkedHashMap<String, Object>();
	 				data.put("userId",  terminalUser.getId());
					data.put("type",  AppConstant.CLOUD_HOST_TYPE_2_AGENT);
					cloudHostBillDetailMapper.deleteAllNotPaid(data); 
				}
				
				return new MethodResult(MethodResult.FAIL, "balance_not_enough");
				
			}
			
			// 计费该账单的费用
			billingStartTime          = DateUtil.stringToDate(cloudDiskBillDetailVO.getStartTime(), "yyyyMMddHHmmssSSS");
			//计算折扣后的价格
 			BigDecimal monthlyPrice = CloudDiskPrice.getCloudDiskPrice(cloudDiskVO.getRegion()+"", CapacityUtil.toGBValue(cloudDiskVO.getDisk(),   0, BigDecimal.ROUND_UP).intValue()+"");
			BigDecimal fee                 = CloudHostPrice.getPeriodPrice(monthlyPrice, billingEndTime.getTime() - billingStartTime.getTime()).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal balanceBeforeChange = agentVO.getAccountBalance();
			BigDecimal balanceAfterChange  = agentVO.getAccountBalance().subtract(fee);
			if(fee.compareTo(BigDecimal.ZERO)==0){
				// 账单金额为0，直接删除，不结算
				cloudDiskBillDetailMapper.deleteCloudDiskBillDetailById(cloudDiskBillDetailVO.getId());
				logger.info("cloud disk "+cloudDiskBillDetailVO.getDiskId()+" fee is zero ,delete the bill,start new bill [" +startNewBillDetail+"]");
				//开始新的计费周期
				if( startNewBillDetail==true )
				{					
					Map<String, Object> cloudDiskBillDetailData2 = new LinkedHashMap<String, Object>();
					cloudDiskBillDetailData2.put("id",               StringUtil.generateUUID());                         
					cloudDiskBillDetailData2.put("diskId",          cloudDiskBillDetailVO.getDiskId());                         
					cloudDiskBillDetailData2.put("type",             AppConstant.CLOUD_HOST_BILL_DETAIL_TYPE_4_PAY_FOR_TIME);                         
					cloudDiskBillDetailData2.put("disk",          cloudDiskBillDetailVO.getDisk());                            
					cloudDiskBillDetailData2.put("startTime",        strBillingEndTime);                         
					cloudDiskBillDetailData2.put("endTime",          null);                         
					cloudDiskBillDetailData2.put("fee",              null);                         
					cloudDiskBillDetailData2.put("isPaid",           AppConstant.CLOUD_HOST_BILL_DETAIL_IS_PAID_NOT);                         
					cloudDiskBillDetailData2.put("createTime",       strNow);                         
					cloudDiskBillDetailMapper.addCloudDiskBillDetail(cloudDiskBillDetailData2);
				}
				return new MethodResult(MethodResult.FAIL, "fee_is_zero");
				
			}
			if(fee.compareTo(balanceBeforeChange)>=0){
				balanceAfterChange = BigDecimal.ZERO;
				fee = balanceBeforeChange;
			}
			
			// 更新云主机账单明细表的费用
			Map<String, Object> cloudDiskBillDetailData = new LinkedHashMap<String, Object>();
			cloudDiskBillDetailData.put("fee",     fee);
			cloudDiskBillDetailData.put("isPaid",  AppConstant.CLOUD_HOST_BILL_DETAIL_IS_PAID_YES);
			cloudDiskBillDetailData.put("endTime", strBillingEndTime);
			cloudDiskBillDetailData.put("id",      cloudDiskBillDetailVO.getId());
			int i = cloudHostBillDetailMapper.updateFeeById(cloudDiskBillDetailData);
			if(i<=0){
				logger.info("cloud disk "+cloudDiskBillDetailVO.getDiskId()+" order "+cloudDiskBillDetailVO.getId()+" is paid ");
				return new MethodResult(MethodResult.SUCCESS, "fee_is_paid");

			}
			
			// 生成账单表的记录
			Map<String, Object> billData = new LinkedHashMap<String, Object>();
			billData.put("id",           StringUtil.generateUUID());
			billData.put("userId",       agentVO.getId());
			billData.put("fee",          fee);
			billData.put("region",       cloudDiskVO.getRegion());
			billData.put("resourceName", cloudDiskVO.getName());
			billData.put("isPaid",       AppConstant.CLOUD_HOST_BILL_DETAIL_IS_PAID_YES);
			billData.put("payableTime", strBillingEndTime);
			billData.put("paymentTime", strNow);
			billData.put("createTime",  strNow);
			billMapper.addBill(billData);
			
			// 生成账单明细表的记录
			Map<String, Object> billDetailData = new LinkedHashMap<String, Object>();
			billDetailData.put("id",       StringUtil.generateUUID());
			billDetailData.put("billId",   billData.get("id"));
			billDetailData.put("itemType", AppConstant.BILL_DETAIL_ITEM_TYPE_CLOUD_DISK);
			billDetailData.put("itemId",   cloudDiskBillDetailVO.getId());
			billDetailMapper.addBillDetail(billDetailData);
			
			// 从终端用户表的账户余额扣除费用
			Map<String, Object> userData = new LinkedHashMap<String, Object>();
			userData.put("accountBalance", balanceAfterChange);
			userData.put("id",             agentVO.getId()); 
			agentMapper.updateBalanceById(userData);
			
			// 添加余额变动明细记录
			Map<String, Object> accountBalanceDetailData = new LinkedHashMap<String, Object>();
			accountBalanceDetailData.put("id",                   StringUtil.generateUUID());
			accountBalanceDetailData.put("userId",               agentVO.getId());
			accountBalanceDetailData.put("type",                 AppConstant.ACCOUNT_BALANCE_DETAIL_TYPE_CONSUMPTION);
			accountBalanceDetailData.put("amount",               fee);
			accountBalanceDetailData.put("balanceBeforeChange",  balanceBeforeChange);
			accountBalanceDetailData.put("balanceAfterChange",   balanceAfterChange);
			accountBalanceDetailData.put("payType",              AppConstant.ACCOUNT_BALANCE_DETAIL_PAY_TYPE_4_FEE_DEFUCTION);
			accountBalanceDetailData.put("description",          "云主机费用扣除");
			accountBalanceDetailData.put("changeTime",           strNow);
			accountBalanceDetailData.put("invoiceId",            null);
			accountBalanceDetailData.put("billId",               billData.get("id"));
			accountBalanceDetailData.put("rechargeStatus",       null);
			accountBalanceDetailData.put("resourceName",         cloudDiskVO.getName()); 
			accountBalanceDetailMapper.addAccountBalanceDetail(accountBalanceDetailData);
			
			if( balanceAfterChange.compareTo(new BigDecimal(0)) > 0 )
			{
				if( startNewBillDetail==true )
				{
					// 如果余额大于0，开始新的计费周期
					Map<String, Object> cloudDiskBillDetailData2 = new LinkedHashMap<String, Object>();
					cloudDiskBillDetailData2.put("id",               StringUtil.generateUUID());                         
					cloudDiskBillDetailData2.put("disk_id",          cloudDiskBillDetailVO.getDiskId());                         
					cloudDiskBillDetailData2.put("type",             AppConstant.CLOUD_HOST_BILL_DETAIL_TYPE_4_PAY_FOR_TIME);                         
					cloudDiskBillDetailData2.put("disk",          cloudDiskBillDetailVO.getDisk());                            
					cloudDiskBillDetailData2.put("startTime",        strBillingEndTime);                         
					cloudDiskBillDetailData2.put("endTime",          null);                         
					cloudDiskBillDetailData2.put("fee",              null);                         
					cloudDiskBillDetailData2.put("isPaid",           AppConstant.CLOUD_HOST_BILL_DETAIL_IS_PAID_NOT);                         
					cloudDiskBillDetailData2.put("createTime",       strNow);                         
					cloudDiskBillDetailMapper.addCloudDiskBillDetail(cloudDiskBillDetailData2); 
				}
			}
			else
			{
				List<TerminalUserVO> terminalUserList = terminalUserMapper.getTerminalUserFromAgent(agentVO.getId());
				for(TerminalUserVO terminalUser : terminalUserList){
					
					Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
					cloudHostData.put("status",  AppConstant.CLOUD_HOST_STATUS_2_HALT);
					cloudHostData.put("inactivateTime",  strNow);
					cloudHostData.put("userId",  terminalUser.getId());
					cloudHostData.put("type",  AppConstant.CLOUD_HOST_TYPE_2_AGENT);
					cloudHostMapper.updateUserHostStatusByUserId(cloudHostData);
					
					List<CloudHostVO> cloudHostList = cloudHostMapper.getByUserId(terminalUser.getId());
					if(cloudHostList!=null&&cloudHostList.size()>0){
						for(CloudHostVO vo:cloudHostList){
							//将所有开机状态的云主机关机
							if(vo.getType()==AppConstant.CLOUD_HOST_TYPE_2_AGENT){
								
								if(!(vo.getSummarizedStatusText().equals("创建失败")||vo.getSummarizedStatusText().equals("创建中"))&&vo.getRunningStatus()!=1){						
									HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(vo.getRegion());
									try {
										JSONObject haltResullt = channel.hostStop(vo.getRealHostId());
									} catch (MalformedURLException e) {  
										logger.error(e);
									} catch (IOException e) { 
										logger.error(e);
									}
								}
							}
						} 
						
					} 
 					//删除该用户下所有未结算的账单
					Map<String, Object> data = new LinkedHashMap<String, Object>();
	 				data.put("userId",  terminalUser.getId());
					data.put("type",  AppConstant.CLOUD_HOST_TYPE_2_AGENT);
					cloudHostBillDetailMapper.deleteAllNotPaid(data); 
				}
			}
			
			logger.info("CloudDiskBillHelper.startOneCloudHostBilling() > ["+Thread.currentThread().getId()+"] finish a chargeback record, uuid:["+cloudDiskBillDetailVO.getId()
					+"], user:["+agentVO.getAccount()
					+"], cloudhost:["+cloudDiskVO.getName()
					+"], starttime:["+DateUtil.formatDateString(cloudDiskBillDetailVO.getStartTime(), "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss.SSS")
					+"], endtime:["+DateUtil.formatDateString(strBillingEndTime,                    "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss.SSS")
					+"], fee:["+fee
					+"], balance:["+balanceAfterChange
					+"]");
			
		}else{
			
			// 获取用户信息
			TerminalUserVO terminalUserVO = terminalUserMapper.getBaseInfoById(cloudDiskVO.getUserId());
			// 若用户不存在或者用户余额已为0
			if( terminalUserVO==null)
			{
					return new MethodResult(MethodResult.FAIL, "user_not_found");
			}
			 
			
			// 计费该账单的费用
			BigDecimal fee = new BigDecimal("0");
 			billingStartTime          = DateUtil.stringToDate(cloudDiskBillDetailVO.getStartTime(), "yyyyMMddHHmmssSSS");
 			BigDecimal monthlyPrice = CloudDiskPrice.getCloudDiskPrice(cloudDiskVO.getRegion()+"", CapacityUtil.toGBValue(cloudDiskVO.getDisk(),   0, BigDecimal.ROUND_UP).intValue()+"");
			BigDecimal price               = CloudHostPrice.getPeriodPrice(monthlyPrice, billingEndTime.getTime() - billingStartTime.getTime()).setScale(2, BigDecimal.ROUND_HALF_UP);
			if (terminalUserVO.getPercentOff()==null) 
			{
				fee = price; 
			}
			if (terminalUserVO.getPercentOff()!=null) 
			{
				fee                = price.multiply(new BigDecimal(100).subtract(terminalUserVO.getPercentOff())).divide(new BigDecimal(100), 2, BigDecimal.ROUND_HALF_UP);
			}
			// 加上该云主机的费用
			totalUserPrice = totalUserPrice.add(fee);  
			
		// 更新云主机账单明细表的费用
			Map<String, Object> cloudHostBillDetailData = new LinkedHashMap<String, Object>();
			cloudHostBillDetailData.put("fee",     fee);
			cloudHostBillDetailData.put("isPaid",  AppConstant.CLOUD_HOST_BILL_DETAIL_IS_PAID_YES);
			cloudHostBillDetailData.put("endTime", strBillingEndTime);
			cloudHostBillDetailData.put("id",      cloudDiskBillDetailVO.getId());
			int i = cloudHostBillDetailMapper.updateFeeById(cloudHostBillDetailData);  
			
		} 
		
		// 统计出来的价格一起结算
		TerminalUserVO terminalUserVO = terminalUserMapper.getBaseInfoById(cloudDiskVO.getUserId());
		if(terminalUserVO != null){
			  if(terminalUserVO.getAccountBalance().compareTo(BigDecimal.ZERO)<=0){
					logger.info("CloudDiskBillDetailServiceImpl.startOneVpcBilling() > ["+Thread.currentThread().getId()+"] balance_not_enough, id:["+cloudDiskVO.getUserId()+"]");
					// 如果余额小于或等于0元，停用所有硬盘
					Map<String, Object> data = new LinkedHashMap<String, Object>();
					data.put("userId",     cloudDiskVO.getUserId());
					data.put("type",     AppConstant.CLOUD_DISK_TYPE_1_TERMINAL_USER);
					data.put("inactivateTime",     strNow);
					cloudDiskMapper.updateCloudDisk(data);
				 
				
				UserMessageMapper userMessageMapper =this.sqlSession.getMapper(UserMessageMapper.class);
				Map<String, Object> userMessage = new LinkedHashMap<String, Object>();
				userMessage.put("id", StringUtil.generateUUID()); 
				userMessage.put("userId", terminalUserVO.getId());
				userMessage.put("content", "您的余额不足，请充值");
				userMessage.put("createTime", strNow);
				userMessage.put("status", "2");
				userMessageMapper.insertReg(userMessage);
				
				userMessage.put("id", StringUtil.generateUUID()); 
				userMessage.put("userId", terminalUserVO.getId());
				userMessage.put("content", "您的云硬盘已因欠费停用，请及时充值恢复服务");
				userMessage.put("createTime", strNow);
				userMessage.put("status", "4");
				userMessageMapper.insertReg(userMessage); 
   					
			}else{
				BigDecimal balanceBeforeChange = terminalUserVO.getAccountBalance();  
				BigDecimal balanceAfterChange  = terminalUserVO.getAccountBalance().subtract(totalUserPrice);
				if(totalUserPrice.compareTo(balanceBeforeChange)>=0){
					balanceAfterChange = BigDecimal.ZERO;
					totalUserPrice = balanceBeforeChange;
				}
			    // 更新硬盘账单明细表的费用
				Map<String, Object> data = new LinkedHashMap<String, Object>();
				data.put("fee",     totalUserPrice);
				data.put("isPaid",  AppConstant.CLOUD_HOST_BILL_DETAIL_IS_PAID_YES);
				data.put("endTime", strBillingEndTime);
				data.put("id",      cloudDiskBillDetailVO.getId()); 
				int i = cloudDiskBillDetailMapper.updateBillAfterPay(data);
			   // 从终端用户表的账户余额扣除费用
				Map<String, Object> userData = new LinkedHashMap<String, Object>();
				userData.put("accountBalance", balanceAfterChange);
				userData.put("id",             terminalUserVO.getId()); 
				terminalUserMapper.updateBalanceById(userData);
				
			// 生成账单表的记录
				Map<String, Object> billData = new LinkedHashMap<String, Object>();
				billData.put("id",           StringUtil.generateUUID());
				billData.put("userId",       terminalUserVO.getId());
				billData.put("fee",          totalUserPrice);
				billData.put("region",       cloudDiskVO.getRegion());
				billData.put("resourceName", cloudDiskVO.getName());
				billData.put("isPaid",       AppConstant.CLOUD_HOST_BILL_DETAIL_IS_PAID_YES);
				billData.put("payableTime", strBillingEndTime);
				billData.put("paymentTime", strNow);
				billData.put("createTime",  strNow);
				billMapper.addBill(billData);
				
				// 生成账单明细表的记录
				Map<String, Object> billDetailData = new LinkedHashMap<String, Object>();
				billDetailData.put("id",       StringUtil.generateUUID());
				billDetailData.put("billId",   billData.get("id"));
				billDetailData.put("itemType", AppConstant.BILL_DETAIL_ITEM_TYPE_VPC);
				billDetailData.put("itemId",   cloudDiskBillDetailVO.getId());
				billDetailMapper.addBillDetail(billDetailData);
				
				// 添加余额变动明细记录
				Map<String, Object> accountBalanceDetailData = new LinkedHashMap<String, Object>();
				accountBalanceDetailData.put("id",                   StringUtil.generateUUID());
				accountBalanceDetailData.put("userId",               terminalUserVO.getId());
				accountBalanceDetailData.put("type",                 AppConstant.ACCOUNT_BALANCE_DETAIL_TYPE_CONSUMPTION);
				accountBalanceDetailData.put("amount",               totalUserPrice);
				accountBalanceDetailData.put("balanceBeforeChange",  balanceBeforeChange);
				accountBalanceDetailData.put("balanceAfterChange",   balanceAfterChange);
				accountBalanceDetailData.put("payType",              AppConstant.ACCOUNT_BALANCE_DETAIL_PAY_TYPE_4_FEE_DEFUCTION);
				accountBalanceDetailData.put("description",          "VPC费用扣除");
				accountBalanceDetailData.put("changeTime",           strNow);
				accountBalanceDetailData.put("invoiceId",            null);
				accountBalanceDetailData.put("billId",               billData.get("id"));
				accountBalanceDetailData.put("rechargeStatus",       null);
				accountBalanceDetailData.put("resourceName",         cloudDiskVO.getName()); 
				accountBalanceDetailMapper.addAccountBalanceDetail(accountBalanceDetailData);
				//结算后账单为0，,停用VPC
				if(balanceAfterChange.compareTo(BigDecimal.ZERO) == 0){
					// 如果余额小于或等于0元，停用所有硬盘
 					data.put("userId",     cloudDiskVO.getUserId());
					data.put("type",     AppConstant.CLOUD_DISK_TYPE_1_TERMINAL_USER);
					data.put("inactivateTime",     strNow);
					cloudDiskMapper.updateCloudDisk(data);
				}else{
					// 是否产生新的账单
					if(startNewBillDetail){
						//产生新的账单 
						Map<String, Object> newbill = new LinkedHashMap<String, Object>();
						newbill.put("id", StringUtil.generateUUID());
						newbill.put("diskId", cloudDiskVO.getId());
						newbill.put("disk", cloudDiskVO.getDisk());
						newbill.put("createTime", strNow);
						newbill.put("startTime", strNow);
						cloudDiskBillDetailMapper.addCloudDiskBillDetail(newbill);
						
						 
						}
					}  
			}
		}
		return new MethodResult(MethodResult.SUCCESS, AppConstant.SUCCESS); 
		}
	
	 

}

