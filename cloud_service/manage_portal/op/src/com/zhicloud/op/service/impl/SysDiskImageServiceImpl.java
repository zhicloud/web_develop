package com.zhicloud.op.service.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.op.app.authorization.PrivilegeConstant;
import com.zhicloud.op.app.helper.LoginHelper;
import com.zhicloud.op.app.helper.RegionHelper;
import com.zhicloud.op.app.helper.RegionHelper.RegionData;
import com.zhicloud.op.app.helper.ServiceHelper;
import com.zhicloud.op.app.pool.diskImagePool.DiskImageProgressData;
import com.zhicloud.op.app.pool.diskImagePool.DiskImageProgressPool;
import com.zhicloud.op.app.pool.diskImagePool.DiskImageProgressPoolManager;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.common.util.json.JSONLibUtil;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.httpGateway.HttpGatewayAsyncChannel;
import com.zhicloud.op.httpGateway.HttpGatewayChannelExt;
import com.zhicloud.op.httpGateway.HttpGatewayManager;
import com.zhicloud.op.httpGateway.HttpGatewayResponseHelper;
import com.zhicloud.op.login.LoginInfo;
import com.zhicloud.op.mybatis.mapper.CloudHostMapper;
import com.zhicloud.op.mybatis.mapper.SysDiskImageMapper;
import com.zhicloud.op.mybatis.mapper.SysUserImageRelationMapper;
import com.zhicloud.op.mybatis.mapper.SysUserMapper;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.CallWithoutLogin;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.request.RequestContext;
import com.zhicloud.op.service.SysDiskImageService;
import com.zhicloud.op.service.constant.AppInconstant;
import com.zhicloud.op.vo.CloudHostVO;
import com.zhicloud.op.vo.SysDiskImageVO;
import com.zhicloud.op.vo.SysUserVO;


@Transactional(readOnly = true)
public class SysDiskImageServiceImpl extends BeanDirectCallableDefaultImpl implements SysDiskImageService 
{
	public static final Logger logger = Logger.getLogger(SysDiskImageServiceImpl.class);

	private SqlSession sqlSession;

	public void setSqlSession(SqlSession sqlSession)
	{
		this.sqlSession = sqlSession;
	}
	
	
	@Callable
	public String managePage(HttpServletRequest request,HttpServletResponse response) 
	{
		logger.debug("SysDiskImageServiceImpl.managePage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.hasPrivilege(PrivilegeConstant.sys_disk_image_manage_page)==false )
		{
			return "/public/have_not_access.jsp";
		}
		
		return "/security/operator/sys_disk_image_manage.jsp";
	}

	@Callable
	public String addPage(HttpServletRequest request,HttpServletResponse response) 
	{
		logger.debug("SysDiskImageServiceImpl.addPage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.hasPrivilege(PrivilegeConstant.sys_disk_image_manage_add) == false )
		{
			return "/public/have_not_access_dialog.jsp";
		}
		
		CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
		List<CloudHostVO> cloudHostList = cloudHostMapper.queryOperatorSelfUseCloudHost(new LinkedHashMap<String, Object>());
		request.setAttribute("cloudHostList", cloudHostList);
		
		return "/security/operator/sys_disk_image_add.jsp";
	}

	
	@Callable
	public String modPage(HttpServletRequest request,HttpServletResponse response)
	{
		logger.debug("SysDiskImageServiceImpl.modPage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.hasPrivilege(PrivilegeConstant.sys_disk_image_manage_modify)==false )
		{
			return "/public/have_not_access_dialog.jsp";
		}
		
		// 参数处理
		String sysDiskImageId = StringUtil.trim(request.getParameter("sysDiskImagetId"));
		if( StringUtil.isBlank(sysDiskImageId) )
		{
			throw new AppException("sysDiskImageId不能为空");
		}
		
		SysDiskImageMapper sysDiskImageMapper = this.sqlSession.getMapper(SysDiskImageMapper.class);
		CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
		
		// 获取“来自云主机”列表
		List<CloudHostVO> cloudHostList = cloudHostMapper.queryOperatorSelfUseCloudHost(new LinkedHashMap<String, Object>());
		request.setAttribute("cloudHostList", cloudHostList);
		
		// 获取要修改的镜像数据
		SysDiskImageVO sysDiskImage = sysDiskImageMapper.getById(sysDiskImageId);
		if( sysDiskImage != null )
		{
			request.setAttribute("sysDiskImage", sysDiskImage);
			if(sysDiskImage.getType()!=null&&sysDiskImage.getType()==2){				
				SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
				List<SysUserVO> userList = sysUserMapper.queryUserInImageId(sysDiskImageId);
				request.setAttribute("userList", userList);
			}
			return "/security/operator/sys_disk_image_mod.jsp";
		}
		else
		{
			request.setAttribute("message", "找不到系统镜像的记录");
			return "/public/warning_dialog.jsp";
		}
	}

	@Callable
	public void querySysDiskImage(HttpServletRequest request,HttpServletResponse response)
	{
		logger.debug("SysDiskImageServiceImpl.querySysDiskImage()");
		try
		{
			// 权限判断
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			if( loginInfo.hasPrivilege(PrivilegeConstant.sys_disk_image_manage_page)==false )
			{
				throw new AppException("您没有权限进行此操作");
			}
			
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");

			// 获取参数
			String sysDiskImageName = StringUtil.trim(request.getParameter("sysDiskImage_name"));
			String region = StringUtil.trim(request.getParameter("region"));
			if(StringUtil.isBlank(region)){
				region = null;
			}
			Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
			Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 2);

			// 查询数据库
			SysDiskImageMapper sysDiskImageMapper = this.sqlSession.getMapper(SysDiskImageMapper.class);
			
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("sysDiskImageName", "%" + sysDiskImageName + "%");
			condition.put("region", region);
			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			int total = sysDiskImageMapper.queryPageCount(condition); // 总行数
			List<SysDiskImageVO> sysDiskImageList = sysDiskImageMapper.queryPage(condition);// 分页结果

			// 输出json数据 
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, sysDiskImageList);
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("查询失败");
		}

	}

	@Callable
	@Transactional(readOnly = false)
	public MethodResult addSysDiskImage(Map<String, Object> parameter)
	{
		logger.debug("SysDiskImageServiceImpl.addSysDiskImage()");
		try
		{
			//权限判断
			HttpServletRequest request = RequestContext.getHttpRequest();
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			
			if( loginInfo.hasPrivilege(PrivilegeConstant.sys_disk_image_manage_add) == false )
			{
				return new MethodResult(MethodResult.FAIL, "您没有权限进行此操作");
			}
			
			//参数处理
			String name        = StringUtil.trim(parameter.get("name"));
			String fromHostId  = StringUtil.trim(parameter.get("from_host_id"));
			String tag1        = StringUtil.trim(parameter.get("tag1"));
			String tag2        = StringUtil.trim(parameter.get("tag2"));
			String description = StringUtil.trim(parameter.get("description"));
			String displayName = StringUtil.trim(parameter.get("display_name"));
			String type = StringUtil.trim(parameter.get("type"));
		    Integer sort       = StringUtil.parseInteger((String)parameter.get("sort") ,1); 
			String agentIds = StringUtil.trim(parameter.get("agentIds"));
			
			if( StringUtil.isBlank(name) )
			{
				return new MethodResult(MethodResult.FAIL, "镜像名称不能为空");
			}
			if( StringUtil.isBlank(fromHostId) )
			{
				return new MethodResult(MethodResult.FAIL, "请选择“来自云主机”");
			}
			if( StringUtil.isBlank(type) )
			{
				return new MethodResult(MethodResult.FAIL, "请选择“镜像类型”");
			}
			
			SysDiskImageMapper sysDiskImageMapper = this.sqlSession.getMapper(SysDiskImageMapper.class);
			CloudHostMapper cloudHostMapper       = this.sqlSession.getMapper(CloudHostMapper.class);
			
			CloudHostVO cloudHostVO = cloudHostMapper.getById(fromHostId);
			if( cloudHostVO==null || StringUtil.isBlank(cloudHostVO.getRealHostId()) )
			{
				throw new AppException("云主机还没有创建成功");
			}
			
			String realHostId = cloudHostVO.getRealHostId();
			String id = StringUtil.generateUUID();
			
//			HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(cloudHostVO.getRegion());
//			JSONObject diskImageCreateResult = channel.diskImageCreate(name, realHostId, description, new String[]{tag1, tag2}, "T2", loginInfo.getAccount());
//			if( HttpGatewayResponseHelper.isSuccess(diskImageCreateResult)==false )
//			{
//				logger.warn("SysDiskImageServiceImpl.addSysDiskImage() > ["+Thread.currentThread().getId()+"] create disk image failed, message:["+HttpGatewayResponseHelper.getMessage(diskImageCreateResult)+"]");
//				throw new AppException("磁盘镜像创建不成功");
//			}
//			
//			logger.warn("SysDiskImageServiceImpl.addSysDiskImage() > ["+Thread.currentThread().getId()+"] create disk image succeeded, message:["+HttpGatewayResponseHelper.getMessage(diskImageCreateResult)+"]");
//			
			//异步回调-创建磁盘镜像
			String sessionId = this.createDiskImage(cloudHostVO.getRegion(), name, realHostId, description, new String[]{tag1, tag2}, "T2", loginInfo.getAccount());
			if( sessionId == null )
			{
				logger.warn("SysDiskImageServiceImpl.addSysDiskImage() > ["+Thread.currentThread().getId()+"] create disk image failed");
				throw new AppException("磁盘镜像创建不成功");
			}
			//存入缓存
			Map<String, Object> temporaryData = new LinkedHashMap<String, Object>();
			temporaryData.put("sessionId", sessionId);
			temporaryData.put("name",   name);
			AppInconstant.sysDiskImageProgress.put(id, temporaryData);
			
			// 判断镜像名称是否已经存在
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("region", cloudHostVO.getRegion());
			condition.put("name",   name);
			SysDiskImageVO sysDiskImage = sysDiskImageMapper.getByRegionAndName(condition);
			if( sysDiskImage!=null )
			{
				return new MethodResult(MethodResult.FAIL, "镜像名称[" + name + "]已经存在");
			}

			Map<String, Object> sysDiskImageData =  new LinkedHashMap<String, Object>();
			sysDiskImageData.put("id",          id);
			sysDiskImageData.put("name",        name);
			sysDiskImageData.put("tag",         tag1+","+tag2);
			sysDiskImageData.put("fromHostId",  fromHostId);
			sysDiskImageData.put("description", description );
			sysDiskImageData.put("groupId",     loginInfo.getGroupId());
			sysDiskImageData.put("userId",      loginInfo.getUserId());
			sysDiskImageData.put("sort",        sort);
			sysDiskImageData.put("displayName", displayName);
			sysDiskImageData.put("type", type);
			sysDiskImageData.put("region",      cloudHostVO.getRegion());
			
			int n = sysDiskImageMapper.addSysDiskImage(sysDiskImageData);
			if( n > 0 )
			{
				if("2".equals(type)){
					SysUserImageRelationMapper sysUserImageRelationMapper = this.sqlSession.getMapper(SysUserImageRelationMapper.class);
					String [] agents = agentIds.split(",");
					for( String agent : agents )
					{
						Map<String, Object> data = new LinkedHashMap<String, Object>();
						data.put("id", StringUtil.generateUUID());
						data.put("userId", agent);
						data.put("sysImageId", id);
						sysUserImageRelationMapper.addImageToUser(data);
					}
				}
				return new MethodResult(MethodResult.SUCCESS, "添加成功");
			}
			else
			{
				return new MethodResult(MethodResult.FAIL, "添加失败");
			}
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("添加失败");
		}
		
	}

	@Callable
	@Transactional(readOnly = false)
	public MethodResult updateSysDiskImageById(Map<String, Object> parameter)
	{
		logger.debug("SysDiskImageServiceImpl.updateSysDiskImageById()");
		try
		{
			//权限判断
			HttpServletRequest request = RequestContext.getHttpRequest();
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			if( loginInfo.hasPrivilege(PrivilegeConstant.sys_disk_image_manage_modify) == false )
			{
				return new MethodResult(MethodResult.FAIL, "您没有权限进行此操作");
			}
			//参数处理
			String sysDiskImageId  = StringUtil.trim(parameter.get("sysDiskImage_id"));
			String region          = StringUtil.trim(parameter.get("region"));
			String name            = StringUtil.trim(parameter.get("name"));
			String fromHostId      = StringUtil.trim(parameter.get("from_host_id"));
			String tag1            = StringUtil.trim(parameter.get("tag1"));
			String tag2            = StringUtil.trim(parameter.get("tag2"));
			String description     = StringUtil.trim(parameter.get("description"));
			String displayName     = StringUtil.trim(parameter.get("display_name"));
			String type = StringUtil.trim(parameter.get("type"));
		    Integer sort       = StringUtil.parseInteger((String)parameter.get("sort") ,1); 
			String agentIds = StringUtil.trim(parameter.get("agentIds"));
			
			if( StringUtil.isBlank(sysDiskImageId) )
			{
				return new MethodResult(MethodResult.FAIL, "sysDiskImageId不能为空");
			}
			if( StringUtil.isBlank(region) )
			{
				return new MethodResult(MethodResult.FAIL, "region不能为空");
			}
			if( StringUtil.isBlank(name) )
			{
				return new MethodResult(MethodResult.FAIL, "镜像名称不能为空");
			}
//			if( StringUtil.isBlank(fromHostId) )
//			{
//				return new MethodResult(MethodResult.FAIL, "请选择“来自云主机”");
//			}
			
			
			SysDiskImageMapper sysDiskImageMapper = this.sqlSession.getMapper(SysDiskImageMapper.class);
			
			//判断系统镜像名称是否已经存在
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("region", region);
			condition.put("name",   name);
			SysDiskImageVO sysDiskImage = sysDiskImageMapper.getByRegionAndName(condition);
			if( sysDiskImage!=null && sysDiskImage.getId().equals(sysDiskImageId)==false )
			{
				return new MethodResult(MethodResult.FAIL, "系统镜像名称[" + name + "]已经存在");
			}
			
			//更新系统镜像表
			Map<String, Object> sysDiskImageData = new LinkedHashMap<String, Object>();
			sysDiskImageData.put("id",          sysDiskImageId);
			sysDiskImageData.put("name",        name);
			sysDiskImageData.put("fromHostId",  fromHostId);
			sysDiskImageData.put("tag",         tag1+","+tag2);
			sysDiskImageData.put("description", description);
			sysDiskImageData.put("sort",        sort);
			sysDiskImageData.put("displayName", displayName);
			sysDiskImageData.put("type", type);
			
			int n = sysDiskImageMapper.updateSysDiskImageById(sysDiskImageData);
			if( n==0 )
			{ 				
				return new MethodResult(MethodResult.FAIL, "修改失败");
			}
			else
			{
				if("2".equals(type)){
					SysUserImageRelationMapper sysUserImageRelationMapper = this.sqlSession.getMapper(SysUserImageRelationMapper.class);
					sysUserImageRelationMapper.deleteImageByImageId(sysDiskImageId);
					String [] agents = agentIds.split(",");
					for( String agent : agents )
					{
						Map<String, Object> data = new LinkedHashMap<String, Object>();
						data.put("id", StringUtil.generateUUID());
						data.put("userId", agent);
						data.put("sysImageId", sysDiskImageId);
						sysUserImageRelationMapper.addImageToUser(data);
					}
				}
				//删除分配信息
				if("1".equals(type)){
					SysUserImageRelationMapper sysUserImageRelationMapper = this.sqlSession.getMapper(SysUserImageRelationMapper.class);
					sysUserImageRelationMapper.deleteImageByImageId(sysDiskImageId);
				}
				return new MethodResult(MethodResult.SUCCESS, "修改成功");
			}
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("修改失败");
		}
	}

	
	@Callable
	@Transactional(readOnly = false)
	public MethodResult deleteSysDiskImageId(String sysDiskImageId)
	{
		
		logger.debug("SysDiskImageServiceImpl.deleteSysDiskImageId()");
		try
		{
			if( sysDiskImageId == null )
			{
				throw new AppException("sysDiskImageId不能为空");
			}

			SysDiskImageMapper sysDiskImageMapper =this.sqlSession.getMapper(SysDiskImageMapper.class);
			int n = sysDiskImageMapper.deleteSysDiskImageById(sysDiskImageId);
			

			if( n > 0  )
			{
				return new MethodResult(MethodResult.SUCCESS, "删除成功");
			}
			else
			{
				return new MethodResult(MethodResult.FAIL, "删除失败");
			}
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("删除失败");
		}
	}
	

	@Callable
	@Transactional(readOnly = false)
	public MethodResult deleteSysDiskImageByIds(List<String> sysDiskImageIds)
	{
		
		logger.debug("SysDiskImageServiceImpl.deleteSysDiskImageIds()");
		try
		{
			if( sysDiskImageIds == null || sysDiskImageIds.size()==0  )
			{
				throw new AppException("sysDiskImageIds不能为空");
			}
			
			SysDiskImageMapper sysDiskImageMapper =this.sqlSession.getMapper(SysDiskImageMapper.class);
			
			
			for( String sysDiskImageId : sysDiskImageIds )
			{
				SysDiskImageVO sysDiskImageVO = sysDiskImageMapper.getById(sysDiskImageId);
				if( sysDiskImageVO==null || StringUtil.isBlank(sysDiskImageVO.getRealImageId()) )
				{
					continue;
				}
				// delete from http gateway
				HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(sysDiskImageVO.getRegion());
				JSONObject diskImageDeleteResult = channel.diskImageDelete(sysDiskImageVO.getRealImageId());
				if( HttpGatewayResponseHelper.isSuccess(diskImageDeleteResult) )
				{
					logger.info("SysDiskImageServiceImpl.deleteSysDiskImageIds() > ["+Thread.currentThread().getId()+"] delete disk image succeeded, uuid:["+sysDiskImageVO.getRealImageId()+"], message:["+HttpGatewayResponseHelper.getMessage(diskImageDeleteResult)+"]");
				}
				else
				{
					logger.warn("SysDiskImageServiceImpl.deleteSysDiskImageIds() > ["+Thread.currentThread().getId()+"] delete disk image failed, uuid:["+sysDiskImageVO.getRealImageId()+"], message:["+HttpGatewayResponseHelper.getMessage(diskImageDeleteResult)+"]");
				}
			}
			
			// delete from mysql
			int n = sysDiskImageMapper.deleteSysDiskImageByIds(sysDiskImageIds.toArray(new String[0]));
			if( n > 0  )
			{
				return new MethodResult(MethodResult.SUCCESS, "删除成功");
			}
			else
			{
				return new MethodResult(MethodResult.FAIL, "删除失败");
			}
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("删除失败");
		}
	}
	@Callable
	@Transactional(readOnly = false)
	public MethodResult publishSysDiskImageByIds(List<String> sysDiskImageIds)
	{
		
		logger.debug("SysDiskImageServiceImpl.publishSysDiskImageByIds()");
		try
		{
			if( sysDiskImageIds == null || sysDiskImageIds.size()==0  )
			{
				throw new AppException("未选中发布项");
			}
			
			SysDiskImageMapper sysDiskImageMapper =this.sqlSession.getMapper(SysDiskImageMapper.class);
			
			 
			int n = sysDiskImageMapper.publishSysDiskImageByIds(sysDiskImageIds.toArray(new String[0]));
			if( n > 0  )
			{
				return new MethodResult(MethodResult.SUCCESS, "发布成功");
			}
			else
			{
				return new MethodResult(MethodResult.FAIL, "发布失败");
			}
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("删除失败");
		}
	}
	@Callable
	@Transactional(readOnly = false)
	public MethodResult retrieveSysDiskImageByIds(List<String> sysDiskImageIds)
	{
		
		logger.debug("SysDiskImageServiceImpl.retrieveSysDiskImageByIds()");
		try
		{
			if( sysDiskImageIds == null || sysDiskImageIds.size()==0  )
			{
				throw new AppException("未选中取消项");
			}
			
			SysDiskImageMapper sysDiskImageMapper =this.sqlSession.getMapper(SysDiskImageMapper.class);
			
			
			int n = sysDiskImageMapper.retrieveSysDiskImageByIds(sysDiskImageIds.toArray(new String[0]));
			if( n > 0  )
			{
				return new MethodResult(MethodResult.SUCCESS, "取消发布成功");
			}
			else
			{
				return new MethodResult(MethodResult.FAIL, "取消发布失败");
			}
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("删除失败");
		}
	}

	/**
	 * 
	 */
	@Transactional(readOnly = false)
	public boolean initSysDiskImageFromHttpGateway()
	{
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
					logger.warn("SysDiskImageServiceImpl.initSysDiskImageFromHttpGateway() > ["+Thread.currentThread().getId()+"] init disk images from http gateway failed, message:["+HttpGatewayResponseHelper.getMessage(diskImageResult)+"], region:["+String.format("%s:%s", regionData.getId(), regionData.getName())+"]");
				}

				SysDiskImageMapper sysDiskImageMapper = this.sqlSession.getMapper(SysDiskImageMapper.class);
				long maxSort = sysDiskImageMapper.getMaxSort();
				
				JSONArray diskImages = (JSONArray)diskImageResult.get("disk_images");
				if( diskImages==null )
				{
					logger.error("SysDiskImageServiceImpl.initSysDiskImageFromHttpGateway() > ["+Thread.currentThread().getId()+"] get sys disk images failed, diskImages is null, region:["+String.format("%s:%s", regionData.getId(), regionData.getName()) +"]");
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
						updateSysDiskImageFromRealDataByRealImageId(diskImage, diskImageVO);
					}
					else
					{
						Map<String, Object> condition = new LinkedHashMap<String, Object>();
						condition.put("region", regionData.getId());
						condition.put("name",   name);
						diskImageVO = sysDiskImageMapper.getByRegionAndName(condition);
						// if there is a disk image with same real_image_name in DB, update it.
						if( diskImageVO!=null )
						{
							updateUnrelatedSysDiskImageFromRealDataByRegionAndName(regionData.getId(), diskImage, diskImageVO);
						}
						// if not disk image with same real_image_id in DB, add into DB
						else
						{
							maxSort++;
							Map<String, Object> sysDiskImageData =  new LinkedHashMap<String, Object>();
							sysDiskImageData.put("id",            StringUtil.generateUUID());
							sysDiskImageData.put("realImageId",   uuid);
							sysDiskImageData.put("fromHostId",    null);
							sysDiskImageData.put("name",          name);
							sysDiskImageData.put("tag",           StringUtil.joinWrap(identity, ",", "", StringUtil.JOIN_WRAP_PREPROCESS_FLAG_URLENCODE));
							sysDiskImageData.put("description",   description );
							sysDiskImageData.put("groupId",       "");
							sysDiskImageData.put("userId",        "");
							sysDiskImageData.put("sort",          maxSort);
							sysDiskImageData.put("region",        regionData.getId());
							// 默认为通用镜像
							sysDiskImageData.put("type",        1);
							
							sysDiskImageMapper.addSysDiskImage(sysDiskImageData);
						}
					}
				}
				logger.info("SysDiskImageServiceImpl.initSysDiskImageFromHttpGateway() > ["+Thread.currentThread().getId()+"] 同步系统磁盘镜像完成, region:["+String.format("%s:%s", regionData.getId(), regionData.getName())+"]");
			}
			catch( SocketException e )
			{
				logger.error("SysDiskImageServiceImpl.initSysDiskImageFromHttpGateway() > ["+Thread.currentThread().getId()+"] connect to http gateway failed, exception:["+e.getMessage()+"], region:["+String.format("%s:%s", regionData.getId(), regionData.getName()) +"]");
			}
			catch (Throwable e)
			{
				throw AppException.wrapException(e);
			}
		}
		logger.info("SysDiskImageServiceImpl.initSysDiskImageFromHttpGateway-->finish get disk image info");
		return true;
	}
	
	
	private void updateSysDiskImageFromRealDataByRealImageId(JSONObject diskImage, SysDiskImageVO diskImageVO)
	{
		String uuid        = JSONLibUtil.getString(diskImage,      "uuid");
		String description = JSONLibUtil.getString(diskImage,      "description"); 
		String[] identity  = JSONLibUtil.getStringArray(diskImage, "identity");
		
		//更新系统镜像表
		Map<String, Object> sysDiskImageData = new LinkedHashMap<String, Object>();
		sysDiskImageData.put("tag",         StringUtil.joinWrap(identity, ",", "", StringUtil.JOIN_WRAP_PREPROCESS_FLAG_URLENCODE));
		sysDiskImageData.put("description", description);
		sysDiskImageData.put("realImageId", uuid);
		
		logger.info("SysDiskImageServiceImpl.updateSysDiskImageFromRealDataByRealImageId-->"+diskImageVO.getName()+"-description-"+description+"-tag-"+StringUtil.joinWrap(identity, ",", "", StringUtil.JOIN_WRAP_PREPROCESS_FLAG_URLENCODE));
		logger.info("SysDiskImageServiceImpl.updateSysDiskImageFromRealDataByRealImageId-->"+diskImageVO.getName()+"-description-"+diskImageVO.getDescription()+"-tag-"+diskImageVO.getTag());
        if(!diskImageVO.getDescription().equals(description) || !diskImageVO.getTag().equals(StringUtil.joinWrap(identity, ",", "", StringUtil.JOIN_WRAP_PREPROCESS_FLAG_URLENCODE))){        	
        	logger.info("SysDiskImageServiceImpl.update-->"+diskImageVO.getName()+"-description-"+description+"-tag-"+StringUtil.joinWrap(identity, ",", "", StringUtil.JOIN_WRAP_PREPROCESS_FLAG_URLENCODE));
        	SysDiskImageMapper sysDiskImageMapper = this.sqlSession.getMapper(SysDiskImageMapper.class);
        	sysDiskImageMapper.updateSysDiskImageByRealImageId(sysDiskImageData);
        }
	}
	
	
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
		sysDiskImageMapper.updateUnrelatedSysDiskImageByRegionAndName(sysDiskImageData);
	}
	
	/**
	 * 创建磁盘镜像，通过getDiskImageProgressData()方法可以获得创建磁盘镜像进度。
	 * 
	 * @param regionId
	 * @param name
	 * @param realHostId
	 * @param description
	 * @param identity
	 * @param group
	 * @param user
	 * @param callback
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
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
	@Callable
	@Transactional(readOnly = true)
	public MethodResult getSysDiskImageCreationResult(String id) {
		try {
			if(StringUtil.isBlank(id)){
				
				throw new AppException("参数id为空");
			}
			SysDiskImageMapper sysDiskImageMapper = this.sqlSession.getMapper(SysDiskImageMapper.class);			 
			SysDiskImageVO sysDiskIamgeVO = sysDiskImageMapper.getById(id);
			if (sysDiskIamgeVO == null) {
				throw new AppException("找不到镜像信息"); 
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
				throw new AppException("无法获取创建信息"); 
			}
			DiskImageProgressData data = this.getDiskImageProgressData(temporaryData.get("sessionId").toString(), temporaryData.get("name").toString());
			if(data==null){
				throw new AppException("无法获取创建信息"); 				
			}else{
				
				MethodResult result = new MethodResult(MethodResult.SUCCESS, "");
				result.put("progress", data.getProgress());
				result.put("creation_status", false);
				logger.info("get sys_disk_image progress "+data);
				return result;
			} 
		} catch (AppException e) {
			throw e;
		} catch (Exception e) {
			throw new AppException(e);
		}
	}


	/**
	 * 根据地域筛选镜像
	 * @see com.zhicloud.op.service.SysDiskImageService#selectDiskImageByRegion(java.lang.String,java.lang.String)
	 */
	@Callable
	@CallWithoutLogin
	@Transactional(readOnly = true)
	public MethodResult selectDiskImageByRegion(String region,String agentId) {
		SysDiskImageMapper sysDiskImageMapper = this.sqlSession.getMapper(SysDiskImageMapper.class);	
		List<SysDiskImageVO> sysDiskImageOptions = sysDiskImageMapper.getAllCommonImage();
		List<SysDiskImageVO> list = new ArrayList<SysDiskImageVO>();
		if(!StringUtil.isBlank(agentId)){
			List<SysDiskImageVO> sysDiskImageDIYOptions = sysDiskImageMapper.getDiyImage(agentId);
			for(SysDiskImageVO vo : sysDiskImageDIYOptions){
				if(StringUtil.isBlank(vo.getRealImageId())||vo.getRegion()!=Integer.parseInt(region)||vo.getStatus() == 1){
	 				continue;
	 			}else{
	 				list.add(vo);
	 			}
				
			}
			
		}
		int size = sysDiskImageOptions.size();
 		for(int i = 0;i<size;i++){
 			SysDiskImageVO vo = sysDiskImageOptions.get(i);
 			if(StringUtil.isBlank(vo.getRealImageId())||vo.getRegion()!=Integer.parseInt(region)||vo.getStatus() == 1){
 				continue;
 			}else{
 				list.add(vo);
 			}
 		}
 		MethodResult result = new MethodResult(MethodResult.SUCCESS, "");
 		result.put("imageList", list);
 		return result;
	}
}
