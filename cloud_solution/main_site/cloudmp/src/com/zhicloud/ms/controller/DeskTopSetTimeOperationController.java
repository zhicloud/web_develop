package com.zhicloud.ms.controller;


import com.zhicloud.ms.common.util.StringUtil;
import com.zhicloud.ms.constant.AppConstant;
import com.zhicloud.ms.quartz.OperationJob;
import com.zhicloud.ms.quartz.QuartzManage;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.ICloudHostService;
import com.zhicloud.ms.service.IOperLogService;
import com.zhicloud.ms.service.ISetTimeOperationDetailService;
import com.zhicloud.ms.service.ITimerInfoService;
import com.zhicloud.ms.transform.constant.TransFormPrivilegeConstant;
import com.zhicloud.ms.transform.util.TransFormPrivilegeUtil;
import com.zhicloud.ms.vo.TimerInfoVO;

import org.apache.log4j.Logger;
import org.quartz.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.Date;

/**
 * @ClassName: DeskTopSetTimeOperationController
 * @function: 桌面云主机设置定时操作功能
 * @author 张翔
 * @date 2015年8月4日 上午11:01:05
 */
@Controller
@RequestMapping("/dtsettimeoperation")
public class DeskTopSetTimeOperationController {
    @Resource
    private IOperLogService operLogService;
    
    
    public static final Logger logger = Logger.getLogger(DeskTopSetTimeOperationController.class);
    @Resource
    private ITimerInfoService timerInfoService;
    @Resource
    private ICloudHostService cloudHostService;
    @Resource
    private ISetTimeOperationDetailService setTimeOperationDetailService;

    /**
     * @function 定时操作管理页面
     * @param request
     * @return 定时操作管理页面
     */
    @RequestMapping(value="/manage",method=RequestMethod.GET)
    public String managePage(HttpServletRequest request){
        return manageStartupPage(request);
    }

    /**
     * @function 定时开机管理页面
     * @param request
     * @return 定时开机管理页面
     */
    @RequestMapping(value="/startup/manage",method=RequestMethod.GET)
    public String manageStartupPage(HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_set_time_startup_query)){
            return "not_have_access";
        }
        TimerInfoVO startupTimer = timerInfoService.queryTimerInfoByKey("startup_timer");


        if(startupTimer == null){
            startupTimer = new TimerInfoVO();
            startupTimer.setId(StringUtil.generateUUID());
            startupTimer.setStatus(AppConstant.TIMER_DISABLE);
        }

        request.setAttribute("timer", startupTimer);

        return "desktop/set_time_startup_manage";
    }

    /**
     * @function 定时关机管理页面
     * @param request
     * @return 定时关机管理页面
     */
    @RequestMapping(value="/shutdown/manage",method=RequestMethod.GET)
    public String manageShutdownPage(HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_set_time_shutdown_query)){
            return "not_have_access";
        }
        TimerInfoVO shutdownTimer = timerInfoService.queryTimerInfoByKey("shutdown_timer");

        if(shutdownTimer == null){
            shutdownTimer = new TimerInfoVO();
            shutdownTimer.setId(StringUtil.generateUUID());
            shutdownTimer.setStatus(AppConstant.TIMER_DISABLE);
        }

        request.setAttribute("timer", shutdownTimer);
        return "desktop/set_time_shutdown_manage";
    }

    /**
     * @function 定时开机云主机管理页面
     * @param request
     * @return 定时开机云主机管理页面
     */
    @RequestMapping(value="/startup/host/manage",method=RequestMethod.GET)
    public String manageStartupHostPage(HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_set_time_startup_host_query)){
            return "not_have_access";
        }
        request.setAttribute("startup_not_in_timer", cloudHostService.getCloudHostNotInTimer(
            AppConstant.STARTUP_TIMER));
        request.setAttribute("startup_in_timer", cloudHostService.getCloudHostInTimer(
            AppConstant.STARTUP_TIMER));

        return "desktop/set_time_startup_host_manage";
    }

    /**
     * @function 定时关机云主机管理页面
     * @param request
     * @return 定时关机云主机管理页面
     */
    @RequestMapping(value="/shutdown/host/manage",method=RequestMethod.GET)
    public String manageShutdownHostPage(HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_set_time_shutdown_host_query)){
            return "not_have_access";
        }

        request.setAttribute("shutdown_not_in_timer", cloudHostService.getCloudHostNotInTimer(
            AppConstant.SHUTDOWN_TIMER));
        request.setAttribute("shutdown_in_timer", cloudHostService.getCloudHostInTimer(
            AppConstant.SHUTDOWN_TIMER));

        return "desktop/set_time_shutdown_host_manage";
    }

    /**
     * @function 定时操作记录管理页面
     * @param request
     * @return 定时操作记录管理页面
     */
    @RequestMapping(value="/detail/manage",method=RequestMethod.GET)
    public String manageOperationDetailPage(HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_set_time_operation_detail_query)){
            return "not_have_access";
        }

        request.setAttribute("detailList", setTimeOperationDetailService.getAllDetail());

        return "desktop/set_time_operation_detail_manage";
    }

    /**
     * @function 检测时间差
     * @param timer
     * @return 时间差小于10分钟返回fail
     */
    public MethodResult timeCheck(TimerInfoVO timer){
        String time = timer.getHour() + ":" +timer.getMinute();
        long result = 0;
        MethodResult methodResult = new MethodResult(MethodResult.FAIL, "开关机时间间隔至少大于10分钟");
        if (AppConstant.STARTUP_TIMER.equals(timer.getKey()) && AppConstant.TIMER_ENABLE.equals(timer.getStatus())) {
            TimerInfoVO shutdownTimer = timerInfoService.queryTimerInfoByKey(AppConstant.SHUTDOWN_TIMER);
            if (shutdownTimer != null && AppConstant.TIMER_ENABLE.equals(shutdownTimer.getStatus())){
                String shutdownTime = shutdownTimer.getHour() + ":" + shutdownTimer.getMinute();
                result = setTimeOperationDetailService.getTimeInterval(time, shutdownTime);
                if (result < 10) {
                    return methodResult;
                }
            }

        } else if (AppConstant.SHUTDOWN_TIMER.equals(timer.getKey()) && AppConstant.TIMER_ENABLE.equals(timer.getStatus())){
            TimerInfoVO startupTimer = timerInfoService.queryTimerInfoByKey(AppConstant.STARTUP_TIMER);
            if (startupTimer != null && AppConstant.TIMER_ENABLE.equals(startupTimer.getStatus())) {
                String startupTime = startupTimer.getHour() + ":" + startupTimer.getMinute();
                result = setTimeOperationDetailService.getTimeInterval(startupTime, time);
                if (result < 10) {
                    return methodResult;
                }
            }
        }
        return new MethodResult(MethodResult.SUCCESS, "时间间隔合法");
    }


    /**
     * @function 新增或者是更新定时操作任务
     * @param timer
     * @param request
     * @return 新增或者是更新定时操作任务是否成功
     */
    @RequestMapping(value="/updatecheck",method=RequestMethod.POST)
    @ResponseBody
    public MethodResult updateStartupCheck(TimerInfoVO timer,HttpServletRequest request){
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_set_time_operation_update)){
            return new MethodResult(MethodResult.FAIL,"您没有修改定时信息的权限，请联系管理员");
        }

        //时间间隔过短
        MethodResult result = this.timeCheck(timer);
        if (MethodResult.FAIL.equals(result.status)) {
            operLogService.addLog("预启动", "更新时间失败", "1", "2", request);
            return  new MethodResult(MethodResult.FAIL, result.message);
        }

        //判断是否需要用新的时间
        if(StringUtil.isBlank(timer.getCreateTime())){
            timer.setCreateTime(StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS")); 
        }
        timerInfoService.insertOrUpdateTimerInfo(timer);
        //当设置禁用定时任务时,删除任务
        if(timer.getStatus() == 2){
            try {
                JobDetail jdCheck = QuartzManage.getQuartzManage().getScheduler().getJobDetail(new JobKey(timer.getId(),"groupJob"));
                if(jdCheck != null ){ 
                    // 删除任务和定时器
                    QuartzManage.getQuartzManage().deleteTrigger(new JobKey(timer.getId(),"groupJob"), new TriggerKey(timer.getId(),"groupTrigger"));
                }
                operLogService.addLog("预启动", "禁用预启动成功", "1", "1", request);
                return new MethodResult(MethodResult.SUCCESS,"保存成功");
            } catch (SchedulerException e) {
                e.printStackTrace();
            }           
        }
         //用于设置新的出发时间
        String time = "";
        //按照每月多少号执行
        if(timer.getType() == 1){
            //0 15 10 15 * ? 每月15号上午10点15分触发   格式
            time = "0 "+timer.getMinute()+" "+timer.getHour()+" "+timer.getDay()+" * ?";
        }else if(timer.getType() == 2){
            //按周来执行  0 59 2 ? * FRI    每周5凌晨2点59分触发；  
            time = "0 "+timer.getMinute()+" "+timer.getHour()+" ? * "+StringUtil.parseWeek(timer.getWeek());
        }else if(timer.getType() == 3){
            //每天执行 0 15 10 ? * * 每天10点15分触发
            time = "0 "+timer.getMinute()+" "+timer.getHour()+" ? * *";
        }else{
            operLogService.addLog("预启动", "更新时间失败", "1", "2", request);
            return new MethodResult(MethodResult.FAIL,"模式错误");
        }
        try {
            JobDetail jdCheck = QuartzManage.getQuartzManage().getScheduler().getJobDetail(new JobKey(timer.getId(),"groupJob"));           
                //没有任务则添加任务
                if(jdCheck==null){
                    //定义任务
                    JobDetail jd = JobBuilder.newJob(OperationJob.class)
                            .withIdentity(new JobKey(timer.getId(), "groupJob"))
                            .usingJobData("key", timer.getKey())
                            .requestRecovery(true)
                            .build();
                    //定义触发器
                    CronTrigger ct = (CronTrigger)TriggerBuilder.newTrigger()
                            .withIdentity(new TriggerKey(timer.getId(), "groupTrigger"))
                            .withSchedule(CronScheduleBuilder.cronSchedule(time)
                                .withMisfireHandlingInstructionDoNothing())
                            .startNow()
                            .build();
                    //添加任务
                    QuartzManage.getQuartzManage().addTrigger(jd, ct);
                }else{//存在任务则修改任务 
                        //定义触发器
                        CronTrigger ct = (CronTrigger)TriggerBuilder.newTrigger()
                                .withIdentity(new TriggerKey(timer.getId(), "groupTrigger"))
                                .usingJobData("key", timer.getKey())
                                .withSchedule(CronScheduleBuilder.cronSchedule(time)
                                    .withMisfireHandlingInstructionDoNothing())
                                .startNow()
                                .build();
                        //修改任务出发的时间规则
                        QuartzManage.getQuartzManage().updateTrigger(new TriggerKey(timer.getId(),"groupTrigger"), ct);
                    }   
         } catch (Exception e) {
            e.printStackTrace();
            operLogService.addLog("预启动", "更新时间失败", "1", "2", request);
            return new MethodResult(MethodResult.FAIL, "更新失败");
        }
        operLogService.addLog("预启动", "更新时间成功", "1", "1", request);
        return new MethodResult(MethodResult.SUCCESS, "更新成功");
    }


    /**
     * @function 更新参与定时操作任务的云主机
     * @param request
     * @param response
     * @return 更新参与定时操作任务的云主机是否成功
     * @throws Exception
     */
    @RequestMapping(value = "/update", method=RequestMethod.POST)
    @ResponseBody
    public MethodResult updateStartupHostInTimer(HttpServletRequest request, HttpServletResponse response) throws Exception {
        if( ! new TransFormPrivilegeUtil().isHasPrivilege(request, TransFormPrivilegeConstant.desktop_set_time_operation_host_update)){
            return new MethodResult(MethodResult.FAIL,"您没有修改备份主机的权限，请联系管理员");
        }
        String hostId = request.getParameter("hostId");
        String key = request.getParameter("key");
         try {  
            return cloudHostService.saveHostInTimer(hostId.split(","), key);
 
        } catch (Exception e) {
            e.printStackTrace();
            return new MethodResult(MethodResult.FAIL,"保存失败");

        }
    }


}
