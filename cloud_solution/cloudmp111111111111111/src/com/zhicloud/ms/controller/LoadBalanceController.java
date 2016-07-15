package com.zhicloud.ms.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;



import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.zhicloud.ms.service.IOperLogService;
import com.zhicloud.ms.transform.controller.TransFormBaseAction;

/**
 * @ClassName: LoadBalanceController
 * @Description: 负载均衡控制类
 * @author 张本缘 于 2015年10月23日 上午9:33:28
 */
@Controller
@RequestMapping("/loadbalance")
public class LoadBalanceController extends TransFormBaseAction{
    
    /* 日志 */
    public static final Logger logger = Logger.getLogger(LoadBalanceController.class);
    
    @Resource
    private IOperLogService operLogService;

    /**
     * @Description:负载均衡管理页面跳转
     * @param request
     * @return String
     */
    @RequestMapping(value = "/manage", method = RequestMethod.GET)
    public String managePage(HttpServletRequest request) {
        return "/loadbalance/manage";
    }

    /**
     * @Description:创建ELB页面跳转
     * @param request
     * @return String
     */
    @RequestMapping(value = "/createelb", method = RequestMethod.GET)
    public String beforeCreateELB(HttpServletRequest request) {
        return "/loadbalance/create_elb";
    }

    /**
     * @Description:创建ELB页面跳转
     * @param request
     * @return String
     */
    @RequestMapping(value = "/operator/{type}", method = RequestMethod.GET)
    public String beforeoperator(@PathVariable String type,HttpServletRequest request) {
        return "/loadbalance/"+type;
    }

}
