<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.common.util.FlowUtil"%>
<%@page import="com.zhicloud.op.vo.BandwidthPackageOptionVO"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="com.zhicloud.op.app.propeties.AppProperties"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
	BandwidthPackageOptionVO bandwidthPackageOption = (BandwidthPackageOptionVO)session.getAttribute("bandwidthPackageOption");
	String bandwidthLessThanFive      = AppProperties.getValue("bandwidth_less_than_5","");
	String bandwidthEqualFive         = AppProperties.getValue("bandwidth_equal_5","");
	String bandwidthLessThanFive_2    = AppProperties.getValue("bandwidth_less_than_5_2","");
	String bandwidthEqualFive_2       = AppProperties.getValue("bandwidth_equal_5_2","");
	String bandwidthBase              = AppProperties.getValue("bandwidth_base","");
	String bandwidthBase_2            = AppProperties.getValue("bandwidth_base_2","");
	String bandwidthBase_4            = AppProperties.getValue("bandwidth_base_4","");
	String bandwidthLessThanFive_4    = AppProperties.getValue("bandwidth_less_than_5_4","");
	String bandwidthEqualFive_4       = AppProperties.getValue("bandwidth_equal_5_4","");
	String bandwidthMin_1             = AppProperties.getValue("bandwidthMin_1","");
	String bandwidthMin_2             = AppProperties.getValue("bandwidthMin_2","");
	String bandwidthMin_4             = AppProperties.getValue("bandwidthMin_4","");
	String bandwidthMax_1             = AppProperties.getValue("bandwidthMax_1","");
	String bandwidthMax_2             = AppProperties.getValue("bandwidthMax_2","");
	String bandwidthMax_4             = AppProperties.getValue("bandwidthMax_4","");
	String region                     = request.getParameter("region");
	request.getSession().setAttribute("optionRegion", region);
%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		
		<title>运营商管理员 - 带宽配置列表</title>
		
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
	</head>
	<body>
		<form id="bandwidth_form"  method="post">
			<table id="bandwidth_datagrid" class="easyui-datagrid" 
				style="height:300px;"
				data-options="
					url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=packageOptionService&method=queryPackagePrice',
					queryParams: {
						type:4,
						region:<%=region %>
					},
					loadMeg:'数据加载中，请稍等...',
					toolbar: '#toolbar-bandwidth',
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
						<th field="bandwidth" formatter="formatFlow" width="20%" sortable=true>带宽大小</th>
						<th field="region" formatter="formatRegion" width="20%" >地域</th>
						<th field="price" width="20%">价格(元/月)</th>
						<th field="vpcPrice" width="20%">专属云价格(元/月)</th>
						<th field="operate" formatter="bandwidthColumnFormatter" width="20%">操作</th>
					</tr>
				</thead>
			</table>
			
			<div id="toolbar-bandwidth" style="padding: 3px;">
				<div style="display: table; width: 100%;">
					<div style="display: table-cell; text-align: left">
						<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="add_bandwidth_btn">添加</a> 
					</div>
				</div>
			</div>
		</form>
		<br/>
		<form id="bandwidth_of_one" method="post">
		<input type="hidden" name="region" value="<%=region%>" />
			<table style="margin-top:10px;">
				<tr style="padding-top:30px;">
					<td>最低配置：</td>
					<td><input type="text" id="bandwidth_min" name="minBandwidth" value="<%="4".equals(region)?bandwidthMin_4:("2".equals(region)?bandwidthMin_2:bandwidthMin_1)%>" width="20px" size="10" onblur="checkBandwidthMinAndMax()"/>&nbsp;&nbsp;M</td>
				</tr>
				<tr style="padding-top:30px;">
					<td>最高配置：</td>
					<td><input type="text" id="bandwidth_max" name="maxBandwidth" value="<%="4".equals(region)?bandwidthMax_4:("2".equals(region)?bandwidthMax_2:bandwidthMax_1)%>" size="10" onblur="checkBandwidthMinAndMax()" />&nbsp;&nbsp;M</td>
				</tr>
				<tr>
					<td>计费规则：</td>
					<td><input type="radio" checked="checked" name=""/>&nbsp;5M以内&nbsp;&nbsp;<input id="bandwidth_less_than_five" type="text" name="bandwidthLessThanFive" value="<%="4".equals(region)?bandwidthLessThanFive_4:("2".equals(region)?bandwidthLessThanFive_2:bandwidthLessThanFive) %>" size="7" maxlength="7" onblur="checkBandwidthLessThanFive()"/>&nbsp;&nbsp;元/M/月,
						5M以上<input id="bandwidth_equal_five" type="text" name="bandwidthEqualFive" value="<%="4".equals(region)?bandwidthEqualFive_4:("2".equals(region)?bandwidthEqualFive_2:bandwidthEqualFive) %>" size="7" maxlength="7" onblur="checkBandwidthEqualFive()"/>&nbsp;&nbsp;+&nbsp;&nbsp;(带宽-5)*
						<input id="bandwidth_base" type="text" name="bandwidthBase" value="<%="4".equals(region)?bandwidthBase_4:("2".equals(region)?bandwidthBase_2:bandwidthBase)%>" size="7" maxlength="7" onblur="checkBandwidthBase()"/>元/M/月
					</td>
				</tr>
				<tr>
					<td><a id="save_bandwidth_of_one" href="#" class="easyui-linkbutton" icon="icon-save">保存</a></td>
					<td></td>
				</tr>
			</table>
		</form>
		<script type="text/javascript">
		var _region = "<%=region%>";
		var bandwidthListSize = '0';
		// 添加硬盘配置
			$("#add_bandwidth_btn").click(function(){
				if(bandwidthListSize>=11){
					top.$.messager.alert("提示","带宽显示项不能超过11项","info");
					return;
				}
				top.showSingleDialog({
					url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=packageOptionService&method=addPackagePricePage&type="+encodeURIComponent(4)+"&region="+encodeURIComponent(_region),
					onClose: function(data){
						$('#bandwidth_datagrid').datagrid('reload');
					}
				});
			});
			function checkSize(){
				var d = $("#bandwidth_datagrid").datagrid("getData");
				var t = d.total;
				bandwidthListSize = t;
			}
			function checkBandwidthLessThanFive(){
				var bandwidthLessThanFive = new String($("#bandwidth_less_than_five").val()).trim(); 
				if(bandwidthLessThanFive==null || bandwidthLessThanFive==""){
					top.$.messager.alert('警告','(5M以内)计费规则不能为空','warning');		
					return false;	
				}
				if(!(/^[0-9]+.?[0-9]*$/.test(bandwidthLessThanFive))){
					top.$.messager.alert('警告','带宽计费规则大小必须为正数','warning');
					return false;
				}
				return true;
			}
			
			function checkBandwidthEqualFive(){
				var bandwidthEqualFive = new String($("#bandwidth_equal_five").val()).trim(); 
				if(bandwidthEqualFive==null || bandwidthEqualFive==""){
					top.$.messager.alert('警告','(5M以上)计费规则不能为空','warning');		
					return false;	
				}
				if(!(/^[0-9]+.?[0-9]*$/.test(bandwidthEqualFive))){
					top.$.messager.alert('警告','带宽计费规则大小必须为正数','warning');
					return false;
				}
				return true;
			}
			
			function checkBandwidthBase(){
				var bandwidthBase = new String($("#bandwidth_base").val()).trim(); 
				if(bandwidthBase==null || bandwidthBase==""){
					top.$.messager.alert('警告','基本乘数不能为空','warning');		
					return false;	
				}
				if(!(/^[0-9]+.?[0-9]*$/.test(bandwidthBase))){
					top.$.messager.alert('警告','基本乘数大小必须为正数','warning');
					return false;
				}
				return true;
			}
			
			function checkBandwidthMinAndMax(){
				 min_str = document.getElementById("bandwidth_min").value;
				 max_str =  document.getElementById("bandwidth_max").value;
				 min = parseInt(min_str);
				 max =  parseInt(max_str); 
				 var r = /^[0-9]*[1-9][0-9]*$/;
				if(min_str == "" || max_str == "") {
						top.$.messager.alert('警告','带宽大小不能为空','warning');					
						return false;
				}else if(!(r.test(min_str)) || !(r.test(max_str))){
					top.$.messager.alert('警告','带宽大小必须为正整数','warning');
						return false;
				}else if(min > 999999999|| max > 999999999){
					top.$.messager.alert('警告','本系统最大提供999999999Mbps带宽','warning');
					return false;
				}else if(min>=max){
					top.$.messager.alert('警告','带宽最大值必须大于最小值','warning');
						return false;
				}else{
					return true; 
				}
			}
		
			$("#save_bandwidth_of_one").click(function(){
				if(!(checkBandwidthMinAndMax() && checkBandwidthLessThanFive() && checkBandwidthEqualFive() && checkBandwidthBase())){
					return;
				}
				top.$.messager.confirm("确认", "确定修改?", function (r) {  
			        if (r) {
			        	var formData = $.formToBean(bandwidth_of_one);
						ajax.remoteCall("bean://packageOptionService:updateBandwidthPriceOfOne", 
							[ formData ],
							function(reply) {
								if (reply.status == "exception") {
									top.$.messager.alert('警告',reply.exceptionMessage,'warning');
								} else if (reply.result.status == "success") {
									top.$.messager.alert('提示',reply.result.message,'info');
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