package com.zhicloud.op.service.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.ConnectException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.op.app.helper.CloudHostBillingHelper;
import com.zhicloud.op.app.helper.CloudHostPrice;
import com.zhicloud.op.app.helper.LoginHelper;
import com.zhicloud.op.app.helper.ServiceHelper;
import com.zhicloud.op.app.pool.CloudHostData;
import com.zhicloud.op.app.pool.CloudHostPoolManager;
import com.zhicloud.op.app.propeties.AppProperties;
import com.zhicloud.op.common.util.ObjectUtil;
import com.zhicloud.op.common.util.RandomPassword;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.core.CoreSpringContextManager;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.httpGateway.HttpGatewayChannelExt;
import com.zhicloud.op.httpGateway.HttpGatewayManager;
import com.zhicloud.op.httpGateway.HttpGatewayResponseHelper;
import com.zhicloud.op.mybatis.mapper.CloudHostBillDetailMapper;
import com.zhicloud.op.mybatis.mapper.CloudHostMapper;
import com.zhicloud.op.mybatis.mapper.CloudHostOpenPortMapper;
import com.zhicloud.op.mybatis.mapper.SysUserMapper;
import com.zhicloud.op.mybatis.mapper.TerminalUserMapper;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.service.AccountBalanceService;
import com.zhicloud.op.service.ClientService;
import com.zhicloud.op.service.CloudHostService;
import com.zhicloud.op.service.constant.AppConstant;
import com.zhicloud.op.vo.CloudHostBillDetailVO;
import com.zhicloud.op.vo.CloudHostOpenPortVO;
import com.zhicloud.op.vo.CloudHostVO;
import com.zhicloud.op.vo.SysUserVO;
import com.zhicloud.op.vo.TerminalUserVO;

@Transactional(readOnly=true)
public class ClientServiceImpl implements ClientService
{
	
	private static final Logger logger = Logger.getLogger(ClientServiceImpl.class);
	
	private SqlSession sqlSession;
	
	public void setSqlSession(SqlSession sqlSession)
	{
		this.sqlSession = sqlSession;
	}
	
	//---------------

	public void authenticate(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("ClientServiceImpl.authenticate()");
		try
		{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");
			
			Integer userType       = StringUtil.parseInteger(request.getParameter("user_type"), AppConstant.SYS_USER_TYPE_TERMINAL_USER);	// 
			String userAccount     = StringUtil.trim(request.getParameter("user_account"));
			String password        = StringUtil.trim(request.getParameter("password"));
			String terminalType    = StringUtil.trim(request.getParameter("terminal_type"));
			String cloudTerminalId = StringUtil.trim(request.getParameter("cloud_terminal_id"));
			//System.out.println(password);
			
			SysUserMapper sysUserMapper     = sqlSession.getMapper(SysUserMapper.class);
			CloudHostMapper cloudHostMapper = sqlSession.getMapper(CloudHostMapper.class);
			
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("type",     userType);
			condition.put("account",  userAccount);
			if (password.length()==48) 
			{
				condition.put("password", RandomPassword.toChangeEncrypt(password).toUpperCase());
				//System.out.println(RandomPassword.toChangeEncrypt(password).toUpperCase());
			}
			else 
			{
				condition.put("password", LoginHelper.toEncryptedPassword(password));
			}
			
			SysUserVO sysUserVO = sysUserMapper.getByTypeAndAccountAndPassword(condition);
			if( sysUserVO==null )
			{
				ServiceHelper.writeFailMessage(response.getOutputStream(), "not find account information");
				return ;
			}
			
			String userId = sysUserVO.getId();
			
			if( ObjectUtil.isIn(terminalType, new String[]{"1", "2"}) )
			{
				Map<Object, Object> result = ServiceHelper.toSuccessObject("");
				
				// 云主机列表
				List<CloudHostVO> cloudHostList = new ArrayList<CloudHostVO>();
					
				// 客户端
				if( "1".equals(terminalType) )
				{
					// 云主机列表 
					Map<String, Object> parameter = new LinkedHashMap<String, Object>();
					parameter.put("userId", userId); 
					cloudHostList = cloudHostMapper.getByUserId(userId);
				}
				// 云终端
				else // if( "2".equals(type) )
				{
					Map<String, Object> condition2 = new LinkedHashMap<String, Object>();
					condition2.put("userId", userId);
					condition2.put("cloudTerminalId", null);
					// 云主机列表
					List<CloudHostVO> resultList =  cloudHostMapper.getByUserIdAndCloudTerminalId(condition2);
					if(resultList!=null && resultList.size()>0){
						cloudHostList.add(resultList.get(0));
					}
				}
//				List<CloudHostVO> cloudHostVOList1 = new ArrayList<CloudHostVO>();
//				//开机
//				for (CloudHostVO cloudHostVO1 : cloudHostList) 
//				{
//					if ("运行".equals(cloudHostVO1.getSummarizedStatusText())) 
//					{
//						cloudHostVOList1.add(cloudHostVO1);
//					}
//					
//				}
//				//关机
//				for (CloudHostVO cloudHostVO2 : cloudHostList) 
//				{
//					if ("关机".equals(cloudHostVO2.getSummarizedStatusText())) 
//					{
//						cloudHostVOList1.add(cloudHostVO2);
//					}
//					
//				}
//				//停机
//				for (CloudHostVO cloudHostVO3 : cloudHostList) 
//				{
//					if ("停机".equals(cloudHostVO3.getSummarizedStatusText())) 
//					{
//						cloudHostVOList1.add(cloudHostVO3);
//					}
//					
//				}
//				//欠费
//				for (CloudHostVO cloudHostVO4 : cloudHostList) 
//				{
//					if ("欠费".equals(cloudHostVO4.getSummarizedStatusText())) 
//					{
//						cloudHostVOList1.add(cloudHostVO4);
//					}
//					
//				}
//				//警告
//				for (CloudHostVO cloudHostVO5 : cloudHostList) 
//				{
//					if ("警告".equals(cloudHostVO5.getSummarizedStatusText())) 
//					{
//						cloudHostVOList1.add(cloudHostVO5);
//					}
//					
//				}
//				//故障
//				for (CloudHostVO cloudHostVO6 : cloudHostList) 
//				{
//					if ("故障".equals(cloudHostVO6.getSummarizedStatusText())) 
//					{
//						cloudHostVOList1.add(cloudHostVO6);
//					}
//					
//				}
//				//其他
//				for (CloudHostVO cloudHostVO7 : cloudHostList) 
//				{
//					if ("其他".equals(cloudHostVO7.getSummarizedStatusText())) 
//					{
//						cloudHostVOList1.add(cloudHostVO7);
//					}
//					
//				}
				// 云主机列表
				List<Map<Object, Object>> connList = new ArrayList<Map<Object, Object>>();
				
				for (CloudHostVO cloudHost : cloudHostList)
				{
					if ("创建中".equals(cloudHost.getSummarizedStatusText())||"创建失败".equals(cloudHost.getSummarizedStatusText())) 
					{
						continue;
					}
					Map<Object, Object> connection = new LinkedHashMap<Object, Object>();
					connection.put("display_name", cloudHost.getDisplayName());
					connection.put("display_password", cloudHost.getPassword());
					connection.put("external_address", cloudHost.getOuterIp() + ":" + cloudHost.getOuterPort());
					connection.put("internal_address", cloudHost.getInnerIp() + ":" + cloudHost.getInnerPort());
					connList.add(connection);
				}
				result.put("connections", connList);
				
				// control server 信息
				Map<Object, Object> control_server_info = new LinkedHashMap<Object, Object>();
				control_server_info.put("external_address", AppProperties.getValue("control_server_external_address", ""));
				control_server_info.put("internal_address", AppProperties.getValue("control_server_internal_address", ""));
				result.put("control_server", control_server_info);
				
				// 写到返回流
				ServiceHelper.writeJsonTo(response.getOutputStream(), result);
			}
			else
			{
				ServiceHelper.writeFailMessage(response.getOutputStream(), "'type' must 1 or 2 ");
				return ;
			}
			
		}
		catch (IOException e)
		{
			logger.error("ClientServiceImpl.authenticate()",e);
			throw new AppException("失败");
		}
	}
	

	public void getCloudHost(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("ClientServiceImpl.getCloudHost()");
		try
		{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");
			
			Integer userType       = StringUtil.parseInteger(request.getParameter("user_type"), AppConstant.SYS_USER_TYPE_TERMINAL_USER);	// 
			String userAccount     = StringUtil.trim(request.getParameter("user_account"));
			String password        = StringUtil.trim(request.getParameter("password"));
			//System.out.println(password);
			
			SysUserMapper sysUserMapper                     = sqlSession.getMapper(SysUserMapper.class);
			CloudHostMapper cloudHostMapper                 = sqlSession.getMapper(CloudHostMapper.class);
			CloudHostOpenPortMapper cloudHostOpenPortMapper = sqlSession.getMapper(CloudHostOpenPortMapper.class);
			CloudHostService cloudHostService = CoreSpringContextManager.getCloudHostService();
			
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("type",     userType);
			condition.put("account",  userAccount);
			if (password.length()==48) 
			{
				condition.put("password", RandomPassword.toChangeEncrypt(password).toUpperCase());
				//System.out.println(RandomPassword.toChangeEncrypt(password).toUpperCase());
			}
			else
			{
				condition.put("password", LoginHelper.toEncryptedPassword(password));
			}
			
			SysUserVO sysUserVO = sysUserMapper.getByTypeAndAccountAndPassword(condition);
			if( sysUserVO==null )
			{
				ServiceHelper.writeFailMessage(response.getOutputStream(), "not find account information");
				return ;
			}
			
			Map<Object, Object> result = ServiceHelper.toSuccessObject("");
			
			// 云主机列表 
			List<CloudHostVO> cloudHostVOList = cloudHostMapper.getByUserId(sysUserVO.getId());
//			List<CloudHostVO> cloudHostVOList1 = new ArrayList<CloudHostVO>();
//			//开机
//			for (CloudHostVO cloudHostVO1 : cloudHostVOList) 
//			{
//				if ("运行".equals(cloudHostVO1.getSummarizedStatusText())) 
//				{
//					cloudHostVOList1.add(cloudHostVO1);
//				}
//				
//			}
//			//关机
//			for (CloudHostVO cloudHostVO2 : cloudHostVOList) 
//			{
//				if ("关机".equals(cloudHostVO2.getSummarizedStatusText())) 
//				{
//					cloudHostVOList1.add(cloudHostVO2);
//				}
//				
//			}
//			//停机
//			for (CloudHostVO cloudHostVO3 : cloudHostVOList) 
//			{
//				if ("停机".equals(cloudHostVO3.getSummarizedStatusText())) 
//				{
//					cloudHostVOList1.add(cloudHostVO3);
//				}
//				
//			}
//			//欠费
//			for (CloudHostVO cloudHostVO4 : cloudHostVOList) 
//			{
//				if ("欠费".equals(cloudHostVO4.getSummarizedStatusText())) 
//				{
//					cloudHostVOList1.add(cloudHostVO4);
//				}
//				
//			}
//			//警告
//			for (CloudHostVO cloudHostVO5 : cloudHostVOList) 
//			{
//				if ("警告".equals(cloudHostVO5.getSummarizedStatusText())) 
//				{
//					cloudHostVOList1.add(cloudHostVO5);
//				}
//				
//			}
//			//故障
//			for (CloudHostVO cloudHostVO6 : cloudHostVOList) 
//			{
//				if ("故障".equals(cloudHostVO6.getSummarizedStatusText())) 
//				{
//					cloudHostVOList1.add(cloudHostVO6);
//				}
//				
//			}
//			//其他
//			for (CloudHostVO cloudHostVO7 : cloudHostVOList) 
//			{
//				if ("其他".equals(cloudHostVO7.getSummarizedStatusText())) 
//				{
//					cloudHostVOList1.add(cloudHostVO7);
//				}
//				
//			}
			// 返回的云主机列表
			List<Map<Object, Object>> hostDatas = new ArrayList<Map<Object, Object>>();
			for (CloudHostVO cloudHostVO : cloudHostVOList)
			{
				if ("创建中".equals(cloudHostVO.getSummarizedStatusText())||"创建失败".equals(cloudHostVO.getSummarizedStatusText())) 
				{
					continue;
				}
				if("停机".equals(cloudHostVO.getSummarizedStatusText()) && cloudHostVO.getInactivateTime()!=null)
				{
	 				Date date = null;
	 				try {
	 					date = StringUtil.stringToDate(cloudHostVO.getInactivateTime(),"yyyyMMddHHmmssSSS");
	 				} catch (ParseException e) {
	 					e.printStackTrace();
	 				}
	 				Calendar calendar = Calendar.getInstance();
	 				calendar.setTime(date);
	 				calendar.add(Calendar.DAY_OF_MONTH,7);
	 				String lastTime = StringUtil.dateToString(calendar.getTime(),"yyyyMMddHHmmssSSS");
	 				String nowTime = StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS");
	 				if(Long.parseLong(nowTime) - Long.parseLong(lastTime) > 0)
	 				{
	 					continue;
	 				}
	 			}
				Map<Object, Object> hostData = new LinkedHashMap<Object, Object>();
				hostData.put("uuid",             cloudHostVO.getId());                                      
				hostData.put("host_name",        cloudHostVO.getDisplayName()); 
				if (cloudHostVO.getDisplayName()==null) 
				{
					hostData.put("host_name",        cloudHostVO.getHostName());
				}
				hostData.put("type",             cloudHostVO.getType());                         
				hostData.put("account",          cloudHostVO.getAccount());
				hostData.put("password",         cloudHostVO.getPassword());
				hostData.put("cpu_core",         cloudHostVO.getCpuCore());                                    
				hostData.put("cpu_usage",        0);
				hostData.put("memory",           cloudHostVO.getMemory());        
				hostData.put("memory_usage",     0); 
				hostData.put("sys_disk",         cloudHostVO.getSysDisk());
				hostData.put("sys_disk_usage",   0);
				hostData.put("data_disk",        cloudHostVO.getDataDisk());
				hostData.put("data_disk_usage",  0);
				hostData.put("bandwidth",        cloudHostVO.getBandwidth());                               
				hostData.put("is_auto_startup",  cloudHostVO.getIsAutoStartup());                           
				hostData.put("running_status",   cloudHostVO.getRunningStatus());                           
				hostData.put("status",           cloudHostVO.getStatus());                                  
				hostData.put("inner_ip",         cloudHostVO.getInnerIp());                                 
				hostData.put("inner_port",       cloudHostVO.getInnerPort());                               
				hostData.put("outer_ip",         cloudHostVO.getOuterIp());                                 
				hostData.put("outer_port",       cloudHostVO.getOuterPort());   
				hostData.put("region",           cloudHostVO.getRegion());
				hostData.put("sysImageName",     cloudHostVO.getSysImageName());
				if (cloudHostVO.getSysImageName()==null)
				{
					hostData.put("sysImageName",     cloudHostVO.getSysImageNameOld());
				}
				hostData.put("inactivate_time",  cloudHostVO.getInactivateTime());
//				hostData.put("network_io_receive_byte", 0);
//				hostData.put("network_io_send_byte",    0);
//				hostData.put("disk_io_read_byte",       0);
//				hostData.put("disk_io_write_byte",      0);
				
				CloudHostData cacheCloudHostData = CloudHostPoolManager.getCloudHostPool().getByRealHostId(cloudHostVO.getRealHostId());
//				System.out.println(cloudHostService.startMonitor(cloudHostVO.getRegion(), cloudHostVO.getRealHostId())==true);
//				if (cloudHostService.startMonitor(cloudHostVO.getRegion(), cloudHostVO.getRealHostId())==true) 
//				{
//				CloudHostMonitorPlusData  cacheCloudHostData = cloudHostService.getMonitorData(cloudHostVO.getRealHostId());
				if( cacheCloudHostData!=null )
				{
					hostData.put("cpu_usage",               cacheCloudHostData.getCpuUsage());
					hostData.put("memory_usage",            cacheCloudHostData.getMemoryUsage());
					hostData.put("sys_disk_usage",          cacheCloudHostData.getSysDiskUsage());
					hostData.put("data_disk_usage",         cacheCloudHostData.getDataDiskUsage());
					
//					hostData.put("network_io_receive_byte", cacheCloudHostData.getNetworkIOReceiveByte());
//					hostData.put("network_io_send_byte",    cacheCloudHostData.getNetworkIOSendByte());
//					
//					hostData.put("disk_io_read_byte",       cacheCloudHostData.getDiskIOReadByte());
//					hostData.put("disk_io_write_byte",      cacheCloudHostData.getDiskIOWriteByte());
					
				}
				
				List<CloudHostOpenPortVO> portVOs = cloudHostOpenPortMapper.getByHostId(cloudHostVO.getId());
				List<Map<Object, Object>> portsData = new ArrayList<Map<Object,Object>>();
				for( CloudHostOpenPortVO portVO : portVOs )
				{
					Map<Object, Object> portData = new LinkedHashMap<Object, Object>();
					portData.put("protocol",    portVO.getProtocol());
					portData.put("port",        portVO.getPort());
					portData.put("server_port", portVO.getServerPort());
					portData.put("outer_port",  portVO.getOuterPort());
					portsData.add(portData);
				}
				hostData.put("ports", portsData);
				
				hostDatas.add(hostData);
			}
			result.put("hosts", hostDatas);
			
			
			// 写到返回流
			ServiceHelper.writeJsonTo(response.getOutputStream(), result);
		
			
		}
		catch (IOException e)
		{
			logger.error("ClientServiceImpl.authenticate()",e);
			throw new AppException("失败");
		}
	}

	public void getCloudHostPrice(HttpServletRequest request,HttpServletResponse response) 
		{
			logger.debug("ClientServiceImpl.getCloudHostPrice()");
			try 
			{
				response.setCharacterEncoding("utf-8");
				response.setContentType("text/json; charset=utf-8");
				
				String cloudHostId     = StringUtil.trim(request.getParameter("cloud_host_id"));
				Integer cpuCore        = new Integer(StringUtil.trim(request.getParameter("cpu_core")));
				BigInteger memory      = new BigInteger(StringUtil.trim(request.getParameter("memory")));
				BigInteger bandwidth   = new BigInteger(StringUtil.trim(request.getParameter("bandwidth")));
				if (cloudHostId==null) 
				{
					throw new AppException("cloud_host_id cannot be null.");
				}
				CloudHostMapper cloudHostMapper = sqlSession.getMapper(CloudHostMapper.class);
				TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class); 
				CloudHostVO  cloudHostVO        = cloudHostMapper.getById(cloudHostId);
				if (cloudHostVO==null) 
				{
					throw new AppException("not found your cloudHost");
				}
				if(cloudHostVO.getRealHostId()==null )
				{
					ServiceHelper.writeFailMessage(response.getOutputStream(), "Cloud host creates is not successful");
					return ;
				}
				//结算余额
				BigDecimal balance=null;
				if (cloudHostVO.getUserId()!=null) 
				{
					TerminalUserVO terminalUserVO = terminalUserMapper.getBalanceById(cloudHostVO.getUserId()); 
					if(terminalUserVO!=null)
					{
						CloudHostBillDetailMapper cloudHostBillDetailMapper = this.sqlSession.getMapper(CloudHostBillDetailMapper.class);
						String now = StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS");
						Map<String, Object> cloudHostBillDetailData = new LinkedHashMap<String, Object>();
						cloudHostBillDetailData.put("userId",     cloudHostVO.getUserId());
						cloudHostBillDetailData.put("beforeTime",   now); 
						List<CloudHostBillDetailVO> billList = cloudHostBillDetailMapper.getAllUndoneByUserId(cloudHostBillDetailData);
						BigDecimal totalConsumption = new BigDecimal("0"); 
						for(CloudHostBillDetailVO vo: billList){
							Date d1 = StringUtil.stringToDate(now, "yyyyMMddHHmmssSSS");
							Date d2 = StringUtil.stringToDate(vo.getStartTime(), "yyyyMMddHHmmssSSS");
							long diff = d1.getTime() - d2.getTime();
							long seconds = diff / (1000 * 60);
							String daysecond = (30*24*60)+"";
							BigDecimal nowPrice =  vo.getMonthlyPrice().divide(new BigDecimal(daysecond),3,BigDecimal.ROUND_HALF_UP);
							nowPrice = 		nowPrice.multiply(new BigDecimal(seconds+"")).setScale(2, BigDecimal.ROUND_HALF_UP);
							totalConsumption = totalConsumption.add(nowPrice);
					}
	                    balance = terminalUserVO.getAccountBalance().subtract(totalConsumption);
						if(balance.compareTo(BigDecimal.ZERO)<0)
						{
							balance = BigDecimal.ZERO;
						}
					} 
				}
				Map<Object, Object> result = ServiceHelper.toSuccessObject("");
				Map<Object, Object> priceData = new LinkedHashMap<Object, Object>();
				//价格计算，按月计算
				BigDecimal monthlyPrice = CloudHostPrice.getMonthlyPrice(cloudHostVO.getRegion(),
																		 3,
						                                                 cpuCore, memory,
						                                                 cloudHostVO.getDataDisk(), 
						                                                 bandwidth).setScale(2, BigDecimal.ROUND_HALF_UP);
				priceData.put("monthlyPrice", monthlyPrice);
				priceData.put("balance", balance);
	            result.put("price",  priceData);
	            //写到返回流
				ServiceHelper.writeJsonTo(response.getOutputStream(), result);
				
			}catch (AppException e)
			{ 
				logger.error("ClientServiceImpl.getCloudHostPrice()",e);
				throw new AppException("失败");
			}
			catch (Exception e)
			{
				logger.error("ClientServiceImpl.getCloudHostPrice()",e); 
				throw new AppException("失败");
			}
			
		}

	public void getClientVersion(HttpServletRequest request, HttpServletResponse response) 
	{
		logger.debug("ClientServiceImpl.getClientVersion()");
		try {
			File path= new File(request.getSession().getServletContext().getRealPath("/"));
			String parentPath = path.getParent();
			String downloadPath=parentPath+"/download";
//			String downloadPath="D:/Tools/apache-tomcat-7.0.55/webapps/download";
	
			File file = new File(downloadPath);   
		    File[] array = file.listFiles(); 
		    if (array.length==0) 
		    {
		    	ServiceHelper.writeFailMessage(response.getOutputStream(), "No update version");
				return ;
			}
		    
		    Map<Object, Object> result = ServiceHelper.toSuccessObject("");
			Map<Object, Object> versions = new LinkedHashMap<Object, Object>();
			for(int i=0;i<array.length;i++){   
				String name =array[i].getName();
	            if(array[i].isFile() && ((name.substring(0,name.length()-4)).contains("zhicloud_client")))
	            {   
//	            	System.out.println(array[i].getPath());
	                versions.put("name", array[i].getName());
	                versions.put("path", array[i].getPath());
	            }
	            if (array[i].isFile() && ((name.substring(0,name.length()-4))).contains("updateinfo")) 
	            { 
	            	InputStreamReader read = new InputStreamReader(new FileInputStream(new File(downloadPath+"/"+name)),"utf-8");
	            	StringBuffer stringBuffer = new StringBuffer();
	            	BufferedReader bufferedReader = new BufferedReader(read);
                    String updateinfo = null;
                    while((updateinfo = bufferedReader.readLine()) != null)
                    {
                    	stringBuffer.append(updateinfo+"\n");
                    }
                    read.close();
                    versions.put("updateinfo", stringBuffer.toString());
				}
	        } 
			if(versions.size()<1){
				ServiceHelper.writeFailMessage(response.getOutputStream(), "No update version");
				return ;
			}
			result.put("versions", versions);
			//返回
			ServiceHelper.writeJsonTo(response.getOutputStream(), result);
			 
		} catch (AppException e)
		{ 
			logger.error("ClientServiceImpl.getClientVersion()",e);
			throw new AppException("失败");
		}
		catch (Exception e)
		{
			logger.error("ClientServiceImpl.getClientVersion()",e); 
			throw new AppException("失败");
		}
	};
	
	@Callable
	@Transactional(readOnly = false)
	public void cloudHostOperation(HttpServletRequest request, HttpServletResponse response) 
	{
		logger.debug("ClientServiceImpl.cloudHostOperation()");
		try 
		{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");
			
			String cloudHostId    = StringUtil.trim(request.getParameter("cloud_host_id"));
			String operationType  = StringUtil.trim(request.getParameter("operation_type"));
		
//			SysUserMapper sysUserMapper     = sqlSession.getMapper(SysUserMapper.class);
			CloudHostMapper cloudHostMapper = sqlSession.getMapper(CloudHostMapper.class);
			
			CloudHostVO cloudHost = cloudHostMapper.getById(cloudHostId);
			String realHostId = cloudHost.getRealHostId();
			if( realHostId==null )
			{
				ServiceHelper.writeFailMessage(response.getOutputStream(), "Cloud host has not been created successfully");
				return ;
			}
			
			//启动
			if (AppConstant.CLOUD_HOST_START.equals(operationType)) 
			{
				//如果前后台不一致，后台开机，显示的开机
				CloudHostData myCloudHostData = CloudHostPoolManager.getCloudHostPool().getByRealHostId(cloudHost.getRealHostId());
				if(myCloudHostData!=null && myCloudHostData.getRunningStatus() == 2)
				{
					Map<String,Object> hostData = new LinkedHashMap<String, Object>();
					hostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_RUNNING);
					hostData.put("realHostId", realHostId);
					cloudHostMapper.updateRunningStatusByRealHostId(hostData);
					logger.info("ClientServiceImpl.startCloudHost() > ["+Thread.currentThread().getId()+"] start host succeeded");
					ServiceHelper.writeSuccessMessage(response.getOutputStream(), "Cloud host started successfully");
					return ;
				}
				
				HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(cloudHost.getRegion());
				JSONObject startResullt = channel.hostStart(realHostId, 0, "");
				
				Map<String,Object> cloudHostData = new LinkedHashMap<String, Object>();
				cloudHostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_RUNNING);
				cloudHostData.put("realHostId", realHostId);
				if( HttpGatewayResponseHelper.isSuccess(startResullt) )
				{
					cloudHostMapper.updateRunningStatusByRealHostId(cloudHostData);
					logger.info("ClientServiceImpl.startCloudHost() > ["+Thread.currentThread().getId()+"] start host succeeded");
					ServiceHelper.writeSuccessMessage(response.getOutputStream(), "Cloud host started successfully");
					return ;
				}
				else
				{
					logger.warn("ClientServiceImpl.startCloudHost() > ["+Thread.currentThread().getId()+"] start host failed, message:["+HttpGatewayResponseHelper.getMessage(startResullt)+"]");
					ServiceHelper.writeFailMessage(response.getOutputStream(), "Cloud host started fail");
					return ;
				}
			}
			
			//正常关机
			if (AppConstant.CLOUD_HOST_STOP.equals(operationType)) 
			{
				//如果前后台不一致，后台关机，显示的是开机
				CloudHostData myCloudHostData = CloudHostPoolManager.getCloudHostPool().getByRealHostId(cloudHost.getRealHostId());
				if(myCloudHostData!=null && myCloudHostData.getRunningStatus() == 1)
				{
					Map<String,Object> hostData = new LinkedHashMap<String, Object>();
					hostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN);
					hostData.put("realHostId", realHostId);
					cloudHostMapper.updateRunningStatusByRealHostId(hostData);
					logger.info("ClientServiceImpl.stopCloudHost() > ["+Thread.currentThread().getId()+"] stop host succeeded");
					ServiceHelper.writeSuccessMessage(response.getOutputStream(), "Orders issued successfully");
					return ;
				}
				
				HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(cloudHost.getRegion());
				JSONObject stopResullt = channel.hostStop(realHostId);
				
				Map<String,Object> cloudHostData = new LinkedHashMap<String, Object>();
				cloudHostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN);
				cloudHostData.put("realHostId", realHostId);
				if( HttpGatewayResponseHelper.isSuccess(stopResullt) )
				{	
//					cloudHostMapper.updateRunningStatusByRealHostId(cloudHostData);
					logger.info("ClientServiceImpl.stopCloudHost() > ["+Thread.currentThread().getId()+"] stop host succeeded");
					ServiceHelper.writeSuccessMessage(response.getOutputStream(), "Orders issued successfully");
					return ;
				}
				else
				{
					logger.warn("ClientServiceImpl.haltCloudHost() > ["+Thread.currentThread().getId()+"] halt host failed, message:["+HttpGatewayResponseHelper.getMessage(stopResullt)+"]");
					ServiceHelper.writeFailMessage(response.getOutputStream(), "cloud host stop fail");
					return ;
				}
			}
			
			//重启
			if (AppConstant.CLOUD_HOST_RESTART.equals(operationType)) 
			{
				//如果前后台不一致，显示运行 实际关机状态
				Map<String,Object> cloudHostData = new LinkedHashMap<String, Object>();
				CloudHostData myCloudHostData = CloudHostPoolManager.getCloudHostPool().getByRealHostId(cloudHost.getRealHostId());
				cloudHostData.put("realHostId", realHostId);
				
				if(myCloudHostData!=null && myCloudHostData.getRunningStatus() == 1)
				{
					HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(cloudHost.getRegion());
					JSONObject startResullt = channel.hostStart(realHostId, 0, "");
					if( HttpGatewayResponseHelper.isSuccess(startResullt)==false )
					{
						cloudHostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN);
						cloudHostMapper.updateRunningStatusByRealHostId(cloudHostData);
						logger.warn("ClientServiceImpl.restartCloudHost() > ["+Thread.currentThread().getId()+"] start host failed, message:["+HttpGatewayResponseHelper.getMessage(startResullt)+"]");
						ServiceHelper.writeFailMessage(response.getOutputStream(), "Cloud host restart failure");
						return;
					}
					logger.info("ClientServiceImpl.restartCloudHost() > ["+Thread.currentThread().getId()+"] restart host succeeded");
					ServiceHelper.writeSuccessMessage(response.getOutputStream(), "Cloud host restart successfully");
					return;
				}
				
				HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(cloudHost.getRegion());
				
				JSONObject haltResullt = channel.hostHalt(realHostId);
				
//				Map<String,Object> cloudHostData = new LinkedHashMap<String, Object>();
//				cloudHostData.put("realHostId", realHostId);
				
				if( HttpGatewayResponseHelper.isSuccess(haltResullt)==false )
				{
//					cloudHostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN);
//					cloudHostMapper.updateRunningStatusByRealHostId(cloudHostData);
					logger.warn("ClientServiceImpl.restartCloudHost() > ["+Thread.currentThread().getId()+"] halt host failed, message:["+HttpGatewayResponseHelper.getMessage(haltResullt)+"]");
					ServiceHelper.writeFailMessage(response.getOutputStream(), "Cloud host restart failure");
					return ;
				}
				
				JSONObject startResullt = channel.hostStart(realHostId, 0, "");
				if( HttpGatewayResponseHelper.isSuccess(startResullt)==false )
				{
					cloudHostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN);
					cloudHostMapper.updateRunningStatusByRealHostId(cloudHostData);
					logger.warn("ClientServiceImpl.restartCloudHost() > ["+Thread.currentThread().getId()+"] start host failed, message:["+HttpGatewayResponseHelper.getMessage(startResullt)+"]");
					ServiceHelper.writeFailMessage(response.getOutputStream(), "Cloud host restart failure");
					return ;
				}
				cloudHostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_RUNNING);
				cloudHostMapper.updateRunningStatusByRealHostId(cloudHostData);
				logger.info("ClientServiceImpl.restartCloudHost() > ["+Thread.currentThread().getId()+"] restart host succeeded");
				ServiceHelper.writeSuccessMessage(response.getOutputStream(), "Cloud host restart successfully");
				return ;
			}

			//强制关机关机
			if (AppConstant.CLOUD_HOST_HALT.equals(operationType)) 
			{
				//如果前后台不一致，后台关机，显示的是开机
				CloudHostData myCloudHostData = CloudHostPoolManager.getCloudHostPool().getByRealHostId(cloudHost.getRealHostId());
				if(myCloudHostData!=null && myCloudHostData.getRunningStatus() == 1)
				{
					Map<String,Object> hostData = new LinkedHashMap<String, Object>();
					hostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN);
					hostData.put("realHostId", realHostId);
					cloudHostMapper.updateRunningStatusByRealHostId(hostData);
					logger.info("ClientServiceImpl.haltCloudHost() > ["+Thread.currentThread().getId()+"] halt host succeeded");
					ServiceHelper.writeSuccessMessage(response.getOutputStream(), "cloud host halt successfully");
					return ;
				}
				
				HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(cloudHost.getRegion());
				JSONObject haltResullt = channel.hostHalt(realHostId);
				
				Map<String,Object> cloudHostData = new LinkedHashMap<String, Object>();
				cloudHostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN);
				cloudHostData.put("realHostId", realHostId);
				if( HttpGatewayResponseHelper.isSuccess(haltResullt) )
				{
					cloudHostMapper.updateRunningStatusByRealHostId(cloudHostData);
					logger.info("ClientServiceImpl.haltCloudHost() > ["+Thread.currentThread().getId()+"] halt host succeeded");
					ServiceHelper.writeSuccessMessage(response.getOutputStream(), "cloud host halt successfully");
					return ;
				}
				else
				{
					logger.warn("ClientServiceImpl.haltCloudHost() > ["+Thread.currentThread().getId()+"] halt host failed, message:["+HttpGatewayResponseHelper.getMessage(haltResullt)+"]");
					ServiceHelper.writeFailMessage(response.getOutputStream(), "cloud host halt failure");
					return ;
				}
			}
			
			
		} 
		catch (ConnectException e)
		{
//			throw new AppException("连接http gateway失败", e);
			logger.error("ClientServiceImpl.cloudHostOperation() > ["+Thread.currentThread().getId()+"] ",e);
			throw new AppException("连接失败");
		}
		catch (AppException e)
		{ 
			logger.error("ClientServiceImpl.cloudHostOperation() > ["+Thread.currentThread().getId()+"] ",e); 
			throw new AppException("失败");
		}
		catch (Exception e)
		{ 
			logger.error("ClientServiceImpl.cloudHostOperation() > ["+Thread.currentThread().getId()+"] ",e);
			throw new AppException("失败");
		}
		
	}

	
	@Callable
	@Transactional(readOnly = false)
	public void cloudHostChange(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("ClientServiceImpl.cloudHostChange()");
		try 
		{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");
			
			String cloudHostId     = StringUtil.trim(request.getParameter("cloud_host_id"));
			Integer cpuCore        = new Integer(StringUtil.trim(request.getParameter("cpu_core")));
			BigInteger memory      = new BigInteger(StringUtil.trim(request.getParameter("memory")));
			BigInteger bandwidth   = new BigInteger(StringUtil.trim(request.getParameter("bandwidth")));
//			System.out.println(cloudHostId+"--"+cpuCore+"===="+memory+"/////"+bandwidth);
			if (cloudHostId==null) 
			{
				throw new AppException("cloud_host_id cannot be null.");
			}
			
			CloudHostMapper cloudHostMapper = sqlSession.getMapper(CloudHostMapper.class);
			TerminalUserMapper terminalUserMapper = sqlSession.getMapper(TerminalUserMapper.class);
			CloudHostVO  cloudHostVO        = cloudHostMapper.getById(cloudHostId);
			if (cloudHostVO==null) 
			{
				throw new AppException("not found your cloudHost");
			}
			if(cloudHostVO.getRealHostId()==null )
			{
				ServiceHelper.writeFailMessage(response.getOutputStream(), "Cloud host has not been created successfully");
				return ;
			}

			//价格计算，按月计算
			BigDecimal price   = CloudHostPrice.getMonthlyPrice(cloudHostVO.getRegion(),3,cpuCore, memory, cloudHostVO.getDataDisk(), bandwidth).setScale(2,   BigDecimal.ROUND_HALF_UP);
			// 计算余额能否支持一天，不足不创建主机
			TerminalUserVO terminalUserVO = terminalUserMapper.getBalanceById(cloudHostVO.getUserId());
			if (terminalUserVO != null) 
			{
				List<CloudHostVO> cloudHostList = cloudHostMapper.getByUserId(cloudHostVO.getUserId());
				BigDecimal totalPrice = new BigDecimal("0");
				if (cloudHostList != null && cloudHostList.size() > 0) 
				{
					for (CloudHostVO vo : cloudHostList) 
					{
						if (vo.getType() == 3) 
						{
							if (!(vo.getSummarizedStatusText().equals("创建失败") || vo.getSummarizedStatusText().equals("停机")) && vo.getMonthlyPrice() != null && (!vo.getId().equals(cloudHostId))) 
							{
								totalPrice = vo.getMonthlyPrice().add(totalPrice);
							}
						}
					}
				}
				// 计算出每天的费用
				totalPrice = totalPrice.add(price);
				totalPrice = totalPrice.divide(new BigDecimal("31"), 0, BigDecimal.ROUND_HALF_UP);
				BigDecimal balance = terminalUserVO.getAccountBalance().setScale(0, BigDecimal.ROUND_HALF_UP);
				if (balance.compareTo(totalPrice) < 0) 
				{
					if (totalPrice.subtract(balance).compareTo(BigDecimal.TEN) < 0) 
					{
//						return new MethodResult(MethodResult.FAIL, "余额不足，至少充值10元 ");
						ServiceHelper.writeFailMessage(response.getOutputStream(), "1");
						return ;
					} 
					else 
					{
//						return new MethodResult(MethodResult.FAIL, "余额不足，至少充值" + totalPrice.subtract(balance) + "元 ");
						ServiceHelper.writeFailMessage(response.getOutputStream(), "1");
						return ;
					}
				}

				// 修改云平台上的云主机的信息 ，没有的参数""/0/[]表示
				HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(cloudHostVO.getRegion());

				JSONObject hostModifyResult = channel.hostModify(	cloudHostVO.getRealHostId(), 
																	"", 
																	cpuCore, 
																	memory, 
																	new Integer[]{0}, 
																	new Integer[0], 
																	"", 
																	"", 
																	"", 
																	bandwidth, 
																	bandwidth);
				if( HttpGatewayResponseHelper.isSuccess(hostModifyResult)==false )
				{
					logger.warn("ClientServiceImpl.cloudHostChange() > ["+Thread.currentThread().getId()+"] modify cloud host failed, message:["+HttpGatewayResponseHelper.getMessage(hostModifyResult)+"], region:["+cloudHostVO.getRegion()+"]");
					throw new AppException("修改云主机信息失败");
				}
				else 
				{
					// 结算，结算之后产生新的账单
					Date now = new Date();
					new CloudHostBillingHelper(sqlSession).settleAllUndoneCloudHostBills(cloudHostId, now, true);
					//更新相关数据
					Map<String, Object> condition = new LinkedHashMap<String, Object>();
					condition.put("id",        cloudHostId);
					condition.put("cpuCore",   cpuCore);
					condition.put("memory",    memory);
					condition.put("bandwidth", bandwidth);
					condition.put("price",     price);
					int n = cloudHostMapper.updateClientCloudHostById(condition);
					if (n>0) 
					{
						logger.info("ClientServiceImpl.cloudHostChange() > ["+Thread.currentThread().getId()+"] cloudHost change host succeeded");
						ServiceHelper.writeSuccessMessage(response.getOutputStream(), "change cloud host successfully");
						return ;
					}
					else 
					{
						logger.info("ClientServiceImpl.cloudHostChange() > ["+Thread.currentThread().getId()+"] cloudHost change host failed");
						ServiceHelper.writeFailMessage(response.getOutputStream(), "change cloud host failure");
						return ;
					}
				}
			}
			else 
			{
				ServiceHelper.writeFailMessage(response.getOutputStream(), "failure");
				return ;
			} 
			 
		}catch (AppException e)
		{ 
			logger.error("ClientServiceImpl.cloudHostChange()",e);
			throw new AppException("失败");
		}
		catch (Exception e)
		{
			logger.error("ClientServiceImpl.cloudHostChange()",e); 
			throw new AppException("失败");
		}
		
	}
	
	@Callable
	@Transactional(readOnly = false)
	public void changeDisplayName(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("ClientServiceImpl.changeDisplayName()");
		try 
		{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");
			
			String cloudHostId    = StringUtil.trim(request.getParameter("cloud_host_id"));
			String hostName    = StringUtil.trim(request.getParameter("host_name"));
			if (hostName.length()>30) 
			{
				logger.info("ClientServiceImpl.changeDisplayName() > ["+Thread.currentThread().getId()+"] cloudHost display name change host succeeded");
				ServiceHelper.writeFailMessage(response.getOutputStream(), "hostName more than 30 characters in length");
				return ;
			}
			
			CloudHostMapper cloudHostMapper =this.sqlSession.getMapper(CloudHostMapper.class);
			
			Map<String, Object>cloudHost = new LinkedHashMap<String, Object>();
			cloudHost.put("id", cloudHostId);
			cloudHost.put("hostName", hostName);
			int n =cloudHostMapper.updateDisplayNameById(cloudHost);
			if (n>0) 
			{
				logger.info("ClientServiceImpl.changeDisplayName() > ["+Thread.currentThread().getId()+"] cloudHost display name change host succeeded");
				ServiceHelper.writeSuccessMessage(response.getOutputStream(), "change cloud host  display name successfully");
				return ;
			}
			else 
			{
				logger.info("ClientServiceImpl.changeDisplayName() > ["+Thread.currentThread().getId()+"] cloudHost display name change host succeeded");
				ServiceHelper.writeFailMessage(response.getOutputStream(), "change cloud host  display name failure");
				return ;
			}
			
		}catch (AppException e)
		{ 
			logger.error("ClientServiceImpl.changeDisplayName()",e);
			throw new AppException("失败");
		}
		catch (Exception e)
		{
			logger.error("ClientServiceImpl.changeDisplayName()",e); 
			throw new AppException("失败");
		}
	}
}
