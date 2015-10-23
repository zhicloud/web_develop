package com.zhicloud.ms.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhicloud.ms.constant.StatusConstant;
import com.zhicloud.ms.service.DomainService;
import com.zhicloud.ms.service.LogService;
import com.zhicloud.ms.vo.UserVO;

/**
 * @ClassName: DomainController
 * @Description: 域名管理
 * @author 张本缘 于 2015年9月11日 下午2:28:43
 */
@Controller
@RequestMapping("/domain")
public class DomainController {

    /* 日志 */
    public static final Logger logger = Logger.getLogger(DomainController.class);
    
    /* 模块名称 */
    public static final String data_index = "data_index";
    public static final String node_client = "node_client";
    public static final String http_gateway = "http_gateway";
    public static final String control_server = "control_server";
    
    /* 服务存放的公用路径 */
    public static final String commonpath = "/home/zhicloud/";
    /* 命令(起服务和停止服务) */
    public static final String exec_start = "start";
    public static final String exec_stop = "stop";
    
    /**
     * 日志接口
     */
    @Resource(name = "logService")
    private LogService logService;

    /**
     * 本业务接口
     */
    @Resource(name = "domainService")
    private DomainService domainService;
    
    /**
     * @Description: 域名管理跳转
     * @param request
     * @return String
     */
    @RequestMapping(value = "/manage", method = RequestMethod.GET)
    public String managePage(HttpServletRequest request) {
        request.setAttribute("broadcast", domainService.getBroadcast(http_gateway));
        request.setAttribute("domain", domainService.getDomain(http_gateway));
        return "/domain/domainmanage";
    }
    

    /**
     * @Description:设置组播地址
     * @param broadcast 组播地址
     * @param request
     * @return JSONObject
     */
    @RequestMapping(value = "/setbroadcast", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject setBroadcast(@RequestParam(value = "broadcast") String broadcast, HttpServletRequest request) {
        // 参数处理
        JSONObject re = new JSONObject();
        try {
            UserVO user = (UserVO) request.getSession().getAttribute("user");
            if (broadcast.isEmpty() || "".equals(broadcast)) {
                re.put("status", StatusConstant.fail);
                re.put("message", "操作失败,请联系管理员");
            } else {
                // 存储由于暂时还没有好，所以先注释掉
                //domainService.setBroadcast(broadcast, data_index);
                // NC
                domainService.setBroadcast(broadcast, node_client);
                // http_gateway
                domainService.setBroadcast(broadcast, http_gateway);
                // CS
                domainService.setBroadcast(broadcast, control_server);
                // 为了使修改生效，需要重新启动服务
                stopAllService();
                startAllService();
                re.put("status", StatusConstant.success);
                logService.addLog("设置组播地址:" + broadcast, user.getId());
            }

        } catch (Exception e) {
            e.printStackTrace();
            re.put("status", StatusConstant.fail);
            re.put("message", "操作失败,请联系管理员");
        }
        return re;
    }
    
    /**
     * @Description:设置域名
     * @param broadcast 组播地址
     * @param request
     * @return JSONObject
     */
    @RequestMapping(value = "/setdomain", method = RequestMethod.POST)
    @ResponseBody
    public JSONObject setDomain(@RequestParam(value = "domain") String domain, HttpServletRequest request) {
        // 参数处理
        JSONObject re = new JSONObject();
        try {
            UserVO user = (UserVO) request.getSession().getAttribute("user");
            if (domain.isEmpty() || "".equals(domain)) {
                re.put("status", StatusConstant.fail);
                re.put("message", "操作失败,请联系管理员");
            } else {
                // 存储还没好,暂时注释掉
                //domainService.setDomain(domain, data_index);
                // NC
                domainService.setDomain(domain, node_client);
                // http_gateway
                domainService.setDomain(domain, http_gateway);
                // CS
                domainService.setDomain(domain, control_server);
                // 为了使修改生效，需要重新启动服务
                stopAllService();
                startAllService();
                re.put("status", StatusConstant.success);
                logService.addLog("设置域名信息:" + domain, user.getId());
            }

        } catch (Exception e) {
            e.printStackTrace();
            re.put("status", StatusConstant.fail);
            re.put("message", "操作失败,请联系管理员");
        }
        return re;
    }
    
    /**
     * @Description:启动所有模块的服务
     * @throws
     */
    public void startAllService() {
        try {
            //Runtime.getRuntime().exec(commonpath + data_index + "/" + data_index + " " + exec_start);
            Runtime.getRuntime().exec(commonpath + node_client + "/" + node_client + " " + exec_start);
            Runtime.getRuntime().exec(commonpath + http_gateway + "/" + http_gateway + " " + exec_start);
            Runtime.getRuntime().exec(commonpath + control_server + "/" + control_server + " " + exec_start);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("启动服务出错:" + e.getMessage());
        }
    }
    
    /**
     * @Description:停止所有模块的服务
     * @throws
     */
    public void stopAllService() {
        try {
            //Runtime.getRuntime().exec(commonpath + data_index + "/" + data_index + " " + exec_stop);
            Runtime.getRuntime().exec(commonpath + node_client + "/" + node_client + " " + exec_stop);
            Runtime.getRuntime().exec(commonpath + http_gateway + "/" + http_gateway + " " + exec_stop);
            Runtime.getRuntime().exec(commonpath + control_server + "/" + control_server + " " + exec_stop);
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("停止服务出错:" + e.getMessage());
        }
    }
}
