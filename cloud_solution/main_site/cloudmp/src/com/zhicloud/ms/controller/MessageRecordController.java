package com.zhicloud.ms.controller;

import com.zhicloud.ms.constant.AppConstant;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.IMessageRecordService;
import com.zhicloud.ms.service.IOperLogService;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.util.TransFormPrivilegeUtil;
import com.zhicloud.ms.vo.MessageRecordVO;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author 张翔
 * @function 消息记录Controller
 *
 */
@Controller
@RequestMapping("/message/record")
public class MessageRecordController {

    public static final Logger logger = Logger.getLogger(MessageRecordController.class);

    @Resource
    private IMessageRecordService messageRecordService;
    
    
    @Resource
    private IOperLogService operLogService;

    /**
     * @function 邮件记录管理页面
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value="/email/list",method= RequestMethod.GET)
    public String emailManagePage(Model model,HttpServletRequest request) {
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.message_record_query)){
            return "not_have_access";
        }
        List<MessageRecordVO> messageRecordVOList = messageRecordService.getAllRecord(AppConstant.MESSAGE_TYPE_EMAIL);
        model.addAttribute("message_record_list", messageRecordVOList);
        return "message/email/email_record_manage";
    }

    /**
     * @function 邮件记录详细页面
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value="/email/{id}/detail",method= RequestMethod.GET)
    public String emailDetailPage(@PathVariable("id") String id, Model model,HttpServletRequest request) {
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.message_record_query)){
            return "not_have_access";
        }
        MessageRecordVO messageRecordVO = messageRecordService.getRecordById(id);
        model.addAttribute("message_record_vo", messageRecordVO);
        return "message/email/email_record_detail";
    }

    /**
     * @function 短信记录管理页面
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value="/sms/list",method= RequestMethod.GET)
    public String smsManagePage(Model model,HttpServletRequest request) {
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.message_record_query)){
            return "not_have_access";
        }
        List<MessageRecordVO> messageRecordVOList = messageRecordService.getAllRecord(AppConstant.MESSAGE_TYPE_SMS);
        model.addAttribute("message_record_list", messageRecordVOList);
        return "message/sms/sms_record_manage";
    }

    /**
     * @function 短信记录管理页面
     * @param model
     * @param request
     * @return
     */
    @RequestMapping(value="/sms/{id}/detail",method= RequestMethod.GET)
    public String smsDetailPage(@PathVariable("id") String id, Model model,HttpServletRequest request) {
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.message_record_query)){
            return "not_have_access";
        }
        MessageRecordVO messageRecordVO = messageRecordService.getRecordById(id);
        model.addAttribute("message_record_vo", messageRecordVO);
        return "message/sms/sms_record_detail";
    }


    /**
     * @function 删除邮件发送记录
     * @param id
     * @param request
     * @return
     */
    @RequestMapping(value="/{id}/remove",method= RequestMethod.GET)
    @ResponseBody
    public MethodResult remove(@PathVariable("id") String id, HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.message_record_remove)){
            return new MethodResult(MethodResult.FAIL,"您没有删除发送记录的权限，请联系管理员");
        }
        List<String> ids = new ArrayList<String>();
        ids.add(id);

        if(MethodResult.SUCCESS.equals(messageRecordService.removeRecordByIds(ids).status)) {
            operLogService.addLog("消息记录", "删除发送记录成功", "1", "1", request);
            return new MethodResult(MethodResult.SUCCESS,"删除成功");
        }
        operLogService.addLog("消息记录", "删除发送记录失败", "1", "2", request);
        return new MethodResult(MethodResult.FAIL,"删除失败");
    }

    /**
     * @function 删除邮件发送记录
     * @param ids
     * @param request
     * @return
     */
    @RequestMapping(value="/remove",method= RequestMethod.POST)
    @ResponseBody
    public MethodResult removeRecords(@RequestParam("ids[]") String[] ids, HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.message_record_remove)){
            return new MethodResult(MethodResult.FAIL,"您没有删除发送记录的权限，请联系管理员");
        }

        List<String> idsList = Arrays.asList(ids);

        if(MethodResult.SUCCESS.equals(messageRecordService.removeRecordByIds(idsList).status)) {
            operLogService.addLog("消息记录", "批量删除发送记录成功", "1", "1", request);
            return new MethodResult(MethodResult.SUCCESS,"删除成功");
        }
        operLogService.addLog("消息记录", "批量删除发送记录首脑", "1", "2", request);
        return new MethodResult(MethodResult.FAIL,"删除失败");
    }

}
