
package com.zhicloud.ms.transform.serviceimpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.ms.common.util.StringUtil;
import com.zhicloud.ms.exception.AppException;
import com.zhicloud.ms.transform.constant.TransformConstant;
import com.zhicloud.ms.transform.mapper.ManSystemLogMapper;
import com.zhicloud.ms.transform.mapper.ManSystemMenuMapper;
import com.zhicloud.ms.transform.mapper.ManSystemRightMapper;
import com.zhicloud.ms.transform.mapper.ManSystemRoleMapper;
import com.zhicloud.ms.transform.service.ManSysRoleService;
import com.zhicloud.ms.transform.util.TransFormLoginInfo;
import com.zhicloud.ms.transform.util.TransFormTree;
import com.zhicloud.ms.transform.vo.ManSystemMenuVO;
import com.zhicloud.ms.transform.vo.ManSystemRightVO;
import com.zhicloud.ms.transform.vo.ManSystemRoleVO;

/**
 * @ClassName: ManSysRoleServiceImpl
 * @Description: 角色管理实现
 * @author 张本缘 于 2015年5月7日 上午9:47:50
 */
@Transactional(readOnly = true)
public class ManSysRoleServiceImpl implements ManSysRoleService {
    /**
     * 日志
     */
    public static final Logger logger = Logger.getLogger(ManSysRoleServiceImpl.class);

    private SqlSession sqlSession;

    /**
     * @Description:sqlSession注入
     * @param sqlSession
     */
    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    /* ==================================角色======================================================== */
    /**
     * Description:取得所有角色信息
     * 
     * @return List<ManSystemRoleVO>
     */
    public List<ManSystemRoleVO> getAllRole() {
        ManSystemRoleMapper systemRoleMapper = this.sqlSession.getMapper(ManSystemRoleMapper.class);
        return systemRoleMapper.getAll();
    }

    /**
     * Description:取得所有角色组信息
     * 
     * @return List<ManSystemRoleVO>
     */
    public List<ManSystemRoleVO> getAllGroup() {
        ManSystemRoleMapper systemRoleMapper = this.sqlSession.getMapper(ManSystemRoleMapper.class);
        return systemRoleMapper.getAllGroup();
    }
    
    /**
     * @Description:根据ID取得角色的实体对象
     * @param billid
     * @return ManSystemRoleVO
     */
    public ManSystemRoleVO getRoleByID(String billid) {
        ManSystemRoleMapper systemRoleMapper = this.sqlSession.getMapper(ManSystemRoleMapper.class);
        return systemRoleMapper.getRoleById(billid);
    }

    /**
     * @Description:新增系统用户
     * @param parameter 参数信息
     * @return MethodResult
     */
    @Transactional(readOnly = false)
    public String addSysRole(Map<String, Object> parameter, TransFormLoginInfo login) {
        logger.debug("SysUserServiceImpl.addSysUser()");
        try {
            String name = StringUtil.trim(parameter.get("name"));
            String code = StringUtil.trim(parameter.get("code"));
            String insert_date = StringUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
            String billid = StringUtil.generateUUID();
            ManSystemRoleMapper systemRoleMapper = this.sqlSession.getMapper(ManSystemRoleMapper.class);
            // 判断账户是否已经存在
            LinkedHashMap<String, Object> condition = new LinkedHashMap<String, Object>();
            condition.put("billid", billid);
            condition.put("name", name);
            condition.put("code", code);
            condition.put("insert_date", insert_date);
            condition.put("insert_user", login.getBillid());
            // 验证该名称或编码是否已经存在
            ManSystemRoleVO systemRoleVO = systemRoleMapper.validateRoleIsExists(condition);
            if (systemRoleVO != null) {
                return "名称或编码已经存在";
            }
            // 添加角色数据
            int n = systemRoleMapper.addSystemRole(condition);

            // 添加日志信息
            Map<String, Object> operatorData = new LinkedHashMap<String, Object>();
            operatorData.put("billid", StringUtil.generateUUID());
            operatorData.put("operateid", login.getBillid());
            operatorData.put("content", "新增了角色：" + name);
            operatorData.put("operate_date", insert_date);
            operatorData.put("type", TransformConstant.transform_log_role);

            int m = this.addSystemLogInfo(operatorData);
            if (n > 0 && m > 0) {
                return TransformConstant.success;
            } else {
                return "添加失败";
            }

        } catch (Exception e) {
            logger.error(e);
            throw new AppException("添加失败");
        }
    }

    /**
     * @Description:添加日志信息
     * @param condition
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int addSystemLogInfo(Map<String, Object> condition) {
        try {
            ManSystemLogMapper systemLogMapper = this.sqlSession.getMapper(ManSystemLogMapper.class);
            systemLogMapper.deleteLogsWithoutDays(TransformConstant.logkeep);
            return systemLogMapper.addSystemLog(condition);
        } catch (Exception e) {
            logger.error(e);
            throw new AppException("日志添加失败");
        }

    }

    /**
     * @Description:删除角色信息
     * @param parameter
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public String deleteSysRole(String billids, TransFormLoginInfo login) {
        logger.debug("SysUserServiceImpl.deleteSysRole()");
        try {
            ManSystemRoleMapper systemRoleMapper = this.sqlSession.getMapper(ManSystemRoleMapper.class);
            String[] array = billids.split(",");
            // 添加日志信息
            for (String billid : array) {
                Map<String, Object> operatorData = new LinkedHashMap<String, Object>();
                operatorData.put("billid", StringUtil.generateUUID());
                operatorData.put("operateid", login.getBillid());
                operatorData.put("content", "删除了角色：" + systemRoleMapper.getRoleById(billid).getName());
                operatorData.put("operate_date", StringUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
                operatorData.put("type", TransformConstant.transform_log_role);
                this.addSystemLogInfo(operatorData);
            }
            // 删除与角色的关联关系
            for (String billid : array) {
                systemRoleMapper.deleteRoleGroupByRoleID(billid);
                systemRoleMapper.deleteRoleMenuByRole(billid);
                systemRoleMapper.deleteUserRoleByRoleID(billid);
            }
            systemRoleMapper.deleteSystemRoleByIds(array);

            return TransformConstant.success;

        } catch (Exception e) {
            logger.error(e);
            throw new AppException("删除失败");
        }
    }

    /**
     * @Description:修改角色信息
     * @param parameter
     * @return
     * @throws
     */
    @Transactional(readOnly = false)
    public String modSysRole(Map<String, Object> parameter, TransFormLoginInfo login) {
        logger.debug("SysUserServiceImpl.modSysRole()");
        try {
            String name = StringUtil.trim(parameter.get("name"));
            String code = StringUtil.trim(parameter.get("code"));
            String billid = StringUtil.trim(parameter.get("billid"));
            ManSystemRoleMapper systemRoleMapper = this.sqlSession.getMapper(ManSystemRoleMapper.class);
            // 判断账户是否已经存在
            LinkedHashMap<String, Object> condition = new LinkedHashMap<String, Object>();
            condition.put("name", name);
            condition.put("code", code);
            condition.put("billid", billid);
            // 验证该账名称和编码是否已经存在
            ManSystemRoleVO systemRoleVO = systemRoleMapper.validateRoleIsExistsMod(condition);
            if (systemRoleVO != null) {
                return "名称或编码已经存在";
            }
            // 更新角色数据
            int n = systemRoleMapper.updateSystemRole(condition);

            // 添加日志信息
            Map<String, Object> operatorData = new LinkedHashMap<String, Object>();
            operatorData.put("billid", StringUtil.generateUUID());
            operatorData.put("operateid", login.getBillid());
            operatorData.put("content", "修改了角色：" + name);
            operatorData.put("operate_date", StringUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
            operatorData.put("type", TransformConstant.transform_log_role);

            int m = this.addSystemLogInfo(operatorData);
            if (n > 0 && m > 0) {
                return TransformConstant.success;
            } else {
                return "修改失败";
            }

        } catch (Exception e) {
            logger.error(e);
            throw new AppException("修改失败");
        }
    }

    /**
     * @Description:新增系统菜单和角色关联关系
     * @param parameter 参数信息
     * @return String
     */
    @Transactional(readOnly = false)
    public String saveRoleRight(String[] rightids, String roleid) {
        if (rightids == null || rightids.length == 0) {
            return TransformConstant.fail;
        }
        try {
            ManSystemRoleMapper systemRoleMapper = this.sqlSession.getMapper(ManSystemRoleMapper.class);
            for (String rightid : rightids) {
                if ("".equals(rightid)) {
                    continue;
                }
                Map<String, Object> relateData = new LinkedHashMap<String, Object>();
                relateData.put("billid", StringUtil.generateUUID());
                relateData.put("roleid", roleid);
                relateData.put("rightid", rightid);
                systemRoleMapper.saveRoleRight(relateData);
            }
        } catch (Exception e) {
            throw new AppException(e);
        }
        return TransformConstant.success;
    }

    /**
     * @Description:根据角色ID删除菜单和权限关联关系
     * @param roleid 菜单ID
     * @return
     */
    @Transactional(readOnly = false)
    public String deleteRoleRight(String roleid) {
        if (roleid == null || "".equals(roleid)) {
            return TransformConstant.fail;
        }
        try {
            ManSystemRoleMapper systemRoleMapper = this.sqlSession.getMapper(ManSystemRoleMapper.class);
            systemRoleMapper.deleteRoleRightByRole(roleid);
        } catch (Exception e) {
            throw new AppException(e);
        }
        return TransformConstant.success;
    }

    /**
     * @Description:根据菜单ID递归构造树
     * @param menuid 菜单ID
     * @return List<TransFormTree>
     */
    public List<TransFormTree> buildTree(String menuid, TransFormTree tree, String roleid) {
        ManSystemMenuMapper manSystemMenuMapper = this.sqlSession.getMapper(ManSystemMenuMapper.class);
        List<TransFormTree> returnList = new ArrayList<TransFormTree>();
        if (tree == null)
            return null;
        // 拼装根节点
        if ("all".equals(menuid)) {
            tree.setId("all");
            tree.setText("全部");
            tree.setChildren(buildTree(null, tree, ""));
            returnList.add(tree);
        }
        List<ManSystemMenuVO> menuList = null;
        // 所有一级菜单
        if (menuid == null || "".equals(menuid)) {
            menuList = manSystemMenuMapper.getAllParent();
        } else {
            menuList = manSystemMenuMapper.getChildren(menuid);
        }
        if (menuList != null & menuList.size() > 0) {
            ManSystemRoleMapper manSystemRoleMapper = this.sqlSession.getMapper(ManSystemRoleMapper.class);
            for (ManSystemMenuVO manSystemMenuVO : menuList) {
                TransFormTree child = new TransFormTree();
                child.setId(manSystemMenuVO.getBillid());
                child.setText(manSystemMenuVO.getMenuname());
                Map<String, Object> data = new LinkedHashMap<String, Object>();
                data.put("roleid", roleid);
                data.put("menuid", manSystemMenuVO.getBillid());
                if (manSystemRoleMapper.queryRoleByRoleAndMenu(data) != null) {
                    data.put("ischecked", "true");
                }
                List<TransFormTree> children = buildTree(manSystemMenuVO.getBillid(), child, roleid);
                if (children != null && children.size() > 0) {
                    child.setChildren(children);
                }
                returnList.add(child);
            }
        }
        return returnList;

    }

    /**
     * @Description:根据菜单ID递归构造树
     * @param menuid 菜单ID
     * @return List<TransFormTree>
     */
    public JSONArray buildTreeJSON(String menuid, JSONArray array, String roleid) {
        ManSystemMenuMapper manSystemMenuMapper = this.sqlSession.getMapper(ManSystemMenuMapper.class);
        JSONArray returnArray = new JSONArray();
        if (array == null)
            return null;
        // 拼装根节点
        if ("all".equals(menuid)) {
            JSONObject json = new JSONObject();
            json.put("id", "all");
            json.put("text", "全部");
            JSONArray obj = buildTreeJSON(null, array, roleid);
            if (obj.size() > 0) {
                json.put("children", obj);
            }
            returnArray.add(json);
        }
        List<ManSystemMenuVO> menuList = null;
        // 所有一级菜单
        if (menuid == null || "".equals(menuid)) {
            menuList = manSystemMenuMapper.getAllParent();
        } else {
            menuList = manSystemMenuMapper.getChildren(menuid);
        }
        if (menuList != null & menuList.size() > 0) {
            ManSystemRoleMapper manSystemRoleMapper = this.sqlSession.getMapper(ManSystemRoleMapper.class);
            for (ManSystemMenuVO manSystemMenuVO : menuList) {
                JSONObject obj = new JSONObject();
                obj.put("id", manSystemMenuVO.getBillid());
                obj.put("text", manSystemMenuVO.getMenuname());
                Map<String, Object> data = new LinkedHashMap<String, Object>();
                data.put("roleid", roleid);
                data.put("menuid", manSystemMenuVO.getBillid());
                if (manSystemRoleMapper.queryRoleByRoleAndMenu(data) != null) {
                    obj.put("ischecked", "true");
                }
                JSONArray children = buildTreeJSON(manSystemMenuVO.getBillid(), array, roleid);
                if (children != null && children.size() > 0) {
                    obj.put("children", children);
                }
                returnArray.add(obj);
            }
        }
        return returnArray;

    }

    /**
     * @Description:新增系统菜单和角色关联关系
     * @param parameter 参数信息
     * @return String
     */
    @Transactional(readOnly = false)
    public String saveRoleMenu(String[] menuids, String roleid) {
        if (menuids == null || menuids.length == 0) {
            return TransformConstant.fail;
        }
        try {
            ManSystemRoleMapper systemRoleMapper = this.sqlSession.getMapper(ManSystemRoleMapper.class);
            for (String menuid : menuids) {
                if ("".equals(menuid)) {
                    continue;
                }
                Map<String, Object> relateData = new LinkedHashMap<String, Object>();
                relateData.put("billid", StringUtil.generateUUID());
                relateData.put("roleid", roleid);
                relateData.put("menuid", menuid);
                systemRoleMapper.saveRoleMenu(relateData);
            }
        } catch (Exception e) {
            throw new AppException(e);
        }
        return TransformConstant.success;
    }

    /**
     * @Description:根据角色ID删除菜单和菜单关联关系
     * @param roleid 角色ID
     * @return
     */
    @Transactional(readOnly = false)
    public String deleteRoleMenu(String roleid) {
        if (roleid == null || "".equals(roleid)) {
            return TransformConstant.fail;
        }
        try {
            ManSystemRoleMapper systemRoleMapper = this.sqlSession.getMapper(ManSystemRoleMapper.class);
            systemRoleMapper.deleteRoleMenuByRole(roleid);
        } catch (Exception e) {
            throw new AppException(e);
        }
        return TransformConstant.success;
    }

    /* =====================================角色组================================================== */
    /**
     * @Description:新增系统角色组
     * @param parameter 参数信息
     * @return MethodResult
     */
    @Transactional(readOnly = false)
    public String addSysGroup(Map<String, Object> parameter, TransFormLoginInfo login) {
        logger.debug("SysUserServiceImpl.addSysGroup()");
        try {
            String name = StringUtil.trim(parameter.get("name"));
            String insert_date = StringUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
            String billid = StringUtil.generateUUID();
            ManSystemRoleMapper systemRoleMapper = this.sqlSession.getMapper(ManSystemRoleMapper.class);
            // 判断账户是否已经存在
            LinkedHashMap<String, Object> condition = new LinkedHashMap<String, Object>();
            condition.put("billid", billid);
            condition.put("name", name);
            condition.put("insert_date", insert_date);
            condition.put("insert_user", login.getBillid());
            // 验证该名称或编码是否已经存在
            ManSystemRoleVO systemRoleVO = systemRoleMapper.validateGroupIsExists(condition);
            if (systemRoleVO != null) {
                return "名称已经存在";
            }
            // 添加角色组数据
            int n = systemRoleMapper.addSystemGroup(condition);

            // 添加日志信息
            Map<String, Object> operatorData = new LinkedHashMap<String, Object>();
            operatorData.put("billid", StringUtil.generateUUID());
            operatorData.put("operateid", login.getBillid());
            operatorData.put("content", "新增了角色组：" + name);
            operatorData.put("operate_date", insert_date);
            operatorData.put("type", TransformConstant.transform_log_group);

            int m = this.addSystemLogInfo(operatorData);
            if (n > 0 && m > 0) {
                return TransformConstant.success;
            } else {
                return "添加失败";
            }

        } catch (Exception e) {
            logger.error(e);
            throw new AppException("添加失败");
        }
    }

    /**
     * @Description:删除角色组信息
     * @param parameter
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public String deleteSysGroup(String billids, TransFormLoginInfo login) {
        logger.debug("SysUserServiceImpl.deleteSysGroup()");
        try {
            ManSystemRoleMapper systemRoleMapper = this.sqlSession.getMapper(ManSystemRoleMapper.class);
            String[] array = billids.split(",");
            // 添加日志信息
            for (String billid : array) {
                Map<String, Object> operatorData = new LinkedHashMap<String, Object>();
                operatorData.put("billid", StringUtil.generateUUID());
                operatorData.put("operateid", login.getBillid());
                operatorData.put("content", "删除了角色组：" + systemRoleMapper.getGroupById(billid).getName());
                operatorData.put("operate_date", StringUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
                operatorData.put("type", TransformConstant.transform_log_group);
                this.addSystemLogInfo(operatorData);
            }
            // 删除角色组的关联关系
            for (String billid : array) {
                systemRoleMapper.deleteGroupRole(billid);
                systemRoleMapper.deleteUserGroup(billid);
            }
            systemRoleMapper.deleteSystemGroupByIds(array);

            return TransformConstant.success;

        } catch (Exception e) {
            logger.error(e);
            throw new AppException("删除失败");
        }
    }

    /**
     * @Description:修改角色组信息
     * @param parameter
     * @return
     * @throws
     */
    @Transactional(readOnly = false)
    public String modSysGroup(Map<String, Object> parameter, TransFormLoginInfo login) {
        logger.debug("SysUserServiceImpl.modSysGroup()");
        try {
            String name = StringUtil.trim(parameter.get("name"));
            String billid = StringUtil.trim(parameter.get("billid"));
            ManSystemRoleMapper systemRoleMapper = this.sqlSession.getMapper(ManSystemRoleMapper.class);
            // 判断账户是否已经存在
            LinkedHashMap<String, Object> condition = new LinkedHashMap<String, Object>();
            condition.put("name", name);
            condition.put("billid", billid);
            // 验证该角色组名称是否已经存在
            ManSystemRoleVO systemRoleVO = systemRoleMapper.validateGroupIsExistsMod(condition);
            if (systemRoleVO != null) {
                return "名称已经存在";
            }
            // 更新角色组数据
            int n = systemRoleMapper.updateSystemGroup(condition);

            // 添加日志信息
            Map<String, Object> operatorData = new LinkedHashMap<String, Object>();
            operatorData.put("billid", StringUtil.generateUUID());
            operatorData.put("operateid", login.getBillid());
            operatorData.put("content", "修改了角色组：" + name);
            operatorData.put("operate_date", StringUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
            operatorData.put("type", TransformConstant.transform_log_group);

            int m = this.addSystemLogInfo(operatorData);
            if (n > 0 && m > 0) {
                return TransformConstant.success;
            } else {
                return "修改失败";
            }

        } catch (Exception e) {
            logger.error(e);
            throw new AppException("修改失败");
        }
    }

    /**
     * @Description:根据角色ID删除该角色和用户的所有关联信息
     * @param roleid 角色ID
     * @return
     */
    @Transactional(readOnly = false)
    public String deleteUserRole(String roleid) {
        if (roleid == null || "".equals(roleid)) {
            return TransformConstant.fail;
        }
        try {
            ManSystemRoleMapper systemRoleMapper = this.sqlSession.getMapper(ManSystemRoleMapper.class);
            systemRoleMapper.deleteUserRole(roleid);
        } catch (Exception e) {
            throw new AppException(e);
        }
        return TransformConstant.success;
    }

    /**
     * @Description:保存用户和角色关联信息
     * @param billids 用户ID
     * @param roleid 角色ID
     * @return String
     */
    @Transactional(readOnly = false)
    public String saveUserRole(String[] userids, String roleid) {
        if (userids == null || userids.length == 0) {
            return TransformConstant.fail;
        }
        try {
            ManSystemRoleMapper systemRoleMapper = this.sqlSession.getMapper(ManSystemRoleMapper.class);
            for (String userid : userids) {
                if ("".equals(userid)) {
                    continue;
                }
                Map<String, Object> relateData = new LinkedHashMap<String, Object>();
                relateData.put("billid", StringUtil.generateUUID());
                relateData.put("userid", userid);
                relateData.put("roleid", roleid);
                systemRoleMapper.saveUserRole(relateData);
            }
        } catch (Exception e) {
            throw new AppException(e);
        }
        return TransformConstant.success;
    }

    /**
     * Description:查询该角色组合角色的关联信息
     * 
     * @param billid 角色ID
     * @return
     */
    public List<ManSystemRoleVO> queryGroupInRoleID(String billid) {
        if (billid == null || "".equals(billid)) {
            return null;
        }
        ManSystemRoleMapper manSystemRoleMapper = this.sqlSession.getMapper(ManSystemRoleMapper.class);

        return manSystemRoleMapper.queryGroupInRoleID(billid);
    }

    /**
     * @Description:根据角色ID删除该角色和角色组的所有关联信息
     * @param roleid 角色ID
     * @return
     */
    @Transactional(readOnly = false)
    public String deleteRoleGroup(String roleid) {
        if (roleid == null || "".equals(roleid)) {
            return TransformConstant.fail;
        }
        try {
            ManSystemRoleMapper systemRoleMapper = this.sqlSession.getMapper(ManSystemRoleMapper.class);
            systemRoleMapper.deleteRoleGroup(roleid);
        } catch (Exception e) {
            throw new AppException(e);
        }
        return TransformConstant.success;
    }

    /**
     * @Description:保存角色组和角色关联信息
     * @param billids 用户ID
     * @param roleid 角色ID
     * @return String
     */
    @Transactional(readOnly = false)
    public String saveRoleGroup(String[] groups, String roleid) {
        if (groups == null || groups.length == 0) {
            return TransformConstant.fail;
        }
        try {
            ManSystemRoleMapper systemRoleMapper = this.sqlSession.getMapper(ManSystemRoleMapper.class);
            for (String group : groups) {
                if ("".equals(group)) {
                    continue;
                }
                Map<String, Object> relateData = new LinkedHashMap<String, Object>();
                relateData.put("billid", StringUtil.generateUUID());
                relateData.put("groupid", group);
                relateData.put("roleid", roleid);
                systemRoleMapper.saveRoleGroup(relateData);
            }
        } catch (Exception e) {
            throw new AppException(e);
        }
        return TransformConstant.success;
    }

    /**
     * @Description:根据角色组ID删除该角色组和用户的所有关联信息
     * @param roleid 角色ID
     * @return
     */
    @Transactional(readOnly = false)
    public String deleteUserGroup(String groupid) {
        if (groupid == null || "".equals(groupid)) {
            return TransformConstant.fail;
        }
        try {
            ManSystemRoleMapper systemRoleMapper = this.sqlSession.getMapper(ManSystemRoleMapper.class);
            systemRoleMapper.deleteUserGroup(groupid);
        } catch (Exception e) {
            throw new AppException(e);
        }
        return TransformConstant.success;
    }

    /**
     * @Description:保存用户和角色组关联信息
     * @param billids 用户ID
     * @param groupid 角色组ID
     * @return String
     */
    @Transactional(readOnly = false)
    public String saveUserGroup(String[] userids, String groupid) {
        if (userids == null || userids.length == 0) {
            return TransformConstant.fail;
        }
        try {
            ManSystemRoleMapper systemRoleMapper = this.sqlSession.getMapper(ManSystemRoleMapper.class);
            for (String userid : userids) {
                if ("".equals(userid)) {
                    continue;
                }
                Map<String, Object> relateData = new LinkedHashMap<String, Object>();
                relateData.put("billid", StringUtil.generateUUID());
                relateData.put("userid", userid);
                relateData.put("groupid", groupid);
                systemRoleMapper.saveUserGroup(relateData);
            }
        } catch (Exception e) {
            throw new AppException(e);
        }
        return TransformConstant.success;
    }

    /**
     * Description:查询该角色组和角色的关联信息
     * 
     * @param billid 角色ID
     * @return
     */
    public List<ManSystemRoleVO> queryRoleInGroupID(String billid) {
        if (billid == null || "".equals(billid)) {
            return null;
        }
        ManSystemRoleMapper manSystemRoleMapper = this.sqlSession.getMapper(ManSystemRoleMapper.class);

        return manSystemRoleMapper.queryRoleInGroupID(billid);
    }

    /**
     * @Description:根据角色组ID删除该角色和角色组的所有关联信息
     * @param groupid 角色组ID
     * @return
     */
    @Transactional(readOnly = false)
    public String deleteGroupRole(String groupid) {
        if (groupid == null || "".equals(groupid)) {
            return TransformConstant.fail;
        }
        try {
            ManSystemRoleMapper systemRoleMapper = this.sqlSession.getMapper(ManSystemRoleMapper.class);
            systemRoleMapper.deleteGroupRole(groupid);
        } catch (Exception e) {
            throw new AppException(e);
        }
        return TransformConstant.success;
    }

    /**
     * @Description:保存角色组和角色关联信息
     * @param billids 用户ID
     * @param roleid 角色ID
     * @return String
     */
    @Transactional(readOnly = false)
    public String saveGroupRole(String[] roleids, String groupid) {
        if (roleids == null || roleids.length == 0) {
            return TransformConstant.fail;
        }
        try {
            ManSystemRoleMapper systemRoleMapper = this.sqlSession.getMapper(ManSystemRoleMapper.class);
            for (String roleid : roleids) {
                if ("".equals(roleid)) {
                    continue;
                }
                Map<String, Object> relateData = new LinkedHashMap<String, Object>();
                relateData.put("billid", StringUtil.generateUUID());
                relateData.put("groupid", groupid);
                relateData.put("roleid", roleid);
                systemRoleMapper.saveRoleGroup(relateData);
            }
        } catch (Exception e) {
            throw new AppException(e);
        }
        return TransformConstant.success;
    }
    
    /**
     * @Description:根据ID取得角色组实体
     * @param billid
     * @return ManSystemRoleVO
     */
    public ManSystemRoleVO getGroupByID(String billid) {
        ManSystemRoleMapper systemRoleMapper = this.sqlSession.getMapper(ManSystemRoleMapper.class);
        return systemRoleMapper.getGroupById(billid);
    }
    
    /**
     * @Description:根据菜单ID和角色ID递归构造树
     * @param menuid 菜单ID
     * @return List<TransFormTree>
     */
    public JSONArray buildTreeJSONWithRight(String menuid, JSONArray array, String roleid) {
        ManSystemMenuMapper manSystemMenuMapper = this.sqlSession.getMapper(ManSystemMenuMapper.class);
        ManSystemRightMapper manSystemRightMapper = this.sqlSession.getMapper(ManSystemRightMapper.class);
        JSONArray returnArray = new JSONArray();
        if (array == null)
            return null;
        // 拼装根节点
        if ("all".equals(menuid)) {
            JSONObject json = new JSONObject();
            json.put("id", "all");
            json.put("text", "全部");
            json.put("isright", "false");
            JSONArray obj = buildTreeJSONWithRight(null, array, roleid);
            if (obj.size() > 0) {
                json.put("children", obj);
            }
            returnArray.add(json);
        }
        List<ManSystemMenuVO> menuList = null;
        // 所有一级菜单
        if (menuid == null || "".equals(menuid)) {
            menuList = manSystemMenuMapper.getAllParentByRoleID(roleid);
        } else {
            Map<String, Object> data = new LinkedHashMap<String, Object>();
            data.put("roleid", roleid);
            data.put("parentid", menuid);
            menuList = manSystemMenuMapper.getAllChildrenByRoleID(data);
        }
        Map<String,Object> condition = new LinkedHashMap<String,Object>();
        condition.put("roleid", roleid);
        if (menuList != null & menuList.size() > 0) {
            for (ManSystemMenuVO manSystemMenuVO : menuList) {
                JSONObject obj = new JSONObject();
                obj.put("id", manSystemMenuVO.getBillid());
                obj.put("text", manSystemMenuVO.getMenuname());
                obj.put("isright", "false");
                condition.put("menuid", manSystemMenuVO.getBillid());
                // 菜单树加入权限子节点
                List<ManSystemRightVO> rights = manSystemRightMapper.getRightWithMenuID(condition);
                if (rights.size() > 0) {
                    JSONArray rightarray = new JSONArray();
                    for (ManSystemRightVO rightVO : rights) {
                        JSONObject vo = new JSONObject();
                        vo.put("id", rightVO.getBillid());
                        vo.put("text", rightVO.getName());
                        vo.put("isright", "true");
                        if (rightVO.getRoleid() != null && !"".equals(rightVO.getRoleid())) {
                            vo.put("ischecked", "true");
                        }
                        rightarray.add(vo);
                    }
                    if (!rightarray.isEmpty()) {
                        obj.put("children", rightarray);
                    }
                } else {
                    JSONArray children = buildTreeJSONWithRight(manSystemMenuVO.getBillid(), array, roleid);
                    if (children != null && children.size() > 0) {
                        obj.put("children", children);
                    }
                }
                returnArray.add(obj);
            }
        }
        return returnArray;
    }
}
