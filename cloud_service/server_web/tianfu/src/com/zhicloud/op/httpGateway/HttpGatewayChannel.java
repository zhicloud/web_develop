package com.zhicloud.op.httpGateway;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.MalformedURLException;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.zhicloud.op.app.listener.SysDataInitializerListener;
import com.zhicloud.op.app.propeties.AppProperties;
import com.zhicloud.op.common.util.StringUtil;

public class HttpGatewayChannel
{
	

	public static final Logger logger = Logger.getLogger(SysDataInitializerListener.class);
	
	//--------------------
	
	private HttpGatewayHelper helper = null;

	private int region = 0;
	
	//--------------------
	
	public HttpGatewayChannel(int region)
	{
		this.region = region;
	}
	
	
	protected synchronized void initHelper() throws MalformedURLException, IOException 
	{
		helper = new HttpGatewayHelper(this.region);
		helper.connectEstablish("M1,M2,M3,M4,M5,M6", "1.0");
		helper.connectVerify();
	}
	
	protected HttpGatewayHelper getHelper()
	{
		return helper;
	}
	
	protected int getRegion()
	{
		return region;
	}

	public synchronized void refreshHelper()
	{
		helper = null;
	}
	
	
	/**
	 * 如果上一次与http gateway的请求到现在已经超过了一定的时间，
	 * 则使用一个新的HttpGatewayHelper，然后重新建立连接。
	 * @throws IOException 
	 * @throws MalformedURLException 
	 */
	protected synchronized void checkSessionRefresh() throws MalformedURLException, IOException 
	{
		if( helper==null )
		{
			initHelper();
		}
		else
		{
			long refreshTime = Long.parseLong(AppProperties.getValue("http_gateway_session_refresh_time", "10"));	// 单位为分钟
			refreshTime = refreshTime * 60 * 1000;	// 单位为秒
			// 超过了一定的时间，使用一个新的HttpGatewayHelper
			if( System.currentTimeMillis() - helper.getLastRequestTimestamp() >= refreshTime )
			{
				initHelper();
			}
		}
	}
	
	
	//---------------
	
	
	public synchronized JSONObject messagePushRegister(String url) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.messagePushRegister(url);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	

	public synchronized JSONObject login(String user, String authentication) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.login(user, authentication);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	
	public synchronized JSONObject applicationQuery() throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.applicationQuery();
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	
	//----------------
	
	
	public synchronized JSONObject hostQuery(String pool) throws MalformedURLException, IOException
	{
		try
		{
			logger.info("HttpGatewayChannel.hostQuery() > ["+Thread.currentThread().getId()+"] pool:["+pool+"]");
			checkSessionRefresh();
			JSONObject result = helper.hostQuery(pool);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	
	public synchronized JSONObject hostQueryInfo(String uuid) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.hostQueryInfo(uuid);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	
	public synchronized JSONObject hostCreationResult(String host_name) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.hostCreationResult(host_name);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	
	/**
	 * @param name: string 云主机名
	 * @param pool: string 资源池uuid
	 * @param cpu_count: uint cpu核数
	 * @param memory: uint 内存，单位：字节
	 * @param option: uint array 0否/1是,[是否使用磁盘镜像创建,是否需要数据盘,是否自动启动]
	 * @param image: string 目标磁盘镜像uuid
	 * @param disk_volume: uint array 创建磁盘容量，单位：字节，[系统磁盘，数据盘]
	 * @param port: uint array 需要开放的端口清单，[type1,port1,type2,port2,...]
	 * 		type=0  所有协议
	 * 		type=1 TCP
	 * 		type=2 UDP
	 * @param user: string 所属用户标签
	 * @param group: string 所属用户组标签
	 * @param display: string 监控验证用户名
	 * @param authentication: string 监控验证密码
	 * @param network: string 连接的虚拟网络（预留）
	 * @param inbound_bandwidth: uint 入口带宽，单位：字节
	 * @param outbound_bandwidth: uint 出口带宽，单位：字节
	 */
	public synchronized JSONObject hostCreate(
			String name, String pool, Integer cpu_count, BigInteger memory, Integer[] option, String image, 
			BigInteger[] disk_volume, Integer[] port, String user, String group, String display, String authentication, 
			String network, BigInteger inbound_bandwidth, BigInteger outbound_bandwidth
			) throws MalformedURLException, IOException
	{
		try
		{
			logger.info("HttpGatewayChannel.hostCreate() > ["+Thread.currentThread().getId()+"] region=["+region+"], " +
														"name=["+name+"], "+
														"pool=["+pool+"], "+
														"cpu_count=["+cpu_count+"], "+
														"memory=["+memory+"], "+
														"option=["+StringUtil.joinWrap(option)+"], "+
														"image=["+image+"], "+
														"disk_volume=["+StringUtil.joinWrap(disk_volume)+"], "+
														"port=["+StringUtil.joinWrap(port)+"], "+
														"user=["+user+"], "+
														"group=["+group+"], "+
														"display=["+display+"], "+
														"authentication=[******], "+
														"network=["+network+"], "+
														"inbound_bandwidth=["+inbound_bandwidth+"], "+
														"outbound_bandwidth=["+outbound_bandwidth+"]");
			checkSessionRefresh();
			JSONObject result = helper.hostCreate( name, pool, cpu_count, memory, option, image, 
										disk_volume, port, user, group, display, authentication, 
										network, inbound_bandwidth, outbound_bandwidth );
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}

	
	public synchronized JSONObject hostDelete(String uuid) throws MalformedURLException, IOException
	{
		try
		{
			logger.info("HttpGatewayChannel.hostDelete() > ["+Thread.currentThread().getId()+"] uuid=["+uuid+"]");
			checkSessionRefresh();
			JSONObject result = helper.hostDelete(uuid);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	/**
	 * 修改云主机名
	 */
	public synchronized JSONObject hostModifyName( String uuid, String name,Integer[] port) throws MalformedURLException, IOException
	{
		return helper.hostModify(	uuid, 
									name, 
									0, 
									BigInteger.ZERO, 
									new Integer[0], 
									port,
									"", 
									"", 
									"", 
									BigInteger.ZERO, 
									BigInteger.ZERO);
	}
	
	
	/**
	 * @param uuid: string 云主机唯一标识
	 * @param cpu_count: uint cpu核数
	 * @param memory: uint 内存，单位：字节
	 * @param option: uint array  0/1,[是否自动启动]
	 * @param port: uint array 需要开放的端口清单，[type1,port1,type2,port2,...]
	 * 		type=0
	 * 			所有协议
	 * 		type=1
	 * 			TCP
	 * 		type=2
	 * 			UDP
	 * @param display: string 监控验证用户名
	 * @param authentication: string 监控验证密码
	 * @param network: string 连接的虚拟网络（预留）
	 * @param inbound_bandwidth: uint 入口带宽，单位：字节
	 * @param outbound_bandwidth: uint 出口带宽，单位：字节
	 * 
	 * @return: nat uint array
	 * 
	 * 开放的端口映射，[type1,server_port1, host_port1,public_port1,type2,...]
	 * 	type:
	 * 		0=所有协议
	 * 		1=tcp
	 * 		2=udp
	 */
	public synchronized JSONObject hostModify(  String uuid, 
												String name, 
												Integer cpu_count, 
												BigInteger memory, 
												Integer[] option, 
												Integer[] port, 
												String display, 
												String authentication, 
												String network, 
												BigInteger inbound_bandwidth, 
												BigInteger outbound_bandwidth) throws MalformedURLException, IOException
	{
		try
		{
			logger.info("HttpGatewayChannel.hostModify() > ["+Thread.currentThread().getId()+"] uuid:["+uuid
															+"], name:["+name
															+"], cpu_count:["+cpu_count
															+"], memory:["+memory
															+"], option:["+StringUtil.joinWrap(option)
															+"], port:["+StringUtil.joinWrap(port)
															+"], display:["+display
															+"], authentication:["+authentication
															+"], network:["+network
															+"], inbound_bandwidth:["+inbound_bandwidth
															+"], outbound_bandwidth:["+outbound_bandwidth
															+"]");
			checkSessionRefresh();
			JSONObject result = helper.hostModify(	uuid, 
													name, 
													cpu_count, 
													memory, 
													option, 
													port, 
													display, 
													authentication, 
													network, 
													inbound_bandwidth, 
													outbound_bandwidth);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	/**
	 * @param uuid: 云主机唯一标识
	 * @param boot: 启动方法，0=硬盘启动,1=光盘启动
	 * @param image: 镜像uuid
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public synchronized JSONObject hostStart(String uuid, Integer boot, String image) throws MalformedURLException, IOException
	{
		try
		{
			logger.info("HttpGatewayChannel.hostStart() > ["+Thread.currentThread().getId()+"] uuid:["+uuid+"], boot:["+boot+"], image:["+image+"]");
			checkSessionRefresh();
			JSONObject result = helper.hostStart(uuid, boot, image);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	

	/**
	 * @param uuid: 云主机唯一标识
	 * @param boot: 启动方法，0=硬盘启动,1=光盘启动
	 * @param image: 镜像uuid
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	public synchronized JSONObject hostRestart(String uuid, Integer boot, String image) throws MalformedURLException, IOException
	{
		try
		{
			logger.info("HttpGatewayChannel.hostRestart() > ["+Thread.currentThread().getId()+"] uuid:["+uuid+"], boot:["+boot+"], image:["+image+"]");
			checkSessionRefresh();
			JSONObject result = helper.hostRestart(uuid, boot, image);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	

	public synchronized JSONObject hostStop(String uuid) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.hostStop(uuid);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	
	public synchronized JSONObject hostHalt(String uuid) throws MalformedURLException, IOException
	{
		try
		{
			logger.info("HttpGatewayChannel.hostHalt() > ["+Thread.currentThread().getId()+"] uuid:["+uuid+"]");
			checkSessionRefresh();
			JSONObject result = helper.hostHalt(uuid);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}

	/**
	 * @param uuid: string 目标主机唯一标识
	 * @param image: string 镜像uuid
	 */
	public synchronized JSONObject hostInsertMedia(String uuid, String image) throws MalformedURLException, IOException
	{
		try
		{
			logger.info("HttpGatewayChannel.hostInsertMedia() > ["+Thread.currentThread().getId()+"] uuid:["+uuid+"], image:["+image+"]");
			
			checkSessionRefresh();
			JSONObject result = helper.hostInsertMedia(uuid, image);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	/**
	 * @param uuid: string 目标主机唯一标识
	 * @param image: string 镜像uuid
	 */
	public synchronized JSONObject hostChangeMedia(String uuid, String image) throws MalformedURLException, IOException
	{
		try
		{
			logger.info("HttpGatewayChannel.hostChangeMedia() > ["+Thread.currentThread().getId()+"] uuid:["+uuid+"], image:["+image+"]");
			
			checkSessionRefresh();
			JSONObject result = helper.hostChangeMedia(uuid, image);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	/**
	 * @param uuid: string 目标主机唯一标识
	 */
	public synchronized JSONObject hostEjectMedia(String uuid) throws MalformedURLException, IOException
	{
		try
		{
			logger.info("HttpGatewayChannel.hostEjectMedia() > ["+Thread.currentThread().getId()+"] uuid:["+uuid+"]");
			
			checkSessionRefresh();
			JSONObject result = helper.hostEjectMedia(uuid);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}

	/**
		uuid
			string
			云主机唯一标识
		disk_volume
			uint
			新增磁盘容量，单位：字节
		disk_type
			uint
			磁盘模式，0=本地，1=云存储
		disk_source
			string
			云存储时，对应的存储资源池uuid
		mode
			uint
			磁盘格式
				0=raw，裸盘
	 */
	public synchronized JSONObject hostAttachDisk(String uuid, BigInteger disk_volume, Integer disk_type, String disk_source, Integer mode) throws MalformedURLException, IOException
	{
		try
		{
			logger.info("HttpGatewayChannel.hostAttachDisk() > ["+Thread.currentThread().getId()+"] uuid:["+uuid+"], disk_volume:["+disk_volume+"], disk_type:["+disk_type+"], disk_source:["+disk_source+"], mode:["+mode+"]");
			
			checkSessionRefresh();
			JSONObject result = helper.hostAttachDisk(uuid, disk_volume, disk_type, disk_source, mode);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}

	/**
		uuid
			string
			云主机唯一标识
		index
			uint
			数据磁盘索引，0代表第一个数据磁盘
	 */
	public synchronized JSONObject hostDetachDisk(String uuid, Integer index) throws MalformedURLException, IOException
	{
		try
		{
			logger.info("HttpGatewayChannel.hostDetachDisk() > ["+Thread.currentThread().getId()+"] uuid:["+uuid+"], index:["+index+"]");
			
			checkSessionRefresh();
			JSONObject result = helper.hostDetachDisk(uuid, index);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	public synchronized JSONObject hostStartMonitor(String[] target, String callback) throws MalformedURLException, IOException {
		try {
			this.checkSessionRefresh();
			JSONObject result = helper.hostStartMonitor(target, callback);
			
			if (HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result))) {
				helper = null;
			}
			
			return result;
		} catch (IOException e) {
			helper = null;
			throw e;
		}
	}
	
	public synchronized JSONObject hostStopMonitor(Integer task) throws MalformedURLException, IOException {
		try {
			this.checkSessionRefresh();
			JSONObject result = helper.hostStopMonitor(task);
			
			if (HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result))) {
				helper = null;
			}
			
			return result;
		} catch (IOException e) {
			throw e;
		}
	}
	
	//---------------
	

//	public synchronized JSONObject diskQuery(String pool) throws MalformedURLException, IOException
//	{
//		try
//		{
//			logger.info("HttpGatewayChannel.diskQuery() > ["+Thread.currentThread().getId()+"] pool:["+pool+"]");
//			
//			checkSessionRefresh();
//			JSONObject result = helper.diskQuery(pool);
//			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
//			{
//				helper = null;
//			}
//			return result;
//		}
//		catch (IOException e)
//		{
//			helper = null;
//			throw e;
//		}
//	}
//	
//
//	public synchronized JSONObject diskCreate(String name, String pool, String disk_volume, String count) throws MalformedURLException, IOException
//	{
//		try
//		{
//			checkSessionRefresh();
//			JSONObject result = helper.diskCreate(name, pool, disk_volume, count);
//			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
//			{
//				helper = null;
//			}
//			return result;
//		}
//		catch (IOException e)
//		{
//			helper = null;
//			throw e;
//		}
//	}
//	
//
//	public synchronized JSONObject diskDelete(String uuid) throws MalformedURLException, IOException
//	{
//		try
//		{
//			checkSessionRefresh();
//			JSONObject result = helper.diskDelete(uuid);
//			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
//			{
//				helper = null;
//			}
//			return result;
//		}
//		catch (IOException e)
//		{
//			helper = null;
//			throw e;
//		}
//	}
	
	//---------------
	

	public synchronized JSONObject cloudTerminalQuery() throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.cloudTerminalQuery();
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	

	public synchronized JSONObject cloudTerminalAdd(String uuid) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.cloudTerminalAdd(uuid);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	

	public synchronized JSONObject cloudTerminalRemove(String uuid) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.cloudTerminalRemove(uuid);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	public synchronized JSONObject cloudTerminalBind(String uuid, String host) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.cloudTerminalBind(uuid, host);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	//---------------
	
	
	public synchronized JSONObject diskImageQuery() throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.diskImageQuery();
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	/**
	 * @param name:        string 镜像名称
	 * @param uuid:        string target host uuid
	 * @param description: string 镜像描述
	 * @param identity:    string array 标签列表
	 * @param group:       string "system"
	 * @param user:        string "system"
	 */
	public synchronized JSONObject diskImageCreate(String name, String uuid, String description, String[] identity, String group, String user) throws MalformedURLException, IOException
	{
		try
		{
			logger.info("HttpGatewayChannel.diskImageCreate() > ["+Thread.currentThread().getId()+"] name:["+name+"], uuid:["+uuid+"], description:["+description+"], identity:["+StringUtil.joinWrap(identity)+"], group:["+group+"], user:["+user+"]");
			checkSessionRefresh();
			JSONObject result = helper.diskImageCreate(name, uuid, description, identity, group, user, null);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	public synchronized JSONObject diskImageCreateAsync(String name, String uuid, String description, String[] identity, String group, String user, String callback) throws MalformedURLException, IOException {
		try
		{
			logger.info("["+Thread.currentThread().getId()+"]  create disk image. Parameters - name:["+name+"], uuid:["+uuid+"], callback:[" + callback + "]");
			checkSessionRefresh();
			JSONObject result = helper.diskImageCreate(name, uuid, description, identity, group, user, callback);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	

	public synchronized JSONObject diskImageDelete(String uuid) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.diskImageDelete(uuid);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	

	public synchronized JSONObject diskImageModify(String uuid, String name, String description, String[] identity) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.diskImageModify(uuid, name, description, identity);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	
	//-----------
	

	public synchronized JSONObject isoImageQuery() throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.isoImageQuery();
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	

	public synchronized JSONObject isoImageUpload(String name, String target, String description, String group, String user, String callback) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.isoImageUpload(name, target, description, group, user, callback);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	

	public synchronized JSONObject isoImageModify(String uuid, String name, String description) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.isoImageModify(uuid, name, description);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	

	public synchronized JSONObject isoImageDelete(String uuid) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.isoImageDelete(uuid);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	//----------
	

	public synchronized JSONObject resourcePoolQuery() throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.resourcePoolQuery();
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	//------------
	

	public synchronized JSONObject computePoolQuery() throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.computePoolQuery();
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	
	public synchronized JSONObject computePoolCreate(String name, int network_type, String network, int disk_type, String disk_source) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.computePoolCreate(name, network_type, network, disk_type, disk_source);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	
	public synchronized JSONObject computePoolDelete(String uuid) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.computePoolDelete(uuid);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	

	public synchronized JSONObject computePoolAddResource(String pool, String name) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.computePoolAddResource(pool, name);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	

	public synchronized JSONObject computePoolRemoveResource(String pool, String name) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.computePoolRemoveResource(pool, name);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	

	public synchronized JSONObject computePoolQueryResource(String pool) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.computePoolQueryResource(pool);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	//-----------
	

	public synchronized JSONObject storagePoolQuery() throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.storagePoolQuery();
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	public synchronized JSONObject storagePoolCreate(String name) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.storagePoolCreate(name);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	public synchronized JSONObject storagePoolModify(String uuid, String name) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.storagePoolModify(uuid, name);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}	

	public synchronized JSONObject storagePoolDelete(String uuid) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.storagePoolDelete(uuid);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	

	public synchronized JSONObject storagePoolAddResource(String pool, String name) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.storagePoolAddResource(pool, name);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	

	public synchronized JSONObject storagePoolRemoveResource(String pool, String name) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.storagePoolRemoveResource( pool,  name);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	

	public synchronized JSONObject storagePoolQueryResource(String pool) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.storagePoolQueryResource(pool);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	//----------
	
	
	public synchronized JSONObject addressPoolQuery() throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.addressPoolQuery();
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	

	public synchronized JSONObject addressPoolCreate(String name) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.addressPoolCreate(name);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	

	public synchronized JSONObject addressPoolDelete(String uuid) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.addressPoolDelete(uuid);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	

	public synchronized JSONObject addressPoolAddResource(String pool, String ip, String range) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.addressPoolAddResource( pool,  ip,  range);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	
	public synchronized JSONObject addressPoolRemoveResource(String pool, String ip) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.addressPoolRemoveResource( pool,  ip);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	

	public synchronized JSONObject addressPoolQueryResource(String pool) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.addressPoolQueryResource(pool);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	//----------
	

	public synchronized JSONObject portPoolQuery() throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.portPoolQuery();
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	

	public synchronized JSONObject portPoolCreate(String name) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.portPoolCreate(name);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	

	public synchronized JSONObject portPoolDelete(String uuid) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.portPoolDelete(uuid);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	

	public synchronized JSONObject portPoolAddResource(String pool, String ip, String port) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.portPoolAddResource( pool,  ip,  port);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	
	public synchronized JSONObject portPoolRemoveResource(String pool, String ip) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.portPoolRemoveResource( pool,  ip);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	

	public synchronized JSONObject portPoolQueryResource(String pool) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.portPoolQueryResource(pool);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	//-----------
	

	public synchronized JSONObject structQuery() throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.structQuery();
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	//---------
	

	public synchronized JSONObject serverRoomQuery() throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.serverRoomQuery();
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	

	public synchronized JSONObject serverRoomCreate(String name) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.serverRoomCreate(name);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	
	public synchronized JSONObject serverRoomDelete(String uuid) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.serverRoomDelete(uuid);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	
	public synchronized JSONObject serverRoomModify(String uuid, String name) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.serverRoomModify(uuid, name);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	//----------------

	public synchronized JSONObject serverRackQuery(String room) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.serverRackQuery(room);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	

	public synchronized JSONObject serverRackCreate(String name, String room) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.serverRackCreate( name,  room);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	

	public synchronized JSONObject serverRackDelete(String uuid) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.serverRackDelete(uuid);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	

	public synchronized JSONObject serverRackModify(String uuid, String name, String room) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.serverRackModify( uuid,  name,  room);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	//-----------
	

	public synchronized JSONObject serverQuery(String rack) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.serverQuery(rack);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	

	public synchronized JSONObject serverAdd(String name, String rack, String ethernet_address) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.serverAdd( name,  rack,  ethernet_address);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	

	public synchronized JSONObject serverRemove(String uuid) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.serverRemove(uuid);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	

	public synchronized JSONObject serverModify(String uuid, String name, String rack) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.serverModify( uuid,  name,  rack);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	

	public synchronized JSONObject serverReboot(String uuid) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.serverReboot(uuid);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	

	public synchronized JSONObject serverShutdown(String uuid) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.serverShutdown(uuid);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	
	//----------
	
	
	public synchronized JSONObject serviceTypeQuery() throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.serviceTypeQuery();
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	

	public synchronized JSONObject serviceGroupQuery(int type) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.serviceGroupQuery(type);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	
	public synchronized JSONObject serviceQuery(int type, String group) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.serviceQuery(type, group);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	

	public synchronized JSONObject serviceRestart(String name) throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.serviceRestart(name);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	
	//----------
	

	public synchronized JSONObject statisticsQuery() throws MalformedURLException, IOException
	{
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.statisticsQuery();
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	//----------------
	
	public synchronized JSONObject deviceQuery(String pool) throws MalformedURLException, IOException {
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.deviceQuery(pool, 0);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	public synchronized JSONObject deviceCreate(String name, String pool, BigInteger diskVolume, int page, int replication, 
			Integer[] option, int diskType, String user, String authentication, String crypt, String snapshot) throws MalformedURLException, IOException {
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.deviceCreate(name, pool, diskVolume, page, replication, option, diskType, user, authentication, crypt, snapshot);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	public synchronized JSONObject deviceDelete(String uuid) throws MalformedURLException, IOException {
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.deviceDelete(uuid);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	public synchronized JSONObject deviceModify(String uuid, String name,  
			Integer[] option, int diskType, String user, String authentication, String snapshot) throws MalformedURLException, IOException {
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.deviceModify(uuid, name, option, diskType, user, authentication, snapshot);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	public synchronized JSONObject forwarderAdd(String target, int type, int networkType, String networkSource) throws MalformedURLException, IOException {
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.forwarderAdd(target, type, networkType, networkSource);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	public synchronized JSONObject forwarderRemove(String target, int type, String uuid) throws MalformedURLException, IOException {
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.forwarderRemove(target, type, uuid);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)) )
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	public synchronized JSONObject fileUpload(String param, String fileName, InputStream fileStream) throws MalformedURLException, IOException {
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.fileUpload(param, fileName, fileStream);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)))
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	public synchronized JSONObject networkCreate(String name, int netmask, String description, String pool, String callback) throws MalformedURLException, IOException {
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.networkCreate(name, netmask, description, pool, callback);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)))
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	public synchronized JSONObject networkQuery(String callback) throws MalformedURLException, IOException {
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.networkQuery(callback);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)))
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	public synchronized JSONObject networkModify(String uuid, String name, String description, String callback) throws MalformedURLException, IOException {
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.networkModify(uuid, name, description, callback);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)))
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	public synchronized JSONObject networkDetail(String uuid, String callback) throws MalformedURLException, IOException {
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.networkDetail(uuid, callback);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)))
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	public synchronized JSONObject networkStart(String uuid, String callback) throws MalformedURLException, IOException {
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.networkStart(uuid, callback);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)))
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	public synchronized JSONObject networkStop(String uuid, String callback) throws MalformedURLException, IOException {
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.networkStop(uuid, callback);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)))
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	public synchronized JSONObject networkDelete(String uuid, String callback) throws MalformedURLException, IOException {
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.networkDelete(uuid, callback);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)))
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	public synchronized JSONObject networkQueryHost(String uuid, String callback) throws MalformedURLException, IOException {
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.networkQueryHost(uuid, callback);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)))
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	public synchronized JSONObject networkAttachHost(String uuid, String hostUuid, String callback) throws MalformedURLException, IOException {
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.networkAttachHost(uuid, hostUuid, callback);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)))
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	public synchronized JSONObject networkDetachHost(String uuid, String hostUuid, String callback) throws MalformedURLException, IOException {
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.networkDetachHost(uuid, hostUuid, callback);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)))
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	public synchronized JSONObject networkAttachAddress(String uuid, int count, String callback) throws MalformedURLException, IOException {
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.networkAttachAddress(uuid, count, callback);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)))
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	public synchronized JSONObject networkDetachAddress(String uuid, String[] ip, String callback) throws MalformedURLException, IOException {
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.networkDetachAddress(uuid, ip, callback);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)))
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	public synchronized JSONObject networkBindPort(String uuid, String[] protocolList, String[] ipList, String[] portList, String[] hostList, String[] hostPortList, String callback) throws MalformedURLException, IOException {
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.networkBindPort(uuid, protocolList, ipList, portList, hostList, hostPortList, callback);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)))
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	public synchronized JSONObject networkUnbindPort(String uuid, String[] protocolList, String[] ipList, String[] portList, String callback) throws MalformedURLException, IOException {
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.networkUnbindPort(uuid, protocolList, ipList, portList, callback);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)))
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	public synchronized JSONObject startSystemMonitor(String callback) throws MalformedURLException, IOException {
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.startSystemMonitor(callback);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)))
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	public synchronized JSONObject stopSystemMonitor(int task) throws MalformedURLException, IOException {
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.stopSystemMonitor(task);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)))
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	public synchronized JSONObject snapshotPoolQuery(String callback) throws MalformedURLException, IOException {
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.snapshotPoolQuery(callback);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)))
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	public synchronized JSONObject snapshotPoolCreate(String name, String callback) throws MalformedURLException, IOException {
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.snapshotPoolCreate(name, callback);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)))
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	public synchronized JSONObject snapshotPoolModify(String uuid, String name, String callback) throws MalformedURLException, IOException {
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.snapshotPoolModify(uuid, name, callback);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)))
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	public synchronized JSONObject snapshotPoolDelete(String uuid, String callback) throws MalformedURLException, IOException {
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.snapshotPoolDelete(uuid, callback);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)))
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	public synchronized JSONObject snapshotPoolAddNode(String pool, String nodeName, String callback) throws MalformedURLException, IOException {
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.snapshotPoolAddNode(pool, nodeName, callback);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)))
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	public synchronized JSONObject snapshotPoolRemoveNode(String pool, String nodeName, String callback) throws MalformedURLException, IOException {
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.snapshotPoolRemoveNode(pool, nodeName, callback);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)))
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	public synchronized JSONObject snapshotPoolQueryNode(String pool, String callback) throws MalformedURLException, IOException {
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.snapshotPoolQueryNode(pool, callback);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)))
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	public synchronized JSONObject snapshotQuery(String uuid, String callback) throws MalformedURLException, IOException {
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.snapshotQuery(uuid, callback);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)))
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	public synchronized JSONObject snapshotCreate(String target, String name, String callback) throws MalformedURLException, IOException {
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.snapshotCreate(target, name, callback);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)))
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	public synchronized JSONObject snapshotDelete(String uuid, String callback) throws MalformedURLException, IOException {
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.snapshotDelete(uuid, callback);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)))
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
	public synchronized JSONObject snapshotResume(String uuid, String callback) throws MalformedURLException, IOException {
		try
		{
			checkSessionRefresh();
			JSONObject result = helper.snapshotResume(uuid, callback);
			if( HttpGatewayReturnCode.SESSION_NOT_FOUND.equals(HttpGatewayResponseHelper.getReturnCode(result)))
			{
				helper = null;
			}
			return result;
		}
		catch (IOException e)
		{
			helper = null;
			throw e;
		}
	}
	
}
