<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page import="com.zhicloud.op.app.propeties.AppProperties"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
%>
<!DOCTYPE html>
<!-- app_properties_manage.jsp -->
<html>
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		
		<title>超级管理员-全局属性管理</title>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
		<style type="text/css">
		/************/
		.panel-header {
			border-top: 0px;
			border-bottom: 1px solid #dddddd;
		}
		.panel-header, 
		.panel-body {
			border-left: 0px;
			border-right: 0px;
		}
		.panel-body {
			border-bottom: 0px;
		}
		body {
			overflow-y: scroll;
		}
		</style>
	</head>
	<body >
		<form id="big_form" method="post">
		
			<div class="panel-header">
				<div class="panel-title">全局属性管理</div>
				<div class="panel-tool"></div>
			</div>
		
			<div id="package_manage_dlg"    style="padding:0px;">
				
				<%-- ******************** --%>
				
				<table style="width:100%; margin:0 auto 0 auto;">
					<tr>
						<td width="140px" style="font-weight:bolder;">
							本系统的地址：
						</td>
						<td >
							<input id="address_of_this_system" name="address_of_this_system" type="text"
								style="width:400px"/>
						</td>
<!-- 						<td align="right" style="width:100px;"> -->
<!-- 							&nbsp;&nbsp; -->
<!-- 							<a href="javascript:" class="easyui-linkbutton save-btn" data-options="iconCls:'icon-save'" > &nbsp;保存&nbsp; </a> -->
<!-- 						</td> -->
					</tr>
					<tr>
						<td width="140px">
						</td>
						<td style="color:blue;">
							（此属性设置本系统即运营管理平台的地址，值为形如："http://clouduan.com/op/"的字符串）
						</td>
					</tr>
					<tr>
						<td colspan="95" style="padding: 10px 0 10px 0;">
							<hr />
						</td>
					</tr>
				</table>
				
				<%-- ******************** --%>
				
				<table style="width:100%; margin:0 auto 0 auto;">
					<tr>
						<td width="140px" style="font-weight:bolder;">
							CPU套餐上限：
						</td>
						<td >
							最大
							<input id="cpu_package_option_upper_limit" type="text" name="cpu_upper"  value="" style="width:50px"/>核
						</td>
<!-- 						<td align="right" style="width:100px;"> -->
<!-- 							&nbsp;&nbsp; -->
<!-- 							<a href="javascript:" class="easyui-linkbutton save-btn" data-options="iconCls:'icon-save'" > &nbsp;保存&nbsp; </a> -->
<!-- 						</td> -->
					</tr>
					<tr>
						<td width="140px">
						</td>
						<td style="color:blue;">
							（此属性将会影响运营商在套餐项管理页可以设置的最大的CPU核数）
						</td>
					</tr>
					<tr>
						<td colspan="95" style="padding: 10px 0 10px 0;">
							<hr />
						</td>
					</tr>
				</table>
				
				<%-- ******************** --%>
					
				<table style="width:100%; margin:0 auto 0 auto;">
					<tr>
						<td width="140px" style="font-weight:bolder;">
							内存套餐上限：
						</td>
						<td >
							最大
							<input id="memory_package_option_upper_limit" type="text" name="memory_upper"  value=""  style="width:50px"/>GB
						</td>
<!-- 						<td align="right" style="width:100px;"> -->
<!-- 							&nbsp;&nbsp; -->
<!-- 							<a href="javascript:" class="easyui-linkbutton save-btn" data-options="iconCls:'icon-save'" > &nbsp;保存&nbsp; </a> -->
<!-- 						</td> -->
					</tr>
					<tr>
						<td width="140px">
						</td>
						<td style="color:blue;">
							（此属性将会影响运营商在套餐项管理页可以设置的最大的内存容量）
						</td>
					</tr>
					<tr>
						<td colspan="95" style="padding: 10px 0 10px 0;">
							<hr />
						</td>
					</tr>
				</table>
				
				<%-- ******************** --%>
					
				<table style="width:100%; margin:0 auto 0 auto;">
					<tr>
						<td width="140px" style="font-weight:bolder;">
							上传最大文件容量：
						</td>
						<td >
							最大
							<input id="upload_file_upper_limit" type="text" name="upload_file_upper"  value=""  style="width:50px"/>MB
						</td>
<!-- 						<td align="right" style="width:100px;"> -->
<!-- 							&nbsp;&nbsp; -->
<!-- 							<a href="javascript:" class="easyui-linkbutton save-btn" data-options="iconCls:'icon-save'" > &nbsp;保存&nbsp; </a> -->
<!-- 						</td> -->
					</tr>
					<tr>
						<td width="140px">
						</td>
						<td style="color:blue;">
							（此属性将会限定用户上传的单个文件大小）
						</td>
					</tr>
					<tr>
						<td colspan="95" style="padding: 10px 0 10px 0;">
							<hr />
						</td>
					</tr>
				</table>
				
				<%-- ******************** --%>
					
				<table style="width:100%; margin:0 auto 0 auto;">
					<tr>
						<td width="140px" style="font-weight:bolder;">
							用户最大的可用空间：
						</td>
						<td >
							最大
							<input id="upload_total_file_upper_limit" type="text" name="upload_total_file_upper"  value=""  style="width:50px"/>MB
						</td>
<!-- 						<td align="right" style="width:100px;"> -->
<!-- 							&nbsp;&nbsp; -->
<!-- 							<a href="javascript:" class="easyui-linkbutton save-btn" data-options="iconCls:'icon-save'" > &nbsp;保存&nbsp; </a> -->
<!-- 						</td> -->
					</tr>
					<tr>
						<td width="140px">
						</td>
						<td style="color:blue;">
							（此属性将会限定用户最大的可用空间）
						</td>
					</tr>
					<tr>
						<td colspan="95" style="padding: 10px 0 10px 0;">
							<hr />
						</td>
					</tr>
				</table>
				
				<%-- ******************** --%>
					
				<table style="width:100%; margin:0 auto 0 auto;">
					<tr>
						<td width="140px" style="font-weight:bolder;">
							现金券最大额度：
						</td>
						<td >
							最大
							<input id="cash_upper_limit" type="text" name="cash_upper"  value=""  style="width:50px"/>元
						</td>
<!-- 						<td align="right" style="width:100px;"> -->
<!-- 							&nbsp;&nbsp; -->
<!-- 							<a href="javascript:" class="easyui-linkbutton save-btn" data-options="iconCls:'icon-save'" > &nbsp;保存&nbsp; </a> -->
<!-- 						</td> -->
					</tr>
					<tr>
						<td width="140px">
						</td>
						<td style="color:blue;">
							（此属性将会限定现金券的最大额度）
						</td>
					</tr>
					<tr>
						<td colspan="95" style="padding: 10px 0 10px 0;">
							<hr />
						</td>
					</tr>
				</table>
				
				<%-- ******************** --%>
				
				<table style="width:100%; margin:0 auto 0 auto;">
					<tr>
						<td width="140px" style="font-weight:bolder;">
							致云客户端版本：
						</td>
						<td >
							<input id="client_version_name" type="text" name="version_name"  value=""  style="width:400px"/>
						</td>
<!-- 						<td align="right" style="width:100px;"> -->
<!-- 							&nbsp;&nbsp; -->
<!-- 							<a href="javascript:" class="easyui-linkbutton save-btn" data-options="iconCls:'icon-save'" > &nbsp;保存&nbsp; </a> -->
<!-- 						</td> -->
					</tr>
					<tr>
						<td width="140px">
						</td>
						<td style="color:blue;">
							（此属性将限定下载路径显示）
						</td>
					</tr>
					<tr>
						<td colspan="95" style="padding: 10px 0 10px 0;">
							<hr />
						</td>
					</tr>
				</table>
				
				<%-- ******************** --%>
				<table style="width:100%; margin:0 auto 0 auto;">
					<tr>
						<td width="140px" style="font-weight:bolder;">
							监控通知邮箱：
						</td>
						<td >
							<input id="monitor_mail" type="text" name="monitor_mail"  value=""  style="width:400px"/>
						</td>
<!-- 						<td align="right" style="width:100px;"> -->
<!-- 							&nbsp;&nbsp; -->
<!-- 							<a href="javascript:" class="easyui-linkbutton save-btn" data-options="iconCls:'icon-save'" > &nbsp;保存&nbsp; </a> -->
<!-- 						</td> -->
					</tr>
					<tr>
						<td width="140px">
						</td>
						<td style="color:blue;">
							（此属性将限定监控邮件通知）
						</td>
					</tr>
					<tr>
						<td colspan="95" style="padding: 10px 0 10px 0;">
							<hr />
						</td>
					</tr>
					<tr>
						<td width="140px" style="font-weight:bolder;">
							服务邮箱：
						</td>
						<td >
							<input id="support_email" type="text" name="support_email"  value=""  style="width:400px"/>
						</td>
<!-- 						<td align="right" style="width:100px;"> -->
<!-- 							&nbsp;&nbsp; -->
<!-- 							<a href="javascript:" class="easyui-linkbutton save-btn" data-options="iconCls:'icon-save'" > &nbsp;保存&nbsp; </a> -->
<!-- 						</td> -->
					</tr>
					<tr>
						<td width="140px">
						</td>
						<td style="color:blue;">
							（此属性将限定服务邮件通知）
						</td>
					</tr>
					<tr>
						<td colspan="95" style="padding: 10px 0 10px 0;">
							<hr />
						</td>
					</tr>
				</table>
				
				<%-- ******************** --%>
				<%-- ******************** --%>
				<table style="width:100%; margin:0 auto 0 auto;">
					<tr>
						<td width="140px" style="font-weight:bolder;">
							监控手机1：
						</td>
						<td >
							<input id="monitor_phone1" type="text" name="monitor_phone1"  value=""  style="width:400px"/>
						</td>
<!-- 						<td align="right" style="width:100px;"> -->
<!-- 							&nbsp;&nbsp; -->
<!-- 							<a href="javascript:" class="easyui-linkbutton save-btn" data-options="iconCls:'icon-save'" > &nbsp;保存&nbsp; </a> -->
<!-- 						</td> -->
					</tr>
					<tr>
						<td width="140px">
						</td>
						<td style="color:blue;">
							（此属性将限定监控手机通知）
						</td>
					</tr>
					<tr>
						<td colspan="95" style="padding: 10px 0 10px 0;">
							<hr />
						</td>
					</tr>
				</table>
				
				<%-- ******************** --%>
				<%-- ******************** --%>
				<table style="width:100%; margin:0 auto 0 auto;">
					<tr>
						<td width="140px" style="font-weight:bolder;">
							监控手机2：
						</td>
						<td >
							<input id="monitor_phone2" type="text" name="monitor_phone2"  value=""  style="width:400px"/>
						</td>
<!-- 						<td align="right" style="width:100px;"> -->
<!-- 							&nbsp;&nbsp; -->
<!-- 							<a href="javascript:" class="easyui-linkbutton save-btn" data-options="iconCls:'icon-save'" > &nbsp;保存&nbsp; </a> -->
<!-- 						</td> -->
					</tr>
					<tr>
						<td width="140px">
						</td>
						<td style="color:blue;">
							（此属性将限定监控手机通知）
						</td>
					</tr>
					<tr>
						<td colspan="95" style="padding: 10px 0 10px 0;">
							<hr />
						</td>
					</tr>
				</table>
				
				<%-- ******************** --%>
				<%-- ******************** --%>
				<table style="width:100%; margin:0 auto 0 auto;">
					<tr>
						<td width="140px" style="font-weight:bolder;">
							监控手机3：
						</td>
						<td >
							<input id="monitor_phone3" type="text" name="monitor_phone3"  value=""  style="width:400px"/>
						</td>
<!-- 						<td align="right" style="width:100px;"> -->
<!-- 							&nbsp;&nbsp; -->
<!-- 							<a href="javascript:" class="easyui-linkbutton save-btn" data-options="iconCls:'icon-save'" > &nbsp;保存&nbsp; </a> -->
<!-- 						</td> -->
					</tr>
					<tr>
						<td width="140px">
						</td>
						<td style="color:blue;">
							（此属性将限定监控手机通知）
						</td>
					</tr>
					<tr>
						<td colspan="95" style="padding: 10px 0 10px 0;">
							<hr />
						</td>
					</tr>
				</table>
				<table style="width:100%; margin:0 auto 0 auto;">
					<tr>
						<td width="140px" style="font-weight:bolder;">
						WEB发布包类型：
						</td>
						<td >
							<input id="version_type" type="text" name="version_type"  value=""  style="width:100px"/>
						</td>
					</tr>
					<tr>
						<td width="140px">
						</td>
						<td style="color:blue;">
							（此属性将提示发布包的类型）
						</td>
					</tr>
					<tr>
						<td colspan="95" style="padding: 10px 0 10px 0;">
							<hr />
						</td>
					</tr>
				</table>
				
				<table style="width:100%; margin:0 auto 0 auto;">
					<tr>
						<td width="140px" style="font-weight:bolder;">
						是否发送故障监控通知：
						</td>
						<td >
							<input id="notification-on" type="radio" name="notification_on_off"  value="yes" checked="checked">是&nbsp;&nbsp;&nbsp;&nbsp;
							<input id="notification-off" type="radio" name="notification_on_off"  value="no">否
						</td>
					</tr>
					<tr>
						<td width="140px">
						</td>
						<td style="color:blue;">
							（此属性控制是否发送监控通知）
						</td>
					</tr>
					<tr>
						<td colspan="95" style="padding: 10px 0 10px 0;">
							<hr />
						</td>
					</tr>
				</table>
				<table style="width:100%; margin:0 auto 0 auto;">
					<tr>
						<td width="140px" style="font-weight:bolder;">
						是否打印调试日志：
						</td>
						<td >
							<input id="log-on" type="radio" name="log_on_off"  value="yes" checked="checked">是&nbsp;&nbsp;&nbsp;&nbsp;
							<input id="log-off" type="radio" name="log_on_off"  value="no">否
						</td>
					</tr>
					<tr>
						<td width="140px">
						</td>
						<td style="color:blue;">
							（此属性控制是否打印调试日志）
						</td>
					</tr>
					<tr>
						<td colspan="95" style="padding: 10px 0 10px 0;">
							<hr />
						</td>
					</tr>
				</table>
				
				<table style="width:100%; margin:0 auto 0 auto;">
					<tr>
						<td width="140px" style="font-weight:bolder;">
							成都主机最大端口数：
						</td>
						<td >
							最多
							<input id="cd_max_ports" type="text" name="cd_max_ports"  value="" style="width:50px"/>个
						</td> 
					</tr>
					<tr>
						<td colspan="95" style="padding: 10px 0 10px 0;">
							<hr />
						</td>
					</tr>
					 
				</table>
				<table style="width:100%; margin:0 auto 0 auto;">
					<tr>
						<td width="140px" style="font-weight:bolder;">
							广州地域存储网关IP：
						</td>
						<td >
							<input id="address_of_iscsi_gateway_1" name="address_of_iscsi_gateway_1" type="text"
								style="width:400px"/>
						</td>
<!-- 						<td align="right" style="width:100px;"> -->
<!-- 							&nbsp;&nbsp; -->
<!-- 							<a href="javascript:" class="easyui-linkbutton save-btn" data-options="iconCls:'icon-save'" > &nbsp;保存&nbsp; </a> -->
<!-- 						</td> -->
					</tr> 
					<tr>
						<td width="140px" style="font-weight:bolder;">
							成都地域存储网关IP：
						</td>
						<td >
							<input id="address_of_iscsi_gateway_2" name="address_of_iscsi_gateway_2" type="text"
								style="width:400px"/>
						</td>
<!-- 						<td align="right" style="width:100px;"> -->
<!-- 							&nbsp;&nbsp; -->
<!-- 							<a href="javascript:" class="easyui-linkbutton save-btn" data-options="iconCls:'icon-save'" > &nbsp;保存&nbsp; </a> -->
<!-- 						</td> -->
					</tr> 
					<tr>
						<td width="140px" style="font-weight:bolder;">
							香港地域存储网关IP：
						</td>
						<td >
							<input id="address_of_iscsi_gateway_4" name="address_of_iscsi_gateway_4" type="text"
								style="width:400px"/>
						</td>
<!-- 						<td align="right" style="width:100px;"> -->
<!-- 							&nbsp;&nbsp; -->
<!-- 							<a href="javascript:" class="easyui-linkbutton save-btn" data-options="iconCls:'icon-save'" > &nbsp;保存&nbsp; </a> -->
<!-- 						</td> -->
					</tr> 
					<tr>
						<td colspan="95" style="padding: 10px 0 10px 0;">
							<hr />
						</td>
					</tr>
				</table>
				
				<table style="width:100%; margin:0 auto 0 auto;">
					<tr>
						<td width="140px" style="font-weight:bolder;">
							http server 服务地址：
						</td>
						<td >
							<input id="server_monitor_request" type="text" name="server_monitor_request"  value=""  style="width:400px"/>
						</td> 
					</tr> 
					<tr>
						<td width="140px">
						</td>
						<td style="color:blue;">
							（此属性将用于数据库监控功能，值为形如："http://127.0.0.1:8081"的字符串）
						</td>
					</tr>
					<tr>
						<td colspan="95" style="padding: 10px 0 10px 0;">
							<hr />
						</td>
					</tr>
				</table>
				<%-- ******************** --%>
				
				<table style="width:90%; margin:0 auto 0 auto;">
					<tr>
						<td align="right" style="width:100px;">
							&nbsp;&nbsp;
							<a href="javascript:" class="easyui-linkbutton save-btn" data-options="iconCls:'icon-save'" > &nbsp;保存&nbsp; </a>
						</td>
					</tr>
				</table>
				
				<%-- ******************** --%>
				
			</div>
		
		</form>
	</body>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.ext.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.util.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/big.min.js"></script>
	
	<script type="text/javascript">
	
	var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
	ajax.async = false;
	
	var _package_option_upper_dlg_scope_ = new function(){
		
		var upper_obj = this;
		$(function(){
			var notification_on_off = "<%=AppProperties.getValue("notification_on_off", "yes")%>";
			if(notification_on_off == "no"){
// 				$("#notification-on").removeAttr("checked");
// 				$("#notification-off").attr("checked","checked");
				document.getElementById("notification-off").checked = true;
			}else{
// 				$("#notification-off").removeAttr("checked");
// 				$("#notification-on").attr("checked","checked");
				document.getElementById("notification-on").checked = true;
			}
			var log_on_off = "<%=AppProperties.getValue("log_on_off", "yes")%>";
			if(log_on_off == "no"){
// 				$("#notification-on").removeAttr("checked");
// 				$("#notification-off").attr("checked","checked");
				document.getElementById("log-off").checked = true;
			}else{
// 				$("#notification-off").removeAttr("checked");
// 				$("#notification-on").attr("checked","checked");
				document.getElementById("log-on").checked = true;
			}
		});
		$("#address_of_this_system").val("<%=AppProperties.getValue("address_of_this_system", "")%>");
		$("#cpu_package_option_upper_limit").val("<%=AppProperties.getValue("cpu_package_option_upper_limit", "")%>");
		$("#memory_package_option_upper_limit").val("<%=AppProperties.getValue("memory_package_option_upper_limit", "")%>");
		$("#upload_file_upper_limit").val("<%=AppProperties.getValue("upload_file_upper_limit", "")%>");
		$("#upload_total_file_upper_limit").val("<%=AppProperties.getValue("upload_total_file_upper_limit", "")%>");
		$("#cash_upper_limit").val("<%=AppProperties.getValue("cash_upper_limit", "")%>");
		$("#client_version_name").val("<%=AppProperties.getValue("client_version_name", "")%>");
		$("#monitor_mail").val("<%=AppProperties.getValue("monitor_mail", "")%>");
		$("#support_email").val("<%=AppProperties.getValue("support_email", "")%>");
		$("#monitor_phone1").val("<%=AppProperties.getValue("monitor_phone1", "")%>"); 
		$("#monitor_phone2").val("<%=AppProperties.getValue("monitor_phone2", "")%>"); 
		$("#monitor_phone3").val("<%=AppProperties.getValue("monitor_phone3", "")%>"); 
		$("#version_type").val("<%=AppProperties.getValue("version_type", "")%>"); 
		$("#cd_max_ports").val("<%=AppProperties.getValue("cd_max_ports", "")%>"); 
		$("#address_of_iscsi_gateway_4").val("<%=AppProperties.getValue("address_of_iscsi_gateway_4", "")%>"); 
		$("#address_of_iscsi_gateway_1").val("<%=AppProperties.getValue("address_of_iscsi_gateway_1", "")%>"); 
		$("#address_of_iscsi_gateway_2").val("<%=AppProperties.getValue("address_of_iscsi_gateway_2", "")%>"); 
		$("#server_monitor_request").val("<%=AppProperties.getValue("server_monitor_request", "")%>"); 

		
		
		// 更新CPU核心数、内存上限、网关配置
		upper_obj.saveCpuUpper = function() {
			var   r = /^[0-9]*[1-9][0-9]*$/;
// 			var   r =/^(?!0+(?:\.0+)?$)(?:[1-9]\d*|0)(?:\.\d{1,2})?$/;
			var core = $("#cpu_package_option_upper_limit").val().trim();
			if( core=="" || !(r.test(core)))
			{
				top.$.messager.alert("警告","CPU套餐上限必须为正整数",'warning',function(){
					window.location.reload();
				});
				return ;
			}
			var memory = $("#memory_package_option_upper_limit").val().trim(); 
			if( memory=="" || !(r.test(memory)))
			{
				top.$.messager.alert("警告","内存套餐上限必须为正整数",'warning',function(){
					window.location.reload();
				});
				return ;
			}
			var upload_file_upper = $("#upload_file_upper_limit").val().trim(); 
			if( upload_file_upper=="" || !(r.test(upload_file_upper)))
			{
				top.$.messager.alert("警告","单个上传文件大小上限必须为正整数",'warning',function(){
					window.location.reload();
				});
				return ;
			}
			var cash_upper = $("#cash_upper_limit").val().trim(); 
			if( cash_upper=="" || !(r.test(cash_upper)) || cash_upper > 2000)
			{
				top.$.messager.alert("警告","现金券的最大额度必须为不大于2000的正整数",'warning',function(){
					window.location.reload();
				});
				return ;
			}
			var version_name = $("#client_version_name").val().trim(); 
			if(version_name==""||version_name==null)
			{
				top.$.messager.alert("警告","客户端版本不能为空",'warning',function(){
					window.location.reload();
				});
				return ;
			}
			var upload_total_file_upper = $("#upload_total_file_upper_limit").val().trim(); 
			if( upload_total_file_upper=="" || !(r.test(upload_total_file_upper)))
			{
				top.$.messager.alert("警告","最大的可用空间必须为正整数",'warning',function(){
					window.location.reload();
				});
				return ;
			} 
			var phone = new String($("#monitor_phone1").val()).trim();
			if(phone==null || phone==""){
				top.$.messager.alert("警告","监控手机1不能为空",'warning',function(){
					window.location.reload();
				});
				return ; 
			}
			if(!(/^1[3|4|5|8][0-9]\d{8,8}$/.test(phone))){ 
				top.$.messager.alert("警告","监控手机1不是正确的手机号",'warning',function(){
					window.location.reload();
				});
				return ;  
			} 
			phone = new String($("#monitor_phone2").val()).trim();
			if(phone==null || phone==""){
				top.$.messager.alert("警告","监控手机2不能为空",'warning',function(){
					window.location.reload();
				});
				return ; 
			}
			if(!(/^1[3|4|5|8][0-9]\d{8,8}$/.test(phone))){ 
				top.$.messager.alert("警告","监控手机2不是正确的手机号",'warning',function(){
					window.location.reload();
				});
				return ;  
			} 
			phone = new String($("#monitor_phone3").val()).trim();
			if(phone==null || phone==""){
				top.$.messager.alert("警告","监控手机3不能为空",'warning',function(){
					window.location.reload();
				});
				return ; 
			}
			if(!(/^1[3|4|5|8][0-9]\d{8,8}$/.test(phone))){ 
				top.$.messager.alert("警告","监控手机3不是正确的手机号",'warning',function(){
					window.location.reload();
				});
				return ;  
			} 
			var account = new String($("#monitor_mail").val()).trim();
			var myreg = /^([a-zA-Z0-9]+[_|\_|\.]?)*@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
		 	if(account==null || account==""){
				top.$.messager.alert("警告","监控通知邮箱不能为空",'warning',function(){
					window.location.reload();
				});
				return ;   
			}
			if(!myreg.test(account)){
				top.$.messager.alert("警告","监控通知邮箱格式不正确",'warning',function(){
					window.location.reload();
				});
				return ;    
			}
			
			account = new String($("#support_email").val()).trim();
 		 	if(account==null || account==""){
				top.$.messager.alert("警告","服务邮箱不能为空",'warning',function(){
					window.location.reload();
				});
				return ;   
			}
			if(!myreg.test(account)){
				top.$.messager.alert("警告","服务邮箱格式不正确",'warning',function(){
					window.location.reload();
				});
				return ;    
			}
			
			var versionType = new String($("#version_type").val()).trim();
			if(versionType==null || versionType==""){
				top.$.messager.alert("警告","发布包类型不能为空",'warning',function(){
					window.location.reload();
				});
				return ; 
			}
			var cd_max_ports = $("#cd_max_ports").val().trim(); 
			if( cd_max_ports=="" || !(r.test(cd_max_ports)) || cd_max_ports > 100 || cd_max_ports <3)
			{
				top.$.messager.alert("警告","成都主机最大端口数必须为3-100的整数",'warning',function(){
					window.location.reload();
				});
				return ;
			}
			
			var server_monitor_request = $("#server_monitor_request").val().trim(); 
			if(server_monitor_request==null || server_monitor_request==""){
				top.$.messager.alert("警告","http server地址不能为空",'warning',function(){
					window.location.reload();
				});
				return ; 
			}
			
			var formData = $.formToBean(big_form);
			ajax.remoteCall("bean://appPropertyService:updateAppPropertyService", 
				[ formData ],
				function(reply) {
					if (reply.status == "exception") {
						if(reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>"){
							top.$.messager.alert("警告","会话超时，请重新登录","warning");
						}else{
							top.$.messager.alert("警告",reply.exceptionMessage,"warning",function(){
								top.location.reload();
							});
						}
					} else if (reply.result.status == "success") {
						top.$.messager.alert("提示","保存成功",'info',function() {
							window.location.reload();
						});
					} 
				}
			);
		};
	
		// 
		$(document).ready(function(){
			
			$(".save-btn").click(function(){
				if($("#cpu_package_option_upper_limit").val() < <%=request.getAttribute("cpu")%>){
					top.$.messager.alert('警告',"CPU核心数小于当前套餐项中的最大值，请联系运营商删除",'warning',function(){
						window.location.reload();
					});
					return;
				}
				if($("#memory_package_option_upper_limit").val() < <%=request.getAttribute("memory")%>){
					top.$.messager.alert('警告',"内存大小心数小于当前套餐项中的最大值，请联系运营商删除",'warning',function(){
						window.location.reload();
					});
					return;
				}
				upper_obj.saveCpuUpper();
			});
		});
		
	};
	</script>
</html>
