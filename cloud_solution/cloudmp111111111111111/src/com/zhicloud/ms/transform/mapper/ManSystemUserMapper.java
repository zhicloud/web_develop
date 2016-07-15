package com.zhicloud.ms.transform.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.ms.transform.vo.ManSystemUserVO;



/**
 * @ClassName: SystemUser
 * @Description: 系统用户ibatis映射
 * @author 张本缘 于 2015年4月23日 下午3:57:06
 */
public interface ManSystemUserMapper {
    
    /**
     * @Description:取得所有用户信息
     * @return
     */
    public List<ManSystemUserVO> getAll();

    /**
     * @Description:取得所有未设置归属租户的用户
     * @return
     */
    public List<ManSystemUserVO> queryUserOutTenant(String tenant_id);
    
    /**
     * @Description:取得该归属租户ID的用户
     * @param tenant_id
     * @return
     */
    public List<ManSystemUserVO> queryUserTenant(String tenant_id);  
    
    
    /**
     *  @Description:根据多个条件取得所有用户信息
     *  @author 张翔
     * @param condition
     * @return
     */
    public List<ManSystemUserVO> queryAllWithConditions(Map<String, Object> condition);

    /**
     * @Description:验证用户账号或者邮箱是否已经存在
     * @param condition 用户信息
     */
    public ManSystemUserVO validateUserIsExists(Map<String, Object> data);

    /**
     * @Description:新增用户
     * @param condition 用户信息
     */
    public int addSystemUser(Map<String, Object> data);

    /**
     * @Description:更新用户信息
     * @param condition 用户信息
     */
    public int updateSystemUser(Map<String, Object> data);

    /**
     * @Description:批量删除用户信息
     * @param billids 主键数组
     */
    public int deleteSystemUserByIds(String[] billids);
    
    /**
     * @Description:根据ID取得用户信息
     * @param billid
     * @return ManSystemUserVO
     */
    public ManSystemUserVO getUserById(String billid);
    
    /**
     * @Description:修改用户信息时验证用户账号和邮箱是否已经存在
     * @param data
     * @return ManSystemUserVO
     */
    public ManSystemUserVO validateUserIsExistsMod(Map<String, Object> data);
    
    /**
     * @Description:根据角色Id查询该角色拥有的用户信息
     * @param billid
     * @return List
     */
    public List<ManSystemUserVO> queryUserInRoleID(String billid);
    
    /**
     * @Description:根据角色ID查询不在该角色里面的用户信息
     * @param billid
     * @return List
     */
    public List<ManSystemUserVO> queryUserOutRoleID(String billid);
    
    /**
     * @Description:根据角色组ID查询该角色组拥有的用户信息
     * @param billid
     * @return
     */
    public List<ManSystemUserVO> queryUserInGroupID(String billid); 
    
    /**
     * @Description:根据角色组ID查询不在该角色组里面的用户信息
     * @param billid
     * @return List
     */
    public List<ManSystemUserVO> queryUserOutGroupID(String billid);   
    
    /**
     * @Description:根据条件查询用户信息
     * @param data 条件
     * @return ManSystemUserVO
     */
    public ManSystemUserVO getUserByCondition(Map<String, Object> data);
    
    /**
     * @Description:根据用户ID删除用户和角色关联关系
     * @param userid
     * @return int
     */
    public int deleteUserRoleByUserID(String userid);
    
    /**
     * @Description:清除所有tenantId的记录。以便重新写入。
     * @param tenantId
     * @return
     */
	public void updateTenantToNull(String tenant_id);
	
    /**
     * @Description:设置tenant_id
     * @param tenantId
     * @param billid
     * @return
     */
	public void updateTenantById(Map<String, Object> data);
	/**
	 * 
	* @Title: addUserTenantRelationship 
	* @Description: 新增租户和管理员关系
	* @param @param data
	* @param @return      
	* @return Integer     
	* @throws
	 */
	public Integer addUserTenantRelationship(Map<String, Object> data);
	/**
	 * 
	* @Title: deleteRelationshipByTenantId 
	* @Description: 根据租户id删除租户分配的所有管理员 
	* @param @param tenantId
	* @param @return      
	* @return Integer     
	* @throws
	 */
	public Integer deleteRelationshipByTenantId(String tenantId);
	/**
	 * 
	* @Title: deleteRelationshipByUserId 
	* @Description: 根据用户id删除用户和租户关系信息 
	* @param @param userId
	* @param @return      
	* @return Integer     
	* @throws
	 */
	public Integer deleteRelationshipByUserId(String userId);
}
