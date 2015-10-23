package com.zhicloud.op.service.impl; 

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.op.app.authorization.PrivilegeConstant;
import com.zhicloud.op.app.helper.CloudHostPrice;
import com.zhicloud.op.app.helper.CloudHostWarehouseHelper;
import com.zhicloud.op.app.helper.CountUserProductsPriceHelper;
import com.zhicloud.op.app.helper.LoginHelper;
import com.zhicloud.op.app.helper.ServiceHelper;
import com.zhicloud.op.app.listener.WarehouseCloudHostCreationListener;
import com.zhicloud.op.app.propeties.AppProperties;
import com.zhicloud.op.common.util.CapacityUtil;
import com.zhicloud.op.common.util.FlowUtil;
import com.zhicloud.op.common.util.RandomPassword;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.core.CoreSpringContextManager;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.login.LoginInfo;
import com.zhicloud.op.mybatis.mapper.AccountBalanceDetailMapper;
import com.zhicloud.op.mybatis.mapper.AgentMapper;
import com.zhicloud.op.mybatis.mapper.BandwidthPackageOptionMapper;
import com.zhicloud.op.mybatis.mapper.CloudHostBillDetailMapper;
import com.zhicloud.op.mybatis.mapper.CloudHostMapper;
import com.zhicloud.op.mybatis.mapper.CloudHostOpenPortMapper;
import com.zhicloud.op.mybatis.mapper.CloudHostShoppingConfigMapper;
import com.zhicloud.op.mybatis.mapper.CloudHostShoppingPortConfigMapper;
import com.zhicloud.op.mybatis.mapper.CloudHostSysDefaultPortsMapper;
import com.zhicloud.op.mybatis.mapper.CpuPackageOptionMapper;
import com.zhicloud.op.mybatis.mapper.DiskPackageOptionMapper;
import com.zhicloud.op.mybatis.mapper.MemoryPackageOptionMapper;
import com.zhicloud.op.mybatis.mapper.OperLogMapper;
import com.zhicloud.op.mybatis.mapper.OrderDetailMapper;
import com.zhicloud.op.mybatis.mapper.OrderInfoMapper;
import com.zhicloud.op.mybatis.mapper.PackagePriceMapper;
import com.zhicloud.op.mybatis.mapper.ShoppingCartMapper;
import com.zhicloud.op.mybatis.mapper.SysDiskImageMapper;
import com.zhicloud.op.mybatis.mapper.SysUserMapper;
import com.zhicloud.op.mybatis.mapper.TerminalUserMapper;
import com.zhicloud.op.mybatis.mapper.UserOrderMapper;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.CallWithoutLogin;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.request.RequestContext;
import com.zhicloud.op.service.AccountBalanceService;
import com.zhicloud.op.service.CloudHostService;
import com.zhicloud.op.service.PaymentService;
import com.zhicloud.op.service.constant.AppConstant;
import com.zhicloud.op.vo.AccountBalanceDetailVO;
import com.zhicloud.op.vo.AgentVO;
import com.zhicloud.op.vo.BandwidthPackageOptionVO;
import com.zhicloud.op.vo.CloudHostSysDefaultPortsVO;
import com.zhicloud.op.vo.CloudHostVO;
import com.zhicloud.op.vo.CloudHostWarehouseDetailVO;
import com.zhicloud.op.vo.CpuPackageOptionVO;
import com.zhicloud.op.vo.DiskPackageOptionVO;
import com.zhicloud.op.vo.JsBankFormVO;
import com.zhicloud.op.vo.MemoryPackageOptionVO;
import com.zhicloud.op.vo.PackagePriceVO;
import com.zhicloud.op.vo.PriceVO;
import com.zhicloud.op.vo.ShoppingCartVO;
import com.zhicloud.op.vo.SysDiskImageVO;
import com.zhicloud.op.vo.SysUserVO;
import com.zhicloud.op.vo.TerminalUserVO;
import com.zhicloud.op.vo.UserOrderVO;

 
 

@Transactional(readOnly = true)
public class PaymentServiceImpl extends BeanDirectCallableDefaultImpl implements PaymentService
{
	public static final Logger logger = Logger.getLogger(PaymentServiceImpl.class);
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	private SqlSession sqlSession;
	
	public void setSqlSession(SqlSession sqlSession)
	{
		this.sqlSession = sqlSession;
	}
	
	@Callable
	public boolean toPayPage(StringBuffer formstr,JsBankFormVO bank) {
		boolean sign = true ;
		StringBuffer propertystr = new StringBuffer(); 
		String MERCHANTID = "300000059";//商户代号
		String POSID = "000000000";//柜台号
		String BRANCHID = "442000000";//建行分行代号
		String ORDERID = "11111111123";//订单号
		String PAYMENT = "10.00";//支付金额
		String CURCODE = "01";//支付币种类型 ，01代表人民币
		String TXCODE = "520100";//交易码，由建行统一分配，个人客户520100，企业客户为690401
		String REMARK1 = "4123";//备注一
		String REMARK2 = "1234";//备注二，备注网银不处理，直接传送城综网
		formstr.append("<form action='https://ibsbjstar.ccb.com.cn/app/ccbMain?' method='post'>\n" );
		propertystr.append("<input type=hidden name='MERCHANTID' value='").append(MERCHANTID).append("' />\n");
		propertystr.append("<input type=hidden name='POSID' value='").append(POSID).append("'/>\n");
		propertystr.append("<input type=hidden name='BRANCHID' value='").append(BRANCHID).append("'/>\n");
		propertystr.append("<input type=hidden name='ORDERID' value='").append(ORDERID).append("'/>\n");
		propertystr.append("<input type=hidden name='PAYMENT' value='").append(PAYMENT).append("'/>\n");
		propertystr.append("<input type=hidden name='CURCODE' value='").append(CURCODE).append("'/>\n");
		propertystr.append("<input type=hidden name='TXCODE' value='").append(TXCODE).append("'/>\n");
		propertystr.append("<input type=hidden name='REMARK1' value='").append(REMARK1).append("'/>\n");
		propertystr.append("<input type=hidden name='REMARK2' value='").append(REMARK2).append("'/>\n");
		//有一点需注意，建行返回地址为商户和银行签约时协商定号的地址，这里不知道怎么办，故没做
		try {
//			MD5 md5 = new MD5();//只用标准的MD5算法，但返回为全大写字符串，银行接收要小写，故转为小写
//			StringBuffer back = new  StringBuffer();
//			back.append("MERCHANTID=").append(MERCHANTID).append("&POSID=").append(POSID).append("&BRANCHID=").append(BRANCHID)
//			.append("&ORDERID=").append(ORDERID).append("&PAYMENT=").append(PAYMENT).append("&CURCODE=").append(CURCODE)
//			.append("&TXCODE=").append(TXCODE).append("&REMARK1=").append(REMARK1).append("&REMARK2=").append(REMARK2);
			propertystr.append("<input type=hidden name='MAC' value='").append("'/>\n");
//			System.out.println(back.toString());
		} catch (RuntimeException e) {
			e.printStackTrace();
			sign = false ;
		}
		 
		formstr.append(propertystr);
		formstr.append("<input type=submit value='确认支付' />\n") ;
		formstr.append("</form>\n");
		System.out.println(formstr);
		return sign;
	}
	/**
	 * 跳转到支付页面
	 */
	@Callable
	public String execute( HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
		 
		 return "/security/user/pay_page.jsp";
	}
	/**
	 * 验签半成品
	 * @param bank
	 * @return
	 * @throws Exception
	 */
	@Callable
	public static boolean RtnPayinfo(HttpServletRequest request) throws Exception	{
		boolean rtn =false ;//验证签名结果
		boolean jieguo = false ;//返回交易是否成功结果 
		CCBSign.RSASig s = new CCBSign.RSASig();
//		String POSTID = bank.getPOSID();
//		String BRANCHID = bank.getBRANCHID();
//		String ORDERID = bank.getORDERID();
//		String PAYMENT = bank.getPAYMENT();
//		String CURCODE = bank.getCURCODE();
//		String REMARK1 = bank.getREMARK1();
//		String REMARK2 = bank.getREMARK2(); 
//		String SUCCESS = "Y";  
//		String SIGN = ""; 
		String POSTID = request.getParameter("POSTID");
		String BRANCHID = request.getParameter("BRANCHID");
		String ORDERID = request.getParameter("ORDERID");
		String PAYMENT = request.getParameter("PAYMENT");
		String CURCODE = request.getParameter("CURCODE");
		String REMARK1 = request.getParameter("REMARK1");
		String REMARK2 = request.getParameter("REMARK2");
		String ACC_TYPE = request.getParameter("ACC_TYPE");
		String SUCCESS = request.getParameter("SUCCESS");
		String TYPE = request.getParameter("TYPE");
		String REFERER = request.getParameter("REFERER");
		String CLIENTIP = request.getParameter("CLIENTIP");
		String ACCDATE = request.getParameter("ACCDATE"); 
		String USRMSG = request.getParameter("USRMSG"); 
		String SIGN = request.getParameter("SIGN"); 
		
		StringBuffer plain = new StringBuffer();
		plain.append("POSID=").append(POSTID).append("&BRANCHID=").append(BRANCHID).append("&ORDERID=").append(ORDERID)
			.append("&PAYMENT=").append(PAYMENT).append("&CURCODE=").append(CURCODE).append("&REMARK1=").append(REMARK1)
			.append("&REMARK2=").append(REMARK2).append("&ACC_TYPE=").append(ACC_TYPE).append("&SUCCESS=").append(SUCCESS)
			.append("&TYPE=").append(TYPE).append("&REFERER=").append(REFERER).append("&CLIENTIP=").append(CLIENTIP)
			.append("&ACCDATE=").append(ACCDATE).append("&USRMSG=").append(USRMSG);
		 
		//用签名验证原始数据
		rtn =s.verifySigature(SIGN, plain.toString());  
		System.out.println(rtn);
		if(rtn){
			if(SUCCESS!=null&&"Y".equals(SUCCESS)){
				/*
				这一段时省略的支付成功之后，对订单等表的操作。
				 */
				jieguo = true ;
			}
		}
		return jieguo ; 
		
	}
	
//	public static void main(String[] args)
//	{
//		JsBankFormVo bank = new JsBankFormVo(); 
//		try {
////			RtnPayinfo(bank);
//		} catch (Exception e) {
//			e.printStackTrace();
//		} 
//	}
	
	/**
	 * 获取页面所需参数
	 */
	@Callable
	@CallWithoutLogin
	public void getParameter(HttpServletRequest request, HttpServletResponse response)
	{
		CpuPackageOptionMapper cpuPackageOptionMapper       = this.sqlSession.getMapper(CpuPackageOptionMapper.class);
		MemoryPackageOptionMapper memoryPackageOptionMapper = this.sqlSession.getMapper(MemoryPackageOptionMapper.class);
		DiskPackageOptionMapper diskPackageOptionMapper     = this.sqlSession.getMapper(DiskPackageOptionMapper.class);
		SysDiskImageMapper sysDiskImageMapper               = this.sqlSession.getMapper(SysDiskImageMapper.class); 
		BandwidthPackageOptionMapper bandwidthPackageOptionMapper = this.sqlSession.getMapper(BandwidthPackageOptionMapper.class);
		CloudHostMapper  cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class); 
        PackagePriceMapper packagePriceMapper = this.sqlSession.getMapper(PackagePriceMapper.class);
        CloudHostSysDefaultPortsMapper cloudHostSysDefaultPortsMapper = this.sqlSession.getMapper(CloudHostSysDefaultPortsMapper.class);
        Map<String, Object> data = new LinkedHashMap<String, Object>();
		data.put("status1",             2);
		data.put("status2",         3);
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
		
		
		ShoppingCartMapper shoppingCartMapper = this.sqlSession.getMapper(ShoppingCartMapper.class);
		
		// 参数处理
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request, AppConstant.SYS_USER_TYPE_TERMINAL_USER);
		String exsit = "false";
		if( loginInfo != null )
		{
			String userId = loginInfo.getUserId();
			UserOrderMapper userOrderMapper = this.sqlSession.getMapper(UserOrderMapper.class);
			List<UserOrderVO> trail = userOrderMapper.getTrailOrderConfigByUserId(userId);
			if( trail!=null && trail.size()>0 )
			{
				request.setAttribute("trailchance", 0);			
			}
			else
			{
				request.setAttribute("trailchance", 1);			
			}
			ShoppingCartVO cart = (ShoppingCartVO) shoppingCartMapper.getCartByUserId(userId);
			request.setAttribute("cart", cart);
			if( cart != null )
			{
				List<ShoppingCartVO> detail = shoppingCartMapper.getCartDetailByCartId(cart.getId());// 分页结果
				request.setAttribute("detail", detail);
				for( ShoppingCartVO vo : detail )
				{
					if( vo.getType() == 3 )
					{
						exsit = "true";
					}
				}
				request.setAttribute("exsit", exsit);

			}
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
		
		
	}
	
	/**
	 * 获取购物车信息
	 */
	@Callable 
	public void getCartParameter(HttpServletRequest request, HttpServletResponse response) {
		 
		
		ShoppingCartMapper shoppingCartMapper = this.sqlSession.getMapper(ShoppingCartMapper.class);
		
		// 参数处理
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if(loginInfo!=null){
			String userId = loginInfo.getUserId(); 	 		
			ShoppingCartVO cart = (ShoppingCartVO)shoppingCartMapper.getCartByUserId(userId);
			request.setAttribute("cart", cart);       	
			if(cart!=null){       		
				List<ShoppingCartVO> detail = shoppingCartMapper.getCartDetailByCartId(cart.getId());// 分页结果
				request.setAttribute("detail", detail);
				
			}  
		}
		
		
	}
	
	/**
	 * 申请试用
	 */
	@Callable
	@Transactional(readOnly = false) 
	public MethodResult getTrail(Map<String, Object> parameter)
	{
		logger.debug("PaymentServiceImpl.getTrail()");
		try
		{
			HttpServletRequest request = RequestContext.getHttpRequest();
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			
			String userId     = loginInfo.getUserId();
			Integer region    = StringUtil.parseInteger((String)parameter.get("region"), 1);
//			String cpuCore    = StringUtil.trim(parameter.get("cpuCore"));
			String item       = StringUtil.trim(parameter.get("item"));
			String sysImageId = StringUtil.trim(parameter.get("sysImageId"));
//			String sysDisk    = StringUtil.trim(parameter.get("sysDisk"));
//			String bandwidth  = StringUtil.trim(parameter.get("bandwidth"));
//			
//			if (bandwidth != null && bandwidth.length() > 0)
//			{
//				bandwidth = (Integer.parseInt(bandwidth) * 1000000) + "";
//			}
			
			if( region==null )
			{
				throw new AppException("‘地域’不能为空");
			}
			
			String price = "0";
			String duration = StringUtil.trim(parameter.get("duration"));
			JSONArray ports = (JSONArray)parameter.get("ports");
			Integer cpuCore = null;
			BigInteger memory = null;
			BigInteger sysDisk = null;
			BigInteger dataDisk = null;
			BigInteger bandwidth = null;
			
			if (duration == null || duration.length() == 0)
			{
				duration = "0";
			}
			if (item == null || item == "")
			{
				return new MethodResult(MethodResult.FAIL, "请选择试用套餐");
			}
			else
			{
				if (item.equals("1"))
				{
					cpuCore     = 2;
					memory      = CapacityUtil.fromCapacityLabel(2 + "GB");
					dataDisk    = CapacityUtil.fromCapacityLabel(100 + "GB");
					bandwidth   = FlowUtil.fromFlowLabel("2Mbps");
				}
				else if (item.equals("2"))
				{
					cpuCore     = 4;
					memory      = CapacityUtil.fromCapacityLabel(4 + "GB");
					dataDisk    = CapacityUtil.fromCapacityLabel(100 + "GB");
					bandwidth   = FlowUtil.fromFlowLabel("2Mbps");
				}
				else if (item.equals("3"))
				{
					cpuCore     = 4;
					memory      = CapacityUtil.fromCapacityLabel(8 + "GB");
					dataDisk    = CapacityUtil.fromCapacityLabel(100 + "GB");
					bandwidth   = FlowUtil.fromFlowLabel("2Mbps");
				}
				else
				{
					throw new AppException("wrong value["+item+"] of paramete 'item'");
				}
				sysDisk  = CapacityUtil.fromCapacityLabel("10GB");
				duration = "1";
			}
			
			
			OrderInfoMapper orderInfoMapper                                     = this.sqlSession.getMapper(OrderInfoMapper.class);
			OrderDetailMapper orderDetailMapper                                 = this.sqlSession.getMapper(OrderDetailMapper.class);
			CloudHostShoppingConfigMapper cloudHostShoppingConfigMapper         = this.sqlSession.getMapper(CloudHostShoppingConfigMapper.class);
			CloudHostShoppingPortConfigMapper cloudHostShoppingPortConfigMapper = this.sqlSession.getMapper(CloudHostShoppingPortConfigMapper.class);
			CloudHostMapper cloudHostMapper                                     = this.sqlSession.getMapper(CloudHostMapper.class);
			SysDiskImageMapper sysDiskImageMapper                               = this.sqlSession.getMapper(SysDiskImageMapper.class);
			CloudHostBillDetailMapper cloudHostBillDetailMapper                 = this.sqlSession.getMapper(CloudHostBillDetailMapper.class);
			
			String sysImageName = sysDiskImageMapper.getById(sysImageId).getName();
			String orderId = StringUtil.generateUUID();
			
			int orderInsertResult = 0;
			int orderDetailInsertResult = 0;
			int shoppingConfigInsertResult = 0;
			
			String now = StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS");
			
			// 新增订单
			Map<String, Object> newOrder = new LinkedHashMap<String, Object>();
			newOrder.put("id",             orderId);
			newOrder.put("userId",         userId);
			newOrder.put("createTime",     now);
			newOrder.put("totalPrice",     price);
			newOrder.put("isPaid",         AppConstant.ORDER_INFO_IS_PAID_YES);						// 试用的云主机状态默认为已付费
			newOrder.put("processStatus",  AppConstant.ORDER_INFO_PROCESS_STATUS_NOT_PROCESSED);
			newOrder.put("processMessage", "");
			orderInsertResult = orderInfoMapper.addOrderInfo(newOrder);
			
			if (orderInsertResult <= 0)
			{
				throw new AppException("添加订单失败");
			}

			String configId    = StringUtil.generateUUID();
			String hostName    = getNewCloudHostNameByUserId(userId,region+"");
			String cloudHostId = null;
			Integer processStatus = null;
			String processMessage = null;
			
			// 从云主机仓库获取云主机
			CloudHostWarehouseDetailVO cloudHostWarehouseDetailVO = new CloudHostWarehouseHelper(this.sqlSession).getWarehouseCloudHost(sysImageId, region);
			if( cloudHostWarehouseDetailVO==null )
			{
				logger.info("PaymentServiceImpl.getTrail() > ["+Thread.currentThread().getId()+"] 云主机仓库没有合适的库存云主机, sysImageId:["+sysImageId+"], region:["+region+"]");
				cloudHostId    = StringUtil.generateUUID();
				processStatus  = AppConstant.CLOUD_HOST_SHOPPING_CONFIG_PROCESS_STATUS_NOT_PROCESSED;
				processMessage = "";
			}
			else
			{
				logger.info("PaymentServiceImpl.getTrail() > ["+Thread.currentThread().getId()+"] 云主机仓库获取的库存云主机成功, sysImageId:["+sysImageId+"], region:["+region+"]");
				cloudHostId    = cloudHostWarehouseDetailVO.getHostId();
				processStatus  = AppConstant.CLOUD_HOST_SHOPPING_CONFIG_PROCESS_STATUS_SUCCESS;
				processMessage = "fetch from cloud host warehouse";
			}
			
			// 新增配置
			Map<String, Object> newConfig = new LinkedHashMap<String, Object>();
			newConfig.put("id",             configId);
			newConfig.put("hostId",         cloudHostId);
			newConfig.put("userId",         userId);
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
			newConfig.put("region",         region);
			shoppingConfigInsertResult = cloudHostShoppingConfigMapper.addCloudHostShoppingConfig(newConfig);
			
			// 新增配置端口
			List<Integer> hgPorts = new ArrayList<Integer>();
			if (shoppingConfigInsertResult > 0 && ports.size() > 0)
			{
				for( int i=0; i<ports.size(); i++ )
				{
					String portStr = (String)ports.get(i);
					String[] arr1 = StringUtil.splitTrim(portStr, ":");
					String protocol = arr1[0];
					String[] portArr = StringUtil.splitTrim(arr1[1], ",");
					int intProtocol = 	"tcp".equalsIgnoreCase(protocol) ? 1 : 
										"udp".equalsIgnoreCase(protocol) ? 2 :
										0;
					for( String intPort : portArr )
					{
						hgPorts.add(intProtocol);
						hgPorts.add(Integer.valueOf(intPort));
						Map<String, Object> new_port = new LinkedHashMap<String, Object>();
						new_port.put("id",        StringUtil.generateUUID());
						new_port.put("config_id", configId);
						new_port.put("protocol",  intProtocol);
						new_port.put("port",      intPort);
						cloudHostShoppingPortConfigMapper.inserIntoConfigPort(new_port);
					}
				}
			}
			
			// 新增订单详情
			Map<String, Object> new_order_detail = new LinkedHashMap<String, Object>();
			new_order_detail.put("id",       StringUtil.generateUUID());
			new_order_detail.put("orderId",  orderId);
			new_order_detail.put("itemType", AppConstant.ORDER_DETAIL_ITEM_TYPE_CLOUD_HOST);
			new_order_detail.put("itemId",   configId);
			orderDetailInsertResult = orderDetailMapper.insertIntoOrderDetail(new_order_detail);
			
			if( cloudHostWarehouseDetailVO!=null )
			{
				// 将云主机分配给用户
				Map<String, Object> newCloudHostData = new LinkedHashMap<String, Object>();
				newCloudHostData.put("type",          AppConstant.CLOUD_HOST_TYPE_3_TERMINAL_USER);
				newCloudHostData.put("userId",        userId);
				newCloudHostData.put("hostName",      hostName);
				newCloudHostData.put("account",       loginInfo.getAccount());
				newCloudHostData.put("password",      loginInfo.getAccount());
				newCloudHostData.put("cpuCore",       cpuCore);
				newCloudHostData.put("memory",        memory);
				newCloudHostData.put("bandwidth",     bandwidth);
				newCloudHostData.put("isAutoStartup", AppConstant.CLOUD_HOST_IS_AUTO_STARTUP_NO);
				newCloudHostData.put("monthlyPrice",  CloudHostPrice.getMonthlyPrice(region,3,cpuCore, memory, dataDisk, bandwidth));
				newCloudHostData.put("ports",         hgPorts.toArray(new Integer[0]));
				MethodResult allocateWarehouseCloudHost = new CloudHostWarehouseHelper(this.sqlSession).allocateWarehouseCloudHost(cloudHostWarehouseDetailVO, newCloudHostData);
				
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
				
				return allocateWarehouseCloudHost;
			}
			else
			{
				// 从云主机仓库获取云主机失败，新增云主机信息，创建不需要添加计费记录，到云主机创建成功之后再去创建
				Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
				cloudHostData.put("id",              cloudHostId);                                                              
				cloudHostData.put("realHostId",      null);                                                                     
				cloudHostData.put("type",            AppConstant.CLOUD_HOST_TYPE_3_TERMINAL_USER);                            
				cloudHostData.put("userId",          userId);                                                                   
				cloudHostData.put("hostName",        hostName);                                                                     
				cloudHostData.put("account",         loginInfo.getAccount());                                                   
				cloudHostData.put("password",        loginInfo.getAccount());                                                   
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
				cloudHostData.put("monthlyPrice",    CloudHostPrice.getMonthlyPrice(region,3,cpuCore, memory, dataDisk, bandwidth));     
				cloudHostData.put("region",          region);
				cloudHostMapper.addCloudHost(cloudHostData);
			}
			
			
			if (orderInsertResult > 0 && orderDetailInsertResult > 0 && shoppingConfigInsertResult > 0)
			{
				return new MethodResult(MethodResult.SUCCESS, "试用成功");
			}
			else
			{
				return new MethodResult(MethodResult.FAIL, "试用失败");
			}
			
		}
		catch (AppException e)
		{ 
			logger.error(e);
			throw new AppException("试用失败");
		}
		catch (Exception e)
		{
			logger.error(e);
			throw new AppException("试用失败");
		}
	}
	
	
	/**
	 * 用户购买云主机
	 */
	@Callable
	@Transactional(readOnly = false) 
	public MethodResult getCloudHost(Map<String, Object> parameter)
	{
		Long begin = System.currentTimeMillis();
		Integer logStatus = AppConstant.OPER_LOG_FAIL;
		logger.debug("TerminalUserServiceImpl.updateBaseInfo()");
		HttpServletRequest request = RequestContext.getHttpRequest();
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		String myHostName = "";
		logger.debug("PaymentServiceImpl.getCloudHost()");
		try
		{
			String userId         = loginInfo.getUserId();
			String cpuCore_str    = StringUtil.trim(parameter.get("cpu"));
			// 获取推荐配置
			String item           = StringUtil.trim(parameter.get("item"));  
			String sysImageId     = StringUtil.trim(parameter.get("sysImageId"));
			String dataDisk_str   = StringUtil.trim(parameter.get("dataDisk"));
			String bandwidth_str  = StringUtil.trim(parameter.get("bandwidth")); 
			String displayName    = StringUtil.trim(parameter.get("displayName"));
			if(displayName==null||displayName.equals("")){
				
				return new MethodResult(MethodResult.FAIL, "请输入主机名");
			}
			if(dataDisk_str==null||dataDisk_str.equals("")){
				
				dataDisk_str   = StringUtil.trim(parameter.get("dataDiskDIY"));
			}
			if(bandwidth_str==null||bandwidth_str.equals("")){
				
				bandwidth_str   = StringUtil.trim(parameter.get("bandwidthDIY"));
			} 
			String memory_str     = StringUtil.trim(parameter.get("memory")); 
			Integer region        = StringUtil.parseInteger((String)parameter.get("region"), null); 
			
			String price = "0";
//			String duration = StringUtil.trim(parameter.get("duration"));
			String duration = "0";
			JSONArray ports = (JSONArray)parameter.get("ports"); 
			Integer cpuCore = null;
			BigInteger memory = null;
			BigInteger sysDisk = null;
			BigInteger dataDisk = null;
			BigInteger bandwidth = null; 
			if(region==null||region.equals("")){
				return new MethodResult(MethodResult.FAIL, "请选择地域");
				
			}
			OrderInfoMapper orderInfoMapper                                     = this.sqlSession.getMapper(OrderInfoMapper.class);
			OrderDetailMapper orderDetailMapper                                 = this.sqlSession.getMapper(OrderDetailMapper.class);
			CloudHostShoppingConfigMapper cloudHostShoppingConfigMapper         = this.sqlSession.getMapper(CloudHostShoppingConfigMapper.class);
			CloudHostShoppingPortConfigMapper cloudHostShoppingPortConfigMapper = this.sqlSession.getMapper(CloudHostShoppingPortConfigMapper.class);
			CloudHostMapper cloudHostMapper                                     = this.sqlSession.getMapper(CloudHostMapper.class);
			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class); 
			
			CloudHostBillDetailMapper cloudHostBillDetailMapper                 = this.sqlSession.getMapper(CloudHostBillDetailMapper.class);
			TerminalUserVO terminalUserVO = terminalUserMapper.getBalanceById(loginInfo.getUserId()); 
			if(item!=null&&!(item.equals(""))){
				PackagePriceMapper packagePriceMapper = this.sqlSession.getMapper(PackagePriceMapper.class);
				List<PriceVO> priceList =  packagePriceMapper.getPriceByInfoId(item);
				PackagePriceVO packagePriceVO = packagePriceMapper.getById(item);
 				if(priceList==null){					
					return new MethodResult(MethodResult.FAIL, "套餐选择有误"); 
				}
  				for(PriceVO priceVo : priceList){
 					if(priceVo.getStatus() == 3){
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
//				if(!(StringUtil.isInt(cpuCore_str))||cpuCore_str.length()>2){					
//					return new MethodResult(MethodResult.FAIL, "CPU选择有误");
//				} 
				cpuCore = Integer.parseInt(cpuCore_str);
				if(memory_str==null||memory_str.equals("")){
					return new MethodResult(MethodResult.FAIL, "请选择内存");
					
				}
//				if(!(StringUtil.isInt(memory_str))||memory_str.length()>2){					
//					return new MethodResult(MethodResult.FAIL, "内存选择有误");
//				} 
				memory      = CapacityUtil.fromCapacityLabel(memory_str + "GB"); 
				if(dataDisk_str==null||dataDisk_str.equals("")){
					return new MethodResult(MethodResult.FAIL, "请选择硬盘");
					
				}
//				if(!(StringUtil.isInt(dataDisk_str))||dataDisk_str.length()>5){					
//					return new MethodResult(MethodResult.FAIL, "硬盘选择有误");
//				} 
				dataDisk    = CapacityUtil.fromCapacityLabel(dataDisk_str + "GB");
				if(bandwidth_str==null||bandwidth_str.equals("")){
					return new MethodResult(MethodResult.FAIL, "请选择带宽");
					
				}
//				if(!(StringUtil.isInt(bandwidth_str))||bandwidth_str.length()>5){					
//					return new MethodResult(MethodResult.FAIL, "带宽选择有误");
//				} 
				bandwidth   = FlowUtil.fromFlowLabel(bandwidth_str+"Mbps");;
				price =  CloudHostPrice.getMonthlyPrice(region,3,cpuCore, memory, dataDisk, bandwidth).setScale(2,   BigDecimal.ROUND_HALF_UP)+"";
			}
			sysDisk  = CapacityUtil.fromCapacityLabel("10GB"); 
			
			
			//计算余额能否支持一天，不足不创建主机
			if(terminalUserVO!=null){
 				 
				
 				BigDecimal totalPrice = new BigDecimal("0");
				//算出已有产品每天的费用
 				totalPrice = new CountUserProductsPriceHelper(sqlSession).getAllOneDayPrice(userId);
				//计算出每天的费用
				totalPrice = totalPrice.add(new BigDecimal(price)).multiply(new BigDecimal("100").subtract(terminalUserVO.getPercentOff()).multiply(new BigDecimal("0.01")));;
				totalPrice = totalPrice.divide(new BigDecimal("31"),0,BigDecimal.ROUND_HALF_UP);
				BigDecimal balance = terminalUserVO.getAccountBalance().setScale(0, BigDecimal.ROUND_HALF_UP);
				if(balance.compareTo(totalPrice)<0){
					if(totalPrice.subtract(balance).compareTo(BigDecimal.TEN)<0){						
 						return new MethodResult(MethodResult.FAIL, "余额不足，至少充值<a href='bean/page.do?userType=4&bean=accountBalanceService&method=toRechargePage' target='blank'>10</a>元<br/>多充多优惠，<a href='bean/page.do?userType=4&bean=accountBalanceService&method=toRechargePage' target='blank'>点击进入充值</a>");
					}else{						
 						return new MethodResult(MethodResult.FAIL, "余额不足，至少充值<a href='bean/page.do?userType=4&bean=accountBalanceService&method=toRechargePage' target='blank'>"+totalPrice.subtract(balance)+"</a>元<br/>多充多优惠，<a href='bean/page.do?userType=4&bean=accountBalanceService&method=toRechargePage' target='blank'>点击进入充值</a>");
					}
 					
				} 
				
			} 
			
			String configId       = StringUtil.generateUUID();
			String hostName       = getNewCloudHostNameByUserId(userId,region+"");
			myHostName = displayName;
			String cloudHostId    = null;
			Integer processStatus = null;
			String processMessage = null;
			
			List<String> host_ports = new ArrayList<String>();
			if (ports!=null&& ports.size() > 0&& region==2)
			{ 
				for( int i=0; i<ports.size(); i++ )
				{
					String [] portArray = StringUtil.trim(ports.get(i)).split("&");
					String portName = (String)portArray[0];  
					String protocol = portArray[1]; 
					String port = (String)portArray[2]; 
					if(!StringUtil.isInt(port)){
						continue;
					}
					host_ports.add(protocol+":"+port+":"+portName);   
				} 
				host_ports.add("1:21:FTP");   
				host_ports.add("1:30000:FTP");       
				host_ports.add("1:30001:FTP");       
				host_ports.add("1:30002:FTP");       
				host_ports.add("1:30003:FTP");       
				
			} 
			
			// 从云主机仓库获取云主机
			Map<String, Object> newCloudHostData = new LinkedHashMap<String, Object>();
			newCloudHostData.put("type",          AppConstant.CLOUD_HOST_TYPE_3_TERMINAL_USER);
			newCloudHostData.put("userId",        loginInfo.getUserId());
			newCloudHostData.put("hostName",      hostName);
			newCloudHostData.put("displayName",      displayName);
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
			MethodResult result = new CloudHostWarehouseHelper(this.sqlSession).getAndAllocateWarehouseCloudHost(sysImageId, region, newCloudHostData);
			if( MethodResult.SUCCESS.equals(result.status) )
			{
				CloudHostWarehouseDetailVO vo = (CloudHostWarehouseDetailVO) result.get("CloudHostWarehouseDetailVO");
				String orderId = StringUtil.generateUUID();
				
				int orderInsertResult = 0; 
				int shoppingConfigInsertResult = 0;
				
				String now = StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS");
				
				// 新增订单
				Map<String, Object> newOrder = new LinkedHashMap<String, Object>();
				newOrder.put("id",             orderId);
				newOrder.put("userId",         userId);
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
				processStatus  = AppConstant.CLOUD_HOST_SHOPPING_CONFIG_PROCESS_STATUS_SUCCESS;
				processMessage = "fetch from cloud host warehouse"; 
				
				// 新增配置
				Map<String, Object> newConfig = new LinkedHashMap<String, Object>();
				newConfig.put("id",             configId);
				newConfig.put("hostId",         cloudHostId);
				newConfig.put("userId",         userId);
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
				
				// 新增配置端口 
				if (shoppingConfigInsertResult > 0 &&host_ports!=null&& host_ports.size() > 0 &&region==2)
				{
					for( int i=0; i<host_ports.size(); i++ )
					{
						String portStr = (String)host_ports.get(i);
						String[] arr1 = StringUtil.splitTrim(portStr, ":");
						String name = arr1[2];
						String protocol = arr1[0];
						String port = arr1[1]; 
						int intProtocol = 	"tcp".equalsIgnoreCase(protocol) ? 1 : 
											"udp".equalsIgnoreCase(protocol) ? 2 :
											0;
						Map<String, Object> new_port = new LinkedHashMap<String, Object>();
						new_port.put("id",        StringUtil.generateUUID());
						new_port.put("config_id", configId);
						new_port.put("name",  name);
						new_port.put("protocol",  intProtocol);
						new_port.put("port",      port);
						cloudHostShoppingPortConfigMapper.inserIntoConfigPort(new_port); 
					}
				}
				
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
				// 启动云主机
				CloudHostService cloudHostService = CoreSpringContextManager.getCloudHostService();
				cloudHostService.startCloudHost(cloudHostId); 
				
				 
				
				//分配完成，立即创建一个补位 
				WarehouseCloudHostCreationListener.instance.addWarehouseIdNeedToBeImmediatelyCreated(vo.getWarehouseId());
				

				 
				// 创建成功，返回云主机ID
				logStatus = AppConstant.OPER_LOG_SUCCESS;
				MethodResult result_for_web = new MethodResult(MethodResult.SUCCESS, "");
				result_for_web.put("cloudHostId", cloudHostId);
				return result_for_web; 
				
				
//				logStatus = AppConstant.OPER_LOG_SUCCESS;
//				return result;
			}else{
				// 从仓库获取云主机失败，正常创建
				return getCloudHostForNormal(parameter);
			}
			
			 
			
		}
		catch (Exception e)
		{
			logger.error(e);
			throw new AppException("创建失败");
		}finally{
			try{
				OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
				Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
				operLogData.put("id", StringUtil.generateUUID());
				operLogData.put("userId", loginInfo.getUserId());
				operLogData.put("content", "创建云主机:"+myHostName);
				operLogData.put("operTime", StringUtil.dateToString(new Date(),"yyyyMMddHHmmssSSS"));
				operLogData.put("status", logStatus);
				operLogData.put("resourceName", myHostName);
				operLogData.put("operDuration", System.currentTimeMillis()-begin);
				operLogMapper.addOperLog(operLogData);
			}catch(Exception e){
				logger.error(e);
			}
			
		} 
	}
	/**
	 * 代理商为用户创建云主机
	 */
	@Callable
	@Transactional(readOnly = false) 
	public MethodResult getCloudHostFromAgent(Map<String, Object> parameter)
	{ 
		Long begin = System.currentTimeMillis();
		Integer logStatus = AppConstant.OPER_LOG_FAIL;
		HttpServletRequest request = null;
		String myHostName = "";
		logger.debug("PaymentServiceImpl.getCloudHostFromAgent()");
		LoginInfo loginInfo;
		if(parameter.get("loginInfo")!=null){			
			 loginInfo = (LoginInfo) parameter.get("loginInfo");
		}else{			
			request = RequestContext.getHttpRequest();
			 loginInfo = LoginHelper.getLoginInfo(request);
		}
		try
		{
			String userId         = StringUtil.trim(parameter.get("userId"));
			String cpuCore_str    = StringUtil.trim(parameter.get("cpu")); 
			String sysImageId     = StringUtil.trim(parameter.get("sysImageId"));
			String dataDisk_str   = StringUtil.trim(parameter.get("dataDisk"));
			String bandwidth_str  = StringUtil.trim(parameter.get("bandwidth")); 
			String displayName    = StringUtil.trim(parameter.get("displayName"));
			String item           = StringUtil.trim(parameter.get("item")); 
			if(displayName==null||displayName.equals("")){
				
				return new MethodResult(MethodResult.FAIL, "请输入主机名");
			}
			if(dataDisk_str==null||dataDisk_str.equals("")){
				
				dataDisk_str   = StringUtil.trim(parameter.get("dataDiskDIY"));
			}
			dataDisk_str = dataDisk_str.replace("B", "");
			if(bandwidth_str==null||bandwidth_str.equals("")){
				
				bandwidth_str   = StringUtil.trim(parameter.get("bandwidthDIY"));
			} 
			String memory_str     = StringUtil.trim(parameter.get("memory")); 
			Integer region        = StringUtil.parseInteger((String)parameter.get("region"), null); 
			
			String price = "0";
//			String duration = StringUtil.trim(parameter.get("duration"));
			String duration = "0";
			JSONArray ports = (JSONArray)parameter.get("ports");
			Integer cpuCore = null;
			BigInteger memory = null;
			BigInteger sysDisk = null;
			BigInteger dataDisk = null;
			BigInteger bandwidth = null; 
			BigDecimal countPrice = null;
			if(region==null||region.equals("")){
				return new MethodResult(MethodResult.FAIL, "请选择地域");
				
			}
			OrderInfoMapper orderInfoMapper                                     = this.sqlSession.getMapper(OrderInfoMapper.class);
			OrderDetailMapper orderDetailMapper                                 = this.sqlSession.getMapper(OrderDetailMapper.class);
			CloudHostShoppingConfigMapper cloudHostShoppingConfigMapper         = this.sqlSession.getMapper(CloudHostShoppingConfigMapper.class);
			CloudHostShoppingPortConfigMapper cloudHostShoppingPortConfigMapper = this.sqlSession.getMapper(CloudHostShoppingPortConfigMapper.class);
			CloudHostMapper cloudHostMapper                                     = this.sqlSession.getMapper(CloudHostMapper.class);
			AgentMapper agentMapper = this.sqlSession.getMapper(AgentMapper.class); 
			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class); 
			
			CloudHostBillDetailMapper cloudHostBillDetailMapper                 = this.sqlSession.getMapper(CloudHostBillDetailMapper.class);
			TerminalUserVO terminalUser = terminalUserMapper.getTerminalUserById(userId);
			AgentVO agentVO = agentMapper.getAgentById(loginInfo.getUserId());
			if(item!=null&&!(item.equals(""))){
				PackagePriceMapper packagePriceMapper = this.sqlSession.getMapper(PackagePriceMapper.class);
				List<PriceVO> priceList =  packagePriceMapper.getPriceByInfoId(item);
				PackagePriceVO packagePriceVO = packagePriceMapper.getById(item);
 				if(priceList==null){					
					return new MethodResult(MethodResult.FAIL, "套餐选择有误"); 
				}
  				for(PriceVO priceVo : priceList){
 					if(priceVo.getStatus() == 3){
 						countPrice = priceVo.getMonthlyPrice();
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
				memory = CapacityUtil.fromCapacityLabel(memory_str+"GB");
				if(dataDisk_str==null||dataDisk_str.equals("")){
					return new MethodResult(MethodResult.FAIL, "请选择硬盘");
					
				}  
				dataDisk = CapacityUtil.fromCapacityLabel(dataDisk_str+"GB");
				if(bandwidth_str==null||bandwidth_str.equals("")){
					return new MethodResult(MethodResult.FAIL, "请选择带宽");
					
				} 
				bandwidth   = FlowUtil.fromFlowLabel(bandwidth_str+"Mbps");
				countPrice = CloudHostPrice.getMonthlyPrice(region,3,cpuCore, memory, dataDisk, bandwidth).setScale(2,   BigDecimal.ROUND_HALF_UP);
			}
			
			price = countPrice+"";
//			price =   countPrice.subtract(countPrice.multiply(agentVO.getPercentOff().multiply(new BigDecimal("0.01")))).setScale(2,   BigDecimal.ROUND_HALF_UP)+"";
			sysDisk  = CapacityUtil.fromCapacityLabel("10GB"); 
			
			
			//计算余额能否支持一天，不足不创建主机
			if(agentVO!=null){
				
				
				List<CloudHostVO> cloudHostList = cloudHostMapper.getByAgentId(loginInfo.getUserId());
				BigDecimal totalPrice = new BigDecimal("0");
				if(cloudHostList!=null&&cloudHostList.size()>0){
					for(CloudHostVO vo:cloudHostList){
						if(vo.getType() == 2){
							if(!(vo.getSummarizedStatusText().equals("创建失败")||vo.getSummarizedStatusText().equals("停机"))&&vo.getMonthlyPrice()!=null){						
								totalPrice = vo.getMonthlyPrice().add(totalPrice);
							}
						}
					} 
					
				} 
				//计算出每天的费用
				totalPrice = totalPrice.add(countPrice).multiply(new BigDecimal("100").subtract(agentVO.getPercentOff()).multiply(new BigDecimal("0.01")));
				totalPrice = totalPrice.divide(new BigDecimal("31"),0,BigDecimal.ROUND_HALF_UP);
				BigDecimal balance = agentVO.getAccountBalance().setScale(0, BigDecimal.ROUND_HALF_UP);
				if(balance.compareTo(totalPrice)<0){
					if(totalPrice.subtract(balance).compareTo(BigDecimal.TEN)<0){						
						return new MethodResult(MethodResult.FAIL, "余额不足，至少充值10元 ");
					}else{						
						return new MethodResult(MethodResult.FAIL, "余额不足，至少充值"+totalPrice.subtract(balance)+"元 ");
					}
					
				} 
				
			} 
			
			String configId       = StringUtil.generateUUID();
			String hostName       = getNewCloudHostNameByUserId(userId,region+"");
			myHostName = hostName;
			String cloudHostId    = null;
			Integer processStatus = null;
			String processMessage = null;
			
			List<String> host_ports = new ArrayList<String>();
			if (ports!=null&& ports.size() > 0&& region==2)
			{ 
				for( int i=0; i<ports.size(); i++ )
				{
					String [] portArray = StringUtil.trim(ports.get(i)).split("&");
					String portName = (String)portArray[0];  
					String protocol = portArray[1]; 
					String port = (String)portArray[2]; 
					if(!StringUtil.isInt(port)){
						continue;
					}
					host_ports.add(protocol+":"+port+":"+portName);   
				} 
				host_ports.add("1:21:FTP");   
				host_ports.add("1:30000:FTP");       
				host_ports.add("1:30001:FTP");       
				host_ports.add("1:30002:FTP");       
				host_ports.add("1:30003:FTP");       
				
			} 
			
			// 从云主机仓库获取云主机
			Map<String, Object> newCloudHostData = new LinkedHashMap<String, Object>();
			newCloudHostData.put("type",          AppConstant.CLOUD_HOST_TYPE_2_AGENT);
			newCloudHostData.put("userId",        terminalUser.getId());
			newCloudHostData.put("hostName",      hostName);
			newCloudHostData.put("displayName",      displayName);
			newCloudHostData.put("account",       terminalUser.getAccount());
			newCloudHostData.put("password",      RandomPassword.getRandomPwd(16));
			newCloudHostData.put("cpuCore",       Integer.valueOf(cpuCore));
			newCloudHostData.put("memory",        memory);
			newCloudHostData.put("dataDisk",      dataDisk);
			newCloudHostData.put("bandwidth",     bandwidth);
			newCloudHostData.put("isAutoStartup", AppConstant.CLOUD_HOST_IS_AUTO_STARTUP_YES);
			newCloudHostData.put("monthlyPrice", price);
			newCloudHostData.put("ports",         _formatToHttpGatewayFormatPorts(host_ports));
			newCloudHostData.put("item", item);
			MethodResult result = new CloudHostWarehouseHelper(this.sqlSession).getAndAllocateWarehouseCloudHost(sysImageId, region, newCloudHostData);
			if( MethodResult.SUCCESS.equals(result.status) )
			{
				CloudHostWarehouseDetailVO vo = (CloudHostWarehouseDetailVO) result.get("CloudHostWarehouseDetailVO");
				String orderId = StringUtil.generateUUID();
				
				int orderInsertResult = 0;
				int orderDetailInsertResult = 0;
				int shoppingConfigInsertResult = 0;
				
				String now = StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS");
				
				// 新增订单
				Map<String, Object> newOrder = new LinkedHashMap<String, Object>();
				newOrder.put("id",             orderId);
				newOrder.put("userId",         userId);
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
				processStatus  = AppConstant.CLOUD_HOST_SHOPPING_CONFIG_PROCESS_STATUS_SUCCESS;
				processMessage = "fetch from cloud host warehouse"; 
				
				// 新增配置
				Map<String, Object> newConfig = new LinkedHashMap<String, Object>();
				newConfig.put("id",             configId);
				newConfig.put("hostId",         cloudHostId);
				newConfig.put("userId",         userId);
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
				
				// 新增配置端口 
				if (shoppingConfigInsertResult > 0 &&host_ports!=null&& host_ports.size() > 0 &&region==2)
				{
					for( int i=0; i<host_ports.size(); i++ )
					{
						String portStr = (String)host_ports.get(i);
						String[] arr1 = StringUtil.splitTrim(portStr, ":");
						String name = arr1[2];
						String protocol = arr1[0];
						String port = arr1[1]; 
						int intProtocol = 	"tcp".equalsIgnoreCase(protocol) ? 1 : 
											"udp".equalsIgnoreCase(protocol) ? 2 :
											0;
						Map<String, Object> new_port = new LinkedHashMap<String, Object>();
						new_port.put("id",        StringUtil.generateUUID());
						new_port.put("config_id", configId);
						new_port.put("name",  name);
						new_port.put("protocol",  intProtocol);
						new_port.put("port",      port);
						cloudHostShoppingPortConfigMapper.inserIntoConfigPort(new_port); 
					}
				}
				
				// 新增订单详情
				Map<String, Object> new_order_detail = new LinkedHashMap<String, Object>();
				new_order_detail.put("id",       StringUtil.generateUUID());
				new_order_detail.put("orderId",  orderId);
				new_order_detail.put("itemType", AppConstant.ORDER_DETAIL_ITEM_TYPE_CLOUD_HOST);
				new_order_detail.put("itemId",   configId);
				orderDetailInsertResult = orderDetailMapper.insertIntoOrderDetail(new_order_detail);
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
				// 启动云主机
				CloudHostService cloudHostService = CoreSpringContextManager.getCloudHostService();
				cloudHostService.startCloudHost(cloudHostId); 
				
				
				//分配完成，立即创建一个补位 
				WarehouseCloudHostCreationListener.instance.addWarehouseIdNeedToBeImmediatelyCreated(vo.getWarehouseId());
				

				
				// 创建成功，返回云主机ID 
				logStatus = AppConstant.OPER_LOG_SUCCESS;
				MethodResult result_for_web = new MethodResult(MethodResult.SUCCESS, "");
				result_for_web.put("cloudHostId", cloudHostId);
				return result_for_web; 
				
				
//				logStatus = AppConstant.OPER_LOG_SUCCESS;
//				return result;
			}else{
				// 从仓库获取云主机失败，正常创建
				return getCloudHostForAgentNormal(parameter);
			}
		}
		catch (AppException e)
		{ 
			logger.error(e);
			throw new AppException("创建失败");
		}
		catch (Exception e)
		{
			logger.error(e);
			throw new AppException(e);
		}finally{
			try{
				OperLogMapper operLogMapper = this.sqlSession.getMapper(OperLogMapper.class);
				Map<String, Object> operLogData = new LinkedHashMap<String, Object>();
				operLogData.put("id", StringUtil.generateUUID());
				operLogData.put("userId", loginInfo.getUserId());
				operLogData.put("content", "创建云主机:"+myHostName);
				operLogData.put("operTime", StringUtil.dateToString(new Date(),"yyyyMMddHHmmssSSS"));
				operLogData.put("status", logStatus);
				operLogData.put("resourceName", myHostName);
				operLogData.put("operDuration", System.currentTimeMillis()-begin);
				operLogMapper.addOperLog(operLogData);
			}catch(Exception e){
				logger.error(e);
			}
			
		} 
	}
	/**
	 * 用户购买云主机
	 */
	@Callable
	@Transactional(readOnly = false) 
	public MethodResult getCloudHostForNormal(Map<String, Object> parameter)
	{
		Long begin = System.currentTimeMillis();
		Integer logStatus = AppConstant.OPER_LOG_FAIL;
		HttpServletRequest request = RequestContext.getHttpRequest();
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		String myHostName = "";
		logger.debug("PaymentServiceImpl.getCloudHostForNormal()");
		try
		{
			String userId         = loginInfo.getUserId();
			String cpuCore_str    = StringUtil.trim(parameter.get("cpu"));
			// 获取推荐配置
			String item           = StringUtil.trim(parameter.get("item"));  
			String sysImageId     = StringUtil.trim(parameter.get("sysImageId"));
			String dataDisk_str   = StringUtil.trim(parameter.get("dataDisk"));
			String bandwidth_str  = StringUtil.trim(parameter.get("bandwidth")); 
			String displayName    = StringUtil.trim(parameter.get("displayName"));
			if(displayName==null||displayName.equals("")){
				
				return new MethodResult(MethodResult.FAIL, "请输入主机名");
			}
			if(dataDisk_str==null||dataDisk_str.equals("")){
				
				dataDisk_str   = StringUtil.trim(parameter.get("dataDiskDIY"));
			}
			if(bandwidth_str==null||bandwidth_str.equals("")){
				
				bandwidth_str   = StringUtil.trim(parameter.get("bandwidthDIY"));
			} 
			String memory_str     = StringUtil.trim(parameter.get("memory")); 
			Integer region        = StringUtil.parseInteger((String)parameter.get("region"), null); 
			
			String price = "0";
//			String duration = StringUtil.trim(parameter.get("duration"));
			String duration = "0"; 
			JSONArray ports = (JSONArray)parameter.get("ports");  
			Integer cpuCore = null;
			BigInteger memory = null;
			BigInteger sysDisk = null;
			BigInteger dataDisk = null;
			BigInteger bandwidth = null;
			BigDecimal level_price = null;
			if(region==null||region.equals("")){
				return new MethodResult(MethodResult.FAIL, "请选择地域");
				
			}
			OrderInfoMapper orderInfoMapper                                     = this.sqlSession.getMapper(OrderInfoMapper.class);
			OrderDetailMapper orderDetailMapper                                 = this.sqlSession.getMapper(OrderDetailMapper.class);
			CloudHostShoppingConfigMapper cloudHostShoppingConfigMapper         = this.sqlSession.getMapper(CloudHostShoppingConfigMapper.class);
			CloudHostShoppingPortConfigMapper cloudHostShoppingPortConfigMapper = this.sqlSession.getMapper(CloudHostShoppingPortConfigMapper.class);
			CloudHostMapper cloudHostMapper                                     = this.sqlSession.getMapper(CloudHostMapper.class);
			TerminalUserMapper terminalUserMapper                               = this.sqlSession.getMapper(TerminalUserMapper.class); 
			SysDiskImageMapper sysDiskImageMapper                               = this.sqlSession.getMapper(SysDiskImageMapper.class); 
			
			CloudHostBillDetailMapper cloudHostBillDetailMapper                 = this.sqlSession.getMapper(CloudHostBillDetailMapper.class);
 			TerminalUserVO terminalUserVO = terminalUserMapper.getBalanceById(loginInfo.getUserId()); 
			if(item!=null&&!(item.equals(""))){
				PackagePriceMapper packagePriceMapper = this.sqlSession.getMapper(PackagePriceMapper.class);
				List<PriceVO> priceList =  packagePriceMapper.getPriceByInfoId(item);
				PackagePriceVO packagePriceVO = packagePriceMapper.getById(item);
 				if(priceList==null){					
					return new MethodResult(MethodResult.FAIL, "套餐选择有误"); 
				}
  				for(PriceVO priceVo : priceList){
 					if(priceVo.getStatus() == 3){
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
//				if(!(StringUtil.isInt(cpuCore_str))||cpuCore_str.length()>2){					
//					return new MethodResult(MethodResult.FAIL, "CPU选择有误");
//				} 
				cpuCore = Integer.parseInt(cpuCore_str);
				if(memory_str==null||memory_str.equals("")){
					return new MethodResult(MethodResult.FAIL, "请选择内存");
					
				}
//				if(!(StringUtil.isInt(memory_str))||memory_str.length()>2){					
//					return new MethodResult(MethodResult.FAIL, "内存选择有误");
//				} 
				memory      = CapacityUtil.fromCapacityLabel(memory_str + "GB"); 
				if(dataDisk_str==null||dataDisk_str.equals("")){
					return new MethodResult(MethodResult.FAIL, "请选择硬盘");
					
				}
//				if(!(StringUtil.isInt(dataDisk_str))||dataDisk_str.length()>5){					
//					return new MethodResult(MethodResult.FAIL, "硬盘选择有误");
//				} 
				dataDisk    = CapacityUtil.fromCapacityLabel(dataDisk_str + "GB");
				if(bandwidth_str==null||bandwidth_str.equals("")){
					return new MethodResult(MethodResult.FAIL, "请选择带宽");
					
				}
//				if(!(StringUtil.isInt(bandwidth_str))||bandwidth_str.length()>5){					
//					return new MethodResult(MethodResult.FAIL, "带宽选择有误");
//				} 
				bandwidth   = FlowUtil.fromFlowLabel(bandwidth_str+"Mbps");
				price =  CloudHostPrice.getMonthlyPrice(region,3,cpuCore, memory, dataDisk, bandwidth).setScale(2,   BigDecimal.ROUND_HALF_UP)+"";
			}
			sysDisk  = CapacityUtil.fromCapacityLabel("10GB"); 
			
			 
			
			String orderId = StringUtil.generateUUID();
			
			int orderInsertResult = 0;
			int orderDetailInsertResult = 0;
			int shoppingConfigInsertResult = 0;
			
			String now = StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS");
			
			// 新增订单
			Map<String, Object> newOrder = new LinkedHashMap<String, Object>();
			newOrder.put("id",             orderId);
			newOrder.put("userId",         userId);
			newOrder.put("createTime",     now);
			newOrder.put("totalPrice",     price);
			newOrder.put("isPaid",         AppConstant.ORDER_INFO_IS_PAID_YES);						// 试用的云主机状态默认为已付费
			newOrder.put("processStatus",  AppConstant.ORDER_INFO_PROCESS_STATUS_NOT_PROCESSED);
			newOrder.put("processMessage", "");
			orderInsertResult = orderInfoMapper.addOrderInfo(newOrder);
			
			if (orderInsertResult <= 0)
			{
				throw new AppException("添加订单失败");
			}
			
			String configId       = StringUtil.generateUUID();
			String hostName       = getNewCloudHostNameByUserId(userId,region+"");
			myHostName = hostName;
			String cloudHostId    = null;
			Integer processStatus = null;
			String processMessage = null;
			
			 
			logger.info("PaymentServiceImpl.getCloudHost() > ["+Thread.currentThread().getId()+"] 云主机仓库没有合适的库存云主机, sysImageId:["+sysImageId+"], dataDisk:["+dataDisk+"], region:["+region+"]");
			cloudHostId    = StringUtil.generateUUID();
			processStatus  = AppConstant.CLOUD_HOST_SHOPPING_CONFIG_PROCESS_STATUS_NOT_PROCESSED;
		 
			
			// 新增配置
			Map<String, Object> newConfig = new LinkedHashMap<String, Object>();
			newConfig.put("id",             configId);
			newConfig.put("hostId",         cloudHostId);
			newConfig.put("userId",         userId);
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
			
			// 新增配置端口 
			if (ports!=null&& ports.size() > 0&& region==2)
			{ 
				for( int i=0; i<ports.size(); i++ )
				{
					String [] portArray = StringUtil.trim(ports.get(i)).split("&");
					String portName = (String)portArray[0];  
					String protocol = portArray[1]; 
					String port = (String)portArray[2]; 
					if(!StringUtil.isInt(port)){
						continue;
					}
					Map<String, Object> new_port = new LinkedHashMap<String, Object>();
					new_port.put("id",        StringUtil.generateUUID());
					new_port.put("config_id", configId);
					new_port.put("name",      portName);
					new_port.put("protocol",  protocol);
					new_port.put("port",      port);
					cloudHostShoppingPortConfigMapper.inserIntoConfigPort(new_port); 
				}
				Map<String, Object> new_port = new LinkedHashMap<String, Object>();
				new_port.put("id",        StringUtil.generateUUID());
				new_port.put("config_id", configId);
				new_port.put("name",      "FTP");
				new_port.put("protocol",  1);
				new_port.put("port",      21);
				cloudHostShoppingPortConfigMapper.inserIntoConfigPort(new_port); 
				new_port = new LinkedHashMap<String, Object>();
				new_port.put("id",        StringUtil.generateUUID());
				new_port.put("config_id", configId);
				new_port.put("name",      "FTP");
				new_port.put("protocol",  1);
				new_port.put("port",      30000);
				cloudHostShoppingPortConfigMapper.inserIntoConfigPort(new_port); 
				new_port = new LinkedHashMap<String, Object>();
				new_port.put("id",        StringUtil.generateUUID());
				new_port.put("config_id", configId);
				new_port.put("name",      "FTP");
				new_port.put("protocol",  1);
				new_port.put("port",      30001);
				cloudHostShoppingPortConfigMapper.inserIntoConfigPort(new_port); 
 				new_port = new LinkedHashMap<String, Object>();
				new_port.put("id",        StringUtil.generateUUID());
				new_port.put("config_id", configId);
				new_port.put("name",      "FTP");
				new_port.put("protocol",  1);
				new_port.put("port",      30002);
				cloudHostShoppingPortConfigMapper.inserIntoConfigPort(new_port); 
				new_port = new LinkedHashMap<String, Object>();
				new_port.put("id",        StringUtil.generateUUID());
				new_port.put("config_id", configId);
				new_port.put("name",      "FTP");
				new_port.put("protocol",  1);
				new_port.put("port",      30003);
				cloudHostShoppingPortConfigMapper.inserIntoConfigPort(new_port); 
				
			} 
			
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
			cloudHostData.put("type",            AppConstant.CLOUD_HOST_TYPE_3_TERMINAL_USER);                            
			cloudHostData.put("userId",          userId);                                                                   
			cloudHostData.put("hostName",        hostName);                                                                     
			cloudHostData.put("displayName",        displayName);                                                                     
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
				
				
		 
			
			
			if (orderInsertResult > 0 && orderDetailInsertResult > 0 && shoppingConfigInsertResult > 0)
			{
				// 创建成功，返回云主机ID
				logStatus = AppConstant.OPER_LOG_SUCCESS;
				MethodResult result = new MethodResult(MethodResult.SUCCESS, "");
				result.put("cloudHostId", cloudHostId);
				return result; 
			}
			else
			{
				return new MethodResult(MethodResult.FAIL, "创建失败");
			}
			
		}
		catch (Exception e)
		{
			logger.error(e);
			throw new AppException("创建失败");
		}
	}
	/**
	 * 用户购买云主机
	 */
	@Callable
	@Transactional(readOnly = false) 
	public MethodResult getCloudHostForAgentNormal(Map<String, Object> parameter)
	{ 
		String myHostName = "";
		logger.debug("PaymentServiceImpl.getCloudHostForAgentNormal()");
		HttpServletRequest request = RequestContext.getHttpRequest();
		LoginInfo loginInfo;
		if(parameter.get("loginInfo")!=null){			
			 loginInfo = (LoginInfo) parameter.get("loginInfo");
		}else{			 
			 loginInfo = LoginHelper.getLoginInfo(request);
		}
		try
		{
			String userId         = StringUtil.trim(parameter.get("userId"));
			String cpuCore_str    = StringUtil.trim(parameter.get("cpu")); 
			String sysImageId     = StringUtil.trim(parameter.get("sysImageId"));
			String dataDisk_str   = StringUtil.trim(parameter.get("dataDisk"));
			String bandwidth_str  = StringUtil.trim(parameter.get("bandwidth")); 
			String displayName    = StringUtil.trim(parameter.get("displayName"));
			String item           = StringUtil.trim(parameter.get("item")); 
			if(displayName==null||displayName.equals("")){
				
				return new MethodResult(MethodResult.FAIL, "请输入主机名");
			}
			if(dataDisk_str==null||dataDisk_str.equals("")){
				
				dataDisk_str   = StringUtil.trim(parameter.get("dataDiskDIY"));
			}
			dataDisk_str = dataDisk_str.replace("B", "");
			if(bandwidth_str==null||bandwidth_str.equals("")){
				
				bandwidth_str   = StringUtil.trim(parameter.get("bandwidthDIY"));
			} 
			String memory_str     = StringUtil.trim(parameter.get("memory")); 
			Integer region        = StringUtil.parseInteger((String)parameter.get("region"), null); 
			
			String price = "0";
//			String duration = StringUtil.trim(parameter.get("duration"));
			String duration = "0";
			JSONArray ports = (JSONArray)parameter.get("ports");
			
			Integer cpuCore = null;
			BigInteger memory = null;
			BigInteger sysDisk = null;
			BigInteger dataDisk = null;
			BigInteger bandwidth = null; 
			BigDecimal countPrice = null;
			if(region==null||region.equals("")){
				return new MethodResult(MethodResult.FAIL, "请选择地域");
				
			}
			OrderInfoMapper orderInfoMapper                                     = this.sqlSession.getMapper(OrderInfoMapper.class);
			OrderDetailMapper orderDetailMapper                                 = this.sqlSession.getMapper(OrderDetailMapper.class);
			CloudHostShoppingConfigMapper cloudHostShoppingConfigMapper         = this.sqlSession.getMapper(CloudHostShoppingConfigMapper.class);
			CloudHostShoppingPortConfigMapper cloudHostShoppingPortConfigMapper = this.sqlSession.getMapper(CloudHostShoppingPortConfigMapper.class);
			CloudHostMapper cloudHostMapper                                     = this.sqlSession.getMapper(CloudHostMapper.class);
			AgentMapper agentMapper = this.sqlSession.getMapper(AgentMapper.class); 
			TerminalUserMapper terminalUserMapper = this.sqlSession.getMapper(TerminalUserMapper.class); 
			SysDiskImageMapper sysDiskImageMapper = this.sqlSession.getMapper(SysDiskImageMapper.class); 

 			TerminalUserVO terminalUser = terminalUserMapper.getTerminalUserById(userId);
			AgentVO agentVO = agentMapper.getAgentById(loginInfo.getUserId());
			if(item!=null&&!(item.equals(""))){
				PackagePriceMapper packagePriceMapper = this.sqlSession.getMapper(PackagePriceMapper.class);
				List<PriceVO> priceList =  packagePriceMapper.getPriceByInfoId(item);
				PackagePriceVO packagePriceVO = packagePriceMapper.getById(item);
 				if(priceList==null){					
					return new MethodResult(MethodResult.FAIL, "套餐选择有误"); 
				}
  				for(PriceVO priceVo : priceList){
 					if(priceVo.getStatus() == 3){
 						countPrice = priceVo.getMonthlyPrice();
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
				memory = CapacityUtil.fromCapacityLabel(memory_str+"GB");
				if(dataDisk_str==null||dataDisk_str.equals("")){
					return new MethodResult(MethodResult.FAIL, "请选择硬盘");
					
				}  
				dataDisk = CapacityUtil.fromCapacityLabel(dataDisk_str+"GB");
				if(bandwidth_str==null||bandwidth_str.equals("")){
					return new MethodResult(MethodResult.FAIL, "请选择带宽");
					
				} 
				bandwidth   = FlowUtil.fromFlowLabel(bandwidth_str+"Mbps");
				countPrice = CloudHostPrice.getMonthlyPrice(region,3,cpuCore, memory, dataDisk, bandwidth).setScale(2,   BigDecimal.ROUND_HALF_UP);
			}
			
			price = countPrice+"";
			
		//	price =   countPrice.subtract(countPrice.multiply(agentVO.getPercentOff().multiply(new BigDecimal("0.01")))).setScale(2,   BigDecimal.ROUND_HALF_UP)+"";
			sysDisk  = CapacityUtil.fromCapacityLabel("10GB"); 
			
			String orderId = StringUtil.generateUUID();
			
			int orderInsertResult = 0;
			int orderDetailInsertResult = 0;
			int shoppingConfigInsertResult = 0;
			
			String now = StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS");
			
			// 新增订单
			Map<String, Object> newOrder = new LinkedHashMap<String, Object>();
			newOrder.put("id",             orderId);
			newOrder.put("userId",         userId);
			newOrder.put("createTime",     now);
			newOrder.put("totalPrice",     price);
			newOrder.put("isPaid",         AppConstant.ORDER_INFO_IS_PAID_YES);						// 试用的云主机状态默认为已付费
			newOrder.put("processStatus",  AppConstant.ORDER_INFO_PROCESS_STATUS_NOT_PROCESSED);
			newOrder.put("processMessage", "");
			orderInsertResult = orderInfoMapper.addOrderInfo(newOrder);
			
			if (orderInsertResult <= 0)
			{
				throw new AppException("添加订单失败");
			}
			
			String configId       = StringUtil.generateUUID();
			String hostName       = getNewCloudHostNameByUserId(userId,region+"");
			myHostName = hostName;
			String cloudHostId    = null;
			Integer processStatus = null;
			String processMessage = null;
			
			
			logger.info("PaymentServiceImpl.getCloudHost() > ["+Thread.currentThread().getId()+"] 云主机仓库没有合适的库存云主机, sysImageId:["+sysImageId+"], dataDisk:["+dataDisk+"], region:["+region+"]");
			cloudHostId    = StringUtil.generateUUID();
			processStatus  = AppConstant.CLOUD_HOST_SHOPPING_CONFIG_PROCESS_STATUS_NOT_PROCESSED;
			
			
			// 新增配置
			Map<String, Object> newConfig = new LinkedHashMap<String, Object>();
			newConfig.put("id",             configId);
			newConfig.put("hostId",         cloudHostId);
			newConfig.put("userId",         userId);
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
			
			// 新增配置端口 
			if (ports!=null&& ports.size() > 0&& region==2)
			{ 
				for( int i=0; i<ports.size(); i++ )
				{
					String [] portArray = StringUtil.trim(ports.get(i)).split("&");
					String portName = (String)portArray[0];  
					String protocol = portArray[1]; 
					String port = (String)portArray[2]; 
					if(!StringUtil.isInt(port)){
						continue;
					}
					Map<String, Object> new_port = new LinkedHashMap<String, Object>();
					new_port.put("id",        StringUtil.generateUUID());
					new_port.put("config_id", configId);
					new_port.put("name",      portName);
					new_port.put("protocol",  protocol);
					new_port.put("port",      port);
					cloudHostShoppingPortConfigMapper.inserIntoConfigPort(new_port); 
				}
				Map<String, Object> new_port = new LinkedHashMap<String, Object>();
				new_port.put("id",        StringUtil.generateUUID());
				new_port.put("config_id", configId);
				new_port.put("name",      "FTP");
				new_port.put("protocol",  1);
				new_port.put("port",      21);
				cloudHostShoppingPortConfigMapper.inserIntoConfigPort(new_port); 
				new_port = new LinkedHashMap<String, Object>();
				new_port.put("id",        StringUtil.generateUUID());
				new_port.put("config_id", configId);
				new_port.put("name",      "FTP");
				new_port.put("protocol",  1);
				new_port.put("port",      30000);
				cloudHostShoppingPortConfigMapper.inserIntoConfigPort(new_port); 
				new_port = new LinkedHashMap<String, Object>();
				new_port.put("id",        StringUtil.generateUUID());
				new_port.put("config_id", configId);
				new_port.put("name",      "FTP");
				new_port.put("protocol",  1);
				new_port.put("port",      30001);
				cloudHostShoppingPortConfigMapper.inserIntoConfigPort(new_port); 
 				new_port = new LinkedHashMap<String, Object>();
				new_port.put("id",        StringUtil.generateUUID());
				new_port.put("config_id", configId);
				new_port.put("name",      "FTP");
				new_port.put("protocol",  1);
				new_port.put("port",      30002);
				cloudHostShoppingPortConfigMapper.inserIntoConfigPort(new_port); 
				new_port = new LinkedHashMap<String, Object>();
				new_port.put("id",        StringUtil.generateUUID());
				new_port.put("config_id", configId);
				new_port.put("name",      "FTP");
				new_port.put("protocol",  1);
				new_port.put("port",      30003);
				cloudHostShoppingPortConfigMapper.inserIntoConfigPort(new_port); 
				
			} 
			// 新增订单详情
			Map<String, Object> new_order_detail = new LinkedHashMap<String, Object>();
			new_order_detail.put("id",       StringUtil.generateUUID());
			new_order_detail.put("orderId",  orderId);
			new_order_detail.put("itemType", AppConstant.ORDER_DETAIL_ITEM_TYPE_CLOUD_HOST);
			new_order_detail.put("itemId",   configId);
			orderDetailInsertResult = orderDetailMapper.insertIntoOrderDetail(new_order_detail);
			
			
			// 新增云主机信息，创建不需要添加计费记录，到云主机创建成功之后再去创建
			String sysImageName = sysDiskImageMapper.getById(sysImageId).getName();
			Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
			cloudHostData.put("id",              cloudHostId);                                                              
			cloudHostData.put("realHostId",      null);                                                                     
			cloudHostData.put("type",            AppConstant.CLOUD_HOST_TYPE_2_AGENT);                            
			cloudHostData.put("userId",          userId);                                                                   
			cloudHostData.put("hostName",        hostName);                                                                     
			cloudHostData.put("displayName",        displayName);                                                                     
			cloudHostData.put("account",         terminalUser.getAccount());                                                   
			cloudHostData.put("password",        RandomPassword.getRandomPwd(16));                                                   
			cloudHostData.put("cpuCore",         cpuCore);                                                                  
			cloudHostData.put("memory",          memory);                                                                   
			cloudHostData.put("sysImageId",      sysImageId);                                                               
			cloudHostData.put("sysImageName",    sysImageName);                                                               
			cloudHostData.put("sysDisk",         sysDisk);                                                                  
			cloudHostData.put("dataDisk",        dataDisk);                                                                 
			cloudHostData.put("bandwidth",       bandwidth);                                                                
			cloudHostData.put("isAutoStartup",   AppConstant.CLOUD_HOST_IS_AUTO_STARTUP_YES);                                
			cloudHostData.put("runningStatus",   AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN);                           
			cloudHostData.put("status",          AppConstant.CLOUD_HOST_STATUS_1_NORNAL);                                     
			cloudHostData.put("innerIp",         null);                                                                     
			cloudHostData.put("innerPort",       null);                                                                     
			cloudHostData.put("outerIp",         null);                                                                     
			cloudHostData.put("outerPort",       null);  
			cloudHostData.put("region",       region);
			cloudHostData.put("package_id",       item);
			// 计算代理商优惠之后的价格
//			BigDecimal monthlyPrice = CloudHostPrice.getMonthlyPrice(region,cpuCore, memory, dataDisk, bandwidth);
			cloudHostData.put("monthlyPrice",    price);

 			cloudHostMapper.addCloudHost(cloudHostData);
			
			
			
			
			
			if (orderInsertResult > 0 && orderDetailInsertResult > 0 && shoppingConfigInsertResult > 0)
			{
				// 创建成功，返回云主机ID 
				MethodResult result = new MethodResult(MethodResult.SUCCESS, "");
				result.put("cloudHostId", cloudHostId);
				return result; 
			}
			else
			{
				return new MethodResult(MethodResult.FAIL, "创建失败");
			}
			
		}
		catch (AppException e)
		{ 
			logger.error(e);
			throw new AppException("创建失败");
		}
		catch (Exception e)
		{
			logger.error(e);
			throw new AppException("创建失败");
		} 
	}
	public String getNewCloudHostNameByUserId(String userId,String region)
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
//		List<CloudHostVO> userCloudHostList = cloudHostMapper.getByUserId(userId);
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
	
	/**
	 * 加入购物车
	 */
	@Callable
	@Transactional(readOnly = false) 
	public MethodResult addCart(Map<String, String> parameter)
	{
		logger.debug("PaymentServiceImpl.addCart()");
		try
		{
			HttpServletRequest request = RequestContext.getHttpRequest();
			
			 
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			String userId = loginInfo.getUserId(); 	  
			String type = StringUtil.trim(parameter.get("type"));
			String cpuCore    = StringUtil.trim(parameter.get("cpuCore"));
			String memory       = StringUtil.trim(parameter.get("memory"));
			String item       = StringUtil.trim(parameter.get("item"));
			if(memory!=null&&memory.length()>0){
				memory= CapacityUtil.fromCapacityLabel(memory + "GB")+"";
			}
			String sysImageId   = StringUtil.trim(parameter.get("sysImageId"));
			String sysDisk    = StringUtil.trim(parameter.get("sysDisk"));
			if(sysDisk!=null&&sysDisk.length()>0){
				sysDisk= CapacityUtil.fromCapacityLabel(sysDisk + "GB")+""; 
			}
			String dataDisk     = StringUtil.trim(parameter.get("dataDisk"));
			if(dataDisk!=null&&dataDisk.length()>0){
				dataDisk= CapacityUtil.fromCapacityLabel(dataDisk + "GB")+"";  
			}
			String bandwidth      = StringUtil.trim(parameter.get("bandwidth"));
			if(bandwidth!=null&&bandwidth.length()>0){
				bandwidth=(Integer.parseInt(bandwidth)*1000000)+"";
			}
			String startTime      = StringUtil.trim(parameter.get("startTime"));
			String endTime      = StringUtil.trim(parameter.get("endTime"));
			String price      = StringUtil.trim(parameter.get("price")); 
			String duration = StringUtil.trim(parameter.get("duration")); 
			String ports = StringUtil.trim(parameter.get("ports")); 
			String name = StringUtil.trim(parameter.get("name"));
			if(duration==null||duration.length()==0){
				duration="0";
			}
			if("3".equals(type)){
				if(item==null||item==""){					
					return new MethodResult(MethodResult.FAIL, "请选择试用套餐");
				}else{
					if(item.equals("1")){
						cpuCore = "2";
						memory= CapacityUtil.fromCapacityLabel(2 + "GB")+"";
						sysDisk= CapacityUtil.fromCapacityLabel(100 + "GB")+"";
						bandwidth=2000000+""; 
					}else if(item.equals("2")){
						cpuCore = "4";
						memory= CapacityUtil.fromCapacityLabel(4 + "GB")+"";
						sysDisk= CapacityUtil.fromCapacityLabel(500 + "GB")+"";
						bandwidth=2000000+""; 						
					}else if(item.equals("3")){
						cpuCore = "4";
						memory= CapacityUtil.fromCapacityLabel(8 + "GB")+"";
						sysDisk= CapacityUtil.fromCapacityLabel(500 + "GB")+"";
						bandwidth=2000000+""; 						
					} 
					dataDisk = "0";
					duration = "1";
				}
				
			}
			String id         = StringUtil.generateUUID();
			String cartId = id; 
			
			// 判断购物车是否已经存在
			ShoppingCartMapper shoppingCartMapper = this.sqlSession.getMapper(ShoppingCartMapper.class);
			ShoppingCartVO cart = (ShoppingCartVO)shoppingCartMapper.getCartByUserId(userId);
			int a;
			if(cart==null){
				Map<String, Object> new_cart = new LinkedHashMap<String, Object>();
				new_cart.put("id",id);  
				new_cart.put("userId",userId);  
				new_cart.put("createTime",formatter.format(new Date()));  
				new_cart.put("totalPrice",price);  
				a = shoppingCartMapper.inserIntoCart(new_cart);				
			}else{
				Map<String, Object> new_price = new LinkedHashMap<String, Object>();
				new_price.put("id", cart.getId());
				new_price.put("price", price);
				cartId = cart.getId();
				a = shoppingCartMapper.updateCart(new_price);
			}
			Map<String, Object> new_cart_detail = new LinkedHashMap<String, Object>();
			new_cart_detail.put("id",id);  
			new_cart_detail.put("cartId",cartId);  
			new_cart_detail.put("itemType","1");  
			new_cart_detail.put("itemId",id);  
			int b = shoppingCartMapper.inserIntoCartDetail(new_cart_detail);
			Map<String, Object> new_config = new LinkedHashMap<String, Object>();
			new_config.put("id",id);  
			new_config.put("userId",userId);  
			new_config.put("type",type);  
			new_config.put("cpuCore",cpuCore);  
			new_config.put("memory",memory);  
			new_config.put("sysImageId",sysImageId);  
			new_config.put("sysDisk",sysDisk);  
			new_config.put("dataDisk",dataDisk);  
			new_config.put("bandwidth",bandwidth);  
			new_config.put("startTime",startTime);  
			new_config.put("endTime",endTime);  
			new_config.put("price",price);  
			new_config.put("createTime",formatter.format(new Date()));   
			new_config.put("duration",duration);   
			new_config.put("name",name);   
			int c = shoppingCartMapper.inserIntoConfig(new_config);
			if(c>0&&ports!=null&&ports.length()>0){
				String [] port = ports.split(",");
				for(int i=0;i<port.length;i++){
					Map<String, Object> new_port = new LinkedHashMap<String, Object>(); 
					new_port.put("id", StringUtil.generateUUID());
					new_port.put("config_id", id);
					new_port.put("port", port[i]);
					shoppingCartMapper.inserIntoConfigPort(new_port);
				}
				
			}
			 
			if(a > 0 && b > 0 && c >0 )
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
	
	/**
	 * 删除购物车中的物品
	 */
	@Callable
	@Transactional(readOnly = false) 
	public MethodResult deleteDetailAndConfig(Map<String, String> parameter)
	{
		logger.debug("PaymentServiceImpl.deleteDetailAndConfig()");
		try
		{ 
	   
			String cartId    = StringUtil.trim(parameter.get("cartId"));
			String configIds    = StringUtil.trim(parameter.get("ids"));
			String price    ="-"+StringUtil.trim(parameter.get("detailPrice"));
			String userId    =StringUtil.trim(parameter.get("userId"));
			 
			
			 
			ShoppingCartMapper shoppingCartMapper = this.sqlSession.getMapper(ShoppingCartMapper.class);
			String [] ids = configIds.split(",");
			int a = shoppingCartMapper.deleteConfigById(ids);
			int b = shoppingCartMapper.deleteDetailByItemId(ids);
			int d = shoppingCartMapper.deletePortByIds(ids);
			int c = 0;
			Map<String, Object> new_price = new LinkedHashMap<String, Object>();
			if(a>0&b>0){				
				new_price.put("id", cartId);
				new_price.put("price", price); 
				 c = shoppingCartMapper.updateCart(new_price);  
			}
			List<ShoppingCartVO> detail = shoppingCartMapper.getCartDetailByCartId(cartId);
			if(detail==null||detail.size()==0){
				shoppingCartMapper.deleteCartDetail(userId);
				shoppingCartMapper.deleteAllCart(userId);
			}
			if(a > 0 && b > 0 && c > 0)
			{   
				
				return new MethodResult(MethodResult.SUCCESS, "刪除成功");
			}
			else
			{
				return new MethodResult(MethodResult.FAIL, "刪除失败");
			}
			
		}
		catch( Exception e )
		{ 
			logger.error(e);
			throw new AppException("删除失败");
		}
	}
	/**
	 * 跳转到结算页面
	 */
	@Callable
	@Transactional(readOnly = false) 
	public String toAccountPage(HttpServletRequest request,
			HttpServletResponse response) {
 		logger.debug("PaymentServiceImpl.toAccountPage()");
 		String returnUrl = "/security/user/pay.jsp";
		 
		//选中的配置项进行结算 
 		String userType = request.getParameter("userType");
 		String type = (String) request.getParameter("payType");
 		String orderId = "";
 		String totalFee = "";
 		if("3".equals(userType)){
 			
 			LoginInfo loginInfo = LoginHelper.getLoginInfo(request, AppConstant.SYS_USER_TYPE_AGENT);
 			totalFee = request.getParameter("total_fee"); 
 			String totalFee_diy = request.getParameter("total_fee_diy");
 			if(StringUtil.isBlank(totalFee)){
 				totalFee = totalFee_diy;
 			}
 			request.setAttribute("email", request.getParameter("email"));
 			request.setAttribute("userId", request.getParameter("userId"));
 			request.setAttribute("notifyUrl", AppProperties.getValue("address_of_this_system", "")+"public/user/notify_url.jsp");
 			orderId = StringUtil.generateUUID();
  			 
 			AccountBalanceDetailMapper accountBalanceDetailMapper = this.sqlSession.getMapper(AccountBalanceDetailMapper.class);  
 			Map<String, Object> balanceDetailData = new LinkedHashMap<String, Object>(); 
 			balanceDetailData.put("id",     orderId); 
 			balanceDetailData.put("amount",     totalFee); 
 			balanceDetailData.put("giftAmount",     0); 
 			balanceDetailData.put("userId",     loginInfo.getUserId());  
 			balanceDetailData.put("rechargeStatus",     "1");  
 			balanceDetailData.put("type",     "1");  
 			balanceDetailData.put("payType",     type);  
 			balanceDetailData.put("description",     "充值");  
 			int m = accountBalanceDetailMapper.addAccountBalanceDetail(balanceDetailData);
 			request.setAttribute("orderId", orderId);
 			request.setAttribute("totalFee", totalFee);
 		}else{
 			
 			LoginInfo loginInfo = LoginHelper.getLoginInfo(request, AppConstant.SYS_USER_TYPE_TERMINAL_USER);
 			totalFee = request.getParameter("total_fee");
 			String totalFee_diy = request.getParameter("total_fee_diy");
 			if(StringUtil.isBlank(totalFee)){
 				totalFee = totalFee_diy;
 			}
 			request.setAttribute("email", request.getParameter("email")); 
 			request.setAttribute("email", request.getParameter("email"));
 			request.setAttribute("userId", request.getParameter("userId"));
 			request.setAttribute("notifyUrl", AppProperties.getValue("address_of_this_system", "")+"public/user/notify_url.jsp");
  			orderId = StringUtil.generateUUID();
 			String month = (String) request.getParameter("month");
 			String giftAmount = "0";
 			if(month!=null&&!(month.equals(""))){
 				
 				CloudHostMapper  cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class); 
 				
 				List<CloudHostVO> cloudHostList = cloudHostMapper.getByUserId(loginInfo.getUserId());
 				BigDecimal totalPrice = new BigDecimal("0");
 				if(cloudHostList!=null&&cloudHostList.size()>0){
 					for(CloudHostVO vo:cloudHostList){
 						if(!(vo.getSummarizedStatusText().equals("创建失败")||vo.getSummarizedStatusText().equals("停机"))&&vo.getMonthlyPrice()!=null){						
 							totalPrice = vo.getMonthlyPrice().add(totalPrice);
 						}
 					} 
 					
 				}
 				int m = Integer.parseInt(request.getParameter("month")); 
 				request.setAttribute("month", month); 
 				int month_pay = (m - (m/12)*2); 
 				int month_gift = (m/12)*2; 
 				totalFee = totalPrice.multiply(new BigDecimal(""+month_pay)).setScale(2, BigDecimal.ROUND_HALF_UP)+"";
 				giftAmount = totalPrice.multiply(new BigDecimal(""+month_gift)).setScale(2, BigDecimal.ROUND_HALF_UP)+"";
 			}else{
 				int i = Integer.parseInt(totalFee)/1000;  
 				if(i>=0){
 					giftAmount = (i*200)+"";
 				}
 			} 
 			AccountBalanceDetailMapper accountBalanceDetailMapper = this.sqlSession.getMapper(AccountBalanceDetailMapper.class);  
 			Map<String, Object> balanceDetailData = new LinkedHashMap<String, Object>(); 
 			balanceDetailData.put("id",     orderId); 
 			balanceDetailData.put("amount",     totalFee); 
 			balanceDetailData.put("giftAmount",     giftAmount); 
 			balanceDetailData.put("userId",     loginInfo.getUserId());  
 			balanceDetailData.put("rechargeStatus",     "1");  
 			balanceDetailData.put("type",     "1");  
 			balanceDetailData.put("payType",     type);  
 			balanceDetailData.put("description",     "充值");  
 			int m = accountBalanceDetailMapper.addAccountBalanceDetail(balanceDetailData);
 			request.setAttribute("orderId", orderId);
 			request.setAttribute("totalFee", totalFee);
 		}
		if("1".equals(type)){
			return "/security/user/alipayapi.jsp";
			
//		}else{
//			try {
//				response.sendRedirect(AppProperties.getValue("address_of_recharge", "")+"public/user/unionpayForRjy.jsp?orderId="+orderId+"&totalFee="+totalFee);
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			return "/security/user/alipayapi.jsp"; 
//			
//		} 
		}else{ 
			return "/security/user/unionpayapi.jsp"; 
			
		} 
	}
	/**
	 * 跳转到结算页面
	 */
	@Callable
	@CallWithoutLogin
	@Transactional(readOnly = false) 
	public void toAccountPageForSoftWarePark(HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("PaymentServiceImpl.toAccountPageForSoftWarePark()"); 
		
		//选中的配置项进行结算 
		String orderId = request.getParameter("orderId"); 
		AccountBalanceDetailMapper accountBalanceDetailMapper = this.sqlSession.getMapper(AccountBalanceDetailMapper.class); 
		AccountBalanceDetailVO order = accountBalanceDetailMapper.getBalanceDetailById(orderId);
		//订单存在、是充值订单、状态是未付款
		if(order == null || order.getType() !=1 || order.getRechargeStatus() !=1 ){ 
			try {
				response.sendRedirect("https://www.zhicloud.com/public/page_not_exists.jsp");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		request.setAttribute("orderId", order.getId());
		request.setAttribute("totalFee", order.getAmount().toString()); 
		try { 
			response.sendRedirect(request.getContextPath()+"/public/user/unionpayForRjy.jsp?orderId="+order.getId()+"&totalFee="+order.getAmount().toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		if("1".equals(order.getPayType())){
//			return "/security/user/alipayapi.jsp";
//			
//		}else{
//			return "/security/user/unionpayapi.jsp";
//			
//		} 
	}
	/**
	 * 根据支付宝返回信息修改订单状态
	 */
	@Callable
	@CallWithoutLogin
	@Transactional(readOnly = false) 
	public String updateOrder(String id) {
		int a = 0;
		try{
			UserOrderMapper userOrderMapper = this.sqlSession.getMapper(UserOrderMapper.class);
			// 修改订单状态
			a = userOrderMapper.updateOrder(id);	
			// 根据订单信息生成发票信息 
			
		}catch(Exception e){
			e.printStackTrace();
		}
		if(a>0){
			return "success";
		}else{
			return "failure";
		}
	}
	@Callable 
	public String managePage(HttpServletRequest request,
			HttpServletResponse response) {
		logger.debug("PaymentServiceImpl.managePage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request, 2);
		if( loginInfo.hasPrivilege(PrivilegeConstant.order_cloud_host_manage_page)==false )
		{
			return "/public/have_not_access.jsp";
		}
		
		return "/security/operator/order_cloud_host_manage.jsp";
	} 
	@Callable 
	public void queryOrderConfig(HttpServletRequest request,
			HttpServletResponse response) {
		logger.debug("PaymentServiceImpl.queryOrderConfig()");
		try
		{
			// 权限判断
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request, 2);
			if( loginInfo.hasPrivilege(PrivilegeConstant.order_cloud_host_manage_page)==false )
			{
				throw new AppException("您没有权限进行此操作");
			}
			
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");

			// 获取参数 
			String processStatus = request.getParameter("processStatus");
			String account = request.getParameter("account");
			Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
			Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 2);

			// 查询数据库 
			UserOrderMapper userOrderMapper = this.sqlSession.getMapper(UserOrderMapper.class);
			
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			if(processStatus!=null&&processStatus!=""){				
				condition.put("processStatus",  processStatus );
			}
			if(account!=null&&account!=""){				
				condition.put("account", "%"+account+"%");
			}
			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			int total = userOrderMapper.queryOrderConfigPageCount(condition); // 总行数
			List<UserOrderVO> configList =userOrderMapper.queryOrderConfigPage(condition);// 分页结果

			// 输出json数据 
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, configList);
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("查询失败");
		}
	} 
	@Callable 
	public String toManageModifyPage(HttpServletRequest request,
			HttpServletResponse response) { 
		logger.debug("PaymentServiceImpl.toManageModifyPage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.hasPrivilege(PrivilegeConstant.order_cloud_host_manage_page)==false )
		{
			return "/public/have_not_access.jsp";
		}
		
		request.setAttribute("id", request.getParameter("id"));
		UserOrderMapper userOrderMapper = this.sqlSession.getMapper(UserOrderMapper.class); 
		UserOrderVO detail = userOrderMapper.getOrderConfigById(request.getParameter("id"));
		request.setAttribute("detail", detail); 
		return "/security/operator/order_cloud_host_manage_modify.jsp";
	}
	@Callable 
	public String toManageDetailPage(HttpServletRequest request,
			HttpServletResponse response) { 
		String id = request.getParameter("id");
		logger.debug("PaymentServiceImpl.toManageDetailPage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.hasPrivilege(PrivilegeConstant.order_cloud_host_manage_page)==false )
		{
			return "/public/have_not_access.jsp";
		}
		UserOrderMapper userOrderMapper = this.sqlSession.getMapper(UserOrderMapper.class); 
		UserOrderVO detail = userOrderMapper.getOrderConfigById(id);
		request.setAttribute("detail", detail); 
		return "/security/operator/order_cloud_host_manage_detail.jsp";
	}
	/**
	 * 
	 */
	@Callable
	@Transactional(readOnly=false)
	public MethodResult addRealHost(Map<String, String> parameter)
	{
		logger.debug("SysRoleServiceImpl.addRealHost()");
		try
		{
			String id = StringUtil.trim(parameter.get("id"));
			String realId = StringUtil.trim(parameter.get("realId"));  
			String isHostCreated = StringUtil.trim(parameter.get("isHostCreated"));  
			// 判断主机名是否已经存在
			UserOrderMapper userOrderMapper = this.sqlSession.getMapper(UserOrderMapper.class);
			UserOrderVO config = userOrderMapper.getOrderConfigById(id);
			if(config==null){
				return new MethodResult(MethodResult.FAIL, "云主机不存在");
				
			}  
			if(realId==null||realId.equals("")){
				return new MethodResult(MethodResult.FAIL, "真实云主机ID不能为空！");
			}
			if(realId.length() > 32){
				return new MethodResult(MethodResult.FAIL, "真实云主机ID长度不能超过32个字符！");
			}
			CloudHostMapper cloudHostMapper = this.sqlSession.getMapper(CloudHostMapper.class); 
			// 添加云主机进数据库
			String hostId = StringUtil.generateUUID();
			Map<String, Object> cloudHostData = new LinkedHashMap<String, Object>();
			cloudHostData.put("id",            hostId);
			cloudHostData.put("type",          AppConstant.CLOUD_HOST_TYPE_3_TERMINAL_USER);
			cloudHostData.put("userId",        config.getUserId());
			cloudHostData.put("hostName",      config.getHostName());
			cloudHostData.put("realHostId",    realId);
			cloudHostData.put("cpuCore",       config.getCpuCore());
			cloudHostData.put("memory",        config.getMemory());
			cloudHostData.put("sysImageId",    config.getSysImageId());
			cloudHostData.put("sysImageName",  config.getImageName());
			cloudHostData.put("sysDisk",       config.getSysDisk());
			cloudHostData.put("dataDisk",      config.getDataDisk());
			cloudHostData.put("bandwidth",     new BigInteger("9999999999"));
			cloudHostData.put("isAutoStartup", AppConstant.CLOUD_HOST_IS_AUTO_STARTUP_NO );
			cloudHostData.put("runningStatus", AppConstant.CLOUD_HOST_RUNNING_STATUS_SHUTDOWN);
			cloudHostData.put("status",        AppConstant.CLOUD_HOST_STATUS_1_NORNAL);
			
			int a = cloudHostMapper.addCloudHost(cloudHostData);
			if(a>0){
				List<UserOrderVO> ports = userOrderMapper.getPortsByConfigId(id);
				if(ports!=null&&ports.size()>0){
					// 添加开放端口
					CloudHostOpenPortMapper cloudHostOpenPortMapper = this.sqlSession.getMapper(CloudHostOpenPortMapper.class);
					for( UserOrderVO port : ports )
					{
						Map<String, Object> cloudHostOpenPort = new LinkedHashMap<String, Object>();
						cloudHostOpenPort.put("id",     StringUtil.generateUUID());
						cloudHostOpenPort.put("hostId", hostId);
						cloudHostOpenPort.put("port",   Integer.parseInt(port.getPort()));
						cloudHostOpenPortMapper.addCloudHostOpenPort(cloudHostOpenPort);
					} 
				}
			}
			Map<String, Object> configData = new LinkedHashMap<String, Object>(); 
			configData.put("id",       id);
			configData.put("hostId", realId);
			configData.put("processStatus", "1");
			int b = userOrderMapper.updateOrderConfig(configData);
			if( a>0&&b>0 )
			{
				return new MethodResult(MethodResult.SUCCESS, "添加成功");
			}
			else
			{
				return new MethodResult(MethodResult.SUCCESS, "添加失败");
			}
			
		}
		catch (Exception e)
		{
			logger.error(e);
			return new MethodResult(MethodResult.FAIL, "添加失败");
			//throw new AppException(e);
		}
	}
	
	
	/**
	 * 结算前检查
	 */
	@Callable 
	public MethodResult checkOrderTrail(Map<String, String> parameter)
	{
		logger.debug("PaymentServiceImpl.checkOrderTrail()");
		try
		{
			HttpServletRequest request = RequestContext.getHttpRequest();
			
			 
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			String userId = loginInfo.getUserId(); 	  
			UserOrderMapper userOrderMapper = this.sqlSession.getMapper(UserOrderMapper.class);
			List<UserOrderVO> vo = userOrderMapper.getTrailOrderConfigByUserId(userId);
			 
			if(vo!=null&&vo.size()>0 )
			{   
				
				return new MethodResult(MethodResult.FAIL, "已存在试用，不能结算");
			}
			else
			{
				return new MethodResult(MethodResult.SUCCESS, "不存在试用");
			}

		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("失败");
		}
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
	
	
	

} 

