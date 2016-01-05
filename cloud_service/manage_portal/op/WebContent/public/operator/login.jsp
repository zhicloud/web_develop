<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%
	Integer userType = AppConstant.SYS_USER_TYPE_OPERATOR;
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta http-equiv="X-UA-Compatible" content="IE=9;IE=8;IE=7;" />
	<title>运营商 - 登录</title>
	<link rel="shortcut icon" href="<%=request.getContextPath()%>/newoperator/common/img/favicon.ico" type="image/x-icon" /> 
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/common.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/operlogin.css">
	<script type="text/javascript"> 
	var $buoop = {vs:{i:8,f:20,o:15,s:5.1}}; 
	$buoop.ol = window.onload; 
	window.onload=function(){ 
		try {if ($buoop.ol) $buoop.ol();}catch (e) {} 
		var e = document.createElement("script"); 
		e.setAttribute("type", "text/javascript"); 
		e.setAttribute("src", "//browser-update.org/update.js"); 
		document.body.appendChild(e); 
	}
	</script>
</head>

<body>
<div class="container">
	<div class="login-wraper">
		<div class="lg-box">
			<div class="sec-left-cont"><img src="<%=request.getContextPath()%>/images/operator/oper_login_scenery.png" alt="scenery"/></div>
			<div class="sec-right-cont">
				<div class="l-rc-tit">
					<img class="rct-logo" src="<%=request.getContextPath()%>/images/operator/oper_login_logo.png" alt="logo" />
					<h3>致云公有云运营平台v2.0</h3>
				</div>
				<div class="l-rc-form">
					<form id="big_form">
						<input type="hidden" id="type" name="type" value="2" />
						<ul>
							<li><input type="text" name="account" id="account" class="ipt-sty" placeholder="用户名" autocomplete="on"/></li>
							<li><input type="password" name="password" id="password" class="ipt-sty" placeholder="密码" autocomplete="off"/></li>
							<li>
								<input type="text" name="verification_code" id="verification_code" class="ipt-sty wid80" autocomplete="off" placeholder="验证码" maxlength="4" />
								<img id="verification_code_img" class="verf-code" src="<%=request.getContextPath()%>/public/verificationCode/new.do?userType=<%=userType%>" alt="vercode" />
								<a id="change_verification_code_link" class="change-code" href="javascript:;">换一个</a>
							</li>
							<li class="f-pt25"><a id="login_btn" class="btn-login" href="javascript:;">立即登陆</a></li>
						</ul>
					</form>
				</div>
			</div>
		</div>
	</div>
</div>
<!-- JavaScript_start -->
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.ext.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.util.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/jquery.easyui.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/locale/easyui-lang-zh_CN.js"></script>
<script type="text/javascript">
var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
ajax.async = false;

$(function(){
	// 换一个验证码
	$("#change_verification_code_link").click(function(){
		$("#verification_code_img").attr("src", "<%=request.getContextPath()%>/public/verificationCode/new.do?userType=<%=userType%>&ts="+Math.random());
	});
	// 回车
	$("#account, #password, #verification_code").keypress(function(evt){
		if( evt.keyCode==13 ){
			login();
		}
	});
	// 登录
	$("#login_btn").click(function(){
		login();
	});
});

function login(){
	var formData = $.formToBean(big_form);
	ajax.remoteCall("bean://sysUserService:login",
		[formData],
		function(reply){
			if( reply.status=="exception" ){
				top.$.messager.alert('警告',reply.exceptionMessage,'warning');
			}else if( reply.result.status=="success" ){
				// 登录成功
				$("<div class=\"datagrid-mask\"></div>").css({
					display:"block",
					width:"100%",
					height:"100%"
				}).appendTo("body"); 
				$("<div class=\"datagrid-mask-msg\"></div>").html("登录成功，正在跳转页面。。。").appendTo("body").css({
					display:"block",
					left:($(document.body).outerWidth(true) - 190) / 2,
					top:($(window).height() - 45) / 2
				});
				// 跳转页面
				window.setTimeout(function(){
					top.location.href = "<%=request.getContextPath()%>/operator.do";
				}, 500);
			}else{
				$("#password").val("");
				$("#change_verification_code_link").click();
				$("#verification_code").val("");
				top.$.messager.alert('警告',reply.result.message,'warning');
			}
		}
	);
}

function reshVerification(){ 
	$("#change_verification_code_link").click(); 	 
}
var t2 = window.setInterval(reshVerification,1740000);
</script>
<!-- JavaScript_end -->
</body>
</html>