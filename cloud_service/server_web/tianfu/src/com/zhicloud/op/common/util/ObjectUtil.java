package com.zhicloud.op.common.util;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.zhicloud.op.exception.AppException;
import com.zhicloud.op.exception.CodedException;

public class ObjectUtil
{


	/*
	 * 获取名称相同,参数个数相同的方法
	 */
	public static List<Method> getSameNameMethod(Class<?> klass, String methodName, Object[] parameters)
	{
		Method[] methods = klass.getMethods();
		List<Method> sameNameMethod = new ArrayList<Method>();
		for( int i = 0; i < methods.length; i++ )
		{
			Method method = methods[i];
			if( method.getName().equals(methodName) && method.getParameterTypes().length == parameters.length )
			{
				sameNameMethod.add(method);
			}
		}
		return sameNameMethod;
	}
	
	/**
	 * 判断childClass的实例是否可以赋值给superClass的实例
	 */
	public static boolean isAssignableFrom(Class<?> superClass, Class<?> childClass)
	{
		return superClass.isAssignableFrom(childClass);
	}

	/*
	 * 判断一个类是否是另一个类的父类
	 */
	public static boolean isSuperClass(Class<?> superClass, Class<?> subClass)
	{
		if( superClass == null || subClass == null )
		{
			throw new NullPointerException();
		}
		Class<?> tempClass = subClass.getSuperclass();
		while( tempClass != null )
		{
			if( superClass.equals(tempClass) )
			{
				return true;
			}
			tempClass = tempClass.getSuperclass();
		}
		return false;
	}
	
	/**
	 * 判断cls是否实现了intfCls接口
	 */
	public static boolean isInterfaceImplemented(Class<?> interfaceClass, Class<?> cls)
	{
		for( Class<?> tempIntfClass : cls.getInterfaces() )
		{
			tempIntfClass.isAssignableFrom(cls);
			if( tempIntfClass.equals(interfaceClass) )
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断一个类是否是另一个类的本类或父类
	 */
	public static boolean isEqualOrSuperClass(Class<?> superClass, Class<?> subClass)
	{
		if( superClass == null || subClass == null )
		{
			throw new NullPointerException();
		}
		if( superClass == subClass )
		{
			return true;
		}
		else
		{
			return isSuperClass(superClass, subClass);
		}
	}
	
	public static boolean isIn(Object obj, Object[] arr)
	{
		for(Object per : arr)
		{
			if( obj.equals(per) )
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * 
	 */
	public static List<Class<?>> toClassList(List<?> list)
	{
		List<Class<?>> typeList = new ArrayList<Class<?>>();
		for( Object obj : list )
		{
			typeList.add( (obj==null) ? null : obj.getClass() );
		}
		return typeList;
	}
	
	/**
	 * 
	 */
	public static List<Class<?>> toClassList(Object[] array)
	{
		List<Class<?>> typeList = new ArrayList<Class<?>>();
		for( Object obj : array )
		{
			typeList.add(obj.getClass());
		}
		return typeList;
	}
	
	/*
	 * 查找兼容函数签名的方法
	 */
	public static Method getCompatibleMethod(Class<?> klass, String methodName, Class<?>[] parameterTypes)
	{
		List<Method> sameNameMethods = getSameNameMethod(klass, methodName, parameterTypes);
		for (int i = 0; i < sameNameMethods.size(); i++)
		{
			Method method = sameNameMethods.get(i);
			Class<?>[] types = method.getParameterTypes();
			boolean isCompatable = true;
			for (int j = 0; j < types.length; j++)
			{
				Class<?> targetType = parameterTypes[j];
				Class<?> compareType = types[j];
				// 目标类型是null
				if( targetType==null )
				{
					// 查找的参数类型必须不是原生类型
					if( compareType.isPrimitive()==false )
					{
						continue;	// 可接受，判断下一个参数
					}
					else
					{
						isCompatable = false;
						break; // 不可接受，查找下一个method
					}
				}
				else 
				{
					if( isAssignableFrom(compareType, targetType) )
					{
						continue;	// 可接受，判断下一个参数
					}
					else
					{
						isCompatable = false;
						break; // 不可接受，查找下一个method
					}
				}
			}
			if( isCompatable )
			{
				return method;
			}
		}
		return null;
	}
	
	
	public static String toMethodDescription(Method method)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(method.getReturnType().getName() + " " + method.getName() + "(");
		Class<?>[] parameterTypes = method.getParameterTypes();
		for (int i = 0; i < parameterTypes.length - 1; i++)
		{
			sb.append(" " + parameterTypes[i].getName() + ",");
		}
		if (parameterTypes.length > 0)
		{
			sb.append(" " + parameterTypes[parameterTypes.length - 1].getName() + " ");
		}
		sb.append(")");
		return sb.toString();
	}
	
	
	public static String toMethodDescription(String methodName, Class<?>[] parameterTypes)
	{
		StringBuffer sb = new StringBuffer();
		sb.append(methodName + "(");
		for (int i = 0; i < parameterTypes.length - 1; i++)
		{
			sb.append(" " + parameterTypes[i].getName() + ",");
		}
		if (parameterTypes.length > 0)
		{
			sb.append(" " + parameterTypes[parameterTypes.length - 1].getName() + " ");
		}
		sb.append(")");
		return sb.toString();
	}
	
	
	public static Map<String, Object> getPropertyMap(Object obj)
	{
		Map<String, Object> map = new TreeMap<String, Object>();
		try
		{
			Class<?> klass = obj.getClass();
			// 找public 属性
			Field[] fields = klass.getFields();
			for (int i = 0; i < fields.length; i++)
			{
				Field field = fields[i];
				if( !Modifier.isStatic(field.getModifiers()) && Modifier.isPublic(field.getModifiers()) )
				{
					map.put(fields[i].getName(), fields[i].get(obj));
				}
			}
			// 找private属性对应的方法
			BeanInfo beanInfo = Introspector.getBeanInfo(klass);
			PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
			for (int i = 0; i < propertyDescriptors.length; i++)
			{
				PropertyDescriptor property = propertyDescriptors[i];
				Method method = property.getReadMethod();
				if( method != null && Modifier.isPublic(method.getModifiers()) )
				{
					map.put(property.getName(), method.invoke(obj));
				}
			}
		}
		catch (InvocationTargetException e)
		{
			Throwable targetException = e.getTargetException();
			if( targetException instanceof AppException )
			{
//				throw (AppException)targetException;
				throw new AppException("失败");
			}
			else
			{
//				throw new AppException(targetException);
				throw new AppException("失败");
			}
		}
		catch (AppException e)
		{ 
			throw new AppException("失败");
		}
		catch (Exception e)
		{
			throw new AppException("失败");
		}
		return map;
	}

	
	public static void main(String[] args)
	{
		System.out.println(getPropertyMap(new CodedException("ddd")));
	}
}