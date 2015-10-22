<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.vo.MarkVO"%>
<%@page import="java.util.List"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
	List<MarkVO> markList = (List<MarkVO>)request.getAttribute("markList");
%>
<!DOCTYPE html>
<!-- vpc_manage.jsp -->
<html>
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		
		<title>运营商 - 用户专属云管理</title>
		
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
		<style type="text/css">
		#vpc_datagrid {
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
		.messager-input {
		  width: 100px;
		  padding: 2px 2px 3px 2px;
		  height:16px;
		  border: 1px solid #95B8E7;
		  margin:2px;
		}		
		</style>
	</head>
	<body style="visibility:hidden;">
		<form id="big_form"  method="post">
	
			<table id="vpc_datagrid" class="easyui-datagrid" title="用户VPC管理"
				data-options="
					url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=vpcService&method=queryVpcHost',
					queryParams: {},
					toolbar: '#toolbar',
					rownumbers: true,
					striped: true,
					remoteSort:false,
					fitColumns: false,
					pagination: true,
					pageList: [10, 20, 50, 100, 200],
					pageSize: 20,
					view: createView(),
					onLoadSuccess: onLoadSuccess
				">
				<thead>
					<tr>
						<th data-options="checkbox:true"></th>
						<th field="userName" width="150px" formatter="userFormatter" sortable=true>所属用户</th>
						<th field="displayName" width="150px" sortable=true>VPC名</th>
						<th field="region" formatter="formatRegion" width="100px" sortable=true>地域</th>
						<th field="status" formatter="formatStatus" width="100px" sortable=true>状态</th>
						<th field="hostAmount" width="100px" sortable=true>绑定主机数</th>
						<th field="ipAmount" width="100px" sortable=true>绑定IP数</th>
						<th field="operate" formatter="operateColumnFormatter" width="250px">更多信息</th>
					</tr>
				</thead>
			</table>
	
			<div id="toolbar" style="padding: 3px;">
				<div style="display: table; width: 100%;">
					<div style="margin-bottom:5px;">
						<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" id="del_vpc_btn">删除</a>
						<a href="#" class="easyui-linkbutton" iconCls="icon-print" plain="true" id="export_data_btn">导出数据</a>
					</div>
					<div style="margin-left:5px;">
						<span style="position: relative; top: 2px;">标签</span>
						<select id="query_by_mark" style="position: relative; top: 2px;">
							<option value="all">全部</option>
							<%
							for( MarkVO mark : markList ) 
							{ 
				  			%> 
				            <option value="<%=mark.getId()%>"><%=mark.getName()%></option>
				            <%
								} 
						    %>
						</select>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<span style="position: relative; top: 2px;">状态</span>
						<select id="query_by_status" style="position: relative; top: 2px;">
						   <option value="0">全部</option>
						   <option value="1">启用</option>
						   <option value="2">暂停</option>
						</select>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<span style="position: relative; top: 2px;">地域</span>
						<select id="region" style="position: relative; top: 2px;">
						   <option value="">全部</option>
						   <option value="1">广州</option>
						   <option value="2">成都</option>
						   <option value="4">香港</option>
						</select>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<span style="position: relative; top: 2px;">VPC名</span> 
						<input type="text" id="vpc_name" class="messager-input" /> 
						<a href="#" class="easyui-linkbutton" iconCls="icon-search" id="query_vpc_btn">查询</a>
						<a href="#" class="easyui-linkbutton" iconCls="icon-redo" id="clear_agent_btn">清除</a>
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
	$("#vpc_datagrid").height( $(document.body).height());
	
	function formatStatus(val)
	{  
		if(val == 1) {  
		    return "启用";  
		} else if(val == 2) {
		    return "暂停";  
		} else {
		 	return "关闭";
		}
	}  
	
	function formatRegion(val)
	{  
		if(val == 1) {  
		    return "广州";  
		} else if(val == 2) {
		    return "成都";  
		} else if(val == 3){
		 	return "北京";
		}else{
			return "香港";
		}
	}
	
	function operateColumnFormatter(value, row, index)
	{
		return "<div row_index='"+index+"'>\
					<a href='javascript:void(0);' class='datagrid_row_linkbutton view-detail'>查看详情</a>\
					<a href='javascript:void(0);' class='datagrid_row_linkbutton host_list'>主机列表</a>\
					<a href='javascript:void(0);' class='datagrid_row_linkbutton ip_list'>IP列表</a>\
					<a href='javascript:void(0);' class='datagrid_row_linkbutton port_list'>端口绑定列表</a>\
				</div>";
	}
	function userFormatter(value, row, index)
	{
		return "<div row_index='"+index+"'>\
					<a href='javascript:void(0);' class='datagrid_row_linkbutton user-detail'>"+value+"</a>\
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
		// 每一行的'详情'按钮
		$("a.view-detail").click(function(){
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#vpc_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].id;
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=vpcService&method=vpcViewDetailPage",
				params: {
					"vpcId" : id
				},
				onClose : function(data) {
					self.query();
				}
			});
		});
		// 每一行的'用户详情'按钮
		$("a.user-detail").click(function(){
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#vpc_datagrid").datagrid("getData");
			var userId = data.rows[rowIndex].userId;
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=terminalUserService&method=userDetailPage",
				params: {
					"userId" : userId
				},
				onClose : function(data) {
					self.query();
				}
			});
		});
		// 每一行的'主机列表'按钮
		$("a.host_list").click(function(){
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#vpc_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].id;
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=vpcService&method=hostListForOperator",
				params: {
					"vpcId" : id
				},
				onClose : function(data) {
					self.query();
				}
			});
		});
		// 每一行的'IP列表'按钮
		$("a.ip_list").click(function(){
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#vpc_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].id;
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=vpcService&method=vpcIpManagePage",
				params: {
					"vpcId" : id
				},
				onClose : function(data) {
					self.query();
				}
			});
		});
		// 每一行的'端口绑定列表'按钮
		$("a.port_list").click(function(){
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#vpc_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].id;
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=vpcService&method=vpcNetworkManagePage",
				params: {
					"vpcId" : id
				},
				onClose : function(data) {
					self.query();
				}
			});
		});
	}
	
	$(function(){
		// 查询
		$("#query_vpc_btn").click(function(){
			var queryParams = {};
			queryParams.vpc_name = $("#vpc_name").val().trim();
			queryParams.query_mark = $("#query_by_mark").val().trim(); //.combobox("getValue");
			queryParams.query_status = $("#query_by_status").val().trim(); //.combobox("getValue");
			queryParams.region = $("#region").val().trim(); //.combobox("getValue");
			if($("#vpc_name").val().trim()!=""){
				if($("#vpc_name").val().trim().length>50){ 
				    top.$.messager.alert("警告","VPC名最多允许50个字符","warning");
					return;
				}
			}	
			$('#vpc_datagrid').datagrid({
				"queryParams": queryParams
			});
		});
		//清除
		$("#clear_agent_btn").click(function(){
			$("#vpc_name").val("");
			$("#query_by_mark").val("all"); //.combobox("setValue","all");
			$("#query_by_status").val("0"); //.combobox("setValue","0");
			$("#region").val(""); //.combobox("setValue","");
		});
		//按标签查询
		/* $("#query_by_mark").change(function(){
			var queryParams = {};
			queryParams.vpc_name = $("#vpc_name").val().trim();
			queryParams.query_mark = $("#query_by_mark").combobox("getValue");
			queryParams.query_status = $("#query_by_status").combobox("getValue");
			queryParams.region = $("#region").combobox("getValue");
			$('#vpc_datagrid').datagrid({
				"queryParams": queryParams
			});
		}); */
		/* $("#query_by_status").change(function(){
			var queryParams = {};
			queryParams.vpc_name = $("#vpc_name").val().trim();
			queryParams.query_mark = $("#query_by_mark").val();
			queryParams.query_status = $("#query_by_status").val();
			queryParams.region = $("#region").val();
			$('#vpc_datagrid').datagrid({
				"queryParams": queryParams
			});
		});
		$("#region").change(function(){
			var queryParams = {};
			queryParams.vpc_name = $("#vpc_name").val().trim();
			queryParams.query_mark = $("#query_by_mark").val();
			queryParams.query_status = $("#query_by_status").val();
			queryParams.region = $("#region").val();
			$('#vpc_datagrid').datagrid({
				"queryParams": queryParams
			});
		}); */
		// 删除VPC
		$("#del_vpc_btn").click(function() {
			var rows = $('#vpc_datagrid').datagrid('getSelections');
			if (rows == null || rows.length == 0)
			{
				top.$.messager.alert("警告","未选择删除项","warning");
				return;
			}
			var ids = rows.joinProperty("id");
			console.info(ids);
			top.$.messager.confirm("确认", "确定删除?", function (r) {  
		        if (r) {   
					ajax.remoteCall("bean://vpcService:deleteVpcByIds",
						[ ids ], 
						function(reply) {
							if (reply.status == "exception") {
								if(reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>"){
									top.$.messager.alert("警告","会话超时，请重新登录","warning",function(){
										top.location.reload();
									});
								}else{
									top.$.messager.alert("警告",reply.exceptionMessage,"warning",function(){
										
									});
								}
							} else {
								$('#vpc_datagrid').datagrid('reload');
							}
						}
					);
		        }  
		    }); 
		});
		//导出数据
		$("#export_data_btn").click(function() {
			var queryParams = "";
			queryParams +="vpc_name="+$("#vpc_name").val().trim();
			queryParams +="&query_mark="+$("#query_by_mark").val().trim(); //.combobox("getValue");
			queryParams +="&query_status="+$("#query_by_status").val().trim(); //.combobox("getValue");
			queryParams +="&region="+$("#region").val().trim(); //.combobox("getValue");
			if($("#vpc_name").val().trim()!=""){
				if($("#vpc_name").val().trim().length>50){ 
				    top.$.messager.alert("警告","VPC名最多允许50个字符","warning");
					return;
				}
			}	
			top.$.messager.confirm("确认", "确定导出？", function (r) {  
		        if (r) {   
					window.location.href= "<%=request.getContextPath()%>/cloudVPCExportData.do?"+queryParams;
		        }  
		    });   
		});
		$('#vpc_name').bind('keypress',function(event){
	        if(event.keyCode == "13")    
	        {
	        	$("#query_vpc_btn").click();
	        }
	    });
	});
	</script>
</html>