package com.zhicloud.ms.quartz;
 

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;
 
import com.zhicloud.ms.service.ICloudHostService; 

public class CloudHostRunningStatusJob implements Job {
    
    BeanFactory factory = new ClassPathXmlApplicationContext("classpath:/applicationContext*.xml"); 
    ICloudHostService cloudHostService = (ICloudHostService)factory.getBean("cloudHostService");
     private final static Logger logger = Logger.getLogger(BackUpJob.class);
     
    @Override
    public void execute(JobExecutionContext context)throws JobExecutionException { 
        logger.info("begin to update cloud host running status");
        cloudHostService.updateCloudHostRunningStatus();
        
     }
    
    

}
