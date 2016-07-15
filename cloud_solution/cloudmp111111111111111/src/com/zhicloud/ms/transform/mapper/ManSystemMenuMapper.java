
package com.zhicloud.ms.transform.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.ms.transform.vo.ManSystemMenuVO;

/**
 * @ClassName: ManSystemMenuMapper
 * @Description: 菜单映射
 * @author 张本缘 于 2015年5月12日 下午3:58:30
 */
public interface ManSystemMenuMapper {
    /**
     * @Description:取得所有菜单信息
     * @return
     */
    public List<ManSystemMenuVO> getAll();

    /**
     * @Description:取得所有的一级菜单信息
     * @return
     * @throws
     */
    public List<ManSystemMenuVO> getAllParent();

    /**
     * @Description:根据父节点ID取得下面的子菜单信息
     * @param parentid
     * @return
     */
    public List<ManSystemMenuVO> getChildren(String parentid);

    /**
     * @Description:新增菜单信息
     * @param data
     * @return int
     */
    public int addSystemMenu(Map<String, Object> data);

    /**
     * @Description:修改菜单信息
     * @param data
     * @return int
     */
    public int updateSystemMenu(Map<String, Object> data);

    /**
     * @Description:批量删除菜单信息
     * @param billids
     * @return int
     */
    public int deleteSystemMenuByIds(String[] billids);

    /**
     * @Description:验证菜单名称是否已经存在
     * @param condition 菜单信息
     */
    public ManSystemMenuVO validateMenuIsExists(Map<String, Object> data);
    
    /**
     * @Description:验证菜单名称是否已经存在
     * @param condition 菜单信息
     */
    public ManSystemMenuVO validateMenuIsExistsMod(Map<String, Object> data);   
    
    /**
     * @Description:根据ID取得菜单信息
     * @param billid
     * @return ManSystemMenuVO
     */
    public ManSystemMenuVO getMenuById(String billid);
    
    /**
     * @Description:根据用户ID取得该用户拥有的所有子菜单权限
     * @param userid 用户ID
     * @return List<ManSystemMenuVO>
     */
    public List<ManSystemMenuVO> getChildrenMenuByUserID(Map<String, Object> data);
    
    /**
     * @Description:根据用户ID取得该用户拥有的所有上级菜单权限
     * @param userid 用户ID
     * @return List<ManSystemMenuVO>
     */
    public List<ManSystemMenuVO> getParentMenuByUserID(Map<String, Object> data);  
    
    /**
     * @Description:根据菜单ID删除菜单和角色的关联关系
     * @param menuid
     * @return int
     */
    public int deleteRoleMenuByMenuID(String menuid);
    
    /**
     * @Description:根据菜单ID删除菜单和权限的关联关系
     * @param menuid 菜单ID
     * @return int
     */
    public int deleteRightMenuByMenuID(String menuid);

    /**
     * @Description:保存菜单和功能权限的关联关系
     * @param data
     * @return int
     */
    public int addMenuRight(Map<String, Object> data);
    
    /**
     * @Description:根据角色ID取得当前角色拥有的一级菜单
     * @param roleid 角色ID
     * @return List<ManSystemMenuVO>
     */
    public List<ManSystemMenuVO> getAllParentByRoleID(String roleid);

    /**
     * @Description:根据角色ID和上级ID取得当前角色拥有的子菜单
     * @param roleid 角色ID
     * @return List<ManSystemMenuVO>
     */
    public List<ManSystemMenuVO> getAllChildrenByRoleID(Map<String, Object> data);
    
}
