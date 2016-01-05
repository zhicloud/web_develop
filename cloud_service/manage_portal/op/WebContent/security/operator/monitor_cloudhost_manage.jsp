<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	String areaid = (String)request.getAttribute("areaid");
	String roomid = (String)request.getAttribute("roomid");
	String rackid = (String)request.getAttribute("rackid");
	String serverid = (String)request.getAttribute("serverid");
	String areaname = (String)request.getAttribute("areaname");
	String roomname = (String)request.getAttribute("roomname");
	String rackname = (String)request.getAttribute("rackname");
	String servername = (String)request.getAttribute("servername");
	String menuflag = (String)request.getAttribute("menuflag");
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
%>
<!DOCTYPE html>
<!-- monitor_cloudhost_manage.jsp -->
<html>
<head>
	<meta charset="UTF-8" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
	<title>监控信息管理 - 云主机信息查看</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
</head>

<body style="visibility:hidden;">
<div class="oper-wrap">
	<div class="tab-container">
		<div id="toolbar">
			<div style="display: table; width: 100%;">
				<div style="line-height:24px;margin-bottom:5px;">
					<span class="round_normal"></span> 
					<span style="font-size: 12px;" id="normalspan"></span>
					<span class="f-ml10 round_warning"></span> 
					<span style="font-size: 12px;" id="warningspan"></span>
					<span class="f-ml10 round_error"></span> 
					<span style="font-size: 12px;" id="errorspan"></span>
					<span class="f-ml10 round_stop"></span> 
					<span style="font-size: 12px;" id="shieldspan"></span>
					<% if(!"host".equals(menuflag)) { %><a class="easyui-linkbutton oper-btn-sty" href="javascript:;" data-options="plain:true,iconCls:'icon-redo'" style="float:right;" id="back_area_btn">返回服务器</a><% }else{ } %>
				</div>
				<% if(!"host".equals(menuflag)) {}else{ %>
				<div class="sear-info">
					<span class="sear-row">
						<label class="f-mr5">地域</label>
						<select id="region" class="slt-sty">
							<option value="">全部</option>
							<option value="1">广州</option>
							<option value="2">成都</option>
							<option value="4">香港</option>
						</select>
					</span>
					<span class="sear-row">
						<label class="f-ml10 f-mr5">状态</label>
						<select id="status" class="slt-sty">
							<option value="error">故障</option>
							<option value="all">全部</option>
							<option value="normal">正常</option>
							<option value="warning">告警</option>
							<option value="stop">屏蔽</option>
						</select>
					</span>
					<span class="sear-row">				
						<label class="f-ml10 f-mr5">主机名</label> 
						<input type="text" id="host_name" class="messager-input" /> 
					</span>
					<span class="sear-row">		
						<a class="easyui-linkbutton oper-btn-sty f-ml10" href="javascript:;" data-options="iconCls:'icon-search'" id="query_cloud_host_btn">查询</a>
						<a class="easyui-linkbutton oper-btn-sty f-ml10" href="javascript:;" data-options="iconCls:'icon-redo'" id="clear_cloud_host_btn">清除</a>
					</span>
				</div>
				<% } %>
			</div>
		</div>
		<% if(!"host".equals(menuflag)) { %>
		<table id="area_datagrid" class="easyui-datagrid" title="<%=areaname %> >> <%=roomname %> >> <%=rackname %> >> <%=servername %> >> 云主机信息" data-options="url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=monitorService&method=hostQuery&serverid=<%=serverid %>&menuflag=<%=menuflag %>',queryParams: {},toolbar: '#toolbar',rownumbers: false,striped: true,remoteSort:false,fitColumns: true,pagination: true,pageSize: 20,view: createView(),onLoadSuccess: onLoadSuccess">
		<% }else{ %>
		<table id="area_datagrid" class="easyui-datagrid" title="云主机信息" data-options="url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=monitorService&method=hostQuery&serverid=<%=serverid %>&menuflag=<%=menuflag %>',queryParams: {},toolbar: '#toolbar',rownumbers: false,striped: true,remoteSort:false,fitColumns: true,pagination: true,pageSize: 20,view: createView(),onLoadSuccess: onLoadSuccess">				
		<% } %>
			<thead>
				<tr>
					<th data-options="checkbox:true"></th>
					<% if(!"host".equals(menuflag)) {}else{ %>
					<th data-options="field:'regionname',width:100">区域</th>
					<% } %>
					<th data-options="field:'hostName',width:100">主机名</th>
					<th data-options="field:'serverip',width:100">宿主机IP</th>
					<th data-options="field:'commonip',width:100">公网IP</th>
					<th data-options="field:'status',formatter:statusColumnFormatter,width:100">当前状态</th>
					<th data-options="field:'operate',formatter:agentColumnFormatter,width:150">操作</th>
				</tr>
			</thead>
		</table>
	</div>
</div>
<!-- JavaScript -->
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
$("#area_datagrid").height( $(document.body).height()-20);

function agentColumnFormatter(value, row, index)
{
	if(row.status=="stop"){
		return "<div row_index='"+index+"'>\
			<a href='#' class='datagrid_row_linkbutton view-detail'>查看云主机详情</a>\
			<a href='#' class='datagrid_row_linkbutton shield_off'>取消屏蔽</a>\
		</div>";
	}else{
		return "<div row_index='"+index+"'>\
			<a href='#' class='datagrid_row_linkbutton view-detail'>查看云主机详情</a>\
			<a href='#' class='datagrid_row_linkbutton shield_on'>屏蔽</a>\
		</div>";
	}
}
function statusColumnFormatter(value, row, index)
{
	return "<span class='round_"+value+"'></span>";
}
function goback(){
	window.location.href = "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=monitorService&method=serverPage&areaid=<%=areaid%>&roomid=<%=roomid%>&rackid=<%=rackid%>&serverid=<%=serverid%>&menuflag=<%=menuflag%>";
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
//更新状态数量	
function updatenormalnum(){
	var data=$('#area_datagrid').datagrid('getData');
	var tempmap = data.map;
	$("#normalspan").html("正常:"+tempmap.normal);
	$("#warningspan").html("告警:"+tempmap.warning);
	$("#errorspan").html("故障:"+tempmap.error);
	$("#shieldspan").html("屏蔽:"+tempmap.stop);
}
//屏蔽和取消屏蔽
function updateshield(obj,id){
	ajax.remoteCall("bean://monitorService:updateShield",
			[ 'host',obj,id ], 
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
				}
				else if(reply.status=="fail"){
					top.$.messager.alert("提示",reply.result.message,"warning");
				}
				else {
					$('#area_datagrid').datagrid(
							'reload');
				}
			}
		);
}	
function onLoadSuccess()
{
	$("body").css({
		"visibility":"visible"
	});
	updatenormalnum();
	// 每一行的'修改'按钮
	$("a.modify_btn").click(function(){
		$this = $(this);
		rowIndex = $this.parent().attr("row_index");
		var data = $("#area_datagrid").datagrid("getData");
		var id = data.rows[rowIndex].id;
		window.location.href = "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=monitorService&method=roomPage&areaid="+encodeURIComponent(id);
	});
	// 每一行的'屏蔽'按钮
	$("a.shield_on").click(function(){
		$this = $(this);
		rowIndex = $this.parent().attr("row_index");
		var data = $("#area_datagrid").datagrid("getData");
		updateshield("1",data.rows[rowIndex].uuid);
	});

	$("a.shield_off").click(function(){
		$this = $(this);
		rowIndex = $this.parent().attr("row_index");
		var data = $("#area_datagrid").datagrid("getData");
		updateshield("0",data.rows[rowIndex].uuid);
	});
	$("a.view-detail").click(function(){
		$this = $(this);
		rowIndex = $this.parent().attr("row_index");
		var data = $("#area_datagrid").datagrid("getData");
		var id = data.rows[rowIndex].uuid;
		top.showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=monitorService&method=viewDetailPage&type=host&uuid="+id,
			params: {
			},
			onClose : function(data) {
				self.query();
			}
		});
	});
	//返回
	$("#back_area_btn").click(function(){
		goback();
	});	
}
$(function(){
	// 查询
	$("#query_cloud_host_btn").click(function(){
		var queryParams = {};
		queryParams.region = $("#region").combobox("getValue");
		queryParams.host_name = $("#host_name").val().trim();
		queryParams.status = $("#status").combobox("getValue");
		if($("#host_name").val().trim()!=""){
			if($("#host_name").val().trim().length>50){ 
			    top.$.messager.alert("警告","主机名最多允许50个字符","warning");
				return;
			}
		}
		$('#area_datagrid').datagrid({
			"queryParams": queryParams
		});
	});

	$("#clear_cloud_host_btn").click(function(){
		$("#region").combobox("setValue","");
		$("#host_name").val("");
		$("#status").combobox("setValue","error");
	});
    self.setInterval(function(){
    	$.extend($.fn.datagrid.methods, { 
    		//显示遮罩 
    		loading: function(jq){ 
    		} ,
    		//隐藏遮罩 
    		loaded: function(jq){ 

    		} 
    		}); 
    	$("#area_datagrid").datagrid("reload");
    },10000);
});
</script>
</body>
</html>