<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
%>
<!DOCTYPE html>
<!-- recharge_record_manage.jsp -->
<html>
	<head> 
		<meta charset="UTF-8" />
	    <meta http-equiv="Content-Type" content="textml; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		
		<title>用户 - 充值记录</title>
		
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
	<body style="visibility:hidden;">
		<form id="big_form"  method="post">
	
			<table id="recharge_record_list" class="easyui-datagrid" title="充值记录"
				data-options="
					url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=accountBalanceService&method=queryRechargeRecord',
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
						<th field="payType" formatter="typeFormatter" width="5px">支付类型</th>
						<th field="amount"   width="10px">金额</th> 
						<th field="changeTime"  formatter="timeFormatter" width="10px">更新时间</th>  
					</tr>
				</thead>
			</table>  
	        <div id="toolbar" style="padding: 3px;">
				<div style="display: table; width: 100%;" align="right"> 
					起止时间：<input class="easyui-datebox" name="startTime" id="startTime"
					 ></input>
					&nbsp; 到 &nbsp; <input class="easyui-datebox" name="endTime" id="endTime"
					 ></input>
					&nbsp;
					<a href="javascript:void(0);" class="easyui-linkbutton" iconCls="icon-search" id="query_suggestion_btn">查询</a>
					
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
		$("#recharge_record_list").height( $(document.body).height());

		function typeFormatter(val, row)
		{  
			if(val == 1){  
			    return "支付宝";  
			}else if(val == 2) {
			    return "银联";  
			}else{
			 	return "系统赠送";
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
			    return val.substring(0,4)+"-"+val.substring(4,6)+"-"+val.substring(6,8);
			}else{
				return "";
			}
		}
		function operateColumnFormatter(value, row, index)
		{
			return "<div row_index='"+index+"'>\
						<a href='javascript:void(0);' class='datagrid_row_linkbutton view-detail'>查看详情</a>\
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
			 
		}
		$(function(){
			// 查询
			$("#query_suggestion_btn").click(function(){
				var queryParams = {}; 
				queryParams.startTime = $('#startTime').datebox('getValue').trim();
				queryParams.endTime = $('#endTime').datebox('getValue').trim();
				$('#recharge_record_list').datagrid({
					"queryParams": queryParams
				});
			}); 
			 
			 $('#type').bind('keypress',function(event){
		        if(event.keyCode == "13")    
		        {
		        	$("#query_suggestion_btn").click();
		        }
		    });
		});
		
	</script>
</html>