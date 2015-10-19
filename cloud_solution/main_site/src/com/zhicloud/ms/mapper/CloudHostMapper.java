/**
 * Project Name:CloudDeskTopMS
 * File Name:CloudHostMapper.java
 * Package Name:com.zhicloud.ms.mapper
 * Date:2015年3月16日下午12:44:18
 * 
 *
*/
/**
 * Project Name:CloudDeskTopMS
 * File Name:CloudHostMapper.java
 * Package Name:com.zhicloud.ms.mapper
 * Date:2015年3月16日下午12:44:18
 * 
 *
 */

package com.zhicloud.ms.mapper; 

import java.util.List;
import java.util.Map;

import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.vo.CloudHostVO;

/**
 * ClassName: CloudHostMapper 
 * Function: 用于查询cloud_host表数据. 
 * 
 * date: 2015年3月16日 下午12:44:18 
 *
 * @author sasa
 * @version 
 * @since JDK 1.7
 */
public interface CloudHostMapper {
	/**
	 * 
	 * getCloudHost:查询主机.   
	 * 不传入用户名就表示查询全部主机. 
	 *
	 * @author sasa
	 * @param condition
	 * @return List<CloudHost>
	 * @since JDK 1.7
	 */
	public List<CloudHostVO> getCloudHost(Map<String, Object> condition);
	/**
	 * 
	 * getCloudHostById:根据主机ID查询主机信息.  
	 *
	 * @author sasa
	 * @param condition
	 * @return CloudHost
	 * @since JDK 1.7
	 */
	public CloudHostVO getCloudHostById(Map<String, Object> condition);
	/**
	 * 
	 * updateRunningStatusByRealHostId:根据ID更新主机的运行状态.  
	 *
	 * @author sasa
	 * @param cloudHostData
	 * @return int
	 * @since JDK 1.7
	 */
	public int updateRunningStatusByRealHostId(Map<String, Object> cloudHostData);
	
	/**
	 * 
	 * getByRealHostId: 通过真实id查询主机
	 * @author sasa
	 * @param readHostId
	 * @return CloudHost
	 * @since JDK 1.7
	 */
	public CloudHostVO getByRealHostId(String readHostId);
	/**
	 * 
	 * getByHostName:通过host_name查找主机
	 *
	 * @author sasa
	 * @param cloudHostData
	 * @return CloudHost
	 * @since JDK 1.7
	 */
	public CloudHostVO getByHostName(Map<String, Object> cloudHostData);
	/**
	 * 
	 * updateRealHostIdById:通过Id更新主机真实id
	 *
	 * @author sasa
	 * @param cloudHostData
	 * @return int
	 * @since JDK 1.7
	 */
	public int updateRealHostIdById(Map<String, Object> cloudHostData);
	/**
	 * 
	 * getCloudHostByWareId:根据仓库Id筛选出主机列表 
	 *
	 * @author sasa
	 * @param cloudHostData
	 * @return List<CloudHostVO>
	 * @since JDK 1.7
	 */
	public List<CloudHostVO> getCloudHostByWareId(Map<String, Object> cloudHostData);
	/**
	 * 
	 * getOneUncreatedCloudHost:获取一个未创建的主机
	 *
	 * @author sasa
	 * @param getOneUncreatedCloudHost
	 * @return CloudHostVO
	 * @since JDK 1.7
	 */
	public CloudHostVO getOneUncreatedCloudHost();
	/**
	 * 
	 * updateStautsById:根据ID更新主机创建状态 
	 *
	 * @author sasa
	 * @param cloudHostData
	 * @return int
	 * @since JDK 1.7
	 */
	public int updateStautsById(Map<String, Object> cloudHostData);
	
	/**
	 * 往数据库增加一条云主机信息
	 * @param condition
	 * @return
	 */
	public int insertCloudHost(Map<String,Object> condition);
	/**
	 * 
	 * updateCloudHostUserIdByWarehouseId:根据仓库id分配一个云主机 
	 *
	 * @author sasa
	 * @param Map<String, Object> cloudHostData
	 * @return int
	 * @since JDK 1.7
	 */
	public int updateCloudHostUserIdById(Map<String, Object> cloudHostData);
	/**
	 * 
	 * getHostNotDispatchByWarehouseId:根据仓库id取出全部未分配主机 
	 *
	 * @author sasa
	 * @param data
	 * @return List<CloudHostVO>
	 * @since JDK 1.7
	 */
	public List<CloudHostVO> getHostNotDispatchByWarehouseId(Map<String, Object> data); 
	
	/**
	 * 根据仓库Id查询云主机
	 * @param warehouseId
	 * @return
	 */
	public List<CloudHostVO> getByWarehouseId(String warehouseId);

    /**
     * 根据仓库Id以及多个参数查询云主机
     * @param parameter
     * @return
     */
    public List<CloudHostVO> queryByWarehouseIdAndParams(Map<String, Object> parameter);

	/**
	 * 根据主机id删除一条主机记录
	 * @param id
	 */
	public void deleteById(String id);
	/**
	 * 
	 * getAllCloudHost:获取所有的主机
	 *
	 * @author sasa
	 * @return List<CloudHostVO>
	 * @since JDK 1.7
	 */
	public List<CloudHostVO> getAllCloudHost();
	/**
	 * 
	 * getCloudHostByUserId:根据用户id查询主机
	 *
	 * @author sasa
	 * @param userId
	 * @return List<CloudHostVO>
	 * @since JDK 1.7
	 */
	public List<CloudHostVO> getCloudHostByUserId(String userId);
	
	/**
	 * 获取所有非仓库云主机
	 * 
	 * @return
	 */
	public List<CloudHostVO> getAllServer();
	
	/**
	 * 根据id查询主机
	 * 
	 * @param id
	 * @return
	 */
	public CloudHostVO getById(String id);
	
	/**
	 * 修改主机配置
	 * @param condition
	 * @return
	 */
	public int updateById(Map<String,Object> condition);
 
	/**
	 * 
	* @Title: getDesktopCloudHostNotInTimerBackUp 
	* @Description: 查询已分配且未参与定时任务的云桌面主机 
	* @param @return      
	* @return List<CloudHostVO>     
	* @throws
	 */
	public List<CloudHostVO> getDesktopCloudHostNotInTimerBackUp();
	/**
	 * 
	* @Title: getDesktopCloudHostInTimerBackUp 
	* @Description: 查询已分配且参与定时任务的云桌面主机  
	* @param @return      
	* @return List<CloudHostVO>     
	* @throws
	 */
	public List<CloudHostVO> getDesktopCloudHostInTimerBackUp();
	
	/**
	 * 
	* @Title: getCloudHostInTimerBackUpStart 
	* @Description: 获取目前仍开机的主机，备份前强制关机
	* @param @return      
	* @return List<CloudHostVO>     
	* @throws
	 */
    public List<CloudHostVO> getCloudHostInTimerBackUpStart(String timerKey);
    /**
     * 
    * @Title: getDesktopCloudHostInTimerBackUpStop 
    * @Description: 获取规定条数以内的主机
    * @param @param condition
    * @param @return      
    * @return List<CloudHostVO>     
    * @throws
     */
    public List<CloudHostVO> getDesktopCloudHostInTimerBackUpStop(Map<String,Object> condition);
    
    /**
     * 
    * @Title: updateBackUpTimeByHostId 
    * @Description: 备份命令发送之后更新备份时间
    * @param @param condition
    * @param @return      
    * @return int     
    * @throws
     */
    public int updateBackUpTimeByHostId(Map<String,Object> condition);
    /**
     * 
    * @Title: addHostIntoTimer 
    * @Description: 新增主机到桌面云 
    * @param @param condition
    * @param @return      
    * @return int     
    * @throws
     */
    public int addHostIntoTimer(Map<String,Object> condition);
    /**
     * 
    * @Title: deleteAllCloudHostInTimer 
    * @Description: 删除定时器内的所有主机
    * @param @param key 定时器名
    * @param @return      
    * @return int     
    * @throws
     */
    public int deleteAllCloudHostInTimer(String key);
    /**
     * 
    * @Title: updateStautsByName 
    * @Description: 根据主机名设置主机状态 
    * @param @param data
    * @param @return      
    * @return int     
    * @throws
     */
    public int updateStautsByName(Map<String, Object> data);
 

    /**
     * @function 查询已分配且未参与定时任务的主机
     * @author 张翔
     * @param key 定时器名
     * @return
     */
    public List<CloudHostVO> queryCloudHostNotInTimer(String key);

    /**
     * @function 查询已分配且未参与定时任务的云桌面主机
     * @author 张翔
     * @param key 定时器名
     * @return
     */
    public List<CloudHostVO> queryCloudHostInTimer(String key);


    /**
     * @function 获取所有VPC中的云主机
     * @author 周亚锋
     * @param condition
     * @return
     */
    public List<CloudHostVO> getAllHostByVpcId(Map<String,Object> condition);
    
    /**
     * 查询正在vpc中的主机
     * @param vpcId
     * @return
     */
    public List<CloudHostVO> getCloudHostInVpc(String vpcId);
    
    /**
     * 更新vpcIP
     * @param condition
     * @return
     */
    public int updateVpcIpById(Map<String, Object> condition);
     /**
     * 
    * @Title: getAllAallocateDesktopHost 
    * @Description: 获取桌面云已分配主机
    * @param @return      
    * @return List<CloudHostVO>     
    * @throws
     */
    public List<CloudHostVO> getAllAallocateDesktopHost(); 
    /**
     * 
    * @Title: bindHostToTenant 
    * @Description: 主机绑定租户 
    * @param @param condition
    * @param @return      
    * @return Integer     
    * @throws
     */
    public Integer bindHostToTenant(Map<String, Object> condition);
    /**
     * 
    * @Title: unboundHostInTenant 
    * @Description: 根据主机id解绑主机 
    * @param @param hostId
    * @param @return      
    * @return Integer     
    * @throws
     */
    public Integer unboundHostInTenant(String hostId);
    /**
     * 
    * @Title: getCloudHostInTenant 
    * @Description: 根据主机 
    * @param @param tenantId
    * @param @return      
    * @return List<CloudHostVO>     
    * @throws
     */
    public List<CloudHostVO> getCloudHostInTenant(String tenantId);
    
    /**
     * 根据真实ID删除云主机
     * @param id
     * @return
     */
    public int deleteByRealId(String id);
    /**
     * 
    * @Title: updateInnerIpByRealHostId 
    * @Description: 根据主机真实id更新innerip和innerport
    * @param @param condition
    * @param @return      
    * @return int     
    * @throws
     */ 
    public int updateInnerIpByRealHostId(Map<String, Object> condition);
}


