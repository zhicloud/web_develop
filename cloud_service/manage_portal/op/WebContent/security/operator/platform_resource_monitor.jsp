<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType); 
%>
<!DOCTYPE html>
<!-- suggestion_manage.jsp -->
<html>
	<head> 
		<meta charset="UTF-8" />
	    <meta http-equiv="Content-Type" content="textml; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		
		<title>资源管理 - 平台资源监控</title>
		
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
		<style type="text/css">
		#sys_role_datagrid {
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
	<body  >
		<form id="big_form"  method="post">
	
			<table id="suggestion_list" class="easyui-datagrid" title="平台资源监控"
				data-options="
					url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=platformResourceMonitorService&method=getData',
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
						<th field="region" formatter="formatRegion" width="20px">地域</th>
						<th field="server"  width="50px">宿主机[停止,告警,故障,正常]</th>
						<th field="host"  width="50px">云主机[停止,告警,故障,正常]</th>
						<th field="cpuCount"  width="15px">CPU</th>
						<th field="cpuUsage"  width="20px">CPU利用率</th>
						<th field="memory"  width="50px">内存[可用，总量]</th>
						<th field="memoryUsage"  width="20px">内存利用率</th>
						<th field="diskVolume"  width="50px">磁盘[可用,总量]</th>
						<th field="diskUsage"  width="20px">磁盘利用率</th>
						<th field="diskIO"  width="50px">磁盘IO[读请求,读字节,写请求,写字节,IO错误次数]</th>
						<th field="networkIO"  width="50px">网络IO[接收字节,接收包,接收错误,接收丢包,发送字节,发送包,发送错误,发送丢包]</th>
						<th field="speed"  width="50px">速度[读速度,写速度,接收速度,发送速度]</th>
 						<th field="timestamp"    width="50px" >时间戳</th>   
					</tr>
				</thead>
			</table> 
			 
	
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
		$("#suggestion_list").height( $(document.body).height());

		function typeFormatter(val, row)
		{  
			if(val == 1){  
			    return "页面风格";  
			}else if(val == 2) {
			    return "产品功能";  
			}else{
			 	return "其他";
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
		function statusFormatter(val, row)
		{  
			if(val == 1){  
			    return "未答复";  
			}else if(val == 2) {
			    return "已答复";  
			}
		}
		function timeFormatter(val, row){ 
			
			if(val!=null&&val.length>0){ 
				return $.formatDateString(val, "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss");
			}else{
				return "";
			}
		}
		function resultFormatter(value, row, index)
		{ 
			var status = $("#suggestion_list").datagrid("getData").rows[index].status;
			if(status==1){
				return "<div row_index='"+index+"'>\
				  <a href='#' class='datagrid_row_linkbutton view-reply'>答复</a>\
			</div>"; 
			}else{
				return "<div row_index='"+index+"'>\
						  "+value+"\
					</div>"; 
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
			// 每一行的'查看详情列表'按钮
			$("a.view-detail").click(function(){
				$this = $(this);
				rowIndex = $this.parent().attr("row_index");
				var data = $("#suggestion_list").datagrid("getData");
				var id = data.rows[rowIndex].id; 
				top.showSingleDialog({
					url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=suggestionService&method=suggestionDetailPage",
					params: {
						"suggestionId" : id
					},
					onClose : function(data) {
						$('#agent_datagrid').datagrid('reload');
					}
				});
			});
			// 每一行的'答复'按钮
			$("a.view-reply").click(function(){
				$this = $(this);
				rowIndex = $this.parent().attr("row_index");
				var data = $("#suggestion_list").datagrid("getData");
				var id = data.rows[rowIndex].id;
				var type = "reply";  
				top.showSingleDialog({
					url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=suggestionService&method=suggestionDetailPage",
					params: {
						"suggestionId" : id,
						"type" : type
					},
					onClose : function(data) {
						$('#suggestion_list').datagrid('reload'); 
					}
				});
			});
			 
		}
		$(function(){
			ajax.remoteCall("bean://platformResourceMonitorService:startMonistor",
					[ ], 
					function(reply) {
						if (reply.status == "exception") { 
						}
					}
				); 
			window.setInterval(getDate,5000);
			
		});
		
		function getDate(){
			var queryStatus = {}; 
			$('#suggestion_list').datagrid({
				"queryParams": queryStatus
			});
		} 
		
	</script>
</html>