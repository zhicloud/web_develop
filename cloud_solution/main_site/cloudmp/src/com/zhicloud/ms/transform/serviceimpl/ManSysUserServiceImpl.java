
package com.zhicloud.ms.transform.serviceimpl;

import com.zhicloud.ms.app.propeties.AppProperties;
import com.zhicloud.ms.common.util.RandomPassword;
import com.zhicloud.ms.common.util.StringUtil;
import com.zhicloud.ms.common.util.constant.MailConstant;
import com.zhicloud.ms.constant.AppConstant;
import com.zhicloud.ms.constant.AppInconstant;
import com.zhicloud.ms.exception.AppException;
import com.zhicloud.ms.mapper.DictionaryMapper;
import com.zhicloud.ms.mapper.SysUserMapper;
import com.zhicloud.ms.mapper.TerminalUserMapper;
import com.zhicloud.ms.message.MessageServiceManager;
import com.zhicloud.ms.message.email.EmailSendService;
import com.zhicloud.ms.message.email.EmailTemplateConstant;
import com.zhicloud.ms.message.util.MessageAsyncHelper;
import com.zhicloud.ms.message.util.MessageConstant;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.IOperLogService;
import com.zhicloud.ms.transform.constant.TransformConstant;
import com.zhicloud.ms.transform.mapper.*;
import com.zhicloud.ms.transform.service.ManSysUserService;
import com.zhicloud.ms.transform.util.TransFormLoginHelper;
import com.zhicloud.ms.transform.util.TransFormLoginInfo;
import com.zhicloud.ms.transform.vo.ManSystemMenuVO;
import com.zhicloud.ms.transform.vo.ManSystemRightVO;
import com.zhicloud.ms.transform.vo.ManSystemRoleVO;
import com.zhicloud.ms.transform.vo.ManSystemUserVO;
import com.zhicloud.ms.util.MD5;
import com.zhicloud.ms.vo.DictionaryVO;
import com.zhicloud.ms.vo.OperLogVO;
import com.zhicloud.ms.vo.SysUser;
import com.zhicloud.ms.vo.TerminalUserVO;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @ClassName: SysUserServiceImpl
 * @Description: 用户管理实现
 * @author 张本缘 于 2015年4月23日 下午3:47:23
 */
@Transactional(readOnly = true)
public class ManSysUserServiceImpl implements ManSysUserService {
    /**
     * 日志
     */
    public static final Logger logger = Logger.getLogger(ManSysUserServiceImpl.class);

    private SqlSession sqlSession;
    
    @Resource
    private IOperLogService operLogService;

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * @Description:sqlSession注入
     * @param sqlSession
     */
    public void setSqlSession(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    /**
     * @Description:拼装结果集
     * @param obj
     * @return
     */
    public Map<String, Object> toSuccessReply(Object obj, boolean flag) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        if (flag) {
            map.put("status", TransformConstant.success);
        } else {
            map.put("status", TransformConstant.fail);
        }
        map.put("result", obj);
        return map;
    }

    /**
     * 用户登录
     * 
     * @param parameter
     * @return MethodResult
     */
    @Transactional(readOnly = false)
    public Map<String, Object> login(Map<String, String> parameter) {
        logger.debug("SysUserServiceImpl.login()");
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        //HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();        
        String usercount = StringUtil.trim(parameter.get("usercount"));
        String password = StringUtil.trim(parameter.get("password"));
        OperLogVO operLog = new OperLogVO();
        operLog.setContent("用户"+usercount+"登录");
        operLog.setLevel(1);
        operLog.setStatus(2);
        operLog.setModule("用户管理");
        // String verificationCode = StringUtil.trim(parameter.get("verification_code"));
        // 数据完整性判断
        if (usercount.isEmpty()) {
            return toSuccessReply("用户名不能为空", false);
        }
        if (password.isEmpty()) {
            return toSuccessReply("密码不能为空", false);
        }

        // 从数据库查询用户信息
        ManSystemUserMapper manSystemUserMapper = this.sqlSession.getMapper(ManSystemUserMapper.class);

        Map<String, Object> condition = new LinkedHashMap<String, Object>();
        condition.put("usercount", usercount);
        condition.put("password", TransFormLoginHelper.md5(AppConstant.PASSWORD_MD5_STR, password));
        ManSystemUserVO manSystemUserVO = manSystemUserMapper.getUserByCondition(condition);
        MethodResult result = new MethodResult(MethodResult.SUCCESS, "登陆成功");
        if (TransformConstant.transform_username_admin.equals(usercount)
                && TransformConstant.transform_password_admin.equals(password)) {
             //验证该用户ID，是否已经登录。当前用户比较已登录到系统的静态变量中的值，是否存在。  
             Boolean hasLogin = TransFormLoginHelper.isRepeatLogin(TransformConstant.transform_billid_admin, request);
             if(hasLogin){
                 operLog.setContent("超级管理员["+usercount+"]登录失败-管理员已登录");
                 operLog.setStatus(2);
                 return toSuccessReply("用户已经登录", false);
             }
            // 管理员登录
            setTransFormUserInfo(request);
            result.put(TransformConstant.transform_session_admin, TransformConstant.transform_billid_admin);
            request.getSession().setAttribute("displayname", "超级管理员");
            operLog.setContent("超级管理员["+usercount+"]登录成功");
            operLog.setStatus(1);
        } else {
            if (manSystemUserVO == null) {
                //管理员登录不成功，尝试终端用户登录
                Integer type = AppConstant.SYS_USER_TYPE_TERMINAL_USER;
                SysUserMapper userMapper = this.sqlSession.getMapper(SysUserMapper.class);
                Map<String,Object> userCondition = new LinkedHashMap<String,Object>();
                userCondition.put("username", usercount);
                userCondition.put("password",  MD5.md5(usercount, password)); 
                userCondition.put("type", type);
                //返回查询结果
                SysUser  sysUser = userMapper.checkLogin(userCondition);
                
                //未查询到用户信息，返回密码错误
                if(sysUser == null ){
                    //如果用户名登录查不到结果，我们认为是使用别名登录
                    //通过别名查询用户
                    sysUser = userMapper.queryUserByAlias(usercount);
                    //如果用户存在，取出用户名再和密码进行查询
                    if(sysUser!=null){
                        //String name = sysUser.getUsername();
                        Map<String,Object> myCondition = new LinkedHashMap<String,Object>();
                        myCondition.put("username", usercount);
                        myCondition.put("password", MD5.md5(usercount, password)); 
                        myCondition.put("type", type);
                        //返回结果
                        sysUser = userMapper.checkLogin(condition);
                        if(sysUser==null){
                            return toSuccessReply("用户名或密码错误", false);
                        }else{
                            //查询用户详细信息
                            TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
                            TerminalUserVO terminalUser = terminalUserMapper.getById(sysUser.getId());
                            //用户已被禁用
                            if(terminalUser.getStatus() == 1){
                                return toSuccessReply("该账户已经被停用", false);
                            }else{
                                //验证该用户ID，是否已经登录。当前用户比较已登录到系统的静态变量中的值，是否存在。  
                                Boolean hasLogin = TransFormLoginHelper.isRepeatLogin(terminalUser.getId(), request);
                                if(hasLogin){
                                    return toSuccessReply("用户已经登录", false);
                                }else{
                                    // 设置菜单权限（终端用户登录无需设置功能权限）
                                    this.setTerminalUserInfo(request, terminalUser);
                                    result.put(TransformConstant.transform_session_admin, terminalUser.getId());
                                    request.getSession().setAttribute("displayname", terminalUser.getName());
//                                  return toSuccessReply(result, true);
                                    return toSuccessReply("终端用户已经登录成功了", false);
                                }
                            }
                        }
                    }else{
                        return toSuccessReply("用户名或密码错误", false);
                    }
                }else{
                    //查询用户详细信息
                    TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
                    TerminalUserVO terminalUser = terminalUserMapper.getById(sysUser.getId());
                    //用户已被禁用
                    if(terminalUser.getStatus() == 1){
                        return toSuccessReply("该账户已经被停用", false);
                    }else{
                        //验证该用户ID，是否已经登录。当前用户比较已登录到系统的静态变量中的值，是否存在。  
                        Boolean hasLogin = TransFormLoginHelper.isRepeatLogin(terminalUser.getId(), request);
                        if(hasLogin){
                            return toSuccessReply("用户已经登录", false);
                        }else{
                            // 设置菜单权限（终端用户登录无需设置功能权限）
                            this.setTerminalUserInfo(request, terminalUser);
                            result.put(TransformConstant.transform_session_admin, terminalUser.getId());
                            request.getSession().setAttribute("displayname", terminalUser.getName());
//                          return toSuccessReply(result, true);
                            return toSuccessReply("终端用户已经登录成功了", false);

                        }
                    }
                    
                }
                
            }
            // 判断用户是否被禁用
            if (manSystemUserVO.getStatus() == TransformConstant.transform_status_forbidden) {
                return toSuccessReply("该账户已被停用", false);
            }
            
            //验证该用户ID，是否已经登录。当前用户比较已登录到系统的静态变量中的值，是否存在。  
            Boolean hasLogin = TransFormLoginHelper.isRepeatLogin(manSystemUserVO.getBillid(), request);
            if(hasLogin){
                //如果用户已经登录，挤掉原来的用户
                TransFormLoginHelper.removeSessionMap(manSystemUserVO.getBillid());
                //return toSuccessReply("用户已经登录", false);
            }
            operLog.setStatus(1);
            setTransFormUserInfo(request,manSystemUserVO);
            result.put(TransformConstant.transform_session_admin, manSystemUserVO.getBillid());
            request.getSession().setAttribute("displayname", manSystemUserVO.getDisplayname());
        }
        operLogService.addLog(operLog, request);
        return toSuccessReply(result, true);
    }

    /**
     * 注销
     * @return
     * @see com.zhicloud.ms.transform.service.ManSysUserService#logout(java.lang.String)
     */
    @Transactional(readOnly = false)
    public Map<String, Object> logout(String sessionID) {
        logger.debug("ManSysUserServiceImpl.logout()");
        TransFormLoginHelper.removeSessionMap(sessionID);
        return toSuccessReply("注销成功", true);
    }

    /**
     * Description:取得所有用户信息
     * @return
     */
    public List<ManSystemUserVO> getAll() {
        ManSystemUserMapper systemUserMapper = this.sqlSession.getMapper(ManSystemUserMapper.class);
        return systemUserMapper.getAll();
    }

    /**
     * @Description:根据多个条件得到所有用户
     * @author  张翔
     * @return
     */
    @Override public List<ManSystemUserVO> getAll(Map<String, Object> condition) {
        return this.sqlSession.getMapper(ManSystemUserMapper.class).queryAllWithConditions(
            condition);
    }

    /**
     * @Description:新增系统用户
     * @param parameter 参数信息
     * @return MethodResult
     */
    @Transactional(readOnly = false)
    public String addSysUser(Map<String, Object> parameter, TransFormLoginInfo login) {
        logger.debug("SysUserServiceImpl.addSysUser()");
        try {
            String usercount = StringUtil.trim(parameter.get("usercount"));
            String email = StringUtil.trim(parameter.get("email"));
            String telphone = StringUtil.trim(parameter.get("telphone"));
            String insert_date = StringUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss");
            String status = StringUtil.trim(parameter.get("status"));
            String usb_status = StringUtil.trim(parameter.get("usb_status"));
            String password = StringUtil.trim(parameter.get("password"));
            String displayname = StringUtil.trim(parameter.get("displayname"));
            String userType =  StringUtil.trim(parameter.get("userType"));
            //String user_type = "1";
            
            if (TransformConstant.transform_username_admin.equals(usercount)) {
                return "用户名或邮箱已经存在";
            }
            // 生成主键信息
            //String password = RandomPassword.getRandomNum(MailConstant.PASSWORD_LENGTH);
            ManSystemUserMapper systemUserMapper = this.sqlSession.getMapper(ManSystemUserMapper.class);
            // 判断账户是否已经存在
            LinkedHashMap<String, Object> condition = new LinkedHashMap<String, Object>();
            condition.put("usercount", usercount);
            condition.put("email", email);
            condition.put("telphone", telphone);
            condition.put("password", TransFormLoginHelper.md5(AppConstant.PASSWORD_MD5_STR, password));
            condition.put("status", status);
            condition.put("usb_status", usb_status);
            condition.put("displayname", displayname);
            condition.put("userType", userType);
             // 验证该账号和邮箱是否已经存在
            ManSystemUserVO systemUserVO = systemUserMapper.validateUserIsExists(condition);
            if (systemUserVO != null) {
                return "用户名或邮箱已经存在";
            }
            //查询终端用户是否使用过这个用户名
            SysUserMapper userMapper = this.sqlSession.getMapper(SysUserMapper.class);
            SysUser terminalUser = userMapper.queryUserByUsername(usercount);
            if(terminalUser != null){
                return "用户名或邮箱已经被终端用户注册 ";
            }
            String billid = StringUtil.generateUUID();
            condition.put("billid", billid);
            condition.put("insert_date", insert_date);
            // 取得当前操作的用户信息
            /*
             * HttpServletRequest request = RequestContext.getHttpRequest(); LoginInfo loginInfo =
             * LoginHelper.getLoginInfo(request);
             */
            if(login != null){ 
                condition.put("insert_user", login.getBillid());
            }
            // 添加用户数据
            int n = systemUserMapper.addSystemUser(condition);
            int m = 1;
            // 添加日志信息
            if(login != null){ 
                Map<String, Object> operatorData = new LinkedHashMap<String, Object>();
                operatorData.put("billid", StringUtil.generateUUID());
                operatorData.put("operateid", login.getBillid());
                operatorData.put("content", "新增了用户：" + usercount);
                operatorData.put("operate_date", insert_date);
                operatorData.put("type", TransformConstant.transform_log_user);
                m = this.addSystemLogInfo(operatorData);
                
            }
            
            if (n > 0 && m > 0) {
                if(AppInconstant.initUser.equals("false")){ 
                    ManSystemRoleMapper systemRoleMapper = this.sqlSession.getMapper(ManSystemRoleMapper.class);
                    List<ManSystemRoleVO> roleList = systemRoleMapper.getAll();
                    if(roleList != null && roleList.size()>0){
                      //将用户和角色关联
                        Map<String, Object> relateData = new LinkedHashMap<String, Object>();
                        relateData.put("billid", StringUtil.generateUUID());
                        relateData.put("userid", billid);
                        relateData.put("roleid", roleList.get(0).getBillid());
                        systemRoleMapper.saveUserRole(relateData);
                    }
                    
                    //更新字典表和缓存的数据
                    DictionaryMapper dictionaryMapper = this.sqlSession.getMapper(DictionaryMapper.class);
                    List<DictionaryVO> dList = dictionaryMapper.queryValueByCode("init_user");
                    if(dList != null && dList.size()>0){                      
                        Map<String,Object> data = new LinkedHashMap<String,Object>();
                        data.put("id", dList.get(0).getId());
                        data.put("value", "true");
                        data.put("modifyTime",  StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
                        dictionaryMapper.updateValueById(data); 
                        //同步代码
                        synchronized (AppInconstant.initUser) {
                             AppInconstant.initUser = "true";      
                        } 
                    }
                    

                }
                // 发送注册通知邮件
                try {
                    if(!StringUtil.isBlank(email)) {
                        Map<String, Object> param = new LinkedHashMap<>();
                        param.put("email", email);
                        param.put("name", usercount);
                        param.put("password", password);
                        param.put("url", AppProperties.getValue("address_of_this_system", ""));
                        param.put(MessageConstant.EMAIL_SEND_TYPE_KEY, MessageConstant.EMAIL_TO_RECIPIENT_WITH_BCC);
                        param.put(MessageConstant.EMAIL_TEMPLATE_KEY, EmailTemplateConstant.INFO_ADMIN_REGISTER);
                        //异步发送邮件
                        MessageAsyncHelper.getInstance().publishMessageEvent(param);
                    }
                } catch (Exception e) {
                    logger.error(e);
                    return TransformConstant.success;
                }
                return TransformConstant.success;
            } else {
                return "添加失败";
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e);
            throw new AppException("添加失败");
        }
    } 

    /**
     * @Description:添加日志信息
     * @param condition
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public int addSystemLogInfo(Map<String, Object> condition) {
        try {
            ManSystemLogMapper systemLogMapper = this.sqlSession.getMapper(ManSystemLogMapper.class);
            systemLogMapper.deleteLogsWithoutDays(TransformConstant.logkeep);
            return systemLogMapper.addSystemLog(condition);
        } catch (Exception e) {
            logger.error(e);
            throw new AppException("日志添加失败");
        }

    }

    /**
     * @Description:删除用户信息
     * @param billids
     * @param login
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public String deleteSysUser(String billids, TransFormLoginInfo login) {
        logger.debug("SysUserServiceImpl.deleteSysUser()");
        try {
            ManSystemUserMapper systemUserMapper = this.sqlSession.getMapper(ManSystemUserMapper.class);
            String[] array = billids.split(",");
            // 添加日志信息
            for (String billid : array) {
                Map<String, Object> operatorData = new LinkedHashMap<String, Object>();
                operatorData.put("billid", StringUtil.generateUUID());
                operatorData.put("operateid", login.getBillid());
                operatorData.put("content", "删除了用户：" + systemUserMapper.getUserById(billid).getUsercount());
                operatorData.put("operate_date", StringUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
                operatorData.put("type", TransformConstant.transform_log_user);
                this.addSystemLogInfo(operatorData);
            }
            // 删除用户和角色关联关系
            for (String billid : array) {
                systemUserMapper.deleteUserRoleByUserID(billid);
            }
            systemUserMapper.deleteSystemUserByIds(array);

            return TransformConstant.success;

        } catch (Exception e) {
            logger.error(e);
            throw new AppException("删除失败");
        }
    }

    /**
     * @Description:重置密码操作
     * @param billid
     * @return
     */
    @Transactional(readOnly = false)
    public String resetPassword(String billid, TransFormLoginInfo login) {
        logger.debug("SysUserServiceImpl.resetPassword()");
        try {
            ManSystemUserMapper systemUserMapper = this.sqlSession.getMapper(ManSystemUserMapper.class);
            LinkedHashMap<String, Object> condition = new LinkedHashMap<String, Object>();
            ManSystemUserVO manSystemUserVO = systemUserMapper.getUserById(billid);
            String password = RandomPassword.getRandomNum(MailConstant.PASSWORD_LENGTH);
            condition.put("password", TransFormLoginHelper.md5(AppConstant.PASSWORD_MD5_STR, password));
            condition.put("billid", billid);
            condition.put("tenant_id", manSystemUserVO.getTenant_id());
            systemUserMapper.updateSystemUser(condition);
            
            // 添加日志信息
            Map<String, Object> operatorData = new LinkedHashMap<String, Object>();
            operatorData.put("billid", StringUtil.generateUUID());
            operatorData.put("operateid", login.getBillid());
            operatorData.put("content", "重置了用户" + manSystemUserVO.getUsercount() + "密码");
            operatorData.put("operate_date", StringUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
            operatorData.put("type", TransformConstant.transform_log_user);
            this.addSystemLogInfo(operatorData);
            // 发送新密码邮件
            try {
                String email = manSystemUserVO.getEmail();
                Map<String, Object> user = new LinkedHashMap<String, Object>();
                user.put("password", password);
                //                user.put("email", manSystemUserVO.getEmail());
                //                user.put("usercount", manSystemUserVO.getUsercount());
                EmailSendService emailSendService = MessageServiceManager.singleton().getMailService();
                emailSendService.sendMailWithBcc(EmailTemplateConstant.INFO_RESET_PASSWORD_RANDOM, email, user);
            } catch (Exception e) {
                logger.error(e);
                e.printStackTrace();
                return TransformConstant.success;
            }
            return TransformConstant.success;

        } catch (Exception e) {
            logger.error(e);
            throw new AppException("添加失败");
        }
    }

    /**
     * @Description:修改用户信息
     * @param parameter
     * @return
     * @throws
     */
    @Transactional(readOnly = false)
    public String modSysUser(Map<String, Object> parameter, TransFormLoginInfo login) {
        logger.debug("SysUserServiceImpl.modSysUser()");
        try {
            String usercount = StringUtil.trim(parameter.get("usercount"));
            String email = StringUtil.trim(parameter.get("email"));
            String telphone = StringUtil.trim(parameter.get("telphone"));
            String status = StringUtil.trim(parameter.get("status"));
            String billid = StringUtil.trim(parameter.get("billid"));
            String usb_status = StringUtil.trim(parameter.get("usb_status"));
            String displayname = StringUtil.trim(parameter.get("displayname"));
            String userType =  StringUtil.trim(parameter.get("userType"));         
            
            if (TransformConstant.transform_username_admin.equals(usercount)) {
                return "用户名或邮箱已经存在";
            }
            ManSystemUserMapper systemUserMapper = this.sqlSession.getMapper(ManSystemUserMapper.class);
            // 判断账户是否已经存在
            LinkedHashMap<String, Object> condition = new LinkedHashMap<String, Object>();
            condition.put("usercount", usercount);
            condition.put("email", email);
            condition.put("telphone", telphone);
            condition.put("status", status);
            condition.put("billid", billid);
            condition.put("usb_status", usb_status);
            condition.put("displayname", displayname);
            condition.put("userType", userType);            
            // 验证该账号和邮箱是否已经存在
            ManSystemUserVO systemUserVO = systemUserMapper.validateUserIsExistsMod(condition);
            if (systemUserVO != null) {
                return "用户名或邮箱已经存在";
            }
            // 更新用户数据
            int n = systemUserMapper.updateSystemUser(condition);
            //如果用户是管理员用户，删除关联的租户关联
            if(userType.equals("0")){
                systemUserMapper.deleteRelationshipByUserId(billid);
            }

            // 添加日志信息
            Map<String, Object> operatorData = new LinkedHashMap<String, Object>();
            operatorData.put("billid", StringUtil.generateUUID());
            operatorData.put("operateid", login.getBillid());
            operatorData.put("content", "修改了用户：" + usercount);
            operatorData.put("operate_date", StringUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
            operatorData.put("type", TransformConstant.transform_log_user);

            int m = this.addSystemLogInfo(operatorData);
            if (n > 0 && m > 0) {
                return TransformConstant.success;
            } else {
                return "修改失败";
            }

        } catch (Exception e) {
            logger.error(e);
            throw new AppException("修改失败");
        }
    }

    /**
     * Description:查询该角色拥有的用户信息
     * 
     * @param billid
     * @return
     */
    public List<ManSystemUserVO> getUserInRoleID(String billid) {
        if (billid == null || "".equals(billid)) {
            return null;
        }
        ManSystemUserMapper manSystemUserMapper = this.sqlSession.getMapper(ManSystemUserMapper.class);

        return manSystemUserMapper.queryUserInRoleID(billid);
    }

    /**
     * Description:查询不在该角色里面的用户信息
     * 
     * @param billid
     * @return
     */
    public List<ManSystemUserVO> getUserOutRoleID(String billid) {
        if (billid == null || "".equals(billid)) {
            return null;
        }
        ManSystemUserMapper manSystemUserMapper = this.sqlSession.getMapper(ManSystemUserMapper.class);

        return manSystemUserMapper.queryUserOutRoleID(billid);
    }

    /**
     * Description:查询该角色组拥有的用户信息
     * @param billid
     * @return
     */
    public List<ManSystemUserVO> getUserInGroupID(String billid) {
        if (billid == null || "".equals(billid)) {
            return null;
        }
        ManSystemUserMapper manSystemUserMapper = this.sqlSession.getMapper(ManSystemUserMapper.class);

        return manSystemUserMapper.queryUserInGroupID(billid);
    }

    /**
     * Description:查询不在该角色组里面的用户信息
     * @param billid
     * @return
     */
    public List<ManSystemUserVO> getUserOutGroupID(String billid) {
        if (billid == null || "".equals(billid)) {
            return null;
        }
        ManSystemUserMapper manSystemUserMapper = this.sqlSession.getMapper(ManSystemUserMapper.class);

        return manSystemUserMapper.queryUserOutGroupID(billid);
    }

    /**
     * @Description:将用户和相关信息设置到TransFormLoginInfo，存放于session
     * @param user 用户
     */
    public void setTransFormUserInfo(HttpServletRequest request, ManSystemUserVO user) {
        ManSystemMenuMapper menuMapper = this.sqlSession.getMapper(ManSystemMenuMapper.class);
        ManSystemRightMapper rightMapper = this.sqlSession.getMapper(ManSystemRightMapper.class);
        TransFormLoginInfo loginInfo = new TransFormLoginInfo(true);
        loginInfo.setBillid(user.getBillid());
        loginInfo.setEmail(user.getEmail());
        loginInfo.setTelphone(user.getTelphone());
        loginInfo.setUsercount(user.getUsercount());
        loginInfo.setUserType(user.getUserType());
        loginInfo.setStatus(user.getStatus());
        Map<String, Object> conditionData = new LinkedHashMap<String, Object>();
        conditionData.put("userid", user.getBillid());
        // 处理用户菜单权限
        List<ManSystemMenuVO> menuList = menuMapper.getParentMenuByUserID(conditionData);
        if (menuList != null && menuList.size() > 0) {
            for (ManSystemMenuVO menu : menuList) {
                Map<String, Object> data = new LinkedHashMap<String, Object>();
                // 根据上级节点信息查询该节点拥有的子节点信息
                data.put("userid", user.getBillid());
                data.put("parentid", menu.getBillid());
                List<ManSystemMenuVO> children = menuMapper.getChildrenMenuByUserID(data);
                if (children != null && children.size() > 0) {
                    Set<ManSystemMenuVO> childrenMenu = new LinkedHashSet<ManSystemMenuVO>();
                    // 将List转换成Set
                    for (ManSystemMenuVO child : children) {
                        childrenMenu.add(child);
                    }
                    menu.setChildren(childrenMenu);
                }
            }
            loginInfo.setMenuSet(menuList);
        }
        // 处理用户功能权限
        List<ManSystemRightVO> rightList = rightMapper.getRightByUserID(user.getBillid());
        if (rightList != null && rightList.size() > 0) {
            Set<String> rightSet = new LinkedHashSet<String>();
            // 将List转换成Set
            for (ManSystemRightVO right : rightList) {
                rightSet.add(right.getCode());
            }
            if (rightSet != null && rightSet.size() > 0) {
                loginInfo.setRightSet(rightSet);
            }
        }
        TransFormLoginHelper.putSessionMap(loginInfo.getBillid(), loginInfo, request);
    }
    /**
     * 
    * @Title: setTerminalUserInfo 
    * @Description: 设置终端用户的session信息
    * @param @param request
    * @param @param user      
    * @return void     
    * @throws
     */
    public void setTerminalUserInfo(HttpServletRequest request, TerminalUserVO user) {
        TransFormLoginInfo loginInfo = new TransFormLoginInfo(true);
        loginInfo.setBillid(user.getId());
        loginInfo.setEmail(user.getEmail());
        loginInfo.setTelphone(user.getCreateTime());
        loginInfo.setUsercount(user.getName());
        loginInfo.setStatus(user.getStatus());
        Map<String, Object> conditionData = new LinkedHashMap<String, Object>();
        conditionData.put("userid", user.getId());
        // 处理用户菜单权限
        List<ManSystemMenuVO> menuList = new ArrayList<ManSystemMenuVO>();
        // 基本信息菜单
        ManSystemMenuVO menu1 = new ManSystemMenuVO();
        menu1.setCssname("fa");
        menu1.setMenuname("主机列表");
        menuList.add(menu1);
//        menuList.set(index, element)
        
//        if (menuList != null && menuList.size() > 0) {
//            for (ManSystemMenuVO menu : menuList) {
//                Map<String, Object> data = new LinkedHashMap<String, Object>();
//                // 根据上级节点信息查询该节点拥有的子节点信息
//                data.put("userid", user.getId());
//                data.put("parentid", menu.getBillid());
//                List<ManSystemMenuVO> children = menuMapper.getChildrenMenuByUserID(data);
//                if (children != null && children.size() > 0) {
//                    Set<ManSystemMenuVO> childrenMenu = new LinkedHashSet<ManSystemMenuVO>();
//                    // 将List转换成Set
//                    for (ManSystemMenuVO child : children) {
//                        childrenMenu.add(child);
//                    }
//                    menu.setChildren(childrenMenu);
//                }
//            }
//            loginInfo.setMenuSet(menuList);
//        }
       
        TransFormLoginHelper.putSessionMap(loginInfo.getBillid(), loginInfo, request);
    }

    public void setTransFormUserInfo(HttpServletRequest request) {
        ManSystemMenuMapper menuMapper = this.sqlSession.getMapper(ManSystemMenuMapper.class);
        ManSystemRightMapper rightMapper = this.sqlSession.getMapper(ManSystemRightMapper.class);
        TransFormLoginInfo loginInfo = new TransFormLoginInfo(true);
        loginInfo.setBillid(TransformConstant.transform_billid_admin);
        loginInfo.setUsercount(TransformConstant.transform_username_admin);
        // 管理员获取所有菜单权限
        List<ManSystemMenuVO> menuList = menuMapper.getAllParent();
        List<ManSystemMenuVO> newList = new LinkedList<ManSystemMenuVO>();
        if (menuList != null && menuList.size() > 0) {
            for (ManSystemMenuVO menu : menuList) {
                // 管理员登录的时候 过滤掉特殊的菜单
                if (!"登录管理".equals(menu.getMenuname())) {
                    newList.add(menu);
                }
            }
            for (ManSystemMenuVO menu : newList) {

                // 根据上级节点信息查询该节点拥有的子节点信息
                List<ManSystemMenuVO> children = menuMapper.getChildren(menu.getBillid());
                if (children != null && children.size() > 0) {
                    Set<ManSystemMenuVO> childrenMenu = new LinkedHashSet<ManSystemMenuVO>();
                    // 将List转换成Set
                    for (ManSystemMenuVO child : children) {
                        childrenMenu.add(child);
                    }
                    menu.setChildren(childrenMenu);
                }
            }
            loginInfo.setMenuSet(newList);
        }
        // 处理用户功能权限
        List<ManSystemRightVO> rightList = rightMapper.getAll();
        if (rightList != null && rightList.size() > 0) {
            Set<String> rightSet = new LinkedHashSet<String>();
            // 将List转换成Set
            for (ManSystemRightVO right : rightList) {
                rightSet.add(right.getCode());
            }
            if (rightSet != null && rightSet.size() > 0) {
                loginInfo.setRightSet(rightSet);
            }
        }
        TransFormLoginHelper.putSessionMap(loginInfo.getBillid(), loginInfo, request);
    }
    
    public List<ManSystemMenuVO> getALL() {
        ManSystemMenuMapper menuMapper = this.sqlSession.getMapper(ManSystemMenuMapper.class);
        ManSystemRightMapper rightMapper = this.sqlSession.getMapper(ManSystemRightMapper.class);
        TransFormLoginInfo loginInfo = new TransFormLoginInfo(true);
        loginInfo.setBillid(TransformConstant.transform_billid_admin);
        loginInfo.setUsercount(TransformConstant.transform_username_admin);
        // 管理员获取所有菜单权限
        List<ManSystemMenuVO> menuList = menuMapper.getAllParent();
        List<ManSystemMenuVO> newList = new LinkedList<ManSystemMenuVO>();
        if (menuList != null && menuList.size() > 0) {
            for (ManSystemMenuVO menu : menuList) {
                // 管理员登录的时候 过滤掉特殊的菜单
                if (!"登录管理".equals(menu.getMenuname())) {
                    newList.add(menu);
                }
            }
            for (ManSystemMenuVO menu : newList) {

                // 根据上级节点信息查询该节点拥有的子节点信息
                List<ManSystemMenuVO> children = menuMapper.getChildren(menu.getBillid());
                if (children != null && children.size() > 0) {
                    Set<ManSystemMenuVO> childrenMenu = new LinkedHashSet<ManSystemMenuVO>();
                    // 将List转换成Set
                    for (ManSystemMenuVO child : children) {
                        childrenMenu.add(child);
                    }
                    menu.setChildren(childrenMenu);
                }
            } 
        }
        
        return newList;
         
    }
    
    /**
     * Description:查询用户对象并返回
     * @param billid
     * @return
     */
    public ManSystemUserVO getUserInfoByID(String billid) {
        if (billid == null || "".equals(billid)) {
            return null;
        }
        ManSystemUserMapper manSystemUserMapper = this.sqlSession.getMapper(ManSystemUserMapper.class);
        return manSystemUserMapper.getUserById(billid);
    }   
    
    /**
     * @Description:修改密码操作
     * @param billid
     * @param newpassword 新密码
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public String updatePassword(String billid, String newpassword, String oldpassword) {
        logger.debug("SysUserServiceImpl.updatePassword()");
        try {
            ManSystemUserMapper systemUserMapper = this.sqlSession.getMapper(ManSystemUserMapper.class);
            // 先比较旧密码和数据中的密码是否一致
            ManSystemUserVO manSystemUserVO = systemUserMapper.getUserById(billid);
            if (!manSystemUserVO.getPassword().equals(
                    TransFormLoginHelper.md5(AppConstant.PASSWORD_MD5_STR, oldpassword))) {
                return "用户密码不正确";
            }
            LinkedHashMap<String, Object> condition = new LinkedHashMap<String, Object>();
            condition.put("password", TransFormLoginHelper.md5(AppConstant.PASSWORD_MD5_STR, newpassword));
            condition.put("billid", billid);
            condition.put("tenant_id", manSystemUserVO.getTenant_id());            
            systemUserMapper.updateSystemUser(condition);

            // 添加日志信息
            Map<String, Object> operatorData = new LinkedHashMap<String, Object>();
            operatorData.put("billid", StringUtil.generateUUID());
            operatorData.put("operateid", billid);
            operatorData.put("content", "修改了用户 " + manSystemUserVO.getUsercount() + " 密码");
            operatorData.put("operate_date", StringUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
            operatorData.put("type", TransformConstant.transform_log_user);
            this.addSystemLogInfo(operatorData);
            return TransformConstant.success;
        } catch (Exception e) {
            logger.error(e);
            throw new AppException("添加失败");
        }
    }
    

    /**
     * @Description:设置用户状态
     * @param billids 数组
     * @param status 状态
     * @param login 登录信息
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public String updateUserStatus(String billids, String status, TransFormLoginInfo login) {
        logger.debug("SysUserServiceImpl.updateUserStatus()");
        try {
            ManSystemUserMapper systemUserMapper = this.sqlSession.getMapper(ManSystemUserMapper.class);
            String[] array = billids.split(",");
            Map<String, Object> condition = new LinkedHashMap<String, Object>();
            condition.put("status", status);
            // 添加日志信息
            for (String billid : array) {
                if(billid.equals(login.getBillid()) && status.equals(AppConstant.USER_STATUS_DELETE.toString())){
                    continue;
                }
                Map<String, Object> operatorData = new LinkedHashMap<String, Object>();
                operatorData.put("billid", StringUtil.generateUUID());
                operatorData.put("operateid", login.getBillid());
                operatorData.put("content", "修改了用户状态：" + systemUserMapper.getUserById(billid).getUsercount());
                operatorData.put("operate_date", StringUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
                operatorData.put("type", TransformConstant.transform_log_user);
                this.addSystemLogInfo(operatorData);
                ManSystemUserVO manSystemUserVO = systemUserMapper.getUserById(billid);
                condition.put("billid", billid);
                condition.put("tenant_id", manSystemUserVO.getTenant_id());                
                // 更新用户数据
                systemUserMapper.updateSystemUser(condition);
            }

            return TransformConstant.success;

        } catch (Exception e) {
            logger.error(e);
            throw new AppException("操作失败");
        }
    }
    
    /**
     * @Description:设置用户USB权限
     * @param billids 数组
     * @param status 权限
     * @param login 登录信息
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public String updateUserUSBStatus(String billids, String status, TransFormLoginInfo login) {
        logger.debug("SysUserServiceImpl.updateUserUSBStatus()");
        try {
            ManSystemUserMapper systemUserMapper = this.sqlSession.getMapper(ManSystemUserMapper.class);
            String[] array = billids.split(",");
            Map<String, Object> condition = new LinkedHashMap<String, Object>();
            condition.put("usb_status", status);
            // 添加日志信息
            for (String billid : array) {
                Map<String, Object> operatorData = new LinkedHashMap<String, Object>();
                operatorData.put("billid", StringUtil.generateUUID());
                operatorData.put("operateid", login.getBillid());
                operatorData.put("content", "修改了用户USB权限：" + systemUserMapper.getUserById(billid).getUsercount());
                operatorData.put("operate_date", StringUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm:ss"));
                operatorData.put("type", TransformConstant.transform_log_user);
                this.addSystemLogInfo(operatorData);
                ManSystemUserVO manSystemUserVO = systemUserMapper.getUserById(billid);
                condition.put("billid", billid);
                condition.put("tenant_id", manSystemUserVO.getTenant_id());  
                // 更新用户数据
                systemUserMapper.updateSystemUser(condition);
            }

            return TransformConstant.success;

        } catch (Exception e) {
            logger.error(e);
            throw new AppException("操作失败");
        }
    }

    @Override
    public List<ManSystemUserVO> queryUserOutTenant(String id) {
        ManSystemUserMapper systemUserMapper = this.sqlSession.getMapper(ManSystemUserMapper.class);
        return systemUserMapper.queryUserOutTenant(id);
    }

    @Override
    public List<ManSystemUserVO> queryUserTenant(String id) {
        ManSystemUserMapper systemUserMapper = this.sqlSession.getMapper(ManSystemUserMapper.class);
        return systemUserMapper.queryUserTenant(id);
    }

    /**
     * 更新租户的用户设置
    * <p>Title: setTenantUser</p> 
    * <p>Description: </p> 
    * @param id
    * @param userIds
    * @return 
    * @see com.zhicloud.ms.transform.service.ManSysUserService#setTenantUser(java.lang.String, java.lang.String)
     */
    @Override
    @Transactional(readOnly=false)
    public MethodResult setTenantUser(String id, String userIds) {
        ManSystemUserMapper systemUserMapper = this.sqlSession.getMapper(ManSystemUserMapper.class);
        systemUserMapper.deleteRelationshipByTenantId(id);  
        String[] idArray = userIds.split(",");
        Map<String, Object> condition = new LinkedHashMap<String, Object>();
        condition.put("tenantId", id);
        for (String userId : idArray){
            condition.put("id", StringUtil.generateUUID());    
            condition.put("userId", userId);    
            systemUserMapper.addUserTenantRelationship(condition);
        }
        return new MethodResult(MethodResult.SUCCESS, "重新设置租户用户成功");
    }
    
    /**
     * @Description:直接修改密码操作
     * @param billid
     * @param newpassword 新密码
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public String manualPassword(String billid, String newpassword, String email) {
        logger.debug("SysUserServiceImpl.manualPassword()");
        try {
            try {
                Map<String, Object> user = new LinkedHashMap<String, Object>();
                user.put("password", newpassword);
                EmailSendService emailSendService = MessageServiceManager.singleton().getMailService();
                emailSendService.sendMailWithBcc(EmailTemplateConstant.INFO_RESET_PASSWORD_MANUAL, email, user);
            } catch (Exception e) {
                logger.error(e);
                return TransformConstant.success;
            }

            return TransformConstant.success;
        } catch (Exception e) {
            logger.error(e);
            throw new AppException("添加失败");
        }
    }
}
