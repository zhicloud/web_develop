package com.zhicloud.ms.service; 

import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.vo.CloudHostVO;
import com.zhicloud.ms.vo.VpcBaseInfoVO;
import com.zhicloud.ms.vo.VpcBindPortVO;
import com.zhicloud.ms.vo.VpcOuterIpVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * @author ZYFTMX
 *
 */
public interface IVpcService {
	
	/**
	 * 获取所以vpc
	 * @return
	 */
	public List<VpcBaseInfoVO> getAllVpc();
	
	/**
	 * 创建vpc
	 * @param displayName
	 * @param ipAmount
	 * @param description
	 * @param hosts
	 * @param request
	 * @return
	 */
	public MethodResult addVpc(String displayName,String ipAmount, String groupId, String description,String[] hosts,HttpServletRequest request);
	
	/**
	 * 删除vpc
	 * @param id
	 * @return
	 */
	public MethodResult deleteVpc(String id);
	
	/**
	 * 停用vpc
	 * @param vpcId
	 * @return
	 */
	public MethodResult disableVpc(String vpcId);
	
	/**
	 * 恢复vpc
	 * @param vpcId
	 * @return
	 */
	public MethodResult ableVpc(String vpcId);
	
	/**
	 * 通过vpcid查询所有ip
	 * @param vpcId
	 * @return
	 */
	public List<VpcOuterIpVO> getAllIpByVpcId(String vpcId);
	
	/**
	 * 添加IP数量
	 * @param ipCount
	 * @param vpcId
	 * @return
	 */
	public MethodResult addIpCount(String ipCount,String vpcId);
	
	/**
	 * 批量删除IP
	 * @param vpcId
	 * @param ids
	 * @param ipValues
	 * @return
	 */
	public MethodResult deleteIps(String vpcId,List<String> ids,List<String> ipValues);
	
	/**
	 * 查询所有配置绑定项
	 * @param vpcId
	 * @return
	 */
	public List<VpcBindPortVO> getAllBindPort(String vpcId);
	
	/**
	 * 添加端口配置
	 * @param vpcId
	 * @param outerIp
	 * @param outerPort
	 * @param hostId
	 * @param port
	 * @param protocol
	 * @return
	 */
	public MethodResult addBindPortItem(String vpcId, String outerIp,String outerPort, String hostId, String port, String protocol);
	
	/**
	 *  批量删除端口配置
	 * @param vpcId
	 * @param ids
	 * @return
	 */
	public MethodResult deleteBindPorts(String vpcId,List<String> ids);
	
	/**
	 * 获取vpc的主机列表
	 * @param vpcId
	 * @return
	 */
	public List<CloudHostVO> toVpcHostList(String vpcId);
	
	/**
	 * 添加vpc主机
	 * @param vpcId
	 * @param cpuCore
	 * @param memory
	 * @param dataDisk
	 * @param bandwidth
	 * @param sysImageId
	 * @param hostAmount
	 * @return
	 */
	public MethodResult addNewHostToVpcBaseInfo(String vpcId,String cpuCore,String memory,String dataDisk,String bandwidth,String sysImageId,String hostAmount);
	
	/**
	 * 解除主机的绑定
	 * @param hostId
	 * @return
	 */
	public MethodResult unboundByHostId(String hostId);
	
	/**
	 * 批量删除vpc
	 * @param ids
	 * @return
	 */
	public MethodResult deleteVpcByIds(List<String> ids);
}

