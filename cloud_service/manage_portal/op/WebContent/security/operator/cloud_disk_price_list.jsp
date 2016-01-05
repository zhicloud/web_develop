<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.common.util.CapacityUtil"%>
<%@page import="com.zhicloud.op.vo.CloudDiskVO"%>
<%@page import="com.zhicloud.op.app.helper.CloudDiskPrice"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="java.math.BigDecimal"%>
<%@page import="com.zhicloud.op.app.propeties.AppProperties"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
	String cloudDiskMin = AppProperties.getValue("cloudDiskMin","");
	String cloudDiskMax = AppProperties.getValue("cloudDiskMax","");
	String cloudDiskPrice = AppProperties.getValue("cloud_disk_price","");
	String cloudDiskPrice_2 = AppProperties.getValue("cloud_disk_price_2","");
	String cloudDiskPrice_4 = AppProperties.getValue("cloud_disk_price_4","");
	String region = request.getParameter("region");
	request.getSession().setAttribute("optionRegion", region);
	BigDecimal p = CloudDiskPrice.getCloudDiskPrice("1", "20");
	
%>
<!DOCTYPE html>
<!-- cloud_disk_price_list.jsp -->
<html>
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		
		<title>运营商管理员 - 云硬盘配置列表</title>
		
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
	</head>
	<body>
		<form id="cloud_disk_form"  method="post">
			<table id="cloud_disk_datagrid" class="easyui-datagrid" 
				style="height:300px;"
				data-options="
					url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=cloudDiskService&method=queryDiskPrice',
					queryParams: {
						region:<%=region %>
					},
					loadMeg:'数据加载中，请稍等...',
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
						<th field="disk" width="20%" sortable=true>硬盘大小</th>
						<th field="region" formatter="formatRegion" width="20%" >地域</th>
						<th field="monthlyPrice" width="20%">价格(元/月)</th>
<!-- 						<th field="operate" formatter="diskColumnFormatter" width="20%">操作</th> -->
					</tr>
				</thead>
			</table>
			
			<!-- <div id="toolbar-disk" style="padding: 3px;">
				<div style="display: table; width: 100%;">
					<div style="display: table-cell; text-align: left">
						<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="add_cloud_disk_btn">添加</a> 
					</div>
				</div>
			</div> -->
		</form>
		<br/>
		<form id="cloud_disk_of_one" method="post">
		<input type="hidden" name="region" value="<%=region %>" />
			<table style="margin-top:10px;">
				<tr style="padding-top:30px;">
					<td>最低配置：</td>
					<td><input type="text" id="min_disk" name="minDisk" value="<%=cloudDiskMin%>" width="20px" size="10" onblur="checkDiskMinAndMax()"/>&nbsp;&nbsp;GB</td>
				</tr>
				<tr style="padding-top:30px;">
					<td>最高配置：</td>
					<td><input type="text" id="max_disk" name="maxDisk" value="<%=cloudDiskMax%>" size="10" onblur="checkDiskMinAndMax()"/>&nbsp;&nbsp;GB</td>
				</tr>
				<tr>
					<td>计费规则：</td>
					<td><input type="radio" checked="checked" name=""/>&nbsp;<input id="price_of_one" type="text" name="priceOfOne" value="" size="7" onblur="checkPriceOfOne()" maxlength="7"/>&nbsp;&nbsp;元/G/月</td>
				</tr>
				<tr>
					<td><a id="save_cloud_disk_of_one" href="#" class="easyui-linkbutton" icon="icon-save">保存</a></td>
					<td></td>
				</tr>
			</table>
		</form>
		<script type="text/javascript">
			var _p = "<%=p%>";
			var _region = "<%=region%>";
			var _diskPriceOfOne = "<%=cloudDiskPrice%>";
			var _diskPriceOfOne2 = "<%=cloudDiskPrice_2%>";
			var _diskPriceOfOne4 = "<%=cloudDiskPrice_4%>";
			var diskListSize = '0';
			$(function(){
				if(_region==1){
					$("#price_of_one").val(_diskPriceOfOne);
				}else if(_region==2){
					$("#price_of_one").val(_diskPriceOfOne2);
				}else if(_region==4){
					$("#price_of_one").val(_diskPriceOfOne4);
				}
			});
			function checkPriceOfOne(){
				var priceOfOne = new String($("#price_of_one").val()).trim(); 
				if(priceOfOne==null || priceOfOne==""){
					top.$.messager.alert('警告','计费规则不能为空','warning');		
					return false;	
				}
				if(!(/^[0-9]+.?[0-9]*$/.test(priceOfOne))){
					top.$.messager.alert('警告','硬盘计费必须为正数','warning');
					return false;
				}
				return true;
			}
			function checkDiskMinAndMax(){
				var min_str = document.getElementById("min_disk").value;
				var max_str = document.getElementById("max_disk").value;
				var min = parseInt(min_str);
				var max =  parseInt(max_str);
				
					var   r = /^[0-9]*[1-9][0-9]*$/;
					
					if(min_str == "" || max_str == "") {
							top.$.messager.alert('警告','磁盘大小不能为空','warning');		
							return false;
					}else if(!(r.test(min_str)) || !(r.test(max_str))){
						top.$.messager.alert('警告','磁盘大小必须为正整数','warning');
							return false;
					}else if(min > 999999999|| max > 999999999){
						top.$.messager.alert('警告','本系统最大提供99999999GB磁盘空间','warning');
						return false;
					}else if(min>=max){
						top.$.messager.alert('警告','磁盘最大值必须大于最小值','warning');
							return false;
					}else{
						return true;
						
					}
			}
			$("#save_cloud_disk_of_one").click(function(){
				if(!(checkPriceOfOne() && checkDiskMinAndMax())){
					return;
				}
				top.$.messager.confirm("确认", "确定修改?", function (r) {  
			        if (r) {
			        	var formData = $.formToBean(cloud_disk_of_one);
						ajax.remoteCall("bean://cloudDiskService:updateCloudDiskPriceOfOne", 
							[ formData ],
							function(reply) {
								if (reply.status == "exception") {
									top.$.messager.alert('警告',reply.exceptionMessage,'warning');
								} else if (reply.result.status == "success") {
									top.$.messager.alert('提示',reply.result.message,'info');
									$('#cloud_disk_datagrid').datagrid('reload');
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