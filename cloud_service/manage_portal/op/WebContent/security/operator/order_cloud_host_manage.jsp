<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType    = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
	out.clear(); //清空缓存的内容。
	out = pageContext.pushBody(); //参考API
%>
<!DOCTYPE html>
<!-- order_cloud_host_manage.jsp -->
<html>
<head>
	<meta charset="UTF-8" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
	<title>运营商管理员 - 订购云主机管理</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
	<style type="text/css">
	#sys_disk_image_datagrid {
		border: 0px solid red;
		margin-left: -10px;
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
	
			<table id="order_cloud_host_datagrid" class="easyui-datagrid" title="订购云主机管理"
				style="height: 500px; margin: 10px;"
				data-options="
					url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=paymentService&method=queryOrderConfig',
					queryParams: {},
					toolbar: '#toolbar',
					rownumbers: true,
					striped: true,
					remoteSort:false,
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
						<th field="createTime" width="50px"sortable=true>订购时间</th>
						<th field="account" width="50px" sortable=true>所属用户</th>
						<th field="type" formatter="typeColumnFormatter" width="50px" sortable=true sortable=true>付费方式</th>
						<th field="processStatus" formatter="isHostCreatedColumnFormatter" width="50px" sortable=true>状态</th>
						<th field="operate" formatter="operateColumnFormatter" width="50px">操作</th>
					</tr>
				</thead>
			</table>
	
			<div id="toolbar" style="padding: 3px;">
				<div style="display: table; width: 100%;"> 
					<div style="display: table-cell; text-align: right;">
						<span style="position: relative; top: 2px;">所属用户</span> 
						<input name="account" id="account"/>
						<span style="position: relative; top: 2px;">状态</span> 
						<select class="shortselect" name="processStatus" id="processStatus">
								<option value="">全部</option>
								<option value="1">已创建</option>
								<option value="0">未处理</option>
								<option value="2">失败</option>
								<option value="3">正在创建</option>
							</select>
						<a href="#" class="easyui-linkbutton" iconCls="icon-search" id="query_order_cloud_host_btn">查询</a>
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
	<script type="text/javascript">
	window.name = "selfWin";
	
	var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
	ajax.async = false;
	
	function isHostCreatedColumnFormatter(val,row){
		if(val == 0){
			return "未处理";
		}else if(val == 1){
			return "已创建";
		}else if(val == 2){
			return "失败";
		}else if(val == 3){
			return "正在创建";
		} 
	}
	function typeColumnFormatter(val,row){
		if(val == 1){
			return "包月付费";
		}else if(val == 2){
			return "按量付费";
		}else if(val == 3){
			return "试用";
		}
	}
	function operateColumnFormatter(value, row, index)
	{
		return "<div row_index='"+index+"'>\
					<a href='#' class='datagrid_row_linkbutton modify_btn'>编辑</a>\
					<a href='#' class='datagrid_row_linkbutton detail_btn'>查看详情</a>\
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
		// 每一行的'编辑'按钮
		$("a.modify_btn").click(function(){
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#order_cloud_host_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].id;
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=paymentService&method=toManageModifyPage&id="+encodeURIComponent(id),
				onClose: function(data){
					$('#order_cloud_host_datagrid').datagrid('reload');
				}
			});
		});
		// 每一行的'查看详情'按钮
		$("a.detail_btn").click(function(){
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#order_cloud_host_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].id;
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=paymentService&method=toManageDetailPage&id="+encodeURIComponent(id),
				onClose: function(data){
					$('#order_cloud_host_datagrid').datagrid('reload');
				}
			});
		});
	}
	
	$(function(){ 
		
		// 查询
		$("#query_order_cloud_host_btn").click(function(){
			var queryParams = {};
			queryParams.processStatus = $("#processStatus").val().trim();
			queryParams.account = $("#account").val();
			$('#order_cloud_host_datagrid').datagrid({
				"queryParams": queryParams
			});
		}); 
		
		$('#account').bind('keypress',function(event){
	        if(event.keyCode == "13")    
	        {
	        	$("#query_order_cloud_host_btn").click();
	        }
	    });
	});
	</script>
</html>