package com.zhicloud.op.service.impl;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.op.app.authorization.PrivilegeConstant;
import com.zhicloud.op.app.helper.CloudHostPrice;
import com.zhicloud.op.app.helper.LoginHelper;
import com.zhicloud.op.app.helper.ServiceHelper;
import com.zhicloud.op.app.propeties.AppProperties;
import com.zhicloud.op.common.util.CapacityUtil;
import com.zhicloud.op.common.util.DateUtil;
import com.zhicloud.op.common.util.FlowUtil;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.common.util.json.JSONLibUtil;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.login.LoginInfo;
import com.zhicloud.op.mybatis.mapper.BandwidthPackageOptionMapper;
import com.zhicloud.op.mybatis.mapper.CpuPackageOptionMapper;
import com.zhicloud.op.mybatis.mapper.DiskPackageOptionMapper;
import com.zhicloud.op.mybatis.mapper.MemoryPackageOptionMapper;
import com.zhicloud.op.mybatis.mapper.PackagePriceMapper;
import com.zhicloud.op.mybatis.mapper.TrialPeriodParamMapper;
import com.zhicloud.op.mybatis.mapper.VpcPriceMapper;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.CallWithoutLogin;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.request.RequestContext;
import com.zhicloud.op.service.PackageOptionService;
import com.zhicloud.op.service.constant.AppConstant;
import com.zhicloud.op.vo.BandwidthPackageOptionVO;
import com.zhicloud.op.vo.CpuPackageOptionVO;
import com.zhicloud.op.vo.DiskPackageOptionVO;
import com.zhicloud.op.vo.MemoryPackageOptionVO;
import com.zhicloud.op.vo.PackagePriceVO;
import com.zhicloud.op.vo.PriceVO;
import com.zhicloud.op.vo.TrialPeriodParamVO;
import com.zhicloud.op.vo.VpcPriceVO;

public class PackageOptionServiceImpl extends BeanDirectCallableDefaultImpl implements PackageOptionService
{
	public static final Logger logger = Logger.getLogger(PackageOptionServiceImpl.class);

	private SqlSession sqlSession;

	public void setSqlSession(SqlSession sqlSession)
	{
		this.sqlSession = sqlSession;
	}
	
	@Callable
	public String managePage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("PackageOptionServiceImpl.managePage()");

		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if (loginInfo.hasPrivilege(PrivilegeConstant.package_item_manage_page) == false)
		{
			return "/public/have_not_access.jsp";
		}
		
		CpuPackageOptionMapper cpuPackageOptionMapper             = this.sqlSession.getMapper(CpuPackageOptionMapper.class);
		MemoryPackageOptionMapper memoryPackageOptionMapper       = this.sqlSession.getMapper(MemoryPackageOptionMapper.class);
		DiskPackageOptionMapper diskPackageOptionMapper           = this.sqlSession.getMapper(DiskPackageOptionMapper.class);
		BandwidthPackageOptionMapper bandwidthPackageOptionMapper = this.sqlSession.getMapper(BandwidthPackageOptionMapper.class);
		TrialPeriodParamMapper trialPeriodParamMapper             = this.sqlSession.getMapper(TrialPeriodParamMapper.class);
		
		//获取CPU和内存套餐的上限
		request.setAttribute("cpu_package_option_upper_limit", AppProperties.getValue("cpu_package_option_upper_limit", ""));
		request.setAttribute("memory_package_option_upper_limit", AppProperties.getValue("memory_package_option_upper_limit", ""));
		
		// 获取CPU套餐项
		List<CpuPackageOptionVO>  cpuOptions = cpuPackageOptionMapper.getAll();
		request.setAttribute("cpuOptions", cpuOptions);
		
		// 获取内存套餐项
		List<MemoryPackageOptionVO> memoryOptions = memoryPackageOptionMapper.getAll();
		request.setAttribute("memoryOptions", memoryOptions);

		// 磁盘套餐项
		DiskPackageOptionVO diskOption = diskPackageOptionMapper.getOne();
		if( diskOption==null )
		{	// 如果还没有配置磁盘套餐项，则设置一下默认的
			diskOption = new DiskPackageOptionVO(CapacityUtil.fromCapacityLabel("1GB"), CapacityUtil.fromCapacityLabel("100GB"));
		
			LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();
			
			String id = StringUtil.generateUUID();
			
			data.put("id", id);
			data.put("min", diskOption.getMin());
			data.put("max", diskOption.getMax());
			
			diskPackageOptionMapper.addDiskPackageOption(data);
			
		}
		request.setAttribute("diskOption", diskOption);
		
		// 带宽套餐项
		BandwidthPackageOptionVO bandwidthOption = bandwidthPackageOptionMapper.getOne();;
		if( bandwidthOption==null )
		{	// 如果还没有配置带宽套餐项，则设置一下默认的
			bandwidthOption = new BandwidthPackageOptionVO(FlowUtil.fromFlowLabel("1Mbps"), FlowUtil.fromFlowLabel("100Mbps"));
		
			LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();
			
			String id = StringUtil.generateUUID();
			
			data.put("id", id);
			data.put("min", bandwidthOption.getMin());
			data.put("max", bandwidthOption.getMax());
			
			bandwidthPackageOptionMapper.addBandwidthPackageOption(data);
			
		}
		request.setAttribute("bandwidthOption", bandwidthOption);		
		
		//试用期参数
		TrialPeriodParamVO trialPeriodCloudHost = trialPeriodParamMapper.getOne(AppConstant.CLOUD_HOST);
		if( trialPeriodCloudHost==null )
		{	// 如果还没有配置试用期云主机套餐项，则设置一下默认的
			trialPeriodCloudHost = new TrialPeriodParamVO(AppConstant.CLOUD_HOST, AppConstant.DEFAULT_DAY);
		
			LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();
			
			String id = StringUtil.generateUUID();
			
			data.put("id", id);
			data.put("type", trialPeriodCloudHost.getType());
			data.put("day", trialPeriodCloudHost.getDay());
			
			trialPeriodParamMapper.addTrialPeriodParam(data);
		}
		request.setAttribute("trialPeriodCloudHost", trialPeriodCloudHost);	
		
		TrialPeriodParamVO trialPeriodCloudDisk = trialPeriodParamMapper.getOne(AppConstant.CLOUD_DISK);
		if( trialPeriodCloudDisk==null )
		{	// 如果还没有配置试用期云存储餐项，则设置一下默认的
			trialPeriodCloudDisk = new TrialPeriodParamVO(AppConstant.CLOUD_DISK, AppConstant.DEFAULT_DAY);
		
			LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();
			
			String id = StringUtil.generateUUID();
			
			data.put("id", id);
			data.put("type", trialPeriodCloudDisk.getType());
			data.put("day", trialPeriodCloudDisk.getDay());
			
			trialPeriodParamMapper.addTrialPeriodParam(data);
		}
		request.setAttribute("trialPeriodCloudDisk", trialPeriodCloudDisk);	

		return "/security/operator/package_option_manage.jsp";
	}


	@Callable
	public String packagePriceManagePage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("PackageOptionServiceImpl.packagePriceManagePage()");

		 //权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if (loginInfo.hasPrivilege(PrivilegeConstant.package_price_manage_page) == false)
		{
			return "/public/have_not_access.jsp";
		}

		return "/security/operator/package_price_manage.jsp";
	}
	
	@Callable
	@Transactional(readOnly = false)
	public MethodResult addCpuPackageOption(Map<String, String> parameter)
	{
		logger.debug("PackageOptionServiceImpl.addCpuPackageOption()");
		try
		{
			// 权限判断
			HttpServletRequest request = RequestContext.getHttpRequest();
			
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			if( loginInfo.hasPrivilege(PrivilegeConstant.cpu_package_option_add) == false )
			{
				return new MethodResult(MethodResult.FAIL, "您没有权限进行此操作");
			}

			// 参数处理
			String core = StringUtil.trim(parameter.get("core"));

			if( StringUtil.isBlank(core) )
			{
				return new MethodResult(MethodResult.FAIL, "CPU核心数不能为空");
			}
			
			boolean isNum = core.matches("[0-9]+");
			
			if(!isNum) {
				return new MethodResult(MethodResult.FAIL, "CPU核心数必须为正整数");
			}
			
			CpuPackageOptionMapper cpuPackageOptionMapper = this.sqlSession.getMapper(CpuPackageOptionMapper.class);

			if( cpuPackageOptionMapper.getByCore(core) != null )
			{
				return new MethodResult(MethodResult.FAIL, "该选项已存在");
			}

			// 判断套餐项是否已经存在
			LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();

			String id = StringUtil.generateUUID();

			data.put("id", id);
			data.put("core", core);
			data.put("sort", core);

			int n = cpuPackageOptionMapper.addCpuPackageOption(data);

			if( n > 0 )
			{
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
	public MethodResult deleteCpuPackageOption(String coreId)
	{
		logger.debug("PackageOptionServiceImpl.deleteCpuPackageOption()");
		try
		{
			// 权限判断
			HttpServletRequest request = RequestContext.getHttpRequest();
			
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			if( loginInfo.hasPrivilege(PrivilegeConstant.cpu_package_option_delete) == false )
			{
				return new MethodResult(MethodResult.FAIL, "您没有权限进行此操作");
			}
			
			if( coreId == null )
			{
				return new MethodResult(MethodResult.FAIL, "coreId不能为空");
			}

			CpuPackageOptionMapper cpuPackageOptionMapper = this.sqlSession.getMapper(CpuPackageOptionMapper.class);
			int n = cpuPackageOptionMapper.deleteCpuPackageOption(coreId);

			if( n > 0 )
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
	public MethodResult addMemoryPackageOption(Map<String, String> parameter)
	{
		logger.debug("PackageOptionServiceImpl.addMemoryPackageOption()");
		try
		{
			// 权限判断
			HttpServletRequest request = RequestContext.getHttpRequest();
			
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			if( loginInfo.hasPrivilege("memory_package_option_add") == false )
			{
				return new MethodResult(MethodResult.FAIL, "您没有权限进行此操作");
			}

			// 参数处理
			String memory = StringUtil.trim(parameter.get("memory"));
			BigInteger realMemory = CapacityUtil.fromCapacityLabel(memory + "GB");

			if( StringUtil.isBlank(memory) )
			{
				return new MethodResult(MethodResult.FAIL, "内存大小不能为空");
			}
			
			boolean isNum = memory.matches("[0-9]+");
			
			if(!isNum) {
				return new MethodResult(MethodResult.FAIL, "内存大小必须为正整数");
			}

			MemoryPackageOptionMapper memoryPackageOptionMapper = this.sqlSession.getMapper(MemoryPackageOptionMapper.class);

			if( memoryPackageOptionMapper.getByMemory(realMemory) != null )
			{
				return new MethodResult(MethodResult.FAIL, "该选项已存在！");
			}

			LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();

			String id = StringUtil.generateUUID();

			data.put("id", id);
			data.put("memory", realMemory);
			data.put("label",  CapacityUtil.toCapacityLabel(realMemory, 0));
			data.put("sort",   realMemory);

			int n = memoryPackageOptionMapper.addMemoryPackageOption(data);

			if( n > 0 )
			{
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
	public MethodResult deleteMemoryPackageOption(String memoryId)
	{
		logger.debug("PackageOptionServiceImpl.deleteMemoryPackageOption()");
		try
		{
			// 权限判断
			HttpServletRequest request = RequestContext.getHttpRequest();
			
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			if( loginInfo.hasPrivilege(PrivilegeConstant.memory_package_option_delete)==false )
			{
				return new MethodResult(MethodResult.FAIL, "您没有权限进行此操作");
			}
			
			if( memoryId == null )
			{
				return new MethodResult(MethodResult.FAIL, "memoryId不能为空");
			}
			
			MemoryPackageOptionMapper memoryPackageOptionMapper = this.sqlSession.getMapper(MemoryPackageOptionMapper.class);
			int n = memoryPackageOptionMapper.deleteMemoryPackageOption(memoryId);

			if( n > 0 )
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
	public MethodResult updateDiskPackageOption(Map<String, Object> parameter)
	{
		logger.debug("PackageOptionServiceImpl.updateDiskPackageOption()");
		try
		{
			// 权限判断
			HttpServletRequest request = RequestContext.getHttpRequest();
			
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			if( loginInfo.hasPrivilege("disk_package_option_modify") == false )
			{
				return new MethodResult(MethodResult.FAIL, "您没有权限进行此操作");
			}

			// 参数处理
			String min = StringUtil.trim(parameter.get("disk_min"));
			String max = StringUtil.trim(parameter.get("disk_max"));

			if( StringUtil.isBlank(min) )
			{
				return new MethodResult(MethodResult.FAIL, "最小磁盘空间不能为空");
			}
			if( StringUtil.isBlank(max) )
			{
				return new MethodResult(MethodResult.FAIL, "最大磁盘空间不能为空");
			}
			
			boolean isNum1= min.matches("[0-9]+");
			boolean isNum2= max.matches("[0-9]+");
			
			if(!isNum1|| !isNum2) {
				return new MethodResult(MethodResult.FAIL, "磁盘空间必须为正整数");
			}
			
			int minInt = Integer.parseInt(min);
			int maxInt = Integer.parseInt(max);
			
			if(minInt>=maxInt) {
				return new MethodResult(MethodResult.FAIL, "磁盘最小值不能大于等于最大值");
			}

			DiskPackageOptionMapper diskPackageOptionMapper = this.sqlSession.getMapper(DiskPackageOptionMapper.class);

			LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();

			data.put("min", CapacityUtil.fromCapacityLabel(min + "GB"));
			data.put("max", CapacityUtil.fromCapacityLabel(max + "GB"));

			int n = diskPackageOptionMapper.updateDiskPackageOption(data);

			if( n > 0 )
			{
				return new MethodResult(MethodResult.SUCCESS, "修改成功");
			}
			else
			{
				return new MethodResult(MethodResult.FAIL, "修改失败");
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
	public MethodResult updateBandwidthPackageOption(Map<String, Object> parameter)
	{
		logger.debug("PackageOptionServiceImpl.updateBandwidthPackageOption()");
		try
		{
			// 权限判断
			HttpServletRequest request = RequestContext.getHttpRequest();
			
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			if( loginInfo.hasPrivilege("bandwidth_package_option_modify") == false )
			{
				return new MethodResult(MethodResult.FAIL, "您没有权限进行此操作");
			}

			// 参数处理
			String min = StringUtil.trim(parameter.get("bandwidth_min"));
			String max = StringUtil.trim(parameter.get("bandwidth_max"));

			if( StringUtil.isBlank(min) )
			{
				return new MethodResult(MethodResult.FAIL, "最小带宽不能为空");
			}
			if( StringUtil.isBlank(max) )
			{
				return new MethodResult(MethodResult.FAIL, "最大带宽不能为空");
			}
			
			boolean isNum1= min.matches("[0-9]+");
			boolean isNum2= max.matches("[0-9]+");
			
			if(!isNum1|| !isNum2) {
				return new MethodResult(MethodResult.FAIL, "带宽大小必须为正整数");
			}
			
			int minInt = Integer.parseInt(min);
			int maxInt = Integer.parseInt(max);
			
			if(minInt>=maxInt) {
				return new MethodResult(MethodResult.FAIL, "带宽最小值不能大于等于最大值");
			}

			BandwidthPackageOptionMapper bandwidthPackageOptionMapper = this.sqlSession.getMapper(BandwidthPackageOptionMapper.class);

			LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();

			data.put("min", FlowUtil.fromFlowLabel(min + "Mbps"));
			data.put("max", FlowUtil.fromFlowLabel(max + "Mbps"));

			int n = bandwidthPackageOptionMapper.updateBandwidthPackageOption(data);

			if( n > 0 )
			{
				return new MethodResult(MethodResult.SUCCESS, "修改成功");
			}
			else
			{
				return new MethodResult(MethodResult.FAIL, "修改失败");
			}
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("修改失败");
		}
	}

	@SuppressWarnings("unchecked")
	@Callable
	@Transactional(readOnly = false)
	public MethodResult updateTrialPeriodParam(Map<String, Object> parameter)
	{
		logger.debug("PackageOptionServiceImpl.updateTrialPeriodParam()");
		try
		{
			// 权限判断
			HttpServletRequest request = RequestContext.getHttpRequest();
			
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			if (loginInfo.hasPrivilege(PrivilegeConstant.trial_period_manage) == false)
			{
				return new MethodResult(MethodResult.FAIL, "您没有权限进行此操作");
			}

			// 参数处理
			String id1 = (String) parameter.get("id1");
			String id2 = (String) parameter.get("id2");
			List<String> checkStr1 = (List<String>) parameter.get("check1");
			List<String> checkStr2 = (List<String>) parameter.get("check2");
			int check1 =checkStr1.contains("yes") ? 1 : 0;
			int check2 =checkStr2.contains("yes") ? 1 : 0;
			
			String day1Str = (String)StringUtil.trim(parameter.get("day1"));
			String day2Str = (String)StringUtil.trim(parameter.get("day2"));
			
		
			
			if(StringUtil.isBlank(day1Str) || StringUtil.isBlank(day2Str)){
				return new MethodResult(MethodResult.FAIL, "试用期不能为空");
			}
			
			boolean isNum1 = day1Str.matches("[0-9]+");  
			boolean isNum2 = day2Str.matches("[0-9]+");  
			
			if(!isNum1 || !isNum2) {
				return new MethodResult(MethodResult.FAIL, "试用期必须为正整数");
			}
			
			int day1 = Integer.parseInt(day1Str);
			int day2 = Integer.parseInt(day2Str);
			
			if( check1 == 0 )
			{
				day1 = 0;
			}
			if( check2 == 0 )
			{
				day2 = 0;
			}
			
			TrialPeriodParamMapper trialPeriodParamMapper = this.sqlSession.getMapper(TrialPeriodParamMapper.class);
			
			LinkedHashMap<String, Object> data1 = new LinkedHashMap<String, Object>();	
			
			data1.put("id", id1);
			data1.put("type", AppConstant.CLOUD_HOST);
			data1.put("day",day1);

			int n = trialPeriodParamMapper.updateTrialPeriodParam(data1);
			
			LinkedHashMap<String, Object> data2 = new LinkedHashMap<String, Object>();	
			
			data2.put("id", id2);
			data2.put("type", AppConstant.CLOUD_DISK);
			data2.put("day",day2);

			int m= trialPeriodParamMapper.updateTrialPeriodParam(data2);
			
			if (!(n > 0 && m > 0)) {
				return new MethodResult(MethodResult.FAIL, "保存失败");
			}
			return new MethodResult(MethodResult.SUCCESS, "保存成功");
		} catch (Exception e) {
			logger.error(e);
			throw new AppException("保存失败");
		}	
	}
	
	@Callable
	public void queryPackagePrice(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("PackageOptionServiceImpl.queryPackagePrice()");
		try
		{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");
			// 获取参数
			String type    = request.getParameter("type"); 
			Integer region = StringUtil.parseInteger(request.getParameter("region"),1);
			Integer page   = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
			Integer rows   = StringUtil.parseInteger(request.getParameter("rows"), 10);
			// 查询数据库
			PackagePriceMapper packagePriceMapper = this.sqlSession.getMapper(PackagePriceMapper.class);
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("type", type);
			condition.put("region", region);
			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			int total = packagePriceMapper.queryCount(condition); // 总行数
			List<PackagePriceVO> packagePriceList = packagePriceMapper.getPackagePrice(condition);// 分页结果
			List<PackagePriceVO> curPackagePriceList = new ArrayList<PackagePriceVO>();
			if(packagePriceList!=null && packagePriceList.size()>0){
				for(PackagePriceVO pp : packagePriceList){
					List<PriceVO> priceList = packagePriceMapper.getPriceByInfoId(pp.getId());
					if(priceList!=null && priceList.size()>0){
						for(PriceVO price : priceList){
							if(price.getStatus() == 2){
								pp.setVpcPrice(price.getMonthlyPrice());
							}else if(price.getStatus() == 3){
								pp.setPrice(price.getMonthlyPrice());
							}
						}
					}
					curPackagePriceList.add(pp);
				}
			}
			List<PackagePriceVO> newPackagePriceList = new ArrayList<PackagePriceVO>();
			if("5".equals(type) && curPackagePriceList!=null && (curPackagePriceList.size()>0)){
				for(PackagePriceVO pp : packagePriceList){
					pp.setMemory(CapacityUtil.toGBValue(pp.getMemory(),0).toBigInteger());
					pp.setDataDisk(CapacityUtil.toGBValue(pp.getDataDisk(),0).toBigInteger());
					pp.setBandwidth(FlowUtil.toMbpsValue(pp.getBandwidth(),0).toBigInteger());
					newPackagePriceList.add(pp);
				}
				ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, newPackagePriceList);
				return;
			}
			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, packagePriceList);
		}
		catch( Exception e )
		{
			logger.error("AgentServiceImpl.queryAgent()",e);
			throw new AppException("查询失败");
		}
	}
	
	public List<PackagePriceVO> getPackagePrice(String type)
	{
		logger.debug("PackageOptionServiceImpl.getPackagePrice()");
		List<PackagePriceVO> packagePriceList = new ArrayList<PackagePriceVO>();
		List<PackagePriceVO> newPackagePriceList = new ArrayList<PackagePriceVO>();
		try
		{
			PackagePriceMapper packagePriceMapper = this.sqlSession.getMapper(PackagePriceMapper.class);
			packagePriceList = packagePriceMapper.queryPackagePrice(type);
			if(packagePriceList!=null && packagePriceList.size()>0){
				for(PackagePriceVO pp : packagePriceList){
					List<PriceVO> priceList = packagePriceMapper.getPriceByInfoId(pp.getId());
					if(priceList!=null && priceList.size()>0){
						for(PriceVO price : priceList){
							if(price.getStatus() == 2){
								pp.setVpcPrice(price.getMonthlyPrice());
							}else if(price.getStatus() == 3){
								pp.setPrice(price.getMonthlyPrice());
							}
						}
					}
					newPackagePriceList.add(pp);
				}
			}
		}
		catch( Exception e )
		{
			logger.error("AgentServiceImpl.queryAgent()",e);
			throw new AppException("查询失败");
		}
		return newPackagePriceList;
	}
	
	@Callable
	public String addPackagePricePage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("PackageOptionServiceImpl.addPackagePricePage()");

		// 权限判断
		String region = request.getParameter("region");
		String type = request.getParameter("type");
		request.setAttribute("region", region);
		if("1".equals(type)){
			return "/security/operator/package_price_cpu_add.jsp";
		}else if("2".equals(type)){
			return "/security/operator/package_price_memory_add.jsp";
		}else if("3".equals(type)){
			return "/security/operator/package_price_disk_add.jsp";
		}else if("4".equals(type)){
			return "/security/operator/package_price_bandwidth_add.jsp";
		}else{
			return "/security/operator/package_price_add.jsp";
		}
	}

	@Callable
	@Transactional(readOnly = false)
	public MethodResult addPackagePrice(Map<String, String> parameter)
	{
		logger.debug("PackageOptionServiceImpl.addPackagePrice()");
		try
		{
			// 权限判断
//			HttpServletRequest request = RequestContext.getHttpRequest();
			// 参数处理
			String type         = StringUtil.trim(parameter.get("type"));
			String region       = StringUtil.trim(parameter.get("region"));
			String normalPrice  = StringUtil.trim(parameter.get("price"));
			String vpcPrice     = StringUtil.trim(parameter.get("vpcPrice"));
			String core         = StringUtil.trim(parameter.get("cpuCore"));
			String memory       = StringUtil.trim(parameter.get("memory"));
			String disk         = StringUtil.trim(parameter.get("dataDisk"));
			String bandwidth    = StringUtil.trim(parameter.get("bandwidth"));
			String description  = StringUtil.trim(parameter.get("description"));
			String status       = StringUtil.trim(parameter.get("status"));
			BigInteger _memory    = null;
			BigInteger _disk      = null;
			BigInteger _bandwidth = null;
			String diskPriceOfOne = AppProperties.getValue("package_price_disk","");
			String diskPriceOfOne_2 = AppProperties.getValue("package_price_disk_2","");
			String diskPriceOfOne_4 = AppProperties.getValue("package_price_disk_4",""); 
			String diskPriceOfOne_vpc = AppProperties.getValue("package_price_disk_vpc","");
			String diskPriceOfOne_2_vpc = AppProperties.getValue("package_price_disk_2_vpc","");
			String diskPriceOfOne_4_vpc = AppProperties.getValue("package_price_disk_4_vpc","");
			if(StringUtil.isBlank(status) )
			{
				return new MethodResult(MethodResult.FAIL, "类型不能为空");
			}
			if("2".equals(status)){
				if(!StringUtil.isBlank(normalPrice)){
					normalPrice = null;
				}
			}
			if("3".equals(status)){
				if(!StringUtil.isBlank(vpcPrice)){
					vpcPrice = null;
				}
			}
			
			if(!StringUtil.isBlank(memory)){
				_memory = CapacityUtil.fromCapacityLabel(StringUtil.trim(memory + "GB"));
			}
			if(!StringUtil.isBlank(disk)){
				_disk = CapacityUtil.fromCapacityLabel(StringUtil.trim(disk + "GB"));
				//计算硬盘的价格
				if(StringUtil.isBlank(vpcPrice)){
					BigDecimal diskCount = new BigDecimal(disk);
					BigDecimal priceOfOne = null;
					if("4".equals(region)){
						priceOfOne = new BigDecimal(diskPriceOfOne_4_vpc);
					}else if("2".equals(region)){
						priceOfOne = new BigDecimal(diskPriceOfOne_2_vpc);
					}else{
						priceOfOne = new BigDecimal(diskPriceOfOne_vpc);
					}
					vpcPrice = diskCount.multiply(priceOfOne).toString();
				}
				if(StringUtil.isBlank(normalPrice)){
					BigDecimal diskCount = new BigDecimal(disk);
					BigDecimal priceOfOne = null;
					if("4".equals(region)){
						priceOfOne = new BigDecimal(diskPriceOfOne_4);
					}else if("2".equals(region)){
						priceOfOne = new BigDecimal(diskPriceOfOne_2);
					}else{
						priceOfOne = new BigDecimal(diskPriceOfOne);
					}
					normalPrice = diskCount.multiply(priceOfOne).toString();
				}
			}
			if(!StringUtil.isBlank(bandwidth)){
				_bandwidth = FlowUtil.fromFlowLabel(bandwidth + "Mbps");
				BigDecimal bandwidthCount = new BigDecimal(bandwidth);
				//计算带宽价格
				if(StringUtil.isBlank(normalPrice)){
					BigDecimal LESS_THAN_5_GZ = new BigDecimal(AppProperties.getValue("bandwidth_less_than_5", "30"));
					BigDecimal LESS_THAN_5_CD = new BigDecimal(AppProperties.getValue("bandwidth_less_than_5_2", "30"));
					BigDecimal LESS_THAN_5_HK = new BigDecimal(AppProperties.getValue("bandwidth_less_than_5_4", "60"));
					BigDecimal BASE_PRICE_5_GZ = new BigDecimal(AppProperties.getValue("bandwidth_equal_5", "150"));
					BigDecimal BASE_PRICE_5_CD = new BigDecimal(AppProperties.getValue("bandwidth_equal_5_2", "150"));
					BigDecimal BASE_PRICE_5_HK = new BigDecimal(AppProperties.getValue("bandwidth_equal_5_4", "300"));
					BigDecimal BANDWIDTH_MONTHLY_PRICE_PER_1MBPS = new BigDecimal(AppProperties.getValue("bandwidth_base", ""));
					BigDecimal BANDWIDTH_MONTHLY_PRICE_PER_1MBPS_2 = new BigDecimal(AppProperties.getValue("bandwidth_base_2", ""));
					BigDecimal BANDWIDTH_MONTHLY_PRICE_PER_1MBPS_4 = new BigDecimal(AppProperties.getValue("bandwidth_base_4", ""));
					if("4".equals(region)){
						if(bandwidthCount.compareTo(new BigDecimal("5")) <= 0){
							normalPrice = LESS_THAN_5_HK.multiply(bandwidthCount).toString();
						}else{
							normalPrice = BASE_PRICE_5_HK.add((bandwidthCount.subtract(new BigDecimal("5"))).multiply(BANDWIDTH_MONTHLY_PRICE_PER_1MBPS_4)).toString();
						}
					}else if("2".equals(region)){
						if(bandwidthCount.compareTo(new BigDecimal("5")) <= 0){
							normalPrice = LESS_THAN_5_CD.multiply(bandwidthCount).toString();
						}else{
							normalPrice = BASE_PRICE_5_CD.add((bandwidthCount.subtract(new BigDecimal("5"))).multiply(BANDWIDTH_MONTHLY_PRICE_PER_1MBPS_2)).toString();
						}
					}else{
						if(bandwidthCount.compareTo(new BigDecimal("5")) <= 0){
							normalPrice = LESS_THAN_5_GZ.multiply(bandwidthCount).toString();
						}else{
							normalPrice = BASE_PRICE_5_GZ.add((bandwidthCount.subtract(new BigDecimal("5"))).multiply(BANDWIDTH_MONTHLY_PRICE_PER_1MBPS)).toString();
						}
					}
				}
				//计算vpc带宽价格
				if(StringUtil.isBlank(vpcPrice)){
					BigDecimal LESS_THAN_5_GZ_vpc = new BigDecimal(AppProperties.getValue("bandwidth_less_than_5_vpc", "30"));
					BigDecimal LESS_THAN_5_CD_vpc = new BigDecimal(AppProperties.getValue("bandwidth_less_than_5_2_vpc", "30"));
					BigDecimal LESS_THAN_5_HK_vpc = new BigDecimal(AppProperties.getValue("bandwidth_less_than_5_4_vpc", "60"));
					BigDecimal BASE_PRICE_5_GZ_vpc = new BigDecimal(AppProperties.getValue("bandwidth_equal_5_vpc", "150"));
					BigDecimal BASE_PRICE_5_CD_vpc = new BigDecimal(AppProperties.getValue("bandwidth_equal_5_2_vpc", "150"));
					BigDecimal BASE_PRICE_5_HK_vpc = new BigDecimal(AppProperties.getValue("bandwidth_equal_5_4_vpc", "300"));
					BigDecimal BANDWIDTH_MONTHLY_PRICE_PER_1MBPS_vpc = new BigDecimal(AppProperties.getValue("bandwidth_base_vpc", ""));
					BigDecimal BANDWIDTH_MONTHLY_PRICE_PER_1MBPS_2_vpc = new BigDecimal(AppProperties.getValue("bandwidth_base_2_vpc", ""));
					BigDecimal BANDWIDTH_MONTHLY_PRICE_PER_1MBPS_4_vpc = new BigDecimal(AppProperties.getValue("bandwidth_base_4_vpc", ""));
					if("4".equals(region)){
						if(bandwidthCount.compareTo(new BigDecimal("5")) <= 0){
							vpcPrice = LESS_THAN_5_HK_vpc.multiply(bandwidthCount).toString();
						}else{
							vpcPrice = BASE_PRICE_5_HK_vpc.add((bandwidthCount.subtract(new BigDecimal("5"))).multiply(BANDWIDTH_MONTHLY_PRICE_PER_1MBPS_4_vpc)).toString();
						}
					}else if("2".equals(region)){
						if(bandwidthCount.compareTo(new BigDecimal("5")) <= 0){
							vpcPrice = LESS_THAN_5_CD_vpc.multiply(bandwidthCount).toString();
						}else{
							vpcPrice = BASE_PRICE_5_CD_vpc.add((bandwidthCount.subtract(new BigDecimal("5"))).multiply(BANDWIDTH_MONTHLY_PRICE_PER_1MBPS_2_vpc)).toString();
						}
					}else{
						if(bandwidthCount.compareTo(new BigDecimal("5")) <= 0){
							vpcPrice = LESS_THAN_5_GZ_vpc.multiply(bandwidthCount).toString();
						}else{
							vpcPrice = BASE_PRICE_5_GZ_vpc.add((bandwidthCount.subtract(new BigDecimal("5"))).multiply(BANDWIDTH_MONTHLY_PRICE_PER_1MBPS_vpc)).toString();
						}
					}
				}
			}
			if((!"2".equals(status)) && StringUtil.isBlank(normalPrice) )
			{
				return new MethodResult(MethodResult.FAIL, "正常价格不能为空");
			}
			if((!"3".equals(status)) && StringUtil.isBlank(vpcPrice) )
			{
				return new MethodResult(MethodResult.FAIL, "VPC价格不能为空");
			}
			
			if("1".equals(type) && StringUtil.isBlank(core) )
			{
				return new MethodResult(MethodResult.FAIL, "CPU核数不能为空");
			}
			
			if("2".equals(type) && StringUtil.isBlank(memory) )
			{
				return new MethodResult(MethodResult.FAIL, "内存不能为空");
			}
			
			if("3".equals(type) && StringUtil.isBlank(disk) )
			{
				return new MethodResult(MethodResult.FAIL, "硬盘不能为空");
			}
			
			if("4".equals(type) && StringUtil.isBlank(bandwidth) )
			{
				return new MethodResult(MethodResult.FAIL, "带宽不能为空");
			}
			if(!StringUtil.isBlank(core)){
				boolean isNum = core.matches("[0-9]+");
				
				if(!isNum) {
					return new MethodResult(MethodResult.FAIL, "CPU核心数必须为正整数");
				}
			}
			PackagePriceMapper packagePriceMapper = this.sqlSession.getMapper(PackagePriceMapper.class);
			// 判断套餐项是否已经存在
			LinkedHashMap<String, Object> checkData = new LinkedHashMap<String, Object>();
			checkData.put("region", region);
			checkData.put("type", type);
			checkData.put("cpuCore",StringUtil.isBlank(core)?null:core);
			checkData.put("memory", _memory);
			checkData.put("dataDisk", _disk);
			checkData.put("bandwidth", _bandwidth);
			checkData.put("status", status);
			PackagePriceVO packagePrice = packagePriceMapper.getByRegionAndTypeAndOption(checkData);
			if(packagePrice!=null){
				return new MethodResult(MethodResult.FAIL, "该项已存在");
			}
			LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();

			String id = StringUtil.generateUUID();
			data.put("id", id);
			data.put("region", region);
			data.put("type", type);
			data.put("cpuCore", StringUtil.isBlank(core)?null:core);
			data.put("memory", _memory);
			data.put("disk", _disk);
			data.put("bandwidth", _bandwidth);
			data.put("description", description);
			data.put("status", status);

			int n = packagePriceMapper.addPackagePrice(data);
			if("1".equals(status)){
				LinkedHashMap<String, Object> priceData = new LinkedHashMap<String, Object>();
				priceData.put("id",StringUtil.generateUUID());
				priceData.put("packageId",id);
				priceData.put("status",2);
				priceData.put("monthlyPrice",vpcPrice);
				priceData.put("createTime",DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
				packagePriceMapper.addPrice(priceData);
				priceData.put("id",StringUtil.generateUUID());
				priceData.put("status",3);
				priceData.put("monthlyPrice",normalPrice);
				packagePriceMapper.addPrice(priceData);
			}else if("2".equals(status)){
				LinkedHashMap<String, Object> priceData = new LinkedHashMap<String, Object>();
				priceData.put("id",StringUtil.generateUUID());
				priceData.put("packageId",id);
				priceData.put("status",2);
				priceData.put("monthlyPrice",vpcPrice);
				priceData.put("createTime",DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
				packagePriceMapper.addPrice(priceData);
			}else if("3".equals(status)){
				LinkedHashMap<String, Object> priceData = new LinkedHashMap<String, Object>();
				priceData.put("id",StringUtil.generateUUID());
				priceData.put("packageId",id);
				priceData.put("status",3);
				priceData.put("monthlyPrice",normalPrice);
				priceData.put("createTime",DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
				packagePriceMapper.addPrice(priceData);
			}
			if( n > 0 )
			{
				CloudHostPrice.updateAllOptions();
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
	public String modPackagePricePage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("PackageOptionServiceImpl.modPackagePricePage()");

		String id = request.getParameter("id");
		String type = request.getParameter("type");
		
		if( StringUtil.isBlank(id) )
		{
			throw new AppException("id不能为空");
		}
		PackagePriceMapper packagePriceMapper = this.sqlSession.getMapper(PackagePriceMapper.class);
		PackagePriceVO packagePrice = packagePriceMapper.getById(id);
		List<PriceVO> priceList = packagePriceMapper.getPriceByInfoId(packagePrice.getId());
		if(priceList!=null && priceList.size()>0){
			for(PriceVO price : priceList){
				if(price.getStatus() == 2){
					packagePrice.setVpcPrice(price.getMonthlyPrice());
				}else if(price.getStatus() == 3){
					packagePrice.setPrice(price.getMonthlyPrice());
				}
			}
		}
		request.setAttribute("packagePrice", packagePrice);
		if("1".equals(type)){
			return "/security/operator/package_price_cpu_mod.jsp";
		}else if("2".equals(type)){
			return "/security/operator/package_price_memory_mod.jsp";
		}else if("3".equals(type)){
			return "/security/operator/package_price_disk_mod.jsp";
		}else if("4".equals(type)){
			return "/security/operator/package_price_bandwidth_mod.jsp";
		}else{
			return "/security/operator/package_price_mod.jsp";
		}
	}
	
	@Callable
	@Transactional(readOnly = false)
	public MethodResult modPackagePrice(Map<String, String> parameter)
	{
		logger.debug("PackageOptionServiceImpl.modPackagePrice()");
		try
		{
			// 权限判断
			HttpServletRequest request = RequestContext.getHttpRequest();
			HttpSession session = request.getSession();
			// 参数处理
			String id          = StringUtil.trim(parameter.get("id"));
			String region      = (String)session.getAttribute("optionRegion");
			String type        = StringUtil.trim(parameter.get("type"));
			String normalPrice = StringUtil.trim(parameter.get("price"));
			String vpcPrice    = StringUtil.trim(parameter.get("vpcPrice"));
			String core        = StringUtil.trim(parameter.get("cpuCore"));
			String memory      = StringUtil.trim(parameter.get("memory"));
			String disk        = StringUtil.trim(parameter.get("dataDisk"));
			String bandwidth   = StringUtil.trim(parameter.get("bandwidth"));
			String description = StringUtil.trim(parameter.get("description"));
			String status       = StringUtil.trim(parameter.get("status"));
			BigInteger _memory    = null;
			BigInteger _disk      = null;
			BigInteger _bandwidth = null;
			String diskPriceOfOne = AppProperties.getValue("package_price_disk","");
			String diskPriceOfOne_2 = AppProperties.getValue("package_price_disk_2","");
			String diskPriceOfOne_4 = AppProperties.getValue("package_price_disk_4",""); 
			String diskPriceOfOne_vpc = AppProperties.getValue("package_price_disk_vpc","");
			String diskPriceOfOne_2_vpc = AppProperties.getValue("package_price_disk_2_vpc","");
			String diskPriceOfOne_4_vpc = AppProperties.getValue("package_price_disk_4_vpc","");
			if(StringUtil.isBlank(status) )
			{
				return new MethodResult(MethodResult.FAIL, "类型不能为空");
			}
			if("2".equals(status)){
				if(!StringUtil.isBlank(normalPrice)){
					normalPrice = null;
				}
			}
			if("3".equals(status)){
				if(!StringUtil.isBlank(vpcPrice)){
					vpcPrice = null;
				}
			}
			
			if(!StringUtil.isBlank(memory)){
				_memory = CapacityUtil.fromCapacityLabel(StringUtil.trim(memory + "GB"));
			}
			if(!StringUtil.isBlank(disk)){
				_disk = CapacityUtil.fromCapacityLabel(StringUtil.trim(disk + "GB"));
				//计算硬盘的价格
				if(StringUtil.isBlank(vpcPrice)){
					BigDecimal diskCount = new BigDecimal(disk);
					BigDecimal priceOfOne = null;
					if("4".equals(region)){
						priceOfOne = new BigDecimal(diskPriceOfOne_4_vpc);
					}else if("2".equals(region)){
						priceOfOne = new BigDecimal(diskPriceOfOne_2_vpc);
					}else{
						priceOfOne = new BigDecimal(diskPriceOfOne_vpc);
					}
					vpcPrice = diskCount.multiply(priceOfOne).toString();
				}
				if(StringUtil.isBlank(normalPrice)){
					BigDecimal diskCount = new BigDecimal(disk);
					BigDecimal priceOfOne = null;
					if("4".equals(region)){
						priceOfOne = new BigDecimal(diskPriceOfOne_4);
					}else if("2".equals(region)){
						priceOfOne = new BigDecimal(diskPriceOfOne_2);
					}else{
						priceOfOne = new BigDecimal(diskPriceOfOne);
					}
					normalPrice = diskCount.multiply(priceOfOne).toString();
				}
			}
			if(!StringUtil.isBlank(bandwidth)){
				_bandwidth = FlowUtil.fromFlowLabel(bandwidth + "Mbps");
				BigDecimal bandwidthCount = new BigDecimal(bandwidth);
				//计算带宽价格
				if(StringUtil.isBlank(normalPrice)){
					BigDecimal LESS_THAN_5_GZ = new BigDecimal(AppProperties.getValue("bandwidth_less_than_5", "30"));
					BigDecimal LESS_THAN_5_CD = new BigDecimal(AppProperties.getValue("bandwidth_less_than_5_2", "30"));
					BigDecimal LESS_THAN_5_HK = new BigDecimal(AppProperties.getValue("bandwidth_less_than_5_4", "60"));
					BigDecimal BASE_PRICE_5_GZ = new BigDecimal(AppProperties.getValue("bandwidth_equal_5", "150"));
					BigDecimal BASE_PRICE_5_CD = new BigDecimal(AppProperties.getValue("bandwidth_equal_5_2", "150"));
					BigDecimal BASE_PRICE_5_HK = new BigDecimal(AppProperties.getValue("bandwidth_equal_5_4", "300"));
					BigDecimal BANDWIDTH_MONTHLY_PRICE_PER_1MBPS = new BigDecimal(AppProperties.getValue("bandwidth_base", ""));
					BigDecimal BANDWIDTH_MONTHLY_PRICE_PER_1MBPS_2 = new BigDecimal(AppProperties.getValue("bandwidth_base_2", ""));
					BigDecimal BANDWIDTH_MONTHLY_PRICE_PER_1MBPS_4 = new BigDecimal(AppProperties.getValue("bandwidth_base_4", ""));
					if("4".equals(region)){
						if(bandwidthCount.compareTo(new BigDecimal("5")) <= 0){
							normalPrice = LESS_THAN_5_HK.multiply(bandwidthCount).toString();
						}else{
							normalPrice = BASE_PRICE_5_HK.add((bandwidthCount.subtract(new BigDecimal("5"))).multiply(BANDWIDTH_MONTHLY_PRICE_PER_1MBPS_4)).toString();
						}
					}else if("2".equals(region)){
						if(bandwidthCount.compareTo(new BigDecimal("5")) <= 0){
							normalPrice = LESS_THAN_5_CD.multiply(bandwidthCount).toString();
						}else{
							normalPrice = BASE_PRICE_5_CD.add((bandwidthCount.subtract(new BigDecimal("5"))).multiply(BANDWIDTH_MONTHLY_PRICE_PER_1MBPS_2)).toString();
						}
					}else{
						if(bandwidthCount.compareTo(new BigDecimal("5")) <= 0){
							normalPrice = LESS_THAN_5_GZ.multiply(bandwidthCount).toString();
						}else{
							normalPrice = BASE_PRICE_5_GZ.add((bandwidthCount.subtract(new BigDecimal("5"))).multiply(BANDWIDTH_MONTHLY_PRICE_PER_1MBPS)).toString();
						}
					}
				}
				//计算vpc带宽价格
				if(StringUtil.isBlank(vpcPrice)){
					BigDecimal LESS_THAN_5_GZ_vpc = new BigDecimal(AppProperties.getValue("bandwidth_less_than_5_vpc", "30"));
					BigDecimal LESS_THAN_5_CD_vpc = new BigDecimal(AppProperties.getValue("bandwidth_less_than_5_2_vpc", "30"));
					BigDecimal LESS_THAN_5_HK_vpc = new BigDecimal(AppProperties.getValue("bandwidth_less_than_5_4_vpc", "60"));
					BigDecimal BASE_PRICE_5_GZ_vpc = new BigDecimal(AppProperties.getValue("bandwidth_equal_5_vpc", "150"));
					BigDecimal BASE_PRICE_5_CD_vpc = new BigDecimal(AppProperties.getValue("bandwidth_equal_5_2_vpc", "150"));
					BigDecimal BASE_PRICE_5_HK_vpc = new BigDecimal(AppProperties.getValue("bandwidth_equal_5_4_vpc", "300"));
					BigDecimal BANDWIDTH_MONTHLY_PRICE_PER_1MBPS_vpc = new BigDecimal(AppProperties.getValue("bandwidth_base_vpc", ""));
					BigDecimal BANDWIDTH_MONTHLY_PRICE_PER_1MBPS_2_vpc = new BigDecimal(AppProperties.getValue("bandwidth_base_2_vpc", ""));
					BigDecimal BANDWIDTH_MONTHLY_PRICE_PER_1MBPS_4_vpc = new BigDecimal(AppProperties.getValue("bandwidth_base_4_vpc", ""));
					if("4".equals(region)){
						if(bandwidthCount.compareTo(new BigDecimal("5")) <= 0){
							vpcPrice = LESS_THAN_5_HK_vpc.multiply(bandwidthCount).toString();
						}else{
							vpcPrice = BASE_PRICE_5_HK_vpc.add((bandwidthCount.subtract(new BigDecimal("5"))).multiply(BANDWIDTH_MONTHLY_PRICE_PER_1MBPS_4_vpc)).toString();
						}
					}else if("2".equals(region)){
						if(bandwidthCount.compareTo(new BigDecimal("5")) <= 0){
							vpcPrice = LESS_THAN_5_CD_vpc.multiply(bandwidthCount).toString();
						}else{
							vpcPrice = BASE_PRICE_5_CD_vpc.add((bandwidthCount.subtract(new BigDecimal("5"))).multiply(BANDWIDTH_MONTHLY_PRICE_PER_1MBPS_2_vpc)).toString();
						}
					}else{
						if(bandwidthCount.compareTo(new BigDecimal("5")) <= 0){
							vpcPrice = LESS_THAN_5_GZ_vpc.multiply(bandwidthCount).toString();
						}else{
							vpcPrice = BASE_PRICE_5_GZ_vpc.add((bandwidthCount.subtract(new BigDecimal("5"))).multiply(BANDWIDTH_MONTHLY_PRICE_PER_1MBPS_vpc)).toString();
						}
					}
				}
			}
			if((!"2".equals(status)) && StringUtil.isBlank(normalPrice) )
			{
				return new MethodResult(MethodResult.FAIL, "正常价格不能为空");
			}
			if((!"3".equals(status)) && StringUtil.isBlank(vpcPrice) )
			{
				return new MethodResult(MethodResult.FAIL, "VPC价格不能为空");
			}
			if("1".equals(type) && StringUtil.isBlank(core) )
			{
				return new MethodResult(MethodResult.FAIL, "CPU核数不能为空");
			}
			
			if("2".equals(type) && StringUtil.isBlank(memory) )
			{
				return new MethodResult(MethodResult.FAIL, "内存不能为空");
			}
			
			if("3".equals(type) && StringUtil.isBlank(disk) )
			{
				return new MethodResult(MethodResult.FAIL, "硬盘不能为空");
			}
			
			if("4".equals(type) && StringUtil.isBlank(bandwidth) )
			{
				return new MethodResult(MethodResult.FAIL, "带宽不能为空");
			}
			if(!StringUtil.isBlank(core)){
				boolean isNum = core.matches("[0-9]+");
				
				if(!isNum) {
					return new MethodResult(MethodResult.FAIL, "CPU核心数必须为正整数");
				}
			}
			PackagePriceMapper packagePriceMapper = this.sqlSession.getMapper(PackagePriceMapper.class);
			// 判断套餐项是否已经存在
			LinkedHashMap<String, Object> checkData = new LinkedHashMap<String, Object>();
			checkData.put("region", region);
			checkData.put("type", type);
			checkData.put("cpuCore",StringUtil.isBlank(core)?null:core);
			checkData.put("memory", _memory);
			checkData.put("dataDisk", _disk);
			checkData.put("bandwidth", _bandwidth);
			checkData.put("status", status);
			PackagePriceVO packagePrice = packagePriceMapper.getByRegionAndTypeAndOption(checkData);
			if(packagePrice!=null && packagePrice.getId().equals(id)==false){
				return new MethodResult(MethodResult.FAIL, "该项已存在");
			}
			LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();

			data.put("id", id);
			data.put("cpuCore", StringUtil.isBlank(core)?null:core);
			data.put("memory", _memory);
			data.put("disk", _disk);
			data.put("bandwidth", _bandwidth);
			data.put("description", description);
			data.put("status", status);
			int n = packagePriceMapper.updatePackagePrice(data);
			packagePriceMapper.deletepriceByInfoId(id);
			if("1".equals(status)){
				LinkedHashMap<String, Object> priceData = new LinkedHashMap<String, Object>();
				priceData.put("id",StringUtil.generateUUID());
				priceData.put("packageId",id);
				priceData.put("status",2);
				priceData.put("monthlyPrice",vpcPrice);
				priceData.put("createTime",DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
				packagePriceMapper.addPrice(priceData);
				priceData.put("id",StringUtil.generateUUID());
				priceData.put("status",3);
				priceData.put("monthlyPrice",normalPrice);
				packagePriceMapper.addPrice(priceData);
			}else if("2".equals(status)){
				LinkedHashMap<String, Object> priceData = new LinkedHashMap<String, Object>();
				priceData.put("id",StringUtil.generateUUID());
				priceData.put("packageId",id);
				priceData.put("status",2);
				priceData.put("monthlyPrice",vpcPrice);
				priceData.put("createTime",DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
				packagePriceMapper.addPrice(priceData);
			}else if("3".equals(status)){
				LinkedHashMap<String, Object> priceData = new LinkedHashMap<String, Object>();
				priceData.put("id",StringUtil.generateUUID());
				priceData.put("packageId",id);
				priceData.put("status",3);
				priceData.put("monthlyPrice",normalPrice);
				priceData.put("createTime",DateUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
				packagePriceMapper.addPrice(priceData);
			}
			if( n > 0 )
			{
				CloudHostPrice.updateAllOptions();
				return new MethodResult(MethodResult.SUCCESS, "修改成功");
			}
			else
			{
				return new MethodResult(MethodResult.FAIL, "修改失败");
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
	public MethodResult deletePackagePrice(String id) {
		try {
			if (StringUtil.isBlank(id)) {
				throw new AppException("id不能为空");
			}
			PackagePriceMapper packagePriceMapper = this.sqlSession.getMapper(PackagePriceMapper.class);
			int n = packagePriceMapper.deleteById(id);
			int m = packagePriceMapper.deletepriceByInfoId(id);
			if (n <= 0 || m<=0) {
				return new MethodResult(MethodResult.FAIL, "删除失败");
			}
			CloudHostPrice.updateAllOptions();
			return new MethodResult(MethodResult.SUCCESS, "删除成功");
		} catch (Exception e) {
			throw new AppException(e);
		}
	}
	
	@Callable
	@Transactional(readOnly = false)
	public MethodResult updateDiskPriceOfOne(Map<String, String> parameter)
	{
		logger.debug("PackageOptionServiceImpl.updateDiskPriceOfOne()");
		try
		{
			// 权限判断
//			HttpServletRequest request = RequestContext.getHttpRequest();
			// 参数处理
//			String id         = StringUtil.trim(parameter.get("id"));
			String region     = StringUtil.trim(parameter.get("region"));
			String minDisk    = StringUtil.trim(parameter.get("minDisk"));
			String maxDisk    = StringUtil.trim(parameter.get("maxDisk"));
			String priceOfOne = StringUtil.trim(parameter.get("priceOfOne"));
			if(StringUtil.isBlank(priceOfOne) )
			{
				return new MethodResult(MethodResult.FAIL, "价格不能为空");
			}
			
			if(StringUtil.isBlank(minDisk) )
			{
				return new MethodResult(MethodResult.FAIL, "最小硬盘值不能为空");
			}
			
			if(StringUtil.isBlank(maxDisk) )
			{
				return new MethodResult(MethodResult.FAIL, "最大硬盘值不能为空");
			}
			boolean isNum  = minDisk.matches("[0-9]+");
			boolean isNum2 = maxDisk.matches("[0-9]+");
			
			if(!isNum || !isNum2) {
				return new MethodResult(MethodResult.FAIL, "硬盘值必须为正整数");
			}
			
			int minInt = Integer.parseInt(minDisk);
			int maxInt = Integer.parseInt(maxDisk);
			
			if(minInt>=maxInt) {
				return new MethodResult(MethodResult.FAIL, "磁盘最小值不能大于等于最大值");
			}
			
			PackagePriceMapper packagePriceMapper = this.sqlSession.getMapper(PackagePriceMapper.class);
			BigDecimal diskPriceOfOne = new BigDecimal(AppProperties.getValue("package_price_disk",""));
			BigDecimal diskPriceOfOne_2 = new BigDecimal(AppProperties.getValue("package_price_disk_2",""));
			BigDecimal diskPriceOfOne_4 = new BigDecimal(AppProperties.getValue("package_price_disk_4",""));
			BigDecimal newPrice = new BigDecimal(priceOfOne);
			if("4".equals(region)){
				if(diskPriceOfOne_4.compareTo(newPrice) != 0){
					BigDecimal rate = newPrice.divide(diskPriceOfOne_4,10,RoundingMode.UP);
					Map<String,Object> priceData = new LinkedHashMap<String, Object>();
					priceData.put("price",rate);
					priceData.put("type",3);
					priceData.put("region",region);
					packagePriceMapper.updateAllDiskPrice(priceData);		
				}
			}else if("2".equals(region)){
				if(diskPriceOfOne_2.compareTo(newPrice) != 0){
					BigDecimal rate = newPrice.divide(diskPriceOfOne_2,10,RoundingMode.UP);
					Map<String,Object> priceData = new LinkedHashMap<String, Object>();
					priceData.put("price",rate);
					priceData.put("type",3);
					priceData.put("region",region);
					packagePriceMapper.updateAllDiskPrice(priceData);		
				}
			}else{
				if(diskPriceOfOne.compareTo(newPrice) != 0){
					BigDecimal rate = newPrice.divide(diskPriceOfOne,10,RoundingMode.UP);
					Map<String,Object> priceData = new LinkedHashMap<String, Object>();
					priceData.put("price",rate);
					priceData.put("type",3);
					priceData.put("region",region);
					packagePriceMapper.updateAllDiskPrice(priceData);		
				}
			}
				
			if("4".equals(region)){
				AppProperties.setValue("package_price_disk_4", priceOfOne);
			}else if("2".equals(region)){
				AppProperties.setValue("package_price_disk_2", priceOfOne);
			}else{
				AppProperties.setValue("package_price_disk", priceOfOne);
			}
			AppProperties.setValue("dataDiskMin", minDisk);
			AppProperties.setValue("dataDiskMax", maxDisk);
			AppProperties.save();
			return new MethodResult(MethodResult.SUCCESS, "修改成功");
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("修改失败");
		}
	}
	
	@Callable
	@Transactional(readOnly = false)
	public MethodResult updateBandwidthPriceOfOne(Map<String, String> parameter)
	{
		logger.debug("PackageOptionServiceImpl.updateBandwidthPriceOfOne()");
		try
		{
			// 权限判断
//			HttpServletRequest request = RequestContext.getHttpRequest();
			// 参数处理
//			String id                    = StringUtil.trim(parameter.get("id"));
			String region                = StringUtil.trim(parameter.get("region"));
			String minBandwidth          = StringUtil.trim(parameter.get("minBandwidth"));
			String maxBandwidth          = StringUtil.trim(parameter.get("maxBandwidth"));
			String bandwidthLessThanFive = StringUtil.trim(parameter.get("bandwidthLessThanFive"));
			String bandwidthEqualFive    = StringUtil.trim(parameter.get("bandwidthEqualFive"));
			String bandwidthBase         = StringUtil.trim(parameter.get("bandwidthBase"));
			if(StringUtil.isBlank(bandwidthLessThanFive) || StringUtil.isBlank(bandwidthEqualFive) || StringUtil.isBlank(bandwidthBase))
			{
				return new MethodResult(MethodResult.FAIL, "价格不能为空");
			}
			
			boolean isNum  = minBandwidth.matches("[0-9]+");
			boolean isNum2 = maxBandwidth.matches("[0-9]+");
			
			if(!isNum || !isNum2) {
				return new MethodResult(MethodResult.FAIL, "带宽值必须为正整数");
			}
			
			int minInt = Integer.parseInt(minBandwidth);
			int maxInt = Integer.parseInt(maxBandwidth);
			
			if(minInt>=maxInt) {
				return new MethodResult(MethodResult.FAIL, "带宽最小值不能大于等于最大值");
			}
			if("4".equals(region)){
				AppProperties.setValue("bandwidth_less_than_5_4", bandwidthLessThanFive);
				AppProperties.setValue("bandwidth_equal_5_4", bandwidthEqualFive);
				AppProperties.setValue("bandwidthMin_4", minBandwidth);
				AppProperties.setValue("bandwidthMax_4", maxBandwidth);
				AppProperties.setValue("bandwidth_base_4", bandwidthBase);
			}else if("2".equals(region)){
				AppProperties.setValue("bandwidth_less_than_5_2", bandwidthLessThanFive);
				AppProperties.setValue("bandwidth_equal_5_2", bandwidthEqualFive);	
				AppProperties.setValue("bandwidthMin_2", minBandwidth);
				AppProperties.setValue("bandwidthMax_2", maxBandwidth);
				AppProperties.setValue("bandwidth_base_2", bandwidthBase);
			}else{
				AppProperties.setValue("bandwidth_less_than_5", bandwidthLessThanFive);
				AppProperties.setValue("bandwidth_equal_5", bandwidthEqualFive);	
				AppProperties.setValue("bandwidthMin_1", minBandwidth);
				AppProperties.setValue("bandwidthMax_1", maxBandwidth);
				AppProperties.setValue("bandwidth_base", bandwidthBase);
			}
			AppProperties.save();
			return new MethodResult(MethodResult.SUCCESS, "修改成功");
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("修改失败");
		}
	}
	
	@Callable
	public void getOption(HttpServletRequest request,HttpServletResponse response)
	{
		logger.debug("PackageOptionServiceImpl.getOption()");
		try
		{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");
			
			String type    = request.getParameter("type"); 
			Integer region = StringUtil.parseInteger(request.getParameter("region"),1);
			// 查询数据库
			PackagePriceMapper packagePriceMapper = this.sqlSession.getMapper(PackagePriceMapper.class);
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("type", type);
			condition.put("region", region);
			condition.put("start_row", 0);
			condition.put("row_count", 100);
			List<PackagePriceVO> packagePriceList = packagePriceMapper.getPackagePrice(condition);// 分页结果
			if("1".equals(type) && packagePriceList!=null && (packagePriceList.size()>0)){
				List<Map<String,BigInteger>> optionList = new ArrayList<Map<String,BigInteger>>();
				for(PackagePriceVO pp : packagePriceList){
					Map<String,BigInteger> cpuMap = new HashMap<String, BigInteger>();
					cpuMap.put("text", new BigInteger(pp.getCpuCore().toString()));
					cpuMap.put("value", new BigInteger(pp.getCpuCore().toString()));
					optionList.add(cpuMap);
				}
				response.getWriter().write(JSONLibUtil.toJSONString(optionList));
				return;
			}
			if("2".equals(type) && packagePriceList!=null && (packagePriceList.size()>0)){
				List<Map<String,BigInteger>> optionList = new ArrayList<Map<String,BigInteger>>();
				for(PackagePriceVO pp : packagePriceList){
					Map<String,BigInteger> memoryMap = new HashMap<String, BigInteger>();
					memoryMap.put("text",CapacityUtil.toGBValue(pp.getMemory(),0).toBigInteger());
					memoryMap.put("value",CapacityUtil.toGBValue(pp.getMemory(),0).toBigInteger());
					optionList.add(memoryMap);
				}
				response.getWriter().write(JSONLibUtil.toJSONString(optionList));
				return;
			}
			if("3".equals(type) && packagePriceList!=null && (packagePriceList.size()>0)){
				List<Map<String,BigInteger>> optionList = new ArrayList<Map<String,BigInteger>>();
				for(PackagePriceVO pp : packagePriceList){
					Map<String,BigInteger> dataDiskMap = new HashMap<String, BigInteger>();
					dataDiskMap.put("text", CapacityUtil.toGBValue(pp.getDataDisk(),0).toBigInteger());
					dataDiskMap.put("value", CapacityUtil.toGBValue(pp.getDataDisk(),0).toBigInteger());
					optionList.add(dataDiskMap);
				}
				response.getWriter().write(JSONLibUtil.toJSONString(optionList));
				return;
			}
			if("4".equals(type) && packagePriceList!=null && (packagePriceList.size()>0)){
				List<Map<String,BigInteger>> optionList = new ArrayList<Map<String,BigInteger>>();
				for(PackagePriceVO pp : packagePriceList){
					Map<String,BigInteger> bandwidthMap = new HashMap<String, BigInteger>();
					bandwidthMap.put("text", FlowUtil.toMbpsValue(pp.getBandwidth(),0).toBigInteger());
					bandwidthMap.put("value", FlowUtil.toMbpsValue(pp.getBandwidth(),0).toBigInteger());
					optionList.add(bandwidthMap);
				}
				response.getWriter().write(JSONLibUtil.toJSONString(optionList));
				return;
			}
		}
		catch( Exception e )
		{
			logger.error("PackageOptionServiceImpl.getOption()",e);
			throw new AppException("查询失败");
		}
	}

	@Callable
	@CallWithoutLogin
	public MethodResult getCurrentPrice(String region,String priceStatus, String cpuCore, String memory,String dataDisk, String bandwidth) {
		if(StringUtil.isBlank(region)){
			region = "1";
		}
		if(StringUtil.isBlank(priceStatus)){
			priceStatus = "3";
		}
		if(StringUtil.isBlank(cpuCore)){
			cpuCore = "0";
		}
		if(StringUtil.isBlank(memory)){
			memory = "0";
		}
		if(StringUtil.isBlank(dataDisk)){
			dataDisk = "0";
		}
		if(StringUtil.isBlank(bandwidth)){
			bandwidth = "0";
		}
		BigDecimal price = CloudHostPrice.getMonthlyPrice(Integer.parseInt(region),Integer.parseInt(priceStatus),Integer.parseInt(cpuCore),Integer.parseInt(memory), Double.parseDouble(dataDisk), Integer.parseInt(bandwidth));
		return new MethodResult(MethodResult.SUCCESS,price.toString());
	}
	@Callable
	@Override
	public void queryVPCPrice(HttpServletRequest request,HttpServletResponse response) {
		try{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");
			// 获取参数
			Integer region = StringUtil.parseInteger(request.getParameter("region"),1);
			Integer page   = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
			Integer rows   = StringUtil.parseInteger(request.getParameter("rows"), 10);
			// 查询数据库
			VpcPriceMapper vpcPriceMapper = this.sqlSession.getMapper(VpcPriceMapper.class);
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("region", region);
			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			int total = vpcPriceMapper.getCount(condition); // 总行数
			List<VpcPriceVO> vpcPriceList = vpcPriceMapper.queryAllVpcPriceByRegion(condition);// 分页结果
			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, vpcPriceList);
			
		}catch(Exception e){
			logger.error("PackageOptionServiceImpl.queryVPCPrice()",e);
		}
	}
	@Callable
	public String addVPCPricePage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("PackageOptionServiceImpl.addVPCPricePage()");

		// 权限判断
		String region = request.getParameter("region");
		request.setAttribute("region", region);
		return "/security/operator/vpc_price_add.jsp";
	}
	
	@Callable
	@Transactional(readOnly = false)
	public MethodResult addVPCPrice(Map<String, String> parameter)
	{
		logger.debug("PackageOptionServiceImpl.addVPCPrice()");
		try
		{
			// 权限判断
//			HttpServletRequest request = RequestContext.getHttpRequest();
			// 参数处理
			String region       = StringUtil.trim(parameter.get("region"));
			String price        = StringUtil.trim(parameter.get("price"));
			String vpcAmount    = StringUtil.trim(parameter.get("vpcAmount"));
			
			
			if(StringUtil.isBlank(price) )
			{
				return new MethodResult(MethodResult.FAIL, "价格不能为空");
			}
			if(StringUtil.isBlank(vpcAmount) )
			{
				return new MethodResult(MethodResult.FAIL, "VPC数量不能为空");
			}
			if(!StringUtil.isBlank(vpcAmount)){
				boolean isNum = vpcAmount.matches("[0-9]+");
				
				if(!isNum) {
					return new MethodResult(MethodResult.FAIL, "VPC数量必须为正整数");
				}
			}
			VpcPriceMapper vpcPriceMapper = this.sqlSession.getMapper(VpcPriceMapper.class);
			// 判断套餐项是否已经存在
			LinkedHashMap<String, Object> checkData = new LinkedHashMap<String, Object>();
			checkData.put("region", region);
			checkData.put("vpcAmount", vpcAmount);
			VpcPriceVO vpcPrice = vpcPriceMapper.getAmountAndRegion(checkData);
			if(vpcPrice!=null){
				return new MethodResult(MethodResult.FAIL, "该项已存在");
			}
			LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();

			String id = StringUtil.generateUUID();
			data.put("id", id);
			data.put("region", region);
			data.put("vpcAmount", vpcAmount);
			data.put("price", price);
			int n = vpcPriceMapper.addVpcPrice(data);
			if( n > 0 )
			{
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
	public String modVPCPricePage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("PackageOptionServiceImpl.addVPCPricePage()");

		// 权限判断
		String id = request.getParameter("id");
		VpcPriceMapper vpcPriceMapper = this.sqlSession.getMapper(VpcPriceMapper.class);
		VpcPriceVO vpcPrice = vpcPriceMapper.getById(id);
		request.setAttribute("vpcPrice", vpcPrice);
		return "/security/operator/vpc_price_mod.jsp";
	}
	
	@Callable
	@Transactional(readOnly = false)
	public MethodResult modVPCPrice(Map<String, String> parameter)
	{
		logger.debug("PackageOptionServiceImpl.addPackagePrice()");
		try
		{
			// 权限判断
//			HttpServletRequest request = RequestContext.getHttpRequest();
			// 参数处理
			String id           = StringUtil.trim(parameter.get("id"));
			String region       = StringUtil.trim(parameter.get("region"));
			String price        = StringUtil.trim(parameter.get("price"));
			String vpcAmount    = StringUtil.trim(parameter.get("vpcAmount"));
			
			if(StringUtil.isBlank(price) )
			{
				return new MethodResult(MethodResult.FAIL, "价格不能为空");
			}
			if(StringUtil.isBlank(vpcAmount) )
			{
				return new MethodResult(MethodResult.FAIL, "VPC数量不能为空");
			}
			if(!StringUtil.isBlank(vpcAmount)){
				boolean isNum = vpcAmount.matches("[0-9]+");
				
				if(!isNum) {
					return new MethodResult(MethodResult.FAIL, "VPC数量必须为正整数");
				}
			}
			VpcPriceMapper vpcPriceMapper = this.sqlSession.getMapper(VpcPriceMapper.class);
			// 判断套餐项是否已经存在
			LinkedHashMap<String, Object> checkData = new LinkedHashMap<String, Object>();
			checkData.put("region", region);
			checkData.put("vpcAmount", vpcAmount);
			VpcPriceVO vpcPrice = vpcPriceMapper.getAmountAndRegion(checkData);
			if(vpcPrice!=null && (!id.equals(vpcPrice.getId()))){
				return new MethodResult(MethodResult.FAIL, "该项已存在");
			}
			LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();

			data.put("id", id);
			data.put("vpcAmount", vpcAmount);
			data.put("price", price);
			int n = vpcPriceMapper.updateVpcPrice(data);
			if( n > 0 )
			{
				return new MethodResult(MethodResult.SUCCESS, "修改成功");
			}
			else
			{
				return new MethodResult(MethodResult.FAIL, "修改失败");
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
	public MethodResult deleteVPCPrice(String id) {
		try {
			if (StringUtil.isBlank(id)) {
				throw new AppException("id不能为空");
			}
			VpcPriceMapper vpcPriceMapper = this.sqlSession.getMapper(VpcPriceMapper.class);
			int n = vpcPriceMapper.deleteVpcPrice(id);
			if (n <= 0) {
				return new MethodResult(MethodResult.FAIL, "删除失败");
			}
			return new MethodResult(MethodResult.SUCCESS, "删除成功");
		} catch (Exception e) {
			throw new AppException(e);
		}
	}

	@Callable
	@Transactional(readOnly = false)
	public MethodResult updateIpPrice(Map<String, String> parameter)
	{
		logger.debug("PackageOptionServiceImpl.updateIpPrice()");
		try
		{
			// 权限判断
//			HttpServletRequest request = RequestContext.getHttpRequest();
			// 参数处理
			String region     = StringUtil.trim(parameter.get("region"));
			String ipPrice    = StringUtil.trim(parameter.get("ipPrice"));
			if(StringUtil.isBlank(ipPrice) )
			{
				return new MethodResult(MethodResult.FAIL, "价格不能为空");
			}
			
			if("4".equals(region)){
				AppProperties.setValue("ip_price_4", ipPrice);
			}else if("2".equals(region)){
				AppProperties.setValue("ip_price_2", ipPrice);
			}else{
				AppProperties.setValue("ip_price", ipPrice);
			}
			AppProperties.save();
			return new MethodResult(MethodResult.SUCCESS, "修改成功");
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("修改失败");
		}
	}

	@Override
	public BigDecimal getVpcPrice(String region, Integer amount) {
		VpcPriceMapper vpcPriceMapper = this.sqlSession.getMapper(VpcPriceMapper.class);
		Map<String,Object> condition = new HashMap<String, Object>();
		condition.put("region", region);
		if(amount == 0){
			amount = 2;
		}
		condition.put("vpcAmount", amount);
		VpcPriceVO vpcPrice = vpcPriceMapper.getAmountAndRegion(condition);
		if(vpcPrice!=null){
			return vpcPrice.getPrice();
		}
		return null;
	}
}
