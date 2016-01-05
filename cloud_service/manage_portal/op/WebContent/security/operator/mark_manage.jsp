﻿<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
%>
<!DOCTYPE html>
<!-- mark_manage.jsp -->
<html>
<head>
	<meta charset="UTF-8" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
	<title>运营商管理员 - 标签管理</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
</head>

<body style="visibility:hidden;">
<div class="oper-wrap">
	<div class="tab-container">
		<div id="toolbar" class="toolbar-sty">
			<div style="display: table; width: 100%;">
				<div style="display: table-cell; text-align: left">
					<a id="add_mark_btn" class="easyui-linkbutton oper-btn-sty" href="javascript:;" data-options="plain:true,iconCls:'icon-add'">添加</a> 
					<a id="del_mark_btn" class="easyui-linkbutton oper-btn-sty f-ml10" href="javascript:;" data-options="plain:true,iconCls:'icon-remove'">删除</a>
				</div>
				<div style="display: table-cell; text-align: right;">
					<label class="f-mr5">标签名</label>
					<input class="mark-name-sty" type="text" id="mark_account" /> 
					<a id="query_mark_btn" class="easyui-linkbutton oper-btn-sty f-ml10" href="javascript:;" data-options="iconCls:'icon-search'">查询</a>
				</div>
			</div>
		</div>
		<table id="mark_datagrid" class="easyui-datagrid" title="标签管理" data-options="url:'<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=markService&method=queryAllMarks',queryParams: {},toolbar: '#toolbar',rownumbers: false,striped: true,remoteSort: false,fitColumns: true,pagination: true,pageSize: 20,view:createView(),onLoadSuccess: onLoadSuccess">
			<thead>
				<tr>
					<th data-options="field:'ck',checkbox:true"></th>
					<th data-options="field:'name',sortable:true,width:100">标签名</th>
					<th data-options="field:'description',width:200">描述</th>
					<th data-options="field:'operate',formatter:markFormatter,width:100">操作</th>
				</tr>
			</thead>
		</table>
	</div>
</div>
	
<!-- JavaScript_start -->
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.ext.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
window.name = "selfWin";
var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
ajax.async = false;

//布局初始化
$("#mark_datagrid").height($('body').height()-20);

function formatCreateTime(val, row){
	return $.formatDateString(val, "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss");
}

function markFormatter(value, row, index){
	var tplHtml = "<div row_index='"+index+"'><a class='datagrid_row_linkbutton modify_btn' href='javascript:;'>修改</a></div>"
	return tplHtml;
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

function onLoadSuccess(){
	$("body").css({
		"visibility":"visible"
	});
	// 每一行的'修改'按钮
	$("a.modify_btn").click(function(){
		$this = $(this);
		rowIndex = $this.parent().attr("row_index");
		var data = $("#mark_datagrid").datagrid("getData");
		var id = data.rows[rowIndex].id; 
		top.showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=markService&method=modPage&markId="+encodeURIComponent(id),
			onClose: function(data){
				$('#mark_datagrid').datagrid('reload');
			}
		});
	});
	// 每一行的'重置密码'按钮
	$("a.reset_password_btn").click(function(){
		$this = $(this);
		rowIndex = $this.parent().attr("row_index");
		var data = $("#mark_datagrid").datagrid("getData");
		var id = data.rows[rowIndex].id;
		top.showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=terminalUserService&method=resetPasswordPage&terminalUserId="+encodeURIComponent(id),
			onClose: function(data){
				$('#mark_datagrid').datagrid('reload');
			}
		});
	});
	// 每一行的'充值记录'按钮
	$("a.recharge_btn").click(function(){
		$this = $(this);
		rowIndex = $this.parent().attr("row_index");
		var data = $("#mark_datagrid").datagrid("getData");
		var id = data.rows[rowIndex].id;
		top.showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=accountBalanceService&method=getRechargeRecordByUserId&terminalUserId="+encodeURIComponent(id),
			onClose: function(data){
				$('#mark_datagrid').datagrid('reload');
			}
		});
	});
	// 每一行的'消费记录'按钮
	$("a.consume_btn").click(function(){
		$this = $(this);
		rowIndex = $this.parent().attr("row_index");
		var data = $("#mark_datagrid").datagrid("getData");
		var id = data.rows[rowIndex].id;
		top.showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=accountBalanceService&method=toConsumptionRecordPage&terminalUserId="+encodeURIComponent(id),
			onClose: function(data){
				$('#mark_datagrid').datagrid('reload');
			}
		});
	});
	// 每一行的'操作日志'按钮
	$("a.operation_btn").click(function(){
		$this = $(this);
		rowIndex = $this.parent().attr("row_index");
		var data = $("#mark_datagrid").datagrid("getData");
		var id = data.rows[rowIndex].id;
		top.showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=operLogService&method=managePage&terminalUserId="+encodeURIComponent(id),
			onClose: function(data){
				$('#mark_datagrid').datagrid('reload');
			}
		});
	});
}

$(function(){
	// 查询
	$("#query_mark_btn").click(function(){
		var queryParams = {};
		queryParams.mark_name = $("#mark_account").val().trim();
		$('#mark_datagrid').datagrid({
			"queryParams": queryParams
		});
	});
	// 添加标签
	$("#add_mark_btn").click(function(){
		top.showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=markService&method=addPage",
			onClose: function(data){
				$('#mark_datagrid').datagrid('reload');
			}
		});
	});
	// 删除标签
	$("#del_mark_btn").click(function() {
		var rows = $('#mark_datagrid').datagrid('getSelections');
		if (rows == null || rows == "" || rows.length == 0) {
			top.$.messager.alert("警告","未选择删除项","info");
			return;
		}
		var ids = rows.joinProperty("id");
		top.$.messager.confirm("确认", "删除标签同时会删除与用户的关联<br/>确定要删除吗?", function (r) {  
	        if (r) {   
				ajax.remoteCall("bean://markService:deleteMarkByIds",
					[ ids ], 
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
						} else {
							$('#mark_datagrid').datagrid(
									'reload');
						}
					}
				);
	        }  
	    }); 
	});
	
	$('#mark_account').bind('keypress',function(event){
        if(event.keyCode == "13"){ $("#query_mark_btn").click(); }
    });
});
</script>
<!-- JavaScript_end -->
</body>
</html>