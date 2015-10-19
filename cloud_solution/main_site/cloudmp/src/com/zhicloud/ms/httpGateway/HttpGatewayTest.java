package com.zhicloud.ms.httpGateway;

import net.sf.json.JSONObject;






public class HttpGatewayTest
{

//	public static void main(String[] args) throws Exception
//	{
//		HttpGatewayChannelExt channel = HttpGatewayManager.getChannel();
//
//		channel.getNodeClients();
//		channel.getDefaultComputePool();
//	}
	
	
//	public static void main(String[] args) throws Exception
//	{
//		HttpGatewayChannelExt channel = HttpGatewayManager.getChannel();
////		channel.computePoolAddResource((String)channel.getDefaultComputePool().get("uuid"), "node_client_002590934164");
//		channel.computePoolQueryResource((String)channel.getDefaultComputePool().get("uuid"));
//	}
	
	
//	public static void main(String[] args) throws Exception
//	{
//		HttpGatewayChannelExt channel = HttpGatewayManager.getChannel();
//		
//		channel.hostCreate("chz_host", 
//							(String)channel.getDefaultComputePool().get("uuid"), 
//							1, 
//							new BigInteger("1024").pow(3).multiply(new BigInteger("2")),	// 内存2G
//							new Integer[]{0,0,0}, 			
//							"", 
//							new BigInteger[]{new BigInteger("1024").pow(3).multiply(new BigInteger("10"))},	// 磁盘 10G
//							new Integer[]{1,90,1,9080},		// 端口
//							"chz_user", 
//							"chz_group", 
//							"chz_display", 
//							"chz_authentication", 
//							"", 
//							new BigInteger("1000").pow(2).multiply(new BigInteger("10")), 
//							new BigInteger("1000").pow(2).multiply(new BigInteger("10")));
//		
//	}
	
//	public static void main(String[] args) throws Exception
//	{
//		HttpGatewayChannelExt channel = HttpGatewayManager.getChannel();
//		
//		channel.hostQuery((String)channel.getDefaultComputePool().get("uuid"));
//		
//		channel.diskImageQuery();
//		
//	}
	
//	public static void main(String[] args) throws Exception
//	{
//		HttpGatewayChannelExt channel = HttpGatewayManager.getChannel();
//		
//		channel.diskImageCreate(
//				"chz_disk_image", 
//				"aae2a115-e9dc-4805-abb3-a6770194f301", 
//				"chz_description", 
//				new String[]{"chz_identity"}, 
//				"chz_group", 
//				"chz_user");
//		
//	}
	
	public static void main(String[] args) throws Exception
	{
		HttpGatewayChannelExt channel = HttpGatewayManager.getChannel(1);
		JSONObject json = channel.diskImageQuery();
		System.out.println(json.get("disk_images").getClass());
		
	}
}
