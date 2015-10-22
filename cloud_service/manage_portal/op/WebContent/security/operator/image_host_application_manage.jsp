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
		
		<title>运营商管理员 - 镜像主机申请管理</title>
		
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
		<style type="text/css">
		#image_host_application_datagrid {
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
	
			<table id="image_host_application_datagrid" class="easyui-datagrid" title="镜像主机申请管理"
				data-options="
					url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=imageHostApplicationService&method=queryImageHostApplication',
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
						<th field="name" width="50px" sortable=true>公司全称</th>
						<th field="contacts"   >联系人</th>
						<th field="contactsPosition" width="50px">联系人职位</th>
						<th field="contactsPhone">联系人电话</th>
						<th field="qqOrWeixin" width="50px">微信或者QQ</th>
 						<th field="status" formatter="handleStatus" >状态</th>
						<th field="operate" formatter="sysDiskImageColumnFormatter">操作</th>
					</tr>
				</thead>
			</table>
	
			<div id="toolbar" style="padding: 3px;">
				<div style="display: table; width: 100%;">
					<div style="display: table-cell; text-align: left">
 						<a href="#" class="easyui-linkbutton" iconCls="icon-ok" plain="true" id="handle">标记为已处理</a>
 					</div>
					<div style="display: table-cell; text-align: right;">
						<span style="position: relative; top: 2px;">状态</span>
						<select id="status" style="position: relative; top: 2px;">
						   <option value="">全部</option>
						   <option value="1">待处理</option>
						   <option value="2">已处理</option> 
						</select>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<span style="position: relative; top: 2px;">公司全称</span> 
						<input type="text" id="name" style="position: relative; top: 2px; height: 18px;" /> 
						<a href="#" class="easyui-linkbutton" iconCls="icon-search" id="query_image_host_btn">查询</a>
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
	$("#image_host_application_datagrid").height( $(document.body).height());
	
	
	function sysDiskImageColumnFormatter(value, row, index)
	{
		return "<div row_index='"+index+"'>\
					<a href='#' class='datagrid_row_linkbutton detail_btn'>查看详情</a>\
				</div>";
	}  
	function handleStatus(value, row, index)
	{ 
		if(value=='1'){
			return "未处理";
		}else if(value=='2'){
			return "已处理";
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
		// 每一行的'查看详情'按钮
		$("a.detail_btn").click(function(){
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#image_host_application_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].id;
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=imageHostApplicationService&method=imageHostApplicationManagePageForDetail&id="+encodeURIComponent(id),
				onClose: function(data){
					$('#image_host_application_datagrid').datagrid('reload');
				}
			});
		});
	}
	
	$(function(){ 
		
		// 查询
		$("#query_image_host_btn").click(function(){
			var queryParams = {};
			queryParams.name = $("#name").val().trim();
			queryParams.status = $("#status").val().trim();
			$('#image_host_application_datagrid').datagrid({
				"queryParams": queryParams
			});
		}); 
		// 标记为已处理
		$("#handle").click(function() {
			var rows = $('#image_host_application_datagrid').datagrid('getSelections');
			if (rows == null || rows.length == 0) {
				top.$.messager.alert("警告","未选择处理项","warning");
				return;
			}
			var ids = rows.joinProperty("id");
			$.messager.confirm("确认", "确定标记成已处理?", function (r) {  
		        if (r) {     
					ajax.remoteCall("bean://imageHostApplicationService:updateImageHostApplicationByIds",
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
								$('#image_host_application_datagrid').datagrid('reload');
							}
						}
					);
		        }  
		    });   
		});
		 
	});
	</script>
</html>