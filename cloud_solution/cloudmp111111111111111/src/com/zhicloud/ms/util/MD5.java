package com.zhicloud.ms.util;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.zhicloud.ms.exception.MyException;

/**
 * @author ZYFTMX
 * MD5加密
 */
public class MD5 {
	/**
	 * 对单个字符串进行加密
	 * @param str
	 * @return
	 */
    public static  String md5(String plainText) {
        String result = null;
        try {
         MessageDigest md = MessageDigest.getInstance("MD5");
         md.update(plainText.getBytes("utf-8"));
         byte b[] = md.digest();
         int i;
         StringBuffer buf = new StringBuffer("");
         for (int offset = 0; offset < b.length; offset++) {
          i = b[offset];
          if (i < 0)
           i += 256;
          if (i < 16)
           buf.append("0");
          buf.append(Integer.toHexString(i));
         }  
         result =  buf.toString();
         System.out.println(result);
        } catch (Exception e) {
         e.printStackTrace();
        }
        return result;
    }
	
	
	/**
	 * 使用用户名和密码同时加密
	 * @param str1
	 * @param str2
	 * @return
	 */
	public static String md5(String str1, String str2) {
		String re_md5 = new String();
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update((str1+str2).getBytes());
            byte b[] = md.digest();
 
            int i;
 
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
 
            re_md5 = buf.toString();
 
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return re_md5;

	}
	/**
	 * 16位md5加密
	 */
	public static String md5_16(String plainText) {
	    String result = null;
	    try {
	     MessageDigest md = MessageDigest.getInstance("MD5");
	     md.update(plainText.getBytes());
	     byte b[] = md.digest();
	     int i;
	     StringBuffer buf = new StringBuffer("");
	     for (int offset = 0; offset < b.length; offset++) {
	      i = b[offset];
	      if (i < 0)
	       i += 256;
	      if (i < 16)
	       buf.append("0");
	      buf.append(Integer.toHexString(i));
	     }
	     // result = buf.toString();  //md5 32bit
	     // result = buf.toString().substring(8, 24))); //md5 16bit
	     result = buf.toString().substring(8, 24);
	     System.out.println("mdt 16bit: " + buf.toString().substring(8, 24));
	     System.out.println("md5 32bit: " + buf.toString() );
	    } catch (NoSuchAlgorithmException e) {
	     e.printStackTrace();
	    }
	    return result;
	  }
	
	public static void main(String[] args) {
		System.out.println(MD5.md5("username", "password"));
		System.out.println(MD5.md5("usernamepassword"));
		System.out.println(MD5.md5_16("usernamepassword"));
	}
}
