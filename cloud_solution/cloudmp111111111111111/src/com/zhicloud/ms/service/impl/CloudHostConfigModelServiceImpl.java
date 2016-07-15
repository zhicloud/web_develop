package com.zhicloud.ms.service.impl;

import com.zhicloud.ms.mapper.CloudHostConfigModelMapper;
import com.zhicloud.ms.mapper.CloudHostWarehouseMapper;
import com.zhicloud.ms.mapper.SysDiskImageMapper;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.CloudHostConfigModelService;
import com.zhicloud.ms.util.CapacityUtil;
import com.zhicloud.ms.util.DateUtil;
import com.zhicloud.ms.util.FlowUtil;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.vo.CloudHostConfigModel;
import com.zhicloud.ms.vo.CloudHostWarehouse;
import com.zhicloud.ms.vo.SysDiskImageVO;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
		if(chcm.getSysDiskType().equals("from_empty")){
            condition.put("sysDisk", CapacityUtil.fromCapacityLabel(chcm.getEmptyDisk()+"GB"));
        }else{
            condition.put("sysDisk", CapacityUtil.fromCapacityLabel("10GB"));
        }
        condition.put("dataDisk", CapacityUtil.fromCapacityLabel(dataDisk+"GB"));
        condition.put("bandwidth", FlowUtil.fromFlowLabel(bandwidth+"Mbps"));
        if(chcm.getSysDiskType().equals("from_empty")){
          condition.put("sysImageName", "空白镜像");              
        }else{
          condition.put("sysImageId", chcm.getSysImageId());
          condition.put("sysImageName", sysDiskImage.getName()); 
          if(sysDiskImage.getSize()!=null && sysDiskImage.getSize().compareTo(BigInteger.ZERO)>0){
              condition.put("sysDisk", sysDiskImage.getSize());

          }
        }
		condition.put("createTime", DateUtil.dateToString(new Date(),"yyyyMMddHHmmssSSS"));
		condition.put("type", 1);
		condition.put("supportH264",chcm.getSupportH264());
		condition.put("codeRate",chcm.getCodeRate());
		condition.put("frameRate",chcm.getFrameRate());
		condition.put("operationSystem",chcm.getOperationSystem());
		
		chcmMapper.addType(condition);
		return id;
	}

	@Override
	@Transactional(readOnly=false)
	public MethodResult deleteType(String id) {
      logger.debug("CloudHostConfigModelServiceImpl.deleteType()");
      CloudHostConfigModelMapper chcmMapper = this.sqlSession.getMapper(CloudHostConfigModelMapper.class);
      CloudHostWarehouseMapper cloudHostWarehouseMapper = this.sqlSession.getMapper(CloudHostWarehouseMapper.class);

      int su1 = 0; //  完全删除成功
      int su2 = 0; //  镜像类型不能删除
      int su3 = 0; //  删除失败
      String [] typeIds = id.split(",");
      for(String typeId : typeIds){

        List<CloudHostWarehouse> modelList = cloudHostWarehouseMapper.getByConfigModelId(typeId);
        if(modelList != null && modelList.size() > 0){
            su2 ++ ;
        }else{
            int re = chcmMapper.deleteType(typeId);
            if(re > 0){
                su1 ++ ;
            }else{
                su3 ++ ;
            }
        }
		}


      String [] imageIds = id.split(",");
      for(String imageId : imageIds){

      }

      String flag = MethodResult.FAIL;
      String message = "";


      if (su1 > 0) {
          flag = MethodResult.SUCCESS;
          message = "删除类型成功";
      } else {
          message = "删除类型失败";
      }

      if (su2 > 0) {
          flag = MethodResult.FAIL;
          message += ", 部分类型已经创建仓库, 需先删除仓库";
      }
      if (su3 > 0) {
          flag = MethodResult.FAIL;
          message += ", 部分类型删除失败";
      }

      return new MethodResult(flag, message);
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
  		if(chcm.getSysDiskType().equals("from_empty")){
            condition.put("sysDisk", CapacityUtil.fromCapacityLabel(chcm.getEmptyDisk()+"GB"));
       }else{
           condition.put("sysDisk", CapacityUtil.fromCapacityLabel("10GB"));
       }
       condition.put("dataDisk", CapacityUtil.fromCapacityLabel(dataDisk+"GB"));
       condition.put("bandwidth", FlowUtil.fromFlowLabel(bandwidth+"Mbps"));
       if(chcm.getSysDiskType().equals("from_empty")){
         condition.put("sysImageName", "空白镜像");              
       }else{
         condition.put("sysImageId", chcm.getSysImageId());
         condition.put("sysImageName", sysDiskImage.getName());            
       }
		condition.put("modifiedTime", DateUtil.dateToString(new Date(),"yyyyMMddHHmmssSSS"));
	    condition.put("supportH264",chcm.getSupportH264());
	    condition.put("codeRate",chcm.getCodeRate());
	    condition.put("frameRate",chcm.getFrameRate());
	    condition.put("operationSystem",chcm.getOperationSystem());

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
				sysDisk = sysDiskImage.getSize()+"";
			}else{
				sysImageId = null;
//				sysDisk = chcm.getEmptyDisk()+"GB";
				sysDisk = CapacityUtil.fromCapacityLabel(chcm.getEmptyDisk()+"GB")+"";
			}
			String dataDisk = chcm.getDataDisk()==null ? chcm.getDiskdiy().toString() : chcm.getDataDisk().toString();
			String bandwidth = chcm.getBandwidth()==null ? chcm.getBandwidthdiy().toString() : chcm.getBandwidth().toString();
			Map<String,Object> condition = new HashMap<String,Object>();
			id = StringUtil.generateUUID();
			condition.put("id", id);
			condition.put("name", chcm.getName());
			condition.put("cpuCore", new Integer(chcm.getCpuCore()));
			condition.put("memory", CapacityUtil.fromCapacityLabel(chcm.getMemory()+"GB"));
//			condition.put("sysDisk", sysDisk==null?null:CapacityUtil.fromCapacityLabel(sysDisk));
            condition.put("sysDisk", sysDisk);
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
                sysDisk = sysDiskImage.getSize()+"";
            }else{
                sysImageId = null;
//              sysDisk = chcm.getEmptyDisk()+"GB";
                sysDisk = CapacityUtil.fromCapacityLabel(chcm.getEmptyDisk()+"GB")+"";
            }
			String dataDisk = chcm.getDataDisk()==null ? chcm.getDiskdiy().toString() : chcm.getDataDisk().toString();
			String bandwidth = chcm.getBandwidth()==null ? chcm.getBandwidthdiy().toString() : chcm.getBandwidth().toString();
			Map<String,Object> condition = new HashMap<String,Object>();
			condition.put("id", id);
			condition.put("name", chcm.getName());
			condition.put("cpuCore", new Integer(chcm.getCpuCore()));
			condition.put("memory", CapacityUtil.fromCapacityLabel(chcm.getMemory()+"GB"));
			condition.put("sysDisk", sysDisk);
			condition.put("dataDisk", CapacityUtil.fromCapacityLabel(dataDisk+"GB"));
			condition.put("bandwidth", FlowUtil.fromFlowLabel(bandwidth+"Mbps"));
			condition.put("sysImageId", sysImageId);
			condition.put("sysImageName", sysDiskImage==null?null:sysDiskImage.getDisplayName());
			condition.put("modifiedTime", DateUtil.dateToString(new Date(),"yyyyMMddHHmmssSSS"));
			condition.put("supportH264",chcm.getSupportH264());
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
