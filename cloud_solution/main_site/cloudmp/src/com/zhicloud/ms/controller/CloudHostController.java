package com.zhicloud.ms.controller;

import com.zhicloud.ms.app.pool.CloudHostData;
import com.zhicloud.ms.app.pool.CloudHostPoolManager;
import com.zhicloud.ms.app.pool.computePool.ComputeInfo;
import com.zhicloud.ms.app.pool.host.back.HostBackupProgressData;
import com.zhicloud.ms.app.pool.host.back.HostBackupProgressPool;
import com.zhicloud.ms.app.pool.host.back.HostBackupProgressPoolManager;
import com.zhicloud.ms.app.pool.host.reset.HostResetProgressData;
import com.zhicloud.ms.app.pool.host.reset.HostResetProgressPool;
import com.zhicloud.ms.app.pool.host.reset.HostResetProgressPoolManager;
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
import com.zhicloud.ms.transform.util.TransFormPrivilegeUtil;
import com.zhicloud.ms.util.CapacityUtil;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.BackUpDetailVO;
import com.zhicloud.ms.vo.CloudHostVO;
import com.zhicloud.ms.vo.CloudHostWarehouse;
import com.zhicloud.ms.vo.SysDiskImageVO;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.*;


@Controller
@RequestMapping("/warehouse/cloudhost")
public class CloudHostController {
    
    
    public static final Logger logger = Logger.getLogger(CloudServerController.class);

	@Resource
	ICloudHostService cloudHostService;
	@Resource
	ICloudHostWarehouseService cloudHostWarehouseService;
	@Resource
	ISysDiskImageService sysDiskImageService;
	@Resource
	private IBackUpDetailService backUpDetailService;
	@Resource
    private IOperLogService operLogService;
	
	
	/**
	 * 查询所有云主机
	 * @param model
	 * @return
	 */
	@RequestMapping(value="/{id}/all",method=RequestMethod.GET)
	public String getAll(@PathVariable("id") String id,Model model,HttpServletRequest request)
      throws UnsupportedEncodingException {
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_warehouse_detail)){
			return "not_have_access";
		}


      String param = request.getParameter("param");
      String runningStatusStr = request.getParameter("running_status");
      String flagStr = request.getParameter("flag");


      model.addAttribute("running_status", runningStatusStr);
      model.addAttribute("flag", flagStr);

      Integer runningStatus, flag;

      if(StringUtil.isBlank(param)){
          param = null;
      }else{
          param = new String(request.getParameter("param").getBytes("ISO-8859-1"),"UTF-8");
          model.addAttribute("parameter", param);
          param = "%"+param+"%";
      }

      if(StringUtil.isBlank(runningStatusStr)){
          runningStatus = null;
      } else{
          runningStatus = Integer.valueOf(runningStatusStr);
      }
      if(StringUtil.isBlank(flagStr)){
          flag = null;
      } else {
          flag = Integer.valueOf(flagStr);
      }

      Map<String, Object> condition = new LinkedHashMap<String, Object>();
      condition.put("warehouse_id", id);
      condition.put("param", param);
      condition.put("running_status", runningStatus);
      condition.put("flag", flag);

      List<CloudHostVO> cloudHostList = cloudHostService.getByWarehouseIdAndParams(condition);

      CloudHostWarehouse cloudHostWarehouse = cloudHostWarehouseService.getById(id);
      model.addAttribute("warehouse", cloudHostWarehouse);
		  List<CloudHostVO> newCloudServerList = new ArrayList<CloudHostVO>();
          HostResetProgressPool resetpool = HostResetProgressPoolManager.singleton().getPool();

		
		for(CloudHostVO vo : cloudHostList){
		    HostBackupProgressPool pool = HostBackupProgressPoolManager.singleton().getPool();
			HostBackupProgressData data = pool.get(vo.getRealHostId());
			if(data!=null &&data.getBackupStatus()!=null&& data.getBackupStatus() ==9 ) {
				vo.setStatus(9);			}
			
			if(data!=null &&data.getBackupStatus()!=null&& data.getBackupStatus() == 10) {
				vo.setStatus(10);
			} 

            HostResetProgressData flush = resetpool.get(vo.getRealHostId());
            if(flush != null && flush.getResetStatus() == 1){
                vo.setStatus(11); 
            } 
 			newCloudServerList.add(vo);
		}
 		model.addAttribute("cloudHostList", newCloudServerList);
		model.addAttribute("warehouseId", id);
		try {
            List<ComputeInfo> cList = new ArrayList<>();
                HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
                if(channel!=null){
                    JSONObject result = channel.computePoolQuery();
            if ("fail".equals(result.getString("status"))) {
                logger.error("CloudHostController.getAll()>>获取计算资源池失败");
                model.addAttribute("computerPool", cList);
                return "not_responsed";
            }
                    JSONArray computerList = result.getJSONArray("compute_pools");
                    for (int i = 0; i < computerList.size(); i ++) {
                        JSONObject computerObject = computerList.getJSONObject(i);
                        String name = computerObject.getString("name");
                        if(!name.contains("desktop_pool") ){
                            continue;
                        }
                        String uuid = computerObject.getString("uuid");
                        int status = computerObject.getInt("status");

                        ComputeInfo computer = new ComputeInfo();
                        computer.setName(name);
                        computer.setStatus(status);
                        computer.setUuid(uuid);
                        computer.setRegion(1);
                        cList.add(computer);
                    }
                }
            
            model.addAttribute("computerPool", cList);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "host_manage";
    }
    /**
     * 
     * toHostDetail:根据Id查询主机并跳转到主机详情
     *
     * @author sasa
     * @param id
     * @param model
     * @return String
     * @since JDK 1.7
     */
    @RequestMapping(value="/{id}/detail",method=RequestMethod.GET)
    public String toHostDetail(@PathVariable("id") String id,Model model,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_warehouse_host_detail)){
            return "not_have_access";
        }
        CloudHostVO host = cloudHostService.queryCloudHostById(id);
        CloudHostData realdata = CloudHostPoolManager.getSingleton().getCloudHostFromPool(host.getRealHostId());
        realdata.setCpuUsage(countPercent(realdata.getCpuUsage()));
        realdata.setMemoryUsage(countPercent(realdata.getMemoryUsage()));
        realdata.setDataDiskUsage(countPercent(realdata.getDataDiskUsage()));
        model.addAttribute("host", host);
        model.addAttribute("realdata", realdata);
        return "host_detail";
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
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_warehouse_host_start)){
            return new MethodResult(MethodResult.FAIL,"您没有启动主机的权限，请联系管理员");
        }
        MethodResult mr = cloudHostService.operatorCloudHost(id, "1");
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
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_warehouse_host_shut_down)){
            return new MethodResult(MethodResult.FAIL,"您没有强制关闭主机的权限，请联系管理员");
        }
        MethodResult mr = cloudHostService.operatorCloudHost(id, "2"); 
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
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_warehouse_host_restart)){
            return new MethodResult(MethodResult.FAIL,"您没有重启主机的权限，请联系管理员");
        }
        MethodResult mr = cloudHostService.operatorCloudHost(id, "3");
        return mr;
    }
    /**
     * 强制重启云主机
     * @param id
     * @return
     */
    @RequestMapping(value="/{id}/reset",method=RequestMethod.GET)
    @ResponseBody
    public MethodResult resetCloudHost(@PathVariable("id") String id,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_warehouse_host_reset)){
            return new MethodResult(MethodResult.FAIL,"您没有强制重启主机的权限，请联系管理员");
        }
        MethodResult mr = cloudHostService.operatorCloudHost(id, "5");
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
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_warehouse_host_shut_down)){
            return new MethodResult(MethodResult.FAIL,"您没有强制关机的权限，请联系管理员");
        }
        MethodResult mr = cloudHostService.operatorCloudHost(id, "4");
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
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_warehouse_host_delete)){
            return new MethodResult(MethodResult.FAIL,"您没有删除主机的权限，请联系管理员");
        }
        MethodResult result = cloudHostService.deleteById(id);
        return result;
    }
    /**
     * 
     * disassociation:通过主机id，解除其与用户的关联
     *
     * @author sasa
     * @param id
     * @return MethodResult
     * @since JDK 1.7
     */
    @RequestMapping(value="/{id}/disassociation",method=RequestMethod.GET)
    @ResponseBody
    public MethodResult disassociation(@PathVariable("id") String id,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_warehouse_host_allocate)){
            return new MethodResult(MethodResult.FAIL,"您没有分配主机的权限，请联系管理员");
        }
        MethodResult result = cloudHostService.getDisassociationById(id);
        return result;
    }
    /**
     * 
     * allocateHost:批量删除主机 
     *
     * @author sasa
     * @param ids
     * @return MethodResult
     * @since JDK 1.7
     */
    @RequestMapping(value="/deletehosts",method=RequestMethod.POST)
    @ResponseBody
    public MethodResult deleteHosts(String ids,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_warehouse_host_delete)){
            return new MethodResult(MethodResult.FAIL,"您没有删除主机的权限，请联系管理员");
        }
        if(StringUtil.isBlank(ids)){
            return new MethodResult(MethodResult.FAIL, "未选择主机");            
        } 
        return cloudHostService.deleteByIds(ids);
    }
    
    private double countPercent(double val){ 
        DecimalFormat df=new DecimalFormat(".##"); 
        String st=df.format(val); 
        return Double.parseDouble(st) * 100;
    }
    
    /**
     * 查询一个主机类型，返回至更新页
     * @param id
     * @return
     */
    @RequestMapping(value="/{id}/{warehouseId}/{status}/update",method=RequestMethod.GET) 
    public String updataServerPage(@PathVariable("id") String id,@PathVariable("warehouseId") String warehouseId,@PathVariable("status") String status ,Model model,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.server_manage_modify)){
            return "not_have_access";
        }
        CloudHostVO cloudServer = cloudHostService.getById(id);
        List<SysDiskImageVO> list = sysDiskImageService.querySysDiskImageByImageType(AppConstant.DISK_IMAGE_TYPE_SERVER);
        model.addAttribute("cloudServer", cloudServer);
        model.addAttribute("imageList",list);
        model.addAttribute("warehouseId",warehouseId);
        model.addAttribute("runStatus",status);
        return "host_manage_update";
    }
    
    @RequestMapping(value="/update",method=RequestMethod.POST)
    @ResponseBody
    public MethodResult updateServer(CloudHostVO server){
        MethodResult mr = cloudHostService.modifyAllocation(server);
        return mr;
    }
    
    @RequestMapping(value="/{id}/diskManage",method=RequestMethod.GET)
    public String diskManage(@PathVariable("id") String id,Model model,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_disk_manage_query)){
            return "not_have_access";
        }
        CloudHostVO host = cloudHostService.getById(id);
        if(host!=null && host.getRealHostId()!=null){
            try {
                HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
                BigInteger[] dList = null;
                if(channel!=null){
                    JSONObject result = channel.hostQueryInfo(host.getRealHostId());
                    if("fail".equals(result.getString("status"))){
              logger.error("CloudHostController.diskManage()>>>获取硬盘数据失败");
              model.addAttribute("noDisk", "yes");
              return "not_responsed";
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
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "host_disk_manage";
    }
    @RequestMapping(value="/{realId}/addDataDisk",method=RequestMethod.GET)
    public String addDataDiskPage(@PathVariable("realId") String realId,Model model,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_disk_manage_add)){
            return "not_have_access";
        }
        model.addAttribute("realId", realId);
        return "host_disk_manage_add";
    }
    
    @RequestMapping(value="/addDataDisk",method=RequestMethod.POST)
    @ResponseBody
    public MethodResult addDataDisk(@RequestParam("uuid") String uuid,
            @RequestParam("dataDisk") String dataDisk,
            @RequestParam("diskType") String diskType,
            @RequestParam("diskId") String diskId,
            @RequestParam("mode") String mode){
        if(StringUtil.isBlank(dataDisk)){
            return new MethodResult(MethodResult.FAIL,"磁盘大小不能为空");
        }
        try{
            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
            JSONObject result = channel.hostAttachDisk(uuid, CapacityUtil.fromCapacityLabel(dataDisk+"GB"), new Integer(diskType), diskId, new Integer(mode));
            if("success".equals(result.getString("status"))){
                return new MethodResult(MethodResult.SUCCESS,"添加成功");
            }
        }catch(Exception e){
            
        }
        return new MethodResult(MethodResult.FAIL,"添加失败");
    }
    
    @RequestMapping(value="/ddd",method=RequestMethod.POST)
    @ResponseBody
    public MethodResult deleteDataDisk(@RequestParam("realId") String realId,
            @RequestParam("curIndex") String curIndex,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_disk_manage_delete)){
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
    
    @RequestMapping(value="/{id}/backupManage",method=RequestMethod.GET)
    public String backupManage(@PathVariable("id") String id,Model model,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_back_up_manage_query)){
            return "not_have_access";
        }
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
                sessionId = this.hostBackup(params);
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
        return "desktop/back_up_manage";
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
    public MethodResult backupHost(Integer mode,Integer disk,String hostId,HttpServletRequest request) {
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_back_up_manage_add)){
            return new MethodResult(MethodResult.FAIL,"您没有恢复的权限，请联系管理员");
        }
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
//			@SuppressWarnings("unchecked")
//			Map<String, Object> temporaryData = (Map<String, Object>) AppInconstant.hostBackupProgress.get(uuid+"backup_progress");
//			if(temporaryData==null){
//				throw new AppException("无法获取备份信息"); 
//			}
		    HostBackupProgressPool pool = HostBackupProgressPoolManager.singleton().getPool();
	        HostBackupProgressData data = pool.get(uuid);
//			HostBackupProgressData data = this.getProgressData(uuid);
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
				return result;
			}			
			if(data.isFinished()){
				if(!data.isSuccess()) {
					result.put("backup_status", false);
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
	public MethodResult resumeHostBackup(Integer mode,Integer disk,String hostId,HttpServletRequest request) {
		
		if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_back_up_manage_resume)){
			return new MethodResult(MethodResult.FAIL,"您没有恢复的权限，请联系管理员");
		}
		
		String sessionId = null;
		Map<String, Object> parameter = new LinkedHashMap<String, Object>();
 		HostBackupProgressData data = this.getProgressData(hostId);
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
			HostBackupProgressData  hostBackup = new HostBackupProgressData();
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
//			@SuppressWarnings("unchecked")
//			Map<String, Object> temporaryData = (Map<String, Object>) AppInconstant.hostBackupProgress.get(uuid+"resume_progress");
//			if(temporaryData==null){
//				throw new AppException("无法获取恢复信息"); 
//			}
			
		    HostBackupProgressPool pool = HostBackupProgressPoolManager.singleton().getPool();
	        HostBackupProgressData data = pool.get(uuid);
			if(data==null){
				MethodResult message = new MethodResult(MethodResult.FAIL, "恢复还未开始");
				return message; 				
			}
				
//			System.err.println("data.isReady():"+data.isReady()+"data.isFinished():"+data.isFinished()+"data.isSuccess():"+data.isSuccess());
				
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
            Integer[] option = JSONLibUtil.getIntegerArray(hostInfo, "option");
            if(option.length>3){
                if(option[2] == 0){
                    channel.hostModify(realHostId, "", 0, BigInteger.ZERO, new Integer[]{0, 1, 1}, new Integer[]{1,200}, "", "", "", BigInteger.ZERO, BigInteger.ZERO);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    

    @RequestMapping(value="/{id}/flushdiskimage",method=RequestMethod.GET)
    public String flushDiskImageManage(@PathVariable("id") String id,Model model,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_flush_disk_image)){
            return "not_have_access";
        }
        CloudHostVO host = cloudHostService.getById(id); 
        String uuid = host.getRealHostId();
        int resetFlag = 0;
        HostResetProgressPool pool = HostResetProgressPoolManager.singleton().getPool();

        HostResetProgressData resetdata = pool.get(uuid);
        if (resetdata != null && resetdata.getResetStatus() == 1) {
            resetFlag = 1;
        }

        List<SysDiskImageVO> sysDiskImageList = sysDiskImageService.querySysDiskImageByImageType(AppConstant.DISK_IMAGE_TYPE_SERVER);
 

        System.err.println(sysDiskImageList.size());

        request.setAttribute("host", host);
        request.setAttribute("resetFlag", resetFlag);
        request.setAttribute("sysDiskImageList", sysDiskImageList);
        return "/desktop/flush_disk_manage";
    }
    
    @RequestMapping(value="/flushdiskimage",method=RequestMethod.POST)
    @ResponseBody
    public MethodResult flushdiskimage(String uuid,String imageId,HttpServletRequest request) {
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_flush_disk_image)){
            return new MethodResult(MethodResult.FAIL,"您没有重装系统的权限，请联系管理员");
        } 

        CloudHostVO host = cloudHostService.getByRealHostId(uuid);
        int mode = 0;
        int disk = 0;
        String sessionId = null;
        HostBackupProgressData data = this.getProgressData(uuid);
        if(data!=null &&data.getBackupStatus()!=null&& data.getBackupStatus() ==9 ) {
            operLogService.addLog("云主机", "重装主机"+host.getDisplayName()+"失败", "1", "2", request);
            throw new AppException("正在备份中 请完成后再进行此操作");
        }
        
        if(data!=null &&data.getBackupStatus()!=null&& data.getBackupStatus() == 10) {
            operLogService.addLog("云主机", "重装主机"+host.getDisplayName()+"失败", "1", "2", request);
            throw new AppException("正在恢复中 请完成后再进行此操作");
        } 
        HostResetProgressPool pool = HostResetProgressPoolManager.singleton().getPool();

        HostResetProgressData resetdata = pool.get(uuid);
        if (resetdata !=null && resetdata.getResetStatus() == 1) {
            operLogService.addLog("云主机", "重装主机"+host.getDisplayName()+"失败", "1", "2", request);
            throw new AppException("正在安装操作系统中 请完成后再进行此操作");
        }
        //存放镜像id和name
        SysDiskImageVO sysDiskImageVO = sysDiskImageService.getSysDiskImageById(imageId);
        try {
            sessionId = this.hostReset(uuid, disk, mode, sysDiskImageVO.getRealImageId());
            if (sessionId == null) {
                logger.warn("CloudHostServiceImpl.resetHost() > [" + Thread.currentThread().getId()
                    + "] rest host failed");
                operLogService.addLog("云主机", "重装主机"+host.getDisplayName()+"失败", "1", "2", request);
                throw new AppException("安装命令发送失败");
            }

            //存入缓存
            Map<String, Object> temporaryData = new LinkedHashMap<String, Object>();
            temporaryData.put("uuid", uuid);
            temporaryData.put("session_id", sessionId);
            AppInconstant.hostResetProgress.put(uuid + "_reset_progress", temporaryData);            
            AppInconstant.hostResetProgress.put(uuid + "_image_id", sysDiskImageVO.getId());
            AppInconstant.hostResetProgress.put(uuid + "_image_name", sysDiskImageVO.getName());
            operLogService.addLog("云主机", "重装主机"+host.getDisplayName()+"成功", "1", "1", request);

            return new MethodResult(MethodResult.SUCCESS, "重装命令发送成功");
        } catch (Exception e) {
            System.err.println("fail to send reset host request.");
            e.printStackTrace();
            return new MethodResult(MethodResult.FAIL, "重装命令发送失败");
        }
    }
    
    public String hostReset(String uuid, int disk, int mode, String image)
            throws MalformedURLException, IOException {
            HttpGatewayAsyncChannel channel = HttpGatewayManager.getAsyncChannel(1);
            try {

                JSONObject result = channel.hostFlushDiskImage(uuid, disk, mode, image);
                if (HttpGatewayResponseHelper.isSuccess(result) == true) {
                    System.err.println("success to send flush disk image request.");
                    HostResetProgressPool pool = HostResetProgressPoolManager.singleton().getPool();
                    HostResetProgressData hostReset = pool.get(uuid);
                  //对象不存在
                    if(hostReset == null){
                        hostReset = new HostResetProgressData();
                        hostReset.setRealHostId(uuid);
                        hostReset.setSessionId(null);
                        hostReset.setResetStatus(1);
                        pool.put(hostReset);
                    }
                    return channel.getSessionId();
                } else {
                    System.err.println("fail to send flush disk image request.");
                    channel.release();
                }
            } catch (Exception e) {
                System.err.println("fail to send flush disk image request.");
                channel.release();
                throw e;
            }
            return null;
        }
    
    /**
     * 
    * @Title: getFlushHostResult 
    * @Description: 获取重装进度
    * @param @param uuid
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    @RequestMapping(value="/getflushprogress",method=RequestMethod.GET)
    @ResponseBody
    public MethodResult getFlushHostResult(String uuid) {
        MethodResult result = new MethodResult(MethodResult.SUCCESS, "安装成功");

        try {
            @SuppressWarnings("unchecked") Map<String, Object> temporaryData =
                (Map<String, Object>) AppInconstant.hostResetProgress.get(uuid + "_reset_progress");
            if (temporaryData == null) {
                throw new AppException("无法获取安装信息");
            }

            HostResetProgressPool pool = HostResetProgressPoolManager.singleton().getPool();

            HostResetProgressData data = pool.get(uuid);
            if (data == null) {
                MethodResult message = new MethodResult(MethodResult.FAIL, "安装还未开始");
                return message;
            }
            else if(data.getResetStatus() == 0 ){
                if(data.isSuccess()){
                    result.put("progress", 100);
                    result.put("reset_status", true);
                    return result;
 
                }else{
                    result.put("reset_status", false);
                    return result;
                }
            }

            if (data.isReady()) {
                if (data.isFinished()) {
                    if (data.isSuccess()) {
                        result.put("progress", 100);
                        result.put("reset_status", true);

                        Map<String, Object> imageData = new LinkedHashMap<>();
                        imageData.put("real_host_id", uuid);
                        imageData.put("sys_image_id", StringUtil
                            .trim(AppInconstant.hostResetProgress.get(uuid + "_image_id")));
                        imageData.put("sys_image_name", StringUtil
                            .trim(AppInconstant.hostResetProgress.get(uuid + "_image_name")));
//                        CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
//                        cloudHostMapper.updateImageByRealId(imageData);
                    } else {
                        result.put("reset_status", false);
                    }
                } else {
                    result.put("progress", data.getProgress());
                }
                return result;
            }

            MethodResult message = new MethodResult(MethodResult.FAIL, "安装还未开始");
            return message;
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw new AppException(e);
        }
    }
}
