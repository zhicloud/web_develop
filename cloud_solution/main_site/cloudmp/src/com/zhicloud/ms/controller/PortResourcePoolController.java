package com.zhicloud.ms.controller;

import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhicloud.ms.common.util.json.JSONLibUtil;
import com.zhicloud.ms.httpGateway.HttpGatewayChannelExt;
import com.zhicloud.ms.httpGateway.HttpGatewayManager;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.IOperLogService;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.util.TransFormPrivilegeUtil;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.PortResourcePoolDetailVO;
import com.zhicloud.ms.vo.PortResourcePoolVO;

@Controller
@RequestMapping("/portresourcepool")
public class PortResourcePoolController {
    @Resource
    private IOperLogService operLogService;
    
    public static final Logger logger = Logger.getLogger(PortResourcePoolController.class);
    
    /**
     * 
    * @Title: getAll 
    * @Description: 查询地址资源池 
    * @param @param model
    * @param @param request
    * @param @return      
    * @return String     
    * @throws
     */
    @RequestMapping(value="/all",method=RequestMethod.GET)
    public String getAll(Model model,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.port_resource_pool_query)){
            return "not_have_access";
        }
        try {
            List<PortResourcePoolVO> cList = new ArrayList<PortResourcePoolVO>();
            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
            if(channel!=null){
                JSONObject  result = channel.portPoolQuery();
                if("success".equals(result.get("status"))){                 
                    JSONArray ipList = result.getJSONArray("portPools");
                    for (int i = 0; i < ipList.size(); i ++) {
                        JSONObject ipObject = ipList.getJSONObject(i);
                        String uuid = ipObject.getString("uuid");
                        String name = ipObject.getString("name");
                        int status = ipObject.getInt("status");
                        BigInteger[] count = JSONLibUtil.getBigIntegerArray(ipObject, "count");
                         
                        PortResourcePoolVO port = new PortResourcePoolVO(); 
                        port.setName(name);
                        port.setStatus(status);
                        port.setUuid(uuid);
                        port.setCount(count);
                        cList.add(port);
                    }
                }
            }
            
            model.addAttribute("portPool", cList);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "resourcepool/all_port_resource_pool_manage";
    }
    
    /**
     * 
    * @Title: add
    * @Description: 打开创建端口资源池页面 
    * @param @param model
    * @param @param request     
    * @return String     
    * @throws
     */
    @RequestMapping(value="/add",method=RequestMethod.GET)
    public String portResourcePoolCreate(Model model,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.port_resource_pool_add)){
            return "not_have_access";
        }
        return "resourcepool/port_resource_pool_create";
    }
    
    /**
     * 
    * @Title: add 
    * @Description: 保存创建端口资源池
    * @param @param name      
    * @return MethodResult     
    * @throws
     */
    @RequestMapping(value="/add",method=RequestMethod.POST)
    @ResponseBody 
    public MethodResult portResourcePoolAdd(String name,HttpServletRequest request){  
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.port_resource_pool_add)){
            return new MethodResult(MethodResult.FAIL,"对不起，您无添加端口资源池的权限！");
        }       
        
        if(StringUtil.isBlank(name)){
            operLogService.addLog("端口资源池", "创建端口资源池失败", "1", "2", request);
            return new MethodResult(MethodResult.FAIL,"端口资源池名不能为空");
        }
        try {
            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
            if(channel!=null){
                
                JSONObject  resultList = channel.portPoolQuery();
                if("success".equals(resultList.get("status"))){                 
                    JSONArray ipList = resultList.getJSONArray("portPools");
                    for (int i = 0; i < ipList.size(); i ++) {
                        JSONObject ipObject = ipList.getJSONObject(i);
                        if  (ipObject.getString("name").equals(name)){
                            operLogService.addLog("端口资源池", "创建端口资源池失败", "1", "2", request);
                            return new MethodResult(MethodResult.FAIL,"该名字的端口资源池已经存在");
                        }
                    }
                } 
//                else{
//                    operLogService.addLog("端口资源池", "创建端口资源池失败", "1", "2", request);
//
//                    return new MethodResult(MethodResult.FAIL,"校验端口资源池名失败！");
//                }
                
                JSONObject result = channel.portPoolCreate(name);
                if("success".equals(result.get("status"))){
                    operLogService.addLog("端口资源池", "创建端口资源池成功", "1", "1", request);
                    return new MethodResult(MethodResult.SUCCESS,"创建成功");
                }
                operLogService.addLog("端口资源池", "创建端口资源池失败", "1", "2", request);
                return new MethodResult(MethodResult.FAIL,"创建失败: gateway返回失败！");
                
            }
        } catch (Exception e) {
            e.printStackTrace();
            operLogService.addLog("端口资源池", "创建端口资源池失败", "1", "2", request);
    
            return new MethodResult(MethodResult.FAIL,"创建失败: 调用 gateway失败！");
        }
            operLogService.addLog("端口资源池", "创建端口资源池失败", "1", "2", request);
    
        return new MethodResult(MethodResult.FAIL,"创建失败");
    }
    
    /**
     * 
    * @Title: delete 
    * @Description: 删除端口资源池
    * @param @param uuid      
    * @return MethodResult     
    * @throws
     */
    @RequestMapping(value="/rm",method=RequestMethod.POST) 
    @ResponseBody
    public MethodResult portResourcePoolDelete(String uuid,HttpServletRequest request){   
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.port_resource_pool_remove)){
            return new MethodResult(MethodResult.FAIL,"对不起，您无删除端口资源池的权限！");
        }       
        try {
            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
            if(channel!=null){
                JSONObject result = channel.portPoolDelete(uuid);
                if("success".equals(result.get("status"))){
                    operLogService.addLog("端口资源池", "删除端口资源池失败", "1", "2", request);
                    return new MethodResult(MethodResult.SUCCESS,"删除端口资源池成功");
                }
                operLogService.addLog("端口资源池", "删除端口资源池失败", "1", "2", request);
                return new MethodResult(MethodResult.FAIL,"删除端口资源池: gateway返回失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            operLogService.addLog("端口资源池", "删除端口资源池失败", "1", "2", request);
            return new MethodResult(MethodResult.FAIL,"删除端口资源池：调用gateway失败!");
        }
        operLogService.addLog("端口资源池", "删除端口资源池失败", "1", "2", request);
        return new MethodResult(MethodResult.FAIL,"删除端口资源池失败");
    }
    /**
     * 
    * @Title: portResourceDetail 
    * @Description: 查询端口资源
    * @param @param name      
    * @return MethodResult     
    * @throws
     */
    @RequestMapping(value="/{uuid}/qn",method=RequestMethod.GET)
    public String portResourceDetail(@PathVariable("uuid") String uuid, Model model,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.port_resource_pool_detail_query)){
            return "not_have_access";
        }
        
        try {            
            List<PortResourcePoolDetailVO> cList = new ArrayList<PortResourcePoolDetailVO>();
            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
            if(channel!=null){
                JSONObject result = channel.portPoolQueryResource(uuid);
                if("success".equals(result.get("status"))){
                    JSONArray portsList = result.getJSONArray("ports");
                    for (int i = 0; i < portsList.size(); i ++) {
                        PortResourcePoolDetailVO portVO = new PortResourcePoolDetailVO();
                        JSONObject portObject = portsList.getJSONObject(i);
                        portVO.setIp(portObject.getString("ip"));
                        portVO.setStatus(portObject.getInt("status"));
                        BigInteger[] count = JSONLibUtil.getBigIntegerArray(portObject, "count");
                        portVO.setCount(count);
                        cList.add(portVO);
                    }
                }
            }
            model.addAttribute("portPoolDetailList", cList);
            model.addAttribute("poolId", uuid);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "resourcepool/port_resource_pool_detail";
    }
    
    /**
     * 删除端口资源(节点)
     * @param ip
     * @param poolId
     * @return
     */
    @RequestMapping(value="/dn",method=RequestMethod.POST)
    @ResponseBody
    public MethodResult deleteNode(@RequestParam("ip") String ip,
                                   @RequestParam("poolId") String poolId,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.port_resource_pool_detail_remove)){
            return new MethodResult(MethodResult.FAIL,"对不起，您无移除端口资源的权限!");
        }       
        try {
            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
            if(channel!=null){
                JSONObject dnr = channel.portPoolRemoveResource(poolId, ip);
                if("success".equals(dnr.getString("status"))){
                    operLogService.addLog("端口资源池", "删除端口资源成功", "1", "1", request);
                    return new MethodResult(MethodResult.SUCCESS,"端口资源移除成功");
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            operLogService.addLog("端口资源池", "删除端口资源失败", "1", "2", request);
            return new MethodResult(MethodResult.FAIL,"端口资源移除：调用gateway失败!");
        } catch (IOException e) {
            e.printStackTrace();
            operLogService.addLog("端口资源池", "删除端口资源失败", "1", "2", request);
            return new MethodResult(MethodResult.FAIL,"端口资源移除：调用gateway失败!");
        }
        operLogService.addLog("端口资源池", "删除端口资源失败", "1", "2", request);
        return new MethodResult(MethodResult.FAIL,"端口资源移除：gateway返回失败！");
    }   
    
    /**
     * 
    * @Title: add 
    * @Description: 打开新增端口资源页面 
    * @param @param model
    * @param @param request     
    * @return String     
    * @throws
     */
    @RequestMapping(value="/{uuid}/an",method=RequestMethod.GET)
    public String portResourcePoolAdd(@PathVariable("uuid") String uuid,Model model,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.port_resource_pool_detail_add)){
            return "not_have_access";
        }
        model.addAttribute("poolId", uuid);
        return "resourcepool/port_resource_pool_detail_add";
    }
    
    /**
     * 
    * @Title: add 
    * @Description: 新增端口资源
    * @param @param model
    * @param @param request     
    * @return String     
    * @throws
     */
    @RequestMapping(value="/{uuid}/an",method=RequestMethod.POST)
    @ResponseBody
    public MethodResult portResourcePoolAdd(@PathVariable("uuid") String uuid,String ip,String range,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.port_resource_pool_detail_add)){
            return new MethodResult(MethodResult.FAIL,"对不起，您无创建端口资源的权限!");
        }   
        if(StringUtil.isBlank(ip)){
            operLogService.addLog("端口资源池", "新增端口资源失败", "1", "2", request);
            return new MethodResult(MethodResult.FAIL,"起始IP地址不能为空");
        }
        if(StringUtil.isBlank(range)){
            operLogService.addLog("端口资源池", "新增端口资源失败", "1", "2", request);
            return new MethodResult(MethodResult.FAIL,"IP数量不能为空");
        }
        try {
            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
            if(channel!=null){
                String[] ips = new String[]{ip};
                //Integer[] ranges = new Integer[]{Integer.valueOf(range)};
                // JSONObject result = channel.addressPoolAddResource(uuid, ips, ranges);
                JSONObject result = channel.portPoolAddResource(uuid, ip, range);
                if("success".equals(result.getString("status"))){
                    operLogService.addLog("端口资源池", "新增端口资源成功", "1", "1", request);
                    return new MethodResult(MethodResult.SUCCESS,"创建成功！");
                }
                operLogService.addLog("端口资源池", "新增端口资源失败", "1", "2", request);
                return new MethodResult(MethodResult.FAIL,"创建端口资源：gateway返回"+result.getString("message"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            operLogService.addLog("端口资源池", "新增端口资源失败", "1", "2", request);
            return new MethodResult(MethodResult.FAIL,"创建端口资源：调用gateway失败!");
        }
        operLogService.addLog("端口资源池", "新增端口资源失败", "1", "2", request);
        return new MethodResult(MethodResult.FAIL,"创建端口资源失败!");
    }
}
