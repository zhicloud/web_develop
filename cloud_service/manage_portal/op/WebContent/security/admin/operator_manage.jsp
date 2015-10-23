<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));	
%>
<!DOCTYPE html>
<!-- operator_manage.jsp -->
<html>
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		<title>超级管理员 - 运营商员工管理</title>
		
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
		<style type="text/css">
		#operator_datagrid {
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
	        <input type="hidden" name="operator_id" id="operator_id" value=""/>
			<table id="operator_datagrid" class="easyui-datagrid" title="运营商员工管理"
				data-options="
					url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=operatorService&method=queryOperator',
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
						<th data-options="checkbox:true"></th>
						<th field="account" width="70px" sortable=true>邮箱</th>
						<th field="name" width="70px" sortable=true>昵称</th>
<!-- 						<th field="email" width="70px">e-mail</th> -->
						<th field="phone" width="70px">手机</th>
						<th field="groupName" width="70px">权限组</th>
						<th field="status"  formatter="formatStatus" width="70px" sortable=true>状态</th>
						<th field="operate" formatter="operateColumnFormatter" width="50px">操作</th>
					</tr>
				</thead>
			</table>
	
			<div id="toolbar" style="padding: 3px;">
				<div style="display: table; width: 100%;">
					<div style="display: table-cell; text-align: left">
						<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="add_operator_btn">添加</a> 
						<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" id="del_operator_btn">删除</a>
					</div>
					<div style="display: table-cell; text-align: right;">
						<span style="position: relative; top: 2px;">邮箱</span> 
						<input type="text" id="account" style="position: relative; top: 2px; height: 18px;" /> 
						<a href="#" class="easyui-linkbutton" iconCls="icon-search" id="query_operator_btn">查询</a>
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
		$("#operator_datagrid").height( $(document.body).height());
		
		function formatStatus(val,row)
		{  
			if(val == 1)
			{  
			    return "正常";  
			}
			else if(val == 2)
			{
			    return "禁用";  
			}
			else
			{
			 	return "结束";
			}
		}  
		
		function operateColumnFormatter(value, row, index)
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
				var data = $("#operator_datagrid").datagrid("getData");
				var id = data.rows[rowIndex].id;
				top.showSingleDialog({
					url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=operatorService&method=modPage&operatorId="+encodeURIComponent(id),
					onClose: function(data){
						$('#operator_datagrid').datagrid('reload');
					}
				});
			});
			// 每一行的'重置密码'按钮
			$("a.reset_password_btn").click(function(){
				$this = $(this);
				rowIndex = $this.parent().attr("row_index");
				var data = $("#operator_datagrid").datagrid("getData");
				var id = data.rows[rowIndex].id;
				top.showSingleDialog({
					url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=operatorService&method=resetPasswordPage&operatorId="+encodeURIComponent(id),
					onClose: function(data){
						$('#operator_datagrid').datagrid('reload');
					}
				});
			});
		}
		
		$(function(){
			// 查询
			$("#query_operator_btn").click(function(){
				var queryParams = {};
				queryParams.account = $("#account").val().trim();
				$('#operator_datagrid').datagrid({
					"queryParams": queryParams
				});
			});
			// 添加运营商
			$("#add_operator_btn").click(function(){
				top.showSingleDialog({
					url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=operatorService&method=addPage",
					onClose: function(data){
						$('#operator_datagrid').datagrid('reload');
					}
				});
			});
			// 删除角色
			$("#del_operator_btn").click(function() {
				var rows = $('#operator_datagrid').datagrid('getSelections');
				if (rows == null || rows.length == 0)
				{
					top.$.messager.alert("警告","未选择删除项","warning");
					return;
				}
				var ids = rows.joinProperty("id");
				top.$.messager.confirm("确认", "确定删除?", function (r) {  
			        if (r) {    
							ajax.remoteCall("bean://operatorService:deleteOperatorByIds",
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
										$('#operator_datagrid').datagrid('reload');
									}
								}
							);
			        }  
			    });   
			});
			
			$('#account').bind('keypress',function(event){
		        if(event.keyCode == "13")    
		        {
		        	$("#query_operator_btn").click();
		        }
		    });
		});
	</script>
</html>