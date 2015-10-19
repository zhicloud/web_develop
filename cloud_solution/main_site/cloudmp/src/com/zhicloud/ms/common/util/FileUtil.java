package com.zhicloud.ms.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 跟文件操作有关
 */
public class FileUtil
{
	
	public static boolean mkdirs(File dir)
	{
		return dir.mkdirs();
	}

	/*
	 * 实现文件的复制
	 */
	public static void copyFile(File prefile, File copyfile) throws IOException
	{
		FileInputStream fis = null;
		FileOutputStream fos = null;
		try
		{
			fis = new FileInputStream(prefile);
			fos = new FileOutputStream(copyfile);
			byte[] b = new byte[1024];
			int len = 0;
			while( true )
			{
				len = fis.read(b);
				if( len == -1 )
				{
					break;
				}
				fos.write(b, 0, len);
			}
		}
		finally
		{
			if( fis != null )
			{
				fis.close();
			}
			if( fos != null )
			{
				fos.close();
			}
		}
	}

	/**
	 * 
	 */
	public static byte[] getBytesFromFile(File f) throws IOException
	{
		InputStream in = null;
		byte[] bytes = null;
		try
		{
			in = StreamUtil.getInputStreamFromFile(f);
			bytes = StreamUtil.inputStreamToByteArray(in);
		}
		finally
		{
			if( in != null )
			{
				in.close();
			}
		}
		return bytes;
	}

	/**
	 * 删除文件或者目录
	 * 
	 * @param file
	 *            ：要删除的文件或者目录
	 * @param delete
	 *            ：如果file是目录，删除目录的内容之后是否删除本目录
	 */
	public static void deleteDirectory(File file, boolean delete)
	{
		if( file.isFile() )
		{
			file.delete();
		}
		else if( file.isDirectory() )
		{
			File[] files = file.listFiles();
			for( File afile : files )
			{
				if( afile.isFile() )
				{
					afile.delete();
				}
				else
				{
					deleteDirectory(afile, false);
					afile.delete();
				}
			}
			if( delete )
			{
				file.delete();
			}
		}
	}

	/**
	 * 
	 */
	public static void writeBytesToFile(File file, byte[] bytes) throws IOException
	{
		file.getAbsoluteFile().getParentFile().mkdirs();
		FileOutputStream fos = null;
		try
		{
			fos = new FileOutputStream(file);
			fos.write(bytes);
		}
		finally
		{
			if( fos != null )
			{
				fos.close();
			}
		}
	}

	
	/**
	 * @throws IOException
	 * 
	 */
	public static void renameFile(File fromFile, File toFile) throws IOException
	{
		if( fromFile.exists() == false )
		{
			throw new IOException("源文件不存在: " + fromFile.getAbsolutePath());
		}
		if( toFile.exists() )
		{
			throw new IOException("目标文件已经存在: " + toFile.getAbsolutePath());
		}
		boolean b = fromFile.renameTo(toFile); // renameTo方法不可靠, 有时会改名失败
		if( b == false )
		{ 	// 如果失败,则通过文件copy的方式改名
			copyFile(fromFile, toFile);
			fromFile.delete();
		}
	}

	/**
	 * 
	 */
	public static void loopSaveToFile(final byte[] bytes, final File saveFile, final int backupCount)
	{
		new Runnable()
		{
			public void run()
			{
				try
				{
					saveFile.getParentFile().mkdirs();
					if( backupCount > 0 )
					{
						// 日志文件的备份数有限最大为loopCount
						if( saveFile.exists() )
						{
							loopRename();
						}
						writeBytesToFile(saveFile, bytes);
					}
					else
					{
						throw new RuntimeException("backupCount=[" + backupCount + "]");
					}
				}
				catch( Exception e )
				{
					throw new RuntimeException(e);
				}
			}

			//
			private void loopRename() throws IOException
			{
				File bakLoggerFile = new File(saveFile.getAbsolutePath() + "." + backupCount);
				if( !bakLoggerFile.exists() )
				{
					renameFile(saveFile, bakLoggerFile);
				}
				else
				{
					new File(saveFile.getAbsolutePath() + ".1").delete();
					for( int i = 2; i <= backupCount; i++ )
					{
						File tempFile = new File(saveFile.getAbsolutePath() + "." + i);
						File toNameFile = new File(saveFile.getAbsolutePath() + "." + (i - 1));
						if( tempFile.exists() )
						{
							renameFile(tempFile, toNameFile);
						}
					}
					renameFile(saveFile, bakLoggerFile);
				}
			}
		}.run();
	}
	/**
	 * 获取文件夹和文件大小
	 */
	public static double getDirSize(File file) {     
        //判断文件是否存在     
        if (file.exists()) {     
            //如果是目录则递归计算其内容的总大小    
            if (file.isDirectory()) {     
                File[] children = file.listFiles();     
                double size = 0;     
                for (File f : children)     
                    size += getDirSize(f);     
                return size;     
            } else {//如果是文件则直接返回其大小 
                double size = (double) file.length();      
                return size;     
            }     
        } else {     
            return 0.0;     
        }     
    }     

	public static void main(String[] args)
	{
		new File("E:\\work\\zhicloud\\svn\\all_bak2\\项目\\运营管理平台\\src\\1.0").mkdirs();
	}

}
