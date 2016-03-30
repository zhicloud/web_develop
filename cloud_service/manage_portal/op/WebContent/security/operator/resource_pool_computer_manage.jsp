<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
%>
<!DOCTYPE html>
<!-- resource_pool_computer_manage.jsp -->
<html>
<head>
	<meta charset="UTF-8" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
	<title>运营商管理员 - 计算资源池管理</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
</head>
	
<body style="visibility:hidden;">
<div class="oper-wrap">
	<div class="tab-container">
        <input name="agent_id" id="agent_id" type="hidden" value=""/>
        <div id="toolbar">
			<div style="display: table; width: 100%;">
				<div style="display: table-cell; text-align: left">
                    <span class="sear-row">
					</span>
				</div>
				<div style="display: table-cell; text-align: right;">
					<span class="sear-row">
					</span>
				</div>
			</div>
		</div>
		<table id="computer_datagrid" class="easyui-datagrid" title="计算资源池管理" data-options="url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=resourcePoolService&method=queryComputerPool',queryParams: {},toolbar: '#toolbar',rownumbers: true,striped: true,remoteSort:false,fitColumns: true,pagination: true,pageSize: 20, onLoadSuccess: onLoadSuccess()">
			<thead>
				<tr>
					<th data-options="field:'name',width:80">资源池名</th>
					<th data-options="field:'region',formatter:formatRegion,width:80">地域</th>
					<th data-options="field:'cpu_count_text',sortable:true,formatter:formatCpuCore,width:100">CPU核数</th>
					<th data-options="field:'cpuUsage',sortable:true,formatter:formatUsage,width:100">CPU占用率</th>
					<th data-options="field:'memory_text',sortable:true,width:100">内存[可用,总量]</th>
					<th data-options="field:'memoryUsage',sortable:true,formatter:formatUsage,width:100">内存利用率</th>
					<th data-options="field:'disk_text',sortable:true,width:80">磁盘[可用,总量]</th>
					<th data-options="field:'diskUsage',sortable:true,formatter:formatUsage,width:100">磁盘利用率</th>
					<th data-options="field:'host',sortable:true,width:150">主机数[停止,告警,故障,正常]</th>
					<th data-options="field:'node',sortable:true,width:150">节点数[停止,告警,故障,正常]</th>
					<th data-options="field:'status',sortable:true,formatter:formatStatus,width:80">状态</th>
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
$("#computer_datagrid").height( $(document.body).height()-20);
function formatRegion(val)
{
	if(val==1){
		return "广州";
	}else if(val==2){
		return "成都";
	}else if(val==3){
		return "北京";
	}else {
		return "香港";
	}
}
function formatCpuCore(val)
{
	return val+"核";
}
function formatArray(val)
{
	var my_val = new String(val);
	var arr = my_val.split(",");
	var arrL = [];
	for(var i=0;i<arr.length;i++){
		var l = CapacityUtil.toCapacityLabel(arr[i], 0);
		arrL.push(l);
	}
	return arrL;
}
function formatStatus(val)
{
	if(val==0){
		return "正常";
	}else if(val==1){
		return "告警";
	}else if(val==2){
		return "故障";
	}else {
		return "停止";
	}
}
function formatUsage(val)
{
	if(val!=0){
		var usage = val * 100;
		var u = usage.toFixed(2);
		return u+"%";
	}else{
		return "0";
	}
	
}

function formatCapacity(val)
{
	return CapacityUtil.toCapacityLabel(val, 0);
}

function formatFlow(val) {
	return FlowUtil.toFlowLabel(val, 0);
}


function onLoadSuccess() {
	$("body").css({
		"visibility":"visible"
	});

}


</script>
</body>
</html>
