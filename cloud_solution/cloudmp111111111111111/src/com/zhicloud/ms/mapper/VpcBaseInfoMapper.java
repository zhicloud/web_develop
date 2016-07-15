package com.zhicloud.ms.mapper; 

import java.util.List;
import java.util.Map;

import com.zhicloud.ms.vo.VpcBaseInfoVO;


/**
 * @author ZYFTMX
 *
 */
/**
 * @author ZYFTMX
 *
 */
public interface VpcBaseInfoMapper {
	
	/**
	 * 查询所有vpc
	 * @param condition
	 * @return
	 */
	public List<VpcBaseInfoVO> getAll();
	
	/**
	 * 添加vpc
	 * @param condition
	 * @return
	 */
	public int addVpc(Map<String,Object> condition);
	
	/**
	 * 更新IP数量
	 * @param condition
	 * @return
	 */
	public int updateVpcIpAmount(Map<String, Object> condition);
	
	
    /**
     * 更新主机数量
     * @param condition
     * @return
     */
    public int updateVpcHostAmount(Map<String, Object> condition);
    
    /**
     * 根据主机ID查询关联VPC
     * @param hostId
     * @return
     */
    public VpcBaseInfoVO  queryVpcByHostId(String hostId);
	
    /**
     * 根据vpcid删除vpc
     * @param vpcId
     * @return
     */
    public int deleteVpc(String vpcId);
	/**
	 * 根据vpcid批量删除vpc
	 * @param ids
	 * @return
	 */
	public int deleteVpcByIds(List<String> ids);
	
	
	/**
	 * 逻辑删除vpc
	 * @param ids
	 * @return
	 */
	public int logicDeleteVpcByIds(List<String> ids);
	
	/**
	 * 通过vpcId查询vpc
	 * @param id
	 * @return
	 */
	public VpcBaseInfoVO queryVpcById(String id);
	
	/**
	 * 更新vpc
	 * @param condition
	 * @return
	 */
	public int updateVpc(Map<String, Object> condition);
}

