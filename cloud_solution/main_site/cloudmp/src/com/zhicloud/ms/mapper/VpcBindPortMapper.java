package com.zhicloud.ms.mapper; 

import java.util.List;
import java.util.Map;

import com.zhicloud.ms.vo.VpcBindPortVO;

public interface VpcBindPortMapper {
	
	/**
	 * 新增一个配置
	 * @param condition
	 * @return
	 */
	public int add(Map<String, Object> condition);
	
	/**
	 * 删除一个配置
	 * @param condition
	 * @return
	 */
	public int delete(Map<String, Object> condition);
	
	/**
	 * 根据vpcid查询端口配置
	 * @param condition
	 * @return
	 */
	public List<VpcBindPortVO> queryByVpcId(Map<String, Object> condition);
	
	/**
	 * 查询vpc的端口配置个数
	 * @param condition
	 * @return
	 */
	public int queryCountByVpcId(Map<String, Object> condition);
	
	
	/**
	 * 批量删除绑定端口
	 * @param ids
	 * @return
	 */
	public int deleteByIds(List<String> ids);
	
	
	/**
	 * 根据IP删除绑定端口
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
	 * 根据主机id删除绑定的端口
	 * @param hostIds
	 * @return
	 */
	public int deleteByHostIds(String[] hostIds);
	
}

