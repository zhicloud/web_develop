<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.login.LoginInfo"%>
<%@page import="com.zhicloud.op.app.helper.LoginHelper"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<% 
	String URL = (String)request.getAttribute("url");
	Integer userType = AppConstant.SYS_USER_TYPE_TERMINAL_USER;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta charset="UTF-8" />
	    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		
		<title>忘记密码 - 云端在线</title>
        <link rel="stylesheet" type="text/css" href="css/style.css" />
        <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/style.css" />
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/default/easyui.css">
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/js/easyui/themes/icon.css"> 
		<style type="text/css">
		body input.input {
			width: 200px;
			height: 20px;
		}
		div.logo_text {
			color:#3E8433;
			text-align:center;
		}
		#login_table tr td {
			padding: 3px;
		}
		</style>
	</head>

	<body>
		<div class="page">
		  <div class="pagebox">
		    <div class="header">
		      <div class="headerbox">
		        <div class="headerlogo l"><a href="<%=request.getContextPath() %>/"><img src="<%=request.getContextPath()%>/images/logo_green_light_145_29.gif" width="145" height="29" alt="云端在线" /></a></div>
		        <div class="headerregister r"><a href="<%=request.getContextPath()%>/public/user/register.jsp">注册</a></div>
		        <div class="headerlogin r"><a href="<%=request.getContextPath()%>/user.do">登录</a></div>
		      </div>
		    </div>
		    <div class="nav">
				<div class="navbox">
					<div class="navbutton l">
						<a href="<%=request.getContextPath()%>/">首页</a>
					</div>
					<div class="navsplitter l">&nbsp;</div> 
					 <div class="navbutton l"  ><a href="#" id="pbtn">产品<img src="<%=request.getContextPath()%>/images/button_navlist.gif" width="12" height="12" /></a></div>
		                  <div class="navsplitter l">&nbsp;</div>
					<div class="navbutton l"  >
						<a href="<%=request.getContextPath()%>/user/buy.do">定制云主机</a>
					</div> 
					<div class="navsplitter l">&nbsp;</div>
		            <div class="navbutton l"><a href="<%=request.getContextPath()%>/public/downloadPage.do">相关下载</a></div>
					<div class="navcontrol r">
						<a href="<%=request.getContextPath()%>/user.do"><span>我的云端</span></a>
						<input id="url" type="hidden" value="<%=request.getContextPath()%>"/>
					</div>
					<div id="productmenu"  >  
				<a href="<%=request.getContextPath()%>/vpcserver.do">专属云</a>
			 
		        <a href="<%=request.getContextPath()%>/user/cloudsever.do">云主机</a>
		       <a href="<%=request.getContextPath()%>/user/cloudstorage.do">云存储</a> 
		       <a href="<%=request.getContextPath()%>/user/database.do">云数据库</a>
		       <a href="<%=request.getContextPath()%>/user/banancing.do">负载均衡</a>
		       <a href="<%=request.getContextPath()%>/user/sdn.do">SDN</a>
		       <a href="<%=request.getContextPath()%>/user/recovery.do">云容灾</a>
		      </div>
					
				</div> 
			</div>
		    <div class="main">
		      <div class="container l">
		        <div class="titlebar">
		          <div class="tab tabactive">忘记密码</div>
		        </div>
		        <div class="content">
		          <form id="big_form" style="width:100%; height:100%; display:table-cell; vertical-align:middle;">
		          <input type="hidden" id="type" name="type" value="4" />
		            <span>邮&nbsp;&nbsp;&nbsp;箱</span>
		            <input class="textfield" id="account" name="account" type="text" onblur="checkAccount()"/>
		            <span class="tip" id="tip-account">&nbsp;</span> 
		            <span>验证码</span>
		            <input class="shortfield" id="verification_code" name="verification_code" type="text" />
		            <img id="verification_code_img" src="<%=request.getContextPath()%>/public/verificationCode/new.do?userType=<%=userType%>" width="180" height="30" alt="验证码" class="code" /><span class="tip"><a id="change_verification_code_link" href="#"><img src="<%=request.getContextPath()%>/images/button_refresh_orange_24_24.gif" width="24" height="24" alt="换一个" />换一个</a></span>
		            <input type="button" id="login_btn" class="inputbutton" value="确定"/>
		          </form>
		          <div class="clear">&nbsp;</div>
		        </div>
		      </div>
		      <div class="sidebar r">
		        <div class="sidebarimagelogin">&nbsp;</div>
		        <div class="sidebarcenter">还没有云端在线账户？<br />
		          <a href="<%=request.getContextPath()%>/register.do">立即注册<img class="pointer" src="<%=request.getContextPath()%>/images/pointer_blue_5_10.gif" width="5" height="9" alt="现在注册" /></a></div>
		      </div>
		      <div class="clear">&nbsp;</div>
		    </div>
		  </div>
		  <div class="pagefooter">
		    <div class="footer">
		      <div class="footerbox">
		        <div class="footercopyright">Copyright &copy; 2014 <a href="http://www.zhicloud.com" target="_blank">致云科技有限公司</a>, All rights reserved.　　蜀ICP备14004217号-1</div>
		      </div>
		    </div>
		  </div>
		</div>
	</body>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.ext.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.util.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/jquery.easyui.min.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/easyui/locale/easyui-lang-zh_CN.js"></script>
	<script type="text/javascript" src="<%=request.getContextPath()%>/js/jquery.cookie.js"></script>
	<script type="text/javascript">
	
	function checkAccount() {
		var account = new String($("#account").val()).trim();
		if(account==null || account==""){
			$("#tip-account").html("<img src='<%=request.getContextPath()%>/images/button_caution_red_24_24.gif' width='24' height='24' alt='注意'/>不能为空");
			return false;
		}
		else{
			$("#tip-account").html("");
			return true;
			}
		}
	
	
	window.name = "selfWin";
// 	window.location.reload();
	
	var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
	ajax.async = false;
	
	function login()
	{
		var formData = $.formToBean(big_form);
		ajax.remoteCall("bean://terminalUserService:optionChangePassword",
			[formData],
			function(reply)
			{
				if( reply.status=="exception" )
				{
					top.$.messager.alert("提示", reply.exceptionMessage, "warning");
				} 
				else if( reply.result.status=="success" )
				{
					// 登录成功
					$("<div class=\"datagrid-mask\"></div>").css({
						display:"block",
						width:"100%",
						height:"100%"
					}).appendTo("body"); 
					$("<div class=\"datagrid-mask-msg\"></div>").html("正在跳转页面。。。").appendTo("body").css({
						display:"block",
						left:($(document.body).outerWidth(true) - 190) / 2,
						top:($(window).height() - 45) / 2
					});
					// 跳转页面
					window.setTimeout(function(){
						if("<%=URL%>"!="null"){
							top.location.href ="<%=URL%>";	
						}else{
// 							 window.location.reload();
							top.location.href = "<%=request.getContextPath()%>/public/user/forget_password_option.jsp";							
						}
					}, 500);
				}
			else
			{
// 				alert(reply.result.message);
				top.$.messager.alert("提示", reply.result.message, "warning");
<%-- 				$("#tip-account").html("<img src='<%=request.getContextPath()%>/images/button_caution_red_24_24.gif' width='24' height='24' alt='注意'/>用户名不能为空！"); --%>
				$("#change_verification_code_link").click();
				$("#verification_code").val("");
			}
			}
		);
	}
	
	$(function(){
		$("#account").val("");
		$("#verification_code").val("");
		// 换一个验证码
		$("#change_verification_code_link").click(function(){
			$("#verification_code_img").attr("src", "<%=request.getContextPath()%>/public/verificationCode/new.do?userType=<%=userType%>&ts="+Math.random());
		});
		// 回车
		$("#account,#verification_code").keypress(function(evt){
			if( evt.keyCode==13 ){
				login();
			}
		});
		// 登录
		$("#login_btn").click(function(){
			if(checkAccount()){
			login();
			}
		});
		// 注册
		$("#register_btn").click(function(){ 
 			window.location.href = "<%=request.getContextPath()%>/public/user/register.jsp"; 
			 
		});
	});
	
	$(document).ready(function(){
		$('body').mousemove(function(e) {  
			var xx=e.pageX;
			var yy=e.pageY; 
			var movement=-(xx/$(window).width()-0.5);
			$(".scloud1").css("margin-left",(movement*200)+180+"px");
		$(".scloud2").css("margin-left",(movement*120)-700+"px");
		$(".scloud3").css("margin-left",(movement*20)-200+"px");
	});
		setInterval("parallax()",20);
		var b3;
		var b4;
		var s3;
		var s4;
		$("#pbtn").mouseenter(function(){
			b3=setTimeout("$('#productmenu').fadeIn(300)",300);
			s3 && clearTimeout(s3);
			s4 && clearTimeout(s4);
		});
		$("#pbtn").mouseleave(function(){
			s3=setTimeout("$('#productmenu').fadeOut(300)",300);
			b3 && clearTimeout(b3);
			b4 && clearTimeout(b4);
		});
		$("#productmenu").mouseenter(function(){
			b4=setTimeout("$('#productmenu').fadeIn(300)",300);
			s3 && clearTimeout(s3);
			s4 && clearTimeout(s4);
		});
		$("#productmenu").mouseleave(function(){
			s4=setTimeout("$('#productmenu').fadeOut(300)",300);
			b3 && clearTimeout(b3);
			b4 && clearTimeout(b4);
		});
	});
	function parallax(){
		$(".banner").html(movement);
		var movement=$(document).scrollTop()/($(document).height()-$(window).height())-0.5;
		$(".scloud1").css("margin-top",(movement*200)+"px");
		$(".scloud2").css("margin-top",(movement*120)+"px");
		$(".scloud3").css("margin-top",(movement*20)+"px");
	}
	</script>
</html>
