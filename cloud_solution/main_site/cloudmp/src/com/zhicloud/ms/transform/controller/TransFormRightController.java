
package com.zhicloud.ms.transform.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zhicloud.ms.common.util.json.JSONLibUtil;
import com.zhicloud.ms.service.IOperLogService;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.constant.TransformConstant;
import com.zhicloud.ms.transform.service.ManSysRightService;
import com.zhicloud.ms.transform.util.TransFormLoginInfo;

/**
 * @ClassName: TransFormMenuController
 * @Description: 权限管理控制层
 * @author 张本缘 于 2015年5月12日 下午4:20:03
 */
@Controller
public class TransFormRightController extends TransFormBaseAction {
    public static final Logger logger = Logger.getLogger(TransFormRightController.class);

    @Resource
    ManSysRightService manSysRightService;
    
    @Resource
    private IOperLogService operLogService;
    
    /**
     * @Description:权限界面跳转
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/transform/rightadmin/index")
    public String rightManage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("TransFormRightController.rightManage()");
        boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_right_query);
        if (!flag) {
            return TransformConstant.transform_jsp_noaccsess;
        }
        request.setAttribute("systemRightList", manSysRightService.getAll());
        return TransformConstant.transform_jsp_rightmanage;
    }
    
    /**
     * @Description:修改权限信息界面跳转
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/transform/rightadmin/beforeedit")
    public String beforAddRight(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("TransFormRightController.beforAddRight()");
        boolean flag = false;
        String type = request.getParameter("type");
        if ("add".equals(type)) {
            request.setAttribute("modflag", "0");
            flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_right_add);
        } else {
            request.setAttribute("modflag", "1");
            String billid = request.getParameter("billid");
            request.setAttribute("systemRight", manSysRightService.getRightByID(billid));
            flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_right_modify);
        }
        if (!flag) {
            return TransformConstant.transform_jsp_noaccsess;
        }
        return TransformConstant.transform_jsp_rightedit;
    }
    
    /**
     * @Description:新增权限信息
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/transform/rightadmin/saveright")
    public void addSysRight(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("utf-8");
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("name", request.getParameter("name"));
        data.put("code", request.getParameter("code"));
        String result = "";
        String printresult = "";
        TransFormLoginInfo login = this.getLoginInfo(request);
        // 判断是修改还是新增
        if ("1".equals(request.getParameter("modflag"))) {
            boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_right_modify);
            if (flag) {
                data.put("billid", request.getParameter("billid"));
                result = manSysRightService.modSysRight(data, login);
                printresult = "修改";
            } else {
                result = "您没有操作权限!";
            }

        } else {
            boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_right_add);
            if (flag) {
                result = manSysRightService.addSysRight(data, login);
                printresult = "添加";
            } else {
                result = "您没有操作权限!";
            }

        }
        if (TransformConstant.success.equals(result)) {
            operLogService.addLog("系统权限", "新增系统权限成功", "1", "1", request);
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply(printresult + "成功", true)));
        } else {
            operLogService.addLog("系统权限", "新增系统权限失败", "1", "2", request);
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply(result, false)));
        }
    }

    /**
     * @Description:删除权限信息
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/transform/rightadmin/deleteright")
    public void deleteSysRight(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String result = "";
        boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_right_delete);
        if (flag) {
            TransFormLoginInfo login = this.getLoginInfo(request);
            String billids = request.getParameter("billids");
            result = manSysRightService.deleteSysRight(billids, login);
        } else {
            result = "您没有操作数据的权限";
        }

        if (TransformConstant.success.equals(result)) {
            operLogService.addLog("系统权限", "删除系统权限成功", "1", "1", request);
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("删除成功", true)));
        } else {
            operLogService.addLog("系统权限", "删除系统权限失败", "1", "2", request);
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply(result, false)));
        }

    }
}
