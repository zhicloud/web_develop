package com.zhicloud.ms.controller;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.ResponseBody;

import com.zhicloud.ms.app.pool.CloudHostData;
import com.zhicloud.ms.app.pool.CloudHostPoolManager; 
import com.zhicloud.ms.constant.AppConstant;
import com.zhicloud.ms.constant.AppInconstant;
import com.zhicloud.ms.exception.AppException;
import com.zhicloud.ms.httpGateway.HttpGatewayAsyncChannel;
import com.zhicloud.ms.httpGateway.HttpGatewayChannelExt;
import com.zhicloud.ms.httpGateway.HttpGatewayManager;
import com.zhicloud.ms.httpGateway.HttpGatewayResponseHelper;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.ICloudHostService;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.util.TransFormPrivilegeUtil;
import com.zhicloud.ms.vo.CloudHostVO;
import com.zhicloud.ms.app.pool.computePool.ComputeInfoExt;
/**
 * 
* @ClassName: HostMigrateController 
* @Description: TODO 
* @author sasa
* @date 2015年10月10日 上午10:40:49 
*
 */
@Controller
public class HostMigrateController {
    
    @Resource
    private ICloudHostService cloudHostService;
    
    public static final Logger logger = Logger.getLogger(EmailTemplateController.class);
    
    /**
     * 
    * @Title: toMigratePage 
    * @Description: 到迁移页面 
    * @param @param id
    * @param @param model
    * @param @param request
    * @param @return      
    * @return String     
    * @throws
     */
    @RequestMapping(value="/cloudserver/{id}/migrate",method=RequestMethod.GET) 
    public String toMigratePage(@PathVariable("id") String id,Model model,HttpServletRequest request){
        CloudHostData realdata = CloudHostPoolManager.getSingleton().getCloudHostFromPool(id);
        CloudHostVO host = cloudHostService.getByRealHostId(id);
        HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1); 
        try {
            JSONObject result = channel.computePoolQueryResource(realdata.getPoolId());
            JSONArray computerList = result.getJSONArray("compute_resources");
            List<ComputeInfoExt> cList = new ArrayList<>();
            for (int i = 0; i < computerList.size(); i ++) {
                JSONObject computerObject = computerList.getJSONObject(i);
                String name = computerObject.getString("name");
                String ip = computerObject.getString("ip");
                if(ip.equals(realdata.getInnerIp())){
                    continue;
                }
                int status = computerObject.getInt("status");
                if(status != 0){
                    continue;
                } 
                
                ComputeInfoExt computer = new ComputeInfoExt();
                computer.setName(name); 
                cList.add(computer);
            }
            model.addAttribute("cList", cList);
            model.addAttribute("host", host);
            if(host.getRunningStatus().equals(AppConstant.CLOUD_HOST_RUNNING_STATUS_RUNNING)){
                model.addAttribute("description", "主机当前是开机状态，需关机才能进行冷迁移");
            }
            if(host.getRunningStatus().equals(AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN)){
                model.addAttribute("description", "主机当前是关机状态，可进行冷迁移");
            }
            Object progress = AppInconstant.cloudHostMigrate.get(id);
            
            if(progress!=null && Integer.parseInt(progress.toString())<=100){
                model.addAttribute("isMigrate", true);
            }else{
                model.addAttribute("isMigrate", false);
            }
            
        } catch (Exception e) { 
            e.printStackTrace();
        }
        return "/server/migrate_page";

    }
    
    /**
     * 
    * @Title: migrate 
    * @Description: 主机迁移命令 
    * @param @param type
    * @param @param nodeName
    * @param @param hostId
    * @param @param request
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    @RequestMapping(value="/cloudserver/migrate",method=RequestMethod.POST)
    @ResponseBody
    public MethodResult migrate(Integer type,String nodeName,String hostId,HttpServletRequest request) {
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.server_migrate)){
            return new MethodResult(MethodResult.FAIL,"您没有迁移的权限，请联系管理员");
        }
        String sessionId = null;
        Map<String, Object> parameter = new LinkedHashMap<String, Object>();
        CloudHostVO host = cloudHostService.getById(hostId);
          
        try{
            HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(1);
            sessionId = channel.getSessionId(); 
            JSONObject result = channel.hostMigrate(host.getRealHostId(), nodeName, type);
            if (HttpGatewayResponseHelper.isSuccess(result) == true) {
                AppInconstant.cloudHostMigreateSessionId.put(sessionId, hostId);
                AppInconstant.cloudHostMigrate.put(hostId, 0);
                 return new MethodResult(MethodResult.SUCCESS,"迁移开始");
            } else {
                channel.release();
                return new MethodResult(MethodResult.FAIL,"迁移失败");
            }
            
  
            
             
        } catch (Exception e) {
            System.err.println("fail to send migrate host request.");
            e.printStackTrace();
            return new MethodResult(MethodResult.FAIL, "迁移失败");
        }
    }
    
    /**
     * 
    * @Title: getMigrateResult 
    * @Description: 迁移结果查询 
    * @param @param uuid
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    @RequestMapping(value="/cloudserver/getmigrateprogress",method=RequestMethod.GET)
    @ResponseBody
    public MethodResult getMigrateResult(String uuid) {
        try{     
            Object progress = AppInconstant.cloudHostMigrate.get(uuid);
            if(progress == null){
                MethodResult message = new MethodResult(MethodResult.FAIL, "迁移失败");
                return message;         
            }    
            MethodResult result = new MethodResult(MethodResult.SUCCESS); 
            result.put("progress", progress); 
            return result;
                     
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(e);
        }
    }


}
