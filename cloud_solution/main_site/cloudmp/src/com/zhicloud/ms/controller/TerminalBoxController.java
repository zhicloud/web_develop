package com.zhicloud.ms.controller;

import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.ICloudHostWarehouseService;
import com.zhicloud.ms.service.IOperLogService;
import com.zhicloud.ms.service.ITerminalBoxService;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.util.TransFormPrivilegeUtil;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.TerminalBoxVO;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by sean on 6/23/15.
 */

@Controller
@RequestMapping("/box")
public class TerminalBoxController {

    public static final Logger logger = Logger.getLogger(ResourcePoolController.class);


    @Resource
    private ITerminalBoxService terminalBoxService;

    @Resource
    ICloudHostWarehouseService cloudHostWarehouseService;
    
    @Resource
    private IOperLogService operLogService;

    @RequestMapping(value="/list",method= RequestMethod.GET)
    public String list(Model model,HttpServletRequest request) throws UnsupportedEncodingException {
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.terminal_box_query)){
            return "not_have_access";
        }
        String param = request.getParameter("param");
        String statusStr = request.getParameter("status");
        Integer status;

        if(StringUtil.isBlank(param)){
            param = null;
        }else{
            param = new String(request.getParameter("param").getBytes("ISO-8859-1"),"UTF-8");
            model.addAttribute("parameter", param);
            param = "%"+param+"%";
        }

        if(StringUtil.isBlank(statusStr)){
            status = null;
        } else{
            status = Integer.valueOf(statusStr);
        }
        model.addAttribute("status", status);


        Map<String, Object> condition = new LinkedHashMap<String, Object>();
        condition.put("param", param);
        condition.put("status", status);

        List<TerminalBoxVO> terminalBoxList = terminalBoxService.getAll(condition);
        model.addAttribute("terminal_box_list", terminalBoxList);
        return "box/terminal_box_manage";
    }

    @RequestMapping(value="/add",method= RequestMethod.GET)
    public String addPage(HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.terminal_box_add)){
            return "not_have_access";
        }

        return "box/terminal_box_add";
    }

    @RequestMapping(value="/add",method= RequestMethod.POST)
    @ResponseBody
    public MethodResult add(TerminalBoxVO terminalBoxVO, HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.terminal_box_add)){
            return new MethodResult(MethodResult.FAIL,"您没有新增云终端的权限，请联系管理员");
        }

        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("serial_number", terminalBoxVO.getSerialNumber());
        data.put("name", terminalBoxVO.getName());
        if(MethodResult.SUCCESS.equals(terminalBoxService.addTerminalBox(data).status)) {
            operLogService.addLog("云终端", "新增云终端"+terminalBoxVO.getName()+"成功", "1", "1", request);
            return new MethodResult(MethodResult.SUCCESS,"创建成功");
        } 
        operLogService.addLog("云终端", "新增云终端"+terminalBoxVO.getName()+"失败", "1", "2", request);
        return new MethodResult(MethodResult.FAIL,"创建失败");
    }

    @RequestMapping(value="/{id}/modify",method= RequestMethod.GET)
    public String modifyPage(@PathVariable("id") String id, Model model,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.terminal_box_modify)){
            return "not_have_access";
        }

        TerminalBoxVO terminalBoxVO = terminalBoxService.getById(id);
        model.addAttribute("terminal_box", terminalBoxVO);
        return "box/terminal_box_mod";
    }

    @RequestMapping(value="/modify",method= RequestMethod.POST)
    @ResponseBody
    public MethodResult modify(TerminalBoxVO terminalBoxVO, HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.terminal_box_modify)){
            return new MethodResult(MethodResult.FAIL,"您没有修改云终端的权限，请联系管理员");
        }

        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("id", terminalBoxVO.getId());
        data.put("serial_number", terminalBoxVO.getSerialNumber());
        data.put("name", terminalBoxVO.getName());
        if(MethodResult.SUCCESS.equals(terminalBoxService.modifyTerminalBox(data).status)) {
            operLogService.addLog("云终端", "修改云终端"+terminalBoxVO.getName()+"成功", "1", "1", request);
            return new MethodResult(MethodResult.SUCCESS,"修改成功");
        }
        operLogService.addLog("云终端", "修改云终端"+terminalBoxVO.getName()+"失败", "1", "2", request);
        return new MethodResult(MethodResult.FAIL,"修改失败");
    }


    @RequestMapping("/assignTree")
    public void getAssignTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.setHeader("content-type", "application/x-www-form-urlencoded;charset=utf-8");
        response.getWriter().print(JSONArray.fromObject(terminalBoxService.buildTreeJSON("all", new JSONArray(), null))
                .toString());

    }

    @RequestMapping(value="/{id}/allocate",method=RequestMethod.GET)
    public String AssignWare(@PathVariable("id") String id,Model model,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_warehouse_allocate)){
            return "not_have_access";
        }
        TerminalBoxVO terminalBoxVO = terminalBoxService.getById(id);
        model.addAttribute("terminal_box", terminalBoxVO);
        return "box/terminal_box_allocate";
    }

    @RequestMapping(value="/allocate",method= RequestMethod.POST)
    @ResponseBody
    public MethodResult allocate(@RequestParam("boxId") String id, @RequestParam("userId") String userId, HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.terminal_box_allocate)){
            return new MethodResult(MethodResult.FAIL,"您没有分配云终端的权限，请联系管理员");
        }

        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("id", id);
        data.put("allocate_user_id", userId);

        if(MethodResult.SUCCESS.equals(terminalBoxService.allocateTerminalBox(data).status)) {
            operLogService.addLog("云终端", "分配云终端成功", "1", "1", request);
            return new MethodResult(MethodResult.SUCCESS,"分配成功");
        }
        operLogService.addLog("云终端", "分配云终端失败", "1", "2", request);
        return new MethodResult(MethodResult.FAIL,"分配失败");
    }

    @RequestMapping(value="/release",method= RequestMethod.POST)
    @ResponseBody
    public MethodResult release(@RequestParam("id") String id, HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.terminal_box_release)){
            return new MethodResult(MethodResult.FAIL,"您没有回收云终端的权限，请联系管理员");
        }

        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("id", id);
        if(MethodResult.SUCCESS.equals(terminalBoxService.releaseTerminalBox(data).status)) {
            operLogService.addLog("云终端", "回收云终端成功", "1", "1", request);
            return new MethodResult(MethodResult.SUCCESS,"回收成功");
        }
        operLogService.addLog("云终端", "回收云终端失败", "1", "2", request);
        return new MethodResult(MethodResult.FAIL,"回收失败");
    }

    @RequestMapping(value="/{id}/delete",method= RequestMethod.GET)
    @ResponseBody
    public MethodResult delete(@PathVariable("id") String id, HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.terminal_box_delete)){
            return new MethodResult(MethodResult.FAIL,"您没有删除云终端的权限，请联系管理员");
        }
        List<String> ids = new ArrayList<String>();
        ids.add(id);

        if(MethodResult.SUCCESS.equals(terminalBoxService.deleteTerminalBoxByIds(ids).status)) {
            operLogService.addLog("云终端", "删除云终端成功", "1", "1", request);
            return new MethodResult(MethodResult.SUCCESS,"删除成功");
        }
        operLogService.addLog("云终端", "删除云终端失败", "1", "2", request);
        return new MethodResult(MethodResult.FAIL,"删除失败");
    }

    @RequestMapping(value="/delete",method= RequestMethod.POST)
    @ResponseBody
    public MethodResult deleteBoxes(@RequestParam("ids[]") String[] ids, HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.terminal_box_delete)){
            return new MethodResult(MethodResult.FAIL,"您没有删除云终端的权限，请联系管理员");
        }

        List<String> idsList = Arrays.asList(ids);

        if(MethodResult.SUCCESS.equals(terminalBoxService.deleteTerminalBoxByIds(idsList).status)) {
            operLogService.addLog("云终端", "删除云终端成功", "1", "1", request);
            return new MethodResult(MethodResult.SUCCESS,"删除成功");
        }
        operLogService.addLog("云终端", "删除云终端失败", "1", "2", request);
        return new MethodResult(MethodResult.FAIL,"删除失败");
    }

    @RequestMapping(value="/checkSerial",method= RequestMethod.POST)
    @ResponseBody
    public MethodResult checkSerial(@RequestParam("serialNumber") String serialNumber, HttpServletRequest request){

        if(MethodResult.FAIL.equals(terminalBoxService.checkSerialNumber(serialNumber).status)) {
            return new MethodResult(MethodResult.FAIL,"该编号已存在");
        }
        return new MethodResult(MethodResult.SUCCESS,"该编号不存在");
    }
}
