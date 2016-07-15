package com.zhicloud.ms.message.util;

/**
 * Created by sean on 7/30/15.
 */
public class MessageConstant {

    // 邮件发送方式
    public static final String EMAIL_SEND_TYPE_KEY                   = "EMAIL_SEND_TYPE_KEY";       // 邮件发送方式key
    public static final String EMAIL_FROM_TEMPLATE_ONLY              = "0";                         // 直接发送给模板邮箱
    public static final String EMAIL_TO_RECIPIENT                    = "1";                         // 发送给指定邮箱
    public static final String EMAIL_TO_RECIPIENT_WITH_BCC           = "2";                         // 发送给指定邮箱并密送给模板邮箱


    //邮件模板
    public static final String EMAIL_TEMPLATE_KEY                 = "EMAIL_TEMPLATE_KEY";
    public static final String EMAIL_INFO_WARN_SUMMARY            = "INFO_WARN_SUMMARY";
    public static final String EMAIL_INFO_SUGGESTION              = "INFO_SUGGESTION";
    public static final String EMAIL_INFO_WARN                    = "INFO_WARN";
    public static final String EMAIL_INFO_RCOVER                  = "INFO_RCOVER";
    public static final String EMAIL_INFO_BALANCE_ZERO            = "INFO_BALANCE_ZERO";
    public static final String EMAIL_INFO_BALANCE_NOTIFICATION    = "INFO_BALANCE_NOTIFICATION";
    public static final String EMAIL_INFO_INVITAION_CODE          = "INFO_INVITAION_CODE";
    public static final String EMAIL_INFO_LOGIN_CHECK             = "INFO_LOGIN_CHECK";
    public static final String EMAIL_INFO_PHONE_CHECK             = "INFO_PHONE_CHECK";
    public static final String EMAIL_INFO_EMAIL_CHECK             = "INFO_EMAIL_CHECK";
    public static final String EMAIL_INFO_INIT_PASSWORD           = "INFO_INIT_PASSWORD";
    public static final String EMAIL_INFO_AGENT_INIT_PASSWORD     = "INFO_AGENT_INIT_PASSWORD";
    public static final String EMAIL_INFO_USER_INIT_PASSWORD      = "INFO_USER_INIT_PASSWORD";
    public static final String EMAIL_INFO_FORGET_USERNAME         = "INFO_FORGET_USERNAME";
    public static final String EMAIL_INFO_RESET_PASSWORD          = "INFO_RESET_PASSWORD";
    public static final String EMAIL_INFO_CHANGE_PASSWORD         = "INFO_CHANGE_PASSWORD";
    public static final String EMAIL_INFO_REGISTER                = "INFO_REGISTER";                //终端用户注册通知模板
    public static final String EMAIL_INFO_ADMIN_REGISTER          = "INFO_ADMIN_REGISTER";          //管理员注册通知模板
    public static final String EMAIL_INFO_RESET_PASSWORD_MANUAL   = "INFO_RESET_PASSWORD_MANUAL";   //用户手动重置密码
    public static final String EMAIL_INFO_RESET_PASSWORD_RANDOM	  = "INFO_RESET_PASSWORD_RANDOM";   //用户随机重置密码



    //短信模板
    public static final String SMS_INFO_NOTIFICATION              = "INFO_NOTIFICATION";

}
