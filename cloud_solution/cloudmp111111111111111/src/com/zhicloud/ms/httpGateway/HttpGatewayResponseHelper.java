package com.zhicloud.ms.httpGateway;


import com.zhicloud.ms.constant.AppConstant;

import net.sf.json.JSONObject;


public class HttpGatewayResponseHelper
{

	public static boolean isSuccess(JSONObject json)
	{
		String success = (String)json.get("status");
		if(AppConstant.STATUS_SUCCESS.equals(success))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static String getStatus(JSONObject json)
	{
		String status = (String)json.get("status");
		return status;
	}
	
	public static String getMessage(JSONObject json)
	{
		String message = (String)json.get("message");
		return message;
	}
	
	public static String getReturnCode(JSONObject json)
	{
		String returnCode = (String)json.get("return_code");
		return returnCode;
	}
}
