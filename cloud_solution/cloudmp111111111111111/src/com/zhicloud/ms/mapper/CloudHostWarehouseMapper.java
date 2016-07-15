package com.zhicloud.ms.mapper;

import com.zhicloud.ms.vo.CloudHostWarehouse;

import java.util.List;
import java.util.Map;


/**
 * @author ZYFTMX
 *
 */
public interface CloudHostWarehouseMapper {
	public List<CloudHostWarehouse> getAll();
	public int addWarehouse(Map<String,Object> condition);
	public int updateWarehouse(Map<String,Object> condition);
	public CloudHostWarehouse getById(String id);
	public CloudHostWarehouse getByName(String name);
	public int deleteWarehouse(String id);
	public int addAmount(Map<String,Object> condition);
	/**
	 * 
	 * updateWarehouseRemainByAddOne: 可用数量+1
	 * 传入仓库ID
	 * @author sasa
	 * @param condition
	 * @return int
	 * @since JDK 1.7
	 */
	public int updateWarehouseRemainByAddOne(Map<String,Object> condition);
	/**
	 * 
	 * updateWarehouseRemainByDeleteOne: 可用数量-1
	 * 传入仓库ID
	 * @author sasa
	 * @param condition
	 * @return int
	 * @since JDK 1.7
	 */
	public int updateWarehouseRemainByDeleteOne(Map<String,Object> condition);
	/**
	 * 
	 * updateWarehouseForDispachHost: 主机分配操作：可用数量 remain_amount-1 ，已分配assigned_amount+1
	 * 传入仓库ID
	 * @author sasa
	 * @param condition
	 * @return int
	 * @since JDK 1.7
	 */
	public int updateWarehouseForDispatchHost(Map<String,Object> condition);
	/**
	 * 
	 * updateWarehouseForRetrieveHost:回收一个主机时，可用数量 remain_amount + 1 ，已分配assigned_amount - 1
	 * 传入仓库ID
	 * @author sasa
	 * @param condition
	 * @return int
	 * @since JDK 1.7
	 */
	public int updateWarehouseForRetrieveHost(Map<String,Object> condition);
	/**
	 * 
	 * updateWarehouseAmountForDeleteHost:删除已经分配的主机
	 * 总数-1
	 * 已分配-1
	 * @author sasa
	 * @param id
	 * @return int
	 * @since JDK 1.7
	 */
	public int updateWarehouseAmountForDeleteDispatchedHost(String id);
	/**
	 * 
	 * updateWarehouseAmountForDeleteHost:删除创建成功未分配的主机
	 * 总数-1
	 * 未分配-1
	 * @author sasa
	 * @param id
	 * @return int
	 * @since JDK 1.7
	 */
	public int updateWarehouseAmountForDeleteHost(String id);
	/**
	 * 
	 * updateWarehouseAmountForDeleteNotCreatedHost:删除未创建的主机
	 * 总数-1 
	 * @author sasa
	 * @param id
	 * @return int
	 * @since JDK 1.7
	 */
	public int updateWarehouseAmountForDeleteNotCreatedHost(String id);
	
	/**
	 * 
	 * 批量删除仓库
	 * @param ids
	 * @return
	 */
	public int deleteByIds(List<String> ids);
	/**
	 * 修改检查时间和最小库存
	 * @param condition
	 * @return
	 */
	public int updateCheckTimeOrMinimum(Map<String,Object> condition);
	/**
	 * 
	* @Title: updateWarehouseForAddNewHost 
	* @Description: 总数+1 ；剩余+1
	* @param @param id
	* @param @return      
	* @return int     
	* @throws
	 */
	public int updateWarehouseForAddNewHost(String id);
	/**
	 * 
	* @Title: correctAllCount 
	* @Description: 根据仓库id更正当前仓库 总量 剩余 和已分配
	* @param @param id
	* @param @return      
	* @return int     
	* @throws
	 */
	public int correctAllCount(String id);
	
    /**
     * @Description:增加资源池最大并发创建数
     * @param condition 参数
     * @return int
     */
    public int addConcurrent(Map<String, Object> condition);

    /**
     * @Description:更新资源池最大并发创建数
     * @param condition 参数
     * @return int
     */
    public int updateConcurrent(Map<String, Object> condition);
    
    /**
     * @Description:取得所有的资源池最大并发创建数集合
     * @return List<CloudHostWarehouse>
     */
    public List<CloudHostWarehouse> getAllConcurrent();
    
    /**
     * @Description:取得单个的资源池保存的信息
     * @return CloudHostWarehouse
     */
    public CloudHostWarehouse getConcurrent(String pool_id);

    /**
     *
     * @param ConfigModelId 关联类型ID
     * @return List<CloudHostWarehouse>
     */
    public List<CloudHostWarehouse> getByConfigModelId(String ConfigModelId);
}
