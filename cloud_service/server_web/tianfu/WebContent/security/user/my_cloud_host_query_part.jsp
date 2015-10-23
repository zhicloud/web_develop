<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.vo.CloudHostVO"%>
<%@page import="com.zhicloud.op.vo.UserOrderVO"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	List<CloudHostVO> myCloudHostList = (List<CloudHostVO>)request.getAttribute("myCloudHostList");
	List<UserOrderVO> orderList = (List<UserOrderVO>)request.getAttribute("orderList");
	if( myCloudHostList.isEmpty() )
	{
%>
<center>没有相关记录</center>
<%
	}
	for( CloudHostVO cloudHost : myCloudHostList )
	{
%>
<div class="cloud_host_item_outer_box" cloudHostId="<%=cloudHost.getId()%>"  
	style="
		border:1px solid #dddddd; 
		background-color:#fdfdfd; 
		margin:5px; 
		padding:3px;
	">
	<table style="width:100%;">
		<tr>
			<td width="100px" style="padding-left:20px;">
				<img src="<%=request.getContextPath()%>/images/computer.png" width="70px" height="80px" />
			</td>
			<td>
				<div>
					<span class="cloud_host_item_label">云主机名：<%=cloudHost.getHostName()%></span>
					<span class="cloud_host_item_label">内网监控地址：<%=cloudHost.getInnerMonitorAddr()%></span>
					<span class="cloud_host_item_label">外网监控地址：<%=cloudHost.getOuterMonitorAddr()%></span>
					<span class="cloud_host_item_label">状态：<%=cloudHost.getSummarizedStatusText()%></span>
				</div>
				<div>
					<span class="cloud_host_item_label">CPU：<%=cloudHost.getCpuCore()%>核</span>
					<span class="cloud_host_item_label">内存：<%=cloudHost.getMemoryText(0)%></span>
					<span class="cloud_host_item_label">系统磁盘：<%=cloudHost.getSysDiskText(0)%></span>
					<span class="cloud_host_item_label">数据磁盘：<%=cloudHost.getDataDiskText(0)%></span>
					<span class="cloud_host_item_label">带宽：<%=cloudHost.getBandwidthText(0)%></span>
				</div>
				<div class="cloud_host_item_buttons_box" style="text-align:right;">
					<a href="#" class="easyui-linkbutton view-detail" data-options="iconCls:'icon-search'">查看详情</a>
					&nbsp;
					<%if("关机".equals(cloudHost.getSummarizedStatusText())) {%>
						<a href="#" class="easyui-linkbutton launch" data-options="iconCls:'icon-tip'">启动</a>
						<a href="#" class="easyui-linkbutton " data-options="iconCls:'icon-no'" disabled="false">关机</a>
						<a href="#" class="easyui-linkbutton " data-options="iconCls:'icon-reload'" disabled="false">重启</a>
					<%}else if("运行".equals(cloudHost.getSummarizedStatusText())) {%>
						<a href="#" class="easyui-linkbutton " data-options="iconCls:'icon-tip'" disabled="false">启动</a>
						<a href="#" class="easyui-linkbutton shutdown" data-options="iconCls:'icon-no'">关机</a>
						<a href="#" class="easyui-linkbutton restart" data-options="iconCls:'icon-reload'">重启</a>
					<%} else{%>
						<a href="#" class="easyui-linkbutton launch" data-options="iconCls:'icon-tip'">启动</a>
						<a href="#" class="easyui-linkbutton shutdown" data-options="iconCls:'icon-no'">关机</a>
						<a href="#" class="easyui-linkbutton restart" data-options="iconCls:'icon-reload'">重启</a>
					<%} %>
					&nbsp;
					<a href="#" class="easyui-menubutton other-oper" data-options="menu:'#cloud_host_item_other_oper_menu', plain:false">其它操作</a>
				</div>
			</td>
		</tr>
	</table>
</div>
<%
	}
%>

<div id="cloud_host_item_other_oper_menu" style="display:none; width:150px;">
    <div class="popup-iso" data-options="iconCls:'icon-undo'">弹出光盘</div>
    <div class="insert-iso" data-options="iconCls:'icon-redo'">加载光盘</div>
    <div class="menu-sep"></div>
    <div class="open-port">开放端口</div>
    <div class="modify-allocation">修改配置</div>
    <div class="binding-terminal">绑定云终端</div>
    <div class="modify_password">修改监控密码</div>
    <div class="menu-sep"></div>
    <div class="inactivate">申请停机</div>
    <div class="reactivate">申请停机恢复</div>
    <div class="menu-sep"></div>
    <div class="delete-cloud-host" data-options="iconCls:'icon-remove'">删除云主机</div>
</div>
   
