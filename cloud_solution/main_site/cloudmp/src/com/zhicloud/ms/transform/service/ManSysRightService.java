
package com.zhicloud.ms.transform.service;

import java.util.List;
import java.util.Map;

import com.zhicloud.ms.transform.util.TransFormLoginInfo;
import com.zhicloud.ms.transform.vo.ManSystemRightVO;

/**
 * @ClassName: ManSysRightService
 * @Description: 权限管理
 * @author 张本缘 于 2015年5月12日 下午4:03:58
 */
public interface ManSysRightService {

    /**
     * @Description:取得所有的功能权限信息
     * @return List<ManSystemRightVO>
     */
    public List<ManSystemRightVO> getAll();

    /**
     * @Description: 新增功能权限信息
     * @param parameter
     * @return String
     */
    public String addSysRight(Map<String, Object> parameter, TransFormLoginInfo login);

    /**
     * @Description:删除功能权限信息
     * @param billids ID数组
     * @return String
     */
    public String deleteSysRight(String billids, TransFormLoginInfo login);

    /**
     * @Description:修改功能权限信息
     * @param parameter
     * @return
     */
    public String modSysRight(Map<String, Object> parameter, TransFormLoginInfo login);

    /**
     * @Description:取得ManSystemRightVO对象
     * @param billid 主键ID
     * @return ManSystemRightVO
     */
    public ManSystemRightVO getRightByID(String billid);

    /**
     * @Description:取得所有的权限信息和关联的角色信息
     * @param menuid 角色ID
     * @return List<ManSystemRightVO>
     */
    public List<ManSystemRightVO> getRightAndRole(String roleid);
    
    /**
     * @Description:根据菜单ID查询该菜单已经勾选的功能权限和其他可用的功能权限
     * @param menuid 菜单ID
     * @return List<ManSystemRightVO>
     */
    public List<ManSystemRightVO> getRightOutMenu(String menuid);
    
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
}
