<%@page import="com.zhicloud.op.vo.SysGroupVO"%>
<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	String warehouseId = (String)request.getAttribute("warehouseId");
%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/popup.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/metro/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css" /> 

<div id="warehouse_detail_dlg_container">

	<div id="warehouse_detail_dlg" class="easyui-dialog" title="库存类型详情"
		style="width:680px; height:550px;"
		data-options="
			iconCls: 'icon-add',
 			buttons: '#warehouse_detail_dlg_buttons',
			modal: true,
			onMove: _warehouse_detail_dlg_scope_.onMove,
			onClose: function(){
				$(this).dialog('destroy');
			},
			onDestroy: function(){
				JsUtil.doDelete(_warehouse_detail_dlg_scope_);
			}
		">
		<form id="big_form"  method="post">
	        <input name="warehouse_id" id="warehouse_id" type="hidden" value=""/>
			<table id="warehouse_detail_datagrid" class="easyui-datagrid"
				style="height:514px;border:1px solid #ccc;"
				 data-options="
					url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=cloudHostWarehouseService&method=queryWarehouseDetail&warehouse_id=<%=warehouseId%>',
					fitColumns: true,
					rownumbers: true,
					striped: true,
					view: _warehouse_detail_dlg_scope_.createView(),
					onLoadSuccess: _warehouse_detail_dlg_scope_.onLoadSuccess
					">
				<thead>
					<tr>
						<th data-options="checkbox:true"></th>
						<th field="hostName" width="200px">云主机</th>
						<th field="createTime" width="100px" formatter="_warehouse_detail_dlg_scope_.timeFormatter">创建时间</th>
						<th field="status" formatter="_warehouse_detail_dlg_scope_.formatStatus" width="70px">状态</th>
						<th field="operate" formatter="_warehouse_detail_dlg_scope_.warehouseDetailColumnFormatter">操作</th>
					</tr>
				</thead>
			</table>
		</form>
	</div>
</div>

<script type="text/javascript">

//====================check end================
var _warehouse_detail_dlg_scope_ = new function(){
	
	var self = this;
	

	self.onMove = function(){
		var thisId = "#warehouse_detail_dlg";
		var topValue = $(thisId).offset().top;
		var leftValue = $(thisId).offset().left;
		if(topValue==0){
			topValue = 30;
		}
		if(topValue<30){
			$(thisId).dialog('move',{
				left:leftValue,
				top:30
			});
			return;
		}
		if(leftValue>1315){
			$(thisId).dialog('move',{
				left:1300,
				top:topValue
			});
			return;
		}
		if(topValue>600){
			$(thisId).dialog('move',{
				left:leftValue,
				top:570
			});
			return;
		}
	};
	

	self.createView = function(){
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
	};
	

	self.warehouseDetailColumnFormatter = function(value, row, index)
	{
		var status = $("#warehouse_detail_datagrid").datagrid("getData").rows[index].status;
		if(status==4){
			return "<div row_index='"+index+"'>\
			<a href='#' class='datagrid_row_linkbutton detail_btn'>详情</a>\
			</div>";
		}
		return "<div row_index='"+index+"'>\
					<a href='#' class='datagrid_row_linkbutton detail_btn'>详情</a>\
					<a href='#' class='datagrid_row_linkbutton delete_btn'>删除</a>\
				</div>";
	};



	self.timeFormatter = function(val, row)
	{ 
		if( val!=null && val.length>0 )
		{ 
		    return $.formatDateString(val, "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss");
		}
		else
		{
			return "";
		}
	};
	

	self.formatStatus = function(val, row)
	{  
		if(val == 1)
		{  
		    return "未处理";  
		}
		else if(val == 2)
		{
		    return "创建失败";  
		}
		else if(val == 3)
		{
		 	return "创建成功未分配";
		}
		else if(val == 4)
		{
			return "创建成功已分配";
		}
		else
		{
			return "云主机创建中";
		}
	};
	
	
	self.onLoadSuccess = function()
	{
		// 每一行的'详情'按钮
		$("a.detail_btn").click(function(){
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#warehouse_detail_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].hostId;
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=cloudHostViewDetailPage&cloudHostId="+encodeURIComponent(id),
				onClose: function(data){
					$('#warehouse_detail_datagrid').datagrid('reload');
				}
			});
		});
		// 每一行的'删除'按钮
		$("a.delete_btn").click(function() {
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#warehouse_detail_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].hostId;
			top.$.messager.confirm("确认", "确定删除?", function (r) {  
		        if (r) {   
					ajax.remoteCall("bean://cloudHostWarehouseService:deleteCloudHostById",
						[ id ], 
						function(reply) {
							if (reply.status == "exception") {
								top.$.messager.alert("警告","会话超时，请重新登录","warning",function(){
									top.location.reload();
								});
							} else {
								$('#warehouse_detail_datagrid').datagrid('reload');
							}
						}
					);
		        }  
		    }); 
		});
	};
	
	// 保存
	self.save = function() {
		var result = null;
		var formData = $.formToBean(warehouse_detail_dlg_form);
		ajax.remoteCall("bean://cloudHostWarehouseService:", 
			[ formData ],
			function(reply) {
				result = new String(reply.result.message);
				if (reply.status == "exception") {
					top.$.messager.alert('警告',reply.exceptionMessage,'warning');
				} else if (reply.result.status == "success") {
					var data = $("#warehouse_detail_dlg_container").parent().prop("_data_");
					$("#warehouse_detail_dlg").dialog("close");
					data.onClose(reply.result);
				} else {
					if(result.endsWith("账号")){
						$("#operator-tip-phone").html("<b>"+reply.result.message+"</b>");
					}else{
						$("#operator-tip-account").html("<b>"+reply.result.message+"</b>");
					}
				}
			}
		);
	};

	// 关闭
	self.close = function() {
		$("#warehouse_detail_dlg").dialog("close");
	};

	//--------------------------
	
	// 初始化
	$(document).ready(function(){
		// 保存
		$("#warehouse_detail_dlg_save_btn").click(function() {
			if( checkGroupId() && checkAccount()   && checkEmail() && checkPhone() ){
				self.save();
			}
		});
		// 关闭
		$("#warehouse_detail_dlg_close_btn").click(function() {
			self.close();
		});
	});
};
</script>



