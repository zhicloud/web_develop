package com.zhicloud.ms.controller;

import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.IEmailConfigService;
import com.zhicloud.ms.service.IEmailTemplateService;
import com.zhicloud.ms.service.IOperLogService;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.util.TransFormPrivilegeUtil;
import com.zhicloud.ms.vo.EmailConfigVO;
import com.zhicloud.ms.vo.EmailTemplateVO;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.*;

/**
 * @author 张翔
 * @function 邮件模板管理Controller
 *
 */
@Controller
@RequestMapping("/message/email/template")
public class EmailTemplateController {

    public static final Logger logger = Logger.getLogger(EmailTemplateController.class);

    @Resource
    private IEmailTemplateService emailTemplateService;

    @Resource
    private IEmailConfigService emailConfigService;

    @Resource
    private IOperLogService operLogService;

    /**
     * @function 模板管理页面
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value="/list",method= RequestMethod.GET)
    public String managePage(Model model, HttpServletRequest request) {
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.message_email_template_query)){
            return "not_have_access";
        }
        List<EmailTemplateVO> emailConfigVOList = emailTemplateService.getAllTemplate();
        model.addAttribute("email_template_list", emailConfigVOList);
        return "message/email/template/email_template_manage";
    }

    /**
     * @function 模板名查重
     * @param name
     * @return
     */
    @RequestMapping(value="/checkname",method= RequestMethod.POST)
    @ResponseBody
    public MethodResult checkName(String name) {
        if (MethodResult.SUCCESS.equals(emailTemplateService.checkTemplateName(name).status)) {
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
        if (MethodResult.SUCCESS.equals(emailTemplateService.checkTemplateCode(code).status)) {
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
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.message_email_template_add)){
            return "not_have_access";
        }
        List<EmailConfigVO> emailConfigList = emailConfigService.getAllConfig();
        model.addAttribute("email_config_list", emailConfigList);
        return "message/email/template/email_template_add";
    }

    /**
     * @function 新增模板
     * @param emailTemplateVO
     * @param request
     * @return
     */
    @RequestMapping(value="/add",method= RequestMethod.POST)
    @ResponseBody
    public MethodResult add(EmailTemplateVO emailTemplateVO, HttpServletRequest request) {
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.message_email_template_add)){
            return new MethodResult(MethodResult.FAIL,"您没有新增邮件模板的权限，请联系管理员");
        }
        try{

            Map<String, Object> data = new LinkedHashMap<String, Object>();
            data.put("config_id", emailTemplateVO.getConfigId());
            data.put("name", emailTemplateVO.getName());
            data.put("code", emailTemplateVO.getCode());
            data.put("recipient", emailTemplateVO.getRecipient());
            data.put("subject", emailTemplateVO.getSubject());
            data.put("content", emailTemplateVO.getContent());
            MethodResult result = emailTemplateService.addTemplate(data);
            if(MethodResult.SUCCESS.equals(result.status)) {
                operLogService.addLog("邮件模板", "新增邮件模板成功", "1", "1", request);
                return new MethodResult(MethodResult.SUCCESS,result.message);
            }

        } catch (Exception e) {
            e.printStackTrace();
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

        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.message_email_template_mod)){
            return "not_have_access";
        }

        List<EmailConfigVO> emailConfigList = emailConfigService.getAllConfig();
        EmailTemplateVO mailTemplateVO = emailTemplateService.getTemplateById(id);

        model.addAttribute("email_config_list", emailConfigList);
        model.addAttribute("mail_template_vo", mailTemplateVO);

        return "message/email/template/email_template_mod";
    }

    /**
     * @function 修改模板
     * @param emailTemplateVO
     * @param request
     * @return
     */
    @RequestMapping(value="/mod",method= RequestMethod.POST)
    @ResponseBody
    public MethodResult mod(EmailTemplateVO emailTemplateVO, HttpServletRequest request) {
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.message_email_template_mod)){
            return new MethodResult(MethodResult.FAIL,"您没有修改邮件模板的权限，请联系管理员");
        }
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("id", emailTemplateVO.getId());
        data.put("config_id", emailTemplateVO.getConfigId());
        data.put("name", emailTemplateVO.getName());
        data.put("code", emailTemplateVO.getCode());
        data.put("recipient", emailTemplateVO.getRecipient());
        data.put("subject", emailTemplateVO.getSubject());
        data.put("content", emailTemplateVO.getContent());
        if(MethodResult.SUCCESS.equals(emailTemplateService.modifyTemplateById(data).status)) {
            operLogService.addLog("邮件模板", "修改邮件模板成功", "1", "1", request);
            return new MethodResult(MethodResult.SUCCESS,"修改成功");
        }
        operLogService.addLog("邮件模板", "修改邮件模板失败", "1", "2", request);
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
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.message_email_template_remove)){
            return new MethodResult(MethodResult.FAIL,"您没有删除邮件模板的权限，请联系管理员");
        }
        List<String> ids = new ArrayList<String>();
        ids.add(id);

        if(MethodResult.SUCCESS.equals(emailTemplateService.removeTemplateByIds(ids).status)) {
            operLogService.addLog("邮件模板", "删除邮件模板成功", "1", "1", request);
            return new MethodResult(MethodResult.SUCCESS,"删除成功");
        }
        operLogService.addLog("邮件模板", "删除邮件模板失败", "1", "2", request);
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
    public MethodResult multiRemove(@RequestParam("ids[]") String[] ids, HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.message_email_template_remove)){
            return new MethodResult(MethodResult.FAIL,"您没有删除邮件模板的权限，请联系管理员");
        }
        List<String> idList = Arrays.asList(ids);
        MethodResult result = emailTemplateService.removeTemplateByIds(idList);

        if(MethodResult.SUCCESS.equals(result.status)) {
            operLogService.addLog("邮件模板", "删除邮件模板失败", "1", "1", request);
            return new MethodResult(MethodResult.SUCCESS, result.message);
        }
        operLogService.addLog("邮件模板", "删除邮件模板失败", "1", "2", request);
        return new MethodResult(MethodResult.FAIL,"删除失败");
    }

}
