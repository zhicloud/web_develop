<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType    = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
%>
<!DOCTYPE html>
<!-- sys_disk_image_manage.jsp -->
<html>
<head>
	<meta charset="UTF-8" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
	<title>运营商管理员 - 系统镜像管理</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
</head>

<body style="visibility:hidden;">
<div class="oper-wrap">
	<div class="tab-container">
		<div id="toolbar">
			<div style="display: table; width: 100%;">
				<div style="display: table-cell; text-align: left">
					<a class="easyui-linkbutton oper-btn-sty" href="javascript:;" data-options="plain:true,iconCls:'icon-add'" id="add_sysDiskImage_btn">制作系统镜像</a> 
					<a class="easyui-linkbutton oper-btn-sty f-ml10" href="javascript:;" data-options="plain:true,iconCls:'icon-remove'" id="del_sysDiskImage_btn">删除</a>
					<a class="easyui-linkbutton oper-btn-sty f-ml10" href="javascript:;" data-options="plain:true,iconCls:'icon-ok'" id="mo_sysDiskImage_btn">验证发布</a>
					<a class="easyui-linkbutton oper-btn-sty f-ml10" href="javascript:;" data-options="plain:true,iconCls:'icon-cancel'" id="retrieve_sysDiskImage_btn">取消发布</a>
				</div>
				<div style="display: table-cell; text-align: right;">
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
						<label class="f-ml10 f-mr5">镜像名称</label> 
						<input type="text" id="sysDiskImage_name" class="messager-input" /> 
					</span>
					<span class="sear-row">
						<a class="easyui-linkbutton oper-btn-sty f-ml10" href="javascript:;" data-options="iconCls:'icon-search'" id="query_sysDiskImage_btn">查询</a>
					</span>
				</div>
			</div>
		</div>
		<table id="sys_disk_image_datagrid" class="easyui-datagrid" title="系统镜像管理" data-options="url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=sysDiskImageService&method=querySysDiskImage',queryParams: {},toolbar: '#toolbar',rownumbers: false,striped: true,remoteSort:false,fitColumns: true,pagination: true,pageSize: 20,view:createView(),onLoadSuccess: onLoadSuccess">
			<thead>
				<tr>
					<th data-options="field:'ck',checkbox:true"></th>
					<th data-options="field:'name',sortable:true,width:100">镜像名称</th>
					<th data-options="field:'type',formatter:regionType,width:100" >类型</th>
					<th data-options="field:'tag',width:100" >标签</th>
					<th data-options="field:'group_id',width:100">用户所在组</th>
					<th data-options="field:'description',width:100">描述</th>
					<th data-options="field:'region',formatter:regionFormatter,width:100">地域</th>
					<th data-options="field:'status',formatter:regionStatus,width:100">状态</th>
					<th data-options="field:'operate',formatter:sysDiskImageColumnFormatter,width:100">操作</th>
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
$("#sys_disk_image_datagrid").height( $(document.body).height()-20);


function sysDiskImageColumnFormatter(value, row, index)
{
	return "<div row_index='"+index+"'>\
				<a href='#' class='datagrid_row_linkbutton modify_btn'>修改</a>\
			</div>";
}
function regionFormatter(value, row, index)
{ 
	if(value=='1'){
		return "广州";
	}else if(value=='2'){
		return "成都";
	}else if(value=='3'){
		return "";
	}else if(value=='4'){
		return "香港";
	}else{
		return "";
	}
}
function regionType(value, row, index)
{ 
	if(value==1){
		return "通用镜像";
	}else if(value=='2'){
		return "代理商定制镜像";
	}else{
		return "";
	}
}
function regionStatus(value, row, index)
{ 
	if(value=='1'){
		return "未验证";
	}else if(value=='2'){
		return "已验证";
	}else{
		return "";
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
		var data = $("#sys_disk_image_datagrid").datagrid("getData");
		var id = data.rows[rowIndex].id;
		top.showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=sysDiskImageService&method=modPage&sysDiskImagetId="+encodeURIComponent(id),
			onClose: function(data){
				$('#sys_disk_image_datagrid').datagrid('reload');
			}
		});
	});
}

$(function(){
	// 添加镜像
	$("#add_sysDiskImage_btn").click(function(){
		top.showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=sysDiskImageService&method=addPage",
			onClose: function(data){
				$('#sys_disk_image_datagrid').datagrid('reload');
			}
		});
	});
	
	// 查询
	$("#query_sysDiskImage_btn").click(function(){
		var queryParams = {};
		queryParams.sysDiskImage_name = $("#sysDiskImage_name").val().trim();
		queryParams.region = $("#region").val().trim();
		$('#sys_disk_image_datagrid').datagrid({
			"queryParams": queryParams
		});
	});
	
	
	$("#region").change(function(){
		var queryParams = {};
		queryParams.sysDiskImage_name = $("#sysDiskImage_name").val().trim();
		queryParams.region = $("#region").val().trim();
		$('#sys_disk_image_datagrid').datagrid({
			"queryParams": queryParams
		});
	});
	// 删除镜像
	$("#del_sysDiskImage_btn").click(function() {
		var rows = $('#sys_disk_image_datagrid').datagrid('getSelections');
		if (rows == null || rows.length == 0) {
			top.$.messager.alert("警告","未选择删除项","warning");
			return;
		}
		var ids = rows.joinProperty("id");
		$.messager.confirm("确认", "确定删除?", function (r) {  
	        if (r) {     
				ajax.remoteCall("bean://sysDiskImageService:deleteSysDiskImageByIds",
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
							$('#sys_disk_image_datagrid').datagrid('reload');
						}
					}
				);
	        }  
	    });   
	});
	
	// 删除镜像
	$("#retrieve_sysDiskImage_btn").click(function() {
		var rows = $('#sys_disk_image_datagrid').datagrid('getSelections');
		if (rows == null || rows.length == 0) {
			top.$.messager.alert("警告","未选择发布镜像","warning");
			return;
		}
		var ids = rows.joinProperty("id");
			$.messager.confirm("确认", "确定取消发布?", function (r) {  
	        if (r) {     
				ajax.remoteCall("bean://sysDiskImageService:retrieveSysDiskImageByIds",
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
							$('#sys_disk_image_datagrid').datagrid('reload');
						}
					}
				);
	        }  
	    });   
	});
	// 删除镜像
	$("#mo_sysDiskImage_btn").click(function() {
		var rows = $('#sys_disk_image_datagrid').datagrid('getSelections');
		if (rows == null || rows.length == 0) {
			top.$.messager.alert("警告","未选择发布镜像","warning");
			return;
		}
		var ids = rows.joinProperty("id");
			$.messager.confirm("确认", "确定发布?", function (r) {  
	        if (r) {     
				ajax.remoteCall("bean://sysDiskImageService:publishSysDiskImageByIds",
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
							$('#sys_disk_image_datagrid').datagrid('reload');
						}
					}
				);
	        }  
	    });   
	});
	
	
	$('#sysDiskImage_name').bind('keypress',function(event){
        if(event.keyCode == "13")    
        {
        	$("#query_sysDiskImage_btn").click();
        }
    });
});
</script>
</body>
</html>