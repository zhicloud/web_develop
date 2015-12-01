package com.zhicloud.ms.quartz;

import com.zhicloud.ms.app.listener.WarehouseCheckTimeListener;
import com.zhicloud.ms.constant.AppConstant;
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
* @author 张翔
* @date 2015年11月19日 下午4:17:11
*
 */
public class BoxCumulativeOnlineTimeBatchJob implements Job{
    BeanFactory factory = new ClassPathXmlApplicationContext("classpath:/applicationContext*.xml"); 
    IBoxRealInfoService boxRealInfoService = (IBoxRealInfoService)factory.getBean("boxRealInfoService");
    private final static Logger logger = Logger.getLogger(WarehouseCheckTimeListener.class);

    @Override
    public void execute(JobExecutionContext context)throws JobExecutionException {

        try {

            boxRealInfoService.CumulativeOnLineTimeBatch();

        } catch (Exception e) {
            logger.error(e);
            e.printStackTrace();
        }
    }

}
