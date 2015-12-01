package com.zhicloud.ms.quartz;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Hashtable;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException; 
 





import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zhicloud.ms.app.pool.computePool.ComputeInfoExt;
import com.zhicloud.ms.app.pool.computePool.ComputeInfoPool;
import com.zhicloud.ms.app.pool.computePool.ComputeInfoPoolManager;
import com.zhicloud.ms.constant.AppConstant;
import com.zhicloud.ms.httpGateway.HttpGatewayChannelExt;
import com.zhicloud.ms.httpGateway.HttpGatewayManager;  
import com.zhicloud.ms.service.IComputePoolService;

public class ComputeInfoCacheJob implements Job{
    
    private static final Logger logger = Logger.getLogger(ComputeInfoCacheJob.class); 
    BeanFactory factory = new ClassPathXmlApplicationContext("classpath:/applicationContext*.xml");
    IComputePoolService computePoolService = (IComputePoolService)factory.getBean("computePoolService");


    @Override
    public void execute(JobExecutionContext arg0) throws JobExecutionException {
        try{
            logger.info("ComputeInfoCacheJob.execute->begin to get channel");
            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
            // 获取计算资源池
            logger.info("ComputeInfoCacheJob.execute->begin to get get compute pool");
            JSONObject allPool = channel.computePoolQuery();
            if("fail".equals(allPool.getString("status"))) {
                logger.info("ComputeInfoCacheJob.execute->fail to get compute pool");
                return;
            }
            JSONArray computerPoolList = allPool.getJSONArray("compute_pools");
            ComputeInfoPool  pool = ComputeInfoPoolManager.singleton().getPool();
            Map<String,ComputeInfoExt> newPoolData = new Hashtable<String, ComputeInfoExt>();

            //循环查询计算资源池详细信息
            for (int i = 0; i < computerPoolList.size(); i ++) {
                JSONObject computerObject = computerPoolList.getJSONObject(i); 
                String uuid = computerObject.getString("uuid"); 
                String name = computerObject.getString("name"); 
                int status = computerObject.getInt("status");
                Integer cpuCount = computerObject.getInt("cpu_count");
                BigDecimal cpuUsage = new BigDecimal(computerObject.getString("cpu_usage"));
                BigDecimal memoryUsage = new BigDecimal(computerObject.getString("memory_usage"));
                BigDecimal diskUsage = new BigDecimal(computerObject.getString("disk_usage"));
                
                JSONArray memoryList = computerObject.getJSONArray("memory");
                BigInteger[] mcount = new BigInteger[memoryList.size()];
                for(int j=0;j<memoryList.size();j++){
                    mcount[j] = new BigInteger(memoryList.getString(j));
                }
                
                JSONArray diskList = computerObject.getJSONArray("disk_volume");
                BigInteger[] dcount = new BigInteger[diskList.size()];
                for(int j=0;j<diskList.size();j++){
                    dcount[j] = new BigInteger(diskList.getString(j));
                }
                
                JSONArray nList = computerObject.getJSONArray("node");
                Integer[] ncount = new Integer[nList.size()];
                for(int j=0;j<nList.size();j++){
                    ncount[j] = nList.getInt(j);
                }
                
                JSONArray hList = computerObject.getJSONArray("host");
                Integer[] hcount = new Integer[hList.size()];
                for(int j=0;j<hList.size();j++){
                    hcount[j] = hList.getInt(j);
                }
                logger.info("ComputeInfoCacheJob.execute->begin to get compute pool detail");
                ComputeInfoExt computer = computePoolService.getComputePoolDetailSync(uuid);
                computer.setCpuCount(cpuCount);
                computer.setCpuUsage(cpuUsage);
                computer.setDiskUsage(diskUsage);
                computer.setDiskVolume(dcount);
                computer.setHost(hcount);
                computer.setMemory(mcount);
                computer.setMemoryUsage(memoryUsage);
                computer.setName(name);
                computer.setNode(ncount);
                computer.setStatus(status);
                computer.setUuid(uuid);
                computer.setRegion(1);
                newPoolData.put(uuid, computer);
                pool.putToComputePool(uuid, computer);
            }
            pool.clearComputePool();
            pool.setComputePool(newPoolData);
            logger.info("ComputeInfoCacheJob.execute->finish get compute pool and update cache");
        }catch(Exception e){
            e.printStackTrace();
            
        }
         
    }

}
