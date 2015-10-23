/**
 * Project Name:op
 * File Name:VpnService.java
 * Package Name:com.zhicloud.op.service
 * Date:2015年4月1日下午2:12:43
 * 
 *
*/ 

package com.zhicloud.op.service;  	  

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.vo.CloudHostVO;
import com.zhicloud.op.vo.VpcBaseInfoVO;

/**
 * ClassName: VpnService 
 * Function: VPN的一系操作
 *  
 * date: 2015年4月1日 下午2:12:43 
 *
 * @author sasa
 * @version 
 * @since JDK 1.7
 */
public interface VpcService {
	
	/**
	 * 
	 * managePage:跳转到我的VPC列表页面
	 *
	 * @author sasa
	 * @param request
	 * @param response
	 * @return String
	 * @since JDK 1.7
	 */
	public String managePage(HttpServletRequest request,HttpServletResponse response); 
	/**
	 * 
	 * createVpnPage:跳转到创建VPN页面
	 *
	 * @author sasa
	 * @param request
	 * @param response
	 * @return String
	 * @since JDK 1.7
	 */
	public String createVpnPage(HttpServletRequest request,HttpServletResponse response); 
	/**
	 * 
	 * addVpcBaseInfo: 新增VPC基本信息
	 *
	 * @author sasa
	 * @param parameter
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	public MethodResult addVpcBaseInfo(Map<String, String> parameter);
	/**
	 * 
	 * deleteVpc:删除Vpc
	 *
	 * @author sasa
	 * @param parameter
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	public MethodResult deleteVpc(Map<String, String> parameter);
    /**
     * 
     * toVpcHostList: 跳转到该Vpc的主机列表 
     *
     * @author sasa
     * @param request
     * @param response
     * @return String
     * @since JDK 1.7
     */
	public String toVpcHostList(HttpServletRequest request,HttpServletResponse response);  
	/**
	 * 
	 * countVpcPrice:计算VPC的价格
	 *
	 * @author sasa
	 * @param parameter
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	public MethodResult countVpcPrice(Map<String, String> parameter) ;
	/**
	 * 
	 * ableVpc:恢复VPC
	 *
	 * @author sasa
	 * @param vpcId
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	public MethodResult ableVpc(String vpcId);
	/**
	 * 
	 * disableVpc:停用VPC 
	 *
	 * @author sasa
	 * @param vpcId
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	public MethodResult disableVpc(String vpcId);
	/**
	 * 
	 * toVpcDetail:跳转到VPC详情
	 *
	 * @author sasa
	 * @param request
	 * @param response
	 * @return String
	 * @since JDK 1.7
	 */
	public String toVpcDetail(HttpServletRequest request,HttpServletResponse response);  
	
	/**
	 * 
	 * 跳转到vpc的ip管理页
	 * @param request
	 * @param response
	 * @return
	 */
	public String vpcIpManagePage(HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 添加Ip数
	 * 
	 * @param ipCount
	 * @return
	 */
	public MethodResult addIpCount(String ipCount,String vpcId);
	
	/**
	 * 批量删除ip
	 * 
	 * @param ips
	 * @return
	 */
	public MethodResult deleteIps(String vpcId,List<String> ips,List<String> ipValues);
	
	/**
	 * 跳转到网络配置管理页
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public String vpcNetworkManagePage(HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 通过vpcId获取所有的Ip
	 * 
	 * @param request
	 * @param response
	 */
	public void getAllIps(HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 增加一条绑定端口的记录
	 * 
	 * @param vpcId
	 * @param outerIp
	 * @param outerPort
	 * @param hostId
	 * @param port
	 * @param protocol
	 * @return
	 */
	public MethodResult addBindPortItem(String vpcId,String outerIp,String outerPort,String hostId,String port,String protocol);
	
	/**
	 * 批量删除绑定端口
	 * 
	 * @param ids
	 * @return
	 */
	public MethodResult deleteBindPorts(String vpcId,List<String> ids);
	/**
	 * 
	 * Unbound:解除绑定
	 *
	 * @author sasa
	 * @param hostId
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	public MethodResult unboundByHostId(String hostId);
	/**
	 * 
	 * BoundByHostIds:绑定现有云主机 
	 *
	 * @author sasa
	 * @param hostIds
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	public MethodResult boundByHostIds(List<String> ids,String vpcId);
	
	/**
	 * 修改vpc显示名
	 * 
	 * @param vpcId
	 * @param newDisplayName
	 * @return
	 */
	public MethodResult updateVpcDisplayNameById(String vpcId,String newDisplayName);
	/**
	 * 
	 * addNewHostToVpcBaseInfo:新创建主机，添加到vpc
	 *
	 * @author sasa
	 * @param parameter
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	public MethodResult addNewHostToVpcBaseInfo(Map<String, String> parameter);
	/**
	 * 
	 * getCreateInfo: 获取VPC创建需要的信息
	 *
	 * @author sasa
	 * @param userId
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	public MethodResult getCreateInfo(String userId);
	
	/**
	 * 跳转到运营商的VPC管理页
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public String getAllVpcPage(HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 返回所有vpc
	 * 
	 * @param request
	 * @param response
	 */
	public void queryVpcHost(HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 根据vpcId批量删除VPC
	 * 
	 * @param ids
	 * @return
	 */
	public MethodResult deleteVpcByIds(List<String> ids);
	
	/**
	 * 跳转到vpc详情页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public String vpcViewDetailPage(HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 运营商管理vpc中的主机列表页
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	public String hostListForOperator(HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 根据vpcid获取所有云主机
	 * 
	 * @param request
	 * @param response
	 */
	public void getAllHostForVpcId(HttpServletRequest request,HttpServletResponse response);
 	
	/**
	 * 
	 * 通过vpcid查询所有ip地址
	 * @param request
	 * @param response
	 */
	public void queryAllIpForVpcId(HttpServletRequest request,HttpServletResponse response);
	
	/**
	 * 根据vpcid查询所有端口绑定信息
	 * 
	 * @param request
	 * @param response
	 */
	public void getAllBindPort(HttpServletRequest request,HttpServletResponse response);
 

    /**
     * @Description:创建vpc以后随机绑定默认端口.
     * @param vpcid
     * @param wwip
     */
    public void defaultBindPort(String vpcid, CloudHostVO cloudHost);
    /**
     * 
     * agentAddVpcBaseInfo: 代理商为用户创建vpc
     *
     * @author sasa
     * @param parameter
     * @return MethodResult
     * @since JDK 1.7
     */
    public MethodResult agentAddVpcBaseInfo(Map<String, String> parameter);
    /**
     * 
     * agentCreateVpnPage: 代理商创建专属云页面 
     *
     * @author sasa
     * @param request
     * @param response
     * @return String
     * @since JDK 1.7
     */
	public String agentCreateVpnPage(HttpServletRequest request,HttpServletResponse response); 
	
    /**
     * @Description:查询用户VPC数据，不分页
     * @param request
     * @param response
     * @return List<VpcBaseInfoVO>
     */
    public List<VpcBaseInfoVO> queryVpcHostForExport(HttpServletRequest request, HttpServletResponse response);

}

