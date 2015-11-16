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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.zhicloud.ms.app.helper.AppHelper;
import com.zhicloud.ms.app.pool.IsoImagePool;
import com.zhicloud.ms.app.pool.IsoImagePool.IsoImageData;
import com.zhicloud.ms.app.pool.IsoImagePoolManager;
import com.zhicloud.ms.app.pool.isoImagePool.IsoImageProgressData;
import com.zhicloud.ms.app.pool.isoImagePool.IsoImageProgressPool;
import com.zhicloud.ms.app.pool.isoImagePool.IsoImageProgressPoolManager;
import com.zhicloud.ms.common.util.StringUtil;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.IOperLogService;
import com.zhicloud.ms.service.IsoImageService;
import com.zhicloud.ms.service.SharedMemoryService;
import com.zhicloud.ms.service.impl.EmailTemplateServiceImpl;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.util.TransFormPrivilegeUtil;
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
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.iso_image_query)){
            return "not_have_access";
        }
//        IsoImageProgressPool pool = IsoImageProgressPoolManager.singleton().getPool();
        IsoImagePool pool = IsoImagePoolManager.getSingleton().getIsoImagePool();
        List<IsoImageData> isoArray = pool.getAllIsoImageData();
        model.addAttribute("isoArray", isoArray);
         return "isoimage/iso_image_manage";
    }
     
    /**
     * 
    * @Title: startCloudHost 
    * @Description: 删除镜像 
    * @param @param id
    * @param @param request
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    @RequestMapping(value="/isoimage/{id}/delete",method=RequestMethod.GET)
    @ResponseBody
    public MethodResult startCloudHost(@PathVariable("id") String id,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.iso_image_delete)){
            return new MethodResult(MethodResult.FAIL,"您没有删除光盘镜像的权限，请联系管理员");
        }
        IsoImagePool pool = IsoImagePoolManager.getSingleton().getIsoImagePool();
        IsoImageData data = pool.getByRealImageId(id);
        MethodResult mr = isoImageService.delete(id);
        if(mr.isSuccess()){
            operLogService.addLog("iso镜像", "删除镜像"+data.getName(), "1", "1", request);
        }else{
            operLogService.addLog("iso镜像", "删除镜像"+data.getName(), "1", "2", request);
        }
        return mr;
    }
    

}
