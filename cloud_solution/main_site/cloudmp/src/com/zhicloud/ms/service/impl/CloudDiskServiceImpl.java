package com.zhicloud.ms.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.List;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.ms.constant.AppConstant;
import com.zhicloud.ms.httpGateway.HttpGatewayChannelExt;
import com.zhicloud.ms.httpGateway.HttpGatewayManager;
import com.zhicloud.ms.httpGateway.HttpGatewayResponseHelper;
import com.zhicloud.ms.mapper.CloudDiskMapper;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.ICloudDiskService;
import com.zhicloud.ms.transform.util.TransFormLoginHelper;
import com.zhicloud.ms.transform.util.TransFormLoginInfo;
import com.zhicloud.ms.util.CapacityUtil;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.CloudDisk;


@Service("cloudDiskService")
@Transactional(readOnly=true)
public class CloudDiskServiceImpl implements ICloudDiskService{
	private static final Logger logger = Logger.getLogger(CloudDiskServiceImpl.class);
	public static final Integer Region_LOCAL  = 0;
	@Resource
	private SqlSession sqlSession;
	@Override
	public List<CloudDisk> getAllCloudDisk(CloudDisk parameter) {
		// TODO Auto-generated method stub
		CloudDiskMapper mapper= this.sqlSession.getMapper(CloudDiskMapper.class);
		List<CloudDisk>  list = mapper.selectAll();
		return list;
	}

    @Override
    @Transactional(readOnly=false)
	public MethodResult addCloudDisk(CloudDisk disk, HttpServletRequest request) {
        try{ 
        	
    		if(StringUtil.isBlank(disk.getDiskPoolId())){
    			return new MethodResult(MethodResult.FAIL,"未指定存储资源池！");
    		}
        	
        	TransFormLoginInfo loginInfo = TransFormLoginHelper.getLoginInfo(request);
            CloudDiskMapper mapper= this.sqlSession.getMapper(CloudDiskMapper.class);
            
            // 添加创建的硬盘进数据库
            disk.setId(StringUtil.generateUUID());
            disk.setAccount(loginInfo.getUsercount());
            disk.setPassword(loginInfo.getUsercount());
            disk.setRunningStatus(AppConstant.CLOUD_DISK_RUNNING_STATUS_RUNNING);
            disk.setStatus(AppConstant.CLOUD_DISK_STATUS_NORMAL);
            disk.setCreateTime(StringUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm"));
            if (disk.getDiskdiy()!=null){
            	disk.setDisk(new BigDecimal(disk.getDiskdiy()));
            }
    		BigInteger realDisk  = CapacityUtil.fromCapacityLabel(disk.getDisk()+"GB");
    		disk.setDisk(new BigDecimal(realDisk));
    		disk.setMonthlyPrice(new BigDecimal("0"));
            disk.setRegion(Region_LOCAL);
    		
            
            // 发消息到http gateway
            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
			JSONObject result = channel.deviceCreate(disk.getId(),disk.getDiskPoolId(), realDisk, 4096, 0, new Integer[] { 0, 0, 0, 0, 0, 0 }, disk.getType(), disk.getAccount(), disk.getPassword(), "", "");
			if (HttpGatewayResponseHelper.isSuccess(result) == false) {
				logger.warn("CloudDiskServiceImpl.addCloudDisk() > [" + Thread.currentThread().getId() + "] create disk '" + disk.getName()+"["+disk.getUserId()+"]" + "' failed, message:[" + HttpGatewayResponseHelper.getMessage(result) + "]");
				return new MethodResult(MethodResult.FAIL, "创建失败：http gateway返回失败！");
			}
			
			disk.setRealDiskId(result.getString("uuid"));
			mapper.insert(disk);
            return new MethodResult(MethodResult.SUCCESS, "添加成功");
        }catch(Exception e){
            e.printStackTrace();
            return new MethodResult(MethodResult.FAIL, "创建失败：调用http gateway失败！");
        }
	}

	@Override
	public MethodResult delCloudDisk(String id) {
        CloudDiskMapper mapper= this.sqlSession.getMapper(CloudDiskMapper.class);
        CloudDisk disk  =  mapper.selectByPrimaryKey(id);
        if (disk.getRealDiskId()==null){
        	disk.setRunningStatus(AppConstant.CLOUD_DISK_RUNNING_STATUS_SHUTDOWN);
			disk.setStatus(AppConstant.CLOUD_DISK_STATUS_HALT_FOREVER);
			mapper.updateByPrimaryKey(disk);
        	return new MethodResult(MethodResult.SUCCESS, "删除成功");
        }
        // 发消息到http gateway
        HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);   
        try {
			JSONObject result = channel.deviceDelete(disk.getRealDiskId());
			if (HttpGatewayResponseHelper.isSuccess(result) == false) {
				logger.warn("CloudDiskServiceImpl.delDisk() > [" + Thread.currentThread().getId() + "] delete disk '" + disk.getName()+"["+disk.getUserId()+"]" + "' failed, message:[" + HttpGatewayResponseHelper.getMessage(result) + "]");
				return new MethodResult(MethodResult.FAIL, "删除失败：http gateway返回失败！");
			}
			disk.setRunningStatus(AppConstant.CLOUD_DISK_RUNNING_STATUS_SHUTDOWN);
			disk.setStatus(AppConstant.CLOUD_DISK_STATUS_HALT_FOREVER);
			mapper.updateByPrimaryKey(disk);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new MethodResult(MethodResult.FAIL, "删除失败：http gateway返回失败！");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return new MethodResult(MethodResult.FAIL, "删除失败：http gateway返回失败！");			
		}
		// TODO Auto-generated method stub
		return new MethodResult(MethodResult.SUCCESS, "删除成功");
	}

	@Override
	public MethodResult changeDiskStatus(String id, Integer status,
			HttpServletRequest request) {
		// TODO Auto-generated method stub        
		CloudDiskMapper mapper= this.sqlSession.getMapper(CloudDiskMapper.class);
        CloudDisk disk  =  mapper.selectByPrimaryKey(id);
        String retMsg = "";
        if (AppConstant.CLOUD_DISK_STATUS_NORMAL.equals(status)){
        	disk.setStatus(AppConstant.CLOUD_DISK_STATUS_HALT);  
        	retMsg = "停用成功";
        }else{
        	disk.setStatus(AppConstant.CLOUD_DISK_STATUS_NORMAL);  
        	retMsg = "启用成功";      	
        }
        mapper.updateByPrimaryKey(disk);
        return new MethodResult(MethodResult.SUCCESS, retMsg);
	}

	@Override
	public CloudDisk selectCloudDiskById(String id) {
		CloudDiskMapper mapper= this.sqlSession.getMapper(CloudDiskMapper.class);
		return  mapper.selectByPrimaryKey(id);
	}

	@Override
	public MethodResult updateCloudDisk(CloudDisk disk,	HttpServletRequest request) {
        try{ 
            CloudDiskMapper mapper= this.sqlSession.getMapper(CloudDiskMapper.class);
            
            CloudDisk o = mapper.selectByPrimaryKey(disk.getId());
            o.setType(disk.getType());
            o.setName(disk.getName());
           
            // 发消息到http gateway
            HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
			JSONObject result = channel.deviceModify(o.getRealDiskId(), o.getName(),  new Integer[] { 0, 0, 0 }, o.getType(), o.getAccount(), o.getPassword(), "");
			if (HttpGatewayResponseHelper.isSuccess(result) == false) {
				logger.warn("CloudDiskServiceImpl.addCloudDisk() > [" + Thread.currentThread().getId() + "] create disk '" + disk.getName()+"["+disk.getUserId()+"]" + "' failed, message:[" + HttpGatewayResponseHelper.getMessage(result) + "]");
				return new MethodResult(MethodResult.FAIL, "调整硬盘参数失败：http gateway返回失败");
			}
			mapper.updateByPrimaryKey(o);
            return new MethodResult(MethodResult.SUCCESS, "修改成功");
        }catch(Exception e){
            e.printStackTrace();
            return new MethodResult(MethodResult.FAIL, "调整硬盘参数失败：调用http gateway失败");
        }
	}
	
    

}
