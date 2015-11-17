package com.zhicloud.ms.app.pool;

import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.zhicloud.ms.common.util.NumberUtil;
import com.zhicloud.ms.common.util.StringUtil;
import com.zhicloud.ms.common.util.json.JSONLibUtil;
import com.zhicloud.ms.constant.AppConstant;
import com.zhicloud.ms.exception.AppException;
import com.zhicloud.ms.service.ICloudHostService;
import com.zhicloud.ms.vo.CloudHostVO;  
public class CloudHostPoolManager {

	private static final Logger logger = Logger.getLogger(CloudHostPoolManager.class);
	private static final CloudHostPoolManager singleton = new CloudHostPoolManager();
	private final CloudHostPool cloudHostPool = new CloudHostPool();
	private boolean initialized = false;
	private ICloudHostService cloudHostService;
	private final static BlockingQueue<JSONArray> hostVOueue = new ArrayBlockingQueue<JSONArray>(2000);
	public static CloudHostPoolManager getSingleton() {
		return singleton;
	}

	public static CloudHostPool getCloudHostPool() {
		return getSingleton().cloudHostPool;
	}

	/**
	 * 初始化云主机缓冲池
	 */
	public boolean initCloudHostPool() throws MalformedURLException, IOException {
		logger.info("init cloud host pool.");
        BeanFactory factory = new ClassPathXmlApplicationContext("classpath:/applicationContext*.xml");  
        
		cloudHostService = (ICloudHostService)factory.getBean("cloudHostService"); 

		// 初始化时，从数据库获取云主机的配置信息
 		List<CloudHostVO> allCloudHosts = cloudHostService.getAllCloudHost();

		for (int i = 0; i < allCloudHosts.size(); i++) {
			CloudHostVO cloudHost = allCloudHosts.get(i);

			CloudHostData cloudHostData = new CloudHostData();
			// cloudHostData.setId(cloudHost.getId());
			cloudHostData.setRealHostId(cloudHost.getRealHostId());
			cloudHostData.setHostName(cloudHost.getHostName());
			cloudHostData.setCpuCore(cloudHost.getCpuCore());
			cloudHostData.setCpuUsage(0.0);
			cloudHostData.setMemory(cloudHost.getMemory());
			cloudHostData.setMemoryUsage(0.0);
			cloudHostData.setSysDisk(cloudHost.getSysDisk());
			cloudHostData.setSysDiskUsage(0.0);
			cloudHostData.setDataDisk(cloudHost.getDataDisk());
			cloudHostData.setDataDiskUsage(0.0);
			cloudHostData.setBandwidth(cloudHost.getBandwidth()); 
			cloudHostData.setInnerIp(cloudHost.getInnerIp());
			cloudHostData.setInnerPort(cloudHost.getInnerPort());
			cloudHostData.setOuterIp(cloudHost.getOuterIp());
			cloudHostData.setOuterPort(cloudHost.getOuterPort());
			cloudHostData.setRunningStatus(cloudHost.getRunningStatus());
			cloudHostData.setStatus(cloudHost.getStatus());
			cloudHostData.setRegion(cloudHost.getRegion());

			cloudHostPool.put(cloudHostData);
		}
		//处理异步接口返回的业务数据
		for(int i=0;i<5;i++){  //5个线程处理,处理来不及可以多个。
            new Thread(){
                public void run(){
                    while(true){
                    	try {
 							JSONArray hostList = hostVOueue.take();
							
							for (int index = 0; index < hostList.size(); index++) {	
								JSONObject host = hostList.getJSONObject(index);
								// 更新数据
								try {
									CloudHostPoolManager.this.updateMonitorData(host);
								} catch (Exception e) {
									logger.warn(String.format("error occurs when processing host monitor data. host[%s], uuid[%s]", 
											JSONLibUtil.getString(host, "name"), JSONLibUtil.getString(host, "uuid")), e);
								}
							}
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}//没有就会阻塞
                    }
                }
            }.start();	
		}
		initialized = true;
		return true;
	}

	public boolean isInitialized() {
		return this.initialized;
	}

	/**
	 * 更新或增加云主机
	 * 
	 * @param region
	 * @param realCloudHost
	 * @return
	 */
	public boolean updateRealCloudHost(int region, JSONObject realCloudHost,CloudHostVO host) {
		String uuid = JSONLibUtil.getString(realCloudHost, "uuid");
		String name = JSONLibUtil.getString(realCloudHost, "name");
		Integer cpuCount = JSONLibUtil.getInteger(realCloudHost, "cpu_count");
		Double cpuUsage = JSONLibUtil.getDouble(realCloudHost, "cpu_usage");
		BigInteger[] memory = JSONLibUtil.getBigIntegerArray(realCloudHost, "memory"); // [可用,总量]
		Double memoryUsage = JSONLibUtil.getDouble(realCloudHost, "memory_usage");
		BigInteger[] diskVolume = JSONLibUtil.getBigIntegerArray(realCloudHost, "disk_volume"); // [可用,总量]
		Double diskUsage = JSONLibUtil.getDouble(realCloudHost, "disk_usage");
		String[] ip = JSONLibUtil.getStringArray(realCloudHost, "ip"); // [宿主机ip,公网ip]，不分配则为""
		Integer runningStatus = JSONLibUtil.getInteger(realCloudHost, "status"); // 0=正常,1=告警,2=故障,3=停止

		// 获取池里面已有的数据
		CloudHostData oldCloudHostData = cloudHostPool.getByRealHostId(uuid);

		CloudHostData newCloudHostData = null;
		if (oldCloudHostData == null) {
			newCloudHostData = new CloudHostData();
			newCloudHostData.setRegion(region);
			newCloudHostData.setRealHostId(uuid);
		} else {
			newCloudHostData = oldCloudHostData.clone();
		}

		newCloudHostData.setHostName(name);
		newCloudHostData.setCpuCore(cpuCount);
		newCloudHostData.setCpuUsage(cpuUsage);
		newCloudHostData.setMemory(memory[1]);
		newCloudHostData.setMemoryUsage(memoryUsage);
		newCloudHostData.setDataDisk(diskVolume[1]);
		newCloudHostData.setDataDiskUsage(diskUsage);
		newCloudHostData.setInnerIp(ip[0]);
		newCloudHostData.setOuterIp(ip[1]);
		newCloudHostData.setRunningStatus(transforRunningStatus(runningStatus));
		newCloudHostData.setLastOperStatus(0);
		
		// 如果running_status变了，则更新数据库
        if (host != null && (oldCloudHostData == null || (oldCloudHostData != null && NumberUtil.equals(newCloudHostData.getRunningStatus(), oldCloudHostData.getRunningStatus()) == false))) {
            boolean update_flag = true;
//            if(oldCloudHostData.getRunningStatus() == AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTINGDOWM && newCloudHostData.getRunningStatus() == AppConstant.CLOUD_HOST_RUNNING_STATUS_RUNNING){             
//                if(oldCloudHostData.getLastOperTime() != null){
//                    
//                    Date d1 = new Date();
//                    Date d2;
//                    try {
//                        d2 = StringUtil.stringToDate(oldCloudHostData.getLastOperTime(), "yyyyMMddHHmmssSSS");
//                        long diff = d1.getTime() - d2.getTime();
//                        long seconds = diff / (1000 * 60);
//                        if(seconds<5){ 
//                            update_flag = false;
//                        }
//                    } catch (ParseException e) { 
//                        e.printStackTrace();
//                        
//                    }           
//                }
//            }else   if(oldCloudHostData.getRunningStatus() == AppConstant.CLOUD_HOST_RUNNING_STATUS_STARTING && newCloudHostData.getRunningStatus() == AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN){             
//                if(oldCloudHostData.getLastOperTime() != null){
//                    
//                    Date d1 = new Date();
//                    Date d2;
//                    try {
//                        d2 = StringUtil.stringToDate(oldCloudHostData.getLastOperTime(), "yyyyMMddHHmmssSSS");
//                        long diff = d1.getTime() - d2.getTime();
//                        long seconds = diff / (1000 * 60);
//                        if(seconds<5){ 
//                            update_flag = false;
//                        }
//                    } catch (ParseException e) { 
//                        e.printStackTrace();
//                        
//                    }           
//                }
//            } else   if(oldCloudHostData.getRunningStatus() == AppConstant.CLOUD_HOST_RUNNING_STATUS_RESTARTING){             
//                if(oldCloudHostData.getLastOperStatus() != null && oldCloudHostData.getLastOperStatus() >= 1){
//                    update_flag = true;
//                }else if(oldCloudHostData.getLastOperTime() != null){
//                    
//                    Date d1 = new Date();
//                    Date d2;
//                    try {
//                        d2 = StringUtil.stringToDate(oldCloudHostData.getLastOperTime(), "yyyyMMddHHmmssSSS");
//                        long diff = d1.getTime() - d2.getTime();
//                        long seconds = diff / (1000 * 60);
//                        if(seconds<5){ 
//                            update_flag = false;
//                        }
//                    } catch (ParseException e) { 
//                        e.printStackTrace();
//                        
//                    }           
//                }
//            } 
            if(update_flag){ 
                if(oldCloudHostData == null){
                    oldCloudHostData = new CloudHostData();
                    oldCloudHostData.setRunningStatus(null);
                }
                logger.info("CloudHostPoolManager.updateReadCloudHost() > [" + Thread.currentThread().getId() + "] 云主机状态发生变化, realHostId:[" + newCloudHostData.getRealHostId() + "], hostName:[" + newCloudHostData.getHostName() + "], oldRunningStatus:[" + oldCloudHostData.getRunningStatus()
                        + "], newRunningStatus:[" + newCloudHostData.getRunningStatus() + "]");
                Map<String, Object> data = new LinkedHashMap<String, Object>();
                data.put("runningStatus", newCloudHostData.getRunningStatus());
                data.put("realHostId", newCloudHostData.getRealHostId());
                cloudHostService.updateRunningStatusByRealHostId(data);
                cloudHostPool.put(newCloudHostData); 
            }
        } 

		return true;
	}

	/**
	 * 只更新云主机缓冲池中的云主机
	 * 
	 * @param cloudHost
	 *            - 云主机监控数据
	 */
	public void updateMonitorData(JSONObject cloudHost) {
		String uuid = JSONLibUtil.getString(cloudHost, "uuid");
		Integer cpuCount = JSONLibUtil.getInteger(cloudHost, "cpu_count");
		Double cpuUsage = JSONLibUtil.getDouble(cloudHost, "cpu_usage");
		BigInteger[] memory = JSONLibUtil.getBigIntegerArray(cloudHost, "memory"); // [可用,总量]
		Double memoryUsage = JSONLibUtil.getDouble(cloudHost, "memory_usage");
		BigInteger[] diskVolume = JSONLibUtil.getBigIntegerArray(cloudHost, "disk_volume"); // [可用,总量]
		Double diskUsage = JSONLibUtil.getDouble(cloudHost, "disk_usage");
		BigInteger[] diskIO = JSONLibUtil.getBigIntegerArray(cloudHost, "disk_io");// 磁盘io，[读请求,读字节,写请求,写字节,IO错误次数]
		BigInteger[] networkIO = JSONLibUtil.getBigIntegerArray(cloudHost, "network_io");// 网络io，[接收字节,接收包,接收错误,接收丢包,发送字节,发送包,发送错误,发送丢包]
		BigInteger[] speed = JSONLibUtil.getBigIntegerArray(cloudHost, "speed");// 速度，单位字节/s，[读速度,写速度,接收速度,发送速度]
		Integer runningStatus = JSONLibUtil.getInteger(cloudHost, "status"); // 0=正常,1=告警,2=故障,3=停止
		String timestamp = JSONLibUtil.getString(cloudHost, "timestamp");// 时间戳，格式"yyyy-MM-dd HH:mm:ss"
		long time = 0;
		try {
			time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(timestamp).getTime();
		} catch (ParseException e) {
			logger.warn(String.format("fail to convert string to date. string[%s]", timestamp));
		}

		// 获取池里面已有的数据
		CloudHostData cloudHostData = cloudHostPool.getByRealHostId(uuid);
		if (cloudHostData == null) {
			return;
		}
		// 时间戳判断:如果时间戳小于云主机数据记录的时间戳
		if (time != 0 && time < cloudHostData.getTimestamp()) {
			return;
		}

		// 记录云主机原运行状态
		Integer originalRunningStatue = cloudHostData.getRunningStatus();

		cloudHostData.setCpuCore(cpuCount);
		cloudHostData.setCpuUsage(cpuUsage);
		cloudHostData.setMemory(memory[1]);
		cloudHostData.setMemoryUsage(memoryUsage);
		if(diskVolume.length == 1){
		    cloudHostData.setDataDisk(BigInteger.ZERO);
		}else{
		    cloudHostData.setDataDisk(diskVolume[1]);
		}
		cloudHostData.setDataDiskUsage(diskUsage);
		cloudHostData.setDiskIOReadTimes(diskIO[0]);
		cloudHostData.setDiskIOReadByte(diskIO[1]);
		cloudHostData.setDiskIOWriteTimes(diskIO[2]);
		cloudHostData.setDiskIOWriteByte(diskIO[3]);
		cloudHostData.setDiskIOErrorTimes(diskIO[4]);
		cloudHostData.setNetworkIOReceiveByte(networkIO[0]);
		cloudHostData.setNetworkIOReceivePackage(networkIO[1]);
		cloudHostData.setNetworkIOReceiveError(networkIO[2]);
		cloudHostData.setNetworkIOReceiveLoss(networkIO[3]);
		cloudHostData.setNetworkIOSendByte(networkIO[4]);
		cloudHostData.setNetworkIOSendPackage(networkIO[5]);
		cloudHostData.setNetworkIOSendError(networkIO[6]);
		cloudHostData.setNetworkIOSendLoss(networkIO[7]);
		cloudHostData.setReadSpeed(speed[0]);
		cloudHostData.setWriteSpeed(speed[1]);
		cloudHostData.setReceiveSpeed(speed[2]);
		cloudHostData.setSendSpeed(speed[3]);
		cloudHostData.setRunningStatus(transforRunningStatus(runningStatus));
		cloudHostData.setTimestamp(time);
		cloudHostData.setLastOperStatus(0);

		

        // 如果running_status变了，则更新数据库 
        if (NumberUtil.equals(cloudHostData.getRunningStatus(), originalRunningStatue) == false) {
            boolean update_flag = true;
//            if(originalRunningStatue == AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTINGDOWM && cloudHostData.getRunningStatus() == AppConstant.CLOUD_HOST_RUNNING_STATUS_RUNNING){              
//                if(cloudHostData.getLastOperTime() != null){
//                    
//                    Date d1 = new Date();
//                    Date d2;
//                    try {
//                        d2 = StringUtil.stringToDate(cloudHostData.getLastOperTime(), "yyyyMMddHHmmssSSS");
//                        long diff = d1.getTime() - d2.getTime();
//                        long seconds = diff / (1000 * 60);
//                        if(seconds<5){ 
//                            update_flag = false;
//                        }
//                    } catch (ParseException e) { 
//                        e.printStackTrace();
//                        
//                    }           
//                }
//            }else   if(originalRunningStatue == AppConstant.CLOUD_HOST_RUNNING_STATUS_STARTING && cloudHostData.getRunningStatus() == AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN){              
//               if(cloudHostData.getLastOperTime() != null){                  
//                    Date d1 = new Date();
//                    Date d2;
//                    try {
//                        d2 = StringUtil.stringToDate(cloudHostData.getLastOperTime(), "yyyyMMddHHmmssSSS");
//                        long diff = d1.getTime() - d2.getTime();
//                        long seconds = diff / (1000 * 60);
//                        if(seconds<5){ 
//                            update_flag = false;
//                        }
//                    } catch (ParseException e) { 
//                        e.printStackTrace();                        
//                    }           
//                }
//            }
//            else   if(originalRunningStatue == AppConstant.CLOUD_HOST_RUNNING_STATUS_RESTARTING){             
//                if(cloudHostData.getLastOperStatus() != null && cloudHostData.getLastOperStatus() >= 1){
//                    update_flag = true;
//                }else if(cloudHostData.getLastOperTime() != null){
//                    
//                    Date d1 = new Date();
//                    Date d2;
//                    try {
//                        d2 = StringUtil.stringToDate(cloudHostData.getLastOperTime(), "yyyyMMddHHmmssSSS");
//                        long diff = d1.getTime() - d2.getTime();
//                        long seconds = diff / (1000 * 60);
//                        if(seconds<5){ 
//                            update_flag = false;
//                        }
//                    } catch (ParseException e) { 
//                        e.printStackTrace();
//                        
//                    }           
//                }
//            } 
            if(update_flag){                 
                logger.debug(String.format("host running status changes, realHostId:[%s], hostName:[%s], running_status:[%s->%s].", cloudHostData.getRealHostId(), cloudHostData.getHostName(), originalRunningStatue, cloudHostData.getRunningStatus()));
                Map<String, Object> data = new LinkedHashMap<String, Object>();
                data.put("runningStatus", cloudHostData.getRunningStatus());
                data.put("realHostId", cloudHostData.getRealHostId());
                cloudHostService.updateRunningStatusByRealHostId(data);
                
            }
        }
        cloudHostPool.put(cloudHostData);
	}

	/**
	 * 
	 * @param hostList
	 */
	public void updateMonitorDataThreadly(final JSONArray hostList) {
		Thread processThread = new Thread() {// 匿名内部类,处理一系列云主机的资源监控数据。
			@Override
			public void run() {
				// 遍历所有主机监控数据
				for (int index = 0; index < hostList.size(); index++) {
					JSONObject host = hostList.getJSONObject(index);
					// 更新数据
					try {
						CloudHostPoolManager.this.updateMonitorData(host);
					} catch (Exception e) {
						logger.warn(String.format("error occurs when processing host monitor data. host[%s], uuid[%s]", JSONLibUtil.getString(host, "name"), JSONLibUtil.getString(host, "uuid")), e);
					}
				}
			}

		};

		processThread.setDaemon(true);
		processThread.start();
	}


	/**
	 * 
	 * @param hostList
	 */
	public void puHostList(final JSONArray hostList) {
		try {
			hostVOueue.put(hostList);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private Integer transforRunningStatus(Integer runningStatus) {
		if (runningStatus == null) {
			return null;
		}
		if (runningStatus == 0) {
			return AppConstant.CLOUD_HOST_RUNNING_STATUS_RUNNING;
		} else if (runningStatus == 1) {
			return AppConstant.CLOUD_HOST_RUNNING_STATUS_ALARM;
		} else if (runningStatus == 2) {
			return AppConstant.CLOUD_HOST_RUNNING_STATUS_FAULT;
		} else if (runningStatus == 3) {
			return AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN;
		} else {
			throw new AppException("wrong value of runningStatus[" + runningStatus + "]");
		}
	}
	public CloudHostData getCloudHostFromPool(String id){
		return  cloudHostPool.getByRealHostId(id);
	}
}
