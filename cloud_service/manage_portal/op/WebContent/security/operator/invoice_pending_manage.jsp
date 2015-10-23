<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));

%>
<!DOCTYPE html>
<!-- invoice_pending_manage.jsp -->
<html>
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		
		<title>运营商管理员 - 发票管理</title>
		
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
		<style type="text/css">
		#invoice_datagrid {
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
		</style>
	</head>
	<body style="visibility:hidden;">
		<form id="big_form"  method="post">
	        <input name="invoice_id" id="invoice_id" type="hidden" value=""/>
			<table id="invoice_datagrid" class="easyui-datagrid" title="待审核发票"
				style=""
				data-options="
					url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=invoiceService&method=queryAllInvoice',
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
						<th data-options="field:'totalAmount',width:60" >金额（元）</th>
						<th data-options="field:'invoiceTitle',width:120">发票抬头</th>
						<th data-options="field:'taxNumber',width:100">税号</th>
						<th data-options="field:'bankNumber',width:180">开户行及帐号</th>
						<th data-options="field:'addressTel',width:180">联系地址及电话</th>
						<th data-options="field:'submitTime',width:150" formatter= "timeFormat"sortable=true>提交时间</th>
						<th data-options="field:'status',width:100" formatter="statusFormatter">状态</th>
					</tr>
				</thead>
			</table>
	
			<div id="toolbar" style="padding: 3px;">
				<div style="display: table; width: 100%;">
					<div style="display: table-cell; text-align: left">
						<a href="#" class="easyui-linkbutton" iconCls="icon-print" plain="true" id="print_invoice_btn">导出</a>
						<a href="#" class="easyui-linkbutton" plain="true" id="export_invoice_btn">已导出</a> 
						<a href="#" class="easyui-linkbutton" plain="true" id="send_invoice_btn">已寄送</a>
						<a href="#" class="easyui-linkbutton" plain="true" id="send_success_btn">寄送成功</a>
					</div>
					<div style="display: table-cell; text-align: right;">
						<span style="position: relative; top: 2px;">发票抬头</span> 
						<input type="text" id="pending_title" style="position: relative; top: 2px; height: 18px;" /> 
						<a href="#" class="easyui-linkbutton" iconCls="icon-search" id="query_invoice_btn">查询</a>
						<a href="#" class="easyui-linkbutton" plain="true" id="invoice_setting_btn">设置</a>
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
	var INVOICE_IS_PENDING = 2;
	

	// 布局初始化
	$("#invoice_datagrid").height( $(document.body).height());
	
	function timeFormat(val, row)
	{
		return $.formatDateString(val, "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss");
	}
	
	function statusFormatter(val)	
	{   
		return "待审核";
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
		
	}
	
	
	$(function(){
		// 查询
		$("#query_invoice_btn").click(function(){
			var queryParams = {};
			queryParams.invoiceTitle = $("#pending_title").val().trim();
			queryParams.status = INVOICE_IS_PENDING;
			$('#invoice_datagrid').datagrid({
				"queryParams": queryParams
			});
		});
		// 批量导出
		$("#print_invoice_btn").click(function() {
			var rows = $('#invoice_datagrid').datagrid('getSelections');
			if (rows == null || rows.length == 0) {
				top.$.messager.alert("警告","未选择导出项","warning");
				return;
			}
			var ids = rows.joinProperty("id");
			top.$.messager.confirm("确认", "确定导出？", function (r) {  
		        if (r) {   
					ajax.remoteCall("bean://invoiceService:exportInvoiceByIds",
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
							} else if(reply.status == "success"){
								window.location.href= "<%=request.getContextPath()%>/invoiceExport.do?ids="+encodeURIComponent(ids)+"&ids=ids";
								$('#invoice_datagrid').datagrid(
								'reload');
							}else {
								$('#invoice_datagrid').datagrid(
										'reload');
							}
						}
					); 
					
		        }  
		    });   
		});
		// 已导出发票管理
		$("#export_invoice_btn").click(function(){
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=invoiceService&method=exportedPage",
				onClose: function(data){
					$('#invoice_datagrid').datagrid('reload');
				}
			});
		});
		
		// 已寄送发票管理
		$("#send_invoice_btn").click(function(){
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=invoiceService&method=sentPage",
				onClose: function(data){
					$('#invoice_datagrid').datagrid('reload');
				}
			});
		});
		
		//发票内容设置
		$("#invoice_setting_btn").click(function(){
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=invoiceService&method=settingPage",
				onClose: function(data){
					$('#invoice_datagrid').datagrid('reload');
				}
			});
		});
		
		// 已寄送成功发票管理
		$("#send_success_btn").click(function(){
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=invoiceService&method=sentSuccessPage",
				onClose: function(data){
					$('#invoice_datagrid').datagrid('reload');
				}
			});
		});
		
		 $('#pending_title').bind('keypress',function(event){
	        if(event.keyCode == "13")    
	        {
	        	$("#query_invoice_btn").click();
	        }
	    });
		 
		//初始化
		$(document).ready(function(){
			var queryParams = {};
			queryParams.status = INVOICE_IS_PENDING;
			$('#invoice_datagrid').datagrid({
				"queryParams": queryParams
			});
		});
		 
	});
	</script>
</html>