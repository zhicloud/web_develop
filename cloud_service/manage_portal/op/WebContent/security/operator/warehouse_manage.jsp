<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));	
%>
<!DOCTYPE html>
<!-- warehouse_manage.jsp -->
<html>
<head>
	<meta charset="UTF-8" />
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
	<title>运营商管理员 - 仓库类型管理</title>
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
					<a class="easyui-linkbutton oper-btn-sty" href="javascript:;" data-options="plain:true,iconCls:'icon-add'"  id="add_warehouse_btn">新增库存类型</a> 
					<a class="easyui-linkbutton oper-btn-sty f-ml10" href="javascript:;" data-options="plain:true,iconCls:'icon-remove'" id="del_warehouse_btn">删除</a>
				</div>
				<div style="display: table-cell; text-align: right;">
					<span class="sear-row">
						<label class="f-mr5">系统镜像</label>
						<input type="text" id="description" class="messager-input" />
					</span>
					<span class="sear-row">
						<a class="easyui-linkbutton oper-btn-sty f-ml10" href="javascript:;" data-options="iconCls:'icon-search'" id="query_warehouse_btn">查询</a>
					</span>
				</div>
			</div>
		</div>
		<table id="warehouse_datagrid" class="easyui-datagrid" title="仓库类型管理" data-options="url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=cloudHostWarehouseService&method=queryWarehouse',queryParams: {},toolbar: '#toolbar',rownumbers: false,striped: true,remoteSort:false,fitColumns: true,pagination: true,pageList: [10, 20, 50, 100, 200],pageSize: 20,view: createView(),onLoadSuccess: onLoadSuccess()">
			<thead>
				<tr>
					<th data-options="field:'ck',checkbox:true"></th>
					<th data-options="field:'sysImageName',width:100">系统镜像</th>
					<th data-options="field:'description',width:100">描述</th>
					<th data-options="field:'region',formatter:formatRegion,width:100">地域</th>
					<th data-options="field:'totalAmount',width:100">库存总量</th>
					<th data-options="field:'remainAmount',width:100">剩余库存</th>
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
<script type="text/javascript" src="<%=request.getContextPath()%>/js/big.min.js"></script>
<script type="text/javascript">
	window.name = "selfWin";
	
	var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
		ajax.async = false;
		

		// 布局初始化
		$("#warehouse_datagrid").height( $(document.body).height()-20);
		
// 		function formatDataDisk(val)	
// 		{   
// 			return CapacityUtil.toCapacityLabel(val,0);
// 		} 
		
		function operateColumnFormatter(value, row, index)
		{
			return "<div row_index='"+index+"'>\
						<a href='javascript:;' class='datagrid_row_linkbutton modify_btn'>修改</a>\
						<a href='javascript:;' class='datagrid_row_linkbutton details_btn'>详情</a>\
					</div>";
		}
		function formatRegion(val, row)
		{  
			if(val == 1){  
			    return "广州";  
			}else if(val == 2) {
			    return "成都";  
			}else if(val == 3){
				return "北京";
			}else{
			 	return "香港";
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
				var data = $("#warehouse_datagrid").datagrid("getData");
				var id = data.rows[rowIndex].id;
				top.showSingleDialog({
					url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostWarehouseService&method=modPage&warehouseId="+encodeURIComponent(id),
				onClose: function(data){
					$('#warehouse_datagrid').datagrid('reload');
				}
			});
		});
		// 每一行的'详情'按钮
		$("a.details_btn").click(function(){
			var $this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#warehouse_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].id;
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostWarehouseService&method=warehouseDetailPage&warehouseId="+encodeURIComponent(id),
				onClose: function(data){
					$('#warehouse_datagrid').datagrid('reload');
				}
			});
		});
	}
	
	$(function(){
		// 查询
		$("#query_warehouse_btn").click(function(){
			var queryParams = {};
			queryParams.description = $("#description").val().trim();
			$('#warehouse_datagrid').datagrid({
				"queryParams": queryParams
			});
		});
		// 添加库存类型
		$("#add_warehouse_btn").click(function(){
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostWarehouseService&method=addPage",
				onClose: function(data){
					$('#warehouse_datagrid').datagrid('reload');
				}
			});
		});
		// 删除库存类型
		$("#del_warehouse_btn").click(function() {
			var rows = $('#warehouse_datagrid').datagrid('getSelections');
			if (rows == null || rows.length == 0)
			{
				top.$.messager.alert("警告","未选择删除项","warning");
				return;
			}
			var ids = rows.joinProperty("id");
			top.$.messager.confirm("确认", "确定删除?", function (r) {  
		        if (r) {    
					ajax.remoteCall("bean://cloudHostWarehouseService:deleteWarehouseByIds",
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
								$('#warehouse_datagrid').datagrid('reload');
							}
						}
					);
		        }  
		    });   
		});
		
		$('#account').bind('keypress',function(event){
	        if(event.keyCode == "13")    
	        {
	        	$("#query_warehouse_btn").click();
	        }
	    });
	});
</script>
</body>
</html>