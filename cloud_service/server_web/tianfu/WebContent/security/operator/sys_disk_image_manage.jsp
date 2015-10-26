<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType    = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
%>
<!DOCTYPE html>
<!-- sys_disk_image_manage.jsp -->
<html>
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		
		<title>运营商管理员 - 系统磁盘管理</title>
		
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
		<style type="text/css">
		#sys_disk_image_datagrid {
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
		<form id="big_form"  method="post">
	
			<table id="sys_disk_image_datagrid" class="easyui-datagrid" title="系统镜像管理"
				data-options="
					url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=sysDiskImageService&method=querySysDiskImage',
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
						<th field="name" width="50px" sortable=true>镜像名称</th>
						<th field="type" formatter="regionType" >类型</th>
						<th field="tag" width="50px">标签</th>
						<th field="group_id">用户所在组</th>
						<th field="description" width="50px">描述</th>
						<th field="region" formatter="regionFormatter">地域</th>
						<th field="status" formatter="regionStatus" >状态</th>
						<th field="operate" formatter="sysDiskImageColumnFormatter">操作</th>
					</tr>
				</thead>
			</table>
	
			<div id="toolbar" style="padding: 3px;">
				<div style="display: table; width: 100%;">
					<div style="display: table-cell; text-align: left">
						<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="add_sysDiskImage_btn">制作系统镜像</a> 
						<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" id="del_sysDiskImage_btn">删除</a>
						<a href="#" class="easyui-linkbutton" iconCls="icon-ok" plain="true" id="mo_sysDiskImage_btn">验证发布</a>
					</div>
					<div style="display: table-cell; text-align: right;">
						<span style="position: relative; top: 2px;">镜像名称</span> 
						<input type="text" id="sysDiskImage_name" style="position: relative; top: 2px; height: 18px;" /> 
						<a href="#" class="easyui-linkbutton" iconCls="icon-search" id="query_sysDiskImage_btn">查询</a>
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
	$("#sys_disk_image_datagrid").height( $(document.body).height());
	
	
	function sysDiskImageColumnFormatter(value, row, index)
	{
		return "<div row_index='"+index+"'>\
					<a href='#' class='datagrid_row_linkbutton modify_btn'>修改</a>\
				</div>";
	}
	function regionFormatter(value, row, index)
	{ 
		if(value=='1'){
			return "广州";
		}else if(value=='2'){
			return "成都";
		}else if(value=='3'){
			return "";
		}else if(value=='4'){
			return "香港";
		}else{
			return "";
		}
	}
	function regionType(value, row, index)
	{ 
		if(value==1){
			return "通用镜像";
		}else if(value=='2'){
			return "代理商定制镜像";
		}else{
			return "";
		}
	}
	function regionStatus(value, row, index)
	{ 
		if(value=='1'){
			return "未验证";
		}else if(value=='2'){
			return "已验证";
		}else{
			return "";
		}
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
			var data = $("#sys_disk_image_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].id;
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=sysDiskImageService&method=modPage&sysDiskImagetId="+encodeURIComponent(id),
				onClose: function(data){
					$('#sys_disk_image_datagrid').datagrid('reload');
				}
			});
		});
	}
	
	$(function(){
		// 添加镜像
		$("#add_sysDiskImage_btn").click(function(){
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=sysDiskImageService&method=addPage",
				onClose: function(data){
					$('#sys_disk_image_datagrid').datagrid('reload');
				}
			});
		});
		
		// 查询
		$("#query_sysDiskImage_btn").click(function(){
			var queryParams = {};
			queryParams.sysDiskImage_name = $("#sysDiskImage_name").val().trim();
			$('#sys_disk_image_datagrid').datagrid({
				"queryParams": queryParams
			});
		});
		
		// 删除镜像
		$("#del_sysDiskImage_btn").click(function() {
			var rows = $('#sys_disk_image_datagrid').datagrid('getSelections');
			if (rows == null || rows.length == 0) {
				top.$.messager.alert("警告","未选择删除项","warning");
				return;
			}
			var ids = rows.joinProperty("id");
			$.messager.confirm("确认", "确定删除?", function (r) {  
		        if (r) {     
					ajax.remoteCall("bean://sysDiskImageService:deleteSysDiskImageByIds",
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
								$('#sys_disk_image_datagrid').datagrid('reload');
							}
						}
					);
		        }  
		    });   
		});
		// 删除镜像
		$("#mo_sysDiskImage_btn").click(function() {
			var rows = $('#sys_disk_image_datagrid').datagrid('getSelections');
			if (rows == null || rows.length == 0) {
				top.$.messager.alert("警告","未选择发布镜像","warning");
				return;
			}
			var ids = rows.joinProperty("id");
			$.messager.confirm("确认", "确定发布?", function (r) {  
		        if (r) {     
					ajax.remoteCall("bean://sysDiskImageService:publishSysDiskImageByIds",
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
								$('#sys_disk_image_datagrid').datagrid('reload');
							}
						}
					);
		        }  
		    });   
		});
		
		$('#sysDiskImage_name').bind('keypress',function(event){
	        if(event.keyCode == "13")    
	        {
	        	$("#query_sysDiskImage_btn").click();
	        }
	    });
	});
	</script>
</html>