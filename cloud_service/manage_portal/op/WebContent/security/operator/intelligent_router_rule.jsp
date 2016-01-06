<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="com.zhicloud.op.vo.MarkVO"%>
<%@page import="java.util.List"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
	
 %>
<!DOCTYPE html>
<!-- intelligent_router_rule.jsp -->
<html>
<head>
	<meta charset="UTF-8" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
	<title>运营商管理员 - 例外规则</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
</head>
	
<body style="visibility:hidden;">
<div class="oper-wrap">
	<div class="tab-container">
		<div id="toolbar">
			<div style="display: table; width: 100%;">
				<div style="display: table-cell; text-align: left">
					<a class="easyui-linkbutton oper-btn-sty" href="javascript:;" data-options="plain:true,iconCls:'icon-add'" id="add_rule_btn">添加</a> 
					<a class="easyui-linkbutton oper-btn-sty f-ml10" href="javascript:;" data-options="plain:true,iconCls:'icon-remove'" id="del_rule_btn">删除</a>
				</div>
				<div style="display: table-cell; text-align: right;">
					<span class="sear-row">
						<label class="f-mr5">地域</label>
						<select id="region" name="region" class="slt-sty">
							<option value="1">广州</option>
							<option value="2">成都</option>
							<option value="4">香港</option>
						</select>
					</span>
					<span class="sear-row">
						<a class="easyui-linkbutton oper-btn-sty f-ml10" href="javascript:;" data-options="iconCls:'icon-search'" id="query_cloud_host_btn">查询</a>
					</span>
				</div>
			</div>
		</div>
		<table id="agent_datagrid" class="easyui-datagrid" title="例外规则" data-options="url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=intelligentRouterService&method=queryRule',queryParams: {},toolbar: '#toolbar',rownumbers: false,striped: true,remoteSort:false,fitColumns: true,pagination: false,pageSize: 20,view: createView(),onLoadSuccess: onLoadSuccess()">
			<thead>
				<tr>
					<th data-options="field:'ck',checkbox:true"></th>
					<th data-options="field:'region',formatter:formatRegion,width:100">地域</th>
					<th data-options="field:'target',sortable:true,width:100">智能路由</th>
					<th data-options="field:'mode',sortable:true,formatter:formatMode,width:100">规则</th>
					<th data-options="field:'ip',width:100">IP[目的地址IP，源地址IP]</th>
					<th data-options="field:'port',sortable:true,width:100">port[地址转换前目的端口号, 地址转换后目的端口号]</th>
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
<script type="text/javascript">
window.name = "selfWin";

var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
ajax.async = false;

// 布局初始化
$("#agent_datagrid").height( $(document.body).height()-20);

function onLoadSuccess(){
	$("body").css({"visibility":"visible"});
}

function formatRegion(val){  
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

function formatMode(val, row)
{  
	if(val == 0){  
	    return "INPUT规则";  
	}else if(val == 1) {
	    return "FORWARD规则";  
	}else{
	 	return "NAT规则";
	}
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

$(function(){ 
	$("#query_cloud_host_btn").click(function(){
		var queryParams = {};
			queryParams.region = $("#region").combobox("getValue"); 
		$('#agent_datagrid').datagrid({
			"queryParams": queryParams
		});
	});
	 
	$("#add_rule_btn").click(function(){ 
		top.showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=intelligentRouterService&method=addRulePage",
			onClose: function(data){
				$('#agent_datagrid').datagrid('reload');
			}
		});
	});
	// 删除规则
	$("#del_rule_btn").click(function() {
		var rows = $('#agent_datagrid').datagrid('getSelections');
		if (rows == null || rows.length == 0) {
			top.$.messager.alert("警告","未选择删除项","warning");
			return;
		}
		var region = rows[0].region;
		var mode = rows[0].mode;
		var target = rows[0].target;
		var ip = rows[0].ip;
		var port = rows[0].port;
			top.$.messager.confirm("确认", "确定删除?", function (r) {  
	        if (r) {   
				ajax.remoteCall("bean://intelligentRouterService:deleteRule",
					[ target,ip,port,region ,mode], 
					function(reply) {
						if (reply.status == "exception") {
							if(reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>"){
								top.$.messager.alert("警告","会话超时，请重新登录","warning",function(){
									top.location.reload();
								});
							}else{
								top.$.messager.alert("警告",reply.result.exceptionMessage,"warning",function(){
									top.location.reload();
								});
							}
						} else {
							top.$.messager.alert("警告",reply.result.message,"warning",function(){
								$('#agent_datagrid').datagrid(
								'reload');
							});
							
						}
					}
				); 
	        }  
	    });   
	});
});
</script>
</body>
</html>