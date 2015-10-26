<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="com.zhicloud.op.app.pool.addressPool.AddressExt"%>
<%@page import="java.util.List"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
%>
<!DOCTYPE html>
<!-- agent_manage.jsp -->
<html>
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		
		<title>运营商管理员 - 地址资源池管理</title>
		
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
	        <input name="agent_id" id="agent_id" type="hidden" value=""/>
			<table id="agent_datagrid" class="easyui-datagrid" title="地址资源池管理"
				style=""
				data-options="
					url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=resourcePoolService&method=queryAddressPool',
					queryParams: {},
					rownumbers: true,
					striped: true,
					remoteSort:false,
					fitColumns: true,
					pagination: true,
					pageList: [10, 20, 50, 100, 200],
					pageSize: 200,
					view: createView(),
					onLoadSuccess: onLoadSuccess
				">
				<thead>
					<tr>
<!-- 						<th data-options="checkbox:true"></th> -->
						<th field="name" width="50px" sortable=true>地址名</th>
						<th field="region" formatter="formatRegion" width="30px">地域</th>
						<th field="count" width="40px" sortable=true>数量(剩余，总数)</th>
						<th field="status" formatter="formatStatus" width="30px">状态</th>
					</tr>
				</thead>
			</table>
	
			<div id="toolbar" style="padding: 3px;">
				<div style="display: table; width: 100%;">
					<div style="display: table-cell; text-align: left">
						<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="add_agent_btn">添加</a> 
						<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" id="del_agent_btn">删除</a>
					</div>
					<div style="display: table-cell; text-align: right;">
						<span style="position: relative; top: 2px;">邮箱</span> 
						<input type="text" id="account" style="position: relative; top: 2px; height: 18px;" /> 
						<a href="#" class="easyui-linkbutton" iconCls="icon-search" id="query_agent_btn">查询</a>
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
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/big.min.js"></script>
	<script type="text/javascript">
	window.name = "selfWin";
	
	var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
	ajax.async = false;
	

	// 布局初始化
	$("#agent_datagrid").height( $(document.body).height());
	function formatRegion(val)
	{
		if(val==1){
			return "广州";
		}else if(val==2){
			return "成都";
		}else if(val==3){
			return "北京";
		}else {
			return "香港";
		}
	}
	function formatCpuCore(val)
	{
		return val+"核";
	}
	function formatStatus(val)
	{
		if(val==1){
			return "可用";
		}
		return "不可用";
	}
	function formatCapacity(val)
	{
		return CapacityUtil.toCapacityLabel(val, 0);
	}

	function formatFlow(val)
	{
		return FlowUtil.toFlowLabel(val, 0);
	}  
	
	function agentColumnFormatter(value, row, index)
	{
		return "<div row_index='"+index+"'>\
					<a href='#' class='datagrid_row_linkbutton modify_btn'>修改</a>\
					<a href='#' class='datagrid_row_linkbutton reset_password_btn'>重置密码</a>\
					<a href='#' class='datagrid_row_linkbutton recharge'>充值</a>\
					<a href='#' class='datagrid_row_linkbutton recharge_btn'>充值记录</a>\
					<a href='#' class='datagrid_row_linkbutton consume_btn'>消费记录</a>\
					<a href='#' class='datagrid_row_linkbutton operation_btn'>操作日志</a>\
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
			var data = $("#agent_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].id;
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=agentService&method=modPage&agentId="+encodeURIComponent(id),
				onClose: function(data){
					$('#agent_datagrid').datagrid('reload');
				}
			});
		});
		// 每一行的'充值'按钮
		$("a.recharge").click(function(){
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#agent_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].id;
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=agentService&method=toRecharge&agentId="+encodeURIComponent(id),
				onClose: function(data){
					$('#agent_datagrid').datagrid('reload');
				}
			});
		});
		// 每一行的'重置密码'按钮
		$("a.reset_password_btn").click(function(){
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#agent_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].id;
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=agentService&method=resetPasswordPage&agentId="+encodeURIComponent(id),
				onClose: function(data){
					$('#agent_datagrid').datagrid('reload');
				}
			});
		});
		// 每一行的'充值记录'按钮
		$("a.recharge_btn").click(function(){
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#agent_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].id;
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=accountBalanceService&method=getRechargeRecordByUserId&terminalUserId="+encodeURIComponent(id),
				onClose: function(data){
					$('#agent_datagrid').datagrid('reload');
				}
			});
		});
		// 每一行的'消费记录'按钮
		$("a.consume_btn").click(function(){
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#agent_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].id;
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=accountBalanceService&method=toConsumptionRecordPage&terminalUserId="+encodeURIComponent(id),
				onClose: function(data){
					$('#agent_datagrid').datagrid('reload');
				}
			});
		});
		// 每一行的'操作日志'按钮
		$("a.operation_btn").click(function(){
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#agent_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].id;
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=operLogService&method=managePage&terminalUserId="+encodeURIComponent(id),
				onClose: function(data){
					$('#agent_datagrid').datagrid('reload');
				}
			});
		});
	}
	
	$(function(){
		// 查询
		$("#query_agent_btn").click(function(){
			var queryParams = {};
			queryParams.account = $("#account").val().trim();
			queryParams.query_mark = $("#query_by_mark").val();
			$('#agent_datagrid').datagrid({
				"queryParams": queryParams
			});
		});
		//按标签查询
		$("#query_by_mark").change(function(){
			var queryParams = {};
			queryParams.account = $("#account").val().trim();
			queryParams.query_mark = $("#query_by_mark").val();
			$('#agent_datagrid').datagrid({
				"queryParams": queryParams
			});
		});
		// 添加代理商
		$("#add_agent_btn").click(function(){
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=agentService&method=addPage",
				onClose: function(data){
					$('#agent_datagrid').datagrid('reload');
				}
			});
		});
		// 删除代理商
		$("#del_agent_btn").click(function() {
			var rows = $('#agent_datagrid').datagrid('getSelections');
			if (rows == null || rows.length == 0) {
				top.$.messager.alert("警告","未选择删除项","warning");
				return;
			}
			var ids = rows.joinProperty("id");
			top.$.messager.confirm("确认", "确定删除?", function (r) {  
		        if (r) {   
					ajax.remoteCall("bean://agentService:deleteAgentByIds",
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
								$('#agent_datagrid').datagrid(
										'reload');
							}
						}
					); 
		        }  
		    });   
		});
		 $('#account').bind('keypress',function(event){
	        if(event.keyCode == "13")    
	        {
	        	$("#query_agent_btn").click();
	        }
	    });
	});
	</script>
</html>