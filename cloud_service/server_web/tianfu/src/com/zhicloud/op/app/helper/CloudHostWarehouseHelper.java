package com.zhicloud.op.app.helper;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;

import com.zhicloud.op.app.listener.WarehouseCloudHostCreationListener;
import com.zhicloud.op.common.util.CapacityUtil;
import com.zhicloud.op.common.util.DateUtil;
import com.zhicloud.op.common.util.FlowUtil;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.common.util.ThreadUtil;
import com.zhicloud.op.core.CoreSpringContextManager;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.httpGateway.HttpGatewayChannelExt;
import com.zhicloud.op.httpGateway.HttpGatewayManager;
import com.zhicloud.op.httpGateway.HttpGatewayResponseHelper;
import com.zhicloud.op.mybatis.mapper.CloudHostMapper;
import com.zhicloud.op.mybatis.mapper.CloudHostOpenPortMapper;
import com.zhicloud.op.mybatis.mapper.CloudHostSysDefaultPortsMapper;
import com.zhicloud.op.mybatis.mapper.CloudHostWarehouseDefinitionMapper;
import com.zhicloud.op.mybatis.mapper.CloudHostWarehouseDetailMapper;
import com.zhicloud.op.mybatis.mapper.SysDiskImageMapper;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.service.CloudHostWarehouseService;
import com.zhicloud.op.service.constant.AppConstant;
import com.zhicloud.op.vo.CloudHostSysDefaultPortsVO;
import com.zhicloud.op.vo.CloudHostVO;
import com.zhicloud.op.vo.CloudHostWarehouseDetailVO;
import com.zhicloud.op.vo.SysDiskImageVO;

public class CloudHostWarehouseHelper
{
	
	private static final Logger logger = Logger.getLogger(CloudHostWarehouseHelper.class);
	
	private SqlSession sqlSession;
	
	public CloudHostWarehouseHelper(SqlSession sqlSession)
	{
		this.sqlSession =  sqlSession;
	}
	
	/**
	 * @param sysDiskImageId: for finding warehouse cloud host
	 * @param dataDisk: for finding warehouse cloud host
	 */
	public CloudHostWarehouseDetailVO getWarehouseCloudHost(String sysImageId, Integer region) throws MalformedURLException, IOException
	{
		CloudHostWarehouseDetailMapper cloudHostWarehouseDetailMapper = this.sqlSession.getMapper(CloudHostWarehouseDetailMapper.class);
		
		// 查找仓库里的还没有分配云主机
		Map<String, Object> condition = new LinkedHashMap<String, Object>();
		condition.put("sysImageId", sysImageId);
		condition.put("region",     region);
		return cloudHostWarehouseDetailMapper.getOneUndistributedCloudHostFromWarehouse(condition);
	}

	/**
	 * @param sysDiskImageId: for finding warehouse cloud host
	 * @param dataDisk: for finding warehouse cloud host
	 */
	public List<CloudHostWarehouseDetailVO> getAllWarehouseCloudHost(String sysImageId, Integer region) throws MalformedURLException, IOException
	{
		CloudHostWarehouseDetailMapper cloudHostWarehouseDetailMapper = this.sqlSession.getMapper(CloudHostWarehouseDetailMapper.class);
		
		// 查找仓库里的还没有分配云主机
		Map<String, Object> condition = new LinkedHashMap<String, Object>();
		condition.put("sysImageId", sysImageId);
		condition.put("region",     region);
		return cloudHostWarehouseDetailMapper.getAllUndistributedCloudHostFromWarehouse(condition);
	}
	
	/**
	 * 
	 * @param sysDiskImageId: for finding warehouse cloud host
	 * @param dataDisk: for finding warehouse cloud host
	 * @param cloudHostData: need keys below:
	 * 		userId(String),
	 * 		hostName(String),
	 * 		account(String),
	 * 		password(String),
	 * 		cpuCore(Integer),
	 * 		memory(BigInteger),
	 * 		bandwidth(BigInteger),
	 * 		isAutoStartup(Integer),
	 * 		ports(Integer[]),
	 */
	public MethodResult allocateWarehouseCloudHost(CloudHostWarehouseDetailVO cloudHostWarehouseDetailVO, Map<String, Object> newCloudHostData) throws MalformedURLException, IOException
	{
		Integer type            = (Integer)newCloudHostData.get("type");
		String userId           = (String)newCloudHostData.get("userId");
//		String hostName         = (String)newCloudHostData.get("hostName");
		String hostName         = cloudHostWarehouseDetailVO.getHostName();
		String displayName         = (String)newCloudHostData.get("displayName");
		String account          = (String)newCloudHostData.get("account");
		String password         = (String)newCloudHostData.get("password");
		Integer cpuCore         = (Integer)newCloudHostData.get("cpuCore");
		BigInteger memory       = (BigInteger)newCloudHostData.get("memory");
		BigInteger dataDisk     = (BigInteger)newCloudHostData.get("dataDisk");
		BigInteger bandwidth    = (BigInteger)newCloudHostData.get("bandwidth");
		Integer isAutoStartup   = (Integer)newCloudHostData.get("isAutoStartup");
		BigDecimal monthlyPrice = new BigDecimal(newCloudHostData.get("monthlyPrice")+"");
		Integer[] ports         = (Integer[])newCloudHostData.get("ports");
		String item             = (String)newCloudHostData.get("item");
		
		if( userId==null )
		{
			throw new AppException("userId cannot be null.");
		}
		if( hostName==null )
		{
			throw new AppException("hostName cannot be null.");
		}
		if( account==null )
		{
			throw new AppException("account cannot be null.");
		}
		if( password==null )
		{
			throw new AppException("password cannot be null.");
		}
		if( cpuCore==null )
		{
			throw new AppException("cpuCore cannot be null.");
		}
		if( memory==null )
		{
			throw new AppException("memory cannot be null.");
		}
		if( dataDisk==null )
		{
			throw new AppException("dataDisk cannot be null.");
		}
		if( bandwidth==null )
		{
			throw new AppException("bandwidth cannot be null.");
		}
		if( isAutoStartup==null )
		{
			throw new AppException("isAutoStartup cannot be null.");
		}
		if( ports==null )
		{
			throw new AppException("ports cannot be null.");
		}
		
		HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(cloudHostWarehouseDetailVO.getRegion());

		// 修改云平台上的云主机的配置 
		JSONObject hostModifyResult = channel.hostModify(	cloudHostWarehouseDetailVO.getRealHostId(), 
															"",		 		// not modify host name first, modify after all is done
															cpuCore, 
															memory, 
															new Integer[]{0}, 
															ports, 
															account, 
															password, 
															"", 
															bandwidth, 
															bandwidth);
		
		if( HttpGatewayResponseHelper.isSuccess(hostModifyResult)==false )
		{
			logger.warn("CloudHostWarehouseHelper.allocateWarehouseCloudHost() > ["+Thread.currentThread().getId()+"] modify warehouse cloud host configuration failed, message:["+HttpGatewayResponseHelper.getMessage(hostModifyResult)+"]");
			return new MethodResult(MethodResult.FAIL, "modify warehouse cloud configuration failed");
		}

		logger.info("CloudHostWarehouseHelper.allocateWarehouseCloudHost() > ["+Thread.currentThread().getId()+"] modify warehouse cloud host configuration succeeded, message:["+HttpGatewayResponseHelper.getMessage(hostModifyResult)+"]");
		
		// 为云主机添加数据磁盘
		JSONObject attachDiskResult = channel.hostAttachDisk(cloudHostWarehouseDetailVO.getRealHostId(), dataDisk, 0, "", 0);

		if( HttpGatewayResponseHelper.isSuccess(attachDiskResult)==false )
		{
			logger.warn("CloudHostWarehouseHelper.allocateWarehouseCloudHost() > ["+Thread.currentThread().getId()+"] attach disk to warehouse cloud host failed, message:["+HttpGatewayResponseHelper.getMessage(attachDiskResult)+"]");
			return new MethodResult(MethodResult.FAIL, "attach disk to warehouse cloud failed");
		}

		logger.info("CloudHostWarehouseHelper.allocateWarehouseCloudHost() > ["+Thread.currentThread().getId()+"] attach disk to warehouse cloud host succeeded, message:["+HttpGatewayResponseHelper.getMessage(attachDiskResult)+"]");
		
		// 以上修改都成功，修改云平台上的云主机名 ,这一个步骤没有必要所以不执行
//		JSONObject hostModifyNameResult = channel.hostModifyName( cloudHostWarehouseDetailVO.getRealHostId(), hostName ,ports);
//		
//		if( HttpGatewayResponseHelper.isSuccess(hostModifyNameResult)==false )
//		{
//			logger.warn("CloudHostWarehouseHelper.allocateWarehouseCloudHost() > ["+Thread.currentThread().getId()+"] modify warehouse cloud host name failed, message:["+HttpGatewayResponseHelper.getMessage(hostModifyNameResult)+"]");
//			return new MethodResult(MethodResult.FAIL, "modify warehouse cloud name failed");
//		}
//
//		logger.info("CloudHostWarehouseHelper.allocateWarehouseCloudHost() > ["+Thread.currentThread().getId()+"] modify warehouse cloud host name succeeded, message:["+HttpGatewayResponseHelper.getMessage(hostModifyNameResult)+"]");		
		
		CloudHostMapper cloudHostMapper                                       = this.sqlSession.getMapper(CloudHostMapper.class);
		CloudHostWarehouseDefinitionMapper cloudHostWarehouseDefinitionMapper = this.sqlSession.getMapper(CloudHostWarehouseDefinitionMapper.class);
		CloudHostWarehouseDetailMapper cloudHostWarehouseDetailMapper         = this.sqlSession.getMapper(CloudHostWarehouseDetailMapper.class);
		CloudHostOpenPortMapper cloudHostOpenPortMapper                       = this.sqlSession.getMapper(CloudHostOpenPortMapper.class);
		CloudHostSysDefaultPortsMapper cloudHostSysDefaultPortsMapper = this.sqlSession.getMapper(CloudHostSysDefaultPortsMapper.class);

		
		CloudHostVO cloudHost = cloudHostMapper.getById(cloudHostWarehouseDetailVO.getHostId());
		// 将找到的云主机分配给用户
		Map<String, Object> newCloudHostData2 = new LinkedHashMap<String, Object>();
		newCloudHostData2.put("id",            cloudHostWarehouseDetailVO.getHostId());
		newCloudHostData2.put("type",          type);
		newCloudHostData2.put("userId",        userId);
		newCloudHostData2.put("hostName",      hostName);
		newCloudHostData2.put("displayName",      displayName);
		newCloudHostData2.put("account",       account);
		newCloudHostData2.put("password",      password);
		newCloudHostData2.put("cpuCore",       cpuCore);
		newCloudHostData2.put("memory",        memory);
		newCloudHostData2.put("bandwidth",     bandwidth);
		newCloudHostData2.put("dataDisk",     dataDisk);
		newCloudHostData2.put("isAutoStartup", isAutoStartup);
		newCloudHostData2.put("monthlyPrice",  monthlyPrice);
		newCloudHostData2.put("package_id",  item);
		
		int n = cloudHostMapper.distributeWarehouseCloudHostToUser(newCloudHostData2);
		if( n<=0 )
		{
			logger.warn("CloudHostWarehouseHelper.allocateWarehouseCloudHost() > ["+Thread.currentThread().getId()+"] distribute warehouse cloud host to user failed");
			return new MethodResult(MethodResult.FAIL, "allocate warehouse cloud host to user failed");
		}
		
		// 修改仓库云主机明细表上的状态字段
		Map<String, Object> detailStatusData = new LinkedHashMap<String, Object>();
		detailStatusData.put("status",         AppConstant.CLOUD_HOST_WAREHOUSE_DETAIL_STATUS_4_DISTRIBUTED);
		detailStatusData.put("processMessage", "allocated");
		detailStatusData.put("hostId",         cloudHostWarehouseDetailVO.getHostId());
		cloudHostWarehouseDetailMapper.updateStatusByHostId(detailStatusData);
		
		// 更新云主机仓库表的剩余库存数量
		if( StringUtil.isBlank(cloudHostWarehouseDetailVO.getWarehouseId())==false )
		{
			int remainAmount = cloudHostWarehouseDetailMapper.getRemainAmountByWarehouseId(cloudHostWarehouseDetailVO.getWarehouseId());
			Map<String, Object> updateRemainAmountByIdData = new LinkedHashMap<String, Object>();
			updateRemainAmountByIdData.put("remainAmount", remainAmount);
			updateRemainAmountByIdData.put("id",           cloudHostWarehouseDetailVO.getWarehouseId());
			cloudHostWarehouseDefinitionMapper.updateRemainAmountById(updateRemainAmountByIdData);
		}
		
		// 删除云主机的之前已有的开放端口
		cloudHostOpenPortMapper.deleteByHostId(cloudHostWarehouseDetailVO.getHostId());
		
		// 修改成功之后添加返回的云主机的端口信息
		JSONArray natList = hostModifyResult.getJSONArray("nat");
		for( int i=0; i<natList.size(); i+=4 )
		{
			Integer protocol   = natList.getInt(i);
			Integer serverPort = natList.getInt(i+1);
			Integer hostPort   = natList.getInt(i+2);
			Integer publicPort = natList.getInt(i+3);
			
			Map<String, Object> portCondition = new LinkedHashMap<String, Object>();
			Map<String, Object> cloudHostOpenPort = new LinkedHashMap<String, Object>();
			cloudHostOpenPort.put("id",         StringUtil.generateUUID());
			cloudHostOpenPort.put("hostId",     cloudHostWarehouseDetailVO.getHostId());
			cloudHostOpenPort.put("protocol",   protocol);
			cloudHostOpenPort.put("port",       hostPort);
			//获取服务名称
			portCondition.put("protocol", protocol);
			portCondition.put("port", hostPort);
			CloudHostSysDefaultPortsVO cloudHostSysDefaultPortsVO = cloudHostSysDefaultPortsMapper.getByProtocolAndPort(portCondition);
			if(cloudHostSysDefaultPortsVO != null){
				String name = cloudHostSysDefaultPortsVO.getName();
				cloudHostOpenPort.put("name", name);
			}
			
			cloudHostOpenPort.put("serverPort", serverPort);
			cloudHostOpenPort.put("outerPort",  publicPort);
			cloudHostOpenPortMapper.addCloudHostOpenPort(cloudHostOpenPort);
		}
		//库存增加一个
		CloudHostWarehouseService cloudHostWarehouseService = CoreSpringContextManager.getCloudHostWarehouseService();
		cloudHostWarehouseService.addACloudHostToWareById(cloudHostWarehouseDetailVO.getWarehouseId(),cloudHost.getUserId());
		
		return new MethodResult(MethodResult.SUCCESS, "添加成功");
	}
	
	/**
	 * 
	 * @param sysDiskImageId: for finding warehouse cloud host
	 * @param dataDisk: for finding warehouse cloud host
	 * @param cloudHostData: need keys below:
	 * 		userId(String),
	 * 		hostName(String),
	 * 		account(String),
	 * 		password(String),
	 * 		cpuCore(Integer),
	 * 		memory(BigInteger),
	 * 		dataDisk(BigInteger),
	 * 		bandwidth(BigInteger),
	 * 		isAutoStartup(Integer),
	 * 		ports(Integer[]),
	 */
	public MethodResult getAndAllocateWarehouseCloudHost(String sysImageId, Integer region, Map<String, Object> newCloudHostData) throws MalformedURLException, IOException
	{
		// get all the warehouse cloud host
		List<CloudHostWarehouseDetailVO> allCloudHostWarehouseDetailVOs = getAllWarehouseCloudHost(sysImageId, region);
		
		// check all the warehouse house iteratively
		Set<String> innerIpSet = new HashSet<String>();
		for( CloudHostWarehouseDetailVO cloudHostWarehouseDetailVO : allCloudHostWarehouseDetailVOs )
		{
			if( StringUtil.isBlank(cloudHostWarehouseDetailVO.getInnerIp()) )
			{
				continue;
			}
			if( innerIpSet.contains(cloudHostWarehouseDetailVO.getInnerIp()) )
			{
				// innerIpSet contains the inner ip means that the host server which cloud host reside in is checked already
				continue;
			}
			MethodResult result =  allocateWarehouseCloudHost(cloudHostWarehouseDetailVO, newCloudHostData);
			if( result.isSuccess() )
			{
				// if a warehouse cloud host can be found for allocating to user, return immediately
				result.put("CloudHostWarehouseDetailVO", cloudHostWarehouseDetailVO);
				
				return result;
			}
		}
		
		return new MethodResult(MethodResult.FAIL, "allocate warehouse cloud host to user success");
	}
	
//	/**
//	 * 
//	 */
//	public MethodResult createOneWarehouseCloudHostAndSleep(CloudHostWarehouseDetailVO cloudHostWarehouseDetailVO)
//	{
//		int intervalTime = WarehouseCloudHostCreationListener.instance.getIntervalTime();
//		return createOneWarehouseCloudHostAndSleep(cloudHostWarehouseDetailVO, intervalTime);
//	}
//
//	/**
//	 * 
//	 */
//	private static final Object warehouseCloudHostCreationLock = new Object();
//	
//	/**
//	 * 
//	 * @param cloudHostWarehouseDetailVO
//	 * @param intervalTime: 创建成功之后要sleep的时间，单位移
//	 * @return
//	 */
//	public MethodResult createOneWarehouseCloudHostAndSleep(CloudHostWarehouseDetailVO cloudHostWarehouseDetailVO)
//	{
//		synchronized (warehouseCloudHostCreationLock)
//		{
//			MethodResult result = createOneWarehouseCloudHost(cloudHostWarehouseDetailVO);
//			long sleepMills = 10 * 1000;	// sleep for 10 seconds
//			if( result.isSuccess() )
//			{
//				if( "success".equals(result.message) )
//				{
//					// 发送了一个创建云主机的消息，过60s再试
//					sleepMills = intervalTime * 1000;	// sleep for 60 seconds
//				}
//				if( "no_more_uncreated_warehouse_cloud_host".equals(result.message) )
//				{
//					// 已经没有未创建的仓库云主机，过10s再试
//					sleepMills = 10 * 1000;	// sleep for 10 seconds
//				}
//			}
//			ThreadUtil.sleep(sleepMills);
//			return result;
//		}
//	}
	
	/**
	 * 
	 */
	public MethodResult createOneWarehouseCloudHost(CloudHostWarehouseDetailVO cloudHostWarehouseDetailVO)
	{
		try
		{
			logger.info("CloudHostWarehouseHelper.createOneWarehouseCloudHost() > ["+Thread.currentThread().getId()+"] 创建一个库存云主机");
			if( cloudHostWarehouseDetailVO==null )
			{
				throw new AppException("cloudHostWarehouseDetailVO is null");
			}
			
			CloudHostWarehouseDetailMapper cloudHostWarehouseDetailMapper         = this.sqlSession.getMapper(CloudHostWarehouseDetailMapper.class);
			SysDiskImageMapper sysDiskImageMapper                                 = this.sqlSession.getMapper(SysDiskImageMapper.class);
			
			// 获取订单里的磁盘镜像的信息
			SysDiskImageVO sysDiskImageVO = sysDiskImageMapper.getById(cloudHostWarehouseDetailVO.getSysImageId());
			if( sysDiskImageVO==null )
			{
				logger.info("CloudHostWarehouseHelper.createOneWarehouseCloudHost() > ["+Thread.currentThread().getId()+"] sys_disk_image_not_found");
				// 磁盘镜像处理失败
				Map<String, Object> data = new LinkedHashMap<String, Object>();
				data.put("id",             cloudHostWarehouseDetailVO.getId());
				data.put("status",         AppConstant.CLOUD_HOST_WAREHOUSE_DETAIL_STATUS_2_FAIL);
				data.put("processMessage", "sys_disk_image_not_found");
				data.put("failTime", DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
				cloudHostWarehouseDetailMapper.updateStatusById(data);
				return new MethodResult(MethodResult.FAIL, "sys_disk_image_not_found");
			}
			if( StringUtil.isBlank(sysDiskImageVO.getRealImageId()) )
			{
				logger.info("CloudHostWarehouseHelper.createOneWarehouseCloudHost() > ["+Thread.currentThread().getId()+"] real_image_id_of_sys_disk_image_is_blank.");
				// 磁盘镜像处理失败
				Map<String, Object> data = new LinkedHashMap<String, Object>();
				data.put("id",             cloudHostWarehouseDetailVO.getId());
				data.put("status",         AppConstant.CLOUD_HOST_WAREHOUSE_DETAIL_STATUS_2_FAIL);
				data.put("failTime", DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
				data.put("processMessage", "real_image_id_of_sys_disk_image_is_blank");
				cloudHostWarehouseDetailMapper.updateStatusById(data);
				return new MethodResult(MethodResult.FAIL, "real_image_id_of_sys_disk_image_is_blank");
			}
			
			// 
			HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(cloudHostWarehouseDetailVO.getRegion());
			
			// 获取默认资源池
			JSONObject computePool = channel.getDefaultComputePool();
			if( computePool==null )
			{
				logger.info("CloudHostWarehouseHelper.createOneWarehouseCloudHost() > ["+Thread.currentThread().getId()+"] compute_pool_not_found");
				return new MethodResult(MethodResult.FAIL, "compute_pool_not_found");
			}
			
			// 发消息创建云主机
			JSONObject hostCreateResult = channel.hostCreate(
															cloudHostWarehouseDetailVO.getHostName(), 
															(String)computePool.get("uuid"), 
															1,												//cloudHostWarehouseDetailVO.getCpuCore(), 
															CapacityUtil.fromCapacityLabel("1GB"),			//cloudHostWarehouseDetailVO.getMemory(), 
															new Integer[]{1, 0, 0},							// options
															sysDiskImageVO.getRealImageId(), 
															new BigInteger[]{ 
																CapacityUtil.fromCapacityLabel("1GB")		// cloudHostWarehouseDetailVO.getSysDisk(), 
																// cloudHostWarehouseDetailVO.getDataDisk() 
															}, 
															new Integer[0], 
															"warehouse", 
															"warehouse",
															"warehouse", 
															"warehouse",									// password is same as account
															"", 
															FlowUtil.fromFlowLabel("4Mbps"),				// cloudHostWarehouseDetailVO.getBandwidth(), 
															FlowUtil.fromFlowLabel("4Mbps")					// cloudHostWarehouseDetailVO.getBandwidth()
															);
			
			if( HttpGatewayResponseHelper.isSuccess(hostCreateResult)==false )
			{
				logger.info("CloudHostWarehouseHelper.createOneWarehouseCloudHost() > ["+Thread.currentThread().getId()+"] create cloud host fail, http gateway return: "+HttpGatewayResponseHelper.getMessage(hostCreateResult));
				// 创建云主机失败
				Map<String, Object> data = new LinkedHashMap<String, Object>();
				data.put("id",             cloudHostWarehouseDetailVO.getId());
				data.put("status",         AppConstant.CLOUD_HOST_WAREHOUSE_DETAIL_STATUS_2_FAIL);
				data.put("processMessage", "http gateway return: "+HttpGatewayResponseHelper.getMessage(hostCreateResult));
				data.put("failTime", DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
				cloudHostWarehouseDetailMapper.updateStatusById(data);
				return new MethodResult(MethodResult.FAIL, "http gateway return: "+HttpGatewayResponseHelper.getMessage(hostCreateResult));
			}
			
			
			// 更新云主机配置的信息
			Map<String, Object> configData = new LinkedHashMap<String, Object>();
			configData.put("id",             cloudHostWarehouseDetailVO.getId());
			configData.put("status",         AppConstant.CLOUD_HOST_WAREHOUSE_DETAIL_STATUS_5_CREATING);
			configData.put("processMessage", "http gateway return: "+HttpGatewayResponseHelper.getMessage(hostCreateResult));
			cloudHostWarehouseDetailMapper.updateStatusById(configData);
			
			logger.info("CloudHostWarehouseHelper.createOneWarehouseCloudHost() > ["+Thread.currentThread().getId()+"] create cloud host suceeded, message:["+HttpGatewayResponseHelper.getMessage(hostCreateResult)+"]");
			
			return new MethodResult(MethodResult.SUCCESS, "success");
		}
		catch (AppException e)
		{ 
			logger.error("CloudHostWarehouseHelper.createOneWarehouseCloudHost() > ["+Thread.currentThread().getId()+"] ",e);
			throw new AppException("创建失败");
		}
		catch (Exception e)
		{ 
			logger.error("CloudHostWarehouseHelper.createOneWarehouseCloudHost() > ["+Thread.currentThread().getId()+"] ",e);
			throw new AppException("创建失败");
		}
	}
}
