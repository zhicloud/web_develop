<%@page import="java.util.List"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="com.zhicloud.op.vo.CloudHostVO"%>
<%@page import="com.zhicloud.op.vo.CloudDiskVO"%>
<%@page import="com.zhicloud.op.vo.TerminalUserVO"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = Integer.valueOf(request.getParameter("userType"));
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, userType);
	TerminalUserVO terminalUser = (TerminalUserVO)request.getAttribute("terminalUser");
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
	
});
function formatStatus(value, row, index)
{
	var status = $("#tb").datagrid("getData").rows[index].status;
	var realHostId = $("#tb").datagrid("getData").rows[index].realHostId;
	var runningStatus = $("#tb").datagrid("getData").rows[index].runningStatus;
	var processStatus = $("#tb").datagrid("getData").rows[index].processStatus;
	if(status==2){
		return "停机";
	}
	if(processStatus!=null && processStatus==2){
		return "创建失败";
	}
	if(status==1 && runningStatus==2){
		return "运行";
	}
	if(processStatus != null && (processStatus == 0 || processStatus == 3)){
		return "创建中";
	}
	if(status==1 && runningStatus==1){
		return "关机";
	}
	if(realHostId==null){
		return "创建中";
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
	$("#total_host").html(t);
	//查看主机详情
	$("#view_detail_btn").click(function(){
		var d = $("#tb").datagrid("getSelected");
		if(d==null){
			top.$.messager.alert("提示","请选择您要查看的云主机","info",function(){
			});
			return;
		}
		if(d.processStatus!=null && d.processStatus==2){
			top.$.messager.alert("提示","未创建成功的云主机无法查看详情","info",function(){
			});
			return;
		}
		if(d.processStatus != null && (d.processStatus == 0 || d.processStatus == 3)){
			top.$.messager.alert("提示","未创建成功的云主机无法查看详情","info",function(){
			});
			return;
		}
		if(d.realHostId==null){
			top.$.messager.alert("提示","未创建成功的云主机无法查看详情","info",function(){
			});
			return;
		}
		var id = d.id;
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=cloudHostViewDetailPage&cloudHostId="+encodeURIComponent(id);
	});
	//修改主机配置
	$("#modify_allocation_btn").click(function(){
		var d = $("#tb").datagrid("getSelected");
		if(d==null){
			top.$.messager.alert("提示","请选择一台云主机修改配置","info",function(){
			});
			return;
		}
		if(d.processStatus!=null && d.processStatus==2){
			top.$.messager.alert("提示","未创建成功的云主机无法修改配置","info",function(){
			});
			return;
		}
		if(d.processStatus != null && (d.processStatus == 0 || d.processStatus == 3)){
			top.$.messager.alert("提示","未创建成功的云主机无法修改配置","info",function(){
			});
			return;
		}
		if(d.realHostId==null){
			top.$.messager.alert("提示","未创建成功的云主机无法修改配置","info",function(){
			});
			return;
		}
		var id = d.id;
		if(d.status==2){
			top.$.messager.alert("提示","该主机已停机，无法修改配置","info",function(){
			});
			return;
		}
		if(d.status==1 && d.runningStatus==1){
			window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=modifyAllocationPage&hostId="+encodeURIComponent(id);
		}else{
			top.$.messager.alert("提示","关机状态才能修改配置","info",function(){
			});
			return;
		}
	});
	//停用
	$("#inactivate_host_btn").click(function(){
		var d = $("#tb").datagrid("getSelected");
		if(d==null){
			top.$.messager.alert("提示","请选择您要停用的云主机","info",function(){
			});
			return;
		}
		if(d.processStatus!=null && d.processStatus==2){
			top.$.messager.alert("提示","未创建成功的云主机无法停用","info",function(){
			});
			return;
		}
		if(d.processStatus != null && (d.processStatus == 0 || d.processStatus == 3)){
			top.$.messager.alert("提示","未创建成功的云主机无法停用","info",function(){
			});
			return;
		}
		if(d.realHostId==null){
			top.$.messager.alert("提示","未创建成功的云主机无法停用","info",function(){
			});
			return;
		}
		var id = d.id;
		if(d.status==2){
			top.$.messager.alert("提示","该主机已停用","info",function(){
			});
			return;
		}else{
			top.$.messager.confirm("确认", "确定要停用该云主机吗？", function (r) { 
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
								$('#tb').datagrid('reload');
							}else{
								top.$.messager.alert("信息", reply.result.message, "info",function(){
									$('#tb').datagrid('reload');
								});
							}
						}
					);
				}
			});
		}
	});
	//恢复停用
	$("#reinactivate_host_btn").click(function(){
		var d = $("#tb").datagrid("getSelected");
		if(d==null){
			top.$.messager.alert("提示","请选择您要恢复的云主机","info",function(){
			});
			return;
		}
		var id = d.id;
		if(d.status!=2){
			top.$.messager.alert("提示","只能恢复已停用的云主机","info",function(){
			});
			return;
		}
		top.$.messager.confirm("确认", "确定要恢复该云主机吗？", function (r) { 
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
								$('#tb').datagrid('reload');
							}
							else
							{
								top.$.messager.alert("信息", reply.result.message, "info",function(){
									$('#tb').datagrid('reload');
								});
							}
						}
					);
			}
		});
	});
	//创建云主机
	$("#create_host").click(function(){
		var id = "<%=terminalUser.getId()%>";
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=cloudHostService&method=createCloudHostPage&terminalUserId="+encodeURIComponent(id);
	});
	//查看用户操作
	$("#operLog").click(function(){
		var id = "<%=terminalUser.getId()%>";
		window.location.href="<%=request.getContextPath()%>/bean/page.do?userType=<%=userType%>&bean=operLogService&method=managePage&terminalUserId="+encodeURIComponent(id);
	});
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
	    <a href="#" id="business"><img id="agent_nav_1" class="swapimage" src="<%=request.getContextPath()%>/image/agent_nav_1_i.png" width="21" height="21" />业务信息</a>
	    <a href="#" id="user_manage"><img id="agent_nav_2" class="swapimage" src="<%=request.getContextPath()%>/image/agent_nav_2_i.png" width="21" height="21" />用户管理</a>
	    <a href="#" id="my_account"><img id="agent_nav_3" class="swapimage" src="<%=request.getContextPath()%>/image/agent_nav_3_i.png" width="21" height="21" />我的账户</a>
	    <a href="#" id="oper_log"><img id="agent_nav_4" class="swapimage" src="<%=request.getContextPath()%>/image/agent_nav_4_i.png" width="21" height="21" />操作日志</a>
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
          <div id="total_host" style="display:block;margin:15px auto 0 auto; width:238px; height:90px; background:url(<%=request.getContextPath()%>/image/sidebar_server_bg.png) no-repeat center center; padding:49px 0 15px 0; font-size:36px; line-height:40px; text-align:center; color:#626262; border-top:solid 1px #b2b2b2"></div> <a id="create_host" class="greenbutton" href="#" style="margin:0 auto; width:80px; height:24px; line-height:24px; font-size:12px;">创建云主机</a> 
<%--           <div style="display:block;margin:0 auto; width:238px; height:90px; background:url(<%=request.getContextPath()%>/image/sidebar_storage_bg.png) no-repeat center center; padding:49px 0 15px 0; font-size:36px; line-height:40px; text-align:center; color:#626262;">0</div> <a class="greenbutton" href="#" style="margin:0 auto; width:80px; height:24px; line-height:24px; font-size:12px;">创建云硬盘</a> --%>
        </div>
      </div>
      <div class="r" style="width:650px;">
        <div class="titlebar" style="width:650px;">
          <div class="l links"><a href="#" class="active">云主机</a>
<!--           	｜　<a href="#">云硬盘</a> -->
          </div>
        </div>
        <div class="titlebar" style="width:650px;padding:15px 0 5px 0;">
      <div class="blocks l">
        <div class="l"><a id="view_detail_btn" href="#" class="bluebutton" style="width:76px">查看详情</a></div>
        <div class="l"><a id="modify_allocation_btn" href="#" class="bluebutton" style="width:76px">修改配置</a></div>
        <div class="l"><a id="inactivate_host_btn" href="#" class="orangebutton" style="width:48px">停用</a></div>
        <div class="l"><a id="reinactivate_host_btn" href="#" class="greenbutton" style="width:48px">恢复</a></div>
        <div class="clear"></div>
      </div>
      
      
    </div>
        <table id="tb" class="easyui-datagrid"
        	data-options="
					url: '<%=request.getContextPath()%>/bean/ajax.do?userType=<%=userType%>&bean=terminalUserService&method=viewItem&terminalUserId=<%=terminalUser.getId()%>',
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
              <th data-options="field:'displayName',width:153">名称</th>
              <th data-options="field:'cpuCore',formatter:formatCpuCore,width:47">CPU</th>
              <th data-options="field:'memory',formatter:formatCapacity,width:47">内存</th>
              <th data-options="field:'dataDisk',formatter:formatCapacity,width:67">硬盘</th>
              <th data-options="field:'bandwidth',formatter:formatFlow,width:57">带宽</th>
              <th data-options="field:'outerIp',formatter:formatIp,width:127">IP</th>
<!--               <th data-options="field:'c4',width:55">备案</th> -->
              <th data-options="field:'status',formatter:formatStatus,width:82">状态</th>
            </tr>
          </thead>
          <!-- <tbody>
            <tr>
            <td>&nbsp;</td>
            <td>广州</td>
              <td>Host_name</td>
              <td>4核</td>
              <td>2G</td>
              <td>2048G</td>
              <td>9M</td>
              <td>172.172.172.172</td>
              <td><strong class="orange">未备案</strong></td>
              <td>运行中</td>
            </tr>
            <tr>
            <td>&nbsp;</td>
            <td>广州</td>
              <td>Host_name</td>
              <td>4核</td>
              <td>2G</td>
              <td>2048G</td>
              <td>9M</td>
              <td>172.172.172.172</td>
              <td><strong class="gray">审核中</strong></td>
              <td><strong class="gray">已关机</strong></td>
            </tr>
            <tr>
            <td>&nbsp;</td>
            <td>广州</td>
              <td>Host_name</td>
              <td>4核</td>
              <td>2G</td>
              <td>2048G</td>
              <td>9M</td>
              <td>172.172.172.172</td>
              <td>已备案</td>
              <td><strong class="gray">已停用</strong></td>
            </tr>
            <tr>
            <td>&nbsp;</td>
            <td>广州</td>
              <td>Host_name</td>
              <td>4核</td>
              <td>2G</td>
              <td>2048G</td>
              <td>9M</td>
              <td>172.172.172.172</td>
              <td>已备案</td>
              <td><strong class="gray">创建中</strong></td>
            </tr>
            <tr>
            <td>&nbsp;</td>
            <td>广州</td>
              <td>Host_name</td>
              <td>4核</td>
              <td>2G</td>
              <td>2048G</td>
              <td>9M</td>
              <td>172.172.172.172</td>
              <td>已备案</td>
              <td><strong class="orange">CPU告警</strong></td>
            </tr>
            <tr>
            <td>&nbsp;</td>
            <td>广州</td>
              <td>Host_name</td>
              <td>4核</td>
              <td>2G</td>
              <td>2048G</td>
              <td>9M</td>
              <td>172.172.172.172</td>
              <td>已备案</td>
              <td><strong class="red">故障</strong></td>
            </tr>
            <tr>
            <td>&nbsp;</td>
            <td>广州</td>
              <td>Host_name</td>
              <td>4核</td>
              <td>2G</td>
              <td>2048G</td>
              <td>9M</td>
              <td>172.172.172.172</td>
              <td><strong class="orange">未备案</strong></td>
              <td>运行中</td>
            </tr>
            <tr>
            <td>&nbsp;</td>
            <td>广州</td>
              <td>Host_name</td>
              <td>4核</td>
              <td>2G</td>
              <td>2048G</td>
              <td>9M</td>
              <td>172.172.172.172</td>
              <td><strong class="orange">未备案</strong></td>
              <td>运行中</td>
            </tr>
            <tr>
            <td>&nbsp;</td>
            <td>广州</td>
              <td>Host_name</td>
              <td>4核</td>
              <td>2G</td>
              <td>2048G</td>
              <td>9M</td>
              <td>172.172.172.172</td>
              <td><strong class="orange">未备案</strong></td>
              <td>运行中</td>
            </tr>
            <tr>
            <td>&nbsp;</td>
            <td>广州</td>
              <td>Host_name</td>
              <td>4核</td>
              <td>2G</td>
              <td>2048G</td>
              <td>9M</td>
              <td>172.172.172.172</td>
              <td><strong class="orange">未备案</strong></td>
              <td>运行中</td>
            </tr>
          </tbody> -->
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
