package com.zhicloud.ms.thread;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
 
 
/**
 * 删除服务器上的文件
 *  
 */
//实现Runnable接口(推荐)，可以线程接口，预留一个extends(继承)，方便扩展
public class  LogFileManager implements Runnable {
    private static String path;//路径
 
    private static String RETENTION_TIME = "0";// 文件保存的时间 
    /**
     * 构造函数。初始化参数
     * @param path
     */
    @SuppressWarnings("static-access")
    public LogFileManager(String path) {
        this.path = path; 
    }
    /**
     * 把线程要执行的代码放在run()中
     */
    public void run() {  
        File file = new File(path);
        deletefiles(file);
    }
 
    /**
     * 批量删除日志文件
     * 
     * @param folder
     */
    public void deletefiles(File folder) {
        File[] files = folder.listFiles(); 
        for (int i = 0; i < files.length; i++) {
        	if(files[i].getName().contains("zhicloud.op.log")){
         		Calendar now = Calendar.getInstance();
        		Calendar file_time=Calendar.getInstance();
        		Date time_date = new Date(files[i].lastModified());
        		file_time.setTime(time_date);
        		long gap = (now.getTimeInMillis()-file_time.getTimeInMillis())/(1000*3600*24);
        		//两天前的日志将被删除
        		if(gap>2){        			
         			deleteFile(files[i]);        			
        		}
        		
        	} 
        }
    }
 
    /**
     * 删除文件
     * 
     * @param file
     */
    private void deleteFile(File file) {
        try {
            if (file.isFile()) {
                // 删除符合条件的文件
                if (canDeleteFile(file)) {
                    if (file.delete()) {
                        System.out.println("文件" + file.getName() + "删除成功!");
                    } else {
                        System.out.println("文件" + file.getName()
                                + "删除失败!此文件可能正在被使用");
                    }
                } else {
 
                }
            } else {
                System.out.println("没有可以删除的文件了");
            }
 
        } catch (Exception e) {
            System.out.println("删除文件失败========");
            e.printStackTrace();
        }
    }
 
    /**
     * 判断文件是否能够被删除
     */
    private boolean canDeleteFile(File file) {
        Date fileDate = getfileDate(file);
        Date date = new Date();
        long time = (date.getTime() - fileDate.getTime()) / 1000 / 60
                - Integer.parseInt(RETENTION_TIME);// 当前时间与文件间隔的分钟
        if (time > 0) {
            return true;
        } else {
            return false;
        }
 
    }
 
    /**
     * 获取文件最后的修改时间
     * 
     * @param file
     * @return
     */
    private Date getfileDate(File file) {
        long modifiedTime = file.lastModified();
        Date d = new Date(modifiedTime);
        return d;
    }
  
  
 
}