﻿<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="com.zhicloud.op.vo.MarkVO"%>
<%@page import="java.util.List"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
	List<MarkVO> markList = (List<MarkVO>)request.getAttribute("markList");
%>
<!DOCTYPE html>
<!-- agent_manage.jsp -->
<html>
<head>
	<meta charset="UTF-8" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
	<title>运营商管理员 - 代理商管理</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
</head>

<body>
<div class="oper-wrap">
	<div class="tab-container">
		<input name="agent_id" id="agent_id" type="hidden" value=""/>
		<div id="toolbar">
			<div style="display: table; width: 100%;">
				<div class="btn-info f-mb5">
					<a id="add_agent_btn" class="easyui-linkbutton oper-btn-sty" href="javascript:;" data-options="plain:true,iconCls:'icon-add'">添加</a> 
					<a id="del_agent_btn" class="easyui-linkbutton oper-btn-sty f-ml10" href="javascript:;" data-options="plain:true,iconCls:'icon-remove'">删除</a>
					<a id="user_agent_btn" class="easyui-linkbutton oper-btn-sty f-ml10" href="javascript:;" data-options="plain:true,iconCls:'icon-undo'">查看代理商用户</a>
					<a id="export_data_btn" class="easyui-linkbutton oper-btn-sty f-ml10" href="javascript:;" data-options="plain:true,iconCls:'icon-print'">导出数据</a>
				</div>
				<div class="sear-info f-mb5">
					<span class="sear-row">
						<label class="f-mr5" style="width:48px;">折扣</label>
						<select id="percentoff_op" class="slt-sty">
							<option value = "0">等于</option>
							<option value = "1">大于</option>
							<option value = "-1">小于</option>
						</select>
						<input type="text" name = "percentoff" id="percentoff" class="messager-input" />
					</span>
					<span class="sear-row">
						<label class="f-ml10 f-mr5">账户余额</label>
						<select id="account_balance_op" class="slt-sty">
							<option value = "0">等于</option>
							<option value = "1">大于</option>
							<option value = "-1">小于</option>
						</select>
						<input type="text" name="account_balance" id="account_balance" class="messager-input"/>
					</span>
					<span class="sear-row">
						<label class="f-ml10 f-mr5">创建时间</label>
						<input type="text" name = "create_time_from" id="create_time_from" class="easyui-datebox date-sty"/>
						<i class="f-ml5 f-mr5">到</i>
						<input type="text" name = "create_time_to" id="create_time_to" class="easyui-datebox date-sty"/>
					</span>
				</div>
				<div class="sear-info">
					<span class="sear-row">
						<label class="f-mr5">累计充值</label>
						<select id="recharge_op" class="slt-sty">
							<option value = "0">等于</option>
							<option value = "1">大于</option>
							<option value = "-1">小于</option>
						</select>					
						<input type="text" name = "recharge" id="recharge" class="messager-input" />
					</span>
					<span class="sear-row">
						<label class="f-ml10 f-mr5">累计消费</label>
						<select id="consum_op" class="slt-sty">
							<option value = "0">等于</option>
							<option value = "1">大于</option>
							<option value = "-1">小于</option>
						</select>						
						<input type="text" name = "consum" id="consum" class="messager-input"/>
					</span>
					<span class="sear-row">
						<label class="f-ml10 f-mr5">手机号码</label>
						<input type="text" name = "phone" id="phone" class="messager-input"/>
					</span>
					<span class="sear-row">
						<label class="f-ml10 f-mr5" style="width:24px;">邮箱</label>
						<input type="text" name = "account" id="account" class="messager-input"/>
					</span>
					<span class="sear-row">
						<label class="f-ml10 f-mr5" style="width:24px;">标签</label>
						<select id="query_by_mark" class="slt-sty">
							<option value="all">全部</option>
							<%for(MarkVO mark : markList ){%><option value="<%=mark.getId()%>"><%=mark.getName()%></option><%}%>
						</select>
					</span>
					<span class="sear-row"> 
						<a id="query_agent_btn" class="easyui-linkbutton oper-btn-sty f-ml10" href="javascript:;" data-options="iconCls:'icon-search'">查询</a>
						<a id="clear_agent_btn" class="easyui-linkbutton oper-btn-sty f-ml10" href="javascript:;" data-options="iconCls:'icon-redo'">清除</a>
					</span>
				</div>
			</div>
		</div>
		<table id="agent_datagrid" class="easyui-datagrid" title="代理商管理" data-options="url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=agentService&method=queryAgent',queryParams: {},toolbar: '#toolbar',rownumbers: false,striped: true,remoteSort:false,fitColumns: true,pagination: true,pageSize: 20,view: createView(),onLoadSuccess: onLoadSuccess">
			<thead>
				<tr>
					<th data-options="field:'ck',checkbox:true"></th>
					<th data-options="field:'name',width:100">昵称</th>
					<th data-options="field:'account',sortable:true,width:150">邮箱</th>
					<th data-options="field:'phone',width:120">手机</th>
					<th data-options="field:'markName',sortable:true,width:100">标签</th>
					<th data-options="field:'percentOff',sortable:true,width:100">折扣率(%)</th>
					<th data-options="field:'accountBalance',sortable:true,width:100">账户余额</th>
					<th data-options="field:'recharge',sortable:true,width:100">累计充值</th>
					<th data-options="field:'consum',sortable:true,width:100">累计消费</th>
					<th data-options="field:'status',sortable:true,formatter:formatStatus,width:100">状态</th>
					<th data-options="field:'create_time',formatter:formatStatus,width:100">创建时间</th>
					<th data-options="field:'operate',formatter:formatStatus,width:100">操作</th>
				</tr>
			</thead>
		</table>
	</div>
</div>

<!-- JavaScript_start -->
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
$("#agent_datagrid").height($(document.body).height()-20);

function formatCreateTime(val, row){
	return $.formatDateString(val, "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss");
}

function formatStatus(val, row){  
	if(val == 1){  
	    return "正常";  
	}else if(val == 2) {
	    return "禁用";  
	}else{
	 	return "结束";
	}
}  

function agentColumnFormatter(value, row, index){
	return "<div row_index='"+index+"'>\
				<a href='#' class='datagrid_row_linkbutton modify_btn'>修改</a>\
				<a href='#' class='datagrid_row_linkbutton reset_password_btn'>重置密码</a>\
				<a href='#' class='datagrid_row_linkbutton recharge'>充值</a>\
				<a href='#' class='datagrid_row_linkbutton recharge_btn'>充值记录</a>\
				<a href='#' class='datagrid_row_linkbutton consume_btn'>消费记录</a>\
				<a href='#' class='datagrid_row_linkbutton operation_btn'>操作日志</a>\
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

function onLoadSuccess(){
	$("body").css({
		"visibility":"visible"
	});
	// 每一行的'修改'按钮
	$("a.modify_btn").click(function(){
		$this = $(this);
		rowIndex = $this.parent().attr("row_index");
		var data = $("#agent_datagrid").datagrid("getData");
		var id = data.rows[rowIndex].id;
		top.showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?type=3&userType=<%=userType%>&bean=agentService&method=modPage&agentId="+encodeURIComponent(id),
			onClose: function(data){
				$('#agent_datagrid').datagrid('reload');
			}
		});
	});
	// 每一行的'充值'按钮
	$("a.recharge").click(function(){
		$this = $(this);
		rowIndex = $this.parent().attr("row_index");
		var data = $("#agent_datagrid").datagrid("getData");
		var id = data.rows[rowIndex].id;
		top.showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?type=3&userType=<%=userType%>&bean=agentService&method=toRecharge&agentId="+encodeURIComponent(id),
			onClose: function(data){
				$('#agent_datagrid').datagrid('reload');
			}
		});
	});
	// 每一行的'重置密码'按钮
	$("a.reset_password_btn").click(function(){
		$this = $(this);
		rowIndex = $this.parent().attr("row_index");
		var data = $("#agent_datagrid").datagrid("getData");
		var id = data.rows[rowIndex].id;
		top.showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?type=3&userType=<%=userType%>&bean=agentService&method=resetPasswordPage&agentId="+encodeURIComponent(id),
			onClose: function(data){
				$('#agent_datagrid').datagrid('reload');
			}
		});
	});
	// 每一行的'充值记录'按钮
	$("a.recharge_btn").click(function(){
		$this = $(this);
		rowIndex = $this.parent().attr("row_index");
		var data = $("#agent_datagrid").datagrid("getData");
		var id = data.rows[rowIndex].id;
		top.showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?type=3&userType=<%=userType%>&bean=accountBalanceService&method=getRechargeRecordByUserId&terminalUserId="+encodeURIComponent(id),
			onClose: function(data){
				$('#agent_datagrid').datagrid('reload');
			}
		});
	});
	// 每一行的'消费记录'按钮
	$("a.consume_btn").click(function(){
		$this = $(this);
		rowIndex = $this.parent().attr("row_index");
		var data = $("#agent_datagrid").datagrid("getData");
		var id = data.rows[rowIndex].id;
		top.showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?type=3&userType=<%=userType%>&bean=accountBalanceService&method=toConsumptionRecordPage&terminalUserId="+encodeURIComponent(id),
			onClose: function(data){
				$('#agent_datagrid').datagrid('reload');
			}
		});
	});
	// 每一行的'操作日志'按钮
	$("a.operation_btn").click(function(){
		$this = $(this);
		rowIndex = $this.parent().attr("row_index");
		var data = $("#agent_datagrid").datagrid("getData");
		var id = data.rows[rowIndex].id;
		top.showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?type=3&userType=<%=userType%>&bean=operLogService&method=managePage&terminalUserId="+encodeURIComponent(id),
				onClose: function(data){
					$('#agent_datagrid').datagrid('reload');
				}
			});
		});
	}
	
	$(function(){
		// 查询
		$("#query_agent_btn").click(function(){
			var queryParams = {};
			queryParams.percent_off_op = $("#percentoff_op").val().trim(); //.combobox("getValue");
			queryParams.percent_off = $("#percentoff").val().trim();
			if($("#percentoff").val()!=""){
			if(!(/^[0-9][0-9]?$/.test($("#percentoff").val()))){ 
			    top.$.messager.alert("警告","折扣请输入0-99的整数","warning");
				return;
			}
			}
			
			queryParams.account_balance = $("#account_balance").val().trim();
			queryParams.account_balance_op = $("#account_balance_op").val().trim(); //.combobox("getValue");
			
			if($("#account_balance").val()!=""){
				if(!(/^([1-9][\d]{0,7}|0)(\.[\d]{1,2})?$/.test($("#account_balance").val()))){ 
				    top.$.messager.alert("警告","请输入正确的账户余额","warning");
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
			
			queryParams.recharge = $("#recharge").val().trim();
			queryParams.recharge_op = $("#recharge_op").val().trim(); //.combobox("getValue");
			if($("#recharge").val()!=""){
				if(!(/^([1-9][\d]{0,7}|0)(\.[\d]{1,2})?$/.test($("#recharge").val()))){ 
				    top.$.messager.alert("警告","请输入正确的充值金额","warning");
					return;
				}
			}				
			
			queryParams.consum = $("#consum").val().trim();
			queryParams.consum_op = $("#consum_op").val().trim(); //.combobox("getValue");
			
			if($("#consum").val()!=""){
				if(!(/^([1-9][\d]{0,7}|0)(\.[\d]{1,2})?$/.test($("#consum").val()))){ 
				    top.$.messager.alert("警告","请输入正确的消费金额","warning");
					return;
				}
			}				
			queryParams.account = $("#account").val().trim();
			queryParams.phone = $("#phone").val().trim();
			if($("#phone").val()!=""){
				if(!(/^[0-9][0-9]{0,10}?$/.test($("#phone").val()))){ 
				    top.$.messager.alert("警告","请输入正确的手机号码","warning");
					return;
				}
			}
			queryParams.query_mark = $("#query_by_mark").val().trim(); //.combobox("getValue");
			if($("#account").val().trim()!=""){
				if($("#account").val().trim().length>50){ 
				    top.$.messager.alert("警告","邮箱最多允许50个字符","warning");
					return;
				}
			}	
			$('#agent_datagrid').datagrid({
				"queryParams": queryParams
			});
		});
		//清除
		$("#clear_agent_btn").click(function(){
			$("#percentoff").val("");
			$("#percentoff_op").val("0"); //.combobox("setValue","0");
			$("#account_balance").val("");
			$("#account_balance_op").val("0"); //.combobox("setValue","0");
			$("#create_time_from").val("");
			$("#create_time_to").val("");
			$("#recharge").val("");
			$("#recharge_op").val("0"); //.combobox("setValue","0");
			$("#consum").val("");
			$("#consum_op").val("0"); //.combobox("setValue","0");
			$("#account").val("");
			$("#query_by_mark").val("all"); //.combobox("setValue","all");
			$("#create_time_from").datebox("setValue","");
			$("#create_time_to").datebox("setValue","");
			$("#phone").val("");
			
		});		
		//按标签查询
/* 		$("#query_by_mark").change(function(){
			var queryParams = {};
			queryParams.account = $("#account").val().trim();
			queryParams.query_mark = $("#query_by_mark").val();
			$('#agent_datagrid').datagrid({
				"queryParams": queryParams
			});
		}); */
		// 添加代理商
		$("#add_agent_btn").click(function(){
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=agentService&method=addPage",
			onClose: function(data){
				$('#agent_datagrid').datagrid('reload');
			}
		});
	});
	// 删除代理商
	$("#del_agent_btn").click(function() {
		var rows = $('#agent_datagrid').datagrid('getSelections');
		if (rows == null || rows.length == 0) {
			top.$.messager.alert("警告","未选择删除项","warning");
			return;
		}
		var ids = rows.joinProperty("id");
		top.$.messager.confirm("确认", "确定删除?", function (r) {  
	        if (r) {   
				ajax.remoteCall("bean://agentService:deleteAgentByIds",
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
							$('#agent_datagrid').datagrid(
									'reload');
						}
					}
				); 
	        }  
	    });   
	});
	
	$("#user_agent_btn").click(function(){
		var rows = $('#agent_datagrid').datagrid('getSelections');
		if (rows == null || rows.length == 0) {
			top.$.messager.alert("警告","请选择代理商","warning");
			return;
		}	
		if (rows.length > 1) {
			top.$.messager.alert("警告","请选择一个代理商进行查看","warning");
			return;
		}	
		top.showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=agentService&method=userPage&agentid="+rows[0].id,
			onClose: function(data){
				$('#agent_datagrid').datagrid('reload');
			}
		});
	});		
	//导出数据
	$("#export_data_btn").click(function() {
		var queryParams = "";
		queryParams += "percent_off_op="+ $("#percentoff_op").val().trim(); //.combobox("getValue");
		queryParams +="&percent_off="+ $("#percentoff").val().trim();
		if($("#percentoff").val()!=""){
		if(!(/^[0-9][0-9]?$/.test($("#percentoff").val()))){ 
		    top.$.messager.alert("警告","折扣请输入0-99的整数","warning");
			return;
		}
		}
		
		queryParams +="&account_balance="+$("#account_balance").val().trim();
		queryParams +="&account_balance_op="+$("#account_balance_op").val().trim(); //.combobox("getValue");
		
		if($("#account_balance").val()!=""){
			if(!(/^([1-9][\d]{0,7}|0)(\.[\d]{1,2})?$/.test($("#account_balance").val()))){ 
			    top.$.messager.alert("警告","请输入正确的账户余额","warning");
				return;
			}
		}		
		queryParams +="&create_time_from="+$("#create_time_from").datebox("getValue");
		if($("#create_time_from").datebox("getValue")!=""){
			if(!(/\d{4}-\d{2}-\d{2}/.test($("#create_time_from").datebox("getValue")))){ 
			    top.$.messager.alert("警告","请输入正确的起始日期","warning");
				return;
			}
		}				
		queryParams +="&create_time_to="+$("#create_time_to").datebox("getValue");
		if($("#create_time_to").datebox("getValue")!=""){
			if(!(/\d{4}-\d{2}-\d{2}/.test($("#create_time_to").datebox("getValue")))){ 
			    top.$.messager.alert("警告","请输入正确的截止日期","warning");
				return;
			}
		}		
		
		queryParams +="&recharge="+ $("#recharge").val().trim();
		queryParams +="&recharge_op="+$("#recharge_op").val().trim(); //.combobox("getValue");
		if($("#recharge").val()!=""){
			if(!(/^([1-9][\d]{0,7}|0)(\.[\d]{1,2})?$/.test($("#recharge").val()))){ 
			    top.$.messager.alert("警告","请输入正确的充值金额","warning");
				return;
			}
		}				
		
		queryParams +="&consum="+$("#consum").val().trim();
		queryParams +="&consum_op="+$("#consum_op").val().trim(); //.combobox("getValue");
		
		if($("#consum").val()!=""){
			if(!(/^([1-9][\d]{0,7}|0)(\.[\d]{1,2})?$/.test($("#consum").val()))){ 
			    top.$.messager.alert("警告","请输入正确的消费金额","warning");
				return;
			}
		}				
		queryParams +="&account="+ $("#account").val().trim();
		queryParams +="&query_mark="+$("#query_by_mark").val().trim(); //.combobox("getValue");
		if($("#account").val().trim()!=""){
			if($("#account").val().trim().length>50){ 
			    top.$.messager.alert("警告","邮箱最多允许50个字符","warning");
				return;
			}
		}
		top.$.messager.confirm("确认", "确定导出？", function (r) {  
	        if (r) {   
				window.location.href= "<%=request.getContextPath()%>/agentExportData.do?"+queryParams;
	        }  
	    });   
	});
	 $('#account').bind('keypress',function(event){
        if(event.keyCode == "13")    
        {
        	$("#query_agent_btn").click();
        }
    });
});
</script>
<!-- JavaScript_end -->
</body>
</html>