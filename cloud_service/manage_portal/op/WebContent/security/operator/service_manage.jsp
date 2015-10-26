<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.app.pool.serviceInfoPool.ServiceInfoExt"%>
<%@page import="java.util.List"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
%>
<!DOCTYPE html>
<!-- cloud_host_manage.jsp -->
<html>
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		
		<title>运营商 - 服务管理</title>
		
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
		<style type="text/css">
		#cloud_host_datagrid {
			border: 0px solid red;
		}
		
		.panel-header {
			border-top: 0px;
			border-bottom: 1px solid #dddddd;
		}
		
		.panel-header,.panel-body {
			border-left: 0px;
			border-right: 0px;
		}
		
		.panel-body {
			border-bottom: 0px;
		}
		.messager-input {
		  width: 100px;
		  padding: 2px 2px 3px 2px;
		  height:16px;
		  border: 1px solid #95B8E7;
		  margin:2px;
		}
		.round_normal{width:16px;height:16px;display: inline-block;font-size:20px;line-heigth:16px;text-align:center;color:#99FF66;text-decoration:none}
		.round_warning{width:16px;height:16px;display: inline-block;font-size:20px;line-heigth:16px;text-align:center;color:#FFCC66;text-decoration:none}
		.round_error{width:16px;height:16px;display: inline-block;font-size:20px;line-heigth:16px;text-align:center;color:#FF0000;text-decoration:none}
		.round_stop{width:16px;height:16px;display: inline-block;font-size:20px;line-heigth:16px;text-align:center;color:#666666;text-decoration:none}
		
		.round_normal:hover{text-decoration:none}
		.round_warning:hover{text-decoration:none}
		.round_error:hover{text-decoration:none}
		.round_stop:hover{text-decoration:none}		
		</style>
	</head>
	<body style="visibility:hidden;">
		<form id="big_form"  method="post">
	
			<table id="cloud_host_datagrid" class="easyui-datagrid" title="服务管理"
				data-options="
					url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=serviceService&method=queryService',
					queryParams: {},
					toolbar: '#toolbar',
					rownumbers: true,
					striped: true,
					remoteSort:false,
					fitColumns: true,
					pagination: true,
					pageList: [10, 20, 50, 100, 200],
					pageSize: 20,
					view: createView(),
					onLoadSuccess: onLoadSuccess
				">
				<thead>
					<tr>
<!-- 						<th data-options="checkbox:true"></th> -->
						<th field="name" width="80px">服务名称</th>
						<th field="group"  width="30px" >服务组名</th>
						<th field="type" formatter="formatType" width="60px" >服务类型</th>
						<th field="region"  formatter="formatRegion" width="30px" >地域</th>
						<th field="ip"  width="60px" >IP</th>
						<th field="port"  width="40px" >端口</th>
						<th field="version" width="80px" >版本号</th>
						<th field="newStatus" formatter="statusColumnFormatter" width="40px" align="center">状态</th>
						<th field="operate" formatter="operateColumnFormatter" width="80px">操作</th>
					</tr>
				</thead>
			</table>
	
			<div id="toolbar" style="padding: 3px;">
				<div style="display: table; width: 100%;">
					<div style="margin-bottom:5px;">
			<span class="round_normal">●</span> <span style="font-size: 14px;" id="normalspan"></span>
			<span class="round_warning">●</span> <span style="font-size: 14px;" id="warningspan"></span>
			<span class="round_error">●</span> <span style="font-size: 14px;" id="errorspan"></span>
			<span class="round_stop">●</span> <span style="font-size: 14px;" id="shieldspan"></span>					
			</div>
			<div style="margin-left:5px;">
						<span style="position: relative; top: 2px;">服务状态</span>
						<select id="query_by_status" class="easyui-combobox">
						   <option value="error">故障</option>
						   <option value="all">全部</option>
						   <option value="normal">正常</option>
						   <option value="warn">告警</option>
						   <option value="stop">屏蔽</option>
						</select>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<span style="position: relative; top: 2px;">地域</span>
						<select id="region" class="easyui-combobox">
						   <option value="all">全部</option>
						   <option value="1">广州</option>
						   <option value="2">成都</option>
						   <option value="4">香港</option>
						</select>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<span style="position: relative; top: 2px;">服务名</span> 
						<input type="text" id="host_name" class="messager-input" /> 
						<a href="#" class="easyui-linkbutton" iconCls="icon-search" id="query_cloud_host_btn">查询</a>
						<a href="#" class="easyui-linkbutton" iconCls="icon-redo" id="clear_cloud_host_btn">清除</a>
						
					</div>
				</div>
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
		
	window.name = "selfWin";
	
	var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
	ajax.async = false;

	// 布局初始化
	$("#cloud_host_datagrid").height( $(document.body).height());
	
	function formatCpuCore(val)
	{
		return val+"核";
	}
	function formatType(val)
	{
		if(val==0){
			return "INVALID";
		}else if(val == 1){
			return "DATA_SERVER";
		}else if(val == 2){
			return "CONTROL_SERVER";
		}else if(val == 3){
			return "NODE_CLIENT";
		}else if(val == 4){
			return "STORAGE_SERVER";
		}else if(val == 5){
			return "STATISTIC_SERVER";
		}else if(val == 6){
			return "MANAGE_TERMINAL";
		}else if(val == 7){
			return "HTTP_GATEWAY";
		}else if(val == 8){
			return "DATA_INDEX";
		}else if(val == 9){
			return "DATA_NODE";
		}else if(val == 10){
			return "STORAGE_MANAGER";
		}else if(val == 11){
			return "STORAGE_CLIENT";
		}else if(val == 12){
			return "STORAGE_PROTAL";
		}else if(val == 13){
			return "STORAGE_OBJECT";
		}else if(val == 14){
			return "INTELLIGENT_REOUTER";
		}
	}
	function formatRegion(val)
	{  
		if(val == 1) {  
		    return "广州";  
		} else if(val == 2) {
		    return "成都";  
		} else if(val == 3){
		 	return "北京";
		}else{
			return "香港";
		}
	}	
	function formatCreateTime(val, row)
	{
		return $.formatDateString(val, "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss");
	}
	
	function operateColumnFormatter(value, row, index)
	{
		if(row.newStatus=="stop"){
			return "<div row_index='"+index+"'>\
				<a href='#' class='datagrid_row_linkbutton shield_off'>取消屏蔽</a>\
			</div>";
		}else{
			return "<div row_index='"+index+"'>\
				<a href='#' class='datagrid_row_linkbutton shield_on'>屏蔽</a>\
			</div>";
		}
	}	
	function formatStatus(val)
	{  
		if(val == 0) {  
		    return "正常";  
		} else if(val == 1) {
		    return "告警";  
		} else if(val == 2){
		 	return "故障";
		}else{
			return "停止";
		}
	}  
	function statusColumnFormatter(value, row, index)
	{
		return "<span class='round_"+value+"'>●</span>";
	}	
	//更新状态数量	
	function updatenormalnum(){
		var data=$('#cloud_host_datagrid').datagrid('getData');
		var tempmap = data.map;
		$("#normalspan").html("正常:"+tempmap.normal);
		$("#warningspan").html("告警:"+tempmap.warning);
		$("#errorspan").html("故障:"+tempmap.error);
		$("#shieldspan").html("屏蔽:"+tempmap.stop);
	}		
	//查询结果为空
	function createView(){
		return $.extend({},$.fn.datagrid.defaults.view,{
		    onAfterRender:function(target){
		        $.fn.datagrid.defaults.view.onAfterRender.call(this,target);
		        var opts = $(target).datagrid('options');
		        var vc = $(target).datagrid('getPanel').children('div.datagrid-view');
		        vc.children('div.datagrid-empty').remove();
		        if (!$(target).datagrid('getRows').length){
		            var d = $('<div class="datagrid-empty"></div>').html( '没有相关记录').appendTo(vc);
		            d.css({
		                position:'absolute',
		                left:0,
		                top:50,
		                width:'100%',
		                textAlign:'center'
		            });
		        }
		    }
	    });
	}
	
	function onLoadSuccess()
	{
		$("body").css({
			"visibility":"visible"
		});
		updatenormalnum();
		// 每一行的'屏蔽'按钮
		$("a.shield_on").click(function(){
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#cloud_host_datagrid").datagrid("getData");
			updateshield("1",data.rows[rowIndex].name);
		});

		$("a.shield_off").click(function(){
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#cloud_host_datagrid").datagrid("getData");
			updateshield("0",data.rows[rowIndex].name);
		});
	}
	//屏蔽和取消屏蔽
	function updateshield(obj,id){
		ajax.remoteCall("bean://monitorService:updateShield",
				[ 'service',obj,id ], 
				function(reply) {
					if (reply.status == "exception") {
						if(reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>"){
							top.$.messager.alert("警告","会话超时，请重新登录","warning",function(){
								top.location.reload();
							});
						}else{
							top.$.messager.alert("警告",reply.exceptionMessage,"warning",function(){
								top.location.reload();
							});
						}
					}
					else if(reply.status=="fail"){
						top.$.messager.alert("提示",reply.result.message,"warning");
					}
					else {
						$('#cloud_host_datagrid').datagrid(
								'reload');
					}
				}
			);
	}	
	$(function(){
		// 查询
		$("#query_cloud_host_btn").click(function(){
			var queryParams = {};
			queryParams.query_by_status = $("#query_by_status").combobox("getValue");
			queryParams.region = $("#region").combobox("getValue");
			queryParams.service_name = $("#host_name").val().trim();
			if($("#host_name").val().trim()!=""){
				if($("#host_name").val().trim().length>50){ 
				    top.$.messager.alert("警告","主机名最多允许50个字符","warning");
					return;
				}
			}	
			$('#cloud_host_datagrid').datagrid({
				"queryParams": queryParams
			});
		});
		
		$("#clear_cloud_host_btn").click(function(){
			$("#query_by_status").combobox("setValue","error");
			$("#region").combobox("setValue","all");
			$("#host_name").val("");
		});
		$('#host_name').bind('keypress',function(event){
	        if(event.keyCode == "13")    
	        {
	        	$("#query_cloud_host_btn").click();
	        }
	    });
	});
	</script>
</html>