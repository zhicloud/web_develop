package com.zhicloud.ms.controller;
 

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.IBlacklistService;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.util.TransFormPrivilegeUtil;
import com.zhicloud.ms.vo.BlacklistVO;

@Controller
@RequestMapping(value="/blacklist")
public class BlacklistController {
    
    public static final Logger logger = Logger.getLogger(BlacklistController.class);
    @Resource
    private IBlacklistService blacklistService;
    
    @RequestMapping(value="/all",method=RequestMethod.GET)
    public String managePage(Model model,HttpServletRequest request){ 
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.blacklist_manage)){
            return "not_have_access";
        }
        List<BlacklistVO> blacklistList = blacklistService.getAll();
        model.addAttribute("blacklistList", blacklistList);
        return "black_white_list/blacklist_manage";
    }
    
    @RequestMapping(value="/add",method=RequestMethod.GET)
    public String addPage(HttpServletRequest request){
    	if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.blacklist_manage_add)){
            return "not_have_access";
        }
    	return "black_white_list/blacklist_manage_add";
    }
    
    @RequestMapping(value="/add",method=RequestMethod.POST)
    @ResponseBody
    public MethodResult addBlacklist(BlacklistVO blacklist,HttpServletRequest request){
    	MethodResult mr = blacklistService.addBlacklist(blacklist, request);
    	return mr;
    }
    
    @RequestMapping(value="/{id}/update",method=RequestMethod.GET)
    public String updatePage(@PathVariable("id") String id, Model model,HttpServletRequest request){
    	if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.blacklist_manage_update)){
            return "not_have_access";
        }
    	BlacklistVO blacklist = blacklistService.getByRuleId(id);
    	model.addAttribute("blacklist", blacklist);
    	return "black_white_list/blacklist_manage_update";
    }
    
    @RequestMapping(value="/update",method=RequestMethod.POST)
    @ResponseBody
    public MethodResult updateBlacklist(BlacklistVO blacklist,HttpServletRequest request){
    	MethodResult mr = blacklistService.updateBlacklist(blacklist, request);
    	return mr;
    }
    
    @RequestMapping(value="/{id}/delete",method=RequestMethod.GET)
    @ResponseBody
    public MethodResult deleteBlacklist(@PathVariable("id") String id,HttpServletRequest request){
    	if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.blacklist_manage_delete)){
            return new MethodResult(MethodResult.FAIL,"您没有删除黑名单的权限，请联系管理员");
        }
    	MethodResult mr = blacklistService.deleteBlacklist(id);
    	return mr;
    }
    
    @RequestMapping(value="/deletemore",method=RequestMethod.POST)
    @ResponseBody
    public MethodResult deleteBlacklists(@RequestParam("ids[]") String[] ids,HttpServletRequest request){
    	if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.blacklist_manage_delete)){
            return new MethodResult(MethodResult.FAIL,"您没有删除黑名单的权限，请联系管理员");
        }
    	List<String> idList = Arrays.asList(ids);
    	MethodResult mr = blacklistService.deleteBlacklists(idList);
    	return mr;
    }
    
    @RequestMapping(value="/checkname",method=RequestMethod.POST)
    @ResponseBody
    public MethodResult checkName(@RequestParam("name") String name){
    	BlacklistVO blacklist = blacklistService.getByRuleName(name);
    	if(blacklist==null){
    		return new MethodResult(MethodResult.SUCCESS,"名字可用");
    	}else{
    		return new MethodResult(MethodResult.FAIL,"名字不可用");
    	}
    }
}
