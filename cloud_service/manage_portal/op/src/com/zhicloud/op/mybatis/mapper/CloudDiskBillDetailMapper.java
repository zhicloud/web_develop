
/**
 * Project Name:op
 * File Name:CloudDIskBillDetailMapper.java
 * Package Name:com.zhicloud.op.mybatis.mapper
 * Date:2015年4月23日上午10:16:39
 * 
 *
 */

package com.zhicloud.op.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.CloudDiskBillDetailVO; 
/**
 * ClassName: CloudDiskBillDetailMapper 
 * Function: disk账单
 * date: 2015年4月23日 上午10:16:39 
 *
 * @author sasa
 * @version 
 * @since JDK 1.7
 */
public interface CloudDiskBillDetailMapper {
	
	/**
	 *    
	 * getOneUndoneReocrdBeforeTime:获取某时间点前的数据
	 *
	 * @author sasa
	 * @param beforeTime
	 * @return CloudDiskBillDetailVO
	 * @since JDK 1.7
	 */
	public CloudDiskBillDetailVO getOneUndoneReocrdBeforeTime(String beforeTime);
	/**
	 * 
	 * getOneUndoneReocrdByVpcId:获取VPC的为结算记录
	 *
	 * @author sasa
	 * @param vpcId
	 * @return CloudDiskBillDetailVO
	 * @since JDK 1.7
	 */
	public CloudDiskBillDetailVO getOneUndoneReocrdByDiskId(String diskId);
	/**
	 * 
	 * addVpcBillDetail:新增一个账单明细
	 *
	 * @author sasa
	 * @param data
	 * @return int
	 * @since JDK 1.7
	 */
	public int addCloudDiskBillDetail(Map<String, Object> data);
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
	public int deleteCloudDiskBillDetailById(String id);
	/**
	 * 
	 * getUndoneBillByUserId:根据用户Id查询出所有的VPC账单
	 *
	 * @author sasa
	 * @param data
	 * @return List<CloudDiskBillDetailVO>
	 * @since JDK 1.7
	 */
	public List<CloudDiskBillDetailVO> getUndoneBillByUserId(Map<String, Object> data);

}

