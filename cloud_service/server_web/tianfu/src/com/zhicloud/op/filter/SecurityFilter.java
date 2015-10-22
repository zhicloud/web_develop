package com.zhicloud.op.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhicloud.op.app.helper.LoginHelper;

public class SecurityFilter implements Filter
{

	public void destroy()
	{
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException
	{
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) resp;
		if( LoginHelper.isLogin(request) )
		{
			chain.doFilter(req, resp);
		}
		else
		{
			response.sendRedirect(request.getContextPath() + "/public/not_login_yet.jsp");
		}
	}

	public void init(FilterConfig arg0) throws ServletException
	{
	}

}
