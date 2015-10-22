<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.vo.SysUserVO"%>
<%@page import="java.util.List"%>
<%@page import="java.math.BigDecimal"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
	List<SysUserVO> operList = (List<SysUserVO>)request.getAttribute("operList");
// 	BigDecimal totalMoney = (BigDecimal)request.getSession().getAttribute("totalMoney");
%>
<!DOCTYPE html>
<!-- operator_cash_coupon.jsp -->
<html>
	<head>
		<meta charset="UTF-8" />
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
		
		<title>运营商 - 现金券管理</title>
		
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
		<style type="text/css">
		#agent_datagrid {
			border: 0px solid red;
			margin-left: -10px;
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
		<table border="0" style="width:auto;">
				<tr>
					<td id="total_amount" style="padding:5px;width:150px">总现金券数：</td>
					<td id="total_money" style="padding:5px;">总金额：</td>
				</tr>
<!-- 				<tr> -->
<!-- 					<td style="padding:5px;">已使用数量：</td> -->
<!-- 					<td style="padding:5px;">已使用总额：</td> -->
<!-- 				</tr> -->
<!-- 				<tr> -->
<!-- 					<td style="padding:5px;">未使用数量：</td> -->
<!-- 					<td style="padding:5px;">未使用总额：</td> -->
				</tr>
			</table>
		<form id="big_form"  method="post">
	
			<table id="invite_code_datagrid" class="easyui-datagrid" title="现金券管理"
				style="height: 505px; margin: 10px;"
				data-options="
					url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=cashCouponService&method=queryCashCouponForOperator',
					queryParams: {},
					toolbar: '#toolbar',
					rownumbers: true,
					striped: true,
					remoteSort:false,
					fitColumns: false,
					pagination: true,
					pageList: [10, 20, 50, 100, 200],
					pageSize: 20,
					view:createView(),
					onLoadSuccess: onLoadSuccess
				">
				<thead>
					<tr>
						<th data-options="checkbox:true"></th>
						<th field="cashCode" width="150px" sortable=true>现金券券码</th>
						<th field="userName" width="100px">所属运营商</th>
						<th field="name" width="100px">昵称</th>
						<th field="money" formatter="formatMoney" width="80px">价值(元)</th>
						<th field="createTime" formatter="timeFormat" width="120px">创建时间</th>
						<th field="email" width="100px">电子邮箱</th>
						<th field="phone" width="100px">手机号码</th>
						<th field="status" formatter="formatStatus" width="100px">状态</th>
						<th field="sendAddress"   width="100px">发送地址</th>
						<th field="operate" formatter="terminalUserColumnFormatter" width="200px">操作</th>
					</tr>
				</thead>
			</table>
	
			<div id="toolbar" style="padding: 3px;">
				<div style="display: table; width: 100%;">
					<div style="margin-bottom:5px;">
						<a href="#" class="easyui-linkbutton" iconCls="icon-add" plain="true" id="add_invite_code_btn">添加</a> 
						<a href="#" class="easyui-linkbutton" iconCls="icon-remove" plain="true" id="del_invite_code_btn">删除</a>
						<a href="#" class="easyui-linkbutton" iconCls="icon-print" plain="true" id="export_data_btn">导出数据</a>
					</div>
					<div style="margin-left:5px;">
						<span style="position: relative; top: 2px;">状态</span>
						<select id="query_by_status" class="easyui-combobox">
						   <option value="0">全部</option>
						   <option value="1">未发送</option>
						   <option value="2">已发送</option>
						   <option value="3">已过期</option>
						   <option value="4">已使用</option>
						</select>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<span style="position: relative; top: 2px;">运营商</span>
						<select id="query_by_operator" class="easyui-combobox">
							<option value="all">全部</option>
							<%
							for( SysUserVO oper : operList ) 
							{ 
				  			%> 
				            <option value="<%=oper.getId()%>"><%=oper.getAccount()%></option>
				            <%
								} 
						    %>
						</select>
						&nbsp;&nbsp;&nbsp;&nbsp;
						<span style="position: relative; top: 2px;">昵称</span> 
						<input type="text" id="username" class="messager-input" /> 
						&nbsp;&nbsp;&nbsp;&nbsp;
					<span style="width:60px;">创建时间</span> 从 <input class="easyui-datebox" type="text" name = "create_time_from" id="create_time_from" style="width:100px"/>
					到 <input class="easyui-datebox" type="text" name = "create_time_to" id="create_time_to" style="width:100px"/>
								
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

// 	function formatCreateTime(val, row)
// 	{
// 		return $.formatDateString(val, "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss");
// 	}
<%-- 	var total_money = "<%=(BigDecimal)request.getSession().getAttribute("totalMoney")%>"; --%>
	function formatStatus(val,row)
	{  
		if(val == 1) {  
		    return "未发送";  
		} else if(val == 2) {
		    return "已发送";  
		} else if(val == 3) {
		 	return "已过期";
		} else if(val == 4) {
			return "已使用";
		} 
	}  
	function formatMoney(val,row)
	{
		return val.toFixed(2);
	}
	function timeFormat(val, row)
	{
		return $.formatDateString(val, "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss");
	}
// 	function formatBelongingType(val,row){
// 		if(val == 1){
// 			return "运营商";
// 		}else{
// 			return "代理商";
// 		}
// 	}
	function terminalUserColumnFormatter(value, row, index)
	{
		var data = $("#invite_code_datagrid").datagrid("getData");
		var id = data.rows[index].status;
		if(id==1){
		return "<div row_index='"+index+"'>\
					<a href='#' class='datagrid_row_linkbutton sendPhone_btn'>发送到手机</a>\
					<a>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</>\
					<a href='#' class='datagrid_row_linkbutton sendEmail_btn'>发送到邮箱</a>\
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
		var total_money = "<%=(BigDecimal)request.getSession().getAttribute("totalMoney")%>";
		var data=$('#invite_code_datagrid').datagrid('getData');
		$("#total_money").html("现金券总额:"+data.cashAmount+"元");
		$("#total_amount").html("现金券数:"+data.total+"个");
		// 每一行的'发送到邮箱'按钮
		$("a.sendEmail_btn").click(function(){
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#invite_code_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].id;
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cashCouponService&method=sendCashCouponByEmailPage&cashCouponId="+encodeURIComponent(id),
				onClose: function(data){
					$('#invite_code_datagrid').datagrid('reload');
				}
			});
		});
		//每一行的'发送到手机'按钮
		$("a.sendPhone_btn").click(function(){
			$this = $(this);
			rowIndex = $this.parent().attr("row_index");
			var data = $("#invite_code_datagrid").datagrid("getData");
			var id = data.rows[rowIndex].id;
			top.showSingleDialog({
				url: "<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cashCouponService&method=sendCashCouponByPhonePage&cashCouponId="+encodeURIComponent(id),
				onClose: function(data){
					$('#invite_code_datagrid').datagrid('reload');
				}
			});
		});
	}
	
	$(function(){
		// 查询
		$("#query_cloud_host_btn").click(function(){
			var queryParams = {};
			queryParams.cash_status = $("#query_by_status").combobox("getValue");
			queryParams.cash_operator = $("#query_by_operator").combobox("getValue");
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
			$('#invite_code_datagrid').datagrid({
				"queryParams": queryParams
			});
		});
		//清除
		$("#clear_cloud_host_btn").click(function(){
			$("#username").val("");
			$("#query_by_status").combobox("setValue","0");
			$("#query_by_operator").combobox("setValue","all");
			$("#create_time_from").datebox("setValue","");
			$("#create_time_to").datebox("setValue","");
			
		});	
		// 添加邀请码
		$("#add_invite_code_btn").click(function(){
			top.showSingleDialog({
				url:"<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cashCouponService&method=addCashCouponPageByOperator",
				onClose: function(data){
					$('#invite_code_datagrid').datagrid('reload');
				}
			});
		});
		// 删除邀请码
		$("#del_invite_code_btn").click(function() {
			var rows = $('#invite_code_datagrid').datagrid('getSelections');
			if (rows == null || rows.length == 0) {
				top.$.messager.alert("警告","未选择删除项");
				return;
			}
			var ids = rows.joinProperty("id");
			top.$.messager.confirm("确认", "确定要删除吗?", function (r) {  
		        if (r) {   
						ajax.remoteCall("bean://cashCouponService:deleteCashCouponByIds",
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
								}
								else if(reply.status=="fail"){
									top.$.messager.alert("提示",reply.result.message,"warning");
								}
								else {
									$('#invite_code_datagrid').datagrid(
											'reload');
								}
							}
						);
		        }  
		    }); 
		});
		//导出数据
		$("#export_data_btn").click(function() {
			var queryParams = "";
			queryParams +="cash_status="+$("#query_by_status").combobox("getValue");
			queryParams +="&cash_operator="+$("#query_by_operator").combobox("getValue");
			queryParams +="&username="+$("#username").val().trim();
			if($("#username").val()!=""){
				if($("#username").val().length>20){ 
				    top.$.messager.alert("警告","昵称最多允许20个字符","warning");
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
			top.$.messager.confirm("确认", "确定导出？", function (r) {  
		        if (r) {   
					window.location.href= "<%=request.getContextPath()%>/cashCouponExportData.do?"+queryParams;
		        }  
		    });   
		});		
// 		 $('#terminal_user_account').bind('keypress',function(event){
// 	        if(event.keyCode == "13")    
// 	        {
// 	        	$("#query_terminal_user_btn").click();
// 	        }
// 	    });
	});
	</script>
</html>