<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="com.zhicloud.op.vo.CloudHostVO"%>
<%@page import="com.zhicloud.op.vo.CloudDiskVO"%>
<%@page import="com.zhicloud.op.vo.TerminalUserVO"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
	TerminalUserVO terminalUser = (TerminalUserVO)request.getAttribute("terminalUser");
	Integer hostTotal = (Integer)request.getAttribute("hostTotal");
// 	List<CloudHostVO> cloudHostList = (List<CloudHostVO>)request.getAttribute("cloudHostList");
// 	List<CloudDiskVO> cloudDiskList = (List<CloudDiskVO>)request.getAttribute("cloudDiskList");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>致云代理商管理平台</title>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/style/agent.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/javascript/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath() %>/javascript/themes/icon.css" />
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/agent.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script> 
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/big.min.js"></script>
<script type="text/javascript">
var a = '<%= request.getContextPath()%>';
var ajax = new RemoteCallUtil(a+"/bean/call.do?userType=3");
$(document).ready(function(){
	init(2);
	inituser("<%=loginInfo.getAccount()%>",0);
	
	var pager = $('#tb').datagrid('getPager');
	pager.pagination({
		showPageList: false,
		showRefresh: false,
		displayMsg: '',
		beforePageText: '',
		afterPageText: ''
	 });
	
	//-----------------
	//查看主机详情
	$("#view_host_btn").click(function(){
		var d = $("#tb").datagrid("getSelected");
		if(d==null){
			top.$.messager.alert("提示","请选择您要查看的专属云","info",function(){
			});
			return;
		}
		var id = d.id;
		if(d.status!=1){
			top.$.messager.alert("提示","该专属云已停用,无法查看主机","info",function(){
			});
			return;
		}
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=3&bean=vpcService&method=toVpcHostList&vpcId="+encodeURIComponent(id);
	});
	//查看ip详情
	$("#view_ip_btn").click(function(){
		var d = $("#tb").datagrid("getSelected");
		if(d==null){
			top.$.messager.alert("提示","请选择您要查看的专属云","info",function(){
			});
			return;
		}
		if(d.status!=1){
			top.$.messager.alert("提示","该专属云已停用,无法查看IP","info",function(){
			});
			return;
		}
		var id = d.id;
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=3&bean=vpcService&method=vpcIpManagePage&vpcId="+encodeURIComponent(id);
	});
	//查看主机绑定详情
	$("#view_network_btn").click(function(){
		var d = $("#tb").datagrid("getSelected");
		if(d==null){
			top.$.messager.alert("提示","请选择您要查看的专属云","info",function(){
			});
			return;
		}
		if(d.status!=1){
			top.$.messager.alert("提示","该专属云已停用,无法查看网络配置","info",function(){
			});
			return;
		}
		var id = d.id;
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=3&bean=vpcService&method=vpcNetworkManagePage&vpcId="+encodeURIComponent(id);
	});
	//停用专属云
	$("#disable_service").click(function(){
		var d = $("#tb").datagrid("getSelected");
		if(d==null){
			top.$.messager.alert("提示","请选择您要停用的专属云","info",function(){
			});
			return;
		}
		if(d.status!=1){
			top.$.messager.alert("提示","该专属云已停用","info",function(){
			});
			return;
		}
		var id = d.id;
		top.$.messager.confirm("确认", "确定停用该VPC？", function (r) {
			if(r){
				ajax.remoteCall("bean://vpcService:disableVpc", 
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
									top.$.messager.alert("警告", reply.exceptionMessage, "warning");
								}
							}
							else if (reply.result.status == "success")
							{
								top.$.messager.alert("信息", reply.result.message, "info",function(){
									window.location.reload();
								});
							}
							else
							{
								top.$.messager.alert('警告', reply.result.message, 'warning',function(){
									window.location.reload();
								});
							}
						}
					) ;
			}else{
				return;
			}
		});
	});
	//恢复专属云
	$("#able_service").click(function(){
		var d = $("#tb").datagrid("getSelected");
		if(d==null){
			top.$.messager.alert("提示","请选择您要停用的专属云","info",function(){
			});
			return;
		}
		if(d.status!=2){
			top.$.messager.alert("提示","该专属云已启动","info",function(){
			});
			return;
		}
		var id = d.id;
		top.$.messager.confirm("确认", "确定恢复该VPC？", function (r) {
			if(r){
				ajax.remoteCall("bean://vpcService:ableVpc", 
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
									top.$.messager.alert("警告", reply.exceptionMessage, "warning");
								}
							}
							else if (reply.result.status == "success")
							{
								top.$.messager.alert("信息", reply.result.message, "info",function(){
									window.location.reload();
								});
							}
							else
							{
								top.$.messager.alert('警告', reply.result.message, 'warning',function(){
									window.location.reload();
								});
							}
						}
					) ;
			}else{
				return;
			}
		});
	});
	
});
function formatStatus(value, row, index)
{
	if(value==1){
		return "启用";
	}else if(value==2){
		return "暂停";
	}
	return "";
	
}
function formatIp(val)
{
	if(val==null){
		return "无";
	}
	return val;
}

function formatRegion(val)
{
	if(val==1){
		return "广州";
	}else if(val==2){
		return "成都";
	}else if(val==3){
		return "北京";
	}
	return "香港";
}
function formatType(val)
{
	if(val==2){
		return "代理商创建";
	}else if(val==1){
		return "用户创建";
	} 
	return "";
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
function onLoadSuccess()
{
	var d = $("#tb").datagrid("getData");
	var t = d.total;
	$("#total_vpc").html(t);
	
	//去掉没用的checkbox
	$("input[type='checkbox'][name!='ck']").remove();
}
</script>
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
    <div style="width:920px;">
      <div class="sidebar l" style="height:575px">
        <div style="width:238px;margin-top:15px;">
          <div class="accounttitle1">用户</div>
          <div class="accounttitle2" style="margin-bottom:15px"><b><%=terminalUser.getAccount() %></b></div>
          <div class="accounttitle1">手机</div>
          <div class="accounttitle2"><b><%=terminalUser.getPhone() %></b></div>
           <div class="accounttitle1"><a href="javascript:void(0);" id="operLog" class="green">操作日志</a></div>
          <div style="display:block;margin:15px auto 0 auto; width:238px; height:90px; background:url(<%=request.getContextPath()%>/image/sidebar_server_bg.png) no-repeat center center; padding:49px 0 15px 0; font-size:36px; line-height:40px; text-align:center; color:#626262; border-top:solid 1px #b2b2b2"><%=hostTotal %></div> <a id="create_host" class="greenbutton" href="#" style="margin:0 auto; width:80px; height:24px; line-height:24px; font-size:12px;">创建云主机</a> 
          <div id="total_vpc" style="display:block;margin:0 auto; width:238px; height:90px; background:url(<%=request.getContextPath()%>/image/sidebar_vpc_bg.png) no-repeat center center; padding:49px 0 15px 0; font-size:36px; line-height:40px; text-align:center; color:#626262;">0</div> <a class="greenbutton" href="<%=request.getContextPath() %>/bean/page.do?userType=3&bean=vpcService&method=agentCreateVpnPage&terminalUserId=<%=terminalUser.getId() %>" style="margin:0 auto; width:80px; height:24px; line-height:24px; font-size:12px;">创建专属云</a>
        </div>
      </div>
      <div class="r" style="width:650px;">
        <div class="titlebar" style="width:650px;">
          <div class="l links"><a href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=terminalUserService&method=viewItemPage&terminalUserId=<%=terminalUser.getId()%>" >云主机</a> &nbsp;&nbsp; 
<!--           |&nbsp;&nbsp; <a href="#"  class="active">云硬盘</a> &nbsp;&nbsp; -->
          |&nbsp;&nbsp; <a href="#" class="active">专属云</a>
<!--           	｜　<a href="#">云硬盘</a> -->
          </div>
        </div>
        <div class="titlebar" style="width:650px;padding:15px 0 5px 0;">
      <div class="blocks l">
        <div class="l"><a id="view_host_btn" href="#" class="bluebutton" style="width:76px">查看主机</a></div> 
        <div class="l"><a id="view_network_btn" href="#" class="bluebutton" style="width:76px">网络配置</a></div> 
        <div class="l"><a id="view_ip_btn" href="#" class="bluebutton" style="width:48px">IP</a></div> 
        <div class="l"><a id="disable_service" href="#" class="greenbutton" style="width:48px">停用</a></div> 
        <div class="l"><a id="able_service" href="#" class="greenbutton" style="width:48px">恢复</a></div> 
        <div class="clear"></div>
      </div>
      
      
    </div>
        <table id="tb" class="easyui-datagrid"
        	data-options="
					url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=terminalUserService&method=viewVpc&terminalUserId=<%=terminalUser.getId()%>',
					queryParams: {},
					border:false,
					singleSelect:true,
					selectOnCheck:true,
					checkOnSelect:true,
					scrollbarSize:0,
					fitColumns: true,
					pagination: true,
					pageList: [10, 20, 50, 100, 200],
					pageSize: 10,
					onLoadSuccess: onLoadSuccess
				">
          <thead>
            <tr>
            <th data-options="field:'ck',checkbox:true,width:28">&nbsp;</th>
            <th data-options="field:'region',formatter:formatRegion,width:43">地域</th>
            <th data-options="field:'type',formatter:formatType,width:95">类型</th>
            <th data-options="field:'displayName',width:153">名称</th>
            <th data-options="field:'hostAmount',width:47">主机数</th>
            <th data-options="field:'ipAmount',width:47">IP数</th>
            <th data-options="field:'status',formatter:formatStatus,width:82">状态</th>
            </tr>
          </thead> 
        </table>
<!--         <div class="easyui-pagination" data-options="total: 114, showPageList: false, showRefresh: false, displayMsg: '', beforePageText: '', afterPageText: ''"></div> -->
      </div>
    </div>
    <div class="clear">&nbsp;</div>
  </div>
  <div class="footer">
     Copyright &copy; 2015 <a href="http://www.zhicloud.com" target="_blank">致云科技有限公司</a>, All rights reserved.　　蜀ICP备14004217号-1
  </div>
</div>
</body> 
</html>
