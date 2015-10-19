package com.zhicloud.ms.httpGateway;

import com.zhicloud.ms.app.pool.CloudHostData;
import com.zhicloud.ms.app.pool.CloudHostPoolManager;
import com.zhicloud.ms.app.pool.computePool.ComputeInfoExt;
import com.zhicloud.ms.app.pool.computePool.ComputeInfoPool;
import com.zhicloud.ms.app.pool.computePool.ComputeInfoPoolManager;
import com.zhicloud.ms.app.pool.diskImagePool.DiskImageProgressData;
import com.zhicloud.ms.app.pool.diskImagePool.DiskImageProgressPool;
import com.zhicloud.ms.app.pool.diskImagePool.DiskImageProgressPoolManager;
import com.zhicloud.ms.app.pool.host.back.HostBackupProgressData;
import com.zhicloud.ms.app.pool.host.back.HostBackupProgressPool;
import com.zhicloud.ms.app.pool.host.back.HostBackupProgressPoolManager;
import com.zhicloud.ms.app.pool.host.reset.HostResetProgressData;
import com.zhicloud.ms.app.pool.host.reset.HostResetProgressPool;
import com.zhicloud.ms.app.pool.host.reset.HostResetProgressPoolManager;
import com.zhicloud.ms.app.pool.hostMonitorInfoPool.HostMonitorInfo;
import com.zhicloud.ms.app.pool.hostMonitorInfoPool.HostMonitorInfoManager;
import com.zhicloud.ms.app.pool.isoImagePool.IsoImageProgressData;
import com.zhicloud.ms.app.pool.isoImagePool.IsoImageProgressPool;
import com.zhicloud.ms.app.pool.isoImagePool.IsoImageProgressPoolManager;
import com.zhicloud.ms.app.pool.network.*;
import com.zhicloud.ms.app.pool.network.NetworkInfoExt.Host;
import com.zhicloud.ms.app.pool.network.NetworkInfoExt.Port;
import com.zhicloud.ms.app.pool.rule.RuleInfo;
import com.zhicloud.ms.app.pool.rule.RuleInfoPool;
import com.zhicloud.ms.app.pool.rule.RulePoolManager;
import com.zhicloud.ms.app.pool.serviceInfoPool.ServiceInfoExt;
import com.zhicloud.ms.app.pool.serviceInfoPool.ServiceInfoPool;
import com.zhicloud.ms.app.pool.serviceInfoPool.ServiceInfoPoolManager;
import com.zhicloud.ms.app.pool.storage.StorageManager;
import com.zhicloud.ms.common.util.json.JSONLibUtil;
import com.zhicloud.ms.constant.AppConstant;
import com.zhicloud.ms.constant.AppInconstant;
import com.zhicloud.ms.constant.MonitorConstant;
import com.zhicloud.ms.service.IBackUpDetailService;
import com.zhicloud.ms.util.CapacityUtil;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.PlatformResourceMonitorVO;
import com.zhicloud.ms.app.pool.snapshot.SnapshotManager;

 
 


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
 
public class HttpGatewayAsyncMessageHandlerImpl {

	private final static Logger logger = Logger.getLogger(HttpGatewayAsyncMessageHandlerImpl.class);
  
	BeanFactory factory = new ClassPathXmlApplicationContext("classpath:/applicationContext*.xml"); 
	IBackUpDetailService backUpDetailService = (IBackUpDetailService)factory.getBean("backUpDetailService");
    
	@HttpGatewayMessageHandler(messageType = "host_monitor_data")
	public Map<String, String> hostMonitorData(HttpGatewayAsyncChannel channel, JSONObject messageData) {
		logger.debug("start to process host monitor data.");
		// 新起线程处理数据
		JSONArray hostList = messageData.getJSONArray("host_list");
		
		// 新起线程处理数据2015-9-1 
		CloudHostPoolManager.getSingleton().puHostList(hostList);		
		//CloudHostPoolManager.getSingleton().updateMonitorDataThreadly(hostList);

		// 处理返回值,返回http_gateway格式{"statue":0/1}
		int task = messageData.getInt("task");
		String sessionId = channel.getSessionId();

		boolean result = false;
		HostMonitorInfo[] hostMonitorInfoList = HostMonitorInfoManager.singleton().getAllHostMonitorInfo();
		for (HostMonitorInfo hostMonitorInfo : hostMonitorInfoList) {// 遍历所有HostMonitorInfo,判断sessionid和task是否都对应。
			if (sessionId != null && sessionId.equalsIgnoreCase(hostMonitorInfo.getSessionId()) && task == hostMonitorInfo.getTask()) {// 吻合，则更新时间和设置标志位result
				hostMonitorInfo.updateTime();
				result = true;
				break;
			}
		}
		// sessionid和task对应上，则认为该监控可继续进行，否则，则认为是不可再继续的。
		Map<String, String> response = new HashMap<String, String>();
		if (result) {
			response.put("status", "1");
		} else {
			response.put("status", "0");
		}

		return response;
	}

	@HttpGatewayMessageHandler(messageType = "disk_image_create_progress")
	public void diskImageCreateProgress(HttpGatewayAsyncChannel channel, JSONObject messageData) {
		logger.debug("start to process disk image create progress data.");
		// 获取数据
		String name = messageData.getString("name");
		int progress = messageData.getInt("progress");
		String sessionId = channel.getSessionId();
		// 获取对象
		DiskImageProgressPool pool = DiskImageProgressPoolManager.singleton().getPool();
		DiskImageProgressData diskImage = pool.get(sessionId, name);
		//对象不存在
		if (diskImage == null) {
			diskImage = new DiskImageProgressData();
			diskImage.setSessionId(sessionId);
			diskImage.setName(name);
			
			pool.put(diskImage);
		}
		// 更新
		diskImage.setProgress(progress);
		diskImage.setFinished(false);
		diskImage.updateTime();
	}

	@HttpGatewayMessageHandler(messageType = "disk_image_create_result")
	public void diskImageCreateResult(HttpGatewayAsyncChannel channel, JSONObject messageData) {
		logger.debug("start to process disk image create result data.");

		String sessionId = channel.getSessionId();
		// 获取数据
		String name = messageData.getString("name");
		String message = messageData.getString("message");

		// 获取对象
		DiskImageProgressPool pool = DiskImageProgressPoolManager.singleton().getPool();
		DiskImageProgressData diskImage = pool.get(sessionId, name);
		//对象不存在
		if (diskImage == null) {
			diskImage = new DiskImageProgressData();
			diskImage.setSessionId(sessionId);
			diskImage.setName(name);
			
			pool.put(diskImage);
		}
		// 更新
		diskImage.setFinished(true);
		diskImage.setMessage(message);
		diskImage.updateTime();
		if (HttpGatewayResponseHelper.isSuccess(messageData) == false) {
			diskImage.setSuccess(false);
		} else {
			diskImage.setSuccess(true);
			String uuid = messageData.getString("uuid");
			long size = messageData.getLong("size");

			diskImage.setRealImageId(uuid);
			diskImage.setSize(size);
		}
		// 释放资源
		channel.release();
	}
	
	@HttpGatewayMessageHandler(messageType = "iso_image_upload_progress")
	public void isoImageUploadProgress(HttpGatewayAsyncChannel channel, JSONObject messageData) {
		logger.debug("start to process iso image upload progress data.");
		// 获取数据
		String name = messageData.getString("name");
		int progress = messageData.getInt("progress");
		String sessionId = channel.getSessionId();
		// 获取对象
		IsoImageProgressPool pool = IsoImageProgressPoolManager.singleton().getPool();
		IsoImageProgressData isoImage = pool.get(sessionId, name);
		//对象不存在
		if (isoImage == null) {
			isoImage = new IsoImageProgressData();
			isoImage.setSessionId(sessionId);
			isoImage.setName(name);
			
			pool.put(isoImage);
		}
		// 更新
		isoImage.setProgress(progress);
		isoImage.setFinished(false);
		isoImage.updateTime();
	}

	@HttpGatewayMessageHandler(messageType = "iso_image_upload_result")
	public void isoImageUploadResult(HttpGatewayAsyncChannel channel, JSONObject messageData) {
		logger.debug("start to process iso image upload result data.");

		String sessionId = channel.getSessionId();
		// 获取数据
		String name = messageData.getString("name");
		String message = messageData.getString("message");

		// 获取对象
		IsoImageProgressPool pool = IsoImageProgressPoolManager.singleton().getPool();
		IsoImageProgressData isoImage = pool.get(sessionId, name);
		//对象不存在
		if (isoImage == null) {
			isoImage = new IsoImageProgressData();
			isoImage.setSessionId(sessionId);
			isoImage.setName(name);
			
			pool.put(isoImage);
		}

		// 更新
		isoImage.setFinished(true);
		isoImage.setMessage(message);
		isoImage.updateTime();
		if (HttpGatewayResponseHelper.isSuccess(messageData) == false) {
			isoImage.setSuccess(false);
		} else {
			isoImage.setSuccess(true);
			String uuid = messageData.getString("uuid");
			String ip = messageData.getString("ip");
			String port = messageData.getString("port");
			BigInteger size = JSONLibUtil.getBigInteger(messageData, "size");

			isoImage.setRealImageId(uuid);
			isoImage.setIp(ip);
			isoImage.setPort(port);
			isoImage.setSize(size);
		}
		// 释放资源
		channel.release();
	}
	
	@HttpGatewayMessageHandler(messageType = "system_monitor_data")
	public Map<String, String> systemMonitorData(HttpGatewayAsyncChannel channel, JSONObject messageData) {
		logger.debug("start to process system monitor data.");
		PlatformResourceMonitorVO resource = new PlatformResourceMonitorVO();
		DecimalFormat    df   = new DecimalFormat("######0.00");   
		int task = messageData.getInt("task");//监控任务id
		JSONArray server = messageData.getJSONArray("server");//宿主机数量[停止,告警,故障,正常]
		JSONArray host = messageData.getJSONArray("host");//云主机数量[停止,告警,故障,正常]
		int cpuCount = messageData.getInt("cpu_count");//cpu总核心数
		double cpuUsage = messageData.getDouble("cpu_usage");//cpu利用率
		JSONArray memory = messageData.getJSONArray("memory");//内存空间，单位：字节，[可用,总量]
		double memoryUsage = messageData.getDouble("memory_usage");//内存利用率
		JSONArray diskVolume = messageData.getJSONArray("disk_volume");//磁盘空间，单位：字节，[可用,总量]
		double diskUsage = messageData.getDouble("disk_usage");//磁盘利用率
		JSONArray diskIO = messageData.getJSONArray("disk_io");//磁盘io，[读请求,读字节,写请求,写字节,IO错误次数]
		JSONArray networkIO = messageData.getJSONArray("network_io");//网络io，[接收字节,接收包,接收错误,接收丢包,发送字节,发送包,发送错误,发送丢包]
		JSONArray speed = messageData.getJSONArray("speed");//速度，单位字节/s，[读速度,写速度,接收速度,发送速度]
		String timestamp = messageData.getString("timestamp");//时间戳，格式"YYYY-MM-DD HH:MI:SS"
		resource.setTask(task);
		resource.setServer(server);
		resource.setHost(host);
		resource.setCpuCount(cpuCount);
		resource.setCpuUsage(df.format(cpuUsage*100)+"%");
		for(int i = 0;i<memory.size();i++){
			memory.set(i, CapacityUtil.toGBValue(new BigInteger(memory.get(i).toString()),0)+"GB");
		}
		resource.setMemory(memory);
		resource.setMemoryUsage(df.format(memoryUsage*100)+"%");
		for(int i = 0;i<diskVolume.size();i++){
			diskVolume.set(i, CapacityUtil.toGBValue(new BigInteger(diskVolume.get(i).toString()),0));
		}
		resource.setDiskVolume(diskVolume);
		resource.setDiskUsage(df.format(diskUsage*100)+"%");
		for(int i = 0;i<diskIO.size();i++){
			if(i == 1 || i == 3){				
				diskIO.set(i, CapacityUtil.toGBValue(new BigInteger(diskIO.get(i).toString()),0)+"GB");
			}
		}
 		resource.setDiskIO(diskIO);
 		for(int i = 0;i<networkIO.size();i++){
 			if(i == 0 || i == 4){			
 				networkIO.set(i, CapacityUtil.toGBValue(new BigInteger(networkIO.get(i).toString()),0)+"GB");
 			}
 		}
		resource.setNetworkIO(networkIO);
		for(int i = 0;i<speed.size();i++){
			speed.set(i, CapacityUtil.toKB(new BigInteger(speed.get(i).toString()),0));
		}
		resource.setSpeed(speed);
		resource.setTimestamp(timestamp);
//		Integer region = AppInconstant.regionAndTaskRelation.get(task);
		Integer region = 1;
		resource.setRegion(region);
		AppInconstant.platformResourceMonitorData.put(region, resource);
//		System.err.println(String.format("task[%s], server[%s], host[%s], cpu_count[%s], cpu_usage[%s], memory[%s], memory_usage[%s], disk_volume[%s], disk_usage[%s], disk_io[%s], network_io[%s], speed[%s], timestamp[%s]",
//				task, server, host, cpuCount, cpuUsage, memory, memoryUsage, diskVolume, diskUsage, diskIO, networkIO, speed, timestamp));
		Map<String, String> response = new HashMap<String, String>();
		response.put("status", "1");

		return response;
	}
	
	@HttpGatewayMessageHandler(messageType = "flush_disk_image_ack")
    public void flushDiskImageAck(HttpGatewayAsyncChannel channel, JSONObject messageData) {
        logger.debug("recieve flush disk image ack.");
        
        String sessionId = channel.getSessionId();
        
        //获取数据
        String uuid = messageData.getString("uuid");
        int disk = messageData.getInt("disk");
        
        AppInconstant.hostResetProgress.put(uuid+"_reset", "reset_true");
        
        //获取对象
        HostResetProgressPool pool = HostResetProgressPoolManager.singleton().getPool();
        HostResetProgressData hostReset = pool.get(uuid);
        
        //对象不存在
        if(hostReset == null){
            hostReset = new HostResetProgressData();
            hostReset.setRealHostId(uuid);
            hostReset.setSessionId(sessionId);
            hostReset.setResetStatus(1);
            pool.put(hostReset);
        }
        hostReset.setResetStatus(1); 
        hostReset.setReady(true);
        
        hostReset.updateTime();
        
        
        logger.info(String.format("start to flush disk image. uuid[%s], disk[%d]", uuid, disk));
    }

    @HttpGatewayMessageHandler(messageType = "flush_disk_image_progress")
    public void flushDiskImageProgress(HttpGatewayAsyncChannel channel, JSONObject messageData) {
        logger.debug("recieve flush disk image progress response.");
        
        String sessionId = channel.getSessionId();
        
        //获取数据
        String uuid = messageData.getString("uuid");
        int disk = messageData.getInt("disk");
        int level = messageData.getInt("level");

        AppInconstant.hostResetProgress.put(uuid+"_reset", "reset_true");


        //获取对象
        HostResetProgressPool pool = HostResetProgressPoolManager.singleton().getPool();
        HostResetProgressData hostReset = pool.get(uuid);
                
        //对象不存在
        if(hostReset == null){
            hostReset = new HostResetProgressData();
            hostReset.setRealHostId(uuid);
            hostReset.setSessionId(sessionId);
            hostReset.setResetStatus(1);
            pool.put(hostReset);
        }
        hostReset.setResetStatus(1);        
        hostReset.setProgress(level);
        hostReset.setFinished(false);
        hostReset.updateTime();
        
        logger.error(String.format("flush disk image at progress[%d]. uuid[%s], disk[%d]", level, uuid, disk));
    }
	
	@HttpGatewayMessageHandler(messageType = "flush_disk_image")
    public void flushDiskImage(HttpGatewayAsyncChannel channel, JSONObject messageData) {
        logger.debug("recieve flush disk image response.");
        
        //获取数据
        String sessionId = channel.getSessionId();
        String uuid = messageData.getString("uuid");
        int disk = messageData.getInt("disk");
                        
        //获取对象
        HostResetProgressPool pool = HostResetProgressPoolManager.singleton().getPool();
        HostResetProgressData hostReset = pool.get(uuid);
                
        //对象不存在
        if(hostReset == null){
            hostReset = new HostResetProgressData();
            hostReset.setRealHostId(uuid);
            hostReset.setSessionId(sessionId);
            hostReset.setResetStatus(0);
            pool.put(hostReset);
        }
        hostReset.setResetStatus(0);        
        hostReset.setFinished(true);
        hostReset.updateTime();
                
        if (HttpGatewayResponseHelper.isSuccess(messageData) == true) {
            hostReset.setSuccess(true);
            logger.info(String.format("flush disk image success. uuid[%s], disk[%d], success", uuid, disk));
        } else {
            hostReset.setSuccess(false);
            logger.error(String.format("[%s]flush disk image fail. uuid[%s]", sessionId, uuid));
        }
        AppInconstant.hostResetProgress.put(uuid+"_reset", "reset_false");
        channel.release();
        
    }
	
	@HttpGatewayMessageHandler(messageType = "backup_host_ack")
	public void backupHostAck(HttpGatewayAsyncChannel channel, JSONObject messageData) {
		logger.debug("recieve backup host ack.");
		
		String sessionId = channel.getSessionId();
		
		//获取数据
		String uuid = messageData.getString("uuid");
		
		AppInconstant.hostBackupProgress.put(uuid+"backup", "backup_true");
		
		//获取对象
		HostBackupProgressPool pool = HostBackupProgressPoolManager.singleton().getPool();
		HostBackupProgressData hostBackup = pool.get(uuid);
		
		//对象不存在
		if(hostBackup == null){
			hostBackup = new HostBackupProgressData();
			hostBackup.setSessionId(sessionId);
			hostBackup.setUuid(uuid);
			pool.put(hostBackup);
		}
		
		hostBackup.setReady(true);
		hostBackup.setSuccess(true);
		hostBackup.updateTime();
		hostBackup.setBackupStatus(9);
		
		
		System.err.println(String.format("[%s]start to backup host. uuid[%s]", sessionId, uuid));
	}

	@HttpGatewayMessageHandler(messageType = "backup_host_progress")
	public void backupHostProgress(HttpGatewayAsyncChannel channel, JSONObject messageData) {
		logger.debug("recieve backup host progress notify.");
		String sessionId = channel.getSessionId();
		
		//获取数据
		String uuid = messageData.getString("uuid");
		int level = messageData.getInt("level");
		
		//获取对象
		HostBackupProgressPool pool = HostBackupProgressPoolManager.singleton().getPool();
		HostBackupProgressData hostBackup = pool.get(uuid);
				
				
		//对象不存在
		if(hostBackup == null){
			hostBackup = new HostBackupProgressData();
			hostBackup.setSessionId(sessionId);
			hostBackup.setUuid(uuid);
			pool.put(hostBackup);
		}
		
		hostBackup.setProgress(level);
		hostBackup.setFinished(false);
		hostBackup.setReady(true);
		hostBackup.setSuccess(true);
		hostBackup.updateTime();
		hostBackup.setBackupStatus(9);
		
		System.err.println(String.format("[%s]backup host at progress %d%%. uuid[%s]", sessionId, level, uuid));
	}

	@HttpGatewayMessageHandler(messageType = "backup_host")
	public void backupHost(HttpGatewayAsyncChannel channel, JSONObject messageData) {
		logger.debug("recieve backup host progress response.");
		
		//获取数据
		String sessionId = channel.getSessionId();
		String uuid = messageData.getString("uuid");
				
		//获取对象
		HostBackupProgressPool pool = HostBackupProgressPoolManager.singleton().getPool();
		HostBackupProgressData hostBackup = pool.get(uuid);
				
		//对象不存在
		if(hostBackup == null){
			hostBackup = new HostBackupProgressData();
			hostBackup.setSessionId(sessionId);
			hostBackup.setUuid(uuid);
			pool.put(hostBackup);
		}
		
		hostBackup.setFinished(true);
		AppInconstant.hostBackupProgress.put(uuid+"backup", "backup_false");
		hostBackup.updateTime();
		hostBackup.setBackupStatus(0);
		
		if (HttpGatewayResponseHelper.isSuccess(messageData) == true) {
			hostBackup.setSuccess(true);
			// 成功 
			    backUpDetailService.updateDetail(uuid,  AppConstant.BACK_UP_DETAIL_STATUS_BACKINGUP, AppConstant.BACK_UP_DETAIL_STATUS_SUCCESS);
                backUpDetailService.updateDetail(uuid,  AppConstant.BACK_UP_DETAIL_STATUS_ANOTHOR, AppConstant.BACK_UP_DETAIL_STATUS_EXPIRED);

			
//			String timestamp = messageData.getString("timestamp");			
			System.err.println(String.format("[%s]backup host success. uuid[%s]", sessionId, uuid));
		} else {
			hostBackup.setSuccess(false);
 			// 失败 
                backUpDetailService.updateDetail(uuid,  AppConstant.BACK_UP_DETAIL_STATUS_BACKINGUP, AppConstant.BACK_UP_DETAIL_STATUS_FAIL);
                backUpDetailService.updateDetail(uuid,  AppConstant.BACK_UP_DETAIL_STATUS_ANOTHOR, AppConstant.BACK_UP_DETAIL_STATUS_SUCCESS);

            System.err.println(String.format("[%s]backup host fail. uuid[%s]", sessionId, uuid));			
		}
		channel.release();
	}

	@HttpGatewayMessageHandler(messageType = "resume_host_ack")
	public void resumeHostAck(HttpGatewayAsyncChannel channel, JSONObject messageData) {
		logger.debug("recieve resume host ack.");
		
		String sessionId = channel.getSessionId();
		
		//获取数据
		String uuid = messageData.getString("uuid");
		
		AppInconstant.hostBackupProgress.put(uuid+"resume", "resume_true");
		
		//获取对象
		HostBackupProgressPool pool = HostBackupProgressPoolManager.singleton().getPool();
		HostBackupProgressData hostBackup = pool.get(uuid);
		
		//对象不存在
		if(hostBackup == null){
			hostBackup = new HostBackupProgressData();
			hostBackup.setSessionId(sessionId);
			hostBackup.setUuid(uuid);
			pool.put(hostBackup);
		}
		
		hostBackup.setReady(true);
		hostBackup.setSuccess(true);
		hostBackup.updateTime();
		hostBackup.setBackupStatus(10);
		
		System.err.println(String.format("[%s]start to resume host. uuid[%s]", sessionId, uuid));
		
	}

	@HttpGatewayMessageHandler(messageType = "resume_host_progress")
	public void resumeHostProgress(HttpGatewayAsyncChannel channel, JSONObject messageData) {
		logger.debug("recieve resume host progress notify.");
		
		String sessionId = channel.getSessionId();
		
		//获取数据
		String uuid = messageData.getString("uuid");
		int level = messageData.getInt("level");
		
		//获取对象
		HostBackupProgressPool pool = HostBackupProgressPoolManager.singleton().getPool();
		HostBackupProgressData hostBackup = pool.get(uuid);
				
				
		//对象不存在
		if(hostBackup == null){
			hostBackup = new HostBackupProgressData();
			hostBackup.setSessionId(sessionId);
			hostBackup.setUuid(uuid);
			pool.put(hostBackup);
		}
		
		hostBackup.setProgress(level);
		hostBackup.setFinished(false);
		hostBackup.setReady(true);
		hostBackup.setSuccess(true);
		hostBackup.setBackupStatus(10);
		hostBackup.updateTime();
		
		System.err.println(String.format("[%s]resume host at progress %d%%. uuid[%s]", sessionId, level, uuid));

		
	}

	@HttpGatewayMessageHandler(messageType = "resume_host")
	public void resumeHost(HttpGatewayAsyncChannel channel, JSONObject messageData) {
		logger.debug("recieve resume host progress response.");
		
		//获取数据
		String sessionId = channel.getSessionId();
		String uuid = messageData.getString("uuid");
					
		//获取对象
		HostBackupProgressPool pool = HostBackupProgressPoolManager.singleton().getPool();
		HostBackupProgressData hostBackup = pool.get(uuid);
						
		//对象不存在
		if(hostBackup == null){
			hostBackup = new HostBackupProgressData();
			hostBackup.setSessionId(sessionId);
			hostBackup.setUuid(uuid);
				pool.put(hostBackup);
		}
				
		hostBackup.setFinished(true);
		AppInconstant.hostBackupProgress.put(uuid+"resume", "resume_false");
		hostBackup.updateTime();
		hostBackup.setBackupStatus(0);		
		if (HttpGatewayResponseHelper.isSuccess(messageData) == true) 
		{
			hostBackup.setSuccess(true);
//			String timestamp = messageData.getString("timestamp");
            backUpDetailService.updateDetail(uuid, AppConstant.BACK_UP_DETAIL_STATUS_RESUMING, AppConstant.BACK_UP_DETAIL_STATUS_SUCCESS);

//			System.err.println(String.format("[%s]resume host success. uuid[%s] timestamp[%s]", sessionId, uuid, timestamp));
		} else {
			hostBackup.setSuccess(false);
			//移除信息
			pool.remove(hostBackup);
 			System.err.println(String.format("[%s]resume host fail. uuid[%s]", sessionId, uuid));			
		}
		
		channel.release();
		
		
	}

	@HttpGatewayMessageHandler(messageType = "query_host_backup")
	public void queryHostBackup(HttpGatewayAsyncChannel channel, JSONObject messageData) {
		logger.debug("recieve query host backup response.");
		String sessionId = channel.getSessionId();
		
		//获取数据
		String uuid = messageData.getString("uuid");
		
		//获取对象
		HostBackupProgressPool pool = HostBackupProgressPoolManager.singleton().getPool();
		HostBackupProgressData hostBackup = pool.get(uuid);
		
		
		//对象不存在
		if(hostBackup == null){
			hostBackup = new HostBackupProgressData();
			hostBackup.setSessionId(sessionId);
			hostBackup.setUuid(uuid);
			pool.put(hostBackup);
		}
		
		hostBackup.updateTime();
		
		if (HttpGatewayResponseHelper.isSuccess(messageData) == true) {
			JSONArray diskVolume = messageData.getJSONArray("disk_volume");
			JSONArray timestamp = messageData.getJSONArray("timestamp");
			if(diskVolume.isEmpty()) {
				hostBackup.setAvaliable(false);
			} else {
				hostBackup.setAvaliable(true);
				if("".equals(timestamp.get(0))){
					hostBackup.setTimestamp(timestamp.get(0).toString());
				}else {
					hostBackup.setTimestamp(timestamp.get(1).toString());
				}
			}
			
			System.err.println(String.format("[%s]query host backup success. uuid[%s], disk_volume[%s], timestamp[%s]", sessionId, uuid, diskVolume, timestamp));
		} else {
			logger.error((String.format(" n[%s]query host backup fail. uuid[%s]", sessionId, uuid)));			
		}
		channel.release();
	}
	
	@HttpGatewayMessageHandler(messageType = "add_rule")
    public void addRule(HttpGatewayAsyncChannel channel, JSONObject messageData) {
        logger.debug("recieve add rule response.");
        String session = channel.getSessionId();
        
        String target = messageData.getString("target");
        int mode = messageData.getInt("mode");
        JSONArray ip = messageData.getJSONArray("ip");
        JSONArray port = messageData.getJSONArray("port");
        if (HttpGatewayResponseHelper.isSuccess(messageData) == true) {
            System.err.println(String.format("[%s]add rule success. target[%s], mode[%d], ip[%s], port[%s]", session, target, mode, ip, port));
        } else {
            System.err.println(String.format("[%s]add rule fail. target[%s], mode[%d], ip[%s], port[%s]", session, target, mode, ip, port));            
        }
        String message = HttpGatewayResponseHelper.getMessage(messageData);
         
        RuleInfoPool pool = RulePoolManager.singleton().getInfoPool();
        List<RuleInfo> ruleList = pool.get(session);
        if (ruleList == null) {
            ruleList = new ArrayList<RuleInfo>();
            RuleInfo rule = new RuleInfo();
            rule.setSessionId(session);
            rule.setMessage("");
            rule.initAsyncStatus();
            ruleList.add(rule);
            pool.put(ruleList);
        }

        if (HttpGatewayResponseHelper.isSuccess(messageData) == true) {
             
            RuleInfo rule = ruleList.get(0); 
            rule.success();
            rule.updateTime();
            rule.setMessage(message); 
        } else {
            ruleList.get(0).fail();
            ruleList.get(0).updateTime();
            ruleList.get(0).setMessage(message);
        }
        synchronized (ruleList.get(0)) {
            ruleList.get(0).notifyAll();
        }
    }

    @HttpGatewayMessageHandler(messageType = "remove_rule")
    public void removeRule(HttpGatewayAsyncChannel channel, JSONObject messageData) {
        logger.debug("recieve remove rule response.");
        String session = channel.getSessionId();
        
        String target = messageData.getString("target");
        int mode = messageData.getInt("mode");
        JSONArray ip = messageData.getJSONArray("ip");
        JSONArray port = messageData.getJSONArray("port");
        if (HttpGatewayResponseHelper.isSuccess(messageData) == true) {
            System.err.println(String.format("[%s]remove rule success. target[%s], mode[%d], ip[%s], port[%s]", session, target, mode, ip, port));
        } else {
            System.err.println(String.format("[%s]remove rule fail. target[%s], mode[%d], ip[%s], port[%s]", session, target, mode, ip, port));         
        }
        String message = HttpGatewayResponseHelper.getMessage(messageData);
         
        RuleInfoPool pool = RulePoolManager.singleton().getInfoPool();
        List<RuleInfo> ruleList = pool.get(session);
        if (ruleList == null) {
            ruleList = new ArrayList<RuleInfo>();
            RuleInfo rule = new RuleInfo();
            rule.setSessionId(session);
            rule.setMessage("");
            rule.initAsyncStatus();
            ruleList.add(rule);
            pool.put(ruleList);
        }

        if (HttpGatewayResponseHelper.isSuccess(messageData) == true) {
             
            RuleInfo rule = ruleList.get(0); 
            rule.success();
            rule.updateTime();
            rule.setMessage(message); 
        } else {
            ruleList.get(0).fail();
            ruleList.get(0).updateTime();
            ruleList.get(0).setMessage(message);
        }
        synchronized (ruleList.get(0)) {
            ruleList.get(0).notifyAll();
        }
    }

    @HttpGatewayMessageHandler(messageType = "query_rule")
    public void queryRule(HttpGatewayAsyncChannel channel, JSONObject messageData) {
        logger.debug("recieve query rule response.");
        String sessionId = channel.getSessionId();
        
        String target = messageData.getString("target");
        if (HttpGatewayResponseHelper.isSuccess(messageData) == true) {
            JSONArray mode = messageData.getJSONArray("mode");
            JSONArray ip = messageData.getJSONArray("ip");
            JSONArray port = messageData.getJSONArray("port");
            System.err.println(String.format("[%s]query rule success. target[%s], mode[%s], ip[%s], port[%s]", sessionId, target, mode, ip, port));
 
        } else {
            System.err.println(String.format("[%s]query rule fail. target[%s]", sessionId, target));            
        }
        
        String message = HttpGatewayResponseHelper.getMessage(messageData);
 
        RuleInfoPool pool = RulePoolManager.singleton().getInfoPool();
        List<RuleInfo> ruleList = pool.get(sessionId);
        if (ruleList == null) {
            ruleList = new ArrayList<RuleInfo>();
            RuleInfo rule = new RuleInfo();
            rule.setSessionId(sessionId);
            rule.setMessage("");
            rule.initAsyncStatus();
            ruleList.add(rule);
            pool.put(ruleList);
        }

        if (HttpGatewayResponseHelper.isSuccess(messageData) == true) {
            JSONArray modeObjectArr = messageData.getJSONArray("mode");
            String[] mode = new String[modeObjectArr.size()*2];
            for(int i=0;i<modeObjectArr.size();i++){ 
                RuleInfo rule = new RuleInfo();
                mode = JSONLibUtil.getStringArray(JSONArray.fromObject(modeObjectArr.get(i)));
                rule.setMode(mode);
                ruleList.add(rule);
            }
            
            
            JSONArray ipObjectArr = messageData.getJSONArray("ip");
            String[] ip = new String[ipObjectArr.size()*2];
            for(int i=0;i<ipObjectArr.size();i++){ 
                RuleInfo rule = ruleList.get(i+1);
                ip = JSONLibUtil.getStringArray(JSONArray.fromObject(ipObjectArr.get(i)));
                rule.setIpList(ip);
            }
            
            JSONArray portObjectArr = messageData.getJSONArray("port");
            BigInteger[] port = new BigInteger[portObjectArr.size()*2];
            for(int i=0;i<portObjectArr.size();i++){ 
                RuleInfo rule = ruleList.get(i+1);
                port = JSONLibUtil.getBigIntegerArray(JSONArray.fromObject(portObjectArr.get(i)));
                rule.setPortList(port);
            }
            RuleInfo rule = ruleList.get(0);
            rule.setPortList(port);
            rule.setIpList(ip);
            rule.setMode(mode);

            rule.success();
            rule.updateTime();
            rule.setMessage(message); 
        } else {
            ruleList.get(0).fail();
            ruleList.get(0).updateTime();
            ruleList.get(0).setMessage(message);
        }
        synchronized (ruleList.get(0)) {
            ruleList.get(0).notifyAll();
        }
    }
    
 // 创建vpc
 	@HttpGatewayMessageHandler(messageType = "network_create_result")
 	public void networkCreateResult(HttpGatewayAsyncChannel channel, JSONObject messageData) {
 		logger.debug("start to process network create result data.");

 		String sessionId = channel.getSessionId();
 		Integer regionId = channel.getRegion();
 		// 释放资源
 		channel.release();
 		// 获取数据
 		String name = messageData.getString("name");

 		// 获取对象
 		NetworkCreateInfoPool pool = NetworkPoolManager.singleton().getCreateInfoPool();
 		NetworkInfoExt network = pool.get(regionId, name);
 		// 对象不存在
 		if (network == null) {
 			network = new NetworkInfoExt();
 			network.setRegionId(regionId);
 			network.setSessionId(sessionId);
 			network.setName(name);

 			network.initAsyncStatus();
 			network.setMessage("");
 			network.updateTime();

 			pool.put(network);
 		}

 		// 更新
 		network.updateTime();
 		if (HttpGatewayResponseHelper.isSuccess(messageData) == false) {
 			String message = messageData.getString("message");

 			network.fail();
 			network.setMessage(message);
 		} else {
 			String uuid = messageData.getString("uuid");
 			String networkAddress = messageData.getString("network_address");
 			String message = messageData.getString("message");

 			network.success();
 			network.setMessage(message);
 			network.setUuid(uuid);
 			network.setNetworkAddress(networkAddress);
 		}
 		synchronized (network) {
 			network.notifyAll();
 		}
 	}

 	// 查询vpc
 	@HttpGatewayMessageHandler(messageType = "network_query_result")
 	public void networkQueryResult(HttpGatewayAsyncChannel channel, JSONObject messageData) {
 		logger.debug("start to process network query result data.");

 		String sessionId = channel.getSessionId();
 		Integer regionId = channel.getRegion();
 		// 释放资源
 		channel.release();

 		if (HttpGatewayResponseHelper.isSuccess(messageData) == true) {
 			// 获取对象
 			NetworkInfoPool pool = NetworkPoolManager.singleton().getInfoPool();

 			JSONArray networkList = messageData.getJSONArray("networks");
 			for (int index = 0; index < networkList.size(); index++) {
 				JSONObject network = networkList.getJSONObject(index);
 				String uuid = network.getString("uuid");
 				String name = network.getString("name");

 				NetworkInfoExt localNetwork = pool.get(uuid);
 				if (localNetwork == null) {
 					localNetwork = new NetworkInfoExt();
 					localNetwork.setRegionId(regionId);
 					localNetwork.setSessionId(sessionId);
 				}

 				localNetwork.setName(name);
 				localNetwork.setUuid(uuid);
 				localNetwork.updateTime();

 				pool.put(localNetwork);
 			}
 		}
 	}

 	// 修改vpc
 	@HttpGatewayMessageHandler(messageType = "network_modify_result")
 	public void networkModifyResult(HttpGatewayAsyncChannel channel, JSONObject messageData) {
 		logger.debug("start to process network modify result data.");

 		String sessionId = channel.getSessionId();
 		Integer regionId = channel.getRegion();
 		// 释放资源
 		channel.release();

 		String uuid = messageData.getString("uuid");
 		String message = HttpGatewayResponseHelper.getMessage(messageData);

 		NetworkInfoPool pool = NetworkPoolManager.singleton().getModifyPool();
 		NetworkInfoExt network = pool.get(uuid);
 		if (network == null) {
 			network = new NetworkInfoExt();
 			network.setRegionId(regionId);
 			network.setUuid(uuid);
 			network.setSessionId(sessionId);
 			network.updateTime();

 			pool.put(network);
 		}

 		if (HttpGatewayResponseHelper.isSuccess(messageData) == true) {
 			network.success();
 			network.setMessage(message);
 			network.updateTime();
 		} else {
 			network.fail();
 			network.setMessage(message);
 			network.updateTime();
 		}
 		synchronized (network) {
 			network.notifyAll();
 		}
 	}

 	// 查询vpc详情
 	@HttpGatewayMessageHandler(messageType = "network_detail_result")
 	public void networkDetailResult(HttpGatewayAsyncChannel channel, JSONObject messageData) {
 		logger.debug("start to process query network detail result data.");

 		String sessionId = channel.getSessionId();
 		Integer regionId = channel.getRegion();
 		// 释放资源
 		channel.release();

 		String uuid = messageData.getString("uuid");
 		String message = HttpGatewayResponseHelper.getMessage(messageData);

 		NetworkInfoPool pool = NetworkPoolManager.singleton().getInfoPool();
 		NetworkInfoExt network = pool.get(uuid);
 		if (network == null) {
 			network = new NetworkInfoExt();
 			network.setUuid(uuid);
 			network.setRegionId(regionId);
 			network.setSessionId(sessionId);
 			network.initAsyncStatus();
 			network.updateTime();

 			pool.put(network);
 		}

 		if (HttpGatewayResponseHelper.isSuccess(messageData) == true) {
 			String name = messageData.getString("name");
 			int size = messageData.getInt("size");
 			String networkAddress = messageData.getString("network_address");
 			int netmask = messageData.getInt("netmask");
 			String description = messageData.getString("description");
 			int status = messageData.getInt("network_status");
 			String[] ip = JSONLibUtil.getStringArray(messageData, "ip");

 			network.setName(name);
 			network.setSize(size);
 			network.setNetworkAddress(networkAddress);
 			network.setNetmask(netmask);
 			network.setDescription(description);
 			network.setStatus(status);
 			network.setIp(ip);

 			network.success();
 			network.updateTime();
 			network.setMessage(message);
 		} else {
 			network.fail();
 			network.updateTime();
 			network.setMessage(message);
 		}
 		synchronized (network) {
 			network.notifyAll();
 		}
 	}

 	// 启动vpc
 	@HttpGatewayMessageHandler(messageType = "start_network_result")
 	public void networkStartResult(HttpGatewayAsyncChannel channel, JSONObject messageData) {
 		logger.debug("start to process start network result data.");

 		String sessionId = channel.getSessionId();
 		Integer regionId = channel.getRegion();
 		// 释放资源
 		channel.release();

 		String uuid = messageData.getString("uuid");
 		String message = HttpGatewayResponseHelper.getMessage(messageData);

 		NetworkInfoPool pool = NetworkPoolManager.singleton().getStartPool();
 		NetworkInfoExt network = pool.get(uuid);
 		if (network == null) {
 			network = new NetworkInfoExt();
 			network.setRegionId(regionId);
 			network.setUuid(uuid);
 			network.setSessionId(sessionId);
 			network.updateTime();
 			network.setMessage("");
 			network.initAsyncStatus();

 			pool.put(network);
 		}

 		if (HttpGatewayResponseHelper.isSuccess(messageData) == true) {
 			network.success();
 			network.setMessage(message);
 			network.updateTime();
 		} else {
 			network.fail();
 			network.setMessage(message);
 			network.updateTime();
 		}
 		synchronized (network) {
 			network.notifyAll();
 		}
 	}

 	// 停止vpc
 	@HttpGatewayMessageHandler(messageType = "stop_network_result")
 	public void networkStopResult(HttpGatewayAsyncChannel channel, JSONObject messageData) {
 		logger.debug("start to process stop network result data.");

 		String sessionId = channel.getSessionId();
 		Integer regionId = channel.getRegion();
 		// 释放资源
 		channel.release();

 		String uuid = messageData.getString("uuid");
 		String message = HttpGatewayResponseHelper.getMessage(messageData);

 		NetworkInfoPool pool = NetworkPoolManager.singleton().getStopPool();
 		NetworkInfoExt network = pool.get(uuid);
 		if (network == null) {
 			network = new NetworkInfoExt();
 			network.setRegionId(regionId);
 			network.setUuid(uuid);
 			network.setSessionId(sessionId);
 			network.updateTime();
 			network.setMessage("");
 			network.initAsyncStatus();

 			pool.put(network);
 		}

 		if (HttpGatewayResponseHelper.isSuccess(messageData) == true) {
 			network.success();
 			network.setMessage(message);
 			network.updateTime();
 		} else {
 			network.fail();
 			network.setMessage(message);
 			network.updateTime();
 		}
 		synchronized (network) {
 			network.notifyAll();
 		}
 	}

 	// 删除vpc
 	@HttpGatewayMessageHandler(messageType = "delete_network_result")
 	public void networkDeleteResult(HttpGatewayAsyncChannel channel, JSONObject messageData) {
 		logger.debug("start to process delete network result data.");

 		String sessionId = channel.getSessionId();
 		Integer regionId = channel.getRegion();
 		// 释放资源
 		channel.release();

 		String uuid = messageData.getString("uuid");
 		String message = HttpGatewayResponseHelper.getMessage(messageData);

 		NetworkInfoPool pool = NetworkPoolManager.singleton().getDelPool();
 		NetworkInfoExt network = pool.get(uuid);
 		if (network == null) {
 			network = new NetworkInfoExt();
 			network.setRegionId(regionId);
 			network.setUuid(uuid);
 			network.setSessionId(sessionId);
 			network.updateTime();
 			network.setMessage("");
 			network.initAsyncStatus();

 			pool.put(network);
 		}

 		if (HttpGatewayResponseHelper.isSuccess(messageData) == true) {
 			network.success();
 			network.setMessage(message);
 			network.updateTime();
 		} else {
 			network.fail();
 			network.setMessage(message);
 			network.updateTime();
 		}
 		synchronized (network) {
 			network.notifyAll();
 		}
 	}

 	// 查询vpc关联云主机
 	@HttpGatewayMessageHandler(messageType = "query_network_host_result")
 	public void networkQueryHostResult(HttpGatewayAsyncChannel channel, JSONObject messageData) {
 		logger.debug("start to process query network host result data.");

 		String sessionId = channel.getSessionId();
 		Integer regionId = channel.getRegion();
 		// 释放资源
 		channel.release();

 		String uuid = messageData.getString("uuid");
 		String message = HttpGatewayResponseHelper.getMessage(messageData);

 		NetworkInfoPool pool = NetworkPoolManager.singleton().getQueryHostPool();
 		NetworkInfoExt network = pool.get(uuid);
 		if (network == null) {
 			network = new NetworkInfoExt();
 			network.setRegionId(regionId);
 			network.setUuid(uuid);
 			network.setSessionId(sessionId);
 			network.updateTime();
 			network.setMessage("");
 			network.initAsyncStatus();
 			network.setHostList(new Host[0]);

 			pool.put(network);
 		}

 		if (HttpGatewayResponseHelper.isSuccess(messageData) == true) {
 			JSONArray hostList = messageData.getJSONArray("hosts");
 			Host[] hostArray = new Host[hostList.size()];

 			for (int i = 0; i < hostList.size(); i++) {
 				JSONObject host = hostList.getJSONObject(i);
 				String hostUuid = host.getString("uuid");
 				String hostName = host.getString("name");
 				String hostNetwokAddress = host.getString("network_address");

 				Host attachHost = network.new Host();
 				attachHost.setName(hostName);
 				attachHost.setUuid(hostUuid);
 				attachHost.setNetworkAddress(hostNetwokAddress);
 				hostArray[i] = attachHost;
 			}

 			network.success();
 			network.setHostList(hostArray);
 			network.updateTime();
 			network.setMessage(message);
 		} else {
 			network.fail();
 			network.setMessage(message);
 			network.updateTime();
 		}
 		synchronized (network) {
 			network.notifyAll();
 		}
 	}

 	// vpc关联云主机
 	@HttpGatewayMessageHandler(messageType = "attach_host_result")
 	public void networkAttachHostResult(HttpGatewayAsyncChannel channel, JSONObject messageData) {
 		logger.debug("start to process attach network host result data.");

 		String sessionId = channel.getSessionId();
 		Integer regionId = channel.getRegion();
 		// 释放资源
 		channel.release();

 		String message = HttpGatewayResponseHelper.getMessage(messageData);
 		String uuid = messageData.getString("uuid");
 		String hostUuid = messageData.getString("host");

 		NetworkInfoSessionPool pool = NetworkPoolManager.singleton().getAttachHostPool();
 		NetworkInfoExt network = pool.get(sessionId, uuid);
 		if (network == null) {
 			network = new NetworkInfoExt();
 			network.setRegionId(regionId);
 			network.setUuid(uuid);
 			network.setSessionId(sessionId);
 			network.updateTime();
 			network.setMessage("");
 			network.initAsyncStatus();
 			network.setHostUuid(hostUuid);

 			pool.put(network);
 		}

 		if (HttpGatewayResponseHelper.isSuccess(messageData) == true) {
 			network.success();
 			network.updateTime();
 			network.setMessage(message);
 			network.setHostNetworkAddress(messageData.getString("network_address"));
 		} else {
 			network.fail();
 			network.updateTime();
 			network.setMessage(message);
 		}
 		synchronized (network) {
 			network.notifyAll();
 		}
 	}

 	// vpc移除云主机
 	@HttpGatewayMessageHandler(messageType = "detach_host_result")
 	public void networkDetachHostResult(HttpGatewayAsyncChannel channel, JSONObject messageData) {
 		logger.debug("start to process detach network host result data.");

 		String sessionId = channel.getSessionId();
 		Integer regionId = channel.getRegion();
 		// 释放资源
 		channel.release();

 		String message = HttpGatewayResponseHelper.getMessage(messageData);
 		String uuid = messageData.getString("uuid");
 		String hostUuid = messageData.getString("host");

 		NetworkInfoSessionPool pool = NetworkPoolManager.singleton().getDetachHostPool();
 		NetworkInfoExt network = pool.get(sessionId, uuid);
 		if (network == null) {
 			network = new NetworkInfoExt();
 			network.setRegionId(regionId);
 			network.setUuid(uuid);
 			network.setSessionId(sessionId);
 			network.updateTime();
 			network.setMessage("");
 			network.initAsyncStatus();
 			network.setHostUuid(hostUuid);

 			pool.put(network);
 		}

 		if (HttpGatewayResponseHelper.isSuccess(messageData) == true) {
 			network.success();
 			network.updateTime();
 			network.setMessage(message);
 		} else {
 			network.fail();
 			network.updateTime();
 			network.setMessage(message);
 		}
 		synchronized (network) {
 			network.notifyAll();
 		}
 	}

 	// vpc申请公网ip
 	@HttpGatewayMessageHandler(messageType = "attach_address_result")
 	public void networkAttachAddressResult(HttpGatewayAsyncChannel channel, JSONObject messageData) {
 		logger.debug("start to process attach network address result data.");

 		String sessionId = channel.getSessionId();
 		Integer regionId = channel.getRegion();
 		// 释放资源
 		channel.release();

 		String message = HttpGatewayResponseHelper.getMessage(messageData);
 		String uuid = messageData.getString("uuid");

 		NetworkInfoSessionPool pool = NetworkPoolManager.singleton().getAttachAddressPool();
 		NetworkInfoExt network = pool.get(sessionId, uuid);
 		if (network == null) {
 			network = new NetworkInfoExt();
 			network.setRegionId(regionId);
 			network.setUuid(uuid);
 			network.setSessionId(sessionId);
 			network.updateTime();
 			network.setMessage("");
 			network.initAsyncStatus();

 			pool.put(network);
 		}

 		if (HttpGatewayResponseHelper.isSuccess(messageData) == true) {
 			String[] ip = JSONLibUtil.getStringArray(messageData, "ip");
 			network.setIp(ip);

 			network.success();
 			network.updateTime();
 			network.setMessage(message);
 		} else {
 			network.fail();
 			network.updateTime();
 			network.setMessage(message);
 		}
 		synchronized (network) {
 			network.notifyAll();
 		}
 	}

 	// vpc移除公网ip
 	@HttpGatewayMessageHandler(messageType = "detach_address_result")
 	public void networkDetachAddressResult(HttpGatewayAsyncChannel channel, JSONObject messageData) {
 		logger.debug("start to process detach network address result data.");

 		String sessionId = channel.getSessionId();
 		Integer regionId = channel.getRegion();
 		// 释放资源
 		channel.release();

 		String message = HttpGatewayResponseHelper.getMessage(messageData);
 		String uuid = messageData.getString("uuid");

 		NetworkInfoSessionPool pool = NetworkPoolManager.singleton().getDetachAddressPool();
 		NetworkInfoExt network = pool.get(sessionId, uuid);
 		if (network == null) {
 			network = new NetworkInfoExt();
 			network.setRegionId(regionId);
 			network.setUuid(uuid);
 			network.setSessionId(sessionId);
 			network.updateTime();
 			network.setMessage("");
 			network.initAsyncStatus();

 			pool.put(network);
 		}

 		if (HttpGatewayResponseHelper.isSuccess(messageData) == true) {
 			String[] successIpList = JSONLibUtil.getStringArray(messageData, "success_ip_list");
 			String[] failIpList = JSONLibUtil.getStringArray(messageData, "fail_ip_list");
 			network.setSuccessIpList(successIpList);

 			network.success();
 			network.updateTime();
 			network.setMessage(message);
 		} else {
 			network.fail();
 			network.updateTime();
 			network.setMessage(message);
 		}
 		synchronized (network) {
 			network.notifyAll();
 		}
 	}

 	// vpc绑定端口
 	@HttpGatewayMessageHandler(messageType = "network_bind_port_result")
 	public void networkBindPortResult(HttpGatewayAsyncChannel channel, JSONObject messageData) {
 		logger.debug("start to process bind network port result data.");

 		String sessionId = channel.getSessionId();
 		Integer regionId = channel.getRegion();
 		// 释放资源
 		channel.release();

 		String message = HttpGatewayResponseHelper.getMessage(messageData);
 		String uuid = messageData.getString("uuid");

 		NetworkInfoSessionPool pool = NetworkPoolManager.singleton().getBindPortPool();
 		NetworkInfoExt network = pool.get(sessionId, uuid);
 		if (network == null) {
 			network = new NetworkInfoExt();
 			network.setRegionId(regionId);
 			network.setUuid(uuid);
 			network.setSessionId(sessionId);
 			network.updateTime();
 			network.setMessage("");
 			network.initAsyncStatus();

 			pool.put(network);
 		}

 		if (HttpGatewayResponseHelper.isSuccess(messageData) == true) {
 			JSONArray portInfoList = messageData.getJSONArray("port");
 			Port[] successPortList = new Port[portInfoList.size()];

 			for (int i = 0; i < portInfoList.size(); i++) {
 				JSONObject portInfo = portInfoList.getJSONObject(i);
 				String protocol = portInfo.getString("protocol");
 				String publicIp = portInfo.getString("public_ip");
 				String publicPort = portInfo.getString("public_port");
 				String hostUuid = portInfo.getString("host_uuid");
 				String hostPort = portInfo.getString("host_port");

 				Port port = network.new Port();
 				port.setProtocol(protocol);
 				port.setPublicIp(publicIp);
 				port.setPublicPort(publicPort);
 				port.setHostUuid(hostUuid);
 				port.setHostPort(hostPort);

 				successPortList[i] = port;
 			}

 			network.setSuccessPortList(successPortList);
 			network.success();
 			network.updateTime();
 			network.setMessage(message);
 		} else {
 			network.fail();
 			network.updateTime();
 			network.setMessage(message);
 		}
 		synchronized (network) {
 			network.notifyAll();
 		}
 	}

 	// vpc解除绑定端口
 	@HttpGatewayMessageHandler(messageType = "network_unbind_port_result")
 	public void networkUnbindPortResult(HttpGatewayAsyncChannel channel, JSONObject messageData) {
 		logger.debug("start to process unbind network port result data.");

 		String sessionId = channel.getSessionId();
 		Integer regionId = channel.getRegion();
 		// 释放资源
 		channel.release();

 		String message = HttpGatewayResponseHelper.getMessage(messageData);
 		String uuid = messageData.getString("uuid");

 		NetworkInfoSessionPool pool = NetworkPoolManager.singleton().getUnbindPortPool();
 		NetworkInfoExt network = pool.get(sessionId, uuid);
 		if (network == null) {
 			network = new NetworkInfoExt();
 			network.setRegionId(regionId);
 			network.setUuid(uuid);
 			network.setSessionId(sessionId);
 			network.updateTime();
 			network.setMessage("");
 			network.initAsyncStatus();

 			pool.put(network);
 		}

 		if (HttpGatewayResponseHelper.isSuccess(messageData) == true) {
 			JSONArray portInfoList = messageData.getJSONArray("port");
 			Port[] successPortList = new Port[portInfoList.size()];

 			for (int i = 0; i < portInfoList.size(); i++) {
 				JSONObject portInfo = portInfoList.getJSONObject(i);
 				String protocol = portInfo.getString("protocol");
 				String publicIp = portInfo.getString("public_ip");
 				String publicPort = portInfo.getString("public_port");
 				String hostUuid = portInfo.getString("host_uuid");
 				String hostPort = portInfo.getString("host_port");

 				Port port = network.new Port();
 				port.setProtocol(protocol);
 				port.setPublicIp(publicIp);
 				port.setPublicPort(publicPort);
 				port.setHostUuid(hostUuid);
 				port.setHostPort(hostPort);

 				successPortList[i] = port;
 			}

 			network.setSuccessPortList(successPortList);
 			network.success();
 			network.updateTime();
 			network.setMessage(message);
 		} else {
 			network.fail();
 			network.updateTime();
 			network.setMessage(message);
 		}
 		synchronized (network) {
 			network.notifyAll();
 		}
 	}
 	  
    /**
     * @Description:启动监控接口后的异步回调接收数据方法
     * @param channel
     * @param messageData
     */
    @HttpGatewayMessageHandler(messageType = "monitor_data")
    public void receiveMonitorData(HttpGatewayAsyncChannel channel, JSONObject messageData) {
        logger.debug("recieve monitor data.");
        //获取数据
        String sessionId = channel.getSessionId();

        int task = messageData.getInt("task");
        int level = messageData.getInt("level");
        try {
            MonitorConstant.saveUUIDAndTaskID(messageData);
            channel.monitorHeartbeat(task);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    @HttpGatewayMessageHandler(messageType = "create_compute_pool")
    public void createComputePool(HttpGatewayAsyncChannel channel, JSONObject messageData) {
        //获取数据
        String sessionId = channel.getSessionId();
        channel.release();

        if (HttpGatewayResponseHelper.isSuccess(messageData) == true) {
            System.err.println(String.format("[%s]create compute success, message '%s'", sessionId, messageData));
        } else {
            System.err.println(String.format("[%s]create compute fail, message '%s'", sessionId, messageData));

        }

    }

    @HttpGatewayMessageHandler(messageType = "modify_compute_pool")
    public void modifyComputePool(HttpGatewayAsyncChannel channel, JSONObject messageData) {
        //获取数据
        String sessionId = channel.getSessionId();

        // 获取对象
        ComputeInfoPool  pool = ComputeInfoPoolManager.singleton().getPool();
        ComputeInfoExt computeInfoExt = pool.get(sessionId);

        if (computeInfoExt == null) {
            computeInfoExt = new ComputeInfoExt();
            computeInfoExt.initAsyncStatus();
            pool.put(sessionId, computeInfoExt);
        }

        if (HttpGatewayResponseHelper.isSuccess(messageData) == true) {
            computeInfoExt.success();
        } else {
            computeInfoExt.fail();
        }
        channel.release();

    }

    @HttpGatewayMessageHandler(messageType = "query_compute_pool_detail")
    public void queryComputePoolDetail(HttpGatewayAsyncChannel channel, JSONObject messageData) {
        //获取数据
        String sessionId = channel.getSessionId();
        String uuid = messageData.getString("uuid");

        // 获取对象
        ComputeInfoPool  pool = ComputeInfoPoolManager.singleton().getPool();
        ComputeInfoExt computeInfoExt = pool.get(uuid);

        if (computeInfoExt == null) {
            computeInfoExt = new ComputeInfoExt();
            computeInfoExt.setUuid(uuid);
            computeInfoExt.initAsyncStatus();

            pool.put(uuid, computeInfoExt);
        }

        if (HttpGatewayResponseHelper.isSuccess(messageData) == true) {
            String name = messageData.getString("name");
            int networkType = messageData.getInt("network_type");
            String network = messageData.getString("network");
            int diskType = messageData.getInt("disk_type");
            String diskSource = messageData.getString("disk_source");
            JSONArray mode = messageData.getJSONArray("mode");
            String path = messageData.getString("path");
            String crypt = messageData.getString("crypt");
            
            Integer[] modeArr = new Integer[mode.size()];
            for (int i = 0; i < mode.size(); i++) {
                modeArr[i] = (Integer) mode.get(i);
            }

            computeInfoExt.setName(name);
            computeInfoExt.setNetworkType(networkType);
            computeInfoExt.setNetwork(network);
            computeInfoExt.setDiskType(diskType);
            computeInfoExt.setDiskSource(diskSource);
            computeInfoExt.setMode(modeArr);
            computeInfoExt.setMode0(modeArr[0]);
            computeInfoExt.setMode1(modeArr[1]);
            computeInfoExt.setMode2(modeArr[2]);
            computeInfoExt.setMode3(modeArr[3]);
            computeInfoExt.setPath(path);
            computeInfoExt.setCrypt(crypt);
            computeInfoExt.success();

            System.err.println(String.format(
                "[%s]query compute pool detail success, uuid '%s', name '%s', network_type '%d', network '%s', disk_type '%d', disk_source '%s', mode '%s', path '%s', crypt '%s'",
                sessionId, uuid, name, networkType, network, diskType, diskSource, mode, path, crypt));
        } else {
            String message = messageData.getString("message");
            computeInfoExt.fail();
            computeInfoExt.setMessage(message);
            System.err.println(String.format("[%s]query compute pool detail fail, uuid '%s', message '%s'", sessionId, uuid, HttpGatewayResponseHelper.getMessage(messageData)));

        }
    }



    @HttpGatewayMessageHandler(messageType = "reset_host")
    public void resetHost(HttpGatewayAsyncChannel channel, JSONObject messageData) {
        //补充完整代码后，请删除控制台输出代码。
        logger.debug("recieve reset host response.");
        //获取数据
        String sessionId = channel.getSessionId();
        channel.release();
        
        String uuid = messageData.getString("uuid");
        int status = 1;          
        if (HttpGatewayResponseHelper.isSuccess(messageData) == true) {
            status = 2; //表示成功了
         }  
        CloudHostData myCloudHostData = CloudHostPoolManager.getCloudHostPool().getByRealHostId(uuid);
        CloudHostData newCloudHostData = myCloudHostData.clone();
        newCloudHostData.setLastOperStatus(status);
        CloudHostPoolManager.getCloudHostPool().put(newCloudHostData);
    }
    
	@HttpGatewayMessageHandler(messageType = "query_storage_device")
	public void queryStorageDevice(HttpGatewayAsyncChannel channel, JSONObject messageData) {
        //获取数据
		String sessionId = channel.getSessionId();
		channel.release();
		StorageManager.singleton().put(messageData);		
		logger.info(String.format("[%s]query storage device response, data '%s'", sessionId, messageData));
    }

	@HttpGatewayMessageHandler(messageType = "add_storage_device")
	public void addStorageDevice(HttpGatewayAsyncChannel channel, JSONObject messageData) {
		StorageManager.singleton().setAddResult(messageData);
		String sessionId = channel.getSessionId();
		channel.release();
		logger.info(String.format("[%s]add storage device response, data '%s'", sessionId, messageData));		
	}

	@HttpGatewayMessageHandler(messageType = "remove_storage_device")
	public void removeStorageDevice(HttpGatewayAsyncChannel channel, JSONObject messageData) {
		StorageManager.singleton().setRemoveResult(messageData);
		String sessionId = channel.getSessionId();
		channel.release();
		logger.info(String.format("[%s]remove storage device response, data '%s'", sessionId, messageData));
    }

	@HttpGatewayMessageHandler(messageType = "enable_storage_device")
	public void enableStorageDevice(HttpGatewayAsyncChannel channel, JSONObject messageData) {
		StorageManager.singleton().setEnableResult(messageData);
        //获取数据
		String sessionId = channel.getSessionId();
		channel.release();
		logger.info(String.format("[%s]enable storage device response, data '%s'", sessionId, messageData));
    }

	@HttpGatewayMessageHandler(messageType = "disable_storage_device")
	public void disableStorageDevice(HttpGatewayAsyncChannel channel, JSONObject messageData) {
		StorageManager.singleton().setDisableResult(messageData);
        //获取数据
		String sessionId = channel.getSessionId();
		channel.release();
		logger.info(String.format("[%s]disable storage device response, data '%s'", sessionId, messageData));
    }   
 
	/**
	 * 
	* @Title: migrateHostAck 
	* @Description: 迁移是否开始
	* @param @param channel
	* @param @param messageData      
	* @return void     
	* @throws
	 */
	@HttpGatewayMessageHandler(messageType = "migrate_host_ack")
    public void migrateHostAck(HttpGatewayAsyncChannel channel, JSONObject messageData) {
         
        String sessionId = channel.getSessionId();
        String hostId = AppInconstant.cloudHostMigreateSessionId.get(sessionId);
        if (HttpGatewayResponseHelper.isSuccess(messageData) == true) {
            logger.info("host "+hostId+" migrate begin ");
        }else{
            if(hostId != null){
                logger.info("host "+hostId+" migrate fail ");
                AppInconstant.cloudHostMigrate.remove(hostId);
                AppInconstant.cloudHostMigreateSessionId.remove(sessionId);
            }
        }
        channel.release();
     }  
	/**
	 * 
	* @Title: migrateHostReport 
	* @Description: 迁移进度 
	* @param @param channel
	* @param @param messageData      
	* @return void     
	* @throws
	 */
	@HttpGatewayMessageHandler(messageType = "migrate_host_report")
    public void migrateHostReport(HttpGatewayAsyncChannel channel, JSONObject messageData) {
         String sessionId = channel.getSessionId();
         String hostId = AppInconstant.cloudHostMigreateSessionId.get(sessionId); 
         AppInconstant.cloudHostMigrate.put(hostId, messageData.getInt("level"));
         logger.info("host "+hostId+" migrate progress "+ messageData.getInt("level"));
         channel.release();
     }

	/**
	 * 
	* @Title: migrateHostResult 
	* @Description: 迁移结果 
	* @param @param channel
	* @param @param messageData      
	* @return void     
	* @throws
	 */
    @HttpGatewayMessageHandler(messageType = "migrate_host_result")
    public void migrateHostResult(HttpGatewayAsyncChannel channel, JSONObject messageData) {
        logger.debug("recieve backup host progress response.");
        
        //获取数据
        String sessionId = channel.getSessionId();
        String hostId = AppInconstant.cloudHostMigreateSessionId.get(sessionId); 
        AppInconstant.cloudHostMigrate.remove(hostId);
        AppInconstant.cloudHostMigreateSessionId.remove(sessionId);
        if (HttpGatewayResponseHelper.isSuccess(messageData) == true) {
            logger.info("host "+hostId+" migrate success ");
            //成功代码
            AppInconstant.cloudHostMigrate.put(hostId, 101);
        }else{
            if(hostId != null){
                logger.info("host "+hostId+" migrate fail ");
                //失败代码
                AppInconstant.cloudHostMigrate.put(hostId, 102);
            }
        }
        channel.release();
    }  
	
  	@HttpGatewayMessageHandler(messageType = "enable_service")
	public void serviceEnable(HttpGatewayAsyncChannel channel, JSONObject messageData) {
		String status = messageData.getString("status");
		String message = messageData.getString("message");
		AppInconstant.serviceEnableResult.put("status", status);
		AppInconstant.serviceEnableResult.put("message", message);
		//获取数据
		String sessionId = channel.getSessionId();
		channel.release();
		logger.info(String.format("[%s]enable storage device response, data '%s'", sessionId, messageData));
	}
	
	@HttpGatewayMessageHandler(messageType = "disable_service")
	public void serviceDisable(HttpGatewayAsyncChannel channel, JSONObject messageData) {
		String status = messageData.getString("status");
		String message = messageData.getString("message");
		AppInconstant.serviceDisableResult.put("status", status);
		AppInconstant.serviceDisableResult.put("message", message);
		//获取数据
		String sessionId = channel.getSessionId();
		channel.release();
		logger.info(String.format("[%s]disable storage device response, data '%s'", sessionId, messageData));
	}
	

@HttpGatewayMessageHandler(messageType = "query_snapshot")
	public void querySnapshot(HttpGatewayAsyncChannel channel, JSONObject messageData) {
        //获取数据快照查询
		String sessionId = channel.getSessionId();
		SnapshotManager.singleton().putSnapShot(sessionId,messageData);
		channel.release();
		logger.info(String.format("[%s]query snapshot report response, data '%s'", sessionId, messageData));
    }   

	
	@HttpGatewayMessageHandler(messageType = "create_snapshot_report")
	public void createSnapshopReport(HttpGatewayAsyncChannel channel, JSONObject messageData) {
        //创建快照进度
		String sessionId = channel.getSessionId();
		SnapshotManager.singleton().updatePressLevelSnapShot(sessionId,messageData);
		channel.release();
		logger.info(String.format("[%s]create snapshot report response, data '%s'", sessionId, messageData));
    }   
	
	@HttpGatewayMessageHandler(messageType = "create_snapshot")
	public void createSnapshop(HttpGatewayAsyncChannel channel, JSONObject messageData) {
        //创建快照进度
		String sessionId = channel.getSessionId();
		SnapshotManager.singleton().finishCommandSnapShot(sessionId,messageData);
		channel.release();
		logger.info(String.format("[%s]create snapshot response, data '%s'", sessionId, messageData));
    }  
	
	@HttpGatewayMessageHandler(messageType = "delete_snapshot")
	public void deleteSnapshot(HttpGatewayAsyncChannel channel, JSONObject messageData) {
        //创建快照进度
		String sessionId = channel.getSessionId();
		SnapshotManager.singleton().finishCommandSnapShot(sessionId,messageData);
		channel.release();
		logger.info(String.format("[%s]delete snapshot response, data '%s'", sessionId, messageData));
    } 
	
	@HttpGatewayMessageHandler(messageType = "resume_snapshot_report")
	public void resumeSnapshotReport(HttpGatewayAsyncChannel channel, JSONObject messageData) {
        //创建快照进度
		String sessionId = channel.getSessionId();
		SnapshotManager.singleton().updatePressLevelSnapShot(sessionId,messageData);
		channel.release();
		logger.info(String.format("[%s]resume snapshot report response, data '%s'", sessionId, messageData));
    }   
	
	@HttpGatewayMessageHandler(messageType = "resume_snapshot")
	public void resumeSnapshot(HttpGatewayAsyncChannel channel, JSONObject messageData) {
        //创建快照进度
		String sessionId = channel.getSessionId();
		SnapshotManager.singleton().finishCommandSnapShot(sessionId,messageData);
		channel.release();
		logger.info(String.format("[%s]resume snapshot response, data '%s'", sessionId, messageData));
    }  	
    @HttpGatewayMessageHandler(messageType = "modify_service")
    public void modifyService(HttpGatewayAsyncChannel channel, JSONObject messageData) {
        //获取数据
        String sessionId = channel.getSessionId();
        String target = messageData.getString("target");

        // 获取对象
        ServiceInfoPool pool = ServiceInfoPoolManager.singleton().getPool();
        ServiceInfoExt serviceInfoExt = pool.get(target);

        if (serviceInfoExt == null) {
            serviceInfoExt = new ServiceInfoExt();
            serviceInfoExt.initAsyncStatus();
            pool.put(serviceInfoExt);
        }

        if (HttpGatewayResponseHelper.isSuccess(messageData) == true) {
            serviceInfoExt.success();
        } else {
            serviceInfoExt.fail();
        }
        channel.release();
        logger.info(String.format("[%s]modify service response, data '%s'", sessionId, messageData));
    }   
}
