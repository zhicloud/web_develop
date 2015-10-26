package test;

import org.apache.log4j.Logger;

public class TestLog4j
{
	static Logger logger = Logger.getLogger(TestLog4j.class);

	public static void main(String[] args)
	{
		logger.error("ddd", new Exception("222"));
	}
}
