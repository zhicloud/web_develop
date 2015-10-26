package com.zhicloud.op.service.impl;

import java.math.BigInteger;
import java.net.ConnectException;
import java.net.SocketException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.op.app.helper.CloudHostShoppingPortConfigHelper;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.httpGateway.HttpGatewayChannelExt;
import com.zhicloud.op.httpGateway.HttpGatewayManager;
import com.zhicloud.op.httpGateway.HttpGatewayResponseHelper;
import com.zhicloud.op.mybatis.mapper.CloudHostMapper;
import com.zhicloud.op.mybatis.mapper.CloudHostShoppingConfigMapper;
import com.zhicloud.op.mybatis.mapper.CloudHostShoppingPortConfigMapper;
import com.zhicloud.op.mybatis.mapper.OrderInfoMapper;
import com.zhicloud.op.mybatis.mapper.SysDiskImageMapper;
import com.zhicloud.op.mybatis.mapper.SysUserMapper;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.CallWithoutLogin;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.service.OrderInfoService;
import com.zhicloud.op.service.constant.AppConstant;
import com.zhicloud.op.vo.CloudHostShoppingConfigVO;
import com.zhicloud.op.vo.CloudHostVO;
import com.zhicloud.op.vo.OrderInfoVO;
import com.zhicloud.op.vo.SysDiskImageVO;
import com.zhicloud.op.vo.SysUserVO;

@Transactional(readOnly=true)
public class OrderInfoServiceImpl extends BeanDirectCallableDefaultImpl implements OrderInfoService
{
	
	public static final Logger logger = Logger.getLogger(OrderInfoServiceImpl.class);
	
	
	private SqlSession sqlSession;
	
	
	public void setSqlSession(SqlSession sqlSession)
	{
		this.sqlSession = sqlSession;
	}

	//---------------
	
	@Callable
	@CallWithoutLogin
	@Transactional(readOnly=false)
	public MethodResult createOneUserOrderedCloudHost(int region)
	{
		try
		{
			OrderInfoMapper orderInfoMapper                                     = this.sqlSession.getMapper(OrderInfoMapper.class);
			CloudHostShoppingConfigMapper cloudHostShoppingConfigMapper         = this.sqlSession.getMapper(CloudHostShoppingConfigMapper.class);
			CloudHostShoppingPortConfigMapper cloudHostShoppingPortConfigMapper = this.sqlSession.getMapper(CloudHostShoppingPortConfigMapper.class);
			SysUserMapper sysUserMapper                                         = this.sqlSession.getMapper(SysUserMapper.class);
			SysDiskImageMapper sysDiskImageMapper                               = this.sqlSession.getMapper(SysDiskImageMapper.class);
			CloudHostMapper cloudHostMapper                                     = this.sqlSession.getMapper(CloudHostMapper.class);
			
			// 获取未处理的订单
			OrderInfoVO orderInfo = orderInfoMapper.getOneUnprocessedOrderByRegion(region);
			if( orderInfo==null )
			{
				logger.info("OrderInfoServiceImpl.createOneUserOrderedCloudHost(): ["+Thread.currentThread().getId()+"] no_order_unprocessed");
				return new MethodResult(MethodResult.SUCCESS, "no_order_unprocessed");
			}else {
				logger.info("OrderInfoServiceImpl.createOneUserOrderedCloudHost(): ["+Thread.currentThread().getId()+"] user_not_found");
				Map<String, Object> data = new LinkedHashMap<String, Object>();
				data.put("id",             orderInfo.getId());
				data.put("processStatus",  AppConstant.ORDER_INFO_PROCESS_STATUS_PENDING);
				data.put("processMessage", "on_process");
				int i = orderInfoMapper.updateProcessStatusToPendingById(data);
				logger.info("+++++++++更新结果是+++++++"+i+"++++++++++++++++++++++");
				if(i<=0){
					return new MethodResult(MethodResult.SUCCESS, "the_order_is_processed");					
				}
			}

			// 获取订单的用户信息
			SysUserVO user = sysUserMapper.getById(orderInfo.getUserId());
			if( user==null )
			{
				logger.info("OrderInfoServiceImpl.createOneUserOrderedCloudHost(): ["+Thread.currentThread().getId()+"] user_not_found");
				Map<String, Object> data = new LinkedHashMap<String, Object>();
				data.put("id",             orderInfo.getId());
				data.put("processStatus",  AppConstant.ORDER_INFO_PROCESS_STATUS_FAIL);
				data.put("processMessage", "user_not_found");
				orderInfoMapper.updateProcessStatusById(data);
				return new MethodResult(MethodResult.FAIL, "user_not_found");
			} 
			
			
			// 获取订单里的一个未创建的云主机的配置
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("orderId", orderInfo.getId());
			condition.put("region",  region);
			CloudHostShoppingConfigVO cloudHostShoppingConfigVO = cloudHostShoppingConfigMapper.getOneUncreatedCloudHostConfigByOrderIdAndRegion(condition);
			if( cloudHostShoppingConfigVO==null )
			{
				logger.info("OrderInfoServiceImpl.createOneUserOrderedCloudHost(): ["+Thread.currentThread().getId()+"] no_cloud_host_config_unprocessed");
				// 表示这个订单的所有云主机都已经处理
				Map<String, Object> data = new LinkedHashMap<String, Object>();
				data.put("id",             orderInfo.getId());
				data.put("processStatus",  AppConstant.ORDER_INFO_PROCESS_STATUS_SUCCESS);
				data.put("processMessage", "all_done");
				orderInfoMapper.updateProcessStatusById(data);
				return new MethodResult(MethodResult.SUCCESS, "no_cloud_host_config_unprocessed");
			}
			
			// 获取订单里的磁盘镜像的信息
			SysDiskImageVO sysDiskImageVO = sysDiskImageMapper.getById(cloudHostShoppingConfigVO.getSysImageId());
			if( sysDiskImageVO==null )
			{
				logger.info("OrderInfoServiceImpl.createOneUserOrderedCloudHost(): ["+Thread.currentThread().getId()+"] sys_disk_image_not_found");
				// 磁盘镜像处理失败
				Map<String, Object> data = new LinkedHashMap<String, Object>();
				data.put("id",             cloudHostShoppingConfigVO.getId());
				data.put("processStatus",  AppConstant.CLOUD_HOST_SHOPPING_CONFIG_PROCESS_STATUS_FAIL);
				data.put("processMessage", "sys_disk_image_not_found");
				cloudHostShoppingConfigMapper.updateProcessStatusById(data);
				return new MethodResult(MethodResult.FAIL, "sys_disk_image_not_found");
			}
			if( StringUtil.isBlank(sysDiskImageVO.getRealImageId()) )
			{
				logger.info("OrderInfoServiceImpl.createOneUserOrderedCloudHost(): ["+Thread.currentThread().getId()+"] real_image_id_of_sys_disk_image_is_blank.");
				// 磁盘镜像处理失败
				Map<String, Object> data = new LinkedHashMap<String, Object>();
				data.put("id",             cloudHostShoppingConfigVO.getId());
				data.put("processStatus",  AppConstant.CLOUD_HOST_SHOPPING_CONFIG_PROCESS_STATUS_FAIL);
				data.put("processMessage", "real_image_id_of_sys_disk_image_is_blank");
				cloudHostShoppingConfigMapper.updateProcessStatusById(data);
				return new MethodResult(MethodResult.FAIL, "real_image_id_of_sys_disk_image_is_blank");
			}
			
			// 从云主机仓库获取云主机失败，那么直接创建云主机
			HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(region);
			
			// 获取默认资源池
			JSONObject computePool = channel.getDefaultComputePool();
			if( computePool==null )
			{
				logger.warn("OrderInfoServiceImpl.createOneUserOrderedCloudHost(): ["+Thread.currentThread().getId()+"] compute_pool_not_found");
				return new MethodResult(MethodResult.FAIL, "compute_pool_not_found");
			}
			
			CloudHostVO cloudHost = cloudHostMapper.getById(cloudHostShoppingConfigVO.getHostId());
			
			// 发消息创建云主机
			JSONObject hostCreateResult = channel.hostCreate(
															cloudHostShoppingConfigVO.getHostName(), 
															(String)computePool.get("uuid"), 
															cloudHostShoppingConfigVO.getCpuCore(), 
															cloudHostShoppingConfigVO.getMemory(), 
															new Integer[]{1, 1, 0},		// options
															sysDiskImageVO.getRealImageId(), 
															new BigInteger[]{ cloudHostShoppingConfigVO.getSysDisk(), cloudHostShoppingConfigVO.getDataDisk() }, 
															CloudHostShoppingPortConfigHelper.getPortsByConfigId(cloudHostShoppingPortConfigMapper, cloudHostShoppingConfigVO.getId()), 
															orderInfo.getUserId(), 
															"terminal_user",
															user.getAccount(), 
															cloudHost.getPassword(),	// 获取之前创建的16位随机密码
															"", 
															cloudHostShoppingConfigVO.getBandwidth(), 
															cloudHostShoppingConfigVO.getBandwidth()
															);
			
			if( HttpGatewayResponseHelper.isSuccess(hostCreateResult)==false )
			{
				logger.info("OrderInfoServiceImpl.createOneUserOrderedCloudHost(): ["+Thread.currentThread().getId()+"] create cloud host fail, http gateway return: "+HttpGatewayResponseHelper.getMessage(hostCreateResult));
				// 创建云主机失败
				Map<String, Object> data = new LinkedHashMap<String, Object>();
				data.put("id",             cloudHostShoppingConfigVO.getId());
				data.put("processStatus",  AppConstant.CLOUD_HOST_SHOPPING_CONFIG_PROCESS_STATUS_FAIL);
				data.put("processMessage", "http gateway return: "+HttpGatewayResponseHelper.getMessage(hostCreateResult));
				cloudHostShoppingConfigMapper.updateProcessStatusById(data);
				return new MethodResult(MethodResult.FAIL, "http gateway return: "+HttpGatewayResponseHelper.getMessage(hostCreateResult));
			}else {
				logger.info("OrderInfoServiceImpl.createOneUserOrderedCloudHost(): ["+Thread.currentThread().getId()+"] user_not_found");
				Map<String, Object> data = new LinkedHashMap<String, Object>();
				data.put("id",             orderInfo.getId());
				data.put("processStatus",  AppConstant.ORDER_INFO_PROCESS_STATUS_SUCCESS);
				data.put("processMessage", "is_done");
				orderInfoMapper.updateProcessStatusById(data);
			}
			
			// 更新云主机配置的信息
			Map<String, Object> configData = new LinkedHashMap<String, Object>();
			configData.put("id",             cloudHostShoppingConfigVO.getId());
			configData.put("processStatus",  AppConstant.CLOUD_HOST_SHOPPING_CONFIG_PROCESS_STATUS_CREATING);
			configData.put("processMessage", "http gateway return: "+HttpGatewayResponseHelper.getMessage(hostCreateResult));
			cloudHostShoppingConfigMapper.updateProcessStatusById(configData);
			//更新创建时间，用于删除一直处于创建中的云主机
			Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
			cloudHostData.put("id",cloudHostShoppingConfigVO.getHostId());
			cloudHostData.put("createTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
			cloudHostMapper.updateCreateTimeById(cloudHostData);
			logger.info("OrderInfoServiceImpl.createOneUserOrderedCloudHost(): ["+Thread.currentThread().getId()+"] create cloud host suceeded, message:["+HttpGatewayResponseHelper.getMessage(hostCreateResult)+"]");
			
			return new MethodResult(MethodResult.SUCCESS, "success");
		}
		catch (SocketException e)
		{
			logger.error("OrderInfoServiceImpl.createOneUserOrderedCloudHost(): ["+Thread.currentThread().getId()+"] connect to http gateway failed, exception:["+e.getMessage()+"], region:["+region+"]");
			return new MethodResult(MethodResult.FAIL, "failed_to_connect_http_gateway");
		}
		catch (Exception e)
		{
			throw AppException.wrapException(e);
		}
	}
	

}

















