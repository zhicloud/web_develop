package com.zhicloud.op.app.helper;

import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.zhicloud.op.common.util.json.JSONLibUtil;
import com.zhicloud.op.exception.AppException;

public class ServiceHelper
{

	public static String SUCCESS = "succ";
	public static String FAIL    = "fail";
	
	public static Map<Object, Object> toSuccessObject(String message)
	{
		Map<Object, Object> obj = new LinkedHashMap<Object, Object>();
		obj.put("status",  SUCCESS);
		obj.put("message", message);
		return obj;
	}
	
	public static Map<Object, Object> toFailObject(String message)
	{
		Map<Object, Object> obj = new LinkedHashMap<Object, Object>();
		obj.put("status",  FAIL);
		obj.put("message", message);
		return obj;
	}
	
	//---------------
	
	public static void writePlainTextTo(OutputStream out, Object obj, String encoding)
	{
		try
		{
			out.write(obj.toString().getBytes(encoding));
		}
		catch (Exception e)
		{
			throw new AppException("失败");
		}
	}
	
	public static void writePlainTextTo(OutputStream out, Object obj)
	{
		writePlainTextTo(out, obj, "utf8");
	}
	
	public static void writeJsonTo(OutputStream out, Object obj, String encoding)
	{
		try
		{
			out.write(JSONLibUtil.toJSONString(obj).getBytes(encoding));
		}
		catch (Exception e)
		{
			throw new AppException("失败");
		}
	}
	
	public static void writeJsonTo(OutputStream out, Object obj)
	{
		writeJsonTo(out, obj, "utf-8");
	}
	
	public static void writeSuccessMessage(OutputStream out, String message)
	{
		writeJsonTo(out, toSuccessObject(message), "utf-8");
	}
	
	public static void writeDatagridResultAsJsonTo(OutputStream out, int total, List<?> rows)
	{
		Map<String, Object> datagridResult = new LinkedHashMap<String, Object>();
		datagridResult.put("total", total);
		datagridResult.put("rows", rows);
		writeJsonTo(out, datagridResult);
	}
	
	public static void writeFailMessage(OutputStream out, String message)
	{
		writeJsonTo(out, toFailObject(message), "utf-8");
	}
	
	public static void main(String[] args)
	{
		
	}
	/**
	 * @Description:response输出时需要额外增加一个属性
	 * @param out
	 * @param total
	 * @param rows
	 */
    public static void writeMapResultAsJsonTo(OutputStream out, int total, List<?> rows, Map<String, String> map) {
        Map<String, Object> datagridResult = new LinkedHashMap<String, Object>();
        datagridResult.put("total", total);
        datagridResult.put("rows", rows);
        datagridResult.put("map", map);
        writeJsonTo(out, datagridResult);
    }
}
