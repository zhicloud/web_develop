package com.zhicloud.ms.transform.service;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import com.zhicloud.ms.transform.util.TransFormLoginInfo;
import com.zhicloud.ms.transform.util.TransFormTree;
import com.zhicloud.ms.transform.vo.ManSystemRoleVO;

/**
 * @ClassName: ManSysRoleService
 * @Description: 角色管理
 * @author 张本缘 于 2015年5月7日 上午9:35:27
 */
public interface ManSysRoleService {

    /*======================角色==================================*/
    public List<ManSystemRoleVO> getAllRole();
    
    public String addSysRole(Map<String, Object> parameter, TransFormLoginInfo login);

    public String deleteSysRole(String billids, TransFormLoginInfo login);

    public String modSysRole(Map<String, Object> parameter, TransFormLoginInfo login);

    public String saveRoleRight(String[] rightids, String roleid);

    public String deleteRoleRight(String roleid);
    
    public List<TransFormTree> buildTree(String menuid, TransFormTree tree, String roleid);    
    
    public JSONArray buildTreeJSON(String menuid, JSONArray array, String roleid);

    public String saveRoleMenu(String[] rightids, String roleid);

    public String deleteRoleMenu(String roleid);
    
    public ManSystemRoleVO getRoleByID(String billid);
    
    public JSONArray buildTreeJSONWithRight(String menuid, JSONArray array, String roleid);
    
    /*=======================角色组===============================*/
    public List<ManSystemRoleVO> getAllGroup();
    
    public String addSysGroup(Map<String, Object> parameter, TransFormLoginInfo login);

    public String deleteSysGroup(String billids, TransFormLoginInfo login);

    public String modSysGroup(Map<String, Object> parameter, TransFormLoginInfo login);
    
    public String deleteUserRole(String roleid);
    
    public String saveUserRole(String[] userids, String roleid);
    
    public List<ManSystemRoleVO> queryGroupInRoleID(String billid);
    
    public String deleteRoleGroup(String roleid);
    
    public String deleteGroupRole(String groupid);
    
    public String saveRoleGroup(String[] groups, String roleid);
    
    public String saveGroupRole(String[] roleids, String groupid);
    
    public String deleteUserGroup(String groupid);
    
    public String saveUserGroup(String[] userids, String groupid);    
    
    public List<ManSystemRoleVO> queryRoleInGroupID(String billid);
    
    public ManSystemRoleVO getGroupByID(String billid);
}
