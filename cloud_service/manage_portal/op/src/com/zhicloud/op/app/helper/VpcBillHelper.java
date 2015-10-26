/**
 * Project Name:op
 * File Name:VpcBillHelper.java
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
import com.zhicloud.op.vo.CloudHostBillDetailVO;
import com.zhicloud.op.vo.CloudHostVO;
import com.zhicloud.op.vo.PackagePriceVO;
import com.zhicloud.op.vo.PriceVO;
import com.zhicloud.op.vo.TerminalUserVO;
import com.zhicloud.op.vo.VpcBaseInfoVO;
import com.zhicloud.op.vo.VpcBillDetailVO;

/**
 * ClassName: VpcBillHelper 
 * Function: VPC计费
 * date: 2015年4月8日 下午1:59:08 
 *
 * @author sasa
 * @version 
 * @since JDK 1.7
 */
public class VpcBillHelper {
	
private static final Logger logger = Logger.getLogger(CloudHostBillingHelper.class);
	
	private SqlSession sqlSession;
	
	public VpcBillHelper(SqlSession sqlSession)
	{
		this.sqlSession = sqlSession;
	}
	
	/**
	 * 从所有还没有结算的正在计费的云主机的账单中获取出一条进行结算
	 */
	public MethodResult startOneVpcBilling(Date billingEndTime, boolean startNewBillDetail) throws ParseException
	{
		VpcBillDetailMapper vpcBillDetailMapper = this.sqlSession.getMapper(VpcBillDetailMapper.class);
		
		String strEndTime = DateUtil.dateToString(billingEndTime, "yyyyMMddHHmmssSSS");
		
		// 获取end_time is null的记录，表示计费还没有完成的记录
		VpcBillDetailVO vpcBillDetailVO = vpcBillDetailMapper.getOneUndoneReocrdBeforeTime(strEndTime);
		if( vpcBillDetailVO==null )
		{
			logger.info("VpcBillDetailServiceImpl.startOneVpcBilling() > ["+Thread.currentThread().getId()+"] no_undone_bill_detail_record");
			return new MethodResult(MethodResult.SUCCESS, "no_undone_bill_detail_record");
		}
		
		return doVpcBilling(vpcBillDetailVO, billingEndTime, startNewBillDetail);
	}
	 
	
	/**
	 * @param cloudHostBillDetailVO: 要结算的那条云主机计费明细
	 * @param billingEndTime:        结算结束时间
	 * @param startNewBillDetail:    完成结算之后是否开启一条新的结算
	 */
	private MethodResult doVpcBilling(VpcBillDetailVO vpcBillDetailVO, Date billingEndTime, boolean startNewBillDetail) throws ParseException
	{

		TerminalUserMapper terminalUserMapper                 = this.sqlSession.getMapper(TerminalUserMapper.class);
		AccountBalanceDetailMapper accountBalanceDetailMapper = this.sqlSession.getMapper(AccountBalanceDetailMapper.class);
		CloudHostMapper cloudHostMapper                       = this.sqlSession.getMapper(CloudHostMapper.class);
		BillMapper billMapper                                 = this.sqlSession.getMapper(BillMapper.class);
		BillDetailMapper billDetailMapper                     = this.sqlSession.getMapper(BillDetailMapper.class);
		CloudHostBillDetailMapper cloudHostBillDetailMapper   = this.sqlSession.getMapper(CloudHostBillDetailMapper.class);
		VpcBillDetailMapper vpcBillDetailMapper   = this.sqlSession.getMapper(VpcBillDetailMapper.class);
		VpcBaseInfoMapper vpcBaseInfoMapper   = this.sqlSession.getMapper(VpcBaseInfoMapper.class);
		AgentMapper agentMapper   = this.sqlSession.getMapper(AgentMapper.class);
 		
		String strNow            = DateUtil.dateToString(new Date(),     "yyyyMMddHHmmssSSS");
		String strBillingEndTime = DateUtil.dateToString(billingEndTime, "yyyyMMddHHmmssSSS");
		// 计算24：00的费用，新的账单也从24点开始去算
//		String strBillingEndTime = DateUtil.dateToString(billingEndTime, "yyyyMMdd")+"000000000";
		// 查询Vpc所有未结算的账单
		Map<String, Object> data1 = new LinkedHashMap<String, Object>();
		data1.put("vpcId",     vpcBillDetailVO.getVpcId());
		List<VpcBillDetailVO> billList = vpcBillDetailMapper.getAllUndoneReocrdByVpcId(data1);
		if(billList!=null && billList.size()>1){
			for(VpcBillDetailVO bill : billList){
				if(bill.getId()!=vpcBillDetailVO.getId()){
					vpcBillDetailMapper.deleteVpcBillDetailById(bill.getId());
				}
			}
		}
		
		vpcBillDetailVO.setEndTime(strBillingEndTime);
		Date billingStartTime          = DateUtil.stringToDate(vpcBillDetailVO.getStartTime(), "yyyyMMddHHmmssSSS");
		// 获取云主机信息
		VpcBaseInfoVO  vpcBaseInfo = vpcBaseInfoMapper.queryVpcById(vpcBillDetailVO.getVpcId()); 
		if(vpcBaseInfo == null || vpcBaseInfo.getStatus() == 2 || vpcBaseInfo.getStatus() == 3){
 				// 删除这条没用的记录
			    vpcBillDetailMapper.deleteVpcBillDetailById(vpcBillDetailVO.getId());
				return new MethodResult(MethodResult.FAIL, "vpc_cant_pay");
		}
		List<CloudHostVO> cloudHostVOs = cloudHostMapper.getCloudHostInVpc(vpcBillDetailVO.getVpcId());
		//用户应扣总额
		BigDecimal totalUserPrice = BigDecimal.ZERO;
		//代理商应扣总额
		BigDecimal totalAgentPrice = BigDecimal.ZERO;
		//计算Vpc实际的价格
		BigDecimal vpcPrice = BigDecimal.ZERO;
		// 加上VPC价格
		vpcPrice = VPCAmountAndPrice.getVpcPrice(vpcBaseInfo.getRegion()+"", vpcBaseInfo.getHostAmount()+"");
		vpcPrice = vpcPrice.add(new BigDecimal(AppProperties.getValue("ip_price_"+vpcBaseInfo.getRegion(), "")));
		if(vpcBaseInfo.getType() == 1){			
			totalUserPrice = totalUserPrice.add(VpcBillHelper.getPeriodPrice( VPCAmountAndPrice.getVpcPrice(vpcBaseInfo.getRegion()+"", vpcBaseInfo.getHostAmount()+""), billingEndTime.getTime() - billingStartTime.getTime()).setScale(2, BigDecimal.ROUND_HALF_UP));
			totalUserPrice = totalUserPrice.add(VpcBillHelper.getPeriodPrice( new BigDecimal(AppProperties.getValue("ip_price_"+vpcBaseInfo.getRegion(), "")).multiply(new BigDecimal(vpcBaseInfo.getIpAmount())), billingEndTime.getTime() - billingStartTime.getTime()).setScale(2, BigDecimal.ROUND_HALF_UP));
		}else{
			totalAgentPrice = totalUserPrice.add(VpcBillHelper.getPeriodPrice( VPCAmountAndPrice.getVpcPrice(vpcBaseInfo.getRegion()+"", vpcBaseInfo.getHostAmount()+""), billingEndTime.getTime() - billingStartTime.getTime()).setScale(2, BigDecimal.ROUND_HALF_UP));
			totalAgentPrice = totalUserPrice.add(VpcBillHelper.getPeriodPrice( new BigDecimal(AppProperties.getValue("ip_price_"+vpcBaseInfo.getRegion(), "")).multiply(new BigDecimal(vpcBaseInfo.getIpAmount())), billingEndTime.getTime() - billingStartTime.getTime()).setScale(2, BigDecimal.ROUND_HALF_UP));
			
		}
		// 加上IP的价格
  		
		if( cloudHostVOs==null )
		{
			logger.info("VpcBillDetailServiceImpl.startOneVpcBilling() > ["+Thread.currentThread().getId()+"] cloud_host_not_found, vpc_id:["+vpcBaseInfo.getId()+"]");
  		}else{
  			
  			for(CloudHostVO cloudHostVO : cloudHostVOs){
  				if(StringUtil.isBlank(cloudHostVO.getRealHostId())){
  					continue;
  				}
  				// 查询云主机所有未结算的账单 , 若有多条，除了真在结算的，其余的都删除
  				Map<String, Object> condition = new LinkedHashMap<String, Object>();
  				condition.put("hostId",     cloudHostVO.getId());
  				condition.put("beforeTime", strNow);
  				// 默认结算首条计费信息
  				List<CloudHostBillDetailVO> cloudHostBillDetailVOList = cloudHostBillDetailMapper.getAllUndoneByHostIdBeforeTime(condition); 
  				if(cloudHostBillDetailVOList == null || cloudHostBillDetailVOList.size() == 0){
  					continue;
  				}
  				CloudHostBillDetailVO cloudHostBillDetailVO = cloudHostBillDetailVOList.get(0);
  				
  				
  				
  				BigDecimal nowPrice = null;
  				if(StringUtil.isBlank(cloudHostVO.getPackageId())){
  					//未选择套餐			
  					nowPrice = CloudHostPrice.getMonthlyPrice(cloudHostVO.getRegion(),2,cloudHostVO.getCpuCore(), cloudHostVO.getMemory(), cloudHostVO.getDataDisk(), cloudHostVO.getBandwidth()).setScale(2,   BigDecimal.ROUND_HALF_UP);
  				}else{
  					//选择了套餐，根据套餐那再计算价格
  					PackagePriceMapper packagePriceMapper = this.sqlSession.getMapper(PackagePriceMapper.class);
  					
  		 			List<PriceVO> priceList =  packagePriceMapper.getPriceByInfoId(cloudHostVO.getPackageId());
  					if(priceList==null){					
  						nowPrice = CloudHostPrice.getMonthlyPrice(cloudHostVO.getRegion(),2,cloudHostVO.getCpuCore(), cloudHostVO.getMemory(), cloudHostVO.getDataDisk(), cloudHostVO.getBandwidth()).setScale(2,   BigDecimal.ROUND_HALF_UP);		    		
   				    }else{
  				    	boolean flag = true;
  				    	for(PriceVO priceVo : priceList){
  				    		if(priceVo.getStatus() == 3){
  				    			flag = false;
  				    			nowPrice = priceVo.getMonthlyPrice(); 
  				    		}		    		
  				    	}
  				    	if(flag){
  				    		nowPrice = CloudHostPrice.getMonthlyPrice(cloudHostVO.getRegion(),2,cloudHostVO.getCpuCore(), cloudHostVO.getMemory(), cloudHostVO.getDataDisk(), cloudHostVO.getBandwidth()).setScale(2,   BigDecimal.ROUND_HALF_UP);		    		
  				    	}
  				    }
  				} 
  				if(nowPrice!=null){
  					//套餐价格和主机现有价格不符合,添加主机价格变更记录
  					if(nowPrice.compareTo(cloudHostVO.getMonthlyPrice())!=0){
  						PriceUpdateMapper priceUpdateMapper = this.sqlSession.getMapper(PriceUpdateMapper.class);
  						Map<String, Object> updatePriceData = new LinkedHashMap<String, Object>();
  						updatePriceData.put("id", StringUtil.generateUUID());
  						updatePriceData.put("item", 1);//1表示云主机
  						updatePriceData.put("itemId", cloudHostVO.getId());
  						updatePriceData.put("beforePrice", cloudHostVO.getMonthlyPrice());
  						updatePriceData.put("price", nowPrice);
  						updatePriceData.put("updateTime", strNow);
  						priceUpdateMapper.addPriceUpdate(updatePriceData);	
  						//更改数据库价格
  						Map<String, Object> priceData = new LinkedHashMap<String, Object>();
  						priceData.put("id", cloudHostVO.getId()); 
  						priceData.put("monthlyPrice", nowPrice); 
  						cloudHostMapper.updatePriceById(priceData);
  						
  						//按新的价格计费
  						cloudHostVO.setMonthlyPrice(nowPrice);
  						
  						logger.info("cloud host "+cloudHostVO.getDisplayName() +" monthlyPrice  change");
  					}
  				}
  				vpcPrice = vpcPrice.add(nowPrice);
  				if(cloudHostVO.getMonthlyPrice().compareTo(BigDecimal.ZERO)==0){
  					logger.info("CloudHostBillDetailServiceImpl.startOneCloudHostBilling() > ["+Thread.currentThread().getId()+"] cloud_host_is_free, id:["+cloudHostBillDetailVO.getHostId()+"]");
  					// 删除这条没用的记录
  					cloudHostBillDetailMapper.deleteById(cloudHostBillDetailVO.getId());
  					continue;
   					
  				}
  				BigDecimal fee = new BigDecimal("0");
  				billingStartTime          = DateUtil.stringToDate(cloudHostBillDetailVO.getStartTime(), "yyyyMMddHHmmssSSS");
  				billingStartTime          = DateUtil.stringToDate(vpcBillDetailVO.getStartTime(), "yyyyMMddHHmmssSSS");
  				BigDecimal price               = CloudHostPrice.getPeriodPrice(cloudHostVO.getMonthlyPrice(), billingEndTime.getTime() - billingStartTime.getTime()).setScale(2, BigDecimal.ROUND_HALF_UP);
  				//如果云主机是代理商创建
  				if(cloudHostVO.getType() == AppConstant.CLOUD_HOST_TYPE_6_Agent_VPC){ 	 					
  					totalAgentPrice = totalUserPrice.add(price);					
  				}else{
  					totalUserPrice = totalUserPrice.add(price);					
  				}
  				// 更新云主机账单明细表的费用
  				Map<String, Object> cloudHostBillDetailData = new LinkedHashMap<String, Object>();
  				cloudHostBillDetailData.put("fee",     fee);
  				cloudHostBillDetailData.put("isPaid",  AppConstant.CLOUD_HOST_BILL_DETAIL_IS_PAID_YES);
  				cloudHostBillDetailData.put("endTime", strBillingEndTime);
  				cloudHostBillDetailData.put("id",      cloudHostBillDetailVO.getId());
  				int i = cloudHostBillDetailMapper.updateFeeById(cloudHostBillDetailData);  

  				
  			}
  			
  			// 计算应该扣除用户的钱
  			TerminalUserVO terminalUserVO = terminalUserMapper.getBaseInfoById(vpcBaseInfo.getUserId());
  			if(totalAgentPrice.compareTo(BigDecimal.ZERO)>0){
  				AgentVO agentVO = agentMapper.getAgentByUserId(vpcBaseInfo.getUserId());
  				
  				if(agentVO!=null && agentVO.getAccountBalance().compareTo(BigDecimal.ZERO)<=0){
  					// 如果余额小于或等于0元，将代理商用户的下属终端用户的的全部云主机置为停机
  					logger.info("CloudHostBillDetailServiceImpl.startOneCloudHostBilling() > ["+Thread.currentThread().getId()+"] balance_not_enough, id:["+agentVO.getId()+"]");
  					List<TerminalUserVO> terminalUserList = terminalUserMapper.getTerminalUserFromAgent(agentVO.getId());
  					for(TerminalUserVO terminalUser : terminalUserList){
  						//普通主机
  						Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
  						cloudHostData.put("status",  AppConstant.CLOUD_HOST_STATUS_2_HALT);
  						cloudHostData.put("inactivateTime",  strNow);
  						cloudHostData.put("userId",  terminalUser.getId());
  						cloudHostData.put("type",  AppConstant.CLOUD_HOST_TYPE_2_AGENT);
  						cloudHostMapper.updateUserHostStatusByUserId(cloudHostData);
  						//VPC主机
  						cloudHostData.put("status",  AppConstant.CLOUD_HOST_STATUS_2_HALT);
  						cloudHostData.put("inactivateTime",  strNow);
  						cloudHostData.put("userId",  terminalUser.getId());
  						cloudHostData.put("type",  AppConstant.CLOUD_HOST_TYPE_6_Agent_VPC);
  						cloudHostMapper.updateUserHostStatusByUserId(cloudHostData);
  						
  						List<CloudHostVO> cloudHostList = cloudHostMapper.getByUserId(terminalUser.getId());
  						if(cloudHostList!=null&&cloudHostList.size()>0){
  							for(CloudHostVO vo:cloudHostList){
  								//将所有开机状态的云主机关机
  								if(vo.getType()==AppConstant.CLOUD_HOST_TYPE_2_AGENT || vo.getType()==AppConstant.CLOUD_HOST_TYPE_6_Agent_VPC){
  									
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
  						
  						data.put("userId",  terminalUser.getId());
  						data.put("type",  AppConstant.CLOUD_HOST_TYPE_6_Agent_VPC);
  						cloudHostBillDetailMapper.deleteAllNotPaid(data); 
  					}
  					
  					
  				}else{
  					totalAgentPrice = totalAgentPrice.subtract(totalAgentPrice.multiply(agentVO.getPercentOff().multiply(new BigDecimal("0.01")))).setScale(2,   BigDecimal.ROUND_HALF_UP);			 

  					BigDecimal balanceBeforeChange = agentVO.getAccountBalance();
  					BigDecimal balanceAfterChange  = agentVO.getAccountBalance().subtract(totalAgentPrice);
  				// 更新VPC账单明细表的费用
  					Map<String, Object> data = new LinkedHashMap<String, Object>();
  					data.put("fee",     totalUserPrice.add(totalAgentPrice));
  					data.put("isPaid",  AppConstant.CLOUD_HOST_BILL_DETAIL_IS_PAID_YES);
  					data.put("endTime", strBillingEndTime);
  					data.put("id",      vpcBillDetailVO.getId()); 
  					int i = vpcBillDetailMapper.updateBillAfterPay(data);
  					// 生成账单表的记录
  					Map<String, Object> billData = new LinkedHashMap<String, Object>();
  					billData.put("id",           StringUtil.generateUUID());
  					billData.put("userId",       agentVO.getId());
  					billData.put("fee",          totalAgentPrice);
  					billData.put("region",       vpcBaseInfo.getRegion());
  					billData.put("resourceName", vpcBaseInfo.getDisplayName());
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
  					billDetailData.put("itemId",   vpcBillDetailVO.getId());
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
  					accountBalanceDetailData.put("amount",               totalAgentPrice);
  					accountBalanceDetailData.put("balanceBeforeChange",  balanceBeforeChange);
  					accountBalanceDetailData.put("balanceAfterChange",   balanceAfterChange);
  					accountBalanceDetailData.put("payType",              AppConstant.ACCOUNT_BALANCE_DETAIL_PAY_TYPE_4_FEE_DEFUCTION);
  					accountBalanceDetailData.put("description",          "云主机费用扣除");
  					accountBalanceDetailData.put("changeTime",           strNow);
  					accountBalanceDetailData.put("invoiceId",            null);
  					accountBalanceDetailData.put("billId",               billData.get("id"));
  					accountBalanceDetailData.put("rechargeStatus",       null);
  					accountBalanceDetailData.put("resourceName",         vpcBaseInfo.getDisplayName());
  					if (vpcBaseInfo.getDisplayName()==null) 
  					{
  						accountBalanceDetailData.put("resourceName",         vpcBaseInfo.getName());
  					}
  					accountBalanceDetailMapper.addAccountBalanceDetail(accountBalanceDetailData);
  					
  				}
  				
  			}
  			
  			totalUserPrice = totalUserPrice.subtract(totalAgentPrice.multiply(terminalUserVO.getPercentOff().multiply(new BigDecimal("0.01")))).setScale(2,   BigDecimal.ROUND_HALF_UP);			 

  			if(terminalUserVO != null && totalUserPrice.compareTo(BigDecimal.ZERO)>0){
  				if(terminalUserVO.getAccountBalance().compareTo(BigDecimal.ZERO)<=0){
  					logger.info("VpcBillDetailServiceImpl.startOneVpcBilling() > ["+Thread.currentThread().getId()+"] balance_not_enough, id:["+vpcBaseInfo.getUserId()+"]");
  					// 如果余额小于或等于0元，停用所有VPC
  					// 发送命令停用主机
  					Map<String, Object> data = new LinkedHashMap<String, Object>();
  					data.put("userId",vpcBaseInfo.getUserId());
  					NetworkService service = CoreSpringContextManager.getNetworkService();
   					List<VpcBaseInfoVO> vpcList = vpcBaseInfoMapper.queryUserVpcsByUserId(data);
   					for(VpcBaseInfoVO vpc : vpcList){
   						if(vpc.getStatus() != 1){
   							continue;
   						}
   						boolean result = false;
   						try {
   							result = service.stopAsync(vpc.getRegion(), vpc.getRealVpcId());
   						} catch (MalformedURLException e) {
   							e.printStackTrace();
   							
   						} catch (IOException e) {
   							e.printStackTrace();
   						}
   						if(result){ 						
    					    data.put("status",  2);
   							data.put("modifyTime",  strNow);
   							data.put("id",  vpcBaseInfo.getId());
   							vpcBaseInfoMapper.updateVpc(data); 
   							// 删除账单
   							vpcBillDetailMapper.deleteVpcBillDetailById(vpcBillDetailVO.getId());
   						}
   					}
   					
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
   					userMessage.put("content", "您的VPC已因欠费停用，请及时充值恢复服务");
   					userMessage.put("createTime", strNow);
   					userMessage.put("status", "4");
   					userMessageMapper.insertReg(userMessage);
  					
       					
  				}else{
  					BigDecimal balanceBeforeChange = terminalUserVO.getAccountBalance();  
  					BigDecimal balanceAfterChange  = terminalUserVO.getAccountBalance().subtract(totalUserPrice);
  					terminalUserVO.setAccountBalance(balanceAfterChange);
  					if(totalUserPrice.compareTo(balanceBeforeChange)>=0){
  						balanceAfterChange = BigDecimal.ZERO;
  						totalUserPrice = balanceBeforeChange;
  					}
  				    // 更新VPC账单明细表的费用
  					Map<String, Object> data = new LinkedHashMap<String, Object>();
  					data.put("fee",     totalUserPrice.add(totalAgentPrice));
  					data.put("isPaid",  AppConstant.CLOUD_HOST_BILL_DETAIL_IS_PAID_YES);
  					data.put("endTime", strBillingEndTime);
  					data.put("id",      vpcBillDetailVO.getId()); 
  					int i = vpcBillDetailMapper.updateBillAfterPay(data);
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
  					billData.put("region",       vpcBaseInfo.getRegion());
  					billData.put("resourceName", vpcBaseInfo.getDisplayName());
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
  					billDetailData.put("itemId",   vpcBillDetailVO.getId());
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
  					accountBalanceDetailData.put("resourceName",         vpcBaseInfo.getDisplayName()); 
  					accountBalanceDetailMapper.addAccountBalanceDetail(accountBalanceDetailData);
  					//结算后账单为0，,停用VPC
  					if(balanceAfterChange.compareTo(BigDecimal.ZERO) == 0){
  						UserMessageMapper userMessageMapper =this.sqlSession.getMapper(UserMessageMapper.class);
  	   					Map<String, Object> userMessage = new LinkedHashMap<String, Object>();
  	   					userMessage.put("id", StringUtil.generateUUID()); 
  	   					userMessage.put("userId", terminalUserVO.getId());
  	   					userMessage.put("content", "您的余额不足，请充值");
  	   					userMessage.put("createTime", strNow);
  	   					userMessage.put("status", "2");
  	   					userMessageMapper.insertReg(userMessage);
  	   					
  	   					
  						Map<String, Object> vpcdata = new LinkedHashMap<String, Object>();
  						vpcdata.put("userId",terminalUserVO.getId()); 
  						List<VpcBaseInfoVO> vpcList = vpcBaseInfoMapper.queryUserVpcsByUserId(vpcdata);
  						for(VpcBaseInfoVO vo:vpcList){
  							if(vo.getStatus() == 1){
  								
  							// 如果余额小于或等于0元，停用该VPC
  		  	  					// 发送命令停用VPC
  		  	  					NetworkService service = CoreSpringContextManager.getNetworkService();
  		  	  					boolean result = false;
  		  	  					try {
  		  							 result = service.stopAsync(vo.getRegion(), vo.getRealVpcId());
  		  						} catch (MalformedURLException e) {
  		  	  						e.printStackTrace();
  		  							
  		  						} catch (IOException e) {
  		  	  						e.printStackTrace();
  		  	 					}
  		  	  					if(result){ 						
  		  	  						data = new LinkedHashMap<String, Object>();
  		  	  						data.put("status",  2);
  		  	  						data.put("modifyTime",  strNow);
  		  	  						data.put("id",  vo.getId());
  		  	  						vpcBaseInfoMapper.updateVpc(data); 
  		    	  				}
  								
  							}
  						}
   					    
  	  					
  	  				   //普通主机
  						Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
  						cloudHostData.put("status",  AppConstant.CLOUD_HOST_STATUS_2_HALT);
  						cloudHostData.put("inactivateTime",  strNow);
  						cloudHostData.put("userId",  terminalUserVO.getId());
  						cloudHostData.put("type",  AppConstant.CLOUD_HOST_TYPE_3_TERMINAL_USER);
  						cloudHostMapper.updateUserHostStatusByUserId(cloudHostData); 
  						
  						List<CloudHostVO> cloudHostList = cloudHostMapper.getByUserId(terminalUserVO.getId());
  						if(cloudHostList!=null&&cloudHostList.size()>0){
  							for(CloudHostVO vo:cloudHostList){
  								//将所有开机状态的云主机关机
  								if(vo.getType()==AppConstant.CLOUD_HOST_TYPE_3_TERMINAL_USER){
  									
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
   						data.put("userId",  terminalUserVO.getId());
  						data.put("type",  AppConstant.CLOUD_HOST_TYPE_3_TERMINAL_USER);
  						cloudHostBillDetailMapper.deleteAllNotPaid(data);  
  					}
  				}
  				
  			}
  			
  			
			// 是否产生新的账单
			if(startNewBillDetail&&terminalUserVO.getAccountBalance().compareTo(BigDecimal.ZERO)>0){
				//产生新的账单 
				Map<String, Object> newbill = new LinkedHashMap<String, Object>();

  					newbill.put("id", StringUtil.generateUUID());
  					newbill.put("vpcId", vpcBaseInfo.getId());
  					newbill.put("createTime", strNow);
  					newbill.put("startTime", DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
					vpcBillDetailMapper.addVpcBillDetail(newbill);		
				
				for(CloudHostVO cloudHostVO:cloudHostVOs){
					if(cloudHostVO.getType() == AppConstant.CLOUD_HOST_TYPE_5_TERMINAL_USER_VPC || cloudHostVO.getType() == AppConstant.CLOUD_HOST_TYPE_6_Agent_VPC){ 	
						if(!StringUtil.isBlank(cloudHostVO.getRealHostId())){  										
							Map<String, Object> cloudHostBillDetailData2 = new LinkedHashMap<String, Object>();
							cloudHostBillDetailData2.put("id",               StringUtil.generateUUID());                         
							cloudHostBillDetailData2.put("host_id",          cloudHostVO.getId());                         
							cloudHostBillDetailData2.put("type",             AppConstant.CLOUD_HOST_BILL_DETAIL_TYPE_4_PAY_FOR_TIME);                         
							cloudHostBillDetailData2.put("cpuCore",          cloudHostVO.getCpuCore());                         
							cloudHostBillDetailData2.put("cpuUsed",          cloudHostVO.getCpuCore());                         
							cloudHostBillDetailData2.put("memory",           CapacityUtil.toMBValue(cloudHostVO.getMemory(), 2));                         
							cloudHostBillDetailData2.put("memoryUsed",       CapacityUtil.toMBValue(cloudHostVO.getMemory(), 2));                         
							cloudHostBillDetailData2.put("sysImageId",       cloudHostVO.getSysImageId());                         
							cloudHostBillDetailData2.put("sysDisk",          CapacityUtil.toGBValue(cloudHostVO.getSysDisk(), 2));                         
							cloudHostBillDetailData2.put("sysDiskUsed",      CapacityUtil.toGBValue(cloudHostVO.getSysDisk(), 2));                         
							cloudHostBillDetailData2.put("dataDisk",         CapacityUtil.toGBValue(cloudHostVO.getDataDisk(), 2));                         
							cloudHostBillDetailData2.put("dataDiskUsed",     CapacityUtil.toGBValue(cloudHostVO.getDataDisk(), 2));                         
							cloudHostBillDetailData2.put("diskRead",         0);                         
							cloudHostBillDetailData2.put("diskWrite",        0);                         
							cloudHostBillDetailData2.put("bandwidth",        FlowUtil.toMbpsValue(cloudHostVO.getBandwidth(), 2));                         
							cloudHostBillDetailData2.put("networkTraffic",   0);                         
							cloudHostBillDetailData2.put("startTime",        strBillingEndTime);                         
							cloudHostBillDetailData2.put("endTime",          null);                         
							cloudHostBillDetailData2.put("fee",              null);                         
							cloudHostBillDetailData2.put("isPaid",           AppConstant.CLOUD_HOST_BILL_DETAIL_IS_PAID_NOT);                         
							cloudHostBillDetailData2.put("createTime",       DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));                         
							cloudHostBillDetailMapper.addCloudHostBillDetail(cloudHostBillDetailData2);
						}
					}
				}
			}
					
					
			
		}
		//vpc价格变更
		if(vpcBaseInfo.getMonthlyPrice().compareTo(vpcPrice)!=0){			
			Map<String, Object> data = new LinkedHashMap<String, Object>();
			data.put("monthlyPrice",  vpcPrice);
			data.put("id",  vpcBillDetailVO.getVpcId());
			vpcBaseInfoMapper.updateVpcPrice(data);
		}
		return new MethodResult(MethodResult.SUCCESS, AppConstant.SUCCESS);
	}
	
	/**
	 * 
	 */
	public MethodResult startOneNewVpcBillDetail(String vpcId, Date startTime)
	{
		VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class);
		
		// 获取VPC的信息
		VpcBaseInfoVO vpcBaseInfoVO = vpcBaseInfoMapper.queryVpcById(vpcId);
		if( vpcBaseInfoVO==null )
		{
			logger.warn("VpcBillingHelper.startOneNewVpcBillDetail() > ["+Thread.currentThread().getId()+"] vpc not found with id:["+vpcId+"]");
			return new MethodResult(MethodResult.FAIL, "vpc_not_found");
		}
		
		return startOneNewVpcBillDetail(vpcBaseInfoVO, startTime);
	}
	
	/**
	 * 
	 */
	public MethodResult startOneNewVpcBillDetail(VpcBaseInfoVO vpcBaseInfo, Date startTime)
	{
		VpcBillDetailMapper vpcBillDetailMapper   = this.sqlSession.getMapper(VpcBillDetailMapper.class);
		CloudHostMapper cloudHostMapper   = this.sqlSession.getMapper(CloudHostMapper.class);
		CloudHostBillDetailMapper cloudHostBillDetailMapper   = this.sqlSession.getMapper(CloudHostBillDetailMapper.class);

		if( vpcBaseInfo == null )
		{
			throw new AppException("未找到VPC信息");
		}
		
 		
		String strStartTime = DateUtil.dateToString(startTime, "yyyyMMddHHmmssSSS");
		
 		Map<String, Object> data = new LinkedHashMap<String, Object>();
		//产生新的账单 
		data.put("id", StringUtil.generateUUID());
		data.put("vpcId", vpcBaseInfo.getId());
		data.put("createTime", strStartTime);
		data.put("startTime", strStartTime);
		vpcBillDetailMapper.addVpcBillDetail(data);
		
		List<CloudHostVO> cloudHostVOs = cloudHostMapper.getCloudHostInVpc(vpcBaseInfo.getId());
		
		
		for(CloudHostVO cloudHostVO:cloudHostVOs){
				if(cloudHostVO.getType() == AppConstant.CLOUD_HOST_TYPE_5_TERMINAL_USER_VPC || cloudHostVO.getType() == AppConstant.CLOUD_HOST_TYPE_6_Agent_VPC){ 
					if(!StringUtil.isBlank(cloudHostVO.getRealHostId())){						
						Map<String, Object> cloudHostBillDetailData2 = new LinkedHashMap<String, Object>();
						cloudHostBillDetailData2.put("id",               StringUtil.generateUUID());                         
						cloudHostBillDetailData2.put("host_id",          cloudHostVO.getId());                         
						cloudHostBillDetailData2.put("type",             AppConstant.CLOUD_HOST_BILL_DETAIL_TYPE_4_PAY_FOR_TIME);                         
						cloudHostBillDetailData2.put("cpuCore",          cloudHostVO.getCpuCore());                         
						cloudHostBillDetailData2.put("cpuUsed",          cloudHostVO.getCpuCore());                         
						cloudHostBillDetailData2.put("memory",           CapacityUtil.toMBValue(cloudHostVO.getMemory(), 2));                         
						cloudHostBillDetailData2.put("memoryUsed",       CapacityUtil.toMBValue(cloudHostVO.getMemory(), 2));                         
						cloudHostBillDetailData2.put("sysImageId",       cloudHostVO.getSysImageId());                         
						cloudHostBillDetailData2.put("sysDisk",          CapacityUtil.toGBValue(cloudHostVO.getSysDisk(), 2));                         
						cloudHostBillDetailData2.put("sysDiskUsed",      CapacityUtil.toGBValue(cloudHostVO.getSysDisk(), 2));                         
						cloudHostBillDetailData2.put("dataDisk",         CapacityUtil.toGBValue(cloudHostVO.getDataDisk(), 2));                         
						cloudHostBillDetailData2.put("dataDiskUsed",     CapacityUtil.toGBValue(cloudHostVO.getDataDisk(), 2));                         
						cloudHostBillDetailData2.put("diskRead",         0);                         
						cloudHostBillDetailData2.put("diskWrite",        0);                         
						cloudHostBillDetailData2.put("bandwidth",        FlowUtil.toMbpsValue(cloudHostVO.getBandwidth(), 2));                         
						cloudHostBillDetailData2.put("networkTraffic",   0);                         
						cloudHostBillDetailData2.put("startTime",        strStartTime);                         
						cloudHostBillDetailData2.put("endTime",          null);                         
						cloudHostBillDetailData2.put("fee",              null);                         
						cloudHostBillDetailData2.put("isPaid",           AppConstant.CLOUD_HOST_BILL_DETAIL_IS_PAID_NOT);                         
						cloudHostBillDetailData2.put("createTime",       strStartTime);                         
						cloudHostBillDetailMapper.addCloudHostBillDetail(cloudHostBillDetailData2);
					}
				}
			}
		
		
		
		
		return new MethodResult(MethodResult.SUCCESS, AppConstant.SUCCESS);
	}
	
	private static BigDecimal getPeriodPrice(BigDecimal monthlyPrice, long periodOfMillisecond)
	{
		return monthlyPrice.multiply(divide(new BigDecimal(periodOfMillisecond), new BigDecimal(DateUtil.MILLISECOND_PER_MONTH)));
	}
	private static BigDecimal divide(BigDecimal d1, BigDecimal d2)
	{
		return d1.divide(d2, 50, BigDecimal.ROUND_HALF_EVEN);
	}
	
	
	public MethodResult settleAllUndoneVpcBills(String vpcId, Date billingEndTime, boolean startNewBillDetail) throws ParseException
	{
		VpcBillDetailMapper vpcBillDetailMapper   = this.sqlSession.getMapper(VpcBillDetailMapper.class); 
		VpcBillDetailVO vo = vpcBillDetailMapper.getOneUndoneReocrdByVpcId(vpcId);
		if(vo == null){
			return new MethodResult(MethodResult.SUCCESS, "success");		
		}else{			
			return doVpcBilling(vo, billingEndTime, startNewBillDetail);
		}  
		
	}

}

