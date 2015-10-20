package com.zhicloud.ms.quartz;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zhicloud.ms.app.listener.WarehouseCheckTimeListener;
import com.zhicloud.ms.service.IBoxRealInfoService;

/**
* 
* @ClassName: WarehouseCheckCountJob 
* @Description: 主机仓库检测数据
* @author sasa
* @date 2015年8月25日 下午4:17:11 
*
 */
public class BoxCumulativeOnlineTimeJob implements Job{
    BeanFactory factory = new ClassPathXmlApplicationContext("classpath:/applicationContext*.xml"); 
    IBoxRealInfoService boxRealInfoService = (IBoxRealInfoService)factory.getBean("boxRealInfoService");
    private final static Logger logger = Logger.getLogger(WarehouseCheckTimeListener.class);
    @Override
    public void execute(JobExecutionContext context)throws JobExecutionException {  
    	boxRealInfoService.CumulativeOnLineTime();
    }

}
