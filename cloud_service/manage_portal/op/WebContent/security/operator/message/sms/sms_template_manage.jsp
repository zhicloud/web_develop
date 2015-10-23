﻿<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
%>
<!DOCTYPE html>
<!-- sms_template_manage.jsp -->
<html>
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		
		<title>运营商管理员 - 短信模版管理</title>
		
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
		<%--<link rel="stylesheet" href="<%=request.getContextPath() %>/editor/themes/default/default.css" />--%>


		<style type="text/css">
		#sms_template_datagrid {
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
		</style>
	</head>
	<body style="visibility:hidden;">
		<form id="big_form"  method="post">
			<table id="sms_template_datagrid" class="easyui-datagrid" title="短信模版管理"
				data-options="
					url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=smsTemplateService&method=getAllTemplate',
					queryParams: {},
					toolbar: '#toolbar',
					rownumbers: true,
					striped: true,
					remoteSort: false,
					fitColumns: false,
					pagination: true,
					pageList: [10, 20, 50, 100, 200],
					pageSize: 20,
					view:createView(),
					onLoadSuccess: onLoadSuccess
				">
				<thead>
					<tr>
						<th data-options="checkbox:true"></th>
						<th field="name" width="150px" sortable=true>模版名称</th>
						<th field="configName" width="150px">发送人</th>
                        <th field="code" width="150px">标识代码</th>
                        <th field="modifiedTime" formatter="timeFormat" width="150px">更新时间</th>
						<th field="operate" formatter="templateColumnFormatter" width="100px">操作</th>
					</tr>
				</thead>
			</table>
	
			<div id="toolbar" style="padding: 3px;">
				<div style="display: table; width: 100%;">
					<div style="margin-bottom:5px;">
						<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="add_sms_template_btn">添加</a> 
						<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" id="del_sms_template_btn">删除</a>
					</div>
						模版名称<input class="messager-input" type="text" name = "name" id="template_name"/>
						<a href="#" class="easyui-linkbutton" iconCls="icon-search" id="query_sms_template_btn">查询</a>
						<a href="#" class="easyui-linkbutton" iconCls="icon-redo" id="clear_sms_template_btn">清除</a>
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
	<%--<script charset="utf-8" src="<%=request.getContextPath() %>/editor/kindeditor-min.js" type="text/javascript"></script>--%>
	<%--<script charset="utf-8" src="<%=request.getContextPath() %>/editor/zh_CN.js" type="text/javascript"></script>--%>

	<script type="text/javascript">
	window.name = "selfWin";

	var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
	ajax.async = false;
	

	// 布局初始化
	$("#sms_template_datagrid").height( $(document.body).height());

	function timeFormat(val, row)
	{
		return $.formatDateString(val, "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss");
	}

	function templateColumnFormatter(value, row, index)
	{
		return "<div row_index='"+index+"'>\
					<a href='#' class='datagrid_row_linkbutton modify_btn'>修改</a>\
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
			var data = $("#sms_template_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].id; 
            window.location.href = "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=smsTemplateService&method=modPage&id="+encodeURIComponent(id)
				
		});
	}
	
	$(function(){
		// 查询
		$("#query_sms_template_btn").click(function(){
			var queryParams = {};
			queryParams.name = $("#template_name").val().trim();
			$('#sms_template_datagrid').datagrid({
				"queryParams": queryParams
			});
		});
		//清除
		$("#clear_sms_template_btn").click(function(){
			$("#template_name").val("");

		});

		// 添加短信模版
		$("#add_sms_template_btn").click(function(){
			window.location.href = "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=smsTemplateService&method=addPage";

		});
		// 删除短信模版
		$("#del_sms_template_btn").click(function() {
			var rows = $('#sms_template_datagrid').datagrid('getSelections');
			if (rows == null || rows.length == 0) {
				top.$.messager.alert("警告","未选择删除项","warning");
				return;
			}
			var ids = rows.joinProperty("id");
			top.$.messager.confirm("确认", "确定要删除吗?", function (r) {
		        if (r) {   
					ajax.remoteCall("bean://smsTemplateService:deleteTemplateByIds",
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
								$('#sms_template_datagrid').datagrid(
										'reload');
							}
						}
					);
		        }  
		    }); 
		});


		$('#template_name').bind('keypress',function(event){
	        if(event.keyCode == "13")    
	        {
	        	$("#query_sms_template_btn").click();
	        }
	    });
	});
	</script>
</html>
