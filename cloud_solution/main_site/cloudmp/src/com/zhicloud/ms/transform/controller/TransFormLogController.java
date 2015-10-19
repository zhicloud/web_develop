
package com.zhicloud.ms.transform.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.constant.TransformConstant;
import com.zhicloud.ms.transform.service.ManSysLogService;
import com.zhicloud.ms.transform.service.ManSysUserService;
import com.zhicloud.ms.transform.vo.ManSystemLogVO;
import com.zhicloud.ms.transform.vo.ManSystemUserVO;

/**
 * @ClassName: TransFormLogController
 * @Description: 日志业务处理控制
 * @author 张本缘 于 2015年5月22日 上午10:12:36
 */
@Controller
public class TransFormLogController extends TransFormBaseAction {
    public static final Logger logger = Logger.getLogger(TransFormLogController.class);

    @Resource
    ManSysUserService manSysUserService;
    
    @Resource
    ManSysLogService manSysLogService;
    /**
     * @Description:后台管理登录跳转
     * @param request
     * @param response
     * @return String
     * @throws Exception
     */
    @RequestMapping("/transform/querylog")
    public String queryLogByType(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("TransFormLogController.queryLogByType()");
        // 判断有无功能权限
        boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_log_query);
        if (!flag) {
            return TransformConstant.transform_jsp_noaccsess;
        }
        String type = request.getParameter("type");
        List<ManSystemLogVO> logList = manSysLogService.getAllByType(type);
        // 关联操作人员信息
        for (ManSystemLogVO log : logList) {
            ManSystemUserVO user = null;
            if (TransformConstant.transform_billid_admin.equals(log.getOperateid())) {
                user = new ManSystemUserVO();
                user.setBillid(TransformConstant.transform_billid_admin);
                user.setUsercount(TransformConstant.transform_username_admin);
            } else {
                manSysUserService.getUserInfoByID(log.getOperateid());
            }
            log.setUser(user);
        }
        request.setAttribute("logList", logList);
        return TransformConstant.transform_jsp_logmanage;
    }
}
