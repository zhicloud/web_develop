package com.zhicloud.op.common.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * 跟字符串有关
 * 
 * @author chenhz
 */
public class StringUtil
{

	public static String forNull(String str, String forNull)
	{
		return (str == null) ? forNull : str;
	}

	public static String forNull(String str)
	{
		return forNull(str, "");
	}

	public static String forEmpty(String str, String forEmpty)
	{
		return ("".equals(str)) ? forEmpty : str;
	}

	public static String forEmpty(String str)
	{
		return forEmpty(str, "");
	}
	
	public static String leftPad(String str, int len, char padChar)
	{
		if( str.length()>=len )
		{
			return str;
		}
		StringBuilder sb = new StringBuilder();
		int cn = len - str.length();
		for( int i=0; i<cn; i++ )
		{
			sb.append(padChar);
		}
		sb.append(str);
		return sb.toString();
	}
	
	public static String rightPad(String str, int len, char padChar)
	{
		if( str.length()>=len )
		{
			return str;
		}
		StringBuilder sb = new StringBuilder(str);
		int cn = len - str.length();
		for( int i=0; i<cn; i++ )
		{
			sb.append(padChar);
		}
		return sb.toString();
	}
	
	/**
	 * 取左边的长度为len的子串，如果长度不足len，刚补字符padChar
	 */
	public static String leftSubstring(String str, int len, String padChar)
	{
		if( str.length()==len )
		{
			return str;
		}
		if( str.length()>len )
		{
			return str.substring(0, len);
		}
		if( padChar==null || padChar.length()!=1 )
		{
			return str;
		}
		else
		{
			return rightPad(str, len, padChar.charAt(0));
		}
		
	}
	
	/**
	 * 取右边的长度为len的子串，如果长度不足len，刚补字符padChar
	 */
	public static String rightSubstring(String str, int len, String padChar)
	{
		if( str.length()==len )
		{
			return str;
		}
		if( str.length()>len )
		{
			return str.substring(str.length() - len);
		}
		if( padChar==null || padChar.length()!=1 )
		{
			return str;
		}
		else
		{
			return leftPad(str, len, padChar.charAt(0));
		}
	}


	/**
	 * 判断是否是空串，"   \t  \r   \n  \r\n" 也算是空串
	 */
	public static boolean isBlank(String input)
	{
		if( input==null )
		{
			return true;
		}
		for( int i = 0; i < input.length(); i++ )
		{ 	
			char c = input.charAt(i);
			// 在ascii表时小于等于' '的都视为空串
			if( c>' ' )
			{
				return false;
			}
		}
		return true;
	}

	/*
	 * 判断是否是整数
	 */
	public static boolean isInt(String str)
	{
		char c1 = str.charAt(0);
		int i = 0;
		if( c1 == '-' )
		{
			i++;
		}
		for( ; i < str.length(); i++ )
		{
			char c = str.charAt(i);
			if( c >= '0' && c <= '9' )
			{
				continue;
			}
			else
			{
				return false;
			}
		}
		return true;
	}

	/*
	 * 判断是否是整数
	 */
	public static boolean isUnsignedInt(String str)
	{
		for( int i = 0; i < str.length(); i++ )
		{
			char c = str.charAt(i);
			if( c >= '0' && c <= '9' )
			{
				continue;
			}
			else
			{
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断字符串是否为浮点数
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isFloat(String str)
	{
		String pattern = "^(-?\\d+)(\\.\\d+)?$";
		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(str);
		return m.find();
	}

	/*
	 * 判断是否是ip
	 */
	public static boolean isIP(String str)
	{
		String[] arr = split(str, "\\.");
		if( arr.length != 4 )
		{
			return false;
		}
		for( int i = 0; i < arr.length; i++ )
		{
			try
			{
				int num = Integer.parseInt(arr[i]);
				if( num < 0 || num > 255 )
				{
					return false;
				}
			}
			catch( NumberFormatException e )
			{
				return false;
			}
		}

		return true;
	}
	
	/**
	 * 
	 */
	public static boolean isEquals(String str1, String str2)
	{
		if( str1==null && str2==null )
		{
			return true;
		}
		if( str1!=null )
		{
			return str1.equals(str2);
		}
		else
		{
			return str2.equals(str1);
		}
	}
	
	/**
	 * 
	 */
	public static boolean isEqualsIgnoreCase(String str1, String str2)
	{
		if( str1==null && str2==null )
		{
			return true;
		}
		if( str1!=null )
		{
			return str1.equalsIgnoreCase(str2);
		}
		else
		{
			return str2.equalsIgnoreCase(str1);
		}
	}


	/**
	 * 
	 */
	public static String showLimitedString(String input, int len)
	{
		if( input == null )
		{
			return "";
		}
		else if( input.length() > len )
		{
			return input.substring(0, len - 1) + "..";
		}
		else
		{
			return input;
		}
	}

	/**
	 * 
	 */
	public static String substring(String str, int start, int end)
	{
		if( str == null )
		{
			return "";
		}
		if( start > str.length() )
		{
			return "";
		}
		if( end > str.length() )
		{
			return str.substring(start);
		}
		else
		{
			return str.substring(start, end);
		}
	}

	/**
	 * 
	 */
	public static String substring(String str, int start)
	{
		if( str == null )
		{
			return "";
		}
		if( start > str.length() )
		{
			return "";
		}
		return str.substring(start);
	}

	// ----------------

	public static long JOIN_WRAP_PREPROCESS_FLAG_REPLACE_NULL = 0x01;
	public static long JOIN_WRAP_PREPROCESS_FLAG_TRIM = 0x01 << 1;
	public static long JOIN_WRAP_PREPROCESS_FLAG_URLENCODE = 0x01 << 2;
	public static long JOIN_WRAP_PREPROCESS_FLAG_URLDECODE = 0x01 << 3;
	public static long JOIN_WRAP_PREPROCESS_FLAG_DOUBLE_SINGLE_QUOTE_MARK = 0x01 << 4;

	public static String joinWrapPreprocess(String str, long flag)
	{
		String result = str;
		if( ByteUtil.isMask(flag, JOIN_WRAP_PREPROCESS_FLAG_REPLACE_NULL) )
		{
			result = StringUtil.forNull(str);
		}
		if( result == null )
		{
			return result;
		}
		if( ByteUtil.isMask(flag, JOIN_WRAP_PREPROCESS_FLAG_TRIM) )
		{
			result = str.trim();
		}
		if( ByteUtil.isMask(flag, JOIN_WRAP_PREPROCESS_FLAG_URLENCODE) )
		{
			result = StringUtil.urlEncoder(str);
		}
		if( ByteUtil.isMask(flag, JOIN_WRAP_PREPROCESS_FLAG_URLDECODE) )
		{
			result = StringUtil.urlDecoder(str);
		}
		if( ByteUtil.isMask(flag, JOIN_WRAP_PREPROCESS_FLAG_DOUBLE_SINGLE_QUOTE_MARK) )
		{
			result = str.replace("'", "''");
		}
		return result;
	}

	public static String joinWrap(Iterable<?> iterable, String seperator, String wrapper, long flag)
	{
		StringBuffer sb = new StringBuffer();
		Iterator<?> iter = iterable.iterator();
		if( iter.hasNext() )
		{
			String str = null;
			str = joinWrapPreprocess(String.valueOf(iter.next()), flag);
			sb.append(wrapper).append(str).append(wrapper);
			while( iter.hasNext() )
			{
				str = joinWrapPreprocess(String.valueOf(iter.next()), flag);
				sb.append(seperator).append(wrapper).append(str).append(wrapper);
			}
		}
		return sb.toString();
	}

	public static String joinWrap(Iterable<?> iterable, String seperator, String wrapper)
	{
		return joinWrap(iterable, seperator, wrapper, 0);
	}

	public static String joinWrap(Iterable<?> iterable, String seperator)
	{
		return joinWrap(iterable, seperator, "", 0);
	}

	public static String joinWrap(Iterable<?> iterable)
	{
		return joinWrap(iterable, ",", "", 0);
	}

	public static String joinWrap(Object[] arr, String seperator, String wrapper, long flag)
	{
		return joinWrap(Arrays.asList(arr), seperator, wrapper, flag);
	}

	public static String joinWrap(Object[] arr, String seperator, String wrapper)
	{
		return joinWrap(arr, seperator, wrapper, 0);
	}

	public static String joinWrap(Object[] arr, String seperator)
	{
		return joinWrap(arr, seperator, "", 0);
	}

	public static String joinWrap(Object[] arr)
	{
		return joinWrap(arr, ",", "", 0);
	}

	// ----------------

	/**
	 * 将str切分以后,放到一个String[]里面去
	 */
	public static String[] split(String str, String seperator)
	{
		if( "".equals(str) )
		{
			return new String[0];
		}
		else
		{
			return str.split(seperator);
		}
	}

	/**
	 * 将str切分以后,每个字符串trim以后放到一个String[]里面去,空字符串会被过滤
	 */
	public static String[] splitTrim(String str, String seperator)
	{
		if( "".equals(str) )
		{
			return new String[0];
		}
		else
		{
			String[] arr = str.split(seperator);
			List<String> list = new ArrayList<String>();
			for( String s : arr )
			{
				s = StringUtil.trim(s);
				if( "".equals(s) == false )
				{
					list.add(s);
				}
			}
			return list.toArray(new String[0]);
		}
	}

	/**
	 * 
	 */
	public static String[] fillInArray(String[] ss, String s)
	{
		for( int i = 0; i < ss.length; i++ )
		{
			ss[i] = s;
		}
		return ss;
	}
	
	
	
	
	
	
	
	

	/**
	 * 将一Date类型的对象，转换为一个 "1998-01-01 01:01:01" 这样的字符串
	 */
	public static String dateToString(Date date)
	{
		return DateUtil.dateToString(date);
	}

	/**
	 * 将一Date类型的对象，转换为一个字符串
	 * 
	 * @param format
	 *            默认"yyyy-MM-dd HH:mm:ss.SSS"
	 */
	public static String dateToString(Date date, String format)
	{
		return DateUtil.dateToString(date, format);
	}
	
	
	
	/**
	 * 将字符串转化为Date
	 * @param str		要转化的字符串
	 * @param format	转化格式,如"yyyy-MM-dd HH:mm:ss.SSS"
	 */
	public static Date stringToDate(String str, String format) throws ParseException
	{
		return DateUtil.stringToDate(str, format);
	}
	
	/**
	 * 将字符串转化为Date
	 * 输入格式: "2001-12-11 22:59:00"
	 * @throws ParseException 
	 */
	public static Date stringToDate(String str) throws ParseException
	{
		return DateUtil.stringToDate(str);
	}
	
	
	/**
	 * 将一个字符串格式的日期从一种格式转化为另一种格式
	 * @param fromFormat: 转化前的格式
	 * @param toFormat:   转化后的格式
	 */
	public static String formatDateString(String dateString, String fromFormat, String toFormat) throws ParseException
	{
		return DateUtil.formatDateString(dateString, fromFormat, toFormat);
	}
	
	
	
	

	/**
	 * 若输入为字符为null,则输出forNull
	 */
	public static String trim(Object str, String forNull)
	{
		if( str==null )
		{
			return forNull;
		}
		else
		{
			return str.toString().trim();
		}
	}

	/**
	 * 若输入为字符为null,则输出""
	 */
	public static String trim(Object str)
	{
		return trim(str, "");
	}

	/**
	 * 若输入为字符为null,则输出forNull
	 */
	public static String escapeTrim(String str, String forNull)
	{
		if( str == null )
		{
			return forNull;
		}
		else
		{
			return escape(str);
		}
	}

	/**
	 * 
	 */
	public static String escapeTrim2(String str, String forNull)
	{
		if( str == null || str.trim().equals("") )
		{
			return forNull;
		}
		else
		{
			return escape(str);
		}
	}

	/**
	 * 若输入为字符为null,则输出""
	 */
	public static String escapeTrim(String str)
	{
		return escapeTrim(str, "");
	}

	/**
	 * 若输入为字符为null,则输出forNull
	 */
	public static String sqlTrim(String str, String forNull)
	{
		if( str == null )
		{
			return forNull;
		}
		else
		{
			return str.trim().replace("\'", "\'\'");
		}
	}

	/**
	 * 若输入为字符为null,则输出""
	 */
	public static String sqlTrim(String str)
	{
		return sqlTrim(str, "");
	}

	/**
	 * 将一个字符转化为可以在代码里书写的字面量,主要对下面几个特殊字符进行转换 \, /, \r, \n, \b, \f, \t, \", \'
	 */
	public static String escape(String s)
	{
		if( s == null )
		{
			return null;
		}
		StringBuffer sb = new StringBuffer();
		escape(s, sb);
		return sb.toString();
	}

	/**
	 * 将一个字符转化为可以在代码里书写的字面量,主要对下面几个特殊字符进行转换 \, /, \r, \n, \b, \f, \t, \", \'
	 */
	public static void escape(String s, StringBuffer sb)
	{
		for( int i = 0; i < s.length(); i++ )
		{
			char ch = s.charAt(i);
			switch( ch )
			{
				case '"':
					sb.append("\\\"");
					break;
				case '\\':
					sb.append("\\\\");
					break;
				case '\b':
					sb.append("\\b");
					break;
				case '\f':
					sb.append("\\f");
					break;
				case '\n':
					sb.append("\\n");
					break;
				case '\r':
					sb.append("\\r");
					break;
				case '\t':
					sb.append("\\t");
					break;
				case '/':
					sb.append("\\/");
					break;
				default:
					if( (ch >= '\u0000' && ch <= '\u001F') || (ch >= '\u007F' && ch <= '\u009F') || (ch >= '\u2000' && ch <= '\u20FF') )
					{
						String ss = Integer.toHexString(ch);
						sb.append("\\u");
						for( int k = 0; k < 4 - ss.length(); k++ )
						{
							sb.append('0');
						}
						sb.append(ss.toUpperCase());
					}
					else
					{
						sb.append(ch);
					}
			}
		}
	}

	/**
	 * 如果preString为ifStr,则返回toString,否则返回原来的preString
	 */
	public static String instead(String preString, String ifStr, String toString)
	{
		if( preString == null )
		{
			if( ifStr == null )
			{
				return toString;
			}
			else
			{
				return "null";
			}
		}
		else
		{
			if( preString.equals(ifStr) )
			{
				return toString;
			}
			else
			{
				return preString;
			}
		}
	}


	/**
	 * 
	 * @param str
	 *            文件全名，如 "/template/hello.exe" 或者 "c:/template.hello.exe"
	 * @return 去掉路径的文件名，如："hello.exe"
	 */
	public static String getFileName(String str)
	{
		str = str.replace("\\", "/");
		String[] arr = str.split("/");
		return arr[arr.length - 1];

	}

	/**
	 * 
	 * @param firstStr
	 * @param replaceStr
	 * @param targetStr
	 * @return
	 */
	public static String replaceFirstChar(String firstStr, String replaceStr, String targetStr)
	{
		if( targetStr.startsWith(firstStr) )
		{
			targetStr = targetStr.replaceFirst(firstStr, replaceStr);
		}
		return targetStr;
	}

	/**
	 * 将字节流转换为base64的编码
	 */
	public static String bytesToBase64(byte[] b){
		if( b == null )
		{
			return null;
		}
		return new BASE64Encoder().encode(b);
	}

	/**
	 * 将base64的编码转换为字节流
	 */
	public static byte[] base64ToBytes(String s) throws IOException
	{
		if( s == null )
		{
			return null;
		}
		return new BASE64Decoder().decodeBuffer(s);
	}

	/**
	 * 解析url?后的参数,如 key1=value1&key2=value2 这样的字符串
	 */
	public static Map<String, String> getUrlParameters(String param)
	{
		Map<String, String> paramMap = new TreeMap<String, String>();
		if( param == null )
		{
			return paramMap;
		}
		String[] arr = param.split("&");
		for( int i = 0; i < arr.length; i++ )
		{
			try
			{
				String s = arr[i];
				if( !s.equals("") )
				{
					int pos = s.indexOf("=");
					if( pos == -1 )
					{ // 找不到
						String key = s;
						String value = null;
						paramMap.put(key, value);
					}
					else
					{ // 找到
						String key = substring(s, 0, pos);
						String value = substring(s, pos + 1, s.length());
						paramMap.put(key, URLDecoder.decode(value, "utf-8"));
					}
				}
			}
			catch( UnsupportedEncodingException e )
			{
				e.printStackTrace();
			}
		}
		return paramMap;
	}

	/**
	 * 
	 */
	public static String generateSessionId()
	{
		byte[] chars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		Random ran = new Random();
		byte[] bytes = new byte[32];
		for( int i = 0; i < 32; i++ )
		{
			bytes[i] = chars[Math.abs(ran.nextInt()) % chars.length];
		}
		return new String(bytes);
	}

	/**
	 * 
	 */
	public static String generateRandom(int len)
	{
		byte[] chars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', };
		Random ran = new Random();
		byte[] bytes = new byte[len];
		for( int i = 0; i < len; i++ )
		{
			bytes[i] = chars[Math.abs(ran.nextInt()) % chars.length];
		}
		return new String(bytes);
	}
	
	/**
	 * uuid不带有"-"
	 */
	public static String generateUUID()
	{
		return generateUUID(0);
	}

	/**
	 * uuid不带有"-"
	 */
	public static String generateUUID(int len)
	{
		if( len==0 )
		{
			return UUID.randomUUID().toString().replace("-", "");
		}
		else
		{
			return UUID.randomUUID().toString().replace("-", "") + generateRandom(len);
		}
	}
	
	/**
	 * 
	 */
	public static String generateUUID3()
	{
		return generateUUID3(17);
	}

	/**
	 * len可以使用17,这样能生成一个长度为32的字符串
	 */
	public static String generateUUID3(int len)
	{
		return new SimpleDateFormat("yyMMddHHmmssSSS").format(new Date()) + generateRandom(len);
	}

	/**
	 * 作用类似于数据库的like条件 如: S.like("1111abc222", "%abc%") -> true
	 */
	public static boolean like(String input, String sPattern)
	{
		Pattern pattern = Pattern.compile(sPattern.replace("%", "(.*)").replace("_", "(.)"));
		Matcher matcher = pattern.matcher(input);
		return matcher.matches();
	}

	/**
	 * 
	 */
	public static String stringToUnicode(String preStr)
	{
		StringBuffer result = new StringBuffer();
		;
		for( int i = 0; i < preStr.length(); i++ )
		{
			String s = Integer.toHexString(preStr.charAt(i)).toUpperCase();
			result.append("\\u").append(String.format("%4s", s).replace(" ", "0"));
		}
		return result.toString();
	}

	/**
	 * 
	 */
	public static String unicodeToString(String s)
	{
		String[] arr = s.split("\\\\u");
		StringBuffer result = new StringBuffer();
		for( int i = 0; i < arr.length; i++ )
		{
			if( !"".equals(arr[i]) )
			{
				result.append((char) Integer.parseInt(arr[i], 16));
			}
		}
		return result.toString();
	}

	/**
	 * 
	 * @param ip
	 *            要检测的IP
	 * @param format
	 *            要检测的格式,如: 10.*;172.*
	 * @return
	 */
	public static boolean isIpMatchFormat(String ip, String format)
	{
		if( ip == null || format == null )
		{
			return false;
		}
		String[] arr = format.split(";");
		for( String s : arr )
		{
			s = s.trim().replace("*", "");
			if( ip.startsWith(s) )
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * 获取带省略号的字符串。 比如：S.getSkipSubString("abcdefghijk", 5) -> 返回字符串：abcde...
	 * S.getSkipSubString("abcde", 5) -> 返回字符串：abcde
	 * 
	 * @param str
	 * @param len
	 * @return
	 */
	public static String getSkipSubString(String str, int len)
	{
		String subStr;

		if( str == null )
		{
			subStr = "";
		}
		else if( str.length() <= len )
		{
			subStr = str;
		}
		else
		{
			subStr = str.substring(0, len) + "..";
		}

		return subStr;
	}

	/**
	 * 将第一个字母替换成大写
	 * 
	 * @param str
	 * @return
	 */
	public static String upperFirst(String str)
	{
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}

	/**
	 * 将第一个字母替换成小写
	 * 
	 * @param str
	 * @return
	 */
	public static String lowerFirst(String str)
	{
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}

	//---------------

	/**
	 * 
	 */
	public static String bytesToHex(byte[] bytes)
	{
		char[] chars = new char[] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		for( int i = 0; i < bytes.length; i++ )
		{
			byte b = bytes[i];
			sb.append(chars[b >> 4 & 0xf]);
			sb.append(chars[b & 0xf]);
		}
		return sb.toString();
	}

	/**
	 * 
	 */
	public static byte[] hexToBytes(String hex)
	{
		if( hex.length() % 2 != 0 )
		{
			throw new RuntimeException("字符的长度必须可以被2整除, length:[" + hex.length() + "]");
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		for( int i=0; i<hex.length(); i+=2 )
		{
			String s = hex.substring(i, i+2);
			bos.write(Integer.parseInt(s, 16) & 0xff);
		}
		return bos.toByteArray();
	}
	
	//--------------
	
	public static String urlEncoder(String s)
	{
		try
		{
			return URLEncoder.encode(s, "utf-8");
		}
		catch( UnsupportedEncodingException e )
		{
			throw new RuntimeException(e);
		}
	}

	public static String urlDecoder(String s)
	{
		try
		{
			return URLDecoder.decode(s, "utf-8");
		}
		catch( UnsupportedEncodingException e )
		{
			throw new RuntimeException(e);
		}
	}

	//--------------

	public static String myUrlEncoder(String s)
	{
		try
		{
			return URLEncoder.encode(s, "utf-8").replace("-", "%2D").replace("%", "-");
		}
		catch( UnsupportedEncodingException e )
		{
			throw new RuntimeException(e);
		}
	}

	public static String myUrlDecoder(String s)
	{
		try
		{
			return URLDecoder.decode(s.replace("-", "%"), "utf-8");
		}
		catch( UnsupportedEncodingException e )
		{
			throw new RuntimeException(e);
		}
	}
	
	//-------------
	
	public static Integer parseInteger(String sInt, Integer defaultValue)
	{
		try
		{
			return Integer.valueOf(sInt);
		}
		catch( NumberFormatException e )
		{
			return defaultValue;
		}
	}
	
	//-----------------
	
	/**
	 * 判断是不是形如"ip:port"的字符串，如：
	 * 	127.0.0.1:8080
	 * 	www.sina.com:9081
	 */
	public static boolean isDomainNameWithPort(String str)
	{
		return Pattern.matches("^(\\w+(\\.\\w+)*)(:\\d+)?$", str);
	}
	
	/**
	 *  获取发票的单据号
	 * 	参数时间和序号
	 * 	yyyyMMddHHmmssNNNN
	 */	
	public static String getSerialNo(Date date, Integer i)
	{
		return DateUtil.dateToString(date,"yyyyMMddHHmm")+StringUtil.leftPad(String.valueOf(i),3,'0');
	}

	/* **********************************************************
	 * 
	 * 这里是公共函数，写完后记得加函数说明，这样其它人也可以使用了，谢谢
	 * 
	 * **********************************************************
	 */

	/**
	 * @throws UnsupportedEncodingException 
	 * 
	 */
	public static void main(String[] args) throws Exception
	{
		boolean b = isDomainNameWithPort("w.sina.dd");
		String serialNo =  DateUtil.dateToString(new Date(),"yyyyMMddHHmmss")+StringUtil.leftPad("1",4,'0');
		System.out.println(serialNo);		
		
	}


}
