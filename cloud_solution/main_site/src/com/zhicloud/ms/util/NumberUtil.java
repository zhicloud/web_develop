package com.zhicloud.ms.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;

public class NumberUtil
{
	
	public static Integer toInteger(Object data)
	{
		if( data==null )
		{
			return null;
		}
		else if( data instanceof Number )
		{
			return ((Number)data).intValue();
		}
		else
		{
			return Integer.valueOf(String.valueOf(data));
		}
	}
	
	public static Long toLong(Object data)
	{
		if( data==null )
		{
			return null;
		}
		else if( data instanceof Number )
		{
			return ((Number)data).longValue();
		}
		else
		{
			return Long.valueOf(String.valueOf(data));
		}
	}
	
	public static Double toDouble(Object data)
	{
		if( data==null )
		{
			return null;
		}
		else if( data instanceof Number )
		{
			return ((Number)data).doubleValue();
		}
		else
		{
			return Double.valueOf(String.valueOf(data));
		}
	}
	
	public static BigInteger toBigInteger(Object data)
	{
		if( data==null )
		{
			return null;
		}
		else
		{
			return new BigInteger(String.valueOf(data));
		}
	}
	
	public static BigDecimal toBigDecimal(Object data)
	{
		if( data==null )
		{
			return null;
		}
		else
		{
			return new BigDecimal(String.valueOf(data));
		}
	}
	
	public static boolean equals(Number number1, Number number2)
	{
		if( number1==null && number2==null )
		{
			return true;
		}
		if( number1!=null )
		{
			return number1.equals(number2);
		}
		else
		{
			return number2.equals(number1);
		}
	}
	
	/**
	 * 
	 */
	public static BigDecimal divide(BigDecimal d1, BigDecimal... d2Arr)
	{
		//return d1.divide(d2, 50, BigDecimal.ROUND_HALF_EVEN);
		BigDecimal result = d1;
		for( BigDecimal d2 : d2Arr )
		{
			result = result.divide(d2, 50, BigDecimal.ROUND_HALF_EVEN);
		}
		return result;
	}

	/**
	 * 
	 */
	public static BigDecimal scale( double num, int scale)
	{
		return scale(num, scale, RoundingMode.HALF_UP);
	}
	
	/**
	 * 
	 */
	public static BigDecimal scale( double num, int scale, RoundingMode roundingMode)
	{
		BigDecimal bigNum = new BigDecimal(num);
		return bigNum.setScale(scale, roundingMode);
	}
	
	/**
	 * 
	 */
	public static void main(String[] args)
	{
		System.out.println(scale(1111.111111111, 2, RoundingMode.HALF_UP));
	}
	
	
}
