package com.zhicloud.ms.common.util;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.log4j.Logger;

import com.zhicloud.ms.exception.AppException;

public class AESUtil
{
	
	private static final Logger logger = Logger.getLogger(AESUtil.class);
	
	private static final String KEY_ALGORITHM    = "AES";
	private static final String CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";
	
	//----------------
	
	public static byte[] generateKey(int length, byte[] seed) throws NoSuchAlgorithmException
	{
		KeyGenerator keyGenerator = KeyGenerator.getInstance(KEY_ALGORITHM);
		if( seed!=null )
		{
			keyGenerator.init(length, new SecureRandom(seed));
		}
		else
		{
			keyGenerator.init(length);
		}
		SecretKey secretKey = keyGenerator.generateKey();
		byte[] key = secretKey.getEncoded();
		return key;
	}
	
	//----------------
	
	public static byte[] encrypt(byte[] key, byte[] content)
	{
		try
		{
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, KEY_ALGORITHM));// ³õÊ¼»¯
			return cipher.doFinal(content);
		}
		catch( InvalidKeyException e )
		{
			throw AppException.wrapException(e);
		}
		catch( NoSuchAlgorithmException e )
		{
			throw AppException.wrapException(e);
		}
		catch( NoSuchPaddingException e )
		{
			throw AppException.wrapException(e);
		}
		catch( IllegalBlockSizeException e )
		{
			throw AppException.wrapException(e);
		}
		catch( BadPaddingException e )
		{
			throw AppException.wrapException(e);
		}
		
	}
	
	public static byte[] encrypt(String password, String content)
	{
		try
		{
			return encrypt(password.getBytes("utf-8"), content.getBytes("utf-8"));
		}
		catch( UnsupportedEncodingException e )
		{
			throw AppException.wrapException(e);
		}
	}
	
	public static byte[] encrypt(byte[] password, String content)
	{
		try
		{
			return encrypt(password, content.getBytes("utf-8"));
		}
		catch( UnsupportedEncodingException e )
		{
			throw AppException.wrapException(e);
		}
	}
	
	//-----------------
	
	public static byte[] decrypt(byte[] key, byte[] content)
	{
		try
		{
			Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
			cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, KEY_ALGORITHM));// ³õÊ¼»¯
			return cipher.doFinal(content);
		}
		catch( InvalidKeyException e )
		{
			throw AppException.wrapException(e);
		}
		catch( NoSuchAlgorithmException e )
		{
			throw AppException.wrapException(e);
		}
		catch( NoSuchPaddingException e )
		{
			throw AppException.wrapException(e);
		}
		catch( IllegalBlockSizeException e )
		{
			throw AppException.wrapException(e);
		}
		catch( BadPaddingException e )
		{
			// ½âÃÜÊ§°Ü
			logger.error("½âÃÜÊ§°Ü", e);
			return null;
		}
	}
	
	public static byte[] decrypt(String password, String content)
	{
		try
		{
			return decrypt(password.getBytes("utf-8"), content.getBytes("utf-8"));
		}
		catch( UnsupportedEncodingException e )
		{
			throw AppException.wrapException(e);
		}
	}
	
	public static byte[] decrypt(String password, byte[] content)
	{
		try
		{
			return decrypt(password.getBytes("utf-8"), content);
		}
		catch( UnsupportedEncodingException e )
		{
			throw AppException.wrapException(e);
		}
	}
	
	//------------------
	
	
	public static void main(String[] args)
	{
		System.out.println(StringUtil.bytesToBase64(encrypt("1234567890123450".getBytes(), "admin")));
	}
	
}
