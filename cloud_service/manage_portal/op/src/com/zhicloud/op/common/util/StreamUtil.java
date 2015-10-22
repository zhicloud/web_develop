package com.zhicloud.op.common.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class StreamUtil
{

	/**
	 * 将一个注入流转化为字节数组
	 */
	public static byte[] inputStreamToByteArray(InputStream in) throws IOException
	{
		java.io.ByteArrayOutputStream bos = new java.io.ByteArrayOutputStream();
		byte[] b = new byte[1024];
		while( true )
		{
			int len = in.read(b);
			if( len == -1 )
			{
				break;
			}
			bos.write(b, 0, len);
		}
		return bos.toByteArray();
	}

	/**
	 * 将一个输入流写入到一个文件当中
	 * 
	 * @throws IOException
	 */
	public static void writeInputStreamToFile(InputStream in, File f) throws IOException
	{
		FileOutputStream fos = new FileOutputStream(f);
		try
		{
			byte[] b = new byte[1024];
			while( true )
			{
				int len = in.read(b);
				if( len == -1 )
				{
					break;
				}
				fos.write(b, 0, len);
			}
		}
		finally
		{
			fos.close();
		}
	}

	/**
	 * 将一个字节数组写入到一个文件当中
	 * 
	 * @throws IOException
	 */
	public static void writeByteArrayToFile(byte[] bytes, File f) throws IOException
	{
		if( bytes == null )
		{
			throw new IllegalArgumentException("bytes is null");
		}
		FileOutputStream fos = new FileOutputStream(f);
		try
		{
			fos.write(bytes);
		}
		finally
		{
			fos.close();
		}
	}

	/**
	 * 从一个文件取出流
	 */
	public static InputStream getInputStreamFromFile(File f) throws FileNotFoundException
	{
		FileInputStream fis = new FileInputStream(f);
		return fis;
	}

	/**
	 * 从一个文件取出流
	 */
	public static InputStream getInputStreamFromFile(String fileName) throws FileNotFoundException
	{
		return getInputStreamFromFile(new File(fileName));
	}

	/**
	 * 
	 */
	public static InputStream getInputStreamFromByteArray(byte[] bytes)
	{
		return new ByteArrayInputStream(bytes);
	}

	/**
	 * 
	 */
	public static InputStream getInputStreamFromString(String string, String encoding) throws UnsupportedEncodingException
	{
		byte[] bytes = string.getBytes(encoding);
		return new ByteArrayInputStream(bytes);
	}

}
