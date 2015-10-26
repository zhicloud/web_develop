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
	
    /** 
    * 复制单个文件 
    * @param oldPath String 原文件路径 如：c:/fqf.txt 
    * @param newPath String 复制后路径 如：f:/fqf.txt 
    * @return boolean 
    */ 
    public static void copyFile(String oldPath, String newPath) { 
        try { 
            int bytesum = 0; 
            int byteread = 0; 
            File oldfile = new File(oldPath); 
            if (oldfile.exists()) { //文件存在时 
                InputStream inStream = new FileInputStream(oldPath); //读入原文件 
                FileOutputStream fs = new FileOutputStream(newPath); 
                byte[] buffer = new byte[1444]; 
                while ( (byteread = inStream.read(buffer)) != -1) { 
                    bytesum += byteread; //字节数 文件大小 
                    fs.write(buffer, 0, byteread); 
                } 
                inStream.close(); 
            } 
        } 
        catch (Exception e) { 
            e.printStackTrace();     
        } 
    } 

    /** 
    * 复制整个文件夹内容 
    * @param oldPath String 原文件路径 如：c:/fqf 
    * @param newPath String 复制后路径 如：f:/fqf/ff 
    * @return boolean 
    */ 
    public static void copyFolder(String oldPath, String newPath) { 
        try { 
            (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹 
            File a=new File(oldPath); 
            String[] file=a.list(); 
            File temp=null; 
            for (int i = 0; i < file.length; i++) { 
                if(oldPath.endsWith(File.separator)){ 
                    temp=new File(oldPath+file[i]); 
                } 
                else{ 
                    temp=new File(oldPath+File.separator+file[i]); 
                } 
                if(temp.isFile()){ 
                    FileInputStream input = new FileInputStream(temp); 
                    FileOutputStream output = new FileOutputStream(newPath + "/" + (temp.getName()).toString()); 
                    byte[] b = new byte[1024 * 5]; 
                    int len; 
                    while ( (len = input.read(b)) != -1) { 
                        output.write(b, 0, len); 
                    } 
                    output.flush(); 
                    output.close(); 
                    input.close(); 
                } 
                if(temp.isDirectory()){//如果是子文件夹 
                    copyFolder(oldPath+"/"+file[i],newPath+"/"+file[i]); 
                } 
            } 
        } 
        catch (Exception e) { 
            // System.out.println("复制整个文件夹内容操作出错"); 
            e.printStackTrace(); 
        } 
    }

	public static void main(String[] args)
	{
		new File("E:\\work\\zhicloud\\svn\\all_bak2\\项目\\运营管理平台\\src\\1.0").mkdirs();
	}

}
