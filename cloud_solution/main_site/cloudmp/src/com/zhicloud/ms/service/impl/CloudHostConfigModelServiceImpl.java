package com.zhicloud.ms.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.ms.mapper.CloudHostConfigModelMapper;
import com.zhicloud.ms.mapper.SysDiskImageMapper;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.CloudHostConfigModelService;
import com.zhicloud.ms.util.CapacityUtil;
import com.zhicloud.ms.util.DateUtil;
import com.zhicloud.ms.util.FlowUtil;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.CloudHostConfigModel;
import com.zhicloud.ms.vo.SysDiskImageVO;

@Service("cloudHostConfigModelService")
@Transactional(readOnly=true)
public class CloudHostConfigModelServiceImpl implements CloudHostConfigModelService {
	private static final Logger logger = Logger.getLogger(UserServiceImpl.class);
	@Resource
	private SqlSession sqlSession;
	
	/* (non-Javadoc)
	 * @see com.zhicloud.ms.service.CloudHostConfigModelService#getAll()
	 * 查询全部云主机类型
	 */
	@Override
	public List<CloudHostConfigModel> getAll() {
		logger.debug("CloudHostConfigModelServiceImpl.getAll()");
		CloudHostConfigModelMapper chcmMapper = this.sqlSession.getMapper(CloudHostConfigModelMapper.class);
		List<CloudHostConfigModel> chcmList = chcmMapper.getAll();
		return chcmList;
	}

	@Override
	@Transactional(readOnly=false)
	public String addType(CloudHostConfigModel chcm) {
		logger.debug("CloudHostConfigModelServiceImpl.addType()");
		CloudHostConfigModelMapper chcmMapper = this.sqlSession.getMapper(CloudHostConfigModelMapper.class);
		SysDiskImageMapper sdiMapper = this.sqlSession.getMapper(SysDiskImageMapper.class);
		SysDiskImageVO sysDiskImage = sdiMapper.getById(chcm.getSysImageId());
		String allocation = null;
		String cpuCore = null;
		String memory = null;
		String dataDisk = null;
		String bandwidth = null;
		if("notdiy".equals(chcm.getIsdiy())){
			allocation = chcm.getAllocationType();
			cpuCore = allocation.split("-")[0];
			memory = allocation.split("-")[1];
			dataDisk = allocation.split("-")[2];
			bandwidth = allocation.split("-")[3];
		}else{
			cpuCore = chcm.getCpuCore().toString();
			memory = chcm.getMemory().toString();
			dataDisk = chcm.getDataDisk()==null?chcm.getDiskdiy().toString():chcm.getDataDisk().toString();
			bandwidth = chcm.getBandwidth()==null?chcm.getBandwidthdiy().toString():chcm.getBandwidth().toString();
		}
		Map<String,Object> condition = new HashMap<String,Object>();
		String id = StringUtil.generateUUID();
		condition.put("id", id);
		condition.put("name", chcm.getName());
		condition.put("cpuCore", new Integer(cpuCore));
		condition.put("memory", CapacityUtil.fromCapacityLabel(memory+"GB"));
		condition.put("sysDisk", CapacityUtil.fromCapacityLabel("10GB"));
		condition.put("dataDisk", CapacityUtil.fromCapacityLabel(dataDisk+"GB"));
		condition.put("bandwidth", FlowUtil.fromFlowLabel(bandwidth+"Mbps"));
		condition.put("sysImageId", chcm.getSysImageId());
		condition.put("sysImageName", sysDiskImage.getName());
		condition.put("createTime", DateUtil.dateToString(new Date(),"yyyyMMddHHmmssSSS"));
		condition.put("type", 1);
		
		chcmMapper.addType(condition);
		return id;
	}

	@Override
	@Transactional(readOnly=false)
	public void deleteType(String id) {
		logger.debug("CloudHostConfigModelServiceImpl.deleteType()");
		CloudHostConfigModelMapper chcmMapper = this.sqlSession.getMapper(CloudHostConfigModelMapper.class);
		String [] typeIds = id.split(",");
		for(String typeId : typeIds){			
			chcmMapper.deleteType(typeId);
		}
	}

	@Override
	public CloudHostConfigModel getById(String id) {
		logger.debug("CloudHostConfigModelServiceImpl.getById()");
		CloudHostConfigModelMapper chcmMapper = this.sqlSession.getMapper(CloudHostConfigModelMapper.class);
		CloudHostConfigModel chcm = chcmMapper.getById(id);
		return chcm;
	}

	@Override
	@Transactional(readOnly=false)
	public int updateType(String id, CloudHostConfigModel chcm) {
		logger.debug("CloudHostConfigModelServiceImpl.updateType()");
		CloudHostConfigModelMapper chcmMapper = this.sqlSession.getMapper(CloudHostConfigModelMapper.class);
		SysDiskImageMapper sdiMapper = this.sqlSession.getMapper(SysDiskImageMapper.class);
		SysDiskImageVO sysDiskImage = sdiMapper.getById(chcm.getSysImageId());
		String allocation = null;
		String cpuCore = null;
		String memory = null;
		String dataDisk = null;
		String bandwidth = null;
		if("notdiy".equals(chcm.getIsdiy())){
			allocation = chcm.getAllocationType();
			cpuCore = allocation.split("-")[0];
			memory = allocation.split("-")[1];
			dataDisk = allocation.split("-")[2];
			bandwidth = allocation.split("-")[3];
		}else{
			cpuCore = chcm.getCpuCore().toString();
			memory = chcm.getMemory().toString();
			dataDisk = chcm.getDataDisk()==null?chcm.getDiskdiy().toString():chcm.getDataDisk().toString();
			bandwidth = chcm.getBandwidth()==null?chcm.getBandwidthdiy().toString():chcm.getBandwidth().toString();
		}
		Map<String,Object> condition = new HashMap<String,Object>();
		condition.put("id", id);
		condition.put("name", chcm.getName());
		condition.put("cpuCore", new Integer(cpuCore));
		condition.put("memory", CapacityUtil.fromCapacityLabel(memory+"GB"));
		condition.put("dataDisk", CapacityUtil.fromCapacityLabel(dataDisk+"GB"));
		condition.put("bandwidth", FlowUtil.fromFlowLabel(bandwidth+"Mbps"));
		condition.put("sysImageId", chcm.getSysImageId());
		condition.put("sysImageName", sysDiskImage.getName());
		condition.put("modifiedTime", DateUtil.dateToString(new Date(),"yyyyMMddHHmmssSSS"));
		int n = chcmMapper.updateById(condition);
		return n;
	}

	/**
	 * 根据镜像ID查询主机类型
	 * @see com.zhicloud.ms.service.CloudHostConfigModelService#getCloudHostConfigModelServiceByImageId(java.lang.String)
	 */
	@Override
	public List<CloudHostConfigModel> getCloudHostConfigModelServiceByImageId(
			String imageId) {
		
		CloudHostConfigModelMapper chcmMapper = this.sqlSession.getMapper(CloudHostConfigModelMapper.class);
		return chcmMapper.getCloudHostConfigModelServiceByImageId(imageId);
 	}

	/**
	 * 根据名称获取主机类型
	 * @see com.zhicloud.ms.service.CloudHostConfigModelService#getCloudHostConfigModelByName(java.lang.String)
	 */
	@Override
	public CloudHostConfigModel getCloudHostConfigModelByName(String name) {
		CloudHostConfigModelMapper chcmMapper = this.sqlSession.getMapper(CloudHostConfigModelMapper.class);
		return chcmMapper.getCloudHostConfigModelByName(name);
	}
	
	@Override
	public List<CloudHostConfigModel> getAllServer() {
		logger.debug("CloudHostConfigModelServiceImpl.getAll()");
		CloudHostConfigModelMapper chcmMapper = this.sqlSession.getMapper(CloudHostConfigModelMapper.class);
		List<CloudHostConfigModel> chcmList = chcmMapper.getAllServer();
		return chcmList;
	}
	
	@Override
	@Transactional(readOnly=false)
	public MethodResult addServerType(CloudHostConfigModel chcm) {
		logger.debug("CloudHostConfigModelServiceImpl.addServerType()");
		String id;
		try {
			CloudHostConfigModelMapper chcmMapper = this.sqlSession.getMapper(CloudHostConfigModelMapper.class);
			SysDiskImageMapper sdiMapper = this.sqlSession.getMapper(SysDiskImageMapper.class);
			SysDiskImageVO sysDiskImage = null;
			String sysDisk = "";
			String sysImageId = "";
			if("from_sys_image".equals(chcm.getSysDiskType())){
				sysImageId = chcm.getSysImageId();
				sysDiskImage = sdiMapper.getById(chcm.getSysImageId());
				sysDisk = null;
			}else{
				sysImageId = null;
				sysDisk = chcm.getEmptyDisk()+"GB";
			}
			String dataDisk = chcm.getDataDisk()==null ? chcm.getDiskdiy().toString() : chcm.getDataDisk().toString();
			String bandwidth = chcm.getBandwidth()==null ? chcm.getBandwidthdiy().toString() : chcm.getBandwidth().toString();
			Map<String,Object> condition = new HashMap<String,Object>();
			id = StringUtil.generateUUID();
			condition.put("id", id);
			condition.put("name", chcm.getName());
			condition.put("cpuCore", new Integer(chcm.getCpuCore()));
			condition.put("memory", CapacityUtil.fromCapacityLabel(chcm.getMemory()+"GB"));
			condition.put("sysDisk", sysDisk==null?null:CapacityUtil.fromCapacityLabel(sysDisk));
			condition.put("dataDisk", CapacityUtil.fromCapacityLabel(dataDisk+"GB"));
			condition.put("bandwidth", FlowUtil.fromFlowLabel(bandwidth+"Mbps"));
			condition.put("sysImageId", sysImageId);
			condition.put("sysImageName", sysDiskImage==null?null:sysDiskImage.getDisplayName());
			condition.put("createTime", DateUtil.dateToString(new Date(),"yyyyMMddHHmmssSSS"));
			condition.put("type",2);
			int n = chcmMapper.addType(condition);
			if(n > 0){
				return new MethodResult(MethodResult.SUCCESS,"添加成功");
			}
			return new MethodResult(MethodResult.FAIL,"添加失败");
		} catch (Exception e) {
			e.printStackTrace();
			return new MethodResult(MethodResult.FAIL,"添加失败");
		}
		
	}
	
	@Override
	@Transactional(readOnly=false)
	public MethodResult updateServerType(String id,CloudHostConfigModel chcm) {
		logger.debug("CloudHostConfigModelServiceImpl.updateServerType()");
		try {
			CloudHostConfigModelMapper chcmMapper = this.sqlSession.getMapper(CloudHostConfigModelMapper.class);
			SysDiskImageMapper sdiMapper = this.sqlSession.getMapper(SysDiskImageMapper.class);
			SysDiskImageVO sysDiskImage = null;
			String sysDisk = "";
			String sysImageId = "";
			if("from_sys_image".equals(chcm.getSysDiskType())){
				sysImageId = chcm.getSysImageId();
				sysDiskImage = sdiMapper.getById(chcm.getSysImageId());
				sysDisk = null;
			}else{
				sysImageId = null;
				sysDisk = chcm.getEmptyDisk()+"GB";
			}
			String dataDisk = chcm.getDataDisk()==null ? chcm.getDiskdiy().toString() : chcm.getDataDisk().toString();
			String bandwidth = chcm.getBandwidth()==null ? chcm.getBandwidthdiy().toString() : chcm.getBandwidth().toString();
			Map<String,Object> condition = new HashMap<String,Object>();
			condition.put("id", id);
			condition.put("name", chcm.getName());
			condition.put("cpuCore", new Integer(chcm.getCpuCore()));
			condition.put("memory", CapacityUtil.fromCapacityLabel(chcm.getMemory()+"GB"));
			condition.put("sysDisk", sysDisk==null?null:CapacityUtil.fromCapacityLabel(sysDisk));
			condition.put("dataDisk", CapacityUtil.fromCapacityLabel(dataDisk+"GB"));
			condition.put("bandwidth", FlowUtil.fromFlowLabel(bandwidth+"Mbps"));
			condition.put("sysImageId", sysImageId);
			condition.put("sysImageName", sysDiskImage==null?null:sysDiskImage.getDisplayName());
			condition.put("modifiedTime", DateUtil.dateToString(new Date(),"yyyyMMddHHmmssSSS"));
			int n = chcmMapper.updateById(condition);
			if(n > 0){
				return new MethodResult(MethodResult.SUCCESS,"修改成功");
			}
			return new MethodResult(MethodResult.FAIL,"修改失败");
		} catch (Exception e) {
			e.printStackTrace();
			return new MethodResult(MethodResult.FAIL,"修改失败");
		}
		
	}

}
