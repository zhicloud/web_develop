<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request);
	String userId  = loginInfo.getUserId();
	Integer userType = Integer.valueOf(request.getParameter("userType"));
%>

<!--invoice_address_manage.jsp -->

<div id="invoice_address_manage_dlg_container">
	<div id="invoice_address_manage_dlg" class="easyui-dialog" title="邮寄地址管理"
		style="width:700px; height:430px; padding:10px;"
		data-options="
			iconCls: 'icon-print', 
 			buttons: '#invoice_address_manage_dlg_buttons', 
 			modal: true, 
 			onClose: function(){ 
 				$(this).dialog('destroy'); 
 			}, 
 			onDestroy: function(){ 
 				delete _invoice_address_manage_dlg_scope_; 
 			}
		">
		<div id="toolbar" style="padding: 3px;">
				<div style="display: table; width: 100%;">
					<div style="display: table-cell; text-align: left">
						<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="add_invoice_address_btn">添加</a> 
						<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" id="del_invoice_address_btn">删除</a>
					</div>
				</div>
			</div>
		<form id="invoice_address_manage_dlg_form" method="post">
			<input id = "userId" name = "userId"  value = "<%=userId %>"type="hidden">
		<table id="invoice_address_datagrid" class="easyui-datagrid" title=""
				style="height: 300px; margin: 10px;"
				data-options="
						url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=invoiceAddressService&method=queryAddress',
						queryParams: {},
						rownumbers: true,
						striped: true,
						remoteSort:false,
						fitColumns: true,
 						pagination: true, 
						pageList: [10, 20, 50, 100, 200],
						pageSize: 10,
						onLoadSuccess: onLoadSuccess
						">
				<thead>
					<tr>
						<th data-options="checkbox:true"></th>
						<th field="invoiceTitle" width="70px" >发票抬头	</th>
						<th field="address"  width="70px">邮寄地址</th>
						<th field="recipients" width="70px" >收件人</th>
						<th field="phone" width="70px" >联系方式</th>
						<th field="edit" formatter="editColumnFormatter" width="50px">操作</th>
					</tr>
				</thead>
			</table>
			</form>
		</div>
	<div id="invoice_address_manage_dlg_buttons">
		<a href="javascript:" class="easyui-linkbutton" id="invoice_address_manage_dlg_close_btn"> &nbsp;关&nbsp;闭&nbsp; </a> 
	</div>
</div>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.ext.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.util.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">

function editColumnFormatter(value, row, index)
{
	return "<div row_index='"+index+"'>\
				<a href='#' class='datagrid_row_linkbutton modify_btn'>编辑</a>\
			</div>";
}

function onLoadSuccess(){
	$("body").css({
		"visibility":"visible"
	});
	// 每一行的'编辑'按钮
	$("a.modify_btn").click(function(){
		$this = $(this);
		rowIndex = $this.parent().attr("row_index");
		var data = $("#invoice_address_datagrid").datagrid("getData");
		var id = data.rows[rowIndex].id;
		top.showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=invoiceAddressService&method=modPage&id="+encodeURIComponent(id),
			onClose: function(data){
				$('#invoice_address_datagrid').datagrid('reload');
			}
		});
	});
	
}

var _invoice_address_manage_dlg_scope_ = new function(){
	
	var self = this;
	
	// 关闭
	self.close = function() {
		$("#invoice_address_manage_dlg").dialog("close");
	};

	//--------------------------
	
	$(function(){
		
		// 删除地址
		$("#del_invoice_address_btn").click(function() {
			var rows = $('#invoice_address_datagrid').datagrid('getSelections');
			if (rows == null || rows.length == 0)
			{
				top.$.messager.alert("警告","未选择删除项","warning");
				return;
			}
			var ids = rows.joinProperty("id");
			top.$.messager.confirm("确认", "确定删除?", function (r) {  
		        if (r) {    
						ajax.remoteCall("bean://invoiceAddressService:deleteAddressByIds",
							[ ids ], 
							function(reply) {
								if (reply.status == "exception") {
									if(reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>"){
										top.$.messager.alert("警告","会话超时，请重新登录","warning");
									}else{
										top.$.messager.alert("警告",reply.exceptionMessage,"warning",function(){
											top.location.reload();
										});
									}
								} else {
									$('#invoice_address_datagrid').datagrid('reload');
								}
							}
						);
		        }  
		    });   
		});
	});
	
	$(document).ready(function() {
		
		// 关闭
		$("#invoice_address_manage_dlg_close_btn").click(function() {
			self.close();
		});
	});
	
		// 保存
		$("#add_invoice_address_btn").click(function() {
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=invoiceAddressService&method=addAddressPage",
				onClose: function(data){
					$('#invoice_address_datagrid').datagrid('reload');
				}
			});
		});
	
};
	
	
</script>



