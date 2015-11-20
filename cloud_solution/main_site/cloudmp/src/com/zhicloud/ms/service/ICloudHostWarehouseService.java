package com.zhicloud.ms.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import com.zhicloud.ms.app.helper.DefaultTreeNode;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.vo.CloudHostWarehouse;


public interface ICloudHostWarehouseService {
	public List<CloudHostWarehouse> getAll();
	public MethodResult addWarehouse(CloudHostWarehouse chw,HttpServletRequest request);
	public CloudHostWarehouse getById(String id);
	public CloudHostWarehouse getByName(String name);
	public MethodResult updateWarehouse(String id,CloudHostWarehouse chw);
	public MethodResult deleteWarehouse(String id);
	public MethodResult addAmount(String id,String addAmount,String poolId);
	public DefaultTreeNode getAllTree();
	public MethodResult assignToUsers(String warehouseId,String[] nodes);
	public MethodResult assignToUsersTwo(String warehouseId,String[] nodes,String[] hostIds);
	/**
	 * 
	 * allocateHostsByWarehouseIdAndUserIds: 根据用户Id批量分配主机
	 *
	 * @author sasa
	 * @param warehouseId
	 * @param userIds
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	public MethodResult allocateHostsByWarehouseIdAndUserIds(String warehouseId,String userIds);
	
	/**
	 * @param menuid
	 * @param array
	 * @param roleid
	 * @return
	 */
	public JSONArray buildTreeJSON(String menuid, JSONArray array, String roleid);
	
	/**
	 * 批量删除仓库
	 * @param ids
	 * @return
	 */
	public MethodResult deleteByIds(List<String> ids);
	
	/**
	 * 修改检查时间或最小库存
	 * @param condition
	 * @return
	 */
	public MethodResult updateCheckTimeOrMinimum(Map<String,Object> condition);
	/**
	 * 
	* @Title: updateCount 
	* @Description: 根据仓库id核对仓库当前数量的正确性
	* @param @param warehouseId
	* @param @return      
	* @return MethodResult     
	* @throws
	 */
	public MethodResult updateCount(String warehouseId);
	
    /**
     * @Description:增加资源池最大并发创建数
     * @param condition 参数
     * @return int
     */
    public int addConcurrent(Map<String, Object> condition);

    /**
     * @Description:修改资源池最大并发创建数
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
     * @Description:保存资源池最大并发创建数设置
     * @param condition 参数
     * @return MethodResult
     */
    public int saveConcurrent(Map<String, Object> condition);
}
