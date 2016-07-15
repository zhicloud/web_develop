
package com.zhicloud.ms.transform.service;

import java.util.List;
import java.util.Map;

import com.zhicloud.ms.transform.util.TransFormLoginInfo;
import com.zhicloud.ms.transform.vo.ManSystemMenuVO;

/**
 * @ClassName: ManSysMenuService
 * @Description: 菜单管理
 * @author 张本缘 于 2015年5月12日 下午4:03:03
 */
public interface ManSysMenuService {

    /**
     * @Description:根据标示取得不同的菜单信息
     * @return List<ManSystemMenuVO>
     */
    public List<ManSystemMenuVO> getAll(String flag, String parentid);

    /**
     * @Description: 新增菜单信息
     * @param parameter
     * @return String
     */
    public String addSysMenu(Map<String, Object> parameter, TransFormLoginInfo login);

    /**
     * @Description:删除菜单信息
     * @param billids ID数组
     * @param flag 标示
     * @return String
     */
    public String deleteSysMenu(String billids, TransFormLoginInfo login);

    /**
     * @Description:修改菜单信息
     * @param parameter
     * @return
     */
    public String modSysMenu(Map<String, Object> parameter, TransFormLoginInfo login);

    /**
     * @Description:取得ManSystemMenuVO对象
     * @param billid 主键ID
     * @return ManSystemMenuVO
     */
    public ManSystemMenuVO getMenuByID(String billid);
    
    /**
     * @Description:根据菜单ID删除菜单和权限的关联关系
     * @param menuid 菜单ID
     * @return String
     */
    public String deleteMenuRightByID(String menuid);
    
    /**
     * @Description:保存菜单和权限的关联关系
     * @param rightids 权限ID数组
     * @param menuid 菜单ID
     * @return String
     */
    public String saveMenuRight(String[] rightids, String menuid);
    

}
