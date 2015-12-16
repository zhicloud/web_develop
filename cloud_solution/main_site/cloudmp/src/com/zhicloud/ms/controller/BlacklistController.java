package com.zhicloud.ms.controller;
 

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

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

import com.zhicloud.ms.app.propeties.AppProperties;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.IBlacklistService;
import com.zhicloud.ms.service.IWhitelistService;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.util.TransFormPrivilegeUtil;
import com.zhicloud.ms.vo.BlacklistVO;
import com.zhicloud.ms.vo.WhitelistVO;

@Controller
@RequestMapping(value="/blacklist")
public class BlacklistController {
    
    public static final Logger logger = Logger.getLogger(BlacklistController.class);
    @Resource
    private IBlacklistService blacklistService;
    @Resource
    private IWhitelistService whitelistService;
    
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
    
    @RequestMapping(value="/checkIpAvailable",method=RequestMethod.GET)
    @ResponseBody
    public MethodResult checkIpAvailable(HttpServletRequest request){
    	//获取用户IP
    	String userIp = request.getRemoteAddr();
    	//获取黑白名单启用规则
    	String blackAndWhite = AppProperties.getValue("black_white_list");
    	//判断若为启用黑名单
    	if("1".equals(blackAndWhite)){
    		//获取所有黑名单规则
    		List<BlacklistVO> blacklistList = blacklistService.getAll();
    		if(blacklistList!=null && blacklistList.size()>0){
    			//循环匹配，若有一个匹配则不可用
    			for(BlacklistVO blacklist : blacklistList){
    				boolean b = Pattern.matches(blacklist.getRuleIp(), userIp);
    				if(b){
    					return new MethodResult(MethodResult.FAIL,"该IP不可上传镜像");
    				}
    			}
    			return new MethodResult(MethodResult.SUCCESS,"该IP可上传镜像");
    		}else{
    			return new MethodResult(MethodResult.SUCCESS,"该IP可上传镜像");
    		}
    	}else if("2".equals(blackAndWhite)){
    		List<WhitelistVO> whitelistList = whitelistService.getAll();
    		if(whitelistList!=null && whitelistList.size()>0){
    			for(WhitelistVO whitelist : whitelistList){
    				boolean b = Pattern.matches(whitelist.getRuleIp(), userIp);
    				if(b){
    					return new MethodResult(MethodResult.SUCCESS,"该IP可上传镜像");
    				}
    			}
    			return new MethodResult(MethodResult.FAIL,"该IP不可上传镜像");
    		}else{
    			return new MethodResult(MethodResult.FAIL,"该IP不可上传镜像");
    		}
    	}else {
    		return new MethodResult(MethodResult.SUCCESS,"该IP可上传镜像");
    	}
    }
}
