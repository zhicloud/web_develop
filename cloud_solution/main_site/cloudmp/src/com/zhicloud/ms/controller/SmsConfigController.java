package com.zhicloud.ms.controller;

import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.IOperLogService;
import com.zhicloud.ms.service.ISmsConfigService;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.util.TransFormPrivilegeUtil;
import com.zhicloud.ms.vo.SmsConfigVO;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.*;

/**
 * @author 张翔
 * @function 邮件配置管理Controller
 *
 */
@Controller
@RequestMapping("/message/sms/config")
public class SmsConfigController {

    public static final Logger logger = Logger.getLogger(SmsConfigController.class);

    @Resource
    private ISmsConfigService smsConfigService;
    
    @Resource
    private IOperLogService operLogService;

    /**
     * @function 配置管理页面
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value="/list",method= RequestMethod.GET)
    public String managePage(Model model,HttpServletRequest request) {
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.message_sms_config_query)){
            return "not_have_access";
        }
        List<SmsConfigVO> smsConfigVOList = smsConfigService.getAllConfig();
        model.addAttribute("sms_config_list", smsConfigVOList);
        return "message/sms/config/sms_config_manage";
    }

    /**
     * @function 配置名查重
     * @param name
     * @return
     */
    @RequestMapping(value="/checkname",method= RequestMethod.POST)
    @ResponseBody
    public MethodResult checkName(String name) {
        if (MethodResult.SUCCESS.equals(smsConfigService.checkConfigName(name).status)) {
            return new MethodResult(MethodResult.SUCCESS, "该配置名有效");
        }
        return new MethodResult(MethodResult.FAIL, "该配置名已存在");
    }

    /**
     * @function 新增配置页面
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value="/add",method= RequestMethod.GET)
    public String addPage(HttpServletRequest request, HttpServletResponse response) {
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.message_sms_config_add)){
            return "not_have_access";
        }

        return "message/sms/config/sms_config_add";
    }

    /**
     * @function 新增配置
     * @param smsConfigVO
     * @param request
     * @return
     */
    @RequestMapping(value="/add",method= RequestMethod.POST)
    @ResponseBody
    public MethodResult add(SmsConfigVO smsConfigVO, HttpServletRequest request) {
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.message_sms_config_add)){
            return new MethodResult(MethodResult.FAIL,"您没有新增邮件配置的权限，请联系管理员");
        }

        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("sms_id", smsConfigVO.getSmsId());
        data.put("name", smsConfigVO.getName());
        data.put("service_url", smsConfigVO.getServiceUrl());
        data.put("config_name", smsConfigVO.getConfigName());
        data.put("password", smsConfigVO.getPassword());
        if(MethodResult.SUCCESS.equals(smsConfigService.addConfig(data).status)) {
            operLogService.addLog("短信模板", "新增短信模板成功", "1", "1", request);
            return new MethodResult(MethodResult.SUCCESS,"创建成功");
        }
        operLogService.addLog("短信模板", "新增短信模板失败", "1", "2", request);
        return new MethodResult(MethodResult.FAIL,"创建失败");

    }

    /**
     * @function 配置修改页面
     * @param id
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value="/{id}/mod",method= RequestMethod.GET)
    public String modPage(@PathVariable("id") String id, Model model,HttpServletRequest request) {

        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.message_sms_config_mod)){
            return "not_have_access";
        }

        SmsConfigVO mailConfigVO = smsConfigService.getConfigById(id);
        model.addAttribute("sms_config_vo", mailConfigVO);

        return "message/sms/config/sms_config_mod";
    }

    /**
     * @function 修改配置
     * @param smsConfigVO
     * @param request
     * @return
     */
    @RequestMapping(value="/mod",method= RequestMethod.POST)
    @ResponseBody
    public MethodResult mod(SmsConfigVO smsConfigVO, HttpServletRequest request) {
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.message_sms_config_mod)){
            return new MethodResult(MethodResult.FAIL,"您没有修改邮件配置的权限，请联系管理员");
        }
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("id", smsConfigVO.getId());
        data.put("sms_id", smsConfigVO.getSmsId());
        data.put("name", smsConfigVO.getName());
        data.put("service_url", smsConfigVO.getServiceUrl());
        data.put("config_name", smsConfigVO.getConfigName());
        data.put("password", smsConfigVO.getPassword());
        if(MethodResult.SUCCESS.equals(smsConfigService.modifyConfigById(data).status)) {
            operLogService.addLog("短信模板", "修改短信模板成功", "1", "1", request);
            return new MethodResult(MethodResult.SUCCESS,"修改成功");
        }
        operLogService.addLog("短信模板", "修改短信模板失败", "1", "2", request);
        return new MethodResult(MethodResult.FAIL,"修改失败");
    }

    /**
     * @function 删除
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value="/{id}/remove",method= RequestMethod.GET)
    @ResponseBody
    public MethodResult remove(@PathVariable("id") String id, HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.message_sms_config_remove)){
            return new MethodResult(MethodResult.FAIL,"您没有删除邮件配置的权限，请联系管理员");
        }
        List<String> ids = new ArrayList<String>();
        ids.add(id);

        if(MethodResult.SUCCESS.equals(smsConfigService.removeConfigByIds(ids).status)) {
            operLogService.addLog("短信模板", "删除短信模板失败", "1", "1", request);
            return new MethodResult(MethodResult.SUCCESS,"删除成功");
        }
        operLogService.addLog("短信模板", "删除短信模板失败", "1", "2", request);
        return new MethodResult(MethodResult.FAIL,"删除失败");
    }

    /**
     * @function 批量删除
     * @param ids
     * @param request
     * @return
     */
    @RequestMapping(value="/remove",method= RequestMethod.POST)
    @ResponseBody
    public MethodResult MultiRemove(@RequestParam("ids[]") String[] ids, HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.message_sms_config_remove)){
            return new MethodResult(MethodResult.FAIL,"您没有删除邮件配置的权限，请联系管理员");
        }
        List<String> idList = Arrays.asList(ids);

        if(MethodResult.SUCCESS.equals(smsConfigService.removeConfigByIds(idList).status)) {
            operLogService.addLog("短信模板", "删除短信模板失败", "1", "1", request);
            return new MethodResult(MethodResult.SUCCESS,"删除成功");
        }
        operLogService.addLog("短信模板", "删除短信模板失败", "1", "2", request);
        return new MethodResult(MethodResult.FAIL,"删除失败");
    }

}
