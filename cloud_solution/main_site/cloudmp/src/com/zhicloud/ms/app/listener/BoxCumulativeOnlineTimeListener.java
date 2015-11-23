package com.zhicloud.ms.app.listener;

import com.zhicloud.ms.app.cache.BoxRealInfoCacheManager;
import com.zhicloud.ms.quartz.BoxCumulativeOnlineTimeBatchJob;
import com.zhicloud.ms.quartz.BoxCumulativeOnlineTimeJob;
import com.zhicloud.ms.quartz.QuartzManage;
import com.zhicloud.ms.vo.BoxRealInfoVO;
import org.apache.log4j.Logger;
import org.quartz.*;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class BoxCumulativeOnlineTimeListener implements ServletContextListener{
    
    public static final Logger logger = Logger.getLogger(BoxCumulativeOnlineTimeListener.class);

    //每隔1分钟执行一次：0 */1 * * * ?
    private static final String PERIOD = "0 */1 * * * ?";

    //每隔15分钟执行一次：0 */15 * * * ?
    private static final String TIMMER = "0 */15 * * * ?";

    public static final String  BOX_CUMULATIVE_TIME_QUARTZ_ID= "timer_for_boxUser_cumulative_onlineTime"; //在线用户时长计算
    public static final String  BOX_CUMULATIVE_TIME_BATCH_QUARTZ_ID= "timer_for_boxUser_cumulative_onlineTime_batch"; //在线用户时长计算累计更新



    @Override
    public void contextDestroyed(ServletContextEvent arg0) {
        // TODO Auto-generated method stub       
    }

    @Override
    public void contextInitialized(ServletContextEvent arg0) {
        // 盒子用户计算时长任务启动。
        logger.debug("BoxCumulativeOnLineTimeListener.init()");   
  
        try {
           JobDetail jdCheck = QuartzManage.getQuartzManage().getScheduler().getJobDetail(new JobKey(BOX_CUMULATIVE_TIME_QUARTZ_ID,"groupJob"));           
               //没有任务则添加任务
               if(jdCheck==null){
                   //定义任务
                   JobDetail jd = JobBuilder.newJob(BoxCumulativeOnlineTimeJob.class)
                           .withIdentity(new JobKey(BOX_CUMULATIVE_TIME_QUARTZ_ID,"groupJob"))   
                           .requestRecovery(true)
                           .build();
                   //定义触发器
                   CronTrigger ct = (CronTrigger)TriggerBuilder.newTrigger()
                           .withIdentity(new TriggerKey(BOX_CUMULATIVE_TIME_QUARTZ_ID,"groupTrigger"))
                           .withSchedule(CronScheduleBuilder.cronSchedule(PERIOD).withMisfireHandlingInstructionDoNothing())
                           .startNow()
                           .build();
                   //添加任务
                   QuartzManage.getQuartzManage().addTrigger(jd, ct);
               }   
        } catch (SchedulerException e) {
           e.printStackTrace();
        }

        try {
            JobDetail jdCheck = QuartzManage.getQuartzManage().getScheduler().getJobDetail(new JobKey(BOX_CUMULATIVE_TIME_BATCH_QUARTZ_ID,"groupJob"));
            //没有任务则添加任务
            if(jdCheck==null){
                //定义任务
                JobDetail jd = JobBuilder.newJob(BoxCumulativeOnlineTimeBatchJob.class)
                    .withIdentity(new JobKey(BOX_CUMULATIVE_TIME_BATCH_QUARTZ_ID,"groupJob"))
                    .requestRecovery(true)
                    .build();
                //定义触发器
                CronTrigger ct = (CronTrigger)TriggerBuilder.newTrigger()
                    .withIdentity(new TriggerKey(BOX_CUMULATIVE_TIME_BATCH_QUARTZ_ID,"groupTrigger"))
                    .withSchedule(CronScheduleBuilder.cronSchedule(TIMMER).withMisfireHandlingInstructionDoNothing())
                    .startNow()
                    .build();
                //添加任务
                QuartzManage.getQuartzManage().addTrigger(jd, ct);
            }

            BoxRealInfoVO box = new BoxRealInfoVO();
            box.setUserId("b0232eff8c5b47f082f7b5ee9e7622fb");
            box.setCumulativeOnlineTime(12345678);

            BoxRealInfoCacheManager.singleton().getCache().put(box);

        } catch (SchedulerException e) {
            e.printStackTrace();
        }

    }

}
