package com.zhicloud.op.exception;

/**
 * 数据不合法的异常
 */
public class IllegalDataException extends AppException
{

	private static final long serialVersionUID = 798163562345088556L;

	public IllegalDataException(String message)
	{
		super(message);
	}
}
