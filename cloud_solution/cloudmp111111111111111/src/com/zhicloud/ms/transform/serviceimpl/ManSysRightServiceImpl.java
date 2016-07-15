
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
import com.zhicloud.ms.transform.mapper.ManSystemRightMapper;
import com.zhicloud.ms.transform.service.ManSysRightService;
import com.zhicloud.ms.transform.util.TransFormLoginInfo;
import com.zhicloud.ms.transform.vo.ManSystemMenuVO;
import com.zhicloud.ms.transform.vo.ManSystemRightVO;

/**
 * @ClassName: ManSysMenuServiceImpl
 * @Description: 菜单管理实现
 * @author 张本缘 于 2015年5月12日 下午4:08:55
 */
@Transactional(readOnly = true)
public class ManSysRightServiceImpl implements ManSysRightService {
    /**
     * 日志
     */
    public static final Logger logger = Logger.getLogger(ManSysRightServiceImpl.class);

    private SqlSession sqlSession;

    /**
     * @Description:sqlSession注入
     * @param sqlSession
     */
    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
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
     * Description:取得所有的功能权限信息
     * 
     * @return
     */
    @Transactional(readOnly = false)
    public List<ManSystemRightVO> getAll() {
        ManSystemRightMapper systemRightMapper = this.sqlSession.getMapper(ManSystemRightMapper.class);
        ManSystemMenuMapper  systemMenuMapper = this.sqlSession.getMapper(ManSystemMenuMapper.class);
        ManSystemMenuVO menu = null;
        // 根据menuid获取menuname
        List<ManSystemRightVO> rights = systemRightMapper.getAll();
        if (rights != null && rights.size() > 0) {
            for (ManSystemRightVO right : rights) {
                menu = systemMenuMapper.getMenuById(right.getMenuid());
                if (menu != null) {
                    right.setMenuname(menu.getMenuname());
                }
            }
        }
        return rights;
    }

    /**
     * Description:新增功能权限
     * @param parameter
     * @return String
     */
    @Transactional(readOnly = false)
    public String addSysRight(Map<String, Object> parameter, TransFormLoginInfo login) {
        logger.debug("ManSysRightServiceImpl.addSysRight()");
        try {
            String name = StringUtil.trim(parameter.get("name"));
            String code = StringUtil.trim(parameter.get("code"));
            String insert_date = StringUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
            String billid = StringUtil.generateUUID();
            ManSystemRightMapper systemRightMapper = this.sqlSession.getMapper(ManSystemRightMapper.class);
            LinkedHashMap<String, Object> condition = new LinkedHashMap<String, Object>();
            condition.put("billid", billid);
            condition.put("name", name);
            condition.put("code", code);
            condition.put("insert_date", insert_date);
            condition.put("insert_user", login.getBillid());
            // 验证该名称是否已经存在
            ManSystemRightVO systemRightVO = systemRightMapper.validateRightIsExists(condition);
            if (systemRightVO != null) {
                return "名称或编码已经存在";
            }
            // 添加功能权限数据
            int n = systemRightMapper.addSystemRight(condition);

            // 添加日志信息
            Map<String, Object> operatorData = new LinkedHashMap<String, Object>();
            operatorData.put("billid", StringUtil.generateUUID());
            operatorData.put("operateid", login.getBillid());
            operatorData.put("content", "新增了功能权限：" + name);
            operatorData.put("operate_date", insert_date);
            operatorData.put("type", TransformConstant.transform_log_right);

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
     * Description: 删除功能权限信息
     * @param billids ID数组字符串
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public String deleteSysRight(String billids, TransFormLoginInfo login) {
        logger.debug("ManSysRightServiceImpl.deleteSysRight()");
        try {
            ManSystemRightMapper systemRightMapper = this.sqlSession.getMapper(ManSystemRightMapper.class);
            String[] array = billids.split(",");
            // 添加日志信息
            for (String billid : array) {
                Map<String, Object> operatorData = new LinkedHashMap<String, Object>();
                operatorData.put("billid", StringUtil.generateUUID());
                operatorData.put("operateid", login.getBillid());
                operatorData.put("content", "删除了功能权限：" + systemRightMapper.getRightById(billid).getName());
                operatorData.put("operate_date", StringUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
                operatorData.put("type", TransformConstant.transform_log_right);
                this.addSystemLogInfo(operatorData);
            }
            // 根据权限ID删除权限和角色关联关系
            for (String billid : array) {
                systemRightMapper.deleteRoleRightByRightID(billid);
                systemRightMapper.deleteMenuRightByRightID(billid);
            }
            systemRightMapper.deleteSystemRightByIds(array);

            return TransformConstant.success;

        } catch (Exception e) {
            logger.error(e);
            throw new AppException("删除失败");
        }
    }

    /**
     * Description:修改功能权限信息
     * @param parameter
     * @return
     */
    @Transactional(readOnly = false)
    public String modSysRight(Map<String, Object> parameter, TransFormLoginInfo login) {
        logger.debug("SysRightServiceImpl.modSysRight()");
        try {
            String name = StringUtil.trim(parameter.get("name"));
            String code = StringUtil.trim(parameter.get("code"));
            String billid = StringUtil.trim(parameter.get("billid"));
            ManSystemRightMapper systemRightMapper = this.sqlSession.getMapper(ManSystemRightMapper.class);
            LinkedHashMap<String, Object> condition = new LinkedHashMap<String, Object>();
            condition.put("name", name);
            condition.put("code", code);
            condition.put("billid", billid);
            // 验证该权限名称是否已经存在
            ManSystemRightVO systemRightVO = systemRightMapper.validateRightIsExistsMod(condition);
            if (systemRightVO != null) {
                return "名称或编码已经存在";
            }
            // 更新权限数据
            int n = systemRightMapper.updateSystemRight(condition);

            // 添加日志信息
            Map<String, Object> operatorData = new LinkedHashMap<String, Object>();
            operatorData.put("billid", StringUtil.generateUUID());
            operatorData.put("operateid", login.getBillid());
            operatorData.put("content", "修改了功能权限：" + name);
            operatorData.put("operate_date", StringUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
            operatorData.put("type", TransformConstant.transform_log_right);

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
     * Description: 根据ID取得ManSystemRightVO对象
     * @param billid 主键ID
     * @return ManSystemRightVO
     */
    @Transactional(readOnly = false)
    public ManSystemRightVO getRightByID(String billid) {
        if (billid == null)
            return null;
        ManSystemRightMapper systemRightMapper = this.sqlSession.getMapper(ManSystemRightMapper.class);
        return systemRightMapper.getRightById(billid);
    }

    /**
     * Description:取得所有的功能权限信息和已经关联的角色信息
     * @return
     */
    @Transactional(readOnly = false)
    public List<ManSystemRightVO> getRightAndRole(String roleid) {
        ManSystemRightMapper systemRightMapper = this.sqlSession.getMapper(ManSystemRightMapper.class);
        return systemRightMapper.getRightAndRole(roleid);
    }
    
    /**
     * @Description:根据菜单ID查询该菜单已经勾选的功能权限和其他可用的功能权限
     * @param menuid 菜单ID
     * @return List<ManSystemRightVO>
     */
    @Transactional(readOnly = false)
    public List<ManSystemRightVO> getRightOutMenu(String menuid) {
        ManSystemRightMapper systemRightMapper = this.sqlSession.getMapper(ManSystemRightMapper.class);
        return systemRightMapper.getRightOutMenu(menuid);
    }
    
    /**
     * @Description:查询已关联该菜单的权限
     * @param menuid
     * @return
     */
    public List<ManSystemRightVO> queryRightInMenuID(String menuid) {
        ManSystemRightMapper systemRightMapper = this.sqlSession.getMapper(ManSystemRightMapper.class);
        return systemRightMapper.queryRightInMenuID(menuid);
    }

    /**
     * @Description:查询未关联该菜单的权限
     * @param menuid
     * @return
     */
    public List<ManSystemRightVO> queryRightOutMenuID(String menuid) {
        ManSystemRightMapper systemRightMapper = this.sqlSession.getMapper(ManSystemRightMapper.class);
        return systemRightMapper.queryRightOutMenuID(menuid);
    }
}
