package com.zhicloud.op.service.impl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.op.app.helper.LoginHelper;
import com.zhicloud.op.app.helper.ServiceHelper;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.login.LoginInfo;
import com.zhicloud.op.mybatis.mapper.InvoiceAddressMapper;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.service.InvoiceAddressService;
import com.zhicloud.op.service.constant.AppConstant;
import com.zhicloud.op.vo.InvoiceAddressVO;

@Transactional(readOnly = true)
public class InvoiceAddressServiceImpl extends BeanDirectCallableDefaultImpl implements InvoiceAddressService {
	
	public static final Logger logger = Logger.getLogger(AgentServiceImpl.class);

	private SqlSession sqlSession;

	public void setSqlSession(SqlSession sqlSession)
	{
		this.sqlSession = sqlSession;
	} 
	
	@Callable
	public String managePage(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("InvoiceAddressServiceImpl.managePage()");
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request, 4);
		if( AppConstant.SYS_USER_TYPE_TERMINAL_USER.equals(loginInfo.getUserType())==false ){
			return "/public/have_not_access.jsp";
		}
		return "/security/user/invoice_address_manage.jsp";
	}

	@Callable
	public String addAddressPage(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("InvoiceAddressServiceImpl.addAddressPage()");
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request, 4);
		if( AppConstant.SYS_USER_TYPE_TERMINAL_USER.equals(loginInfo.getUserType())==false ){
			return "/public/have_not_access.jsp";
		}
		return "/security/user/invoice_address_add.jsp";
	}

	@Callable
	public String modPage(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("InvoiceAddressServiceImpl.modPage()");
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request, 4);
		if( AppConstant.SYS_USER_TYPE_TERMINAL_USER.equals(loginInfo.getUserType())==false ){
			return "/public/have_not_access.jsp";
		}
		
		// 参数处理
		String id = StringUtil.trim(request.getParameter("id"));
		if( StringUtil.isBlank(id) )
		{
			throw new AppException("id不能为空");
		}

		InvoiceAddressMapper invoiceAddressMapper = this.sqlSession.getMapper(InvoiceAddressMapper.class);
		
		InvoiceAddressVO invoiceAddressVO = invoiceAddressMapper.getInvoiceAddressById(id);
		if( invoiceAddressVO != null )
		{
			request.setAttribute("invoiceAddress", invoiceAddressVO);
			return "/security/user/invoice_address_mod.jsp";
		}
		else
		{
			request.setAttribute("message", "找不到邮寄地址的记录");
			return "/public/warning_dialog.jsp";
		}
	}

	@Callable
	public void queryAddress(HttpServletRequest request, HttpServletResponse response) {
		logger.debug("InvoiceServiceImpl.queryInvoice()");
		try
		{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");

			// 获取参数
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			String userId = loginInfo.getUserId();
			Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
			Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 10);

			// 查询数据库
			InvoiceAddressMapper invoiceAddressMapper = this.sqlSession.getMapper(InvoiceAddressMapper.class);
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			condition.put("userId", userId);
			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			int total = invoiceAddressMapper.queryPageCount(condition); // 总行数
			List<InvoiceAddressVO> invoiceList = invoiceAddressMapper.queryPage(condition);// 分页结果
			
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
	public MethodResult addAddress(Map<String, String> parameter) {
		logger.debug("InvoiceAddressServiceImpl.addAddress()");
		try
		{
			String userId = StringUtil.trim(parameter.get("userId"));
			String invoiceTitle  = StringUtil.trim(parameter.get("invoiceTitle"));
			String address  = StringUtil.trim(parameter.get("address"));
			String recipients     = StringUtil.trim(parameter.get("recipients"));
			String phone = StringUtil.trim(parameter.get("phone"));
			
			InvoiceAddressMapper invoiceAddressMapper = this.sqlSession.getMapper(InvoiceAddressMapper.class);
			
			
			LinkedHashMap<String, Object> data = new LinkedHashMap<String, Object>();
			data.put("id", StringUtil.generateUUID());
			data.put("userId", userId);
			data.put("invoiceTitle", invoiceTitle);
			data.put("address", address);
			data.put("recipients", recipients);
			data.put("phone", phone);
			
			int n = invoiceAddressMapper.addInvoiceAddress(data);
			if( n>0 )
			{
				return new MethodResult(MethodResult.SUCCESS, "保存成功");
			}
			return new MethodResult(MethodResult.FAIL, "保存失败");
		}catch( Exception e )
		
		{
			logger.error(e);
			throw new AppException("保存失败");
		}
	}

	@Callable
	@Transactional(readOnly = false)
	public MethodResult updateAddress(Map<String, String> parameter) {
		logger.debug("InvoiceAddressServiceImpl.updateAddress()");
		try
		{
			
			// 参数处理
			String id = StringUtil.trim(parameter.get("id"));
			String invoiceTitle  = StringUtil.trim(parameter.get("invoiceTitle"));
			String address  = StringUtil.trim(parameter.get("address"));
			String recipients     = StringUtil.trim(parameter.get("recipients"));
			String phone = StringUtil.trim(parameter.get("phone"));

			InvoiceAddressMapper invoiceAddressMapper = this.sqlSession.getMapper(InvoiceAddressMapper.class);

			// 更新系统用户表
			Map<String, Object> data = new LinkedHashMap<String, Object>();
			data.put("id",       id);
			data.put("invoiceTitle",  invoiceTitle);
			data.put("address",  address);
			data.put("recipients",  recipients);
			data.put("phone",  phone);

			int n = invoiceAddressMapper.updateInvoiceAddress(data);
			if( n >0 ){
				return new MethodResult(MethodResult.SUCCESS, "修改成功");
			}

			return new MethodResult(MethodResult.FAIL, "修改失败");

		}
		catch( Exception e )
		{
			logger.error(e);
			throw new AppException("修改失败");
		}
	}

	@Callable
	@Transactional(readOnly = false)
	public MethodResult deleteAddressByIds(List<?> ids) {
		logger.debug("InvoiceAddressServiceImpl.deleteAddressByIds()");
		try
		{
			if( ids == null || ids.size() == 0 )
			{
				return new MethodResult(MethodResult.FAIL, "id不能为空");
			}

			InvoiceAddressMapper invoiceAddressMapper = this.sqlSession.getMapper(InvoiceAddressMapper.class);
			int n = invoiceAddressMapper.deleteAddressByIds(ids.toArray(new String[0]));

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

}
