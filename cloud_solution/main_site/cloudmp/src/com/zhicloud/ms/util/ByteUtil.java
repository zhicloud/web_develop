package com.zhicloud.ms.util;

public class ByteUtil
{

	public static boolean isMask(long value, long mask)
	{
		return (value & mask) > 0;
	}
	
	public static byte[] newBytes(byte[] src, int start, int len)
	{
		byte[] result = new byte[len];
		System.arraycopy(src, start, result, 0, len);
		return result;
	}

	public static void main(String[] args)
	{
		byte[] bytes = "1234567890".getBytes();
		bytes = newBytes(bytes, 1, 6);
		System.out.println(new String(bytes));
	}
}
