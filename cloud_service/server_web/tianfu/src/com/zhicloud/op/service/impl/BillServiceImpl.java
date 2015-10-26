package com.zhicloud.op.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.ibatis.session.SqlSession;
import org.apache.log4j.Logger;
import org.springframework.transaction.annotation.Transactional;

import com.zhicloud.op.app.authorization.PrivilegeConstant;
import com.zhicloud.op.app.helper.LoginHelper;
import com.zhicloud.op.app.helper.ServiceHelper;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.login.LoginInfo;
import com.zhicloud.op.mybatis.mapper.AccountBalanceDetailMapper;
import com.zhicloud.op.mybatis.mapper.BillMapper;
import com.zhicloud.op.mybatis.mapper.CloudHostBillDetailMapper;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.service.BillService;
import com.zhicloud.op.service.constant.AppConstant;
import com.zhicloud.op.vo.AccountBalanceDetailVO;
import com.zhicloud.op.vo.BillVO;
import com.zhicloud.op.vo.CloudHostBillDetailVO;
import com.zhicloud.op.vo.UserOrderVO;

@Transactional(readOnly = true)
public class BillServiceImpl extends BeanDirectCallableDefaultImpl implements BillService
{

	public static final Logger logger = Logger.getLogger(BillServiceImpl.class);

	private SqlSession sqlSession;
	
	
	public void setSqlSession(SqlSession sqlSession)
	{
		this.sqlSession = sqlSession;
	}

	
	/**
	 * 账单项的查询结果
	 */
	@Callable
	public void billQueryResultPartPage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("BillServiceImpl.billQueryResultPartPage()");
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
//		if( loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_OPERATOR && loginInfo.hasPrivilege(PrivilegeConstant.bill_part_page) == false )
//		{
//			return "/public/have_not_access.jsp";
//		}else if(loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT && loginInfo.hasPrivilege(PrivilegeConstant.bill_part_page_agent) == false){
//			return "/public/have_not_access.jsp";
//		}
		
		// 参数处理
		String account = StringUtil.trim(request.getParameter("account"));
		
		BillMapper billMapper = this.sqlSession.getMapper(BillMapper.class);
		AccountBalanceDetailMapper accountBalanceDetailMapper = this.sqlSession.getMapper(AccountBalanceDetailMapper.class);
		
		//
		Map<String, Object> condition = new LinkedHashMap<String, Object>();
		List<BillVO> billList = null;
		condition.put("account", "%"+account+"%");
		if(loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT){
			condition.put("agentId",loginInfo.getUserId());
			billList = billMapper.queryPageForAgent(condition);
		}else if(loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_TERMINAL_USER){
//			billList = billMapper.queryPageForTerminal(condition);
			Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
			Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 10);
			 
			 
			// 查询数据库   
			condition.put("userId", loginInfo.getUserId());
			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			int total = accountBalanceDetailMapper.getDetailCount(condition);// 总行数
			List<AccountBalanceDetailVO> billDetailList = accountBalanceDetailMapper.getDetail(condition);  
			List<UserOrderVO> newUserOrderList = new ArrayList<UserOrderVO>(); 

			// 输出json数据
				try {
					ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, billDetailList);
				} catch (IOException e) {  
					logger.error("BillServiceImpl.billQueryResultPartPage()",e);
					throw new AppException("查询失败");
				}
			 
		}
		else{
			billList = billMapper.queryPage(condition);
		}
		request.setAttribute("billList", billList);
//		if(loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT){
//			return "/security/agent/bill_part.jsp";
//		}
//		if(loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_TERMINAL_USER){
//			return "/security/user/bill_part.jsp";
//		}
//		return "/security/operator/bill_part.jsp";
	}

	/**
	 * 
	 */
	@Callable
	public String managePage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("BillServiceImpl.managePage()");
		
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_OPERATOR && loginInfo.hasPrivilege(PrivilegeConstant.bill_manage_page) == false )
		{
			return "/public/have_not_access.jsp";
		}else if(loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT && loginInfo.hasPrivilege(PrivilegeConstant.bill_manage_page_agent) == false){
			return "/public/have_not_access.jsp";
		}
		if(loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT){
			return "/security/agent/bill_manage.jsp";
		}else if(loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_OPERATOR){
			return "/security/operator/bill_manage.jsp";
		}else{
			return "/security/user/bill_manage.jsp";
		}
	}
	
	@Callable
	@Override
	public String queryBillDetailPage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("BillServiceImpl.queryBillDetailPage()");
		String billId = StringUtil.trim(request.getParameter("billId"));
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_OPERATOR && loginInfo.hasPrivilege(PrivilegeConstant.bill_detail_page) == false )
		{
			return "/public/have_not_access.jsp";
		}else if(loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT && loginInfo.hasPrivilege(PrivilegeConstant.bill_detail_page_agent) == false){
			return "/public/have_not_access.jsp";
		}
		
		CloudHostBillDetailMapper cloudHostBillDetailMapper = this.sqlSession.getMapper(CloudHostBillDetailMapper.class);
		
		List<CloudHostBillDetailVO> cloudHostBillDetailList = cloudHostBillDetailMapper.queryCloudHostByDetailByBillId(billId);
		if( cloudHostBillDetailList.size() > 0 )
		{
			request.setAttribute("cloudHostBillDetailList", cloudHostBillDetailList);
			if(loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT){
				return "/security/agent/bill_detail.jsp";
			}else if(loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_TERMINAL_USER){
				return "/security/user/bill_detail.jsp";
			}
			return "/security/operator/bill_detail.jsp";
		}
		else
		{
			request.setAttribute("message", "找不到账单项的记录");
			return "/public/warning_dialog.jsp";
		}
	}
	
	
	@Callable
	@Override
	public String queryCloudHostBillDetailPage(HttpServletRequest request, HttpServletResponse response)
	{
		logger.debug("BillServiceImpl.queryCloudHostBillDetailPage()");
		String billDetailId = StringUtil.trim(request.getParameter("billDetailId"));
		
		// 权限判断
		LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
		if( loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_OPERATOR && loginInfo.hasPrivilege(PrivilegeConstant.cloud_host_bill_detail_page) == false )
		{
			return "/public/have_not_access.jsp";
		}else if(loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT && loginInfo.hasPrivilege(PrivilegeConstant.cloud_host_bill_detail_page_agent) == false){
			return "/public/have_not_access.jsp";
		}

		CloudHostBillDetailMapper cloudHostBillDetailMapper = this.sqlSession.getMapper(CloudHostBillDetailMapper.class);

		CloudHostBillDetailVO cloudHostBillDetail = cloudHostBillDetailMapper.queryCloudHostBillDetailByBillDetailId(billDetailId);
		if( cloudHostBillDetail != null )
		{
			request.setAttribute("cloudHostBillDetail", cloudHostBillDetail);
			if(loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_AGENT){
				return "/security/agent/bill_detail_for_cloud_host.jsp";
			}else if(loginInfo.getUserType() == AppConstant.SYS_USER_TYPE_TERMINAL_USER){
				return "/security/user/bill_detail_for_cloud_host.jsp";
			}
			return "/security/operator/bill_detail_for_cloud_host.jsp";
		}
		else
		{
			request.setAttribute("message", "找不到云主机账单的记录");
			return "/public/warning_dialog.jsp";
		}
	}
	
	
	/**
	 * 
	 */
	@Callable
	@Transactional(readOnly=false)
	public MethodResult deleteBillByIds(List<?> billIds)
	{
		logger.debug("BillServiceImpl.deleteBillByIds()");
		try
		{
			if( billIds==null || billIds.size()==0 )
			{
				throw new AppException("billIds不能为空");
			}
			
			BillMapper billMapper = this.sqlSession.getMapper(BillMapper.class);
			int n = billMapper.deleteByIds(billIds.toArray(new String[0]));
			
			if( n>0 )
			{
				return new MethodResult(MethodResult.SUCCESS, "删除成功");
			}
			else
			{
				return new MethodResult(MethodResult.FAIL, "删除失败");
			}
		}
		catch (Exception e)
		{
			logger.error("BillServiceImpl.deleteBillByIds()",e);
			throw new AppException("删除失败");
		}
	}


	
	
}
