package com.zhicloud.ms.remote;


import java.util.HashMap;
import java.util.Map;

import com.zhicloud.ms.common.util.json.JSONBean;

public class MethodResult implements JSONBean
{
	
	public static final String SUCCESS = "success";
	public static final String FAIL    = "fail";
	

	public String status = FAIL;
	public String message = "";
	public Exception exception = null;
	
	
	// -----------------
	
	
	public MethodResult()
	{
	}
	
	
	public MethodResult(String message)
	{
		this.message = message;
	}
	
	
	public MethodResult(String status, String message)
	{
		this.status = status;
		this.message = message;
	}
	
	
	public boolean isSuccess()
	{
		return SUCCESS.equals(this.status);
	}
	
	
	// --------------

	public Map<Object, Object> properties = new HashMap<Object, Object>();

	public Object put(Object key, Object value)
	{
		return properties.put(key, value);
	}

	public Object get(Object key)
	{
		return properties.get(key);
	}

	public String setProperty(String key, String value)
	{
		return (String) properties.put(key, value);
	}

	public String getProperty(String key)
	{
		return (String) properties.get(key);
	}

}
