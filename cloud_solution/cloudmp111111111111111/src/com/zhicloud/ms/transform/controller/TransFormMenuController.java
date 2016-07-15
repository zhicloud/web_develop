
package com.zhicloud.ms.transform.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zhicloud.ms.common.util.json.JSONLibUtil;
import com.zhicloud.ms.service.IOperLogService;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.constant.TransformConstant;
import com.zhicloud.ms.transform.service.ManSysMenuService;
import com.zhicloud.ms.transform.service.ManSysRightService;
import com.zhicloud.ms.transform.util.TransFormLoginInfo;

/**
 * @ClassName: TransFormMenuController
 * @Description: 菜单管理控制层
 * @author 张本缘 于 2015年5月12日 下午4:20:03
 */
@Controller
public class TransFormMenuController extends TransFormBaseAction {
    public static final Logger logger = Logger.getLogger(TransFormMenuController.class);

    @Resource
    ManSysMenuService manSysMenuService;
    
    @Resource
    ManSysRightService manSysRightService;
    
    @Resource
    private IOperLogService operLogService;
    /**
     * @Description:菜单界面跳转
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/transform/menuadmin/index")
    public String menuManage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("TransFormMenuController.menuManage()");
        boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_menu_query);
        if (!flag) {
            return TransformConstant.transform_jsp_noaccsess;
        }
        request.setAttribute("systemMenuList", manSysMenuService.getAll("2", ""));
        return TransformConstant.transform_jsp_menumanage;
    }

    /**
     * @Description:修改菜单信息界面跳转
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/transform/menuadmin/beforeedit")
    public String beforAddMenu(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("TransFormMenuController.beforAddMenu()");
        boolean flag = false;
        String type = request.getParameter("type");
        if ("add".equals(type)) {
            request.setAttribute("modflag", "0");
            request.setAttribute("parentid", request.getParameter("parentid"));
            flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_menu_add);
        } else {
            request.setAttribute("modflag", "1");
            String billid = request.getParameter("billid");
            request.setAttribute("parentid", request.getParameter("parentid"));
            request.setAttribute("systemMenu", manSysMenuService.getMenuByID(billid));
            flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_menu_modify);
        }
        if (!flag) {
            return TransformConstant.transform_jsp_noaccsess;
        }
        return TransformConstant.transform_jsp_menuedit;
    }
    
    /**
     * @Description:新增菜单信息
     * @param systemUserVO
     * @return MethodResult
     */
    @RequestMapping(value = "/transform/menuadmin/savemenu")
    public void addSysMenu(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("utf-8");
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("menuname", request.getParameter("menuname"));
        data.put("linkname", request.getParameter("linkname"));
        data.put("remark", request.getParameter("remark"));
        data.put("status", request.getParameter("status"));
        data.put("parentid", request.getParameter("parentid"));
        data.put("sort", request.getParameter("sort"));
        data.put("cssname", request.getParameter("cssname"));
        String result = "";
        String printresult = "";
        TransFormLoginInfo login = this.getLoginInfo(request);
        // 判断是修改还是新增
        if ("1".equals(request.getParameter("modflag"))) {
            boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_menu_modify);
            if (flag) {
                data.put("billid", request.getParameter("billid"));
                result = manSysMenuService.modSysMenu(data, login);
                printresult = "修改";
            } else {
                result = "您没有操作权限";
            }

        } else {
            boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_menu_add);
            if (flag) {
                result = manSysMenuService.addSysMenu(data, login);
                printresult = "添加";
            } else {
                result = "您没有操作权限";
            }

        }
        if (TransformConstant.success.equals(result)) {
            operLogService.addLog("系统菜单", printresult+"系统菜单成功", "1", "1", request);
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply(printresult + "成功", true)));
        } else {
            operLogService.addLog("系统菜单", printresult+"系统菜单失败", "1", "2", request);
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply(result, false)));
        }
    }

    /**
     * @Description:删除菜单信息
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/transform/menuadmin/deletemenu")
    public void deleteSysMenu(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String result = "";
        boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_menu_delete);
        if (flag) {
            TransFormLoginInfo login = this.getLoginInfo(request);
            String billids = request.getParameter("billids");
            result = manSysMenuService.deleteSysMenu(billids, login);
        } else {
            result = "您没有操作权限";
        }

        if (TransformConstant.success.equals(result)) {
            operLogService.addLog("系统菜单", "删除系统菜单成功", "1", "1", request);
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("删除成功", true)));
        } else {
            operLogService.addLog("系统菜单", "删除系统菜单失败", "1", "2", request);
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply(result, false)));
        }

    }

    /**
     * @Description:子菜单界面跳转
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/transform/menuadmin/children")
    public String menuChildrenManage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("TransFormMenuController.menuChildrenManage()");
        boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_menu_query);
        if (!flag) {
            return TransformConstant.transform_jsp_noaccsess;
        }
        String parentid = request.getParameter("parentid");
        request.setAttribute("systemMenuVO", manSysMenuService.getMenuByID(parentid));
        request.setAttribute("systemMenuList", manSysMenuService.getAll("3", parentid));
        return TransformConstant.transform_jsp_menuchildrenmanage;
    }
    /**
     * @Description:设置功能权限页面跳转
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/transform/menuadmin/beforesetright")
    public String beforInitRight(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("TransFormMenuController.beforInitRight()");
        String menuid = request.getParameter("menuid");
        request.setAttribute("menuid", menuid);
        request.setAttribute("parentid", request.getParameter("parentid"));
        request.setAttribute("existsList", manSysRightService.queryRightInMenuID(menuid));
        request.setAttribute("notexistsList", manSysRightService.queryRightOutMenuID(menuid));
        return TransformConstant.transform_jsp_menuright;
    }
    
    /**
     * @Description:保存菜单和权限关联关系
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/transform/menuadmin/savemenuright")
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveMenuRight(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String rightids = request.getParameter("rightids");
        String menuid = request.getParameter("menuid");
        try {
            boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_menu_right);
            if (flag) {
                manSysMenuService.deleteMenuRightByID(menuid);
                manSysMenuService.saveMenuRight(rightids.split(","), menuid);
                operLogService.addLog("系统菜单", "更新菜单和权限关联关系成功", "1", "1", request);
                printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("保存成功", true)));
            } else {
                printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("您没有操作权限", false)));
            }

        } catch (Exception e) {
            e.printStackTrace();
            operLogService.addLog("系统菜单", "更新菜单和权限关联关系失败", "1", "2", request);
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("保存失败,请联系管理员", false)));
        }
    }
}
