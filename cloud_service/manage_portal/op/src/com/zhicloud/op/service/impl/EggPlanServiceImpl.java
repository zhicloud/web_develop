package com.zhicloud.op.service.impl;

import java.util.Date;
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
import com.zhicloud.op.app.helper.VerificationCodeHelper;
import com.zhicloud.op.common.util.DateUtil;
import com.zhicloud.op.common.util.RandomPassword;
import com.zhicloud.op.common.util.SendMail;
import com.zhicloud.op.common.util.StringUtil;
import com.zhicloud.op.common.util.constant.MailConstant;
import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.login.LoginInfo;
import com.zhicloud.op.mybatis.mapper.AgentMapper;
import com.zhicloud.op.mybatis.mapper.CashCouponMapper;
import com.zhicloud.op.mybatis.mapper.CloudDiskMapper;
import com.zhicloud.op.mybatis.mapper.EggPlanMapper;
import com.zhicloud.op.mybatis.mapper.MarkBindUserMapper;
import com.zhicloud.op.mybatis.mapper.SysUserMapper;
import com.zhicloud.op.remote.BeanDirectCallableDefaultImpl;
import com.zhicloud.op.remote.CallWithoutLogin;
import com.zhicloud.op.remote.Callable;
import com.zhicloud.op.remote.MethodResult;
import com.zhicloud.op.request.RequestContext;
import com.zhicloud.op.service.EggPlanService;
import com.zhicloud.op.service.constant.AppConstant;
import com.zhicloud.op.vo.AgentVO;
import com.zhicloud.op.vo.CloudDiskVO;
import com.zhicloud.op.vo.EggPlanVO;
@Transactional(readOnly = true)
public class EggPlanServiceImpl  extends BeanDirectCallableDefaultImpl  implements EggPlanService {
	public static final Logger logger = Logger.getLogger(EggPlanServiceImpl.class);

	private SqlSession sqlSession;
	
	public void setSqlSession(SqlSession sqlSession)
	{
		this.sqlSession = sqlSession;
	}
    @Callable
	public String eggPlanManagePage(HttpServletRequest request,HttpServletResponse response) {
// 		LoginInfo loginInfo = LoginHelper.getLoginInfo(request, 4);
//		if( AppConstant.SYS_USER_TYPE_TERMINAL_USER.equals(loginInfo.getUserType())==false ){
//			return "/public/have_not_access.jsp";
//		}
		return "/security/operator/egg_plan_manage.jsp";
	}

	@Callable
	public void queryEggPlan(HttpServletRequest request, HttpServletResponse response) {
 		try{
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/json; charset=utf-8");
			
 
			// 获取参数
			String status = StringUtil.trim(request.getParameter("status"));
			String name = StringUtil.trim(request.getParameter("name"));
 			Integer page = StringUtil.parseInteger(request.getParameter("page"), 1) - 1; // 前台过来的是从1开始，需要的是从0开始
			Integer rows = StringUtil.parseInteger(request.getParameter("rows"), 10);
			
 

			// 查询数据库
			EggPlanMapper eggPlanMapper = this.sqlSession.getMapper(EggPlanMapper.class);
			Map<String, Object> condition = new LinkedHashMap<String, Object>();
			if(!StringUtil.isBlank(status)){
				condition.put("status", status);
			}
			if(!StringUtil.isBlank(name)){
				condition.put("name", "%"+name+"%");
			}

			condition.put("start_row", page * rows);
			condition.put("row_count", rows);
			int total = eggPlanMapper.queryEggPlanCount(condition); // 总行数
			List<EggPlanVO> eggPlanList = eggPlanMapper.queryEggPlan(condition);// 分页结果
			
			// 输出json数据
			ServiceHelper.writeDatagridResultAsJsonTo(response.getOutputStream(), total, eggPlanList);
 		}catch( Exception e )
		{
			logger.error("EggPlanServiceImpl.queryEggPlan()",e); 
			throw new AppException("查询失败");
		}

	}

	@Callable 
	@CallWithoutLogin
	@Transactional(readOnly = false)
	public MethodResult addEggPlan(Map<String, String> parameter) {

 		try
		{
			HttpServletRequest request = RequestContext.getHttpRequest(); 
			// 参数处理
			String incubatorName     = StringUtil.trim(parameter.get("incubatorName"));
			String contacts    = StringUtil.trim(parameter.get("contacts"));
			String contactsPosition    = StringUtil.trim(parameter.get("contactsPosition"));
			String contactsPhone       = StringUtil.trim(parameter.get("contactsPhone"));
			String qqOrWeixin   = StringUtil.trim(parameter.get("qqOrWeixin"));
			String summary      = StringUtil.trim(parameter.get("summary")); 
			String verificationCode = StringUtil.trim(parameter.get("verificationCode"));
					
			if(StringUtil.isBlank(incubatorName)){
				return new MethodResult(MethodResult.FAIL,"请输入孵化器或者园区名字");
			} 
			if(StringUtil.isBlank(contacts)){
				return new MethodResult(MethodResult.FAIL,"联系人不能为空");
			} 
//			if(StringUtil.isBlank(contactsPosition)){
//				return new MethodResult(MethodResult.FAIL,"联系人职位不能为空");
//			}
			if(StringUtil.isBlank(contactsPhone)){
				return new MethodResult(MethodResult.FAIL,"联系人电话不能为空");
			}
			if(StringUtil.isBlank(qqOrWeixin)){
				return new MethodResult(MethodResult.FAIL,"QQ或者微信不能为空");
			}
			if(StringUtil.isBlank(summary)){
				return new MethodResult(MethodResult.FAIL,"简介不能为空");
			}
			// 验证友判断
			if( VerificationCodeHelper.isMatch(request, verificationCode)==false )
			{
				return new MethodResult(MethodResult.FAIL, "验证码错误");
			}
			
			EggPlanMapper eggPlanMapper = this.sqlSession.getMapper(EggPlanMapper.class);

			
			Map<String, Object> eggPlanData = new LinkedHashMap<String, Object>();
			eggPlanData.put("id",          	StringUtil.generateUUID());
			eggPlanData.put("incubatorName",        incubatorName);
			eggPlanData.put("contacts",       contacts);
			eggPlanData.put("contactsPosition",       contactsPosition);
			eggPlanData.put("createTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
			eggPlanData.put("status",      1);//默认为待处理
			eggPlanData.put("contactsPhone",  contactsPhone);
			eggPlanData.put("qqOrWeixin",  qqOrWeixin);
			eggPlanData.put("summary",  summary);

			int m = eggPlanMapper.addEggPlan(eggPlanData);
			
			 
			if(m<0 )
			{
				return new MethodResult(MethodResult.FAIL, "添加失败");
			}
			else
			{
				return new MethodResult(MethodResult.SUCCESS, "添加成功");
			}
		}
		catch( Exception e )
		{
			logger.error("EggPlanServiceImpl.addEggPlan()",e);
			throw new AppException("新增失败");
		}
	}

	@Callable 
	@Transactional(readOnly = false)
	public MethodResult updateEggPlanByIds(List<String> eggPlanIds) {

 		try
		{
 			HttpServletRequest request = RequestContext.getHttpRequest(); 
			LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
			
			
			// 参数处理
			if( eggPlanIds == null || eggPlanIds.size() == 0 )
			{
				return new MethodResult(MethodResult.FAIL,"选择删除项");
			}

			EggPlanMapper eggPlanMapper = this.sqlSession.getMapper(EggPlanMapper.class);

			
			Map<String, Object> eggPlanData = new LinkedHashMap<String, Object>();
			eggPlanData.put("ids",          	eggPlanIds.toArray(new String[0]));
			eggPlanData.put("handleUserId",        loginInfo.getUserId());
			eggPlanData.put("handleTime", StringUtil.dateToString(new Date(), "yyyyMMddHHmmssSSS"));
			eggPlanData.put("status",          	2);//已处理
			
			int n = eggPlanMapper.updateEggPlan(eggPlanData);
			
			if( n >0  )
			{
				return new MethodResult(MethodResult.SUCCESS, "更新成功");
			}
			else
			{
				return new MethodResult(MethodResult.FAIL, "更新失败");
			}
		}
		catch( Exception e )
		{
			logger.error("EggPlanServiceImpl.updateEggPlanByIds()",e);
			throw new AppException("更新失败");
		}
	}
	@Callable
	public String eggPlanManagePageForDetail(HttpServletRequest request,
			HttpServletResponse response) {
		String  id = StringUtil.trim(request.getParameter("id"));
		EggPlanMapper eggPlanMapper = this.sqlSession.getMapper(EggPlanMapper.class);
		EggPlanVO eggPlan = eggPlanMapper.queryEggPlanById(id);
		request.setAttribute("eggPlan", eggPlan);
		return "/security/operator/egg_plan_manage_detail.jsp";

	}

}
