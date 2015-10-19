package com.zhicloud.ms.controller;

import java.io.IOException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.zhicloud.ms.app.pool.isoImagePool.IsoImageProgressData;
import com.zhicloud.ms.app.pool.isoImagePool.IsoImageProgressPool;
import com.zhicloud.ms.app.pool.isoImagePool.IsoImageProgressPoolManager;
import com.zhicloud.ms.service.IOperLogService;
import com.zhicloud.ms.service.IsoImageService;
import com.zhicloud.ms.service.impl.EmailTemplateServiceImpl;
import com.zhicloud.ms.util.ServiceUtil;
@Controller
public class IsoImageController {
    
    public static final Logger logger = Logger.getLogger(EmailTemplateServiceImpl.class);

    
    @Resource
    private IOperLogService operLogService;
    
    @Resource
    private IsoImageService isoImageService;
    
    
    @RequestMapping(value="/isoimage/all",method=RequestMethod.GET)
    public String toAll(Model model,HttpServletRequest request, HttpServletResponse response) throws IOException
    { 
        IsoImageProgressPool pool = IsoImageProgressPoolManager.singleton().getPool();
        IsoImageProgressData[] isoArray = pool.getAll();
        model.addAttribute("isoArray", isoArray);
        return "isoimage/iso_image_manage";
    }
    
    @RequestMapping(value="/isoimage/add",method=RequestMethod.GET)
    public String addPage(HttpServletRequest request, HttpServletResponse response) throws IOException
    { 
           return "isoimage/iso_image_add";
    }
    
    @RequestMapping(value="/isoimage/add",method=RequestMethod.POST)
    public String add(String name,String diskType,String filename,String description,HttpServletRequest request, HttpServletResponse response) throws IOException
    { 
        MultipartHttpServletRequest  multipartRequest = (MultipartHttpServletRequest) request;
        
        MultipartFile attach = multipartRequest.getFile("filename");
//        isoImageService.upload(region, filename, fileStream, name, description, group, user);
           return "isoimage/iso_image_add";
    }
    

}
