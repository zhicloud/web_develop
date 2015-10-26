package com.zhicloud.op.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class ProcessTimeLogFilter implements Filter
{
	
	private static final Logger logger = Logger.getLogger(ProcessTimeLogFilter.class);
	
	
	public void destroy()
	{
	}
	
	
	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException
	{
		long t1 = System.currentTimeMillis();
		
		HttpServletRequest request = (HttpServletRequest)req;
		try
		{
			chain.doFilter(req, resp);
		}
		finally
		{
			// 判断处理时间 
			long t2 = System.currentTimeMillis();
			if( t2-t1 > 1*1000 )	// 大于1秒
			{
				logger.warn("ProcessTimeLogFilter.doFilter() > ["+Thread.currentThread().getId()+"] 请求处理用时["+(t2-t1)+"ms],url["+request.getRequestURL()+"],queryString["+request.getQueryString()+"]");
			}
		}
		
	}
	

	public void init(FilterConfig arg0) throws ServletException
	{
	}
	
	

}
