package com.zhicloud.ms.app.listener;

import com.zhicloud.ms.common.util.StringUtil;
import com.zhicloud.ms.constant.AppConstant;
import com.zhicloud.ms.quartz.BackUpJob;
import com.zhicloud.ms.quartz.OperationJob;
import com.zhicloud.ms.quartz.QuartzManage;
import com.zhicloud.ms.quartz.WarehouseCheckCountJob;
import com.zhicloud.ms.service.ITimerInfoService;
import com.zhicloud.ms.vo.TimerInfoVO;
import org.apache.log4j.Logger;
import org.quartz.*;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
 
public class TimerCheckListener implements ServletContextListener{
    
    public static final Logger logger = Logger.getLogger(TimerCheckListener.class);
    BeanFactory factory = new ClassPathXmlApplicationContext("classpath:/applicationContext*.xml"); 
    ITimerInfoService timerInfoService = (ITimerInfoService)factory.getBean("timerInfoService");
    
     

    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        
        
        // 仓库检查数值正确性的定时器
        logger.debug("CloudHostWarehouseListener.init()");   
        //用于设置新的出发时间
       String time = "30 34 * * * ?";
        try {
           JobDetail jdCheck = QuartzManage.getQuartzManage().getScheduler().getJobDetail(new JobKey(AppConstant.WAREHOUSE_CHECK_COUNT_QUARTZ_ID,"groupJob"));           
               //没有任务则添加任务
               if(jdCheck==null){
                   //定义任务
                   JobDetail jd = JobBuilder.newJob(WarehouseCheckCountJob.class)
                           .withIdentity(new JobKey(AppConstant.WAREHOUSE_CHECK_COUNT_QUARTZ_ID,"groupJob"))   
                           .requestRecovery(true)
                           .build();
                   //定义触发器
                   CronTrigger ct = (CronTrigger)TriggerBuilder.newTrigger()
                           .withIdentity(new TriggerKey(AppConstant.WAREHOUSE_CHECK_COUNT_QUARTZ_ID,"groupTrigger"))
                           .withSchedule(CronScheduleBuilder.cronSchedule(time).withMisfireHandlingInstructionDoNothing())
                           .startNow()
                           .build();
                   //添加任务
                   QuartzManage.getQuartzManage().addTrigger(jd, ct);
               }   
        } catch (SchedulerException e) {
           e.printStackTrace();
        }
        
        // 检查定时备份定时器
        
        TimerInfoVO timer = timerInfoService.queryTimerInfoByKey("desktop_back_up");
        if(timer != null){
          //用于设置新的出发时间
            time = "";
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
            }
            try {
                JobDetail jdCheck = QuartzManage.getQuartzManage().getScheduler().getJobDetail(new JobKey(timer.getId(),"groupJob"));           
                    //没有任务则添加任务
                    if(jdCheck==null){
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
                    } 
             } catch (SchedulerException e) {
                e.printStackTrace();
             }
        }
        
        
        // 检查定时备份服务器定时器
        
        timer = timerInfoService.queryTimerInfoByKey("server_back_up");
        if(timer != null){
          //用于设置新的出发时间
            time = "";
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
            }
            try {
                JobDetail jdCheck = QuartzManage.getQuartzManage().getScheduler().getJobDetail(new JobKey(timer.getId(),"groupJob"));           
                    //没有任务则添加任务
                    if(jdCheck==null){
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
                                .withIdentity(new TriggerKey(timer.getId(), "groupTrigger"))
                                .withSchedule(CronScheduleBuilder.cronSchedule(time)
                                    .withMisfireHandlingInstructionDoNothing())
                                .startNow()
                                .build();
                        //添加任务
                        QuartzManage.getQuartzManage().addTrigger(jd, ct);
                    } 
             } catch (SchedulerException e) {
                e.printStackTrace();
             }
        }

        // 检查定时开机定时器

        timer = timerInfoService.queryTimerInfoByKey("startup_timer");
        if(timer != null){
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
            }
        }


        // 检查定时关机定时器

        timer = timerInfoService.queryTimerInfoByKey("shutdown_timer");
        if(timer != null){
            if(timer.getType() == 1){
                //0 15 10 15 * ? 每月15号上午10点15分触发   格式
                time = "0 "+timer.getMinute()+" "+timer.getHour()+" "+timer.getDay()+" * ?";
            }else if(timer.getType() == 2){
                //按周来执行  0 59 2 ? * FRI    每周5凌晨2点59分触发；
                time = "0 "+timer.getMinute()+" "+timer.getHour()+" ? * "+StringUtil.parseWeek(timer.getWeek());
            }else if(timer.getType() == 3){
                //每天执行 0 15 10 ? * * 每天10点15分触发
                time = "0 "+timer.getMinute()+" "+timer.getHour()+" ? * *";
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
            }
        }

    }

}
