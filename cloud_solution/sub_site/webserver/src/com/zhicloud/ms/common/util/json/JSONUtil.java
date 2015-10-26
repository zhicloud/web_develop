package com.zhicloud.ms.common.util.json;
//package com.zhicloud.op.common.util.json;
//
//import java.beans.BeanInfo;
//import java.beans.Introspector;
//import java.beans.PropertyDescriptor;
//import java.lang.reflect.Array;
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.lang.reflect.Modifier;
//import java.math.BigInteger;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.Iterator;
//import java.util.List;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.Set;
//
//import org.json.simple.JSONArray;
//import org.json.simple.JSONAware;
//import org.json.simple.JSONObject;
//import org.json.simple.JSONValue;
//
//import com.zhicloud.op.common.util.DateUtil;
//import com.zhicloud.op.common.util.StringUtil;
//import com.zhicloud.op.exception.AppException;
//
//public class JSONUtil
//{
//
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	public static String toJSONString(Object obj)
//	{
//		// null
//		if (obj == null)
//		{
//			return "null";
//		}
//		// 原生类型
//		if (obj instanceof Number || obj instanceof Boolean || obj instanceof Character || obj instanceof String)
//		{
//			return JSONValue.toJSONString(obj);
//		}
//		// Class
//		if (obj instanceof Class)
//		{
//			return classToJSONString((Class<?>) obj);
//		}
//		// Date
//		if (obj instanceof Date)
//		{
//			return dateToJSONString((Date) obj);
//		}
//		// 异常
//		if (obj instanceof Throwable)
//		{
//			return exceptionToJSONString((Throwable) obj);
//		}
//		// 数组
//		if (obj.getClass().isArray())
//		{
//			return arrayToJSONString(obj);
//		}
//		// Map
//		if (obj instanceof Map)
//		{
//			return mapToJSONString((Map<Object, Object>) obj);
//		}
//		// 迭代器
//		if (obj instanceof Iterable)
//		{
//			return iterableToJSONString((Iterable) obj);
//		}
//		// JavaBean对象
//		if (obj instanceof JSONBean)
//		{
//			return beanToJSONString(obj);
//		}
//		// JSONAware
//		if (obj instanceof JSONAware)
//		{
//			return JSONValue.toJSONString(obj);
//		}
//		throw new AppException("不能转换为json字符, class:[" + (obj.getClass()) + "]");
//	}
//
//	// 异常
//	@SuppressWarnings("unchecked")
//	public static String exceptionToJSONString(Throwable e)
//	{
//		JSONObject obj = new JSONObject();
//		obj.put("exceptionClass", e.getClass().getName());
//		obj.put("exceptionMessage", e.getMessage());
//		return obj.toJSONString();
//	}
//
//	// Date
//	public static String dateToJSONString(Date date)
//	{
//		return "\"" + DateUtil.dateToString(date) + "\"";
//	}
//
//	// Class
//	public static String classToJSONString(Class<?> klass)
//	{
//		return "\"" + klass.getName() + "\"";
//	}
//
//	// JavaBean
//	public static String beanToJSONString(Object obj)
//	{
//		try
//		{
//			List<String> result = new ArrayList<String>();
//			Class<?> klass = obj.getClass();
//			// 找public 属性
//			Field[] fields = klass.getFields();
//			for (int i = 0; i < fields.length; i++)
//			{
//				Field field = fields[i];
//				if (!Modifier.isStatic(field.getModifiers()))
//				{
//					result.add(toJSONString(fields[i].getName()) + ":" + toJSONString(fields[i].get(obj)));
//				}
//			}
//			// 找private属性对应的方法
//			BeanInfo beanInfo = Introspector.getBeanInfo(klass);
//			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
//			for (int i = 0; i < propertyDescriptors.length; i++)
//			{
//				PropertyDescriptor property = propertyDescriptors[i];
//				Method method = property.getReadMethod();
//				if (method != null)
//				{
//					result.add(toJSONString(property.getName()) + ":" + toJSONString(method.invoke(obj)));
//				}
//			}
//			return new StringBuffer("{").append(StringUtil.joinWrap(result)).append("}").toString();
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//			return "{}";
//		}
//	}
//
//	// Map
//	public static String mapToJSONString(Map<Object, Object> map)
//	{
//		List<String> result = new ArrayList<String>();
//		Set<Entry<Object, Object>> entrySet = map.entrySet();
//		for (Iterator<Entry<Object, Object>> it = entrySet.iterator(); it.hasNext();)
//		{
//			Entry<Object, Object> entry = it.next();
//			result.add(toJSONString(entry.getKey()) + ":" + toJSONString(entry.getValue()));
//		}
//		return new StringBuffer("{").append(StringUtil.joinWrap(result)).append("}").toString();
//	}
//
//	// 迭代器
//	public static String iterableToJSONString(Iterable<Object> iterable)
//	{
//		List<String> result = new ArrayList<String>();
//		for (Iterator<Object> it = iterable.iterator(); it.hasNext();)
//		{
//			result.add(toJSONString(it.next()));
//		}
//		return new StringBuffer("[").append(StringUtil.joinWrap(result)).append("]").toString();
//	}
//
//	// 数组
//	public static String arrayToJSONString(Object arr)
//	{
//		List<String> result = new ArrayList<String>();
//		int len = Array.getLength(arr);
//		for (int i = 0; i < len; i++)
//		{
//			result.add(toJSONString(Array.get(arr, i)));
//		}
//		return new StringBuffer("[").append(StringUtil.joinWrap(result)).append("]").toString();
//	}
//	
//	//---------------
//
//	//
//	public static String getString(JSONObject json, String key)
//	{
//		return (String)json.get(key);
//	}
//	
//	//
//	public static Integer getInteger(JSONObject json, String key)
//	{
//		return (Integer)json.get(key);
//	}
//	
//	//
//	public static Long getLong(JSONObject json, String key)
//	{
//		return (Long)json.get(key);
//	}
//	
//	//
//	public static Double getDouble(JSONObject json, String key)
//	{
//		return (Double)json.get(key);
//	}
//	
//	//
//	public static BigInteger getBigInteger(JSONObject json, String key)
//	{
//		return new BigInteger((String)json.get(key));
//	}
//
//	//
//	public static String[] getStringArray(JSONObject json, String key)
//	{
//		JSONArray list = (JSONArray)json.get(key);
//		String[] result = new String[list.size()];
//		for( int i=0; i<list.size(); i++)
//		{
//			result[i] = (String)list.get(i);
//		}
//		return result;
//	}
//	
//	//
//	public static Integer[] getIntegerArray(JSONObject json, String key)
//	{
//		JSONArray list = (JSONArray)json.get(key);
//		Integer[] result = new Integer[list.size()];
//		for( int i=0; i<list.size(); i++)
//		{
//			result[i] = (Integer)list.get(i);
//		}
//		return result;
//	}
//	
//	//
//	public static Long[] getLongArray(JSONObject json, String key)
//	{
//		JSONArray list = (JSONArray)json.get(key);
//		Long[] result = new Long[list.size()];
//		for( int i=0; i<list.size(); i++)
//		{
//			result[i] = (Long)list.get(i);
//		}
//		return result;
//	}
//	
//	//
//	public static BigInteger[] getBigIntegerArray(JSONObject json, String key)
//	{
//		JSONArray list = (JSONArray)json.get(key);
//		BigInteger[] result = new BigInteger[list.size()];
//		for( int i=0; i<list.size(); i++)
//		{
//			result[i] = new BigInteger((String)list.get(i));
//		}
//		return result;
//	}
//	
//	
////	public static void main(String[] args)
////	{
////		JSONObject json = new JSONObject();
////		json.put("a", new BigInteger("12345678901234567890123456789012345678901234567890"));
////		String s = json.toJSONString();
////		System.out.println(s);
////		
////		json = (JSONObject)JSONValue.parse(s);
////		System.out.println(json==null);
////		System.out.println(json);
////	}
//}
