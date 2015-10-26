package com.zhicloud.op.common.util;

public class PrintUtil
{
	
	public static void printObjectArray(Object[] arr)
	{
		if( arr==null )
		{
			System.out.println(arr);
		}
		for( int i=0; i<arr.length; i++ )
		{
			System.out.println(String.format("%8d: %s", i, arr[i]));
		}
	}
}
