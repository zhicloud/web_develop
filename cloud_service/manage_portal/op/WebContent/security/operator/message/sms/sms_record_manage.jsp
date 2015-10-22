﻿<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
%>
<!DOCTYPE html>
<!-- email_record_manage.jsp -->
<html>
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		
		<title>运营商管理员 - 短信发送记录</title>
		
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
		<style type="text/css">
		#mail_record_datagrid {
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
	<body style="visibility:hidden;height:500px;padding-bottom:10px;">
			<table border="0" style="width:auto;">
				<tr>
					<td id="totaled" style="padding:5px;width:150px">总充值数量：<%=(String)request.getAttribute("balance_totaled") %>条</td>
					<td id="balance" style="padding:5px;width:180px"> 帐户短信可用数量 ：<%=(String)request.getAttribute("balance_balance") %>条</td>
					<td id="sended" style="padding:5px;width:200px"> 已发送或使用短信数量 ：<%=(String)request.getAttribute("balance_sended") %>条</td>
				</tr>
			</table>
		<form id="big_form"  method="post">
			<table id="mail_record_datagrid" class="easyui-datagrid" title="短信发送记录" 
				data-options="
					url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=messageRecordService&method=getAllRecord',
					queryParams: {type:1},
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
						<th field="senderAddress" width="150px">发送人</th>
                        <th field="recipientAddress" width="150px">接收方电话</th>
                        <th field="content" width="400px">内容</th>
                        <th field="sms_state" width="100px" formatter="stateFormat">状态</th>
                        <th field="createTime" formatter="timeFormat" width="150px">发送时间</th>
<!-- 						<th field="operate" formatter="recordColumnFormatter" width="100px">操作</th>
 -->					</tr>
				</thead>
			</table>
	
			<div id="toolbar" style="padding: 3px;">
				<div style="display: table; width: 100%;">
					<div style="margin-bottom:5px;">
						<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" id="del_mail_record_btn">删除</a>
					</div>
						接收方电话<input class="messager-input" type="text" name = "recipient_address" id="recipient_address"/>
						<a href="#" class="easyui-linkbutton" iconCls="icon-search" id="query_mail_record_btn">查询</a>
						<a href="#" class="easyui-linkbutton" iconCls="icon-redo" id="clear_mail_record_btn">清除</a>
						
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
	$("#mail_record_datagrid").height( $(document.body).height());

	function timeFormat(val, row)
	{
		return $.formatDateString(val, "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss");
	}

	function stateFormat(val, row)
	{
		if(val=="1"){
			return "成功";
		}
		if(val==""||val=="null"||val==null){
			return "";
		}
		return "失败";
	}

	function recordColumnFormatter(value, row, index)
	{
		return "<div row_index='"+index+"'>\
					<a href='#' class='datagrid_row_linkbutton show_btn'>查看详情</a>\
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
		$("a.show_btn").click(function(){
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#mail_record_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].id; 
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=emailConfigService&method=modPage&id="+encodeURIComponent(id),
				onClose: function(data){
					$('#mail_record_datagrid').datagrid('reload');
				}
			});
		});
	}
	
	$(function(){
		// 查询
		$("#query_mail_record_btn").click(function(){
			var queryParams = {};
			queryParams.recipient_address = $("#recipient_address").val().trim();
            queryParams.type = 1;
			$('#mail_record_datagrid').datagrid({
				"queryParams": queryParams
			});
		});
		//清除
		$("#clear_mail_record_btn").click(function(){
			$("#recipient_address").val("");

		});
		// 删除邮件发送记录
		$("#del_mail_record_btn").click(function() {
			var rows = $('#mail_record_datagrid').datagrid('getSelections');
			if (rows == null || rows.length == 0) {
				top.$.messager.alert("警告","未选择删除项","warning");
				return;
			}
			var ids = rows.joinProperty("id");
			top.$.messager.confirm("确认", "确定要删除吗?", function (r) {
		        if (r) {   
					ajax.remoteCall("bean://messageRecordService:deleteRecordByIds",
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
								$('#mail_record_datagrid').datagrid(
										'reload');
							}
						}
					);
		        }  
		    }); 
		});


		$('#recipient_address').bind('keypress',function(event){
	        if(event.keyCode == "13")    
	        {
	        	$("#query_mail_record_btn").click();
	        }
	    });
	});
	</script>
</html>
