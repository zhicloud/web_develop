
package com.zhicloud.ms.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.IOperLogService;
import com.zhicloud.ms.service.SysWarnService;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.constant.TransformConstant;
import com.zhicloud.ms.transform.controller.TransFormBaseAction;
import com.zhicloud.ms.transform.util.TransFormLoginHelper;
import com.zhicloud.ms.transform.util.TransFormLoginInfo;
import com.zhicloud.ms.vo.SysWarnRuleVO;
import com.zhicloud.ms.vo.SysWarnValueVO;

/**
 * @ClassName: SysWarnController
 * @Description: 告警规则模块控制类
 * @author 张本缘 于 2015年8月13日 上午10:40:16
 */
@Controller
@RequestMapping("/syswarn")
public class SysWarnController extends TransFormBaseAction {
    /* 日志 */
    public static final Logger logger = Logger.getLogger(SysWarnController.class);
    @Resource
    private SysWarnService sysWarnService;
    
    @Resource
    private IOperLogService operLogService;

    /**
     * @Description:规则首页
     * @param request
     * @return String
     */
    @RequestMapping(value = "/rulelist", method = RequestMethod.GET)
    public String ruleListQuery(HttpServletRequest request) {
        logger.debug("SysWarnController.ruleListQuery()");
        boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.monitor_warn_query);
        if (!flag) {
            return TransformConstant.transform_jsp_noaccsess;
        }
        List<SysWarnRuleVO> sysWarnRuleVO = sysWarnService.RuleQuery();
        request.setAttribute("sysWarnRuleList", sysWarnRuleVO);
        return "/monitor/sys_warnrule_manage";
    }

    /**
     * @Description:规则信息编辑页面跳转
     * @param request
     * @return String
     */
    @RequestMapping(value = "/ruleedit", method = RequestMethod.GET)
    public String ruleEdit(HttpServletRequest request) {
        logger.debug("SysWarnController.ruleEdit()");
        String type = request.getParameter("type").trim();
        boolean flag = true;
        if ("add".equals(type)) {
            flag = isHasPrivilege(request, TransFormPrivilegeConstant.monitor_warn_add);
        } else {
            flag = isHasPrivilege(request, TransFormPrivilegeConstant.monitor_warn_modify);
        }
        if (!flag) {
            return TransformConstant.transform_jsp_noaccsess;
        }
        if (!"add".equals(type)) {
            String id = request.getParameter("id").trim();
            SysWarnRuleVO rule = sysWarnService.getWarnRuleById(id);
            request.setAttribute("ruleVO", rule);
            request.setAttribute("type", type);
        }
        return "/monitor/sys_warnrule_edit";
    }

    /**
     * @Description:规则内容编辑页面跳转
     * @param request
     * @return String
     */
    @RequestMapping(value = "/valueedit", method = RequestMethod.GET)
    public String valueEdit(HttpServletRequest request) {
        logger.debug("SysWarnController.ruleEdit()");
        boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.monitor_warn_value);
        if (!flag) {
            return TransformConstant.transform_jsp_noaccsess;
        }
        String ruleid = request.getParameter("ruleid").trim();
        request.setAttribute("ruleid", ruleid);
        List<SysWarnValueVO> valueVOS = sysWarnService.getValueByRuleID(ruleid);
        request.setAttribute("valueVOS", valueVOS);
        return "/monitor/sys_warnvalue_edit";
    }
    
    /**
     * @Description:保存规则信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/saverule", method = RequestMethod.POST)
    @ResponseBody
    public MethodResult saveRuleInfo(SysWarnRuleVO rule, HttpServletRequest request) {
        if ("add".equals(rule.getEdittype())) {
            // 判断code是否重复
            SysWarnRuleVO VO = sysWarnService.getWarnRuleByCode(rule.getCode());
            if (VO != null) {
                operLogService.addLog("告警规则", "添加告警规则失败", "1", "2", request);
                return new MethodResult(MethodResult.FAIL, "标示符已经存在");
            }
            // 判断名称是否重复
            VO = sysWarnService.getWarnRuleByName(rule.getName());
            if (VO != null) {
                operLogService.addLog("告警规则", "添加告警规则失败", "1", "2", request);
                return new MethodResult(MethodResult.FAIL, "名称已经存在");
            }
        } else {
            // 判断code是否重复
            SysWarnRuleVO VO = sysWarnService.getWarnRuleByCode(rule.getCode());
            if (VO != null && !VO.getId().equals(rule.getId())) {
                operLogService.addLog("告警规则", "添加告警规则失败", "1", "2", request);
                return new MethodResult(MethodResult.FAIL, "标示符已经存在");
            }
            // 判断名称是否重复
            VO = sysWarnService.getWarnRuleByName(rule.getName());
            if (VO != null && !VO.getId().equals(rule.getId())) {
                operLogService.addLog("告警规则", "添加告警规则失败", "1", "2", request);
                return new MethodResult(MethodResult.FAIL, "名称已经存在");
            }
        }
        TransFormLoginInfo login = TransFormLoginHelper.getLoginInfo(request);
        int n = sysWarnService.SaveRuleInfo(rule, login.getBillid());
        if (n > 0) {
            operLogService.addLog("告警规则", "添加告警规则成功", "1", "1", request);
            return new MethodResult(MethodResult.SUCCESS, "保存成功");
        } else {
            operLogService.addLog("告警规则", "添加告警规则失败", "1", "2", request);
            return new MethodResult(MethodResult.FAIL, "保存失败");
        }
    }
    
    /**
     * @Description:保存规则信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/deleterule", method= RequestMethod.POST)
    @ResponseBody
    public MethodResult deleterule(@RequestParam("ids[]") String[] ids, HttpServletRequest request) {
        int n = sysWarnService.deleterule(ids);
        if (n > 0) {
            operLogService.addLog("告警规则", "删除告警规则成功", "1", "1", request);
            return new MethodResult(MethodResult.SUCCESS, "删除成功");
        } else {
            operLogService.addLog("告警规则", "删除告警规则失败", "1", "2", request);
            return new MethodResult(MethodResult.FAIL, "删除失败");
        }
    }
    
    /**
     * @Description:保存规则信息
     * @param request
     * @return
     */
    @RequestMapping(value = "/savevalue", method = RequestMethod.POST)
    @ResponseBody
    public MethodResult saveValueInfo(@RequestParam("datas") String datas, @RequestParam("ruleid") String ruleid,
            HttpServletRequest request) {
        JSONArray array = JSONArray.fromObject(datas);
        int n = sysWarnService.saveValueInfo(array, ruleid);
        if (n > 0) {
            operLogService.addLog("告警规则", "更新告警规则内容成功", "1", "1", request);
            return new MethodResult(MethodResult.SUCCESS, "保存成功");
        } else {
            operLogService.addLog("告警规则", "更新告警规则内容失败", "1", "2", request);
            return new MethodResult(MethodResult.FAIL, "保存失败");
        }
    }
}
