
package com.zhicloud.ms.transform.serviceimpl;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.ms.common.util.StringUtil;
import com.zhicloud.ms.exception.AppException;
import com.zhicloud.ms.transform.constant.TransformConstant;
import com.zhicloud.ms.transform.mapper.ManSystemLogMapper;
import com.zhicloud.ms.transform.mapper.ManSystemMenuMapper;
import com.zhicloud.ms.transform.service.ManSysMenuService;
import com.zhicloud.ms.transform.util.TransFormLoginInfo;
import com.zhicloud.ms.transform.vo.ManSystemMenuVO;

/**
 * @ClassName: ManSysMenuServiceImpl
 * @Description: 菜单管理实现
 * @author 张本缘 于 2015年5月12日 下午4:08:55
 */
@Transactional(readOnly = true)
public class ManSysMenuServiceImpl  implements ManSysMenuService {
    /**
     * 日志
     */
    public static final Logger logger = Logger.getLogger(ManSysMenuServiceImpl.class);

    private SqlSession sqlSession;

    /**
     * @Description:sqlSession注入
     * @param sqlSession
     */
    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    /**
     * Description:取得所有菜单信息
     * @param flag 标示
     * @param parentid 父节点ID
     * @return
     */
    @Transactional(readOnly = false)
    public List<ManSystemMenuVO> getAll(String flag, String parentid) {
        ManSystemMenuMapper systemMenuMapper = this.sqlSession.getMapper(ManSystemMenuMapper.class);
        // 查询所有菜单信息
        if ("1".equals(flag)) {
            return systemMenuMapper.getAll();
        }
        // 查询所有一级菜单信息
        if ("2".equals(flag)) {
            return systemMenuMapper.getAllParent();
        }
        // 根据父节点查询该节点的子菜单信息
        if ("3".equals(flag)) {
            return systemMenuMapper.getChildren(parentid);
        }
        return null;
    }

    /**
     * @Description:新增系统菜单
     * @param parameter 参数信息
     * @return MethodResult
     */
    @Transactional(readOnly = false)
    public String addSysMenu(Map<String, Object> parameter, TransFormLoginInfo login) {
        logger.debug("ManSysMenuServiceImpl.addSysMenu()");
        try {
            String menuname = StringUtil.trim(parameter.get("menuname"));
            String linkname = StringUtil.trim(parameter.get("linkname"));
            String status = StringUtil.trim(parameter.get("status"));
            String remark = StringUtil.trim(parameter.get("remark"));
            String parentid = StringUtil.trim(parameter.get("parentid"));
            String cssname = StringUtil.trim(parameter.get("cssname"));
            Integer sort = Integer.parseInt(StringUtil.trim(parameter.get("sort")));
            String insert_date = StringUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
            String billid = StringUtil.generateUUID();
            ManSystemMenuMapper systemMenuMapper = this.sqlSession.getMapper(ManSystemMenuMapper.class);
            // 判断账户是否已经存在
            LinkedHashMap<String, Object> condition = new LinkedHashMap<String, Object>();
            condition.put("billid", billid);
            condition.put("menuname", menuname);
            condition.put("linkname", linkname);
            condition.put("status", status);
            condition.put("remark", remark);
            condition.put("parentid", parentid);
            condition.put("sort", sort);
            condition.put("insert_date", insert_date);
            condition.put("insert_user", login.getBillid());
            condition.put("cssname", cssname);
            // 子菜单不验证菜单名称,只有一级菜单才验证
            if (parentid == null || "".equals(parentid)) {
                ManSystemMenuVO systemMenuVO = systemMenuMapper.validateMenuIsExists(condition);
                if (systemMenuVO != null) {
                    return "菜单名称已经存在";
                }
            }
            // 添加角色数据
            int n = systemMenuMapper.addSystemMenu(condition);

            // 添加日志信息
            Map<String, Object> operatorData = new LinkedHashMap<String, Object>();
            operatorData.put("billid", StringUtil.generateUUID());
            operatorData.put("operateid", login.getBillid());
            operatorData.put("content", "新增菜单：" + menuname);
            operatorData.put("operate_date", insert_date);
            operatorData.put("type", TransformConstant.transform_log_menu);

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
     * @Description:根据ID取得ManSystemMenuVO对象
     * @param billid 主键ID
     * @return ManSystemMenuVO
     */
    @Transactional(readOnly = false)
    public ManSystemMenuVO getMenuByID(String billid) {
        if (billid == null)
            return null;
        ManSystemMenuMapper systemMenuMapper = this.sqlSession.getMapper(ManSystemMenuMapper.class);
        return systemMenuMapper.getMenuById(billid);
    }

    /**
     * @Description:删除菜单信息
     * @param parameter
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public String deleteSysMenu(String billids, TransFormLoginInfo login) {
        logger.debug("ManSysMenuServiceImpl.deleteSysMenu()");
        try {
            ManSystemMenuMapper systemMenuMapper = this.sqlSession.getMapper(ManSystemMenuMapper.class);
            String[] array = billids.split(",");
            boolean childrenflag = false;
            for (String billid : array) {
                // 如果是删除父节点，先判断父节点是否存在子菜单
                List<ManSystemMenuVO> manSystemMenuVO = systemMenuMapper.getChildren(billid);
                if (manSystemMenuVO.size() > 0) {
                    childrenflag = true;
                    break;
                }
            }
            if (childrenflag) {
                return "删除的菜单中存在子菜单信息，请先删除子菜单";
            }
            // 添加日志信息
            for (String billid : array) {
                Map<String, Object> operatorData = new LinkedHashMap<String, Object>();
                operatorData.put("billid", StringUtil.generateUUID());
                operatorData.put("operateid", login.getBillid());
                operatorData.put("content", "删除了菜单：" + systemMenuMapper.getMenuById(billid).getMenuname());
                operatorData.put("operate_date", StringUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
                operatorData.put("type", TransformConstant.transform_log_menu);
                this.addSystemLogInfo(operatorData);
            }
            // 删除关联关系
            for (String billid : array) {
                systemMenuMapper.deleteRoleMenuByMenuID(billid);
                systemMenuMapper.deleteRightMenuByMenuID(billid);
            }
            systemMenuMapper.deleteSystemMenuByIds(array);

            return TransformConstant.success;

        } catch (Exception e) {
            logger.error(e);
            throw new AppException("删除失败");
        }
    }

    /**
     * @Description:修改菜单信息
     * @param parameter
     * @return
     * @throws
     */
    @Transactional(readOnly = false)
    public String modSysMenu(Map<String, Object> parameter, TransFormLoginInfo login) {
        logger.debug("SysMenuServiceImpl.modSysMenu()");
        try {
            String menuname = StringUtil.trim(parameter.get("menuname"));
            String linkname = StringUtil.trim(parameter.get("linkname"));
            String status = StringUtil.trim(parameter.get("status"));
            String remark = StringUtil.trim(parameter.get("remark"));
            String cssname = StringUtil.trim(parameter.get("cssname"));
            Integer sort = Integer.parseInt(StringUtil.trim(parameter.get("sort")));
            String billid = StringUtil.trim(parameter.get("billid"));
            ManSystemMenuMapper systemMenuMapper = this.sqlSession.getMapper(ManSystemMenuMapper.class);
            LinkedHashMap<String, Object> condition = new LinkedHashMap<String, Object>();
            condition.put("menuname", menuname);
            condition.put("linkname", linkname);
            condition.put("status", status);
            condition.put("remark", remark);
            condition.put("sort", sort);
            condition.put("billid", billid);
            condition.put("cssname", cssname);
            
            ManSystemMenuVO menu = systemMenuMapper.getMenuById(billid);
            // 子菜单不验证菜单名称,只有一级菜单才验证
            if (menu.getParentid() == null || "".equals(menu.getParentid())) {
                ManSystemMenuVO systemMenuVO = systemMenuMapper.validateMenuIsExistsMod(condition);
                if (systemMenuVO != null) {
                    return "该菜单名称已经存在";
                }
            }
            // 更新菜单数据
            int n = systemMenuMapper.updateSystemMenu(condition);

            // 添加日志信息
            Map<String, Object> operatorData = new LinkedHashMap<String, Object>();
            operatorData.put("billid", StringUtil.generateUUID());
            operatorData.put("operateid", login.getBillid());
            operatorData.put("content", "修改了菜单：" + menuname);
            operatorData.put("operate_date", StringUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
            operatorData.put("type", TransformConstant.transform_log_menu);

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
     * @Description:根据菜单ID删除菜单和权限关联关系
     * @param roleid 菜单ID
     * @return
     */
    @Transactional(readOnly = false)
    public String deleteMenuRightByID(String menuid) {
        if (menuid == null || "".equals(menuid)) {
            return TransformConstant.fail;
        }
        try {
            ManSystemMenuMapper systemMenuMapper = this.sqlSession.getMapper(ManSystemMenuMapper.class);
            systemMenuMapper.deleteRightMenuByMenuID(menuid);
        } catch (Exception e) {
            throw new AppException(e);
        }
        return TransformConstant.success;
    }
    
    /**
     * @Description:新增系统菜单和权限关联关系
     * @param parameter 参数信息
     * @return String
     */
    @Transactional(readOnly = false)
    public String saveMenuRight(String[] rightids, String menuid) {
        if (rightids == null || rightids.length == 0) {
            return TransformConstant.fail;
        }
        try {
            ManSystemMenuMapper systemMenuMapper = this.sqlSession.getMapper(ManSystemMenuMapper.class);
            for (String rightid : rightids) {
                if ("".equals(rightid)) {
                    continue;
                }
                Map<String, Object> relateData = new LinkedHashMap<String, Object>();
                relateData.put("billid", StringUtil.generateUUID());
                relateData.put("menuid", menuid);
                relateData.put("rightid", rightid);
                systemMenuMapper.addMenuRight(relateData);
            }
        } catch (Exception e) {
            throw new AppException(e);
        }
        return TransformConstant.success;
    }
    
}
