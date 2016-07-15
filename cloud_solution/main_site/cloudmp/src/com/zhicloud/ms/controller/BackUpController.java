package com.zhicloud.ms.controller;
 

import com.zhicloud.ms.app.pool.host.back.HostBackupProgressData;
import com.zhicloud.ms.app.pool.host.back.HostBackupProgressPool;
import com.zhicloud.ms.app.pool.host.back.HostBackupProgressPoolManager;
import com.zhicloud.ms.common.util.StringUtil;
import com.zhicloud.ms.common.util.json.JSONLibUtil;
import com.zhicloud.ms.constant.AppConstant;
import com.zhicloud.ms.exception.AppException;
import com.zhicloud.ms.httpGateway.HttpGatewayAsyncChannel;
import com.zhicloud.ms.httpGateway.HttpGatewayChannelExt;
import com.zhicloud.ms.httpGateway.HttpGatewayManager;
import com.zhicloud.ms.httpGateway.HttpGatewayResponseHelper;
import com.zhicloud.ms.quartz.BackUpJob;
import com.zhicloud.ms.quartz.QuartzManage;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.ICloudHostService;
import com.zhicloud.ms.service.IBackUpDetailService;
import com.zhicloud.ms.service.IOperLogService;
import com.zhicloud.ms.service.ITimerInfoService;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.util.TransFormPrivilegeUtil;
import com.zhicloud.ms.util.AlertDisplayContent;
import com.zhicloud.ms.vo.CloudHostVO;
import com.zhicloud.ms.vo.TimerInfoVO;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.quartz.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
/**
 * 
* @ClassName: DeskTopSetTimeBackUpController 
* @Description: 桌面云主机设置定时备份功能
* @author sasa
* @date 2015年7月28日 上午11:01:05 
*
 */
@Controller
public class BackUpController {
    
    public static final Logger logger = Logger.getLogger(BackUpController.class);
    @Resource
    private ITimerInfoService timerInfoService;
    @Resource
    private ICloudHostService cloudHostService;
    @Resource
    private IBackUpDetailService backUpDetailService;
    @Resource
    private IOperLogService operLogService;
    /**
     * 
    * @Title: managePage 
    * @Description: 桌面云定时备份信息页面
    * @param @param request
    * @param @return      
    * @return String     
    * @throws
     */
    @RequestMapping(value="/backresume/dtsettimebackup/manage",method=RequestMethod.GET)
    public String managePage(HttpServletRequest request){ 
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_set_time_back_up_manage)){
            return "not_have_access";
        }
        //获取桌面云备份的定时器信息
        TimerInfoVO timer = timerInfoService.queryTimerInfoByKey("desktop_back_up");
        
        if(timer == null){
            timer = new TimerInfoVO();
            timer.setId(StringUtil.generateUUID());
            timer.setStatus(2);
        }
        request.setAttribute("timer", timer);
        return "/desktop/set_time_back_up_manage";
    }
    /**
     * 
    * @Title: csmanagePage 
    * @Description: 云服务器定时备份页面
    * @param @param request
    * @param @return      
    * @return String     
    * @throws
     */
    @RequestMapping(value="/csbackresume/dtsettimebackup/manage",method=RequestMethod.GET)
    public String csmanagePage(HttpServletRequest request){ 
         if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.server_set_time_back_up_manage)){
            return "not_have_access";
        }
        //获取服务器备份的定时器信息
        TimerInfoVO timer = timerInfoService.queryTimerInfoByKey("server_back_up");
        if(timer == null){
            timer = new TimerInfoVO();
            timer.setId(StringUtil.generateUUID());
            timer.setStatus(2);
        }
        request.setAttribute("timer", timer);
        return "/server/set_time_back_up_manage";
    }
    /**
     * 
    * @Title: updateCheck 
    * @Description: 新增或者是更新定时任务
    * @param @param timer
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    @RequestMapping(value="/dtsettimebackup/updatecheck",method=RequestMethod.POST)
    @ResponseBody
    public MethodResult updateCheck(TimerInfoVO timer,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_set_time_back_up_update)){
            return new MethodResult(MethodResult.FAIL,"您没有修改定时信息的权限，请联系管理员");
        }
        String content = "";
        //判断是否需要更新创建的时间
        if(StringUtil.isBlank(timer.getCreateTime())){
            timer.setCreateTime(StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS")); 
        }
        timerInfoService.insertOrUpdateBackUpTimerInfo(timer);
        //当设置禁用定时任务时,删除任务
        if(timer.getStatus() == 2){
            try {
                content = "禁用定时备份";
                JobDetail jdCheck = QuartzManage.getQuartzManage().getScheduler().getJobDetail(new JobKey(timer.getId(),"groupJob"));
                if(jdCheck != null ){ 
                    // 删除任务和定时器
                    QuartzManage.getQuartzManage().deleteTrigger(new JobKey(timer.getId(),"groupJob"), new TriggerKey(timer.getId(),"groupTrigger"));
                }
                return new MethodResult(MethodResult.SUCCESS,"保存成功");
            } catch (SchedulerException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }           
        }
         //用于设置新的出发时间
        String time = "";
        //按照每月多少号执行
        if(timer.getType() == 1){
            //0 15 10 15 * ? 每月15号上午10点15分触发   格式
            time = "0 "+timer.getMinute()+" "+timer.getHour()+" "+timer.getDay()+" * ?";
            content = "更新定时备份时间为每月"+timer.getDay()+"号"+timer.getHour()+"点"+timer.getMinute()+"分";
        }else if(timer.getType() == 2){
            //按周来执行  0 59 2 ? * FRI    每周5凌晨2点59分触发；
            time = "0 "+timer.getMinute()+" "+timer.getHour()+" ? * "+StringUtil.parseWeek(timer.getWeek());
            content = "更新定时备份时间为每周"+timer.getWeek()+" "+timer.getHour()+"点"+timer.getMinute()+"分";
        }else if(timer.getType() == 3){
            //每天执行 0 15 10 ? * * 每天10点15分触发
            time = "0 "+timer.getMinute()+" "+timer.getHour()+" ? * *";
            content = "更新定时备份时间为每天"+timer.getHour()+"点"+timer.getMinute()+"分";
        }else{
            return new MethodResult(MethodResult.FAIL,"模式错误");
        }
        try { 
            QuartzManage.getQuartzManage().deleteTrigger(new JobKey(timer.getId(),"groupJob"), new TriggerKey(timer.getId(),"groupTrigger"));

            //定义任务
            JobDetail jd = JobBuilder.newJob(BackUpJob.class)
                    .withIdentity(new JobKey(timer.getId(),"groupJob"))  
                    .usingJobData("mode", timer.getMode())
                    .usingJobData("disk", timer.getDisk())
                    .usingJobData("timerKey", timer.getKey())
                    .requestRecovery(true)
                    .build();
            //定义触发器
            CronTrigger ct = (CronTrigger)TriggerBuilder.newTrigger()
                    .withIdentity(new TriggerKey(timer.getId(),"groupTrigger"))
                    .withSchedule(CronScheduleBuilder.cronSchedule(time).withMisfireHandlingInstructionDoNothing())
                    .startNow()
                    .build();
            //添加任务
            QuartzManage.getQuartzManage().addTrigger(jd, ct); 
         } catch (Exception e) {
            e.printStackTrace();
            operLogService.addLog("桌面云备份和恢复", content, "1", "2", request);
            return new MethodResult(MethodResult.FAIL, "更新失败");
        }
        operLogService.addLog("桌面云备份和恢复", content, "1", "1", request);
        return new MethodResult(MethodResult.SUCCESS, "更新成功");
    }
    /**
     * 
    * @Title: updateCheck 
    * @Description: 云服务器新增或者是更新定时任务
    * @param @param timer
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    @RequestMapping(value="/cssettimebackup/updatecheck",method=RequestMethod.POST)
    @ResponseBody
    public MethodResult csupdateCheck(TimerInfoVO timer,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.server_set_time_back_up_update)){
            return new MethodResult(MethodResult.FAIL,"您没有修改定时信息的权限，请联系管理员");
        }
        String content = "";
        //判断是否需要用新的时间
        if(StringUtil.isBlank(timer.getCreateTime())){
            timer.setCreateTime(StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS")); 
        }
        timerInfoService.insertOrUpdateBackUpTimerInfo(timer);
        //当设置禁用定时任务时,删除任务
        if(timer.getStatus() == 2){
            try {
                content = "禁用定时备份";
                JobDetail jdCheck = QuartzManage.getQuartzManage().getScheduler().getJobDetail(new JobKey(timer.getId(),"groupJob"));
                if(jdCheck != null ){ 
                    // 删除任务和定时器
                    QuartzManage.getQuartzManage().deleteTrigger(new JobKey(timer.getId(),"groupJob"), new TriggerKey(timer.getId(),"groupTrigger"));
                }
                return new MethodResult(MethodResult.SUCCESS,"保存成功");
            } catch (SchedulerException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }           
        }
         //用于设置新的出发时间
        String time = "";
        //按照每月多少号执行
        if(timer.getType() == 1){
            //0 15 10 15 * ? 每月15号上午10点15分触发   格式
            time = "0 "+timer.getMinute()+" "+timer.getHour()+" "+timer.getDay()+" * ?";
            content = "更新定时备份时间为每月"+timer.getDay()+"号"+timer.getHour()+"点"+timer.getMinute()+"分";
        }else if(timer.getType() == 2){
            //按周来执行  0 59 2 ? * FRI    每周5凌晨2点59分触发；
            time = "0 "+timer.getMinute()+" "+timer.getHour()+" ? * "+StringUtil.parseWeek(timer.getWeek());
            content = "更新定时备份时间为每周"+timer.getWeek()+" "+timer.getHour()+"点"+timer.getMinute()+"分";
        }else if(timer.getType() == 3){
            //每天执行 0 15 10 ? * * 每天10点15分触发
            time = "0 "+timer.getMinute()+" "+timer.getHour()+" ? * *";
            content = "更新定时备份时间为每天"+timer.getHour()+"点"+timer.getMinute()+"分";
        }else{
            return new MethodResult(MethodResult.FAIL,"模式错误");
        }
        try {
             QuartzManage.getQuartzManage().deleteTrigger(new JobKey(timer.getId(),"groupJob"), new TriggerKey(timer.getId(),"groupTrigger"));

               
            JobDetail jd = JobBuilder.newJob(BackUpJob.class)
                    .withIdentity(new JobKey(timer.getId(),"groupJob"))  
                    .usingJobData("mode", timer.getMode())
                    .usingJobData("disk", timer.getDisk())
                    .usingJobData("timerKey", timer.getKey())
                    .requestRecovery(true)
                    .build();
            //定义触发器
            CronTrigger ct = (CronTrigger)TriggerBuilder.newTrigger()
                    .withIdentity(new TriggerKey(timer.getId(),"groupTrigger"))
                    .withSchedule(CronScheduleBuilder.cronSchedule(time).withMisfireHandlingInstructionDoNothing())
                    .startNow()
                    .build();
            //添加任务
            QuartzManage.getQuartzManage().addTrigger(jd, ct);
                  
         } catch (Exception e) {
            e.printStackTrace();
            operLogService.addLog("主机备份和恢复", content, "1", "2", request);
            return new MethodResult(MethodResult.FAIL, "更新失败");
        }
        operLogService.addLog("主机备份和恢复", content, "1", "1", request);
        return new MethodResult(MethodResult.SUCCESS, "更新成功");
    }
    /**
     * 
    * @Title: hostManagePage 
    * @Description: 定时备份桌面云主机管理
    * @param @param request
    * @param @return      
    * @return String     
    * @throws
     */
    @RequestMapping(value="/backresume/desktopbackuptimer/manage",method=RequestMethod.GET)
    public String hostManagePage(HttpServletRequest request){ 
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_set_time_back_up_host_manage)){
            return "not_have_access";
        }
        request.setAttribute("notInTimer", cloudHostService.getCloudHostNotInTimer("desktop_back_up"));
        request.setAttribute("inTimer", cloudHostService.getCloudHostInTimer("desktop_back_up"));
        return "/desktop/set_time_back_up_host_manage";
    }
    /**
     * 
    * @Title: cshostManagePage 
    * @Description: 跳转到云服务器定时备份主机页面
    * @param @param request
    * @param @return      
    * @return String     
    * @throws
     */
    @RequestMapping(value="/csbackresume/desktopbackuptimer/manage",method=RequestMethod.GET)
    public String cshostManagePage(HttpServletRequest request){ 
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.server_set_time_back_up_host_manage)){
            return "not_have_access";
        }
         request.setAttribute("notInTimer", cloudHostService.getCloudHostNotInTimer("server_back_up"));
         request.setAttribute("inTimer", cloudHostService.getCloudHostInTimer("server_back_up"));
        return "/server/set_time_back_up_host_manage";
    }
    /**
     * 
    * @Title: updateHostInTimer 
    * @Description: 更新参与定时任务的云主机
    * @param @param request
    * @param @param response
    * @param @return
    * @param @throws Exception      
    * @return MethodResult     
    * @throws
     */
    @RequestMapping(value = "/desktopbackuptimer/update") 
    @ResponseBody
    public MethodResult updateHostInTimer(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_set_time_back_up_host_update)){
            return new MethodResult(MethodResult.FAIL,"您没有修改备份主机的权限，请联系管理员");
        }
        String hostId = request.getParameter("hostId");
         try {  
             MethodResult result = cloudHostService.saveHostInTimer(hostId.split(","), AppConstant.DESTTOP_BACK_UP);
             if(result.isSuccess()){
                 operLogService.addLog("云桌面备份和恢复","修改参与定时备份的主机" , "1", "1", request);
             }else{
                 operLogService.addLog("云桌面备份和恢复","修改参与定时备份的主机" , "1", "2", request);
             }
             return result;
 
        } catch (Exception e) {
            e.printStackTrace();
            operLogService.addLog("云桌面备份和恢复","修改参与定时备份的主机" , "1", "2", request);
            return new MethodResult(MethodResult.FAIL,"保存失败");

        }
    }
    
    @RequestMapping(value = "/serverbackuptimer/update") 
    @ResponseBody
    public MethodResult updateServerHostInTimer(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.server_set_time_back_up_host_update)){
            return new MethodResult(MethodResult.FAIL,"您没有修改备份主机的权限，请联系管理员");
        }
        String hostId = request.getParameter("hostId");
         try {  
             MethodResult result = cloudHostService.saveHostInTimer(hostId.split(","), AppConstant.SERVER_BACK_UP);
            if(result.isSuccess()){
                operLogService.addLog("主机备份和恢复","修改参与定时备份的主机" , "1", "1", request);
            }else{
                operLogService.addLog("主机备份和恢复","修改参与定时备份的主机" , "1", "2", request);
            }
            return result;
 
        } catch (Exception e) {
            e.printStackTrace();
            operLogService.addLog("主机备份和恢复","修改参与定时备份的主机" , "1", "2", request);
            return new MethodResult(MethodResult.FAIL,"保存失败");

        }
    }
    
    /**
     * 手动备份
     * @param request
     * @return
     */
    @RequestMapping(value="/backresume/detailmanage",method=RequestMethod.GET)
    public String hostBackUpDetailManagePage(HttpServletRequest request){ 
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_set_time_back_up_detail_manage)){
            return "not_have_access";
        }
         request.setAttribute("detailList", backUpDetailService.getAllDetail(AppConstant.MESSAGE_TYPE_DESKTOP_BACKUP));
         return "/desktop/set_time_back_up_host_detail_manage";
    }
    @RequestMapping(value="/csbackresume/detailmanage",method=RequestMethod.GET)
    public String cloudServerBackupDetail(HttpServletRequest request){ 
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.server_set_time_back_up_detail_manage)){
            return "not_have_access";
        }
         request.setAttribute("detailList", backUpDetailService.getAllDetail(AppConstant.MESSAGE_TYPE_SERVER_BACKUP));
         return "/server/back_up_host_detail_manage";
    }
    
    
    @RequestMapping(value="/backresume/{id}/{mode}/{disk}/resume",method=RequestMethod.GET)
    @ResponseBody
    public MethodResult hostBackUpDetailManagePage(@PathVariable("id") String id,@PathVariable("mode") Integer mode,@PathVariable("disk") Integer disk,HttpServletRequest request){ 
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_back_and_resume_resume)){
            return new MethodResult(MethodResult.FAIL,"您没有恢复的权限，请联系管理员");
        }
        CloudHostVO host = cloudHostService.getById(id);
        if(host == null){
            operLogService.addLog("桌面云备份和恢复","恢复主机" , "1", "2", request);
            return new MethodResult(MethodResult.FAIL,"主机不存在，恢复失败。");

        }
        if(host.getRunningStatus()!=AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN){
            operLogService.addLog("桌面云备份和恢复","恢复主机失败，请先关闭主机" , "1", "2", request);
            return new MethodResult(MethodResult.FAIL,"主机处于开机状态，需关机后进行恢复。");

        }
        
        String sessionId = null;
        Map<String, Object> parameter = new LinkedHashMap<String, Object>();
        String uuid = (String) parameter.get("uuid");
        HostBackupProgressData data = this.getProgressData(uuid);
        if(data!=null &&data.getBackupStatus()!=null&& data.getBackupStatus() ==9 ) {
            return new MethodResult(MethodResult.FAIL,"正在备份中 请完成后再进行此操作");
        }
        
        if(data!=null &&data.getBackupStatus()!=null&& data.getBackupStatus() == 10) {
            return new MethodResult(MethodResult.FAIL,"正在恢复中 请完成后再进行此操作");
        } 
        
        parameter.put("uuid", host.getRealHostId());
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
            
            backUpDetailService.updateDetail(host.getRealHostId(), AppConstant.BACK_UP_DETAIL_STATUS_SUCCESS, AppConstant.BACK_UP_DETAIL_STATUS_RESUMING);

            HostBackupProgressPool pool = HostBackupProgressPoolManager.singleton().getPool();
            HostBackupProgressData  hostBackup = new HostBackupProgressData();
            hostBackup.setSessionId(sessionId);
            hostBackup.setUuid(host.getRealHostId());
            hostBackup.updateTime();
            hostBackup.setBackupStatus(10);
            pool.put(hostBackup);
                    
            operLogService.addLog("桌面云备份和恢复","恢复主机"+host.getDisplayName() , "1", "1", request);
            return new MethodResult(MethodResult.SUCCESS, "恢复命令发送成功");
        } catch (Exception e) {
            System.err.println("fail to send resume host request.");
            e.printStackTrace();
            operLogService.addLog("桌面云备份和恢复","恢复主机"+host.getDisplayName() , "1", "2", request);
            return new MethodResult(MethodResult.FAIL, "恢复命令发送失败");
        }
        
    }
    
    @RequestMapping(value="/serverbackresume/{id}/{mode}/{disk}/resume",method=RequestMethod.GET)
    @ResponseBody
    public MethodResult serverBackUpDetailManagePage(@PathVariable("id") String id,@PathVariable("mode") Integer mode,@PathVariable("disk") Integer disk,HttpServletRequest request){ 
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.server_back_and_resume_resume)){
            return new MethodResult(MethodResult.FAIL,"您没有恢复的权限，请联系管理员");
        }
        CloudHostVO host = cloudHostService.getById(id);
        if(host == null){
            operLogService.addLog("桌面云备份和恢复","恢复主机" , "1", "2", request);
            return new MethodResult(MethodResult.FAIL,"主机不存在，恢复失败。");

        }
        if(host.getRunningStatus()!=AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN){
            operLogService.addLog("桌面云备份和恢复","恢复主机失败，请先关闭主机" , "1", "2", request);
            return new MethodResult(MethodResult.FAIL,"主机处于开机状态，需关机后进行恢复。");

        }
        String sessionId = null;
        Map<String, Object> parameter = new LinkedHashMap<String, Object>();
        String uuid = (String) parameter.get("uuid");
        HostBackupProgressData data = this.getProgressData(uuid);
        if(data!=null &&data.getBackupStatus()!=null&& data.getBackupStatus() ==9 ) {
            return new MethodResult(MethodResult.FAIL,"正在备份中 请完成后再进行此操作");
        }
        
        if(data!=null &&data.getBackupStatus()!=null&& data.getBackupStatus() == 10) {
            return new MethodResult(MethodResult.FAIL,"正在恢复中 请完成后再进行此操作");
        } 
        
        parameter.put("uuid", host.getRealHostId());
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
            
            backUpDetailService.updateDetail(host.getRealHostId(), AppConstant.BACK_UP_DETAIL_STATUS_SUCCESS, AppConstant.BACK_UP_DETAIL_STATUS_RESUMING);

            HostBackupProgressPool pool = HostBackupProgressPoolManager.singleton().getPool();
            HostBackupProgressData  hostBackup = new HostBackupProgressData();
            hostBackup.setSessionId(sessionId);
            hostBackup.setUuid(uuid);
            hostBackup.updateTime();
            hostBackup.setBackupStatus(10);
            pool.put(hostBackup);
                    
            operLogService.addLog("桌面云备份和恢复","恢复主机"+host.getDisplayName() , "1", "1", request);
            return new MethodResult(MethodResult.SUCCESS, "恢复命令发送成功");
        } catch (Exception e) {
            System.err.println("fail to send resume host request.");
            e.printStackTrace();
            operLogService.addLog("桌面云备份和恢复","恢复主机"+host.getDisplayName() , "1", "2", request);
            return new MethodResult(MethodResult.FAIL, "恢复命令发送失败");
        }
        
    }
    
    public HostBackupProgressData getProgressData(String uuid) {
        HostBackupProgressPool pool = HostBackupProgressPoolManager.singleton().getPool();
        return pool.get(uuid);
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
                    channel.hostModify(realHostId, "", 0, BigInteger.ZERO, new Integer[]{0, 1, 1}, new Integer[]{1,200}, "", "", "", BigInteger.ZERO, BigInteger.ZERO, codeRate,
							frameRate, operating_type, operating_system);
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /**
     * 
    * @Title: manualBackup 
    * @Description: T跳转到云桌面手动备份页面
    * @param @param request
    * @param @return      
    * @return String     
    * @throws
     */
    @RequestMapping(value="/backresume/manualbackup",method=RequestMethod.GET)
    public String desktopManualBackupPage(HttpServletRequest request){ 
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_back_and_resume_manualbackup)){
            return "not_have_access";
        }
        List<CloudHostVO> hostList = cloudHostService.getAllAallocateDesktopHost();
        request.setAttribute("hostList", hostList);
        return "/desktop/manual_backup_manage";
    }
    
    
    /**
    * 
    * @Title: manualBackup 
    * @Description: T跳转弹框页面
    * @param @param request
    * @param @return      
    * @return String     
    * @throws
     */
    @RequestMapping(value="/backresume/gotodialog",method=RequestMethod.GET)
    public String gotodialog(HttpServletRequest request){ 
        String realHostId = request.getParameter("runningStatus");
        //realHostId值为runBack时表示定时备份，否则为手动备份
        if(AlertDisplayContent.RUNBACKSTATUS.equals(realHostId)){
        	request.setAttribute("backupsTitle", AlertDisplayContent.AUTOBACKUP);
        	request.setAttribute("content", AlertDisplayContent.RUNBLACK);
        	request.setAttribute("runningStatus", "1");
        }else{
        	CloudHostVO cloudHost = cloudHostService.getByRealHostId(realHostId);
            if(cloudHost != null){
            	if(cloudHost.getRunningStatus()==1){
            		request.setAttribute("backupsTitle", AlertDisplayContent.MANUALBACKUP);
            		request.setAttribute("content", AlertDisplayContent.HANDBLACK_CLOES);
            	}else{
            		request.setAttribute("backupsTitle", AlertDisplayContent.MANUALBACKUP);
            		request.setAttribute("content", AlertDisplayContent.HANDBLACK_OPEN);
            	}
            	request.setAttribute("runningStatus", cloudHost.getRunningStatus());
            }else
            	request.setAttribute("runningStatus", "2");
        }
        
        return "/desktop/manual_backup_dialog";
    }
    
    /**
     * 
    * @Title: serverManualBackupPage 
    * @Description: 跳转到云服务器手动备份页面 
    * @param @param request
    * @param @return      
    * @return String     
    * @throws
     */
    @RequestMapping(value="/csbackresume/manualbackup",method=RequestMethod.GET)
    public String serverManualBackupPage(HttpServletRequest request){ 
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.server_back_and_resume_manualbackup)){
            return "not_have_access";
        }
        List<CloudHostVO> hostList = cloudHostService.getAllServer();
        request.setAttribute("hostList", hostList);
        return "/server/manual_backup_manage";
    }
    /**
     * 
    * @Title: serverManualBackup 
    * @Description: 手动创建服务器备份 
    * @param @param id
    * @param @param mode
    * @param @param disk
    * @param @param request
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    @RequestMapping(value="/csbackresume/manualbackup",method=RequestMethod.POST)
    @ResponseBody
    public MethodResult serverManualBackup(String id,Integer mode,Integer disk,HttpServletRequest request){ 
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.server_back_and_resume_manualbackup)){
            return new MethodResult(MethodResult.FAIL,"您没有备份的权限，请联系管理员");
        } 
        String sessionId = null;
        CloudHostVO cloudHost = cloudHostService.getByRealHostId(id);
        Map<String, Object> parameter = new LinkedHashMap<String, Object>();
         HostBackupProgressData data = this.getProgressData(id);
        if(data!=null &&data.getBackupStatus()!=null&& data.getBackupStatus() ==9 ) {
            operLogService.addLog("主机备份和恢复","备份主机"+cloudHost.getDisplayName() , "1", "2", request);
            return new MethodResult(MethodResult.FAIL,"正在备份中 请完成后再进行此操作");
        }
        
        if(data!=null &&data.getBackupStatus()!=null&& data.getBackupStatus() == 10) {
            operLogService.addLog("主机备份和恢复","备份主机"+cloudHost.getDisplayName() , "1", "2", request);
            return new MethodResult(MethodResult.FAIL,"正在恢复中 请完成后再进行此操作");
        } 
        
        parameter.put("uuid", id);
        parameter.put("mode", mode);
        parameter.put("disk", disk);
        parameter.put("command", "backup");
        
        try{
            //强制关闭该主机
            if(cloudHost.getRunningStatus() == AppConstant.CLOUD_HOST_RUNNING_STATUS_RUNNING){
                cloudHostService.operatorCloudHost(cloudHost.getId(), "4",false,0);
            }
            sessionId = this.hostBackup(parameter);
            if( sessionId == null )
            {
                operLogService.addLog("主机备份和恢复","备份主机"+cloudHost.getDisplayName() , "1", "2", request);
                logger.warn("CloudHostServiceImpl.resumeHostBackup() > ["+Thread.currentThread().getId()+"] query host backup failed");
                throw new AppException("恢复命令发送失败");
            } 
            
            backUpDetailService.updateDetail(id, AppConstant.BACK_UP_DETAIL_STATUS_SUCCESS, AppConstant.BACK_UP_DETAIL_STATUS_ANOTHOR);
            backUpDetailService.insertDetail(id, AppConstant.BACK_UP_DETAIL_STATUS_BACKINGUP, 0, mode, disk);
            HostBackupProgressPool pool = HostBackupProgressPoolManager.singleton().getPool();
            HostBackupProgressData  hostBackup = new HostBackupProgressData();
            hostBackup.setSessionId(sessionId);
            hostBackup.setUuid(id);
            hostBackup.updateTime();
            hostBackup.setBackupStatus(9);
            pool.put(hostBackup);
                    
            operLogService.addLog("主机备份和恢复","备份主机"+cloudHost.getDisplayName() , "1", "1", request);
            return new MethodResult(MethodResult.SUCCESS, "备份命令发送成功");
        } catch (Exception e) {
            System.err.println("fail to send resume host request.");
            e.printStackTrace();
            return new MethodResult(MethodResult.FAIL, "备份命令发送失败");
        }
        
    }
    /**
     * 
    * @Title: manualBackup 
    * @Description: 创建云桌面手动备份
    * @param @param id
    * @param @param mode
    * @param @param disk
    * @param @param request
    * @param @return      
    * @return MethodResult     
    * @throws
     */
    @RequestMapping(value="/backresume/manualbackup",method=RequestMethod.POST)
    @ResponseBody
    public MethodResult manualBackup(String id,Integer mode,Integer disk,HttpServletRequest request){ 
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_back_and_resume_manualbackup)){
            return new MethodResult(MethodResult.FAIL,"您没有备份的权限，请联系管理员");
        } 
        CloudHostVO cloudHost = cloudHostService.getByRealHostId(id);
        String sessionId = null;
        Map<String, Object> parameter = new LinkedHashMap<String, Object>();
         HostBackupProgressData data = this.getProgressData(id);
        if(data!=null &&data.getBackupStatus()!=null&& data.getBackupStatus() ==9 ) {
            operLogService.addLog("云桌面备份和恢复","备份主机"+cloudHost.getDisplayName() , "1", "2", request);
            return new MethodResult(MethodResult.FAIL,"正在备份中 请完成后再进行此操作");
        }
        
        if(data!=null &&data.getBackupStatus()!=null&& data.getBackupStatus() == 10) {
            operLogService.addLog("云桌面备份和恢复","备份主机"+cloudHost.getDisplayName() , "1", "2", request);
            return new MethodResult(MethodResult.FAIL,"正在恢复中 请完成后再进行此操作");
        } 
        
        parameter.put("uuid", id);
        parameter.put("mode", mode);
        parameter.put("disk", disk);
        parameter.put("command", "backup");
        
        try{
          //强制关闭该主机
            if(cloudHost.getRunningStatus() == AppConstant.CLOUD_HOST_RUNNING_STATUS_RUNNING){
                cloudHostService.operatorCloudHost(cloudHost.getId(), "4",true,0);
            }
            sessionId = this.hostBackup(parameter);
            if( sessionId == null )
            {
                operLogService.addLog("云桌面备份和恢复","备份主机"+cloudHost.getDisplayName() , "1", "2", request);
                logger.warn("CloudHostServiceImpl.resumeHostBackup() > ["+Thread.currentThread().getId()+"] query host backup failed");
                throw new AppException("恢复命令发送失败");
            } 
            
            backUpDetailService.updateDetail(id, AppConstant.BACK_UP_DETAIL_STATUS_SUCCESS, AppConstant.BACK_UP_DETAIL_STATUS_ANOTHOR);
            backUpDetailService.insertDetail(id, AppConstant.BACK_UP_DETAIL_STATUS_BACKINGUP, 0, mode, disk);
            HostBackupProgressPool pool = HostBackupProgressPoolManager.singleton().getPool();
            HostBackupProgressData  hostBackup = new HostBackupProgressData();
            hostBackup.setSessionId(sessionId);
            hostBackup.setUuid(id);
            hostBackup.updateTime();
            hostBackup.setBackupStatus(9);
            pool.put(hostBackup);
                    
            operLogService.addLog("云桌面备份和恢复","备份主机"+cloudHost.getDisplayName() , "1", "1", request);
            return new MethodResult(MethodResult.SUCCESS, "备份命令发送成功");
        } catch (Exception e) {
            System.err.println("fail to send resume host request.");
            e.printStackTrace();
            return new MethodResult(MethodResult.FAIL, "备份命令发送失败");
        }
        
    }

}
