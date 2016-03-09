package com.zhicloud.op.common.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

 
import com.zhicloud.op.exception.AppException;

public class MD5Util
{

	public static byte[] digest(byte[] bytes)
	{
		try
		{
			byte[] md5bytes = MessageDigest.getInstance("MD5").digest(bytes);
			return md5bytes;
		}
		catch( NoSuchAlgorithmException e )
		{ 
			throw new AppException("失败");
		}
	}
	
	public static String digestToHex(byte[] bytes)
	{
		byte[] md5bytes = digest(bytes);
		return StringUtil.bytesToHex(md5bytes);
	}
	
	public static String digestToBase64(byte[] bytes)
	{
		byte[] md5bytes = digest(bytes);
		return StringUtil.bytesToBase64(md5bytes);
	}
	
	/**
     * 16位md5加密
     */
    public static  String md5_16(String plainText) {
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
          } catch (NoSuchAlgorithmException e) {
         e.printStackTrace();
        }
        return result;
      }
    
    
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

	
	public static void main(String[] args)
	{ 
		System.out.println(md5("zykj@zykjca7ed9e6576317c715108288184【致云科技】致云Zhicloud欢迎您，您的手机验证码是：844879，请及时完成验证。20160224152916910"));
 	}
}
