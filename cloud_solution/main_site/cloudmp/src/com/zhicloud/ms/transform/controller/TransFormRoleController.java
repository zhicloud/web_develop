
package com.zhicloud.ms.transform.controller;

import com.zhicloud.ms.common.util.json.JSONLibUtil;
import com.zhicloud.ms.constant.AppConstant;
import com.zhicloud.ms.service.IOperLogService;
import com.zhicloud.ms.service.IUserService;
import com.zhicloud.ms.service.ItenantService;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.constant.TransformConstant;
import com.zhicloud.ms.transform.service.ManSysRightService;
import com.zhicloud.ms.transform.service.ManSysRoleService;
import com.zhicloud.ms.transform.service.ManSysUserService;
import com.zhicloud.ms.transform.util.TransFormLoginHelper;
import com.zhicloud.ms.transform.util.TransFormLoginInfo;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.SysTenant;

import net.sf.json.JSONArray;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: TransFormRoleController
 * @Description: 角色配置控制层
 * @author 张本缘 于 2015年4月24日 上午9:13:36
 */
@Controller
public class TransFormRoleController extends TransFormBaseAction {
    public static final Logger logger = Logger.getLogger(TransFormRoleController.class);

    @Resource
    ManSysRoleService manSysRoleService;
    
    @Resource
    ManSysUserService manSysUserService;
    
    @Resource
    ManSysRightService manSysRightService;
    
    @Resource 
    IUserService userService;
    
     @Resource
    private IOperLogService operLogService;
    
    
     
	@Resource
	ItenantService tenantService;
    
    /* ============================用户=========================================================== */
    /**
     * @Description:用户界面跳转
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/transform/useradmin/index")
    public String userManage(Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("TransFormRoleController.userManage()");
        boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_user_query);
        if (!flag) {
            return TransformConstant.transform_jsp_noaccsess;
        }

        String param = request.getParameter("param");
        String statusStr = request.getParameter("status");
        String userTypeStr = request.getParameter("userType");
        Integer status,userType;

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
        if(StringUtil.isBlank(userTypeStr)){
            userType = null;
        } else{
            userType = Integer.valueOf(userTypeStr);
        }
        model.addAttribute("status", status);
        model.addAttribute("userType", userType);


        Map<String, Object> condition = new LinkedHashMap<String, Object>();
        condition.put("param", param);
        condition.put("status", status);
        condition.put("userType", userType);

        request.setAttribute("systemUserList", manSysUserService.getAll(condition));
        return TransformConstant.transform_jsp_usermanage;
    }
    
    /**
     * @Description:修改用户状态和权限
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/transform/useradmin/updateuserstatus")
    public void updateuserstatus(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("TransFormRoleController.updateuserstatus()");
        String billids = request.getParameter("billids");
        String statusflag = request.getParameter("statusflag");
        String value = request.getParameter("value");
        String result = "";
        boolean flag = false;
        if ("usb_status".equals(statusflag)) {
            flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_user_setusbstatus);
            if (flag) {
                TransFormLoginInfo login = this.getLoginInfo(request);
                result = manSysUserService.updateUserUSBStatus(billids, value, login);
            } else {
                result = "您没有操作权限!";
            }
        }
        if ("status".equals(statusflag)) {
            flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_user_setstatus);
            if (flag) {
                TransFormLoginInfo login = this.getLoginInfo(request);
                result = manSysUserService.updateUserStatus(billids, value, login);
            } else {
                result = "您没有操作权限!";
            }
        }

        if (TransformConstant.success.equals(result)) {
            operLogService.addLog("角色", "修改用户状态成功", "1", "1", request);
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("设置成功", true)));
        } else {
            operLogService.addLog("角色", "修改用户状态失败", "1", "2", request);
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply(result, false)));
        }

    }   
    
    
    /**
     * @Description:获取用户权限列表
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/transform/useradmin/getUserRoleGroup")
    public void getUserRoleGroup(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	String billids = request.getParameter("billids");
    	List<String> roleNames =userService.getSysRoleById(billids);
    	if (roleNames.size()>0){
    		StringBuffer sb = new StringBuffer("用户已授权的角色：");
    		for(String name : roleNames){
    			sb.append("【").append(name).append("】").append(" ");
    		}
    		printWriter(response, JSONLibUtil.toJSONString(toSuccessReply(sb.toString(), true)));
    	}else{
    		 printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("该用户还无任何角色授权!", true)));
    	}
    }   
    
    /**
     * @Description:修改用户信息界面跳转
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/transform/useradmin/beforeedit")
    public String beforAddUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("TransFormRoleController.beforAddUser()");
            boolean flag = false;
            String type = request.getParameter("type");
    		List<SysTenant> list = tenantService.getAllSysTenant(new SysTenant()); 
            request.setAttribute("tenantList", list);           
            if ("add".equals(type)) {
                request.setAttribute("modflag", "0");
                flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_user_add);
            } else {
                request.setAttribute("modflag", "1");
                String billid = request.getParameter("billid");
                request.setAttribute("systemUser", manSysUserService.getUserInfoByID(billid));
                flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_user_modify);
            }
            if (!flag) {
                return TransformConstant.transform_jsp_noaccsess;
            }
            return TransformConstant.transform_jsp_useredit;
    }
    
    /**
     * @Description:新增用户信息
     * @param systemUserVO
     * @return MethodResult
     */
    @RequestMapping(value = "/transform/useradmin/saveuser")
    public void addSysUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("utf-8");
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("usercount", request.getParameter("usercount"));
        data.put("email", request.getParameter("email"));
        data.put("telphone", request.getParameter("telphone"));
        data.put("status", request.getParameter("status"));
        data.put("displayname", request.getParameter("displayname"));       
        data.put("userType", request.getParameter("userType"));
        
        String result = "";
        String printresult = "";
        TransFormLoginInfo login = this.getLoginInfo(request);
        // 判断是修改还是新增
        if ("1".equals(request.getParameter("modflag"))) {
            boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_user_modify);
            if (flag) {
                data.put("billid", request.getParameter("billid"));
                result = manSysUserService.modSysUser(data, login);
                printresult = "修改";
            } else {
                result = "您没有操作权限!";
            }

        } else {
            boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_user_add);
            if (flag) {
                data.put("password", request.getParameter("password"));
                try {
                    result = manSysUserService.addSysUser(data, login);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                printresult = "新增";
            } else {
                result = "您没有操作权限!";
            }

        }
        if (TransformConstant.success.equals(result)) {
            operLogService.addLog("管理员用户", "新增用户成功", "1", "1", request);
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply(printresult + "成功", true)));
        } else {
            operLogService.addLog("管理员用户", "新增用户失败", "1", "2", request);
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply(result, false)));
        }
    }

    
    /**
     * @Description:删除用户信息
     * @param systemUserVO
     * @return MethodResult
     */
    @RequestMapping(value = "/transform/useradmin/deleteuser")
    public void deleteSysUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String billids = request.getParameter("billids");
        String result = "";
        boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_user_delete);
        if (flag) {
            TransFormLoginInfo login = this.getLoginInfo(request);
            result = manSysUserService.updateUserStatus(billids, AppConstant.USER_STATUS_DELETE.toString(), login);
//            result = manSysUserService.deleteSysUser(billids, login);
        } else {
            result = "您没有操作权限!";
        }

        if (TransformConstant.success.equals(result)) {
            operLogService.addLog("管理员用户", "删除用户成功", "1", "1", request);
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("删除成功", true)));
        } else {
            operLogService.addLog("管理员用户", "删除用户失败", "1", "2", request);
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply(result, false)));
        }

    }

    /**
     * @Description:重置用户密码
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/transform/useradmin/resetpassword")
    public void resetPassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String billid = request.getParameter("billid");
        String result = "";
        boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_user_resetpassword);
        if (flag) {
            TransFormLoginInfo login = this.getLoginInfo(request);
            result = manSysUserService.resetPassword(billid, login);
        } else {
            result = "您没有操作权限";
        }
        if (TransformConstant.success.equals(result)) {
            operLogService.addLog("管理员用户", "重置用户密码成功", "1", "1", request);
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("密码重置成功,新密码已发送到邮箱", true)));
        } else {
            operLogService.addLog("管理员用户", "重置用户密码失败", "1", "2", request);
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply(result, false)));
        }

    }
    
    /**
     * @Description:修改用户密码界面跳转
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/transform/updatepass/beforeupdatepassword")
    public String beforUpdatePassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("TransFormRoleController.beforUpdatePassword()");
        boolean flag = false;
        flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_user_changepassword);
        if (!flag) {
            return TransformConstant.transform_jsp_noaccsess;
        }
        return TransformConstant.transform_jsp_userpassword;
    }
    
    /**
     * @Description:修改用户基本信息界面跳转
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/transform/baseinfo/beforeupdateuser")
    public String beforUpdateUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("TransFormRoleController.beforUpdateUser()");
        boolean flag = false;
        flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_admin_moduser);
        if (!flag) {
            return TransformConstant.transform_jsp_noaccsess;
        }
        
        TransFormLoginInfo login = TransFormLoginHelper.getLoginInfo(request);
        request.setAttribute("userbaseinfo", manSysUserService.getUserInfoByID(login.getBillid()));
        return TransformConstant.transform_jsp_baseinfo;
    }    
    
    /**
     * @Description:修改密码信息
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/transform/admin/updatepassword")
    public void updatePassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
        TransFormLoginInfo login = TransFormLoginHelper.getLoginInfo(request);
        String newpassword = request.getParameter("newpassword");
        String oldpassword = request.getParameter("oldpassword");
        try {
            boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_admin_changepass);
            if (flag) {
                if (TransformConstant.transform_billid_admin.equals(login.getBillid())) {
                    printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("超级管理员密码不能修改", false)));
                } else {
                    String result = manSysUserService.updatePassword(login.getBillid(), newpassword, oldpassword);
                    if (TransformConstant.success.equals(result)) {
                        // 修改密码成功以后注销一次
                        operLogService.addLog("管理员用户", "修改用户密码成功", "1", "1", request);
                        manSysUserService.logout(login.getBillid());
                        printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("修改成功", true)));
                    } else {
                        operLogService.addLog("管理员用户", "修改用户密码失败", "1", "2", request);
                        printWriter(response, JSONLibUtil.toJSONString(toSuccessReply(result, false)));
                    }
                }
            } else {
                printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("您没有操作权限", false)));
            }

        } catch (Exception e) {
            e.printStackTrace();
            operLogService.addLog("管理员用户", "修改用户信息失败", "1", "2", request);
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("保存失败,请联系管理员", false)));
        }
    }
    
    /**
     * @Description:修改用户信息
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/transform/updateuserinfo")
    public void updateUserInfo(HttpServletRequest request, HttpServletResponse response) throws Exception {
/*        String sessionId = request.getParameter(TransformConstant.transform_session_admin);
        String email = request.getParameter("email");
        String telphone = request.getParameter("telphone");
        try {
            boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_user_modify);
            if (flag) {
                TransFormLoginInfo loginInfo = TransFormLoginHelper.getLoginInfo(sessionId);
                Map<String, Object> data = new LinkedHashMap<String, Object>();
                data.put("email", email);
                data.put("telphone", telphone);
                String result = manSysUserService.modSysUser(data, loginInfo);
                if (TransformConstant.success.equals(result)) {
                    printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("修改成功", true)));
                } else {
                    printWriter(response, JSONLibUtil.toJSONString(toSuccessReply(result, false)));
                }
            } else {
                printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("您没有操作权限", false)));
            }

        } catch (Exception e) {
            e.printStackTrace();
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("保存失败,请联系管理员", false)));
        }*/
    }   
    
    
     /* ===================================角色==============================================================================*/ 
     
    /**
     * @Description:角色界面跳转
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/transform/roleadmin/index")
    public String roleManage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("TransFormRoleController.roleManage()");
        boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_role_query);
        if (!flag) {
            return TransformConstant.transform_jsp_noaccsess;
        }
        request.setAttribute("systemRoleList", manSysRoleService.getAllRole());
        return TransformConstant.transform_jsp_rolemanage;
    }
    /**
     * @Description:修改角色信息界面跳转
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/transform/roleadmin/beforeedit")
    public String beforAddRole(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("TransFormRoleController.beforAddRole()");
        boolean flag = false;
        String type = request.getParameter("type");
        if ("add".equals(type)) {
            request.setAttribute("modflag", "0");
            flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_role_add);
        } else {
            request.setAttribute("modflag", "1");
            String billid = request.getParameter("billid");
            request.setAttribute("systemRole", manSysRoleService.getRoleByID(billid));
            flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_role_modify);
        }
        if (!flag) {
            return TransformConstant.transform_jsp_noaccsess;
        }
        return TransformConstant.transform_jsp_roleedit;
    }

    /**
     * @Description:修改角色和用户关联信息界面跳转
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/transform/roleadmin/beforerelateuser")
    public String beforRelateRoleAndUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("TransFormRoleController.beforRelateRoleAndUser()");
        String billid = request.getParameter("billid");
        request.setAttribute("roleid", billid);
        request.setAttribute("existsUsers", manSysUserService.getUserInRoleID(billid));
        request.setAttribute("notexistsUsers", manSysUserService.getUserOutRoleID(billid));
        return TransformConstant.transform_jsp_roleuser;
    }
    
    /**
     * @Description:修改角色组和用户关联信息界面跳转
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/transform/groupadmin/beforerelateuser")
    public String beforRelateGroupAndUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("TransFormRoleController.beforRelateGroupAndUser()");

        String billid = request.getParameter("billid");
        request.setAttribute("roleid", billid);
        request.setAttribute("existsUsers", manSysUserService.getUserInGroupID(billid));
        request.setAttribute("notexistsUsers", manSysUserService.getUserOutGroupID(billid));

        return TransformConstant.transform_jsp_groupuser;
    }   
    
    /**
     * @Description:修改角色组和角色关联信息界面跳转
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/transform/groupadmin/beforerelaterole")
    public String beforRelateGroupAndRole(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("TransFormRoleController.beforRelateGroupAndRole()");

        String billid = request.getParameter("billid");
        request.setAttribute("roleid", billid);
        request.setAttribute("roleList", manSysRoleService.queryRoleInGroupID(billid));

        return TransformConstant.transform_jsp_grouprole;
    }     
    
    /**
     * @Description:修改角色权限信息界面跳转
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/transform/roleadmin/beforerelateright")
    public String beforRelateRoleAndRight(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("TransFormRoleController.beforRelateRoleAndRight()");
        String billid = request.getParameter("billid");
        request.setAttribute("roleid", billid);
        return TransformConstant.transform_jsp_roleright;
    }

    /**
     * @Description:修改角色组关联信息界面跳转
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/transform/roleadmin/beforerelategroup")
    public String beforRelateRoleAndGroup(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("TransFormRoleController.beforRelateGroup()");
        String billid = request.getParameter("billid");
        request.setAttribute("roleid", billid);
        request.setAttribute("groupList", manSysRoleService.queryGroupInRoleID(billid));
        return TransformConstant.transform_jsp_rolegroup;
    }    

    /**
     * @Description:修改角色关联菜单信息界面跳转
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/transform/roleadmin/beforerelatemenu")
    public String beforRelateRoleAndMenu(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("TransFormRoleController.beforRelateRoleAndMenu()");
        String billid = request.getParameter("billid");
        request.setAttribute("roleid", billid);
        return TransformConstant.transform_jsp_rolemenu;
    }   
    
    /**
     * @Description:新增角色信息
     * @param systemUserVO
     * @return MethodResult
     */
    @RequestMapping(value = "/transform/roleadmin/saverole")
    public void addSysRole(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("utf-8");
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("name", request.getParameter("name"));
        data.put("code", request.getParameter("code"));
        String result = "";
        String printresult = "";
        TransFormLoginInfo login = this.getLoginInfo(request);
        // 判断是修改还是新增
        if ("1".equals(request.getParameter("modflag"))) {
            boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_role_modify);
            if (flag) {
                data.put("billid", request.getParameter("billid"));
                result = manSysRoleService.modSysRole(data, login);
                printresult = "修改";
            } else {
                result = "您没有操作权限";
            }

        } else {
            boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_role_add);
            if (flag) {
                result = manSysRoleService.addSysRole(data, login);
                printresult = "新增";
            } else {
                result = "您没有操作权限";
            }

        }
        if (TransformConstant.success.equals(result)) {
            operLogService.addLog("角色", "新增角色信息成功", "1", "1", request);
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply(printresult + "成功", true)));
        } else {
            operLogService.addLog("角色", "新增角色信息失败", "1", "2", request);
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply(result, false)));
        }
    }

    /**
     * @Description:删除角色信息
     * @param systemUserVO
     * @return MethodResult
     */
    @RequestMapping(value = "/transform/roleadmin/deleterole")
    public void deleteSysRole(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String billids = request.getParameter("billids");
        String result = "";
        boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_role_delete);
        if (flag) {
            TransFormLoginInfo login = this.getLoginInfo(request);
            result = manSysRoleService.deleteSysRole(billids, login);
        } else {
            result = "您没有操作权限";
        }

        if (TransformConstant.success.equals(result)) {
            operLogService.addLog("角色", "删除角色信息成功", "1", "1", request);
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("删除成功", true)));
        } else {
            operLogService.addLog("角色", "删除角色信息失败", "1", "2", request);
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply(result, false)));
        }
    }

    /**
     * @Description:查询角色拥有的用户信息
     * @param systemUserVO
     * @return MethodResult
     */
    @RequestMapping(value = "/transform/queryUserByRole")
    public void queryUserByRole(HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_role_setright);
        if (flag) {
            String billid = request.getParameter("billid");
            Map<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("alreadyexists", manSysUserService.getUserInRoleID(billid));
            map.put("notexists", manSysUserService.getUserOutRoleID(billid));
            map.put("status", TransformConstant.success);
            printWriter(response, JSONLibUtil.toJSONString(map));
        } else {

            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("您没有操作权限", false)));
        }

    }

    /**
     * @Description:查询角色组拥有的用户信息
     * @param systemUserVO
     * @return MethodResult
     */
    @RequestMapping(value = "/transform/queryUserByGroup")
    public void queryUserByGroup(HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_group_setright);
        if (flag) {
            String billid = request.getParameter("billid");
            Map<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("alreadyexists", manSysUserService.getUserInGroupID(billid));
            map.put("notexists", manSysUserService.getUserOutGroupID(billid));
            map.put("status", TransformConstant.success);
            printWriter(response, JSONLibUtil.toJSONString(map));
        } else {
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("您没有操作权限", false)));
        }

    }

    /**
     * @Description:保存菜单和权限关联关系
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/transform/roleadmin/saveroleright")
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveRoleRight(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String rightids = request.getParameter("rightids");
        String roleid = request.getParameter("roleid");
        try {
            boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_role_setright);
            if (flag) {
                manSysRoleService.deleteRoleRight(roleid);
                manSysRoleService.saveRoleRight(rightids.split(","), roleid);
                operLogService.addLog("角色", "设置角色权限成功", "1", "1", request);
                printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("保存成功", true)));
            } else {
                printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("您没有操作权限", false)));
            }

        } catch (Exception e) {
            e.printStackTrace();
            operLogService.addLog("角色", "设置角色权限失败", "1", "2", request);
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("保存失败,请联系管理员", false)));
        }
    }

    /**
     * @Description:根据roleID查询权限的关联信息
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/transform/roleadmin/queryroleright")
    public void roleRightManage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_role_setright);
        if (flag) {
            String roleid = request.getParameter("roleid");
            Map<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("rightList", manSysRightService.getRightAndRole(roleid));
            map.put("status", TransformConstant.success);
            printWriter(response, JSONLibUtil.toJSONString(map));
        } else {
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("您没有操作权限", false)));
        }

    }

    /**
     * @Description:获取菜单权限树
     * @param request
     * @param response
     * @throws Exception7
     */
    @RequestMapping("/transform/roleadmin/getMenuTree")
    public void getMenuTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_role_setright);
        if (flag) {
            String roleid = request.getParameter("roleid");
            printWriter(response, JSONArray.fromObject(manSysRoleService.buildTreeJSON("all", new JSONArray(), roleid))
                    .toString());
        } else {
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("您没有操作权限", false)));
        }

    }
    
    /**
     * @Description:获取菜单权限树
     * @param request
     * @param response
     * @throws Exception7
     */
    @RequestMapping("/transform/roleadmin/getMenuTreewithRight")
    public void getMenuTreeWithRight(HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_role_setright);
        if (flag) {
            String roleid = request.getParameter("roleid");
            printWriter(response,
                    JSONArray.fromObject(manSysRoleService.buildTreeJSONWithRight("all", new JSONArray(), roleid))
                            .toString());
        } else {
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("您没有操作权限", false)));
        }

    }   

    /**
     * @Description:保存菜单和权限关联关系
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/transform/roleadmin/saverolemenu")
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveRoleMenu(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String menuids = request.getParameter("menuids");
        String roleid = request.getParameter("roleid");
        try {
            boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_role_setright);
            if (flag) {
                manSysRoleService.deleteRoleMenu(roleid);
                manSysRoleService.saveRoleMenu(menuids.split(","), roleid);
                operLogService.addLog("角色", "设置角色菜单权限成功", "1", "1", request);
                printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("保存成功", true)));
            } else {
                printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("您没有操作权限", false)));
            }

        } catch (Exception e) {
            e.printStackTrace();
            operLogService.addLog("角色", "设置角色菜单权限失败", "1", "2", request);
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("保存失败,请联系管理员", false)));
        }
    }

    
     /* ==============================角色组====================================================================================*/
     

    /**
     * @Description:角色组界面跳转
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/transform/groupadmin/index")
    public String groupManage(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("TransFormRoleController.groupManage()");
        boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_group_query);
        if (!flag) {
            return TransformConstant.transform_jsp_noaccsess;
        }
        request.setAttribute("systemGroupList", manSysRoleService.getAllGroup());
        return TransformConstant.transform_jsp_groupmanage;
    }
    
    /**
     * @Description:修改角色信息界面跳转
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/transform/groupadmin/beforeedit")
    public String beforAddGroup(HttpServletRequest request, HttpServletResponse response) throws Exception {
        logger.debug("TransFormRoleController.beforAddGroup()");
        boolean flag = false;
        String type = request.getParameter("type");
        if ("add".equals(type)) {
            request.setAttribute("modflag", "0");
            flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_group_add);
        } else {
            request.setAttribute("modflag", "1");
            String billid = request.getParameter("billid");
            request.setAttribute("systemGroup", manSysRoleService.getGroupByID(billid));
            flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_group_modify);
        }
        if (!flag) {
            return TransformConstant.transform_jsp_noaccsess;
        }
        return TransformConstant.transform_jsp_groupedit;
    }
    
    /**
     * @Description:新增角色组信息
     * @param systemUserVO
     * @return MethodResult
     */
    @RequestMapping(value = "/transform/groupadmin/savegroup")
    public void addSysGroup(HttpServletRequest request, HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("utf-8");
        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("name", request.getParameter("name"));
        String result = "";
        String printresult = "";
        TransFormLoginInfo login = this.getLoginInfo(request);
        // 判断是修改还是新增
        if ("1".equals(request.getParameter("modflag"))) {
            boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_group_add);
            if (flag) {
                data.put("billid", request.getParameter("billid"));
                result = manSysRoleService.modSysGroup(data, login);
                printresult = "修改";
            } else {
                result = "您没有操作权限";
            }

        } else {
            boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_group_modify);
            if (flag) {
                result = manSysRoleService.addSysGroup(data, login);
                printresult = "添加";
            } else {
                result = "您没有操作权限";
            }
        }
        if (TransformConstant.success.equals(result)) {
            operLogService.addLog("角色组", "新增权限组成功", "1", "1", request);
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply(printresult + "成功", true)));
        } else {
            operLogService.addLog("角色组", "新增权限组失败", "1", "2", request);
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply(result, false)));
        }
    }

    /**
     * @Description:删除角色组信息
     * @param systemUserVO
     * @return MethodResult
     */
    @RequestMapping(value = "/transform/groupadmin/deletegroup")
    public void deleteSysGroup(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String result = "";
        boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_role_delete);
        if (flag) {
            TransFormLoginInfo login = this.getLoginInfo(request);
            String billids = request.getParameter("billids");
            result = manSysRoleService.deleteSysGroup(billids, login);
        } else {
            result = "您没有操作权限";
        }
        if (TransformConstant.success.equals(result)) {
            operLogService.addLog("角色组", "删除权限组失败", "1", "1", request);
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("删除成功", true)));
        } else {
            operLogService.addLog("角色组", "删除权限组失败", "1", "2", request);
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply(result, false)));
        }
    }

    /**
     * @Description:保存用户和角色关联信息
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/transform/roleadmin/saveuserrole")
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveUserRole(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String userids = request.getParameter("userids");
        String roleid = request.getParameter("roleid");
        try {
            boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_role_setright);
            if (flag) {
                manSysRoleService.deleteUserRole(roleid);
                manSysRoleService.saveUserRole(userids.split(","), roleid);
                operLogService.addLog("角色组", "删除权限组成功", "1", "1", request);
                printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("保存成功", true)));
            } else {
                printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("您没有操作权限", false)));
            }

        } catch (Exception e) {
            e.printStackTrace();
            operLogService.addLog("角色组", "删除权限组失败", "1", "2", request);
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("保存失败,请联系管理员", false)));
        }
    }

    /**
     * @Description:查询角色组拥有的角色信息
     * @param systemUserVO
     * @return MethodResult
     */
    @RequestMapping(value = "/transform/queryGroupByRole")
    public void queryGroupByRole(HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_role_setright);
        if (flag) {
            String billid = request.getParameter("billid");
            Map<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("groupinfo", manSysRoleService.queryGroupInRoleID(billid));
            map.put("status", TransformConstant.success);
            printWriter(response, JSONLibUtil.toJSONString(map));
        } else {
             printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("您没有操作权限", false)));
        }

    }

    /**
     * @Description:保存角色组和角色关联信息
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/transform/roleadmin/saverolegroup")
    public void saveRoleGroup(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String groupids = request.getParameter("groupids");
        String roleid = request.getParameter("roleid");
        try {
            manSysRoleService.deleteRoleGroup(roleid);
            manSysRoleService.saveRoleGroup(groupids.split(","), roleid);
            operLogService.addLog("角色组", "更新角色组角色成功", "1", "1", request);
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("保存成功", true)));
        } catch (Exception e) {
            e.printStackTrace();
            operLogService.addLog("角色组", "更新角色组角色失败", "1", "2", request);
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("保存失败,请联系管理员", false)));
        }
    }

    /**
     * @Description:保存用户和角色关联信息
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/transform/groupadmin/saveusergroup")
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveUserGroup(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String userids = request.getParameter("userids");
        String groupid = request.getParameter("groupid");
        try {
            boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_group_setright);
            if (flag) {
                manSysRoleService.deleteUserGroup(groupid);
                manSysRoleService.saveUserGroup(userids.split(","), groupid);
                operLogService.addLog("角色组", "更新角色组用户成功", "1", "1", request);
                printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("保存成功", true)));
            } else {
                printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("您没有操作权限", true)));
            }

        } catch (Exception e) {
            e.printStackTrace();
            operLogService.addLog("角色组", "更新角色组用户失败", "1", "2", request);
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("保存失败,请联系管理员", false)));
        }
    }

    /**
     * @Description:查询角色组拥有的角色信息
     * @param systemUserVO
     * @return MethodResult
     */
    @RequestMapping(value = "/transform/queryRoleByGroup")
    public void queryRoleByGroup(HttpServletRequest request, HttpServletResponse response) throws Exception {
        boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_group_setright);
        if (flag) {
            String billid = request.getParameter("billid");
            Map<String, Object> map = new LinkedHashMap<String, Object>();
            map.put("roleinfo", manSysRoleService.queryRoleInGroupID(billid));
            map.put("status", TransformConstant.success);
            printWriter(response, JSONLibUtil.toJSONString(map));
        } else {
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("您没有操作权限", true)));
        }

    }

    /**
     * @Description:保存角色组和角色关联信息
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/transform/groupadmin/savegrouprole")
    @Transactional(propagation = Propagation.REQUIRED)
    public void saveGroupRole(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String groupid = request.getParameter("groupid");
        String roleids = request.getParameter("roleids");
        try {
            boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_group_setright);
            if (flag) {
                manSysRoleService.deleteGroupRole(groupid);
                manSysRoleService.saveGroupRole(roleids.split(","), groupid);
                operLogService.addLog("角色组", "更新角色组和角色关系成功", "1", "1", request);
                printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("保存成功", true)));
            } else {
                printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("您没有操作权限", true)));
            }

        } catch (Exception e) {
            e.printStackTrace();
            operLogService.addLog("角色组", "更新角色组和角色关系失败", "1", "2", request);
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("保存失败,请联系管理员", false)));
        }
    }
    
    
    /**
     * @Description:修改密码信息
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping(value = "/transform/admin/manualpassword")
    public void manualPassword(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String billid = request.getParameter("billid");
        String manualpass = request.getParameter("manualpass");
        String email = request.getParameter("email");
        String result = "";
        boolean flag = isHasPrivilege(request, TransFormPrivilegeConstant.transform_user_resetpassword);
        if (flag) {
            result = manSysUserService.manualPassword(billid, manualpass, email);
        } else {
            result = "您没有操作权限";
        }
        if (TransformConstant.success.equals(result)) {
            operLogService.addLog("管理员用户", "手动重置用户密码成功", "1", "1", request);
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply("手动重置用户密码成功", true)));
        } else {
            operLogService.addLog("管理员用户", "手动重置用户密码失败", "1", "2", request);
            printWriter(response, JSONLibUtil.toJSONString(toSuccessReply(result, false)));
        }
    }

}
