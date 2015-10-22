package com.zhicloud.ms.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;


public class LinuxShellUtil {
    private static final Logger logger = Logger.getLogger(LinuxShellUtil.class);
    /**
     * 调用shell是否成功
     * @param shellString
     * @return
     */
    public static boolean callShell(String shellString) {  
        try {  
            Process process = Runtime.getRuntime().exec(shellString);  
            int exitValue = process.waitFor(); 
            return (exitValue==0?true:false);

        } catch (Throwable e) { 
            logger.error("callShell exception:" + e.getMessage());
            return false;
        }  
    }
    
    /**
     * 调用shell并返回结果
     * @param shellString
     * @return
     */
    public static List<String> getCallShell(String shellString){
        String []cmd = new String[]{"/bin/sh", "-c", shellString};
        Process process = null;  
        List<String> processList = new ArrayList<String>();  
        try {  
            process = Runtime.getRuntime().exec(cmd);  
            BufferedReader br = null;
            BufferedInputStream in = null;

            in = new BufferedInputStream(process.getInputStream());
            br = new BufferedReader(new InputStreamReader(in));
            
            String line = "";  
            while ((line = br.readLine()) != null) {  
                processList.add(line);  
            }  
            in.close();
            return processList;
        } catch (IOException e) {
            logger.error("getCallShell exception:" + e.getMessage());
            e.printStackTrace();  
            processList = new ArrayList<String>();
            return processList;
        } 
    }
    public static void main(String[] args) {
        List<String> list = LinuxShellUtil.getCallShell("netstat -ano");
        System.out.println("count:" + list.size());
        for(String s:list){
            System.out.println(s);
        }
        System.out.println("end.");
    }
}
