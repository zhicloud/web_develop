<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
	String region = request.getParameter("region");
	request.getSession().setAttribute("optionRegion", region);
%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		
		<title>运营商管理员 - 内存配置列表</title>
		
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
	</head>
	<body>
		<form id="memory_form"  method="post">
			<table id="memory_datagrid" class="easyui-datagrid" 
				data-options="
					url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=packageOptionService&method=queryPackagePrice',
					queryParams: {
						type:2,
						region:<%=region %>
					},
					loadMeg:'数据加载中，请稍等...',
					toolbar: '#toolbar_memory',
					rownumbers: true,
					striped: true,
					remoteSort:false,
					fitColumns: true,
					pagination: true,
					pageList: [10, 20, 50, 100, 200],
					pageSize: 20,
					view: createView(),
					onLoadSuccess:function(data){
						onLoadSuccess();
						checkSize();
					}
				">
				<thead>
					<tr>
						<th data-options="checkbox:true"></th>
						<th field="memory" formatter="formatCapacity" width="20%" sortable=true>内存大小</th>
						<th field="region" formatter="formatRegion" width="20%" >地域</th>
						<th field="price" width="20%">价格(元/月)</th>
						<th field="operate" formatter="memoryColumnFormatter" width="20%">操作</th>
					</tr>
				</thead>
			</table>
	
			<div id="toolbar_memory" style="padding: 3px;">
				<div style="display: table; width: 100%;">
					<div style="display: table-cell; text-align: left">
						<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="add_memory_btn">添加</a> 
					</div>
				</div>
			</div>
		</form>
		<script type="text/javascript">
		var _region = "<%=region%>";
		var memoryListSize = '0';
		// 添加内存配置
			$("#add_memory_btn").click(function(){
				if(memoryListSize>=12){
					top.$.messager.alert("提示","内存显示项不能超过12项","info");
					return;
				}
				top.showSingleDialog({
					url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=packageOptionService&method=addPackagePricePage&type="+encodeURIComponent(2)+"&region="+encodeURIComponent(_region),
					onClose: function(data){
						$('#memory_datagrid').datagrid('reload');
					}
				});
			});
			function checkSize(){
				var d = $("#memory_datagrid").datagrid("getData");
				var t = d.total;
				memoryListSize = t;
			}
		</script>
	</body>
</html> 