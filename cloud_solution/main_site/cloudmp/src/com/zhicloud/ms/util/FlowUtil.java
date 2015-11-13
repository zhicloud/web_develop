package com.zhicloud.ms.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern; 

import com.zhicloud.ms.exception.MyException;


/**
 * 用于流量计算
 */
public class FlowUtil
{

	
	public static String toBps(BigInteger data, int scale)
	{
		return data + "bps";
	}
	
	public static String toKbps(BigInteger bpsData, int scale)
	{
		BigDecimal data = new BigDecimal(bpsData);
		data = data.divide(new BigDecimal("1024")).multiply(new BigDecimal("8"));
		data = data.setScale(scale, BigDecimal.ROUND_HALF_UP);
		return data + "Kbps";
	}

	public static BigDecimal toMbpsValue(BigInteger bpsData, int scale, int roundingMode)
	{
		BigDecimal data = new BigDecimal(bpsData);
		data = data.divide(new BigDecimal("1024").pow(2)).multiply(new BigDecimal("8"));
		data = data.setScale(scale, roundingMode);
		return data;
	}
	
	public static BigDecimal toMbpsValue(BigInteger bpsData, int scale)
	{
		return toMbpsValue(bpsData, scale, BigDecimal.ROUND_HALF_UP);
	}
	
	public static String toMbps(BigInteger bpsData, int scale)
	{
		return toMbpsValue(bpsData, scale) + "Mb";
	}
	
	public static String toGbps(BigInteger bpsData, int scale)
	{
		BigDecimal data = new BigDecimal(bpsData);
		data = data.divide(new BigDecimal("1024").pow(3)).multiply(new BigDecimal("8"));
		data = data.setScale(scale, BigDecimal.ROUND_HALF_UP);
		return data + "Gbps";
	}

	public static String toTbps(BigInteger bpsData, int scale)
	{
		BigDecimal data = new BigDecimal(bpsData);
		data = data.divide(new BigDecimal("1000").pow(4)).multiply(new BigDecimal("8"));
		data = data.setScale(scale, BigDecimal.ROUND_HALF_UP);
		return data + "Tbps";
	}
	
	public static String toFlowLabel(BigInteger bps, int scale)
	{
		if( bps.compareTo(new BigInteger("1024").multiply(new BigInteger("8")))<0 )
		{
			return toBps(bps, scale);
		}
		else if( bps.compareTo(new BigInteger("1024").pow(2).multiply(new BigInteger("8")))<0 )
		{
			return toKbps(bps, scale);
		}
		else if( bps.compareTo(new BigInteger("1024").pow(3).multiply(new BigInteger("8")))<0 )
		{
			return toMbps(bps, scale);
		}
		else if( bps.compareTo(new BigInteger("1024").pow(4).multiply(new BigInteger("8")))<0 )
		{
			return toGbps(bps, scale);
		}
		else
		{
			return toTbps(bps, scale);
		}
	}
	
	/**
	 * 将23.5bps，23.5Kbps，23.5Mbps，23.5Gbps，23.5Tbps，这样的字符串转化为以bps为单位的数字
	 */
	public static BigInteger fromFlowLabel(String flowLabel)
	{
		Pattern pattern = Pattern.compile("(\\d+(\\.\\d)?)((bps|Kbps|Mbps|Gbps|Tbps))");
		Matcher matcher = pattern.matcher(flowLabel);
		if( matcher.matches()==false )
		{
			throw new IllegalArgumentException("wrong label:["+flowLabel+"]");
		}
		BigDecimal data = new BigDecimal(matcher.group(1));	// 数字部份
		String unit = matcher.group(3);						// 单位部份
		if( "bps".equals(unit) )
		{
			// do nothing
		}
		else if( "Kbps".equals(unit) )
		{
			data = data.multiply(new BigDecimal("1024")).divide(new BigDecimal("8"));
		}
		else if( "Mbps".equals(unit) )
		{
			data = data.multiply(new BigDecimal("1024").pow(2)).divide(new BigDecimal("8"));
		}
		else if( "Gbps".equals(unit) )
		{
			data = data.multiply(new BigDecimal("1024").pow(3)).divide(new BigDecimal("8"));
		}
		else if( "Tbps".equals(unit) )
		{
			data = data.multiply(new BigDecimal("1024").pow(4)).divide(new BigDecimal("8"));
		}
		else
		{
			throw new MyException("wrong unit:["+unit+"]");
		}
		return data.toBigInteger();
	}
	
	public static void main(String[] args)
	{
		System.out.println(fromFlowLabel("1Mbps"));
	}
}
