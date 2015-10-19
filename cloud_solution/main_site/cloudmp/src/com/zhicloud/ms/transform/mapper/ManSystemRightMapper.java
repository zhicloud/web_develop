
package com.zhicloud.ms.transform.mapper;

import java.util.List;
import java.util.Map;

import com.zhicloud.ms.transform.vo.ManSystemRightVO;

/**
 * @ClassName: ManSystemRightMapper
 * @Description: 权限映射
 * @author 张本缘 于 2015年5月12日 下午4:00:38
 */
public interface ManSystemRightMapper {
    /**
     * @Description:取得所有功能权限信息
     * @return
     */
    public List<ManSystemRightVO> getAll();

    /**
     * @Description:新增功能权限信息
     * @param data
     * @return int
     */
    public int addSystemRight(Map<String, Object> data);

    /**
     * @Description:修改功能权限信息
     * @param data
     * @return int
     */
    public int updateSystemRight(Map<String, Object> data);

    /**
     * @Description:批量删除功能权限信息
     * @param billids
     * @return int
     */
    public int deleteSystemRightByIds(String[] billids);

    /**
     * @Description:验证功能权限名称是否已经存在
     * @param condition 菜单信息
     */
    public ManSystemRightVO validateRightIsExists(Map<String, Object> data);

    /**
     * @Description:验证菜单名称是否已经存在
     * @param condition 菜单信息
     */
    public ManSystemRightVO validateRightIsExistsMod(Map<String, Object> data);

    /**
     * @Description:根据ID取得功能菜单信息
     * @param billid
     * @return ManSystemRightVO
     */
    public ManSystemRightVO getRightById(String billid);
    
    /**
     * @Description:取得所有的权限信息并关联上角色信息
     * @param roleid 角色ID
     * @return List<ManSystemRightVO>
     */
    public List<ManSystemRightVO> getRightAndRole(String roleid);
    
    /**
     * @Description:根据用户ID取得该用户的功能权限
     * @param userid 用户ID
     * @return List<ManSystemRightVO>
     */
    public List<ManSystemRightVO> getRightByUserID(String userid);
    
    /**
     * @Description:根据功能权限ID删除权限和角色关联关系
     * @param rightid 权限ID
     * @return int
     */
    public int deleteRoleRightByRightID(String rightid);
    
    /**
     * @Description:根据菜单ID查询该菜单已经勾选的功能权限和其他可用的功能权限
     * @param menuid 菜单ID
     * @return List<ManSystemRightVO>
     */
    public List<ManSystemRightVO> getRightOutMenu(String menuid);
    
    /**
     * @Description:根据菜单ID查询该菜单已经关联和选择的功能权限
     * @param menuid 菜单ID
     * @return List<ManSystemRightVO>
     */
    public List<ManSystemRightVO> getRightWithMenuID(Map<String, Object> data);
    
    /**
     * @Description:查询已关联该菜单的权限
     * @param menuid
     * @return
     */
    public List<ManSystemRightVO> queryRightInMenuID(String menuid);
    
    /**
     * @Description:查询未关联该菜单的权限
     * @param menuid
     * @return
     */
    public List<ManSystemRightVO> queryRightOutMenuID(String menuid);
    
    /**
     * @Description:根据权限ID取得该权限已经关联的菜单ID
     * @param rightid
     * @return ManSystemRightVO
     */
    public ManSystemRightVO getMenuByRightId(String rightid);
    
    /**
     * @Description:根据权限ID删除权限和菜单的关联关系
     * @param rightid 权限ID
     * @return int
     */
    public int deleteMenuRightByRightID(String rightid);
}
