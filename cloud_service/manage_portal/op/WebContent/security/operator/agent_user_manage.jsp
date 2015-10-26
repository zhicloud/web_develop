<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	String agentid = (String)request.getAttribute("agentid");
%>
<!DOCTYPE html>
<!-- exported_export_manage.jsp -->
<html>
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		
		<title>运营商管理员 - 用户管理</title>
		
	</head>
	<body>
<div id="exported_dlg_container">
	<div id="exported_dlg" class="easyui-dialog" title="用户信息"
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
			<table id="exported_datagrid" class="easyui-datagrid"
				style=""
				data-options="
					url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=terminalUserService&method=queryUserByAgentID&agentid=<%=agentid %>',
					queryParams: {},
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
						<th data-options="checkbox:true"></th>
						<th field="name" width="50px">昵称</th>
						<th field="account" width="80px" sortable=true>邮箱</th>
						<th field="phone" width="50px">手机</th>
						<th field="markName"   width="40px" sortable=true>标签</th>
						<th field="percentOff"   width="30px" sortable=true>折扣率(%)</th>
						<th field="status" formatter="formatStatus" width="30px">状态</th>
						<th field="createTime" formatter="timeFormat" width="70px">创建时间</th>
						<th field="operate" formatter="terminalUserColumnFormatter" width="120px">操作</th>
					</tr>
				</thead>
			</table>
		</form>
		</div>

	<div id="exported_dlg_buttons">
		<a href="javascript:" class="easyui-linkbutton"
			id="exported_dlg_close_btn"> &nbsp;关&nbsp;闭&nbsp; </a>
	</div>
</div>
	</body>
	
	<script type="text/javascript">
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
	function timeFormat(val, row)
	{
		return $.formatDateString(val, "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss");
	}
	function terminalUserColumnFormatter(value, row, index)
	{
		return "<div row_index='"+index+"'>\
					<a href='#' class='datagrid_row_linkbutton recharge_btn'>充值记录</a>\
					<a href='#' class='datagrid_row_linkbutton consume_btn'>消费记录</a>\
				</div>";
	}
	function onLoadSuccess()
	{
		// 每一行的'充值记录'按钮
		$("a.recharge_btn").click(function(){
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#exported_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].id;
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=accountBalanceService&method=getRechargeRecordByUserId&terminalUserId="+encodeURIComponent(id),
				onClose: function(data){
					$('#terminal_user_datagrid').datagrid('reload');
				}
			});
		});
		// 每一行的'消费记录'按钮
		$("a.consume_btn").click(function(){
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#exported_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].id;
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=accountBalanceService&method=toConsumptionRecordPage&terminalUserId="+encodeURIComponent(id),
				onClose: function(data){
					$('#terminal_user_datagrid').datagrid('reload');
				}
			});
		});	
	}
	var _exported_dlg_scope_ = new function(){
		
		// 关闭
		self.close = function() {
			$("#exported_dlg").dialog("close");
		};
		
		// 初始化
		$(document).ready(function(){
			// 关闭
			$("#exported_dlg_close_btn").click(function() {
				$("#exported_dlg").dialog("close");
			});
		});
		
	};
	</script>
</html>