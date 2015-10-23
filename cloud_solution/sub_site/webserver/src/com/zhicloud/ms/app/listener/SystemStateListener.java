package com.zhicloud.ms.app.listener;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.log4j.Logger;
import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.SchedulerException;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;

import com.zhicloud.ms.exception.AppException;
import com.zhicloud.ms.quartz.QuartzManage;
import com.zhicloud.ms.quartz.SystemStateJob;

public class SystemStateListener implements Servlet {

    private final static Logger logger = Logger.getLogger(SystemStateListener.class);
    
    @Override
    public void init(ServletConfig config) throws ServletException {
        logger.info("start scheduler");
        try {
            QuartzManage.getQuartzManage().getScheduler().start();
            
            
            // 1小时执行一次
            String checkTime = "0 0 */1 * * ? *";
            //String checkTime = "0 0/5 * * * ? *";  // 测试5分钟一次
            //定义任务
            JobDetail jd = JobBuilder.newJob(SystemStateJob.class)
                    .withIdentity("job1","group1")
                    .requestRecovery(true)
                    .build();
            //定义触发器
            CronTrigger ct = (CronTrigger)TriggerBuilder.newTrigger()
                    .withIdentity(new TriggerKey("trigger1","group1"))
                    .withSchedule(CronScheduleBuilder.cronSchedule(checkTime).withMisfireHandlingInstructionDoNothing())
                    .startNow()
                    .build();
            //添加任务
            QuartzManage.getQuartzManage().addTrigger(jd, ct);
            
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void destroy() {
        logger.info("destory all the service fetcher listers");
    }

    @Override
    public ServletConfig getServletConfig() {
        throw new AppException("unsupported method");
        //return null;
    }

    @Override
    public String getServletInfo() {
        throw new AppException("unsupported method");
        // return null;
    }


    @Override
    public void service(ServletRequest arg0, ServletResponse arg1)
            throws ServletException, IOException {
        throw new AppException("unsupported method");
    }

}
