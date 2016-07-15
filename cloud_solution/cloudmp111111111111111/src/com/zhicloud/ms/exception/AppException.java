package com.zhicloud.ms.exception;

import java.net.ConnectException;
import java.sql.SQLException;

import org.springframework.dao.DataAccessException;
import org.springframework.transaction.TransactionException;

public class AppException extends RuntimeException
{

	private static final long serialVersionUID = 4590763977994315294L;
	
	private String message = "";
	private String errorCode = "-1";

	public AppException(String message)
	{
		super("");
		this.message = message;
	}
	
	public AppException(String message, String errorCode)
	{
		super("");
		this.message = message;
		this.errorCode = errorCode;
	}

	public AppException(Throwable ex)
	{
		super("", ex);
		if( ex instanceof SQLException )
		{
			this.message = "数据库操作失败，请跟管理员联系";
		}
		else if( ex instanceof DataAccessException )
		{
			this.message = "数据库操作失败，请跟管理员联系";
		}
		else if( ex instanceof TransactionException )
		{
			this.message = "数据库操作失败，请跟管理员联系";
		}
		else if( ex instanceof org.apache.ibatis.transaction.TransactionException )
		{
			this.message = "数据库操作失败，请跟管理员联系";
		}
		else if( ex instanceof ConnectException )
		{
			this.message = "网路连接失败，请跟管理员联系";
		}
		else
		{
			this.message = ex.getMessage();
		}
	}

	public AppException(Throwable ex, String errorCode)
	{
		super("", ex);
		this.message = ex.getMessage();
		this.errorCode = errorCode;
	}
	
	public AppException(String message, Throwable ex)
	{
		super("", ex);
		this.message = message;
	}

	public AppException(String message, Throwable ex, String errorCode)
	{
		super("", ex);
		this.message = message;
		this.errorCode = errorCode;
	}
	
	//-----------
	
	public String getErrorCode()
	{
		return this.errorCode;
	}

	public String getMessage()
	{
		return message;
	}

	public void setMessage(String message)
	{
		this.message = message;
	}
	
	//-------------
	
	public static AppException wrapException(Throwable e)
	{
		if( e instanceof AppException )
		{
			return (AppException)e;
		}
		else
		{
			return new AppException(e);
		}
	}
}
