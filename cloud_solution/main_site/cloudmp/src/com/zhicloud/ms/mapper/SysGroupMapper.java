/**
 * Project Name:ms
 * File Name:SysGroupMapper.java
 * Package Name:com.zhicloud.ms.mapper
 * Date:Mar 16, 20154:01:35 PM
 * 
 *
 */

package com.zhicloud.ms.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.ms.vo.SysGroupVO;

/**
 * ClassName: SysGroupMapper 
 * Function: 用于查询sys_group表数据. 
 * date: Mar 16, 2015 4:01:35 PM 
 *
 * @author sean
 * @version 
 * @since JDK 1.7
 */
public interface SysGroupMapper {

	public List<SysGroupVO> getAll();
	
	public SysGroupVO getById(String groupId);
	
	public Integer addSysGroup(Map<String, Object> groupData);

	public Integer updateSysGroupById(Map<String, Object> groupData);
	
	public Integer deleteSysGroupById(String groupId);
	
	public Integer deleteSysGroupByIds(List<String> groupIds);

	/**
	 * queryGroupByUsername:(这里用一句话描述这个方法的作用).  
	 *
	 * @author sean
	 * @param groupId
	 * @return SysGroupVO
	 * @since JDK 1.7
	 */
	public SysGroupVO queryGroupByGroupName(String groupName);

	/**
	 * queryByParentId:(这里用一句话描述这个方法的作用).  
	 *
	 * @author sean
	 * @param parentId
	 * @return List<SysGroupVO>
	 * @since JDK 1.7
	 */
	public List<SysGroupVO> queryByParentId(String parentId);


}

