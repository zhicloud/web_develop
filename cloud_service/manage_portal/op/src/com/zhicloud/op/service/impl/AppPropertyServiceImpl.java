package com.zhicloud.op.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.op.app.authorization.PrivilegeConstant;
import com.zhicloud.op.app.helper.LoginHelper;
import com.zhicloud.op.app.helper.RegionHelper;
import com.zhicloud.op.app.helper.RegionHelper.RegionData;
import com.zhicloud.op.app.propeties.AppProperties;
import com.zhicloud.op.common.util.CapacityUtil;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.httpGateway.HttpGatewayManager;
import com.zhicloud.op.httpGateway.HttpGatewayReceiveChannel;
import com.zhicloud.op.login.LoginInfo;
import com.zhicloud.op.mybatis.mapper.CpuPackageOptionMapper;
import com.zhicloud.op.mybatis.mapper.MemoryPackageOptionMapper;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.request.RequestContext;
import com.zhicloud.op.service.AppPropertyService;
import com.zhicloud.op.vo.CpuPackageOptionVO;
import com.zhicloud.op.vo.MemoryPackageOptionVO;

@Transactional(readOnly=true)
public class AppPropertyServiceImpl extends BeanDirectCallableDefaultImpl implements AppPropertyService 
{
	
	public static final Logger logger = Logger.getLogger(AppPropertyServiceImpl.class);
	
	
	private SqlSession sqlSession;
	
	
	public void setSqlSession(SqlSession sqlSession)
	{
		this.sqlSession = sqlSession;
	}

	//---------------
	
	@Callable
	public String appPropertiesManagePage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("AppPropertyServiceImpl.appPropertiesManagePage");
		

		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request, 1);
		if (loginInfo.hasPrivilege(PrivilegeConstant.app_porpertise_manage_page) == false)
		{
			return "/public/have_not_access.jsp";
		}
		

		CpuPackageOptionMapper cpuPackageOptionMapper             = this.sqlSession.getMapper(CpuPackageOptionMapper.class);
		MemoryPackageOptionMapper memoryPackageOptionMapper       = this.sqlSession.getMapper(MemoryPackageOptionMapper.class);
		
		// 获取CPU套餐项
		List<CpuPackageOptionVO>  cpuOptions = cpuPackageOptionMapper.getAll();
		List<Integer> cores = new ArrayList<Integer>();
		for(CpuPackageOptionVO cpuOption : cpuOptions){
			cores.add(cpuOption.getCore());
		}
		int cpu = Collections.max(cores);
		request.setAttribute("cpu", cpu);
		
		
		// 获取内存套餐项
		List<MemoryPackageOptionVO> memoryOptions = memoryPackageOptionMapper.getAll();
		List<BigInteger>memorys = new ArrayList<BigInteger>();
		for(MemoryPackageOptionVO memoryOption : memoryOptions){
			memorys.add(memoryOption.getMemory());
		}
		
		String memoryStr = CapacityUtil.toGB(Collections.max(memorys), 0);
		
		
		int memory = Integer.parseInt(memoryStr.replaceAll("GB", ""));
		
		
		request.setAttribute("memory", memory);
		

		return "/security/admin/app_properties_manage.jsp";
	}
	
	@Callable
	public MethodResult updateAppPropertyService(Map<String, String> parameter)
	{
		logger.debug("AppPropertyServiceImpl.updateAppPropertyService()");
		try
		{
			String addressOfThisSystem      = StringUtil.trim(parameter.get("address_of_this_system"));
			String cpuUpper                 = StringUtil.trim(parameter.get("cpu_upper"));
			String memoryUpper              = StringUtil.trim(parameter.get("memory_upper"));
			String uploadFileUpper          = StringUtil.trim(parameter.get("upload_file_upper"));
			String uploadTotalFileUpper     = StringUtil.trim(parameter.get("upload_total_file_upper"));
			String cashUpper                = StringUtil.trim(parameter.get("cash_upper"));
			String versionName              = StringUtil.trim(parameter.get("version_name"));
			String monitorMail              = StringUtil.trim(parameter.get("monitor_mail"));
			String supportEmail  			= StringUtil.trim(parameter.get("support_email"));
			String monitorPhone1            = StringUtil.trim(parameter.get("monitor_phone1"));
			String monitorPhone2            = StringUtil.trim(parameter.get("monitor_phone2"));
			String monitorPhone3            = StringUtil.trim(parameter.get("monitor_phone3"));
			String versionType              = StringUtil.trim(parameter.get("version_type"));
			String notification_on_off      = StringUtil.trim(parameter.get("notification_on_off"));
			String cdMaxPorts               = StringUtil.trim(parameter.get("cd_max_ports"));
			String addressOfIscsiGateway1   = StringUtil.trim(parameter.get("address_of_iscsi_gateway_1"));
			String addressOfIscsiGateway2   = StringUtil.trim(parameter.get("address_of_iscsi_gateway_2"));
			String addressOfIscsiGateway4   = StringUtil.trim(parameter.get("address_of_iscsi_gateway_4"));
			String serverRequest            = StringUtil.trim(parameter.get("server_monitor_request"));
			
			// 判断格式
			if( StringUtil.isBlank(addressOfThisSystem) )
			{
				return new MethodResult(MethodResult.FAIL, "'本系统即运营管理平台地址'的值不能为空");
			}
			if( StringUtil.isBlank(cpuUpper) )
			{
				return new MethodResult(MethodResult.FAIL, "最大CPU核心数不能为空");
			}
			if( StringUtil.isBlank(memoryUpper) )
			{
				return new MethodResult(MethodResult.FAIL, "最大内存不能为空");
			}
			if( StringUtil.isBlank(uploadFileUpper) )
			{
				return new MethodResult(MethodResult.FAIL, "单个上传文件大小不能为空");
			}
			if( StringUtil.isBlank(uploadTotalFileUpper) )
			{
				return new MethodResult(MethodResult.FAIL, "最大的可用空间不能为空");
			}
			if( StringUtil.isBlank(cashUpper) )
			{
				return new MethodResult(MethodResult.FAIL, "代金券最大额度不能为空");
			}
			if (StringUtil.isBlank(versionName)) 
			{
				return new MethodResult(MethodResult.FAIL, "致云客户端不能为空");
			}
			if (StringUtil.isBlank(monitorMail)) 
			{
				return new MethodResult(MethodResult.FAIL, "监控手机不能为空");
			}
			if (StringUtil.isBlank(monitorPhone1)) 
			{
				return new MethodResult(MethodResult.FAIL, "监控手机不能为空");
			}
			if (StringUtil.isBlank(monitorPhone2)) 
			{
				return new MethodResult(MethodResult.FAIL, "监控手机不能为空");
			}
			if (StringUtil.isBlank(monitorPhone3)) 
			{
				return new MethodResult(MethodResult.FAIL, "监控通知邮件不能为空");
			}
			if (StringUtil.isBlank(supportEmail)) 
			{
				return new MethodResult(MethodResult.FAIL, "服务邮箱不能为空");
			}
			if (StringUtil.isBlank(versionType)) 
			{
				return new MethodResult(MethodResult.FAIL, "发布包类型不能为空");
			}
			if (StringUtil.isBlank(cdMaxPorts)) 
			{
				return new MethodResult(MethodResult.FAIL, "成都主机最大端口数不能为空");
			}
			if (StringUtil.isBlank(addressOfIscsiGateway1)) 
			{
				return new MethodResult(MethodResult.FAIL, "广州地域存储网关IP不能为空");
			}
			if (StringUtil.isBlank(addressOfIscsiGateway2)) 
			{
				return new MethodResult(MethodResult.FAIL, "成都地域存储网关IP不能为空");
			}
			if (StringUtil.isBlank(addressOfIscsiGateway4)) 
			{
				return new MethodResult(MethodResult.FAIL, "香港地域存储网关IP不能为空");
			}
			if (StringUtil.isBlank(serverRequest)) 
			{
				return new MethodResult(MethodResult.FAIL, "http server地址不能为空");
			} 
			
			String oldAddressOfThisSystem = AppProperties.getValue("address_of_this_system");
			
			AppProperties.setValue("address_of_this_system",            addressOfThisSystem);
			AppProperties.setValue("cpu_package_option_upper_limit",    cpuUpper);
			AppProperties.setValue("memory_package_option_upper_limit", memoryUpper);
			AppProperties.setValue("upload_file_upper_limit",           uploadFileUpper);
			AppProperties.setValue("upload_total_file_upper_limit",     uploadTotalFileUpper);
			AppProperties.setValue("cash_upper_limit",                  cashUpper);
			AppProperties.setValue("client_version_name",               versionName);
			AppProperties.setValue("monitor_mail",               		monitorMail);
			AppProperties.setValue("support_email",               		supportEmail);
			AppProperties.setValue("monitor_phone1",              	 	monitorPhone1);
			AppProperties.setValue("monitor_phone2",               		monitorPhone2);
			AppProperties.setValue("monitor_phone3",               		monitorPhone3);
			AppProperties.setValue("version_type",               		versionType);
			AppProperties.setValue("notification_on_off",               notification_on_off);
			AppProperties.setValue("cd_max_ports",               		cdMaxPorts);
			AppProperties.setValue("address_of_iscsi_gateway_4",        addressOfIscsiGateway4);
			AppProperties.setValue("address_of_iscsi_gateway_2",        addressOfIscsiGateway2);
			AppProperties.setValue("address_of_iscsi_gateway_1",        addressOfIscsiGateway1);
			AppProperties.setValue("server_monitor_request",               		serverRequest);

			AppProperties.save();
			
			//
//			
			if(!oldAddressOfThisSystem.equals(addressOfThisSystem)){ 
				
				//停止所有线程
				Map<String, HttpGatewayReceiveChannel> map = HttpGatewayManager.getAll().getMap();
				 
				for (Map.Entry<String, HttpGatewayReceiveChannel> entry : map.entrySet()) {  
					
					HttpGatewayReceiveChannel val = (HttpGatewayReceiveChannel) entry.getValue();
					val.setFlag(false); 
		            System.out.println("key = " + entry.getKey() + " and value = " + entry.getValue());  
		        } 
				//清除所有的channel
				map.clear();
				
				HttpServletRequest request = RequestContext.getHttpRequest();
				ServletContextEvent event = new ServletContextEvent(request.getSession().getServletContext());
				Document doc = new SAXReader().read(event.getServletContext().getResource("/META-INF/regions.xml"));
				Element root = doc.getRootElement();
				Iterator<Element> regionIter = root.elementIterator("region");
				while (regionIter.hasNext())
				{
					Element paramElement = regionIter.next();
					
					// 构造region数据
					RegionData regionData = new RegionData();
					regionData.setId(Integer.valueOf(paramElement.attributeValue("id")));
					regionData.setName(paramElement.attributeValue("name"));
					regionData.setHttpGatewayAddr(paramElement.attributeValue("http_gateway_addr"));
					RegionHelper.singleton.putRegionData(regionData);
					
					// 向http gateway注册消息推送地址 
					if( addressOfThisSystem.endsWith("/")==false )
					{
						addressOfThisSystem += "/";
					}
					String registerUrl = addressOfThisSystem + "hgMessage/receive.do";
					HttpGatewayReceiveChannel receiveChannel = new HttpGatewayReceiveChannel(regionData.getId()); 
					receiveChannel.messagePushRegisterThreadly(registerUrl); 
				}
			}  
			return new MethodResult(MethodResult.SUCCESS, "保存成功");
		}
		catch( Exception e )
		{
			logger.error("AppPropertyServiceImpl.updateAppPropertyService()",e);
			throw new AppException("保存失败");
		}
	}
	
}

















