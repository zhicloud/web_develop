package com.zhicloud.op.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Test {

    public static void main(String[] args) throws InterruptedException {
        try {
            Process p = Runtime.getRuntime().exec("ifconfig");
            //取得输入流 
            InputStream input = p.getInputStream();
            //使用一个读输出流类去读
            InputStreamReader reader = new InputStreamReader(input);
            //用缓冲器读行
            BufferedReader buff = new BufferedReader(reader);
            String line = null;
            StringBuilder sb = new StringBuilder();
            while((line=buff.readLine())!=null){
                sb.append(line);
            }
            input.close();
            reader.close();
            buff.close();
            
            System.out.println(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
