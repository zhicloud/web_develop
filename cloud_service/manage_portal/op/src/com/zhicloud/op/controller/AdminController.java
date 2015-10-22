package com.zhicloud.op.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zhicloud.op.app.helper.LoginHelper;
import com.zhicloud.op.login.LoginInfo;
import com.zhicloud.op.service.constant.AppConstant;

@Controller
public class AdminController
{
	
	
	public static final Logger logger = Logger.getLogger(AdminController.class);
	

	@RequestMapping("/admin.do")
	public String publicAdmin(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		logger.debug("AdminController.publicAdmin()");
		
		boolean isLogin = LoginHelper.isLogin(request, AppConstant.SYS_USER_TYPE_ADMIN);
		if( isLogin==false )
		{
			return "/public/admin/login.jsp";
		}
		else
		{
			LoginInfo user = LoginHelper.getLoginInfo(request,AppConstant.SYS_USER_TYPE_ADMIN);
			if( user.getUserType() != 1 )
			{
				request.setAttribute("message", "您不是超级管理员，不能访问本页面！");
				return "/public/warning.jsp";
			}
			return "/security/admin/main.jsp";
		}
	}
	
	
}
