package com.zhicloud.ms.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhicloud.ms.app.pool.host.back.HostBackupProgressData;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.IIntelligentRouterService;
import com.zhicloud.ms.service.IOperLogService;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.util.TransFormPrivilegeUtil;
import com.zhicloud.ms.vo.CloudHostVO;
/**
 * 
* @ClassName: IntelligentRouterRuleController 
* @Description: 智能路由例外规则操作 
* @author sasa
* @date 2015年7月24日 下午3:44:47 
*
 */
@Controller
public class IntelligentRouterRuleController {
    
    @Resource
    private IIntelligentRouterService intelligentRouterService;
    
    @Resource
    private IOperLogService operLogService;
    
    
    /**
     * 
    * @Title: getAll 
    * @Description: 查询所有的例外规则
    * @param @param model
    * @param @param request
    * @param @return      
    * @return String     
    * @throws
     */
    @RequestMapping(value="/rule/all",method=RequestMethod.GET)
    public String getAll(Model model,HttpServletRequest request){
        //检查是否有权限
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.intelligent_router_query)){
            return "not_have_access";
        }
        //查询智能路由的例外规则
        intelligentRouterService.queryRule(request);
        return "/intelligent_router_rule/intelligent_router_rule_manage";
    }
    
    @RequestMapping(value="/rule/add",method=RequestMethod.GET)
    public String toAddRulePage(Model model,HttpServletRequest request){
        //检查是否有权限
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.intelligent_router_add)){
            return "not_have_access";
        }
        //获取新增的数据
        intelligentRouterService.addRulePage(request);
        return "/intelligent_router_rule/intelligent_router_rule_add";
    }
    @RequestMapping(value="/rule/add",method=RequestMethod.POST)
    @ResponseBody
    public MethodResult addRule(String markId,String mode,String ip1,String ip2,String port1,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.intelligent_router_add)){
            return new MethodResult(MethodResult.FAIL,"您没有新增规则的权限，请联系管理员");
        }
        Map<String, Object> parameter = new LinkedHashMap<String, Object>();
        parameter.put("markId", markId);
        parameter.put("mode", mode);
        parameter.put("ip1", ip1);
        parameter.put("ip2", ip2);
        parameter.put("port1", port1);
        MethodResult mr =  intelligentRouterService.addRule(parameter);
        if(mr.isSuccess()){
            operLogService.addLog("智能路由例外规则", "新增规则成功", "1", "1", request);
        }else{
            operLogService.addLog("智能路由例外规则", "新增规则失败", "1", "2", request);
        }
        return mr;
     }
    
    @RequestMapping(value="/rule/delete",method=RequestMethod.GET)
    @ResponseBody
    public MethodResult deleteRule(String   target,String  mode, String   ip0, String   ip1, String   port,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.intelligent_router_remove)){
            return new MethodResult(MethodResult.FAIL,"您没有删除规则的权限，请联系管理员");
        }        
        MethodResult mr =  intelligentRouterService.deleteRule(target, mode, ip0, ip1, port);
        if(mr.isSuccess()){
            operLogService.addLog("智能路由例外规则", "删除规则成功", "1", "1", request);
        }else{
            operLogService.addLog("智能路由例外规则", "删除规则失败", "1", "2", request);
        }
        return mr;
     }

}
