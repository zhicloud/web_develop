package com.zhicloud.op.request;

import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestContext
{

	private Map<String, Object> resource = null;

	private RequestContext(Map<String, Object> map)
	{
		resource = map;
	}

	/**
	 * request
	 */
	public static void setHttpRequest(HttpServletRequest request)
	{
		threadLocal.get().resource.put(HTTP_REQUEST, request);
	}

	public static HttpServletRequest getHttpRequest()
	{
		return (HttpServletRequest) threadLocal.get().resource.get(HTTP_REQUEST);
	}

	/**
	 * response
	 */
	public static void setHttpResponse(HttpServletResponse response)
	{
		threadLocal.get().resource.put(HTTP_RESPONSE, response);
	}

	public static HttpServletResponse getHttpResponse()
	{
		return (HttpServletResponse) threadLocal.get().resource.get(HTTP_RESPONSE);
	}

	/**
	 * 内部类
	 */
	private static ThreadLocal<RequestContext> threadLocal = new ThreadLocal<RequestContext>()
	{
		protected RequestContext initialValue()
		{
			return new RequestContext(new TreeMap<String, Object>());
		}
	};

	public static final String SESSION         = "com.zhicloud.RequestContext.session";
	public static final String APPLICATION     = "com.zhicloud.RequestContext.application";
	public static final String COOKIE          = "com.zhicloud.RequestContext.cookie";
	public static final String PARAMETER       = "com.zhicloud.RequestContext.parameter";
	public static final String HTTP_REQUEST    = "com.zhicloud.RequestContext.http_request";
	public static final String HTTP_RESPONSE   = "com.zhicloud.RequestContext.http_response";
	public static final String HTTP_SESSION    = "com.zhicloud.RequestContext.http_session";
	public static final String SERVLET_CONTEXT = "com.zhicloud.RequestContext.servlet_context";

}
