package com.zhicloud.ms.controller;


import com.zhicloud.ms.app.pool.computePool.ComputeInfoExt;
import com.zhicloud.ms.app.pool.computePool.ComputeInfoPoolManager;
import com.zhicloud.ms.app.pool.host.back.HostBackupProgressData;
import com.zhicloud.ms.app.pool.host.back.HostBackupProgressPool;
import com.zhicloud.ms.app.pool.host.back.HostBackupProgressPoolManager;
import com.zhicloud.ms.common.util.json.JSONLibUtil;
import com.zhicloud.ms.constant.AppConstant;
import com.zhicloud.ms.constant.AppInconstant;
import com.zhicloud.ms.exception.AppException;
import com.zhicloud.ms.httpGateway.HttpGatewayAsyncChannel;
import com.zhicloud.ms.httpGateway.HttpGatewayChannelExt;
import com.zhicloud.ms.httpGateway.HttpGatewayManager;
import com.zhicloud.ms.httpGateway.HttpGatewayResponseHelper;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.*;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.service.ManSysUserService;
import com.zhicloud.ms.transform.util.TransFormLoginHelper;
import com.zhicloud.ms.transform.util.TransFormLoginInfo;
import com.zhicloud.ms.transform.util.TransFormPrivilegeUtil;
import com.zhicloud.ms.util.CapacityUtil;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.BackUpDetailVO;
import com.zhicloud.ms.vo.CloudHostConfigModel;
import com.zhicloud.ms.vo.CloudHostVO;
import com.zhicloud.ms.vo.SharedMemoryVO;
import com.zhicloud.ms.vo.SysDiskImageVO;
import com.zhicloud.ms.vo.SysTenant;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.*;


@Controller
@RequestMapping("/tenant")
public class SysTenantController {
	
	
	public static final Logger logger = Logger.getLogger(CloudServerController.class);

	@Resource
	ItenantService tenantService;
	
    
    @Resource
    ManSysUserService manSysUserService;
    
    @Resource 
    IOperLogService  logService;
    @Resource
    private ICloudHostService cloudHostService;
    
    @Resource
    private IBackUpDetailService backUpDetailService;
    
    @Resource
    CloudHostConfigModelService cloudHostConfigModelService;
    
    @Resource
    ISysDiskImageService sysDiskImageService;
    @Resource
    private SharedMemoryService sharedMemoryService;
    
	/**
	 * 查询所有租户
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/all",method=RequestMethod.GET)
	public String getAll(Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.tenant_manager)){
			return "not_have_access";
		}
		TransFormLoginInfo loginInfo = TransFormLoginHelper.getLoginInfo(request);
		List<SysTenant> list = new ArrayList<SysTenant>();
		if(loginInfo.getUserType() == 0){
		    list = tenantService.getAllSysTenant(new SysTenant()); 
		}else{
		    list = tenantService.getAllSysTenantByUserId(loginInfo.getBillid());
		}
 		model.addAttribute("tenantList", list);
 		model.addAttribute("userType", loginInfo.getUserType());
 		return "tenant_manage";
	}
	
	/**
	 * 
	* @Title: add 
	* @Description: 打开新增租户页面 
	* @param @param model
	* @param @param request     
	* @return String     
	* @throws
	 */
	@RequestMapping(value="/add",method=RequestMethod.GET)
	public String initTenantAdd(Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.tenant_add)){
			return "not_have_access";
		}		
		return "tenant_add";
	}	

	
	
	/**
	 * 
	* @Title: add 
	* @Description: 保存云硬盘
	* @param @param model
	* @param @param request     
	* @return String     
	* @throws
	 */
	@RequestMapping(value="/add",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult cloudDiskAdd(SysTenant tenant,HttpServletRequest request){
		MethodResult ret = tenantService.addSysTenant(tenant, request);
		if (ret.status.equals(MethodResult.SUCCESS)){
			logService.addLog("租户管理", "添加租户"+tenant.getLogCmt()+"成功！", "1", "1", request);
		}else{
			logService.addLog("租户管理", "添加租户"+tenant.getLogCmt()+"失败！", "1", "2", request);			
		}
		return ret;
	}
	
	/**
	 * 
	* @Title: modify 
	* @Description: 打开修改租户页面 
	* @param @param model
	* @param @param request     
	* @return String     
	* @throws
	 */
	@RequestMapping(value="/modify",method=RequestMethod.GET)
	public String initTenantModify(String id,Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.tenant_modify)){
			return "not_have_access";
		}
		SysTenant tenant = tenantService.getSysTenantById(id);
		model.addAttribute("tenant", tenant);
		return "tenant_modify";
	}	
	
	/**
	 * 
	* @Title: modify 
	* @Description: 打开修改租户页面 
	* @param @param model
	* @param @param request     
	* @return String     
	* @throws
	 */
	@RequestMapping(value="/modify",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult modifyTenant(SysTenant tenant,Model model,HttpServletRequest request){
		MethodResult ret =tenantService.modifyTenant(tenant, request);
		if (ret.status.equals(MethodResult.SUCCESS)){
			logService.addLog("租户管理", "修改租户"+tenant.getLogCmt()+"成功！", "1", "1", request);
		}else{
			logService.addLog("租户管理", "修改租户"+tenant.getLogCmt()+"失败！", "1", "2", request);			
		}
		return ret;
	}	
	
	/**
	 * 
	* @Title: del 
	* @Description: 删除云硬盘
	* @param @param model
	* @param @param request     
	* @return String     
	* @throws
	 */
	@RequestMapping(value="/del",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult tenantDel(String ids,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.tenant_del)){
			return new MethodResult(MethodResult.FAIL, "对不起，您没有删除租户权限！");
		}	
		if (ids.endsWith(",")){
			ids = ids.substring(0,ids.length()-1);
		}
        
		MethodResult ret = tenantService.delTenant(ids);
		if (ret.status.equals(MethodResult.SUCCESS)){
			logService.addLog("租户管理", "删除租户ID:"+ids+"成功！", "2", "1", request);
		}else{
			logService.addLog("租户管理", "删除租户ID"+ids+"失败！", "2", "2", request);			
		}
		return ret;
	}
	
	/**
	 * 
	* @Title: setUser 
	* @Description: 设置用户界面
	* @param @param model
	* @param @param request     
	* @return String     
	* @throws
	 */
	@RequestMapping(value="/setUser",method=RequestMethod.GET)
	public String initSetUser(String id,Model model,HttpServletRequest request){
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.tenant_setuser_query)){
			return "not_have_access";
		}
		SysTenant tenant = tenantService.getSysTenantById(id);
		model.addAttribute("tenant", tenant);
		
		model.addAttribute("outTenantUserList", manSysUserService.queryUserOutTenant(id));
  		
		model.addAttribute("tenantUserList", manSysUserService.queryUserTenant(id));
        
		return "tenant_setUser";
	}	
	
	/**
	 * 
	* @Title: modify 
	* @Description: 保存修改租户页面 
	* @param @param model
	* @param @param request     
	* @return String     
	* @throws
	 */
	@RequestMapping(value="/setUser",method=RequestMethod.POST)
	@ResponseBody
	public MethodResult setUser(String id,String userIds,HttpServletRequest request){	
	    if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.tenant_setuser)){
	        return new MethodResult(MethodResult.FAIL, "您没有设置租户管理员权限，请联系管理员！");
        }
		SysTenant tenant = tenantService.getSysTenantById(id);
		MethodResult ret = manSysUserService.setTenantUser(id,userIds);
		if (ret.status.equals(MethodResult.SUCCESS)){
			logService.addLog("租户管理", "配置租户"+tenant.getLogCmt()+"的用户ID:"+userIds+"成功！", "2", "1", request);
		}else{
			logService.addLog("租户管理", "配置租户"+tenant.getLogCmt()+"的用户 ID:"+userIds+"失败！", "2", "2", request);			
		}
		return ret;
	}
	/**
	 * 
	* @Title: toHostDetail 
	* @Description: 查看租户内主机信息 
	* @param @param id
	* @param @param model
	* @param @param request
	* @param @return      
	* @return String     
	* @throws
	 */
	@RequestMapping(value="/{id}/host",method=RequestMethod.GET)
    public String toHostDetail(@PathVariable("id") String id,Model model,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.tenant_host_query)){
            return "not_have_access";
        }
        model.addAttribute("list", cloudHostService.getHostInTenant(id));
        model.addAttribute("tenant", tenantService.getSysTenantById(id));
        return "/tenant/server_manage";
    }
	
	/**
     * 启动云主机
     * 
     * @param id
     * @return
     */
    @RequestMapping(value="/{id}/start",method=RequestMethod.GET)
    @ResponseBody
    public MethodResult startCloudHost(@PathVariable("id") String id,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.tenant_host_start)){
            return new MethodResult(MethodResult.FAIL,"您没有启动主机的权限，请联系管理员");
        }
        MethodResult mr = cloudHostService.operatorCloudHost(id, "1",false,0);
        return mr;
    }
    /**
     * 关闭云主机
     * @param id
     * @return
     */
    @RequestMapping(value="/{id}/shutdown",method=RequestMethod.GET)
    @ResponseBody
    public MethodResult shutdownCloudHost(@PathVariable("id") String id,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.tenant_host_shut_down)){
            return new MethodResult(MethodResult.FAIL,"您没有强制关闭主机的权限，请联系管理员");
        }
        MethodResult mr = cloudHostService.operatorCloudHost(id, "2",false,0);
        return mr;
    }
    
    /**
     * 重启云主机
     * @param id
     * @return
     */
    @RequestMapping(value="/{id}/restart",method=RequestMethod.GET)
    @ResponseBody
    public MethodResult restartCloudHost(@PathVariable("id") String id,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.tenant_host_restart)){
            return new MethodResult(MethodResult.FAIL,"您没有重启主机的权限，请联系管理员");
        }
        MethodResult mr = cloudHostService.operatorCloudHost(id, "3",false,0);
        return mr;
    }

    /**
     * 重启云主机
     * @param id
     * @return
     */
    @RequestMapping(value="/{id}/reset",method=RequestMethod.GET)
    @ResponseBody
    public MethodResult resetCloudHost(@PathVariable("id") String id,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.tenant_host_reset)){
            return new MethodResult(MethodResult.FAIL,"您没有强制重启主机的权限，请联系管理员");
        }
        MethodResult mr = cloudHostService.operatorCloudHost(id, "5",false,0);
        return mr;
    }
    /**
     * 强制关闭云主机
     * @param id
     * @return
     */
    @RequestMapping(value="/{id}/halt",method=RequestMethod.GET)
    @ResponseBody
    public MethodResult haltCloudHost(@PathVariable("id") String id,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.tenant_host_halt)){
            return new MethodResult(MethodResult.FAIL,"您没有强制关机的权限，请联系管理员");
        }
        MethodResult mr = cloudHostService.operatorCloudHost(id, "4",false,0);
        return mr;
    }
    /**
     * 
     * deleteCloudHost:通过id删除主机
     *
     * @author sasa
     * @param id
     * @return MethodResult
     * @since JDK 1.7
     */
    @RequestMapping(value="/{id}/delete",method=RequestMethod.GET)
    @ResponseBody
    public MethodResult deleteCloudHost(@PathVariable("id") String id,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.tenant_host_delete)){
            return new MethodResult(MethodResult.FAIL,"您没有删除主机的权限，请联系管理员");
        }
        MethodResult result = cloudHostService.deleteById(id);
        return result;
    }
    
    /**
     * 跳转到添加服务器页面
     * @param model
     * @return
     */
    @RequestMapping(value="/{id}/host/add",method=RequestMethod.GET)
    public String addServerPage(@PathVariable("id") String id,Model model,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.tenant_host_add)){
            return "not_have_access";
        }
         try {
            List<CloudHostConfigModel> type = cloudHostConfigModelService.getAllServer();
            List<SysDiskImageVO> list = sysDiskImageService.queryAllSysDiskImage();

            model.addAttribute("optionType",type);
            model.addAttribute("imageList",list);

            List<ComputeInfoExt> cList = new ArrayList<>();
                HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
                if(channel!=null){
                    JSONObject result = channel.computePoolQuery();
                    if("success".equals(result.getString("status"))){
                        JSONArray computerList = result.getJSONArray("compute_pools");
                        for (int i = 0; i < computerList.size(); i ++) {
                            JSONObject computerObject = computerList.getJSONObject(i);
                            String name = computerObject.getString("name");
                            if(!name.contains("server_pool") ){
                                continue;
                            }
                            String uuid = computerObject.getString("uuid");
                            int status = computerObject.getInt("status");
                            
                            ComputeInfoExt computer = new ComputeInfoExt(); 
                            computer.setName(name);
                            computer.setStatus(status);
                            computer.setUuid(uuid);
                            computer.setRegion(1);
                            cList.add(computer);
                        }
                    }
                }
            
            model.addAttribute("computerPool", cList);
            model.addAttribute("id", id);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "tenant/server_manage_add";
    }
    /**
     * 
    * @Title: addServer 
    * @Description: 添加主机
    * @param @param id
    * @param @param cloudHost
    * @param @param request
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    @RequestMapping(value="{id}/host/add",method=RequestMethod.POST)
    @ResponseBody
    public MethodResult addServer(@PathVariable("id") String id,CloudHostVO cloudHost,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.tenant_host_add)){
            return new MethodResult(MethodResult.FAIL,"您没有新增主机的权限，请联系管理员");
        }
        String hostId = StringUtil.generateUUID();
        cloudHost.setId(hostId);
        CloudHostConfigModel chcm = cloudHostConfigModelService.getById(cloudHost.getChcmId());
        if(chcm != null){
            cloudHost.setDataDisk(chcm.getDataDisk());
            cloudHost.setCpuCore(chcm.getCpuCore());
            cloudHost.setSysImageId(chcm.getSysImageId());
            cloudHost.setMemory(chcm.getMemory());
        }
        MethodResult mr = cloudHostService.bindHostToTenant(cloudHost, id);
        if(mr.isSuccess()){
            mr = cloudHostService.addServer(cloudHost, request);  
        }
        return mr;
    }
    
    @RequestMapping(value="/{id}/backupManage",method=RequestMethod.GET)
    public String backupManage(@PathVariable("id") String id,Model model,HttpServletRequest request){
        CloudHostVO host = cloudHostService.getById(id); 
        String uuid = host.getRealHostId();
        String sessionId = null;
        int backupFlag = 0;
        int resumeFlag = 0;

        HostBackupProgressData data = this.getProgressData(uuid);
        if(data!=null &&data.getBackupStatus()!=null&& data.getBackupStatus() ==9 ) {
            backupFlag = 1;
        }
        
        if(data!=null &&data.getBackupStatus()!=null&& data.getBackupStatus() == 10) {
            resumeFlag = 1;
        } 
        
        
        //缓存超时清空
        @SuppressWarnings("unchecked")
        Map<String, Object> cacheData = (Map<String, Object>) AppInconstant.hostBackupProgress.get(uuid);
        
        if(cacheData != null) {
            if ((System.currentTimeMillis() - (Long)cacheData.get("last_update_time")) > 2 * 60 * 1000) {// 超过2分钟没有更新，则认为该数据不再使用.
                AppInconstant.hostBackupProgress.remove(uuid);
            }
            
            //从缓存中取出数据
            sessionId = (String) cacheData.get("session_id");
        }
        
        try {
            if(StringUtil.isBlank(sessionId)) {
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("uuid", uuid); 
                params.put("command", "query");
                logger.info("+++++++begin ++++++++++++");
                sessionId = this.hostBackup(params);
                logger.info("+++++++end ++++++++++++");

                if( sessionId == null )
                {
                    logger.warn("CloudHostServiceImpl.queryHostBackup() > ["+Thread.currentThread().getId()+"] query host backup failed");
                    throw new AppException("查询命令发送失败");
                }
                //存入缓存
                Map<String, Object> temporaryData = new LinkedHashMap<String, Object>();
                temporaryData.put("session_id", sessionId);
                temporaryData.put("last_update_time", new Date().getTime());
                AppInconstant.hostBackupProgress.put(uuid, temporaryData);
            }
        } catch (Exception e) {
            
            e.printStackTrace();
        }
        
        request.setAttribute("host", host); 
        request.setAttribute("sessionId", sessionId);
        request.setAttribute("backupFlag", backupFlag);
        request.setAttribute("resumeFlag", resumeFlag);
        return "tenant/back_up_manage";
    }
    
  
    /**
     * 
    * @Title: backupHost 
    * @Description: 添加备份 
    * @param @param mode
    * @param @param disk
    * @param @param hostId
    * @param @return      
    * @return MethodResult     
    * @throws
     */

    @RequestMapping(value="/addbackup",method=RequestMethod.POST)
    @ResponseBody
    public MethodResult backupHost(Integer mode,Integer disk,String hostId) {
        String sessionId = null;
        Map<String, Object> parameter = new LinkedHashMap<String, Object>();
        HostBackupProgressData data = this.getProgressData(hostId);
        if(data!=null &&data.getBackupStatus()!=null&& data.getBackupStatus() ==9 ) {
            throw new AppException("正在备份中 请完成后再进行此操作");
        }
        
        if(data!=null &&data.getBackupStatus()!=null&& data.getBackupStatus() == 10) {
            throw new AppException("正在恢复中 请完成后再进行此操作");
        } 
        
        parameter.put("command", "backup");
        parameter.put("uuid", hostId);
        parameter.put("mode", mode);
        parameter.put("disk", disk);
        
        try{
            sessionId = this.hostBackup(parameter);
            if( sessionId == null )
            {
                logger.warn("CloudHostServiceImpl.backupHost() > ["+Thread.currentThread().getId()+"] query host backup failed");
                throw new AppException("备份命令发送失败");
            }
            
            backUpDetailService.updateDetail(hostId, AppConstant.BACK_UP_DETAIL_STATUS_SUCCESS, AppConstant.BACK_UP_DETAIL_STATUS_ANOTHOR);
            backUpDetailService.insertDetail(hostId, AppConstant.BACK_UP_DETAIL_STATUS_BACKINGUP, AppConstant.MESSAGE_TYPE_DESKTOP_BACKUP, mode, disk);

            
            HostBackupProgressPool pool = HostBackupProgressPoolManager.singleton().getPool(); 
            HostBackupProgressData hostBackup = new HostBackupProgressData();
                             
//          hostBackup = new HostBackupProgressData();
            hostBackup.setSessionId(sessionId);
            hostBackup.setUuid(hostId);
            hostBackup.updateTime();
            hostBackup.setBackupStatus(9);
            pool.put(hostBackup);  
            
            return new MethodResult(MethodResult.SUCCESS, "备份命令发送成功");
        } catch (Exception e) {
            System.err.println("fail to send backup host request.");
            e.printStackTrace();
            return new MethodResult(MethodResult.FAIL, "备份命令发送失败");
        }
    }
    /**
     * 
    * @Title: getBackupHostResult 
    * @Description: 获取备份进度
    * @param @param uuid
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    @RequestMapping(value="/getbackupprogress",method=RequestMethod.GET)
    @ResponseBody
    public MethodResult getBackupHostResult(String uuid) {
        try{    
//          @SuppressWarnings("unchecked")
//          Map<String, Object> temporaryData = (Map<String, Object>) AppInconstant.hostBackupProgress.get(uuid+"backup_progress");
//          if(temporaryData==null){
//              throw new AppException("无法获取备份信息"); 
//          }
            
            HostBackupProgressData data = this.getProgressData(uuid);
            if(data==null){
                MethodResult message = new MethodResult(MethodResult.FAIL, "备份还未开始");
                return message;         
            }               
            MethodResult result = new MethodResult(MethodResult.SUCCESS, "备份成功");
            if(data.isReady()){
                if(data.isFinished()){
                    if(data.isSuccess()) {
                        result.put("progress", 100);
                        result.put("backup_status", true);
                    } else {
                        result.put("backup_status", false);
                        }
                } else {
                    result.put("progress", data.getProgress());
                }
                result.put("isfail", false);
                return result;
            }           
            if(data.isFinished()){
                if(!data.isSuccess()) {
                    result.put("backup_status", false);
                    result.put("isfail", true);
                    return result;
                }
            }           
            MethodResult message = new MethodResult(MethodResult.FAIL, "备份还未开始");
            return message;
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(e);
        }
    }
    /**
     * 
    * @Title: resumeHostBackup 
    * @Description: 将云主机恢复到上次备份
    * @param @param mode
    * @param @param disk
    * @param @param hostId
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    @RequestMapping(value="/resumhost",method=RequestMethod.POST)
    @ResponseBody
    public MethodResult resumeHostBackup(Integer mode,Integer disk,String hostId) {
        
        String sessionId = null;
        Map<String, Object> parameter = new LinkedHashMap<String, Object>();
        String uuid = (String) parameter.get("uuid");
        HostBackupProgressData data = this.getProgressData(uuid);
        if(data!=null &&data.getBackupStatus()!=null&& data.getBackupStatus() ==9 ) {
            throw new AppException("正在备份中 请完成后再进行此操作");
        }
        
        if(data!=null &&data.getBackupStatus()!=null&& data.getBackupStatus() == 10) {
            throw new AppException("正在恢复中 请完成后再进行此操作");
        } 
        
        parameter.put("uuid", hostId);
        parameter.put("mode", mode);
        parameter.put("disk", disk);
        parameter.put("command", "resume");
        
        try{
            sessionId = this.hostBackup(parameter);
            if( sessionId == null )
            {
                logger.warn("CloudHostServiceImpl.resumeHostBackup() > ["+Thread.currentThread().getId()+"] query host backup failed");
                throw new AppException("恢复命令发送失败");
            } 
            backUpDetailService.updateDetail(hostId, AppConstant.BACK_UP_DETAIL_STATUS_SUCCESS, AppConstant.BACK_UP_DETAIL_STATUS_RESUMING);

            HostBackupProgressPool pool = HostBackupProgressPoolManager.singleton().getPool();
            HostBackupProgressData hostBackup = pool.get(hostId);
                             
            hostBackup = new HostBackupProgressData();
            hostBackup.setSessionId(sessionId);
            hostBackup.setUuid(hostId);
            hostBackup.updateTime();
            hostBackup.setBackupStatus(10);
            pool.put(hostBackup);
                    
            
            return new MethodResult(MethodResult.SUCCESS, "备份命令发送成功");
        } catch (Exception e) {
            System.err.println("fail to send backup host request.");
            e.printStackTrace();
            return new MethodResult(MethodResult.FAIL, "备份命令发送失败");
        }
    }
    /**
     * 
    * @Title: getResumeHostResult 
    * @Description: 获取主机恢复进度
    * @param @param uuid
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    @RequestMapping(value="/getresumprogress",method=RequestMethod.GET)
    @ResponseBody
    public MethodResult getResumeHostResult(String uuid) {
        try{    
//          @SuppressWarnings("unchecked")
//          Map<String, Object> temporaryData = (Map<String, Object>) AppInconstant.hostBackupProgress.get(uuid+"resume_progress");
//          if(temporaryData==null){
//              throw new AppException("无法获取恢复信息"); 
//          }
            
            HostBackupProgressData data = this.getProgressData(uuid);
            if(data==null){
                MethodResult message = new MethodResult(MethodResult.FAIL, "恢复还未开始");
                return message;                 
            }
                
//          System.err.println("data.isReady():"+data.isReady()+"data.isFinished():"+data.isFinished()+"data.isSuccess():"+data.isSuccess());
                
            MethodResult result = new MethodResult(MethodResult.SUCCESS, "恢复成功");
            if(data.isReady()){
                if(data.isFinished()){
                    if(data.isSuccess()) {
                        result.put("progress", 100);
                        result.put("resume_status", true);
                    } else {
                        result.put("resume_status", false);
                        }
                } else {
                    result.put("progress", data.getProgress());
                }
                return result;
            }
            if(data.isFinished()){
                if(!data.isSuccess()) {
                    result.put("resume_status", false);
                    return result;
                }
            }
            
            MethodResult message = new MethodResult(MethodResult.FAIL, "恢复还未开始");
            return message;
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(e);
        }
    }

    public HostBackupProgressData getProgressData(String uuid) {
        HostBackupProgressPool pool = HostBackupProgressPoolManager.singleton().getPool();
        System.out.println(pool+"##############getProgressData");
        return pool.get(uuid);
    }
   /**
    * 
   * @Title: queryHostBackup 
   * @Description: 查询主机是否有备份
   * @param @param parameter
   * @param @return      
   * @return MethodResult     
   * @throws
    */
    @RequestMapping(value="/querybackinfo",method=RequestMethod.POST)
    @ResponseBody
    public MethodResult queryHostBackup(String id){
        
        BackUpDetailVO backup = backUpDetailService.getLastAvailableBackUp(id);
        if(backup !=null ) {
            MethodResult result = new MethodResult(MethodResult.SUCCESS, "有可用备份");
            result.setProperty("timestamp", StringUtil.dateToString(backup.getBackUpTimeDate(), "yyyy-MM-dd HH:mm:ss"));
            result.setProperty("mode", backup.getMode().toString());
            result.setProperty("disk", backup.getDisk().toString());
            return result;
        } else {
            return new MethodResult(MethodResult.FAIL, "备份文件不可用或不存在");
        }
        
    }
    
    /**
     * 
    * @Title: hostBackup 
    * @Description: 发送备份云主机命令
    * @param @param parameter
    * @param @return
    * @param @throws Exception      
    * @return String     
    * @throws
     */
    //连接http gateway
    public String hostBackup(Map<String, Object> parameter) throws Exception{
        
        String uuid = (String) parameter.get("uuid");
        Integer region = 1;
        String command = (String) parameter.get("command");
        
        
        HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(region);
        
        if ("backup".equalsIgnoreCase(command)) {
            this.operatorHostBeforeBackup(uuid, region);
            Integer mode = Integer.valueOf(String.valueOf(parameter.get("mode")));
            Integer disk = Integer.valueOf(String.valueOf(parameter.get("disk")));
            
            try {
                JSONObject result = channel.hostBackup(uuid, mode, disk);
                if (HttpGatewayResponseHelper.isSuccess(result) == true) {
                    System.err.println("success to send backup host request."); 
                    return channel.getSessionId();
                } else {
                    System.err.println("fail to send backup host request.");
                    channel.release();
                }
            } catch (Exception e) {
                System.err.println("fail to send backup host request.");
                channel.release();
                throw e;
            } 
        } else if ("resume".equalsIgnoreCase(command)) {
            Integer mode = Integer.valueOf(String.valueOf(parameter.get("mode")));
            Integer disk = Integer.valueOf(String.valueOf(parameter.get("disk")));
            try {
                JSONObject result = channel.hostResume(uuid, mode, disk);
                if (HttpGatewayResponseHelper.isSuccess(result) == true) {
                    System.err.println("success to send resume host request.");
                    return channel.getSessionId();
                } else {
                    System.err.println("fail to send resume host request.");
                    channel.release();
                }
            } catch (Exception e) {
                System.err.println("fail to send resume host request.");
                channel.release();
                throw e;
            }
        }else if ("query".equalsIgnoreCase(command)) {
                try {
                    JSONObject result = channel.hostQueryBackup(uuid);
                    if (HttpGatewayResponseHelper.isSuccess(result) == true) {
                        System.err.println("success to send query host backup request.");
                        return channel.getSessionId();
                    } else {
                        System.err.println("fail to send query host backup request.");
                        channel.release();
                    }
                } catch (Exception e) {
                    System.err.println("fail to send query host backup request.");
                    channel.release();
                    throw e;
                }
            }
            return null;
    }
    /**
     * 
    * @Title: operatorHostBeforeBackup 
    * @Description: 备份前查询主机是否有备份权限，没有添加上 
    * @param @param realHostId
    * @param @param region      
    * @return void     
    * @throws
     */
    private void operatorHostBeforeBackup(String realHostId,Integer region){
        // 从http gateway获取云主机的详细信息
        HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(region);
        try { 
            
            JSONObject hostResult = channel.hostQueryInfo(realHostId);
            JSONObject hostInfo = (JSONObject) hostResult.get("host");
            
            //码率，帖率，系统类型，操作系统
            int codeRate = Integer.valueOf(hostInfo.get("ibt").toString());
            int frameRate = Integer.valueOf(hostInfo.get("fram").toString());
            int operating_type = Integer.valueOf(hostInfo.get("operating_type").toString());
            String operating_system = hostInfo.get("operating_system").toString();
            
            Integer[] option = JSONLibUtil.getIntegerArray(hostInfo, "option");
            if(option.length>3){
                if(option[2] == 0){
                    channel.hostModify(realHostId, "", 0, BigInteger.ZERO, new Integer[]{0, 1, 1}, new Integer[]{1,200}, "", "", "", BigInteger.ZERO, BigInteger.ZERO,0,0, codeRate,
							frameRate, operating_type, operating_system);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @RequestMapping(value="/{id}/{tenantId}/diskManage",method=RequestMethod.GET)
    public String diskManage(@PathVariable("id") String id,@PathVariable("tenantId") String tenantId,Model model,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.tenant_host_disk_manage_query)){
            return "not_have_access";
        }
        CloudHostVO host = cloudHostService.getById(id);
        ComputeInfoExt pool = ComputeInfoPoolManager.singleton().getPool().get(host.getPoolId());
		Integer diskType = pool.getDiskType();
        if(host!=null && host.getRealHostId()!=null){
            try {
                HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
                BigInteger[] dList = null;
                if(channel!=null){
                    JSONObject result = channel.hostQueryInfo(host.getRealHostId());
                    if("fail".equals(result.getString("status"))){
                        model.addAttribute("noDisk", "yes");
                        return "/server/server_disk_manage";
                    }
                    JSONObject hostObject = result.getJSONObject("host");
                    JSONArray diskList = hostObject.getJSONArray("disk_volume");
                    BigInteger[] dcount = new BigInteger[diskList.size()];
                    for(int j=0;j<diskList.size();j++){
                        dcount[j] = new BigInteger(diskList.getString(j));
                    }
                    dList = dcount;
                }
                BigDecimal[] dListValue = new BigDecimal[dList.length];
                for(int i=0;i<dListValue.length;i++){
                    dListValue[i] = CapacityUtil.toGBValue(dList[i], 0);
                }
                model.addAttribute("realId", host.getRealHostId());
                model.addAttribute("diskList", dListValue);
                model.addAttribute("diskType", diskType);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "/tenant/server_disk_manage"; 
    }
    
    @RequestMapping(value="/{realId}/{tenantId}/{diskType}/addDataDisk",method=RequestMethod.GET)
    public String addDataDiskPage(@PathVariable("realId") String realId,@PathVariable("tenantId") String tenantId,@PathVariable("diskType") String diskType,Model model,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.tenant_host_disk_manage_add)){
            return "not_have_access";
        }
        model.addAttribute("realId", realId);
        model.addAttribute("tenantId", tenantId);
        model.addAttribute("diskType", diskType);
        return "/tenant/server_disk_manage_add";
    }
    
    @RequestMapping(value="/addDataDisk",method=RequestMethod.POST)
    @ResponseBody
    public MethodResult addDataDisk(@RequestParam("uuid") String uuid,
            @RequestParam("dataDisk") String dataDisk,
            @RequestParam("diskType") String diskType,
            @RequestParam("tenantId") String tenantId,
            @RequestParam("mode") String mode,HttpServletRequest request){
        if(StringUtil.isBlank(dataDisk)){
            return new MethodResult(MethodResult.FAIL,"磁盘大小不能为空");
        }
        //目前木有云存储模式，所以默认为空
      	String diskId = "";
        String path = "";
        String crypt = "crypt";
		if("2".equals(diskType)){
			SharedMemoryVO sharedMemory = sharedMemoryService.queryAvailable();
			if(sharedMemory==null || sharedMemory.getUrl()==null){
				return new MethodResult(MethodResult.FAIL,"没有可用的共享存储路径");
			}
			path = sharedMemory.getUrl();
		}
        try{
            List<CloudHostVO> hostList = cloudHostService.getHostInTenant(tenantId);
            String name = "";
               BigInteger totalDisk = BigInteger.ZERO;
            for(CloudHostVO host : hostList){ 
                if(host.getRealHostId().equals(uuid)){
                    name = host.getDisplayName();
                }
                totalDisk = totalDisk.add(host.getDataDisk());
            } 
            totalDisk.add(CapacityUtil.fromCapacityLabel(dataDisk+"GB"));
            SysTenant tenant = tenantService.getSysTenantById(tenantId); 
            if(tenant.getDisk().compareTo(totalDisk)<=0){
                logService.addLog("云主机", "主机"+name+"修改配置失败，硬盘资源不足", "1", "2", request);
                return new MethodResult(MethodResult.FAIL,"硬盘资源不足，修改失败");
            }  
            
            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
            JSONObject result = channel.hostAttachDisk(uuid, CapacityUtil.fromCapacityLabel(dataDisk+"GB"), new Integer(diskType), diskId, new Integer(mode), path, crypt);
            if("success".equals(result.getString("status"))){
                return new MethodResult(MethodResult.SUCCESS,"添加成功");
            }
        }catch(Exception e){
            
        }
        return new MethodResult(MethodResult.FAIL,"添加失败");
    }
    
    /**
     * 删除数据磁盘
     * @param realId
     * @param curIndex
     * @return
     */
    @RequestMapping(value="/ddd",method=RequestMethod.POST)
    @ResponseBody
    public MethodResult deleteDataDisk(@RequestParam("realId") String realId,
            @RequestParam("curIndex") String curIndex,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.tenant_host_disk_manage_delete)){
            return new MethodResult(MethodResult.FAIL,"您没有删除服务器硬盘的权限，请联系管理员");
        }
        try{
            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
            JSONObject result = channel.hostDetachDisk(realId, new Integer(curIndex));
            if("success".equals(result.getString("status"))){
                return new MethodResult(MethodResult.SUCCESS,"删除成功");
            }
        }catch(Exception e){
            
        }
        return new MethodResult(MethodResult.FAIL, "删除失败");
    }
    
    
    /**
     * 查询一个主机类型，返回至更新页
     * @param id
     * @return
     */
    @RequestMapping(value="/{id}/{status}/{tenantId}/update",method=RequestMethod.GET) 
    public String updataServerPage(@PathVariable("id") String id,@PathVariable("status") String status,@PathVariable("tenantId") String tenantId,Model model,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.tenant_host_edit)){
            return "not_have_access";
        }
        CloudHostVO cloudServer = cloudHostService.getById(id);
         model.addAttribute("cloudServer", cloudServer);
//      model.addAttribute("imageList",list);
        model.addAttribute("runStatus",status);
        model.addAttribute("tenantId",tenantId);
        return "/tenant/server_manage_update";
    }
    
    @RequestMapping(value="/host/update",method=RequestMethod.POST)
    @ResponseBody
    public MethodResult updateServer(CloudHostVO server,HttpServletRequest request){
        List<CloudHostVO> hostList = cloudHostService.getHostInTenant(server.getTenantId());
        BigInteger totalMemory = BigInteger.ZERO;
        Integer totalCpu = 0;
        BigInteger totalBandwidth = BigInteger.ZERO;
        BigInteger totalDisk = BigInteger.ZERO;
        for(CloudHostVO host : hostList){
            if(host.getId().equals(server.getId())){
                host = server;
            }
            totalCpu = totalCpu+ host.getCpuCore();
            totalMemory = totalMemory.add(host.getMemory());
            totalBandwidth =totalBandwidth.add(host.getBandwidth());
            totalDisk = totalDisk.add(host.getDataDisk());
        } 
        SysTenant tenant = tenantService.getSysTenantById(server.getTenantId()); 
        if(tenant.getDisk().compareTo(totalDisk)<=0){
            logService.addLog("云主机", "主机"+server.getDisplayName()+"修改配置失败，硬盘资源不足", "1", "2", request);
            return new MethodResult(MethodResult.FAIL,"硬盘资源不足，修改失败");
        }
        if(tenant.getMem().compareTo(totalMemory)<0){
            logService.addLog("云主机", "主机"+server.getDisplayName()+"修改配置失败，内存资源不足", "1", "2", request);
            return new MethodResult(MethodResult.FAIL,"内存资源不足，绑定失败");
        }
        if(tenant.getCpu()<totalCpu){
            logService.addLog("云主机", "主机"+server.getDisplayName()+"修改配置失败，CPU资源不足", "1", "2", request);
            return new MethodResult(MethodResult.FAIL,"CPU资源不足，绑定失败");
        }
        MethodResult mr = cloudHostService.modifyAllocation(server);
        return mr;
    }
    
    /**
     * 
    * @Title: breakCloudHostAndTenant 
    * @Description: 解绑主机和租户
    * @param @param id
    * @param @param request
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    @RequestMapping(value="/{id}/breaktenant",method=RequestMethod.GET)
    @ResponseBody
    public MethodResult breakCloudHostAndTenant(@PathVariable("id") String id,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.tenant_host_break)){
            return new MethodResult(MethodResult.FAIL,"您没有解除绑定的权限，请联系管理员");
        }
        MethodResult mr = cloudHostService.unboundHostInTenant(id);
        return mr;
    }
    
    @RequestMapping(value="/{id}/diagram",method=RequestMethod.GET)
    public String serverDiagramPage(@PathVariable("id") String id,Model model,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.trnant_host_manage_diagram)){
            return "not_have_access";
        }
        CloudHostVO server = cloudHostService.getByRealHostId(id);
        model.addAttribute("server", server);
        model.addAttribute("realId",id);
        return "/tenant/server_manage_diagram";
    }
}
