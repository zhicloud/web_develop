package com.zhicloud.ms.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern; 

import com.zhicloud.ms.exception.MyException;

public class CapacityUtil
{
	
	/**
	 * @param byteData: 字节数
	 */
	public static String toByte(BigInteger byteData, int scale)
	{
		return byteData+"B";
	}
	
	/**
	 * @param byteData: 字节数
	 */
	public static String toKB(BigInteger byteData, int scale)
	{
		BigDecimal decData = new BigDecimal(byteData);
		decData = decData.divide(new BigDecimal("1024"));
		decData = decData.setScale(scale, BigDecimal.ROUND_HALF_UP);
		return decData+"KB";
	}

	/**
	 * @param byteData: 字节数
	 */
	public static BigDecimal toMBValue(BigInteger byteData, int scale)
	{
		BigDecimal decData = new BigDecimal(byteData);
		decData = decData.divide(new BigDecimal("1024").pow(2));
		decData = decData.setScale(scale, BigDecimal.ROUND_HALF_UP);
		return decData;
	}

	/**
	 * @param byteData: 字节数
	 */
	public static String toMB(BigInteger byteData, int scale)
	{
		return toMBValue(byteData, scale)+"MB";
	}

	/**
	 * @param byteData: 字节数
	 */
	public static BigDecimal toGBValue(BigInteger byteData, int scale, int roundingMode)
	{
		BigDecimal decData = new BigDecimal(byteData);
		decData = decData.divide(new BigDecimal("1024").pow(3));
		decData = decData.setScale(scale, roundingMode);
		return decData;
	}
	
	/**
	 * @param byteData: 字节数
	 */
	public static BigDecimal toGBValue(BigInteger byteData, int scale)
	{
		return toGBValue(byteData, scale, BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * @param byteData: 字节数
	 */
	public static String toGB(BigInteger byteData, int scale)
	{
		return toGBValue(byteData, scale)+"GB";
	}
	
	/**
	 * @param byteData: 字节数
	 */
	public static String toTB(BigInteger byteData, int scale)
	{
		BigDecimal decData = new BigDecimal(byteData);
		decData = decData.divide(new BigDecimal("1024").pow(4));
		decData = decData.setScale(scale, BigDecimal.ROUND_HALF_UP);
		return decData+"TB";
	}
	

	/**
	 * @param byteData: 字节数
	 */
	public static String toCapacityLabel(BigInteger byteData, int scale)
	{
		if( byteData.compareTo(new BigInteger("1024"))<0 )
		{
			return toByte(byteData, scale);
		}
		else if( byteData.compareTo(new BigInteger("1024").pow(2))<0 )
		{
			return toKB(byteData, scale);
		}
		else if( byteData.compareTo(new BigInteger("1024").pow(3))<0 )
		{
			return toMB(byteData, scale);
		}
		else if( byteData.compareTo(new BigInteger("1024").pow(4))<0 )
		{
			return toGB(byteData, scale);
		}
		else
		{
			return toTB(byteData, scale);
		}
	}
	
	/**
	 * 将23.5B，23.5KB，23.5MB，23.5GB，23.5TB，这样的字符串转化为以字节为单位的数字
	 */
	public static BigInteger fromCapacityLabel(String capacityLabel)
	{
		Pattern pattern = Pattern.compile("(\\d+(\\.\\d*)?)((B|KB|MB|GB|TB))");
		Matcher matcher = pattern.matcher(capacityLabel);
		if( matcher.matches()==false )
		{
			throw new IllegalArgumentException("wrong label:["+capacityLabel+"]");
		}
		BigDecimal decData = new BigDecimal(matcher.group(1));	// 数字部份
		String unit = matcher.group(3);						// 单位部份
		if( "B".equals(unit) )
		{
			// do nothing
		}
		else if( "KB".equals(unit) )
		{
			decData = decData.multiply(new BigDecimal("1024"));
		}
		else if( "MB".equals(unit) )
		{
			decData = decData.multiply(new BigDecimal("1024").pow(2));
		}
		else if( "GB".equals(unit) )
		{
			decData = decData.multiply(new BigDecimal("1024").pow(3));
		}
		else if( "TB".equals(unit) )
		{
			decData = decData.multiply(new BigDecimal("1024").pow(4));
		}
		else
		{
			throw new MyException("wrong unit:["+unit+"]");
		}
		return decData.toBigInteger();
	}
	
	public static void main(String[] args)
	{
		System.out.println(fromCapacityLabel("100GB"));
		System.out.println(toGBValue(new BigDecimal(10908913520L).toBigInteger(), 0));
		
		
	}
}











