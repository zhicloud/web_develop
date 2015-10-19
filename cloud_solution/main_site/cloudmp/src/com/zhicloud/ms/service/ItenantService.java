package com.zhicloud.ms.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.vo.CloudDisk;
import com.zhicloud.ms.vo.CloudHostVO;
import com.zhicloud.ms.vo.SysTenant;


public interface ItenantService {
	
	/**
	 * 
	 * getAllSysTenant:获取所有的租户配置 .  
	 * @author wildBear
	 * @return List<CloudDisk>
	 * @since JDK 1.7
	 */
	public List<SysTenant> getAllSysTenant(SysTenant parameter);
	/**
	 * 
	* @Title: getAllSysTenantByUserId 
	* @Description: 根据用户id查询租户
	* @param @param userId
	* @param @return      
	* @return List<SysTenant>     
	* @throws
	 */
	public List<SysTenant> getAllSysTenantByUserId(String userId);
	
	/**
	 * 
	 * addSysTenant: 增加租户  
	 * @author wildBear
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	public MethodResult addSysTenant(SysTenant tenant,	HttpServletRequest request);

	/**
	 * 
	 * getSysTenantById: 获取租户byID 
	 * @author wildBear
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	public SysTenant getSysTenantById(String id);
	
	/**
	 * 
	 * modifyTenant: 修改租户
	 * @author wildBear
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	public MethodResult modifyTenant(SysTenant tenant, HttpServletRequest request);
	
	/**
	 * 
	 * modifyTenant: 修改租户
	 * @author wildBear
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	public MethodResult delTenant(String ids);
	
} 
