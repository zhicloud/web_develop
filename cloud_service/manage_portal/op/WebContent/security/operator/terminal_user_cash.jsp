<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	String userid = (String)request.getAttribute("userid");
%>
<!DOCTYPE html>
<!-- terminal_user_cash.jsp -->
<html>
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		
		<title>运营商管理员 - 发放现金券</title>
		
	</head>
	<body>
<div id="exported_dlg_container">
	<div id="exported_dlg" class="easyui-dialog" title="现金券信息"
		style="width:800px; height:600px; padding:10px;" 
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
		<input type="hidden" name="userid" id="userid" value="<%=userid%>" />
			<table id="exported_datagrid" class="easyui-datagrid"
				style=""
				data-options="
					url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=cashCouponService&method=queryCashCouponForOperator&cash_status=1',
					queryParams: {},
					toolbar: '#toolbar',
					rownumbers: true,
					striped: true,
					remoteSort:false,
					fitColumns: true,
					pagination: true,
					pageList: [10, 20, 50, 100, 200],
					pageSize: 10,
					view: createView(),
					onLoadSuccess: onLoadSuccess
				">
				<thead>
					<tr>
						<th field="radioid" width="8px" formatter="radioformat" align="center"></th>
						<th field="cashCode" width="28px" sortable=true>现金券券码</th>
						<th field="userName" width="30px">所属运营商</th>
						<th field="money" formatter="formatMoney" width="8px">价值(元)</th>
						<th field="createTime" formatter="timeFormat" width="18px">创建时间</th>
						<th field="email" width="15px">电子邮箱</th>
						<th field="phone" width="12px">手机号码</th>
						<th field="status" formatter="formatStatus" width="8px">状态</th>
						<th field="sendAddress"   width="10px">发送地址</th>
					</tr>
				</thead>
			</table>
		</form>
		</div>
	<div id="exported_dlg_buttons">
			<a href="javascript:" class="easyui-linkbutton" id="phone_dlg_close_btn">发送至手机</a>
			<a href="javascript:" class="easyui-linkbutton" id="email_dlg_close_btn">发送至邮箱</a>
		<a href="javascript:" class="easyui-linkbutton" id="exported_dlg_close_btn"> &nbsp;关&nbsp;闭&nbsp; </a>
	</div>
</div>
	</body>
	
	<script type="text/javascript">
	function formatStatus(val,row)
	{  
		if(val == 1) {  
		    return "未发送";  
		} else if(val == 2) {
		    return "已发送";  
		} else if(val == 3) {
		 	return "已过期";
		} else if(val == 4) {
			return "已使用";
		} 
	}  
	function radioformat(value, rowData, rowIndex){
		 return '<input type="radio" name="selectRadio" id="selectRadio"' + rowIndex + '    value="' + rowData.oid + '" />';
	}
	function formatMoney(val,row)
	{
		return val.toFixed(2);
	}
	function timeFormat(val, row)
	{
		return $.formatDateString(val, "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss");
	}	
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
	function onLoadSuccess()
	{
	}
	var _exported_dlg_scope_ = new function(){
		
		// 关闭
		self.close = function() {
			$("#exported_dlg").dialog("close");
		};
		
		// 初始化
		$(document).ready(function(){
			$("#exported_datagrid").datagrid({ onClickRow:
	            function (rowIndex, rowData) {
				
	            //加载完毕后获取所有的checkbox遍历
	            var radio = $("input[type='radio']")[rowIndex].disabled;
	             //如果当前的单选框不可选，则不让其选中
	            if (radio==false) {
	                 //让点击的行单选按钮选中
	                $("input[type='radio']")[rowIndex].checked = true;
	             }
	             else {
	                 $("input[type='radio']")[rowIndex].checked = false;
	             }
	            }
	        });
			// 关闭
			$("#exported_dlg_close_btn").click(function() {
				$("#exported_dlg").dialog("close");
			});
			// 发送至手机
			$("#phone_dlg_close_btn").click(function() {
				var rows = $('#exported_datagrid').datagrid('getSelected');
				if (rows == null || rows.length == 0) {
					top.$.messager.alert("警告","未选择现金券","warning");
					return;
				}
/* 				if(rows.length!=1){
					top.$.messager.alert("警告","请选择一条现金券数据","warning");
					return;
				} */
				if(rows.status!=1){
					top.$.messager.alert("警告","该现金券不能使用,请重新选择","warning");
					return;	
				}
				var formData = new Object();
				formData.userid = $("#userid").val();
				formData.cashCouponCode = rows.cashCode;
				formData.cashCouponMoney = rows.money;
				formData.cashCouponId = rows.id;
				ajax.remoteCall("bean://terminalUserService:sendCashCouponByPhone",
						[ formData ], 
						function(reply) {
							result = new String(reply.result.message);
							if (reply.status == "exception") {
								top.$.messager.alert('警告',reply.exceptionMessage,'warning');
							} else if (reply.result.status == "success") {
								var data = $("#terminal_user_mod_dlg_container").parent().prop("_data_");
								top.$.messager.alert('提示',result,'info');
								$("#exported_dlg").dialog("close");
							} else {
								top.$.messager.alert('警告',reply.result,'warning');
							}
						}
					);
			});
			// 发送至邮箱
			$("#email_dlg_close_btn").click(function() {
				var rows = $('#exported_datagrid').datagrid('getSelected');
				if (rows == null || rows.length == 0) {
					top.$.messager.alert("警告","未选择现金券","warning");
					return;
				}
/* 				if(rows.length!=1){
					top.$.messager.alert("警告","请选择一条现金券数据","warning");
					return;
				} */
				if(rows.status!=1){
					top.$.messager.alert("警告","该现金券不能使用,请重新选择","warning");
					return;	
				}
				var formData = new Object();
				formData.userid = $("#userid").val();
				formData.cashCouponCode = rows.cashCode;
				formData.cashCouponMoney = rows.money;
				formData.cashCouponId = rows.id;
				ajax.remoteCall("bean://terminalUserService:sendCashCouponByEmail",
						[ formData ], 
						function(reply) {
							result = new String(reply.result.message);
							if (reply.status == "exception") {
								top.$.messager.alert('警告',reply.exceptionMessage,'warning');
							} else if (reply.result.status == "success") {
								var data = $("#terminal_user_mod_dlg_container").parent().prop("_data_");
								top.$.messager.alert('提示',result,'info');
								$("#exported_dlg").dialog("close");
							} else {
								top.$.messager.alert('警告',reply.result,'warning');
							}
						}
					);
			});
		});
		
		
	};
	</script>
</html>