<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
%>
<!DOCTYPE html>
<!-- cloud_host_manage.jsp -->
<html>
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		
		<title>代理商 - 用户云主机管理</title>
		
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
		<style type="text/css">
		#cloud_host_datagrid {
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
	<div class="iframe">
		<form id="big_form" method="post">
	
			<table id="cloud_host_datagrid" class="easyui-datagrid" title="用户云主机管理"
				data-options="
					url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=cloudHostService&method=queryCloudHost',
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
						<th field="userAccount" width="50px"sortable=true>所属用户</th>
						<th field="hostName" width="50px" sortable=true>主机名</th>
						<th field="cpuCore" formatter="formatCpuCore" width="50px" sortable=true>CPU核心数量</th>
						<th field="memory" formatter="formatCapacity" width="50px" sortable=true>内存</th>
						<th field="sysDisk" formatter="formatCapacity" width="50px" sortable=true>系统磁盘</th>
						<th field="dataDisk" formatter="formatCapacity" width="50px" sortable=true>数据磁盘</th>
						<th field="bandwidth" formatter="formatFlow" width="50px" sortable=true>网络带宽</th>
						<th field="innerMonitorAddr" width="50px">内部监控地址</th>
						<th field="outerMonitorAddr" width="50px">外部监控地址</th>
						<th field="operate" formatter="operateColumnFormatter">更多信息</th>
					</tr>
				</thead>
			</table>
	
			<div id="toolbar" style="padding: 3px;">
				<div style="display: table; width: 100%;">
					<div style="display: table-cell; text-align: left">
						<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" id="del_cloud_host_btn">删除</a>
					</div>
					<div style="display: table-cell; text-align: right;">
						<span style="position: relative; top: 2px;">主机名</span> 
						<input type="text" id="host_name" style="position: relative; top: 2px; height: 18px;" /> 
						<a href="#" class="easyui-linkbutton" iconCls="icon-search" id="query_cloud_host_btn">查询</a>
					</div>
				</div>
			</div>
	
		</form>
		</div>
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
	$("#cloud_host_datagrid").height( $(document.body).height());
	
	function formatCpuCore(val)
	{
		return val+"核";
	}
	
	function formatCapacity(val)
	{
		return CapacityUtil.toCapacityLabel(val, 0);
	}

	function formatFlow(val)
	{
		return FlowUtil.toFlowLabel(val, 0);
	}
	
	function formatStartupStatus(val)
	{  
		if(val == 1) {  
		    return "是";  
		} else {
		 	return "否";
		}
	}
	
	function formatRuningStatus(val)
	{  
		if(val == 1) {  
		    return "启动";  
		} else {
		 	return "关机";
		}
	}
	
	function formatStatus(val)
	{  
		if(val == 1) {  
		    return "正常";  
		} else if(val == 2) {
		    return "停机";  
		} else {
		 	return "欠费";
		}
	}  
	
	function operateColumnFormatter(value, row, index)
	{
		return "<div row_index='"+index+"'>\
					<a href='#' class='datagrid_row_linkbutton view-detail'>查看详情</a>\
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
		// 每一行的'查看详情'按钮
		$("a.view-detail").click(function(){
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#cloud_host_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].id;
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=cloudHostService&method=cloudHostViewDetailPage",
				params: {
					"cloudHostId" : id
				},
				onClose : function(data) {
					self.query();
				}
			});
		});
	}
	
	$(function(){
		// 查询
		$("#query_cloud_host_btn").click(function(){
			var queryParams = {};
			queryParams.host_name = $("#host_name").val().trim();
			$('#cloud_host_datagrid').datagrid({
				"queryParams": queryParams
			});
		});

		// 删除云主机
		$("#del_cloud_host_btn").click(function() {
			var rows = $('#cloud_host_datagrid').datagrid('getSelections');
			if (rows == null || rows.length == 0)
			{
				top.$.messager.alert("警告","未选择删除项","warning");
				return;
			}
			var ids = rows.joinProperty("id");
			top.$.messager.confirm("确认", "确定删除?", function (r) {  
		        if (r) {   
						ajax.remoteCall("bean://cloudHostService:deleteCloudHostByIds",
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
									$('#cloud_host_datagrid').datagrid('reload');
								}
							}
						);
		        }  
		    }); 
		});
		
		$('#host_name').bind('keypress',function(event){
	        if(event.keyCode == "13")    
	        {
	        	$("#query_cloud_host_btn").click();
	        }
	    });
	});
	</script>
</html>