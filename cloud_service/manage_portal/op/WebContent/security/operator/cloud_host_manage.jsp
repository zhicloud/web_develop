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
%>
<!DOCTYPE html>
<!-- cloud_host_manage.jsp -->
<html>
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		
		<title>运营商 - 云服务器管理</title>
		
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
		<style type="text/css">
		#cloud_host_datagrid {
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
		.messager-input {
		  width: 100px;
		  padding: 2px 2px 3px 2px;
		  height:16px;
		  border: 1px solid #95B8E7;
		  margin:2px;
		}
		</style>
	</head>
	<body style="visibility:hidden;">
		<form id="big_form"  method="post">
	
			<table id="cloud_host_datagrid" class="easyui-datagrid" title="用户云服务器管理"
				data-options="
					url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=cloudHostService&method=queryCloudHost',
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
						<th field="hostanddisplay" formatter="formatdetail" width="200px" sortable=true>云服务器名/用户定义名</th> 
						<th field="cpuCore" formatter="formatCpuCore" width="40px" sortable=true>CPU</th>
						<th field="memory" formatter="formatCapacity" width="40px" sortable=true>内存</th>
						<th field="sysDisk" width="60px" formatter="formatCapacity" sortable=true>系统磁盘</th>
						<th field="dataDisk" width="60px" formatter="formatCapacity" sortable=true>数据磁盘</th>
						<th field="bandwidth" formatter="formatFlow" width="60px" sortable=true>网络带宽</th>
						<th field="sysImageNameOld" width="150px">系统镜像</th>
						<th field="outerandinnerip" width="120px">内网IP/外网IP</th>
						<th field="monthlyPrice" width="60px">每月资费</th>
						<th field="account_balance" width="60px">用户余额</th>
						<th field="region" formatter="formatRegion" width="60px" >地域</th>
						<th field="belong_accountandusername" formatter="userFormatter"  width="100px" >所属用户/手机</th>
						<th field="username"  width="80px" >别名</th>
						<th field="markname"  width="80px" >用户类型</th>
						<th field="createTime" formatter="formatCreateTime" width="120px" >创建时间</th>
						<th field="description" width="100px" formatter="formatRemark">备注</th>
						<th field="inactivateTimeText" width="120px">状态</th>
						<!-- 
						<th field="userAccount" formatter="userFormatter" width="100px" sortable=true>所属用户</th>
						<th field="username"  width="100px" >用户名</th>
						<th field="phone"  width="80px" >手机</th>
						<th field="email"  width="100px" >邮箱</th>
						<th field="account_balance"  width="80px" >账户余额</th>
						<th field="markname"  width="80px" >用户类型</th>
						
						<th field="createTime" formatter="formatCreateTime" width="120px" >账户创建时间</th>
						<th field="belong_account"  width="80px" >归属</th>


						
						<th field="innerMonitorAddr" width="60px">内部监控地址</th>
						<th field="outerMonitorAddr" width="60px">外部监控地址</th> -->
						
						<th field="operate" formatter="operateColumnFormatter" width="240px">操作</th>
					</tr>
				</thead>
			</table>
	
			<div id="toolbar" style="padding: 3px;">
				<div style="display: table; width: 100%;">
					<div style="margin-bottom:5px;">
						<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" id="del_cloud_host_btn">删除</a>
						<a href="#" class="easyui-linkbutton" iconCls="icon-print" plain="true" id="export_data_btn">导出数据</a>
					</div>
					<div style="margin-left:5px;">
						<span style="position: relative; top: 2px;">用户类型</span>
						<select id="query_by_mark" style="position: relative; top: 2px;">
							<option value="all">全部</option>
							<%
							for( MarkVO mark : markList ) 
							{ 
				  			%> 
				            <option value="<%=mark.getId()%>"><%=mark.getName()%></option>
				            <%
								} 
						    %>
						</select>
						
						<span style="position: relative; top: 2px;">状态</span>
						<select id="query_by_status" style="position: relative; top: 2px;">
						   <option value="all">全部</option>
						   <option value="1">关机</option>
						   <option value="2">运行</option>
						   <option value="3">告警</option>
						   <option value="4">故障</option>
						   <option value="5">停机</option>
						   <option value="7">欠费</option>
						   <option value="8">已过期</option>
						   <option value="9">创建中</option>
<!-- 						   <option value="1">全部</option>
						   <option value="2">正常</option>
						   <option value="3">已过期</option> -->
						</select>
						
						<span style="position: relative; top: 2px;">地域</span>
						<select id="region" style="position: relative; top: 2px;">
						   <option value="">全部</option>
						   <option value="1">广州</option>
						   <option value="2">成都</option>
						   <option value="4">香港</option>
						</select>
						<span style="position: relative; top: 2px;">云服务器名</span> 
						<input type="text" id="host_name" class="messager-input" /> 
						<span style="position: relative; top: 2px;">别名</span> 
						<input type="text" id="username" class="messager-input" /> 
						<span style="position: relative; top: 2px;">所属用户/手机</span> 
						<input type="text" id="belongaccount" class="messager-input" />
						<span style="position: relative; top: 2px;">IP</span> 
						<input type="text" id="outer_ip" class="messager-input" /> 
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
    <script type="text/javascript" src="<%=request.getContextPath()%>/js/big.min.js"></script>
	<script type="text/javascript">
		
	window.name = "selfWin";
	
	var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
	ajax.async = false;

	// 布局初始化
	$("#cloud_host_datagrid").height( $(document.body).height());
	
	function formatCpuCore(val)
	{
		return val+"核";
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
	function formatCreateTime(val, row)
	{
		if(val==null||val==""){
			return "";
		}
		return $.formatDateString(val, "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss");
	}
	function formatRemark(val, row,index){
		if(val==""||val==null){
			val = "无";
		}
		return "<div row_index='"+index+"'>\
			<a href='javascript:void(0);' class='datagrid_row_linkbutton description_host_btn'>"+val+"</a>\
		</div>";
	}
	function userFormatter(value, row, index)
	{
		return "<div row_index='"+index+"'>\
					<a href='javascript:void(0);' class='datagrid_row_linkbutton user-detail'>"+value+"</a>\
				</div>";
	}
	
	function formatCapacity(val)
	{
		return CapacityUtil.toCapacityLabel(val, 0);
	}

	function formatdetail(value, row, index){
		return "<div row_index='"+index+"'>\
		<a href='#' class='datagrid_row_linkbutton view-detail'>"+value+"</a>\
		</div>";
	}
	function formatFlow(val)
	{
		return FlowUtil.toFlowLabel(val, 0);
	}
	
	function formatStartupStatus(val)
	{  
		if(val == 1) {  
		    return "是";  
		} else {
		 	return "否";
		}
	}
	
	function formatRuningStatus(val)
	{  
		if(val == 1) {  
		    return "启动";  
		} else {
		 	return "关机";
		}
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
	
	function operateColumnFormatter(value, row, index)
	{
		var status = $("#cloud_host_datagrid").datagrid("getData").rows[index].status;
		var realHostId = $("#cloud_host_datagrid").datagrid("getData").rows[index].realHostId;
		var runningStatus = $("#cloud_host_datagrid").datagrid("getData").rows[index].runningStatus;
		var processStatus = $("#cloud_host_datagrid").datagrid("getData").rows[index].processStatus;
		var region = $("#cloud_host_datagrid").datagrid("getData").rows[index].region;
		
		if(status==2){
			return "<div row_index='"+index+"'>\
			<a href='#' class='datagrid_row_linkbutton start_host_btn' style=\"color:gray;\" >开机</a>\
			<a href='#' class='datagrid_row_linkbutton shutdown_host_btn' style=\"color:gray;\">关机</a>\
			<a href='#' class='datagrid_row_linkbutton halt_host_btn' style=\"color:gray;\">强制关机</a>\
			<a href='#' class='datagrid_row_linkbutton restart_host_btn' style=\"color:gray;\">重启</a>\
			<a href='#' class='datagrid_row_linkbutton force_restart_host_btn' style=\"color:gray;\">强制重启</a><br>\
			<a href='#' class='datagrid_row_linkbutton inactivate_host_btn' style=\"color:gray;\">停用</a>\
			<a href='#' class='datagrid_row_linkbutton reinactivate_host_btn'>恢复</a>\
			<a href='#' class='datagrid_row_linkbutton open-port' style=\"color:gray;\">端口配置</a>\
			</div>";
		}
		if(processStatus != null && (processStatus == 0 || processStatus == 3 || processStatus==2)){
			return "<div row_index='"+index+"'>\
			<a href='#' class='datagrid_row_linkbutton start_host_btn' style=\"color:gray;\">开机</a>\
			<a href='#' class='datagrid_row_linkbutton shutdown_host_btn' style=\"color:gray;\">关机</a>\
			<a href='#' class='datagrid_row_linkbutton halt_host_btn' style=\"color:gray;\">强制关机</a>\
			<a href='#' class='datagrid_row_linkbutton restart_host_btn' style=\"color:gray;\">重启</a>\
			<a href='#' class='datagrid_row_linkbutton force_restart_host_btn' style=\"color:gray;\">强制重启</a><br>\
			<a href='#' class='datagrid_row_linkbutton inactivate_host_btn' style=\"color:gray;\">停用</a>\
			<a href='#' class='datagrid_row_linkbutton reinactivate_host_btn' style=\"color:gray;\">恢复</a>\
			<a href='#' class='datagrid_row_linkbutton open-port' style=\"color:gray;\">端口配置</a>\
			</div>";		
		}
		if(realHostId==null){
			return "<div row_index='"+index+"'>\
			<a href='#' class='datagrid_row_linkbutton start_host_btn' style=\"color:gray;\">开机</a>\
			<a href='#' class='datagrid_row_linkbutton shutdown_host_btn' style=\"color:gray;\">关机</a>\
			<a href='#' class='datagrid_row_linkbutton halt_host_btn' style=\"color:gray;\">强制关机</a>\
			<a href='#' class='datagrid_row_linkbutton restart_host_btn' style=\"color:gray;\">重启</a>\
			<a href='#' class='datagrid_row_linkbutton force_restart_host_btn' style=\"color:gray;\">强制重启</a><br>\
			<a href='#' class='datagrid_row_linkbutton inactivate_host_btn' style=\"color:gray;\">停用</a>\
			<a href='#' class='datagrid_row_linkbutton reinactivate_host_btn' style=\"color:gray;\">恢复</a>\
			<a href='#' class='datagrid_row_linkbutton open-port' style=\"color:gray;\">端口配置</a>\
			</div>";
		}
		if(status==1 && runningStatus==1 && region ==2){
			return "<div row_index='"+index+"'>\
			<a href='#' class='datagrid_row_linkbutton start_host_btn'>开机</a>\
			<a href='#' class='datagrid_row_linkbutton shutdown_host_btn' style=\"color:gray;\">关机</a>\
			<a href='#' class='datagrid_row_linkbutton halt_host_btn' style=\"color:gray;\">强制关机</a>\
			<a href='#' class='datagrid_row_linkbutton restart_host_btn' style=\"color:gray;\">重启</a>\
			<a href='#' class='datagrid_row_linkbutton force_restart_host_btn' style=\"color:gray;\">强制重启</a><br>\
			<a href='#' class='datagrid_row_linkbutton inactivate_host_btn'>停用</a>\
			<a href='#' class='datagrid_row_linkbutton reinactivate_host_btn' style=\"color:gray;\">恢复</a>\
			<a href='#' class='datagrid_row_linkbutton open-port'>端口配置</a>\
			</div>";
		}
		if(status==1 && runningStatus==1){
			return "<div row_index='"+index+"'>\
			<a href='#' class='datagrid_row_linkbutton start_host_btn'>开机</a>\
			<a href='#' class='datagrid_row_linkbutton shutdown_host_btn' style=\"color:gray;\">关机</a>\
			<a href='#' class='datagrid_row_linkbutton halt_host_btn' style=\"color:gray;\">强制关机</a>\
			<a href='#' class='datagrid_row_linkbutton restart_host_btn' style=\"color:gray;\">重启</a>\
			<a href='#' class='datagrid_row_linkbutton force_restart_host_btn' style=\"color:gray;\">强制重启</a><br>\
			<a href='#' class='datagrid_row_linkbutton inactivate_host_btn'>停用</a>\
			<a href='#' class='datagrid_row_linkbutton reinactivate_host_btn' style=\"color:gray;\">恢复</a>\
			<a href='#' class='datagrid_row_linkbutton open-port' style=\"color:gray;\">端口配置</a>\
			</div>";
		}
		if(status==1 && runningStatus==2 && region == 2){
			return "<div row_index='"+index+"'>\
			<a href='#' class='datagrid_row_linkbutton start_host_btn'>开机</a>\
			<a href='#' class='datagrid_row_linkbutton shutdown_host_btn'>关机</a>\
			<a href='#' class='datagrid_row_linkbutton halt_host_btn'>强制关机</a>\
			<a href='#' class='datagrid_row_linkbutton restart_host_btn'>重启</a>\
			<a href='#' class='datagrid_row_linkbutton force_restart_host_btn'>强制重启</a><br>\
			<a href='#' class='datagrid_row_linkbutton inactivate_host_btn'>停用</a>\
			<a href='#' class='datagrid_row_linkbutton reinactivate_host_btn' style=\"color:gray;\">恢复</a>\
			<a href='#' class='datagrid_row_linkbutton open-port'>端口配置</a>\
			</div>";
		}
		if(status==1 && runningStatus==2 ){
			return "<div row_index='"+index+"'>\
			<a href='#' class='datagrid_row_linkbutton start_host_btn' style=\"color:gray;\">开机</a>\
			<a href='#' class='datagrid_row_linkbutton shutdown_host_btn'>关机</a>\
			<a href='#' class='datagrid_row_linkbutton halt_host_btn'>强制关机</a>\
			<a href='#' class='datagrid_row_linkbutton restart_host_btn'>重启</a>\
			<a href='#' class='datagrid_row_linkbutton force_restart_host_btn'>强制重启</a><br>\
			<a href='#' class='datagrid_row_linkbutton inactivate_host_btn'>停用</a>\
			<a href='#' class='datagrid_row_linkbutton reinactivate_host_btn' style=\"color:gray;\">恢复</a>\
			<a href='#' class='datagrid_row_linkbutton open-port' style=\"color:gray;\">端口配置</a>\
			</div>";
		}
		return "<div row_index='"+index+"'>\
		<a href='#' class='datagrid_row_linkbutton start_host_btn' style=\"color:gray;\">开机</a>\
		<a href='#' class='datagrid_row_linkbutton shutdown_host_btn' style=\"color:gray;\">关机</a>\
		<a href='#' class='datagrid_row_linkbutton halt_host_btn' style=\"color:gray;\">强制关机</a>\
		<a href='#' class='datagrid_row_linkbutton restart_host_btn' style=\"color:gray;\">重启</a>\
		<a href='#' class='datagrid_row_linkbutton force_restart_host_btn' style=\"color:gray;\">强制重启</a><br>\
		<a href='#' class='datagrid_row_linkbutton inactivate_host_btn' style=\"color:gray;\">停用</a>\
		<a href='#' class='datagrid_row_linkbutton reinactivate_host_btn' style=\"color:gray;\">恢复</a>\
		<a href='#' class='datagrid_row_linkbutton open-port' style=\"color:gray;\">端口配置</a>\
		</div>";
		
 		/* if(status==2){
			return "<div row_index='"+index+"'> \<a href='#' class='datagrid_row_linkbutton view-detail'>查看详情</a> \<a href='#' class='datagrid_row_linkbutton reinactivate_host_btn'>恢复</a> \<a href='#' class='datagrid_row_linkbutton description_host_btn'>备注</a>\</div>";
		}
		if(processStatus != null && (processStatus == 0 || processStatus == 3)){
			return "<div row_index='"+index+"'> \<a href='#' class='datagrid_row_linkbutton view-detail'>查看详情</a> \<a href='#' class='datagrid_row_linkbutton description_host_btn'>备注</a>\</div>";
		}
		if(processStatus!=null && processStatus==2){
			return "<div row_index='"+index+"'> \<a href='#' class='datagrid_row_linkbutton view-detail'>查看详情</a> \<a href='#' class='datagrid_row_linkbutton description_host_btn'>备注</a>\</div>";
		}
		if(realHostId==null){
			return "<div row_index='"+index+"'>\<a href='#' class='datagrid_row_linkbutton view-detail'>查看详情</a> \<a href='#' class='datagrid_row_linkbutton description_host_btn'>备注</a>";
		}
		if(status==1 && runningStatus==1 && region ==2){
			return "<div row_index='"+index+"'>\<a href='#' class='datagrid_row_linkbutton view-detail'>查看详情</a>\
			\
			<a href='#' class='datagrid_row_linkbutton start_host_btn'>开机</a>\ <a href='#' class='datagrid_row_linkbutton inactivate_host_btn'>停用</a>\ <a href='#' class='datagrid_row_linkbutton open-port'>端口配置</a>\ <a href='#' class='datagrid_row_linkbutton description_host_btn'>备注</a>\</div>";
		}
		if(status==1 && runningStatus==1){
			return "<div row_index='"+index+"'>\<a href='#' class='datagrid_row_linkbutton view-detail'>查看详情</a>\
			\
			<a href='#' class='datagrid_row_linkbutton start_host_btn'>开机</a>\ <a href='#' class='datagrid_row_linkbutton inactivate_host_btn'>停用</a>\ <a href='#' class='datagrid_row_linkbutton description_host_btn'>备注</a>\</div>";
		}
		if(status==1 && runningStatus==2 && region == 2){
			return "<div row_index='"+index+"'>\
			<a href='#' class='datagrid_row_linkbutton view-detail'>查看详情</a>\
			\
			<a href='#' class='datagrid_row_linkbutton shutdown_host_btn'>关机</a>\
			<a href='#' class='datagrid_row_linkbutton restart_host_btn'>重启</a>\
			<a href='#' class='datagrid_row_linkbutton force_restart_host_btn'>强制重启</a>\
			<a href='#' class='datagrid_row_linkbutton halt_host_btn'>强制关机</a>\
			<a href='#' class='datagrid_row_linkbutton inactivate_host_btn'>停用</a>\
			<a href='#' class='datagrid_row_linkbutton open-port'>端口配置</a>\
			<a href='#' class='datagrid_row_linkbutton description_host_btn'>备注</a>\
			\
			</div>";
		}
		if(status==1 && runningStatus==2 ){
			return "<div row_index='"+index+"'>\
			<a href='#' class='datagrid_row_linkbutton view-detail'>查看详情</a>\
			\
			<a href='#' class='datagrid_row_linkbutton shutdown_host_btn'>关机</a>\
			<a href='#' class='datagrid_row_linkbutton restart_host_btn'>重启</a>\
			<a href='#' class='datagrid_row_linkbutton force_restart_host_btn'>强制重启</a>\
			<a href='#' class='datagrid_row_linkbutton halt_host_btn'>强制关机</a>\
			<a href='#' class='datagrid_row_linkbutton inactivate_host_btn'>停用</a>\
			<a href='#' class='datagrid_row_linkbutton description_host_btn'>备注</a>\
			\
			</div>";
		}
		if(realHostId==null){
			return "<div row_index='"+index+"'> \<a href='#' class='datagrid_row_linkbutton view-detail'>查看详情</a> \<a href='#' class='datagrid_row_linkbutton description_host_btn'>备注</a>\</div>";
		} */
		
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
		// 每一行的'查看详情'按钮
		$("a.view-detail").click(function(){
			if($(this).css("color")=="gray"||$(this).css("color")=="rgb(128, 128, 128)"){
				return;
			}
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#cloud_host_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].id;
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=cloudHostViewDetailPage",
				params: {
					"cloudHostId" : id
				},
				onClose : function(data) {
					self.query();
				}
			});
		});
		// 每一行的'用户详情'按钮
		$("a.user-detail").click(function(){
			if($(this).css("color")=="gray"||$(this).css("color")=="rgb(128, 128, 128)"){
				return;
			}
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#cloud_host_datagrid").datagrid("getData");
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
		// 每一行的'查看详情'按钮
		$("a.description_host_btn").click(function(){
			if($(this).css("color")=="gray"||$(this).css("color")=="rgb(128, 128, 128)"){
				return;
			}
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#cloud_host_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].id;
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=modifyDescriptionPage",
				params: {
					"hostId" : id
				},
				onClose: function(data){
					$('#cloud_host_datagrid').datagrid('reload');
				}
			});
		});
		$("a.open-port").click(function(){
			if($(this).css("color")=="gray"||$(this).css("color")=="rgb(128, 128, 128)"){
				return;
			}
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#cloud_host_datagrid").datagrid("getData");
			if(data.rows[rowIndex].realHostId==null || data.rows[rowIndex].realHostId==''){
				top.$.messager.alert("警告", "云主机尚未创建成功", "warning");
			}
			var id = data.rows[rowIndex].id;
			top.showSingleDialog({
 				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=addPortPage",
				params: {
					"hostId" : id
				},
				onClose : function(data) {
					self.query();
				}
			}); 
		});
		//启动
		$("a.start_host_btn").click(function(){
			if($(this).css("color")=="gray"||$(this).css("color")=="rgb(128, 128, 128)"){
				return;
			}
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#cloud_host_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].id;
			top.$.messager.confirm("确认", "确定要启动该云主机[" + data.rows[rowIndex].hostName +"]吗？", function (r) { 
				if(r){
					ajax.remoteCall("bean://cloudHostService:startCloudHost",
						[ id ], 
						function(reply) {
							if (reply.status=="exception")
							{
								if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
								{
									top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
										window.location.reload();
									});
								}
								else 
								{
									top.$.messager.alert("警告", reply.exceptionMessage, "warning");
								}
							}
							else if(reply.result.status == "success")
							{
								$('#cloud_host_datagrid').datagrid('reload');
							}else{
								top.$.messager.alert("信息", reply.result.message, "info",function(){
									$('#cloud_host_datagrid').datagrid('reload');
								});
							}
						}
					);
				}
			});
		});
		//关机
		$("a.shutdown_host_btn").click(function(){
			if($(this).css("color")=="gray"||$(this).css("color")=="rgb(128, 128, 128)"){
				return;
			}
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#cloud_host_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].id;
			top.$.messager.confirm("确认", "确定要关闭该云主机[" + data.rows[rowIndex].hostName +"]吗？", function (r) { 
				if(r){
					ajax.remoteCall("bean://cloudHostService:stopCloudHost",
						[ id ], 
						function(reply) {
							if (reply.status=="exception")
							{
								if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
								{
									top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
										window.location.reload();
									});
								}
								else 
								{
									top.$.messager.alert("警告", reply.exceptionMessage, "warning");
								}
							}
							else if(reply.result.status == "success")
							{
								$('#cloud_host_datagrid').datagrid('reload');
							}else{
								top.$.messager.alert("信息", reply.result.message, "info",function(){
									$('#cloud_host_datagrid').datagrid('reload');
								});
							}
						}
					);
				}
			});
		});
		//强制关机
		$("a.halt_host_btn").click(function(){
			if($(this).css("color")=="gray"||$(this).css("color")=="rgb(128, 128, 128)"){
				return;
			}
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#cloud_host_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].id;
			top.$.messager.confirm("确认", "确定要强制关闭正在运行的云主机[" + data.rows[rowIndex].hostName +"]吗?", function (r) {  
		        if (r) { 
		        	ajax.remoteCall("bean://cloudHostService:haltCloudHost",
	    				[ id ], 
	    				function(reply) {
	    					if (reply.status=="exception")
	    					{
	    						if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
	    						{
	    							top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
	    								window.location.reload();
	    							});
	    						}
	    						else 
	    						{
	    							top.$.messager.alert("警告", reply.exceptionMessage, "warning");
	    						}
	    					}else if(reply.result.status == "success")
							{
								$('#cloud_host_datagrid').datagrid('reload');
							}
	    					else
	    					{
	    						top.$.messager.alert("信息", reply.result.message, "info",function(){
	    							$('#cloud_host_datagrid').datagrid('reload');
	    						});
	    					}
	    				}
	    			);
		        }  
		    });
		});
		//重启
		$("a.restart_host_btn").click(function(){
			if($(this).css("color")=="gray"||$(this).css("color")=="rgb(128, 128, 128)"){
				return;
			}
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#cloud_host_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].id;
			top.$.messager.confirm("确认", "确定要重启该云主机[" + data.rows[rowIndex].hostName +"]吗？", function (r) { 
				if(r){
					ajax.remoteCall("bean://cloudHostService:restartCloudHost", 
						[ id ],
						function(reply) {
							if( reply.status == "exception" )
							{
		 						if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
									{
										top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
											window.location.reload();
										});
									}
									else 
									{
										top.$.messager.alert("警告", reply.exceptionMessage, "warning",function(){
										});
									}
								}
								else if (reply.result.status == "success")
								{
									$('#cloud_host_datagrid').datagrid('reload');
								}
								else
								{
									top.$.messager.alert("信息", reply.result.message, "info",function(){
										$('#cloud_host_datagrid').datagrid('reload');
									});
								}
							}
						);
				}
			});
		});
		
		//重启
		$("a.force_restart_host_btn").click(function(){
			if($(this).css("color")=="gray"||$(this).css("color")=="rgb(128, 128, 128)"){
				return;
			}
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#cloud_host_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].id;
			top.$.messager.confirm("确认", "确定要强制重启该云主机[" + data.rows[rowIndex].hostName +"]吗？", function (r) { 
				if(r){
					ajax.remoteCall("bean://cloudHostService:forceRestartCloudHost", 
						[ id ],
						function(reply) {
							if( reply.status == "exception" )
							{
		 						if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
									{
										top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
											window.location.reload();
										});
									}
									else 
									{
										top.$.messager.alert("警告", reply.exceptionMessage, "warning",function(){
										});
									}
								}
								else if (reply.result.status == "success")
								{
									$('#cloud_host_datagrid').datagrid('reload');
								}
								else
								{
									top.$.messager.alert("信息", reply.result.message, "info",function(){
										$('#cloud_host_datagrid').datagrid('reload');
									});
								}
							}
						);
				}
			});
		});
		
		//停用
		$("a.inactivate_host_btn").click(function(){
			if($(this).css("color")=="gray"||$(this).css("color")=="rgb(128, 128, 128)"){
				return;
			}
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#cloud_host_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].id;
			top.$.messager.confirm("确认", "确定要停用该云主机[" + data.rows[rowIndex].hostName +"]吗？", function (r) { 
				if(r){
					ajax.remoteCall("bean://cloudHostService:inactivateCloudHost",
						[ id ], 
						function(reply) {
							if (reply.status=="exception")
							{
								if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
								{
									top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
										window.location.reload();
									});
								}
								else 
								{
									top.$.messager.alert("警告", reply.exceptionMessage, "warning");
								}
							}
							else if(reply.result.status == "success")
							{
								$('#cloud_host_datagrid').datagrid('reload');
							}else{
								top.$.messager.alert("信息", reply.result.message, "info",function(){
									$('#cloud_host_datagrid').datagrid('reload');
								});
							}
						}
					);
				}
			});
		});
		//恢复停用
		$("a.reinactivate_host_btn").click(function(){
			if($(this).css("color")=="gray"||$(this).css("color")=="rgb(128, 128, 128)"){
				return;
			}
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#cloud_host_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].id;
			top.$.messager.confirm("确认", "确定要恢复该云主机[" + data.rows[rowIndex].hostName +"]吗？", function (r) { 
				if(r){
					ajax.remoteCall("bean://cloudHostService:reactivateCloudHost",
						[ id ], 
						function(reply) {
							if (reply.status=="exception")
							{
								if( reply.errorCode=="<%=ErrorCode.ERROR_CODE_FAIL_TO_CALL_BEFORE_LOGIN%>" )
								{
									top.$.messager.alert("警告", "会话超时，请重新登录", "warning", function(){
										window.location.reload();
									});
								}
								else 
								{
									top.$.messager.alert("警告", reply.exceptionMessage, "warning");
								}
							}else if(reply.result.status == "success")
							{
								$('#cloud_host_datagrid').datagrid('reload');
							}
							else
							{
								top.$.messager.alert("信息", reply.result.message, "info",function(){
									$('#cloud_host_datagrid').datagrid('reload');
								});
							}
						}
					);
				}
			});
		});
	}
	
	$(function(){
		// 查询
		$("#query_cloud_host_btn").click(function(){
			var queryParams = {};
			queryParams.query_by_mark = $("#query_by_mark").val().trim();//.combobox("getValue");
			queryParams.query_by_status = $("#query_by_status").val().trim();//.combobox("getValue");
			queryParams.region = $("#region").val().trim();//.combobox("getValue");
			queryParams.host_name = $("#host_name").val().trim();
			queryParams.username = $("#username").val().trim();
			queryParams.belongaccount = $("#belongaccount").val().trim();
			queryParams.outerIp = $("#outer_ip").val().trim();
			if($("#host_name").val().trim()!=""){
				if($("#host_name").val().trim().length>50){ 
				    top.$.messager.alert("警告","主机名最多允许50个字符","warning");
					return;
				}
			}	
			if($("#username").val().trim()!=""){
				if($("#username").val().trim().length>20){ 
				    top.$.messager.alert("警告","昵称最多允许20个字符","warning");
					return;
				}
			}	
			if($("#belongaccount").val().trim()!=""){
				if($("#belongaccount").val().trim().length>50){ 
				    top.$.messager.alert("警告","归属最多允许50个字符","warning");
					return;
				}
			}	
			if($("#outer_ip").val().trim()!=""){
				if($("#outer_ip").val().trim().length>16){ 
				    top.$.messager.alert("警告","外网IP最多允许16个字符","warning");
					return;
				}
			}	
			$('#cloud_host_datagrid').datagrid({
				"queryParams": queryParams
			});
		});
		
		$("#clear_cloud_host_btn").click(function(){
			$("#query_by_mark").val("all");//.combobox("setValue","all");
			$("#query_by_status").val("all");//.combobox("setValue","1");
			$("#region").val("");//.combobox("setValue","");
			$("#host_name").val("");
			$("#username").val("");
			$("#belongaccount").val("");
			$("#outer_ip").val("");
		});
		//按标签查询
		/* $("#query_by_mark").change(function(){
			var queryStatus = {};
			queryStatus.query_status = $("#query_by_status").val();
			queryStatus.query_mark = $("#query_by_mark").val();
			queryStatus.region = $("#region").val();
			$('#cloud_host_datagrid').datagrid({
				"queryParams": queryStatus
			});
		});
		$("#query_by_status").change(function(){
			var queryStatus = {};
			queryStatus.query_status = $("#query_by_status").val();
			queryStatus.query_mark = $("#query_by_mark").val();
			queryStatus.region = $("#region").val();
			$('#cloud_host_datagrid').datagrid({
				"queryParams": queryStatus
			});
		});
		$("#region").change(function(){
			var queryStatus = {};
			queryStatus.query_status = $("#query_by_status").val();
			queryStatus.query_mark = $("#query_by_mark").val();
			queryStatus.region = $("#region").val();
			$('#cloud_host_datagrid').datagrid({
				"queryParams": queryStatus
			});
		}); */
		// 删除云主机
		$("#del_cloud_host_btn").click(function() {
			var rows = $('#cloud_host_datagrid').datagrid('getSelections');
			if (rows == null || rows.length == 0)
			{
				top.$.messager.alert("警告","未选择删除项","warning");
				return;
			}
			var ids = rows.joinProperty("id");
			top.$.messager.confirm("确认", "确定删除?", function (r) {  
		        if (r) {   
					ajax.remoteCall("bean://cloudHostService:operatorDeleteCloudHostByIds",
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
								$('#cloud_host_datagrid').datagrid('reload');
							}
						}
					);
		        }  
		    }); 
		});
		//导出数据
		$("#export_data_btn").click(function() {
			var queryParams = "";
			queryParams +="query_by_mark="+$("#query_by_mark").val().trim(); //.combobox("getValue");
			queryParams +="&query_by_status="+$("#query_by_status").val().trim(); //.combobox("getValue");
			queryParams +="&region="+$("#region").val().trim(); //.combobox("getValue");
			queryParams +="&host_name="+$("#host_name").val().trim();
			queryParams +="&username="+$("#username").val().trim();
			queryParams +="&belongaccount="+$("#belongaccount").val().trim();
			if($("#host_name").val().trim()!=""){
				if($("#host_name").val().trim().length>50){ 
				    top.$.messager.alert("警告","主机名最多允许50个字符","warning");
					return;
				}
			}	
			if($("#username").val().trim()!=""){
				if($("#username").val().trim().length>20){ 
				    top.$.messager.alert("警告","昵称最多允许20个字符","warning");
					return;
				}
			}	
			if($("#belongaccount").val().trim()!=""){
				if($("#belongaccount").val().trim().length>50){ 
				    top.$.messager.alert("警告","归属最多允许50个字符","warning");
					return;
				}
			}	
			top.$.messager.confirm("确认", "确定导出？", function (r) {  
		        if (r) {   
					window.location.href= "<%=request.getContextPath()%>/cloudHostExportData.do?"+queryParams;
		        }  
		    });   
		});		
		$('#host_name').bind('keypress',function(event){
	        if(event.keyCode == "13")    
	        {
	        	$("#query_cloud_host_btn").click();
	        }
	    });
	});
	</script>
</html>