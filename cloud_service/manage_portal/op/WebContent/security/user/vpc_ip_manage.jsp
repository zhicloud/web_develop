<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.exception.ErrorCode"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.app.propeties.AppProperties"%>
<%@page import="com.zhicloud.op.vo.VpcOuterIpVO"%>
<%@page import="com.zhicloud.op.common.util.DateUtil"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Map"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	List<VpcOuterIpVO> vpcOuterIpList  = (List<VpcOuterIpVO>)request.getAttribute("ipList");
	String vpcId = (String)request.getAttribute("vpcId");
	LoginInfo loginInfo = LoginHelper.getLoginInfo(request, AppConstant.SYS_USER_TYPE_TERMINAL_USER);
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
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/bootstrap/easyui.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/javascript/themes/icon.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/src/common/css/global.css" media="all"/>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/src/user/css/mycloud.css" media="all"/>

<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/jquery.ext.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/javascript/common.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/src/common/js/unslider.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/src/common/js/goup.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/src/common/js/plugin.js"></script>
<script type="text/javascript" src="<%=request.getContextPath() %>/src/common/js/common.js"></script>
<script type="text/javascript">
window.name = "selfWin";

var a = '<%= request.getContextPath() %>';
var ajax = new RemoteCallUtil(a+"/bean/call.do?userType=4");
ajax.async = false;
var name = "<%=loginInfo.getAccount()%>";
var vpc_id = "<%=vpcId%>";
$(document).ready(function() { 
	initstep(1);
});

function getLoginInfo(name, message, userId) {
	slideright();
	inituser(name, message);
	window.location.reload();
};
function addIp(){
	var $ipCount = $("#add_ip_count").val();
	if($ipCount==null || $ipCount==""){
		top.$.messager.alert("提示", "增加的数量不能为空", "info", function(){
		});
		return;
	}
	if($ipCount==0){
		top.$.messager.alert("提示", "增加的数量不能为0", "info", function(){
		});
		return;
	}
	ajax.remoteCall("bean://vpcService:addIpCount",
		[ $ipCount,vpc_id ], 
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
function deleteIps(){
	var ips = [];
	var ipValues = [];
	var allLength = $("#ip_manage_form :checkbox").length;
	$("input:checked").each(function(){
		ips.push($(this).val());
		ipValues.push($(this).attr("ipValue"));
	});
	if(allLength==ips.length){
		top.$.messager.alert("提示", "请至少保留一个IP", "info", function(){
		});
		return;
	}
	if(ips.length==0){
		top.$.messager.alert("提示", "请选择需要删除的IP", "info", function(){
		});
		return;
	}
	top.$.messager.confirm("确认", "删除IP时其关联的端口配置也会被删除，确定要继续吗？", function (r) {
		if(r){
			ajax.remoteCall("bean://vpcService:deleteIps",
					[ vpc_id,ips,ipValues ], 
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
		<div class="pageleft">
			<jsp:include page="/src/common/tpl/u_header.jsp"></jsp:include>
			<div class="main">
			      <div class="wrap"><jsp:include page="/src/common/tpl/u_mcslider.jsp"></jsp:include></div>
			  
				<div class="titlebar" style="width: 980px; padding: 15px 0 5px 0;">
					<div class="blocks l">
						<div class="l" style="padding-right:15px; ">
							<a href="<%=request.getContextPath() %>/bean/page.do?userType=4&bean=vpcService&method=managePage" onclick="self.location=document.referrer;"><img src="<%=request.getContextPath()%>/image/button_back.png" width="22" height="30" alt="返回" /></a>
						</div>
						<div class="r">
							<a href="javascript:void(0);" class="bluebutton r"
							style="width: 80px;" onclick="deleteIps();">删除</a>
						</div>
					</div>
					<div class="blocks r">
						<a href="javascript:void(0);" class="bluebutton r"
							style="width: 80px;" onclick="addIp();">添加</a>
						<div class="r" style="padding: 0 10px 0 0">
							<input class="textbox" type="text" name="ipCount" id="add_ip_count"style="width: 58px; height: 28px" onkeyup="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}" onafterpaste="if(this.value.length==1){this.value=this.value.replace(/[^1-9]/g,'')}else{this.value=this.value.replace(/\D/g,'')}"></input>个
						</div>
						<div class="r" style="padding: 0 10px 0 0;">增加IP数：</div>
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
								<td style="width: 231px"><div class="datagrid-cell">IP</div></td>
								<td style="width: 231px"><div class="datagrid-cell">创建时间</div></td>
							</tr>
							<%
							for(VpcOuterIpVO vpcOuterIp : vpcOuterIpList){
							%>
							<tr>
								<td style="width: 28px"><div class="datagrid-cell">
								<input type="checkbox" value="<%=vpcOuterIp.getId()%>" ipValue="<%=vpcOuterIp.getIp() %>" />
								</div></td>
								<td style="width: 231px"><div class="datagrid-cell"><%=vpcOuterIp.getIp()%></div></td>
								<td style="width: 231px"><div class="datagrid-cell"><%=DateUtil.formatDateString(vpcOuterIp.getCreateTime(), "yyyyMMddHHmmssSSS", "yyyy-MM-dd HH:mm:ss")%></div></td>
							</tr>

							<% 
							}
              	   			%>
						</table>
					</div>
					<div style="width: 980px; margin: 0 auto 0 auto;">

						<div class="buttonbar" style="border-top: solid 1px #b2b2b2;width:980px;">
							<a class="graybutton r" href="javascript:void(0);" onclick="window.history.back()">返回</a>
						</div>
					</div>
				</form>
			</div>
			<div class="clear"></div>
					<jsp:include page="/src/common/tpl/u_footer.jsp"></jsp:include>

		</div>
		<div class="pageright">
			<iframe id="loginiframe" src="<%=request.getContextPath()%>/public/user/login.jsp"frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
			<iframe id="regiframe" src="<%=request.getContextPath()%>/public/user/register.jsp" frameborder="0" hspace="0" vspace="0" scrolling="no"></iframe>
		</div>
	</div>
	<script type="text/javascript"> 
$(document).ready(function(){
	navHighlight("umc","umec");  
});
 
</script>
</body>
</html>
