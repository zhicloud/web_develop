<%@page import="com.zhicloud.op.app.pool.CloudHostData"%>
<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.app.pool.CloudHostPoolManager"%>
<%@ page pageEncoding="utf-8"  contentType="text/html; charset=utf-8" %>
<%

List<CloudHostData> cloudHostList = CloudHostPoolManager.getCloudHostPool().getAll();
for( CloudHostData cloudHostData : cloudHostList )
{
	System.out.println("---------------------------------------");
	System.out.println("RealHostId: "+cloudHostData.getRealHostId());
	System.out.println("CpuUsage: "+cloudHostData.getCpuUsage());
	System.out.println("MemoryUsage: "+cloudHostData.getMemoryUsage());
	System.out.println("SysDiskUsage: "+cloudHostData.getSysDiskUsage());
	System.out.println("DataDiskUsage: "+cloudHostData.getDataDiskUsage());
}

System.out.println("=============================================");

CloudHostData cloudHostData = CloudHostPoolManager.getCloudHostPool().getByRealHostId("e719dacd-599d-4d2f-801b-6c2241674474");
System.out.println("CpuUsage: "+cloudHostData.getCpuUsage());
System.out.println("MemoryUsage: "+cloudHostData.getMemoryUsage());
System.out.println("SysDiskUsage: "+cloudHostData.getSysDiskUsage());
System.out.println("DataDiskUsage: "+cloudHostData.getDataDiskUsage());
%>
