<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));	
%>
<!DOCTYPE html>
<!-- sys_group_manage.jsp -->
<html>
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		
		<title>超级管理员 - 权限组管理</title>
		
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
		<style type="text/css">
		#sys_group_datagrid {
			border: 0px solid red;
		}
		.panel-header {
			border-top: 0px;
			border-bottom: 1px solid #dddddd;
		}
		.panel-header, .panel-body {
			border-left: 0px;
			border-right: 0px;
		}
		.panel-body {
			border-bottom: 0px;
		}
		a.datagrid_row_linkbutton {
			padding: 2px;
		}
		</style>
	</head>
	<body style="visibility:hidden;">
		<form id="big_form"   method="post">
			
			<table id="sys_group_datagrid" class="easyui-datagrid" title="权限组管理" 
				data-options="
					url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=sysGroupService&method=querySysGroup',
					queryParams: {},
					toolbar: '#toolbar',
					rownumbers: true,
					striped: true,
					remoteSort:false,
					pagination: true,
					pageList: [10, 20, 50, 100, 200],
					pageSize: 20,
					view: createView(),
					onLoadSuccess: onLoadSuccess
				">
				<thead>
					<tr>
						<th data-options="checkbox:true"></th>
						<th field="groupName" width="120" sortable=true>组名</th>
						<th field="operate" width="150" formatter="operateColumnFormatter">操作</th>
					</tr>
				</thead>
			</table>
			
			<div id="toolbar" style="padding:3px;">
				<div style="display: table; width: 100%;">
					<div style="display: table-cell; text-align: left">
						<a href="#" class="easyui-linkbutton" iconCls="icon-add"    plain="true" id="add_group_btn">添加</a>
						<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" id="del_group_btn">删除</a>
					</div>
					<div style="display: table-cell; text-align: right;">
						<span style="position: relative; top: 2px;">组名：</span> 
						<input type="text" id="group_name" style="position: relative; top: 2px; height: 18px;" /> 
						<a href="#" class="easyui-linkbutton" iconCls="icon-search" id="query_role_btn">查询</a>
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
	$("#sys_group_datagrid").height( $(document.body).height());
	
	function operateColumnFormatter(value, row, index)
	{
		return "<div row_index='"+index+"'>\
					<a href='#' class='datagrid_row_linkbutton modify_btn'>修改</a>\
					<a href='#' class='datagrid_row_linkbutton set_groupitem_btn'>组员设置</a>\
					<a href='#' class='datagrid_row_linkbutton set_role_btn'>角色设置</a>\
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
			var data = $("#sys_group_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].id;
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=sysGroupService&method=modPage&groupId="+encodeURIComponent(id),
				onClose: function(data){
					$('#sys_group_datagrid').datagrid('reload');
				}
			});
		});
		// 每一行的'组员设置'按钮
		$("a.set_groupitem_btn").click(function(){
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#sys_group_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].id;
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=sysGroupService&method=setGroupMemberPage&groupId="+encodeURIComponent(id),
				onClose: function(data){
					$('#sys_group_datagrid').datagrid('reload');
				}
			});
		});
		// 每一行的'角色设置'按钮
		$("a.set_role_btn").click(function(){
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#sys_group_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].id;
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=sysGroupService&method=setRolePage&groupId="+encodeURIComponent(id),
				onClose: function(data){
					$('#sys_group_datagrid').datagrid('reload');
				}
			});
		});
	}
	
	$(function(){
		// 查询
		$("#query_role_btn").click(function(){
			var queryParams = {};
			queryParams.group_name = $("#group_name").val().trim();
			$('#sys_group_datagrid').datagrid({
				"queryParams": queryParams
			});
		});
		// 添加权限组
		$("#add_group_btn").click(function(){
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=sysGroupService&method=addPage",
				onClose: function(data){
					$('#sys_group_datagrid').datagrid('reload');
				}
			});
		});
		// 删除权限组
		$("#del_group_btn").click(function(){
			var rows = $('#sys_group_datagrid').datagrid('getSelections');
			if (rows == null || rows.length == 0)
			{
				top.$.messager.alert("警告","未选择删除项","warning");
				return;
			}
			var ids = rows.joinProperty("id");
			top.$.messager.confirm("确认", "确定删除?", function (r) {  
		        if (r) {     
					ajax.remoteCall("bean://sysGroupService:deleteSysGroupByIds",
						[ids],
						function(reply)
						{
							if( reply.status=="exception" )
							{
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
							else
							{
								$('#sys_group_datagrid').datagrid('reload');
							}
						}
					);
		        }  
		    });   
		});
		
		$('#group_name').bind('keypress',function(event){
	        if(event.keyCode == "13")    
	        {
	        	$("#query_role_btn").click();
	        }
	    });
	});
	
	</script>
</html>