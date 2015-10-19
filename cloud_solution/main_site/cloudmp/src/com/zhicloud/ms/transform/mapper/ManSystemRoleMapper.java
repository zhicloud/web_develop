
package com.zhicloud.ms.transform.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.ms.transform.vo.ManSystemRightVO;
import com.zhicloud.ms.transform.vo.ManSystemRoleVO;

/**
 * @ClassName: ManSystemRoleMapper
 * @Description: 系统角色和角色组ibatis映射
 * @author 张本缘 于 2015年5月7日 上午9:36:20
 */
public interface ManSystemRoleMapper {
    /**
     * @Description:取得所有角色信息
     * @return
     */
    public List<ManSystemRoleVO> getAll();

    /**
     * @Description:取得所有角色组信息
     * @return
     */
    public List<ManSystemRoleVO> getAllGroup();

    /**
     * @Description:验证角色名称或者编码是否已经存在
     * @param condition 用户信息
     */
    public ManSystemRoleVO validateRoleIsExists(Map<String, Object> data);

    /**
     * @Description:验证角色组名称是否已经存在
     * @param condition 角色组信息
     */
    public ManSystemRoleVO validateGroupIsExists(Map<String, Object> data);

    /**
     * @Description:新增角色
     * @param condition 角色信息
     */
    public int addSystemRole(Map<String, Object> data);

    /**
     * @Description:新增角色组
     * @param condition 角色组信息
     */
    public int addSystemGroup(Map<String, Object> data);

    /**
     * @Description:更新角色信息
     * @param condition 角色信息
     */
    public int updateSystemRole(Map<String, Object> data);

    /**
     * @Description:更新角色组信息
     * @param condition 角色组信息
     */
    public int updateSystemGroup(Map<String, Object> data);

    /**
     * @Description:批量删除角色信息
     * @param billids 主键数组
     */
    public int deleteSystemRoleByIds(String[] billids);

    /**
     * @Description:批量删除角色组信息
     * @param billids 主键数组
     */
    public int deleteSystemGroupByIds(String[] billids);

    /**
     * @Description:根据ID取得角色信息
     * @param billid
     * @return ManSystemRoleVO
     */
    public ManSystemRoleVO getRoleById(String billid);

    /**
     * @Description:根据ID取得角色组信息
     * @param billid
     * @return ManSystemRoleVO
     */
    public ManSystemRoleVO getGroupById(String billid);

    /**
     * @Description:修改角色信息时验证名称或者编码是否已经存在
     * @param data
     * @return ManSystemRoleVO
     */
    public ManSystemRoleVO validateRoleIsExistsMod(Map<String, Object> data);

    /**
     * @Description:修改角色组信息时验证名称是否已经存在
     * @param data
     * @return ManSystemRoleVO
     */
    public ManSystemRoleVO validateGroupIsExistsMod(Map<String, Object> data);
    
    /**
     * @Description:根据角色ID删除角色和用户关联表信息
     * @param billids
     * @return
     */
    public int deleteUserRole(String roleid);

    /**
     * @Description:保存角色和用户关联信息
     * @param data
     * @return
     */
    public int saveUserRole(Map<String, Object> data);
    
    /**
     * @Description:取得所有角色组和角色的关联信息
     * @return
     */
    public List<ManSystemRoleVO> queryGroupInRoleID(String billid);
    
    /**
     * @Description:根据角色ID删除角色组合角色关联信息
     * @param roleid String
     * @return
     */
    public int deleteRoleGroup(String roleid);
    
    /**
     * @Description:保存角色和角色组的关联信息
     * @param data
     * @return
     */
    public int saveRoleGroup(Map<String, Object> data);    
    
    /**
     * @Description:根据角色组ID删除角色组和用户关联表信息
     * @param groupid
     * @return
     */
    public int deleteUserGroup(String groupid);

    /**
     * @Description:保存角色组和用户关联信息
     * @param data
     * @return
     */
    public int saveUserGroup(Map<String, Object> data);    
    
    /**
     * @Description:取得所有角色组和角色的关联信息
     * @return
     */
    public List<ManSystemRoleVO> queryRoleInGroupID(String billid);   
    
    /**
     * @Description:根据角色组ID删除角色组合角色关联信息
     * @param roleid String
     * @return
     */
    public int deleteGroupRole(String groupid);

    /**
     * @Description:保存角色和角色组的关联信息
     * @param data
     * @return
     */
    public int saveGroupRole(Map<String, Object> data);        
    
    /**
     * @Description:根据角色ID删除角色和权限的关联关系
     * @param menuid 菜单ID
     * @return int
     */
    public int deleteRoleRightByRole(String roleid);
    
    /**
     * @Description:增加角色和权限关联关系
     * @param data Map<String, Object>
     * @return int
     */
    public int saveRoleRight(Map<String, Object> data);    
    
    /**
     * @Description:判断角色和权限是否存在关联
     * @param data
     * @return ManSystemRoleVO
     */
    public ManSystemRoleVO queryRoleByRoleAndMenu(Map<String, Object> data);
    
    /**
     * @Description:根据角色ID删除角色和菜单的关联关系
     * @param menuid 菜单ID
     * @return int
     */
    public int deleteRoleMenuByRole(String roleid);

    /**
     * @Description:增加角色和菜单关联关系
     * @param data Map<String, Object>
     * @return int
     */
    public int saveRoleMenu(Map<String, Object> data);       
    
    /**
     * @Description:根据角色ID删除用户和角色的关联关系
     * @param userid
     * @return int
     */
    public int deleteUserRoleByRoleID(String roleid);
    
    /**
     * @Description:根据角色ID删除角色组和角色的关联关系
     * @param roleid
     * @return int
     */
    public int deleteRoleGroupByRoleID(String roleid);
    
    /**
     * @Description:根据菜单ID取得该菜单关联的权限
     * @param menuid 菜单ID
     * @return List<ManSystemRightVO>
     */
    public List<ManSystemRightVO> getRightWithMenuID(String menuid);
    
}
