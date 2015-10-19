package com.zhicloud.ms.controller;

import com.zhicloud.ms.service.IMessageRecordService;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.util.TransFormPrivilegeUtil;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author 张翔
 * @function 消息管理Controller
 *
 */
@Controller
@RequestMapping("/message/")
public class MessageManageController {

    public static final Logger logger = Logger.getLogger(MessageManageController.class);

    @Resource
    private IMessageRecordService messageRecordService;

    /**
     * @function 消息管理页面
     * @param request
     * @return 重定向到邮件配置管理
     */
    @RequestMapping(value="/manage",method= RequestMethod.GET)
    public String managePage(HttpServletRequest request) {
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.message_record_query)){
            return "not_have_access";
        }
        return "redirect:/message/email/config/list";
    }

}
