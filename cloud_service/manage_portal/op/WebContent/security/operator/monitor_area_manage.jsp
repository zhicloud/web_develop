<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="java.util.List"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
	String menuflag = (String)request.getAttribute("menuflag");
%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		
		<title>资源监控 - 总体概况分析</title>
		
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
			<table id="area_datagrid" class="easyui-datagrid" title="总体概况监控"
				style=""
				data-options="
					url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=monitorService&method=areaQuery',
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
				<thead>
					<tr>
						<th data-options="checkbox:true"></th>
						<th field="name" width="100px">区域</th>
						<th field="status" formatter="statusColumnFormatter"  width="26px" align="center">当前状态</th>
						<th field="operate" formatter="agentColumnFormatter" width="80px">操作</th>
					</tr>
				</thead>
			</table>
	
			<div id="toolbar" style="padding: 2px;">
			<span class="round_normal">●</span> <span style="font-size: 14px;" id="normalspan"></span>
			<span class="round_warning">●</span> <span style="font-size: 14px;" id="warningspan"></span>
			<span class="round_error">●</span> <span style="font-size: 14px;" id="errorspan"></span>
			<span class="round_stop">●</span> <span style="font-size: 14px;" id="shieldspan"></span>
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
		return "<div row_index='"+index+"'>\
					<a href='#' class='datagrid_row_linkbutton modify_btn'>查看机房信息</a>\
				</div>";
	}
	function statusColumnFormatter(value, row, index)
	{
		return "<span class='round_"+value+"'>●</span>";
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
	function onLoadSuccess()
	{
		$("body").css({
			"visibility":"visible"
		});
		updatenormalnum();
		// 每一行的'查看机房'按钮
		$("a.modify_btn").click(function(){
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#area_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].id;
			window.location.href = "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=monitorService&method=roomPage&menuflag=<%=menuflag%>&areaid="+encodeURIComponent(id);
		});
	}
	$(function(){
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
	})
	</script>
</html>