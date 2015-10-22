package com.zhicloud.ms.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhicloud.ms.common.util.AESUtil;
import com.zhicloud.ms.common.util.StringUtil;
import com.zhicloud.ms.constant.StatusConstant;
import com.zhicloud.ms.service.LogService;
import com.zhicloud.ms.service.LoginService;
import com.zhicloud.ms.service.impl.LoginServiceImpl;
import com.zhicloud.ms.util.MD5;
import com.zhicloud.ms.vo.UserVO;

/**
 * @ClassName: LoginController
 * @Description: 用户登录控制类
 * @author 张本缘 于 2015年9月8日 上午10:25:00
 */
@Controller
@RequestMapping("/login")
public class LoginController {
    /* 日志 */
    public static final Logger logger = Logger.getLogger(LoginController.class);
    
    @Resource(name = "loginService")
    private LoginService loginService;    
    
    @Resource(name = "logService")
    private LogService logService;

    /**
     * @Description: 登录页面跳转
     * @param request
     * @return String
     */
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String loginPage(HttpServletRequest request) {
        return "/common/login";
    }
    
    /**
     * @Description: 主页面跳转
     * @param request
     * @return String
     */
    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public String mainPage(HttpServletRequest request) {
        UserVO user = (UserVO) request.getSession().getAttribute("user");
        request.setAttribute("user", user);
        return "/common/main";
    }

    /**
     * @Description: 注销
     * @param request
     * @return String
     */
    @RequestMapping(value = "/logout", method = RequestMethod.GET)
    public String logout(HttpServletRequest request) {
        UserVO user = (UserVO) request.getSession().getAttribute("user");
        logService.addLog("成功注销", user.getId());
        request.getSession().removeAttribute("user");
        return "/common/login";
    }
    
    /**
     * @Description:验证用户名和密码信息
     * @param username 用户名
     * @param password 密码
     * @param request
     * @return JSONObject
     */
    @RequestMapping(value = "/beforelogin", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject beforeLogin(@RequestParam(value = "username") String username,
            @RequestParam(value = "password") String password, HttpServletRequest request) {
        // 参数处理
        JSONObject re = new JSONObject();
        
        try {
            Map<String, Object> condition = new LinkedHashMap<String, Object>();            
            Object obj = request.getSession().getAttribute("keyCodeSrc");
            if(obj != null){
                String newpwd = MD5.md5((String)obj);
                if(newpwd.indexOf(password) >= 0){
                    condition.put("username", username);
                }
                else{
                    condition.put("username", username);
                    condition.put("password", MD5.md5(password));
                }
            }
            else{
                condition.put("username", username);
                condition.put("password", MD5.md5(password));
            }
            
            List<UserVO> users = loginService.queryPage(condition);
            if (users != null && users.size() > 0) {
                request.getSession().setAttribute("user", users.get(0));
                logService.addLog("成功登录", users.get(0).getId());
                re.put("status", StatusConstant.success);
            } else {
                re.put("status", StatusConstant.fail);
                re.put("message", "用户名或密码错误");
            }
        } catch (Exception e) {
            e.printStackTrace();
            re.put("status", StatusConstant.fail);
            re.put("message", "登录失败,请联系管理员");
        }
        return re;
    }
    
    /**
     * @Description:获取机器码
     * @param request
     * @return JSONObject
     */
    @RequestMapping(value = "/getMachineKey", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject getMachineKey(HttpServletRequest request) {
        // 参数处理
        JSONObject re = new JSONObject();
        try {
            String mac = loginService.getMAC();
            if(mac == null || mac.length() == 0){
                re.put("status", StatusConstant.fail);
            }
            else{
                mac = mac.replace(":", "") + "%To5if094D%^&*()";
                mac = mac.substring(0, 16);
                String machinekey = StringUtil.bytesToBase64(AESUtil.encrypt(mac.getBytes(), "zhicloud@123"));
                request.getSession().setAttribute("keyCodeSrc", machinekey.substring(0, 8));
                re.put("status", StatusConstant.success);
                re.put("machinekey", machinekey.substring(0, 8));
            }
        }catch (Exception e) {
            e.printStackTrace();
            re.put("status", StatusConstant.fail);
        }
        return re;
    }  
}
