package com.zhicloud.ms.controller;

import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.IOperLogService;
import com.zhicloud.ms.service.ISmsConfigService;
import com.zhicloud.ms.service.ISmsTemplateService;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.util.TransFormPrivilegeUtil;
import com.zhicloud.ms.vo.SmsConfigVO;
import com.zhicloud.ms.vo.SmsTemplateVO;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 张翔
 * @function 短信模板管理Controller
 *
 */
@Controller
@RequestMapping("/message/sms/template")
public class SmsTemplateController {

    public static final Logger logger = Logger.getLogger(SmsTemplateController.class);

    @Resource
    private ISmsTemplateService smsTemplateService;

    @Resource
    private ISmsConfigService smsConfigService;
    @Resource
    private IOperLogService operLogService;
    /**
     * @function 模板管理页面
     * @param request
     * @return
     */
    @RequestMapping(value="/list",method= RequestMethod.GET)
    public String managePage(Model model, HttpServletRequest request) {
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.message_sms_template_query)){
            return "not_have_access";
        }
        List<SmsTemplateVO> smsTemplateVOList = smsTemplateService.getAllTemplate();
        model.addAttribute("sms_template_list", smsTemplateVOList);
        return "message/sms/template/sms_template_manage";
    }

    /**
     * @function 模板名查重
     * @param name
     * @return
     */
    @RequestMapping(value="/checkname",method= RequestMethod.POST)
    @ResponseBody
    public MethodResult checkName(String name) {
        if (MethodResult.SUCCESS.equals(smsTemplateService.checkTemplateName(name).status)) {
            return new MethodResult(MethodResult.SUCCESS, "该模板名有效");
        }
        return new MethodResult(MethodResult.FAIL, "该模板名已存在");
    }

    /**
     * @function code 查重
     * @param code
     * @return
     */
    @RequestMapping(value="/checkcode",method= RequestMethod.POST)
    @ResponseBody
    public MethodResult checkCode(String code) {
        if (MethodResult.SUCCESS.equals(smsTemplateService.checkTemplateCode(code).status)) {
            return new MethodResult(MethodResult.SUCCESS, "该标识码有效");
        }
        return new MethodResult(MethodResult.FAIL, "该标识码已存在");
    }

    /**
     * @function 新增模板页面
     * @param request
     * @return
     */
    @RequestMapping(value="/add",method= RequestMethod.GET)
    public String addPage(Model model,HttpServletRequest request) {
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.message_sms_template_add)){
            return "not_have_access";
        }
        List<SmsConfigVO> smsConfigList = smsConfigService.getAllConfig();
        model.addAttribute("sms_config_list", smsConfigList);
        return "message/sms/template/sms_template_add";
    }

    /**
     * @function 新增模板
     * @param smsTemplateVO
     * @param request
     * @return
     */
    @RequestMapping(value="/add",method= RequestMethod.POST)
    @ResponseBody
    public MethodResult add(SmsTemplateVO smsTemplateVO, HttpServletRequest request) {
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.message_sms_template_add)){
            return new MethodResult(MethodResult.FAIL,"您没有新增邮件模板的权限，请联系管理员");
        }

        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("config_id", smsTemplateVO.getConfigId());
        data.put("name", smsTemplateVO.getName());
        data.put("code", smsTemplateVO.getCode());
        data.put("recipient", smsTemplateVO.getRecipient());
        data.put("content", smsTemplateVO.getContent());
        if(MethodResult.SUCCESS.equals(smsTemplateService.addTemplate(data).status)) {
            operLogService.addLog("邮件模板", "新增邮件模板成功", "1", "1", request);
            return new MethodResult(MethodResult.SUCCESS,"创建成功");
        }
        operLogService.addLog("邮件模板", "新增邮件模板失败", "1", "2", request);
        return new MethodResult(MethodResult.FAIL,"创建失败");

    }

    /**
     * @function 模板修改页面
     * @param id
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value="/{id}/mod",method= RequestMethod.GET)
    public String modPage(@PathVariable("id") String id, Model model,HttpServletRequest request) {

        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.message_sms_template_mod)){
            return "not_have_access";
        }

        List<SmsConfigVO> smsConfigList = smsConfigService.getAllConfig();
        SmsTemplateVO smsTemplateVO = smsTemplateService.getTemplateById(id);

        model.addAttribute("sms_config_list", smsConfigList);
        model.addAttribute("sms_template_vo", smsTemplateVO);

        return "message/sms/template/sms_template_mod";
    }

    /**
     * @function 修改模板
     * @param smsTemplateVO
     * @param request
     * @return
     */
    @RequestMapping(value="/mod",method= RequestMethod.POST)
    @ResponseBody
    public MethodResult mod(SmsTemplateVO smsTemplateVO, HttpServletRequest request) {
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.message_sms_template_mod)){
            return new MethodResult(MethodResult.FAIL,"您没有修改邮件模板的权限，请联系管理员");
        }
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("id", smsTemplateVO.getId());
        data.put("config_id", smsTemplateVO.getConfigId());
        data.put("name", smsTemplateVO.getName());
        data.put("code", smsTemplateVO.getCode());
        data.put("recipient", smsTemplateVO.getRecipient());
        data.put("content", smsTemplateVO.getContent());
        if(MethodResult.SUCCESS.equals(smsTemplateService.modifyTemplateById(data).status)) {
            operLogService.addLog("邮件模板", "修改邮件模板成功", "1", "1", request);
            return new MethodResult(MethodResult.SUCCESS,"修改成功");
        }
        operLogService.addLog("邮件模板", "修改邮件模板失败", "1", "2", request);
        return new MethodResult(MethodResult.FAIL,"修改失败");
    }

    /**
     * @function 删除修改
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value="/{id}/remove",method= RequestMethod.GET)
    @ResponseBody
    public MethodResult remove(@PathVariable("id") String id, HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.message_sms_template_remove)){
            return new MethodResult(MethodResult.FAIL,"您没有删除邮件模板的权限，请联系管理员");
        }
        List<String> ids = new ArrayList<String>();
        ids.add(id);

        if(MethodResult.SUCCESS.equals(smsTemplateService.removeTemplateByIds(ids).status)) {
            operLogService.addLog("邮件模板", "删除邮件模板成功", "1", "1", request);
            return new MethodResult(MethodResult.SUCCESS,"删除成功");
        }
        operLogService.addLog("邮件模板", "删除邮件模板失败", "1", "2", request);
        return new MethodResult(MethodResult.FAIL,"删除失败");
    }

}
