package com.zhicloud.op.app.helper;

import java.io.File;

public class AppHelper
{

	public static final String APP_NAME = "op";

	// 服务器的所在路径
	private static File server_home;

	public static File getServerHome()
	{
		if (server_home == null)
		{
			server_home = new File(System.getProperty("catalina.home"));
			server_home.mkdirs();
		}
		return server_home;
	}

	// 服务器使用的临时目录
	private static File temp_dir;

	public static File getTempDir()
	{
		if (temp_dir == null)
		{
			temp_dir = new File(getServerHome(), "temp");
			temp_dir.mkdirs();
		}
		return temp_dir;
	}

}
