/**
 * Project Name:CloudDeskTopMS
 * File Name:ICloudHostService.java
 * Package Name:com.zhicloud.ms.service
 * Date:2015年3月16日上午10:59:39
 * 
 *
*/
/**
 * Project Name:CloudDeskTopMS
 * File Name:ICloudHostService.java
 * Package Name:com.zhicloud.ms.service
 * Date:2015年3月16日上午10:59:39
 * 
 *
 */

package com.zhicloud.ms.service; 

import com.zhicloud.ms.app.pool.CloudHostData;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.vo.CloudHostVO;
import com.zhicloud.ms.vo.SysUser;

import javax.servlet.http.HttpServletRequest;

import java.util.List;
import java.util.Map;

/**
 * ClassName: ICloudHostService 
 * Function: 提供云主机的各种方法接口. 
 * 
 * date: 2015年3月16日 上午10:59:39 
 *
 * @author sasa
 * @version 
 * @since JDK 1.7
 */
public interface ICloudHostService {
	
	/**
	 * 
	 * queryCloudHostForTerminal:查询该用户下的所有主机
	 *
	 * @author sasa
	 * @param user
	 * @param isInnerFlag 判断是否是内网ip,true:返回内网地址 false:返回外网地址
	 * @return List<Map<Object,Object>>
	 * @since JDK 1.7
	 */
	public List<Map<Object, Object>> queryCloudHostForTerminal(SysUser user,boolean isInnerFlag); 
	/**
	 * 
	 * queryCloudHostById:通过主机名查询主机.   
	 * @author sasa
	 * @param cloudHostId
	 * @return CloudHost
	 * @since JDK 1.7
	 */
	public CloudHostVO queryCloudHostById(String cloudHostId);
	/**
	 * 
	 * operatorCloudHost:对主机进行开机、关机、重启和强制关机的操作.   
	 *
	 * @author sasa
	 * @param cloudHostId
	 * @param operatorType
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	public MethodResult operatorCloudHost(String cloudHostId,String operatorType);
	/**
	 * 
	* @Title: operatorCloudHostByRealHostId 
	* @Description: 通过真实ID对主机进行开关机操作
	* @param @param realHostId
	* @param @param operatorType
	* @param @return      
	* @return MethodResult     
	* @throws
	 */
	public MethodResult operatorCloudHostByRealHostId(String realHostId,String operatorType);
	/**
	 * 
	 * fetchNewestCloudHostFromHttpGateway:从http-gateway同步新的主机信息
	 * @author sasa
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	public MethodResult fetchNewestCloudHostFromHttpGateway();
	
	/**
	 * 
	 * getHostByTypeId:通过仓库ID筛选主机 
	 *
	 * @author sasa
	 * @param id
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	public List<CloudHostVO> getHostByWareId(String id);

    /**
     *
     * queryByWarehouseIdAndParams:通过仓库ID以及多个参数筛选主机
     *
     * @author 张翔
     * @param parameter
     * @return MethodResult
     * @since JDK 1.7
     */
    public List<CloudHostVO> getByWarehouseIdAndParams(Map<String, Object> parameter);
	
	/**
	 * 
	 * createOneCloudHost:选择一个未创建的主机进行创建
	 *
	 * @author sasa
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	public MethodResult createOneCloudHost();
	
	/**
	 * 查询所有云主机
	 * @return
	 */
	public List<CloudHostVO> getAllHost();
	/**
	 * 按仓库id查询所有云主机
	 * @param id
	 * @return
	 */
	public List<CloudHostVO> getByWarehouseId(String id);
	/**
	 * 
	 * getAllCloudHost:获取所有的云主机 
	 *
	 * @author sasa
	 * @return List<CloudHostVO>
	 * @since JDK 1.7
	 */
	public List<CloudHostVO> getAllCloudHost();
	/**
	 * 
	 * updateRunningStatusByRealHostId:更新运行状态
	 *
	 * @author sasa
	 * @param cloudHostData
	 * @return int
	 * @since JDK 1.7
	 */
	public int updateRunningStatusByRealHostId(Map<String, Object> cloudHostData);
	
	public MethodResult deleteById(String id);
	/**
	 * 
	 * getCloudHostByUserId:根据用户id获取主机
	 *
	 * @author sasa
	 * @param userId
	 * @return List<CloudHostVO>
	 * @since JDK 1.7
	 */
	public List<CloudHostVO> getCloudHostByUserId(String userId);
	/**
	 * 
	 * getDisassociationById:解除主机与用户关联 
	 * cloudhost user_id重置成null
	 * 分配之间重置成null
	 * 仓库库存+1
	 * 用户已分配 -1
	 * @author sasa
	 * @param id
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	public MethodResult getDisassociationById(String id);
	/**
	 * 
	 * deleteByIds:批量删除主机
	 *
	 * @author sasa
	 * @param ids
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	public MethodResult deleteByIds(String ids);
	
	/**
	 * 获取所有非仓库云主机
	 * @return
	 */
	public List<CloudHostVO> getAllServer();
	
	/**
	 * 添加云服务器
	 * @param server
	 * @return
	 */
	public MethodResult addServer(CloudHostVO server,HttpServletRequest request);
	
	/**
	 * 根据id查询云主机
	 * @param id
	 * @return
	 */
	public CloudHostVO getById(String id);
	
	
	/**
	 * 修改主机配置
	 * @param cloudHost
	 * @return
	 */
	public MethodResult modifyAllocation(CloudHostVO cloudHost);
	
	/**
	 * 根据主机id获取资源信息
	 * @param id
	 * @return
	 */
	public CloudHostData refreshData(String id);
	
	/**
	 * 通过真实id查询主机
	 * @param realId
	 * @return
	 */
	public CloudHostVO getByRealHostId(String realId);
	/**
     * 
    * @Title: updateRunningStatusFromTerminal 
    * @Description: 终端更正主机主机运行状态
    * @param @param hostId
    * @param @param runningStatus
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    public MethodResult updateRunningStatusFromTerminal(String hostId,String runningStatus);
    
    /**
     * 
    * @Title: getDesktopCloudHostNotInTimerBackUp 
    * @Description: 查询没有参与定时任务的云桌面主机（已分配）
    * @param @return      
    * @return List<CloudHostVO>     
    * @throws
     */
    public List<CloudHostVO> getDesktopCloudHostNotInTimerBackUp();
    /**
     * 
    * @Title: getDesktopCloudHostNotInTimerBackUp 
    * @Description: 查询没有参与定时任务的云服务器
    * @param @return      
    * @return List<CloudHostVO>     
    * @throws
     */
    public List<CloudHostVO> getServerCloudHostNotInTimerBackUp();
    /**
     * 
    * @Title: getDesktopCloudHostInTimerBackUp 
    * @Description: 查询参与到定时任务的桌面云主机
    * @param @return      
    * @return List<CloudHostVO>     
    * @throws
     */
    public List<CloudHostVO> getDesktopCloudHostInTimerBackUp();
    /**
     * 
    * @Title: getServerCloudHostInTimerBackUp 
    * @Description: 查询参与到定时任务的云服务器
    * @param @return      
    * @return List<CloudHostVO>     
    * @throws
     */
    public List<CloudHostVO> getServerCloudHostInTimerBackUp();
    /**
     * 
    * @Title: getCloudHostInTimerBackUpStart 
    * @Description: 获取还处于开机状态的且加入了定时备份任务的主机 
    * @param @return      
    * @return List<CloudHostVO>     
    * @throws
     */
    public List<CloudHostVO> getCloudHostInTimerBackUpStart(String timerKey);
    /**
     * 
    * @Title: getDesktopCloudHostInTimerBackUpStop 
    * @Description:获取规定条数以内的已关机的主机
    * @param @param limit
    * @param @return      
    * @return List<CloudHostVO>     
    * @throws
     */
    public List<CloudHostVO> getCloudHostInTimerBackUpStop(Integer limit,String now,String timerKey);
    /**
     * 
    * @Title: saveHostInTimer 
    * @Description: 更新定时器内的桌面云主机 
    * @param @param hostId
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    public MethodResult  saveHostInTimer(String[] hostId, String key);
    /**
     * 
    * @Title: updateHostBackUpTimeInTimer 
    * @Description: 更新主机在定时器中备份的最新时间 
    * @param @param hostId
    * @param @param now
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    public MethodResult  updateHostBackUpTimeInTimer(String hostId,String now);
    /**
     * 
    * @Title: updateHostStatusToFailByName 
    * @Description: 根据主机名更新主机状态为创建失败
    * @param @param name      
    * @return void     
    * @throws
     */
    public void updateHostStatusToFailByName(String name);
    /**
     * 
    * @Title: addHostToServerByRealHostId 
    * @Description: 从资源池新增云服务主机
    * @param @param realHostId
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    public MethodResult addHostToServerByRealHostId(String realHostId);
    /**
     * 
    * @Title: addHostToDeskTopByRealHostId 
    * @Description: 从资源池新增云桌面主机
    * @param @param realHostId
    * @param @param wareHouseId
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    public MethodResult addHostToDeskTopByRealHostId(String realHostId,String wareHouseId);
    
    /**
     * 查询vpc中的所有主机
     * @param vpcId
     * @return
     */
    public List<CloudHostVO> getAllHostByVpcId(String vpcId);
    /**
     * @function 查询没有参与定时任务的云桌面主机（已分配）
     * @author 张翔
     * @param key 定时器名
     * @return
     */
    public List<CloudHostVO> getCloudHostNotInTimer(String key);

    /**
     * @function 查询参与到定时任务的主机
     * @author 张翔
     * @param key 定时器名
     * @return
     */
    public List<CloudHostVO> getCloudHostInTimer(String key);
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
    * @Description: 根据主机id绑定租户
    * @param @param hostId
    * @param @param tenantId
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    public MethodResult bindHostToTenant(String hostId,String tenantId);
    /**
     * 
    * @Title: bindHostToTenant 
    * @Description: 绑定 
    * @param @param host
    * @param @param tenantId
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    public MethodResult bindHostToTenant(CloudHostVO host,String tenantId);
    /**
     * 
    * @Title: unboundHostInTenant 
    * @Description: 根据主机id解绑租户 
    * @param @param hostId
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    public MethodResult unboundHostInTenant(String hostId);
    /**
     * 
    * @Title: getHostInTenant 
    * @Description: 根据租户id查询主机
    * @param @param tenantId
    * @param @return      
    * @return List<CloudHostVO>     
    * @throws
     */
    public List<CloudHostVO> getHostInTenant(String tenantId);

    /**
     * 根据真实ID删除主机
     * @param id
     * @return
     */
    public MethodResult deleteByRealId(String id);
    /**
     * 
    * @Title: updateInnerIpByRealHostId 
    * @Description: TODO 
    * @param @param parameter
    * @param @return      
    * @return int     
    * @throws
     */
    public int updateInnerIpByRealHostId(Map<String, Object> parameter);
    /**
     * 
    * @Title: startCloudHostFromIso 
    * @Description: 从光盘启动云主机 
    * @param @param cloudHostId
    * @param @param imageId
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    public MethodResult startCloudHostFromIso(String cloudHostId,String imageId);
}

