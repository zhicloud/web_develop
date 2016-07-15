package com.zhicloud.ms.quartz;
 

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException; 
 

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

 

import com.zhicloud.ms.app.listener.task.AddressFetcherRunnable;
import com.zhicloud.ms.service.ICloudHostService; 

public class CloudHostRunningStatusJob implements Job {
    
    private static CloudHostRunningStatusJob instance = null;
    
     private static ICloudHostService cloudHostService = null;
     private final static Logger logger = Logger.getLogger(BackUpJob.class);
     
     public  static CloudHostRunningStatusJob singleton() {
         if (instance == null) {
             instance = new CloudHostRunningStatusJob();
             BeanFactory factory = new ClassPathXmlApplicationContext("classpath:/applicationContext*.xml"); 
             cloudHostService = (ICloudHostService)factory.getBean("cloudHostService");

         }
         return instance;
     }
     
    @Override
    public void execute(JobExecutionContext context)throws JobExecutionException { 
        logger.info("begin to update cloud host running status");
        cloudHostService.updateCloudHostRunningStatus();
        
     }
    
    public void destroy() {
        logger.info("destory the device fetcher listener");
         
    }
    
    

}
