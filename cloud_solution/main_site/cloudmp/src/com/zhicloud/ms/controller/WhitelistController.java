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
import com.zhicloud.ms.service.IWhitelistService;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.util.TransFormPrivilegeUtil;
import com.zhicloud.ms.vo.WhitelistVO;

@Controller
@RequestMapping(value="/networkrule/whitelist")
public class WhitelistController {
    
    public static final Logger logger = Logger.getLogger(WhitelistController.class);
    @Resource
    private IWhitelistService whitelistService;
    
    @RequestMapping(value="/all",method=RequestMethod.GET)
    public String managePage(Model model,HttpServletRequest request){ 
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.whitelist_manage)){
            return "not_have_access";
        }
        List<WhitelistVO> whitelistList = whitelistService.getAll();
        model.addAttribute("whitelistList", whitelistList);
        return "black_white_list/whitelist_manage";
    }
    
    @RequestMapping(value="/add",method=RequestMethod.GET)
    public String addPage(HttpServletRequest request){
    	if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.whitelist_manage_add)){
            return "not_have_access";
        }
    	return "black_white_list/whitelist_manage_add";
    }
    
    @RequestMapping(value="/add",method=RequestMethod.POST)
    @ResponseBody
    public MethodResult addWhitelist(WhitelistVO whitelist,HttpServletRequest request){
    	MethodResult mr = whitelistService.addWhitelist(whitelist, request);
    	return mr;
    }
    
    @RequestMapping(value="/{id}/update",method=RequestMethod.GET)
    public String updatePage(@PathVariable("id") String id, Model model,HttpServletRequest request){
    	if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.whitelist_manage_update)){
            return "not_have_access";
        }
    	WhitelistVO whitelist = whitelistService.getByRuleId(id);
    	model.addAttribute("whitelist", whitelist);
    	return "black_white_list/whitelist_manage_update";
    }
    
    @RequestMapping(value="/update",method=RequestMethod.POST)
    @ResponseBody
    public MethodResult updateBlacklist(WhitelistVO whitelist,HttpServletRequest request){
    	MethodResult mr = whitelistService.updateWhitelist(whitelist, request);
    	return mr;
    }
    
    @RequestMapping(value="/{id}/delete",method=RequestMethod.GET)
    @ResponseBody
    public MethodResult deleteBlacklist(@PathVariable("id") String id,HttpServletRequest request){
    	if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.whitelist_manage_delete)){
            return new MethodResult(MethodResult.FAIL,"您没有删除白名单的权限，请联系管理员");
        }
    	MethodResult mr = whitelistService.deleteWhitelist(id);
    	return mr;
    }
    
    @RequestMapping(value="/deletemore",method=RequestMethod.POST)
    @ResponseBody
    public MethodResult deleteBlacklists(@RequestParam("ids[]") String[] ids,HttpServletRequest request){
    	if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.whitelist_manage_delete)){
            return new MethodResult(MethodResult.FAIL,"您没有删除黑白名单的权限，请联系管理员");
        }
    	List<String> idList = Arrays.asList(ids);
    	MethodResult mr = whitelistService.deleteWhitelists(idList);
    	return mr;
    }
    
    @RequestMapping(value="/checkname",method=RequestMethod.POST)
    @ResponseBody
    public MethodResult checkName(@RequestParam("name") String name){
    	WhitelistVO whitelist = whitelistService.getByRuleName(name);
    	if(whitelist==null){
    		return new MethodResult(MethodResult.SUCCESS,"名字可用");
    	}else{
    		return new MethodResult(MethodResult.FAIL,"名字不可用");
    	}
    }
}
