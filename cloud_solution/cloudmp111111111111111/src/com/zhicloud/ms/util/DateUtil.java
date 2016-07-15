package com.zhicloud.ms.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 跟时间有关
 */
public class DateUtil
{

	public final static int YEAR         = Calendar.YEAR;
	public final static int MONTH        = Calendar.MONTH;
	public final static int DAY_OF_MONTH = Calendar.DAY_OF_MONTH;
	public final static int DATE         = Calendar.DATE;
	public final static int HOUR         = Calendar.HOUR;
	public final static int MINUTE       = Calendar.MINUTE;
	public final static int SECOND       = Calendar.SECOND;
	
	public final static long MILLISECOND_PER_SECOND = (long)1000;
	public final static long MILLISECOND_PER_MINUTE = (long)1000*60;
	public final static long MILLISECOND_PER_HOUR 	= (long)1000*60*60;
	public final static long MILLISECOND_PER_DAY 	= (long)1000*60*60*24;
	public final static long MILLISECOND_PER_MONTH 	= (long)1000*60*60*24*30;
	public final static long MILLISECOND_PER_YEAR 	= (long)1000*60*60*24*365;
	
	/**
	 * 给日期加上一天
	 * 
	 * @param date
	 * @param amount 
	 * @return
	 */
	public static Date addByDay(Date date, int amount)
	{
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(Calendar.DATE, amount);
		return gc.getTime();
	}
	
	/**
	 * 给时间加上 某个时间段
	 * 
	 * @param date
	 * @param type		时间单位，可以为Calendar.DATE
	 * @param amount 
	 * @return
	 */
	public static Date addTime(Date date, int type, int amount)
	{
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(date);
		gc.add(type, amount);
		return gc.getTime();
	}
	
	/**
	 * 将日期中低于type的部份置0
	 */
	public static Date cutBelow(Date date, int type)
	{
		long ts = date.getTime();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		int y  = cal.get(Calendar.YEAR);
		int m  = cal.get(Calendar.MONTH);
		int d  = cal.get(Calendar.DAY_OF_MONTH);
		int h  = cal.get(Calendar.HOUR_OF_DAY);
		int mi = cal.get(Calendar.MINUTE);
		int s  = cal.get(Calendar.SECOND);
		
		if( type==Calendar.YEAR )
		{
			cal.setTimeInMillis(ts/1000*1000);
			cal.set(y, 0, 1, 0, 0, 0);
			return cal.getTime();
		}
		if( type==Calendar.MONTH )
		{
			cal.setTimeInMillis(ts/1000*1000);
			cal.set(y, m, 1, 0, 0, 0);
			return cal.getTime();
		}
		if( type==Calendar.DATE || type==Calendar.DAY_OF_MONTH || type==Calendar.DAY_OF_WEEK )
		{
			cal.setTimeInMillis(ts/1000*1000);
			cal.set(y, m, d, 0, 0, 0);
			return cal.getTime();
		}
		if( type==Calendar.WEEK_OF_MONTH || type==Calendar.WEEK_OF_YEAR )	// 本周的第一天，星期日视为第一天
		{
			cal.setTimeInMillis(ts/1000*1000);
			cal.set(y, m, d, 0, 0, 0);		// 先获取当天的时间，再送去一些天数的毫秒数就可以得到本周第一天的时间 
			cal.setTimeInMillis( cal.getTimeInMillis() - (cal.get(Calendar.DAY_OF_WEEK) - 1)*MILLISECOND_PER_DAY );
			return cal.getTime();
		}
		if( type==Calendar.HOUR_OF_DAY || type==Calendar.HOUR )
		{
			cal.setTimeInMillis(ts/1000*1000);
			cal.set(y, m, d, h, 0, 0);
			return cal.getTime();
		}
		if( type==Calendar.MINUTE )
		{
			cal.setTimeInMillis(ts/1000*1000);
			cal.set(y, m, d, h, mi, 0);
			return cal.getTime();
		}
		if( type==Calendar.SECOND )
		{
			cal.setTimeInMillis(ts/1000*1000);
			cal.set(y, m, d, h, mi, s);
			return cal.getTime();
		}
		return cal.getTime();
	}
	
	/**
	 * 将字符串转化为Date
	 * 输入格式: "2001-12-11 22:59:00"
	 * @throws ParseException 
	 */
	public static Date stringToDate(String str) throws ParseException
	{
		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").parse(str);
	}
	
	/**
	 * 将字符串转化为Date
	 * @param str		要转化的字符串
	 * @param format	转化格式,如"yyyy-MM-dd HH:mm:ss.SSS" 或 "yyyyMMddHHmmssSSS"
	 */
	public static Date stringToDate(String str, String format) throws ParseException
	{
		if( str==null )
		{
			return null;
		}
		format = (format==null) ? "yyyy-MM-dd HH:mm:ss.SSS" : format;
		return new SimpleDateFormat(format).parse(str);
	}
	
	/**
	 * 将一Date类型的对象，转换为一个 "1998-01-01 01:01:01" 这样的字符串
	 */
	public static String dateToString(Date date)
	{
		return dateToString(date, "yyyy-MM-dd HH:mm:ss.SSS");
	}
	
	public static String dateToString(long date)
	{
		return dateToString(new Date(date));
	}
	
	/**
	 * 将一Date类型的对象，转换为一个字符串
	 * @param format 默认"yyyy-MM-dd HH:mm:ss.SSS", 其它如"yyyyMMddHHmmssSSS"
	 */
	public static String dateToString(Date date, String formate)
	{
		if( date==null )
		{
			return "";
		}
		formate = (formate==null) ? "yyyy-MM-dd HH:mm:ss.SSS" : formate;
		return new SimpleDateFormat(formate).format(date);
	}
	
	public static String dateToString(long date, String formate)
	{
		return dateToString(new Date(date), formate);
	}
	
	/**
	 * 将一个字符串格式的日期从一种格式转化为另一种格式
	 * @param fromFormat: 转化前的格式
	 * @param toFormat:   转化后的格式
	 */
	public static String formatDateString(String dateString, String fromFormat, String toFormat) throws ParseException
	{
		if( dateString==null )
		{
			return null;
		}
		Date date = stringToDate(dateString, fromFormat);
		return dateToString(date, toFormat);
	}
	
	/**
	 * isDateTimeString("2001-01-01 10:03:22")
	 */
	public static boolean isDateTimeString(String str)
	{
		Pattern pattern = Pattern.compile("^([0-9]{4})-([0-9]{2})-([0-9]{2}) ([0-9]{2}):([0-9]{2}):([0-9]{2})$");
		Matcher matcher = pattern.matcher(str);
		if( matcher.find() )
		{
			int d = 0;
			d = Integer.parseInt(matcher.group(2));
			if( d<1 || d > 12 )
			{
				return false;
			}
			d = Integer.parseInt(matcher.group(3));
			if( d<1 || d > 31 )
			{
				return false;
			}
			d = Integer.parseInt(matcher.group(4));
			if( d > 23 )
			{
				return false;
			}
			d = Integer.parseInt(matcher.group(5));
			if( d > 59 )
			{
				return false;
			}
			d = Integer.parseInt(matcher.group(6));
			if( d > 59 )
			{
				return false;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * isDateString("2001-12-01")
	 */
	public static boolean isDateString(String str)
	{
		Pattern pattern = Pattern.compile("^([0-9]{4})-([0-9]{2})-([0-9]{2})$");
		Matcher matcher = pattern.matcher(str);
		if( matcher.find() )
		{
			int d = 0;
			d = Integer.parseInt(matcher.group(2));
			if( d<1 || d > 12 )
			{
				return false;
			}
			d = Integer.parseInt(matcher.group(3));
			if( d<1 || d > 31 )
			{
				return false;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * isTimeString("23:34:22")
	 */
	public static boolean isTimeString(String str)
	{
		Pattern pattern = Pattern.compile("^([0-9]{2}):([0-9]{2}):([0-9]{2})$");
		Matcher matcher = pattern.matcher(str);
		if( matcher.find() )
		{
			int d = 0;
			d = Integer.parseInt(matcher.group(1));
			if( d > 23 )
			{
				return false;
			}
			d = Integer.parseInt(matcher.group(2));
			if( d > 59 )
			{
				return false;
			}
			d = Integer.parseInt(matcher.group(3));
			if( d > 59 )
			{
				return false;
			}
			return true;
		}
		return false;
	}
	
	/**
	 * 传入一个格式为"HH:mm:ss.SSS"的字符串，返回len个时间大于startTime的、"HH:mm:ss.SSS"为strTime指定钟点的Date数组，如：
	 * 
	 * 		getDatesGreaterThan(stringToDate("2014-08-26 01:02:03.444"), "01:02:03.444", 10)
	 * 
	 * 返回：
	 * 		2014-08-27 01:02:03.444
	 * 		2014-08-28 01:02:03.444
	 * 		2014-08-29 01:02:03.444
	 * 		2014-08-30 01:02:03.444
	 * 		2014-08-31 01:02:03.444
	 * 		2014-09-01 01:02:03.444
	 * 		2014-09-02 01:02:03.444
	 * 		2014-09-03 01:02:03.444
	 * 		2014-09-04 01:02:03.444
	 * 		2014-09-05 01:02:03.444
	 * 
	 * @param startTime: 开始时间
	 * @param strTime:   时间格式, "HH:mm:ss.SSS"的格式
	 */
	public static Date[] getDatesGreaterThan(Date startTime, String strTime, int len) throws ParseException
	{
		List<Date> dateList = new ArrayList<Date>();
		Date date = startTime;
		while(true)
		{
			String yyyyMMdd = dateToString(date, "yyyy-MM-dd ");	// 注意后面有个空格
			date = stringToDate(yyyyMMdd+strTime, "yyyy-MM-dd HH:mm:ss.SSS");
			if( date.getTime() > startTime.getTime() )
			{
				dateList.add(date);
			}
			date = new Date( date.getTime() + MILLISECOND_PER_DAY );
			if( dateList.size()>=len )
			{
				break;
			}
		}
		return dateList.toArray(new Date[0]);
	}
	
	
	/**
	 * @throws ParseException 
	 * 
	 */
	public static void main(String[] args) throws Exception 
	{
		Date[] dates = getDatesGreaterThan(stringToDate("2014-08-26 01:02:03.444"), "01:02:03.444", 10);
		for( Date date : dates )
		{
			System.out.println(dateToString(date));
		}
	}
	
}
