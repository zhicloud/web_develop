/**
 * Project Name:op
 * File Name:VpcBindPortMapper.java
 * Package Name:com.zhicloud.op.mybatis.mapper
 * Date:2015年4月1日下午6:29:49
 * 
 *
*/ 

package com.zhicloud.op.mybatis.mapper; 

import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.VpcBindPortVO;

/**
 * ClassName: VpcBindPortMapper 
 * Function:  vpc网络配置
 * date: 2015年4月1日 下午6:29:49 
 *
 * @author sasa
 * @version 
 * @since JDK 1.7
 */
public interface VpcBindPortMapper {
	/**
	 * 
	 * add: 新增一个配置
	 *
	 * @author sasa
	 * @param condition
	 * @return int
	 * @since JDK 1.7
	 */
	public int add(Map<String, Object> condition);
	/**
	 * 
	 * delete:删除一个配置
	 *
	 * @author sasa
	 * @param condition
	 * @return int
	 * @since JDK 1.7
	 */
	public int delete(Map<String, Object> condition);
	/**
	 * 
	 * queryByVpcId:根据vpcId查询端口设置,有分页 
	 *
	 * @author sasa
	 * @param vpcId
	 * @return List<VpcBindPortVO>
	 * @since JDK 1.7
	 */
	public List<VpcBindPortVO> queryByVpcId(Map<String, Object> condition);
	/**
	 * 
	 * queryCountByVpcId:查询vpc的端口配置个数
	 *
	 * @author sasa
	 * @param vpcId
	 * @return int
	 * @since JDK 1.7
	 */
	public int queryCountByVpcId(Map<String, Object> condition);
	
	/**
	 * 批量删除绑定端口
	 * @param ids
	 * @return
	 */
	public int deleteByIds(List<String> ids);
	
	/**
	 * 根据IP删除端口配置
	 * 
	 * @param outerIp
	 * @return
	 */
	public int deleteByOuterIp(String outerIp);
	
	/**
	 * 根据外网ip、外网端口和协议检测该项是否已存在
	 */
	public VpcBindPortVO checkIpAndPortAndProtocol(Map<String,Object> condition);
	
	/**
	 * 根据主机Id、端口和协议检测该项是否已存在
	 * @return
	 */
	public VpcBindPortVO checkHostAndPortAndProtocol(Map<String,Object> condition);
	
	/**
	 * 根据id查询绑定对象
	 * 
	 * @param id
	 * @return
	 */
	public VpcBindPortVO queryById(String id);
	/**
	 * 
	 * deleteByHostIds: 根据主机id删除绑定的端口
	 *
	 * @author sasa
	 * @param ids
	 * @return int
	 * @since JDK 1.7
	 */
	public int deleteByHostIds(String[] hostIds);
	
}

