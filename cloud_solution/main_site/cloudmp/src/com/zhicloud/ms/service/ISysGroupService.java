/**
 * Project Name:ms
 * File Name:ISysGroup.java
 * Package Name:com.zhicloud.ms.service
 * Date:Mar 16, 20152:19:01 PM
 * 
 *
*/


package com.zhicloud.ms.service;

import java.util.List;
import java.util.Map;

import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.vo.SysGroupVO;

/**
 * ClassName: ISysGroup 
 * Function: 定义群组service层接口. 
 * date: Mar 16, 2015 2:19:01 PM 
 *
 * @author sean
 * @version 
 * @since JDK 1.7
 */
public interface ISysGroupService {
	
	public List<SysGroupVO> queryAll();
	
	public SysGroupVO queryById(String groupId);
	
	public boolean checkAvailable(String groupName);
	
	public List<SysGroupVO> getAllWithoutMyself(String groupId);
	
	public List<SysGroupVO> getAllAvailable(String groupId);

	public MethodResult addSysGroup(Map<String, Object> parameter);
	
	public MethodResult addGroupItem(Map<String, Object> parameter);
	
	public MethodResult updateSysGroupById(Map<String, Object> parameter);
	
	public MethodResult deleteSysGroupById(String groupId);
	
	public MethodResult deleteSysGroupByIds(List<String> groupId);
	
	/**
	 * 
	 * manageItems:管理群组成员. 
	 *
	 * @author sean
	 * @param parameter
	 * @return MethodResult
	 * @since JDK 1.7
	 */
	public MethodResult manageItems(Map<String, Object> parameter);	
	
	/**
	 * 
	 * getCloudHostAmountInGroup:获取群组成员数和其已分配云主机数.  
	 *
	 * @author sean
	 * @return int
	 * @since JDK 1.7
	 */
	public List<SysGroupVO> getCloudHostAmountInGroup();

	public List<SysGroupVO> getSysGroupByParentId(String parentId);

	public List<SysGroupVO> getAllChildren(String groupId);
	
	public List<SysGroupVO> removeAllChildren(String groupId);
}

