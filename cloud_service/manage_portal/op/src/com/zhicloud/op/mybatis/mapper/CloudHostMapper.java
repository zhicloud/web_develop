package com.zhicloud.op.mybatis.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.op.vo.CloudHostVO;

public interface CloudHostMapper
{
	
	public int queryPageCount(Map<String, Object> condition);

	public int queryPageCountForAgent(Map<String, Object> condition);
	
	public int cloudHostCount(String userId); 
	
	public int getAllCloudHostCount(Map<String, Object> data); 
	
	public int cloudHostCountForAgent(String userId);
	
	public int cloudHostCountForAgentTwo(String userId);
	
	public int cloudHostCountForOperator(String userId);
	
	public List<CloudHostVO> queryPage(Map<String, Object> condition);
	
	public List<CloudHostVO> queryHostByWarehouseIdAndStatus(Map<String, Object> condition);
	
	public List<CloudHostVO> queryPageForAgent(Map<String, Object> condition);
	
	//----------------

	public List<CloudHostVO> getAllCloudHost();
	
	public List<CloudHostVO> getNoBillCloudHost();	
	
	public CloudHostVO getById(String id);

	public CloudHostVO getByRealHostId(String readHostId);
	
	public CloudHostVO getByHostName(String hostName);
	
	public CloudHostVO getByRegionAndHostName(Map<String, Object> condition);

	public CloudHostVO getByRegionAndDispalyName(Map<String, Object> condition);


	public List<CloudHostVO> getByUserId(String userId);
	
	public int queryHostCount(Map<String, Object> condition);
	
	public List<CloudHostVO> getByTerminalUserId(Map<String, Object> condition);
	
	public List<CloudHostVO> getByAgentId(String agentId);
	 
	public List<CloudHostVO> getByUserIdAndRegion(Map<String, Object> condition);
	
	public List<CloudHostVO> getByUserIdAndCloudTerminalId(Object condition);
	
	public CloudHostVO getOneUndistributedCloudHostFromWarehouse(Map<String, Object> condition);
	
	public int distributeWarehouseCloudHostToUser(Map<String, Object> condition);
	
	public List<CloudHostVO> queryOperatorSelfUseCloudHost(Map<String, Object> condition);
	
	//---------------------
	
	public int addCloudHost(Map<String, Object> cloudHostData);
	
	//------------------
	
	public int updateRealHostIdById(Map<String, Object> cloudHostData);

	public int updateStatusById(Map<String, Object> cloudHostData);

	public int updateStatusByUserId(Map<String, Object> cloudHostData);
	
	public int updateUserHostStatusByUserId(Map<String, Object> cloudHostData);
	
	public int updateRunningStatusByRealHostId(Map<String, Object> cloudHostData);
	
	public int updateClientCloudHostById(Map<String, Object> cloudHostData);
	
	public int updateCreateTimeById(Map<String, Object> cloudHostData);

	public int updateImageByRealId(Map<String, Object> cloudHostData);
	
	
	//----------------------
	
	public int deleteByIds(String[] hostIds);
	
	public int deleteById(String[] hostIds);
	
	public int updateForDeleteByIds(String[] hostIds);

	public int deleteUselessWarehouseCloudHosts(Map<String, Object> condition);
	
	public List<CloudHostVO> getByUserIds(List<String> hostIds);
	
	public int updatePasswordById(Map<String, Object> cloudHostData); 
	
	public int updateHostNameById(Map<String, Object> cloudHostData); 
	
	public int updateDisplayNameById(Map<String, Object> cloudHostData); 
	
	public int updatePriceById(Map<String, Object> cloudHostData); 
	
	public List<CloudHostVO> getCloudHostForRegion(Map<String, Object> cloudHostData); 
	
	//------------------------
	public int getCloudHostForUserByTime(Map<String, Object> condition);
	
	public int updatePackageIdById(String hostId);
	/**
	 * 
	 * getCloudForVpc:获取创建成功并且未分配为Vpc的主机
	 *
	 * @author sasa
	 * @param cloudHostData
	 * @return List<CloudHostVO>
	 * @since JDK 1.7
	 */
	public List<CloudHostVO> getCloudForVpc(Map<String, Object> cloudHostData); 
	/**
	 * 
	 * updateHostTypeById:通过ID更新主机的类型
	 * type 、id
	 * @author sasa
	 * @param condition
	 * @return int
	 * @since JDK 1.7
	 */
	public int updateHostTypeById(Map<String, Object> condition);
	/**
	 * 
	 * getCloudHostInVpc:查询正在VPC中的主机
	 *
	 * @author sasa
	 * @param vpcId
	 * @return List<CloudHostVO>
	 * @since JDK 1.7
	 */
	public List<CloudHostVO> getCloudHostInVpc(String vpcId); 
	
	/**
	 * 通过vpcid查询所有云主机
	 * 
	 * @param vpcId
	 * @return
	 */
	public List<CloudHostVO> getAllHostByVpcId(Map<String,Object> condition);
	
	/**
	 * 通过vpcid查询云主机数量
	 * 
	 * @param condition
	 * @return
	 */
	public int getCountByVpcId(Map<String,Object> condition);
	/**
	 * 
	 * updateVpcIpById:更新vpcip
	 *
	 * @author sasa
	 * @param condition
	 * @return int
	 * @since JDK 1.7
	 */
	public int updateVpcIpById(Map<String, Object> condition);
	/**
	 * 
	 * updateDescription: 云主机备注 
	 *
	 * @author sasa
	 * @param condition
	 * @return int
	 * @since JDK 1.7
	 */
	public int updateDescription(Map<String, Object> condition);
}
