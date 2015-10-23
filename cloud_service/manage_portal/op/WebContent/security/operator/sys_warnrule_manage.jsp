<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
%>
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		
		<title>预警规则 - 管理页面</title>
		
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
			<table id="warnrule_datagrid" class="easyui-datagrid" title="预警规则信息"
				style=""
				data-options="
					url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=sysWarnService&method=RuleQuery',
					queryParams: {},
					toolbar: '#toolbar',
					rownumbers: true,
					striped: true,
					remoteSort:false,
					fitColumns: false,
					pagination: true,
					pageList: [10, 20, 50, 100, 200],
					pageSize: 20,
					view: createView(),
					onLoadSuccess: onLoadSuccess
				">				
				<thead>
					<tr>
						<th data-options="checkbox:true"></th>
						<th field="name" width="100px">规则名称</th>
						<th field="code" width="80px">规则标示符</th>
						<th field="ruletype" formatter="typeColumnFormatter" width="60px">规则类型</th>
						<th field="frequency" width="60px" formatter="frequencyColumnFormatter">采样频率</th>
						<th field="sampletime" width="60px" >采样次数</th>
						<th field="content" width="200px" >规则内容</th>
						<th field="isnotify" formatter="notifyColumnFormatter" width="80px" >是否发送通知</th>
						<th field="realtime" formatter="realColumnFormatter" width="60px" >通知类型</th>
						<th field="sendtime" width="100px" >定时发送时间</th>
						<th field="notify_phone" width="100px" >通知电话</th>
						<th field="notify_email" width="100px" >通知邮件</th>
<!-- 						<th field="username" width="100px" >规则新增人</th>
						<th field="insert_date" width="100px">规则新增时间</th> -->
						<th field="operate" formatter="operateColumnFormatter"  width="120px" align="center">操作</th>
					</tr>
				</thead>
			</table>
	
			<div id="toolbar" style="padding: 2px;">
				<div style="display: table; width: 100%;">
					<div style="margin-bottom:5px;">
						<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="add_rule_btn">添加</a> 
						<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" id="del_rule_btn">删除</a>
					</div>
					<div style="margin-left:5px;">
					<span style="position: relative; top: 2px;">规则类型</span>
						<select id="ruletype" class="easyui-combobox" style="width:80px;">
							   <option value="">全部</option>
							   <option value="0">告警</option>
							   <option value="1">故障</option>
						</select>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<span style="position: relative; top: 2px;">通知类型</span>
						<select id="realtime" class="easyui-combobox" style="width:80px;">
							   <option value="">全部</option>
							   <option value="1">定时</option>
							   <option value="2">实时</option>
						</select>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<span style="position: relative; top: 2px;">通知电话</span> 
						<input type="text" id="notify_phone" class="messager-input" /> 
						&nbsp;&nbsp;&nbsp;&nbsp;
						<span style="position: relative; top: 2px;">通知邮件</span> 
						<input type="text" id="notify_email" class="messager-input" /> 	
																		
						<a href="#" class="easyui-linkbutton" iconCls="icon-search" id="query_cloud_host_btn">查询</a>
						<a href="#" class="easyui-linkbutton" iconCls="icon-redo" id="clear_cloud_host_btn">清除</a>
					</div>
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
	$("#warnrule_datagrid").height( $(document.body).height());
	
	function operateColumnFormatter(value, row, index)
	{
		return "<div row_index='"+index+"'>\
					<a href='#' class='datagrid_row_linkbutton modify_btn'>修改</a>\
					<a href='#' class='datagrid_row_linkbutton content_btn'>编辑规则</a>\
				</div>";
	}
	function frequencyColumnFormatter(val, row)
	{  
		return val+"秒";
	}  
	function notifyColumnFormatter(val, row)
	{  
		if(val == 1){  
		    return "是";  
		}else if(val == 0) {
		    return "否";  
		}
	}  
	function typeColumnFormatter(val, row)
	{  
		if(val == 1){  
		    return "故障";  
		}else if(val == 0) {
		    return "告警";  
		}
	}  	
	function realColumnFormatter(val, row)
	{  
		if(val == 1){  
		    return "定时";  
		}else if(val == 2) {
		    return "实时";  
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
		// 每一行的'修改'按钮
		$("a.modify_btn").click(function(){
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#warnrule_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].id;
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=sysWarnService&method=ModRulePage&ruleid="+id,
				onClose: function(data){
					$('#warnrule_datagrid').datagrid('reload');
				}
			});
		});
		// 每一行的'编辑内容'按钮
		$("a.content_btn").click(function(){
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#warnrule_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].id;
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=sysWarnService&method=ValueManagePage&ruleid="+id,
				onClose: function(data){
					$('#warnrule_datagrid').datagrid('reload');
				}
			});
		});
	}
	$(function(){
		// 查询
		$("#query_cloud_host_btn").click(function(){
			var queryParams = {};
			queryParams.ruletype = $("#ruletype").combobox("getValue");
			queryParams.realtime = $("#realtime").combobox("getValue");
			if($("#notify_phone").val().trim()!=""){
				if(!/^[0-9]{1,9}$/.test($("#notify_phone").val().trim())){ 
				    top.$.messager.alert("警告","请输入数字","warning");
					return;
				}
			}
			queryParams.notify_phone = $("#notify_phone").val().trim();
			if($("#notify_email").val().trim()!=""){
				if($("#notify_email").val().trim().length>50){ 
				    top.$.messager.alert("警告","邮件长度超出定义的50个字符","warning");
					return;
				}
			}
			queryParams.notify_email = $("#notify_email").val().trim();
			$('#warnrule_datagrid').datagrid({
				"queryParams": queryParams
			});
		});

		$("#clear_cloud_host_btn").click(function(){
			$("#ruletype").combobox("setValue","");
			$("#realtime").combobox("setValue","");
			$("#notify_phone").val("");
			$("#notify_email").val("");
		});
		// 添加预警规则
		$("#add_rule_btn").click(function(){
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=sysWarnService&method=AddRulePage",
				onClose: function(data){
					$('#warnrule_datagrid').datagrid('reload');
				}
			});
		});
		// 删除规则
		$("#del_rule_btn").click(function() {
			var rows = $('#warnrule_datagrid').datagrid('getSelections');
			if (rows == null || rows.length == 0) {
				top.$.messager.alert("警告","未选择删除项","warning");
				return;
			}
			var ids = rows.joinProperty("id");
			top.$.messager.confirm("确认", "确定删除?", function (r) {  
		        if (r) {   
					ajax.remoteCall("bean://sysWarnService:deleteRuleByIDS",
						[ ids ], 
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
								$('#warnrule_datagrid').datagrid('reload');
							}
						}
					); 
		        }  
		    });   
		});
	});
	</script>
</html>