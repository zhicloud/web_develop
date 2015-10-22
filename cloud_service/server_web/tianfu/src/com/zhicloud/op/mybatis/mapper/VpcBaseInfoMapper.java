/**
 * Project Name:op
 * File Name:VpcBaseInfoMapper.java
 * Package Name:com.zhicloud.op.mybatis.mapper
 * Date:2015年4月1日下午3:27:21
 * 
 *
*/ 

package com.zhicloud.op.mybatis.mapper; 

import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.VpcBaseInfoVO;

/**
 * ClassName: VpcBaseInfoMapper 
 * Function: vpc基本信息的数据库操作
 * 
 * date: 2015年4月1日 下午3:27:21 
 *
 * @author sasa
 * @version 
 * @since JDK 1.7
 */
public interface VpcBaseInfoMapper {
	/**
	 * 
	 * queryUserVpcsByUserId:通过用户Id查询其vpc
	 *
	 * @author sasa
	 * @param userId
	 * @return List<VpcBaseInfoVO>
	 * @since JDK 1.7
	 */
	public List<VpcBaseInfoVO> queryUserVpcsByUserId(Map<String, Object> condition);
	/**
	 * 
	 * queryVpcById: 查询VPC 通过id
	 *
	 * @author sasa
	 * @param id
	 * @return VpcBaseInfoVO
	 * @since JDK 1.7
	 */
	public VpcBaseInfoVO queryVpcById(String id);
	/**
	 * 
	 * addVpc:新增一个VPC的基本信息
	 *
	 * @author sasa
	 * @param condition
	 * @return int
	 * @since JDK 1.7
	 */
	public int addVpc(Map<String, Object> condition);
	/**
	 * 
	 * updateVpc:更新VPC
	 *
	 * @author sasa
	 * @param condition
	 * @return int
	 * @since JDK 1.7
	 */
	public int updateVpc(Map<String, Object> condition);
	/**
	 * 
	 * deleteVpc:根据VPC id删除VPC
	 *
	 * @author sasa
	 * @param vpcId
	 * @return int
	 * @since JDK 1.7
	 */
	public int deleteVpc(String vpcId);
	/**
	 * 
	 * updateVpcIpAmount: 更新vpc的ip个数
	 *
	 * @author sasa
	 * @param condition
	 * @return int
	 * @since JDK 1.7
	 */
    public int updateVpcIpAmount(Map<String, Object> condition);
    /**
     * 
     * updateVpcHostAmount:更新vpc的主机个数
     *
     * @author sasa
     * @param condition
     * @return int
     * @since JDK 1.7
     */
    public int updateVpcHostAmount(Map<String, Object> condition);
    /**
     * 
     * queryVpcByHostId:根据主机ID查询其关联的VPC
     *
     * @author sasa
     * @param hostId
     * @return VpcBaseInfoVO
     * @since JDK 1.7
     */
    public VpcBaseInfoVO  queryVpcByHostId(String hostId);
	
	/**
	 * 查询所有VPC
	 * 
	 * @param condition
	 * @return
	 */
	public List<VpcBaseInfoVO> getAllVpc(Map<String,Object> condition);
	
	/**
	 * 查询所有vpc数量
	 * 
	 * @param condition
	 * @return
	 */
	public int getCount(Map<String,Object> condition);

	/**
	 * 根据vpcId批量删除vpc
	 * 
	 * @param ids
	 * @return
	 */
	public int deleteVpcByIds(List<String> ids);
}

