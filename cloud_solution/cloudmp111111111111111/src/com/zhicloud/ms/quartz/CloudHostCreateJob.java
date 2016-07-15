package com.zhicloud.ms.quartz;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException; 
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

 import com.zhicloud.ms.service.ICloudHostService;

public class CloudHostCreateJob implements Job{
    
    private static CloudHostCreateJob instance = null;

    
     private static ICloudHostService cloudHostService = null;
     private final static Logger logger = Logger.getLogger(BackUpJob.class);
     
     public synchronized static CloudHostCreateJob singleton() {
         if (CloudHostCreateJob.instance == null) {
             CloudHostCreateJob.instance = new CloudHostCreateJob();
             BeanFactory factory = new ClassPathXmlApplicationContext("classpath:/applicationContext*.xml"); 
             cloudHostService = (ICloudHostService)factory.getBean("cloudHostService");
         }
         return CloudHostCreateJob.instance;
     }
     
    @Override
    public void execute(JobExecutionContext context)throws JobExecutionException { 
        logger.info("begin to create host");
        cloudHostService.createOneCloudHost();
        
     }
    
    public void destroy() {
        logger.info("destory the device fetcher listener");
         
    }

}
