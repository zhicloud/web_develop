/**
 * Project Name:op
 * File Name:CountUserProductsPriceHelper.java
 * Package Name:com.zhicloud.op.app.helper
 * Date:2015年4月14日上午9:29:00
 * 
 *
*/ 

package com.zhicloud.op.app.helper; 

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.zhicloud.op.app.propeties.AppProperties;
import com.zhicloud.op.common.util.DateUtil;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.mybatis.mapper.CloudHostBillDetailMapper;
import com.zhicloud.op.mybatis.mapper.CloudHostMapper;
import com.zhicloud.op.mybatis.mapper.TerminalUserMapper;
import com.zhicloud.op.mybatis.mapper.VpcBaseInfoMapper;
import com.zhicloud.op.mybatis.mapper.VpcBillDetailMapper;
import com.zhicloud.op.vo.CloudHostBillDetailVO;
import com.zhicloud.op.vo.CloudHostVO;
import com.zhicloud.op.vo.TerminalUserVO;
import com.zhicloud.op.vo.VpcBaseInfoVO;
import com.zhicloud.op.vo.VpcBillDetailVO;

/**
 * ClassName: CountUserProductsPriceHelper 
 * Function:  计算所有产品的一天费用
 * date: 2015年4月14日 上午9:29:00 
 *
 * @author sasa
 * @version 
 * @since JDK 1.7
 */
public class CountUserProductsPriceHelper {
	
	   private static final Logger logger = Logger.getLogger(CloudHostWarehouseHelper.class);
		
		private SqlSession sqlSession;
		
		public CountUserProductsPriceHelper(SqlSession sqlSession)
		{
			this.sqlSession =  sqlSession;
		}
		
		/**
		 * 
		 * getAllOneDayPrice: 计算所有产品一天的费用
		 *
		 * @author sasa
		 * @param userId
		 * @return BigDecimal
		 * @since JDK 1.7
		 */
		public BigDecimal getAllOneDayPrice(String userId){
			
			CloudHostMapper cloudHostMapper                                     = this.sqlSession.getMapper(CloudHostMapper.class);
			VpcBaseInfoMapper vpcBaseInfoMapper   = this.sqlSession.getMapper(VpcBaseInfoMapper.class);
 			BigDecimal totalPrice = new BigDecimal("0");
			
			// 计算云主机的费用(包括vpc主机)
			
			List<CloudHostVO> cloudHostList = cloudHostMapper.getByUserId(userId);
			if(cloudHostList!=null&&cloudHostList.size()>0){
				for(CloudHostVO vo:cloudHostList){
					if(!(vo.getSummarizedStatusText().equals("创建失败")||vo.getSummarizedStatusText().equals("停机"))&&vo.getMonthlyPrice()!=null){						
						totalPrice = vo.getMonthlyPrice().add(totalPrice);
					}
				} 				
			}
   			
 			
 			//计算云硬盘的费用 （暂时无，先放这里，以后补）
 			
 			//计算VPC的价格(VPC+ip)
 			Map<String, Object> data = new LinkedHashMap<String, Object>();
 			data.put("userId",userId);
 			List<VpcBaseInfoVO> vpcList = vpcBaseInfoMapper.queryUserVpcsByUserId(data);
 			for(VpcBaseInfoVO vpcBaseInfo : vpcList){
 				//筛选掉已经停用了的vpc
 				if(vpcBaseInfo.getStatus() == 2){
 					continue;
 				}
 				//加上VPC价格
  				totalPrice.add(VPCAmountAndPrice.getVpcPrice(vpcBaseInfo.getRegion()+"", vpcBaseInfo.getHostAmount()+""));
 				//加上IP价格
 				totalPrice.add(new BigDecimal(AppProperties.getValue("ip_price_"+vpcBaseInfo.getRegion(), "")).multiply(new BigDecimal(vpcBaseInfo.getIpAmount())));
 			}
 			 
            			
			//返回每天的价格
			return totalPrice.divide(new BigDecimal("31"),3,BigDecimal.ROUND_HALF_UP);
		}
		/**
		 * 
		 * getRealBalance:获取用户真实的余额信息
		 *
		 * @author sasa
		 * @param userId
		 * @return BigDecimal
		 * @since JDK 1.7
		 */
		public BigDecimal getRealBalance(String userId){
			
 			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class); 
			
			TerminalUserVO terminalUserVO = terminalUserMapper.getBalanceById(userId); 
			try {
				if(terminalUserVO!=null){
					//获取所有云主机的实时费用（正常主机+VPC主机）
					CloudHostBillDetailMapper cloudHostBillDetailMapper = this.sqlSession.getMapper(CloudHostBillDetailMapper.class);
					String now = StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS");
					Map<String, Object> cloudHostBillDetailData = new LinkedHashMap<String, Object>();
					cloudHostBillDetailData.put("userId",     userId);
					cloudHostBillDetailData.put("beforeTime",   now); 
					List<CloudHostBillDetailVO> billList = cloudHostBillDetailMapper.getAllUndoneByUserId(cloudHostBillDetailData);
					BigDecimal totalConsumption = new BigDecimal("0"); 
					for(CloudHostBillDetailVO vo: billList){
						if(vo.getHostType()==3 || vo.getType() == 5){							
							Date d1;
							d1 = StringUtil.stringToDate(now, "yyyyMMddHHmmssSSS");
							Date  d2 = StringUtil.stringToDate(vo.getStartTime(), "yyyyMMddHHmmssSSS");
							long diff = d1.getTime() - d2.getTime();
							long seconds = diff / (1000 * 60);
							String daysecond = (30*24*60)+"";
							BigDecimal nowPrice =  vo.getMonthlyPrice().divide(new BigDecimal(daysecond),3,BigDecimal.ROUND_HALF_UP);
							nowPrice = 		nowPrice.multiply(new BigDecimal(seconds+"")).setScale(2, BigDecimal.ROUND_HALF_UP);
							//计算折扣后的价格
							if(terminalUserVO.getPercentOff()!=null&&terminalUserVO.getPercentOff().compareTo(BigDecimal.ZERO)>0){								
								nowPrice = nowPrice.multiply(new BigDecimal(100).subtract(terminalUserVO.getPercentOff())).multiply(new BigDecimal(0.01)).setScale(2, BigDecimal.ROUND_HALF_UP);
							}
							totalConsumption = totalConsumption.add(nowPrice);
						}
						
						
					}
					//获取云硬盘的实时费用(暂无，后面不补上)
					
					
					//获取VPC的费用
					VpcBillDetailMapper vpcBillDetailMapper = this.sqlSession.getMapper(VpcBillDetailMapper.class);
					VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class);
					
					String strEndTime = DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS");
					
 					cloudHostBillDetailData.put("userId",     userId);
					cloudHostBillDetailData.put("strEndTime",   strEndTime); 
					
					// 获取end_time is null的记录，表示计费还没有完成的记录
					List<VpcBaseInfoVO>  vpcList = vpcBaseInfoMapper.queryUserVpcsByUserId(cloudHostBillDetailData);
 					Date billingEndTime = new Date();
					Date billingStartTime = new Date();
					for(VpcBaseInfoVO vpcBaseInfo : vpcList){
						if(vpcBaseInfo.getStatus() != 1){
							continue;
						}
						VpcBillDetailVO vpcBill = vpcBillDetailMapper.getOneUndoneReocrdByVpcId(vpcBaseInfo.getId());
						if(vpcBill == null){
							continue;
						}
						billingStartTime = StringUtil.stringToDate(vpcBill.getStartTime(), "yyyyMMddHHmmssSSS");
 						// 加上VPC价格
						totalConsumption = totalConsumption.add(getPeriodPrice( VPCAmountAndPrice.getVpcPrice(vpcBaseInfo.getRegion()+"", vpcBaseInfo.getHostAmount()+""), billingEndTime.getTime() - billingStartTime.getTime()).setScale(2, BigDecimal.ROUND_HALF_UP));
						// 加上IP的价格
						totalConsumption = totalConsumption.add(getPeriodPrice( new BigDecimal(AppProperties.getValue("ip_price_"+vpcBaseInfo.getRegion(), "")).multiply(new BigDecimal(vpcBaseInfo.getIpAmount())), billingEndTime.getTime() - billingStartTime.getTime()).setScale(2, BigDecimal.ROUND_HALF_UP));
				  		
						
					}
					
					
					
	                BigDecimal balance = terminalUserVO.getAccountBalance().subtract(totalConsumption).setScale(2, BigDecimal.ROUND_HALF_UP);
					if(balance.compareTo(BigDecimal.ZERO)<0){
						balance = new BigDecimal("0.00");
					}
					return balance;
					 
				}
			
			} catch (ParseException e) {
				e.printStackTrace();
				
			}
			return null;
		}
		
		private static BigDecimal getPeriodPrice(BigDecimal monthlyPrice, long periodOfMillisecond)
		{
			return monthlyPrice.multiply(divide(new BigDecimal(periodOfMillisecond), new BigDecimal(DateUtil.MILLISECOND_PER_MONTH)));
		}
		private static BigDecimal divide(BigDecimal d1, BigDecimal d2)
		{
			return d1.divide(d2, 50, BigDecimal.ROUND_HALF_EVEN);
		}

}

