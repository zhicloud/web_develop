package com.zhicloud.op.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.op.app.authorization.PrivilegeConstant;
import com.zhicloud.op.app.helper.CloudHostWarehouseHelper;
import com.zhicloud.op.app.helper.LoginHelper;
import com.zhicloud.op.app.helper.ServiceHelper;
import com.zhicloud.op.app.listener.WarehouseCloudHostCreationListener;
import com.zhicloud.op.common.util.CapacityUtil;
import com.zhicloud.op.common.util.FlowUtil;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.login.LoginInfo;
import com.zhicloud.op.mybatis.mapper.CloudHostMapper;
import com.zhicloud.op.mybatis.mapper.CloudHostWarehouseDefinitionMapper;
import com.zhicloud.op.mybatis.mapper.CloudHostWarehouseDetailMapper;
import com.zhicloud.op.mybatis.mapper.DiskPackageOptionMapper;
import com.zhicloud.op.mybatis.mapper.SysDiskImageMapper;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.request.RequestContext;
import com.zhicloud.op.service.CloudHostWarehouseService;
import com.zhicloud.op.service.constant.AppConstant;
import com.zhicloud.op.vo.CloudHostWarehouseDefinitionVO;
import com.zhicloud.op.vo.CloudHostWarehouseDetailVO;
import com.zhicloud.op.vo.DiskPackageOptionVO;
import com.zhicloud.op.vo.SysDiskImageVO;

@Transactional(readOnly=true)
public class CloudHostWarehouseServiceImpl extends BeanDirectCallableDefaultImpl implements CloudHostWarehouseService
{
	
	public static final Logger logger = Logger.getLogger(CloudHostWarehouseServiceImpl.class);
	// private static int flog = 0;
	
	private SqlSession sqlSession;
	
	
	public void setSqlSession(SqlSession sqlSession)
	{
		this.sqlSession = sqlSession;
	}

	//---------------
	
	/**
	 * 
	 */
	@Transactional(readOnly=false)
	public MethodResult createOneWarehouseCloudHost()
	{
		try
		{
			CloudHostWarehouseDetailMapper cloudHostWarehouseDetailMapper = this.sqlSession.getMapper(CloudHostWarehouseDetailMapper.class);
			
			// 获取一个需要创建的仓库云主机
			CloudHostWarehouseDetailVO cloudHostWarehouseDetailVO = cloudHostWarehouseDetailMapper.getOneUncreatedCloudHost();
			if( cloudHostWarehouseDetailVO==null )
			{ 
				logger.info("OrderInfoServiceImpl.createOneWarehouseCloudHost(): ["+Thread.currentThread().getId()+"] no_more_uncreated_warehouse_cloud_host");
				return new MethodResult(MethodResult.SUCCESS, "no_more_uncreated_warehouse_cloud_host");					
				 
				
			}
			
			return new CloudHostWarehouseHelper(this.sqlSession).createOneWarehouseCloudHost(cloudHostWarehouseDetailVO);
		}
		catch (Exception e)
		{
			throw AppException.wrapException(e);
		}
	}
	@Transactional(readOnly=false)
	public MethodResult createOneWarehouseFailedCloudHost()
	{
		try
		{ 
			// 获取到已经创建失败的云主机 
			CloudHostWarehouseDetailMapper cloudHostWarehouseDetailMapper = this.sqlSession.getMapper(CloudHostWarehouseDetailMapper.class);

			CloudHostWarehouseDetailVO vo = cloudHostWarehouseDetailMapper.getOneFailedCloudHost();
			if(vo == null ){
				logger.info("OrderInfoServiceImpl.createOneWarehouseFailedCloudHost(): ["+Thread.currentThread().getId()+"] no_more_faied_warehouse_cloud_host");
				return new MethodResult(MethodResult.SUCCESS, "no_more_faied_warehouse_cloud_host");					
			}
				 
			
			logger.info("OrderInfoServiceImpl.createOneWarehouseFailedCloudHost(): ["+Thread.currentThread().getId()+"] create_one_faied_warehouse_cloud_host");
			return new CloudHostWarehouseHelper(this.sqlSession).createOneWarehouseCloudHost(vo);
		}
		catch (Exception e)
		{
			throw AppException.wrapException(e);
		}
	}
	
	/**
	 * 
	 */
	@Transactional(readOnly=false)
	public MethodResult createOneWarehouseCloudHostByWarehouseId(String warehouseId)
	{
		try
		{
			CloudHostWarehouseDetailMapper cloudHostWarehouseDetailMapper = this.sqlSession.getMapper(CloudHostWarehouseDetailMapper.class);
			
			// 获取一个需要创建的仓库云主机
			CloudHostWarehouseDetailVO cloudHostWarehouseDetailVO = cloudHostWarehouseDetailMapper.getOneUncreatedCloudHostByWarehouseId(warehouseId);
			if( cloudHostWarehouseDetailVO==null )
			{
				logger.info("OrderInfoServiceImpl.createOneWarehouseCloudHost(): ["+Thread.currentThread().getId()+"] no_more_uncreated_warehouse_cloud_host, warehouseId:["+warehouseId+"]");
				return new MethodResult(MethodResult.SUCCESS, "no_more_uncreated_warehouse_cloud_host");
			}
			
			return new CloudHostWarehouseHelper(this.sqlSession).createOneWarehouseCloudHost(cloudHostWarehouseDetailVO);
		}
		catch (Exception e)
		{
			throw AppException.wrapException(e);
		}
	}
	
	//=======================================
	@Callable
	public String managePage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("CloudHostWarehouseServiceImpl.managePage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.hasPrivilege(PrivilegeConstant.warehouse_manage_page) == false )
		{
			return "/public/have_not_access.jsp";
		}
		
		return "/security/operator/warehouse_manage.jsp";
	}

	@Callable
	public void queryWarehouse(HttpServletRequest request,HttpServletResponse response)
	{
		logger.debug("CloudHostWarehouseServiceImpl.queryWarehouse()");
		try
		{
			// 权限判断
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			if(loginInfo.hasPrivilege(PrivilegeConstant.warehouse_manage_page) == false )
			{
				throw new AppException("您没有权限进行些操作");
			}
			
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");

			// 获取参数
			String description = StringUtil.trim(request.getParameter("description"));
			Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
			Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 10);

			// 查询数据库
			CloudHostWarehouseDefinitionMapper cloudHostWarehouseDefinitionMapper = this.sqlSession.getMapper(CloudHostWarehouseDefinitionMapper.class);
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("myDescription",  "%"+description+"%");
			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			
			List<CloudHostWarehouseDefinitionVO> warehouseList = null;
			int total     = cloudHostWarehouseDefinitionMapper.queryPageCount(condition); // 总行数
			warehouseList = cloudHostWarehouseDefinitionMapper.queryPage(condition);// 分页结果

			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, warehouseList);
		}
		catch( Exception e )
		{
			throw AppException.wrapException(e);
		}
	}

	@Callable
	public String addPage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("CloudHostWarehouseServiceImpl.addPage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if (loginInfo.hasPrivilege(PrivilegeConstant.warehouse_manage_add) == false)
		{
			return "/public/have_not_access.jsp";
		}
		
		SysDiskImageMapper sysDiskImageMapper = this.sqlSession.getMapper(SysDiskImageMapper.class);
		DiskPackageOptionMapper diskPackageOptionMapper = this.sqlSession.getMapper(DiskPackageOptionMapper.class);
		List<SysDiskImageVO> sysDiskImageOptionsGZ = sysDiskImageMapper.getByRegion(1);
		List<SysDiskImageVO> sysDiskImageOptionsCD = sysDiskImageMapper.getByRegion(2);
		List<SysDiskImageVO> sysDiskImageOptionsHK = sysDiskImageMapper.getByRegion(4);
		DiskPackageOptionVO diskOption = diskPackageOptionMapper.getOne();
		
		if (diskOption == null)
		{ 	// 如果还没有配置磁盘套餐项，则设置一下默认的
			diskOption = new DiskPackageOptionVO(CapacityUtil.fromCapacityLabel("1GB"), CapacityUtil.fromCapacityLabel("100GB"));
		}
		request.setAttribute("diskOption", diskOption);
		request.setAttribute("sysDiskImageOptionsGZ", sysDiskImageOptionsGZ);
		request.setAttribute("sysDiskImageOptionsCD", sysDiskImageOptionsCD);
		request.setAttribute("sysDiskImageOptionsHK", sysDiskImageOptionsHK);
		return "/security/operator/warehouse_add.jsp";
	}
	
	
	@Callable
	@Transactional(readOnly = false)
	public MethodResult addWarehouse(Map<String, Object> parameter)
	{
		logger.debug("CloudHostWarehouseServiceImpl.addWarehouse()");
		try
		{
			HttpServletRequest request = RequestContext.getHttpRequest();
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			// 参数处理
			String region          = StringUtil.trim(parameter.get("region"));
			String sysImageId      = StringUtil.trim(parameter.get("sys_image_id_GZ"));
			String sysImageIdCD    = StringUtil.trim(parameter.get("sys_image_id_CD"));
			String sysImageIdHK    = StringUtil.trim(parameter.get("sys_image_id_HK"));
			// String dataDisk_str    = StringUtil.trim(parameter.get("data_disk"));
			String description     = StringUtil.trim(parameter.get("description"));
			String totalAmount     = StringUtil.trim(parameter.get("total_amount"));
			JSONArray isCreate     = (JSONArray)parameter.get("is_create");
			if(StringUtil.isBlank(sysImageId)){
				if(StringUtil.isBlank(sysImageIdCD)){
					sysImageId = sysImageIdHK;
				}else{
					sysImageId = sysImageIdCD;
				}
			}
			if( StringUtil.isBlank(region) )
			{
				return new MethodResult(MethodResult.FAIL, "请选择一个'地域'");
			}
			if( "1".equals(region) && StringUtil.isBlank(sysImageId) )
			{
				return new MethodResult(MethodResult.FAIL, "请选择一个系统镜像");
			}
			if( "2".equals(region) && StringUtil.isBlank(sysImageIdCD) )
			{
				return new MethodResult(MethodResult.FAIL, "请选择一个系统镜像");
			}
			if( "4".equals(region) && StringUtil.isBlank(sysImageIdHK) )
			{
				return new MethodResult(MethodResult.FAIL, "请选择一个系统镜像");
			}
//			if( StringUtil.isBlank(dataDisk_str) )
//			{
//				return new MethodResult(MethodResult.FAIL, "数据磁盘不能为空");
//			}
//			BigInteger dataDisk    = CapacityUtil.fromCapacityLabel(dataDisk_str);
			if( StringUtil.isBlank(totalAmount) )
			{
				return new MethodResult(MethodResult.FAIL, "库存总量不能为空");
			}
			
			CloudHostWarehouseDefinitionMapper warehouseMapper            = this.sqlSession.getMapper(CloudHostWarehouseDefinitionMapper.class);
			CloudHostMapper cloudHostMapper                               = this.sqlSession.getMapper(CloudHostMapper.class);
			SysDiskImageMapper sysDiskImageMapper                         = this.sqlSession.getMapper(SysDiskImageMapper.class);
			CloudHostWarehouseDetailMapper cloudHostWarehouseDetailMapper = this.sqlSession.getMapper(CloudHostWarehouseDetailMapper.class);
  			// 添加云主机进数据库
			String warehouseId = StringUtil.generateUUID();
			String sysImageName = sysDiskImageMapper.getById(sysImageId).getName();
			Map<String, Object> cloudHostWarehouse = new LinkedHashMap<String, Object>();
			cloudHostWarehouse.put("id",            warehouseId);
			cloudHostWarehouse.put("sysImageId",    sysImageId);
//			cloudHostWarehouse.put("dataDisk",      dataDisk);
			cloudHostWarehouse.put("description",   description);
			cloudHostWarehouse.put("totalAmount",   totalAmount);
			cloudHostWarehouse.put("remainAmount",  totalAmount);
			cloudHostWarehouse.put("region",        region);
			
			int n = warehouseMapper.addWarehouse(cloudHostWarehouse);
			int total_amount = Integer.parseInt(totalAmount); 
			for (int i = 0; i < total_amount; i++)
			{
				Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
				String hostId = StringUtil.generateUUID();
				cloudHostData.put("id",            hostId);
				cloudHostData.put("type",          AppConstant.CLOUD_HOST_TYPE_4_WAREHOUSE);
				cloudHostData.put("userId",        loginInfo.getUserId());
				cloudHostData.put("hostName",      "warehouse_"+hostId);
				cloudHostData.put("displayName",      "warehouse_"+hostId);
				cloudHostData.put("account",       "warehouse");
				cloudHostData.put("password",      "warehouse");
				cloudHostData.put("cpuCore",       1);
				cloudHostData.put("memory",        CapacityUtil.fromCapacityLabel("1GB"));
				cloudHostData.put("sysImageId",    sysImageId);
				cloudHostData.put("sysImageName",  sysImageName);
				cloudHostData.put("sysDisk",       CapacityUtil.fromCapacityLabel("10GB"));
				cloudHostData.put("dataDisk",      0);
				cloudHostData.put("bandwidth",     FlowUtil.fromFlowLabel("1Mbps"));
				cloudHostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN);
				cloudHostData.put("status",        AppConstant.CLOUD_HOST_STATUS_1_NORNAL);
				cloudHostData.put("isAutoStartup", AppConstant.CLOUD_HOST_IS_AUTO_STARTUP_NO);
				cloudHostData.put("region",        region);
				cloudHostMapper.addCloudHost(cloudHostData);
				
				Map<String, Object> cloudHostWarehouseDetail = new LinkedHashMap<String, Object>();
				String warehouseDetailId = StringUtil.generateUUID();
				cloudHostWarehouseDetail.put("id",             warehouseDetailId);
				cloudHostWarehouseDetail.put("warehouseId",    warehouseId);
				cloudHostWarehouseDetail.put("hostId",         hostId);
				cloudHostWarehouseDetail.put("status",         AppConstant.CLOUD_HOST_WAREHOUSE_DETAIL_STATUS_1_NOT_PROCESSED);
				cloudHostWarehouseDetail.put("processMessage", "unprocessed");
				cloudHostWarehouseDetailMapper.addWarehouseDetail(cloudHostWarehouseDetail);
			}
			
			

			// 现在创建
			if( isCreate.size() > 0 && "true".equals(isCreate.get(0)) )
			{
				WarehouseCloudHostCreationListener.instance.addWarehouseIdNeedToBeImmediatelyCreated(warehouseId);
			}
			
			
			if( n <= 0 )
			{
				return new MethodResult(MethodResult.FAIL, "添加失败");
			}
			else
			{
				return new MethodResult(MethodResult.SUCCESS, "添加成功");
			}
		}
		catch(AppException e)
		{ 
			logger.error(e);
			throw new AppException("添加失败");
		}
		catch(Exception e)
		{
			logger.error(e);
			throw new AppException("添加失败");
		}
	}

	@Callable
	@Transactional(readOnly = false)
	public MethodResult deleteWarehouseByIds(List<?> warehouseList)
	{
		logger.debug("CloudHostWarehouseServiceImpl.deleteWarehouseByIds()");
		try
		{
			if( warehouseList==null || warehouseList.size()==0 )
			{
				throw new AppException("warehouseIds不能为空");
			}

			CloudHostWarehouseDefinitionMapper cloudHostWarehouseDefinitionMapper = this.sqlSession.getMapper(CloudHostWarehouseDefinitionMapper.class);
			CloudHostWarehouseDetailMapper cloudHostWarehouseDetailMapper         = this.sqlSession.getMapper(CloudHostWarehouseDetailMapper.class);
			CloudHostMapper cloudHostMapper                                       = this.sqlSession.getMapper(CloudHostMapper.class);
			
			String[] warehouseIds = warehouseList.toArray(new String[0]);
			
			// 删除库存云主机
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("warehouseIds",          warehouseIds);
			condition.put("warehouseDetailStatus", new Integer[]{AppConstant.CLOUD_HOST_WAREHOUSE_DETAIL_STATUS_1_NOT_PROCESSED,
																 AppConstant.CLOUD_HOST_WAREHOUSE_DETAIL_STATUS_2_FAIL});
			cloudHostMapper.deleteUselessWarehouseCloudHosts(condition);
			
			// 删除云主机仓库明细，只删除‘未处理’和‘创建失败’的条目
			Map<String, Object> condition2 = new LinkedHashMap<String, Object>();
			condition2.put("warehouseIds", warehouseIds);
			condition2.put("status",       new Integer[]{AppConstant.CLOUD_HOST_WAREHOUSE_DETAIL_STATUS_1_NOT_PROCESSED,
														 AppConstant.CLOUD_HOST_WAREHOUSE_DETAIL_STATUS_2_FAIL});
			cloudHostWarehouseDetailMapper.deleteByWarehouseIdsAndStatus(condition2);

			// 删除云主机仓库的定义
			int n3 = cloudHostWarehouseDefinitionMapper.deleteByIds(warehouseIds);
			if( n3<=0 )
			{
				throw new AppException("删除云主机库存失败");
			}
			
			return new MethodResult(MethodResult.SUCCESS, "删除成功");
		}
		catch (AppException e)
		{ 
			logger.error(e);
			throw new AppException("删除失败");
		}
		catch (Exception e)
		{
			logger.error(e);
			throw new AppException("删除失败");
		}
	}
	
	@Override
	@Callable
	public String warehouseDetailPage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("CloudHostWarehouseServiceImpl.warehouseDetailPage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if (loginInfo.hasPrivilege(PrivilegeConstant.warehouse_manage_detail) == false)
		{
			return "/public/have_not_access_dialog.jsp";
		}
		
		String warehouseId = StringUtil.trim(request.getParameter("warehouseId"));
		request.setAttribute("warehouseId", warehouseId);
		
		return "/security/operator/warehouse_detail.jsp";
	}
	
	@Callable
	public void queryWarehouseDetail(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("WarehouseServiceImpl.queryWarehouseDetail()");
		try
		{
			// 权限判断
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			if (loginInfo.hasPrivilege(PrivilegeConstant.warehouse_manage_detail) == false)
			{
				throw new AppException("您没有权限进行此操作");
			}
			
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");
			
			// 获取参数
			String warehouseId = StringUtil.trim(request.getParameter("warehouse_id"));
			
			CloudHostWarehouseDetailMapper cloudHostWarehouseDetailMapper = this.sqlSession.getMapper(CloudHostWarehouseDetailMapper.class);
			
			int total                                            = cloudHostWarehouseDetailMapper.queryPageCount(warehouseId);	// 总行数
			List<CloudHostWarehouseDetailVO> warehouseDetailList = cloudHostWarehouseDetailMapper.queryPage(warehouseId);		// 分页结果
			
			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, warehouseDetailList);
		}
		catch (AppException e)
		{ 
			logger.error(e);
			throw new AppException("查询失败");
		}
		catch (Exception e)
		{
			logger.error(e);
			throw new AppException("查询失败");
		}
	}

	@Callable
	public String modPage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("CloudHostWarehouseServiceImpl.modPage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.hasPrivilege(PrivilegeConstant.warehouse_manage_modify) == false )
		{
			return "/public/have_not_access.jsp";
		}
		
		// 参数处理
		String warehouseId = StringUtil.trim(request.getParameter("warehouseId"));
		if( StringUtil.isBlank(warehouseId) )
		{
			throw new AppException("warehouseId不能为空");
		}

		CloudHostWarehouseDefinitionMapper warehouseMapper = this.sqlSession.getMapper(CloudHostWarehouseDefinitionMapper.class);
		
		CloudHostWarehouseDefinitionVO warehouse = warehouseMapper.getWarehouseById(warehouseId);
		if( warehouse != null )
		{
			SysDiskImageMapper sysDiskImageMapper           = this.sqlSession.getMapper(SysDiskImageMapper.class);
			DiskPackageOptionMapper diskPackageOptionMapper = this.sqlSession.getMapper(DiskPackageOptionMapper.class); 
			
			List<SysDiskImageVO> sysDiskImageOptions = sysDiskImageMapper.getAllCommonImage();
			//SysDiskImageVO sysDiskImageVO = sysDiskImageMapper.getById(warehouse.getSysImageId());
			DiskPackageOptionVO diskOption = diskPackageOptionMapper.getOne();
			
			if( diskOption==null )
			{	
				// 如果还没有配置磁盘套餐项，则设置一下默认的
				diskOption = new DiskPackageOptionVO(CapacityUtil.fromCapacityLabel("1GB"), CapacityUtil.fromCapacityLabel("100GB"));
			}
			
			request.setAttribute("diskOption", diskOption);
			//request.setAttribute("sysDiskImage", sysDiskImageVO);
			request.setAttribute("sysDiskImageOptions", sysDiskImageOptions);
			request.setAttribute("warehouse", warehouse);
			
			return "/security/operator/warehouse_mod.jsp";
		}
		else
		{
			request.setAttribute("message", "找不到运营商的记录");
			return "/public/warning_dialog.jsp";
		}
	}

	@Override
	@Callable
	@Transactional(readOnly = false)
	public MethodResult modWarehouse(Map<String, Object> parameter)
	{
		logger.debug("CloudHostWarehouseServiceImpl.modWarehouse()");
		try
		{
			HttpServletRequest request = RequestContext.getHttpRequest();
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			
			// 参数处理
			String warehouseId     = StringUtil.trim(parameter.get("warehouse_id"));
			// Integer oldTotalAmount = Integer.parseInt(StringUtil.trim(parameter.get("old_total_amount")));
			String sysImageId      = StringUtil.trim(parameter.get("sys_image_id"));
			// String dataDisk_str        = StringUtil.trim(parameter.get("data_disk"));
			String description     = StringUtil.trim(parameter.get("description"));
			Integer totalAmount    = Integer.parseInt(StringUtil.trim(parameter.get("total_amount")));
			JSONArray isCreate     = (JSONArray)parameter.get("is_create");
			
			if (StringUtil.isBlank(warehouseId))
			{
				return new MethodResult(MethodResult.FAIL, "warehouseId不能为空");
			}
			if (StringUtil.isBlank(sysImageId))
			{
				return new MethodResult(MethodResult.FAIL, "请选择一个系统镜像");
			}
//			if (StringUtil.isBlank(dataDisk_str))
//			{
//				return new MethodResult(MethodResult.FAIL, "数据磁盘不能为空");
//			}
//			BigInteger dataDisk    = CapacityUtil.fromCapacityLabel(dataDisk_str);
			if( totalAmount == null )
			{
				return new MethodResult(MethodResult.FAIL, "库存总量不能为空");
			}
			
			CloudHostWarehouseDefinitionMapper cloudHostWarehouseDefinitionMapper = this.sqlSession.getMapper(CloudHostWarehouseDefinitionMapper.class);
			CloudHostWarehouseDetailMapper cloudHostWarehouseDetailMapper         = this.sqlSession.getMapper(CloudHostWarehouseDetailMapper.class);
			CloudHostMapper cloudHostMapper                                       = this.sqlSession.getMapper(CloudHostMapper.class);
			SysDiskImageMapper sysDiskImageMapper                                 = this.sqlSession.getMapper(SysDiskImageMapper.class);
			
			String sysImageName = sysDiskImageMapper.getById(sysImageId).getName();
			CloudHostWarehouseDefinitionVO cloudHostWarehouseDefinitionVO = cloudHostWarehouseDefinitionMapper.getById(warehouseId);
			if( cloudHostWarehouseDefinitionVO==null )
			{
				throw new AppException("找不到云主机仓库定义信息");
			}
			
			// 
			int remainAmount = cloudHostWarehouseDetailMapper.getRemainAmountByWarehouseId(warehouseId);
			

			// 添加云主机进数据库
			int createNew = totalAmount - remainAmount;
			for (int i = 0; i < createNew; i++)
			{
				// 添加云主机信息
				Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
				String hostId = StringUtil.generateUUID();
				cloudHostData.put("id",            hostId);
				cloudHostData.put("type",          AppConstant.CLOUD_HOST_TYPE_4_WAREHOUSE);
				cloudHostData.put("userId",        loginInfo.getUserId());
				cloudHostData.put("hostName",      "warehouse_" + hostId);
				cloudHostData.put("displayName",      "warehouse_" + hostId);
				cloudHostData.put("account",       "warehouse");
				cloudHostData.put("password",      "warehouse");
				cloudHostData.put("cpuCore",       1);
				cloudHostData.put("memory",        CapacityUtil.fromCapacityLabel("1GB"));
				cloudHostData.put("sysImageId",    sysImageId);
				cloudHostData.put("sysImageName",  sysImageName);
				cloudHostData.put("sysDisk",       CapacityUtil.fromCapacityLabel("10GB"));
				cloudHostData.put("dataDisk",      0);
				cloudHostData.put("bandwidth",     FlowUtil.fromFlowLabel("1Mbps"));
				cloudHostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN);
				cloudHostData.put("status",        AppConstant.CLOUD_HOST_STATUS_1_NORNAL);
				cloudHostData.put("isAutoStartup", AppConstant.CLOUD_HOST_IS_AUTO_STARTUP_NO);
				cloudHostData.put("region",        cloudHostWarehouseDefinitionVO.getRegion());
				cloudHostMapper.addCloudHost(cloudHostData);
				
				// 添加云主机库存明细
				Map<String, Object> cloudHostWarehouseDetail = new LinkedHashMap<String, Object>();
				cloudHostWarehouseDetail.put("id",             StringUtil.generateUUID());
				cloudHostWarehouseDetail.put("warehouseId",    warehouseId);
				cloudHostWarehouseDetail.put("hostId",         hostId);
				cloudHostWarehouseDetail.put("status",         AppConstant.CLOUD_HOST_WAREHOUSE_DETAIL_STATUS_1_NOT_PROCESSED);
				cloudHostWarehouseDetail.put("processMessage", "unprocessed");
				cloudHostWarehouseDetailMapper.addWarehouseDetail(cloudHostWarehouseDetail);
			}
			
			// 修改云主机库存信息
			Map<String, Object> cloudHostWarehouseData = new LinkedHashMap<String, Object>();
			cloudHostWarehouseData.put("warehouseId",  warehouseId);
			cloudHostWarehouseData.put("sysImageId",   sysImageId);
//			cloudHostWarehouseData.put("dataDisk",     dataDisk);
			cloudHostWarehouseData.put("description",  description);
			cloudHostWarehouseData.put("totalAmount",  totalAmount);
			cloudHostWarehouseData.put("remainAmount", Math.max(totalAmount, remainAmount));
			int n = cloudHostWarehouseDefinitionMapper.updateWarehouseById(cloudHostWarehouseData);
			
			// 现在创建
			if( isCreate.size() > 0 && "true".equals(isCreate.get(0)) )
			{
				WarehouseCloudHostCreationListener.instance.addWarehouseIdNeedToBeImmediatelyCreated(warehouseId);
			}
			
			if (n <= 0)
			{
				return new MethodResult(MethodResult.FAIL, "修改失败");
			}
			else
			{
				return new MethodResult(MethodResult.SUCCESS, "修改成功");
			}
		}
		catch (AppException e)
		{ 
			logger.error(e);
			throw new AppException("修改失败");
		}
		catch (Exception e)
		{
			logger.error(e);
			throw new AppException("修改失败");
		}
	}

	@Override
	@Callable
	@Transactional(readOnly = false)
	public MethodResult deleteCloudHostById(String hostId) {
		logger.debug("CloudHostWarehouseServiceImpl.deleteCloudHostById()");
		try
		{
			if( hostId==null || hostId=="" )
			{
				throw new AppException("hostIds不能为空");
			}
			CloudHostWarehouseDetailMapper warehouseDetailMapper = this.sqlSession.getMapper(CloudHostWarehouseDetailMapper.class);
			CloudHostWarehouseDefinitionMapper warehouseMapper = this.sqlSession.getMapper(CloudHostWarehouseDefinitionMapper.class);
			
			// 从数据库删除
			CloudHostWarehouseDetailVO warehouseDetail = warehouseDetailMapper.getByHostId(hostId);
			warehouseMapper.minusOneRemainAmountById(warehouseDetail.getWarehouseId());
			int m = warehouseDetailMapper.deleteById(hostId);
			if(m<=0)
			{
				return new MethodResult(MethodResult.SUCCESS, "删除失败");
			}
			
			return new MethodResult(MethodResult.SUCCESS, "删除成功");
		}
		catch (AppException e)
		{ 
			logger.error(e);
			throw new AppException("删除失败");
		}
		catch (Exception e)
		{
			logger.error(e);
			throw new AppException("删除失败");
		}
	}

	@Callable
	@Transactional(readOnly = false)
	public MethodResult addACloudHostToWareById(String warehouseId,String userId) {
		logger.debug("CloudHostWarehouseServiceImpl.addACloudHostToWareById()");
		try
		{ 
			CloudHostWarehouseDefinitionMapper cloudHostWarehouseDefinitionMapper = this.sqlSession.getMapper(CloudHostWarehouseDefinitionMapper.class);
			CloudHostWarehouseDetailMapper cloudHostWarehouseDetailMapper         = this.sqlSession.getMapper(CloudHostWarehouseDetailMapper.class);
			CloudHostMapper cloudHostMapper                                       = this.sqlSession.getMapper(CloudHostMapper.class);
			
			 
			CloudHostWarehouseDefinitionVO cloudHostWarehouseDefinitionVO = cloudHostWarehouseDefinitionMapper.getById(warehouseId);
			if( cloudHostWarehouseDefinitionVO==null )
			{
				throw new AppException("找不到云主机仓库定义信息");
			}
			// 
			int remainAmount = cloudHostWarehouseDetailMapper.getRemainAmountByWarehouseId(warehouseId);
			

			// 添加云主机进数据库
			 
			// 添加云主机信息
			Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
			String hostId = StringUtil.generateUUID();
			cloudHostData.put("id",            hostId);
			cloudHostData.put("type",          AppConstant.CLOUD_HOST_TYPE_4_WAREHOUSE);
			cloudHostData.put("userId",        userId);
			cloudHostData.put("hostName",      "warehouse_" + hostId);
			cloudHostData.put("displayName",      "warehouse_" + hostId);
			cloudHostData.put("account",       "warehouse");
			cloudHostData.put("password",      "warehouse");
			cloudHostData.put("cpuCore",       1);
			cloudHostData.put("memory",        CapacityUtil.fromCapacityLabel("1GB"));
			cloudHostData.put("sysImageId",    cloudHostWarehouseDefinitionVO.getSysImageId());
			cloudHostData.put("sysImageName",  cloudHostWarehouseDefinitionVO.getSysImageName());
			cloudHostData.put("sysDisk",       CapacityUtil.fromCapacityLabel("10GB"));
			cloudHostData.put("dataDisk",      0);
			cloudHostData.put("bandwidth",     FlowUtil.fromFlowLabel("1Mbps"));
			cloudHostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN);
			cloudHostData.put("status",        AppConstant.CLOUD_HOST_STATUS_1_NORNAL);
			cloudHostData.put("isAutoStartup", AppConstant.CLOUD_HOST_IS_AUTO_STARTUP_NO);
			cloudHostData.put("region",        cloudHostWarehouseDefinitionVO.getRegion());
			cloudHostMapper.addCloudHost(cloudHostData);
			
			// 添加云主机库存明细
			Map<String, Object> cloudHostWarehouseDetail = new LinkedHashMap<String, Object>();
			cloudHostWarehouseDetail.put("id",             StringUtil.generateUUID());
			cloudHostWarehouseDetail.put("warehouseId",    warehouseId);
			cloudHostWarehouseDetail.put("hostId",         hostId);
			cloudHostWarehouseDetail.put("status",         AppConstant.CLOUD_HOST_WAREHOUSE_DETAIL_STATUS_1_NOT_PROCESSED);
			cloudHostWarehouseDetail.put("processMessage", "unprocessed");
			cloudHostWarehouseDetailMapper.addWarehouseDetail(cloudHostWarehouseDetail); 
			
			// 修改云主机库存信息
			Map<String, Object> cloudHostWarehouseData = new LinkedHashMap<String, Object>();
			cloudHostWarehouseData.put("warehouseId",  warehouseId);
			cloudHostWarehouseData.put("sysImageId",   cloudHostWarehouseDefinitionVO.getSysImageId());
//			cloudHostWarehouseData.put("dataDisk",     dataDisk);
			cloudHostWarehouseData.put("description",  cloudHostWarehouseDefinitionVO.getDescription());
			cloudHostWarehouseData.put("totalAmount",  cloudHostWarehouseDefinitionVO.getTotalAmount()+1);
			cloudHostWarehouseData.put("remainAmount", cloudHostWarehouseDefinitionVO.getRemainAmount()+1);
			int n = cloudHostWarehouseDefinitionMapper.updateWarehouseById(cloudHostWarehouseData);
			
			 
			 
			
			if (n <= 0)
			{
				return new MethodResult(MethodResult.FAIL, "修改失败");
			}
			else
			{
				return new MethodResult(MethodResult.SUCCESS, "修改成功");
			}
		}
		catch (AppException e)
		{ 
			logger.error(e);
			throw new AppException("修改失败");
		}
		catch (Exception e)
		{
			logger.error(e);
			throw new AppException("修改失败");
		}
	}
}
