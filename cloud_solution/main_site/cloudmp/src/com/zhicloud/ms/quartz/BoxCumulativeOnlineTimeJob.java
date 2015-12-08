package com.zhicloud.ms.quartz;

import com.zhicloud.ms.app.listener.WarehouseCheckTimeListener;

import com.zhicloud.ms.service.IBoxRealInfoService;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException; 
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
* 
* @ClassName: WarehouseCheckCountJob 
* @Description: 主机仓库检测数据
* @author sasa
* @date 2015年8月25日 下午4:17:11 
*
 */
public class BoxCumulativeOnlineTimeJob implements Job{
    private static BoxCumulativeOnlineTimeJob instance = null;
    BeanFactory factory = new ClassPathXmlApplicationContext("classpath:/applicationContext*.xml"); 
    IBoxRealInfoService boxRealInfoService = (IBoxRealInfoService)factory.getBean("boxRealInfoService");
    private final static Logger logger = Logger.getLogger(WarehouseCheckTimeListener.class);

    public synchronized static BoxCumulativeOnlineTimeJob singleton() {
        if (BoxCumulativeOnlineTimeJob.instance == null) {
            BoxCumulativeOnlineTimeJob.instance = new BoxCumulativeOnlineTimeJob();
        }
        return BoxCumulativeOnlineTimeJob.instance;
    }
    
    @Override
    public void execute(JobExecutionContext context)throws JobExecutionException {

        try {

            boxRealInfoService.CumulativeOnLineTime();

        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        }
    }

}
