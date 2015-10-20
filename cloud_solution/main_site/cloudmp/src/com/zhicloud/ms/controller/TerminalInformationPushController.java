package com.zhicloud.ms.controller;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.ISysGroupService;
import com.zhicloud.ms.service.ITerminalInformationPushService;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.util.TransFormPrivilegeUtil;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.SysGroupVO;
import com.zhicloud.ms.vo.TerminalBoxVO;
import com.zhicloud.ms.vo.TerminalInformationPushVO;

/**
 * 
* @ClassName: TerminalInformationPushController 
* @Description: 终端推送消息 
* @author sasa
* @date 2015年9月1日 下午2:27:46 
*
 */
@Controller
public class TerminalInformationPushController {
    
    public static final Logger logger = Logger.getLogger(TerminalInformationPushController.class);
    @Resource
    private ITerminalInformationPushService terminalInformationPushService;
    
    @Resource
    private ISysGroupService sysGroupService;
    
    
    
    
    /**
     * 
    * @Title: list 
    * @Description: 消息列表 
    * @param @param model
    * @param @param request
    * @param @return
    * @param @throws UnsupportedEncodingException      
    * @return String     
    * @throws
     */
    @RequestMapping(value="/terminalinfo/list",method= RequestMethod.GET)
    public String list(Model model,HttpServletRequest request) throws UnsupportedEncodingException {
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.terminal_box_info_push_query)){
            return "not_have_access";
        }
       
        String time = request.getParameter("time");
        List<TerminalInformationPushVO> list = new ArrayList<TerminalInformationPushVO>();
        if(StringUtil.isBlank(time)){
            list = terminalInformationPushService.queryAll();           
        }else{
            list = terminalInformationPushService.queryAllByTime(time);
        }
        model.addAttribute("list", list);    
        model.addAttribute("time", time);
        return "terminalinfo/terminal_information_push_manage";
    } 
    
    /**
     * 
    * @Title: infoAddPage 
    * @Description: 消息新增页面 
    * @param @param model
    * @param @param request
    * @param @return
    * @param @throws UnsupportedEncodingException      
    * @return String     
    * @throws
     */
    @RequestMapping(value="/terminalinfo/add",method= RequestMethod.GET)
    public String infoAddPage(Model model,HttpServletRequest request) throws UnsupportedEncodingException {
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.terminal_box_info_push_add)){
            return "not_have_access";
        }
       
        List<SysGroupVO> groupList = sysGroupService.queryAll();
        model.addAttribute("groupList", groupList);        
        return "terminalinfo/terminal_information_push_add";
    }
    /**
     * 
    * @Title: infoAdd 
    * @Description: 新增消息推送
    * @param @param info
    * @param @param request
    * @param @return
    * @param @throws UnsupportedEncodingException      
    * @return MethodResult     
    * @throws
     */
    @RequestMapping(value="/terminalinfo/add",method= RequestMethod.POST)
    @ResponseBody
    public MethodResult infoAdd(TerminalInformationPushVO info,HttpServletRequest request) throws UnsupportedEncodingException {
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.terminal_box_info_push_add)){
            return new MethodResult(MethodResult.FAIL,"您没有新增消息的权限，请联系管理员");
        }
        int i = terminalInformationPushService.addInfomation(info);
        if(i > 0){
            return new MethodResult(MethodResult.SUCCESS,"创建成功");
        }
        return new MethodResult(MethodResult.FAIL,"创建失败");
       
     }
    /**
     * 
    * @Title: deleteBoxes 
    * @Description: 消息删除
    * @param @param ids
    * @param @param request
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    @RequestMapping(value="/terminalinfo/delete",method= RequestMethod.POST)
    @ResponseBody
    public MethodResult deleteBoxes(@RequestParam("ids[]") String[] ids, HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.terminal_box_info_push_delete)){
            return new MethodResult(MethodResult.FAIL,"您没有删除消息的权限，请联系管理员");
        }

        List<String> idsList = Arrays.asList(ids);

        if(terminalInformationPushService.deleteInformation(idsList) > 0 ) {
            return new MethodResult(MethodResult.SUCCESS,"删除成功");
        }
        return new MethodResult(MethodResult.FAIL,"删除失败");
    }
    

}
