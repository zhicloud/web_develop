package com.zhicloud.ms.controller;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.zhicloud.ms.app.helper.AppHelper;
import com.zhicloud.ms.app.propeties.AppProperties;
import com.zhicloud.ms.common.util.FileUtil;
import com.zhicloud.ms.common.util.StringUtil;
import com.zhicloud.ms.service.LogService;
import com.zhicloud.ms.service.SysmanageService;
import com.zhicloud.ms.vo.ServerComponentVO;
import com.zhicloud.ms.vo.UserVO;

/**
 * @ClassName: StateController
 * @Description: 系统管理处理类
 * @author 梁绍辉 于 2015年9月18日 上午11:10:19
 */
@Controller
@RequestMapping("/sysmanage")
public class SysmanageController {
    /* 日志 */
    public static final Logger logger = Logger.getLogger(SysmanageController.class);
    
    @Resource(name="sysmanageService")
    private SysmanageService sysmanageService;
    
    @Resource(name = "logService")
    private LogService logService;
    
    /**
     * @Description: 运行状态页面管理
     * @param request
     * @return String
     */
    @RequestMapping(value = "/manage", method = RequestMethod.GET)
    public String managePage(HttpServletRequest request) {
        return "/sysmanage/sysmanage";
    }


    /**
     * @Description:更新服务器时间
     * @param request
     * @throws
     */
    @RequestMapping(value = "/updateDatetime",method=RequestMethod.POST)
    @ResponseBody
    public JSONObject updateDatetime(HttpServletRequest request, HttpServletResponse response) {
        String newDatetime = StringUtil.trim(request.getParameter("newDatetime"));
        boolean bn = sysmanageService.updateDatetime(newDatetime);
        UserVO user = (UserVO) request.getSession().getAttribute("user");
        logService.addLog("更新服务器时间.", user.getId());
        JSONObject re = new JSONObject();
        re.put("result", (bn?"success":"fail"));
        return re;        
    }
    
    /**
     * @Description:更新服务器主机名
     * @param request
     * @throws
     */
    @RequestMapping(value = "/updateHostname",method=RequestMethod.POST)
    @ResponseBody
    public JSONObject updateHostNmae(HttpServletRequest request, HttpServletResponse response) {
        String hostname = StringUtil.trim(request.getParameter("newHostname"));
        boolean bn = sysmanageService.updateHostName(hostname);
        UserVO user = (UserVO) request.getSession().getAttribute("user");
        logService.addLog("更新服务器名：" + hostname, user.getId());
        JSONObject re = new JSONObject();
        re.put("result", (bn?"success":"fail"));
        return re;        
    }
    
    /**
     * @Description: 强制关机
     * @param request
     * @throws
     */
    @RequestMapping(value = "/shutdownServer",method=RequestMethod.POST)
    @ResponseBody
    public JSONObject shutdownServer(HttpServletRequest request, HttpServletResponse response) {
        JSONObject re = new JSONObject();
        UserVO user = (UserVO) request.getSession().getAttribute("user");
        logService.addLog("关闭服务器.", user.getId());
        boolean bn = sysmanageService.shutdownServer();        
        re.put("result", (bn?"success":"fail"));
        return re;        
    }
    
    /**
     * @Description: 重启服务器
     * @param request
     * @throws
     */
    @RequestMapping(value = "/rebootServer",method=RequestMethod.POST)
    @ResponseBody
    public JSONObject rebootServer(HttpServletRequest request, HttpServletResponse response) {
        UserVO user = (UserVO) request.getSession().getAttribute("user");
        logService.addLog("重启服务器.", user.getId());
        boolean bn = sysmanageService.rebootServer();
        JSONObject re = new JSONObject();
        re.put("result", (bn?"success":"fail"));
        return re;      
    }
    
    @RequestMapping(value = "/uploadFireware",method=RequestMethod.POST)
    @ResponseBody
    public JSONObject uploadFireware(HttpServletRequest request, HttpServletResponse response) {
        try {
            MultipartHttpServletRequest  multipartRequest = (MultipartHttpServletRequest) request;
            
            MultipartFile attach = multipartRequest.getFile("file_fireware");
            //获取文件名
            String fileName = new String(attach.getOriginalFilename().getBytes("ISO-8859-1"), "UTF-8");
            //定义上传路径
            String filePath = AppHelper.getServerHome() + "/webapps"; //+"/projects/"+AppHelper.APP_NAME+"/user_upload/";
            //若无该文件夹自动创建
            File fp = new File(filePath);            
            if(!fp.exists()){
                fp.mkdirs();
            }            
            File file = new File(filePath+"/"+fileName);
            // 如果存在原文件，则备份到/opt/目录下
            String newPath = "/opt/"+fileName;
            if(file.exists()){
                FileUtil.copyFile(filePath+"/"+fileName, newPath);
            }
            // 上传文件
            FileUtils.copyInputStreamToFile(attach.getInputStream(), file);            
            UserVO user = (UserVO) request.getSession().getAttribute("user");
            logService.addLog("上传固件升级文件.", user.getId());
            JSONObject re = new JSONObject();
            re.put("status", "success");
            return re;
        } catch (Exception e) { 
            e.printStackTrace();
            JSONObject re = new JSONObject();
            re.put("status", "fail");
            return re;
        }
    }
    
    /**
     * @Description: 重启服务器
     * @param request
     * @throws
     */
    @RequestMapping(value = "/rebootTomcat",method=RequestMethod.POST)
    @ResponseBody
    public JSONObject rebootTomcat(HttpServletRequest request, HttpServletResponse response) {
        UserVO user = (UserVO) request.getSession().getAttribute("user");
        logService.addLog("重启服务器tomcat服务.", user.getId());
        boolean bn = sysmanageService.rebootTomcat();
        logger.info("rebootTomcat:" + bn);
        JSONObject re = new JSONObject();
        re.put("result", (bn?"success":"fail"));
        return re;      
    }
    
    /**
     * @Description: 上传组件升级包临时目录（暂按只升级1个执行文件）
     * @param request
     * @throws
     */
    @RequestMapping(value = "/uploadComponent",method=RequestMethod.POST)
    @ResponseBody
    public JSONObject uploadComponent(HttpServletRequest request, HttpServletResponse response) {
        try {
            MultipartHttpServletRequest  multipartRequest = (MultipartHttpServletRequest) request;
            
            MultipartFile attach = multipartRequest.getFile("file_component");
            //获取文件名
            String fileName = new String(attach.getOriginalFilename().getBytes("ISO-8859-1"), "UTF-8");
            List<ServerComponentVO> serverComponentVOList = sysmanageService.getComponentList();
            boolean bn_exists_Component = false;
            for(ServerComponentVO vo:serverComponentVOList){
                if(vo.getKeyword().equals(fileName)){
                    bn_exists_Component = true;
                    break;
                }
            }
            if(!bn_exists_Component){
                JSONObject re = new JSONObject();
                re.put("status", "fail");
                re.put("message", "上传的升级文件格式不对。");
                return re;
            }
            //定义上传路径
            String filePath = AppProperties.getValue("component_temp_path", "/home/temp");
            //若无该文件夹自动创建
            File fp = new File(filePath);            
            if(!fp.exists()){
                fp.mkdirs();
            }            
            File file = new File(filePath+"/"+fileName);
            // 上传文件
            FileUtils.copyInputStreamToFile(attach.getInputStream(), file);  
            
            UserVO user = (UserVO) request.getSession().getAttribute("user");
            logService.addLog("上传组件升级程序：" + fileName, user.getId());
            
            JSONObject re = new JSONObject();
            re.put("status", "success");
            return re;
        } catch (Exception e) { 
            e.printStackTrace();
            JSONObject re = new JSONObject();
            re.put("status", "fail");
            re.put("message", "上传文件时异常。");
            return re;
        }
    }
    
    /**
     * @Description: 重启组件服务（停止服务，备份原程序，更新升级文件，启动服务，检测服务是否启动）
     * @param request
     * @throws
     */
    @RequestMapping(value = "/restartComponent",method=RequestMethod.POST)
    @ResponseBody
    public JSONObject restartComponent(HttpServletRequest request, HttpServletResponse response) {
        String componentName = StringUtil.trim(request.getParameter("name"));
        UserVO user = (UserVO) request.getSession().getAttribute("user");
        logService.addLog("重启组件服务:" + componentName, user.getId());        
        boolean bn = sysmanageService.restartComponent(componentName); 
        logger.info("restartComponent:" + bn);
        JSONObject re = new JSONObject();
        if(bn){
            re.put("status", "success");
        }else{
            re.put("status", "fail");
            re.put("message", "重启服务：" + componentName + "失败！");
        }            
        return re;      
    }
}
