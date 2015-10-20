package com.zhicloud.ms.common.util;
/**
 */
import java.security.Key;
import java.security.Security;
import javax.crypto.Cipher;
public class DesActivate {
	private static String strDefaultKey = "national";
	private Cipher ecipher = null;
	private Cipher dcipher = null;
	
	private Key getKey(byte[] arrBTmp) throws Exception{
		
		byte[] arrB = new byte[8];
		
		for(int i=0;i<arrBTmp.length && i<arrB.length;i++){
			arrB[i] = arrBTmp[i];
		}
		Key key = new javax.crypto.spec.SecretKeySpec(arrB, "DES");
		return key;
	}
	
	public DesActivate()throws Exception{
		this(strDefaultKey);
	}
	
	public DesActivate(String strKey)throws Exception{
		Security.addProvider(new com.sun.crypto.provider.SunJCE());
		Key key = getKey(strKey.getBytes());
		ecipher = Cipher.getInstance("DES");
		ecipher.init(Cipher.ENCRYPT_MODE, key);
		dcipher = Cipher.getInstance("DES");
		dcipher.init(Cipher.DECRYPT_MODE, key);
	}
	
	public static String byteArr2HexStr(byte[] arrB) throws Exception{
		int ilen = arrB.length;
		StringBuffer sb = new StringBuffer(ilen*2);
		for(int i=0;i<ilen;i++){
			int intTmp = arrB[i];
			while(intTmp<0){
				intTmp = intTmp+256;
			}
			if(intTmp<16){
				sb.append("0");
			}
			sb.append(Integer.toString(intTmp, 16));
		}
		return sb.toString();
	}
	
	public byte[] encrypt(byte[] arrB) throws Exception{
		return ecipher.doFinal(arrB);
	}
	public String encrypt(String strIn)throws Exception{
		return byteArr2HexStr(encrypt(strIn.getBytes()));
	}
	
	public static byte[] hexStr2ByteArr(String strIn) throws Exception{
		byte[] arrB = strIn.getBytes();
		int ilen = arrB.length;
		byte[] arrOut = new byte[ilen/2];
		for(int i=0;i<ilen;i=i+2){
			String strTmp = new String(arrB, i, 2);
			arrOut[i / 2] = (byte) Integer.parseInt(strTmp, 16);
		}
		return arrOut;
	}
	public byte[] decrypt(byte[] arrB)throws Exception{
		return dcipher.doFinal(arrB);
	}
	public String decrypt(String strIn)throws Exception{
		return new String(decrypt(hexStr2ByteArr(strIn)));
	}
}