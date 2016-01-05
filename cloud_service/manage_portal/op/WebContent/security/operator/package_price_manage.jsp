<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.common.util.CapacityUtil"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>

<%@page import="com.zhicloud.op.vo.BandwidthPackageOptionVO"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
	String path = request.getContextPath();
	BandwidthPackageOptionVO bandwidthPackageOption = (BandwidthPackageOptionVO)request.getAttribute("bandwidthPackageOption");
%>
<!DOCTYPE html>
<!-- package_price_manage.jsp -->
<html>
<head>
	<meta charset="UTF-8" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
	<title>运营商 - 套餐项管理</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
	<style type="text/css">.layout-panel-west .panel-body {border-right:1px solid #95B8E7;}</style>
</head>

<body>
<div class="oper-wrap">
   <div style="margin:0 0;"></div>
   <div class="easyui-layout" style="width:auto;height:600px;">
       <div id="p" data-options="region:'west'" title="菜单" style="width:180px;">
           <ul id="package_tree"></ul>
       </div>
       <div data-options="region:'center'">
       		<div id="package_tabs" class="easyui-tabs" data-options="fit:true,border:false" style="overflow: hidden;"></div>
       </div>
   </div>
</div>
<!-- JavaScript_start -->
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.ext.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/big.min.js"></script>
<script type="text/javascript">
var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
ajax.async = false;
//布局初始化
$("#cpu_datagrid").height( $(document.body).height());
var _path = "<%=path%>";
$(function(){
	var tabs = $('#package_tabs');
	$("#package_tree").tree({
		data:[{
			"id":1,
			"text":"广州",
			"children":[{
				"id":12,
			    "text":"按流量计费",
			    "state":"closed",
			},{
			    "id":11,
			    "text":"按时间长计费",
			    "children":[{
			    	"id":"set_price",
			        "text":"配置定价",
			        "children":[{
			        	"id":"cpu_1",
			            "text":"CPU-广州",
			            "attributes" : {
							"url" : "/security/operator/package_cpu_list.jsp?userType="+<%=userType%>+"&region=1"
						}
			        },{
			            "id":"memory_1",
			            "text":"内存-广州",
			            "attributes" : {
							"url" : "/security/operator/package_memory_list.jsp?userType="+<%=userType%>+"&region=1"
						}
			        },{
			            "id":"disk_1",
			            "text":"硬盘-广州",
			            "attributes" : {
							"url" : "/security/operator/package_disk_list.jsp?userType="+<%=userType%>+"&region=1"
						}
			        },{
			            "id":"bandwidth_1",
			            "text":"带宽-广州",
			            "attributes" : {
							"url" : "/security/operator/package_bandwidth_list.jsp?userType="+<%=userType%>+"&region=1"
						}
			        },{
			        	"id":"package_1",
			            "text":"套餐配置-广州",
			            "attributes" : {
							"url" : "/security/operator/package_price_list.jsp?userType="+<%=userType%>+"&region=1"
						}
			        },{
			        	"id":"vpc_price_1",
			            "text":"专属云价格-广州",
			            "attributes" : {
							"url" : "/security/operator/vpc_price_list.jsp?userType="+<%=userType%>+"&region=1"
						}
			        },{
			        	"id":"cloud_disk_1",
			            "text":"云硬盘价格-广州",
			            "attributes" : {
							"url" : "/security/operator/cloud_disk_price_list.jsp?userType="+<%=userType%>+"&region=1"
						}
			        }]
			    }]
			}]
		},
		{
			"id":4,
			"text":"香港",
			"state":"closed",
			"children":[{
				"id":42,
			    "text":"按流量计费",
			    "state":"closed",
			},{
			    "id":41,
			    "text":"按时间长计费",
			    "children":[{
			    	"id":"set_price",
			        "text":"配置定价",
			        "children":[{
			        	"id":"cpu_4",
			            "text":"CPU-香港",
			            "attributes" : {
							"url" : "/security/operator/package_cpu_list.jsp?userType="+<%=userType%>+"&region=4"
						}
			        },{
			            "id":"memory_4",
			            "text":"内存-香港",
			            "attributes" : {
							"url" : "/security/operator/package_memory_list.jsp?userType="+<%=userType%>+"&region=4"
						}
			        },{
			            "id":"disk_4",
			            "text":"硬盘-香港",
			            "attributes" : {
							"url" : "/security/operator/package_disk_list.jsp?userType="+<%=userType%>+"&region=4"
						}
			        },{
			            "id":"bandwidth_4",
			            "text":"带宽-香港",
			            "attributes" : {
							"url" : "/security/operator/package_bandwidth_list.jsp?userType="+<%=userType%>+"&region=4"
						}
			        },{
			        	"id":"package_4",
			            "text":"套餐配置-香港",
			            "attributes" : {
							"url" : "/security/operator/package_price_list.jsp?userType="+<%=userType%>+"&region=4"
						}
			        },{
			        	"id":"vpc_price_4",
			            "text":"专属云价格-香港",
			            "attributes" : {
							"url" : "/security/operator/vpc_price_list.jsp?userType="+<%=userType%>+"&region=4"
						}
			        },{
			        	"id":"cloud_disk_4",
			            "text":"云硬盘价格-香港",
			            "attributes" : {
							"url" : "/security/operator/cloud_disk_price_list.jsp?userType="+<%=userType%>+"&region=4"
						}
			        }]
			    }]
			}]
		},
		{
			"id":2,
			"text":"成都",
			"state":"closed",
			"children":[{
				"id":22,
			    "text":"按流量计费",
			    "state":"closed",
			},{
			    "id":21,
			    "text":"按时间长计费",
			    "children":[{
			    	"id":"set_price",
			        "text":"配置定价",
			        "children":[{
			        	"id":"cpu_2",
			            "text":"CPU-成都",
			            "attributes" : {
							"url" : "/security/operator/package_cpu_list.jsp?userType="+<%=userType%>+"&region=2"
						}
			        },{
			            "id":"memory_2",
			            "text":"内存-成都",
			            "attributes" : {
							"url" : "/security/operator/package_memory_list.jsp?userType="+<%=userType%>+"&region=2"
						}
			        },{
			            "id":"disk_2",
			            "text":"硬盘-成都",
			            "attributes" : {
							"url" : "/security/operator/package_disk_list.jsp?userType="+<%=userType%>+"&region=2"
						}
			        },{
			            "id":"bandwidth_2",
			            "text":"带宽-成都",
			            "attributes" : {
							"url" : "/security/operator/package_bandwidth_list.jsp?userType="+<%=userType%>+"&region=2"
						}
			        },{
			        	"id":"package_2",
			            "text":"套餐配置-成都",
			            "attributes" : {
							"url" : "/security/operator/package_price_list.jsp?userType="+<%=userType%>+"&region=2"
						}
			        },{
			        	"id":"vpc_price_2",
			            "text":"专属云价格-成都",
			            "attributes" : {
							"url" : "/security/operator/vpc_price_list.jsp?userType="+<%=userType%>+"&region=2"
						}
			        },{
			        	"id":"cloud_disk_2",
			            "text":"云硬盘价格-成都",
			            "attributes" : {
							"url" : "/security/operator/cloud_disk_price_list.jsp?userType="+<%=userType%>+"&region=2"
						}
			        }]
			    }]
			}]
		}],
		onClick:function(node){
			if(node.id!=1 && node.id!=4 && node.id!=11 && node.id!=12 && node.id!=41 && node.id!=42 && node.id!=2 && node.id!=21 && node.id!=22){
				var url = _path + node.attributes.url;
				var opts = {
					title:node.text,
					href:url,
					id:node.id,
					closable:true
				};
				addTabs(opts);
			}
		}
	});
	
	function addTabs(opts) {
		var tab = tabs.tabs('getSelected');
		var opt = null;
		
		if (tabs.tabs('exists', opts.title)) {
			tabs.tabs('select', opts.title);
		} else {
			if(tab != null){
				opt = tab.panel('options'); 
				tabs.tabs('close',opt['title']);
			}
			tabs.tabs('add', opts);
		}
	}
	
});

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
	// 每一行的cpu'修改'按钮
	$("a.modify_cpu_btn").click(function(){
		$this = $(this);
		rowIndex = $this.parent().attr("row_index");
		var data = $("#cpu_datagrid").datagrid("getData");
		var id = data.rows[rowIndex].id;
		top.showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=packageOptionService&method=modPackagePricePage&id="+encodeURIComponent(id)+"&type="+encodeURIComponent(1),
			onClose: function(data){
				$('#cpu_datagrid').datagrid('reload');
			}
		});
	});
	// 每一行的内存'修改'按钮
	$("a.modify_memory_btn").click(function(){
		$this = $(this);
		rowIndex = $this.parent().attr("row_index");
		var data = $("#memory_datagrid").datagrid("getData");
		var id = data.rows[rowIndex].id;
		top.showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=packageOptionService&method=modPackagePricePage&id="+encodeURIComponent(id)+"&type="+encodeURIComponent(2),
			onClose: function(data){
				$('#memory_datagrid').datagrid('reload');
			}
		});
	});
	// 每一行的硬盘'修改'按钮
	$("a.modify_disk_btn").click(function(){
		$this = $(this);
		rowIndex = $this.parent().attr("row_index");
		var data = $("#disk_datagrid").datagrid("getData");
		var id = data.rows[rowIndex].id;
		top.showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=packageOptionService&method=modPackagePricePage&id="+encodeURIComponent(id)+"&type="+encodeURIComponent(3),
			onClose: function(data){
				$('#disk_datagrid').datagrid('reload');
			}
		});
	});
	// 每一行的带宽'修改'按钮
	$("a.modify_bandwidth_btn").click(function(){
		$this = $(this);
		rowIndex = $this.parent().attr("row_index");
		var data = $("#bandwidth_datagrid").datagrid("getData");
		var id = data.rows[rowIndex].id;
		top.showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=packageOptionService&method=modPackagePricePage&id="+encodeURIComponent(id)+"&type="+encodeURIComponent(4),
			onClose: function(data){
				$('#bandwidth_datagrid').datagrid('reload');
			}
		});
	});
	// 每一行的VPC'修改'按钮
	$("a.modify_vpc_btn").click(function(){
		$this = $(this);
		rowIndex = $this.parent().attr("row_index");
		var data = $("#vpc_datagrid").datagrid("getData");
		var id = data.rows[rowIndex].id;
		top.showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=packageOptionService&method=modVPCPricePage&id="+encodeURIComponent(id),
			onClose: function(data){
				$('#vpc_datagrid').datagrid('reload');
			}
		});
	});
	// 每一行的cpu'删除'按钮
	$("a.delete_cpu_btn").click(function(){
		$this = $(this);
		rowIndex = $this.parent().attr("row_index");
		var data = $("#cpu_datagrid").datagrid("getData");
		var id = data.rows[rowIndex].id;
		top.$.messager.confirm("确认", "确定删除?", function (r) {  
	        if (r) {   
				ajax.remoteCall("bean://packageOptionService:deletePackagePrice",
					[ id ], 
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
							$('#cpu_datagrid').datagrid('reload');
						}
					}
				); 
	        }  
	    });
	});
	// 每一行的内存'删除'按钮
	$("a.delete_memory_btn").click(function(){
		$this = $(this);
		rowIndex = $this.parent().attr("row_index");
		var data = $("#memory_datagrid").datagrid("getData");
		var id = data.rows[rowIndex].id;
		top.$.messager.confirm("确认", "确定删除?", function (r) {  
	        if (r) {   
				ajax.remoteCall("bean://packageOptionService:deletePackagePrice",
					[ id ], 
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
							$('#memory_datagrid').datagrid('reload');
						}
					}
				); 
	        }  
	    });
	});
	// 每一行的硬盘'删除'按钮
	$("a.delete_disk_btn").click(function(){
		$this = $(this);
		rowIndex = $this.parent().attr("row_index");
		var data = $("#disk_datagrid").datagrid("getData");
		var id = data.rows[rowIndex].id;
		top.$.messager.confirm("确认", "确定删除?", function (r) {  
	        if (r) {   
				ajax.remoteCall("bean://packageOptionService:deletePackagePrice",
					[ id ], 
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
							$('#disk_datagrid').datagrid('reload');
						}
					}
				); 
	        }  
	    });
	});
	
	// 每一行的带宽'删除'按钮
	$("a.delete_bandwidth_btn").click(function(){
		$this = $(this);
		rowIndex = $this.parent().attr("row_index");
		var data = $("#bandwidth_datagrid").datagrid("getData");
		var id = data.rows[rowIndex].id;
		top.$.messager.confirm("确认", "确定删除?", function (r) {  
	        if (r) {   
				ajax.remoteCall("bean://packageOptionService:deletePackagePrice",
					[ id ], 
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
							$('#bandwidth_datagrid').datagrid('reload');
						}
					}
				); 
	        }  
	    });
	});
	
	// 每一行的vpc'删除'按钮
	$("a.delete_vpc_btn").click(function(){
		$this = $(this);
		rowIndex = $this.parent().attr("row_index");
		var data = $("#vpc_datagrid").datagrid("getData");
		var id = data.rows[rowIndex].id;
		top.$.messager.confirm("确认", "确定删除?", function (r) {  
	        if (r) {   
				ajax.remoteCall("bean://packageOptionService:deleteVPCPrice",
					[ id ], 
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
							$('#vpc_datagrid').datagrid('reload');
						}
					}
				); 
	        }  
	    });
	});
	
	// 每一行的套餐'删除'按钮
	$("a.delete_package_btn").click(function(){
		$this = $(this);
		rowIndex = $this.parent().attr("row_index");
		var data = $("#package_datagrid").datagrid("getData");
		var id = data.rows[rowIndex].id;
		if(id==undefined || id==null || id==''){
			top.$.messager.alert("警告","该项无法删除，您可以选择“取消编辑”","warning",function(){
			});
			return;
		}
		top.$.messager.confirm("确认", "确定删除该套餐?", function (r) {  
	        if (r) {   
				ajax.remoteCall("bean://packageOptionService:deletePackagePrice",
					[ id ], 
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
							$('#package_datagrid').datagrid('reload');
						}
					}
				); 
	        }  
	    });
	});
}

function formatCapacity(val)
{
	return CapacityUtil.toCapacityLabel(val, 0);
}

function descColumnFormatter(val)
{
	if(val==null || val==''){
		return "无";
	}else{
		return val;
	}
}

function formatFlow(val){
	return FlowUtil.toFlowLabel(val, 0);
}

function cpuColumnFormatter(value, row, index){
	return "<div row_index='"+index+"'>\
				<a href='#' class='datagrid_row_linkbutton modify_cpu_btn'>修改</a>\
				<a href='#' class='datagrid_row_linkbutton delete_cpu_btn'>删除</a>\
			</div>";
}

function memoryColumnFormatter(value, row, index){
	return "<div row_index='"+index+"'>\
				<a href='#' class='datagrid_row_linkbutton modify_memory_btn'>修改</a>\
				<a href='#' class='datagrid_row_linkbutton delete_memory_btn'>删除</a>\
			</div>";
}

function diskColumnFormatter(value, row, index){
	return "<div row_index='"+index+"'>\
				<a href='#' class='datagrid_row_linkbutton modify_disk_btn'>修改</a>\
				<a href='#' class='datagrid_row_linkbutton delete_disk_btn'>删除</a>\
			</div>";
}

function bandwidthColumnFormatter(value, row, index){
	return "<div row_index='"+index+"'>\
				<a href='#' class='datagrid_row_linkbutton modify_bandwidth_btn'>修改</a>\
				<a href='#' class='datagrid_row_linkbutton delete_bandwidth_btn'>删除</a>\
			</div>";
}

function vpcColumnFormatter(value, row, index){
	return "<div row_index='"+index+"'>\
				<a href='#' class='datagrid_row_linkbutton modify_vpc_btn'>修改</a>\
				<a href='#' class='datagrid_row_linkbutton delete_vpc_btn'>删除</a>\
			</div>";
}

function packageColumnFormatter(value, row, index){
	return "<div row_index='"+index+"'>\
				<a href='#' class='datagrid_row_linkbutton delete_package_btn'>删除</a>\
			</div>";
}

function formatCreateTime(val, row){
	return $.formatDateString(val, "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss");
}

function formatRegion(val, row){  
	if(val == 1){  
	    return "广州";  
	}else if(val == 2) {
	    return "成都";  
	}else if(val == 3){
		return "北京";
	}else{
	 	return "香港";
	}
}
</script>
</body>
</html>