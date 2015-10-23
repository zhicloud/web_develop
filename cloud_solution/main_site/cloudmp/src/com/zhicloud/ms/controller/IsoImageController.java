package com.zhicloud.ms.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.zhicloud.ms.app.helper.AppHelper;
import com.zhicloud.ms.app.pool.isoImagePool.IsoImageProgressData;
import com.zhicloud.ms.app.pool.isoImagePool.IsoImageProgressPool;
import com.zhicloud.ms.app.pool.isoImagePool.IsoImageProgressPoolManager;
import com.zhicloud.ms.common.util.StringUtil;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.IOperLogService;
import com.zhicloud.ms.service.IsoImageService;
import com.zhicloud.ms.service.SharedMemoryService;
import com.zhicloud.ms.service.impl.EmailTemplateServiceImpl;
import com.zhicloud.ms.util.ServiceUtil;
import com.zhicloud.ms.vo.SharedMemoryVO;
@Controller
public class IsoImageController {
    
    public static final Logger logger = Logger.getLogger(EmailTemplateServiceImpl.class);

    
    @Resource
    private IOperLogService operLogService;
    
    @Resource
    private IsoImageService isoImageService;
    
    @Resource
    private SharedMemoryService sharedMemoryService;
    
    
    @RequestMapping(value="/isoimage/all",method=RequestMethod.GET)
    public String toAll(Model model,HttpServletRequest request, HttpServletResponse response) throws IOException
    { 
        IsoImageProgressPool pool = IsoImageProgressPoolManager.singleton().getPool();
        IsoImageProgressData[] isoArray = pool.getAll();
        model.addAttribute("isoArray", isoArray);
        this.executeShellOfQueryNas("172.18.10.52");
        return "isoimage/iso_image_manage";
    }
    
    @RequestMapping(value="/isoimage/add",method=RequestMethod.GET)
    public String addPage(HttpServletRequest request, HttpServletResponse response) throws IOException
    { 
           return "isoimage/iso_image_add";
    }
    
    @RequestMapping(value="/isoimage/add",method=RequestMethod.POST)
    @ResponseBody
    public MethodResult add(String name,String type,String description,HttpServletRequest request, HttpServletResponse response) throws IOException
    { 
        SharedMemoryVO vo = sharedMemoryService.queryAvailable();
        if(vo == null){
            return new MethodResult(MethodResult.FAIL,"路径未定义");
        }
        MultipartHttpServletRequest  multipartRequest = (MultipartHttpServletRequest) request;
         
        MultipartFile attach = multipartRequest.getFile("filename");
        String uuid = StringUtil.generateUUID();
         
        //获取文件名
        String fileName = new String(attach.getOriginalFilename().getBytes("ISO-8859-1"), "UTF-8");
         
        //定义上传路径
        String filePath = "/image/iso_image/system/";
        
        //若无该文件夹自动创建
        File fp = new File(filePath);
        
        if(!fp.exists()){
            fp.mkdirs();
        }
        
        File file = new File(filePath+"/"+uuid+".iso");
        
        // 上传文件
        FileUtils.copyInputStreamToFile(attach.getInputStream(), file);
        isoImageService.upload(name, uuid, "/iso_image/system/"+"/"+uuid+".iso", type, description);
        return new MethodResult(MethodResult.SUCCESS,"上传成功");
    }
    
    @RequestMapping(value="/isoimage/checkNas",method=RequestMethod.GET)
    @ResponseBody
    public MethodResult checknas(String url) throws IOException
    { 
        HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        MultipartHttpServletRequest  multipartRequest = (MultipartHttpServletRequest) request;
        String[] ip = StringUtil.getIpFromUrl(url);
        if(ip == null || ip.length <= 0){
            return new MethodResult(MethodResult.FAIL,"未挂载");
        }
        Process pro = Runtime.getRuntime().exec("showmount -e "+ip[0]);
        BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
         int i = 0;
        String str = br.readLine();
        while(!StringUtils.isBlank(str)){
            if(i == 0){
                continue;
            }else{
                String [] ips = StringUtil.getIpFromUrl(str); 
                if(ips != null && ips.length >= 1){
                    if(ip[0].equals(ips)){
                        
                    } 
                } 
            }                 
            str = br.readLine();
        }
        MethodResult result =  new MethodResult(MethodResult.SUCCESS,"已经挂载");
        
        return result;
    }
     
 
    /**
     * 
    * @Title: executeShell 
    * @Description: 查询绑定情况
    * @param @return
    * @param @throws IOException      
    * @return Map<String,String>     
    * @throws
     */
    public Map<String,String> executeShellOfQueryNas(String ip) throws IOException { 
        Map<String,String> temp = new HashMap<String,String>();
        try {
            Process pro = Runtime.getRuntime().exec("showmount -e "+ip);
            BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
             int i = 0;
            String str = br.readLine();
            while(!StringUtils.isBlank(str)){
                if(i == 0){
                    continue;
                }else{
                    String [] ips = StringUtil.getIpFromUrl(str); 
                    if(ips != null && ips.length >= 1){
                        temp.put(ips[0], str.replace(ips[0], ""));
                    } 
                }                 
                str = br.readLine();
            }
        } catch (Exception ioe) {
            ioe.printStackTrace();
        } 
        return temp;
     } 
    
    public int executeShellOfMount(String url) throws IOException { 
        Map<String,String> temp = new HashMap<String,String>();
        try {
            Process pro = Runtime.getRuntime().exec("mount -t nfs "+url+" /image/");
            BufferedReader br = new BufferedReader(new InputStreamReader(pro.getInputStream()));
             int i = 0;
            String str = br.readLine();
            while(!StringUtils.isBlank(str)){
                if(i == 0){
                    continue;
                }else{
                    String [] ips = StringUtil.getIpFromUrl(str); 
                    if(ips != null && ips.length >= 1){
                        temp.put(ips[0], str.replace(ips[0], ""));
                    } 
                }                 
                str = br.readLine();
            }
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }  
        return 1;
     } 
    

}
