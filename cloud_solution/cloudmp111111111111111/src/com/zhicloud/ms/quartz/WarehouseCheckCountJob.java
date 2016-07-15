package com.zhicloud.ms.quartz;

 import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException; 
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zhicloud.ms.app.listener.WarehouseCheckTimeListener;
import com.zhicloud.ms.constant.AppConstant;
import com.zhicloud.ms.service.ICloudHostWarehouseService; 
import com.zhicloud.ms.vo.CloudHostWarehouse;
/**
* 
* @ClassName: WarehouseCheckCountJob 
* @Description: 主机仓库检测数据
* @author sasa
* @date 2015年8月25日 下午4:17:11 
*
 */
public class WarehouseCheckCountJob implements Job{
    private static  WarehouseCheckCountJob instance = null;
    
    BeanFactory factory = new ClassPathXmlApplicationContext("classpath:/applicationContext*.xml"); 
    ICloudHostWarehouseService cloudHostWarehouseService = (ICloudHostWarehouseService)factory.getBean("cloudHostWarehouseService");
    private final static Logger logger = Logger.getLogger(WarehouseCheckTimeListener.class);
    public synchronized static WarehouseCheckCountJob singleton() {
        if (WarehouseCheckCountJob.instance == null) {
            WarehouseCheckCountJob.instance = new WarehouseCheckCountJob();
        }
        return WarehouseCheckCountJob.instance;
    }
    @Override
    public void execute(JobExecutionContext context)throws JobExecutionException {  
        List<CloudHostWarehouse> warehouseList = cloudHostWarehouseService.getAll();
        for(CloudHostWarehouse warehouse : warehouseList){
            cloudHostWarehouseService.updateCount(warehouse.getId());
        }
    }

}
