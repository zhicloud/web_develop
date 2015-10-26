﻿<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
%>
<!DOCTYPE html>
<!-- terminal_user_manage.jsp -->
<html>
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		
		<title>运营商管理员 - 终端用户管理</title>
		
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
		<style type="text/css">
		#agent_datagrid {
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
		</style>
	</head>
	<body style="visibility:hidden;">
		<form id="big_form"  method="post">
	        <input name="terminalUser_id" id="terminalUser_id" type="hidden" value=""/>
			<table id="terminal_user_datagrid" class="easyui-datagrid" title="终端用户管理"
				data-options="
					url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=terminalUserService&method=queryTerminalUserForOperator',
					queryParams: {},
					toolbar: '#toolbar',
					rownumbers: true,
					striped: true,
					remoteSort: false,
					fitColumns: true,
					pagination: true,
					pageList: [10, 20, 50, 100, 200],
					pageSize: 20,
					view:createView(),
					onLoadSuccess: onLoadSuccess
				">
				<thead>
					<tr>
						<th data-options="checkbox:true"></th>
						<th field="account" width="100px" sortable=true>邮箱</th>
						<th field="phone" width="100px">手机</th>
						<th field="belongingAccount" width="70px" sortable=true>归属</th>
						<th field="percentOff"   width="70px" sortable=true>折扣率(%)</th>
						<th field="status" formatter="formatStatus" width="70px">状态</th>
						<th field="operate" formatter="terminalUserColumnFormatter" width="60px">操作</th>
					</tr>
				</thead>
			</table>
	
			<div id="toolbar" style="padding: 3px;">
				<div style="display: table; width: 100%;">
					<div style="display: table-cell; text-align: left">
						<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="add_terminal_user_btn">添加</a> 
						<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" id="del_terminal_user_btn">删除</a>
					</div>
					<div style="display: table-cell; text-align: right;">
						<span style="position: relative; top: 2px;">邮箱</span> 
						<input type="text" id="terminal_user_account" style="position: relative; top: 2px; height: 18px;" /> 
						<a href="#" class="easyui-linkbutton" iconCls="icon-search" id="query_terminal_user_btn">查询</a>
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
	<script type="text/javascript">
	window.name = "selfWin";
	
	var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
	ajax.async = false;
	

	// 布局初始化
	$("#terminal_user_datagrid").height( $(document.body).height());

	function formatCreateTime(val, row)
	{
		return $.formatDateString(val, "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss");
	}
	
	function formatStatus(val,row)
	{  
		if(val == 1) {  
		    return "未验证";  
		} else if(val == 2) {
		    return "正常";  
		} else if(val == 3) {
		 	return "禁用";
		} else if(val == 4) {
			return "欠费";
		} else {
			return "结束";
		}
	}  
	
	function formatBelongingType(val,row){
		if(val == 1){
			return "运营商";
		}else{
			return "代理商";
		}
	}
	
	function terminalUserColumnFormatter(value, row, index)
	{
		return "<div row_index='"+index+"'>\
					<a href='#' class='datagrid_row_linkbutton modify_btn'>修改</a>\
					<a href='#' class='datagrid_row_linkbutton reset_password_btn'>重置密码</a>\
				</div>";
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
		// 每一行的'修改'按钮
		$("a.modify_btn").click(function(){
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#terminal_user_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].id; 
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=terminalUserService&method=modPage&terminalUserId="+encodeURIComponent(id),
				onClose: function(data){
					$('#terminal_user_datagrid').datagrid('reload');
				}
			});
		});
		// 每一行的'重置密码'按钮
		$("a.reset_password_btn").click(function(){
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#terminal_user_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].id;
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=terminalUserService&method=resetPasswordPage&terminalUserId="+encodeURIComponent(id),
				onClose: function(data){
					$('#terminal_user_datagrid').datagrid('reload');
				}
			});
		});
	}
	
	$(function(){
		// 查询
		$("#query_terminal_user_btn").click(function(){
			var queryParams = {};
			queryParams.terminal_user_account = $("#terminal_user_account").val().trim();
			$('#terminal_user_datagrid').datagrid({
				"queryParams": queryParams
			});
		});
		// 添加终端用户
		$("#add_terminal_user_btn").click(function(){
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=terminalUserService&method=addPage",
				onClose: function(data){
					$('#terminal_user_datagrid').datagrid('reload');
				}
			});
		});
		// 删除终端用户
		$("#del_terminal_user_btn").click(function() {
			var rows = $('#terminal_user_datagrid').datagrid('getSelections');
			if (rows == null || rows.length == 0) {
				top.$.messager.alert("警告","未选择删除项","warning");
				return;
			}
			var ids = rows.joinProperty("id");
			top.$.messager.confirm("确认", "删除终端用户，并删除其已申请的云主机<br/>确定要删除吗?", function (r) {  
		        if (r) {   
					ajax.remoteCall("bean://terminalUserService:deleteTerminalUserByIds",
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
								$('#terminal_user_datagrid').datagrid(
										'reload');
							}
						}
					);
		        }  
		    }); 
		});
		
		$('#terminal_user_account').bind('keypress',function(event){
	        if(event.keyCode == "13")    
	        {
	        	$("#query_terminal_user_btn").click();
	        }
	    });
	});
	</script>
</html>