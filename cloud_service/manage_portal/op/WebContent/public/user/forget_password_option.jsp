<%@page import="com.zhicloud.op.vo.TerminalUserVO"%>
<%@page import="com.zhicloud.op.service.constant.AppConstant"%>
<%@page import="com.zhicloud.op.common.util.StringUtil"%>
<%@page pageEncoding="utf-8" contentType="text/html; charset=utf-8"%>
<% 
	TerminalUserVO terminalUserVO = (TerminalUserVO)request.getSession().getAttribute("myterminalUserVO");
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
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/css/sequence-wizard.css" />
		<script type="text/javascript" src="js/jquery.js"></script>
		<script src="js/jquery.sequence-min.js"></script>
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
		        <a href="<%=request.getContextPath()%>/user/cloudsever.do">云主机</a>
		       <a href="<%=request.getContextPath()%>/user/cloudstorage.do">云存储</a> 
		       <a href="<%=request.getContextPath()%>/user/database.do">云数据库</a>
		       <a href="<%=request.getContextPath()%>/user/banancing.do">负载均衡</a>
		       <a href="<%=request.getContextPath()%>/user/sdn.do">SDN</a>
		       <a href="<%=request.getContextPath()%>/user/recovery.do">云容灾</a>
		      </div>
					
				</div> 
			</div>
	  <%if(terminalUserVO ==null){
  			response.sendRedirect(request.getContextPath()+"/public/user/forget_password.jsp"); 
	   }else{%>
	  <div class="main">
      <div class="fullcontainer">
        <div class="fulltitlebar">
        <div class="tab tabactive">忘记密码</div>
        </div>
        <div class="fullcontent">
		<div class="fullcenter"><img src="<%=request.getContextPath()%>/images/image_forget_password.gif" width="210" height="111" alt="请通过以下方式找回" /><br /><br /><br />您正在为账户<b class="orange"><%=terminalUserVO.getAccount()%></b>重置登录密码，以下是您的找回方式：</div>
        </div>
        <div class="content fulltable">
             <div class="labeliso" style="padding:18px 18px;height:50px;" onclick="window.location.href = '<%=request.getContextPath()%>/public/user/reset_password_phone.jsp'">
             <div class="labeltitle">通过手机验证码</div>
             <div class="labeldesc">如果您的<b><%=terminalUserVO.getPhone()%></b>手机还在正常使用，请选择此方式找回密码</div>
             </div >
             <%if(terminalUserVO.getEmail()==null||terminalUserVO.getEmail().isEmpty()){%>
			 <div class="labeliso" style="display:none" >
             </div>
             <%}else{%>
              <div class="labeliso" style="padding:18px 18px;height:50px;" onclick="window.location.href = '<%=request.getContextPath()%>/public/user/reset_password_email.jsp'">
              <div class="labeltitle">通过电子邮箱</div>
              <div class="labeldesc">如果您的<b><%=terminalUserVO.getEmail()%></b>邮箱还在正常使用，请选择此方式找回密码
              </div>
             <%}%>
             <br /><br /><br />
        </div>
      </div>
    </div>
  </div>
  <%}%>
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
	
window.name = "selfWin";
	
	var ajax = new RemoteCallUtil("<%=request.getContextPath()%>/bean/call.do?userType=<%=userType%>");
	ajax.async = false;
	
	
	$(function(){
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
			login()
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
