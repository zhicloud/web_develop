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
	<title>用户 - 意见反馈</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
</head>

<body style="visibility:hidden;">
<div class="oper-wrap">
	<div class="tab-container">
		<div id="toolbar">
			<div style="display: table; width: 100%;"> 
				<div style="display: table-cell; text-align: left;">
					<span class="sear-row">
						<label class="f-mr5">状态</label> 
						<select class="easyui-combobox" name="status" id="status" class="slt-sty">
							<option value="">全部</option>
							<option value="1">未答复</option> 
							<option value="2">已答复</option> 
						</select>
					</span>
					<span class="sear-row">
						<label class="f-ml10 f-mr5">昵称</label> 
						<input type="text" id="username" class="messager-input" /> 
					</span>
					<span class="sear-row">
						<label class="f-ml10 f-mr5">提交时间</label> 
						<input class="easyui-datebox" type="text" name = "create_time_from" id="create_time_from" class="messager-input"/>
						<i class="f-ml5 f-mr5">到</i>
						<input class="easyui-datebox" type="text" name = "create_time_to" id="create_time_to" class="messager-input"/>
					</span>
					<span class="sear-row">
						<a class="easyui-linkbutton oper-btn-sty f-ml10" href="javascript:;" data-options="iconCls:'icon-search'" id="query_cloud_host_btn">查询</a>
						<a class="easyui-linkbutton oper-btn-sty f-ml10" href="javascript:;" data-options="iconCls:'icon-redo'" id="clear_cloud_host_btn">清除</a>	
					</span>								
				</div>
			</div>
		</div>
		<table id="suggestion_list" class="easyui-datagrid" title="意见反馈" data-options="url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=suggestionService&method=querySuggestion',queryParams: {},toolbar: '#toolbar',rownumbers: true,striped: true,remoteSort:false,fitColumns: true,pagination: true,pageSize: 20,view:createView(),onLoadSuccess: onLoadSuccess()">
			<thead>
				<tr>  
					<th data-options="field:'account',width:100">用户名</th>
					<th data-options="field:'username',width:100">昵称</th>
					<th data-options="field:'submitTime',formatter:timeFormatter,width:100">提交时间</th> 
					<th data-options="field:'status',formatter:statusFormatter,width:100">状态</th> 
					<th data-options="field:'content',width:100">内容</th> 
					<th data-options="field:'result',formatter:resultFormatter,width:100">答复内容</th> 
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
	$("#suggestion_list").height( $(document.body).height()-20);

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
		// 查询
		$("#query_cloud_host_btn").click(function(){
			var queryParams = {};
			queryParams.status = $("#status").combobox("getValue");
			queryParams.username = $("#username").val().trim();
			if($("#username").val()!=""){
				if($("#username").val().length>20){ 
				    top.$.messager.alert("警告","昵称最多允许20个字符","warning");
					return;
				}
			}				
			queryParams.create_time_from = $("#create_time_from").datebox("getValue");
			if($("#create_time_from").datebox("getValue")!=""){
				if(!(/\d{4}-\d{2}-\d{2}/.test($("#create_time_from").datebox("getValue")))){ 
				    top.$.messager.alert("警告","请输入正确的起始日期","warning");
					return;
				}
			}				
			queryParams.create_time_to = $("#create_time_to").datebox("getValue");
			if($("#create_time_to").datebox("getValue")!=""){
				if(!(/\d{4}-\d{2}-\d{2}/.test($("#create_time_to").datebox("getValue")))){ 
				    top.$.messager.alert("警告","请输入正确的截止日期","warning");
					return;
				}
			}				
			$('#suggestion_list').datagrid({
				"queryParams": queryParams
			});
		});
		//清除
		$("#clear_cloud_host_btn").click(function(){
			$("#username").val("");
			$("#status").combobox("setValue","");
			$("#create_time_from").datebox("setValue","");
			$("#create_time_to").datebox("setValue","");
			
		});	
		// 添加代理商
		$("#add_suggestion_btn").click(function(){
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=suggestionService&method=addSuggestionPage",
				onClose: function(data){
					$('#suggestion_list').datagrid('reload');
				}
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
</body>
</html>