<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
%>
<!DOCTYPE html>
<!-- sys_warnrule_manage.jsp -->
<html>
<head>
	<meta charset="UTF-8" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
	<title>预警规则管理</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
</head>

<body style="visibility:hidden;">
<div class="oper-wrap">
	<div class="tab-container">
		<div id="toolbar">
			<div style="display: table; width: 100%;">
				<div class="btn-info f-mb5">
					<a class="easyui-linkbutton oper-btn-sty" href="javascript:;" data-options="plain:true,iconCls:'icon-add'" id="add_rule_btn">添加</a> 
					<a class="easyui-linkbutton oper-btn-sty f-ml10" href="javascript:;" data-options="plain:true,iconCls:'icon-remove'" id="del_rule_btn">删除</a>
				</div>
				<div class="sear-info">
					<span class="sear-row">
						<label class="f-mr5">规则类型</label>
						<select id="ruletype" class="slt-sty">
							   <option value="">全部</option>
							   <option value="0">告警</option>
							   <option value="1">故障</option>
						</select>
					</span>
					<span class="sear-row">
						<label class="f-mr5">通知类型</label>
						<select id="realtime" class="slt-sty">
							<option value="">全部</option>
							<option value="1">定时</option>
							<option value="2">实时</option>
						</select>
					</span>
					<span class="sear-row">
						<label class="f-mr5">通知电话</label> 
						<input type="text" id="notify_phone" class="messager-input" />
					</span>
					<span class="sear-row">
						<label class="f-mr5">通知邮件</label> 
						<input type="text" id="notify_email" class="messager-input" /> 	
					</span>
					<span class="sear-row">											
						<a class="easyui-linkbutton oper-btn-sty f-ml10" href="javascript:;" data-options="iconCls:'icon-search'" id="query_cloud_host_btn">查询</a>
						<a class="easyui-linkbutton oper-btn-sty f-ml10" href="javascript:;" data-options="iconCls:'icon-redo'" id="clear_cloud_host_btn">清除</a>
					</span>
				</div>
			</div>
		</div>
		
		<table id="warnrule_datagrid" class="easyui-datagrid" title="预警规则信息" data-options="url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=sysWarnService&method=RuleQuery',queryParams: {},toolbar: '#toolbar',rownumbers: false,striped: true,remoteSort:false,fitColumns: true,pagination: true,pageSize: 20,view: createView(),onLoadSuccess: onLoadSuccess()">				
			<thead>
				<tr>
					<th data-options="field:'ck',checkbox:true"></th>
					<th data-options="field:'name',width:100">规则名称</th>
					<th data-options="field:'code',width:100">规则标示符</th>
					<th data-options="field:'ruletype',formatter:typeColumnFormatter,width:100">规则类型</th>
					<th data-options="field:'frequency',formatter:frequencyColumnFormatter,width:100">采样频率</th>
					<th data-options="field:'sampletime',width:100">采样次数</th>
					<th data-options="field:'content',width:100">规则内容</th>
					<th data-options="field:'isnotify',formatter:notifyColumnFormatter,width:100">是否发送通知</th>
					<th data-options="field:'realtime',formatter:realColumnFormatter,width:100">通知类型</th>
					<th data-options="field:'sendtime',width:100">定时发送时间</th>
					<th data-options="field:'notify_phone',width:100">通知电话</th>
					<th data-options="field:'notify_email',width:100">通知邮件</th>
					<th data-options="field:'operate',formatter:operateColumnFormatter,width:100">操作</th>
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
$("#warnrule_datagrid").height( $(document.body).height()-20);

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
</body>
</html>