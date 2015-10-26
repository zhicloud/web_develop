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
<html>
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		
		<title>资源监控 - 云主机信息</title>
		
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
		<style type="text/css">
		#agent_datagrid {
			border: 0px solid red;
		}
		.panel-header {
			border-top: 0px;
			border-bottom: 1px solid #dddddd;
		}
		.panel-header, .panel-body {
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
		.round_normal{width:16px;height:16px;display: inline-block;font-size:20px;line-heigth:16px;text-align:center;color:#99FF66;text-decoration:none}
		.round_warning{width:16px;height:16px;display: inline-block;font-size:20px;line-heigth:16px;text-align:center;color:#FFCC66;text-decoration:none}
		.round_error{width:16px;height:16px;display: inline-block;font-size:20px;line-heigth:16px;text-align:center;color:#FF0000;text-decoration:none}
		.round_stop{width:16px;height:16px;display: inline-block;font-size:20px;line-heigth:16px;text-align:center;color:#666666;text-decoration:none}
		
		.round_normal:hover{text-decoration:none}
		.round_warning:hover{text-decoration:none}
		.round_error:hover{text-decoration:none}
		.round_stop:hover{text-decoration:none}
		</style>
	</head>
	<body style="visibility:hidden;">
		<form id="big_form"  method="post">
		<% if(!"host".equals(menuflag)) {%>
			<table id="area_datagrid" class="easyui-datagrid" title="<%=areaname %> >> <%=roomname %> >> <%=rackname %> >> <%=servername %> >> 云主机信息"
				style=""
				data-options="
					url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=monitorService&method=hostQuery&serverid=<%=serverid %>&menuflag=<%=menuflag %>',
					queryParams: {},
					toolbar: '#toolbar',
					rownumbers: true,
					striped: true,
					remoteSort:false,
					fitColumns: true,
					pagination: true,
					pageList: [10, 20, 50, 100, 200],
					pageSize: 20,
					view: createView(),
					onLoadSuccess: onLoadSuccess
				">
				<%}else{ %>
			<table id="area_datagrid" class="easyui-datagrid" title="云主机信息"
				style=""
				data-options="
					url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=monitorService&method=hostQuery&serverid=<%=serverid %>&menuflag=<%=menuflag %>',
					queryParams: {},
					toolbar: '#toolbar',
					rownumbers: true,
					striped: true,
					remoteSort:false,
					fitColumns: true,
					pagination: true,
					pageList: [10, 20, 50, 100, 200],
					pageSize: 20,
					view: createView(),
					onLoadSuccess: onLoadSuccess
				">				
				<%} %>
				<thead>
					<tr>
						<th data-options="checkbox:true"></th>
						<% if(!"host".equals(menuflag)) {}else{ %>
						<th field="regionname" width="100px">区域</th>
						<%} %>
						<th field="hostName" width="150px">主机名</th>
						<th field="serverip" width="120px">宿主机IP</th>
						<th field="commonip" width="120px">公网IP</th>
<!-- 						<th field="readSpeed" width="120px" sortable="true">磁盘读速度</th>
						<th field="writeSpeed" width="120px" sortable="true">磁盘写速度</th>
						<th field="receiveSpeed" width="120px" sortable="true">网络接收速度</th>
						<th field="sendSpeed" width="120px" sortable="true">网络发送速度</th> -->
						<th field="status" formatter="statusColumnFormatter"  width="80px" align="center">当前状态</th>
						<th field="operate" formatter="agentColumnFormatter" width="150px">操作</th>
					</tr>
				</thead>
			</table>
	
			<div id="toolbar" style="padding: 2px;">
				<div style="display: table; width: 100%;">
					<div style="margin-bottom:5px;">
			<span class="round_normal">●</span> <span style="font-size: 14px;" id="normalspan"></span>
			<span class="round_warning">●</span> <span style="font-size: 14px;" id="warningspan"></span>
			<span class="round_error">●</span> <span style="font-size: 14px;" id="errorspan"></span>
			<span class="round_stop">●</span> <span style="font-size: 14px;" id="shieldspan"></span>
						<% if(!"host".equals(menuflag)) {%>
							<a href="#" style="float:right;" class="easyui-linkbutton" iconCls="icon-redo" id="back_area_btn">返回服务器</a>
						<%}else{ }%>
					</div>
					<% if(!"host".equals(menuflag)) {}else{ %>
					<div style="margin-left:5px;">
					<span style="position: relative; top: 2px;">地域</span>
						<select id="region" class="easyui-combobox" style="width:80px;">
							   <option value="">全部</option>
							   <option value="1">广州</option>
							   <option value="2">成都</option>
							   <option value="4">香港</option>
						</select>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<span style="position: relative; top: 2px;">状态</span>
						<select id="status" class="easyui-combobox" style="width:80px;">
							   <option value="error">故障</option>
							   <option value="all">全部</option>
							   <option value="normal">正常</option>
							   <option value="warning">告警</option>
							   <option value="stop">屏蔽</option>
						</select>							
						&nbsp;&nbsp;&nbsp;&nbsp;
						<span style="position: relative; top: 2px;">主机名</span> 
						<input type="text" id="host_name" class="messager-input" /> 						
						<a href="#" class="easyui-linkbutton" iconCls="icon-search" id="query_cloud_host_btn">查询</a>
						<a href="#" class="easyui-linkbutton" iconCls="icon-redo" id="clear_cloud_host_btn">清除</a>
					</div>
					<%} %>
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
	$("#area_datagrid").height( $(document.body).height());
	
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
		return "<span class='round_"+value+"'>●</span>";
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
</html>