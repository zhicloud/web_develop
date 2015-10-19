/**
 * Project Name:ms
 * File Name:TerminalUserMapper.java
 * Package Name:com.zhicloud.ms.mapper
 * Date:Mar 17, 201510:49:20 PM
 * 
 *
 */

package com.zhicloud.ms.mapper;

import com.zhicloud.ms.vo.TerminalUserVO;

import java.util.List;
import java.util.Map;

/**
 * ClassName: TerminalUserMapper 
 * Function: 用于查询terminal_user表数据. 
 * date: Mar 17, 2015 10:49:20 PM 
 *
 * @author sean
 * @version 
 * @since JDK 1.7
 */
public interface TerminalUserMapper {

	public List<TerminalUserVO> getAll();

    public List<TerminalUserVO> queryAllWithCondition(Map<String, Object> condition);
	
	public TerminalUserVO getById(String userId);
	
	public Integer addTerminalUser(Map<String, Object> terminalUserData);

	public Integer updateTerminalUserById(Map<String, Object> terminalUserData);
	
	public Integer updateUSBStatusById(Map<String, Object> userData);
	/**
	 * 
	* @Title: updateStatusById 
	* @Description: 更新用户状态 0：正常 1：禁用  9 ：删除
	* @param @param userData
	* @param @return      
	* @return Integer     
	* @throws
	 */
	public Integer updateStatusById(Map<String, Object> userData);

	public Integer updateCloudHostAmount(Map<String, Object> userData);

	/**
	 * 
	* @Title: deleteTerminalUserById 
	* @Description: 物理删除用户 
	* @param @param userId
	* @param @return      
	* @return Integer     
	* @throws
	 */
	public Integer deleteTerminalUserById(String userId);
	
	public Integer deleteTerminalUserByIds(String[] userIds);
 	/**
	 * 
	 * updateCloudHostAmountForDelete:减少用户的主机分配 -1
	 *
	 * @author sasa
	 * @param userData
	 * @return Integer
	 * @since JDK 1.7
	 */
	public Integer updateCloudHostAmountForDelete(Map<String, Object> userData);
	
 
	
	public List<TerminalUserVO> getUserByGroupId(String groupId);
	/**
	 * 
	* @Title: getUserByGroupIdForBoxAllocate 
	* @Description: 查询所有未分配盒子的用户，分配盒子 
	* @param @param groupId
	* @param @return      
	* @return List<TerminalUserVO>     
	* @throws
	 */
	public List<TerminalUserVO> getUserByGroupIdForBoxAllocate(String groupId);
}

