/**
 * Project Name:op
 * File Name:VpcBillDetailMapper.java
 * Package Name:com.zhicloud.op.mybatis.mapper
 * Date:2015年4月1日下午7:00:23
 * 
 *
*/ 

package com.zhicloud.op.mybatis.mapper; 

import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.VpcBillDetailVO;

 
/**
 * ClassName: VpcBillDetailMapper 
 * Function:  vpc账单
 * date: 2015年4月1日 下午7:00:23 
 *
 * @author sasa
 * @version 
 * @since JDK 1.7
 */
public interface VpcBillDetailMapper {
	/**
	 * 
	 * getOneUndoneReocrdBeforeTime:获取某时间点前的数据
	 *
	 * @author sasa
	 * @param beforeTime
	 * @return VpcBillDetailVO
	 * @since JDK 1.7
	 */
	public VpcBillDetailVO getOneUndoneReocrdBeforeTime(String beforeTime);
	/**
	 * 
	 * getOneUndoneReocrdByVpcId:获取VPC的为结算记录
	 *
	 * @author sasa
	 * @param vpcId
	 * @return VpcBillDetailVO
	 * @since JDK 1.7
	 */
	public VpcBillDetailVO getOneUndoneReocrdByVpcId(String vpcId);
	/**
	 * 
	 * addVpcBillDetail:新增一个账单明细
	 *
	 * @author sasa
	 * @param data
	 * @return int
	 * @since JDK 1.7
	 */
	public int addVpcBillDetail(Map<String, Object> data);
	/**
	 * 
	 * updateBillStatusBeforPay:支付之前更新账单状态
	 *
	 * @author sasa
	 * @param data
	 * @return int
	 * @since JDK 1.7
	 */
	public int updateBillStatusBeforPay(Map<String, Object> data);
	/**
	 * 
	 * updateBillAfterPay:支付完成之后更新账单表
	 *
	 * @author sasa
	 * @param data
	 * @return int
	 * @since JDK 1.7
	 */
	public int updateBillAfterPay(Map<String, Object> data);
	/**
	 * 
	 * deleteVpcBillDetailById: 通过ID删除vpc账单
	 *
	 * @author sasa
	 * @param id
	 * @return int
	 * @since JDK 1.7
	 */
	public int deleteVpcBillDetailById(String id);
	/**
	 * 
	 * getUndoneBillByUserId:根据用户Id查询出所有的VPC账单
	 *
	 * @author sasa
	 * @param data
	 * @return List<VpcBillDetailVO>
	 * @since JDK 1.7
	 */
	public List<VpcBillDetailVO> getUndoneBillByUserId(Map<String, Object> data);
	/**
	 * 
	 * getAllUndoneReocrdByVpcId: 通过vpcId获取所有未结算账单 
	 *
	 * @author sasa
	 * @param data
	 * @return List<VpcBillDetailVO>
	 * @since JDK 1.7
	 */
	
	public List<VpcBillDetailVO> getAllUndoneReocrdByVpcId(Map<String, Object> data);

}

