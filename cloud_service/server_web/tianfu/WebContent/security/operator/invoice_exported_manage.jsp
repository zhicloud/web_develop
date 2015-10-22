<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));

%>
<!DOCTYPE html>
<!-- exported_export_manage.jsp -->
<html>
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		
		<title>运营商管理员 - 发票管理</title>
		
	</head>
	<body>
<div id="exported_dlg_container">
	<div id="exported_dlg" class="easyui-dialog" title="已导出发票"
		style="width:600px; height:600px; padding:10px;" 
		data-options="
			iconCls: 'icon-print',
			buttons: '#exported_dlg_buttons',
			modal: true,
			onMove:_exported_dlg_scope_.onMove,
			onClose: function(){
				$(this).dialog('destroy');
			},
			onDestroy: function(){
				delete _exported_dlg_scope_;
			}
		">
		<form id="big_form"  method="post">
	        <input name="exported_id" id="exported_id" type="hidden" value=""/>
			<table id="exported_datagrid" class="easyui-datagrid"
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
					pageSize: 10,
					view: createView()
				">
				<thead>
					<tr>
						<th data-options="checkbox:true"></th>
						<th data-options="field:'totalAmount',width:140" >金额（元）</th>
						<th data-options="field:'invoiceTitle',width:300">发票抬头</th>
						<th data-options="field:'submitTime',width:180" formatter= "timeFormat"sortable=true>提交时间</th>
						<th data-options="field:'status',width:100" formatter="statusFormatter">状态</th>
					</tr>
				</thead>
			</table>
	
			<div id="toolbar" style="padding: 3px;">
				<div style="display: table; width: 100%;">
					<div style="display: table-cell; text-align: left">
						<a href="#" class="easyui-linkbutton" iconCls="icon-print" plain="true" id="exported_btn">再次导出</a>
						<a href="#" class="easyui-linkbutton" plain="true" id="confirm_exported_btn">确认寄送</a>
					</div>
					<div style="display: table-cell; text-align: right;">
						<span style="position: relative; top: 2px;">发票抬头</span> 
						<input type="text" id="export_title" style="position: relative; top: 2px; height: 18px;" /> 
						<a href="#" class="easyui-linkbutton" iconCls="icon-search" id="query_exported_btn">查询</a>
					</div>
				</div>
			</div>
	
		</form>
		</div>

	<div id="exported_dlg_buttons">
		<a href="javascript:" class="easyui-linkbutton"
			id="exported_dlg_close_btn"> &nbsp;关&nbsp;闭&nbsp; </a>
	</div>
</div>
	</body>
	
	<script type="text/javascript">
	window.name = "selfWin";
	
	var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
	ajax.async = false;
	var INVOICE_IS_EXPORTED = 4;

	

	// 布局初始化
	$("#exported_datagrid").height(500);
	
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
	
	function timeFormat(val, row)
		{
			return $.formatDateString(val, "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss");
		}
		
		function statusFormatter(val)	
		{   
			return "已导出"
		}
		
		

	
	var _exported_dlg_scope_ = new function(){
		
		// 批量确认
		$("#confirm_exported_btn").click(function() {
			var rows = $('#exported_datagrid').datagrid('getSelections');
			if (rows == null || rows.length == 0) {
				top.$.messager.alert("警告","未选择确认项","warning");
				return;
			}
			var ids = rows.joinProperty("id");
			top.$.messager.confirm("确认", "确定寄送？", function (r) {  
		        if (r) {   
					ajax.remoteCall("bean://invoiceService:sendInvoiceByIds",
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
								$('#exported_datagrid').datagrid(
										'reload');
							}
						}
					); 
					
		        }  
		    });   
		});		
		// 批量导出
		$("#exported_btn").click(function() {
			var rows = $('#exported_datagrid').datagrid('getSelections');
			if (rows == null || rows.length == 0) {
				top.$.messager.alert("警告","未选择导出项","warning");
				return;
			}
			var ids = rows.joinProperty("id");
			top.$.messager.confirm("确认", "确定导出？", function (r) {  
		        if (r) {   
					window.location.href= "<%=request.getContextPath()%>/invoiceExport.do?ids="+encodeURIComponent(ids)+"&ids=ids";
		        }  
		    });   
		});
			// 查询
			$("#query_exported_btn").click(function(){
				var queryParams = {};
				queryParams.invoiceTitle = $("#export_title").val().trim();
				queryParams.status = INVOICE_IS_EXPORTED;
				$('#exported_datagrid').datagrid({
					"queryParams": queryParams
				});
			});
			
					
			 $('#export_title').bind('keypress',function(event){
		        if(event.keyCode == "13")    
		        {
		        	$("#query_exported_btn").click();
		        }
		    });
		
		// 关闭
		self.close = function() {
			$("#exported_dlg").dialog("close");
		};
		
		// 初始化
		$(document).ready(function(){
			var queryParams = {};
			queryParams.status = INVOICE_IS_EXPORTED;
			$('#exported_datagrid').datagrid({
				"queryParams": queryParams
			});
			
			// 关闭
			$("#exported_dlg_close_btn").click(function() {
				self.close();
			});
		});
		
	};
	</script>
</html>