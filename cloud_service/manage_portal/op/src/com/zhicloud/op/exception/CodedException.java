package com.zhicloud.op.exception;

public class CodedException extends AppException
{

	private static final long serialVersionUID = -9046087095576973069L;
	
	private String errorCode = "-1";

	public CodedException(String message)
	{
		super(message);
	}
	
	public CodedException(String message, String errorCode)
	{
		super(message);
		this.errorCode = errorCode;
	}
	
	public CodedException(Throwable ex)
	{
		super(ex);
	}
	

	public CodedException(Throwable ex, String errorCode)
	{
		super(ex);
		this.errorCode = errorCode;
	}
	
	public CodedException(String message, Throwable ex)
	{
		super(message, ex);
	}

	public CodedException(String message, Throwable ex, String errorCode)
	{
		super(message, ex);
		this.errorCode = errorCode;
	}
	
	public String getErrorCode()
	{
		return this.errorCode;
	}
}
