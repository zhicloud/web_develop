<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.app.propeties.AppProperties"%>
<%@page import="com.zhicloud.op.vo.VpcOuterIpVO"%>
<%@page import="com.zhicloud.op.vo.VpcBindPortVO"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	List<VpcBindPortVO> vpcBindPortList  = (List<VpcBindPortVO>)request.getAttribute("vpcBindPortList");
	String vpcId = (String)request.getAttribute("vpcId");
	String terminalUserId = (String)request.getSession().getAttribute("terminalUserId");
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, AppConstant.SYS_USER_TYPE_AGENT);
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<!-- edit_port.jsp -->
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
<title><%=AppConstant.PAGE_TITLE %></title>
<link rel="shortcut icon" href="<%=request.getContextPath() %>/image/logo1.ico" type="image/x-icon" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/style/style.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/style/agent.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/icon.css" />

<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/agent.js"></script>
<script type="text/javascript">
window.name = "selfWin";

var a = '<%= request.getContextPath() %>';
var ajax = new RemoteCallUtil(a+"/bean/call.do?userType=3");
ajax.async = false;
var name = "<%=loginInfo.getAccount()%>";
var vpc_id = "<%=vpcId%>";
$(document).ready(function() {
	init(2);
	if (name != '') {
		inituser(name, 0);
	} else {

		inituser();
	}
	initstep(1);
});

function getLoginInfo(name, message, userId) {
	slideright();
	inituser(name, message);
	window.location.reload();
};
function addItem(){
	var outerIp = $("#outer_ip").combobox("getValue");
	var outerPort = $("#outer_port").val();
	var host = $("#host").combobox("getValue");
	var protocol = $("#protocol").combobox("getValue");
	var port = $("#port").val();
	if(outerIp==null || outerIp==""){
		top.$.messager.alert("提示", "请选择外网IP", "info", function(){
		});
		return;
	}
	if(outerPort==null || outerPort==""){
		top.$.messager.alert("提示", "请输入外网端口", "info", function(){
		});
		return;
	}
	if (outerPort == 0 || outerPort > 65535) {
		top.$.messager.alert('警告', '外网端口范围在1-65535之间，请重新输入', 'warning');
		$("#outer_port").val("");
		return;
	}
	if(host==null || host==""){
		top.$.messager.alert("提示", "请选择主机", "info", function(){
		});
		return;
	}
	if(port==null || port==""){
		top.$.messager.alert("提示", "请输入主机端口", "info", function(){
		});
		return;
	}
	if (port == 0 || port > 65535) {
		top.$.messager.alert('警告', '端口范围在1-65535之间，请重新输入', 'warning');
		$("#port").val("");
		return;
	}
	if(protocol==null || protocol==""){
		top.$.messager.alert("提示", "请选择协议类型", "info", function(){
		});
		return;
	}
	ajax.remoteCall("bean://vpcService:addBindPortItem",
		[ vpc_id,outerIp,outerPort,host,port,protocol ], 
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
			else
			{
				top.$.messager.alert("信息", reply.result.message, "info",function(){
					window.location.reload();
				});
			}
		}
	 );
}
function deleteBindPorts(){
	var ids = [];
	$("input:checked").each(function(){
		ids.push($(this).val());
	});
	if(ids.length==0){
		top.$.messager.alert("提示", "请选择需要删除的项", "info", function(){
		});
		return;
	}
	top.$.messager.confirm("确认", "确定删除选中项？", function (r) {
		if(r){
			ajax.remoteCall("bean://vpcService:deleteBindPorts",
					[ vpc_id,ids ], 
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
						else
						{
							top.$.messager.alert("信息", reply.result.message, "info",function(){
								window.location.reload();
							});
						}
					}
				 );
		}else{
			return;
		}
	});
	
}
</script>
<style type="text/css">
#porttable td {
	width: 60px;
	border-width: 0 1px 1px 0;
	border-style: dotted;
	margin: 0;
	padding: 0;
}

#porttable .headtr td {
	width: 60px;
	border-width: 0 1px 1px 0;
	border-style: dotted;
	margin: 0;
	padding: 0;
	background: #ddedef;
}

#porttable .nochange .datagrid-cell {
	color: #a2a2a2;
}
</style>
<!--[if IE 6]>
<script src="javascript/DD_belatedPNG.js"></script>
<script type="text/javascript">
	DD_belatedPNG.fix("*");
</script>
<![endif]-->
</head>
<body>
	<div class="page">
		<div class="header">
     <div class="top">
	   <a class="logo l" href="#"><img src="<%=request.getContextPath()%>/image/agent_logo.png" width="188" height="25" alt="致云代理商管理平台" /></a>
	   <div class="nav l">
	    <a href="#" id="business">
				<img id="agent_nav_1" class="swapimage" src="<%=request.getContextPath()%>/image/agent_nav_1_i.png" width="21" height="21" />
				<label>业务信息</label>
			</a>
			<a href="#" id="user_manage">
				<img id="agent_nav_2" class="swapimage" src="<%=request.getContextPath()%>/image/agent_nav_2_i.png" width="21" height="21" />
				<label>用户管理</label>
			</a>
			<a href="#" id="my_account">
				<img id="agent_nav_3" class="swapimage" src="<%=request.getContextPath()%>/image/agent_nav_3_i.png" width="21" height="21" />
				<label>我的账户</label>
			</a>
			<a href="#" id="oper_log">
				<img id="agent_nav_4" class="swapimage" src="<%=request.getContextPath()%>/image/agent_nav_4_i.png" width="21" height="21" />
				<label>操作日志</label>
			</a>
			<a href="#" id="user_manual">
				<img id="agent_nav_5" class="swapimage" src="<%=request.getContextPath()%>/image/agent_nav_5_i.png" width="21" height="21"/>
				<label>使用手册</label>
			</a>
	   </div>
	   <div class="user l">
	    <img class="reddot" src="<%=request.getContextPath()%>/image/reddot.png" width="6" height="6" alt=" " />
	    <a id="logoutlink" href="javascript:void(0);">注销</a>
	    <span>|</span>
	    <a id="userlink" href="javascript:void(0);"></a>
	   </div>
	   <div class="clear"></div>
	  </div>
  	</div>
			<div class="main">
				<div class="titlebar" style="width: 980px; padding: 15px 0 5px 0;">
					<div class="blocks l">
						<div class="l" style="padding-right:15px; ">
							<a href="<%=request.getContextPath()%>/bean/page.do?userType=3&bean=terminalUserService&method=agentViewVpcPage&terminalUserId=<%=terminalUserId%>" onclick="self.location=document.referrer;"><img src="<%=request.getContextPath()%>/image/button_back.png" width="22" height="30" alt="返回" /></a>
						</div>
						<div class="l">
							<a href="javascript:void(0);" class="bluebutton r"
							style="width: 80px;" onclick="deleteBindPorts();">删除</a>
						</div>
					</div>
					
					<div class="blocks r">
						<a href="javascript:void(0);" class="bluebutton r"
							style="width: 80px;" onclick="addItem();">添加</a>
						<div class="r">
							主机端口：
							<input class="textbox" type="text" name="port" id="port"style="width: 48px; height: 28px" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"></input>
						</div>
						<div class="r">
							协议：
							<select class="easyui-combobox" id="protocol" data-options="width:'60',height:'30',panelHeight:'auto',editable:false"> 
								<option value="0">全部</option>
								<option value="1">TCP</option>
								<option value="2">UDP</option>
							</select>
						</div>
						<div class="r">
							主机：
							<select class="easyui-combobox" id="host" data-options="width:'180',height:'30',panelHeight:'auto',editable:false,
								valueField:'id',
								textField:'text',
								url:'<%=request.getContextPath()%>/bean/ajax.do?userType=4&bean=cloudHostService&method=getAllHostByVpcId&vpcId=<%=vpcId%>'
								"> 
							</select>
						</div>	
						<div class="r">
							外网端口：
							<input class="textbox" type="text" name="outerPort" id="outer_port"style="width: 48px; height: 28px" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"></input>
						</div>
						<div class="r">
							外网IP：
							<select class="easyui-combobox" id="outer_ip" data-options="width:'80',height:'30',panelHeight:'auto',editable:false,
								valueField:'id',
								textField:'text',
								url:'<%=request.getContextPath()%>/bean/ajax.do?userType=4&bean=vpcService&method=getAllIps&vpcId=<%=vpcId%>'
								"> 
							</select>
						</div>
						<div class="clear"></div>
					</div>
					<div class="clear"></div>
				</div>
				<form id="ip_manage_form" method="post">
					<input type="hidden" name="vpcId" value="<%=vpcId%>" />
					<div class="box"
						style="overflow-y: scroll; width: 980px; height: 410px; margin: 0 auto 0 auto">
						<table id="porttable" cellpadding="0" cellspacing="0" border="0"
							width="100%">
							<tr class="headtr">
								<td style="width: 28px">&nbsp;</td>
								<td style="width: 200px"><div class="datagrid-cell">外网IP</div></td>
								<td style="width: 150px"><div class="datagrid-cell">端口</div></td>
								<td style="width: 200px"><div class="datagrid-cell">主机</div></td>
								<td style="width: 150px"><div class="datagrid-cell">端口</div></td>
								<td style="width: 200px"><div class="datagrid-cell">协议</div></td>
							</tr>
							<%
							for(VpcBindPortVO vpcBindPort : vpcBindPortList){
							%>
							<tr>
								<td style="width: 28px"><div class="datagrid-cell">
 								<input type="checkbox" value="<%=vpcBindPort.getId()%>" />
								</div></td>
								<td style="width: 200px"><div class="datagrid-cell"><%=vpcBindPort.getOuterIp()%></div></td>
								<td style="width: 150px"><div class="datagrid-cell"><%=vpcBindPort.getOuterPort()%></div></td>
								<td style="width: 200px"><div class="datagrid-cell"><%=vpcBindPort.getDisplayName()%></div></td>
								<td style="width: 150px"><div class="datagrid-cell"><%=vpcBindPort.getHostPort()%></div></td>
								<td style="width: 200px"><div class="datagrid-cell"><%=vpcBindPort.getProtocol()==1?"TCP":(vpcBindPort.getProtocol()==0?"全部":"UDP")%></div></td>
							</tr>

							<% 
							}
              	   			%>
						</table>
					</div>
					<div style="width: 980px; margin: 0 auto 0 auto;">

						<div class="buttonbar" style="border-top: solid 1px #b2b2b2;width:980px;">
							<a class="graybutton r" href="<%=request.getContextPath()%>/bean/page.do?userType=3&bean=terminalUserService&method=agentViewVpcPage&terminalUserId=<%=terminalUserId%>" onclick="self.location=document.referrer;">返回</a>
						</div>
					</div>
				</form>
			</div>
			<div class="clear"></div>
			<div class="footer">
	     		Copyright &copy; 2015 <a href="http://www.zhicloud.com" target="_blank">致云科技有限公司</a>, All rights reserved.　　蜀ICP备14004217号-1
	  		</div>
	</div>
</body>
</html>
