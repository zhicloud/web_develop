package com.zhicloud.ms.util;

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
	public static String md5(String str) {
		BigInteger rel = null;
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			rel = new BigInteger(md.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			throw new MyException("加密失败"+e.getMessage());
		}
		return rel.toString(16);
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
	
	public static void main(String[] args) {
		System.out.println(MD5.md5("username", "password"));
		System.out.println(MD5.md5("usernamepassword"));
	}
}
