<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.vo.MarkVO"%>
<%@page import="java.util.List"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
	List<MarkVO> markList = (List<MarkVO>)request.getAttribute("markList");
	Object userIdObj = request.getAttribute("userId");
	String userId = null;
	if(userIdObj!=null){
		userId = (String)userIdObj;
	}
%>
<!DOCTYPE html>
<!-- cloud_disk_manage.jsp -->
<html>
<head>
	<meta charset="UTF-8" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
	<title>运营商 - 用户云硬盘管理</title>
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
					<a class="easyui-linkbutton oper-btn-sty" href="javascript:;" data-options="plain:true,iconCls:'icon-remove'" id="del_cloud_disk_btn">删除</a>
					<a class="easyui-linkbutton oper-btn-sty f-ml10" href="javascript:;" data-options="plain:true,iconCls:'icon-print'" id="export_data_btn">导出数据</a>
				</div>
				<div class="sear-info">
					<span class="sear-row">
						<label class="f-mr5">标签</label>
						<select id="query_by_mark" class="slt-sty">
							<option value="all">全部</option>
							<%for(MarkVO mark : markList){%><option value="<%=mark.getId()%>"><%=mark.getName()%></option><%}%>
						</select>
					</span>
					<span class="sear-row">
						<label class="f-ml10 f-mr5">硬盘状态</label>
						<select id="query_by_status" class="slt-sty">
							<option value="1">全部</option>
							<option value="2">正常</option>
							<option value="3">已过期</option>
						</select>
					</span>
					<span class="sear-row">
						<label class="f-ml10 f-mr5">昵称</label> 
						<input type="text" id="accountname" class="messager-input" />
					</span>
					<span class="sear-row">
						<label class="f-ml10 f-mr5">归属</label> 
						<input type="text" id="belongaccount" class="messager-input" />
					</span>
					<span class="sear-row">					
						<label class="f-ml10 f-mr5">硬盘名</label> 
						<input type="text" id="cloud_disk_name" class="messager-input" />
					</span>
					<span class="sear-row">
						<a class="easyui-linkbutton oper-btn-sty f-ml10" href="javascript:;" data-options="iconCls:'icon-search'" id="query_cloud_disk_btn">查询</a>
						<a class="easyui-linkbutton oper-btn-sty f-ml10" href="javascript:;" data-options="iconCls:'icon-redo'" id="clear_cloud_disk_btn">清除</a>
					</span>
				</div>
			</div>
		</div>
		<table id="cloud_disk_datagrid" class="easyui-datagrid" title="云硬盘管理" data-options="url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=cloudDiskService&method=getAllCloudDisk&user_id=<%=userId %>',queryParams: {},toolbar: '#toolbar',rownumbers: false,striped: true,remoteSort:false,fitColumns: true,pagination: true,pageSize: 20,view: createView(),onLoadSuccess: onLoadSuccess">
			<thead>
				<tr>
					<th data-options="field:'ck',checkbox:true"></th>
					<th data-options="field:'userName',sortable:true,formatter:userFormatter,width:100">所属用户</th>
					<th data-options="field:'accountname',width:100">用户昵称</th>
					<th data-options="field:'markname',width:100">标签</th>
					<th data-options="field:'belongaccount',width:100">归属</th>
					<th data-options="field:'name',sortable:true,width:150">硬盘名</th>
					<th data-options="field:'ip',sortable:true,width:100">IP</th>
					<th data-options="field:'disk',sortable:true,formatter:formatCapacity,width:60">硬盘大小</th>
					<th data-options="field:'region',sortable:true,formatter:formatRegion,width:60">地域</th>
					<th data-options="field:'status',sortable:true,width:60">状态</th>
					<th data-options="field:'createTime',sortable:true,width:150">创建时间</th>
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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/big.min.js"></script>
<script type="text/javascript">
window.name = "selfWin";

var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
ajax.async = false;

// 布局初始化
$("#cloud_disk_datagrid").height( $(document.body).height()-20);

function formatCapacity(val)
{
	return CapacityUtil.toCapacityLabel(val, 0);
}
function timeFormat(val, row)
{
	return $.formatDateString(val, "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss");
}
function userFormatter(value, row, index)
{
	return "<div row_index='"+index+"'>\
				<a href='javascript:void(0);' class='datagrid_row_linkbutton user-detail'>"+value+"</a>\
			</div>";
}
function formatStatus(val)
{  
	if(val == 1) {  
	    return "正常";  
	} else if(val == 2) {
	    return "停机";  
	} else {
	 	return "欠费";
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

function operateColumnFormatter(value, row, index)
{
	return "<div row_index='"+index+"'>\
				<a href='javascript:void(0);' class='datagrid_row_linkbutton delete_disk'>删除</a>\
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
	// 每一行的'详情'按钮
	$("a.delete_disk").click(function(){
		$this = $(this);
		rowIndex = $this.parent().attr("row_index");
		var data = $("#cloud_disk_datagrid").datagrid("getData");
		var id = data.rows[rowIndex].id;
		top.$.messager.confirm("确认", "确定删除?", function (r) {  
	        if (r) {   
				ajax.remoteCall("bean://cloudDiskService:deleteCloudDiskById",
					[ id ], 
					function(reply) {
						if (reply.status == "exception") {
							if(reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>"){
								top.$.messager.alert("警告","会话超时，请重新登录","warning",function(){
									top.location.reload();
								});
							}else{
								top.$.messager.alert("警告",reply.exceptionMessage,"warning",function(){
									
								});
							}
						} else {
							$('#cloud_disk_datagrid').datagrid('reload');
						}
					}
				);
	        }  
	    }); 
	});
	// 每一行的'用户详情'按钮
	$("a.user-detail").click(function(){
		$this = $(this);
		rowIndex = $this.parent().attr("row_index");
		var data = $("#cloud_disk_datagrid").datagrid("getData");
		var userId = data.rows[rowIndex].userId;
		top.showSingleDialog({
			url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=terminalUserService&method=userDetailPage",
			params: {
				"userId" : userId
			},
			onClose : function(data) {
				self.query();
			}
		});
	});
}

$(function(){
	// 查询
	$("#query_cloud_disk_btn").click(function(){
		var queryParams = {};
		queryParams.query_mark = $("#query_by_mark").combobox("getValue");
		queryParams.query_status = $("#query_by_status").combobox("getValue");
		queryParams.accountname = $("#accountname").val().trim();
		queryParams.belongaccount = $("#belongaccount").val().trim();
		queryParams.cloud_disk_name = $("#cloud_disk_name").val().trim();
		
		if($("#accountname").val()!=""){
			if($("#accountname").val().length>20){ 
			    top.$.messager.alert("警告","昵称最多允许20个字符","warning");
				return;
			}
		}	
		if($("#belongaccount").val()!=""){
			if($("#belongaccount").val().length>50){ 
			    top.$.messager.alert("警告","归属最多允许50个字符","warning");
				return;
			}
		}	
		if($("#cloud_disk_name").val()!=""){
			if($("#cloud_disk_name").val().length>50){ 
			    top.$.messager.alert("警告","硬盘名最多允许50个字符","warning");
				return;
			}
		}				
		$('#cloud_disk_datagrid').datagrid({
			"queryParams": queryParams
		});
	});
	//清除
	$("#clear_cloud_disk_btn").click(function(){
		$("#query_by_mark").combobox("setValue","all");
		$("#query_by_status").combobox("setValue","1");
		$("#accountname").val("");
		$("#belongaccount").val("");
		$("#cloud_disk_name").val("");
		
	});	
	/* //按标签查询
	$("#query_by_mark").change(function(){
		var queryParams = {};
		queryParams.cloud_disk_name = $("#cloud_disk_name").val().trim();
		queryParams.query_status = $("#query_by_status").val();
		queryParams.query_mark = $("#query_by_mark").val();
		$('#cloud_disk_datagrid').datagrid({
			"queryParams": queryParams
		});
	});
	
	$("#query_by_status").change(function(){
		var queryParams = {};
		queryParams.cloud_disk_name = $("#cloud_disk_name").val().trim();
		queryParams.query_status = $("#query_by_status").val();
		queryParams.query_mark = $("#query_by_mark").val();
		$('#cloud_disk_datagrid').datagrid({
			"queryParams": queryParams
		});
	}); */
	
	$('#cloud_disk_name').bind('keypress',function(event){
        if(event.keyCode == "13")    
        {
        	$("#query_cloud_disk_btn").click();
        }
    });
	
	// 删除VPC
	$("#del_cloud_disk_btn").click(function() {
		var rows = $('#cloud_disk_datagrid').datagrid('getSelections');
		if (rows == null || rows.length == 0)
		{
			top.$.messager.alert("警告","未选择删除项","warning");
			return;
		}
		var ids = rows.joinProperty("id");
		console.info(ids);
		top.$.messager.confirm("确认", "确定删除?", function (r) {  
	        if (r) {   
				ajax.remoteCall("bean://cloudDiskService:deleteCloudDiskByIds",
					[ ids ], 
					function(reply) {
						if (reply.status == "exception") {
							if(reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>"){
								top.$.messager.alert("警告","会话超时，请重新登录","warning",function(){
									top.location.reload();
								});
							}else{
								top.$.messager.alert("警告",reply.exceptionMessage,"warning",function(){
									
								});
							}
						} else {
							$('#cloud_disk_datagrid').datagrid('reload');
						}
					}
				);
	        }  
	    }); 
	});
	//导出数据
	$("#export_data_btn").click(function() {
		var queryParams = "";
		queryParams +="query_mark="+$("#query_by_mark").combobox("getValue");
		queryParams +="&query_status="+$("#query_by_status").combobox("getValue");
		queryParams +="&accountname="+$("#accountname").val().trim();
		queryParams +="&belongaccount="+$("#belongaccount").val().trim();
		queryParams +="&cloud_disk_name="+$("#cloud_disk_name").val().trim();
		
		if($("#accountname").val()!=""){
			if($("#accountname").val().length>20){ 
			    top.$.messager.alert("警告","昵称最多允许20个字符","warning");
				return;
			}
		}	
		if($("#belongaccount").val()!=""){
			if($("#belongaccount").val().length>50){ 
			    top.$.messager.alert("警告","归属最多允许50个字符","warning");
				return;
			}
		}	
		if($("#cloud_disk_name").val()!=""){
			if($("#cloud_disk_name").val().length>50){ 
			    top.$.messager.alert("警告","硬盘名最多允许50个字符","warning");
				return;
			}
		}	
		top.$.messager.confirm("确认", "确定导出？", function (r) {  
	        if (r) {   
				window.location.href= "<%=request.getContextPath()%>/cloudDiskExportData.do?"+queryParams;
	        }  
	    });   
	});	
});
</script>
</body>
</html>