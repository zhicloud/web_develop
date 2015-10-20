package com.zhicloud.ms.transform.constant;

/**
 * @ClassName: TransformConstant
 * @Description: 平台常量
 * @author 张本缘 于 2015年5月7日 上午10:14:15
 */
public class TransformConstant {
    
    //       超级管理员账号和密码
    public static final String transform_billid_admin = "superadmin";
    public static final String transform_username_admin = "superadmin";
    public static final String transform_password_admin = "superadmin";
    public static final String transform_session_admin = "userlogin";
    public static final String transform_session_menu = "menuhtml";
    
    //   系统日志保存天数
    public static final Integer logkeep = 7;
    
    
    //状态正常和停用
    public static final Integer transform_status_normal = 0;
    public static final Integer transform_status_forbidden = 1;
    
    // 日志类型
    public static final Integer transform_log_user = 1;
    public static final Integer transform_log_role = 2;
    public static final Integer transform_log_group = 3;
    public static final Integer transform_log_menu = 4;
    public static final Integer transform_log_right = 5;
    
    //返回值
    public static final String success = "success";
    public static final String fail = "fail";

    // 公用页面
    public static final String transform_jsp_main = "/transform/admin/main";
    public static final String transform_jsp_login = "/transform/admin/login";
    public static final String transform_jsp_warn = "/public/warning";
    public static final String transform_jsp_noaccsess = "not_have_access";
    public static final String transform_jsp_error = "/views/common/error.jsp";

    // 业务页面
    public static final String transform_jsp_usermanage = "/transform/admin/system_user_manage";
    public static final String transform_jsp_useredit = "/transform/admin/system_user_edit";
    public static final String transform_jsp_userpassword = "/transform/admin/system_user_updatepassword";
    
    public static final String transform_jsp_rolemanage = "/transform/admin/system_role_manage";
    public static final String transform_jsp_roleedit = "/transform/admin/system_role_edit";
    public static final String transform_jsp_roleuser = "/transform/admin/system_role_relateuser";
    public static final String transform_jsp_roleright = "/transform/admin/system_role_relateright";
    public static final String transform_jsp_rolegroup = "/transform/admin/system_role_relategroup";
    public static final String transform_jsp_rolemenu = "/transform/admin/system_role_relatemenu";
    
    public static final String transform_jsp_groupmanage = "/transform/admin/system_group_manage";
    public static final String transform_jsp_groupedit = "/transform/admin/system_group_edit";
    public static final String transform_jsp_groupuser = "/transform/admin/system_group_relateuser";
    public static final String transform_jsp_grouprole = "/transform/admin/system_group_relaterole";
    
    public static final String transform_jsp_menumanage = "/transform/admin/system_menu_manage";
    public static final String transform_jsp_menuedit = "/transform/admin/system_menu_edit";
    public static final String transform_jsp_menuchildrenmanage = "/transform/admin/system_menuchildren_manage";
    public static final String transform_jsp_menuright = "/transform/admin/system_menu_relateright";
    
    public static final String transform_jsp_rightmanage = "/transform/admin/system_right_manage";
    public static final String transform_jsp_rightedit = "/transform/admin/system_right_edit";
    
    public static final String transform_jsp_logmanage = "/transform/admin/system_log_manage";
    public static final String transform_jsp_baseinfo = "/transform/admin/system_user_baseinfo";

}
