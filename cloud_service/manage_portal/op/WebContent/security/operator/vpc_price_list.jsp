<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.vo.VpcPriceVO"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="com.zhicloud.op.app.propeties.AppProperties"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
	String region = request.getParameter("region");
	String ipPrice = AppProperties.getValue("ip_price_1","");
	String ipPrice_2 = AppProperties.getValue("ip_price_2","");
	String ipPrice_4 = AppProperties.getValue("ip_price_4","");
	request.getSession().setAttribute("optionRegion", region);
%>
<!DOCTYPE html>
<!-- vpc_price_list.jsp -->
<html>
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		
		<title>运营商管理员 - 专属云价格列表</title>
		
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
	</head>
	<body>
		<form id="vpc_form"  method="post">
			<table id="vpc_datagrid" class="easyui-datagrid" 
			style="height:300px;"
				data-options="
					url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=packageOptionService&method=queryVPCPrice',
					queryParams: {
						region:<%=region%>
					},
					loadMeg:'数据加载中，请稍等...',
					toolbar: '#toolbar_vpc',
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
					}
				">
				<thead>
					<tr>
						<th data-options="checkbox:true"></th>
						<th field="vpcAmount" width="20%" sortable=true>主机数</th>
						<th field="region" formatter="formatRegion" width="20%" >地域</th>
						<th field="price" width="20%">价格(元/月)</th>
						<th field="operate" formatter="vpcColumnFormatter" width="20%">操作</th>
					</tr>
				</thead>
			</table>
	
			<div id="toolbar_vpc" style="padding: 3px;">
				<div style="display: table; width: 100%;">
					<div style="display: table-cell; text-align: left">
						<a name="add_btn" href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="add_vpc_btn">添加</a> 
					</div>
				</div>
			</div>
		</form>
		<br/>
		<form id="ip_price_form" method="post">
		<input type="hidden" name="region" value="<%=region %>" />
			<table style="margin-top:10px;">
				<tr style="padding-top:30px;">
					<td>每个IP的价格</td>
					<td><input type="text" id="ip_price" name="ipPrice" value="<%="4".equals(region)?ipPrice_4:("2".equals(region)?ipPrice_2:ipPrice)%>" width="20px" size="10" onblur="checkIpPrice()"/>&nbsp;&nbsp;RMB</td>
				</tr>
				<tr>
					<td><a id="save_ip_price" href="#" class="easyui-linkbutton" icon="icon-save">保存</a></td>
					<td></td>
				</tr>
			</table>
		</form>
		<script type="text/javascript">
		var _region = "<%=region%>";
		// 添加VPC价格
		$("#add_vpc_btn").click(function(){
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=packageOptionService&method=addVPCPricePage&region="+encodeURIComponent(_region),
				onClose: function(data){
					$('#vpc_datagrid').datagrid('reload');
				}
			});
		});
		
		function checkIpPrice(){
			var ipPrice = new String($("#ip_price").val()).trim(); 
			if(ipPrice==null || ipPrice==""){
				top.$.messager.alert('警告','IP的价格不能为空','warning');		
				return false;	
			}
			if(!(/^[0-9]+.?[0-9]*$/.test(ipPrice))){
				top.$.messager.alert('警告','IP的价格必须为正数','warning');
				return false;
			}
			return true;
		}
		
		$("#save_ip_price").click(function(){
			if(!checkIpPrice()){
				return;
			}
			top.$.messager.confirm("确认", "确定修改?", function (r) {  
		        if (r) {
		        	var formData = $.formToBean(ip_price_form);
					ajax.remoteCall("bean://packageOptionService:updateIpPrice", 
						[ formData ],
						function(reply) {
							if (reply.status == "exception") {
								top.$.messager.alert('警告',reply.exceptionMessage,'warning');
							} else if (reply.result.status == "success") {
								top.$.messager.alert('提示',reply.result.message,'info');
								$('#disk_datagrid').datagrid('reload');
							} else {
								top.$.messager.alert('警告',reply.result.message,'warning');
							}
						}
					);
		        }
			});
		});
		</script>
	</body>
</html> 