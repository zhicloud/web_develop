package com.zhicloud.ms.controller;

import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.IOperLogService;
import com.zhicloud.ms.service.ImageUploadAddressService;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.util.TransFormPrivilegeUtil;
import com.zhicloud.ms.vo.ImageUploadAddressVO;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @author 张翔
 * @description 上传镜像地址管理controller
 */
@Controller
@RequestMapping("/image/image_upload_address")
public class ImageUploadAddressController {

    public static final Logger logger = Logger.getLogger(ImageUploadAddressController.class);

    @Resource private ImageUploadAddressService imageUploadAddressService;

    @Resource private IOperLogService operLogService;


    @RequestMapping(value="/all",method= RequestMethod.GET)
    public String getAll(Model model,HttpServletRequest request) throws IOException {

        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.image_upload_address_query)){
            return "not_have_access";
        }

        List<ImageUploadAddressVO> imageUploadAddressVOs = imageUploadAddressService.getAllAddress();

        model.addAttribute("image_upload_address", imageUploadAddressVOs);

        return "image_upload_address/image_upload_address_manage";
    }

    @RequestMapping(value="/add",method= RequestMethod.GET)
    public String addPage(Model model,HttpServletRequest request) throws IOException {

        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.image_upload_address_add)){
            return "not_have_access";
        }

        //  获取服务名 内网IP 内网端口
        List<ImageUploadAddressVO> imageUploadAddressVOs = imageUploadAddressService.getAvailableAddressFromPlatform();
        model.addAttribute("image_upload_address_list", imageUploadAddressVOs);

        return "image_upload_address/image_upload_address_add";
    }

    @RequestMapping(value="/add",method= RequestMethod.POST)
    @ResponseBody
    public MethodResult add(ImageUploadAddressVO imageUploadAddressVO, HttpServletRequest request)
        throws IOException {
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.image_upload_address_add)){
            return new MethodResult(MethodResult.FAIL,"您没有新增上传地址的权限，请联系管理员");
        }

        MethodResult result = updateAddress(imageUploadAddressVO, request);

        return result;
    }

    @RequestMapping(value="/{service_name}/mod",method= RequestMethod.GET)
    public String modifyPage(@PathVariable("service_name") String serviceName, Model model,HttpServletRequest request)
        throws IOException {

        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.image_upload_address_mod)){
            return "not_have_access";
        }

        ImageUploadAddressVO imageUploadAddressVO = imageUploadAddressService.getAddressByServiceName(serviceName);
        //  获取服务名 内网IP 内网端口
        List<ImageUploadAddressVO> imageUploadAddressVOs = imageUploadAddressService.getAvailableAddressFromPlatform();
        imageUploadAddressVOs.add(imageUploadAddressVO);

        model.addAttribute("image_upload_address", imageUploadAddressVO);
        model.addAttribute("image_upload_address_list", imageUploadAddressVOs);

        return "image_upload_address/image_upload_address_mod";
    }

    @RequestMapping(value="/mod",method= RequestMethod.POST)
    @ResponseBody
    public MethodResult modify(ImageUploadAddressVO imageUploadAddressVO, HttpServletRequest request)
        throws IOException {

        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.image_upload_address_mod)){
            return new MethodResult(MethodResult.FAIL,"您没有新增上传地址的权限，请联系管理员");

        }

        MethodResult result = updateAddress(imageUploadAddressVO, request);

        return result;
    }

    // 新增和修改复用方法
    private MethodResult updateAddress(ImageUploadAddressVO imageUploadAddressVO, HttpServletRequest request)
        throws IOException {

        MethodResult testResult = imageUploadAddressService.testAvaliable(imageUploadAddressVO);

        if (MethodResult.FAIL.equals(testResult.status)) {
            return testResult;
        }

        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("service_name", imageUploadAddressVO.getServiceName());
        data.put("local_ip", imageUploadAddressVO.getLocalIp());
        data.put("local_port", imageUploadAddressVO.getLocalPort());
        data.put("public_ip", imageUploadAddressVO.getPublicIp());
        data.put("public_port", imageUploadAddressVO.getPublicPort());
        data.put("service_enable", imageUploadAddressVO.getServiceEnable());
        data.put("description", imageUploadAddressVO.getDescription());

        MethodResult result = imageUploadAddressService.modifyAddress(data);

        String flag = "1";

        if (MethodResult.FAIL.equals(result.status)) {
            flag = "2";
        }

        operLogService.addLog("镜像上传地址", result.message, "1", flag, request);

        return result;
    }

    @RequestMapping(value="/{service_name}/remove",method= RequestMethod.GET)
    @ResponseBody
    public MethodResult remove(@PathVariable("service_name") String serviceName, HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.image_upload_address_delete)){
            return new MethodResult(MethodResult.FAIL,"您没有删除上传地址的权限，请联系管理员");
        }
        List<String> serviceNames = new ArrayList<String>();
        serviceNames.add(serviceName);

        String flag = "1";
        MethodResult result = imageUploadAddressService.removeAddress(serviceNames);

        String status = result.status;
        String message = result.message;

        if(MethodResult.FAIL.equals(status)) {
            flag = "2";
        }
        operLogService.addLog("镜像上传地址", message, "1", flag, request);
        return result;
    }

    @RequestMapping(value="/remove",method= RequestMethod.POST)
    @ResponseBody
    public MethodResult remove(@RequestParam("ids[]") String[] serviceNames, HttpServletRequest request) throws UnsupportedEncodingException {

        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.image_upload_address_delete)){
            return new MethodResult(MethodResult.FAIL,"您没有删除上传地址的权限，请联系管理员");

        }

        List<String> Names = Arrays.asList(serviceNames);

        MethodResult result = imageUploadAddressService.removeAddress(Names);

        String flag = "1";

        String status = result.status;
        String message = result.message;

        if(MethodResult.FAIL.equals(status)) {
            flag = "2";
        }
        operLogService.addLog("镜像上传地址", message, "1", flag, request);
        return result;
    }

}
