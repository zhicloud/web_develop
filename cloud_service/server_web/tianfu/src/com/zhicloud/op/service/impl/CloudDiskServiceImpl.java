package com.zhicloud.op.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.ConnectException;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.op.app.authorization.PrivilegeConstant;
import com.zhicloud.op.app.helper.CloudHostBillingHelper;
import com.zhicloud.op.app.helper.LoginHelper;
import com.zhicloud.op.app.pool.CloudHostPoolManager;
import com.zhicloud.op.app.propeties.AppProperties;
import com.zhicloud.op.common.util.CapacityUtil;
import com.zhicloud.op.common.util.DateUtil;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.common.util.json.JSONLibUtil;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.httpGateway.HttpGatewayChannel;
import com.zhicloud.op.httpGateway.HttpGatewayChannelExt;
import com.zhicloud.op.httpGateway.HttpGatewayManager;
import com.zhicloud.op.httpGateway.HttpGatewayResponseHelper;
import com.zhicloud.op.login.LoginInfo;
import com.zhicloud.op.mybatis.mapper.CloudDiskMapper;
import com.zhicloud.op.mybatis.mapper.CloudDiskShoppingConfigMapper;
import com.zhicloud.op.mybatis.mapper.CloudHostBillDetailMapper;
import com.zhicloud.op.mybatis.mapper.CloudHostMapper;
import com.zhicloud.op.mybatis.mapper.CloudHostOpenPortMapper;
import com.zhicloud.op.mybatis.mapper.OrderDetailMapper;
import com.zhicloud.op.mybatis.mapper.OrderInfoMapper;
import com.zhicloud.op.mybatis.mapper.SysUserMapper;
import com.zhicloud.op.mybatis.mapper.TerminalUserMapper;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.CallWithoutLogin;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.request.RequestContext;
import com.zhicloud.op.service.CloudDiskService;
import com.zhicloud.op.service.constant.AppConstant;
import com.zhicloud.op.vo.CloudDiskVO;
import com.zhicloud.op.vo.CloudHostBillDetailVO;
import com.zhicloud.op.vo.CloudHostOpenPortVO;
import com.zhicloud.op.vo.CloudHostVO;
import com.zhicloud.op.vo.SysUserVO;
import com.zhicloud.op.vo.TerminalUserVO;

@Transactional(readOnly = true)
public class CloudDiskServiceImpl extends BeanDirectCallableDefaultImpl implements CloudDiskService {
	
	public static final Logger logger = Logger.getLogger(CloudHostServiceImpl.class);

	private SqlSession sqlSession;
	
	public void setSqlSession(SqlSession sqlSession)
	{
		this.sqlSession = sqlSession;
	}

	/**
	 * 我的云盘
	 */
	@Callable
	public String managePage(HttpServletRequest request, 	HttpServletResponse response) {
		logger.debug("CloudDiskServiceImpl.managePage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request,  AppConstant.SYS_USER_TYPE_TERMINAL_USER);
		if( AppConstant.SYS_USER_TYPE_TERMINAL_USER.equals(loginInfo.getUserType())==false )
		{
			return "/public/have_not_access.jsp";
		}
		String userId = loginInfo.getUserId();
		String region = request.getParameter("region");
		String newRegion = region;
		if("0".equals(region)){
			newRegion = null;
		}
		CloudDiskMapper cloudDiskMapper = this.sqlSession.getMapper(CloudDiskMapper.class);
		Map<String, Object> condition = new LinkedHashMap<String, Object>();
		condition.put("region", newRegion);
		condition.put("userId", userId);
		List<CloudDiskVO> cloudDiskList = cloudDiskMapper.queryPage(condition);// 分页结果
		if(region == null){
			region = "0";
		}
		request.setAttribute("cloudDiskList", cloudDiskList);
		request.setAttribute("region", region);
		return "/security/user/my_cloud_disk_manage.jsp";
	}
	@CallWithoutLogin
	@Callable
	public String addPage(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("CloudDiskServiceImpl.addPage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request,  AppConstant.SYS_USER_TYPE_TERMINAL_USER);
//		if( AppConstant.SYS_USER_TYPE_TERMINAL_USER.equals(loginInfo.getUserType())==false )
//		{
//			return "/public/have_not_access.jsp";
//		}
		if(loginInfo!=null){
			CloudHostMapper  cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class); 
			List<CloudHostVO> cloudHostList = cloudHostMapper.getByUserId(loginInfo.getUserId());
			BigDecimal totalPrice = new BigDecimal("0");
			if(cloudHostList!=null&&cloudHostList.size()>0){
				for(CloudHostVO vo:cloudHostList){
					if(!(vo.getSummarizedStatusText().equals("创建失败")||vo.getSummarizedStatusText().equals("停机"))&&vo.getMonthlyPrice()!=null){						
						totalPrice = vo.getMonthlyPrice().add(totalPrice);
					}
				}
				request.setAttribute("totalPrice", totalPrice);			
			}
		}
		return "/security/user/create_storage.jsp";
	}
	
	@Callable
	public String modPage(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("CloudDiskServiceImpl.modPage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request,  AppConstant.SYS_USER_TYPE_TERMINAL_USER);
		if( AppConstant.SYS_USER_TYPE_TERMINAL_USER.equals(loginInfo.getUserType())==false )
		{
			return "/public/have_not_access.jsp";
		}
		// 参数处理
		String id = StringUtil.trim(request.getParameter("diskId"));
		if( StringUtil.isBlank(id) )
		{
			throw new AppException("id不能为空");
		}
		
		CloudDiskMapper cloudDiskMapper = this.sqlSession.getMapper(CloudDiskMapper.class);
		CloudDiskVO cloudDiskVO =cloudDiskMapper.getCloudDiskById(id);
		
		request.setAttribute("cloudDiskVO", cloudDiskVO);
		return "/security/user/edit_storage.jsp";
	}

	/**
	 * 查看详情页面
	 */
	@Callable
	public String cloudDiskDetailPage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("CloudDiskServiceImpl.cloudDiskDetailPage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_OPERATOR && loginInfo.hasPrivilege(PrivilegeConstant.cloud_host_view_detail) == false )
		{
			return "/public/have_not_access_dialog.jsp";
		}
		else if(loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT && loginInfo.hasPrivilege(PrivilegeConstant.cloud_host_view_detail_agent) == false)
		{
			return "/public/have_not_access_dialog.jsp";
		}
		
		CloudDiskMapper cloudDiskMapper = this.sqlSession.getMapper(CloudDiskMapper.class);
		CloudHostOpenPortMapper cloudHostOpenPortMapper = this.sqlSession.getMapper(CloudHostOpenPortMapper.class);
		
		// 参数处理
		String cloudDiskId = StringUtil.trim(request.getParameter("cloudDiskId"));
		if( StringUtil.isBlank(cloudDiskId) )
		{
			throw new AppException("cloudDiskId不能为空");
		}
		CloudDiskVO cloudDisk = cloudDiskMapper.getCloudDiskById(cloudDiskId);
		request.setAttribute("cloudDisk", cloudDisk);
		if( cloudDisk==null )
		{
			request.setAttribute("message", "找不到云主机信息");
			return "/public/warning_dialog.jsp";
		}
		
		// 创建成功，转到查看云主机详情页面
//		if( cloudHost.getRealHostId()!=null || AppConstant.CLOUD_HOST_SHOPPING_CONFIG_PROCESS_STATUS_SUCCESS.equals(cloudHost.getProcessStatus()) )
//		{
			List<CloudHostOpenPortVO> ports = cloudHostOpenPortMapper.getByHostId(cloudDiskId);
			request.setAttribute("ports", ports);
			if( loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT )
			{
				return "/security/agent/cloud_host_view_detail.jsp";
			}
			else if(loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_OPERATOR)
			{
				return "/security/operator/cloud_host_view_detail.jsp";
			}
			else
			{
				return "/security/user/storage_diagram.jsp";
			}
//		}
		// 非创建成功，转到查看创建进度的页面
//		else
//		{
//			try
//			{
//				MethodResult creationResult = getCloudHostCreationResult(cloudHostId);
//				request.setAttribute("progress",        creationResult.get("progress"));
//				request.setAttribute("creation_status", creationResult.get("creation_status"));
//				request.setAttribute("creation_result", creationResult.get("creation_result"));
//				return "/security/operator/cloud_host_creation_progress.jsp";
//			}
//			catch (Exception e)
//			{
//				request.setAttribute("message", e.getMessage());
//				return "/public/warning_dialog.jsp";
//			}
//		}
		
	}
	@Callable
	public void queryCloudDisk(HttpServletRequest request,HttpServletResponse response) {
		logger.debug("CloudDiskServiceImpl.queryCloudDisk()");
		try{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");
			
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request,  AppConstant.SYS_USER_TYPE_TERMINAL_USER);

			// 获取参数
			String region = StringUtil.trim(request.getParameter("region"));
			String userId = loginInfo.getUserId();
//			Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
//			Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 10);
			
			if(StringUtil.isBlank(region)){
				region = null;
			}

			// 查询数据库
			CloudDiskMapper cloudDiskMapper = this.sqlSession.getMapper(CloudDiskMapper.class);
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("region", region);
			condition.put("userId", userId);
//			condition.put("start_row", page * rows);
//			condition.put("row_count", rows);
//			int total = cloudDiskMapper.queryPageCount(condition); // 总行数
			List<CloudDiskVO> cloudDiskList = cloudDiskMapper.queryPage(condition);// 分页结果
			
			// 输出json数据
//			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, cloudDiskList);
			request.setAttribute("cloudDiskList", cloudDiskList);
		}catch( Exception e )
		{
			logger.error("CloudDiskServiceImpl.queryCloudDisk()",e); 
			throw new AppException("查询失败");
		}
		
	}

	@Callable
	@Transactional(readOnly = false)
	public MethodResult addCloudDisk(Map<String, String> parameter) {
		logger.debug("CloudDiskServiceImpl.addCloudDisk()");
		try
		{
			HttpServletRequest request = RequestContext.getHttpRequest();
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			// 权限判断
			String disk = "0";
			String diskDIY = StringUtil.trim(parameter.get("diskDIY"));
			String diskName = StringUtil.trim(parameter.get("disk_name"));
			String userId  = loginInfo.getUserId();
			String account = loginInfo.getAccount();
			String region  = StringUtil.trim(parameter.get("region"));
			if(StringUtil.isBlank(diskDIY)){
				disk = StringUtil.trim(parameter.get("disk"));
			}else{
				disk = diskDIY;    
			}
			BigInteger realDisk     = CapacityUtil.fromCapacityLabel(disk+"GB");
			String price = "0";
			
			if(StringUtil.isBlank(userId)){
				return new MethodResult(MethodResult.FAIL,"userId不能为空");
			}
			if(StringUtil.isBlank(region)){
				return new MethodResult(MethodResult.FAIL,"地域不能为空");
			}
			if(StringUtil.isBlank(disk)){
				return new MethodResult(MethodResult.FAIL,"硬盘容量不能为空");
			}
			
			// 发消息到http gateway
			HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(Integer.parseInt(region));
			// 获取默认存储资源池
 			JSONObject  pool = channel.storagePoolQuery();
			if (pool == null) {
				logger.info("CloudHostServiceImpl.addCloudHost() > [" + Thread.currentThread().getId() + "] 无法获取资源池");
				throw new AppException("无法获取资源池");
			}
			JSONArray storagePools = (JSONArray) pool.get("storagePools");
			if (storagePools == null) {
				logger.info("CloudHostServiceImpl.addCloudHost() > [" + Thread.currentThread().getId() + "] 无法获取资源池");
				throw new AppException("无法获取资源池");
			}
			JSONObject storagePool = (JSONObject) storagePools.get(0);
 			String realDiskId = null; 
 
			JSONObject result = channel.deviceCreate(diskName,(String) storagePool.get("uuid"), realDisk, 4096, 0, new Integer[] { 0, 0, 0, 0, 0, 0 }, 0, loginInfo.getAccount(), loginInfo.getAccount(), "", "");
			if (HttpGatewayResponseHelper.isSuccess(result) == false) {
				logger.warn("CloudDiskServiceImpl.addCloudDisk() > [" + Thread.currentThread().getId() + "] create disk '" + diskName + "' failed, message:[" + HttpGatewayResponseHelper.getMessage(result) + "]");
				throw new AppException("http gateway创建硬盘失败");
			}
			//返回硬盘uuid
			realDiskId = JSONLibUtil.getString(result, "uuid"); 
			JSONObject diskQueryResult = channel.deviceQuery((String) storagePool.get("uuid"));
			if (HttpGatewayResponseHelper.isSuccess(diskQueryResult) == false) {// 失败则跳过该region
				logger.info(String.format("fail to query disk from http gateway. region[%s], message[%s]"));
				throw new AppException("http gateway创建硬盘失败");
 			}
			JSONArray devices = (JSONArray) diskQueryResult.get("devices");
			for (int i = 0; i < devices.size(); i++) {
				JSONObject device = (JSONObject) devices.get(i);
				String uuid = JSONLibUtil.getString(device, "uuid"); 
				String identify = JSONLibUtil.getString(device, "identity"); 
				if(realDiskId.equals(uuid)){
					
					CloudDiskMapper cloudDiskMapper = this.sqlSession.getMapper(CloudDiskMapper.class);
					 
					String id = StringUtil.generateUUID();
					String orderId = StringUtil.generateUUID();
					String time = StringUtil.dateToString(new Date(), "yyyy-MM-dd HH:mm");
					
					//创建云硬盘
					Map<String, Object> data = new LinkedHashMap<String, Object>();
					data.put("id", id);
					data.put("name", diskName);
					data.put("realDiskId", realDiskId);
					data.put("userId",  userId);
					data.put("account", account);
					data.put("password", account);
					data.put("disk", realDisk);
					data.put("runningStatus", AppConstant.CLOUD_DISK_RUNNING_STATUS_RUNNING);
					data.put("status", AppConstant.CLOUD_DISK_STATUS_NORMAL);
					data.put("createTime", time);
					data.put("region", region);
					data.put("iqn", identify);
					data.put("ip", AppProperties.getValue("address_of_iscsi_gateway"));


					int cloudDiskInsertResult = cloudDiskMapper.addCloudDisk(data);

					 
					
					if(  cloudDiskInsertResult > 0)
					{
						return new MethodResult(MethodResult.SUCCESS, "创建成功");
					}
					else
					{
						return new MethodResult(MethodResult.FAIL, "创建失败");
					}
					
				} 
			}
			return new MethodResult(MethodResult.FAIL, "创建失败");
			
			
 			

		}
		catch( Exception e )
		{
			logger.error("CloudDiskServiceImpl.addCloudDisk()",e);
			throw new AppException("创建失败");
		}
	}


	@Callable
	@Transactional(readOnly = false)
	public MethodResult updateCloudDiskById(Map<String, String> parameter) {
		logger.debug("CloudDiskServiceImpl.updateCloudDiskById()");
		try
		{
			
			// 参数处理
			String disk = "0";
			String diskDIY = StringUtil.trim(parameter.get("diskDIY"));
			String id = StringUtil.trim(parameter.get("id"));
			if(StringUtil.isBlank(diskDIY)){
				disk = StringUtil.trim(parameter.get("disk"));
			}else{
				disk = diskDIY;    
			}
			BigInteger realDisk     = CapacityUtil.fromCapacityLabel(disk+"GB");
			if(id==null || id==""){
				return new MethodResult(MethodResult.FAIL,"id不能为空");
			}
			if(disk==null || disk==""){
				return new MethodResult(MethodResult.FAIL,"不能为空");
			}

			CloudDiskMapper cloudDiskMapper = this.sqlSession.getMapper(CloudDiskMapper.class);
			
			int cloudDiskUpdateResult = 0;
			
			// 更新cloud_disk
			Map<String, Object> data = new LinkedHashMap<String, Object>();
			data.put("id",       id);
			data.put("disk",  realDisk);

			cloudDiskUpdateResult = cloudDiskMapper.updateCloudDisk(data);
					
		
			
			if(cloudDiskUpdateResult > 0 ){
				return new MethodResult(MethodResult.SUCCESS, "修改成功");
			}

			return new MethodResult(MethodResult.FAIL, "修改失败");
		}
		catch( Exception e )
		{
			logger.error("CloudDiskServiceImpl.updateCloudDiskById()",e);
			throw new AppException("修改失败");
		}
	}

	@Callable
	@Transactional(readOnly = false)
	public MethodResult deleteCloudDiskById(String id) {
		logger.debug("CloudDiskServiceImpl.deleteOperatorById()");
		try
		{
			if( id== null)
			{
				return new MethodResult(MethodResult.FAIL, "id不能为空");
			}
			CloudDiskMapper cloudDiskMapper = this.sqlSession.getMapper(CloudDiskMapper.class);
			CloudDiskVO vo = cloudDiskMapper.getCloudDiskById(id);
			if( vo == null ){
				return new MethodResult(MethodResult.FAIL, "未查询到");				
			}
			HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(vo.getRegion());
			JSONObject result = channel.deviceDelete(vo.getRealDiskId());
 			if (HttpGatewayResponseHelper.isSuccess(result) == false) {
				logger.warn("CloudDiskServiceImpl.deleteCloudDiskById() > [" + Thread.currentThread().getId() + "] delete disk '" + vo.getName() + "' failed, message:[" + HttpGatewayResponseHelper.getMessage(result) + "]");
				throw new AppException("http gateway删除硬盘失败");
			}
			//删除数据库硬盘数据
			 
			int deleteResult = cloudDiskMapper.deleteCloudDiskById(id);

			if( deleteResult > 0)
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
			logger.error("CloudDiskServiceImpl.deleteCloudDiskById()",e);
			throw new AppException("删除失败");
		}
	}
	@CallWithoutLogin
	@Callable
	public MethodResult checkInvitationCode(String diskInvitationCode) {
		logger.debug("CloudDiskServiceImpl.checkInvitationCode()");
		try
		{
			if( diskInvitationCode == null || diskInvitationCode == "")
			{
				return new MethodResult(MethodResult.FAIL, "邀请码不能为空");
			}
			if("cloudZhi".equals(diskInvitationCode)){
				return new MethodResult(MethodResult.SUCCESS, "邀请码正确");
			}
				return new MethodResult(MethodResult.FAIL, "请输入正确的邀请码");
		}
		catch( Exception e )
		{
			logger.error("CloudDiskServiceImpl.deleteCloudDiskById()",e);
			throw new AppException(e);
		}
	}	

	/**
	 * 申请停机
	 */
	@Callable
	@Transactional(readOnly=false)
	public MethodResult inactivateCloudDisk(String diskId)
	{
		try
		{
			if( StringUtil.isBlank(diskId) )
			{
				throw new AppException("diskId不能为空");
			}
			
			CloudDiskMapper cloudDiskMapper = this.sqlSession.getMapper(CloudDiskMapper.class);

			CloudDiskVO cloudDiskVO = cloudDiskMapper.getCloudDiskById(diskId);
			if( cloudDiskVO==null )
			{
				throw new AppException("cloud disk not found with id["+diskId+"]");
			}
			if( StringUtil.isBlank(cloudDiskVO.getRealDiskId()) )
			{
				return new MethodResult(MethodResult.FAIL, "云硬盘还没创建成功，无法申请停机");
			}
			if( AppConstant.CLOUD_DISK_STATUS_HALT.equals(cloudDiskVO.getStatus()) )
			{
				return new MethodResult(MethodResult.FAIL, "该云硬盘当前已经是‘停用’状态");
			}
			if( AppConstant.CLOUD_DISK_STATUS_HALT_FOREVER.equals(cloudDiskVO.getStatus()) )
			{
				return new MethodResult(MethodResult.FAIL, "云硬盘已经永久停用，无法申请停机恢复");
			}
			
			Date now = new Date();
			
			// 结算该云主机所有未完成的账单
//			new CloudHostBillingHelper(sqlSession).settleAllUndoneCloudHostBills(diskId, now, false);
			
			// 修改云主机的状态
			Map<String, Object> cloudDiskData = new LinkedHashMap<String, Object>();
			cloudDiskData.put("status",         AppConstant.CLOUD_DISK_STATUS_HALT);
			cloudDiskData.put("inactivateTime", DateUtil.dateToString(now, "yyyyMMddHHmmssSSS"));
			cloudDiskData.put("id",             diskId);
			int n = cloudDiskMapper.updateStatusById(cloudDiskData);
			if( n<=0 )
			{
				return new MethodResult(MethodResult.FAIL, "申请停机失败");
			}
			
			// 发消息到http gateway进行关机
//			HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(cloudHostVO.getRegion());
//			JSONObject hostHaltResult = channel.hostHalt(cloudHostVO.getRealHostId());
//			if( HttpGatewayResponseHelper.isSuccess(hostHaltResult)==false )
//			{
//				logger.warn("CloudHostServiceImpl.inactivateCloudHost() > ["+Thread.currentThread().getId()+"] halt cloud host failed, message:["+HttpGatewayResponseHelper.getMessage(hostHaltResult)+"]");
//			}
//			else
//			{
//				logger.info("CloudHostServiceImpl.inactivateCloudHost() > ["+Thread.currentThread().getId()+"] halt cloud host succeeded, message:["+HttpGatewayResponseHelper.getMessage(hostHaltResult)+"]");
//			}
			
			return new MethodResult(MethodResult.SUCCESS, "停机成功");
		}
//		catch (ConnectException e)
//		{
////			throw new AppException("连接http gateway失败", e);
//			logger.error(e);
//			throw new AppException("停机失败");
//		}
		catch (AppException e)
		{ 
			logger.error(e);
			throw new AppException("停机失败");
		}
		catch (Exception e)
		{
			logger.error(e);
			throw new AppException("停机失败");
		}
	}
	
	/**
	 * 申请停机恢复
	 */
	@Callable
	@Transactional(readOnly=false)
	public MethodResult reactivateCloudDisk(String diskId)
	{
		try
		{
			if( StringUtil.isBlank(diskId) )
			{
				throw new AppException("diskId不能为空");
			}
			
			CloudDiskMapper    cloudDiskMapper    = this.sqlSession.getMapper(CloudDiskMapper.class);
			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
			
			// 获取云主机信息
			CloudDiskVO cloudDiskVO = cloudDiskMapper.getCloudDiskById(diskId);
			if( cloudDiskVO==null )
			{
				throw new AppException("cloud disk not found with id["+diskId+"]");
			}
			
			// 判断云主机的状态
			if( StringUtil.isBlank(cloudDiskVO.getRealDiskId()) )
			{
				return new MethodResult(MethodResult.FAIL, "云硬盘还没创建成功，无法申请停机恢复");
			}
			if( AppConstant.CLOUD_DISK_STATUS_NORMAL.equals(cloudDiskVO.getStatus()) )
			{
				return new MethodResult(MethodResult.FAIL, "该云硬盘当前已经是‘正常’状态，无需申请停机恢复");
			}
			if( AppConstant.CLOUD_DISK_STATUS_HALT_FOREVER.equals(cloudDiskVO.getStatus()) )
			{
				return new MethodResult(MethodResult.FAIL, "云硬盘已经永久停机，无法申请停机恢复");
			}
			
			Date now = new Date();
			
			LoginInfo loginInfo = LoginHelper.getLoginInfo(RequestContext.getHttpRequest());
			
			// 获取用户的信息
			TerminalUserVO terminalUserVO = terminalUserMapper.getBaseInfoById(loginInfo.getUserId());
			if( terminalUserVO==null )
			{
				logger.warn("CloudHostServiceImpl.reactivateCloudHost() > ["+Thread.currentThread().getId()+"] terminate user not found, id:["+loginInfo.getUserId()+"]");
				return new MethodResult(MethodResult.FAIL, "找不到用户信息");
			}
			
			// 判断余额是否足够
			if( terminalUserVO.getAccountBalance().doubleValue() <= 0 )
			{
				logger.warn("CloudHostServiceImpl.reactivateCloudHost() > ["+Thread.currentThread().getId()+"] 余额不足，余额只剩["+terminalUserVO.getAccountBalance()+"]元");
				return new MethodResult(MethodResult.FAIL, "余额不足");
			}
			
			// 添加一条云主机的计费信息
//			new CloudHostBillingHelper(sqlSession).startOneNewCloudHostBillDetail(cloudDiskVO, now);
			
			// 修改云主机的状态
			Map<String, Object> cloudDiskData = new LinkedHashMap<String, Object>();
			cloudDiskData.put("status",         AppConstant.CLOUD_DISK_STATUS_NORMAL);
			cloudDiskData.put("reactivateTime", StringUtil.dateToString(now, "yyyyMMddHHmmssSSS"));
			cloudDiskData.put("id",             diskId);
			int n = cloudDiskMapper.updateStatusById(cloudDiskData);
			if( n<=0 )
			{
				throw new AppException("申请停机恢复失败");
			}
			else
			{
				return new MethodResult(MethodResult.SUCCESS, "停机恢复成功");
			}
		}
		catch (AppException e)
		{ 
			logger.error(e);
			throw new AppException("停机恢复失败");
		}
		catch (Exception e)
		{
			logger.error(e);
			throw new AppException("停机恢复失败");
		}
	}
	//-----------
	public String getNewCloudDiskNameByUserId(String userId,String region)
	{
		SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
		CloudDiskMapper cloudDiskMapper = this.sqlSession.getMapper(CloudDiskMapper.class);
		
		// 获取用户信息
		SysUserVO sysUser = sysUserMapper.getById(userId);
		
		// 获取这个用户名下的所有云主机的主机名
		Map<String, Object> data = new LinkedHashMap<String, Object>();
		data.put("region", region);
		data.put("userId", userId);
		List<CloudDiskVO> userCloudDiskList = cloudDiskMapper.getCloudDiskForRegion(data);
//		List<CloudHostVO> userCloudHostList = cloudHostMapper.getByUserId(userId);
		Set<String> DiskNames = new HashSet<String>();
		if(userCloudDiskList!=null){
			for( CloudDiskVO cloudDisk : userCloudDiskList )
			{
				if(cloudDisk!=null &&cloudDisk.getName()!=null){
					DiskNames.add(StringUtil.trim(cloudDisk.getName()));
				}
			}
		}
		// 获取新的云主机名
		int len = DiskNames.size() + 1;	// 在size + 1个中，总有一个合适的名字
		String region_str = "";
		if("1".equals(region)){
			region_str = "GZ";
		}else if("4".equals(region)){
			region_str = "HK";
			
		}
		for( int i=1; i<=len; i++ )
		{
			String diskName = "D" + sysUser.getType() + "_" + region_str + "_"+sysUser.getAccount()+"_" + i;
			if( DiskNames.contains(diskName)==false )
			{
				return diskName;
			}
		}
		
		throw new AppException("generate new cloud disk name failed.");
	}
	@Callable
	@Transactional(readOnly=false)
	public MethodResult getCreateInfo(String region)
	{
		try
		{
			HttpServletRequest request = RequestContext.getHttpRequest();
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			String userId = loginInfo.getUserId();
			CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);

			List<CloudHostVO> cloudHostList = cloudHostMapper.getByUserId(loginInfo.getUserId());
			BigDecimal totalPrice = new BigDecimal("0");
			if(cloudHostList!=null&&cloudHostList.size()>0){
				for(CloudHostVO vo:cloudHostList){
					if(!(vo.getSummarizedStatusText().equals("创建失败")||vo.getSummarizedStatusText().equals("停机"))&&vo.getMonthlyPrice()!=null){						
						totalPrice = vo.getMonthlyPrice().add(totalPrice);
					}
				} 
				
			}
// 			CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
//			 List<CloudHostVO> cloudHostList = cloudHostMapper.getByUserId(userId);
//			 
//			 BigDecimal totalPrice = new BigDecimal("0");
//			 if(cloudHostList!=null&&cloudHostList.size()>0){
//					for(CloudHostVO vo:cloudHostList){
//						if(!(vo.getSummarizedStatusText().equals("创建失败")||vo.getSummarizedStatusText().equals("停机"))&&vo.getMonthlyPrice()!=null){						
//							totalPrice = vo.getMonthlyPrice().add(totalPrice);
//						}
//					}
//			 }
			
			MethodResult result = new MethodResult(MethodResult.SUCCESS, "");
			result.put("totalPrice",  totalPrice); 
			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class); 
			
			TerminalUserVO terminalUserVO = terminalUserMapper.getBalanceById(loginInfo.getUserId()); 
			if(terminalUserVO!=null){
				CloudHostBillDetailMapper cloudHostBillDetailMapper = this.sqlSession.getMapper(CloudHostBillDetailMapper.class);
				String now = StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS");
				Map<String, Object> cloudHostBillDetailData = new LinkedHashMap<String, Object>();
				cloudHostBillDetailData.put("userId",     loginInfo.getUserId());
				cloudHostBillDetailData.put("beforeTime",   now); 
				List<CloudHostBillDetailVO> billList = cloudHostBillDetailMapper.getAllUndoneByUserId(cloudHostBillDetailData);
				BigDecimal totalConsumption = new BigDecimal("0"); 
				for(CloudHostBillDetailVO vo: billList){
					Date d1 = StringUtil.stringToDate(now, "yyyyMMddHHmmssSSS");
					Date  d2 = StringUtil.stringToDate(vo.getStartTime(), "yyyyMMddHHmmssSSS");
					long diff = d1.getTime() - d2.getTime();
					long seconds = diff / (1000 * 60);
					String daysecond = (30*24*60)+"";
					BigDecimal nowPrice =  vo.getMonthlyPrice().divide(new BigDecimal(daysecond),3,BigDecimal.ROUND_HALF_UP);
					nowPrice = 		nowPrice.multiply(new BigDecimal(seconds+"")).setScale(2, BigDecimal.ROUND_HALF_UP);
					totalConsumption = totalConsumption.add(nowPrice);
					
					
				}
				 BigDecimal balance = terminalUserVO.getAccountBalance().subtract(totalConsumption);
					if(balance.compareTo(BigDecimal.ZERO)<0){
						balance = BigDecimal.ZERO;
					}
					String balance_str =  balance+"";
  				request.setAttribute("balance", balance_str);
				result.put("balance",  balance_str); 
			}
//			result.put("totalPrice", totalPrice); 
//			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
//			TerminalUserVO user = terminalUserMapper.getBalanceById(userId);
//			result.put("balance", user.getAccountBalance()); 
 
			result.put("name",  getNewCloudDiskNameByUserId(userId,region)); 
 			return result;
		}
		catch (AppException e)
		{ 
			logger.error(e);
			throw new AppException("失败");
		}
		catch (Exception e)
		{
			logger.error(e);
			throw new AppException(e);
		}
	}
}
