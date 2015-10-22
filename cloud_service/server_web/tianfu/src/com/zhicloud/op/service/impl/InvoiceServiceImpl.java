package com.zhicloud.op.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.op.app.authorization.PrivilegeConstant;
import com.zhicloud.op.app.helper.LoginHelper;
import com.zhicloud.op.app.helper.ServiceHelper;
import com.zhicloud.op.app.propeties.AppProperties;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.login.LoginInfo;
import com.zhicloud.op.mybatis.mapper.AccountBalanceDetailMapper;
import com.zhicloud.op.mybatis.mapper.InvoiceAddressMapper;
import com.zhicloud.op.mybatis.mapper.InvoiceMapper;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.service.InvoiceService;
import com.zhicloud.op.service.constant.AppConstant;
import com.zhicloud.op.vo.AccountBalanceDetailVO;
import com.zhicloud.op.vo.InvoiceAddressVO;
import com.zhicloud.op.vo.InvoiceVO;

@Transactional(readOnly = true)
public class InvoiceServiceImpl extends BeanDirectCallableDefaultImpl implements InvoiceService {
	
	public static final Logger logger = Logger.getLogger(OperatorServiceImpl.class);

	private SqlSession sqlSession;
	
	public void setSqlSession(SqlSession sqlSession)
	{
		this.sqlSession = sqlSession;
	}

	@Callable
	public String managePage(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("InvoiceServiceImpl.managePage()");
		Integer userType = Integer.parseInt(request.getParameter("userType"));
		if(userType == AppConstant.SYS_USER_TYPE_TERMINAL_USER){			
			return "/security/user/invoice_manage.jsp";
		}else{
			return "/security/agent/invoice_manage.jsp";
			
		}
	}
	
	@Callable
	public String managePageForAgent(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("InvoiceServiceImpl.managePageForAgent()");
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.hasPrivilege(PrivilegeConstant.invoice_manage_agent) == false )
		{
			return "/public/have_not_access.jsp";
		}
		
		return "/security/agent/agent_invoice_manage.jsp";
	}
	
	@Callable
	public String pendingPage(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("InvoiceServiceImpl.pendingPage()");
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.hasPrivilege(PrivilegeConstant.invoice_manage_page_operator) == false )
		{
			return "/public/have_not_access.jsp";
		}
		
		return "/security/operator/invoice_pending_manage.jsp";
	}
	
	@Callable
	public String exportedPage(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("InvoiceServiceImpl.exportedPage()");
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.hasPrivilege(PrivilegeConstant.invoice_manage_page_operator) == false )
		{
			return "/public/have_not_access.jsp";
		}
		return "/security/operator/invoice_exported_manage.jsp";
	}
	
	@Callable
	public String sentPage(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("InvoiceServiceImpl.sentPage()");
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.hasPrivilege(PrivilegeConstant.invoice_manage_page_operator) == false )
		{
			return "/public/have_not_access.jsp";
		}
		return "/security/operator/invoice_sent_manage.jsp";
	}
	
	@Callable
	public String sentSuccessPage(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("InvoiceServiceImpl.sentSuccessPage()");
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.hasPrivilege(PrivilegeConstant.invoice_manage_page_operator) == false )
		{
			return "/public/have_not_access.jsp";
		}
		return "/security/operator/invoice_manage.jsp";
	}
	
	@Callable
	public String settingPage(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("InvoiceServiceImpl.settingPage()");
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.hasPrivilege(PrivilegeConstant.invoice_manage_page_operator) == false )
		{
			return "/public/have_not_access.jsp";
		}
		return "/security/operator/invoice_setting.jsp";
	}
	
	@Callable
	public String addInvoicePage(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("InvoiceServiceImpl.addInvoicePage()");
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
//		if( AppConstant.SYS_USER_TYPE_TERMINAL_USER.equals(loginInfo.getUserType())==false ){
//			return "/public/have_not_access.jsp";
//		}
		//参数处理
		String userId="";
		loginInfo = LoginHelper.getLoginInfo(request);
		if(loginInfo!=null){
			userId = loginInfo.getUserId();
		}
		AccountBalanceDetailMapper accountBalanceDetailMapper = this.sqlSession.getMapper(AccountBalanceDetailMapper.class);
		Map<String, Object> condition = new LinkedHashMap<String, Object>();
		condition.put("userId", userId);
		List<AccountBalanceDetailVO> accountBalanceDetailList = accountBalanceDetailMapper.getBalanceDetailByUserIdForInvoice(condition);
		int total = accountBalanceDetailMapper.getBalanceDetailByUserIdCountForInvoice(condition);
		request.setAttribute("account_balance_detail_list", accountBalanceDetailList);
		request.setAttribute("total", total);
		InvoiceAddressMapper invoiceAddressMapper = this.sqlSession.getMapper(InvoiceAddressMapper.class);
		List<InvoiceAddressVO> invoiceAddressList = invoiceAddressMapper.getInvoiceAddress(userId);
		request.setAttribute("invoice_address_list", invoiceAddressList);
		if(loginInfo.getUserType()==AppConstant.SYS_USER_TYPE_TERMINAL_USER){			
			return "/security/user/invoice_add.jsp";
		}else{
			return "/security/agent/invoice_add.jsp";			
		}
	}
	
	@Callable
	public String addInvoicePageForAgent(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("InvoiceServiceImpl.addInvoicePageForAgent()");
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.hasPrivilege(PrivilegeConstant.invoice_manage_agent) == false )
		{
			return "/public/have_not_access.jsp";
		}
		//参数处理
		String userId="";
		if(loginInfo!=null){
			userId = loginInfo.getUserId();
		}
		AccountBalanceDetailMapper accountBalanceDetailMapper = this.sqlSession.getMapper(AccountBalanceDetailMapper.class);
		Map<String, Object> condition = new LinkedHashMap<String, Object>();
		condition.put("userId", userId);
		List<AccountBalanceDetailVO> accountBalanceDetailList = accountBalanceDetailMapper.getBalanceDetailByUserIdForInvoice(condition);
		int total = accountBalanceDetailMapper.getBalanceDetailByUserIdCountForInvoice(condition);
		request.setAttribute("account_balance_detail_list", accountBalanceDetailList);
		request.setAttribute("total", total);
		InvoiceAddressMapper invoiceAddressMapper = this.sqlSession.getMapper(InvoiceAddressMapper.class);
		List<InvoiceAddressVO> invoiceAddressList = invoiceAddressMapper.getInvoiceAddress(userId);
		request.setAttribute("invoice_address_list", invoiceAddressList);
		return "/security/agent/agent_invoice_add.jsp";
	}

	@Callable
	public void queryInvoice(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("InvoiceServiceImpl.queryInvoice()");
		
		try
		{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");
			
			// 获取参数
			String userId = request.getParameter("user_id");
			Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
			Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 10);

			// 查询数据库
			InvoiceMapper invoiceMapper = this.sqlSession.getMapper(InvoiceMapper.class);
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("userId", userId);
			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			int total = invoiceMapper.queryPageCount(condition); // 总行数
			List<InvoiceVO> invoiceList = invoiceMapper.queryPage(condition);// 分页结果
			
			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, invoiceList);
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("查询失败");
		}
	}
	
	@Callable
	public void queryAllInvoice(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("InvoiceServiceImpl.queryAllInvoice()");
		
		try
		{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");
			
			// 获取参数
			String status = StringUtil.trim(request.getParameter("status"));
			String invoiceTitle = StringUtil.trim(request.getParameter("invoiceTitle"));
			Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
			Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 10);
			

			// 查询数据库
			InvoiceMapper invoiceMapper = this.sqlSession.getMapper(InvoiceMapper.class);
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("invoiceTitle", "%"+invoiceTitle+"%");
			condition.put("status", status);
			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			
//			System.out.println("---"+invoiceTitle+"--");

			int total = invoiceMapper.queryAllPageCount(condition); // 总行数
			List<InvoiceVO> invoiceList = invoiceMapper.queryAllPage(condition);// 分页结果
			
			
			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, invoiceList);
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("查询失败");
		}
	}

	@Callable
	@Transactional(readOnly = false)
	public MethodResult addInvoice(Map<String, String> parameter) {
		logger.debug("InvoiceServiceImpl.addInvoice()");
		try
		{
			Pattern pattern = Pattern.compile("[\\[,\\]|]"); 
			String[] ids = pattern.split(StringUtil.trim(parameter.get("ids")));
			BigDecimal  totalAamount = new BigDecimal (StringUtil.trim(parameter.get("amount")));
			String userId = StringUtil.trim(parameter.get("userId"));
			String addressId = StringUtil.trim(parameter.get("id"));
			String invoiceTitle  = StringUtil.trim(parameter.get("invoiceTitle"));
			String address  = StringUtil.trim(parameter.get("address"));
			String recipients = StringUtil.trim(parameter.get("recipients"));
			String phone = StringUtil.trim(parameter.get("phone"));
			
			int invoiceAddressInsertResult = 0;
			int accountBalanceDetailUpdateResult = 0;
			int invoiceInsertResult = 0;
			
			
			InvoiceAddressMapper invoiceAddressMapper = this.sqlSession.getMapper(InvoiceAddressMapper.class);
			//验证地址是否已存在
			InvoiceAddressVO invoiceAddressVO = invoiceAddressMapper.getInvoiceAddressById(addressId);
			//新增地址
			if(invoiceAddressVO == null){
				Map<String, Object> data = new LinkedHashMap<String, Object>();
				data.put("id", StringUtil.generateUUID());
				data.put("userId", userId);
				data.put("invoiceTitle", invoiceTitle);
				data.put("address", address);
				data.put("recipients", recipients);
				data.put("phone", phone);
				invoiceAddressInsertResult = invoiceAddressMapper.addInvoiceAddress(data);
				if(invoiceAddressInsertResult <= 0 ) {
					return new MethodResult(MethodResult.FAIL, "提交失败");
				}
			}
			
			String invoiceId = StringUtil.generateUUID();
			
			AccountBalanceDetailMapper accountBalanceDetailMapper = this.sqlSession.getMapper(AccountBalanceDetailMapper.class);

			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			for(int i=1; i<ids.length; i ++) {
				String accountBalacneDetailId = ids[i].split("\"")[1];
				condition.put("invoiceId", invoiceId);
				condition.put("id", accountBalacneDetailId);
				 accountBalanceDetailUpdateResult = accountBalanceDetailMapper.updateInvoiceId(condition);
				
			}

			InvoiceMapper invoiceMapper = this.sqlSession.getMapper(InvoiceMapper.class);
			
			Map<String, Object> data = new LinkedHashMap<String, Object>();
			data.put("id", invoiceId);
			data.put("userId", userId);
			data.put("totalAmount", totalAamount);
			data.put("invoiceTitle", invoiceTitle);
			data.put("address", address);
			data.put("recipients", recipients);
			data.put("phone", phone);
			data.put("status", AppConstant.INVOICE_IS_PENDING);
			data.put("submitTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
			
			invoiceInsertResult = invoiceMapper.addInvoice(data);
			
			if(accountBalanceDetailUpdateResult > 0 && invoiceInsertResult>0) {
				return new MethodResult(MethodResult.SUCCESS, "提交成功");
			}

			return new MethodResult(MethodResult.FAIL, "提交失败");
		}catch( Exception e )
		{
			logger.error(e);
			throw new AppException("提交失败");
		}

	}
	
	@Callable
	public MethodResult saveSetting(Map<String, String> parameter)
	{
		logger.debug("InvoiceServiceImpl.saveSetting()");
		try
		{
			String invoiceType      = StringUtil.trim(parameter.get("invoice_type"));
			String invoiceDrawer  = StringUtil.trim(parameter.get("invoice_drawer"));
			String invoicePayee  = StringUtil.trim(parameter.get("invoice_payee"));
			String invoiceSubject  = StringUtil.trim(parameter.get("invoice_subject"));
			
			
			// 判断格式
			if( StringUtil.isBlank(invoiceType) )
			{
				return new MethodResult(MethodResult.FAIL, "行业类别不能为空");
			}
			if( StringUtil.isBlank(invoiceDrawer) )
			{
				return new MethodResult(MethodResult.FAIL, "开票人不能为空");
			}
			if( StringUtil.isBlank(invoicePayee) )
			{
				return new MethodResult(MethodResult.FAIL, "收款人不能为空");
			}
			if( StringUtil.isBlank(invoiceSubject) )
			{
				return new MethodResult(MethodResult.FAIL, "项目名称不能为空");
			}
			
			AppProperties.setValue("invoice_type",      invoiceType);
			AppProperties.setValue("invoice_drawer", invoiceDrawer);
			AppProperties.setValue("invoice_payee", invoicePayee);
			AppProperties.setValue("invoice_subject", invoiceSubject);
			AppProperties.save();
			
			return new MethodResult(MethodResult.SUCCESS, "保存成功");
		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("保存失败");
		}
	}

	@Callable
	@Transactional(readOnly = false)
	public MethodResult exportInvoiceByIds(List<String> ids) {
		logger.debug("InvoiceServiceImpl.exportInvoiceByIds()");
		try
		{
			if( ids == null || ids.size() == 0 )
			{
				return new MethodResult(MethodResult.FAIL, "ids不能为空");
			}
			
			InvoiceMapper invoiceMapper = this.sqlSession.getMapper(InvoiceMapper.class);
			
			for(String id : ids){
				LinkedHashMap<String, Object> condition = new LinkedHashMap<String, Object>();
				condition.put("id", id);
				condition.put("status", AppConstant.INVOICE_IS_EXPORTED);
				invoiceMapper.updateInvoiceStatus(condition);
			}
			return new MethodResult(MethodResult.SUCCESS, "提交成功");
		}catch( Exception e )
		{
			logger.error(e);
			throw new AppException("提交失败");
		}
	}

	@Callable
	@Transactional(readOnly = false)
	public MethodResult sendInvoiceByIds(List<String> ids) {
		logger.debug("InvoiceServiceImpl.sendInvoiceByIds()");
		try
		{
			if( ids == null || ids.size() == 0 )
			{
				return new MethodResult(MethodResult.FAIL, "ids不能为空");
			}
			InvoiceMapper invoiceMapper = this.sqlSession.getMapper(InvoiceMapper.class);
			for(String id : ids){
				LinkedHashMap<String, Object> condition = new LinkedHashMap<String, Object>();
				condition.put("id", id);
				condition.put("status", AppConstant.INVOICE_IS_SENDING);
				invoiceMapper.updateInvoiceStatus(condition);
			}
			return new MethodResult(MethodResult.SUCCESS, "提交成功");
		}catch( Exception e )
		{
			logger.error(e);
			throw new AppException("提交失败");
		}
	}

	@Callable
	@Transactional(readOnly = false)
	public MethodResult confirmInvoiceByIds(List<String> ids) {
		logger.debug("InvoiceServiceImpl.confirmInvoiceByIds()");
		try
		{
			if( ids == null || ids.size() == 0 )
			{
				return new MethodResult(MethodResult.FAIL, "ids不能为空");
			}
			InvoiceMapper invoiceMapper = this.sqlSession.getMapper(InvoiceMapper.class);
			for(String id : ids){
				LinkedHashMap<String, Object> condition = new LinkedHashMap<String, Object>();
				condition.put("id", id);
				condition.put("status", AppConstant.INVOICE_IS_SENT_SUCCESS);
				invoiceMapper.updateInvoiceStatus(condition);
			}
			return new MethodResult(MethodResult.SUCCESS, "提交成功");
		}catch( Exception e )
		{
			logger.error(e);
			throw new AppException("提交失败");
		}
	}

	@Callable
	public List<InvoiceVO> queryInvoiceByIds(String[] ids) {
		logger.debug("InvoiceServiceImpl.queryInvoiceByIds()");
		try{
			if( ids == null || ids.length == 0 )
			{
				throw new AppException( "ids不能为空");
			}
			InvoiceMapper invoiceMapper = this.sqlSession.getMapper(InvoiceMapper.class);
			List<InvoiceVO> invoiceList = new ArrayList<InvoiceVO>();
			for(String id : ids){
				invoiceList.add(invoiceMapper.getInvoiceById(id));
			}
			return invoiceList;
		}catch( Exception e )
		{
			logger.error(e);
			throw new AppException("查询失败");
		}
	}


}
