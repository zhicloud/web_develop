<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="com.zhicloud.op.vo.VpcBaseInfoVO"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	String vpcId = (String)request.getAttribute("vpcId");
	VpcBaseInfoVO vpcBaseInfoVO = (VpcBaseInfoVO)request.getAttribute("vpc");
%>
<!DOCTYPE html>
<!-- exported_export_manage.jsp -->
<html>
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		
		<title>运营商管理员 - 专属云管理</title>
		
	</head>
	<body>
<div id="exported_dlg_container">
	<div id="exported_dlg" class="easyui-dialog" title="主机列表-[<%=vpcBaseInfoVO.getDisplayName() %>]"
		style="width:600px; height:600px; padding:10px;" 
		data-options="
			iconCls: 'icon-print',
			buttons: '#exported_dlg_buttons',
			modal: true,
			onMove:_exported_dlg_scope_.onMove,
			onClose: function(){
				$(this).dialog('destroy');
			},
			onDestroy: function(){
				delete _exported_dlg_scope_;
			}
		">
		<form id="big_form"  method="post">
			<table id="exported_datagrid" class="easyui-datagrid"
				style=""
				data-options="
					url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=vpcService&method=getAllHostForVpcId&id=<%=vpcId %>',
					queryParams: {},
					rownumbers: true,
					striped: true,
					remoteSort:false,
					fitColumns: true,
					pagination: true,
					pageList: [10, 20, 50, 100, 200],
					pageSize: 10,
					view: createView()
				">
				<thead>
					<tr>
						<th data-options="field:'hostName',width:50" >主机名</th>
						<th data-options="field:'cpuCore',width:50" formatter="formatCpuCore" sortable=true>CPU核心数量</th>
						<th data-options="field:'memory',width:50" formatter="formatCapacity" sortable=true>内存</th>
						<th data-options="field:'sysDisk',width:50" formatter="formatCapacity" sortable=true>系统磁盘</th>
						<th data-options="field:'dataDisk',width:50" formatter="formatCapacity" sortable=true>数据磁盘</th>
						<th data-options="field:'bandwidth',width:50" formatter="formatFlow" sortable=true>网络带宽</th>
						<th data-options="field:'innerMonitorAddr',width:50">内部监控地址</th>
						<th data-options="field:'outerMonitorAddr',width:50">外部监控地址</th>
					</tr>
				</thead>
			</table>
		</form>
		</div>

	<div id="exported_dlg_buttons">
		<a href="javascript:" class="easyui-linkbutton"
			id="exported_dlg_close_btn"> &nbsp;关&nbsp;闭&nbsp; </a>
	</div>
</div>
	</body>
	
	<script type="text/javascript">
	window.name = "selfWin";
	
	var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
	ajax.async = false;
	// 布局初始化
	$("#exported_datagrid").height(500);
	
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
	
	function timeFormat(val, row)
	{
		return $.formatDateString(val, "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss");
	}
	
	function formatCpuCore(val)
	{
		return val+"核";
	}
	function formatCapacity(val)
	{
		return CapacityUtil.toCapacityLabel(val, 0);
	}
	function formatFlow(val)
	{
		return FlowUtil.toFlowLabel(val, 0);
	}
		
		

	
	var _exported_dlg_scope_ = new function(){
		
		// 关闭
		self.close = function() {
			$("#exported_dlg").dialog("close");
		};
		
		// 初始化
		$(document).ready(function(){
			// 关闭
			$("#exported_dlg_close_btn").click(function() {
				self.close();
			});
		});
		
	};
	</script>
</html>