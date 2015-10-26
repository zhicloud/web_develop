package com.zhicloud.ms.common.util;

import java.util.Random;

public class RandomPassword {
	
	 /**
	  * 生成随即密码
	  * @param pwd_len 生成的密码的总长度
	  * @return  密码的字符串
	  */
	  public static String getRandomNum(int pwd_len){
	  //71是因为数组是从0开始的，52个字母+10个 数字+10个特殊字符
	  final int  maxNum = 10;
	  int i;  //生成的随机数
	  int count = 0; //生成的密码的长度
	  char[] str = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
	  StringBuffer pwd = new StringBuffer("");
	  Random r = new Random();
	  while(count < pwd_len){
	   //生成随机数，取绝对值，防止 生成负数，
	   i = Math.abs(r.nextInt(maxNum)); 
		   if (i >= 0 && i < str.length) 
		   {
		    pwd.append(str[i]);
		    count ++;
		   }
	  }
	  return pwd.toString();
	 }
	  
	  public static String getRandomPwd(int pwd_len){
	  //71是因为数组是从0开始的，52个字母+10个 数字+10个特殊字符
	  final int  maxNum = 62;
	  int i;  //生成的随机数
	  int count = 0; //生成的密码的长度
	  char[] str = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
			        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
			        'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
			        'u', 'v', 'w', 'x', 'y', 'z',
			        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
			        'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
			        'U', 'V', 'W', 'X', 'Y', 'Z'};
	  
	  StringBuffer pwd = new StringBuffer("");
	  Random r = new Random();
	  while(count < pwd_len){
	   //生成随机数，取绝对值，防止 生成负数，
	   i = Math.abs(r.nextInt(maxNum));  
		   if (i >= 0 && i < str.length) 
		   {
		    pwd.append(str[i]);
		    count ++;
		   }
	  }
	  return pwd.toString();
	 }
	  
	  public static String toChangeEncrypt(String password) 
	  {
		    StringBuilder pwStringBuilder= new StringBuilder();
			for (int i = 0; i < password.length()-1; i=i+6) 
			{
				pwStringBuilder.append(password.substring(i, i+5));
			}
			return pwStringBuilder.toString();
	  }
	  
}

