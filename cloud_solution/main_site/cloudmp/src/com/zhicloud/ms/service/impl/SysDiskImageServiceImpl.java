/**
 * Project Name:CloudDeskTopMS
 * File Name:SysDiskImageServiceImpl.java
 * Package Name:com.zhicloud.ms.service.impl
 * Date:2015年3月17日下午7:23:21
 * 
 *
*/ 

package com.zhicloud.ms.service.impl;

import com.zhicloud.ms.app.pool.diskImagePool.DiskImageProgressData;
import com.zhicloud.ms.app.pool.diskImagePool.DiskImageProgressPool;
import com.zhicloud.ms.app.pool.diskImagePool.DiskImageProgressPoolManager;
import com.zhicloud.ms.constant.AppConstant;
import com.zhicloud.ms.constant.AppInconstant;
import com.zhicloud.ms.exception.MyException;
import com.zhicloud.ms.httpGateway.HttpGatewayAsyncChannel;
import com.zhicloud.ms.httpGateway.HttpGatewayChannelExt;
import com.zhicloud.ms.httpGateway.HttpGatewayManager;
import com.zhicloud.ms.httpGateway.HttpGatewayResponseHelper;
import com.zhicloud.ms.mapper.CloudHostMapper;
import com.zhicloud.ms.mapper.SysDiskImageMapper;
import com.zhicloud.ms.remote.MethodResult;
import com.zhicloud.ms.service.ISysDiskImageService;
import com.zhicloud.ms.util.DateUtil;
import com.zhicloud.ms.util.RegionHelper;
import com.zhicloud.ms.util.RegionHelper.RegionData;
import com.zhicloud.ms.util.StringUtil;
import com.zhicloud.ms.util.json.JSONLibUtil;
import com.zhicloud.ms.vo.CloudHostVO;
import com.zhicloud.ms.vo.SysDiskImageVO;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.util.*;
  
/**
 * ClassName: SysDiskImageServiceImpl 
 * Function: 镜像操作.
 * date: 2015年3月17日 下午7:23:21 
 *
 * @author sasa
 * @version 
 * @since JDK 1.7
 */ 

@Transactional(readOnly=true)
public class SysDiskImageServiceImpl implements ISysDiskImageService {
	private static final Logger logger = Logger.getLogger(UserServiceImpl.class); 
	private SqlSession sqlSession;
	
	

	public SqlSession getSqlSession() {
		return sqlSession;
	}

	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}

	/**
	 * 查询所有的镜像.
	 * @see com.zhicloud.ms.service.ISysDiskImageService#queryAllSysDiskImage()
	 */
	@Override
	public List<SysDiskImageVO> queryAllSysDiskImage() {		
		SysDiskImageMapper sysDiskImageMapper = this.sqlSession.getMapper(SysDiskImageMapper.class);
		List<SysDiskImageVO> sysDiskImageList = sysDiskImageMapper.getAll();
        return sysDiskImageList;
	}

	/**
	 * 新增镜像
	 * @throws IOException 
	 * @throws MalformedURLException 
	 * @see com.zhicloud.ms.service.ISysDiskImageService#addSysDiskImage(com.zhicloud.ms.vo.SysDiskImageVO)
	 */
	@Override
	@Transactional(readOnly=false)
	public MethodResult addSysDiskImage(SysDiskImageVO image) {
		
		CloudHostMapper cloudHostMapper       = this.sqlSession.getMapper(CloudHostMapper.class);
		Map<String,Object> hostdata = new HashMap<String,Object>();
		hostdata.put("id", image.getFromHostId());
		
		CloudHostVO cloudHostVO = cloudHostMapper.getCloudHostById(hostdata);
		//异步回调-创建磁盘镜像
		String sessionId = null;
		try {
			sessionId = this.createDiskImage(cloudHostVO.getRegion(), image.getName(), cloudHostVO.getRealHostId(), image.getDescription(), new String[]{"", ""}, "T2", "administrator");
		} catch (IOException e) { 
			return new MethodResult(MethodResult.FAIL, "http-gateway操作失败");
		}
		if( sessionId == null )
		{
			logger.warn("SysDiskImageServiceImpl.addSysDiskImage() > ["+Thread.currentThread().getId()+"] create disk image failed");
			return new MethodResult(MethodResult.FAIL, "新增失败");
		}
		SysDiskImageMapper sysDiskImageMapper = this.sqlSession.getMapper(SysDiskImageMapper.class);
 		Map<String,Object> data = new HashMap<String,Object>();
		data.put("id", StringUtil.generateUUID());
		data.put("name", image.getName()); 
		data.put("displayName", image.getDisplayName()); 
		data.put("fromHostId", image.getFromHostId()); 
		data.put("type", 1); 
		data.put("description", image.getDescription()); 
		data.put("imageType", image.getImageType());
		data.put("createTime", DateUtil.dateToString(new Date(),"yyyyMMddHHmmssSSS"));
		int i = sysDiskImageMapper.addSysDiskImage(data);
		if(i > 0){
			return new MethodResult(MethodResult.SUCCESS, "新增成功");			
		}else{
			return new MethodResult(MethodResult.FAIL, "新增失败");
		}
	}

	/**
	 * 通过镜像名获取镜像
	 * @see com.zhicloud.ms.service.ISysDiskImageService#getSysDiskImageByName(java.lang.String)
	 */
	@Override
	public SysDiskImageVO getSysDiskImageByName(String name) {
		SysDiskImageMapper sysDiskImageMapper = this.sqlSession.getMapper(SysDiskImageMapper.class);
 		Map<String,Object> data = new HashMap<String,Object>();
 		data.put("name", name);   
 		return sysDiskImageMapper.getSysDiskImageByName(data);
	}
	
	
	public String createDiskImage(Integer regionId, String name, String realHostId, String description, String[] identity, String group, String user) throws MalformedURLException, IOException {
		HttpGatewayAsyncChannel channel = null;
		try {
			//获得channel
			channel = HttpGatewayManager.getAsyncChannel(regionId);
			//发送create disk image请求
			JSONObject result = channel.diskImageCreateAsync(name, realHostId, description, identity, group, user);
			if (HttpGatewayResponseHelper.isSuccess(result)==false) {//失败
				channel.release();
				return null;
			}
			//成功
			DiskImageProgressData diskImage = new DiskImageProgressData();
			diskImage.setName(name);
			diskImage.setRegion(regionId);
			diskImage.setFromHostId(realHostId);
			diskImage.setDescription(description);
			diskImage.setTag(identity[0] + "," + identity[1]);
			diskImage.setGroupId(group);
			diskImage.setUserId(user);
			String sessionId = channel.getSessionId();
			diskImage.setSessionId(sessionId);
			diskImage.updateTime();
			
			DiskImageProgressPool pool = DiskImageProgressPoolManager.singleton().getPool();
			pool.put(diskImage);			
			
			return sessionId;
		} catch(IOException e) {
			if (channel != null) {
				channel.release();
			}
			throw e;
		}
	}
	
	/**
	 * 获取磁盘镜像创建进度。创建完成30分钟后，将不再能获取到。
	 * 
	 * @param sessionId
	 * @param name
	 * @return
	 */
	public DiskImageProgressData getDiskImageProgressData(String sessionId, String name) {
		DiskImageProgressPool pool = DiskImageProgressPoolManager.singleton().getPool();
		
		return pool.get(sessionId, name).clone();
	}
	
	/**
	 * 获取镜像创建的信息
	 */ 
	public MethodResult getSysDiskImageCreationResult(String id) {
		try {
			if(StringUtil.isBlank(id)){
				
				throw new MyException("参数id为空");
			}
			SysDiskImageMapper sysDiskImageMapper = this.sqlSession.getMapper(SysDiskImageMapper.class);			 
			SysDiskImageVO sysDiskIamgeVO = sysDiskImageMapper.getById(id);
			if (sysDiskIamgeVO == null) {
				throw new MyException("找不到镜像信息"); 
			}
			// 如果真实镜像ID已经存在，那么表明创建成功
			if(!StringUtil.isBlank(sysDiskIamgeVO.getRealImageId())){				
				MethodResult result = new MethodResult(MethodResult.SUCCESS, "");
				result.put("progress", 100);
				result.put("creation_status", true); 
				return result;
			}
			 
			Map<String, Object> temporaryData = (Map<String, Object>) AppInconstant.sysDiskImageProgress.get(id);
			if(temporaryData==null){
				throw new MyException("无法获取创建信息"); 
			}
			DiskImageProgressData data = this.getDiskImageProgressData(temporaryData.get("sessionId").toString(), temporaryData.get("name").toString());
			if(data==null){
				throw new MyException("无法获取创建信息"); 				
			}else{
				
				MethodResult result = new MethodResult(MethodResult.SUCCESS, "");
				result.put("progress", data.getProgress());
				result.put("creation_status", false);
				logger.info("get sys_disk_image progress "+data);
				return result;
			} 
		} catch (MyException e) {
			throw e;
		} catch (Exception e) {
			throw new MyException(e);
		}
	}

	/**
	 * 根据镜像名删除镜像
	 * @see com.zhicloud.ms.service.ISysDiskImageService#deleteSysDiskImage(java.lang.String)
	 */
	@Override
	@Transactional(readOnly=false)
	public MethodResult deleteSysDiskImage(String id) {
		SysDiskImageMapper sysDiskImageMapper = this.sqlSession.getMapper(SysDiskImageMapper.class);			 
		SysDiskImageVO sysDiskIamgeVO = sysDiskImageMapper.getById(id);
		if(sysDiskIamgeVO == null ){
			return new MethodResult(MethodResult.FAIL, "未找到镜像信息");
		}
		if(!StringUtil.isBlank(sysDiskIamgeVO.getRealImageId())){			
			// delete from http gateway
			try {
				HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(sysDiskIamgeVO.getRegion());
				JSONObject diskImageDeleteResult;
				diskImageDeleteResult = channel.diskImageDelete(sysDiskIamgeVO.getRealImageId());
				if( HttpGatewayResponseHelper.isSuccess(diskImageDeleteResult) )
				{
					logger.info("SysDiskImageServiceImpl.deleteSysDiskImage() >   delete disk image succeeded, uuid:["+sysDiskIamgeVO.getRealImageId()+"], message:["+HttpGatewayResponseHelper.getMessage(diskImageDeleteResult)+"]");
					String [] ids ={id}; 
					int i = sysDiskImageMapper.deleteSysDiskImageByIds(ids);
					if(i > 0){
						return new MethodResult(MethodResult.SUCCESS, "删除成功");
					}
				}
				else
				{
					logger.warn("SysDiskImageServiceImpl.deleteSysDiskImage() >   delete disk image failed, uuid:["+sysDiskIamgeVO.getRealImageId()+"], message:["+HttpGatewayResponseHelper.getMessage(diskImageDeleteResult)+"]");
					return new MethodResult(MethodResult.FAIL, "删除失败");
				}
			} catch (IOException e) {
				logger.error(e);
				return new MethodResult(MethodResult.FAIL, "删除镜像信息失败");
				
			}
		}else{
			String [] ids ={id}; 
			int i = sysDiskImageMapper.deleteSysDiskImageByIds(ids);
			if(i > 0){
				return new MethodResult(MethodResult.SUCCESS, "删除成功");
			}
		}
		return new MethodResult(MethodResult.FAIL, "删除失败");
		
		
		 
	}

	/**
	 * 根据镜像ID获取Id
	 * @see com.zhicloud.ms.service.ISysDiskImageService#getSysDiskImageById(java.lang.String)
	 */
	@Override
	public SysDiskImageVO getSysDiskImageById(String id) {
		SysDiskImageMapper sysDiskImageMapper = this.sqlSession.getMapper(SysDiskImageMapper.class);			 
		SysDiskImageVO sysDiskIamgeVO = sysDiskImageMapper.getById(id);
		return sysDiskIamgeVO;
	}

	/**
	 * 更新镜像信息
	 * @see com.zhicloud.ms.service.ISysDiskImageService#updateSysDiskImage(java.lang.String, com.zhicloud.ms.vo.SysDiskImageVO)
	 */
	@Override
	@Transactional(readOnly=false)
	public MethodResult updateSysDiskImage(String id, SysDiskImageVO image) {
		SysDiskImageMapper sysDiskImageMapper = this.sqlSession.getMapper(SysDiskImageMapper.class);			 
		SysDiskImageVO sysDiskIamgeVO = sysDiskImageMapper.getById(id);
		if(sysDiskIamgeVO == null ){
			return new MethodResult(MethodResult.FAIL, "未找到镜像信息");
		}
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("id", id);
		data.put("name", sysDiskIamgeVO.getName()); 
		data.put("displayName", image.getDisplayName()); 
		data.put("fromHostId", sysDiskIamgeVO.getFromHostId()); 
		data.put("type", sysDiskIamgeVO.getType()); 
		data.put("description", image.getDescription()); 
		data.put("modifiedTime", DateUtil.dateToString(new Date(),"yyyyMMddHHmmssSSS"));
		int i = sysDiskImageMapper.updateSysDiskImageById(data);
		if(i > 0){
			return new MethodResult(MethodResult.SUCCESS, "更新成功");			
		}else{
			return new MethodResult(MethodResult.FAIL, "更新失败");
		}
		
	}

	/**
	 * 从http-gateway获取镜像信息
	 * @see com.zhicloud.ms.service.ISysDiskImageService#initSysDiskImageFromHttpGateway()
	 */
	@Override
	@Transactional(readOnly=false)
	public boolean initSysDiskImageFromHttpGateway() {
		logger.debug("SysDiskImageServiceImpl.initSysDiskImageFromHttpGateway");
		RegionData[] regionDatas = RegionHelper.singleton.getAllResions();
		for( RegionData regionData : regionDatas )
		{
			try
			{
				logger.info("SysDiskImageServiceImpl.initSysDiskImageFromHttpGateway-->get info  from region:["+String.format("%s:%s", regionData.getId(), regionData.getName())+"]");
				HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(regionData.getId());
				
				JSONObject diskImageResult = channel.diskImageQuery();
				
				if( HttpGatewayResponseHelper.isSuccess(diskImageResult)==false )
				{
					logger.warn("SysDiskImageServiceImpl.initSysDiskImageFromHttpGateway() >   init disk images from http gateway failed, message:["+HttpGatewayResponseHelper.getMessage(diskImageResult)+"], region:["+String.format("%s:%s", regionData.getId(), regionData.getName())+"]");
				}

				SysDiskImageMapper sysDiskImageMapper = this.sqlSession.getMapper(SysDiskImageMapper.class);
 				
				JSONArray diskImages = (JSONArray)diskImageResult.get("disk_images");
				if( diskImages==null )
				{
					logger.error("SysDiskImageServiceImpl.initSysDiskImageFromHttpGateway() >   get sys disk images failed, diskImages is null, region:["+String.format("%s:%s", regionData.getId(), regionData.getName()) +"]");
					continue;
				}
				for( int i=0; i<diskImages.size(); i++ )
				{
					JSONObject diskImage = (JSONObject)diskImages.get(i);
					
					String uuid        = JSONLibUtil.getString(diskImage, "uuid");
					String name        = JSONLibUtil.getString(diskImage, "name");
					String description = JSONLibUtil.getString(diskImage, "description"); 
					String[] identity  = JSONLibUtil.getStringArray(diskImage, "identity");
					
					// 
					SysDiskImageVO diskImageVO = sysDiskImageMapper.getByRealImageId(uuid);
					// if there is a disk image with same real_image_id in DB, update it.
					if( diskImageVO!=null )
					{
						//镜像已经存在，不用更新
						logger.info("SysDiskImageServiceImpl.initSysDiskImageFromHttpGateway() > image "+diskImageVO.getName()+" already exsit ");
 					}
					else
					{
						Map<String, Object> condition = new LinkedHashMap<String, Object>();
						condition.put("region", regionData.getId());
						condition.put("name",   name);
						diskImageVO = sysDiskImageMapper.getSysDiskImageByName(condition);
						// if there is a disk image with same real_image_name in DB, update it.
						if( diskImageVO!=null )
						{
							updateUnrelatedSysDiskImageFromRealDataByRegionAndName(regionData.getId(), diskImage, diskImageVO);
						}
						// if not disk image with same real_image_id in DB, add into DB
						else
						{
 							Map<String, Object> sysDiskImageData =  new LinkedHashMap<String, Object>();
							sysDiskImageData.put("id",            StringUtil.generateUUID());
							sysDiskImageData.put("realImageId",   uuid);
							sysDiskImageData.put("fromHostId",    null);
							sysDiskImageData.put("name",          name);
							sysDiskImageData.put("displayName",          name);
 							sysDiskImageData.put("description",   description );
 							sysDiskImageData.put("userId",        "");
 							// 默认为通用镜像
							sysDiskImageData.put("type",        0);
							sysDiskImageData.put("imageType",        AppConstant.DISK_IMAGE_TYPE_COMMON);
							sysDiskImageData.put("createTime", DateUtil.dateToString(new Date(),"yyyyMMddHHmmssSSS"));
							
							sysDiskImageMapper.addSysDiskImage(sysDiskImageData);
						}
					}
				}
				logger.info("SysDiskImageServiceImpl.initSysDiskImageFromHttpGateway() >    同步系统磁盘镜像完成, region:["+String.format("%s:%s", regionData.getId(), regionData.getName())+"]");
			}
			catch( SocketException e )
			{
				logger.error("SysDiskImageServiceImpl.initSysDiskImageFromHttpGateway() >    connect to http gateway failed, exception:["+e.getMessage()+"], region:["+String.format("%s:%s", regionData.getId(), regionData.getName()) +"]");
			}
			catch (Throwable e)
			{
				throw new  MyException(e);
			}
		}
		logger.info("SysDiskImageServiceImpl.initSysDiskImageFromHttpGateway-->finish get disk image info");
		return true;
	}
	@Transactional(readOnly=false)
	private void updateUnrelatedSysDiskImageFromRealDataByRegionAndName(int region, JSONObject diskImage, SysDiskImageVO diskImageVO)
	{
		String uuid        = JSONLibUtil.getString(diskImage,      "uuid");
		String name        = JSONLibUtil.getString(diskImage,      "name");
		String description = JSONLibUtil.getString(diskImage,      "description"); 
		String[] identity  = JSONLibUtil.getStringArray(diskImage, "identity");
		
		//更新系统镜像表
		Map<String, Object> sysDiskImageData = new LinkedHashMap<String, Object>();
		sysDiskImageData.put("realImageId", uuid);
		sysDiskImageData.put("tag",         StringUtil.joinWrap(identity, ",", "", StringUtil.JOIN_WRAP_PREPROCESS_FLAG_URLENCODE));
		sysDiskImageData.put("description", description);
		sysDiskImageData.put("region",      region);
		sysDiskImageData.put("name",        name);

		SysDiskImageMapper sysDiskImageMapper = this.sqlSession.getMapper(SysDiskImageMapper.class);
		sysDiskImageMapper.updateUnrelatedSysDiskImageByName(sysDiskImageData);
	}
	
	/**
	 * 根据镜像类型查询镜像
	 */
	@Override
	public List<SysDiskImageVO> querySysDiskImageByImageType(Integer imageType) {		
		SysDiskImageMapper sysDiskImageMapper = this.sqlSession.getMapper(SysDiskImageMapper.class);
		List<SysDiskImageVO> sysDiskImageList = sysDiskImageMapper.querySysDiskImageByImageType(imageType);
        return sysDiskImageList; 
	}

    /**
     * 根据根据多个参数获取镜像
     * @author  张翔
     * @param condition
     * @return
     */
    @Override
    public List<SysDiskImageVO> getSysDiskImageByMultiParams(Map<String, Object> condition) {
        return this.sqlSession.getMapper(SysDiskImageMapper.class).querySysDiskImageByMultiParams(condition);
    }

    @Override
	@Transactional(readOnly=false)
	public MethodResult updateImageType(String[] ids, Integer imageType) {
		SysDiskImageMapper sysDiskImageMapper = this.sqlSession.getMapper(SysDiskImageMapper.class);
		Map<String, Object> sysDiskImageData = new LinkedHashMap<String, Object>();
		sysDiskImageData.put("imageType", imageType);
		for(int i=0;i<ids.length;i++){
			sysDiskImageData.put("id", ids[i]);
			sysDiskImageMapper.updateImageTypeById(sysDiskImageData);
		}

		return new MethodResult(MethodResult.SUCCESS, "更新成功");	
		
	} 

}

