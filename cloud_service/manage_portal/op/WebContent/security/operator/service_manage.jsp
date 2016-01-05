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
<!-- service_manage.jsp -->
<html>
<head>
	<meta charset="UTF-8" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
	<title>运营商 - 服务管理</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
</head>
	
<body style="visibility:hidden;">
<div class="oper-wrap">
	<div class="tab-container">
		<div id="toolbar">
			<div style="display: table; width: 100%;">
				<div style="line-height:24px;margin-bottom:5px;">
					<span class="round_normal"></span> 
					<span style="font-size: 12px;" id="normalspan"></span>
					<span class="f-ml10 round_warning"></span> 
					<span style="font-size: 12px;" id="warningspan"></span>
					<span class="f-ml10 round_error"></span> 
					<span style="font-size: 12px;" id="errorspan"></span>
					<span class="f-ml10 round_stop"></span> 
					<span style="font-size: 12px;" id="shieldspan"></span>					
				</div>
				<div class="sear-info f-mb5">
					<span class="sear-row">
						<label class="f-mr5">服务状态</label>
						<select id="query_by_status" class="slt-sty">
							<option value="error">故障</option>
							<option value="all">全部</option>
							<option value="normal">正常</option>
							<option value="warn">告警</option>
							<option value="stop">屏蔽</option>
						</select>
					</span>
					<span class="sear-row">
						<label class="f-ml10 f-mr5">地域</label>
						<select id="region" class="slt-sty">
							<option value="all">全部</option>
							<option value="1">广州</option>
							<option value="2">成都</option>
							<option value="4">香港</option>
						</select>
					</span>
					<span class="sear-row">
						<label class="f-ml10 f-mr5">服务名</label> 
						<input type="text" id="host_name" class="messager-input" /> 
					</span>
					<span class="sear-row">
						<a class="easyui-linkbutton oper-btn-sty f-ml10" href="javascript:;" data-options="iconCls:'icon-search'" id="query_cloud_host_btn">查询</a>
						<a class="easyui-linkbutton oper-btn-sty f-ml10" href="javascript:;" data-options="iconCls:'icon-redo'" id="clear_cloud_host_btn">清除</a>
					</span>
				</div>
			</div>
		</div>
		<table id="cloud_host_datagrid" class="easyui-datagrid" title="服务管理" data-options="url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=serviceService&method=queryService',queryParams: {},toolbar: '#toolbar',rownumbers: true,striped: true,remoteSort:false,fitColumns: true,pagination: true,pageSize: 20,view: createView(),onLoadSuccess: onLoadSuccess">
			<thead>
				<tr>
					<th data-options="field:'name',width:100">服务名称</th>
					<th data-options="field:'group',width:100">服务组名</th>
					<th data-options="field:'type',formatter:formatType,width:100">服务类型</th>
					<th data-options="field:'region',formatter:formatRegion,width:100">地域</th>
					<th data-options="field:'ip',width:100">IP</th>
					<th data-options="field:'port',width:100">端口</th>
					<th data-options="field:'version',width:100">版本号</th>
					<th data-options="field:'newStatus',formatter:statusColumnFormatter,width:100">状态</th>
					<th data-options="field:'operate',formatter:operateColumnFormatter,width:100">操作</th>
				</tr>
			</thead>
		</table>
	</div>
</div>

<!-- JavaScript -->
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
$("#cloud_host_datagrid").height( $(document.body).height()-20);

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
	return "<span class='round_"+value+"'></span>";
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
</body>
</html>