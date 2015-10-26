package com.zhicloud.op.filter;

import java.io.IOException;
import java.net.SocketException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class ExceptionLogFilter implements Filter
{
	
	private static final Logger logger = Logger.getLogger(ExceptionLogFilter.class);

	public void destroy()
	{
	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException
	{
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)resp;
		try
		{
			chain.doFilter(req, resp);
		}
		catch( SocketException e )
		{
			logger.error("["+Thread.currentThread().getId()+"] ", e);
		}
		catch( Throwable e )
		{
			logger.error("["+Thread.currentThread().getId()+"] ", e);
			request.getRequestDispatcher("/public/error.jsp").forward(request, response);
		}
	}
	
	public void init(FilterConfig arg0) throws ServletException
	{
	}

}
