/**
 * Project Name:op
 * File Name:VpnServiceImpl.java
 * Package Name:com.zhicloud.op.service.impl
 * Date:2015年4月1日下午2:16:34
 * 
 *
*/ 

package com.zhicloud.op.service.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.op.app.authorization.PrivilegeConstant;
import com.zhicloud.op.app.helper.CloudHostBillingHelper;
import com.zhicloud.op.app.helper.CloudHostPrice;
import com.zhicloud.op.app.helper.CloudHostWarehouseHelper;
import com.zhicloud.op.app.helper.CountUserProductsPriceHelper;
import com.zhicloud.op.app.helper.LoginHelper;
import com.zhicloud.op.app.helper.ServiceHelper;
import com.zhicloud.op.app.helper.VPCAmountAndPrice;
import com.zhicloud.op.app.helper.VpcBillHelper;
import com.zhicloud.op.app.listener.WarehouseCloudHostCreationListener;
import com.zhicloud.op.app.pool.CloudHostPoolManager;
import com.zhicloud.op.app.pool.addressPool.AddressExt;
import com.zhicloud.op.app.pool.addressPool.AddressPool;
import com.zhicloud.op.app.pool.addressPool.AddressPoolManager;
import com.zhicloud.op.app.pool.network.NetworkInfoExt;
import com.zhicloud.op.app.pool.network.NetworkInfoExt.Host;
import com.zhicloud.op.app.propeties.AppProperties;
import com.zhicloud.op.common.util.CapacityUtil;
import com.zhicloud.op.common.util.DateUtil;
import com.zhicloud.op.common.util.FlowUtil;
import com.zhicloud.op.common.util.RandomPassword;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.common.util.json.JSONLibUtil;
import com.zhicloud.op.core.CoreSpringContextManager;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.httpGateway.HttpGatewayChannelExt;
import com.zhicloud.op.httpGateway.HttpGatewayManager;
import com.zhicloud.op.httpGateway.HttpGatewayResponseHelper;
import com.zhicloud.op.login.LoginInfo;
import com.zhicloud.op.mybatis.mapper.AgentMapper;
import com.zhicloud.op.mybatis.mapper.BandwidthPackageOptionMapper;
import com.zhicloud.op.mybatis.mapper.CloudHostBillDetailMapper;
import com.zhicloud.op.mybatis.mapper.CloudHostMapper;
import com.zhicloud.op.mybatis.mapper.CloudHostShoppingConfigMapper;
import com.zhicloud.op.mybatis.mapper.CloudHostSysDefaultPortsMapper;
import com.zhicloud.op.mybatis.mapper.CpuPackageOptionMapper;
import com.zhicloud.op.mybatis.mapper.DiskPackageOptionMapper;
import com.zhicloud.op.mybatis.mapper.MarkMapper;
import com.zhicloud.op.mybatis.mapper.MemoryPackageOptionMapper;
import com.zhicloud.op.mybatis.mapper.OperLogMapper;
import com.zhicloud.op.mybatis.mapper.OrderDetailMapper;
import com.zhicloud.op.mybatis.mapper.OrderInfoMapper;
import com.zhicloud.op.mybatis.mapper.PackagePriceMapper;
import com.zhicloud.op.mybatis.mapper.SysDiskImageMapper;
import com.zhicloud.op.mybatis.mapper.SysUserMapper;
import com.zhicloud.op.mybatis.mapper.TerminalUserMapper;
import com.zhicloud.op.mybatis.mapper.VpcBaseInfoMapper;
import com.zhicloud.op.mybatis.mapper.VpcBillDetailMapper;
import com.zhicloud.op.mybatis.mapper.VpcBindHostMapper;
import com.zhicloud.op.mybatis.mapper.VpcBindPortMapper;
import com.zhicloud.op.mybatis.mapper.VpcOuterIpMapper;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.CallWithoutLogin;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.request.RequestContext;
import com.zhicloud.op.service.AccountBalanceService;
import com.zhicloud.op.service.CloudHostService;
import com.zhicloud.op.service.NetworkService;
import com.zhicloud.op.service.ResourcePoolService;
import com.zhicloud.op.service.VpcService;
import com.zhicloud.op.service.constant.AppConstant;
import com.zhicloud.op.vo.AgentVO;
import com.zhicloud.op.vo.BandwidthPackageOptionVO;
import com.zhicloud.op.vo.CloudHostSysDefaultPortsVO;
import com.zhicloud.op.vo.CloudHostVO;
import com.zhicloud.op.vo.CloudHostWarehouseDetailVO;
import com.zhicloud.op.vo.CpuPackageOptionVO;
import com.zhicloud.op.vo.DiskPackageOptionVO;
import com.zhicloud.op.vo.MarkVO;
import com.zhicloud.op.vo.MemoryPackageOptionVO;
import com.zhicloud.op.vo.PackagePriceVO;
import com.zhicloud.op.vo.PriceVO;
import com.zhicloud.op.vo.SysDiskImageVO;
import com.zhicloud.op.vo.SysUserVO;
import com.zhicloud.op.vo.TerminalUserVO;
import com.zhicloud.op.vo.VpcBaseInfoVO;
import com.zhicloud.op.vo.VpcBindHostVO;
import com.zhicloud.op.vo.VpcBindPortVO;
import com.zhicloud.op.vo.VpcOuterIpVO;
 
/**
 * ClassName: VpnServiceImpl  
 * date: 2015年4月1日 下午2:16:34 
 *
 * @author sasa
 * @version 
 * @since JDK 1.7
 */
@Transactional(readOnly = true)
public class VpcServiceImpl extends BeanDirectCallableDefaultImpl implements VpcService {
	public static final Logger logger = Logger.getLogger(SuggestionServiceImpl.class);
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	private SqlSession sqlSession;
	
	public void setSqlSession(SqlSession sqlSession)
	{
		this.sqlSession = sqlSession;
	}

	/**
	 * 跳转到我的VPC页面
	 * @see com.zhicloud.op.service.VpcService#managePage(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Callable
	public String managePage(HttpServletRequest request,HttpServletResponse response) {
		String region = StringUtil.trim(request.getParameter("region"));
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request, AppConstant.SYS_USER_TYPE_TERMINAL_USER);
		VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class); 
		Map<String, Object> data = new LinkedHashMap<String, Object>();
		data.put("userId",loginInfo.getUserId());
		if(!StringUtil.isBlank(region)){			
			data.put("region",region);
		}
		List<VpcBaseInfoVO> vpcList = vpcBaseInfoMapper.queryUserVpcsByUserId(data);
		List<VpcBaseInfoVO> vpcListNew = new ArrayList<VpcBaseInfoVO> ();
		for(VpcBaseInfoVO vpc : vpcList){
			if(vpc.getStatus() == 1){
				vpcListNew.add(vpc);
				continue;
			}
			if(vpc.getStatus() == 2){				
				Date date = null;
				try {
					date = StringUtil.stringToDate(vpc.getModifyTime(),"yyyyMMddHHmmssSSS");
				} catch (ParseException e) {
					e.printStackTrace();
				}
				if(date == null){
					date = new Date();
				}
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				calendar.add(Calendar.DAY_OF_MONTH,7);
				String lastTime = StringUtil.dateToString(calendar.getTime(),"yyyyMMddHHmmssSSS");
				String nowTime = StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS");
				if(Long.parseLong(nowTime) - Long.parseLong(lastTime) > 0){
					continue;
				}else{
					vpcListNew.add(vpc);
				}
			}
		}
		
		request.setAttribute("vpcList", vpcListNew);
		if(!StringUtil.isBlank(region)){				
			request.setAttribute("region", region);
		}else{			
			request.setAttribute("region", "");
		}
 		return "/security/user/my_vpc.jsp";
	}

	/**
	 * 跳转到创建VPC页面
	 * @see com.zhicloud.op.service.VpcService#createVpnPage(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Callable
	@CallWithoutLogin
	public String createVpnPage(HttpServletRequest request, HttpServletResponse response) {
		CpuPackageOptionMapper cpuPackageOptionMapper       = this.sqlSession.getMapper(CpuPackageOptionMapper.class);
		MemoryPackageOptionMapper memoryPackageOptionMapper = this.sqlSession.getMapper(MemoryPackageOptionMapper.class);
		DiskPackageOptionMapper diskPackageOptionMapper     = this.sqlSession.getMapper(DiskPackageOptionMapper.class);
		SysDiskImageMapper sysDiskImageMapper               = this.sqlSession.getMapper(SysDiskImageMapper.class); 
		BandwidthPackageOptionMapper bandwidthPackageOptionMapper = this.sqlSession.getMapper(BandwidthPackageOptionMapper.class);
		CloudHostMapper  cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class); 
        PackagePriceMapper packagePriceMapper = this.sqlSession.getMapper(PackagePriceMapper.class);
        CloudHostSysDefaultPortsMapper cloudHostSysDefaultPortsMapper = this.sqlSession.getMapper(CloudHostSysDefaultPortsMapper.class);
        Map<String, Object> data = new LinkedHashMap<String, Object>();
		data.put("status1",             3);
		data.put("status2",         2);
        List<PackagePriceVO> allPriceList =  packagePriceMapper.getAllPackagePrice(data);
        List<PackagePriceVO> package1 = new ArrayList<PackagePriceVO>();
        List<PackagePriceVO> package2 = new ArrayList<PackagePriceVO>();
        List<PackagePriceVO> package4 = new ArrayList<PackagePriceVO>();
        List<PackagePriceVO> cpuRegion1 = new ArrayList<PackagePriceVO>();
        List<PackagePriceVO> cpuRegion2 = new ArrayList<PackagePriceVO>();
        List<PackagePriceVO> cpuRegion4 = new ArrayList<PackagePriceVO>();
        List<PackagePriceVO> memoryRegion1 = new ArrayList<PackagePriceVO>();
        List<PackagePriceVO> memoryRegion2 = new ArrayList<PackagePriceVO>();
        List<PackagePriceVO> memoryRegion4 = new ArrayList<PackagePriceVO>();
        List<PackagePriceVO> diskRegion1 = new ArrayList<PackagePriceVO>();
        List<PackagePriceVO> diskRegion2 = new ArrayList<PackagePriceVO>();
        List<PackagePriceVO> diskRegion4 = new ArrayList<PackagePriceVO>();
        List<PackagePriceVO> bandwidthRegion1 = new ArrayList<PackagePriceVO>();
        List<PackagePriceVO> bandwidthRegion2 = new ArrayList<PackagePriceVO>();
        List<PackagePriceVO> bandwidthRegion4 = new ArrayList<PackagePriceVO>();
        for(PackagePriceVO vo:allPriceList){
        	if(vo.getType()!=null&&vo.getType()==1&&vo.getRegion()==1){
        		cpuRegion1.add(vo); 
        	}else if(vo.getType()!=null&&vo.getType()==1&&vo.getRegion()==2){
        		cpuRegion2.add(vo);        		
        	}else if(vo.getType()!=null&&vo.getType()==1&&vo.getRegion()==4){
        		cpuRegion4.add(vo);        		
        	}else if(vo.getType()!=null&&vo.getType()==2&&vo.getRegion()==1){
        		memoryRegion1.add(vo);        		
        	}else if(vo.getType()!=null&&vo.getType()==2&&vo.getRegion()==2){
        		memoryRegion2.add(vo);        		
        	}else if(vo.getType()!=null&&vo.getType()==2&&vo.getRegion()==4){
        		memoryRegion4.add(vo);        		
        	}else if(vo.getType()!=null&&vo.getType()==3&&vo.getRegion()==1){
        		diskRegion1.add(vo);        		
        	}else if(vo.getType()!=null&&vo.getType()==3&&vo.getRegion()==2){
        		diskRegion2.add(vo);        		
        	}else if(vo.getType()!=null&&vo.getType()==3&&vo.getRegion()==4){
        		diskRegion4.add(vo);        		
        	}else if(vo.getType()!=null&&vo.getType()==4&&vo.getRegion()==1){
        		bandwidthRegion1.add(vo);        		
        	}else if(vo.getType()!=null&&vo.getType()==4&&vo.getRegion()==2){
        		bandwidthRegion2.add(vo);        		
        	}else if(vo.getType()!=null&&vo.getType()==4&&vo.getRegion()==4){
        		bandwidthRegion4.add(vo);        		
        	}else if(vo.getType()!=null&&vo.getType()==5&&vo.getRegion()==1){
        		package1.add(vo);        		
        	}else if(vo.getType()!=null&&vo.getType()==5&&vo.getRegion()==2){
        		package2.add(vo);        		
        	}else if(vo.getType()!=null&&vo.getType()==5&&vo.getRegion()==4){
        		package4.add(vo);        		
        	}
        }
        request.setAttribute("cpuRegion1", cpuRegion1);
        request.setAttribute("cpuRegion2", cpuRegion2);
        request.setAttribute("cpuRegion4", cpuRegion4);
        request.setAttribute("memoryRegion1", memoryRegion1);
        request.setAttribute("memoryRegion2", memoryRegion2);
        request.setAttribute("memoryRegion4", memoryRegion4);
        request.setAttribute("diskRegion1", diskRegion1);
        request.setAttribute("diskRegion2", diskRegion2);
        request.setAttribute("diskRegion4", diskRegion4);
        request.setAttribute("bandwidthRegion1", bandwidthRegion1);
        request.setAttribute("bandwidthRegion2", bandwidthRegion2);
        request.setAttribute("bandwidthRegion4", bandwidthRegion4);
        request.setAttribute("package1", package1);
        request.setAttribute("package2", package2);
        request.setAttribute("package4", package4);
        request.setAttribute("dataDiskMin", AppProperties.getValue("dataDiskMin","")); 
        request.setAttribute("dataDiskMax", AppProperties.getValue("dataDiskMax","")); 
        request.setAttribute("bandwidthMin_1", AppProperties.getValue("bandwidthMin_1","")); 
        request.setAttribute("bandwidthMin_2", AppProperties.getValue("bandwidthMin_2","")); 
        request.setAttribute("bandwidthMin_4", AppProperties.getValue("bandwidthMin_4","")); 
        request.setAttribute("bandwidthMax_1", AppProperties.getValue("bandwidthMax_1","")); 
        request.setAttribute("bandwidthMax_2", AppProperties.getValue("bandwidthMax_2","")); 
        request.setAttribute("bandwidthMax_4", AppProperties.getValue("bandwidthMax_4","")); 
        
        List<CloudHostSysDefaultPortsVO> defaultPorts = cloudHostSysDefaultPortsMapper.getAllPorts();
        request.setAttribute("defaultPorts", defaultPorts);
		// 带宽套餐项
		BandwidthPackageOptionVO bandwidthOption = bandwidthPackageOptionMapper.getOne();;
		if( bandwidthOption==null )
		{	// 如果还没有配置带宽套餐项，则设置一下默认的
			bandwidthOption = new BandwidthPackageOptionVO(FlowUtil.fromFlowLabel("1Mbps"), FlowUtil.fromFlowLabel("100Mbps")); 
			
		}
		request.setAttribute("bandwidthOption", bandwidthOption);
		
		// 获取CPU套餐项
		List<CpuPackageOptionVO>  cpuOptions = cpuPackageOptionMapper.getAll();
		request.setAttribute("cpuOptions", cpuOptions);
		
		// 获取内存套餐项
		List<MemoryPackageOptionVO> memoryOptions = memoryPackageOptionMapper.getAll();
		request.setAttribute("memoryOptions", memoryOptions);

		// 系统磁盘镜像
		List<SysDiskImageVO> sysDiskImageOptions = sysDiskImageMapper.getAllCommonImage();
		request.setAttribute("sysDiskImageOptions", sysDiskImageOptions);
		
		// 磁盘套餐项
		DiskPackageOptionVO diskOption = diskPackageOptionMapper.getOne();
		if( diskOption==null )
		{	// 如果还没有配置磁盘套餐项，则设置一下默认的
			diskOption = new DiskPackageOptionVO(CapacityUtil.fromCapacityLabel("1GB"), CapacityUtil.fromCapacityLabel("100GB"));
		}
		request.setAttribute("diskOption", diskOption); 
		String userType = request.getParameter("userType");
		if(userType.equals("4")){			
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request, AppConstant.SYS_USER_TYPE_TERMINAL_USER);
			if(loginInfo != null){			
				Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
				cloudHostData.put("userId", loginInfo.getUserId());
				List<CloudHostVO> hostList = cloudHostMapper.getCloudForVpc(cloudHostData);
				request.setAttribute("hostList", hostList); 
				AccountBalanceService accountBalanceService = CoreSpringContextManager.getAccountBalanceService();
				accountBalanceService.getBalance(request, response); 
				request.setAttribute("vpcName", this.generateNewVpcName(loginInfo.getUserId())); 
			}else{
				
				request.setAttribute("hostList", new ArrayList<CloudHostVO>()); 
			}
			return "/security/user/create_vpc.jsp";
		}else{
			String userId = StringUtil.trim(request.getParameter("terminalUserId"));
			request.setAttribute("userId", userId); 
			
			Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
			cloudHostData.put("userId", userId);
			List<CloudHostVO> hostList = cloudHostMapper.getCloudForVpc(cloudHostData);
			request.setAttribute("hostList", hostList); 
			AccountBalanceService accountBalanceService = CoreSpringContextManager.getAccountBalanceService();
			accountBalanceService.getBalance(request, response); 
			request.setAttribute("vpcName", this.generateNewVpcName(userId));  
			return "/security/agent/create_vpc.jsp";
		}
	}

	/**
	 * 新增基本信息
	 * @see com.zhicloud.op.service.VpcService#addVpcBaseInfo(java.util.Map)
	 */
	@Callable
	@Transactional(readOnly = false)
	public MethodResult addVpcBaseInfo(Map<String, String> parameter) {

		logger.debug("VpcServiceImpl.addVpcBaseImfo()");
		Long begin = System.currentTimeMillis();
		Integer logStatus = AppConstant.OPER_LOG_FAIL;
		HttpServletRequest request = RequestContext.getHttpRequest();
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		String userId = loginInfo.getUserId();
		String _userName = "";
		try
		{
 			// 获取推荐配置
			String item           = StringUtil.trim(parameter.get("item"));  
   			String ipAmount    = StringUtil.trim(parameter.get("ipAmount")); 
 			String displayName    = StringUtil.trim(parameter.get("vpcname"));
			String description    = StringUtil.trim(parameter.get("description")); 
			Integer region        = StringUtil.parseInteger((String)parameter.get("region"), null); 
			// 关联主机
			String hostId    = StringUtil.trim(parameter.get("hostId")); 
			// 创建主机
			String createhostinfo      = StringUtil.trim(parameter.get("createhostinfo")); 
  			String id         = StringUtil.generateUUID();
            List<CloudHostVO> createhostList = new ArrayList<CloudHostVO>();
            BigDecimal price = BigDecimal.ONE;
            if(!StringUtil.isBlank(createhostinfo)&& !createhostinfo.equals("[]")){
            	String [] cloudStrs = createhostinfo.split(",");
            	for(String cloudStr : cloudStrs){
            		CloudHostVO vo = new CloudHostVO();
            		cloudStr = cloudStr.replace("\"", "");
            		cloudStr = cloudStr.replace("]", "");
            		cloudStr = cloudStr.replace("[", "");
            		String [] cloudInfo = cloudStr.split("/");
            		vo.setCpuCore(Integer.parseInt(cloudInfo[1])); 
            		vo.setMemory(CapacityUtil.fromCapacityLabel(cloudInfo[2] + "GB"));
            		vo.setDataDisk(CapacityUtil.fromCapacityLabel(cloudInfo[3] + "GB"));
            		vo.setBandwidth(FlowUtil.fromFlowLabel(cloudInfo[4]+"Mbps"));
             		vo.setSysDisk(CapacityUtil.fromCapacityLabel("10GB"));
            		vo.setSysImageId(cloudInfo[5]); 
            		if(cloudInfo.length == 7){
            			PackagePriceMapper packagePriceMapper = this.sqlSession.getMapper(PackagePriceMapper.class);
        				List<PriceVO> priceList =  packagePriceMapper.getPriceByInfoId(cloudInfo[6]);
        				PackagePriceVO packagePriceVO = packagePriceMapper.getById(cloudInfo[6]);
         				if(priceList==null){					
        					return new MethodResult(MethodResult.FAIL, "套餐选择有误"); 
        				}
          				for(PriceVO priceVo : priceList){
         					if(priceVo.getStatus() == 2){
         						vo.setMonthlyPrice(priceVo.getMonthlyPrice());  
         					}     					
         				}
            		}else{        			
            			vo.setMonthlyPrice(CloudHostPrice.getMonthlyPrice(region, 2, vo.getCpuCore(), vo.getMemory(), vo.getDataDisk(), vo.getBandwidth()) );
            		}            		for(int i=0;i<Integer.parseInt(cloudInfo[0]);i++){           			
             			createhostList.add(vo);
            			price = price.add( vo.getMonthlyPrice() );
            		} 
            	}
            	
            }
			if( StringUtil.isBlank(displayName) )
			{
				throw new AppException("VPC名称不能为空");
			}   
			
  			if(region==null||region.equals("")){
				return new MethodResult(MethodResult.FAIL, "请选择地域");
				
			}
 			CloudHostMapper cloudHostMapper                                     = this.sqlSession.getMapper(CloudHostMapper.class);
			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class); 			
 			TerminalUserVO terminalUserVO = terminalUserMapper.getBalanceById(loginInfo.getUserId()); 
			  
			if(StringUtil.isBlank(ipAmount)){
				return new MethodResult(MethodResult.FAIL, "请输入ip数量");
				
			} 
 			 
 			
			
			logger.info("begin to count balance for create vpc");
			//计算余额能否支持一天，不足不创建主机
			if(terminalUserVO!=null){
 				 
 				BigDecimal totalPrice = new BigDecimal("0");
				//计算出每天的费用
				totalPrice = totalPrice.add(price.multiply(new BigDecimal("100").subtract(terminalUserVO.getPercentOff()).multiply(new BigDecimal("0.01"))));
				//计算出VPC每天的费用
				MethodResult result = this.countVpcPrice(parameter);
				totalPrice = totalPrice.add(new BigDecimal(result.get("price").toString()));
				totalPrice = totalPrice.divide(new BigDecimal("31"),0,BigDecimal.ROUND_HALF_UP);
				//算出已有产品每天的费用
				totalPrice = totalPrice.add(new CountUserProductsPriceHelper(sqlSession).getAllOneDayPrice(userId));
				BigDecimal balance = terminalUserVO.getAccountBalance().setScale(0, BigDecimal.ROUND_HALF_UP);
				if(balance.compareTo(totalPrice)<0){
					if(totalPrice.subtract(balance).compareTo(BigDecimal.TEN)<0){						
 						return new MethodResult(MethodResult.FAIL, "余额不足，至少充值<a href='page.do?userType=4&bean=accountBalanceService&method=toRechargePage' target='blank'>10</a>元<br/>多充多优惠，<a href='page.do?userType=4&bean=accountBalanceService&method=toRechargePage' target='blank'>点击进入充值</a>");
					}else{						
 						return new MethodResult(MethodResult.FAIL, "余额不足，至少充值<a href='page.do?userType=4&bean=accountBalanceService&method=toRechargePage' target='blank'>"+totalPrice.subtract(balance)+"</a>元<br/>多充多优惠，<a href='page.do?userType=4&bean=accountBalanceService&method=toRechargePage' target='blank'>点击进入充值</a>");
					}
 					
				} 
				
			} 			
			//向平台发送创建主机命令
			// 发消息到http gateway
			logger.info("begin to create vpc");
  // 	    String poolId = "415716e32bd64a908e0a341b8dcbea5f";
 			String poolId = "";
			NetworkService service = CoreSpringContextManager.getNetworkService();
 			AddressPool ap = AddressPoolManager.singleton().getPool(region.toString());
			List<AddressExt> addressList = ap.getAll();
			for(AddressExt address : addressList){
				if(address.getName().equals("default")){
					poolId = address.getUuid();
					break;
				}
			}			
			NetworkInfoExt result = service.createSync(region, id, 27, "test", poolId);
			
			if (result == null) {
				logger.info("fail to create network.");
				return new MethodResult(MethodResult.FAIL, "创建失败");
			} else {
				logger.info(String.format("success to create network. uuid[%s], network_address[%s]", result.getUuid(), result.getHostNetworkAddress()));
			}
			String realVpcId = result.getUuid();
//			String realVpcId = "siusdidso";
			//创建成功回填数据库
			VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class); 
			VpcBindHostMapper vpcBindHostMapper = this.sqlSession.getMapper(VpcBindHostMapper.class); 
			OrderInfoMapper orderInfoMapper     = this.sqlSession.getMapper(OrderInfoMapper.class);
			CloudHostShoppingConfigMapper cloudHostShoppingConfigMapper         = this.sqlSession.getMapper(CloudHostShoppingConfigMapper.class);
			OrderDetailMapper orderDetailMapper                                 = this.sqlSession.getMapper(OrderDetailMapper.class);
			CloudHostBillDetailMapper cloudHostBillDetailMapper                 = this.sqlSession.getMapper(CloudHostBillDetailMapper.class);
			SysDiskImageMapper sysDiskImageMapper = this.sqlSession.getMapper(SysDiskImageMapper.class); 
			VpcOuterIpMapper vpcOuterIpMapper = this.sqlSession.getMapper(VpcOuterIpMapper.class); 
			VpcBillDetailMapper vpcBillDetailMapper   = this.sqlSession.getMapper(VpcBillDetailMapper.class);


			
			
			Map<String, Object> data = new LinkedHashMap<String, Object>();
			data.put("id",       id);
			data.put("realVpcId",  realVpcId); //
			data.put("name",  id);
			data.put("displayName",  displayName);
 			data.put("description",  description);
 			data.put("status",  1);// 创建成功
 			data.put("region",  region);
 			data.put("hostAmount",  0);
 			data.put("ipAmount",  0);
 			data.put("userId",  loginInfo.getUserId());
 			data.put("createTime",  StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
			int n = vpcBaseInfoMapper.addVpc(data);
			
			logger.info("begin to get ip for vpc");
			//申请ip 
			result = service.attachAddressSync(region, realVpcId, Integer.parseInt(ipAmount));
			
			for(int i = 0;i<Integer.parseInt(ipAmount);i++){					
				data.put("id", StringUtil.generateUUID());
				data.put("vpcId", id);
				data.put("ip", result.getIp()[i]);
 				data.put("createTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
				vpcOuterIpMapper.add(data);				
			}
			//更新IP个数
			data.put("id", id);
			data.put("amount", Integer.parseInt(ipAmount));
			vpcBaseInfoMapper.updateVpcIpAmount(data); 
			logger.info("begin to create host for vpc");
			if(n > 0){
				 	
				
				//绑定现有主机
				hostId = hostId.replace("\"", "");
				hostId = hostId.replace("]", "");
				hostId = hostId.replace("[", "");
				if(!StringUtil.isBlank(hostId)){
					String [] ids = hostId.split(",");
					for(int i = 0;i<ids.length;i++){
						ids[i] = ids[i].replace("\"", "");
						ids[i] = ids[i].replace("]", "");
						ids[i] = ids[i].replace("[", "");
						CloudHostVO host = cloudHostMapper.getById(ids[i]);
						NetworkInfoExt attachHostResult = service.attachHostSync(region, realVpcId, host.getRealHostId());
//						boolean attachHostResult = true;
						if(attachHostResult != null ){
							//增加主机和VPC关联
							data.put("id", StringUtil.generateUUID());
							data.put("vpcId", id);
							data.put("hostId", ids[i]);
							data.put("createTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
							vpcBindHostMapper.add(data);
							Integer type = AppConstant.CLOUD_HOST_TYPE_5_TERMINAL_USER_VPC;
							if(host.getType() == 2){
								type = AppConstant.CLOUD_HOST_TYPE_6_Agent_VPC;
							}
							//更新主机的类型，以便后续的查询和计费使用
							data.put("id", host.getId());
							data.put("type", type);
							cloudHostMapper.updateHostTypeById(data);
							//更新vpcip 
							data.put("vpcIp", attachHostResult.getHostNetworkAddress());
							cloudHostMapper.updateVpcIpById(data);
							new CloudHostBillingHelper(sqlSession).settleAllUndoneCloudHostBills(hostId, new Date(), false);
							defaultBindPort(id, host);
  						}else{
  							throw new AppException("创建失败");
  						}
					}
					//更新主机个数
					data.put("id", id);
					data.put("amount", ids.length);
					vpcBaseInfoMapper.updateVpcHostAmount(data);
					
				}
				String now = StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS");
				//新创建云主机
				if(createhostList.size()>0){
					for(int i = 0;i<createhostList.size();i++){
						
						int orderInsertResult = 0; 							
						int shoppingConfigInsertResult = 0; 
						int orderDetailInsertResult = 0;
						String orderId = StringUtil.generateUUID();
						CloudHostVO hostVo = createhostList.get(i);
						// 从云主机仓库获取云主机
						String hostName = getNewCloudHostNameByUserIdForVpc(loginInfo.getUserId(),region.toString());
						Map<String, Object> newCloudHostData = new LinkedHashMap<String, Object>();
						List<String> host_ports = new ArrayList<String>();
						newCloudHostData.put("type",          AppConstant.CLOUD_HOST_TYPE_5_TERMINAL_USER_VPC);  //主机类型为用户自己的VPC主机
						newCloudHostData.put("userId",        loginInfo.getUserId());
						newCloudHostData.put("hostName",      hostName);
						newCloudHostData.put("displayName",   hostName);
						newCloudHostData.put("account",       loginInfo.getAccount());
						newCloudHostData.put("password",      RandomPassword.getRandomPwd(16));
						newCloudHostData.put("cpuCore",       hostVo.getCpuCore());
						newCloudHostData.put("memory",        hostVo.getMemory());
						newCloudHostData.put("dataDisk",      hostVo.getDataDisk());
						newCloudHostData.put("bandwidth",     hostVo.getBandwidth());
						newCloudHostData.put("isAutoStartup", AppConstant.CLOUD_HOST_IS_AUTO_STARTUP_YES);
						newCloudHostData.put("monthlyPrice",  hostVo.getMonthlyPrice());
						newCloudHostData.put("item", item);
						newCloudHostData.put("ports",         _formatToHttpGatewayFormatPorts(host_ports));
						MethodResult warehouseResult = new CloudHostWarehouseHelper(this.sqlSession).getAndAllocateWarehouseCloudHost(hostVo.getSysImageId(), region, newCloudHostData);
						
						if( MethodResult.SUCCESS.equals(warehouseResult.status) )
						{
							CloudHostWarehouseDetailVO vo = (CloudHostWarehouseDetailVO) warehouseResult.get("CloudHostWarehouseDetailVO");
							
							
 							
							// 新增订单
							Map<String, Object> newOrder = new LinkedHashMap<String, Object>();
							newOrder.put("id",             orderId);
							newOrder.put("userId",         userId);
							newOrder.put("createTime",     now);
							newOrder.put("totalPrice",     price);
							newOrder.put("isPaid",         AppConstant.ORDER_INFO_IS_PAID_YES);						
							newOrder.put("processStatus",  AppConstant.ORDER_INFO_PROCESS_STATUS_SUCCESS);
							newOrder.put("processMessage", "");
							orderInsertResult = orderInfoMapper.addOrderInfo(newOrder);
							
							if (orderInsertResult <= 0)
							{
								throw new AppException("添加订单失败");
							}							
							// 从云主机仓库获取云主机 
							logger.info("PaymentServiceImpl.getCloudHost() > ["+Thread.currentThread().getId()+"] 云主机仓库获取的库存云主机成功, sysImageId:["+hostVo.getSysImageId()+"], dataDisk:["+hostVo.getDataDisk()+"], region:["+region+"]");
							String cloudHostId    = vo.getHostId();
							CloudHostVO host = cloudHostMapper.getById(cloudHostId);
							Integer processStatus  = AppConstant.CLOUD_HOST_SHOPPING_CONFIG_PROCESS_STATUS_SUCCESS;
							String  processMessage = "fetch from cloud host warehouse"; 
							// 新增配置
							String configId = StringUtil.generateUUID();
							Map<String, Object> newConfig = new LinkedHashMap<String, Object>();
							newConfig.put("id",             configId);
							newConfig.put("hostId",         cloudHostId);
							newConfig.put("userId",         userId);
							newConfig.put("type",           AppConstant.CLOUD_HOST_SHOPPING_CONFIG_TYPE_TRAIL);
							newConfig.put("cpuCore",        hostVo.getCpuCore());
							newConfig.put("memory",         hostVo.getMemory());
							newConfig.put("sysImageId",     hostVo.getSysImageId());
							newConfig.put("sysDisk",        hostVo.getSysDisk());
							newConfig.put("dataDisk",       hostVo.getDataDisk());
							newConfig.put("bandwidth",      hostVo.getBandwidth());
							newConfig.put("price",          hostVo.getMonthlyPrice());
							newConfig.put("createTime",     now);
							newConfig.put("duration",       "0");
							newConfig.put("processStatus",  processStatus);
							newConfig.put("processMessage", processMessage);
							newConfig.put("hostName",       hostName);
							newConfig.put("region",       region);
							shoppingConfigInsertResult = cloudHostShoppingConfigMapper.addCloudHostShoppingConfig(newConfig);
							// 新增配置端口  
							
							// 新增订单详情
							Map<String, Object> new_order_detail = new LinkedHashMap<String, Object>();
							new_order_detail.put("id",       StringUtil.generateUUID());
							new_order_detail.put("orderId",  orderId);
							new_order_detail.put("itemType", AppConstant.ORDER_DETAIL_ITEM_TYPE_CLOUD_HOST);
							new_order_detail.put("itemId",   configId);
							orderDetailMapper.insertIntoOrderDetail(new_order_detail);
							
//							// 添加一条云主机计费的记录
							Map<String, Object> cloudHostBillDetailData = new LinkedHashMap<String, Object>();
							cloudHostBillDetailData.put("id",               StringUtil.generateUUID());                         
							cloudHostBillDetailData.put("host_id",          cloudHostId);                         
							cloudHostBillDetailData.put("type",             AppConstant.CLOUD_HOST_BILL_DETAIL_TYPE_4_PAY_FOR_TIME);                         
							cloudHostBillDetailData.put("cpuCore",          hostVo.getCpuCore());                         
							cloudHostBillDetailData.put("cpuUsed",          0);                         
							cloudHostBillDetailData.put("memory",           CapacityUtil.toMBValue(hostVo.getMemory(), 2));                         
							cloudHostBillDetailData.put("memoryUsed",       CapacityUtil.toMBValue(hostVo.getMemory(), 2));                         
							cloudHostBillDetailData.put("sysImageId",       hostVo.getSysImageId());                         
							cloudHostBillDetailData.put("sysDisk",          CapacityUtil.toGBValue(hostVo.getSysDisk(), 2));                         
							cloudHostBillDetailData.put("sysDiskUsed",      CapacityUtil.toGBValue(hostVo.getSysDisk(), 2));                         
							cloudHostBillDetailData.put("dataDisk",         CapacityUtil.toGBValue(hostVo.getDataDisk(), 2));                         
							cloudHostBillDetailData.put("dataDiskUsed",     CapacityUtil.toGBValue(hostVo.getDataDisk(), 2));                         
							cloudHostBillDetailData.put("diskRead",         0);                         
							cloudHostBillDetailData.put("diskWrite",        0);                         
							cloudHostBillDetailData.put("bandwidth",        FlowUtil.toMbpsValue(hostVo.getBandwidth(), 2));                         
							cloudHostBillDetailData.put("networkTraffic",   0);                         
							cloudHostBillDetailData.put("startTime",        now);                         
							cloudHostBillDetailData.put("endTime",          null);                         
							cloudHostBillDetailData.put("fee",              null);                         
							cloudHostBillDetailData.put("isPaid",           AppConstant.CLOUD_HOST_BILL_DETAIL_IS_PAID_NOT);                         
							cloudHostBillDetailData.put("createTime",       now);                         
							cloudHostBillDetailMapper.addCloudHostBillDetail(cloudHostBillDetailData); 
							
							NetworkInfoExt attachHostResult = service.attachHostSync(region, realVpcId, host.getRealHostId());
//							boolean attachHostResult = true;
							if(attachHostResult != null ){
								//增加主机和VPC关联
								data.put("id", StringUtil.generateUUID());
								data.put("vpcId", id);
								data.put("hostId", cloudHostId);
								data.put("createTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
								vpcBindHostMapper.add(data); 
								//更新vpcip 
								Map<String, Object> newIP = new LinkedHashMap<String, Object>();
								newIP.put("vpcIp", attachHostResult.getHostNetworkAddress());
								newIP.put("id", host.getId());
								cloudHostMapper.updateVpcIpById(newIP);
								new CloudHostBillingHelper(sqlSession).settleAllUndoneCloudHostBills(hostId, new Date(), false);
								defaultBindPort(id, host);
	  						}else{
	  							throw new AppException("创建失败");
	  						}
							
							// 启动云主机
							CloudHostService cloudHostService = CoreSpringContextManager.getCloudHostService();
							cloudHostService.startCloudHost(cloudHostId);  
							//分配完成，立即创建一个补位 
							WarehouseCloudHostCreationListener.instance.addWarehouseIdNeedToBeImmediatelyCreated(vo.getWarehouseId()); 
							 
						}else{
							// 从仓库获取云主机失败，正常创建
							
							String cloudHostId    = StringUtil.generateUUID();
							Integer processStatus  = AppConstant.CLOUD_HOST_SHOPPING_CONFIG_PROCESS_STATUS_NOT_PROCESSED;
						    String configId = StringUtil.generateUUID();
						    
 							
 							
							// 新增订单
							Map<String, Object> newOrder = new LinkedHashMap<String, Object>();
							newOrder.put("id",             orderId);
							newOrder.put("userId",         userId);
							newOrder.put("createTime",     now);
							newOrder.put("totalPrice",     price);
							newOrder.put("isPaid",         AppConstant.ORDER_INFO_IS_PAID_YES);						
							newOrder.put("processStatus",  AppConstant.ORDER_INFO_PROCESS_STATUS_NOT_PROCESSED);//设置订单为未处理
							newOrder.put("processMessage", "");
							orderInsertResult = orderInfoMapper.addOrderInfo(newOrder);
							
							// 新增配置
							Map<String, Object> newConfig = new LinkedHashMap<String, Object>();
							newConfig.put("id",             configId);
							newConfig.put("hostId",         cloudHostId);
							newConfig.put("userId",         userId);
							newConfig.put("type",           AppConstant.CLOUD_HOST_SHOPPING_CONFIG_TYPE_TRAIL);
							newConfig.put("cpuCore",        hostVo.getCpuCore());
							newConfig.put("memory",         hostVo.getMemory());
							newConfig.put("sysImageId",     hostVo.getSysImageId());
							newConfig.put("sysDisk",        hostVo.getSysDisk());
							newConfig.put("dataDisk",       hostVo.getDataDisk());
							newConfig.put("bandwidth",      hostVo.getBandwidth());
							newConfig.put("price",          hostVo.getMonthlyPrice());
							newConfig.put("createTime",     now);
							newConfig.put("duration",       "0");
							newConfig.put("processStatus",  processStatus);
							newConfig.put("processMessage", "");
							newConfig.put("hostName",       hostName);
							newConfig.put("region",       region);
							shoppingConfigInsertResult = cloudHostShoppingConfigMapper.addCloudHostShoppingConfig(newConfig);
							
							 
							
							// 新增订单详情
							Map<String, Object> new_order_detail = new LinkedHashMap<String, Object>();
							new_order_detail.put("id",       StringUtil.generateUUID());
							new_order_detail.put("orderId",  orderId);
							new_order_detail.put("itemType", AppConstant.ORDER_DETAIL_ITEM_TYPE_CLOUD_HOST);
							new_order_detail.put("itemId",   configId);
							orderDetailInsertResult = orderDetailMapper.insertIntoOrderDetail(new_order_detail);
							
							 SysDiskImageVO sysDiskIamge= sysDiskImageMapper.getById(hostVo.getSysImageId());
							 String sysImageName = (sysDiskIamge == null ? null : sysDiskIamge.getName());
							// 新增云主机信息，创建不需要添加计费记录，到云主机创建成功之后再去创建
							Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
							cloudHostData.put("id",              cloudHostId);                                                              
							cloudHostData.put("realHostId",      null);                                                                     
							cloudHostData.put("type",            AppConstant.CLOUD_HOST_TYPE_5_TERMINAL_USER_VPC);                            
							cloudHostData.put("userId",          userId);                                                                   
							cloudHostData.put("hostName",        hostName);                                                                     
							cloudHostData.put("displayName",        hostName);                                                                     
							cloudHostData.put("account",         loginInfo.getAccount());                                                   
							cloudHostData.put("password",        RandomPassword.getRandomPwd(16));                                                   
							cloudHostData.put("cpuCore",         hostVo.getCpuCore());                                                                  
							cloudHostData.put("memory",          hostVo.getMemory());                                                                   
							cloudHostData.put("sysImageId",      hostVo.getSysImageId());                                                               
							cloudHostData.put("sysImageName",    sysImageName);                                                               
							cloudHostData.put("sysDisk",         hostVo.getSysDisk());                                                                  
							cloudHostData.put("dataDisk",        hostVo.getDataDisk());                                                                 
							cloudHostData.put("bandwidth",       hostVo.getBandwidth());                                                                
							cloudHostData.put("isAutoStartup",   AppConstant.CLOUD_HOST_IS_AUTO_STARTUP_NO);                                
							cloudHostData.put("runningStatus",   AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN);                           
							cloudHostData.put("status",          AppConstant.CLOUD_HOST_STATUS_1_NORNAL);                                     
							cloudHostData.put("innerIp",         null);                                                                     
							cloudHostData.put("innerPort",       null);                                                                     
							cloudHostData.put("outerIp",         null);                                                                     
							cloudHostData.put("outerPort",       null);  
							cloudHostData.put("region",       region);
							cloudHostData.put("package_id",       item); 	
						    cloudHostData.put("monthlyPrice", price); 
							cloudHostMapper.addCloudHost(cloudHostData);
							
							//新增主机和VPC的关系
							data.put("id", StringUtil.generateUUID());
							data.put("vpcId", id);
							data.put("hostId", cloudHostId);
							data.put("createTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
							vpcBindHostMapper.add(data);
							
						}
						
					}
					//更新主机个数
					data.put("id", id);
					data.put("amount", createhostList.size());
					vpcBaseInfoMapper.updateVpcHostAmount(data);
				}
				
				
				// 生成账单
				data.put("id", StringUtil.generateUUID());
				data.put("vpcId", id);
 				data.put("createTime", now);
				data.put("startTime", now);
				vpcBillDetailMapper.addVpcBillDetail(data);			 
				logStatus = AppConstant.OPER_LOG_SUCCESS;
				return new MethodResult(MethodResult.SUCCESS, "添加成功");
			}else{
				return new MethodResult(MethodResult.FAIL, "添加失败");				
			}
		}
		catch( Exception e )
		{
			logger.error(e); 
			throw new AppException(e);
		}finally { 
				try {
					OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
					Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
					operLogData.put("id", StringUtil.generateUUID());
					operLogData.put("userId", loginInfo.getUserId());
					operLogData.put("content", "添加VPC:" + parameter.get("vpcname"));
					operLogData.put("operTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
					operLogData.put("status", logStatus);
					operLogData.put("resourceName", "终端用户名:" + _userName);
					operLogData.put("operDuration", System.currentTimeMillis() - begin);
					operLogMapper.addOperLog(operLogData);
				} catch (Exception e) {
					logger.error(e);
				} 

		} 
	}

	/**
	 * 删除VPC
	 * @see com.zhicloud.op.service.VpcService#deleteVpc(java.util.Map)
	 */
	@Callable
	@Transactional(readOnly = false,noRollbackFor={Exception.class})
	public MethodResult deleteVpc(Map<String, String> parameter) {

		logger.debug("VpcServiceImpl.deleteVpc()");
		Long begin = System.currentTimeMillis();
		Integer logStatus = AppConstant.OPER_LOG_FAIL;
		HttpServletRequest request = RequestContext.getHttpRequest();
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		String _userName = "";
		try
		{
			// 权限判断
 			 
			String id    = StringUtil.trim(parameter.get("id")); 

			if( StringUtil.isBlank(id) )
			{
				throw new AppException("请选择要删除的VPC");
			} 
			 
			VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class); 
			VpcBindHostMapper vpcBindHostMapper = this.sqlSession.getMapper(VpcBindHostMapper.class); 
			CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class); 
			VpcBaseInfoVO vpcBaseInfo = vpcBaseInfoMapper.queryVpcById(id);
			if( vpcBaseInfo == null){
				throw new AppException("未查询到VPC信息");				
			}
			parameter.put("name", vpcBaseInfo.getDisplayName());
			Map<String,Object> cloudHostData = new HashMap<String,Object>();
			cloudHostData.put("vpcId", id);
			List<CloudHostVO> cloudHostList = cloudHostMapper.getAllHostByVpcId(cloudHostData);
			/*
			 结账
			 */
			List<String> hostIds = new ArrayList<String>();
			for(CloudHostVO cloudHost : cloudHostList){
				//结算未停用的云主机
				if(cloudHost.getStatus() == 1){
					VpcBaseInfoVO vo = vpcBaseInfoMapper.queryVpcByHostId(cloudHost.getId());
					new VpcBillHelper(sqlSession).settleAllUndoneVpcBills(vo.getId(), new Date(), true);
				}
				if(!StringUtil.isBlank(cloudHost.getRealHostId())){
					HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(cloudHost.getRegion());
					JSONObject hostDeleteResult = channel.hostDelete(cloudHost.getRealHostId());
					if (HttpGatewayResponseHelper.isSuccess(hostDeleteResult)) {
						logger.info("CloudHostServiceImpl.deleteCloudHostById() > [" + Thread.currentThread().getId() + "] delete host '" + cloudHost.getHostName() + "' succeeded, uuid:[" + cloudHost.getRealHostId() + "], message:[" + HttpGatewayResponseHelper.getMessage(hostDeleteResult) + "]");
						hostIds.add(cloudHost.getId());
					} else {
						logger.warn("CloudHostServiceImpl.deleteCloudHostById() > [" + Thread.currentThread().getId() + "] delete host '" + cloudHost.getHostName() + "' failed, uuid:[" + cloudHost.getRealHostId() + "], message:[" + HttpGatewayResponseHelper.getMessage(hostDeleteResult) + "]");
					}
					
					// 从缓冲池删除
					CloudHostPoolManager.getCloudHostPool().removeByRealHostId(cloudHost.getRealHostId());
					CloudHostPoolManager.getCloudHostPool().removeByHostName(cloudHost.getRegion(), cloudHost.getHostName());
				}else {
					hostIds.add(cloudHost.getId());
				}
			}
			/*
			 删除主机
			 */
			int l = cloudHostMapper.updateForDeleteByIds(hostIds.toArray(new String[0]));
			/**
			 * 删除主机与vpc的关联
			 */
			Map<String,Object> bindHostData = new HashMap<String,Object>();
			bindHostData.put("hostIds",hostIds);
			bindHostData.put("removeTime",DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
			int m = vpcBindHostMapper.deleteLogical(bindHostData);
			//最后删除那个啥
			List<String> idList = new ArrayList<String>();
			idList.add(id);
			int n = vpcBaseInfoMapper.logicDeleteVpcByIds(idList);
			if(n > 0 && m>0){
				logStatus = AppConstant.OPER_LOG_SUCCESS;
				return new MethodResult(MethodResult.SUCCESS, "删除成功");
			}else{
				return new MethodResult(MethodResult.FAIL, "删除失败");				
			}
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("添加失败");
		}finally {
			if(!StringUtil.isBlank(_userName)){
				try {
					OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
					Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
					operLogData.put("id", StringUtil.generateUUID());
					operLogData.put("userId", loginInfo.getUserId());
					operLogData.put("content", "删除VPC:" + parameter.get("name"));
					operLogData.put("operTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
					operLogData.put("status", logStatus);
					operLogData.put("resourceName", "终端用户名:" + _userName);
					operLogData.put("operDuration", System.currentTimeMillis() - begin);
					operLogMapper.addOperLog(operLogData);
				} catch (Exception e) {
					logger.error(e);
				}
			}

		} 
	}
	
	 
	
	public String getNewCloudHostNameByUserIdForVpc(String userId,String region)
	{
		SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
		CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
		
		// 获取用户信息
		SysUserVO sysUser = sysUserMapper.getById(userId);
		
		// 获取这个用户名下的所有云主机的主机名
		Map<String, Object> data = new LinkedHashMap<String, Object>();
		data.put("region", region);
		data.put("userId", userId);
		List<CloudHostVO> userCloudHostList = cloudHostMapper.getCloudHostForRegion(data);
 		Set<String> hostNames = new HashSet<String>();
		for( CloudHostVO cloudHost : userCloudHostList )
		{
			hostNames.add(StringUtil.trim(cloudHost.getHostName()));
		}
		
		// 获取新的云主机名
		int len = hostNames.size() + 1;	// 在size + 1个中，总有一个合适的名字
		String region_str = "";
		if("1".equals(region)){
			region_str = "GZ";
		}else if("2".equals(region)){
			region_str = "CD";
			
		}else if("4".equals(region)){
			region_str = "HK";
			
		}
		for( int i=1; i<=len; i++ )
		{
			String hostName = "T" + sysUser.getType() + "_" + region_str + "_"+sysUser.getAccount()+"_" + i;
			if( hostNames.contains(hostName)==false )
			{
				data.put("hostName", hostName);
				CloudHostVO vo = cloudHostMapper.getByRegionAndHostName(data);
				if(vo==null){					
					return hostName;
				}
			}
		}
		
		throw new AppException("generate new cloud host name failed.");
	}
	
	private Integer[] _formatToHttpGatewayFormatPorts(List<String> ports)
	{
		List<Integer> result = new ArrayList<Integer>();
		for( String port : ports )
		{
			String[] arr = port.split(":");
			result.add(Integer.valueOf(arr[0]));	// protocol
			result.add(Integer.valueOf(arr[1]));	// port
		}
		return result.toArray(new Integer[0]);
	}

	/**
	 * 跳转到VPC主机列表
	 * @see com.zhicloud.op.service.VpcService#toVpcHostList(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Callable
	@Transactional(readOnly = false)
	public String toVpcHostList(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("CloudUserServiceImpl.myCloudHostPage()");
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		try {
 		CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
		VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class);
 		String vpcId = request.getParameter("vpcId"); 
 		VpcBaseInfoVO vpcVo = vpcBaseInfoMapper.queryVpcById(vpcId);
 		List<CloudHostVO> myCloudHostList = cloudHostMapper.getCloudHostInVpc(vpcId);
 		Map<String, Object> condition = new LinkedHashMap<String, Object>();
		condition.put("userId", vpcVo.getUserId());
		List<CloudHostVO> hostList = cloudHostMapper.getCloudForVpc(condition);
		List<CloudHostVO> myNewCloudHostList = new ArrayList<CloudHostVO>();
		String currentTime = StringUtil.dateToString(new Date(),"yyyyMMddHHmmssSSS");
		Integer currentTiemInteger = Integer.parseInt(currentTime.substring(0,10));
		NetworkService service = CoreSpringContextManager.getNetworkService();

 		for(CloudHostVO cloudHost : myCloudHostList){
 			if("停机".equals(cloudHost.getSummarizedStatusText()) && cloudHost.getInactivateTime()!=null){
 				Date date = null;
 				try {
 					date = StringUtil.stringToDate(cloudHost.getInactivateTime(),"yyyyMMddHHmmssSSS");
 				} catch (ParseException e) {
 					e.printStackTrace();
 				}
 				Calendar calendar = Calendar.getInstance();
 				calendar.setTime(date);
 				calendar.add(Calendar.DAY_OF_MONTH,7);
 				String lastTime = StringUtil.dateToString(calendar.getTime(),"yyyyMMddHHmmssSSS");
 				String nowTime = StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS");
 				if(Long.parseLong(nowTime) - Long.parseLong(lastTime) > 0){
 					continue;
 				}else{
 					myNewCloudHostList.add(cloudHost);
 				}
 			}else if("创建中".equals(cloudHost.getSummarizedStatusText()) && cloudHost.getCreateTime()!=null){
 				Integer oldTime = Integer.parseInt(cloudHost.getCreateTime().substring(0,10));
 				if( (currentTiemInteger - oldTime) >= 100){
 					cloudHost.setProcessStatus(2);
 				}
 				myNewCloudHostList.add(cloudHost);
 			}else{
 				//如果没有vpcIP，重新获取
 				if(StringUtil.isBlank(cloudHost.getVpcIp())){
 					
 					NetworkInfoExt network = service.queryHostSync(vpcVo.getRegion(), vpcVo.getRealVpcId());
 					Host[] hosts = null;
 					if(network != null){				
 						hosts = network.getHostList();
 						logger.info(network); 
 						for (Host host : hosts) {
 							if(host.getUuid().equals(cloudHost.getRealHostId())){
 								cloudHost.setVpcIp(host.getNetworkAddress());
 								Map<String, Object> newIP = new LinkedHashMap<String, Object>();
 								newIP.put("vpcIp", host.getNetworkAddress());
 								newIP.put("id", cloudHost.getId());
 								cloudHostMapper.updateVpcIpById(newIP);
 							}
 						}
 					}
 				}
 			
 				myNewCloudHostList.add(cloudHost);
 			}
//			
		} 
		request.setAttribute("myCloudHostList", myNewCloudHostList);
		
		//查詢創建的套餐信息
		
 		SysDiskImageMapper sysDiskImageMapper               = this.sqlSession.getMapper(SysDiskImageMapper.class); 
          PackagePriceMapper packagePriceMapper = this.sqlSession.getMapper(PackagePriceMapper.class);
         Map<String, Object> data = new LinkedHashMap<String, Object>();
		data.put("status1",             3);
		data.put("status2",         2);
        List<PackagePriceVO> allPriceList =  packagePriceMapper.getAllPackagePrice(data);
        List<PackagePriceVO> packageInfo = new ArrayList<PackagePriceVO>(); 
        List<PackagePriceVO> cpuRegion = new ArrayList<PackagePriceVO>(); 
        List<PackagePriceVO> memoryRegion = new ArrayList<PackagePriceVO>(); 
        List<PackagePriceVO> diskRegion = new ArrayList<PackagePriceVO>(); 
        List<PackagePriceVO> bandwidthRegion = new ArrayList<PackagePriceVO>(); 
        
        for(PackagePriceVO vo:allPriceList){
        	if(vo.getType()!=null&&vo.getType()==1&&vo.getRegion()==vpcVo.getRegion()){
        		cpuRegion.add(vo); 
        	}else if(vo.getType()!=null&&vo.getType()==2&&vo.getRegion()==vpcVo.getRegion()){
        		memoryRegion.add(vo);        		
        	}else if(vo.getType()!=null&&vo.getType()==3&&vo.getRegion()==vpcVo.getRegion()){
        		diskRegion.add(vo);        		
        	}else if(vo.getType()!=null&&vo.getType()==4&&vo.getRegion()==vpcVo.getRegion()){
        		bandwidthRegion.add(vo);        		
        	}else if(vo.getType()!=null&&vo.getType()==5&&vo.getRegion()==vpcVo.getRegion()){
        		packageInfo.add(vo);        		
        	} 
        }
        List<SysDiskImageVO> sysDiskImageOptions = sysDiskImageMapper.getAllCommonImage();
        List<SysDiskImageVO> newSysDiskImageOptions = new ArrayList<SysDiskImageVO>();
        for(SysDiskImageVO vo : sysDiskImageOptions){
        	if(StringUtil.isBlank(vo.getRealImageId()) || vo.getStatus() == 1 || vo.getRegion() != vpcVo.getRegion()){
        		continue;
        	}
        	newSysDiskImageOptions.add(vo);
        }
        request.setAttribute("packageInfo", packageInfo);
        request.setAttribute("cpuRegion", cpuRegion);
        request.setAttribute("memoryRegion", memoryRegion);
        request.setAttribute("diskRegion", diskRegion);
        request.setAttribute("bandwidthRegion", bandwidthRegion);	
        request.setAttribute("imageList", newSysDiskImageOptions);	
        request.setAttribute("vpcId", vpcId);	
        request.setAttribute("vpcVo", vpcVo);	
        request.setAttribute("hostList", hostList);	
        if(loginInfo.getUserType()==AppConstant.SYS_USER_TYPE_AGENT){
        	return "/security/agent/my_vpc_cloud_host.jsp";
        }
		return "/security/user/my_vpc_cloud_host.jsp";
		} catch (Exception e) {
			throw new AppException(e);
			
		}
	} 
 

	 

	@Callable 
	public String vpcIpManagePage(HttpServletRequest request,HttpServletResponse response) {
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
 		String vpcId = request.getParameter("vpcId");
 		if(loginInfo.getUserType()==AppConstant.SYS_USER_TYPE_OPERATOR){
 		    VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class); 
 	        VpcBaseInfoVO vbi = vpcBaseInfoMapper.queryVpcById(vpcId);
 	        if (vbi == null) {
 	            request.setAttribute("message", "找不到vpc信息");
 	            return "/public/warning_dialog.jsp";
 	        }
 	        request.setAttribute("vpc",vbi);
 			request.setAttribute("vpcId", vpcId);
 			return "/security/operator/vpc_ip_list.jsp";
 		}
		Map<String,Object> condition = new HashMap<String, Object>();
		condition.put("vpcId", vpcId);
		VpcOuterIpMapper vpcOuterIpMapper = this.sqlSession.getMapper(VpcOuterIpMapper.class); 
		List<VpcOuterIpVO> ipList = vpcOuterIpMapper.getAllIpByVpcId(condition);
		request.setAttribute("ipList", ipList);
		request.setAttribute("vpcId", vpcId);
		if(loginInfo.getUserType()==AppConstant.SYS_USER_TYPE_AGENT){
			return "/security/agent/vpc_ip_manage.jsp";
 		}
 		return "/security/user/vpc_ip_manage.jsp";
	}
 

	@Callable
	@Transactional(readOnly = false)
	public MethodResult deleteIps(List<String> ips,List<String> ipValues) {
		Long begin = System.currentTimeMillis();
		Integer logStatus = AppConstant.OPER_LOG_FAIL;
		HttpServletRequest request = RequestContext.getHttpRequest();
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		try{
			if(ips == null || ips.size()<1){
				return new MethodResult(MethodResult.FAIL,"请选择需要删除的IP");
			}
			VpcOuterIpMapper vpcOuterIpMapper = this.sqlSession.getMapper(VpcOuterIpMapper.class);
			VpcBindPortMapper vpcBindPortMapper = this.sqlSession.getMapper(VpcBindPortMapper.class);
			vpcOuterIpMapper.deleteIps(ips);
 			for(String ip : ipValues){
 				vpcBindPortMapper.deleteByOuterIp(ip);
 				logStatus = AppConstant.OPER_LOG_SUCCESS;
			}
		}catch(Exception e){
			throw e;
		}finally {
			try {
				OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
				Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
				operLogData.put("id", StringUtil.generateUUID());
				operLogData.put("userId", loginInfo.getUserId());
				operLogData.put("content", "删除"+ips.size()+"个VPC外网IP");
				operLogData.put("operTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
				operLogData.put("status", logStatus);
				operLogData.put("resourceName", "终端用户名:" + loginInfo.getAccount());
				operLogData.put("operDuration", System.currentTimeMillis() - begin);
				operLogMapper.addOperLog(operLogData);
			} catch (Exception e) {
				logger.error(e);
			}

		}
		return new MethodResult(MethodResult.SUCCESS,"删除成功");
	} 
	
	@Callable
	@CallWithoutLogin
	@Transactional(readOnly = true)
	public MethodResult countVpcPrice(Map<String, String> parameter) { 
 		MethodResult result = new MethodResult(MethodResult.SUCCESS, "");
 	// 获取推荐配置
//		String item           = StringUtil.trim(parameter.get("item"));  
 		String ipAmount    = StringUtil.trim(parameter.get("ipAmount")); 
  		Integer region        = StringUtil.parseInteger((String)parameter.get("region"), null); 
		// 关联主机
		String hostId    = StringUtil.trim(parameter.get("hostId")); 
		// 创建主机
		String createhostinfo      = StringUtil.trim(parameter.get("createhostinfo")); 
         List<CloudHostVO> createhostList = new ArrayList<CloudHostVO>();
        BigDecimal price = BigDecimal.ZERO;
        int hostAmount = 0;
        if(!StringUtil.isBlank(createhostinfo)&& !createhostinfo.equals("[]")){
        	String [] cloudStrs = createhostinfo.split(",");
        	CloudHostVO vo = new CloudHostVO();
        	for(String cloudStr : cloudStrs){
        		cloudStr = cloudStr.replace("\"", "");
        		cloudStr = cloudStr.replace("]", "");
        		cloudStr = cloudStr.replace("[", "");
        		String [] cloudInfo = cloudStr.split("/");
        		vo.setCpuCore(Integer.parseInt(cloudInfo[1])); 
        		vo.setMemory(CapacityUtil.fromCapacityLabel(cloudInfo[2] + "GB"));
        		vo.setDataDisk(CapacityUtil.fromCapacityLabel(cloudInfo[3] + "GB"));
        		vo.setBandwidth(FlowUtil.fromFlowLabel(cloudInfo[4] + "Mbps"));
        		vo.setSysDisk(CapacityUtil.fromCapacityLabel("10GB"));
 
        		if(cloudInfo.length == 7){
        			PackagePriceMapper packagePriceMapper = this.sqlSession.getMapper(PackagePriceMapper.class);
    				List<PriceVO> priceList =  packagePriceMapper.getPriceByInfoId(cloudInfo[6]);
    				PackagePriceVO packagePriceVO = packagePriceMapper.getById(cloudInfo[6]);
     				if(priceList==null){					
    					return new MethodResult(MethodResult.FAIL, "套餐选择有误"); 
    				}
      				for(PriceVO priceVo : priceList){
     					if(priceVo.getStatus() == 2){
     						vo.setMonthlyPrice(priceVo.getMonthlyPrice());  
     					}     					
     				}
        		}else{        			
        			vo.setMonthlyPrice(CloudHostPrice.getMonthlyPrice(region, 2, vo.getCpuCore(), vo.getMemory(), vo.getDataDisk(), vo.getBandwidth()) );
        		}
        		for(int i=0;i<Integer.parseInt(cloudInfo[0]);i++){           			
         			createhostList.add(vo);
        			price = price.add( vo.getMonthlyPrice() );
        		} 
        	}
        	
        } 
        hostAmount = hostAmount + createhostList.size();
		CloudHostMapper cloudHostMapper                                     = this.sqlSession.getMapper(CloudHostMapper.class); 
		//绑定现有主机
		hostId = hostId.replace("\"", "");
		hostId = hostId.replace("]", "");
		hostId = hostId.replace("[", "");
		if(!StringUtil.isBlank(hostId)){
			String [] ids = hostId.split(",");
			for(int i = 0;i<ids.length;i++){
				ids[i] = ids[i].replace("\"", "");
				ids[i] = ids[i].replace("]", "");
				ids[i] = ids[i].replace("[", "");
				CloudHostVO host = cloudHostMapper.getById(ids[i]); 
				if(StringUtil.isBlank(host.getPackageId())){					
					price = price.add(CloudHostPrice.getMonthlyPrice(region, 2, host.getCpuCore(), host.getMemory(), host.getDataDisk(), host.getBandwidth()) );
				}else{
					PackagePriceMapper packagePriceMapper = this.sqlSession.getMapper(PackagePriceMapper.class);
    				List<PriceVO> priceList =  packagePriceMapper.getPriceByInfoId(host.getPackageId());
    				PackagePriceVO packagePriceVO = packagePriceMapper.getById(host.getPackageId());
      				if(priceList==null){					
     					price = price.add(CloudHostPrice.getMonthlyPrice(region, 2, host.getCpuCore(), host.getMemory(), host.getDataDisk(), host.getBandwidth()) );
     				}else{     					
     					boolean flag = false;
     					for(PriceVO priceVo : priceList){
     						if(priceVo.getStatus() == 2){
     							flag = true;
     							price = price.add(priceVo.getMonthlyPrice());
      						}     					
     					}
     					if(flag == false){
     						price = price.add(CloudHostPrice.getMonthlyPrice(region, 2, host.getCpuCore(), host.getMemory(), host.getDataDisk(), host.getBandwidth()) );
     						
     					}
     				}
				}
			} 
			hostAmount = hostAmount + ids.length;
		}
		if(hostAmount != 0){
			// 添加VPC价格
			price = price.add(VPCAmountAndPrice.getVpcPrice(region+"", hostAmount+""));
 		}
		// 添加IP价格
		price = price.add(new BigDecimal(AppProperties.getValue("ip_price_"+region, "")).multiply(new BigDecimal(ipAmount))).setScale(2,   BigDecimal.ROUND_HALF_UP);
		BigDecimal priceForHour = price.divide(new BigDecimal("720"),3).setScale(3,   BigDecimal.ROUND_HALF_UP);
		result.put("price", price);
		result.put("priceForHour", priceForHour);
	    return result;
	}

	/**
	 * 停用VPC
	 * @see com.zhicloud.op.service.VpcService#disableVpc(java.lang.String)
	 */
	@Callable
 	@Transactional(readOnly = false)
	public MethodResult disableVpc(String vpcId) {
		Long begin = System.currentTimeMillis();
		Integer logStatus = AppConstant.OPER_LOG_FAIL;
		HttpServletRequest request = RequestContext.getHttpRequest();
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class); 
		VpcBaseInfoVO vo = vpcBaseInfoMapper.queryVpcById(vpcId);
		if(vo == null ){
			return new MethodResult(MethodResult.FAIL, "未找到VPC信息");
		}
		if(StringUtil.isBlank(vo.getRealVpcId())){
			return new MethodResult(MethodResult.FAIL, "VPC未创建成功");			
		}
		NetworkService service = CoreSpringContextManager.getNetworkService();
		try {
			boolean result = service.stopSync(vo.getRegion(), vo.getRealVpcId());
			if(result){
				new VpcBillHelper(sqlSession).settleAllUndoneVpcBills(vo.getId(), new Date(), false);
				Map<String, Object> data = new LinkedHashMap<String, Object>();
				data.put("id",              vo.getId());                                                              
				data.put("modifyTime",      StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));         
				data.put("status",      2);         
				vpcBaseInfoMapper.updateVpc(data);
				
				logStatus = AppConstant.OPER_LOG_SUCCESS;
				return new MethodResult(MethodResult.SUCCESS, "停用成功");	
			}else{
				return new MethodResult(MethodResult.FAIL, "停用失败");							
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new AppException("停用失败");
			
		}finally {
			try {
				OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
				Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
				operLogData.put("id", StringUtil.generateUUID());
				operLogData.put("userId", loginInfo.getUserId());
				operLogData.put("content", "停用VPC:"+vo.getDisplayName());
				operLogData.put("operTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
				operLogData.put("status", logStatus);
				operLogData.put("resourceName", "终端用户名:" + loginInfo.getAccount());
				operLogData.put("operDuration", System.currentTimeMillis() - begin);
				operLogMapper.addOperLog(operLogData);
			} catch (Exception e) {
				logger.error(e);
			}

		}
 	}

	/**
	 * 跳转到VPC详情
	 * @see com.zhicloud.op.service.VpcService#toVpcDetail(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Callable
 	@Transactional(readOnly = false)
	public String toVpcDetail(HttpServletRequest request,
			HttpServletResponse response) {
		
		// TODO Auto-generated method stub
		return null;
	} 
 

	@Callable
	@Transactional(readOnly = false)
	public MethodResult addIpCount(String ipCount,String vpcId) {
		Long begin = System.currentTimeMillis();
		Integer logStatus = AppConstant.OPER_LOG_FAIL;
		HttpServletRequest request = RequestContext.getHttpRequest();
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if(ipCount==null){
			return new MethodResult(MethodResult.FAIL,"数量不能为空");
		}
		
		
		Integer count = Integer.parseInt(ipCount);
		VpcOuterIpMapper vpcOuterIpMapper = this.sqlSession.getMapper(VpcOuterIpMapper.class);
		VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class); 
 		VpcBaseInfoVO vpc =  vpcBaseInfoMapper.queryVpcById(vpcId);
		
		if(vpc.getType() == 1){
			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class); 
			TerminalUserVO terminalUserVO = terminalUserMapper.getTerminalUserById(vpc.getUserId());		
			//计算余额能否支持一天，不足不恢复主机
 			if(terminalUserVO!=null){				 
				BigDecimal totalPrice = new BigDecimal("0");
				//计算还需要新增的iP的价格
				totalPrice.add(new BigDecimal(AppProperties.getValue("ip_price_"+vpc.getRegion(), "")).multiply(new BigDecimal(ipCount)));
				//计算出每天的费用
				totalPrice = totalPrice.divide(new BigDecimal("31"),0,BigDecimal.ROUND_HALF_UP);
	  			//算出已有产品每天的费用
				totalPrice = totalPrice.add(new CountUserProductsPriceHelper(sqlSession).getAllOneDayPrice(terminalUserVO.getId()));
				BigDecimal balance = terminalUserVO.getAccountBalance().setScale(0, BigDecimal.ROUND_HALF_UP);
				if(balance.compareTo(totalPrice)<0){
					if(totalPrice.subtract(balance).compareTo(BigDecimal.TEN)<0){						
							return new MethodResult(MethodResult.FAIL, "余额不足，至少充值<a href='page.do?userType=4&bean=accountBalanceService&method=toRechargePage' target='blank'>10</a>元<br/>多充多优惠，<a href='page.do?userType=4&bean=accountBalanceService&method=toRechargePage' target='blank'>点击进入充值</a>");
					}else{						
							return new MethodResult(MethodResult.FAIL, "余额不足，至少充值<a href='page.do?userType=4&bean=accountBalanceService&method=toRechargePage' target='blank'>"+totalPrice.subtract(balance)+"</a>元<br/>多充多优惠，<a href='page.do?userType=4&bean=accountBalanceService&method=toRechargePage' target='blank'>点击进入充值</a>");
					}
						
				} 
				
			} 	
			
		}
		if(vpc.getType() == 2){
 			AgentMapper agentMapper = this.sqlSession.getMapper(AgentMapper.class); 
			AgentVO agentVO = agentMapper.getAgentByUserId(vpc.getUserId());		
			//计算余额能否支持一天，不足不恢复主机
			if(agentVO!=null){				 
				BigDecimal totalPrice = new BigDecimal("0");
				//计算还需要新增的iP的价格
				totalPrice.add(new BigDecimal(AppProperties.getValue("ip_price_"+vpc.getRegion(), "")).multiply(new BigDecimal(ipCount)));
				//计算出每天的费用
				totalPrice = totalPrice.divide(new BigDecimal("31"),0,BigDecimal.ROUND_HALF_UP);
	  			//算出已有产品每天的费用
				totalPrice = totalPrice.add(new CountUserProductsPriceHelper(sqlSession).getAllOneDayPrice(agentVO.getId()));
				BigDecimal balance = agentVO.getAccountBalance().setScale(0, BigDecimal.ROUND_HALF_UP);
				if(balance.compareTo(totalPrice)<0){
					if(totalPrice.subtract(balance).compareTo(BigDecimal.TEN)<0){						
						return new MethodResult(MethodResult.FAIL, "余额不足，至少充值<a href='page.do?userType=3&bean=agentService&method=rechargePage' target='blank'>10</a>元<br/>多充多优惠，<a href='page.do?userType=3&bean=agentService&method=rechargePage' target='blank'>点击进入充值</a>");
					}else{						
						return new MethodResult(MethodResult.FAIL, "余额不足，至少充值<a href='page.do?userType=3&bean=agentService&method=rechargePage' target='blank'>"+totalPrice.subtract(balance)+"</a>元<br/>多充多优惠，<a href='page.do?userType=4&bean=accountBalanceService&method=toRechargePage' target='blank'>点击进入充值</a>");
					}
					
				} 
				
			} 		
			
		}
		
					
		
		NetworkService service = CoreSpringContextManager.getNetworkService();
		NetworkInfoExt result;
		try {
			result = service.attachAddressSync(vpc.getRegion(), vpc.getRealVpcId(), Integer.parseInt(ipCount));
			if(result == null ){				
				return new MethodResult(MethodResult.SUCCESS,"删除失败");
			}
			new VpcBillHelper(sqlSession).settleAllUndoneVpcBills(vpcId, new Date(), true);
			for(int i=0;i<count;i++){
				Map<String,Object> condition = new HashMap<String, Object>();
				condition.put("id",StringUtil.generateUUID());
				condition.put("vpcId", vpcId);
				condition.put("createTime", DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
				condition.put("ip", result.getIp()[i]);
				vpcOuterIpMapper.add(condition);
			}
			Map<String,Object> data = new HashMap<String, Object>();
			data.put("id", vpcId);
			data.put("amount", ipCount);
			data.put("modifyTime", DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
			vpcBaseInfoMapper.updateVpcIpAmount(data);
			logStatus = AppConstant.OPER_LOG_SUCCESS;
			return  new MethodResult(MethodResult.SUCCESS,"添加成功");
		} catch (Exception e) {
			throw new AppException("添加失败");
			
		}finally {
			try {
				OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
				Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
				operLogData.put("id", StringUtil.generateUUID());
				operLogData.put("userId", loginInfo.getUserId());
				operLogData.put("content", "VPC:"+vpc.getDisplayName()+"增加"+ipCount+"个VPC外网IP");
				operLogData.put("operTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
				operLogData.put("status", logStatus);
				operLogData.put("resourceName", "终端用户名:" + loginInfo.getAccount());
				operLogData.put("operDuration", System.currentTimeMillis() - begin);
				operLogMapper.addOperLog(operLogData);
			} catch (Exception e) {
				logger.error(e);
			}

		}
	}

	@Callable
	@Transactional(readOnly = false)
	public MethodResult deleteIps(String vpcId,List<String> ips,List<String> ipValues) {
		Long begin = System.currentTimeMillis();
		Integer logStatus = AppConstant.OPER_LOG_FAIL;
		HttpServletRequest request = RequestContext.getHttpRequest();
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		String vpcName = "";
		try{
			if(ips == null || ips.size()<1){
				return new MethodResult(MethodResult.FAIL,"请选择需要删除的IP");
			}
			VpcOuterIpMapper vpcOuterIpMapper = this.sqlSession.getMapper(VpcOuterIpMapper.class);
			VpcBindPortMapper vpcBindPortMapper = this.sqlSession.getMapper(VpcBindPortMapper.class);
			VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class);
			CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
			VpcBaseInfoVO vpc = vpcBaseInfoMapper.queryVpcById(vpcId);
			vpcName = vpc.getDisplayName();
			NetworkService service = CoreSpringContextManager.getNetworkService();
			String [] ipToDelete = new String[ipValues.size()] ;
			for(int i = 0;i<ipValues.size();i++){
				ipToDelete[i] = ipValues.get(i);
			}
			NetworkInfoExt result = service.detachAddressSync(vpc.getRegion(), vpc.getRealVpcId(), ipToDelete);
			if(result == null ){				
				return new MethodResult(MethodResult.SUCCESS,"删除失败");
			}
			
			new VpcBillHelper(sqlSession).settleAllUndoneVpcBills(vpcId, new Date(), true); 
			
			vpcOuterIpMapper.deleteIps(ips);
			List<CloudHostVO> cloudHostList = cloudHostMapper.getCloudHostInVpc(vpcId);
			for(String ip : ipValues){
				vpcBindPortMapper.deleteByOuterIp(ip);
				// 如果有主机绑定了该ip，重新绑定
				for(CloudHostVO host : cloudHostList){
					if(ip.equals(host.getOuterIp())){
						this.defaultBindPort(vpcId, host);
					}
				}
			}
			Map<String,Object> data = new HashMap<String, Object>();
			data.put("id", vpcId);
			data.put("amount", -ips.size());
			data.put("modifyTime", DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
			vpcBaseInfoMapper.updateVpcIpAmount(data);
			logStatus = AppConstant.OPER_LOG_SUCCESS;
		}catch(Exception e){ 
				throw new AppException("删除失败"); 
		}finally {
			try {
				OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
				Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
				operLogData.put("id", StringUtil.generateUUID());
				operLogData.put("userId", loginInfo.getUserId());
				operLogData.put("content", "VPC:"+vpcName+"删除了"+ips.size()+"个VPC外网IP");
				operLogData.put("operTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
				operLogData.put("status", logStatus);
				operLogData.put("resourceName", "终端用户名:" + loginInfo.getAccount());
				operLogData.put("operDuration", System.currentTimeMillis() - begin);
				operLogMapper.addOperLog(operLogData);
			} catch (Exception e) {
				logger.error(e);
			}
		}
		return new MethodResult(MethodResult.SUCCESS,"删除成功");
	} 

	@Callable
	public String vpcNetworkManagePage(HttpServletRequest request,HttpServletResponse response) {
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		String vpcId = request.getParameter("vpcId");
		if(loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_OPERATOR){
		    VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class); 
            VpcBaseInfoVO vbi = vpcBaseInfoMapper.queryVpcById(vpcId);
            if (vbi == null) {
                request.setAttribute("message", "找不到vpc信息");
                return "/public/warning_dialog.jsp";
            }
            request.setAttribute("vpc",vbi);
			request.setAttribute("vpcId", vpcId);
			return "/security/operator/vpc_network_list.jsp";
		}
		Map<String,Object> condition = new HashMap<String, Object>();
		condition.put("vpcId", vpcId);
		VpcBindPortMapper vpcBindPortMapper = this.sqlSession.getMapper(VpcBindPortMapper.class); 
		List<VpcBindPortVO> vpcBindPortList = vpcBindPortMapper.queryByVpcId(condition);		
		request.setAttribute("vpcBindPortList", vpcBindPortList);
		request.setAttribute("vpcId", vpcId);
		if(loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT){
			return "/security/agent/vpc_network_manage.jsp";
		}
 		return "/security/user/vpc_network_manage.jsp";
	}

	@Callable
	public void getAllIps(HttpServletRequest request,HttpServletResponse response) {
		try{
			String vpcId = request.getParameter("vpcId");
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");
			VpcOuterIpMapper vpcOuterIpMapper = this.sqlSession.getMapper(VpcOuterIpMapper.class);
			Map<String,Object> condition = new HashMap<String, Object>();
			condition.put("vpcId",vpcId );
			List<VpcOuterIpVO> outerIpList = vpcOuterIpMapper.getAllIpByVpcId(condition);
			List<Map<String,String>> ipList = new ArrayList<Map<String,String>>();
			if(outerIpList!=null && outerIpList.size()>0){
				for(VpcOuterIpVO ip : outerIpList){
					Map<String,String> curMap = new HashMap<String, String>();
					curMap.put("id", ip.getIp());
					curMap.put("text", ip.getIp());
					ipList.add(curMap);
				}
			}
			response.getWriter().write(JSONLibUtil.toJSONString(ipList));
		}catch(Exception e){
			
		}
		
	}

	@Callable
	@Transactional(readOnly = false)
	public MethodResult addBindPortItem(String vpcId, String outerIp,String outerPort, String hostId, String port, String protocol) {
		Long begin = System.currentTimeMillis();
		Integer logStatus = AppConstant.OPER_LOG_FAIL;
		HttpServletRequest request = RequestContext.getHttpRequest();
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		String vpcName = "";
		try{
			if(StringUtil.isBlank(vpcId)){
				return new MethodResult(MethodResult.FAIL,"vpcId不能为空");
			}
			if(StringUtil.isBlank(outerIp)){
				return new MethodResult(MethodResult.FAIL,"外网IP不能为空");
			}
			if(StringUtil.isBlank(outerPort)){
				return new MethodResult(MethodResult.FAIL,"外网端口不能为空");
			}
			if(StringUtil.isBlank(hostId)){
				return new MethodResult(MethodResult.FAIL,"主机ID不能为空");
			}
			if(StringUtil.isBlank(port)){
				return new MethodResult(MethodResult.FAIL,"主机端口不能为空");
			}
			if(StringUtil.isBlank(protocol)){
				return new MethodResult(MethodResult.FAIL,"端口类型不能为空");
			}
			VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class);
			VpcBindPortMapper vpcBindPortMapper = this.sqlSession.getMapper(VpcBindPortMapper.class);
			CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
			CloudHostVO host = cloudHostMapper.getById(hostId);
			VpcBaseInfoVO vpc = vpcBaseInfoMapper.queryVpcById(vpcId);
			vpcName = vpc.getDisplayName();
			NetworkService service = CoreSpringContextManager.getNetworkService(); 
			String[] protocolList = new String[] {protocol};
			String[] ipList = new String[] { outerIp };
			String[] portList = new String[] { outerPort };
			String[] hostList = new String[] { host.getRealHostId() };
			String[] hostPortList = new String[] { port };

 			NetworkInfoExt network = service.bindPortSync(vpc.getRegion(), vpc.getRealVpcId(), protocolList, ipList, portList, hostList, hostPortList);
 			if(network == null){
 				return new MethodResult(MethodResult.FAIL,"新增失败");				
 			}
			
			Map<String,Object> checkMapOne = new HashMap<String, Object>();
			checkMapOne.put("outerIp", outerIp);
			checkMapOne.put("outerPort", outerPort);
			checkMapOne.put("protocol", protocol);
			Map<String,Object> checkMapTwo = new HashMap<String, Object>();
			checkMapTwo.put("hostId", hostId);
			checkMapTwo.put("hostPort", port);
			checkMapTwo.put("protocol", protocol);
			VpcBindPortVO vpcBindPortOne = vpcBindPortMapper.checkIpAndPortAndProtocol(checkMapOne);
			VpcBindPortVO vpcBindPortTwo = vpcBindPortMapper.checkHostAndPortAndProtocol(checkMapTwo);
			if(vpcBindPortOne!=null){
				return new MethodResult(MethodResult.FAIL,"IP"+outerIp+"的"+outerPort+"端口已绑定");
			}
			if(vpcBindPortTwo!=null){
				return new MethodResult(MethodResult.FAIL,"主机"+vpcBindPortTwo.getDisplayName()+"的"+port+"端口已绑定");
			}
			Map<String,Object> condition = new HashMap<String, Object>();
			condition.put("id",StringUtil.generateUUID());
			condition.put("vpc_id",vpcId);
			condition.put("host_id",hostId);
			condition.put("outer_ip",outerIp);
			condition.put("outer_port",Integer.parseInt(outerPort));
			condition.put("host_port",Integer.parseInt(port));
			condition.put("protocol",Integer.parseInt(protocol));
			condition.put("flag",0);
			condition.put("createTime",DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
			int result = vpcBindPortMapper.add(condition);
			if(result <= 0){
				return new MethodResult(MethodResult.FAIL,"添加失败");
			}
			logStatus = AppConstant.OPER_LOG_SUCCESS;
		}catch(Exception e){
			
		}finally {
			try {
				OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
				Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
				operLogData.put("id", StringUtil.generateUUID());
				operLogData.put("userId", loginInfo.getUserId());
				operLogData.put("content", "VPC:"+vpcName+"绑定了一个端口"+port);
				operLogData.put("operTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
				operLogData.put("status", logStatus);
				operLogData.put("resourceName", "终端用户名:" + loginInfo.getAccount());
				operLogData.put("operDuration", System.currentTimeMillis() - begin);
				operLogMapper.addOperLog(operLogData);
			} catch (Exception e) {
				logger.error(e);
			}
		}
		return new MethodResult(MethodResult.SUCCESS,"添加成功");
	}
	
	@Callable
	@Transactional(readOnly = false)
	public MethodResult deleteBindPorts(String vpcId,List<String> ids) {
		Long begin = System.currentTimeMillis();
		Integer logStatus = AppConstant.OPER_LOG_FAIL;
		HttpServletRequest request = RequestContext.getHttpRequest();
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		String vpcName = "";
		try{
			if(ids == null || ids.size()<1){
				return new MethodResult(MethodResult.FAIL,"请选择需要删除的项");
			}
			if(StringUtil.isBlank(vpcId)){
				return new MethodResult(MethodResult.FAIL,"vpcId不能为空");
			}
			VpcBindPortMapper vpcBindPortMapper = this.sqlSession.getMapper(VpcBindPortMapper.class);
			VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class);
			VpcBaseInfoVO baseInfo = vpcBaseInfoMapper.queryVpcById(vpcId);
			vpcName = baseInfo.getDisplayName();
			String[] protocolList = new String[ids.size()];
			String[] ipList = new String[ids.size()];
			String[] portList = new String[ids.size()];
			for(int i=0;i<ids.size();i++){
				VpcBindPortVO bindPort = vpcBindPortMapper.queryById(ids.get(i));
				protocolList[i] = bindPort.getProtocol().toString();
				ipList[i] = bindPort.getOuterIp();
				portList[i] = bindPort.getOuterPort().toString();
			}
			NetworkService service = CoreSpringContextManager.getNetworkService();
			NetworkInfoExt result = service.unbindPortSync(baseInfo.getRegion(), baseInfo.getRealVpcId(), protocolList, ipList, portList);
			if(result!=null){
				vpcBindPortMapper.deleteByIds(ids);
				logStatus = AppConstant.OPER_LOG_SUCCESS;
			}else{
				return new MethodResult(MethodResult.FAIL,"删除失败");
			}
		}catch(Exception e){
		}finally {
			try {
				OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
				Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
				operLogData.put("id", StringUtil.generateUUID());
				operLogData.put("userId", loginInfo.getUserId());
				operLogData.put("content", "VPC:"+vpcName+"删除了"+ids.size()+"条端口绑定记录");
				operLogData.put("operTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
				operLogData.put("status", logStatus);
				operLogData.put("resourceName", "终端用户名:" + loginInfo.getAccount());
				operLogData.put("operDuration", System.currentTimeMillis() - begin);
				operLogMapper.addOperLog(operLogData);
			} catch (Exception e) {
				logger.error(e);
			}
		}
		return new MethodResult(MethodResult.SUCCESS,"删除成功");
	}
	
	/**
	 * 通过hostId解除绑定
	 * @see com.zhicloud.op.service.VpcService#unboundByHostId(java.lang.String)
	 */
	@Callable
	@Transactional(readOnly = false)
	public MethodResult unboundByHostId(String hostId) {
		Long begin = System.currentTimeMillis();
		Integer logStatus = AppConstant.OPER_LOG_FAIL;
		HttpServletRequest request = RequestContext.getHttpRequest();
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class);
		VpcBindHostMapper vpcBindHostMapper = this.sqlSession.getMapper(VpcBindHostMapper.class);
		VpcBindPortMapper vpcBindPortMapper = this.sqlSession.getMapper(VpcBindPortMapper.class);
		CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
		
		if(StringUtil.isBlank(hostId)){
			return new MethodResult(MethodResult.FAIL,"请选择需要解除绑定的云主机");
		}
		VpcBaseInfoVO vpcBaseInfoVO = vpcBaseInfoMapper.queryVpcByHostId(hostId);
		if(vpcBaseInfoVO == null){
			return new MethodResult(MethodResult.FAIL,"未查询到VPC信息");			
		}
		List<CloudHostVO> hostList = cloudHostMapper.getCloudHostInVpc(vpcBaseInfoVO.getId());
		if(hostList.size()  == 2){
			return new MethodResult(MethodResult.FAIL,"最少应该保留两个主机");									
		}
		CloudHostVO cloudHost = cloudHostMapper.getById(hostId);
		// 结算产生新的订单
		try {
			//暂时先用主机删除的接口，后面换成下面的detach-host接口
			HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(cloudHost.getRegion());
			JSONObject hostDeleteResult = channel.hostDelete(cloudHost.getRealHostId());
			if (HttpGatewayResponseHelper.isSuccess(hostDeleteResult)) {			
//			NetworkService service = CoreSpringContextManager.getNetworkService();
//			boolean result = service.detachHostSync(vpcBaseInfoVO.getRegion(), vpcBaseInfoVO.getRealVpcId(), cloudHost.getRealHostId());
//			if(result){
				
				new VpcBillHelper(sqlSession).settleAllUndoneVpcBills(vpcBaseInfoVO.getId(), new Date(), true);
				
				
				Map<String, Object> data = new LinkedHashMap<String, Object>();
				data.put("id", vpcBaseInfoVO.getId());
				data.put("amount", -1);
				vpcBaseInfoMapper.updateVpcHostAmount(data);
				
				Map<String,Object> condition = new HashMap<String, Object>();
				condition.put("hostIds",new String[]{hostId});
				condition.put("removeTime",DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
				vpcBindHostMapper.deleteLogical(condition);
				
				// 逻辑删除
				String[] strArray={hostId};
				cloudHostMapper.updateForDeleteByIds(strArray);
				
				//删除端口绑定
				vpcBindPortMapper.deleteByHostIds(strArray);
				
				logStatus = AppConstant.OPER_LOG_SUCCESS;
				return new MethodResult(MethodResult.SUCCESS,"解除绑定成功");
			}else{
				return new MethodResult(MethodResult.FAIL,"解除绑定失败");
				
			}
		} catch (Exception e) {			
 			e.printStackTrace();
 			throw new AppException("解除绑定失败");
		}finally {
			try {
				OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
				Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
				operLogData.put("id", StringUtil.generateUUID());
				operLogData.put("userId", loginInfo.getUserId());
				operLogData.put("content", "VPC:"+vpcBaseInfoVO.getDisplayName()+"解除了绑定的"+cloudHost.getDisplayName()+"云主机");
				operLogData.put("operTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
				operLogData.put("status", logStatus);
				operLogData.put("resourceName", "终端用户名:" + loginInfo.getAccount());
				operLogData.put("operDuration", System.currentTimeMillis() - begin);
				operLogMapper.addOperLog(operLogData);
			} catch (Exception e) {
				logger.error(e);
			}
		}
		
 	}

	/**
	 * 通过主机Id绑定云主机
	 * @see com.zhicloud.op.service.VpcService#boundByHostIds(java.util.List)
	 */
	@Callable
	@Transactional(readOnly = false)
	public MethodResult boundByHostIds(List<String> ids,String vpcId) {
		Long begin = System.currentTimeMillis();
		Integer logStatus = AppConstant.OPER_LOG_FAIL;
		HttpServletRequest request = RequestContext.getHttpRequest();
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		String hostNames = "";
		
		VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class);
		VpcBindHostMapper vpcBindHostMapper = this.sqlSession.getMapper(VpcBindHostMapper.class);
		CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
		List<CloudHostVO> hostList = cloudHostMapper.getCloudHostInVpc(vpcId);
		if(hostList.size() + ids.size() >15){
			return new MethodResult(MethodResult.FAIL,"最多只能绑定15个主机");									
		}

		if(ids == null ||ids.size() == 0){
			return new MethodResult(MethodResult.FAIL,"请选择需要绑定的主机");						
		}
		VpcBaseInfoVO vpcBaseInfoVO = vpcBaseInfoMapper.queryVpcById(vpcId);
		if(vpcBaseInfoVO == null){
			return new MethodResult(MethodResult.FAIL,"未查询到VPC信息");			
		}
		// 结算
 		try {
			new VpcBillHelper(sqlSession).settleAllUndoneVpcBills(vpcBaseInfoVO.getId(), new Date(), true);
			NetworkService service = CoreSpringContextManager.getNetworkService();
			Map<String, Object> data = new LinkedHashMap<String, Object>();
			
			for(int i = 0;i<ids.size();i++){ 
				CloudHostVO host = cloudHostMapper.getById(ids.get(i));
				if(i != 0){
					hostNames = hostNames+",";
				}
				hostNames = hostNames + host.getDisplayName();
				NetworkInfoExt attachHostResult = service.attachHostSync(vpcBaseInfoVO.getRegion(), vpcBaseInfoVO.getRealVpcId(), host.getRealHostId());
				if(attachHostResult != null){
					//主机加入vpc成功，先结算
					new CloudHostBillingHelper(sqlSession).startOneCloudHostBilling(new Date(), true);
					//增加主机和VPC关联
					data.put("id", StringUtil.generateUUID());
					data.put("vpcId", vpcBaseInfoVO.getId());
					data.put("hostId", host.getId());
					data.put("createTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
					vpcBindHostMapper.add(data);
					Integer type = AppConstant.CLOUD_HOST_TYPE_5_TERMINAL_USER_VPC;
					if(host.getType() == 2){
						type = AppConstant.CLOUD_HOST_TYPE_6_Agent_VPC;
					}
					//更新主机的类型，以便后续的查询和计费使用
					data.put("id", host.getId());
					data.put("type", type);
					cloudHostMapper.updateHostTypeById(data);	
					data.put("vpcIp", attachHostResult.getHostNetworkAddress());
					cloudHostMapper.updateVpcIpById(data);
					new CloudHostBillingHelper(sqlSession).settleAllUndoneCloudHostBills(host.getId(), new Date(), true);
					defaultBindPort(vpcId, host);
				}else{
					throw new AppException("绑定失败");
				}
			}
			//更新主机个数
			data.put("id", vpcBaseInfoVO.getId());
			data.put("amount", ids.size());
			vpcBaseInfoMapper.updateVpcHostAmount(data);
			logStatus = AppConstant.OPER_LOG_SUCCESS;
			return new MethodResult(MethodResult.SUCCESS,"绑定成功");
		} catch (Exception e) {			
 			e.printStackTrace();
 			throw new AppException("解除绑定失败");
		}finally {
			try {
				OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
				Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
				operLogData.put("id", StringUtil.generateUUID());
				operLogData.put("userId", loginInfo.getUserId());
				operLogData.put("content", "VPC:"+vpcBaseInfoVO.getDisplayName()+"绑定了云主机"+hostNames);
				operLogData.put("operTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
				operLogData.put("status", logStatus);
				operLogData.put("resourceName", "终端用户名:" + loginInfo.getAccount());
				operLogData.put("operDuration", System.currentTimeMillis() - begin);
				operLogMapper.addOperLog(operLogData);
			} catch (Exception e) {
				logger.error(e);
			}
		} 
 	}



/**
	 * 恢复VPC
	 * @see com.zhicloud.op.service.VpcService#ableVpc(java.lang.String)
	 */
	@Callable
	@Transactional(readOnly = false)
	public MethodResult ableVpc(String vpcId) {
		Long begin = System.currentTimeMillis();
		Integer logStatus = AppConstant.OPER_LOG_FAIL;
		HttpServletRequest request = RequestContext.getHttpRequest();
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		
		VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class); 
		CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class); 
		VpcBaseInfoVO vo = vpcBaseInfoMapper.queryVpcById(vpcId);
 		if(vo == null ){
			return new MethodResult(MethodResult.FAIL, "未找到VPC信息");
		}
		if(StringUtil.isBlank(vo.getRealVpcId())){
			return new MethodResult(MethodResult.FAIL, "VPC未创建成功");			
		}
		
		if(vo.getType() == 1){
			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class); 
			TerminalUserVO terminalUserVO = terminalUserMapper.getTerminalUserById(loginInfo.getUserId());		
			//计算余额能否支持一天，不足不恢复主机
			if(terminalUserVO!=null){				 
				BigDecimal totalPrice = new BigDecimal("0");
				//算出已有产品每天的费用
				//加上VPC价格
				totalPrice.add(VPCAmountAndPrice.getVpcPrice(vo.getRegion()+"", vo.getHostAmount()+""));
				//加上IP价格
				totalPrice.add(new BigDecimal(AppProperties.getValue("ip_price_"+vo.getRegion(), "")).multiply(new BigDecimal(vo.getIpAmount())));
				List<CloudHostVO> hostList = cloudHostMapper.getCloudHostInVpc(vpcId); 
				
				//计算出每天的费用
				totalPrice = totalPrice.divide(new BigDecimal("31"),0,BigDecimal.ROUND_HALF_UP); 
				totalPrice = totalPrice.add(new CountUserProductsPriceHelper(sqlSession).getAllOneDayPrice(terminalUserVO.getId()));
				BigDecimal balance = terminalUserVO.getAccountBalance().setScale(0, BigDecimal.ROUND_HALF_UP);
				if(balance.compareTo(totalPrice)<0){
					if(totalPrice.subtract(balance).compareTo(BigDecimal.TEN)<0){						
						return new MethodResult(MethodResult.FAIL, "余额不足，至少充值<a href='page.do?userType=4&bean=accountBalanceService&method=toRechargePage' target='blank'>10</a>元<br/>多充多优惠，<a href='page.do?userType=4&bean=accountBalanceService&method=toRechargePage' target='blank'>点击进入充值</a>");
					}else{						
						return new MethodResult(MethodResult.FAIL, "余额不足，至少充值<a href='page.do?userType=4&bean=accountBalanceService&method=toRechargePage' target='blank'>"+totalPrice.subtract(balance)+"</a>元<br/>多充多优惠，<a href='page.do?userType=4&bean=accountBalanceService&method=toRechargePage' target='blank'>点击进入充值</a>");
					}
					
				} 
				
			} 		
			
		}
		if(vo.getType() == 2){
			AgentMapper agentMapper = this.sqlSession.getMapper(AgentMapper.class); 
			AgentVO agentVO = agentMapper.getAgentByUserId(vo.getUserId());		
			//计算余额能否支持一天，不足不恢复主机
			if(agentVO!=null){				 
				BigDecimal totalPrice = new BigDecimal("0");
				//算出已有产品每天的费用
				//加上VPC价格
				totalPrice.add(VPCAmountAndPrice.getVpcPrice(vo.getRegion()+"", vo.getHostAmount()+""));
				//加上IP价格
				totalPrice.add(new BigDecimal(AppProperties.getValue("ip_price_"+vo.getRegion(), "")).multiply(new BigDecimal(vo.getIpAmount())));
				List<CloudHostVO> hostList = cloudHostMapper.getCloudHostInVpc(vpcId); 
				
				//计算出每天的费用
				totalPrice = totalPrice.divide(new BigDecimal("31"),0,BigDecimal.ROUND_HALF_UP); 
				totalPrice = totalPrice.add(new CountUserProductsPriceHelper(sqlSession).getAgentAllOneDayPrice(agentVO.getId()));
				BigDecimal balance = agentVO.getAccountBalance().setScale(0, BigDecimal.ROUND_HALF_UP);
				if(balance.compareTo(totalPrice)<0){
					if(totalPrice.subtract(balance).compareTo(BigDecimal.TEN)<0){						
						return new MethodResult(MethodResult.FAIL, "余额不足，至少充值<a href='page.do?userType=3&bean=agentService&method=rechargePage' target='blank'>10</a>元<br/>多充多优惠，<a href='page.do?userType=3&bean=agentService&method=rechargePage' target='blank'>点击进入充值</a>");
					}else{						
						return new MethodResult(MethodResult.FAIL, "余额不足，至少充值<a href='page.do?userType=3&bean=agentService&method=rechargePage' target='blank'>"+totalPrice.subtract(balance)+"</a>元<br/>多充多优惠，<a href='page.do?userType=3&bean=agentService&method=rechargePage' target='blank'>点击进入充值</a>");
					}
					
				} 
				
			} 		
			
		}
		
		NetworkService service = CoreSpringContextManager.getNetworkService();
		try {
			boolean result = service.startAsync(vo.getRegion(), vo.getRealVpcId());
			if(result){
 				
				Map<String, Object> data = new LinkedHashMap<String, Object>();
				data.put("id",              vo.getId());                                                              
				data.put("modifyTime",      StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));         
				data.put("status",      1);         
				vpcBaseInfoMapper.updateVpc(data);				
				logStatus = AppConstant.OPER_LOG_SUCCESS;
				
				
				new VpcBillHelper(sqlSession).startOneNewVpcBillDetail(vpcId, new Date());
				return new MethodResult(MethodResult.SUCCESS, "恢复成功");	
			}else{
				return new MethodResult(MethodResult.FAIL, "恢复失败");							
			}
		} catch (Exception e) {
			throw new AppException("恢复失败");
			
		}finally {
			if(vo!=null){
				try {
					OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
					Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
					operLogData.put("id", StringUtil.generateUUID());
					operLogData.put("userId", loginInfo.getUserId());
					operLogData.put("content", "恢复VPC:"+vo.getDisplayName());
					operLogData.put("operTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
					operLogData.put("status", logStatus);
					operLogData.put("resourceName", "终端用户名:" + loginInfo.getAccount());
					operLogData.put("operDuration", System.currentTimeMillis() - begin);
					operLogMapper.addOperLog(operLogData);
				} catch (Exception e) {
					logger.error(e);
				}
			}
		}
	}

	@Callable
	@Transactional(readOnly = false)
	public MethodResult updateVpcDisplayNameById(String vpcId, String newDisplayName) {
		Long begin = System.currentTimeMillis();
		Integer logStatus = AppConstant.OPER_LOG_FAIL;
		HttpServletRequest request = RequestContext.getHttpRequest();
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class);
		VpcBaseInfoVO vpcBaseInfo = vpcBaseInfoMapper.queryVpcById(vpcId);
		try {
			//
			Map<String, Object> vpcData = new LinkedHashMap<String, Object>();
			vpcData.put("id", vpcId);
			vpcData.put("displayName", newDisplayName);
			vpcData.put("modifyTime", DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
			int n = vpcBaseInfoMapper.updateVpc(vpcData);
			if (n > 0) {
				logStatus = AppConstant.OPER_LOG_SUCCESS;
				return new MethodResult(MethodResult.SUCCESS, "VPC名称修改成功");
			} else {
				return new MethodResult(MethodResult.FAIL, "VPC名称修改失败");
			}
		} catch (AppException e) {
			throw e;
		} catch (Exception e) {
			throw new AppException(e);
		}finally {
			try {
				OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
				Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
				operLogData.put("id", StringUtil.generateUUID());
				operLogData.put("userId", loginInfo.getUserId());
				operLogData.put("content", "VPC:"+vpcBaseInfo.getDisplayName()+"的名字修改为："+newDisplayName);
				operLogData.put("operTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
				operLogData.put("status", logStatus);
				operLogData.put("resourceName", "终端用户名:" + loginInfo.getAccount());
				operLogData.put("operDuration", System.currentTimeMillis() - begin);
				operLogMapper.addOperLog(operLogData);
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}

	/**
	 * 新增VPC主机
	 * @see com.zhicloud.op.service.VpcService#addNewHostToVpcBaseInfo(java.util.Map)
	 */
	@Callable
	@Transactional(readOnly = false)
	public MethodResult addNewHostToVpcBaseInfo(Map<String, String> parameter) {
		logger.debug("VpcServiceImpl.addNewHostToVpcBaseInfo()");
		Long begin = System.currentTimeMillis();
		Integer logStatus = AppConstant.OPER_LOG_FAIL;
		HttpServletRequest request = RequestContext.getHttpRequest();
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		String userId = loginInfo.getUserId();
		String _userName = loginInfo.getAccount();
		String resourceName = "";
		try
		{
 			String cpuCore_str    = StringUtil.trim(parameter.get("cpu"));
			// 获取推荐配置
			String item           = StringUtil.trim(parameter.get("item"));  
			String sysImageId     = StringUtil.trim(parameter.get("sysImageId"));
			String dataDisk_str   = StringUtil.trim(parameter.get("dataDisk"));
			String bandwidth_str  = StringUtil.trim(parameter.get("bandwidth")); 
 			String hostAmount    = StringUtil.trim(parameter.get("hostAmount"));
			String vpcId    = StringUtil.trim(parameter.get("vpcId"));
			if(StringUtil.isBlank(vpcId)){				
				return new MethodResult(MethodResult.FAIL, "错误VPC");
			}
			VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class);
			VpcBaseInfoVO vpc = vpcBaseInfoMapper.queryVpcById(vpcId);
			
			if(vpc == null ){
				
				return new MethodResult(MethodResult.FAIL, "未找到VPC信息");
			}
			resourceName = "专属云"+vpc.getDisplayName()+"添加主机：";
			if(StringUtil.isBlank(hostAmount)){
				
				return new MethodResult(MethodResult.FAIL, "请输入主机个数");
			} 
			if(dataDisk_str==null||dataDisk_str.equals("")){
				
				dataDisk_str   = StringUtil.trim(parameter.get("dataDiskDIY"));
			}
			if(bandwidth_str==null||bandwidth_str.equals("")){
				
				bandwidth_str   = StringUtil.trim(parameter.get("bandwidthDIY"));
			} 
			String memory_str     = StringUtil.trim(parameter.get("memory")); 
 			
			String price = "0";
 			String duration = "0";
 			Integer cpuCore = null;
			BigInteger memory = null;
			BigInteger sysDisk = null;
			BigInteger dataDisk = null;
			BigInteger bandwidth = null;  
			OrderInfoMapper orderInfoMapper                                     = this.sqlSession.getMapper(OrderInfoMapper.class);
			OrderDetailMapper orderDetailMapper                                 = this.sqlSession.getMapper(OrderDetailMapper.class);
			CloudHostShoppingConfigMapper cloudHostShoppingConfigMapper         = this.sqlSession.getMapper(CloudHostShoppingConfigMapper.class);
 			CloudHostMapper cloudHostMapper                                     = this.sqlSession.getMapper(CloudHostMapper.class);
 			SysDiskImageMapper sysDiskImageMapper               = this.sqlSession.getMapper(SysDiskImageMapper.class); 
			VpcBindHostMapper vpcBindHostMapper = this.sqlSession.getMapper(VpcBindHostMapper.class); 

			
			
			CloudHostBillDetailMapper cloudHostBillDetailMapper                 = this.sqlSession.getMapper(CloudHostBillDetailMapper.class);
 			if(item!=null&&!(item.equals(""))){
				PackagePriceMapper packagePriceMapper = this.sqlSession.getMapper(PackagePriceMapper.class);
				List<PriceVO> priceList =  packagePriceMapper.getPriceByInfoId(item);
				PackagePriceVO packagePriceVO = packagePriceMapper.getById(item);
 				if(priceList==null){					
					return new MethodResult(MethodResult.FAIL, "套餐选择有误"); 
				}
  				for(PriceVO priceVo : priceList){
 					if(priceVo.getStatus() == 2){
 						price = priceVo.getMonthlyPrice() +"";
 						if (priceVo.getMonthlyPrice().compareTo(BigDecimal.ZERO)<=0)
 						{ 
 							List<CloudHostVO> cloudHostList = cloudHostMapper.getByUserId(loginInfo.getUserId());
 							if(cloudHostList!=null&&cloudHostList.size()>0){
 								for(CloudHostVO vo:cloudHostList){
 									if(vo.getMonthlyPrice().compareTo(BigDecimal.ZERO)==0){
 										
 										return new MethodResult(MethodResult.FAIL, "您已经申请过免费试用，请选择其他套餐");
 									}
 								} 
 								
 							}
 						}  
 					}
 					
 				}
				cpuCore = packagePriceVO.getCpuCore();
				memory = packagePriceVO.getMemory();
				dataDisk = packagePriceVO.getDataDisk();
				bandwidth = packagePriceVO.getBandwidth();
				
				 
			}else{
				if(cpuCore_str==null||cpuCore_str.equals("")){
					return new MethodResult(MethodResult.FAIL, "请选择CPU");
				} 
				cpuCore = Integer.parseInt(cpuCore_str);
				if(memory_str==null||memory_str.equals("")){
					return new MethodResult(MethodResult.FAIL, "请选择内存");
					
				} 
				memory      = CapacityUtil.fromCapacityLabel(memory_str + "GB"); 
				if(dataDisk_str==null||dataDisk_str.equals("")){
					return new MethodResult(MethodResult.FAIL, "请选择硬盘");
					
				} 
				dataDisk    = CapacityUtil.fromCapacityLabel(dataDisk_str + "GB");
				if(bandwidth_str==null||bandwidth_str.equals("")){
					return new MethodResult(MethodResult.FAIL, "请选择带宽");
					
				} 
				bandwidth   = FlowUtil.fromFlowLabel(bandwidth_str+"Mbps");;
				price =  CloudHostPrice.getMonthlyPrice(vpc.getRegion(),2,cpuCore, memory, dataDisk, bandwidth).setScale(2,   BigDecimal.ROUND_HALF_UP)+"";
			}
			sysDisk  = CapacityUtil.fromCapacityLabel("10GB"); 
			
			
			//计算余额能否支持一天，不足不创建主机
			
			Integer type = AppConstant.CLOUD_HOST_TYPE_5_TERMINAL_USER_VPC;
			if(loginInfo.getUserType() ==  AppConstant.SYS_USER_TYPE_AGENT){
				type = AppConstant.CLOUD_HOST_TYPE_6_Agent_VPC;				
			}
			if(vpc.getType() == 1){
				TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class); 
				TerminalUserVO terminalUserVO = terminalUserMapper.getTerminalUserById(vpc.getUserId());		
				//计算余额能否支持一天，不足不恢复主机
				if(terminalUserVO!=null){				 
					BigDecimal totalPrice = new BigDecimal("0");
					//算出已有产品每天的费用
					//计算还需要新增的主机的价格
					totalPrice.add(new BigDecimal(price));
					//计算出每天的费用
					totalPrice = totalPrice.divide(new BigDecimal("31"),0,BigDecimal.ROUND_HALF_UP); 
					totalPrice = totalPrice.add(new CountUserProductsPriceHelper(sqlSession).getAllOneDayPrice(terminalUserVO.getId()));
					BigDecimal balance = terminalUserVO.getAccountBalance().setScale(0, BigDecimal.ROUND_HALF_UP);
					if(balance.compareTo(totalPrice)<0){
						if(totalPrice.subtract(balance).compareTo(BigDecimal.TEN)<0){						
								return new MethodResult(MethodResult.FAIL, "余额不足，至少充值<a href='page.do?userType=4&bean=accountBalanceService&method=toRechargePage' target='blank'>10</a>元<br/>多充多优惠，<a href='page.do?userType=4&bean=accountBalanceService&method=toRechargePage' target='blank'>点击进入充值</a>");
						}else{						
								return new MethodResult(MethodResult.FAIL, "余额不足，至少充值<a href='page.do?userType=4&bean=accountBalanceService&method=toRechargePage' target='blank'>"+totalPrice.subtract(balance)+"</a>元<br/>多充多优惠，<a href='page.do?userType=4&bean=accountBalanceService&method=toRechargePage' target='blank'>点击进入充值</a>");
						}
							
					} 
					
				} 		
				
			}
			if(vpc.getType() == 2){
				AgentMapper agentMapper = this.sqlSession.getMapper(AgentMapper.class); 
				AgentVO agentVO = agentMapper.getAgentByUserId(vpc.getUserId());		
				//计算余额能否支持一天，不足不恢复主机
				if(agentVO!=null){				 
					BigDecimal totalPrice = new BigDecimal("0");
					//算出已有产品每天的费用
					//计算还需要新增的主机的价格
					totalPrice.add(new BigDecimal(price));
					//计算出每天的费用
					totalPrice = totalPrice.divide(new BigDecimal("31"),0,BigDecimal.ROUND_HALF_UP); 
					totalPrice = totalPrice.add(new CountUserProductsPriceHelper(sqlSession).getAllOneDayPrice(agentVO.getId()));
					BigDecimal balance = agentVO.getAccountBalance().setScale(0, BigDecimal.ROUND_HALF_UP); 
					if(balance.compareTo(totalPrice)<0){
						if(totalPrice.subtract(balance).compareTo(BigDecimal.TEN)<0){						
							return new MethodResult(MethodResult.FAIL, "余额不足，至少充值<a href='page.do?userType=3&bean=agentService&method=rechargePage' target='blank'>10</a>元<br/>多充多优惠，<a href='page.do?userType=3&bean=agentService&method=rechargePage' target='blank'>点击进入充值</a>");
						}else{						
							return new MethodResult(MethodResult.FAIL, "余额不足，至少充值<a href='page.do?userType=3&bean=agentService&method=rechargePage' target='blank'>"+totalPrice.subtract(balance)+"</a>元<br/>多充多优惠，<a href='page.do?userType=3&bean=agentService&method=rechargePage' target='blank'>点击进入充值</a>");
						}
						
					} 
					
				} 		
				
			}
			
			 	 
			int region = vpc.getRegion();
			
			// 结算一次
			
			new VpcBillHelper(sqlSession).settleAllUndoneVpcBills(vpcId, new Date(), true);
			
			
			for(int i =0 ;i<Integer.parseInt(hostAmount);i++){
				String now = StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS");
				String configId       = StringUtil.generateUUID();
				String hostName       = this.getNewCloudHostNameByUserIdForVpc(userId, vpc.getRegion()+"");
				resourceName          = resourceName +" "+hostName;
				String cloudHostId    = null;
				Integer processStatus = null;
				String processMessage = null;
				
				List<String> host_ports = new ArrayList<String>();
				
				// 从云主机仓库获取云主机
				Map<String, Object> newCloudHostData = new LinkedHashMap<String, Object>();
				newCloudHostData.put("type",          type);
				newCloudHostData.put("userId",        vpc.getUserId());
				newCloudHostData.put("hostName",      hostName);
				newCloudHostData.put("displayName",      hostName);
				newCloudHostData.put("account",       loginInfo.getAccount());
				newCloudHostData.put("password",      RandomPassword.getRandomPwd(16));
				newCloudHostData.put("cpuCore",       Integer.valueOf(cpuCore));
				newCloudHostData.put("memory",        memory);
				newCloudHostData.put("dataDisk",      dataDisk);
				newCloudHostData.put("bandwidth",     bandwidth);
				newCloudHostData.put("isAutoStartup", AppConstant.CLOUD_HOST_IS_AUTO_STARTUP_YES);
				newCloudHostData.put("monthlyPrice", new BigDecimal(price));
				newCloudHostData.put("item", item);
				newCloudHostData.put("ports",         _formatToHttpGatewayFormatPorts(host_ports));
				MethodResult result = new CloudHostWarehouseHelper(this.sqlSession).getAndAllocateWarehouseCloudHost(sysImageId, vpc.getRegion(), newCloudHostData);
				int orderInsertResult = 0; 
				int shoppingConfigInsertResult = 0;
				String orderId = StringUtil.generateUUID(); 
				int orderDetailInsertResult = 0;
				if( MethodResult.SUCCESS.equals(result.status) )
				{
					CloudHostWarehouseDetailVO vo = (CloudHostWarehouseDetailVO) result.get("CloudHostWarehouseDetailVO");
					
					
					
					// 新增订单
					Map<String, Object> newOrder = new LinkedHashMap<String, Object>();
					newOrder.put("id",             orderId);
					newOrder.put("userId",         vpc.getUserId());
					newOrder.put("createTime",     now);
					newOrder.put("totalPrice",     price);
					newOrder.put("isPaid",         AppConstant.ORDER_INFO_IS_PAID_YES);						// 试用的云主机状态默认为已付费
					newOrder.put("processStatus",  AppConstant.ORDER_INFO_PROCESS_STATUS_SUCCESS);
					newOrder.put("processMessage", "");
					orderInsertResult = orderInfoMapper.addOrderInfo(newOrder);
					
					if (orderInsertResult <= 0)
					{
						throw new AppException("添加订单失败");
					}
					
					
					// 从云主机仓库获取云主机 
					logger.info("PaymentServiceImpl.getCloudHost() > ["+Thread.currentThread().getId()+"] 云主机仓库获取的库存云主机成功, sysImageId:["+sysImageId+"], dataDisk:["+dataDisk+"], region:["+region+"]");
					cloudHostId    = vo.getHostId();
					CloudHostVO host = cloudHostMapper.getById(cloudHostId);
					processStatus  = AppConstant.CLOUD_HOST_SHOPPING_CONFIG_PROCESS_STATUS_SUCCESS;
					processMessage = "fetch from cloud host warehouse"; 
					
					// 新增配置
					Map<String, Object> newConfig = new LinkedHashMap<String, Object>();
					newConfig.put("id",             configId);
					newConfig.put("hostId",         cloudHostId);
					newConfig.put("userId",         vpc.getUserId());
					newConfig.put("type",           AppConstant.CLOUD_HOST_SHOPPING_CONFIG_TYPE_TRAIL);
					newConfig.put("cpuCore",        cpuCore);
					newConfig.put("memory",         memory);
					newConfig.put("sysImageId",     sysImageId);
					newConfig.put("sysDisk",        sysDisk);
					newConfig.put("dataDisk",       dataDisk);
					newConfig.put("bandwidth",      bandwidth);
					newConfig.put("price",          price);
					newConfig.put("createTime",     now);
					newConfig.put("duration",       duration);
					newConfig.put("processStatus",  processStatus);
					newConfig.put("processMessage", processMessage);
					newConfig.put("hostName",       hostName);
					newConfig.put("region",       region);
					shoppingConfigInsertResult = cloudHostShoppingConfigMapper.addCloudHostShoppingConfig(newConfig);
					 
					
					// 新增订单详情
					Map<String, Object> new_order_detail = new LinkedHashMap<String, Object>();
					new_order_detail.put("id",       StringUtil.generateUUID());
					new_order_detail.put("orderId",  orderId);
					new_order_detail.put("itemType", AppConstant.ORDER_DETAIL_ITEM_TYPE_CLOUD_HOST);
					new_order_detail.put("itemId",   configId);
					orderDetailMapper.insertIntoOrderDetail(new_order_detail);
					
					// 添加一条云主机计费的记录
					Map<String, Object> cloudHostBillDetailData = new LinkedHashMap<String, Object>();
					cloudHostBillDetailData.put("id",               StringUtil.generateUUID());                         
					cloudHostBillDetailData.put("host_id",          cloudHostId);                         
					cloudHostBillDetailData.put("type",             AppConstant.CLOUD_HOST_BILL_DETAIL_TYPE_4_PAY_FOR_TIME);                         
					cloudHostBillDetailData.put("cpuCore",          cpuCore);                         
					cloudHostBillDetailData.put("cpuUsed",          cpuCore);                         
					cloudHostBillDetailData.put("memory",           CapacityUtil.toMBValue(memory, 2));                         
					cloudHostBillDetailData.put("memoryUsed",       CapacityUtil.toMBValue(memory, 2));                         
					cloudHostBillDetailData.put("sysImageId",       sysImageId);                         
					cloudHostBillDetailData.put("sysDisk",          CapacityUtil.toGBValue(sysDisk, 2));                         
					cloudHostBillDetailData.put("sysDiskUsed",      CapacityUtil.toGBValue(sysDisk, 2));                         
					cloudHostBillDetailData.put("dataDisk",         CapacityUtil.toGBValue(dataDisk, 2));                         
					cloudHostBillDetailData.put("dataDiskUsed",     CapacityUtil.toGBValue(dataDisk, 2));                         
					cloudHostBillDetailData.put("diskRead",         0);                         
					cloudHostBillDetailData.put("diskWrite",        0);                         
					cloudHostBillDetailData.put("bandwidth",        FlowUtil.toMbpsValue(bandwidth, 2));                         
					cloudHostBillDetailData.put("networkTraffic",   0);                         
					cloudHostBillDetailData.put("startTime",        now);                         
					cloudHostBillDetailData.put("endTime",          null);                         
					cloudHostBillDetailData.put("fee",              null);                         
					cloudHostBillDetailData.put("isPaid",           AppConstant.CLOUD_HOST_BILL_DETAIL_IS_PAID_NOT);                         
					cloudHostBillDetailData.put("createTime",       now);                         
					cloudHostBillDetailMapper.addCloudHostBillDetail(cloudHostBillDetailData); 
					
					NetworkService service = CoreSpringContextManager.getNetworkService();

					NetworkInfoExt attachHostResult = service.attachHostSync(vpc.getRegion(), vpc.getRealVpcId(), host.getRealHostId());
//					boolean attachHostResult = true;
					if(attachHostResult != null ){
						//增加主机和VPC关联
						Map<String, Object> data = new LinkedHashMap<String, Object>();
						data.put("id", StringUtil.generateUUID());
						data.put("vpcId", vpc.getId());
						data.put("hostId", cloudHostId);
						data.put("createTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
						vpcBindHostMapper.add(data); 
						//更新vpcip 
						Map<String, Object> newIP = new LinkedHashMap<String, Object>();
						newIP.put("vpcIp", attachHostResult.getHostNetworkAddress());
						newIP.put("id", host.getId());
						cloudHostMapper.updateVpcIpById(newIP);
						
 						defaultBindPort(vpc.getId(), host);
						}else{
							throw new AppException("创建失败");
						}
					// 启动云主机
					CloudHostService cloudHostService = CoreSpringContextManager.getCloudHostService();
					cloudHostService.startCloudHost(cloudHostId); 
					
					
					
					//分配完成，立即创建一个补位 
					WarehouseCloudHostCreationListener.instance.addWarehouseIdNeedToBeImmediatelyCreated(vo.getWarehouseId());
					
					 
				}else{

					// 从仓库获取云主机失败，正常创建
					
					cloudHostId    = StringUtil.generateUUID();
					processStatus  = AppConstant.CLOUD_HOST_SHOPPING_CONFIG_PROCESS_STATUS_NOT_PROCESSED;
				    configId = StringUtil.generateUUID();
				    orderId = StringUtil.generateUUID();
						
						
					// 新增订单
					Map<String, Object> newOrder = new LinkedHashMap<String, Object>();
					newOrder.put("id",             orderId);
					newOrder.put("userId",         vpc.getUserId());
					newOrder.put("createTime",     now);
					newOrder.put("totalPrice",     price);
					newOrder.put("isPaid",         AppConstant.ORDER_INFO_IS_PAID_YES);						
					newOrder.put("processStatus",  AppConstant.ORDER_INFO_PROCESS_STATUS_NOT_PROCESSED);//设置订单为未处理
					newOrder.put("processMessage", "");
					orderInsertResult = orderInfoMapper.addOrderInfo(newOrder);
					
					// 新增配置
					Map<String, Object> newConfig = new LinkedHashMap<String, Object>();
					newConfig.put("id",             configId);
					newConfig.put("hostId",         cloudHostId);
					newConfig.put("userId",         vpc.getUserId());
					newConfig.put("type",           AppConstant.CLOUD_HOST_SHOPPING_CONFIG_TYPE_TRAIL);
					newConfig.put("cpuCore",        cpuCore);
					newConfig.put("memory",         memory);
					newConfig.put("sysImageId",     sysImageId);
					newConfig.put("sysDisk",        sysDisk);
					newConfig.put("dataDisk",       dataDisk);
					newConfig.put("bandwidth",      bandwidth);
					newConfig.put("price",          price);
					newConfig.put("createTime",     now);
					newConfig.put("duration",       "0");
					newConfig.put("processStatus",  processStatus);
					newConfig.put("processMessage", "");
					newConfig.put("hostName",       hostName);
					newConfig.put("region",       region);
					shoppingConfigInsertResult = cloudHostShoppingConfigMapper.addCloudHostShoppingConfig(newConfig);
					
					 
					
					// 新增订单详情
					Map<String, Object> new_order_detail = new LinkedHashMap<String, Object>();
					new_order_detail.put("id",       StringUtil.generateUUID());
					new_order_detail.put("orderId",  orderId);
					new_order_detail.put("itemType", AppConstant.ORDER_DETAIL_ITEM_TYPE_CLOUD_HOST);
					new_order_detail.put("itemId",   configId);
					orderDetailInsertResult = orderDetailMapper.insertIntoOrderDetail(new_order_detail);
					
					 SysDiskImageVO sysDiskIamge= sysDiskImageMapper.getById(sysImageId);
					 String sysImageName = (sysDiskIamge == null ? null : sysDiskIamge.getName());
					// 新增云主机信息，创建不需要添加计费记录，到云主机创建成功之后再去创建
					Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
					cloudHostData.put("id",              cloudHostId);                                                              
					cloudHostData.put("realHostId",      null);                                                                     
					cloudHostData.put("type",            type);                            
					cloudHostData.put("userId",          vpc.getUserId());                                                                   
					cloudHostData.put("hostName",        hostName);                                                                     
					cloudHostData.put("displayName",        hostName);                                                                     
					cloudHostData.put("account",         loginInfo.getAccount());                                                   
					cloudHostData.put("password",        RandomPassword.getRandomPwd(16));                                                   
					cloudHostData.put("cpuCore",         cpuCore);                                                                  
					cloudHostData.put("memory",          memory);                                                                   
					cloudHostData.put("sysImageId",      sysImageId);                                                               
					cloudHostData.put("sysImageName",    sysImageName);                                                               
					cloudHostData.put("sysDisk",         sysDisk);                                                                  
					cloudHostData.put("dataDisk",        dataDisk);                                                                 
					cloudHostData.put("bandwidth",       bandwidth);                                                                
					cloudHostData.put("isAutoStartup",   AppConstant.CLOUD_HOST_IS_AUTO_STARTUP_NO);                                
					cloudHostData.put("runningStatus",   AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN);                           
					cloudHostData.put("status",          AppConstant.CLOUD_HOST_STATUS_1_NORNAL);                                     
					cloudHostData.put("innerIp",         null);                                                                     
					cloudHostData.put("innerPort",       null);                                                                     
					cloudHostData.put("outerIp",         null);                                                                     
					cloudHostData.put("outerPort",       null);  
					cloudHostData.put("region",       region);
					cloudHostData.put("package_id",       item); 	
				    cloudHostData.put("monthlyPrice", price); 
					cloudHostMapper.addCloudHost(cloudHostData);
					
					//新增主机和VPC的关系
					Map<String, Object> data = new LinkedHashMap<String, Object>();
					data.put("id", StringUtil.generateUUID());
					data.put("vpcId", vpcId);
					data.put("hostId", cloudHostId);
					data.put("createTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
					vpcBindHostMapper.add(data);
				}
				
			}
			
			//更新主机个数
			Map<String, Object> data = new LinkedHashMap<String, Object>();
			data.put("id", vpcId);
			data.put("amount", hostAmount);
			vpcBaseInfoMapper.updateVpcHostAmount(data);
			logStatus = AppConstant.OPER_LOG_SUCCESS;
			return new MethodResult(MethodResult.SUCCESS, "新增成功"); 
			
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("添加失败");
		}finally {
			if(!StringUtil.isBlank(_userName)){
				try {
					OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
					Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
					operLogData.put("id", StringUtil.generateUUID());
					operLogData.put("userId", loginInfo.getUserId());
					operLogData.put("content", resourceName);
					operLogData.put("operTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
					operLogData.put("status", logStatus);
					operLogData.put("resourceName", "终端用户名:" + _userName);
					operLogData.put("operDuration", System.currentTimeMillis() - begin);
					operLogMapper.addOperLog(operLogData);
				} catch (Exception e) {
					logger.error(e);
				}
			}

		} 
	}

	/**
	 * 获取创建信息
	 * @see com.zhicloud.op.service.VpcService#getCreateInfo(java.lang.String)
	 */
	@Callable
	public MethodResult getCreateInfo(String userId) {
		try {
			
			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class);
			TerminalUserVO user = terminalUserMapper.getBalanceById(userId);  
			MethodResult result = new MethodResult(MethodResult.SUCCESS, "新增成功"); 
			result.put("balance", user.getAccountBalance());

			result.put("name", generateNewVpcName(userId));
			return result;
		} catch (AppException e) {
			logger.error(e);
			throw new AppException("失败");
		} catch (Exception e) {
			logger.error(e);
			throw new AppException("失败");
		}
	}
	
	private String generateNewVpcName(String userId){
		SysUserMapper sysUserMapper = this.sqlSession.getMapper(SysUserMapper.class);
 
		// 获取用户信息
		SysUserVO sysUser = sysUserMapper.getById(userId);
		VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class);
		Map<String, Object> data = new LinkedHashMap<String, Object>();
		data.put("userId",userId);
		List<VpcBaseInfoVO> vpcList = vpcBaseInfoMapper.queryUserVpcsByUserId(data); 
		
		Set<String> vpcNames = new HashSet<String>();
		for (VpcBaseInfoVO vpcVo : vpcList) {
			if (vpcVo.getDisplayName()!= null) {
				vpcNames.add(StringUtil.trim(vpcVo.getDisplayName()));
			}
		}

		// 获取新的云主机名
		int len = vpcNames.size() + 1; // 在size + 1个中，总有一个合适的名字
 		for (int i = 1; i <= len; i++) {
			String vpcName = "VPC_" + sysUser.getAccount() + "_" + i;
			if (vpcNames.contains(vpcName) == false) {
				return vpcName;
			}
		}
		return "VPC_default";
		
	}
	
	@Callable
	public String getAllVpcPage(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("VpcServiceImpl.getAllVpcPage()");
 
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if (loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_OPERATOR && loginInfo.hasPrivilege(PrivilegeConstant.vpc_manage_page) == false) {
			return "/public/have_not_access.jsp";
		} else if (loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT && loginInfo.hasPrivilege(PrivilegeConstant.vpc_manage_page_agent) == false) {
			return "/public/have_not_access.jsp";
		}
		if (loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT) {
			return "/security/agent/vpc_manage.jsp";
		}
		String userId = request.getParameter("user_id");
		MarkMapper markMapper = this.sqlSession.getMapper(MarkMapper.class);
		List<MarkVO> markList = markMapper.getAll();
		request.setAttribute("markList",markList);
		request.setAttribute("userId",userId);
		return "/security/operator/vpc_manage.jsp";
	}
	
	@Callable
	public void queryVpcHost(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("VpcServiceImpl.queryVpcHost()");
		try {
			// 权限判断
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			if (loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_OPERATOR && loginInfo.hasPrivilege(PrivilegeConstant.vpc_manage_page) == false) {
				throw new AppException("您没有权限进行些操作");
			} else if (loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT && loginInfo.hasPrivilege(PrivilegeConstant.vpc_manage_page_agent) == false) {
				throw new AppException("您没有权限进行些操作");
			}

			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");

			// 获取参数
			String queryMark = StringUtil.trim(request.getParameter("query_mark"));
			String queryStatus = StringUtil.trim(request.getParameter("query_status"));
			String vpcName = StringUtil.trim(request.getParameter("vpc_name"));
			String region = StringUtil.trim(request.getParameter("region"));
			String userId = StringUtil.trim(request.getParameter("user_id"));
			Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
			Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 10);
			if ("0".equals(queryStatus)) {
				queryStatus = null;
			}
			if("all".equals(queryMark)){
				queryMark = null;
			}
			if(StringUtil.isBlank(region)){
				region = null;
			}
			if(userId!=null && "null".equals(userId)){
            	userId = null;
            }
//			Calendar calendar = Calendar.getInstance();
//			calendar.setTime(new Date());
//			calendar.add(Calendar.DAY_OF_MONTH, -7);
//			String dayTime = DateUtil.dateToString(calendar.getTime(), "yyyyMMddHHmmssSSS");
			// 查询数据库
			VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class);
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("vpcName", "%" + vpcName + "%");
			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			condition.put("queryStatus", queryStatus);
			condition.put("region", region);
			condition.put("markId", queryMark);
			condition.put("userId", userId);
			int total = 0;
			List<VpcBaseInfoVO> vpcList = null;
			if (loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT) {
//				total = cloudHostMapper.queryPageCountForAgent(condition); // 总行数
//				cloudHostList = cloudHostMapper.queryPageForAgent(condition);// 分页结果
			} else {
//				condition.put("queryStatus", queryStatus);
//				condition.put("time", dayTime);
				total = vpcBaseInfoMapper.getCount(condition); // 总行数
				vpcList = vpcBaseInfoMapper.getAllVpc(condition);// 分页结果
			}

			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, vpcList);
		} catch (Exception e) {
			logger.error(e);
			throw new AppException("失败");
		}
	}
	
	@Callable
	@Transactional(readOnly=false,noRollbackFor={Exception.class})
	public MethodResult deleteVpcByIds(List<String> ids) {
		Long begin = System.currentTimeMillis();
		Integer logStatus = AppConstant.OPER_LOG_FAIL;
		HttpServletRequest request = RequestContext.getHttpRequest();
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		try{
			VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class);
			VpcBindHostMapper vpcBindHostMapper = this.sqlSession.getMapper(VpcBindHostMapper.class);
			CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
			if(ids == null || ids.size()<1){
				return new MethodResult(MethodResult.FAIL,"请选择需要删除的VPC");
			}
			//获取所有hostId
			List<String> hostIds = new ArrayList<String>();
			//后台删除云主机
			for(String id : ids){
				Map<String,Object> cloudHostData = new HashMap<String,Object>();
				cloudHostData.put("vpcId", id);
				VpcBaseInfoVO vpc = vpcBaseInfoMapper.queryVpcById(id);
				if(vpc.getStatus() == 1){					
					new VpcBillHelper(sqlSession).settleAllUndoneVpcBills(vpc.getId(), new Date(), true);
				}
				//根据vpcid查询所有的云主机
				List<CloudHostVO> cloudHostList = cloudHostMapper.getAllHostByVpcId(cloudHostData);
				//结算未停用的云主机
				for(CloudHostVO cloudHost : cloudHostList){ 
					// 从http gateway删除
					if(!StringUtil.isBlank(cloudHost.getRealHostId())){
						HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(cloudHost.getRegion());
						JSONObject hostDeleteResult = channel.hostDelete(cloudHost.getRealHostId());
						if (HttpGatewayResponseHelper.isSuccess(hostDeleteResult)) {
							logger.info("CloudHostServiceImpl.deleteCloudHostById() > [" + Thread.currentThread().getId() + "] delete host '" + cloudHost.getHostName() + "' succeeded, uuid:[" + cloudHost.getRealHostId() + "], message:[" + HttpGatewayResponseHelper.getMessage(hostDeleteResult) + "]");
							hostIds.add(cloudHost.getId());
						} else {
							logger.warn("CloudHostServiceImpl.deleteCloudHostById() > [" + Thread.currentThread().getId() + "] delete host '" + cloudHost.getHostName() + "' failed, uuid:[" + cloudHost.getRealHostId() + "], message:[" + HttpGatewayResponseHelper.getMessage(hostDeleteResult) + "]");
						}
						
						// 从缓冲池删除
						CloudHostPoolManager.getCloudHostPool().removeByRealHostId(cloudHost.getRealHostId());
						CloudHostPoolManager.getCloudHostPool().removeByHostName(cloudHost.getRegion(), cloudHost.getHostName());
					}else{
						hostIds.add(cloudHost.getId());
					}
				}
			}
			//逻辑删除vpc主机
			int n = cloudHostMapper.updateForDeleteByIds(hostIds.toArray(new String[0]));
			//后台同步删除VPC
			NetworkService service = CoreSpringContextManager.getNetworkService();
			for(String id : ids){
				VpcBaseInfoVO vpc = vpcBaseInfoMapper.queryVpcById(id);
				if(vpc!=null && vpc.getRealVpcId()!=null){
					Integer region = vpc.getRegion();
					String realId = vpc.getRealVpcId();
					boolean result = service.deleteSync(region, realId);
					if(!result){
						ids.remove(id);
					}
				}
			}
			// 逻辑删除vpc与主机的关联
			Map<String,Object> condition = new HashMap<String,Object>();
			condition.put("hostIds",hostIds);
			condition.put("removeTime",DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
			int m = vpcBindHostMapper.deleteLogical(condition);
			vpcBaseInfoMapper.logicDeleteVpcByIds(ids);
			if(m>0){
				logStatus = AppConstant.OPER_LOG_SUCCESS;
				return new MethodResult(MethodResult.SUCCESS,"删除成功");
			}
			return new MethodResult(MethodResult.FAIL,"删除失败");
		}catch(Exception e){ 
				throw new AppException("删除失败"); 
		}finally {
			try {
				OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
				Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
				operLogData.put("id", StringUtil.generateUUID());
				operLogData.put("userId", loginInfo.getUserId());
				operLogData.put("content", loginInfo.getAccount()+"删除了"+ids.size()+"个VPC");
				operLogData.put("operTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
				operLogData.put("status", logStatus);
				operLogData.put("resourceName", "终端用户名:" + loginInfo.getAccount());
				operLogData.put("operDuration", System.currentTimeMillis() - begin);
				operLogMapper.addOperLog(operLogData);
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}

	@Callable
	public String vpcViewDetailPage(HttpServletRequest request,HttpServletResponse response) {
		String vpcId = StringUtil.trim(request.getParameter("vpcId"));
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request, AppConstant.SYS_USER_TYPE_TERMINAL_USER);
		VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class); 
		VpcBaseInfoVO vbi = vpcBaseInfoMapper.queryVpcById(vpcId);
		if (vbi == null) {
			request.setAttribute("message", "找不到vpc信息");
			return "/public/warning_dialog.jsp";
		}
		request.setAttribute("vpc", vbi);
 		return "/security/operator/vpc_detail.jsp";
	}

	@Callable
	public String hostListForOperator(HttpServletRequest request,HttpServletResponse response) {
		String vpcId = request.getParameter("vpcId");
		VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class); 
        VpcBaseInfoVO vbi = vpcBaseInfoMapper.queryVpcById(vpcId);
        if (vbi == null) {
            request.setAttribute("message", "找不到vpc信息");
            return "/public/warning_dialog.jsp";
        }
		request.setAttribute("vpcId",vpcId);
		request.setAttribute("vpc", vbi);
		return "/security/operator/vpc_host_list.jsp";
	}

	@Callable
	public void getAllHostForVpcId(HttpServletRequest request,HttpServletResponse response) {
		try {
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");

			// 获取参数
			String vpcId = StringUtil.trim(request.getParameter("id"));
			Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
			Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 10);
			// 查询数据库
			CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("vpcId", vpcId);
			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			int total = 0;
			List<CloudHostVO> cloudHostList = null;
			total = cloudHostMapper.getCountByVpcId(condition); // 总行数
			cloudHostList = cloudHostMapper.getAllHostByVpcId(condition);// 分页结果
			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, cloudHostList);
		} catch (Exception e) {
			logger.error(e);
			throw new AppException("失败");
		}
		
	}

	@Callable
	public void queryAllIpForVpcId(HttpServletRequest request,HttpServletResponse response) {
		try {
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");

			// 获取参数
			String vpcId = StringUtil.trim(request.getParameter("id"));
			Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
			Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 10);
			// 查询数据库
			VpcOuterIpMapper vpcOuterIpMapper = this.sqlSession.getMapper(VpcOuterIpMapper.class);
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("vpcId", vpcId);
			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			int total = vpcOuterIpMapper.getCountByVpcId(condition); // 总行数
			List<VpcOuterIpVO> outerIpList = vpcOuterIpMapper.getAllIpByVpcId(condition);// 分页结果
			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, outerIpList);
		} catch (Exception e) {
			logger.error(e);
			throw new AppException("失败");
		}
	}

	@Callable
	public void getAllBindPort(HttpServletRequest request,HttpServletResponse response) {
		try {
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");

			// 获取参数
			String vpcId = StringUtil.trim(request.getParameter("id"));
			Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
			Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 10);
			// 查询数据库
			VpcBindPortMapper vpcBindPortMapper = this.sqlSession.getMapper(VpcBindPortMapper.class);
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("vpcId", vpcId);
			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			int total = vpcBindPortMapper.queryCountByVpcId(condition); // 总行数
			List<VpcBindPortVO> outerBindPortList = vpcBindPortMapper.queryByVpcId(condition);// 分页结果
			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, outerBindPortList);
		} catch (Exception e) {
			logger.error(e);
			throw new AppException("失败");
		}
		
	} 
	
	   /**
  * @Description:创建vpc以后随机绑定默认端口
  * @param vpcid
  * @param wwip 外网IP
  */
 public void defaultBindPort(String vpcid, CloudHostVO cloudHost) {
     Map<String, Object> condition = new HashMap<String, Object>();
     condition.put("vpcId", vpcid); 
     try {
         // 根据vpcid取得已经分配的外网端口和主机端口
         VpcBindPortMapper vpcBindPortMapper = this.sqlSession.getMapper(VpcBindPortMapper.class);
         CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
         VpcOuterIpMapper vpcOuterIpMapper = this.sqlSession.getMapper(VpcOuterIpMapper.class);
         
         VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class);
         VpcBaseInfoVO vpc = vpcBaseInfoMapper.queryVpcById(vpcid);
         List<VpcBindPortVO> portList = vpcBindPortMapper.queryByVpcId(condition);
         // 外网端口集合
         Set<Integer> outerPorts = new HashSet<Integer>();
         // 主机端口集合
         Set<Integer> hostports = new HashSet<Integer>();
         for (VpcBindPortVO vpcBindPort : portList) {
             outerPorts.add(vpcBindPort.getOuterPort());
             hostports.add(vpcBindPort.getHostPort());
         } 
         Integer outerPort_spice = getVpcPort(outerPorts);
         outerPorts.add(outerPort_spice);
         // 外网IP从主机信息里面取第一个值
         String  wwip = vpcOuterIpMapper.getAllIpByVpcId(condition).get(0).getIp();
         // 绑定
          
         NetworkService service = CoreSpringContextManager.getNetworkService(); 
         String[] protocolList = new String[] {"0"};
         String[] ipList = new String[] { wwip };
         String[] portListNew = new String[] { outerPort_spice+""};
         String[] hostList = new String[] { cloudHost.getRealHostId()};
         String[] hostPortList = new String[] { cloudHost.getInnerPort()+""};

         NetworkInfoExt network = service.bindPortSync(vpc.getRegion(), vpc.getRealVpcId(), protocolList, ipList, portListNew, hostList, hostPortList);
         if(network == null){
             return ;              
         } 
         
         Map<String,Object> data = new HashMap<String, Object>();
         data.put("id",StringUtil.generateUUID());
         data.put("vpc_id",vpcid);
         data.put("host_id",cloudHost.getId());
         data.put("outer_ip",wwip);
         data.put("outer_port",outerPort_spice);
         data.put("host_port",cloudHost.getInnerPort());
         data.put("protocol",0);
         data.put("flag", 1);
         data.put("createTime",DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
         int result = vpcBindPortMapper.add(data);
         if(result <= 0){
             return;
         }
         
         // 绑定成功以后 将ip和端口更新到云主机
         Map<String, Object> newcondition = new HashMap<String, Object>();
         newcondition.put("id", cloudHost.getId());
         newcondition.put("realHostId", cloudHost.getRealHostId());
         newcondition.put("outerIp", wwip);
         newcondition.put("outerPort", outerPort_spice);
         cloudHostMapper.updateRealHostIdById(newcondition); 
         
         //添加3389端口
         if(StringUtil.isBlank(cloudHost.getSysImageName())){
        	 SysDiskImageMapper sysDiskImageMapper = this.sqlSession.getMapper(SysDiskImageMapper.class);
        	 SysDiskImageVO image = sysDiskImageMapper.getById(cloudHost.getSysImageId());
        	 if(image != null){
        		 cloudHost.setSysImageName(image.getName());
        	 }
         }
         if(cloudHost.getSysImageName()!=null && cloudHost.getSysImageName().contains("indows")){
        	 
        	 Integer outerPort_3389 = getVpcPort(outerPorts);
        	 
        	 protocolList = new String[] {"0"};
             ipList = new String[] { wwip };
             portListNew = new String[] { outerPort_3389+""};
             hostList = new String[] { cloudHost.getRealHostId()};
             hostPortList = new String[] { "3389"};

             network = service.bindPortSync(vpc.getRegion(), vpc.getRealVpcId(), protocolList, ipList, portListNew, hostList, hostPortList);
             if(network == null){
                 return ;              
             } 
             
              data.put("id",StringUtil.generateUUID());
             data.put("vpc_id",vpcid);
             data.put("host_id",cloudHost.getId());
             data.put("outer_ip",wwip);
             data.put("outer_port",outerPort_3389);
             data.put("host_port",3389);
             data.put("protocol",0);
             data.put("flag", 0);
             data.put("createTime",DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
             vpcBindPortMapper.add(data);
        	 
         }
         //开通22
         else if(cloudHost.getSysImageName()!=null && (cloudHost.getSysImageName().contains("entos")||cloudHost.getSysImageName().contains("ubuntu"))){
        	 
             Integer outerPort_22 = getVpcPort(outerPorts);
        	 
        	 protocolList = new String[] {"0"};
             ipList = new String[] { wwip };
             portListNew = new String[] { outerPort_22+""};
             hostList = new String[] { cloudHost.getRealHostId()};
             hostPortList = new String[] { "22"};

             network = service.bindPortSync(vpc.getRegion(), vpc.getRealVpcId(), protocolList, ipList, portListNew, hostList, hostPortList);
             if(network == null){
                 return ;              
             } 
             
              data.put("id",StringUtil.generateUUID());
             data.put("vpc_id",vpcid);
             data.put("host_id",cloudHost.getId());
             data.put("outer_ip",wwip);
             data.put("outer_port",outerPort_22);
             data.put("host_port",22);
             data.put("protocol",0);
             data.put("flag", 0);
             data.put("createTime",DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
             vpcBindPortMapper.add(data);
        	 
         }
           
     } catch (Exception e) {
         logger.error(e);
         throw new AppException(e);
     } 
 }
 
 /**
  * @Description:获取外网IP可用端口，随机取一个(1-65535)
  * @param ports 已使用的端口号
  * @return 可用端口号
  */
 public Integer getVpcPort(Set<Integer> ports) {
     Random random = new Random();
     Integer re = random.nextInt(65535);
     if (re != 0 && (ports.size() == 0 || !ports.contains(re))) {
         return re;
     } else {
         getVpcPort(ports);
     }
     return 0;
 }
 
 /**
  * @Description:删除VPC的IP时重新绑定端口
  * @param vpcid
  */
 public void updateVpcPort(String vpcid) {
     Map<String, Object> condition = new HashMap<String, Object>();
     condition.put("vpcId", vpcid);

     try {
         VpcOuterIpMapper vpcOuterIpMapper = this.sqlSession.getMapper(VpcOuterIpMapper.class);
         List<VpcOuterIpVO> vpcOuterIpList = vpcOuterIpMapper.getAllIpByVpcId(condition);
         // 剩余ip集合
         Set<String> outerips = new HashSet<String>();
         for (VpcOuterIpVO vpcOuterIpVO : vpcOuterIpList) {
             outerips.add(vpcOuterIpVO.getIp());
         }
         VpcBindPortMapper vpcBindPortMapper = this.sqlSession.getMapper(VpcBindPortMapper.class);
         List<VpcBindPortVO> portList = vpcBindPortMapper.queryByVpcId(condition);
         Set<String> portips = new HashSet<String>();
         // 已绑定ip集合
         for (VpcBindPortVO vpcBindPortVO : portList) {
             portips.add(vpcBindPortVO.getOuterIp());
         }
         Integer temp = 0;
         if (outerips.size() > 0) {
             // 判断剩余IP是否存在已经被绑定的端口的记录
             for (String outerip : outerips) {
                 if (portips.contains(outerip)) {
                     temp++;
                 }
             }
         }
         // 如果不存在，则重新绑定
         if (temp == 0) {
//             defaultBindPort(vpcid, outerips.iterator().next());
         }
     } catch (Exception e) {
         logger.error(e);
         throw new AppException(e);
     }
 }

 /**
  * @Description:默认绑定spice端口方法，为了不影响页面其他数据，重新写了一遍
  * @param vpcId
  * @param outerIp
  * @param outerPort
  * @param hostId
  * @param port
  * @param protocol
  * @return
  * @throws
  */
 public MethodResult defaultBindPortItem(String vpcId, String outerIp,String outerPort, String hostId, String port, String protocol) {
     Long begin = System.currentTimeMillis();
     Integer logStatus = AppConstant.OPER_LOG_FAIL;
     HttpServletRequest request = RequestContext.getHttpRequest();
     LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
     String vpcName = "";
     try{
         if(StringUtil.isBlank(vpcId)){
             return new MethodResult(MethodResult.FAIL,"vpcId不能为空");
         }
         if(StringUtil.isBlank(outerIp)){
             return new MethodResult(MethodResult.FAIL,"外网IP不能为空");
         }
         if(StringUtil.isBlank(outerPort)){
             return new MethodResult(MethodResult.FAIL,"外网端口不能为空");
         }
         if(StringUtil.isBlank(hostId)){
             return new MethodResult(MethodResult.FAIL,"主机ID不能为空");
         }
         if(StringUtil.isBlank(port)){
             return new MethodResult(MethodResult.FAIL,"主机端口不能为空");
         }
         if(StringUtil.isBlank(protocol)){
             return new MethodResult(MethodResult.FAIL,"端口类型不能为空");
         }
         VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class);
         VpcBindPortMapper vpcBindPortMapper = this.sqlSession.getMapper(VpcBindPortMapper.class);
         CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class);
         CloudHostVO host = cloudHostMapper.getById(hostId);
         VpcBaseInfoVO vpc = vpcBaseInfoMapper.queryVpcById(vpcId);
         vpcName = vpc.getDisplayName();
         NetworkService service = CoreSpringContextManager.getNetworkService(); 
         String[] protocolList = new String[] {protocol};
         String[] ipList = new String[] { outerIp };
         String[] portList = new String[] { outerPort };
         String[] hostList = new String[] { host.getRealHostId() };
         String[] hostPortList = new String[] { port };

         NetworkInfoExt network = service.bindPortSync(vpc.getRegion(), vpc.getRealVpcId(), protocolList, ipList, portList, hostList, hostPortList);
         if(network == null){
             return new MethodResult(MethodResult.FAIL,"新增失败");              
         }
         
         Map<String,Object> checkMapOne = new HashMap<String, Object>();
         checkMapOne.put("outerIp", outerIp);
         checkMapOne.put("outerPort", outerPort);
         checkMapOne.put("protocol", protocol);
         Map<String,Object> checkMapTwo = new HashMap<String, Object>();
         checkMapTwo.put("hostId", hostId);
         checkMapTwo.put("hostPort", port);
         checkMapTwo.put("protocol", protocol);
         VpcBindPortVO vpcBindPortOne = vpcBindPortMapper.checkIpAndPortAndProtocol(checkMapOne);
         VpcBindPortVO vpcBindPortTwo = vpcBindPortMapper.checkHostAndPortAndProtocol(checkMapTwo);
         if(vpcBindPortOne!=null){
             return new MethodResult(MethodResult.FAIL,"IP"+outerIp+"的"+outerPort+"端口已绑定");
         }
         if(vpcBindPortTwo!=null){
             return new MethodResult(MethodResult.FAIL,"主机"+vpcBindPortTwo.getDisplayName()+"的"+port+"端口已绑定");
         }
         Map<String,Object> condition = new HashMap<String, Object>();
         condition.put("id",StringUtil.generateUUID());
         condition.put("vpc_id",vpcId);
         condition.put("host_id",hostId);
         condition.put("outer_ip",outerIp);
         condition.put("outer_port",Integer.parseInt(outerPort));
         condition.put("host_port",Integer.parseInt(port));
         condition.put("protocol",Integer.parseInt(protocol));
         condition.put("flag", 0);
         condition.put("createTime",DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
         int result = vpcBindPortMapper.add(condition);
         if(result <= 0){
             return new MethodResult(MethodResult.FAIL,"添加失败");
         }
         logStatus = AppConstant.OPER_LOG_SUCCESS;
     }catch(Exception e){
         
     }finally {
         try {
             OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
             Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
             operLogData.put("id", StringUtil.generateUUID());
             operLogData.put("userId", loginInfo.getUserId());
             operLogData.put("content", "VPC:"+vpcName+"绑定了一个端口"+port);
             operLogData.put("operTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
             operLogData.put("status", logStatus);
             operLogData.put("resourceName", "终端用户名:" + loginInfo.getAccount());
             operLogData.put("operDuration", System.currentTimeMillis() - begin);
             operLogMapper.addOperLog(operLogData);
         } catch (Exception e) {
             logger.error(e);
         }
     }
     return new MethodResult(MethodResult.SUCCESS,"添加成功");
 } 

	/**
	 * 代理商为用户创建专属云
	 * @see com.zhicloud.op.service.VpcService#agentAddVpcBaseInfo(java.util.Map)
	 */
    @Callable
	@Transactional(readOnly = false)
	public MethodResult agentAddVpcBaseInfo(Map<String, String> parameter) {

		logger.debug("VpcServiceImpl.addVpcBaseImfo()");
		Long begin = System.currentTimeMillis();
		Integer logStatus = AppConstant.OPER_LOG_FAIL;
		HttpServletRequest request = RequestContext.getHttpRequest();
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		String userId = loginInfo.getUserId();
		String _userName = "";
		try
		{
			// 获取推荐配置
			String item           = StringUtil.trim(parameter.get("item"));  
			String ipAmount    = StringUtil.trim(parameter.get("ipAmount")); 
			String displayName    = StringUtil.trim(parameter.get("vpcname"));
			String description    = StringUtil.trim(parameter.get("description")); 
			String terminalUserId    = StringUtil.trim(parameter.get("terminalUserId")); 
			Integer region        = StringUtil.parseInteger((String)parameter.get("region"), null); 
			// 关联主机
			String hostId    = StringUtil.trim(parameter.get("hostId")); 
			// 创建主机
			String createhostinfo      = StringUtil.trim(parameter.get("createhostinfo")); 
			String id         = StringUtil.generateUUID();
           List<CloudHostVO> createhostList = new ArrayList<CloudHostVO>();
           BigDecimal price = BigDecimal.ONE;
           if(!StringUtil.isBlank(createhostinfo)&& !createhostinfo.equals("[]")){
         	String [] cloudStrs = createhostinfo.split(",");
         	for(String cloudStr : cloudStrs){
         		CloudHostVO vo = new CloudHostVO();
         		cloudStr = cloudStr.replace("\"", "");
         		cloudStr = cloudStr.replace("]", "");
         		cloudStr = cloudStr.replace("[", "");
         		String [] cloudInfo = cloudStr.split("/");
         		vo.setCpuCore(Integer.parseInt(cloudInfo[1])); 
         		vo.setMemory(CapacityUtil.fromCapacityLabel(cloudInfo[2] + "GB"));
         		vo.setDataDisk(CapacityUtil.fromCapacityLabel(cloudInfo[3] + "GB"));
         		vo.setBandwidth(FlowUtil.fromFlowLabel(cloudInfo[4]+"Mbps"));
          		vo.setSysDisk(CapacityUtil.fromCapacityLabel("10GB"));
         		vo.setSysImageId(cloudInfo[5]); 
         		if(cloudInfo.length == 7){
         			PackagePriceMapper packagePriceMapper = this.sqlSession.getMapper(PackagePriceMapper.class);
     				List<PriceVO> priceList =  packagePriceMapper.getPriceByInfoId(cloudInfo[6]);
     				PackagePriceVO packagePriceVO = packagePriceMapper.getById(cloudInfo[6]);
      				if(priceList==null){					
     					return new MethodResult(MethodResult.FAIL, "套餐选择有误"); 
     				}
       				for(PriceVO priceVo : priceList){
      					if(priceVo.getStatus() == 2){
      						vo.setMonthlyPrice(priceVo.getMonthlyPrice());  
      					}     					
      				}
         		}else{        			
         			vo.setMonthlyPrice(CloudHostPrice.getMonthlyPrice(region, 2, vo.getCpuCore(), vo.getMemory(), vo.getDataDisk(), vo.getBandwidth()) );
         		}            		for(int i=0;i<Integer.parseInt(cloudInfo[0]);i++){           			
          			createhostList.add(vo);
         			price = price.add( vo.getMonthlyPrice() );
         		} 
         	}
         	
         }
			if( StringUtil.isBlank(displayName) )
			{
				throw new AppException("VPC名称不能为空");
			}   
			
			if(region==null||region.equals("")){
				return new MethodResult(MethodResult.FAIL, "请选择地域");
				
			}
			CloudHostMapper cloudHostMapper                                     = this.sqlSession.getMapper(CloudHostMapper.class);
			AgentMapper agentMapper = this.sqlSession.getMapper(AgentMapper.class); 			
			AgentVO agentVO = agentMapper.getAgentById(userId);
			  
			if(StringUtil.isBlank(ipAmount)){
				return new MethodResult(MethodResult.FAIL, "请输入ip数量");
				
			} 
			 
			
			
			logger.info("begin to count balance for create vpc");
			//计算余额能否支持一天，不足不创建主机
			if(agentVO!=null){
				 
				BigDecimal totalPrice = new BigDecimal("0");
				//计算出每天的费用
				totalPrice = totalPrice.add(price.multiply(new BigDecimal("100").subtract(agentVO.getPercentOff()).multiply(new BigDecimal("0.01"))));
				//计算出VPC每天的费用
				MethodResult result = this.countVpcPrice(parameter);
				totalPrice = totalPrice.add(new BigDecimal(result.get("price").toString()));
				totalPrice = totalPrice.divide(new BigDecimal("31"),0,BigDecimal.ROUND_HALF_UP);
				//算出已有产品每天的费用
				totalPrice = totalPrice.add(new CountUserProductsPriceHelper(sqlSession).getAllOneDayPrice(userId));
				BigDecimal balance = agentVO.getAccountBalance().setScale(0, BigDecimal.ROUND_HALF_UP);
				if(balance.compareTo(totalPrice)<0){
					if(totalPrice.subtract(balance).compareTo(BigDecimal.TEN)<0){						
						return new MethodResult(MethodResult.FAIL, "余额不足，至少充值<a href='page.do?userType=3&bean=agentService&method=rechargePage' target='blank'>10</a>元<br/>多充多优惠，<a href='page.do?userType=3&bean=agentService&method=rechargePage' target='blank'>点击进入充值</a>");
					}else{						
						return new MethodResult(MethodResult.FAIL, "余额不足，至少充值<a href='page.do?userType=3&bean=agentService&method=rechargePage' target='blank'>"+totalPrice.subtract(balance)+"</a>元<br/>多充多优惠，<a href='page.do?userType=3&bean=agentService&method=rechargePage' target='blank'>点击进入充值</a>");
					}
					
				} 
				
			} 			
			//向平台发送创建主机命令
			// 发消息到http gateway
			logger.info("begin to create vpc");
// 	    String poolId = "415716e32bd64a908e0a341b8dcbea5f";
			String poolId = "";
			NetworkService service = CoreSpringContextManager.getNetworkService();
			AddressPool ap = AddressPoolManager.singleton().getPool(region.toString());
			List<AddressExt> addressList = ap.getAll();
			for(AddressExt address : addressList){
				if(address.getName().equals("default")){
					poolId = address.getUuid();
					break;
				}
			}			
			NetworkInfoExt result = service.createSync(region, id, 27, "test", poolId);
			
			if (result == null) {
				logger.info("fail to create network.");
				return new MethodResult(MethodResult.FAIL, "创建失败");
			} else {
				logger.info(String.format("success to create network. uuid[%s], network_address[%s]", result.getUuid(), result.getHostNetworkAddress()));
			}
			String realVpcId = result.getUuid();
//			String realVpcId = "siusdidso";
			//创建成功回填数据库
			VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class); 
			VpcBindHostMapper vpcBindHostMapper = this.sqlSession.getMapper(VpcBindHostMapper.class); 
			OrderInfoMapper orderInfoMapper     = this.sqlSession.getMapper(OrderInfoMapper.class);
			CloudHostShoppingConfigMapper cloudHostShoppingConfigMapper         = this.sqlSession.getMapper(CloudHostShoppingConfigMapper.class);
			OrderDetailMapper orderDetailMapper                                 = this.sqlSession.getMapper(OrderDetailMapper.class);
			CloudHostBillDetailMapper cloudHostBillDetailMapper                 = this.sqlSession.getMapper(CloudHostBillDetailMapper.class);
			SysDiskImageMapper sysDiskImageMapper = this.sqlSession.getMapper(SysDiskImageMapper.class); 
			VpcOuterIpMapper vpcOuterIpMapper = this.sqlSession.getMapper(VpcOuterIpMapper.class); 
			VpcBillDetailMapper vpcBillDetailMapper   = this.sqlSession.getMapper(VpcBillDetailMapper.class);


			
			
			Map<String, Object> data = new LinkedHashMap<String, Object>();
			data.put("id",       id);
			data.put("realVpcId",  realVpcId); //
			data.put("name",  id);
			data.put("displayName",  displayName);
			data.put("description",  description);
			data.put("status",  1);// 创建成功
			data.put("type",  2);// 代理商创建的专属云类型
			data.put("region",  region);
			data.put("hostAmount",  0);
			data.put("ipAmount",  0);
			data.put("userId",  terminalUserId);
			data.put("createTime",  StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
			int n = vpcBaseInfoMapper.addVpc(data);
			
			logger.info("begin to get ip for vpc");
			//申请ip 
			result = service.attachAddressSync(region, realVpcId, Integer.parseInt(ipAmount));
			
			for(int i = 0;i<Integer.parseInt(ipAmount);i++){					
				data.put("id", StringUtil.generateUUID());
				data.put("vpcId", id);
				data.put("ip", result.getIp()[i]);
				data.put("createTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
				vpcOuterIpMapper.add(data);				
			}
			//更新IP个数
			data.put("id", id);
			data.put("amount", Integer.parseInt(ipAmount));
			vpcBaseInfoMapper.updateVpcIpAmount(data); 
			logger.info("begin to create host for vpc");
			if(n > 0){
				 	
				
				//绑定现有主机
				hostId = hostId.replace("\"", "");
				hostId = hostId.replace("]", "");
				hostId = hostId.replace("[", "");
				if(!StringUtil.isBlank(hostId)){
					String [] ids = hostId.split(",");
					for(int i = 0;i<ids.length;i++){
						ids[i] = ids[i].replace("\"", "");
						ids[i] = ids[i].replace("]", "");
						ids[i] = ids[i].replace("[", "");
						CloudHostVO host = cloudHostMapper.getById(ids[i]);
						NetworkInfoExt attachHostResult = service.attachHostSync(region, realVpcId, host.getRealHostId());
//						boolean attachHostResult = true;
						if(attachHostResult != null ){
							//增加主机和VPC关联
							data.put("id", StringUtil.generateUUID());
							data.put("vpcId", id);
							data.put("hostId", ids[i]);
							data.put("createTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
							vpcBindHostMapper.add(data);
							Integer type = AppConstant.CLOUD_HOST_TYPE_5_TERMINAL_USER_VPC;
							if(host.getType() == 2){
								type = AppConstant.CLOUD_HOST_TYPE_6_Agent_VPC;
							}
							//更新主机的类型，以便后续的查询和计费使用
							data.put("id", host.getId());
							data.put("type", type);
							cloudHostMapper.updateHostTypeById(data);
							//更新vpcip 
							data.put("vpcIp", attachHostResult.getHostNetworkAddress());
							cloudHostMapper.updateVpcIpById(data);
							new CloudHostBillingHelper(sqlSession).settleAllUndoneCloudHostBills(hostId, new Date(), false);
							defaultBindPort(id, host);
						}else{
							throw new AppException("创建失败");
						}
					}
					//更新主机个数
					data.put("id", id);
					data.put("amount", ids.length);
					vpcBaseInfoMapper.updateVpcHostAmount(data);
					
				}
				String now = StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS");
				//新创建云主机
				if(createhostList.size()>0){
					for(int i = 0;i<createhostList.size();i++){
						
						int orderInsertResult = 0; 							
						int shoppingConfigInsertResult = 0; 
						int orderDetailInsertResult = 0;
						String orderId = StringUtil.generateUUID();
						CloudHostVO hostVo = createhostList.get(i);
						// 从云主机仓库获取云主机
						String hostName = getNewCloudHostNameByUserIdForVpc(terminalUserId,region.toString());
						Map<String, Object> newCloudHostData = new LinkedHashMap<String, Object>();
						List<String> host_ports = new ArrayList<String>();
						newCloudHostData.put("type",          AppConstant.CLOUD_HOST_TYPE_6_Agent_VPC);  //主机类型为用户自己的VPC主机
						newCloudHostData.put("userId",        terminalUserId);
						newCloudHostData.put("hostName",      hostName);
						newCloudHostData.put("displayName",   hostName);
						newCloudHostData.put("account",       loginInfo.getAccount());
						newCloudHostData.put("password",      RandomPassword.getRandomPwd(16));
						newCloudHostData.put("cpuCore",       hostVo.getCpuCore());
						newCloudHostData.put("memory",        hostVo.getMemory());
						newCloudHostData.put("dataDisk",      hostVo.getDataDisk());
						newCloudHostData.put("bandwidth",     hostVo.getBandwidth());
						newCloudHostData.put("isAutoStartup", AppConstant.CLOUD_HOST_IS_AUTO_STARTUP_YES);
						newCloudHostData.put("monthlyPrice",  hostVo.getMonthlyPrice());
						newCloudHostData.put("item", item);
						newCloudHostData.put("ports",         _formatToHttpGatewayFormatPorts(host_ports));
						MethodResult warehouseResult = new CloudHostWarehouseHelper(this.sqlSession).getAndAllocateWarehouseCloudHost(hostVo.getSysImageId(), region, newCloudHostData);
						
						if( MethodResult.SUCCESS.equals(warehouseResult.status) )
						{
							CloudHostWarehouseDetailVO vo = (CloudHostWarehouseDetailVO) warehouseResult.get("CloudHostWarehouseDetailVO");
							
							
							
							// 新增订单
							Map<String, Object> newOrder = new LinkedHashMap<String, Object>();
							newOrder.put("id",             orderId);
							newOrder.put("userId",         terminalUserId);
							newOrder.put("createTime",     now);
							newOrder.put("totalPrice",     price);
							newOrder.put("isPaid",         AppConstant.ORDER_INFO_IS_PAID_YES);						
							newOrder.put("processStatus",  AppConstant.ORDER_INFO_PROCESS_STATUS_SUCCESS);
							newOrder.put("processMessage", "");
							orderInsertResult = orderInfoMapper.addOrderInfo(newOrder);
							
							if (orderInsertResult <= 0)
							{
								throw new AppException("添加订单失败");
							}							
							// 从云主机仓库获取云主机 
							logger.info("PaymentServiceImpl.getCloudHost() > ["+Thread.currentThread().getId()+"] 云主机仓库获取的库存云主机成功, sysImageId:["+hostVo.getSysImageId()+"], dataDisk:["+hostVo.getDataDisk()+"], region:["+region+"]");
							String cloudHostId    = vo.getHostId();
							CloudHostVO host = cloudHostMapper.getById(cloudHostId);
							Integer processStatus  = AppConstant.CLOUD_HOST_SHOPPING_CONFIG_PROCESS_STATUS_SUCCESS;
							String  processMessage = "fetch from cloud host warehouse"; 
							// 新增配置
							String configId = StringUtil.generateUUID();
							Map<String, Object> newConfig = new LinkedHashMap<String, Object>();
							newConfig.put("id",             configId);
							newConfig.put("hostId",         cloudHostId);
							newConfig.put("userId",         terminalUserId);
							newConfig.put("type",           AppConstant.CLOUD_HOST_SHOPPING_CONFIG_TYPE_TRAIL);
							newConfig.put("cpuCore",        hostVo.getCpuCore());
							newConfig.put("memory",         hostVo.getMemory());
							newConfig.put("sysImageId",     hostVo.getSysImageId());
							newConfig.put("sysDisk",        hostVo.getSysDisk());
							newConfig.put("dataDisk",       hostVo.getDataDisk());
							newConfig.put("bandwidth",      hostVo.getBandwidth());
							newConfig.put("price",          hostVo.getMonthlyPrice());
							newConfig.put("createTime",     now);
							newConfig.put("duration",       "0");
							newConfig.put("processStatus",  processStatus);
							newConfig.put("processMessage", processMessage);
							newConfig.put("hostName",       hostName);
							newConfig.put("region",       region);
							shoppingConfigInsertResult = cloudHostShoppingConfigMapper.addCloudHostShoppingConfig(newConfig);
							// 新增配置端口  
							
							// 新增订单详情
							Map<String, Object> new_order_detail = new LinkedHashMap<String, Object>();
							new_order_detail.put("id",       StringUtil.generateUUID());
							new_order_detail.put("orderId",  orderId);
							new_order_detail.put("itemType", AppConstant.ORDER_DETAIL_ITEM_TYPE_CLOUD_HOST);
							new_order_detail.put("itemId",   configId);
							orderDetailMapper.insertIntoOrderDetail(new_order_detail);
							
//							// 添加一条云主机计费的记录
							Map<String, Object> cloudHostBillDetailData = new LinkedHashMap<String, Object>();
							cloudHostBillDetailData.put("id",               StringUtil.generateUUID());                         
							cloudHostBillDetailData.put("host_id",          cloudHostId);                         
							cloudHostBillDetailData.put("type",             AppConstant.CLOUD_HOST_BILL_DETAIL_TYPE_4_PAY_FOR_TIME);                         
							cloudHostBillDetailData.put("cpuCore",          hostVo.getCpuCore());                         
							cloudHostBillDetailData.put("cpuUsed",          0);                         
							cloudHostBillDetailData.put("memory",           CapacityUtil.toMBValue(hostVo.getMemory(), 2));                         
							cloudHostBillDetailData.put("memoryUsed",       CapacityUtil.toMBValue(hostVo.getMemory(), 2));                         
							cloudHostBillDetailData.put("sysImageId",       hostVo.getSysImageId());                         
							cloudHostBillDetailData.put("sysDisk",          CapacityUtil.toGBValue(hostVo.getSysDisk(), 2));                         
							cloudHostBillDetailData.put("sysDiskUsed",      CapacityUtil.toGBValue(hostVo.getSysDisk(), 2));                         
							cloudHostBillDetailData.put("dataDisk",         CapacityUtil.toGBValue(hostVo.getDataDisk(), 2));                         
							cloudHostBillDetailData.put("dataDiskUsed",     CapacityUtil.toGBValue(hostVo.getDataDisk(), 2));                         
							cloudHostBillDetailData.put("diskRead",         0);                         
							cloudHostBillDetailData.put("diskWrite",        0);                         
							cloudHostBillDetailData.put("bandwidth",        FlowUtil.toMbpsValue(hostVo.getBandwidth(), 2));                         
							cloudHostBillDetailData.put("networkTraffic",   0);                         
							cloudHostBillDetailData.put("startTime",        now);                         
							cloudHostBillDetailData.put("endTime",          null);                         
							cloudHostBillDetailData.put("fee",              null);                         
							cloudHostBillDetailData.put("isPaid",           AppConstant.CLOUD_HOST_BILL_DETAIL_IS_PAID_NOT);                         
							cloudHostBillDetailData.put("createTime",       now);                         
							cloudHostBillDetailMapper.addCloudHostBillDetail(cloudHostBillDetailData); 
							
							NetworkInfoExt attachHostResult = service.attachHostSync(region, realVpcId, host.getRealHostId());
//							boolean attachHostResult = true;
							if(attachHostResult != null ){
								//增加主机和VPC关联
								data.put("id", StringUtil.generateUUID());
								data.put("vpcId", id);
								data.put("hostId", cloudHostId);
								data.put("createTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
								vpcBindHostMapper.add(data); 
								//更新vpcip 
								Map<String, Object> newIP = new LinkedHashMap<String, Object>();
								newIP.put("vpcIp", attachHostResult.getHostNetworkAddress());
								newIP.put("id", host.getId());
								cloudHostMapper.updateVpcIpById(newIP);
								new CloudHostBillingHelper(sqlSession).settleAllUndoneCloudHostBills(hostId, new Date(), false);
								defaultBindPort(id, host);
	  						}else{
	  							throw new AppException("创建失败");
	  						}
							
							// 启动云主机
							CloudHostService cloudHostService = CoreSpringContextManager.getCloudHostService();
							cloudHostService.startCloudHost(cloudHostId);  
							//分配完成，立即创建一个补位 
							WarehouseCloudHostCreationListener.instance.addWarehouseIdNeedToBeImmediatelyCreated(vo.getWarehouseId()); 
							 
						}else{
							// 从仓库获取云主机失败，正常创建
							
							String cloudHostId    = StringUtil.generateUUID();
							Integer processStatus  = AppConstant.CLOUD_HOST_SHOPPING_CONFIG_PROCESS_STATUS_NOT_PROCESSED;
						    String configId = StringUtil.generateUUID();
						    
							
							
							// 新增订单
							Map<String, Object> newOrder = new LinkedHashMap<String, Object>();
							newOrder.put("id",             orderId);
							newOrder.put("userId",         terminalUserId);
							newOrder.put("createTime",     now);
							newOrder.put("totalPrice",     price);
							newOrder.put("isPaid",         AppConstant.ORDER_INFO_IS_PAID_YES);						
							newOrder.put("processStatus",  AppConstant.ORDER_INFO_PROCESS_STATUS_NOT_PROCESSED);//设置订单为未处理
							newOrder.put("processMessage", "");
							orderInsertResult = orderInfoMapper.addOrderInfo(newOrder);
							
							// 新增配置
							Map<String, Object> newConfig = new LinkedHashMap<String, Object>();
							newConfig.put("id",             configId);
							newConfig.put("hostId",         cloudHostId);
							newConfig.put("userId",         terminalUserId);
							newConfig.put("type",           AppConstant.CLOUD_HOST_SHOPPING_CONFIG_TYPE_TRAIL);
							newConfig.put("cpuCore",        hostVo.getCpuCore());
							newConfig.put("memory",         hostVo.getMemory());
							newConfig.put("sysImageId",     hostVo.getSysImageId());
							newConfig.put("sysDisk",        hostVo.getSysDisk());
							newConfig.put("dataDisk",       hostVo.getDataDisk());
							newConfig.put("bandwidth",      hostVo.getBandwidth());
							newConfig.put("price",          hostVo.getMonthlyPrice());
							newConfig.put("createTime",     now);
							newConfig.put("duration",       "0");
							newConfig.put("processStatus",  processStatus);
							newConfig.put("processMessage", "");
							newConfig.put("hostName",       hostName);
							newConfig.put("region",       region);
							shoppingConfigInsertResult = cloudHostShoppingConfigMapper.addCloudHostShoppingConfig(newConfig);
							
							 
							
							// 新增订单详情
							Map<String, Object> new_order_detail = new LinkedHashMap<String, Object>();
							new_order_detail.put("id",       StringUtil.generateUUID());
							new_order_detail.put("orderId",  orderId);
							new_order_detail.put("itemType", AppConstant.ORDER_DETAIL_ITEM_TYPE_CLOUD_HOST);
							new_order_detail.put("itemId",   configId);
							orderDetailInsertResult = orderDetailMapper.insertIntoOrderDetail(new_order_detail);
							
							 SysDiskImageVO sysDiskIamge= sysDiskImageMapper.getById(hostVo.getSysImageId());
							 String sysImageName = (sysDiskIamge == null ? null : sysDiskIamge.getName());
							// 新增云主机信息，创建不需要添加计费记录，到云主机创建成功之后再去创建
							Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
							cloudHostData.put("id",              cloudHostId);                                                              
							cloudHostData.put("realHostId",      null);                                                                     
							cloudHostData.put("type",            AppConstant.CLOUD_HOST_TYPE_6_Agent_VPC);                            
							cloudHostData.put("userId",          terminalUserId);                                                                   
							cloudHostData.put("hostName",        hostName);                                                                     
							cloudHostData.put("displayName",        hostName);                                                                     
							cloudHostData.put("account",         loginInfo.getAccount());                                                   
							cloudHostData.put("password",        RandomPassword.getRandomPwd(16));                                                   
							cloudHostData.put("cpuCore",         hostVo.getCpuCore());                                                                  
							cloudHostData.put("memory",          hostVo.getMemory());                                                                   
							cloudHostData.put("sysImageId",      hostVo.getSysImageId());                                                               
							cloudHostData.put("sysImageName",    sysImageName);                                                               
							cloudHostData.put("sysDisk",         hostVo.getSysDisk());                                                                  
							cloudHostData.put("dataDisk",        hostVo.getDataDisk());                                                                 
							cloudHostData.put("bandwidth",       hostVo.getBandwidth());                                                                
							cloudHostData.put("isAutoStartup",   AppConstant.CLOUD_HOST_IS_AUTO_STARTUP_NO);                                
							cloudHostData.put("runningStatus",   AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN);                           
							cloudHostData.put("status",          AppConstant.CLOUD_HOST_STATUS_1_NORNAL);                                     
							cloudHostData.put("innerIp",         null);                                                                     
							cloudHostData.put("innerPort",       null);                                                                     
							cloudHostData.put("outerIp",         null);                                                                     
							cloudHostData.put("outerPort",       null);  
							cloudHostData.put("region",       region);
							cloudHostData.put("package_id",       item); 	
						    cloudHostData.put("monthlyPrice", price); 
							cloudHostMapper.addCloudHost(cloudHostData);
							
							//新增主机和VPC的关系
							data.put("id", StringUtil.generateUUID());
							data.put("vpcId", id);
							data.put("hostId", cloudHostId);
							data.put("createTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
							vpcBindHostMapper.add(data);
							
						}
						
					}
					//更新主机个数
					data.put("id", id);
					data.put("amount", createhostList.size());
					vpcBaseInfoMapper.updateVpcHostAmount(data);
				}
				
				
				// 生成账单
				data.put("id", StringUtil.generateUUID());
				data.put("vpcId", id);
				data.put("createTime", now);
				data.put("startTime", now);
				vpcBillDetailMapper.addVpcBillDetail(data);			 
				logStatus = AppConstant.OPER_LOG_SUCCESS;
				return new MethodResult(MethodResult.SUCCESS, "添加成功");
			}else{
				return new MethodResult(MethodResult.FAIL, "添加失败");				
			}
		}
		catch( Exception e )
		{
			logger.error(e); 
			throw new AppException(e);
		}finally { 
				try {
					OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
					Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
					operLogData.put("id", StringUtil.generateUUID());
					operLogData.put("userId", loginInfo.getUserId());
					operLogData.put("content", "添加VPC:" + parameter.get("vpcname"));
					operLogData.put("operTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
					operLogData.put("status", logStatus);
					operLogData.put("resourceName", "终端用户名:" + _userName);
					operLogData.put("operDuration", System.currentTimeMillis() - begin);
					operLogMapper.addOperLog(operLogData);
				} catch (Exception e) {
					logger.error(e);
				} 

		} 
	}

	/**
	 * 代理商创建专属云页面
	 * @see com.zhicloud.op.service.VpcService#agentCreateVpnPage(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
    @Callable
    public String agentCreateVpnPage(HttpServletRequest request, HttpServletResponse response) {
		CpuPackageOptionMapper cpuPackageOptionMapper       = this.sqlSession.getMapper(CpuPackageOptionMapper.class);
		MemoryPackageOptionMapper memoryPackageOptionMapper = this.sqlSession.getMapper(MemoryPackageOptionMapper.class);
		DiskPackageOptionMapper diskPackageOptionMapper     = this.sqlSession.getMapper(DiskPackageOptionMapper.class);
		SysDiskImageMapper sysDiskImageMapper               = this.sqlSession.getMapper(SysDiskImageMapper.class); 
		BandwidthPackageOptionMapper bandwidthPackageOptionMapper = this.sqlSession.getMapper(BandwidthPackageOptionMapper.class);
		CloudHostMapper  cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class); 
        PackagePriceMapper packagePriceMapper = this.sqlSession.getMapper(PackagePriceMapper.class);
        CloudHostSysDefaultPortsMapper cloudHostSysDefaultPortsMapper = this.sqlSession.getMapper(CloudHostSysDefaultPortsMapper.class);
        Map<String, Object> data = new LinkedHashMap<String, Object>();
		data.put("status1",             3);
		data.put("status2",         2);
        List<PackagePriceVO> allPriceList =  packagePriceMapper.getAllPackagePrice(data);
        List<PackagePriceVO> package1 = new ArrayList<PackagePriceVO>();
        List<PackagePriceVO> package2 = new ArrayList<PackagePriceVO>();
        List<PackagePriceVO> package4 = new ArrayList<PackagePriceVO>();
        List<PackagePriceVO> cpuRegion1 = new ArrayList<PackagePriceVO>();
        List<PackagePriceVO> cpuRegion2 = new ArrayList<PackagePriceVO>();
        List<PackagePriceVO> cpuRegion4 = new ArrayList<PackagePriceVO>();
        List<PackagePriceVO> memoryRegion1 = new ArrayList<PackagePriceVO>();
        List<PackagePriceVO> memoryRegion2 = new ArrayList<PackagePriceVO>();
        List<PackagePriceVO> memoryRegion4 = new ArrayList<PackagePriceVO>();
        List<PackagePriceVO> diskRegion1 = new ArrayList<PackagePriceVO>();
        List<PackagePriceVO> diskRegion2 = new ArrayList<PackagePriceVO>();
        List<PackagePriceVO> diskRegion4 = new ArrayList<PackagePriceVO>();
        List<PackagePriceVO> bandwidthRegion1 = new ArrayList<PackagePriceVO>();
        List<PackagePriceVO> bandwidthRegion2 = new ArrayList<PackagePriceVO>();
        List<PackagePriceVO> bandwidthRegion4 = new ArrayList<PackagePriceVO>();
        for(PackagePriceVO vo:allPriceList){
        	if(vo.getType()!=null&&vo.getType()==1&&vo.getRegion()==1){
        		cpuRegion1.add(vo); 
        	}else if(vo.getType()!=null&&vo.getType()==1&&vo.getRegion()==2){
        		cpuRegion2.add(vo);        		
        	}else if(vo.getType()!=null&&vo.getType()==1&&vo.getRegion()==4){
        		cpuRegion4.add(vo);        		
        	}else if(vo.getType()!=null&&vo.getType()==2&&vo.getRegion()==1){
        		memoryRegion1.add(vo);        		
        	}else if(vo.getType()!=null&&vo.getType()==2&&vo.getRegion()==2){
        		memoryRegion2.add(vo);        		
        	}else if(vo.getType()!=null&&vo.getType()==2&&vo.getRegion()==4){
        		memoryRegion4.add(vo);        		
        	}else if(vo.getType()!=null&&vo.getType()==3&&vo.getRegion()==1){
        		diskRegion1.add(vo);        		
        	}else if(vo.getType()!=null&&vo.getType()==3&&vo.getRegion()==2){
        		diskRegion2.add(vo);        		
        	}else if(vo.getType()!=null&&vo.getType()==3&&vo.getRegion()==4){
        		diskRegion4.add(vo);        		
        	}else if(vo.getType()!=null&&vo.getType()==4&&vo.getRegion()==1){
        		bandwidthRegion1.add(vo);        		
        	}else if(vo.getType()!=null&&vo.getType()==4&&vo.getRegion()==2){
        		bandwidthRegion2.add(vo);        		
        	}else if(vo.getType()!=null&&vo.getType()==4&&vo.getRegion()==4){
        		bandwidthRegion4.add(vo);        		
        	}else if(vo.getType()!=null&&vo.getType()==5&&vo.getRegion()==1){
        		package1.add(vo);        		
        	}else if(vo.getType()!=null&&vo.getType()==5&&vo.getRegion()==2){
        		package2.add(vo);        		
        	}else if(vo.getType()!=null&&vo.getType()==5&&vo.getRegion()==4){
        		package4.add(vo);        		
        	}
        }
        request.setAttribute("cpuRegion1", cpuRegion1);
        request.setAttribute("cpuRegion2", cpuRegion2);
        request.setAttribute("cpuRegion4", cpuRegion4);
        request.setAttribute("memoryRegion1", memoryRegion1);
        request.setAttribute("memoryRegion2", memoryRegion2);
        request.setAttribute("memoryRegion4", memoryRegion4);
        request.setAttribute("diskRegion1", diskRegion1);
        request.setAttribute("diskRegion2", diskRegion2);
        request.setAttribute("diskRegion4", diskRegion4);
        request.setAttribute("bandwidthRegion1", bandwidthRegion1);
        request.setAttribute("bandwidthRegion2", bandwidthRegion2);
        request.setAttribute("bandwidthRegion4", bandwidthRegion4);
        request.setAttribute("package1", package1);
        request.setAttribute("package2", package2);
        request.setAttribute("package4", package4);
        request.setAttribute("dataDiskMin", AppProperties.getValue("dataDiskMin","")); 
        request.setAttribute("dataDiskMax", AppProperties.getValue("dataDiskMax","")); 
        request.setAttribute("bandwidthMin_1", AppProperties.getValue("bandwidthMin_1","")); 
        request.setAttribute("bandwidthMin_2", AppProperties.getValue("bandwidthMin_2","")); 
        request.setAttribute("bandwidthMin_4", AppProperties.getValue("bandwidthMin_4","")); 
        request.setAttribute("bandwidthMax_1", AppProperties.getValue("bandwidthMax_1","")); 
        request.setAttribute("bandwidthMax_2", AppProperties.getValue("bandwidthMax_2","")); 
        request.setAttribute("bandwidthMax_4", AppProperties.getValue("bandwidthMax_4","")); 
        
        List<CloudHostSysDefaultPortsVO> defaultPorts = cloudHostSysDefaultPortsMapper.getAllPorts();
        request.setAttribute("defaultPorts", defaultPorts);
		// 带宽套餐项
		BandwidthPackageOptionVO bandwidthOption = bandwidthPackageOptionMapper.getOne();;
		if( bandwidthOption==null )
		{	// 如果还没有配置带宽套餐项，则设置一下默认的
			bandwidthOption = new BandwidthPackageOptionVO(FlowUtil.fromFlowLabel("1Mbps"), FlowUtil.fromFlowLabel("100Mbps")); 
			
		}
		request.setAttribute("bandwidthOption", bandwidthOption);
		
		// 获取CPU套餐项
		List<CpuPackageOptionVO>  cpuOptions = cpuPackageOptionMapper.getAll();
		request.setAttribute("cpuOptions", cpuOptions);
		
		// 获取内存套餐项
		List<MemoryPackageOptionVO> memoryOptions = memoryPackageOptionMapper.getAll();
		request.setAttribute("memoryOptions", memoryOptions);

		// 系统磁盘镜像
		List<SysDiskImageVO> sysDiskImageOptions = sysDiskImageMapper.getAllCommonImage();
		request.setAttribute("sysDiskImageOptions", sysDiskImageOptions);
		
		// 磁盘套餐项
		DiskPackageOptionVO diskOption = diskPackageOptionMapper.getOne();
		if( diskOption==null )
		{	// 如果还没有配置磁盘套餐项，则设置一下默认的
			diskOption = new DiskPackageOptionVO(CapacityUtil.fromCapacityLabel("1GB"), CapacityUtil.fromCapacityLabel("100GB"));
		}
		request.setAttribute("diskOption", diskOption); 
 		String userId = StringUtil.trim(request.getParameter("terminalUserId"));
		request.setAttribute("userId", userId); 
		
		Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
		cloudHostData.put("userId", userId);
		List<CloudHostVO> hostList = cloudHostMapper.getCloudForVpc(cloudHostData);
		request.setAttribute("hostList", hostList);  
		request.setAttribute("vpcName", this.generateNewVpcName(userId));  
		AgentMapper agentMapper = this.sqlSession.getMapper(AgentMapper.class);
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		AgentVO agentVO = agentMapper.getAgentById(loginInfo.getUserId());
		request.setAttribute("balance", agentVO.getAccountBalance().toString());
		return "/security/agent/create_vpc.jsp"; 
	}
    
    @Callable
    public List<VpcBaseInfoVO> queryVpcHostForExport(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("VpcServiceImpl.queryVpcHostForExport()");
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json; charset=utf-8");
            // 获取参数
            String queryMark = StringUtil.trim(request.getParameter("query_mark"));
            String queryStatus = StringUtil.trim(request.getParameter("query_status"));
            String vpcName = StringUtil.trim(request.getParameter("vpc_name"));
            String region = StringUtil.trim(request.getParameter("region"));
            if ("0".equals(queryStatus)) {
                queryStatus = null;
            }
            if ("all".equals(queryMark)) {
                queryMark = null;
            }
            if (StringUtil.isBlank(region)) {
                region = null;
            }
            // 查询数据库
            VpcBaseInfoMapper vpcBaseInfoMapper = this.sqlSession.getMapper(VpcBaseInfoMapper.class);
            Map<String, Object> condition = new LinkedHashMap<String, Object>();
            condition.put("vpcName", "%" + vpcName + "%");
            condition.put("queryStatus", queryStatus);
            condition.put("region", region);
            condition.put("markId", queryMark);
            int total = vpcBaseInfoMapper.getCount(condition);
            condition.put("start_row", 0);
            condition.put("row_count", total);
            List<VpcBaseInfoVO> vpcList = vpcBaseInfoMapper.getAllVpc(condition);
            return vpcList;
        } catch (Exception e) {
            logger.error(e);
            throw new AppException("失败");
        }
    }
}
 
