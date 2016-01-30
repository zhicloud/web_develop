package com.zhicloud.ms.controller;

import com.zhicloud.ms.constant.AppConstant;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.ICloudHostService;
import com.zhicloud.ms.service.IOperLogService;
import com.zhicloud.ms.service.IQosService;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.util.TransFormPrivilegeUtil;
import com.zhicloud.ms.vo.CloudHostVO;
import com.zhicloud.ms.vo.QosVO;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * Created by sean on 6/23/15.
 */

@Controller
public class QosController {

    public static final Logger logger = Logger.getLogger(QosController.class);


    @Resource
    private IQosService qosService;
    @Resource
    private ICloudHostService cloudHostService;

    @Resource
    private IOperLogService operLogService;

    @RequestMapping(value="/networkrule/desktopqos/all",method= RequestMethod.GET)
    public String desktopList(Model model,HttpServletRequest request) throws UnsupportedEncodingException {

        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_qos_query)){
            return "not_have_access";
        }

        Map<String, Object> condition = new LinkedHashMap<String, Object>();
        condition.put("type", AppConstant.HOST_TYPE_DESKTOP);

        List<QosVO> qosVOList = qosService.getAll(AppConstant.HOST_TYPE_DESKTOP);
        model.addAttribute("qos_vo_list", qosVOList);
        model.addAttribute("type", AppConstant.HOST_TYPE_DESKTOP);
        return "qos/qos_manage";
    }

    @RequestMapping(value="/serverqos/all",method= RequestMethod.GET)
    public String serverList(Model model,HttpServletRequest request) throws UnsupportedEncodingException {

        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.server_qos_query)){
            return "not_have_access";
        }

        Map<String, Object> condition = new LinkedHashMap<String, Object>();
        condition.put("type", AppConstant.HOST_TYPE_SERVER);

        List<QosVO> qosVOList = qosService.getAll(AppConstant.HOST_TYPE_SERVER);
        model.addAttribute("qos_vo_list", qosVOList);
        model.addAttribute("type", AppConstant.HOST_TYPE_SERVER);
        return "qos/qos_manage";
    }

    @RequestMapping(value="/networkrule/desktopqos/add",method= RequestMethod.GET)
    public String desktopAddPage(Model model, HttpServletRequest request){

        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_qos_add)){
            return "not_have_access";
        }

        List<CloudHostVO> cloudHostVOList = cloudHostService.getAllHost();

        List<QosVO> qosVOs = qosService.getAll(AppConstant.HOST_TYPE_DESKTOP);


        Iterator<QosVO> qosVOIterator = qosVOs.iterator();

        while (qosVOIterator.hasNext()) {
            QosVO qosVO = qosVOIterator.next();
            Iterator<CloudHostVO> cloudHostVOIterator = cloudHostVOList.iterator();
            while (cloudHostVOIterator.hasNext()) {
                CloudHostVO cloudHostVO = cloudHostVOIterator.next();
                if (qosVO.getUuid().equals(cloudHostVO.getRealHostId())) {
                    cloudHostVOIterator.remove();
                    break;
                }
            }

        }

        model.addAttribute("cloud_host_vo_list", cloudHostVOList);
        model.addAttribute("type", AppConstant.HOST_TYPE_DESKTOP);

        return "qos/qos_add";

    }

    @RequestMapping(value="/serverqos/add",method= RequestMethod.GET)
    public String serverAddPage(Model model, HttpServletRequest request){

        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.server_qos_add)){
            return "not_have_access";
        }

        List<CloudHostVO> cloudHostVOList = cloudHostService.getAllServer();

        List<QosVO> qosVOs = qosService.getAll(AppConstant.HOST_TYPE_SERVER);


        Iterator<QosVO> qosVOIterator = qosVOs.iterator();

        while (qosVOIterator.hasNext()) {
            QosVO qosVO = qosVOIterator.next();
            Iterator<CloudHostVO> cloudHostVOIterator = cloudHostVOList.iterator();
            while (cloudHostVOIterator.hasNext()) {
                CloudHostVO cloudHostVO = cloudHostVOIterator.next();
                if (qosVO.getUuid().equals(cloudHostVO.getRealHostId())) {
                    cloudHostVOIterator.remove();
                    break;
                }
            }

        }

        model.addAttribute("cloud_host_vo_list", cloudHostVOList);
        model.addAttribute("type", AppConstant.HOST_TYPE_SERVER);

        return "qos/qos_add";

    }

    @RequestMapping(value="/networkrule/qos/add",method= RequestMethod.POST)
    @ResponseBody
    public MethodResult add(QosVO qosVO, HttpServletRequest request){

        Integer type = qosVO.getType();

        switch (type) {
            case 1: {
                if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_qos_add)){
                    return new MethodResult(MethodResult.FAIL,"您没有新增QoS规则的权限，请联系管理员");
                }
                break;
            }

            case 2: {
                if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.server_qos_add)){
                    return new MethodResult(MethodResult.FAIL,"您没有新增QoS规则的权限，请联系管理员");
                }
                break;
            }

            case 3: {
                if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.vpc_qos_add)){
                    return new MethodResult(MethodResult.FAIL,"您没有新增QoS规则的权限，请联系管理员");
                }
                break;
            }
        }

        String str = qosVO.getUuid();
        String[] strArr = str.split(";");

        String uuid = strArr[0];
        String hostName = strArr[1];

        Map<String, Object> data = new LinkedHashMap<String, Object>();
        data.put("uuid", uuid);
        data.put("host_name", hostName);
        data.put("name", qosVO.getName());
        data.put("type", qosVO.getType());
        data.put("inbound_bandwidth", qosVO.getInboundBandwidth());
        data.put("outbound_bandwidth", qosVO.getOutboundBandwidth());
        data.put("max_iops", qosVO.getMaxIops());
        data.put("priority", qosVO.getPriority());

        MethodResult result = qosService.addQos(data);

        if(MethodResult.SUCCESS.equals(result.status)) {
            operLogService.addLog("Qos规则管理", "新增Qos规则"+qosVO.getName()+"成功", "1", "1", request);
            return new MethodResult(result.status,result.message);
        }
        operLogService.addLog("Qos规则管理", "新增Qos规则" + qosVO.getName() + "失败", "1", "2", request);
        return new MethodResult(result.status,result.message);
    }

    @RequestMapping(value="/networkrule/qos/delete",method= RequestMethod.POST)
    @ResponseBody
    public MethodResult deleteBoxes(@RequestParam("ids[]") String[] ids, @RequestParam("type")int type, HttpServletRequest request){

        switch (type) {
            case 1: {
                if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_qos_remove)){
                    return new MethodResult(MethodResult.FAIL,"您没有删除QoS规则的权限，请联系管理员");
                }
                break;
            }
            case 2: {
                if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.server_qos_remove)){
                    return new MethodResult(MethodResult.FAIL,"您没有删除QoS规则的权限，请联系管理员");
                }
                break;
            }
            case 3: {
                if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.vpc_qos_remove)){
                    return new MethodResult(MethodResult.FAIL,"您没有删除QoS规则的权限，请联系管理员");
                }
                break;
            }
            default:{
                if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_qos_remove)){
                    return new MethodResult(MethodResult.FAIL,"您没有删除QoS规则的权限，请联系管理员");
                }
                break;
            }
        }

        List<String> idsList = Arrays.asList(ids);

        MethodResult result = qosService.removeQos(idsList);

        if(MethodResult.SUCCESS.equals(result.status)) {
            operLogService.addLog("Qos规则管理", "删除QoS规则成功", "1", "1", request);
            return new MethodResult(result.status,result.message);
        }
        operLogService.addLog("Qos规则管理", "删除QoS规则失败", "1", "2", request);
        return new MethodResult(result.status,result.message);
    }

    @RequestMapping(value="/networkrule/qos/check",method= RequestMethod.GET)
    @ResponseBody
    public MethodResult checkName(HttpServletRequest request) throws UnsupportedEncodingException {

        Integer type = Integer.valueOf(request.getParameter("type"));
        String name = new String(request.getParameter("name").getBytes("ISO-8859-1"),"UTF-8");

        Map<String, Object> condition = new LinkedHashMap<String, Object>();
        condition.put("type", type);
        condition.put("name", name);

        MethodResult result = qosService.checkName(condition);

        if (MethodResult.FAIL.equals(result.status)) {
            return new MethodResult(result.status, result.message);

        }
        return new MethodResult(result.status, result.message);
    }

}
